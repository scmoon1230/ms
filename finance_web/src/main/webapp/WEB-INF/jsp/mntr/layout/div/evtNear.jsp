<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp">
	<div class="panel-heading">
		<h3 class="panel-title">발생지점 ${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<div id="searchBar" class="row">
			<div class="searchBox">
				<div class="form-inline" style="white-space: nowrap;">
					<div class="form-group">
						<label for="searchRadius">반경(m)</label>
						<input type="number" class="form-control input-sm" id="searchRadius" size="4" min="100" max="9999" maxlength="4" placeholder="반경">
					</div>
					<div class="form-group">
						<select id="searchFcltUsedTyCd" class="form-control input-sm"></select>
					</div>
					<div class="form-group">
						<button class="btn btn-primary btn-sm btn-ucp" onclick="searchGridNear();">검색</button>
					</div>
				</div>
			</div>
			<div class="searchBox">
				<div class="form-inline">
					<div class="col-xs-12">
						<div class="form-group" style="width: 100%;">
							<label for="searchFcltLblNm" class="sr-only">시설물명</label>
							<input id="searchFcltLblNm" class="form-control input-sm" style="width: 100%;" placeholder="검색할 카메라명을 입력하세요." />
						</div>
					</div>
				</div>
			</div>

			<table id="grid-near"></table>

			<div id="paginate-near" class="paginate text-center"></div>
		</div>
	</div>
</div>
<script>
	var pointX = '${divEvent.pointX}';
	var pointY = '${divEvent.pointY}';

	$(function() {
		$('#searchRadius').val(oConfigure.radsClmt);

		$.ajax({
			type : 'POST',
			url : contextRoot + '/mntr/cmm/selectFcltUsedTyAll.json',
			success : function(result) {
				if(result.list) {
					var $searchFcltUsedTyCd = $('#searchFcltUsedTyCd');
					$searchFcltUsedTyCd.append($('<option />', {
						'text': '시설물용도별유형',
						'value': ''
					}));
					$.each(result.list, function(i, v) {
						$searchFcltUsedTyCd.append($('<option />', {
							'text': v.fcltUsedTyNm,
							'value': v.fcltUsedTyCd
						}));
					});
				}
			},
			error : function() {
				console.log("시설물 유형을 가져오지 못했습니다.");
			}
		});

		/* 이벤트 목록 */
		$('#grid-near').jqGrid({
			url : contextRoot + '/mntr/nearestCctvList.json',
			datatype : 'json',
			mtype : 'POST',
			height : 'auto',
			width : parseInt(oConfigure.mntrViewRight) - 50,
			autowidth : false,
			rowNum : 5,
			postData: {
				pointX : pointX,
				pointY : pointY,
				searchRadius : $('#searchRadius').val() / 1000,
				searchFcltUsedTyCd : $('#searchFcltUsedTyCd option:selected').val(),
				searchFcltLblNm : $('#searchFcltLblNm').val()
			},
			beforeRequest : function() {
				if ($('#searchRadius').val() == '') {
					setGridNodata('near', '반경을 입력하세요.');
					return false;
				}
				else if (pointX == '' || pointY == '') {
					setGridNodata('near', '위치정보가 없습니다.');
					return false;
				}
			},
			loadComplete : function(data) {
				checkGridNodata('near', data);
				pagenationReload('near', data, getGridNearParam());
			},
			colNames: [
					'fcltId',  'pointX', 'pointY', 'fcltUsedTyCd','카메라명', '거리', '보기'
			],
			colModel: [
					{
						name: 'fcltId',
						key : true,
						hidden : true
					}, {
						name: 'pointX',
						hidden : true
					}, {
						name: 'pointY',
						hidden : true
					}, {
						name: 'fcltUsedTyCd',
						hidden : true
					}, {
						name: 'fcltLblNm',
						align : 'left',
						classes : 'jqgrid_cursor_pointer',
						cellattr : function() {
							return 'style="width:70%;"'
						},
						formatter : function(cellvalue, options, rowObject) {
							return '[' + rowObject.fcltUsedTyNm + ']' + rowObject.fcltLblNm + '<br>' + rowObject.portalAdres;
						},
						width : 70
					}, {
						name: 'distance',
						align : 'center',
						classes : 'jqgrid_cursor_pointer',
						cellattr : function() {
							return 'style="width:15%;"'
						},
						formatter : function(cellvalue, options, rowObject) {
							return Math.floor(rowObject.distance * 1000 * 100) / 100 + 'm';
						},
						width : 15
					}, {
						name: 'mntrReq',
						align : 'center',
						sortable : false,
						cellattr : function() {
							return 'style="width: 15%; padding: 5px 0px 5px 0px;"'
						},
						formatter : function(cellvalue, options, rowObject) {
							var btn = $('<button/>', {
								'class' : 'btn btn-primary btn-ucp btn-xs',
								'style' : 'height: 25px; padding: 2px 10px 2px 10px;',
								'onclick' : function() {
//									if(rowObject.fcltUsedTyCd == 'UTI') {
									if(rowObject.viewerTyCd == 'RTSP') {
										return 'javascript:oVmsCommon.openRtspPlayer("' + rowObject.fcltId + '");';
									}
									else {
										return 'javascript:oVmsCommon.openVmsPlayer("' + rowObject.fcltId + '");';
									}
								},
								'text' : '보기'
							});

							return btn.prop('outerHTML');
						},
						width : 15
					}
			],
			jsonReader : {
				root : "rows",
				total : "totalPages",
				records : "totalRows"
			},
			onSelectRow : function(rowId) {
				var rowData = $('#grid-near').getRowData(rowId);
				var point = convertByWGS84(rowData.pointX, rowData.pointY);
				removePreviousFeatureselected();
				previousFeatureselected = featureselected(point, '', '', false, true, true);
			},
			cmTemplate : {
				sortable : false
			}
		});
	});

	function getGridNearParam() {
		if ($('#searchRadius').val() == '') {
			$('#searchRadius').val(radius * 1000);
		}

		var param = {
			pointX : pointX,
			pointY : pointY,
			searchRadius : $('#searchRadius').val() / 1000,
			searchFcltUsedTyCd : $('#searchFcltUsedTyCd option:selected').val(),
			searchFcltLblNm : $('#searchFcltLblNm').val()
		};
		return param;
	}

	function searchGridNear() {
		gridReload('near', 1, getGridNearParam());
	}
</script>