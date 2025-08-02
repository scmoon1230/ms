//
// Nexisoft HiVeWebRtcPlayer
// We refer to the UnrealWebRTCPlayer. (http://umediaserver.net/umediaserver/demos.html)
//

"use strict";

const playerState = {
    READY: 0,
    START: 1,
    PLAY: 2,
    STOP: 3,
    ERROR: 4,
};
Object.freeze(playerState);

const videoState = {
    BLACK: 0,
    ERROR: 1,
    LOAD: 2,
}
Object.freeze(videoState);

class HiVeWebRtcPlayer {
    elementId = "";
    serverIp = "";
    serverPort = "";
    videoName = "";
    singlePort = true;
    playback = false;
    startTime = 0;
    endTime = 0;
    speed = 1;
    ssl = false;

    video = null;
    controls = false;
    state = playerState.STOP;
    socket = null;
    peer = null;
    lastStoppedTime = 0;
    streaming = false;
    pausedStream = false;

    constructor(
        elementId,
        serverIp,
        serverPort,
        videoName,
        singlePort,
        playback,
        startTime,
        endTime,
        speed,
        ssl)
    {
        this.elementId = elementId;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.videoName = videoName;
        this.singlePort = singlePort;
        this.playback = playback;
        this.startTime = startTime;
        this.endTime = endTime;
        this.speed = speed;
        this.ssl = ssl;

        this.video = document.getElementById(elementId);
        this.controls = false;    // videoModule.controls
        this.state = playerState.READY;
        this.socket = null;
        this.peer = null;
        this.lastStoppedTime = 0;
        this.streaming = false;

        this.video.onplay = this.Play;
        this.video.onpause = this.Pause;
    }

    //
    // public functions
    //
    Play = () => {
        if (this.streaming) {
            if (!this.pausedStream)
                return;

            this.Resume();
        }
        else {
            // play/stop 을 너무 빠르게 반복(1초 이내) 하면 강제로 pause 시키기 위함
            if (duration(this.lastStoppedTime) <= 1000)
                this.video.pause();
            else if (this.state === playerState.READY) {
                this.streaming = false;
                this.pausedStream = false;
                this.state = playerState.START;
                this.controls = this.video.controls;
                this.video.controls = false;
                clearVideo(this.video, videoState.LOAD);

                if (this.speed > 1)
                    this.video.playbackRate = 1.0 * speed;

                // start WebRTC signaling
                this.connect();
            }
        }
    }

    Pause = () => {
        if (!this.streaming || this.pausedStream)
            return;

        this.pausedStream = true;

        let jsonObj = {
            type: "pause_stream",
        };

        this.socket.send(JSON.stringify(jsonObj));

        this.video.pause();
    }

    Resume = () => {
        if (!this.streaming || !this.pausedStream)
            return;

        this.pausedStream = false;

        let jsonObj = {
            type: "resume_stream",
        };

        this.socket.send(JSON.stringify(jsonObj));

        this.video.play();
    }

    Stop = () => {
        this.controls = this.video.controls;
        this.state = playerState.STOP;
        this.terminate();
    }

    DoPtzControl = (cmd, action, direction, speed, num, continuous) => {
        if (!this.streaming)
            return;

        let jsonObj = {
            type: cmd,
            action: action,
            videoname: this.videoName,
            direction: direction,
            speed: speed,
            num: num,
            continuous: continuous
        };

        this.socket.send(JSON.stringify(jsonObj));
    }

    //
    // private functions
    //
    connect() {
        let protocol = this.ssl ? "wss" : "ws";
        let url = protocol + "://" + this.serverIp + ":" + this.serverPort + "/web_api/webrtc_stream/" + this.videoName;

        try {
            this.socket = new WebSocket(url);
        }
        catch (error) {
            this.state = playerState.ERROR;
            this.terminate();
            console.error("Failed to create websocket: " + error + " url=" + url);
        }

        this.socket.addEventListener("open", this.onWSOpen);
        this.socket.addEventListener("message", this.onWSMessage);
        this.socket.addEventListener("error", this.onWSError);
        this.socket.addEventListener("close", this.onWSClose);
    }

    terminate() {
        if (this.state === playerState.READY)
            return;

        this.streaming = false;

        if (this.peer !== null) {
            this.peer.onconnectionstatechange = null;
            this.peer.close();
            this.peer = null;
        }

        if (this.socket !== null) {
            this.socket.onerror = null;
            this.socket.close();
            this.socket = null;
        }

        let vs = videoState.BLACK;
        vs = this.state === playerState.STOP ? videoState.BLACK : videoState.ERROR;
        clearVideo(this.video, vs);

        this.state = playerState.READY;
        if (this.controls)
            this.video.controls = this.controls;
        this.video.pause();

        this.lastStoppedTime = new Date().getTime();

        console.log("----- Terminate()");
    }

    // event handlers
    onWSOpen = (evt) => {
        // WebRTC signaling
        // client -> server : web socket connection.
        // client <- server : join message with ice.
        // client -> server : offer message
        // client <- server : answer message
        // client <- server : play stream
        // 위와 같이 서버로 부터 answer 를 받은 후에야 streaming 이 시작 됨.
        // 이 때가 this.streaming = true 가 되는 시점 임
        // this.streaming === true 일 때, ptz 등 추가 제어 함수가 동작 한다.
    }

    onWSMessage = (evt) => {
        let jsonObject = JSON.parse(evt.data);
        if (jsonObject.result === 200)
            this.handleMessage(jsonObject);
        else
            this.handleErrorMessage(jsonObject);
    }

