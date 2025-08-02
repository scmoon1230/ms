<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld"%>

<% response.setContentType("text/html"); %>
<%@ page isELIgnored="false"%>

<c:set var="rootPath"				value="${pageContext.request.contextPath}"		scope="request" />

<spring:eval expression="@config" var="configProp" scope="request" />
<%--
<c:set var="exeEnv"					value="${configProp['Globals.exeEnv']}"			scope="request" />
 --%>

<c:set var="sysId"       scope="request"><prprts:value key="G_SYS_ID" /></c:set>
<c:set var="dstrtCd"     scope="request"><prprts:value key="DSTRT_CD" /></c:set>
<c:set var="ucpId"       scope="request"><prprts:value key="UCP_ID" /></c:set>
<c:set var="vmsSoftware" scope="request"><prprts:value key="VMS_SOFTWARE" /></c:set>
<c:set var="gisEngine"   scope="request"><prprts:value key="GIS_ENGINE" /></c:set>

<c:set var="sysCd" 					value="${LoginVO.sysId}"						scope="request" />
<c:set var="grpId" 					value="${LoginVO.grpId}"						scope="request" />

<%--
<c:set var="evtLcMoveYn" 			value="${configure.evtLcMoveYn}"				scope="request" />
<c:set var="mntr_car_lc_tm" 		value="${configProp['mntr_car_lc_tm']}"			scope="request" />
<c:set var="mntr_car_lc_trace_cnt"	value="${configProp['mntr_car_lc_trace_cnt']}"	scope="request" />
--%>
<%-- VMS --%>
<%--
<c:set var="ftpYn"					value="${configProp['vms.ftpYn']}"				scope="request" />
<c:set var="snapshotYn"				value="${configProp['vms.snapshotYn']}"			scope="request" />
<c:set var="searchYn"				value="${configProp['vms.searchYn']}"			scope="request" />
<c:set var="startPreNum"			value="${configProp['vms.startPreNum']}"		scope="request" />
--%>
<c:set var="ftpYn" scope="request"><prprts:value key='VMS_FTP_YN' /></c:set>

<c:set var="apiKey"					value="${configProp['gis.apiKey']}"				scope="request" />
<c:if test="${!empty ipMapping.gisMp and 'VWORLD' eq gisEngine}">
	<c:set var="apiKey"				value="${ipMapping.gisMp}"						scope="request" />
</c:if>
