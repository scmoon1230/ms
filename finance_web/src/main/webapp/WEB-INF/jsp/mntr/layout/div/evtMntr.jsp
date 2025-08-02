<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<style>
#div-evtMntr .panel-body .form-group {
	margin-bottom: 5px;
}

#div-evtMntr .panel-footer {
	padding: 5px;
}

#div-evtMntr .checkbox-inline {
	line-height: 30px;
}

#div-evtMntr .checkbox-inline input {
	margin-top: 8px;
}
</style>
<div class="panel panel-default panel-ucp" id="div-evtMntr" data-point-x="${divEvent.pointX}" data-point-y="${divEvent.pointY}">
	<div class="panel-heading">
		<h3 class="panel-title">이벤트모니터링</h3>
	</div>
	<div class="panel-body">
		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
					<select class="form-control input-sm" id="sel-eventKindList" name="sel-eventKindList"></select>
				</div>
			</div>
			<div class="col-xs-6 text-right">
				<label class="checkbox-inline">
					<input type="checkbox" id="chk-excludeFcltError" name="chk-excludeFcltError" checked="checked">
					시설물고장제외
				</label>
			</div>
		</div>
		<table id="grid-evtMntr"></table>
		<div id="paginate-evtMntr" class="paginate text-center"></div>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-4">
				<button type="button" class="btn btn-default btn-sm btn-ucp" onclick="stopCastNet();">기본화면</button>
			</div>
			<div class="col-xs-8 text-right">
				<label class="checkbox-inline">
					<input type="checkbox" id="chk-evtLcMoveYn" name="chk-evtLcMoveYn">
					자동표출
				</label>
				<label class="checkbox-inline">
					<input type="checkbox" id="chk-designation" name="chk-designation">
					선택감시
				</label>
			</div>
		</div>
	</div>
