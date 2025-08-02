<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=10">
<title>${data.fcltLblNm}(${data.fcltId})</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mntr/resetCss.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/js/v3/bootstrap/css/bootstrap.min.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mntr/vmsPlayer.css' />">
<style type="text/css">
div#view-vms {
	bottom: 0px;
}
</style>
<script src="<c:url value='/js/jquery/jquery.js' />"></script>
<script src="<c:url value='/js/mntr/cmm.js' />"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp"%>
	<c:choose>
		<c:when test="${not empty url}">
			<div id="view-vms"></div>
			<form>
				<input type="hidden" id="playlist-url" name="playlist-url" value="${url}" />
			</form>
			<script src="<c:url value='/js/bootstrap/v3/js/bootstrap.min.js' />"></script>
			<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-dialog.min.js' />"></script>
			<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-toggle.min.js' />"></script>
			<script>
				$(function() {
					oVlcPlayer = new vlcPlayer();
					oVlcPlayer.init();
				});

				function vlcPlayer() {
					this.init = function() {
						try {
							var $viewVms = $('#view-vms');
							if ($viewVms.length) {
								setTimeout(function() {
									var $activeX = $('<object/>', {
										'id' : 'vlcPlayer',
										'name' : 'vlcPlayer',
										'classid' : 'clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921',
										'codebase' : 'http://download.videolan.org/pub/videolan/vlc/last/win32/axvlc.cab',
									});
									$viewVms.html($activeX);
								}, 1000);

								setTimeout(function() {
									var $vlcPlayer = $('#vlcPlayer');
									$vlcPlayer.css({
										'width' : '100%',
										'height' : '100%'
									});
									var eVlcPlayer = $vlcPlayer.get(0);
									var sPlaylistUrl = $('#playlist-url').val();
									var nVlcId = eVlcPlayer.playlist.add(sPlaylistUrl, '', ':network-caching=10000');
									eVlcPlayer.playlist.playItem(nVlcId);
								}, 1000);
							}
						}
						catch (e) {
							$('#view-vms').html('<h3 style="color: #f00;"> VLC 설치가 필요합니다.</h3>');
						}
					};
				}
			</script>
		</c:when>
		<c:otherwise>
			<h3 class="text-center" style="color: #f00;">영상 접속 정보가 없습니다.</h3>
		</c:otherwise>
	</c:choose>
</body>
</html>
