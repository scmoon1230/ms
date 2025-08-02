<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=10">
<title>${data.fcltLblNm}(${data.fcltId})</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mntr/resetCss.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap/v3/css/bootstrap.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap-dialog.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap-toggle.min.css' />">
<style type="text/css">
div#view-vms {
	bottom: 0px;
}
</style>
<script src="<c:url value='/js/mntr/jquery/jquery.js' />"></script>
<script src="<c:url value='/js/mntr/cmm.js' />"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp"%>
	<c:choose>
		<c:when test="${not empty url}">
			<form>
				<input type="hidden" id="download-url" name="download-url" value="${url}" />
			</form>
			<script src="<c:url value='/js/bootstrap/v3/js/bootstrap.min.js' />"></script>
			<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-dialog.min.js' />"></script>
			<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-toggle.min.js' />"></script>
			<script>
				$(function() {
					oDrm = new drm();
					oDrm.init();
				});

				function drm() {
					this.init = function() {
						var sDownloadUrl = $('#download-url').val();
						console.log(sDownloadUrl);
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
