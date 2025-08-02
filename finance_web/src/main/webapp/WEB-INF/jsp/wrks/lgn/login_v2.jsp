<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>재정부</title>
	<link rel="icon" href="<c:out value="${pageContext.request.contextPath}" />/images/favicon.ico" type="image/x-icon">
	<link rel="stylesheet" type="text/css" href="<c:out value='${pageContext.request.contextPath}' />/js/wrks/lgn/v2/swiper/swiper.min.css">
	<!-- ui_base.css 는 디자인 수정 불가 -->
	<link rel="stylesheet" type="text/css" href="<c:out value='${pageContext.request.contextPath}' />/css/wrks/lgn/v2/ui_base.css">
	<!-- ui_base.dev.css 는 를 통해 수정할 것 -->
	<link rel="stylesheet" type="text/css" href="<c:out value='${pageContext.request.contextPath}' />/css/wrks/lgn/v2/ui_base.dev.css">
</head>
<body data-context-root="<c:out value='${pageContext.request.contextPath}' />" data-dstrt-cd="${systemInfo.dstrtCd}" data-dstrt-nm="${systemInfo.dstrtNm}" data-dstrt-dscrt="${systemInfo.dstrtDscrt}">
<div id="wrap" class="login">
	<div id="loginSlider">
		<div class="swiper-container">
			<ul class="swiper-wrapper">
				<li class="swiper-slide" style="background-image:url('<c:out value='${pageContext.request.contextPath}' />/images/wrks/lgn/v2/bg_login_default.png');"></li>
			</ul>
		</div>
	</div>
	<div id="loginBox">
		<h1>
			<img id="img-ci" alt="${systemInfo.dstrtNm}">
			<!-- 
			<strong><span>명성교회 재정부</span></strong>
			-->
		</h1>
		<ul>
			<li class="id"><label for="id">ID</label><input type="text" class="login" id="id" placeholder="아이디를 입력하세요"></li>
			<li class="pw"><label for="pw">PW</label><input type="password" class="login" id="pw" placeholder="비밀번호를 입력하세요"></li>
		</ul>
		<input type="submit" class="btnLogin" value="LOGIN">
		<c:set var="pwdFindYn" scope="request"><prprts:value key="PWD_FIND_YN"/></c:set>
		<c:if test="${pwdFindYn eq 'YYY'}">
			<a href="${pageContext.request.contextPath}/wrks/lgn/findpwd.do">비밀번호 찾기</a>
		</c:if>
		<c:if test="${pwdFindYn eq 'YYY'}">
			<a href="javascript:download();">Chrome 다운로드</a>
		</c:if>
		<c:set var="userApproveYn" scope="request"><prprts:value key="USER_APPROVE_YN"/></c:set>
		<c:if test="${userApproveYn eq 'YYY'}">
			 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			<a href="javascript:apply();">사용자 등록 신청</a>
		</c:if>
		
	</div>
</div>
<script src="<c:out value='${pageContext.request.contextPath}' />/js/jquery/jquery-3.1.0.min.js"></script>
<script src="<c:out value='${pageContext.request.contextPath}' />/js/jquery/jquery-migrate-3.1.0.min.js"></script>
<script src="<c:out value='${pageContext.request.contextPath}' />/js/wrks/lgn/v2/swiper/swiper.min.js"></script>
<script src="<c:out value='${pageContext.request.contextPath}' />/js/wrks/lgn/v2/login_v2.js"></script>
<script>
	let oApp;
	$(function () {
		oApp = new App();
		oApp.init($('body').data());
	});
	
	function apply() {
		let url = "${pageContext.request.contextPath}/wrks/lgn/selfrgsuser.do";		//document.location.href = url;
		let title = "사용자 등록 신청";
		let width = 1000;
		let height = 450;
        window.open(url, title, 'menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no,location=no,width=' + width + ',height=' + height);
	}
	
	function download() {
		let contextRoot = '<c:out value='${pageContext.request.contextPath}' />';
		
		let $formDownload = $('#form-file');
		if ($formDownload.length) {
			$('#form-file').attr('action',contextRoot + '/tvo/downloadChromeFile.do');
		} else {
			$formDownload = $('<form/>', {
				'id': 'form-file',
				'method': 'POST',
				'action': contextRoot + '/tvo/downloadChromeFile.do',
			});

			$formDownload.appendTo(document.body);
		}
		$formDownload.submit();
	}
</script>
</body>
</html>
