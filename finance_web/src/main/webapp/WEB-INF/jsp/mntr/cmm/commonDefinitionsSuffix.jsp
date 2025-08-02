<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap.min.js' />"></script>
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-dialog.min.js' />"></script>
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-toggle.min.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/moment.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/ko.js' />"></script>

<c:set var="vmsWebRtcYn" scope="request"><prprts:value key="VMS_WEBRTC_YN"/></c:set>
<c:choose>
	<%--<c:when test="${configProp['vms.webRtcYn'] eq 'Y' and configProp['vms.software'] eq 'HIVE'}">--%>
	<c:when test="${vmsWebRtcYn eq 'Y' and vmsSoftware eq 'HIVE'}">
		<script src="<c:url value='/js/mntr/vms/vmsCommon.js' />"></script>
		<script src="<c:url value='/js/mntr/vms/${vmsSoftware}/webrtcadapter.js' />"></script>
		<!-- <script src="<c:url value='/js/mntr/vms/${vmsSoftware}/HiVeWebRtcPlayer_DEV.js' />"></script> -->
		<script src="<c:url value='/js/mntr/vms/${vmsSoftware}/HiVeWebRtcPlayerV2_DEV.js' />"></script>
		<script src="<c:url value='/js/mntr/vms/${vmsSoftware}/${vmsSoftware}_WebRTC.js' />"></script>
	</c:when>
	<c:otherwise>
		<script src="<c:url value='/js/mntr/vms/vmsCommon.js' />"></script>
		<c:if test="${not empty common.pageOption and common.pageOption ne 'NOVMS'}">
			<script src="<c:url value='/js/mntr/vms/${vmsSoftware}/${vmsSoftware}.js' />"></script>
			<c:import url="/WEB-INF/jsp/mntr/vms/${vmsSoftware}/vmsEvent.jsp"></c:import>
		</c:if>
	</c:otherwise>
</c:choose>
