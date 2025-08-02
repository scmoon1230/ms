<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<!-- 체크박스 -->
		<div class="form-inline">
			<select id="sel-carLcInfo" class="form-control input-sm" onchange="javascript:searchGridCarLcInfo();">
				<option value="5" selected="selected">5</option>
				<option value="10">10</option>
				<option value="20">20</option>
				<option value="30">30</option>
				<option value="40">40</option>
				<option value="50">50</option>
			</select>
			<div class="checkbox">
				<label> <input type="checkbox" id="chk-carLcInfo" disabled="disabled" onclick="toggleCarLcInfo()"> 경로 그리기
				</label>
			</div>
		</div>
		<!-- 그리드 생성 -->
		<table id="grid-carLcInfo"></table>
		<!-- 페이징 생성 -->
		<div id="paginate-carLcInfo" class="paginate text-center"></div>
	</div>
</div>
<script>
	var divEvtOcrNo = '${divEvent.evtOcrNo}';
	/*
	var fpCarLcInfoControl = new OpenLayers.Control.FeaturePopups({
		boxSelectionOptions : {},
		popupOptions : {
			hover : {
				followCursor : false
			},
			hoverList : {
				followCursor : false
			}
		},
		layers : [
			[
					carLcInfoLayer, {
						templates : {

							hover : function(feature) {
								var type = feature.attributes.type;
								if (type == 'point') {
									var table = $('<table/>', {
										id : 'tbFcltInfo',
										class : 'table table-striped table-condensed'
									});
									var thead = $('<thead/>');
									var theadTr = $('<tr/>', {
										class : 'warning'
									});
									var th = $.parseHTML('<th>헤더</th><th>헤더</th><th>헤더</th>');
									var tbody = $('<tbody/>');
									var tbodyTr = $('<tr/>');
									tbodyTr.append($('<td/>', {
										text : '내용'
									}));
									tbodyTr.append($('<td/>', {
										text : '내용'
									}));
									tbodyTr.append($('<td/>', {
										html : '내용'
									}));
									theadTr.append(th);
									thead.append(theadTr);
									table.append(thead);
									tbody.append(tbodyTr);
									table.append(tbody);
									return table.prop('outerHTML');
								}
							}
						}

					}
			]
		]
	});
	*/
	$(function() {
		$('#grid-carLcInfo').jqGrid({
			url : contextRoot + '/mntr/carLcInfoList.json',
			datatype : 'json',
			mtype : 'POST',
			height : 'auto',
			width : parseInt(oConfigure.mntrViewRight) - 50,
			autowidth : false,
			rowNum: $('#sel-carLcInfo option:selected').val(),
			postData: {
				evtOcrNo : divEvtOcrNo
			},
			beforeRequest : function() {
				// 유효성 체크.
				if (divEvtOcrNo == '') {
					setGridNodata('carLcInfo', '이벤트 발생번호가 없습니다.');
					return false;
				}
			},
			loadComplete : function(data) {
				// 후처리.
				var result = checkGridNodata('carLcInfo', data);
				// pagenationReload('carLcInfo', data, getGridCarLcInfoParam());
				if (result) {
					$('#chk-carLcInfo').prop('disabled', false);
				}
			},
			colNames: [
					'일시', '경도', '위도'
			],
			colModel: [
					{
						name: 'pointYmdHms'
					}, {
						name: 'pointX'
					}, {
						name: 'pointY'
					}
			],
			jsonReader : {
				root : "rows",
				total : "totalPages",
				records : "totalRows"
			},
			onSelectRow : function(rowId) {
				var rowData = $('#grid-carLcInfo').getRowData(rowId);
				var point = convertByWGS84(rowData.pointX, rowData.pointY);
				/*
				removePreviousFeatureselected();
				previousFeatureselected = featureselected(point, '', '', false, true, true);
				 */
			},
			cmTemplate : {
				sortable : false
			}
		});
		/*
		_map.addControls([
			fpCarLcInfoControl
		]);
		fpCarLcInfoControl.activate();
		*/
	});

	// 파라메터 생성
	function getGridCarLcInfoParam() {
		var param = {
			evtOcrNo : divEvtOcrNo
		};
		return param;
	}

	// 검색 및 갱신
	function searchGridCarLcInfo() {
		$('#grid-carLcInfo').setGridParam({
			rowNum: $('#sel-carLcInfo option:selected').val()
		});
		gridReload('carLcInfo', 1, getGridCarLcInfoParam());
		carLcInfoLayer.removeFeatures(carLcInfoLayer.getFeaturesByAttribute('kind', 'carLcInfo'));
		$('#chk-carLcInfo').prop('checked', false);
	}

	function toggleCarLcInfo() {
		var checked = $('#chk-carLcInfo').is(':checked');

		if (checked) {
			var rowData = $('#grid-carLcInfo').getRowData();
			var points = [];
			for (var i = 0; i < rowData.length; i++) {
				var point = convertByWGS84(rowData[i].pointX, rowData[i].pointY);
				point = new OpenLayers.Geometry.Point(point.x, point.y);
				points.push(point);

				var p = new OpenLayers.Feature.Vector(point, {
					kind : 'carLcInfo',
					type : 'point'
				}, {
					externalGraphic : contextRoot + '/images/mntr/gis/selected/point_car_location.png',
					pointRadius : 10,
					graphicXOffset : -5,
					graphicYOffset : -5,
					graphicWidth : 10,
					graphicHeight : 10,
					zIndex : 20,
					graphicZIndex : 40
				});

				if (i != 0) {
					carLcInfoLayer.addFeatures([p]);
				}
			}

			var line = new OpenLayers.Geometry.LineString(points);
			var style = {
				strokeColor : '#f00',
				strokeOpacity : 0.8,
				strokeWidth : 2,
				strokeDashstyle : 'solid',
				graphicZIndex : 30
			};

			var lineFeature = new OpenLayers.Feature.Vector(line, {
				kind : 'carLcInfo',
				type : 'line'
			}, style);

			carLcInfoLayer.addFeatures([
				lineFeature
			]);
		}
		else {
			carLcInfoLayer.removeFeatures(carLcInfoLayer.getFeaturesByAttribute('kind', 'carLcInfo'));
		}
	}
</script>