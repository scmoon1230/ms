<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp" id="div-carLcInfo">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<div id="searchBar" class="row">
			<div class="searchBox" style="height: auto; padding: 5px 5px 10px 5px;">
				<div class="form-inline">
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" name="rdoEvt" value="selected" checked="checked" onclick="searchGridCarLcInfo();">선택된 재난
						</label>
						<label class="radio-inline">
							<input type="radio" name="rdoEvt" value="all" onclick="searchGridCarLcInfo();">재난 전체(진행중)
						</label>
					</div>
				</div>
			</div>
			<div class="searchBox">
				<div class="form-inline">
					<div class="form-group">
						차량/호출 <input id="searchKeyword" class="form-control input-sm" placeholder="차량/호출 번호를 입력" />
					</div>
					<div class="form-group">
						<button class="btn btn-primary btn-sm btn-ucp" onclick="searchGridCarLcInfo();">검색</button>
					</div>
				</div>
			</div>
		</div>
		<table id="grid-carLcInfo"></table>
		<div id="paginate-carLcInfo" class="paginate text-center"></div>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="form-inline">
				<div class="col-xs-6">
					<div class="form-group">
						<label class="checkbox-inline">
							<input type="checkbox" id="chk-carLcInfo" checked="checked"> 위치표출
						</label>
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group">
						궤적표출갯수 <select id="sel-carLcInfo" class="form-control input-sm" onchange="javascript:searchGridCarLcInfo();">
							<option value="1" <c:if test="${mntr_car_lc_trace_cnt == '1'}">selected="selected"</c:if>>1</option>
							<option value="5" <c:if test="${mntr_car_lc_trace_cnt == '5'}">selected="selected"</c:if>>5</option>
							<option value="10" <c:if test="${mntr_car_lc_trace_cnt == '10'}">selected="selected"</c:if>>10</option>
							<option value="20" <c:if test="${mntr_car_lc_trace_cnt == '20'}">selected="selected"</c:if>>20</option>
							<option value="30" <c:if test="${mntr_car_lc_trace_cnt == '30'}">selected="selected"</c:if>>30</option>
							<option value="40" <c:if test="${mntr_car_lc_trace_cnt == '40'}">selected="selected"</c:if>>40</option>
							<option value="50" <c:if test="${mntr_car_lc_trace_cnt == '50'}">selected="selected"</c:if>>50</option>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="form-inline" id="seleclted-carLcInfo" style="padding-top: 3px;"></div>
	</div>
