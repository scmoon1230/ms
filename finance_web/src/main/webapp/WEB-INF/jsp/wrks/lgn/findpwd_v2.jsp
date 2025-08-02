<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>

<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><prprts:value key="HD_TIT" /></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/wrks/lgn/v2/swiper/swiper.min.css">
    <!-- ui_base.css 는 디자인 수정 불가 -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/wrks/lgn/v2/ui_base.css">
    <!-- ui_base.dev.css 는 를 통해 수정할 것 -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/wrks/lgn/v2/ui_base.dev.css">
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
            <strong><span><c:out value="${systemInfo.dstrtNm}"/> <c:out value="${systemInfo.dstrtDscrt}"/></span></strong>
            <strong><span>비밀번호 찾기</span></strong>
        </h1>
        <ul>
            <li class="userId"><label for="userId">ID</label><input type="text" class="login" id="userId" placeholder="아이디를 입력하세요"></li>
            <li class="userNm"><label for="userNm">NM</label><input type="text" class="login" id="userNm" placeholder="이름을 입력하세요"></li>
            <li class="moblNo"><label for="moblNo">MO</label><input type="tel" class="login" id="moblNo" placeholder="모바일번호를 입력하세요"></li>
        </ul>
        <input type="submit" class="btnLogin" value="비밀번호 찾기">
        <a href="${pageContext.request.contextPath}/wrks/lgn/login.do">로그인 화면</a>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/jquery/jquery-3.7.0.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery/jquery-migrate-3.4.0.min.js"></script>
<script src="${pageContext.request.contextPath}/js/wrks/lgn/v2/swiper/swiper.min.js"></script>
<script src="${pageContext.request.contextPath}/js/wrks/lgn/v2/findpwd_v2.js"></script>
<script>
	let oApp;
	$(function () {
		oApp = new App($('body').data());
		oApp.init();
	});
</script>
</body>
</html>
