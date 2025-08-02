<%--
/**
 * -----------------------------------------------------------
 * @Class Name : findpwd.jsp
 * @Description : 패스워드변경
 * @Version : 1.0
 * Copyright (c) 2016 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * -----------------------------------------------------------
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------------------
 * 2016. 11.08.    seungJun    최초작성
 * -----------------------------------------------------------
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<c:set var="sysCd" value="${LoginVO.sysId}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
	
		$(".btnOk").bind("click",function()
		{
			if($("#loginId").val().trim() == "") {
				alert("사용자 아이디를 입력하세요.");
				return
			}
			if($("#loginNm").val().trim() == "") {
				alert("사용자 이름을 입력하세요.");
				return;
			}
			if($("#moblNo").val().trim() == "") {
				alert("모바일번호를 입력하세요.");
				return;
			}
			var url = "<c:url value='/wrks/lgn/findpwd.json'/>";
			var params = "userId=" + encodeURIComponent($("#loginId").val());
				params += "&userNm=" + encodeURIComponent($("#loginNm").val());
				params += "&moblNo=" + encodeURIComponent($("#moblNo").val());
			$.ajaxEx(null,
			{
				  url : url
				, datatype: "json"
				, data: params
				, success:function(data){
					alert(data.msg);
				}
				, error:function(e){
					alert(e.responseText);
				}
			});
		});
	});
</script>
</head>
<body>
	<div id="loginid">
		<div class="loginCont">
			<div class="loginBoxid">
				<fieldset>
					<legend>로그인</legend>
					<ul>
						<div>사용자 아이디</div>
						<li>
							<label for=""></label>
							<input type="text" name="" id="loginId" class="login_id" placeholder="">
						</li>
						<div>사용자 이름</div>
						<li>
							<label for="loginId">사용자 이름</label>
							<input type="text" name="" id="loginNm" class="login_id" placeholder="">
						</li>
						<div>모바일 번호</div>
						<li>
							<label for="loginPw">모바일번호</label>
							<input type="text" name="" id="moblNo" class="login_pwf" placeholder="">
						</li>
						<br />
						<div class="btn_area">
							<a href="#" class="btnOk">
								<img src="<c:url value='/'/>images/btn_pw.gif" alt="비밀번호 찾기">
							</a>
							<a href="<c:url value='/'/>wrks/lgn/login.do" class="btn" style="height: 43px; color: #aaa; font-weight: bold; padding-top: 12px;">취소</a>
						</div>
					</ul>
				</fieldset>
			</div>
		</div>
	</div>
</body>
</html>
