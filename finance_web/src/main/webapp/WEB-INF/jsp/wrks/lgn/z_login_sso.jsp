<%--
/**
 * 로그인을 관리
 * @author		마곡지구 김정원
 * @version		1.00	2014-01-25
 * @since		JDK 1.7.0_45(x64)
 * @revision
 * /

/**
 * ----------------------------------------------------------
 * @Class Name : login.jsp
 * @Description : 로그인
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 *
 * ----------------------------------------------------------
 * */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="sysCd" 			value="${LoginVO.sysId}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>영상반출 로그인</title>
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript" src="<c:url value='/'/>magicsso/js/deployJava.js" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/'/>magicsso/js/MagicLine.js" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/'/>magicsso/js/MagicPass.js" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/'/>magicsso/js/Login.js" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/'/>magicsso/js/Monitor.js" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/'/>magicsso/js/Certificate.js" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/'/>magicsso/js/cookie.js" charset="UTF-8"></script>

<!-- SSO Script (ë) -->
<script type="text/javascript">

	function getKey(keyStroke)
	{
		if (window.event.keyCode == 13) {
			ssoLogin();
		}
	}

	document.onkeypress = getKey;


</script>

</head>
<body>

<%
	if(null != session.getAttribute("SSO_ID")) {

%>
		<%=session.getAttribute("SSO_ID")%>
		<script>
			window.location.replace("/wrks/lgn/redirect.do");
		</script>
<%
	}
%>


<div id="login">
    <div class="loginCont">
        <div class="tit">
            <h1>
            <!--
            	<c:choose>
           			<c:when test="${not empty sysCd}">
           				<img src="<c:url value='/'/>images/${sysCd}/title_login.png">
           			</c:when>
           			<c:otherwise>
						<img src="<c:url value='/'/>images/title_login.png">
           			</c:otherwise>
           		</c:choose>
           		 -->
            </h1>
        </div>
        <div class="loginBox">
			<form name="loginForm" id="loginForm" method="post">
            <fieldset>
                <legend>로그인</legend>
                <label for="loginId">아이디</label>
                <input type="text" name="loginId" id="loginId" class="login_id"  placeholder="ID" style="ime-mode:inactive" >
                <label for="loginPw">비밀번호</label>
                <input type="password" name="loginPwd" id="loginPwd" class="login_pw" placeholder="Password">
                <input type="image" src="<c:url value='/'/>images/btn_login.gif" id = "login_img" class="login_img" alt="로그인" title="로그인">
                <a href="<c:url value='/'/>wrks/lgn/findpwd.do"><img src="<c:url value='/'/>images/btn_pw.gif" alt="비밀번호 찾기"></a>
            </fieldset>
            </form>
        </div>
    </div>
</div>
<div id="ssoPluginPage">
	<div id="pluginArea"></div>
	<div id="xsignArea"></div>
</div>
<!-- SSO Script 시작 -->
<div id="MagicLineArea"></div>
<script type="text/javascript" defer="defer">


	function ssoLogin() {

		//alert($("#loginId").val() + "/" + $("#loginPwd").val());
		login.start();
	}



	var returnUrl = "/wrks/lgn/redirect.do";
	var login = new Login("loginId", "loginPwd", returnUrl);


	if(MagicPass.option.useCertificate) {
		var certificate = new Certificate(returnUrl);
	}



 	MagicPass.addEvent("login_img", "click", function(){
		MagicPass.job("Execute Login", function(){
			ssoLogin();
		});
	});

 	MagicPass.job("로그인ID 포커스 이동", function() {
		var loginId = document.getElementById("loginId");
		if(loginId && !loginId.value) {
			loginId.select();
		}
	});

	var certificate;
	if(MagicPass.option.useCertificate && !MagicPass.option.useApplet) {
		certificate = new Certificate(returnUrl);
	}

	MagicPass.addEvent("certificateButton", "click", function(){
		if(MagicPass.option.useApplet) {
			certificate = new Certificate(returnUrl);
		}
		certificate.start();
	});


</script>
<!-- SSO Script 끝 -->

</body>
</html>
