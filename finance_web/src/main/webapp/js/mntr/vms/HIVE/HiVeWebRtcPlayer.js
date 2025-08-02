//
// Nexisoft HiVeWebRtcPlayer
// We refer to the UnrealWebRTCPlayer. (http://umediaserver.net/umediaserver/demos.html)
//
function HiVeWebRtcPlayer(
    element_id,
    server_ip,
    server_port,
    video_name,
    use_single_port,
    use_playback,
    stime,
    etime,
    speed)
{
    "use strict";

    if (use_single_port === undefined)
        use_single_port = true;
    if (use_playback === undefined)
        use_playback = false;
    if (stime === undefined)
        stime = 0;
    if (etime === undefined)
        etime = 0;
    if (speed === undefined)
        speed = 1;

    var pc = null;
    var ws = null;
    var state = "ready";
    var latest_stop_time = 0;
    var is_connected = false;
    var video_module = document.getElementById(element_id);
    var show_controls = video_module.controls;

    this.Play = function Play() {
        var cur_time = new Date().getTime();

        if ((state === "ready") && (cur_time - latest_stop_time > 1000)) {
            video_module.srcObject = null;
            state = "start";
            is_connected = false;
            video_module.setAttribute('style', 'background: black url(loader.gif) center no-repeat;');

            show_controls = video_module.controls;
            video_module.controls = false;

            if (speed > 1)
                video_module.playbackRate = 1.0*speed;

            DoSignaling();
        }

        if (cur_time - latest_stop_time <= 1000)
            video_module.pause();
    };

    this.Stop = function Stop() {
        show_controls = video_module.controls;
        state = "stop";
        Terminate();
    };

    this.DoPtzControl = function DoPtzControl(cmd, action, direction, speed, num, continuous)
    {
        if (!is_connected)
            return false;

        if (cmd === null || action === null || video_name === null)
            return false;

        var jsonObj = {
            type: cmd,
            action: action,
            videoname: video_name,
            direction: direction,
            speed: speed,
            num: num,
            continuous: continuous
        };

        ws.onmessage = function (evt) {
            var recv = evt.data;
            var data = JSON.parse(recv);

            if (data.result !== 200)
                console.error("Request Failed:", data.message);
        };

        ws.send(JSON.stringify(jsonObj));
    };

    this.GetPtzSpeed = function GetPtzSpeed()
    {
        return new Promise(function(resolve, reject) {
            if (!is_connected)
                return reject();

            var jsonObj = {
                type: "get_ptz_speed"
            };

            ws.onmessage = function (evt) {
                var recv = evt.data;
                var data = JSON.parse(recv);

                if (data.result !== 200)
                    reject("Request Failed: ", data.message);
                else
                    resolve(data.speed);

            };

            ws.onclse = function (evt) {
                reject("websocket closed: " + evt.code);
                Terminate();
            };

            ws.onerror = function (evt) {
                reject("websocket error");
            };

            ws.send(JSON.stringify(jsonObj));
        });
    };


    video_module.onplay = this.Play;
    video_module.onpause = this.Stop;
    video_module.onloadedmetadata = this.OnLoadedMetadata

    function gotRemoteStream(e) {
        video_module.srcObject = e.streams[0];
        video_module.setAttribute('style', 'background-color:black');
        video_module.setAttribute('style', 'object-fit:fill');

        // video_module이 pause 상태로 player를 실행한 경우에는 어떻게 해야 할까?
        // 첫 frame을 받으면 무조건 실행 한다.
        video_module.play();
        state = "play";

        if (show_controls)
            video_module.controls = true;
    }

    function Terminate() {
        console.log("----- Terminate()");

        latest_stop_time = new Date().getTime();

        video_module.removeAttribute('src');
        if (state !== "stop")
            video_module.setAttribute('style', 'background:black url(error.png) center no-repeat;');
        else
            video_module.setAttribute('style', 'background-color:black');
        video_module.load();

        state = "ready";

        if (show_controls)
            video_module.controls = true;

        if (pc !== null) {
            pc.onconnectionstatechange = null;
            pc.close();
            pc = null;
        }

        if (ws !== null) {
            ws.onerror = null;
            ws.close();
            ws = null;
        }

        is_connected = false;

        video_module.pause();
    }

    function DoSignaling() {
        var offer_url = "ws://" + server_ip + ":" + server_port + "/hive_api/webrtc_stream/" + video_name;

        try
        {
            ws = new WebSocket(offer_url);
        }
        catch (error)
        {
            Terminate();
            console.error("Error creating websocket: " + error);
        }

        console.log("Call Url : " + offer_url);

        ws.onmessage = function (evt) {
            var response = evt.data;
            var json_obj = JSON.parse(response);

            is_connected = true;

            if (json_obj.result === 200) {
                if (json_obj.type === "join") {
                    var servers = null;
                    var offerOptions = { offerToReceiveAudio: 0, offerToReceiveVideo: 1 };

                    pc = new RTCPeerConnection(servers);
                    pc.onicecandidate = onIceCandidate;
                    pc.onconnectionstatechange = onConnStateChange;
                    pc.ontrack = gotRemoteStream;

                    pc.createOffer(offerOptions).then(
                        onCreateOfferSuccess,
                        function () {
                            Terminate();
                            console.error("createOffer Error :" + error.toString());
                        }
                    );
                }
                else if (json_obj.type === "answer") {
                    var server_sdp = json_obj.sdp;
                    var server_endpoint = json_obj.endpoint;

                    server_endpoint.candidate = EnsureValidCandidate(server_endpoint.candidate);

                    pc.setRemoteDescription(new RTCSessionDescription({ type: "answer", sdp: server_sdp }));
                    var candidate = new RTCIceCandidate({ sdpMLineIndex: server_endpoint.sdpMLineIndex, candidate: server_endpoint.candidate });
                    pc.addIceCandidate(candidate);
                }
            }
            else {
                Terminate();
                console.error(response);
            }
        };

        ws.onerror = function (evt) {
            if (!is_connected) {
                Terminate();
                console.error("Connecting error:", evt);
            }
        };

        ws.onclose = function (evt) {
            Terminate();
            console.log("websocket closed");
        }
    }

    function onCreateOfferSuccess(desc) {
        //Fix for some browsers and/or adapter incorrect behavior
        desc.sdp = desc.sdp.replace("a=sendrecv", "a=recvonly");
        desc.sdp = desc.sdp.replace("a=sendrecv", "a=recvonly");

        //var json_msg = desc.toJSON();
        var json_msg = {};
        json_msg["type"] = desc.type;
        json_msg["sdp"] = desc.sdp;
        json_msg["mediatype"] = use_playback ? "playback" : "live";
        json_msg["mediaport"] = use_single_port ? "single" : "random";
        json_msg["start_time"] = String(stime);
        json_msg["end_time"] = String(etime);
        json_msg["play_speed"] = speed;

        //Signal the SDP to the server
        ws.send(JSON.stringify(json_msg));

        pc.setLocalDescription(desc).then(
            function() { console.log("setLocalDescription Succeess"); },
            function() {
                Terminate();
                console.error("setLocalDescription Error :" + error.toString());
            }
        );
    }

    function EnsureValidCandidate(candidate) {
        if ((candidate.search(server_ip) !== -1) || (server_ip === "127.0.0.1") || !ValidateIPaddress(server_ip)) {
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

    function ValidateIPaddress(ipaddr) {
        if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipaddr)) {
            return true;
        }

        return false;
    }

    function onIceCandidate(event) {
        //Do nothing! We only need one endpoint from server; browser is going to connect to it
    }

    function onConnStateChange(event) {
        if (pc.connectionState === "failed") {
            state = "error";
            Terminate();
            console.error("Connection failed - playback stop");
        }
    }
}

