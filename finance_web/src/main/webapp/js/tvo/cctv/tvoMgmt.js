function cctvMgmt() {
	this.init = function () {
		// LEFT 조절
		oConfigure.mntrViewLeft = 500;
		oConfigure.mntrViewRight = 500;
		$('aside#left').css('width', oConfigure.mntrViewLeft);
		$('aside#right').css('width', oConfigure.mntrViewRight);
		$('section#body').css('left', oConfigure.mntrViewLeft + 10);
		$('section#body').css('right', oConfigure.mntrViewRight);
		$('#toggleLeft').css('left', oConfigure.mntrViewLeft);
		$('#toggleLeft').css('right', oConfigure.mntrViewRight);

		collapse({
			left: false,
			right: false,
		});

		map.init();
		cctv.init();
	}

	this.mntr = {
		// oCctvMgmt.mntr.stop()
		stop: function () {
			oCurrentEvent.clear();

			const olSourceVector = olMap.layers.vector.getSource();
			olSourceVector.clear();

			olSwipMap.point.clearAll();
			olSwipMap.mntr.clear();

			oVmsService.selected = null;
			oVmsService.disconnectPlaylist();

			if ($('#div-gnr-playlist').length) $('#div-gnr-playlist').parent().empty();

			collapse({
				left: false,
				right: false,
				bottom: true,
			});
		}
	};


	const cctv = {
		init: function () {
			fcltUsedTyList('.sel-fclt-used-ty-cd', 'CTV', '');
			this.grid.init('cctv-list');
			this.grid.init('trgt-cctv-list');

			$('.txt-keyword').on('keypress', (event) => {
				if (event.which == 13) {
					const sGridId = $(event.target).data('gridId');
					cctv.grid.search(sGridId);
				}
			});

			$('.btn-search').on('click', (event) => {
				const sGridId = $(event.target).data('gridId');
				cctv.grid.search(sGridId);
			});

			$('#btn-send').on('click', cctv.send);
		},
		grid: {
			init: function (id) {
				const oParams = this.params(id);
				const oColNames = ['CCTV명', '용도명', '구분', '상태', 'fcltId', 'ivaTy', '이동'];
				const oColModel = [
					{
						name: 'fcltLblNm',
						classes: 'text-ellipsis',
					}, {
						name: 'fcltUsedTyNm',
						align: 'center',
						width: 85,
					}, {
						name: 'fcltKndDtlNm',
						align: 'center',
						width: 45,
					}, {
						name: 'fcltSttusNm',
						align: 'center',
						width: 45,
					}, {
						name: 'fcltId',
						hidden: true,
					}, {
						name: 'ivaTy',
						hidden: true,
					}, {
						name: 'add',
						align: 'center',
						formatter: function (cellvalue, options, rowObject) {
							const $btn = $('<button/>', {
								'type': 'button',
								'class': 'btn btn-default btn-xs',
								'title': '이동',
								'html': '<i class="fas fa-map-pin"></i>',
								'data-point-x': rowObject.pointX,
								'data-point-y': rowObject.pointY,
								'onclick': 'javascript:olMap.setCenter([' + rowObject.pointX + ' ,' + rowObject.pointY + ']);',
							});
							return $btn.prop('outerHTML');
						},
						width: 50,
					}

				];

				if (id == 'cctv-list') {
					//oColNames.push('추가');
					oColNames.push('제외');
					oColModel.push({
						name: 'add',
						align: 'center',
						formatter: function (cellvalue, options, rowObject) {
							//if (rowObject.ivaTy.length != 5) rowObject.ivaTy = '00000';
							const $btn = $('<button/>', {
								'type': 'button',
								'class': 'btn btn-default btn-xs btn-update',
								//'title': '추가',
								//'html': '<i class="fas fa-plus"></i>',
								'title': '제외',
								'html': '<i class="fas fa-minus"></i>',
								'data-fclt-id': rowObject.fcltId,
								'data-iva-ty': rowObject.ivaTy,
								//'data-operation': 'I',
								'data-operation': 'D',
								'data-source': 'GRID',
							});
							return $btn.prop('outerHTML');
						},
						width: 50,
					});
				} else if (id == 'trgt-cctv-list') {
					oColNames.push('제외');
					//oColNames.push('추가');
					oColModel.push({
						name: 'add',
						align: 'center',
						formatter: function (cellvalue, options, rowObject) {
							//if (rowObject.ivaTy.length != 5) rowObject.ivaTy = '00000';
							const $btn = $('<button/>', {
								'type': 'button',
								'class': 'btn btn-default btn-xs btn-update',
								'title': '제외',
								'html': '<i class="fas fa-minus"></i>',
								//'title': '추가',
								//'html': '<i class="fas fa-plus"></i>',
								'data-fclt-id': rowObject.fcltId,
								'data-iva-ty': rowObject.ivaTy,
								//'data-operation': 'D',
								'data-operation': 'I',
								'data-source': 'GRID',
							});
							return $btn.prop('outerHTML');
						},
						width: 50,
					});
				} else {
					return false;
				}

				if (!$.isEmptyObject(oParams)) {
					$('#grid-' + id).jqGrid({
						url:contextRoot + '/tvo/cctv/cctvMgmt/list.json',
						datatype: 'json',
						mtype: 'POST',
						height: 'auto',
						autowidth: true,
						rowNum: 15,
						postData: oParams,
						colNames: oColNames,
						colModel: oColModel,
						jsonReader: {
							root: 'rows',
							total: 'totalPages',
							records: function (obj) {
								$('#rowCnt').text(obj.totalRows);
								return obj.totalRows;
							}
						},
						cmTemplate: {sortable: false},
						loadComplete: function (data) {

							oCommon.jqGrid.loadComplete(id, data, oParams);
							$('.btn-update').off('click');
							$('.btn-update').on('click', cctv.update);
							
							if (id == 'cctv-list') {
								if (data.rows.length) {
									//const sHtml = '전체건수: ' + data.rows[0].rowcnt + '건, 상시건수 ' + data.rows[0].cntAlwaysY + '건';
									const sHtml = '전체건수: ' + data.rows[0].rowcnt + '건';
									$('#title-cctv-list').html(sHtml);
								} else {
									$('#title-cctv-list').empty();
								}
								
							} else if (id == 'trgt-cctv-list') {
								if (data.rows.length) {
									//const sHtml = '전체건수: ' + data.rows[0].rowcnt + '건, 상시건수 ' + data.rows[0].cntAlwaysY + '건';
									const sHtml = '전체건수: ' + data.rows[0].rowcnt + '건';
									$('#title-trgt-cctv-list').html(sHtml);
								} else {
									$('#title-trgt-cctv-list').empty();
								}
							}
							oCommon.jqGrid.gridComplete(this);
						},
					});
				}
			},
			params: function (id) {
				if (typeof id === 'undefined') return {};
				const oParams = {
					searchKeyword: $('#panel-' + id + ' input.txt-keyword').val(),
					searchFcltUsedTyCd: $('#panel-' + id + ' select.sel-fclt-used-ty-cd option:selected').val(),
					ivaTy: '999',
				};
				if (id === 'cctv-list') {
					oParams.recordingYn = 'Y';
					//oParams.tvoTrgtYn = 'N';
					oParams.tvoTrgtYn = 'Y';
				} else if (id === 'trgt-cctv-list') {
					oParams.recordingYn = 'Y';
					//oParams.tvoTrgtYn = 'Y';
					oParams.tvoTrgtYn = 'N';
				}
				return oParams;
			},
			search: function (id) {
				const oParams = this.params(id);
				console.log("=== oParams => %o",oParams);
				if (!$.isEmptyObject(oParams)) oCommon.jqGrid.reload(id, 1, this.params(id));
			}
		},
		update: function (event) {
			let $btn;
			if (event.target.tagName == 'I') {
				$btn = $($(event.target).parent());
			} else {
				$btn = $(event.target);
			}
			const oData = $btn.data();
			if (typeof oData.ivaTy === 'number') oData.ivaTy = oData.ivaTy.toString();
			
			if (oData.operation == 'I') {
				//oData.ivaTy = oData.ivaTy.replaceAt(2, '1');
				oData.recordingYn = 'Y';
				oData.tvoTrgtYn = 'Y';
			//} else if (oData.operation == 'U') {
			//	let sPrefix = '1';
			//	if (oData.ivaTy.charAt(2) == '1') sPrefix = '2';
			//	oData.ivaTy = oData.ivaTy.replaceAt(2, sPrefix);
			} else if (oData.operation == 'D') {
				//oData.ivaTy = oData.ivaTy.replaceAt(2, '0');
				oData.recordingYn = 'Y';
				oData.tvoTrgtYn = 'N';
			}

			$.ajax({
				type: 'POST',
				url:contextRoot + '/tvo/cctv/cctvMgmt/update.json',
				dataType: 'json',
				data: {
					fcltId: oData.fcltId,
					recordingYn: oData.recordingYn,
					tvoTrgtYn: oData.tvoTrgtYn
				},
				success: function (data) {
					if (data.session == '1') {
						if (oData.operation == 'U') {
							let sHtml = '<i class="far fa-square"></i>';
							if (oData.ivaTy.charAt(2) == '2') sHtml = '<i class="fas fa-check-square"></i>';
							$btn.html(sHtml);
						} else {
							let sGridId = 'cctv-list';
							// let nPage = $('#pagination-' + sGridId).data('page');
							oCommon.jqGrid.reload(sGridId, 1, cctv.grid.params(sGridId));
							sGridId = 'trgt-cctv-list';
							oCommon.jqGrid.reload(sGridId, 1, cctv.grid.params(sGridId));
						}

						const oFeatures = olMap.layers.vector.getSource().getFeatures();
						for (feature of oFeatures) {
							if (feature.getId() && (feature.getId().startsWith('CF_') || feature.getId().startsWith('PF_'))) olMap.layers.vector.getSource().removeFeature(feature);
						}
						olSwipMap.layers.fclt.event.moveend();

						if (oData.source == 'MAP') {
							$btn.data('ivaTy', oData.ivaTy);
							console.log('>>>>> Updated. %s', oData.ivaTy);
							if (oData.operation == 'I') {
								$btn.data('operation', 'D');
								$btn.html('<i class="fas fa-minus"></i>');
								const $btnU = $btn.closest('td').find('button[data-operation=U]');
								$btnU.data('ivaTy', oData.ivaTy);
								$btnU.show();
							} else if (oData.operation == 'U') {
								let sHtml = '<i class="far fa-square"></i>';
								if (oData.ivaTy.charAt(2) == '2') sHtml = '<i class="fas fa-check-square"></i>';
								$btn.html(sHtml);
								let $btnGrid = $('button[data-fclt-id="' + oData.fcltId + '"][data-source="GRID"][data-operation="U"]');
								if ($btnGrid.length) {
									$btnGrid.attr('data-iva-ty', oData.ivaTy);
									$btnGrid.html(sHtml);
								}
							} else if (oData.operation == 'D') {
								$btn.data('operation', 'I');
								$btn.html('<i class="fas fa-plus"></i>');
								const $btnU = $btn.closest('td').find('button[data-operation=U]');
								$btnU.data('ivaTy', oData.ivaTy);
								$btnU.hide();
							}
						} else {
							$('#ol-overlay-click').hide();
							$('#ol-overlay-click tbody').empty();
							olSwipMap.overlays.click.setPosition(undefined);
						}
					}
				},
				error: function (xhr, status, error) {
					console.log(xhr, status, error);
				}
			});
		},
		/*
		send: function () {
			if (confirm('영상분석시스템으로 전송하시겠습니까?')) {
				$.ajax({
					type: 'POST',
					url:contextRoot + '/tvo/cctv/cctvMgmt/send.json',
					dataType: 'json',
					data: {},
					success: function (data) {
						cctv.grid.search('trgt-cctv-list')
						oCommon.modalAlert('modal-notice', '알림', data.msg, undefined);
					},
					error: function (xhr, status, error) {
						console.log(xhr, status, error);
					}
				});
			}
		},
		*/
	}

	const map = {
		init: function () {
			// 1. BASE, SATELLITE 배경지도 생성.
			olSwipMap.init();
			// 2. 시설물 레이어 설정.
			olSwipMap.layers.fclt.init({
				fcltKndCd: 'CTV',
				style: {
					vector: function (feature) {
						let oProperties = feature.getProperties();
						let nIconSize = 0;
						let sStatus = oProperties.fcltSttus;
						let sFcltUsedTyCd = oProperties.fcltUsedTyCd;
						let sKndDtlCd = oProperties.fcltKndDtlCd;
						let sPrefix = 'fclt/typeA/';
						let sSuffix = '.png';

						// ICON TYPE
						if (oConfigure.iconTy == 'A') {
							if (oProperties.fcltKndCd == 'CTV') {
								if (sKndDtlCd == 'RT') {
									sSuffix = '_RT.png';
									nIconSize = 50;
								} else {
									nIconSize = 50;
								}
							} else {
								nIconSize = 40;
							}
						} else if (oConfigure.iconTy == 'B') {
							sPrefix = 'fclt/typeB/';
							if (oProperties.fcltKndCd == 'CTV') {
								if (sKndDtlCd == 'RT') {
									sSuffix = '_RT.png';
								}
								nIconSize = 74;
							} else {
								nIconSize = 50;
							}
						}

						let nIconScale = parseFloat(oConfigure.iconSize) / nIconSize;
						nIconScale = nIconScale.toFixed(2);

						let nGisLabelViewScale = parseInt(oConfigure.gisLabelViewScale);
						let nViewScale = Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
						// 방향각
						if (oProperties.cctvOsvtX && oProperties.cctvOsvtY && oProperties.fcltKndDtlCd == 'FT') {
							let nAg = parseInt(oProperties.cctvOsvtAg);
							if (nAg < 0) {
								nAg = nAg + 360;
							}

							nAg += 225;

							let olSource = olMap.layers.angle.getSource();

							let oCoordinate = [
								oProperties.pointX, oProperties.pointY
							];

							let sProjectionCode = olMap.map.getView().getProjection().getCode();
							oCoordinate = ol.proj.transform(oCoordinate, 'EPSG:4326', sProjectionCode);

							let olFeature = new ol.Feature({
								geometry: new ol.geom.Point(oCoordinate),
							});
							olFeature.setStyle(new ol.style.Style({
								image: new ol.style.Icon({
									anchor: [
										0, 0
									],
									anchorXUnits: 'pixels',
									anchorYUnits: 'pixels',
									scale: 0.25,
									opacity: 1.0,
									rotation: nAg * (Math.PI / 180),
									src: contextRoot + '/images/mntr/gis/etc/type' + oConfigure.iconTy + '/ANGLE.png'
								}),
							}));
							olFeature.setId('AG_' + oProperties.fcltId);
							olFeature.setProperties(oProperties);
							olSource.addFeature(olFeature);
						}
						let nAnchor = nIconSize / 2;

						let olText = null;
						let $input = $('input[type=checkbox][data-layer-grp-id=ETC][data-layer-id=FCLTLBLNM]');
						if ($input.is(':checked')) {
							olText = new ol.style.Text({
								font: 'bold 11px NanumGothic',
								text: (nViewScale < nGisLabelViewScale) ? oProperties.fcltLblNm : '',
								offsetY: 40,
								fill: new ol.style.Fill({
									color: 'rgba(0, 0, 0, 0.8)'
								}),
								stroke: new ol.style.Stroke({
									color: 'rgba(251, 250, 170, 0.8)',
									width: 3
								}),
							});
						}

						let nGisFeatureViewScale = parseInt(oConfigure.gisFeatureViewScale);
						if (nViewScale < nGisFeatureViewScale) {
							let sIvaTy = oProperties.ivaTy;
							oProperties.type = 'circle';
							if (typeof sIvaTy != 'undefined' && sIvaTy.length == 5) {
								let nIvaTy = parseInt(sIvaTy.charAt(2));
								if (nIvaTy == 1) {
									olSwipMap.draw.circle([parseFloat(oProperties.pointX), parseFloat(oProperties.pointY)], 'EPSG:4326', 10, 0, null, new ol.style.Style({
										stroke: new ol.style.Stroke({
											color: '#d9534f',
											width: 2,
											lineDash: [
												8, 8
											]
										})
									}), oProperties);
								} else if (nIvaTy == 2) {
									olSwipMap.draw.circle([parseFloat(oProperties.pointX), parseFloat(oProperties.pointY)], 'EPSG:4326', 10, 0, null, new ol.style.Style({
										stroke: new ol.style.Stroke({
											color: '#d9534f',
											width: 2
										})
									}), oProperties);
								}
							}
						} else {
							const oFeatures = olMap.layers.vector.getSource().getFeatures();
							if (oFeatures.length) {
								for (feature of oFeatures) {
									if (feature.getId() && (feature.getId().startsWith('CF_') || feature.getId().startsWith('PF_'))) olMap.layers.vector.getSource().removeFeature(feature);
								}
							}
						}

						return new ol.style.Style({
							image: new ol.style.Icon(({
								anchor: [
									nAnchor, nAnchor
								],
								anchorXUnits: 'pixels',
								anchorYUnits: 'pixels',
								size: [
									nIconSize, nIconSize
								],
								scale: nIconScale,
								opacity: 0.8,
								src: contextRoot + '/images/mntr/gis/' + sPrefix + sFcltUsedTyCd + '_' + 0 + '_' + sStatus + sSuffix,
							})),
							text: olText
						});
					}
				}
			});
			// 3. 드롭다운 메뉴 : 레이어 선택
			olSwipMap.layers.dropdownLayers('#dropdown-layers', 'CTV');
			// 4. Feature Event
			olSwipMap.events.feature.pointermove = this.events.feature.handler;
			olSwipMap.events.feature.click = this.events.feature.handler;
			olSwipMap.listeners.feature.pointermove = olMap.map.on('pointermove', olSwipMap.events.feature.pointermove);
			olSwipMap.listeners.feature.click = olMap.map.on('click', olSwipMap.events.feature.click);
			//  olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.vector);

			$('#th-pointermove-fclt').text('시설물명[방향각]');
			$('#th-click-fclt').text('시설물명[방향각]');
		},
		events: {
			feature: {
				handler: function (event) {
					let oFeaturesFclt = [];
					olMap.map.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
						if (layer != null) {
							if (olMap.layers.fclt === layer) {
								const olFeatures = feature.get('features');
								if (typeof olFeatures == 'undefined') {
									oFeaturesFclt.push(feature.getProperties());
								} else {
									$.each(olFeatures, function (i, f) {
										oFeaturesFclt.push(f.getProperties());
									});
								}
							} else if (olMap.layers.angle === layer) {
								oFeaturesFclt.push(feature.getProperties());
							} else {
								olSwipMap.bookmark.check(event, feature, layer);
							}
						}
					}, {
						hitTolerance: 5
					});

					const olOverlay = olMap.map.getOverlayById('ol-overlay-' + event.type);
					olOverlay.setPosition(event.coordinate);

					if (event.type == 'pointermove') {
						const oPosition = olSwipMap.overlays.click.getPosition();
						if (typeof oPosition != 'undefined') {
							return false;
						}
					}

					const $olOverlay = $('#ol-overlay-' + event.type);

					if (oFeaturesFclt.length) {
						// title
						const elPanelHeadingTitle = $olOverlay.find('.panel-heading-title');
						if (elPanelHeadingTitle.length == 1) {
							const $panelHeadingTitle = $(elPanelHeadingTitle[0]);
							if (oFeaturesFclt.length) {
								$panelHeadingTitle.text('시설물(' + oFeaturesFclt.length + ')');
							}
						}
					}

					if (oFeaturesFclt && oFeaturesFclt.length > 0) {
						oFeaturesFclt.sort(function (a, b) {
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
						const elTbody = $olOverlay.find('#table-' + event.type + '-fclt tbody');
						if (elTbody.length == 1) {
							const $tbody = $(elTbody[0]);
							$tbody.empty();
							const nMaxCntFclt = 30;
							if (oFeaturesFclt.length <= nMaxCntFclt) {
								$.each(oFeaturesFclt, function (index, feature) {
									delete feature.geometry;

									const oAg = olMap.layers.angle.getSource().getFeatureById('AG_' + feature.fcltId);
									let nCctvOsvtAg = NaN;
									if (oAg != null) {
										nCctvOsvtAg = oAg.getProperties().cctvOsvtAg;
										if (nCctvOsvtAg < 0) nCctvOsvtAg = 360 + nCctvOsvtAg;
										nCctvOsvtAg = Math.round(nCctvOsvtAg / 10) * 10;
									}

									let sFcltKndDtlCd = feature.fcltKndDtlCd;
									let sFcltKndDtlNm = '';
									if (sFcltKndDtlCd === 'RT') {
										sFcltKndDtlNm = '<i class="fas fa-sync-alt" title="회전형CCTV" data-toggle="tooltip" data-placement="top"></i> ';
									} else if (sFcltKndDtlCd === 'FT' && !isNaN(nCctvOsvtAg)) {
										sFcltKndDtlNm = `<i class="fas fa-arrow-up" title="고정형CCTV(${nCctvOsvtAg}˚)" style="transform: rotate(${nCctvOsvtAg}deg)" data-toggle="tooltip" data-placement="top"></i> `;
									}

									let sTitle = '<span>' + sFcltKndDtlNm + feature.fcltLblNm + '</span>';

									const $tr = $('<tr/>');
									$tr.append($('<td/>', {
										'class': 'text-ellipsis td-fclt-lbl-nm',
										'title': feature.fcltLblNm,
										'html': sTitle,
										'data-toggle': 'tooltip',
										'data-placement': 'top',
									}));
									$tr.append($('<td/>', {
										'class': 'text-ellipsis td-fclt-knd-nm',
										'title': `${feature.fcltKndNm}(${feature.fcltUsedTyNm}:${feature.sysCd})`,
										'text': `${feature.fcltKndNm}(${feature.fcltUsedTyNm}:${feature.sysCd})`,
										'data-toggle': 'tooltip',
										'data-placement': 'top',
									}));

									const $fcltSttus = $('<span/>', {
										'text': feature.fcltSttusNm
									});

									if (feature.fcltSttus == '0') {
										$fcltSttus.css('color', 'green');
									} else if (feature.fcltSttus == '1') {
										$fcltSttus.css('color', 'red');
									} else {
										$fcltSttus.css('color', 'orange');
									}
									$tr.append($('<td/>', {
										'html': $fcltSttus
									}));
									if (event.type == 'click') {
										if (feature.fcltKndCd == 'CTV' && feature.viewerTyCd == 'VMS') {
											const $tdReq = $('<td/>');
											/*
											const $btnCastnet = $('<button/>', {
												'type': 'button',
												'class': 'btn btn-default btn-xs btn-castnet',
												'onclick': 'javascript:olSwipMap.mntr.castnet(this);',
												'title': '지점에서 가장 가까운 주 CCTV들의 영상을 재생합니다.',
												'text': '투망',
												'data-toggle': 'tooltip',
												'data-placement': 'left',
											});
											$btnCastnet.data(feature);
											$btnCastnet.data('projection', 'EPSG:4326');
											*/

											const $btnPole = $('<button/>', {
												'type': 'button',
												'class': 'btn btn-default btn-xs btn-pole',
												'onclick': 'javascript:oVmsCommon.group("' + feature.fcltId + '", "N");',
												'title': '폴에 설치된 CCTV들의 영상을 재생합니다.',
												'text': '폴',
												'data-toggle': 'tooltip',
												'data-placement': 'left',
											});

											const $btnPopover = $('<button/>', {
												'type': 'button',
												'class': 'btn btn-default btn-xs btn-popover',
												'onclick': 'javascript:olSwipMap.mntr.popover.open(this);',
												'title': '해당 CCTV 영상을 팝오버 형태로 띄워 재생합니다.',
												'text': '팝오버',
												'data-toggle': 'tooltip',
												'data-placement': 'left',
											});

											$btnPopover.data(feature);
											$btnPopover.data('projection', 'EPSG:4326');

											const $btnNewWindow = $('<button/>', {
												'type': 'button',
												'class': 'btn btn-default btn-xs btn-new-window',
												'onclick': 'javascript:oVmsCommon.openVmsPlayer("' + feature.fcltId + '");',
												'title': '해당 CCTV 영상을 새창으로 분리해 큰화면으로 재생합니다.',
												'text': '새창',
												'data-toggle': 'tooltip',
												'data-placement': 'left',
											});

											if (oConfigure.ptzCntrTy != 'NO') {
												//$tdReq.append($btnCastnet);
												$tdReq.append($btnPole);
												$tdReq.append($btnPopover);
												$tdReq.append($btnNewWindow);
											}

											if (feature.hasOwnProperty('ivaTy') && feature.ivaTy.length == 5) {
												let nIvaTy = parseInt(feature.ivaTy.charAt(2));

												let sHtml = '<i class="far fa-square"></i>';
												if (nIvaTy == 2) sHtml = '<i class="fas fa-check-square"></i>';

												let $btnI = $('<button/>', {
													'type': 'button',
													'class': 'btn btn-default btn-xs btn-update',
													'title': '추가',
													'html': '<i class="fas fa-plus"></i>',
													'data-fclt-id': feature.fcltId,
													'data-iva-ty': feature.ivaTy,
													'data-operation': 'I',
													'data-source': 'MAP',
													'data-toggle': 'tooltip',
													'data-placement': 'left',
												});

												let $btnD = $('<button/>', {
													'type': 'button',
													'class': 'btn btn-default btn-xs btn-update',
													'title': '제외',
													'html': '<i class="fas fa-minus"></i>',
													'data-fclt-id': feature.fcltId,
													'data-iva-ty': feature.ivaTy,
													'data-operation': 'D',
													'data-source': 'MAP',
													'data-toggle': 'tooltip',
													'data-placement': 'left',
												});

												let $btnU = $('<button/>', {
													'type': 'button',
													'class': 'btn btn-default btn-xs btn-update',
													'title': '상시',
													'html': sHtml,
													'data-fclt-id': feature.fcltId,
													'data-iva-ty': feature.ivaTy,
													'data-operation': 'U',
													'data-source': 'MAP',
													'data-toggle': 'tooltip',
													'data-placement': 'left',
												});

												$btnI.on('click', cctv.update);
												$btnD.on('click', cctv.update);
												$btnU.on('click', cctv.update);

												$tdReq.append($btnU);
												if (nIvaTy == 0) {
													$btnU.hide();
													$tdReq.append($btnI);
												} else {
													$tdReq.append($btnD);
												}
											}

											$tr.append($tdReq);
										}
									} else {
										$tr.append($('<td/>'));
									}
									$tbody.append($tr);
								});
							} else {
								const $tr = $('<tr/>');
								$tr.append($('<td/>', {
									'class': 'text-center text-danger',
									'text': '시설물 ' + nMaxCntFclt + '개 초과(지도를 확대하여 사용)',
									'colspan': '4'
								}));
								$tbody.append($tr);
							}
						}
					} else {
						$('#table-' + event.type + '-fclt').hide();
					}
					$('#table-' + event.type + '-event').hide();
					const oPositionClick = olSwipMap.overlays.click.getPosition();
					const oPositionPointermove = olSwipMap.overlays.pointermove.getPosition();
					if (oFeaturesFclt.length == 0) {
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
			},
		}
	}
}
