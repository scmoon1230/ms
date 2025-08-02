<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<style>
#divCameraList td {
	vertical-align: middle;
	padding: 3px;
}
</style>
<div class="panel panel-default panel-ucp" id="div-evtCastnet">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<table class="table table-bordered table-ucp">
			<colgroup>
				<col style="width: *;">
				<col style="width: 50px;">
				<col style="width: 50px;">
			</colgroup>
			<thead>
				<tr>
					<th class="text-center">카메라명</th>
					<th class="text-center">프리셋</th>
					<th class="text-center">영상</th>
				</tr>
			</thead>
			<tbody id="divCameraList"></tbody>
		</table>
	</div>
</div>
<script>
	function selectCctv(pointX, pointY, area) {
		removePreviousFeatureselected();
		var trArea = $('#div-evtCastnet tr[id^=area]');
		trArea.removeClass('active');
		$('#area-' + area).addClass('active');

		var point = convertByWGS84(pointX, pointY);
		previousFeatureselected = featureselected(point, 'fclt', '', false, true, true);

		if (typeof oVmsService != 'undefined') {
			oVmsService.setViewIndex(parseInt(area));
		}
		else if ((oVms.software == 'INNODEPGYC' || oVms.software == 'INNODEP') && isInnodepInit) {
			innodep.SetOcxSelect(parseInt(area));
		}
	}
</script>