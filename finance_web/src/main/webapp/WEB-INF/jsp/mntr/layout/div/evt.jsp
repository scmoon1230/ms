<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp" id="div-evt" data-point-x="${divEvent.pointX}" data-point-y="${divEvent.pointY}">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<table class="table table-bordered divInfo">
			<colgroup>
				<col style="width: 70%;">
				<col style="width: 30%;">
			</colgroup>
			<tbody>
				<tr>
					<td style="padding: 0px 5px; vertical-align: middle;"><span style="color: blue;">${divEvent.evtOcrNo}</span>[<span style="color: red">${divEvent.evtDtl}</span>]<br> ${divEvent.evtOcrYmdHms}&nbsp;${divEvent.evtPlace}</td>
					<td style="padding: 0px 5px; vertical-align: middle; white-space: nowrap;">
						<button class="btn btn-primary btn-xs btn-ucp" onclick="pointMove();">발생위치</button>
						<%--
						<button class="btn btn-primary btn-xs btn-ucp" onclick="stopCastNet();">해제</button>
						--%>
					</td>
				</tr>
				<%--
				<tr>
					<th class="table-ucp">이벤트유형</th>
					<td>${divEvent.evtNm}</td>
				</tr>
			 	<tr>
					<th class="table-ucp">재난내용</th>
					<td>${divEvent.evtDtl}</td>
				</tr>
				<tr>
					<th class="table-ucp">재난위치</th>
					<td>${divEvent.evtPlace}</td>
				</tr>
				<tr>
					<th class="table-ucp">발생시간</th>
					<td>${divEvent.evtOcrYmdHms}</td>
				</tr>
				<tr>
					<th class="table-ucp">처리상태</th>
					<td>${divEvent.evtPrgrsNm}</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: right;">
						<button class="btn" onclick="eventWrksInfo();">상황처리</button>
					</td>
				</tr>
				 --%>
			</tbody>
		</table>
	</div>
</div>
<script>
	/* 현재영상을 보고 있는 이벤트 위치로 이동 */
	function pointMove() {
		var oData = $('#div-evt').data();
		var pX = oData.pointX;
		var pY = oData.pointY;
		if (pX == null || pX == "" || pX == "0") {
			return false;
		}
		if (pX && pY) {
			var point = convertByWGS84(pX, pY);
			var lonLat = new OpenLayers.LonLat(point.x, point.y);
			_map.setCenter(lonLat, _map.getZoom());
		}
	}
</script>
