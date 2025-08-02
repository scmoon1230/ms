<%--
/**
 * 로그인을 관리
 * @author		대전도안 김정원
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
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>영상반출 로그인</title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
	
		/* 기존 로그인 처리 시작 */
	    $(".login_img").bind("click",function(){
		    var url = "<c:url value='/'/>wrks/lgn/login.json";
		    var params = "userId=" + $("#loginId").val();
		        params += "&pwd=" + $("#loginPw").val();
	
		    $.ajaxEx(null, {
			    url : url,
			    datatype: "json",
			    data: params,
		        success:function(data){
					if(data.ret == 1) {
						location.href = "<c:url value='/'/>" + data.redirect;
					}
					else {
			        	alert(data.msg);
					}
		        },
		        error:function(e){
		            //alert(e.responseText);
		        	alert("접속이 지연되고 있습니다. 잠시후 시도해주십시오");
		        }
		    });
	    });
		/* 기존 로그인 처리 끝 */
	    document.getElementById('loginId').focus();
	});
</script>
</head>
<body class="login_con">
<div id="login">
    <div class="loginCont">
        <div class="tit">
            <h1><div class="loginTitle" ></div></h1>
        </div>
        <div class="loginBox">
            <fieldset>
                <legend>로그인</legend>
                <label for="loginId">아이디</label>
                <input type="text" name="loginId" id="loginId" class="login_id"  placeholder="ID" style="ime-mode:inactive" >
                <label for="loginPw">비밀번호</label>
                <input type="password" name="loginPw" id="loginPw" class="login_pw" placeholder="Password">
                <input type="image" id="login_img" class="login_img" alt=" " title=" ">
                <a class="find_pw" href="<c:url value='/'/>wrks/lgn/findpwd.do"></a>
            </fieldset>
        </div>
    </div>
</div>
</body>
</html>
