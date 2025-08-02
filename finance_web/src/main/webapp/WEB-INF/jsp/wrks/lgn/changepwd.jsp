<%--
/**
 * -----------------------------------------------------------
 * @Class Name : changepwd.jsp
 * @Description : 비밀번호 변경
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT"/></title>
    
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<style>
#wrapperPop {
	width: auto;
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
	
		$(".btnOk").bind("click",function(){
			if($("#pwd1").val().trim() != $("#pwd2").val().trim())
			{
				alert("신규 비밀번호가 동일하지 않습니다.");
				return false;
			}
			var url = "<c:url value='/wrks/lgn/changepwd.json'/>";
			var params = "pwdOld=" + $("#pwdOld").val();
				params += "&pwdNew=" + $("#pwd1").val().trim();
	
			$.ajaxEx(null,
			{
				  url : url
				, datatype: "json"
				, data: params
				, success:function(data){
					alert(data.msg);
					window.close();
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
<div id="wrapperPop">
	<!-- container -->
	<div class="container">
		<div class="topArea"></div>
		<!-- content -->
		<div class="content">
			<div class="titArea">
				<h3 class="tit">비밀번호 변경</h3>
			</div>
			<div class="tableType2 mb30">
				<table>
				<caption>비밀번호 변경</caption>
				<colgroup>
					<col style="width: 150px;" />
					<col style="width: *" />
					<col style="width: 150px;" />
					<col style="width: *" />
				</colgroup>
				<tbody>
				<tr><th>기존 비밀번호</th>
					<td colspan="3"><input type="password" name="" id="pwdOld" class="txtType"></td>
				</tr>
				<tr><th>신규 비밀번호</th>
					<td colspan="3"><input type="password" name="" id="pwd1" class="txtType"></td>
				</tr>
				<tr><th>비밀번호 확인</th>
					<td colspan="3"><input type="password" name="" id="pwd2" class="txtType"></td>
				</tr>
				</tbody>
				</table>
			</div>
			<div class="btnWrap btnR">
				<a href="javascript:;" class="btn btnDt btnOk">확인</a>
			</div>
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>