</div>
<script>
	$(function() {
		oEvtMntr = new evtMntr();
		oEvtMntr.init();
	});

	function evtMntr() {
		this.init = function() {
			oEvtMntr.grid();

			$.post(contextRoot + '/mntr/eventKindList.json').done(function(data) {
				var codeInfoList = data.list;
				$('#sel-eventKindList').empty();
				$('#sel-eventKindList').append("<option value=''> 이벤트 선택 </option>");
				for (var i = 0; i < codeInfoList.length; i++) {
					$('#sel-eventKindList').append("<option value='" + codeInfoList[i].evtId + "'>" + codeInfoList[i].evtNm + "</option>");
				}
			});

			if (oConfigure.evtLcMoveYn == 'Y') {
				$('#chk-evtLcMoveYn').prop('checked', true);
			}

			$('#sel-eventKindList').change(function() {
				oEvtMntr.gridReload();
			});

			$('#chk-excludeFcltError').change(function() {
				oEvtMntr.gridReload();
			});

			$("#chk-evtLcMoveYn").change(function() {
				var sEvtLcMoveYn = (this.checked == true) ? 'Y' : 'N';
				$.ajax({
					method : 'POST',
					url : contextRoot + '/mntr/saveMngDisplay.json',
					data : {
						evtLcMoveYn : sEvtLcMoveYn
					},
					success : function(data, textStatus, jqXHR) {
						if (data.status == '1') {
							console.log('자동표출: %s', sEvtLcMoveYn);
							oConfigure.evtLcMoveYn = sEvtLcMoveYn;
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log('자동표출: ' + jqXHR.status + ' ' + jqXHR.statusText);
					}
				});
			});

			$("#chk-designation").change(function() {
				oMain.mode = (this.checked == true) ? oMain.mntr[1] : oMain.mntr[0];
			});
		};

		this.grid = function() {
			$('#grid-evtMntr').jqGrid(
					{
						url : contextRoot + '/mntr/evtMntrList.json',
						datatype : 'json',
						mtype : 'POST',
						height : 'auto',
						autowidth : true,
						rowNum : 5,
						postData: {
							excludeFcltError : $('#chk-excludeFcltError').is(':checked'),
							searchEvtId : $('#sel-eventKindList option:selected').val()
						},
						loadComplete : function(data) {
							checkGridNodata('evtMntr', data);
							pagenationReload('evtMntr', data, oEvtMntr.gridParam());
						},
						colNames: [
								'evtOcrNo', 'evtId', 'pointX', 'pointY', 'ocrFcltId', '유형', '발생일자', '진행상태', '정보', '영상'
						],
						colModel: [
								{
									name: 'evtOcrNo',
									key : true,
									hidden : true
								},
								{
									name: 'evtId',
									hidden : true
								},
								{
									name: 'pointX',
									hidden : true
								},
								{
									name: 'pointY',
									hidden : true
								},
								{
									name: 'ocrFcltId',
									hidden : true
								},
								{
									name: 'evtNm',
									align : 'center',
									classes : 'jqgrid_cursor_pointer'
								},
								{
									name: 'evtOcrYmdHms',
									align : 'center',
									classes : 'jqgrid_cursor_pointer'
								},
								{
									name: 'evtPrgrs',
									align : 'center',
									classes : 'jqgrid_cursor_pointer'
								},
								{
									name: 'detail',
									align : 'center',
									classes : 'jqgrid_cursor_pointer',
									formatter : function(cellvalue, options, rowObject) {
										var btn = $('<button/>', {
											'class' : 'btn btn-primary btn-sm btn-ucp',
											'text' : '상세',
											'onclick' : 'javascript:oEvtMntr.evtDetail("' + rowObject.pointX + '", "' + rowObject.pointY + '", "' + rowObject.evtId + '", "'
													+ rowObject.evtOcrNo + '");'
										});
										return btn.prop('outerHTML');
									}
								}, {
									name: 'castnet',
									align : 'center',
									classes : 'jqgrid_cursor_pointer',
									formatter : function(cellvalue, options, rowObject) {
										var btn = $('<button/>', {
											'class' : 'btn btn-primary btn-sm btn-ucp',
											'text' : '영상'
										});
										btn.attr('onclick', 'javascript:doCastNetByEvtOcrNo("' + rowObject.evtOcrNo + '", "Y");');
										return btn.prop('outerHTML');
									}
								}
						],
						jsonReader : {
							root : 'rows',
							total : 'totalPages',
							records : function(obj) {
								$('#rowCnt').text(obj.totalRows);
								return obj.totalRows;
							}
						},
						onSelectRow : function(row) {
							var rowData = $('#grid-evtMntr').getRowData(row);
							if (parseFloat(rowData.pointX) && parseFloat(rowData.pointY) && _map) {
								var olPoint = OpenLayers.Projection.transform(new OpenLayers.Geometry.Point(rowData.pointX, rowData.pointY), new OpenLayers.Projection("WGS84"),
										new OpenLayers.Projection(oGis.projection));
								oMntrMap.featureselected(olPoint, 'event', '', true, true, true);
							}
						},
						cmTemplate : {
							sortable : false
						}
					});
			$('.ui-jqgrid-sortable').css('cursor', 'default');
		};

		this.gridParam = function() {
			var isExcludeFcltError = $('#chk-excludeFcltError').is(':checked');
			var sEvtId = $('#sel-eventKindList option:selected').val();
			var oParam = {
				excludeFcltError : isExcludeFcltError,
				searchEvtId : sEvtId
			};
			return oParam;
		};

		this.gridReload = function() {
			$('#grid-evtMntr tbody').empty();
			$('#grid-evtMntr').setGridParam({
				page : 1,
				postData : oEvtMntr.gridParam()
			}).trigger("reloadGrid");
		};

		this.evtDetail = function(pointX, pointY, evtId, evtOcrNo) {
			var olPoint = OpenLayers.Projection.transform(new OpenLayers.Geometry.Point(pointX, pointY), new OpenLayers.Projection("WGS84"), new OpenLayers.Projection(
					oGis.projection));
			oMntrMap.featureselected(olPoint, 'event', '', true, true, true);
			doDivSituation(evtId, evtOcrNo, 'Y');
		};
	};
</script>