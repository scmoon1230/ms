<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body" style="padding: 0px; overflow: hidden;">
		<div id="view-rtsp" style="width: 285px; height: 100%;">
			<object id="vlcPlayer" classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921">
				<param name="autostart" value="false" />
				<param name="allowfullscreen" value="true" />
				<param name="controls" value="false" />
			</object>
		</div>
	</div>
</div>
<script>
//	console.log('**** [RTSP] loading ==== ');
	$(function() {
		if (typeof _map != 'undefined') {
			var wgs84 = convertToWGS84(_map.getCenter().lon, _map.getCenter().lat);
			var pointX = wgs84.x;
			var pointY = wgs84.y;

			$.ajax({
				type : 'POST',
				url : contextRoot + '/mntr/nearestCctv.json',
				async : false,
				data : {
					searchFcltViewerType : 'RTSP',
					pointX : pointX,
					pointY : pointY
				},
				success : function(result) {
					$('#vlcPlayer').css('width', '100%');
					$('#vlcPlayer').css('height', '100%');

					// 형식인경우 변경처리 rtsp://30.2.10.30/43/stream1 >>>>  rtsp://30.2.10.30
					// 20180326 space svrConnIp를 rtspUrl로 변경
					var rtspUrl = result.rtspUrl;
					var rtspIp    = "";
					console.log('**** [RTSP] success : x:' + pointX + ' y:' + pointY + ' rtspUrl:' + rtspUrl);
					if (chkValue(rtspUrl)) {
						console.log('**** [RTSP] chkValue : x:' + pointX + ' y:' + pointY + ' rtspUrl:' + rtspUrl);
						rtspIp    = rtspUrl.replace('rtsp://','');
					}

					var svrConnId = result.svrConnId;
					var svrConnPw = result.svrConnPw;

					if (typeof rtspUrl == 'undefined' && rtspUrl == null) {
						console.log('[RTSP] 근처에 교통 CCTV가 없습니다.');
						return false;
					}

					if (!rtspUrl.startsWith('rtsp://')) {
						console.log('[RTSP] 카메라 접속 정보가 잘못되었습니다.');
						return false;
					}
					if (rtspUrl.startsWith('rtsp://') && svrConnId != '' && svrConnPw != '') {
						rtspUrl = rtspUrl.replace('rtsp://', 'rtsp://' + svrConnId + ':' + svrConnPw + '@');
					}

					for (var i = 0; i < oIpMapping.rtspIpAry.length; i++){
						if(rtspUrl.indexOf(oIpMapping.rtspIpAry[i]) > -1) {
							rtspUrl = rtspUrl.replace(oIpMapping.rtspIpAry[i], oIpMapping.rtspIpMpAry[i]);
						}
					}

					try {
						var vlc = document.getElementById('vlcPlayer');
						vlc.playlist.add(rtspUrl);
						vlc.playlist.play();
						point = convertByWGS84(result.pointX, result.pointY);
						previousFeatureselected = featureselected(point, 'label', 'RTSP', false, false, false);
					}
					catch (e) {
						$('#view-rtsp').html('<h4 style="color: #f00;"> VLC 설치가 필요합니다.</h4>');
						$('#view-rtsp').append('<p><a href="' + contextRoot + '/ax/119.pdf"> 1. VLC 설정하기</a></p>');
						$('#view-rtsp').append('<p><a href="' + contextRoot + '/ax/vlc-2.2.2-win32.exe"> 2. VLC 다운로드</a></p>');
					}
				},
				error : function(data, status, err) {

				}
			});
		}
	});
</script>