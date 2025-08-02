$(function() {
	oFcltReg = new fcltReg();
	oFcltReg.init();
});

function fcltReg() {
	this.init = function() {
		// LEFT 조절
		oConfigure.mntrViewLeft = 700;
		$('aside#left').css('width', oConfigure.mntrViewLeft);
		$('section#body').css('left', oConfigure.mntrViewLeft + 10);
		$('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			left : false
		}, function() {
			// 센터코드 설정
			var sCtrCd = $('#ctrCd').data().selected;
			codeInfoList('#ctrCd', 'UCP_ID', sCtrCd, '센터코드');

			// 시설물종류
			var sFcltKndCd = $('#fcltKndCd').data().selected;
			fcltKindInfoList('#fcltKndCd', sFcltKndCd);

			if (sFcltKndCd == '') {
				fcltUsedTyList('#fcltUsedTyCd', '', '');
				codeInfoList('#fcltKndDtlCd', '', '', '기능별유형');
			} else {
				// 시설물 유형
				var sFcltUsedTyCd = $('#fcltUsedTyCd').data().selected;
				var sFcltKndDtlCd = $('#fcltKndDtlCd').data().selected;
				fcltUsedTyList('#fcltUsedTyCd', sFcltKndCd, sFcltUsedTyCd);
				codeInfoList('#fcltKndDtlCd', sFcltKndCd, sFcltKndDtlCd, '기능별유형');
			}

			$("#fcltKndCd").change(function() {
				var sFcltKndCd = $('#fcltKndCd').val();
				fcltUsedTyList('#fcltUsedTyCd', sFcltKndCd, '');
				codeInfoList('#fcltKndDtlCd', sFcltKndCd, '', '기능별유형');
			});

			// 서비스명
			var sSysCd = $('#sysCd').data().selected;
			getSelectboxList('#sysCd', '/mntr/cmm/sysList.json', '', sSysCd, '서비스명');

			// 지구
			var sDstrtCd = $('#dstrtCd').data().selected;
			getSelectboxList('#dstrtCd', '/mntr/cmm/selectDistrictList.json', '', sDstrtCd, '지구');

			// 시군구
			var sSigunguCd = $('#sigunguCd').data().selected;
			getSelectboxList('#sigunguCd', '/mntr/cmm/sigunguList.json', '', sSigunguCd, '시군구');

			// 구역
			/*
			var sAreaCd = $('#areaCd').data().selected;
			oCommon.areaList('#areaCd', sAreaCd, '');
			*/

			// 경찰지구대
			/*
			var sPlcPtrDivCd = $('#plcPtrDivCd').data().selected;
			plcList('#plcPtrDivCd', '', sPlcPtrDivCd, '경찰지구대');
			*/

			// 사용유형
			var sUseTyCd = $('#useTyCd').data().selected;
			codeInfoList('#useTyCd', 'USE_TY', sUseTyCd, '사용유형');

			// 대표상태
			var sFcltSttus = $('#fcltSttus').data().selected;
			codeInfoList('#fcltSttus', 'FCLT_STTUS', sFcltSttus, '대표상태');

			if (oGis.olVersion == 5) {

				// 1. BASE, SATELLITE 배경지도 생성.
				olSwipMap.init({
					dropdown : true,
					bookmark : false
				});

				// 2. 시설물 레이어 설정.
				olSwipMap.layers.fclt.init();

				// 4. Feature 핸들러.
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
									var $tr = $('<tr/>');
									$tr.append($('<td/>', {
										'class' : 'text-ellipsis td-fclt-lbl-nm',
										'title' : v.fcltLblNm,
										'text' : v.fcltLblNm,
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
									$tr.append($('<td/>'));
									$tbody.append($tr);
								});
								$('#table-click-fclt tbody [data-toggle="tooltip"]').tooltip({
									container : 'body'
								});
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
							olSwipMap.point.set(event.coordinate, oGis.projection, 'BLUE', false, false, true);

							var oCoordinate = ol.proj.transform(event.coordinate, oGis.projection, 'EPSG:4326');

							$('#pointX').val(oCoordinate[0].toFixed(6));
							$('#pointY').val(oCoordinate[1].toFixed(6));

							if (oGis.urlWfs != '') {
								var nResolutionForZoom = olMap.view.getResolutionForZoom(olMap.view.getZoom());
								var nMinX = event.coordinate[0] - 0.5 * nResolutionForZoom;
								var nMaxX = event.coordinate[0] + 0.5 * nResolutionForZoom;
								var nMinY = event.coordinate[1] - 0.5 * nResolutionForZoom;
								var nMaxY = event.coordinate[1] + 0.5 * nResolutionForZoom;
								var oBbox = [
										nMinX, nMinY, nMaxX, nMaxY, oGis.projection
								];

								//var sUcpId = oConfigure.ucpId.toLocaleLowerCase();
								var sUcpId = 'com';

								$.ajax({
									type : 'GET',
									dataType : 'json',
									url : oGis.urlWfs,
									async : false,
									data : {
										service : 'WFS',
										version : '1.1.0',
										request : 'GetFeature',
										typename : 'cite:geo_ldreg_' + sUcpId,
										srsname : oGis.projection,
										outputFormat : 'application/json',
										bbox : oBbox.toString()
									},
									success : function(data) {
										if (data.hasOwnProperty('features') && data.features.length) {
											var oFeature = data.features[0];
											if (oFeature.hasOwnProperty('properties') && oFeature.properties.hasOwnProperty('PNU')) {
												var sPnu = oFeature.properties.PNU;
												if (sPnu.length == 19) {
													/*
													var sLgDongCd = sPnu.substring(0, 10);
													var sIsMntn = sPnu.substring(10, 11);
													if (sIsMntn == '1') {
														sIsMntn = '0';
													} else if (sIsMntn == '2') {
														sIsMntn = '1';
													}
													var nJibunMainNo = parseInt(sPnu.substring(11, 15));
													var nJibunSubNo = parseInt(sPnu.substring(15, 19));
													*/
													$.ajax({
														type : 'POST',
														dataType : 'json',
														async : false,
														url : contextRoot + '/mntr/api/addrListByPnu.json',
														data : {
															// lgDongCd : sLgDongCd,
															// isMntn : sIsMntn,
															// jibunMainNo : nJibunMainNo,
															// jibunSubNo : nJibunSubNo,
															pnu : sPnu
														},
														success : function(data) {
															if (data.hasOwnProperty('result') && data.result.hasOwnProperty('addr')) {
																var oAddr = data.result.addr;
																$.each(oAddr, function(index, addr) {
																	/*
																	var sLgEmdNm = addr.lgEmdNm;
																	var sJustnAdmDong = addr.justnAdmDong;
																	var sLgLiNm = addr.lgLiNm == null ? '' : ' ' + addr.lgLiNm;
																	var sIsMntn = addr.isMntn == '1' ? ' 산 ' : ' ';
																	var sJibunMainNo = addr.jibunMainNo;
																	var sJibunSubNo = addr.jibunSubNo == 0 ? '' : '-' + addr.jibunSubNo;
																	var sRoadNm = addr.roadNm;
																	var sIsUndgrnd = addr.isUndgrnd == '1' ? ' 지하 ' : ' ';
																	var sBuldMainNo = addr.buldMainNo;
																	var sBuldSubNo = addr.buldSubNo == 0 ? '' : '-' + addr.buldSubNo
																	*/
																	var sBuldNm = addr.buldNm == null ? '' : ' (' + addr.buldNm + ')';
																	if (!addr.roadAddr) addr.roadAddr = '';
																	/*
																	var sRoad = '';
																	if (sRoadNm) {
																		sRoad = sRoadNm + sIsUndgrnd + sBuldMainNo + sBuldSubNo;
																		if (sLgEmdNm.slice(-1) != '동') {
																			sRoad = sLgEmdNm + ' ' + sRoad;
																		}
																	}
																	var sJibun = '';
																	if (sLgEmdNm) {
																		sJibun = sLgEmdNm + sLgLiNm + sIsMntn + sJibunMainNo + sJibunSubNo;
																	}
																	*/
																	if (index == 0) {
																		// 주소
																		$('#roadAdresNm').val(addr.roadAddr + sBuldNm);
																		$('#lotnoAdresNm').val(addr.jibunAddr);
																		//if (oConfigure.exeEnv == 'DEV')
																		console.log('-- addr => %s',addr);
																	}
																});
															}
														},
														error : function(data, status, err) {
															console.log(data);
														}
													});
												}
											}
										}
									},
									error : function(data, status, err) {
										console.log(data);
									}
								});

							} else {
								$.ajax({
									type : 'POST',
									async : false,
									dataType : 'json',
									url : contextRoot + '/mntr/api/coordToAddr.json',
									data : {
										pointX : oCoordinate[0],
										pointY : oCoordinate[1]
									},
									success : function(data) {
										if (data.result.addr) {
											var oAddr = data.result.addr;
											var sLgEmdNm = oAddr.lgEmdNm;
											var sJustnAdmDong = oAddr.justnAdmDong;
											var sLgLiNm = oAddr.lgLiNm == null ? '' : ' ' + oAddr.lgLiNm;
											var sIsMntn = oAddr.isMntn == '1' ? ' 산 ' : ' ';
											var sJibunMainNo = oAddr.jibunMainNo;
											var sJibunSubNo = oAddr.jibunSubNo == 0 ? '' : '-' + oAddr.jibunSubNo;
											var sRoadNm = oAddr.roadNm;
											var sIsUndgrnd = oAddr.isUndgrnd == '1' ? ' 지하 ' : ' ';
											var sBuldMainNo = oAddr.buldMainNo;
											var sBuldSubNo = oAddr.buldSubNo == 0 ? '' : '-' + oAddr.buldSubNo;
											var sBuldNm = oAddr.buldNm == null ? '' : ' (' + oAddr.buldNm + ')';

											// $('#lblLotnoAdresNm').html('<span class="label label-default">지번</span> ' + sLgEmdNm + sLgLiNm + sJibunMainNo + sJibunSubNo);
											// $('#lblRoadAdresNm').html('<span class="label label-default">도로</span> ' + sRoadNm + ' ' + sBuldMainNo + sBuldSubNo + sBuldNm);

											var sRoad = '';
											if (sRoadNm) {
												sRoad = sRoadNm + sIsUndgrnd + sBuldMainNo + sBuldSubNo + sBuldNm;
												if (sLgEmdNm.slice(-1) != '동') {
													sRoad = sLgEmdNm + ' ' + sRoad;
												}
											}

											var sJibun = '';
											if (sLgEmdNm) {
												sJibun = sLgEmdNm + sLgLiNm + sIsMntn + sJibunMainNo + sJibunSubNo;
											}

											$('#roadAdresNm').val(sRoad);
											$('#lotnoAdresNm').val(sJibun);
											return false;
										} else {
											// $('#lblLotnoAdresNm').html('<span class="label label-default">지번</span>');
											// $('#lblRoadAdresNm').html('<span class="label label-default">도로</span>');
											$('#roadAdresNm').val('');
											$('#lotnoAdresNm').val('');
										}
									},
									error : function(data, status, err) {
										console.log(data);
									}
								});
							}
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

				var sPointX = $('#pointX').val();
				var sPointY = $('#pointY').val();
				if (sPointX != '' && !isNaN(sPointX) && sPointY != '' && !isNaN(sPointY)) {
					olSwipMap.point.set([
							sPointX, sPointY
					], 'EPSG:4326', 'FCLT', true, true, false);
				}

				var nMaxZoom = olMap.map.getView().getMaxZoom();
				olMap.map.getView().setZoom(nMaxZoom);
			} else {
				// 시설물 레이어 추가
				oMntrMap.initFcltLayer('', '', 'N');

				var sPointX = $('#pointX').val();
				var sPointY = $('#pointY').val();
				if (sPointX != '' && sPointY != '') {
					var oFcltPoint = convertByWGS84(sPointX, sPointY);
					var oFcltLonLat = new OpenLayers.LonLat(oFcltPoint.x, oFcltPoint.y);
					featureselected(oFcltPoint, 'fclt', '', false, true, false);
					_map.setCenter(oFcltLonLat);
					_map.zoomToScale(oConfigure.gisLabelViewScale, true);
				}

				// 지도 클릭이벤트
				oClicker = new OpenLayers.Control.Clicker({
					callbackfunc : function(evt) {
						_homeLonLat = _map.getLonLatFromPixel(evt.xy);
						pointLayer.removeFeatures(pointLayer.getFeaturesByAttribute("cls", "homeFeature"));
						var homeFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(_homeLonLat.lon, _homeLonLat.lat), {
							"cls" : "homeFeature"
						}, {
							externalGraphic : contextRoot + "/images/mntr/gis/marker_blue.png",
							graphicWidth : 40,
							graphicHeight : 40
						});
						pointLayer.addFeatures([
							homeFeature
						]);

						oFcltReg.getAddr(homeFeature);
					}
				});
				_map.addControl(oClicker);
				oClicker.activate();
			}
		});
	};

	this.list = function() {
		$('<form/>', {
			'action' : contextRoot + '/mntr/fclt.do'
		}).appendTo(document.body).submit();
	};

	this.save = function() {
		var isConfirmed = confirm('저장하시겠습니까?');

		if (isConfirmed) {
			var isValidated = validateFcltVO(fcltVO);
			var sSerialized = $("#fcltVO").serialize();
			var sFcltId = $('#fcltId').val();
			var sUrl = contextRoot + '/mntr/fcltRegProc.json';
			if (sFcltId) {
				sUrl = contextRoot + '/mntr/fcltUpdProc.json';
			}

			if (isValidated) {
				$.ajax({
					type : "POST",
					url : sUrl,
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					dataType : "json",
					data : sSerialized,
					success : function(data) {
						console.log(data);
						var status = data.status;
						// 정상 저장 확인 후 리스트 혹은 상세페이지 이동
						if (data.status && data.fcltId) {
							alert('등록되었습니다.');
							oFcltReg.detail(data.fcltId);
						} else if (data.status && sFcltId) {
							alert('수정되었습니다.');
							oFcltReg.detail(sFcltId);
						}
					},
					error : function(xhr, status, error) {
						alert("저장에 실패하였습니다.");
					}
				});
			}
		}
	};

	this.detail = function(fcltId) {
		var $form = $('<form/>', {
			'action' : contextRoot + '/mntr/fcltDetail.do'
		});

		var $hidden = $('<input/>', {
			'type' : 'hidden',
			'id' : 'fcltId',
			'name' : 'fcltId',
			'value' : fcltId
		});
		$form.append($hidden);
		$form.appendTo(document.body).submit();
	};

	this.getAddr = function(homeFeature) {
		var oFeature = homeFeature;
		var oWgs84 = convertToWGS84(oFeature.geometry.x, oFeature.geometry.y);

		// $("#lblPointX").text('위도 : ' + oWgs84.x);
		// $("#lblPointY").text('경도 : ' + oWgs84.y);
		$('#pointX').val(oWgs84.x.toFixed(6));
		$('#pointY').val(oWgs84.y.toFixed(6));
		$.ajax({
			type : 'POST',
			async : false,
			dataType : 'json',
			url : contextRoot + '/mntr/api/coordToAddr.json',
			data : {
				pointX : oWgs84.x,
				pointY : oWgs84.y
			},
			success : function(data) {
				if (data.result.addr) {
					var oAddr = data.result.addr;
					var sLgEmdNm = oAddr.lgEmdNm;
					var sLgLiNm = oAddr.lgLiNm == null ? ' ' : ' ' + oAddr.lgLiNm;
					var sJibunMainNo = oAddr.jibunMainNo;
					var sJibunSubNo = oAddr.jibunSubNo == 0 ? '' : '-' + oAddr.jibunSubNo;
					var sRoadNm = oAddr.roadNm;
					var sBuldMainNo = oAddr.buldMainNo;
					var sBuldSubNo = oAddr.buldSubNo == 0 ? '' : '-' + oAddr.buldSubNo
					var sBuldNm = oAddr.buldNm == null ? '' : ' (' + oAddr.buldNm + ')';

					// $('#lblLotnoAdresNm').html('<span class="label label-default">지번</span> ' + sLgEmdNm + sLgLiNm + sJibunMainNo + sJibunSubNo);
					// $('#lblRoadAdresNm').html('<span class="label label-default">도로</span> ' + sRoadNm + ' ' + sBuldMainNo + sBuldSubNo + sBuldNm);

					$('#roadAdresNm').val(sRoadNm + ' ' + sBuldMainNo + sBuldSubNo + sBuldNm);
					$('#lotnoAdresNm').val(sLgEmdNm + sLgLiNm + sJibunMainNo + sJibunSubNo);
					return false;
				} else {
					// $('#lblLotnoAdresNm').html('<span class="label label-default">지번</span>');
					// $('#lblRoadAdresNm').html('<span class="label label-default">도로</span>');
					$('#roadAdresNm').val('');
					$('#lotnoAdresNm').val('');
				}
			},
			error : function(data, status, err) {
				console.log(data);
			}
		});
	};
}