</div>
<script>
	$(function() {
		$('#grid-carLcInfo').jqGrid({
			url : contextRoot + '/mntr/carLcInfoList.json',
			datatype : 'json',
			mtype : 'POST',
			height : 'auto',
			width : parseInt(oConfigure.mntrViewRight) - 50,
			autowidth : false,
			rowNum : 2,
			multiselect : true,
			postData: {
				evtOcrNo : evtOcrNo
			},
			beforeRequest : function() {
				var rdo = $(':radio[name="rdoEvt"]:checked').val();
				if (rdo == 'all') {

				} else if (rdo == 'selected' && (typeof evtOcrNo == 'undefined' | evtOcrNo == '')) {
					setGridNodata('carLcInfo', '요청된 ' + '발생' + '번호가 없습니다.');
					return false;
				}
			},
			loadComplete : function(data) {
				checkGridNodata('carLcInfo', data);
				pagenationReload('carLcInfo', data, getGridCarLcInfoParam());
				var grid = $('#grid-carLcInfo');
				$("#cb_" + grid[0].id).hide();
			},
			colNames: [
					'발생번호', '호출번호', '차량번호'
			],
			colModel: [
					{
						name: 'evtOcrNo',
						align : 'center',
						classes : 'jqgrid_cursor_pointer',
						cellattr : function() {
							return 'style="width:40%;"'
						},
						width : 40
					}, {
						name: 'radioCallNm',
						align : 'center',
						classes : 'jqgrid_cursor_pointer',
						cellattr : function() {
							return 'style="width:30%;"'
						},
						width : 30
					}, {
						name: 'carNum',
						align : 'center',
						classes : 'jqgrid_cursor_pointer',
						cellattr : function() {
							return 'style="width:30%;"'
						},
						width : 30
					}
			],
			jsonReader : {
				root : "rows",
				total : "totalPages",
				records : "totalRows"
			},
			onSelectRow : function(rowId, status, e) {
				var rowData = $('#grid-carLcInfo').getRowData(rowId);

				var sel = $('#sel-carLcInfo option:selected').val();
				var uid = rowData.evtOcrNo + '' + (rowData.radioCallNm).substring(0, 4);
				if (status) {
					var checked = $('#chk-carLcInfo').is(':checked');
					if (checked) {
						var anchorRadioCallNm = $('<a/>', {
							href : '#',
							class : 'carLcInfo',
							title : rowData.evtOcrNo + ', ' + (rowData.radioCallNm).substring(0, 4) + ', ' + rowData.carNum,
							onclick : 'javascript:moveRadioCallNm("' + uid + '");',
							text : rowData.radioCallNm
						});

						var anchorX = $('<a/>', {
							href : '#',
							title : '해제',
							onclick : 'javascript:removeRadioCallNm("' + uid + '");',
							style : 'margin-left: 2px; color: #f00;',
							text : '✖'
						});

						var span = $('<span/>', {
							class : 'radioCallNm',
							id : uid
						});

						span.append(anchorRadioCallNm);
						span.append(anchorX);
						if( !$('#' + rowData.evtOcrNo + '' + (rowData.radioCallNm).substring(0, 4)).exists() ) {
							$('#seleclted-carLcInfo').append(span);
							showCarLc(rowData.evtOcrNo, rowData.carNum, sel, true);
						}
					} else {
						showCarLc(rowData.evtOcrNo, rowData.carNum, 1, true);
					}
				} else {
					removeRadioCallNm(uid);
				}
			},
			cmTemplate : {
				sortable : false
			}
		});
		$(".ui-jqgrid-sortable").css("cursor", "default");

		setInterval(function() {
			searchGridCarLcInfo();

			if(typeof carLcInfoLayer != 'undefined') {
				carLcInfoLayer.removeAllFeatures();
			}

			var anchors 		= $('#seleclted-carLcInfo a.carLcInfo');
			var trace_point_cnt = $('#sel-carLcInfo option:selected').val();
			var auto_ocr_no 	= "";
			// 출동차량 위치 자동 표출 (DEV DEMO시) 20170905
		/*	if (oConfigure.exeEnv == 'DEV') {
				var rowData = $('#grid-carLcInfo').getRowData();
				for (var i = 0; i < rowData.length; i++) {
					if (evtOcrNo == rowData[i].evtOcrNo) {
						showCarLc(rowData[i].evtOcrNo, rowData[i].carNum, trace_point_cnt, false);
						auto_ocr_no = evtOcrNo;
					}
				}
			}
		*/

			for (var i = 0; i < anchors.length; i++) {
				var title = $(anchors[i]).prop('title');
				var carLcInfo = title.split(',');
				if (carLcInfo[0].trim() != auto_ocr_no) {
					showCarLc(carLcInfo[0].trim(), carLcInfo[2].trim(), trace_point_cnt, false);
				}
			}
		}, ${mntr_car_lc_tm});
	});

	function getGridCarLcInfoParam() {
		var rdo = $(':radio[name="rdoEvt"]:checked').val();

		var param = {
			evtOcrNo : rdo == 'selected' ? evtOcrNo : '',
			searchKeyword : $('#div-carLcInfo #searchKeyword').val()
		};
		return param;
	}

	function searchGridCarLcInfo() {
		gridReload('carLcInfo', 1, getGridCarLcInfoParam());
	}

	function showCarLc(evtOcrNo, carNum, rowNum, isCenter) {
		console.log('**** showCarLc evtOcrNo:' + evtOcrNo + ', carNum:' + + ', rowNum:' + rowNum);
		$.post(contextRoot + '/mntr/carLcDtlList.json', {
			evtOcrNo : evtOcrNo,
			searchKeyword : carNum,
			searchRownum : rowNum
		}).done(function(data) {
			var rows = data.rows;
			var uid = rows[0].evtOcrNo + '' + (rows[0].radioCallNm).substring(0, 4);
			var radioCallNm = rows[0].radioCallNm;
			var carTy = rows[0].carTy;
			var = rows[0].carNum;

			var points = [];
			var pointsStyle = {
				externalGraphic : contextRoot + '/images/mntr/gis/selected/point_car_location.png',
				pointRadius : 10,
				graphicXOffset : -5,
				graphicYOffset : -5,
				graphicWidth : 10,
				graphicHeight : 10,
				zIndex : 20,
				graphicZIndex : 40
			}

			if (carTy.indexOf('고가차') > -1) {
				carTy = '119HL';
			} else if (carTy.indexOf('구급차일반') > -1) {
				carTy = '119';
			} else if (carTy.indexOf('구급차특수') > -1) {
				carTy = '119';
			} else if (carTy.indexOf('구조버스') > -1) {
				carTy = '119B';
			} else if (carTy.indexOf('굴절차') > -1) {
				carTy = '119HL';
			} else if (carTy.indexOf('기타승용차') > -1) {
				carTy = '119N';
			} else if (carTy.indexOf('물탱크차') > -1) {
				carTy = '119WT';
			} else if (carTy.indexOf('배연차') > -1) {
				carTy = '119T';
			} else if (carTy.indexOf('지휘차') > -1) {
				carTy = '119HQ';
			} else if (carTy.indexOf('펌프차') > -1) {
				carTy = '119P';
			} else if (carTy.indexOf('화학차') > -1) {
				carTy = '119T';
			} else {
				carTy = '119';
			}

			var carStyle = {
				externalGraphic : contextRoot + '/images/mntr/gis/car/' + carTy + '.png',
				pointRadius : 10,
				graphicXOffset : -18,
				graphicYOffset : -15,
				graphicWidth : 40,
				graphicHeight : 40,
				zIndex : 20,
				graphicZIndex : 40,
				label : radioCallNm,
				fontColor : '#f00',
				fontSize : '17px',
				fontFamily : '맑은고딕',
				fontWeight : 'bold',
				labelAlign : 'cm',
				labelXOffset : 3,
				labelYOffset : -35,
				labelOutlineColor : '#fff',
				labelOutlineWidth : 3
			}

			for (var i = 0; i < rows.length; i++) {
				var point = convertByWGS84(rows[i].pointX, rows[i].pointY);
				point = new OpenLayers.Geometry.Point(point.x, point.y);
				points.push(point);

				if (i != 0) {
					var p = new OpenLayers.Feature.Vector(point, {
						kind : 'carLcInfo',
						type : 'point',
						uid : uid,
						evtOcrNo : rows[i].evtOcrNo,
						radioCallNm : rows[i].radioCallNm,
						: rows[i].carNum,
						carTy : rows[i].carTy,
						carPsitn : rows[i].carPsitn,
						carTelNo : rows[i].carTelNo,
						pointYmdHms : rows[i].pointYmdHms
					}, pointsStyle);
					carLcInfoLayer.addFeatures([
						p
					]);
				}
				else {
					var p = new OpenLayers.Feature.Vector(point, {
						kind : 'carLcInfo',
						type : 'lastPoint',
						uid : uid,
						evtOcrNo : rows[i].evtOcrNo,
						radioCallNm : rows[i].radioCallNm,
						: rows[i].carNum,
						carTy : rows[i].carTy,
						carPsitn : rows[i].carPsitn,
						carTelNo : rows[i].carTelNo,
						pointYmdHms : rows[i].pointYmdHms,
						pointX : rows[i].pointX,
						pointY : rows[i].pointY
					}, carStyle);
					carLcInfoLayer.addFeatures([
						p
					]);

					var lastPoint = convertByWGS84(rows[i].pointX, rows[i].pointY);
					var lonLat = new OpenLayers.LonLat(point.x, point.y);

					if(isCenter) {
						_map.setCenter(lonLat, _map.getZoom());
					}
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
				uid : uid,
				kind : 'carLcInfo',
				type : 'line'
			}, style);

			carLcInfoLayer.addFeatures([
				lineFeature
			]);

		});
	}

	function hideCarLc(evtOcrNo, radioCallNm) {
		var uid = evtOcrNo + '' + radioCallNm;
		carLcInfoLayer.removeFeatures(carLcInfoLayer.getFeaturesByAttribute('uid', uid));
	}

	function removeRadioCallNm(uid) {
		var s = $('#grid-carLcInfo').jqGrid('getGridParam', 'selarrrow');
		for(var i = 0; i < s.length; i++) {
			var rowData = $('#grid-carLcInfo').getRowData(s[i]);
			var uid2 = rowData.evtOcrNo + '' + (rowData.radioCallNm).substring(0, 4);
			if(uid == uid2) {
				$('#grid-carLcInfo').jqGrid('setSelection', s[i]);
			}
		}

		if( $('#' + uid).exists() ) {
			$('#' + uid).remove();
			carLcInfoLayer.removeFeatures(carLcInfoLayer.getFeaturesByAttribute('uid', uid));
		}
	}

	function moveRadioCallNm(uid) {
		var features = carLcInfoLayer.getFeaturesByAttribute('uid', uid);
		for (var i = 0; i < features.length; i++) {
			if(features[i].attributes.type == 'lastPoint') {
				var lonLat = new OpenLayers.LonLat(features[i].geometry.x, features[i].geometry.y);
				_map.setCenter(lonLat, _map.getZoom());
			}
		}
	}
</script>