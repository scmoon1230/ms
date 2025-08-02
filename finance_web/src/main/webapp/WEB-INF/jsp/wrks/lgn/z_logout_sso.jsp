<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html>
<html lang="ko">
<head>
	<title>Logout</title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="<c:url value='/'/>magicsso/js/deployJava.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<c:url value='/'/>magicsso/js/MagicLine.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<c:url value='/'/>magicsso/js/MagicPass.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<c:url value='/'/>magicsso/js/Login.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<c:url value='/'/>magicsso/js/Monitor.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<c:url value='/'/>magicsso/js/Certificate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<c:url value='/'/>magicsso/js/cookie.js" charset="UTF-8"></script>

	
<script type="text/javascript">
	
	MagicPass.siteLogout("/wrks/lgn/login.do",null);

	/* 
	function logout(idx){
		 
		switch(idx){
		case 1 : MagicPass.siteLogout("/wrks/lgn/login.do", "login"); break; 
		case 2 : location.href=MagicPass.loginUrl; break;
		}

	}
	 */	
</script>


</head>
<!-- <body onload="logout(1)"> -->
<body>
</body>
</html>