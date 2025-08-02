<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <tiles:insertAttribute name="prefix" />
	<link rel="icon" href="<c:out value="${pageContext.request.contextPath}" />/images/favicon.ico" type="image/x-icon">
    
    <script src="<c:url value='/js/mntr/div.js' />"></script>
    <title><prprts:value key="HD_TIT" /></title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
	<div id="wrapper">
	    <header>
	        <tiles:insertAttribute name="header"/>
	    </header>
	    <section id="body">
	        <tiles:insertAttribute name="map-ol5"/>
	    </section>
	    <aside id="left">
	        <tiles:insertAttribute name="left" ignore="true"/>
	    </aside>
	    <div id="toggleLeft"></div>
	    <div id="toggleRight"></div>
	    <aside id="right">
	        <tiles:insertAttribute name="right" ignore="true"/>
	    </aside>
	    <div id="toggleBottom"></div>
	    <section id="bottom">
	        <tiles:insertAttribute name="bottom" ignore="true"/>
	    </section>
	    <footer>
	        <tiles:insertAttribute name="footer"/>
	    </footer>
	</div>
	<tiles:insertAttribute name="suffix"/>
</body>
</html>