    handleMessage(jsonObject) {
        switch (jsonObject.type) {
            case "join":
                {
                    let servers = null;
                    this.peer = new RTCPeerConnection(servers);
                    this.peer.onicecandidate = this.onIceCandidate;
                    this.peer.onconnectionstatechange = this.onConnStateChange;
                    this.peer.ontrack = this.onTrack;

                    let offerOptions = { offerToReceiveAudio: 0, offerToReceiveVideo: 1 };
                    this.peer.createOffer(offerOptions).then(
                        this.onCreateOfferSuccess,
                        () => {
                            this.state = playerState.ERROR;
                            this.terminate();
                            console.error("createOffer Error :" + error.toString());
                        }
                    );
                }
                break;
            case "answer":
                {
                    let sdp = jsonObject.sdp;
                    let ep = jsonObject.endpoint;

                    ep.candidate = ensureValidCandidate(ep.candidate, this.serverIp);

                    // 일부 브라우저에서 RTCSessionDescription 직접 생성하는 경우 type, sdp 속성은 readonly 이므로
                    // type error 발생
                    // pc.setRemoteDescription(new RTCSessionDescription({ type: "answer", sdp: sdp }));
                    // setRemoteDescription 에 description(json) 을 직접 넣는 방식으로 바뀌어야 하는 듯
                    this.peer.setRemoteDescription({ type: "answer", sdp: sdp });

                    let candidate = new RTCIceCandidate({ sdpMLineIndex: ep.sdpMLineIndex, candidate: ep.candidate });
                    this.peer.addIceCandidate(candidate);
                }
                break;
            default:
                {
                    console.info(jsonObject.type + " succeeded.");
                }
                break;
        }
    }

    handleErrorMessage(jsonObject) {
        switch (jsonObject.type) {
            case "join":
            case "answer":
                {
                    this.state = playerState.ERROR;
                    this.terminate();
                    console.error(jsonObject);
                }
                break;
            default:
                {
                    console.error(jsonObject);
                }
                break;
        }
    }

    onWSError = (error) => {
        if (!this.streaming) {
            this.state = playerState.ERROR;
            this.terminate();
        }
    }

    onWSClose = (evt) => {
        this.terminate();
        console.log("websocket closed: ", evt.code);
    }

    //
    // WebRTC signaling event handlers
    //
    onIceCandidate = () => {
        //Do nothing! We only need one endpoint from server; browser is going to connect to it
    }

    onConnStateChange = (event) => {
        if (this.peer.connectionState === "failed") {
            this.state = playerState.ERROR;
            this.terminate();
            console.error("Connection failed - playback stop");
        }
    }

    onTrack = (evt) => {
        this.streaming = true;

        this.video.srcObject = evt.streams[0];
        this.video.setAttribute('style', 'background-color:black');
        this.video.setAttribute('style', 'object-fit:fill');

        // video module이 pause 상태로 player를 실행한 경우에는 어떻게 해야 할까?
        // 첫 frame을 받으면 무조건 실행 한다.
        this.video.play();
        this.state = playerState.PLAY;
        if (this.controls)
            this.video.controls = true;
    }

    onCreateOfferSuccess = (desc) => {
        //Fix for some browsers and/or adapter incorrect behavior
        desc.sdp = desc.sdp.replace("a=sendrecv", "a=recvonly");
        desc.sdp = desc.sdp.replace("a=sendrecv", "a=recvonly");

        //var json_msg = desc.toJSON();
        var jsonMsg = {};
        jsonMsg["type"] = desc.type;
        jsonMsg["sdp"] = desc.sdp;
        jsonMsg["mediatype"] = this.playback ? "playback" : "live";
        jsonMsg["mediaport"] = this.singlePort ? "single" : "random";
        jsonMsg["start_time"] = String(this.startTime);
        jsonMsg["end_time"] = String(this.endTime);
        jsonMsg["play_speed"] = this.speed;

        //Signal the SDP to the server
        this.socket.send(JSON.stringify(jsonMsg));

        this.peer.setLocalDescription(desc).then(
            () => { console.log("setLocalDescription Succeess"); },
            () => {
                this.state = playerState.ERROR;
                this.terminate();
                console.error("setLocalDescription Error :" + error.toString());
            }
        );
    }
}

function duration(time) {
    let cur = new Date().getTime();
    return cur - time;
}

function clearVideo(video, state) {
    video.srcObject = null;
    video.removeAttribute('src');

    switch (state) {
        case videoState.ERROR:
            video.setAttribute('style', 'background:black url(error.png) center no-repeat;');
            break;
        case videoState.LOAD:
            video.setAttribute('style', 'background: black url(loader.gif) center no-repeat;');
            break;
        //case videoState.BLACK:
        default:
            this.video.setAttribute('style', 'background-color:black');
    }

    video.load();
}

function ensureValidCandidate(candidate, serverIp) {
    if ((candidate.search(serverIp) !== -1) || (serverIp === "127.0.0.1") || !validateIPaddress(serverIp)) {
        return candidate;
    }

    //In case the server is behind the NAT router, replace private IP with public IP in the candidate
    var candLines = candidate.split(" ");
    var ipIndex = 4;
    for (var i = 0; i < candLines.length; i++) {
        if (candLines[i] === "typ") {
            ipIndex = i - 2;
            break;
        }
    }

    candLines[ipIndex] = server_ip;
    candidate = candLines.join(" ");
    return candidate;
}

function validateIPaddress(ipaddr) {
    if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipaddr)) {
        return true;
    }

    return false;
}
