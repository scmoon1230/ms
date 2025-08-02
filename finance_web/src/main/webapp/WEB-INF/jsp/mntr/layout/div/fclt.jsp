<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">시설물기본정보</h3>
	</div>
	<div class="panel-body">
		<table class="table table-bordered divInfo">
			<colgroup>
				<col span="1" style="width: 30%;">
				<col span="1" style="width: 70%;">
			</colgroup>
			<tbody>
				<tr><th>시설물아이디</th>		<td>${divFclt.fcltId}</td>									</tr>
				<tr><th>시설물명</th>			<td>${divFclt.fcltLblNm}</td>								</tr>
				<tr><th>유형(용도)</th>		<td>${divFclt.fcltUsedTyNm}(${divFclt.fcltUsedTyCd})</td>	</tr>
				<tr><th>위치</th>				<td>${divFclt.roadAdresNm}<br/>(${divFclt.lotnoAdresNm})</td>	</tr>
				<tr><th>영상저장여부</th>
					<td><c:if test="${divFclt.recordingYn eq 'Y'}">영상저장함</c:if>
						<c:if test="${divFclt.recordingYn eq 'N'}">영상저장안함</c:if>
					</td>
				</tr>
				<tr><th>반출제외여부</th>
					<td><c:if test="${divFclt.tvoTrgtYn eq 'Y'}">반출대상임</c:if>
						<c:if test="${divFclt.tvoTrgtYn eq 'N'}">반출대상아님</c:if>
					</td>
				</tr>
				<tr><th>상태</th>				<td>${divFclt.fcltSttusNm}</td>								</tr>
				<%--
				<c:if test="${divFclt.fcltKndCd eq 'CTV'}">
				<tr><th>관할지구대</th><td>${divFclt.plcPtrDivNm}</td>		</tr>
				</c:if>
				 --%>
				<tr><th>영상</th>
					<td>
						<%-- <button class="btn btn-default btn-xs" onclick="oDivFclt.detail();">상세정보</button> --%>
						<c:if test="${divFclt.fcltUsedTyCd eq 'VMSTRF' and fn:startsWith(divFclt.dvcSeeCctvIp, 'rtsp://')}">
							<button class="btn btn-default btn-xs" onclick="oVmsCommon.openRtspPlayer('${divFclt.fcltId}');">보기</button>
						</c:if>
						<c:if test="${divFclt.viewerTyCd eq 'VMS'}">
							<button class="btn btn-default btn-xs" onclick="oVmsCommon.openVmsPlayer('${divFclt.fcltId}');">보기</button>
						</c:if>
						<c:if test="${divFclt.viewerTyCd eq 'RTSP'}">
							<button class="btn btn-default btn-xs" onclick="oVmsCommon.openRtspPlayer('${divFclt.fcltId}');">보기</button>
						</c:if>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<form id="hiddenFrm" name="hiddenFrm" method="POST">
	<input type="hidden" id="fcltId" name="fcltId" value="${divFclt.fcltId}" />
</form>
<script>
	$(function(){
		oDivFclt = new divFclt();
	});

	function divFclt() {
		this.detail = function() {
			document.hiddenFrm.action = contextRoot + '/mntr/fcltDetail.do';
			document.hiddenFrm.submit();
		};
	}
</script>