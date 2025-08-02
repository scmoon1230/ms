<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Player</title>

	<link href="<c:url value='/js/bootstrap/v3/css/bootstrap.min.css'/>" rel="stylesheet">
	<style>
		html, body {height: 100%;margin: 0;}

		.container {
			width: 100%;
			heigth: 100%;
			background: #111;
			padding: 0;
			box-shadow: 0px 0px 5px 5px rgba(255,255,255,0.2);
			display: flex;
			justify-content: center;
			align-items: center;
			position: inherit;
		}

		video {object-fit: fill;width: 100%;height: 100%;background-color: black;}

		.progress {
			flex: 10;
			position: relative;
			display: flex;
			flex-basis: 100%;
			height: 20px;
			transition: height 0.3s;
			background: #111;
			cursor: ew-resize;
			margin-top: 0px;
			margin-bottom: 0px;
			border-radius: 0px;
		}

		.progress__filled {background: #ffc600;flex: 0;flex-basis: 0%;}
		#handle {display:flex;position: inherit;bottom: 0px;height:50px;margin: 0px;padding: 8px;}
		#attach {border: 1px solid transparent;border-radius: 4px;border-color: #ccc;display:flex;padding: 6px 12px;}
	</style>
	<script src="<c:url value='/js/jquery/jquery.js'/>"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp"%>
	<div class="container">
		<video>
			<source src="${videoUrl }">
		</video>
	</div>

	<div class="progress">
		<div class="progress__filled"></div>
	</div>

	<div class="form-group form-inline" id="handle">
		<div class="form-inline" style="border: 1px solid transparent;border-radius: 4px;border-color: #ccc;padding: 6px 12px;">
			<span style="width: 200px;text-align: center;" id="currentTime">${startTime}</span>
		</div>&nbsp;
		<div class="form-inline" style="padding: 6px 12px;">
			<label class="control-label text-muted">재생속도</label>
		</div>
		<select class="form-control" title="재생속도">
			<option value="0.2">0.2</option><option value="0.5">0.5</option>
			<option value="1"> &nbsp;&nbsp; 1</option><option value="2" selected="selected"> &nbsp;&nbsp; 2</option>
			<option value="4"> &nbsp;&nbsp; 4</option><option value="8"> &nbsp;&nbsp; 8</option><option value="16"> &nbsp;16</option>
		</select>&nbsp;
		<button type="button" id="play" class="btn btn-default" aria-label="Play" title="재생" style="display:block;">
			<span class="glyphicon glyphicon-play" aria-hidden="true"></span>
		</button>
		<button type="button" id="pause" class="btn btn-default" aria-label="Pause" title="중지" style="display:none;">
			<span class="glyphicon glyphicon-pause" aria-hidden="true"></span>
		</button>&nbsp;
		<button type="button" id="stop" class="btn btn-default" aria-label="Stop" title="정지">
			<span class="glyphicon glyphicon-stop" aria-hidden="true"></span>
		</button>&nbsp;
		<button type="button" id="full" class="btn btn-default" aria-label="Full" title="전체화면" style="display:block;">
			<span class="glyphicon glyphicon-resize-full" aria-hidden="true"></span>
		</button>
		<button type="button" id="small" class="btn btn-default" aria-label="Small" title="화면복구" style="display:none;">
			<span class="glyphicon glyphicon-resize-small" aria-hidden="true"></span>
		</button>&nbsp;
		<div class="form-inline" id="attach" style="display:none;">
			<span>첨부재생테스트 :</span>&nbsp;<input id="file" type="file" accept="video/mp4,video/mkv, video/x-m4v,video/*"><!-- 첨부파일 재생 테스트용 -->
		</div>
	</div>

	<script>
		//console.clear();

		const oContainer = document.querySelector('.container');
		const oVideo = document.querySelector('video');
		const oFile = document.getElementById("file");

		const oProgress = document.querySelector('.progress');
		const oProgressBar = document.querySelector('.progress__filled');

		const oCurrentTime = document.querySelector('#currentTime');
		const oSelect = document.querySelector('select');
		const oPlay = document.querySelector('#play');
		const oPause = document.querySelector('#pause');
		const oStop = document.querySelector('#stop');
		const oFull = document.querySelector('#full');
		const oSmall = document.querySelector('#small');

		let mousedown = false;
		let touchstart = false;

		oVideo.play();

		oFile.addEventListener("change", function(){
			const file = oFile.files[0];
			const videourl = URL.createObjectURL(file);
			oVideo.setAttribute("src", videourl);

			// 초기화
			for (let i=0; i<oSelect.options.length; i++){
				if(oSelect.options[i].value == '1'){
					oSelect.options[i].selected = true;
				}
			}
			oProgressBar.style.flexBasis = '0%';

			oVideo.play();
		})

		function changeSpeed(pageY, offsetTop, offsetHeight) {
			const min = 0.2;
			const max = 8;
			const position = pageY - offsetTop;
			const percent = position / offsetHeight;
			const height = Math.floor(percent * 1000) / 10 + '%';

			let playbackRate = percent * (max - min) + min;
			if (playbackRate < 0.3) playbackRate = 0.2;
			if (0.3 <= playbackRate && playbackRate < 0.7) playbackRate = 0.5;
			if (0.7 <= playbackRate && playbackRate < 1.5) playbackRate = 1.0;
			if (1.5 <= playbackRate && playbackRate < 3.0) playbackRate = 2.0;
			if (3.0 <= playbackRate && playbackRate < 5.0) playbackRate = 4.0;
			if (5.0 <= playbackRate && playbackRate < 7.0) playbackRate = 6.0;
			if (7.0 <= playbackRate) playbackRate = 8.0;

			oVideo.playbackRate = playbackRate;
			oSelect.value = playbackRate;
		}

		oSelect.addEventListener('change', function(e) {
			let playbackRate = oSelect.value;						//alert(playbackRate);
			oVideo.playbackRate = playbackRate;
		});



		function updateButton() { // playing이라는 property는 없다. 'paused'만 있음.
			if(oVideo.paused) {
				oPlay.style.display="block";
				oPause.style.display="none";
			} else {
				oPlay.style.display="none";
				oPause.style.display="block";
			}
		}
		oVideo.addEventListener('play', updateButton);
		oVideo.addEventListener('pause', updateButton);


		function togglePlay() { // playing이라는 property는 없다. 'paused'만 있음.
			if(oVideo.paused) {		oVideo.play();
			} else {				oVideo.pause();
			}
		}
		oPlay.addEventListener('click', togglePlay);
		oPause.addEventListener('click', togglePlay);
		oStop.addEventListener('click', function(e) {
			oVideo.pause();
			oVideo.currentTime = 0;
			oProgressBar.style.flexBasis = '0%';
		});


		window.addEventListener(`resize`, function() {
			let width = window.innerWidth;
			let height = window.innerHeight-70;
			oContainer.style='width:'+width+'px; height:'+height+'px';
			setTimeout(function () {
				$('video').css({
					'width': $('.container').width(),
					'height': $('.container').height()
				});
			}, 500);
		});
		function fullScreenWindow() {
			window.moveTo(0, 0);
			window.resizeTo(screen.availWidth, screen.availHeight);
		}
		function defaultScreenWindow() {
			window.resizeTo(oConfigure.popWidth, oConfigure.popHeight);
			let x = (screen.availWidth - oConfigure.popWidth) / 2;
			let y = (screen.availHeight - oConfigure.popHeight) / 2;
			window.moveBy(x, y);
		}
		function toggleScreen() {
			let $btn = $(this);
			if ('Full' == $btn.attr('aria-label')) {
				oFull.style.display="none";
				oSmall.style.display="block";
				fullScreenWindow();
			} else {
				oSmall.style.display="none";
				oFull.style.display="block";
				defaultScreenWindow();
			}
		}
		oFull.addEventListener('click', toggleScreen);
		oSmall.addEventListener('click', toggleScreen);


		function getCurrentTime() {
			let curTime = new Date('${startTime}');
			curTime.setSeconds(curTime.getSeconds() + Math.floor(oVideo.currentTime));

			var year = curTime.getFullYear();
			var month = ('0' + (curTime.getMonth() + 1)).slice(-2);
			var day = ('0' + curTime.getDate()).slice(-2);
			var hours = ('0' + curTime.getHours()).slice(-2);
			var minutes = ('0' + curTime.getMinutes()).slice(-2);
			var seconds = ('0' + curTime.getSeconds()).slice(-2);

			return year + '-' + month + '-' + day+" "+hours + ':' + minutes + ':' + seconds;
		}
		function handleProgress() {
			const percent = (oVideo.currentTime / oVideo.duration) * 100;
			oProgressBar.style.flexBasis = percent+'%';
			oCurrentTime.innerHTML = getCurrentTime();
		}
		oVideo.addEventListener('timeupdate', handleProgress);

		function scrub(e) {
			const scrubTime = (e.offsetX / oProgress.offsetWidth) * oVideo.duration;	//console.log(scrubTime);
			oVideo.currentTime = scrubTime;
		}
		oProgress.addEventListener('click', scrub);
		oProgress.addEventListener('mousemove', e => {  mousedown && scrub(e);	});
		oProgress.addEventListener('mousedown', () => mousedown = true);
		oProgress.addEventListener('mouseup', () => mousedown = false);


		// 초기 화면 사이즈 설정
		let width = window.innerWidth;
		let height = window.innerHeight-70;
		oContainer.style='width:'+width+'px; height:'+height+'px';
		setTimeout(function () {
			$('video').css({
				'width': $('.container').width(),
				'height': $('.container').height()
			});
		}, 500);

		 $('video').bind('contextmenu', function() { return false; });	//contextmenu 나오면 false처리

	</script>
</body>
</html>
