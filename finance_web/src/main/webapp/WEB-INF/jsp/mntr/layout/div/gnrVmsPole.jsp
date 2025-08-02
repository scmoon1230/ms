<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp" id="div-gnrVmsPole">
	<div class="panel-heading">
		<h3 class="panel-title">폴감시목록</h3>
	</div>
	<div class="panel-body" style="max-height: 600px;">
		<table class="table table-bordered table-ucp">
			<colgroup>
				<col style="width: *;">
				<col style="width: 50px;">
			</colgroup>
			<thead>
				<tr>
					<th class="text-center">카메라명</th>
					<th class="text-center">영상</th>
				</tr>
			</thead>
			<tbody id="divCameraList"></tbody>
		</table>
	</div>
</div>
<script>
	$(function() {
		oGnrVmsPole = new gnrVmsPole();
		oGnrVmsPole.init();
	});

	function gnrVmsPole() {
		this.init = function() {
			if (oVmsService.playlists != null) {
				var oPlayLists = oVmsService.playlists;
				var $tbody = $('#divCameraList');
				var $tr = $('<tr/>');
				$.each(oPlayLists, function(i, v) {
					var $trC = $tr.clone();
					var sFcltLblNm = v.fcltLblNm;
					var sFcltId = v.fcltId;

					$trC.append($('<td/>', {
						'text': sFcltLblNm
					}));
					$trC.append($('<td/>', {
						'html': '<span><button class="btn btn-primary btn-xs btn-ucp" onclick="oVmsCommon.openVmsPlayer(\'' + v.fcltId + '\')">보기</button></span>',
						'class': 'text-center'
					}));
					$trC.appendTo($tbody);
				});
			}
			else {
				console.log('========== oVmsService.playlists is null ==========');
			}
		};
	}
</script>