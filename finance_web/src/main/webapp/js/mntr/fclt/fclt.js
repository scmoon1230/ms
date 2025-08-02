$(function() {
	oFclt = new fclt();
	oFclt.init();
});

function fclt() {
	this.draw = null;

	this.init = function() {
		// LEFT 조절
		oConfigure.mntrViewLeft = 500;
		$('aside#left').css('width', oConfigure.mntrViewLeft);
		$('section#body').css('left', oConfigure.mntrViewLeft + 10);
		$('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			left : false
		});
		if (oGis.olVersion == 5) {
			// 1. BASE, SATELLITE 배경지도 생성.
			olSwipMap.init({
				dropdown : true,
				bookmark : false
			});

			// 2. 시설물 레이어 설정.
			olSwipMap.layers.fclt.init();

			// 3. Feature 핸들러.
			function handler(event) {
				var oOptOptions = {
					hitTolerance : 10
				};

				var oFeaturesFclt = [];
				olMap.map.forEachFeatureAtPixel(event.pixel, function(feature, layer) {
					if (layer != null) {
						if (olMap.layers.fclt === layer) {
							var olFeatures = feature.get('features');
							if (typeof olFeatures == 'undefined') {
								oFeaturesFclt.push(feature.getProperties());
							} else {
								$.each(olFeatures, function(i, f) {
									oFeaturesFclt.push(f.getProperties());
								});
							}
	                    } else if (olMap.layers.angle === layer) {
	                        oFeaturesFclt.push(feature.getProperties());
	                    } else {
	                        olSwipMap.bookmark.check(event, feature, layer);
						}
					}
				}, oOptOptions);

				var olOverlay = olMap.map.getOverlayById('ol-overlay-' + event.type);
				olOverlay.setPosition(event.coordinate);

				if (event.type == 'pointermove') {
					var oPosition = olSwipMap.overlays.click.getPosition();
					if (typeof oPosition != 'undefined') {
						return false;
					}
				}

				var $olOverlay = $('#ol-overlay-' + event.type);

				if (oFeaturesFclt.length) {
					// title
					var elPanelHeadingTitle = $olOverlay.find('.panel-heading-title');
					if (elPanelHeadingTitle.length == 1) {
						var $panelHeadingTitle = $(elPanelHeadingTitle[0]);
						if (oFeaturesFclt.length) {
							$panelHeadingTitle.text('시설물(' + oFeaturesFclt.length + ')');
						}
					}
					oFeaturesFclt.sort(function(a, b) {
						if (a.fcltId > b.fcltId) {
							return 1;
						}
						if (a.fcltId < b.fcltId) {
							return -1;
						}
						return 0;
					});
					// tbody
					$('#table-' + event.type + '-fclt').show();
					var elTbody = $olOverlay.find('#table-' + event.type + '-fclt tbody');
					if (elTbody.length == 1) {
						var $tbody = $(elTbody[0]);
						$tbody.empty();
						var nMaxCntFclt = 30;
						if (oFeaturesFclt.length <= nMaxCntFclt) {
							$.each(oFeaturesFclt, function(i, v) {
								const oAg = olMap.layers.angle.getSource().getFeatureById('AG_' + v.fcltId);
								let nCctvOsvtAg = NaN;
								if (oAg != null) {
									nCctvOsvtAg = oAg.getProperties().cctvOsvtAg;
									if (nCctvOsvtAg < 0) nCctvOsvtAg = 360 + nCctvOsvtAg;
									nCctvOsvtAg = Math.round(nCctvOsvtAg / 10) * 10;
								}

								let sFcltKndDtlCd = v.fcltKndDtlCd;
								let sFcltKndDtlNm = '';
								if (sFcltKndDtlCd === 'RT') {
									sFcltKndDtlNm = '<i class="fas fa-sync-alt" title="회전형CCTV" data-toggle="tooltip" data-placement="top"></i> ';
								} else if (sFcltKndDtlCd === 'FT' && !isNaN(nCctvOsvtAg)) {
									sFcltKndDtlNm = `<i class="fas fa-arrow-up" title="고정형CCTV(${nCctvOsvtAg}˚)" style="transform: rotate(${nCctvOsvtAg}deg)" data-toggle="tooltip" data-placement="top"></i> `;
								}
								
								var $tr = $('<tr/>');
								$tr.append($('<td/>', {
									'class' : 'text-ellipsis td-fclt-lbl-nm',
									'title' : v.fcltLblNm,
									'html' : (event.type == 'click') ? '<a href="#" onclick="javascript:oFclt.viewDeatil(\'' + v.fcltId + '\');">' + v.fcltLblNm + '</a>' : v.fcltLblNm,
									'data-toggle' : 'tooltip',
									'data-placement' : 'top',
								}));
								$tr.append($('<td/>', {
									'class' : 'text-ellipsis td-fclt-knd-nm',
									'title' : v.fcltKndNm + '(' + v.fcltUsedTyNm + ')',
									'text' : v.fcltKndNm + '(' + v.fcltUsedTyNm + ')',
									'data-toggle' : 'tooltip',
									'data-placement' : 'top',
								}));

								var $fcltSttus = $('<span/>', {
									'text' : v.fcltSttusNm
								});

								if (v.fcltSttus == '0') {
									$fcltSttus.css('color', 'green');
								} else if (v.fcltSttus == '1') {
									$fcltSttus.css('color', 'red');
								} else {
									$fcltSttus.css('color', 'orange');
								}
								$tr.append($('<td/>', {
									'class' : 'text-center',
									'html' : $fcltSttus
								}));

								if (event.type == 'click') {
									var $tdReq = $('<td/>');

									var $btnDetail = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs btn-detail',
										'onclick' : 'javascript:oFclt.detail("' + v.fcltId + '")',
										'title' : '해당 시설물의 상세 페이지로 이동합니다.',
										'text' : '상세',
										'data-toggle' : 'tooltip',
										'data-placement' : 'left',
									});
									$tdReq.append($btnDetail);

									if (v.fcltKndCd == 'CTV' && (v.viewerTyCd == 'VMS' || v.viewerTyCd == 'RTSP')) {
										var $btnPopover = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs btn-popover',
											'onclick' : 'javascript:olSwipMap.mntr.popover.open(this);',
											'title' : '해당 카메라의 영상을 팝오버 형태로 띄워 재생합니다.',
											'text' : '팝오버',
										/*	'data-point-x' : v.pointX, 'data-point-y' : v.pointY, 'data-projection' : 'EPSG:4326', 'data-fclt-id' : v.fcltId, 'data-fclt-uid' : v.fcltUid, 'data-fclt-lbl-nm' : v.fcltLblNm,	*/
											'data-toggle' : 'tooltip',
											'data-placement' : 'left',
										});
	                                    $btnPopover.data(v);
	                                    $btnPopover.data('projection', 'EPSG:4326');

										if (oVms.webRtcYn == 'Y') $tdReq.append($btnPopover);

										var $btnNewWindow = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs btn-new-window',
											'onclick' : 'javascript:oVmsCommon.openVmsPlayer("' + v.fcltId + '");',
											'title' : '해당 카메라의 영상을 새창으로 분리해 큰화면으로 재생합니다.',
											'text' : '새창',
											'data-toggle' : 'tooltip',
											'data-placement' : 'left',
										});
										$tdReq.append($btnNewWindow);
									}
									$tr.append($tdReq);
								} else {
									$tr.append($('<td/>'));
								}

								$tbody.append($tr);
							});
						//	$('#table-click-fclt tbody [data-toggle="tooltip"]').tooltip({
						//		container : 'body'
						//	});
						} else {
							var $tr = $('<tr/>');
							$tr.append($('<td/>', {
								'class' : 'text-center text-danger',
								'text' : '시설물 ' + nMaxCntFclt + '개 초과(지도를 확대하여 사용)',
								'colspan' : '4'
							}));
							$tbody.append($tr);
						}
					}
				} else {
					$('#table-' + event.type + '-fclt').hide();
				}

				$('#table-' + event.type + '-event').hide();

				var oPositionClick = olSwipMap.overlays.click.getPosition();
				var oPositionPointermove = olSwipMap.overlays.pointermove.getPosition();

				if (!oFeaturesFclt.length) {
					if (event.type == 'click' && typeof oPositionClick != 'undefined') {
						$('#ol-overlay-click').hide();
						$('#ol-overlay-click tbody').empty();
						olSwipMap.overlays.click.setPosition(undefined);
					} else if (event.type == 'pointermove' && typeof oPositionPointermove != 'undefined') {
						$('#ol-overlay-pointermove').hide();
						$('#ol-overlay-pointermove tbody').empty();
						this.getTargetElement().style.cursor = '';
						olSwipMap.overlays.pointermove.setPosition(undefined);
					}
				} else {
					if (event.type == 'click') {
						$('#ol-overlay-click').show();
						$('#ol-overlay-pointermove').hide();
						$('#ol-overlay-pointermove tbody').empty();
						this.getTargetElement().style.cursor = '';
						olSwipMap.overlays.pointermove.setPosition(undefined);
	                    $('#ol-overlay-click [data-toggle="tooltip"]').tooltip({container: 'body', trigger: 'hover'});
					} else if (event.type == 'pointermove') {
						$('#ol-overlay-pointermove').show();
						this.getTargetElement().style.cursor = 'pointer';
					}
				}
			}

			olSwipMap.events.feature.pointermove = handler;
			olSwipMap.events.feature.click = handler;

			olSwipMap.listeners.feature.pointermove = olMap.map.on('pointermove', olSwipMap.events.feature.pointermove);
			olSwipMap.listeners.feature.click = olMap.map.on('click', olSwipMap.events.feature.click);
		} else {
			oMntrMap.initFcltLayer('', '', 'N');
			// 반경검색용 레이어 추가
			oFclt.initPolygonLayer();
		}

		$('#setRadius').popover({
			placement : 'right',
			content : '지도를 드래그해서 반경을 설정해주세요.',
			delay : 2
		});
		$('#unSetRadius').popover({
			placement : 'right',
			content : '반경이 해제되었습니다.',
			delay : 2
		});

		// 검색조건 설정
		codeInfoList('#searchFcltSttus', 'FCLT_STTUS', '', '시설물 상태');
		fcltKindInfoList('#searchFcltKndCd', '');
		fcltUsedTyList('#searchFcltUsedTyCd', '', '');
		// plcList('#searchPlcPtrDiv', '', '', '경찰지구대');
		// oCommon.areaList('#searchAreaCd', '', '');

		// 그리드 설정
		oFclt.grid();

		// 이벤트 설정
		$('#searchFcltKndCd').change(function() {
			// reloadGrid();
			fcltUsedTyList('#searchFcltUsedTyCd', $(this).val(), '');
		});

		$('#searchFcltId').keypress(function(event) {
			if (event.which == 13) {
				oFclt.reloadGrid();
			}
		});

		$('#setRadius').popover({
			placement : 'bottom',
			content : '지도를 드래그해서 반경을 설정해주세요.',
			delay : 10
		});

		$('#unSetRadius').popover({
			placement : 'bottom',
			content : '반경이 해제되었습니다.',
			delay : 10
		});

		$('button.tooltip-fclt').tooltip({
			container : 'body',
			placement : 'top'
		});

		// 반경 설정 / 해제
		$('input[name=radioRadius]').click(function(event) {
			var target = $(event.target).val();
			if (target == 'set') {
				oFclt.setRadius();
			} else if (target == 'unset') {
				oFclt.unsetRadius();
			}
		});
	};

	this.initPolygonLayer = function() {
		oPolygonLayer = new OpenLayers.Layer.Vector("Polygon Layer");
		oPolygonControl = new OpenLayers.Control.DrawFeature(oPolygonLayer, OpenLayers.Handler.RegularPolygon, {
			handlerOptions : {
				sides : 80
			}
		});

		oPolygonControl.events.register('featureadded', oPolygonControl, function(evt) {
			if (oAddedFeature != null) {
				oPolygonLayer.removeFeatures([
					oPolygonLayer.getFeatureById(oAddedFeature)
				]);
			}
			oAddedFeature = evt.feature.id;

			var oBoundsSize = evt.feature.geometry.getBounds().getSize();
			var nRadius = ((oBoundsSize.w + oBoundsSize.h) / 4).toFixed(2);

			$('#inputRadius').val(Math.round((oBoundsSize.w + oBoundsSize.h) / 4) + ' 미터');

			var oCenterLonLat = evt.feature.geometry.getBounds().getCenterLonLat();
			oFclt.reloadGridRadius(oCenterLonLat.lon, oCenterLonLat.lat, nRadius);
			oPolygonControl.deactivate();
			$('.searchBox.sm input').prop('disabled', true);
			$('.searchBox.sm select').prop('disabled', true);
			$('#sendBtn').prop('disabled', true);
			$('#setRadius').prop('checked', false);
			$('#unSetRadius').prop('disabled', false);
		});

		oAddedFeature = null;

		_map.addLayer(oPolygonLayer);
		_map.addControl(oPolygonControl);
	};

	this.grid = function() {
		$('#grid-fclt').jqGrid({
			url : contextRoot + '/mntr/fcltList.json',
			datatype : 'json',
			mtype : 'POST',
			height : 'auto',
			autowidth : true,
			rowNum : 10,
			beforeRequest : function() {
				// validate check here!
			},
			postData : {
				searchFcltKndCd : $('#searchFcltKndCd').val()
			},
			loadComplete : function(data) {
				oCommon.jqGrid.loadComplete('fclt', data, oFclt.getGridParams());
				$('td.tooltip-fclt').tooltip({
					container : 'body',
					placement : 'right'
				});
			},
			colNames : [
					'pointX', 'pointY', '시설물ID', 'fcltUid', 'cctvChannel', 'presetBdwStartNum', '시설물명', '유형', '용도', '상태', '지번(도로명)'
			],
			colModel : [
					{		name : 'pointX',				hidden : true
					}, {	name : 'pointY',				hidden : true
					}, {	name : 'fcltId',				hidden : true
					}, {	name : 'fcltUid',				hidden : true
					}, {	name : 'cctvChannel',			hidden : true
					}, {	name : 'presetBdwStartNum',		hidden : true
					}, {	name : 'fcltLblNm',				align : 'left',			classes : 'text-ellipsis tooltip-fclt',
							formatter : function(cellvalue, options, rowObject) {	var fcltLblNm = rowObject.fcltLblNm;
								// var mngSn = rowObject.mngSn;
								// if (mngSn) fcltLblNm = '<strong style="color: #00f;">[' + mngSn + ']</strong>' + fcltLblNm;
								return fcltLblNm;
							}
					}, {	name : 'fcltKndNm',				align : 'center',		width : 85
					}, {	name : 'fcltUsedTyNm',			align : 'center',		width : 85
					}, {	name : 'fcltSttusNm',			align : 'center',		width : 45
					}, {	name : 'address',				align : 'left',			classes : 'text-ellipsis tooltip-fclt',
							formatter : function(cellvalue, options, rowObject) {
								var sAddrRoad = rowObject.roadAdresNm ? rowObject.roadAdresNm : ' - ';
								var sAddrJibun = rowObject.lotnoAdresNm ? rowObject.lotnoAdresNm : ' - ';
								return sAddrJibun + '(' + sAddrRoad + ')';
							}
					}
			],
			jsonReader : {
				root : "rows",
				total : "totalPages",
				records : function(obj) {
					$('#rowCnt').text(obj.totalRows);
					return obj.totalRows;
				}
			},
			onSelectRow : function(row) {
				var rowData = $('#grid-fclt').getRowData(row);
				oFclt.viewDeatil(rowData.fcltId);
				if (rowData.hasOwnProperty('pointX') && rowData.hasOwnProperty('pointY')) {
					var nPointX = parseFloat(rowData.pointX);
					var nPointY = parseFloat(rowData.pointY);
					if (!isNaN(nPointX) && !isNaN(nPointY)) {
						if (oGis.olVersion == 5) {
							olSwipMap.point.set([
									rowData.pointX, rowData.pointY
							], oGis.projection, 'FCLT', true, false, true);
						} else {
							var oPoint = new OpenLayers.Geometry.Point(rowData.pointX, rowData.pointY);
							removePreviousFeatureselected();
							previousFeatureselected = featureselected(oPoint, 'fclt', '', false, true, false);
							var oLonLat = new OpenLayers.LonLat(rowData.pointX, rowData.pointY);
							_map.setCenter(oLonLat);
							_map.zoomToScale(oConfigure.gisLabelViewScale, true);
						}
					} else {
						alert("해당 시설물의 위치 정보가 없습니다.");
					}

				} else {
					alert("해당 시설물의 위치 정보가 없습니다.");
				}
			},
			cmTemplate : {
				sortable : false
			}
		});

		this.getGridParams = function() {
			var searchFcltKndCd = $('#searchFcltKndCd').val();
			var searchFcltUsedTyCd = $('#searchFcltUsedTyCd option:selected').val();
			var searchFcltId = $('#searchFcltId').val();
			var searchFcltSttus = $('#searchFcltSttus option:selected').val();
			var searchPlcPtrDiv = $('#searchPlcPtrDiv option:selected').val();
			var searchAreaCd = $('#searchAreaCd option:selected').val();
			var searchIncludeMissingPlcPtrDivYn = $('#searchIncludeMissingPlcPtrDiv').prop('checked') ? 'Y' : 'N';
			var searchLprOnlyYn = $('#searchLprOnly').prop('checked') ? 'Y' : 'N';

			var params = {
				searchKeyword : '',
				searchFcltKndCd : searchFcltKndCd,
				searchFcltUsedTyCd : searchFcltUsedTyCd,
				searchFcltId : searchFcltId,
				searchFcltSttus : searchFcltSttus,
				searchPlcPtrDiv : searchPlcPtrDiv,
				searchAreaCd : searchAreaCd,
				searchIncludeMissingPlcPtrDivYn : searchIncludeMissingPlcPtrDivYn,
				searchIncludeFcltUsedTyCdYn : 'N',
				searchLprOnlyYn : searchLprOnlyYn,
				radius : 0
			};
			return params;
		};

		this.reloadGrid = function() {
			var oParamsGrid = oFclt.getGridParams();
			oCommon.jqGrid.reload('fclt', 1, oParamsGrid);

			if (oGis.olVersion == 5) {
				var isCluster = olSwipMap.layers.fclt.cluster;
				if ($('#includeResultToMap').is(':checked')) {
					oParamsGrid.bbox = function() {
						return olMap.map.getView().calculateExtent(olMap.map.getSize());
					};
					oParamsGrid.scale = function() {
						return Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
					};

					if (isCluster) {
						oParamsGrid.searchIgnoreScaleYn = 'Y';
					} else {
						oParamsGrid.searchIgnoreScaleYn = 'N';
					}

					olSwipMap.layers.fclt.data = oParamsGrid;
				} else {
					olSwipMap.layers.fclt.data = {
						bbox : function() {
							return olMap.map.getView().calculateExtent(olMap.map.getSize());
						},
						scale : function() {
							return Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
						}
					};

					if (isCluster) {
						olSwipMap.layers.fclt.data.searchIgnoreScaleYn = 'Y';
					} else {
						olSwipMap.layers.fclt.data.searchIgnoreScaleYn = 'N';
					}
				}
				olSwipMap.layers.fclt.source.vector.clear();
			} else {
				if ($('#includeResultToMap').is(':checked')) {
					oFcltLayer.protocol.params.searchFcltKndCd = oParamsGrid.searchFcltKndCd;
					oFcltLayer.protocol.params.searchFcltUsedTyCd = oParamsGrid.searchFcltUsedTyCd;
					oFcltLayer.protocol.params.searchFcltId = oParamsGrid.searchFcltId;
					oFcltLayer.protocol.params.searchFcltSttus = oParamsGrid.searchFcltSttus;
					oFcltLayer.protocol.params.searchPlcPtrDiv = oParamsGrid.searchPlcPtrDiv;
					oFcltLayer.protocol.params.searchAreaCd = oParamsGrid.searchAreaCd;
					oFcltLayer.protocol.params.searchIncludeFcltUsedTyCdYn = oParamsGrid.searchIncludeFcltUsedTyCdYn;
					oFcltLayer.protocol.params.searchIncludeMissingPlcPtrDivYn = oParamsGrid.searchIncludeMissingPlcPtrDivYn;
					oFcltLayer.protocol.params.searchLprOnlyYn = oParamsGrid.searchLprOnlyYn;
					oFcltLayer.protocol.params.radius = oParamsGrid.radius;

					oFcltLayer.refresh({
						force : true,
						params : oFcltLayer
					});
				} else {
					oFcltLayer.protocol.params.searchFcltKndCd = '';
					oFcltLayer.protocol.params.searchFcltUsedTyCd = '';
					oFcltLayer.protocol.params.searchFcltId = '';
					oFcltLayer.protocol.params.searchFcltSttus = '';
					oFcltLayer.protocol.params.searchPlcPtrDiv = '';
					oFcltLayer.protocol.params.searchAreaCd = '';
					oFcltLayer.protocol.params.searchIncludeFcltUsedTyCdYn = 'Y';
					oFcltLayer.protocol.params.searchIncludeMissingPlcPtrDivYn = '';
					oFcltLayer.protocol.params.searchLprOnlyYn = '';
					oFcltLayer.protocol.params.radius = 0;
					oFcltLayer.refresh({
						force : true,
						params : {
							searchIncludeFcltUsedTyCdYn : 'Y',
							radius : 0
						}
					});
				}
			}
		};

		this.retrieveCctvInfoFromBase = function() {
			if(confirm("카메라정보를 기초에서 가져오시겠습니까?") == false) return false;
			
			var params = "";
			$.ajaxEx($('#grid-fclt'), {
				type : "POST",
				url : contextRoot + "/link/cctvlist/test.xx",
				dataType : "json",
				data: params,
				success:function(data){
					alert(data.responseMsg);
				},
				error:function(e){
					alert(data.responseMsg);
				}
			});
		};

		this.retrieveCctvStateFromBase = function() {
			if(confirm("카메라상태정보를 기초에서 가져오시겠습니까?") == false) return false;
			
			var params = "";
			$.ajaxEx($('#grid-fclt'), {
				type : "POST",
				url : contextRoot + "/link/cctvstate/test.xx",
				dataType : "json",
				data: params,
				success:function(data){
					alert(data.responseMsg);
				},
				error:function(e){
					alert(data.responseMsg);
				}
			});
		};

		this.reloadGridRadius = function(lon, lat, radius) {
			var oParams = oFclt.getGridParams();

			if (typeof radius != 'undefined' && radius != 0) {
				oParams.lon = lon;
				oParams.lat = lat;
				oParams.radius = radius;
			} else {
				oParams.radius = 0;
			}

			gridReload('fclt', 1, oParams);

			if ($('#includeResultToMap').is(':checked')) {
				if (oGis.olVersion == 5) {
					var isCluster = olSwipMap.layers.fclt.cluster;
					if ($('#includeResultToMap').is(':checked')) {
						oParams.bbox = function() {
							return olMap.map.getView().calculateExtent(olMap.map.getSize());
						};
						oParams.scale = function() {
							return Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
						};
						oParams.lon = lon;
						oParams.lat = lat;
						oParams.radius = radius;
						if (isCluster) {
							oParams.searchIgnoreScaleYn = 'Y';
						} else {
							oParams.searchIgnoreScaleYn = 'N';
						}
						olSwipMap.layers.fclt.data = oParams;
					}
					olSwipMap.layers.fclt.source.vector.clear();
				} else {
					if (typeof radius != 'undefined' && radius != 0) {
						// var oWgs84 = convertToWGS84(lon, lat);
						oFcltLayer.protocol.params.lon = lon;
						oFcltLayer.protocol.params.lat = lat;
						oFcltLayer.protocol.params.radius = radius;
						oParams.lon = lon;
						oParams.lat = lat;
						oParams.radius = radius;
					} else {
						oFcltLayer.protocol.params.radius = 0;
						oParams.radius = 0;
					}
					oFcltLayer.protocol.params.searchFcltKndCd = oParams.searchFcltKndCd;
					oFcltLayer.protocol.params.searchFcltUsedTyCd = oParams.searchFcltUsedTyCd;
					oFcltLayer.protocol.params.searchFcltId = oParams.searchFcltId;
					oFcltLayer.protocol.params.searchFcltSttus = oParams.searchFcltSttus;
					oFcltLayer.protocol.params.searchPlcPtrDiv = oParams.searchPlcPtrDiv;
					oFcltLayer.protocol.params.searchAreaCd = oParams.searchAreaCd;
					oFcltLayer.protocol.params.searchIncludeMissingPlcPtrDivYn = oParams.searchIncludeMissingPlcPtrDivYn;
					oFcltLayer.protocol.params.searchIncludeFcltUsedTyCdYn = oParams.searchIncludeFcltUsedTyCdYn;
					oFcltLayer.protocol.params.searchLprOnlyYn = oParams.searchLprOnlyYn;

					oFcltLayer.refresh({
						force : true,
						params : oParams
					});
				}
			}
		};

		/* 반경 검색 설정 */
		this.setRadius = function() {
			$('#setRadius').popover('show');
			setTimeout(function() {
				$('#setRadius').popover('hide');
			}, 3000);

			if (oGis.olVersion == 5) {
				if (oFclt.draw == null) {
					oFclt.draw = new ol.interaction.Draw({
						source : olMap.layers.vector.getSource(),
						type : 'Circle'
					});

					oFclt.draw.on('drawend', function(event) {
						var olGeometry = event.feature.getGeometry();
						var oFirstCoordinate = ol.proj.transform(olGeometry.getFirstCoordinate(), oGis.projection, 'EPSG:4326');
						var oLastCoordinate = ol.proj.transform(olGeometry.getLastCoordinate(), oGis.projection, 'EPSG:4326');
						var nDistance = olSwipMap.util.getDistance(oFirstCoordinate, oLastCoordinate);
						$('#inputRadius').val('반경 ' + nDistance.toFixed(0) + ' 미터');
						oFclt.reloadGridRadius(olGeometry.getFirstCoordinate()[0], olGeometry.getFirstCoordinate()[1], nDistance);
						$('.searchBox.sm input').prop('disabled', true);
						$('.searchBox.sm select').prop('disabled', true);
						$('#sendBtn').prop('disabled', true);
						$('#setRadius').prop('checked', false);
						$('#unSetRadius').prop('disabled', false);
						olMap.map.removeInteraction(oFclt.draw);
					}, this);
				}

				oFclt.reloadGrid();
				olMap.layers.vector.getSource().clear();
				olMap.map.addInteraction(oFclt.draw);
			} else {
				oPolygonControl.activate();
			}
		};

		/* 반경 검색 해제 */
		this.unsetRadius = function() {
			$('#unSetRadius').popover('show');
			setTimeout(function() {
				$('#unSetRadius').popover('hide');
			}, 3000);

			if (oGis.olVersion == 5) {
				olMap.layers.vector.getSource().clear();
			} else {
				if (oAddedFeature != null) {
					oPolygonLayer.removeFeatures([
						oPolygonLayer.getFeatureById(oAddedFeature)
					]);
				}
				oAddedFeature = null;
			}

			$('#inputRadius').val('');

			$('.searchBox.sm input').prop('disabled', false);
			$('.searchBox.sm select').prop('disabled', false);
			$('#sendBtn').prop('disabled', false);

			oFclt.reloadGrid();
		};

		this.viewDeatil = function(fcltId) {
			if (oVms.webRtcYn == 'Y') {
				if (typeof oFcltInfo != 'undefined' && oFcltInfo.hasOwnProperty('sessionId')) {
					oVmsService.disconnect(oFcltInfo.sessionId);
					oFcltInfo = undefined;
				}
			}

			$.post(contextRoot + '/mntr/fcltById.json', {
				fcltId : fcltId
			}).done(function(data) {
				var $cols = $('#article-bottom div.col');
				oFcltInfo = data;

				if ('VMS' == oFcltInfo.viewerTyCd) {
					var fnCallBack = function() {
						var $cols = $('#article-bottom div.col');
						$($cols.get(0)).show();
						if (oVms.webRtcYn == 'Y') {
							setTimeout(function() {
								console.log("=== $('#web-rtc-view').width() => "+$('#web-rtc-view').width());
								var nWidth = $('#web-rtc-view').width() - 15;
								var nHeight = parseInt(oConfigure.mntrViewBottom) - 6;
								var $container = oVmsService.createVideoElement(oFcltInfo, {
									'width' : nWidth,
									'height' : nHeight
								});

								var elVideo = $container.find('video')[0];
								oFcltInfo.sessionId = $(elVideo).data('sessionId');
								$('#web-rtc-view').html($container);
								oVmsCommon.play(elVideo);
							}, 500);
						} else {
							oVmsCommon.play(oFcltInfo);
						}
					};
					appendDiv(new div('DIV-VMS', 'B', 0), '', fcltId, fnCallBack);
				} else {
					if (oVmsService.isPlay) {
						oVmsService.stop();
					}
					$($cols.get(0)).hide();
				}

				appendDiv(new div('DIV-FCLT', 'B', 1), '', fcltId);
				appendDiv(new div('DIV-DETAIL', 'B', 2), '', fcltId);

				var isVisible = $('#bottom').is(':visible');
				if (!isVisible) {
					collapse({
						bottom : false
					});
				}
			});
		};

		this.detail = function(fcltId) {
			$('<form/>', {
				'action' : contextRoot + '/mntr/fcltDetail.do'
			}).append($('<input/>', {
				'type' : 'hidden',
				'id' : 'fcltId',
				'name' : 'fcltId',
				'value' : fcltId
			})).appendTo(document.body).submit();

		};

		this.reg = function() {
			$('<form/>', {
				'action' : contextRoot + '/mntr/fcltReg.do'
			}).appendTo(document.body).submit();
		};

		this.uploadExcel = function() {
			$('<form/>', {
				'action' : contextRoot + '/mntr/fcltExcelUpload.do'
			}).appendTo(document.body).submit();
		};

		this.downloadExcel = function() {
			BootstrapDialog.show({
				type : BootstrapDialog.TYPE_INFO,
				cssClass : 'popupDisplay',
				title : '엑셀 다운로드',
				message : $('<div id="displayOption"></div>').load(contextRoot + '/mntr/fcltExcelPopup.do')
			});
		};

		this.checkFcltData = function() {
			var params = {
				searchKeyword : 'CheckList'
			};
			gridReload('fclt', 1, params);
		};
	};
}
