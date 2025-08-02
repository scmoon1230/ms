$(function() {
	oTvoCmn = new tvoCmn();
});

function tvoCmn() {
	this.div = {
		clear: function (selector) {
			var $document = $(selector);
			if ($document.length) {
				var $col = $document.closest('.col');
				$col.empty();
			}
		}
	};

	this.datetimepicker = {
		ymd: function (selector, minDate, maxDate) {
			$(selector).datetimepicker({
				ignoreReadonly: true,
				format: 'YYYY-MM-DD',
				locale: 'ko',
                sideBySide: true,
                toolbarPlacement: 'top',
                showClose: true,
                showTodayButton: true,
				minDate: minDate,
				maxDate: maxDate
			});

			$(selector).on('dp.show', function (e) {
			/*	var $element = $('.bootstrap-datetimepicker-widget.dropdown-menu');
				var rect = $element.get(0).getBoundingClientRect();
				$('.bootstrap-datetimepicker-widget.dropdown-menu').css({
					'position' : 'fixed',
					'top' : rect.top,
					'left' : rect.left,
					'right' : rect.right,
					'bottom' : rect.bottom,
					'width' : rect.width,
					'height' : rect.height
				});	*/
				const oOffset = $('.bootstrap-datetimepicker-widget.dropdown-menu').offset();
				$('.bootstrap-datetimepicker-widget.dropdown-menu').css({
					position: 'fixed',
					top: oOffset.top,
					left: oOffset.left,
				});
			});
		},
		ymdhm: function (selector, minDate, maxDate) {
			$(selector).datetimepicker({
				ignoreReadonly: true,
				sideBySide: true,
				format: 'YYYY-MM-DD HH:mm',
				locale: 'ko',
                sideBySide: true,
                toolbarPlacement: 'top',
                showClose: true,
                showTodayButton: true,
				minDate: minDate,
				maxDate: maxDate
			});

			$(selector).on('dp.show', function (e) {
			/*	var $element = $('.bootstrap-datetimepicker-widget.dropdown-menu');
				var rect = $element.get(0).getBoundingClientRect();
				$('.bootstrap-datetimepicker-widget.dropdown-menu').css({
					'position' : 'fixed',
					'top' : rect.top,
					'left' : rect.left,
					'right' : rect.right,
					'bottom' : rect.bottom,
					'width' : rect.width,
					'height' : rect.height
				});	*/
				const oOffset = $('.bootstrap-datetimepicker-widget.dropdown-menu').offset();
				$('.bootstrap-datetimepicker-widget.dropdown-menu').css({
					position: 'fixed',
					top: oOffset.top,
					left: oOffset.left,
				});
			});
		},
		ymdhms: function (selector, minDate, maxDate) {
			$(selector).datetimepicker({
				ignoreReadonly: true,
				sideBySide: true,
				format: 'YYYY-MM-DD HH:mm:ss',
				locale: 'ko',
                sideBySide: true,
                toolbarPlacement: 'top',
                showClose: true,
                showTodayButton: true,
				minDate: minDate,
				maxDate: maxDate
			});

			$(selector).on('dp.show', function (e) {
			/*	var $element = $('.bootstrap-datetimepicker-widget.dropdown-menu');
				var rect = $element.get(0).getBoundingClientRect();
				$('.bootstrap-datetimepicker-widget.dropdown-menu').css({
					'position' : 'fixed',
					'top' : rect.top,
					'left' : rect.left,
					'right' : rect.right,
					'bottom' : rect.bottom,
					'width' : rect.width,
					'height' : rect.height
				});	*/
				const oOffset = $('.bootstrap-datetimepicker-widget.dropdown-menu').offset();
				$('.bootstrap-datetimepicker-widget.dropdown-menu').css({
					position: 'fixed',
					top: oOffset.top,
					left: oOffset.left,
				});
			});
		}
	};

	this.map = {












		// oTvoCmn.map.moveTo()
		moveTo: function (element) {
			var $panel = $(element).closest('div.panel');
			var $evtPointX = $panel.find('[id*="PointX"]');
			var $evtPointY = $panel.find('[id*="PointY"]');
			if ($evtPointX.length && $evtPointY.length) {
				var sEvtPointX = '';
				var sEvtPointY = '';
				var sTagNameEvtPointX = $evtPointX.get(0).tagName;
				var sTagNameEvtPointY = $evtPointY.get(0).tagName;
				if ('INPUT' == sTagNameEvtPointX && 'INPUT' == sTagNameEvtPointY) {
					sEvtPointX = $evtPointX.val();
					sEvtPointY = $evtPointY.val();
				} else if ('MARK' == sTagNameEvtPointX && 'MARK' == sTagNameEvtPointY) {
					sEvtPointX = $evtPointX.text();
					sEvtPointY = $evtPointY.text();
				} else {
					oCommon.modalAlert('modal-alert', '알림', '잘못된 주소 정보 입니다.');
					return false;
				}
				if (!isNaN(sEvtPointX) && sEvtPointX != '' && !isNaN(sEvtPointY) && sEvtPointY != '') {
					var oPoint = {
						pointX: sEvtPointX,
						pointY: sEvtPointY
					};

					olSwipMap.point.set([
						sEvtPointX, sEvtPointY
					], 'EPSG:4326', 'BLUE', true, false, true);

				} else {
					//oCommon.modalAlert('modal-alert', '알림', '잘못된 주소 정보 입니다.');
					oCommon.modalAlert('modal-alert', '알림', '사건주소를 선택하세요.');
				}
			} else {
				oCommon.modalAlert('modal-alert', '알림', '잘못된 주소 정보 입니다.');
			}
		},
		select: function (element) {
			var oData = {};
			var sTagName = $(element)[0].tagName;
			if (sTagName == 'BUTTON') {
				oData = $(element).closest('tr').data();
			} else {
				oData = $(element).data();
			}

			if (!$.isEmptyObject(oData)) {

				olSwipMap.point.set([
					oData.pointX, oData.pointY
				], 'EPSG:4326', 'BLUE', true, false, true);

			}
		},
		featurePopup: {
			init: function() {
				if (typeof oMap != 'undefined' && oTvoMap.layers.fclt != null) {
					oTvoMap.controls.featurePopup = new OpenLayers.Control.FeaturePopups({
						boxSelectionOptions: {},
						popupOptions: {
							hover: {
								followCursor: false
							},
							hoverList: {
								followCursor: false
							},
							list: {
								panMapIfOutOfView: false
							},
							listItem: {
								panMapIfOutOfView: false
							}
						},
						layers: [
							[
								oTvoMap.layers.fclt, {
								templates: {
									single: oTvoCmn.map.featurePopup.single,
									list: oTvoCmn.map.featurePopup.list,
									item: oTvoCmn.map.featurePopup.item,
									hover: oTvoCmn.map.featurePopup.hover,
									hoverList: oTvoCmn.map.featurePopup.hoverList,
									hoverItem: oTvoCmn.map.featurePopup.hoverItem
								}
							}
							]
						]
					});
					oMap.addControl(oTvoMap.controls.featurePopup);
				}
			},
			list: function(feature) {
				var $table = $('<table/>', {
					'id': 'tbFcltInfo',
					'class': 'table table-striped table-condensed',
				});
				var $caption = $('<caption/>', {
					'html': '총 <strong style="color: red;">' + feature.count + '</strong>개의 시설물이 있습니다.'
				});
				var $thead = $('<thead/>');
				var $theadTr = $('<tr/>', {
					'class': 'info'
				});
				var $th = $.parseHTML('<th>[관리번호]시설물명</th><th>물품구분</th><th>기능별유형</th>');
				var $tbody = $.parseHTML(feature.html);
				$theadTr.append($th);
				$thead.append($theadTr);
				$table.append($caption);
				$table.append($thead);
				$table.append($tbody);
				return $table.prop('outerHTML');
			},
			item: function(feature) {
				return oTvoCmn.map.featurePopup.templates(feature, 'ITEM');
			},
			single: function(feature) {
				return oTvoCmn.map.featurePopup.templates(feature, 'SINGLE');
			},
			hoverList: function(feature) {
				var $table = $('<table/>', {
					'id': 'tbFcltInfo',
					'class': 'table table-striped table-condensed',
				});
				var $caption = $('<caption/>', {
					'html': '총 <strong style="color: red;">' + feature.count + '</strong>개의 시설물이 있습니다.'
				});
				var $thead = $('<thead/>');
				var $theadTr = $('<tr/>', {
					'class': 'info'
				});
				var $th = $.parseHTML('<th>[관리번호]시설물명</th><th>물품구분</th><th>기능별유형</th>');
				var $tbody = $.parseHTML(feature.html);
				$theadTr.append($th);
				$thead.append($theadTr);
				$table.append($caption);
				$table.append($thead);
				$table.append($tbody);
				return $table.prop('outerHTML');
			},
			hover: function(feature) {
				return oTvoCmn.map.featurePopup.templates(feature, 'HOVER');
			},
			hoverItem: function(feature) {
				return oTvoCmn.map.featurePopup.templates(feature, 'HOVERITEM');
			},
			templates: function(feature, type) {
				var oAttributes = feature.attributes;
				var sFcltId = oAttributes.fcltId;
				var sFcltLblNm = oAttributes.fcltLblNm;
				var sMngSn = oAttributes.mngSn;
				if (sMngSn) {
					sFcltLblNm = '<strong style="color: #00f;">[' + sMngSn + ']</strong>' + sFcltLblNm;
				}
				var sFcltKndNm = oAttributes.fcltUsedTyNm;
				var sViewerTyCd = oAttributes.viewerTyCd;
				var sViewerPtzYn = oAttributes.viewerPtzYn;
				var sFcltGdsdtTy = oAttributes.fcltGdsdtTy;
				var sFcltGdsdtNm = (oAttributes.fcltGdsdtTy == '0') ? '주' : '보조';
				var sFcltKndDtlNm = (oAttributes.fcltKndDtlCd == 'RT') ? '회전' : '고정';
				
				var $tbodyTr = $('<tr/>');
				$tbodyTr.append($('<td/>', {
					'scope': 'row',
					'html': sFcltLblNm
				}));
				$tbodyTr.append($('<td/>', {
					'class': 'text-center',
					'text': sFcltGdsdtNm
				}));
				$tbodyTr.append($('<td/>', {
					'class': 'text-center',
					'text': sFcltKndDtlNm
				}));
				
				var sOuterHTML = $tbodyTr.prop('outerHTML');
				if (type == 'SINGLE' || type == 'HOVER') {
					var $table = $('<table/>', {
						'id': 'tbFcltInfo',
						'class': 'table table-striped table-condensed',
					});
					
					var $caption = $('<caption/>', {
						'html': '&nbsp;'
					});
					
					var $thead = $('<thead/>');
					var $theadTr = $('<tr/>', {
						'class': 'info'
					});
					var $th = $.parseHTML('<th>[관리번호]시설물명</th><th>물품구분</th><th>기능별유형</th>');
					var $tbody = $('<tbody/>');
					$theadTr.append($th);
					$thead.append($theadTr);
					$table.append($caption);
					$table.append($thead);
					$tbody.append($tbodyTr);
					$table.append($tbody);
					sOuterHTML = $table.prop('outerHTML');
				}
				
				return sOuterHTML;
			},
			
			handler: function(event) {
				
				var oOptOptions = {
					hitTolerance: 10
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
				}
				
				if (oFeaturesFclt && oFeaturesFclt.length > 0) {
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
									'text': v.fcltLblNm
								}));
								
								$tr.append($('<td/>', {
									'text': v.fcltKndNm + '(' + v.fcltUsedTyNm + ')'
								}));
								
								var sFcltGdsdtNm = (v.fcltGdsdtTy == '0') ? '주' : '보조';
								var sFcltKndDtlNm = (v.fcltKndDtlCd == 'RT') ? '회전' : '고정';
								
								$tr.append($('<td/>', {
									'text': sFcltGdsdtNm + '(' + sFcltKndDtlNm + ')'
								}));
								
								var $fcltSttus = $('<span/>', {
									'text': v.fcltSttusNm
								});
								
								if (v.fcltSttus == '0') {
									$fcltSttus.css('color', 'green');
								} else if (v.fcltSttus == '1') {
									$fcltSttus.css('color', 'red');
								} else {
									$fcltSttus.css('color', 'orange');
								}
								$tr.append($('<td/>', {
									'class': 'text-center',
									'html': $fcltSttus
								}));
								$tr.append($('<td/>'));
								$tbody.append($tr);
							});
						} else {
							var $tr = $('<tr/>');
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
				
				var oPositionClick = olSwipMap.overlays.click.getPosition();
				var oPositionPointermove = olSwipMap.overlays.pointermove.getPosition();
				
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
					} else if (event.type == 'pointermove') {
						$('#ol-overlay-pointermove').show();
						this.getTargetElement().style.cursor = 'pointer';
					}
				}
				
			}
		},
	}

	this.grid = {
		// ex : oTvoCmn.grid.hideCol('out-rqst-dtl-file', 'outFileMasking');
		hideCol: function (selector, name) {
			$('#grid-' + selector).hideCol(name);
		},
		// ex : oTvoCmn.grid.showCol('out-rqst-dtl-file', 'outFileMasking');
		showCol: function (selector, name) {
			$('#grid-' + selector).showCol(name);
		},
		// ex : oTvoCmn.grid.resize('out-rqst-dtl-file');
		resize: function (selector) {
			var nWidth = $('#paginate-' + selector).width();
			$('#grid-' + selector).setGridWidth(nWidth);
			var $th = $('#gview_grid-' + selector + ' tr.ui-jqgrid-labels th:visible');
			var $tr = $('#grid-' + selector + ' tbody tr[role="row"].ui-widget-content');
			if ($tr.length) {
				var isVisibleNodata = false;
				var hasClassNodata = false;
				$.each($tr, function (i, v) {
					hasClassNodata = $(v).hasClass('jqgrow-nodata');
					if (hasClassNodata) {
						isVisibleNodata = $(v).is(':visible');
						return false;
					}
				});

				if (isVisibleNodata) {
					$.each($th, function (i, v) {
						var sId = $(v).attr('id');
						if (sId.substring(sId.length - 3) == '_cb') {
							$(v).css('width', '25px');
						}
					});
				} else {
					var eTr = hasClassNodata ? $tr.get(1) : $tr.get(0);
					var $td = $(eTr).find('td:visible');
					$.each($th, function (i, v) {
						var sId = $(v).attr('id');
						if (sId.substring(sId.length - 3) == '_cb') {
							$(v).css('width', '25px');
						} else {
							$(v).css('width', $($td[i]).css('width'));
						}
					});
				}
			}
		}
	};

	this.jqGrid = {
		loadComplete: function (id, data, params) {
			oTvoCmn.jqGrid.checkNodata(id, data);
			oTvoCmn.jqGrid.pagenation(id, data, params);
			oTvoCmn.jqGrid.resize(id);
		},
		// ex : oTvoCmn.jqGrid.hideCol('out-rqst-dtl-file', 'outFileMasking');
		hideCol: function (selector, name) {
			$('#grid-' + selector).hideCol(name);
		},
		// ex : oTvoCmn.jqGrid.showCol('out-rqst-dtl-file', 'outFileMasking');
		showCol: function (selector, name) {
			$('#grid-' + selector).showCol(name);
		},
		// ex : oTvoCmn.jqGrid.resize('out-rqst-dtl-file');
		resize: function (selector) {
			var nWidth = $('#paginate-' + selector).width();
			$('#grid-' + selector).setGridWidth(nWidth);
			var $th = $('#gview_grid-' + selector + ' tr.ui-jqgrid-labels th:visible');
			var $tr = $('#grid-' + selector + ' tbody tr[role="row"].ui-widget-content');
			if ($tr.length) {
				var isVisibleNodata = false;
				var hasClassNodata = false;
				$.each($tr, function (i, v) {
					hasClassNodata = $(v).hasClass('jqgrow-nodata');
					if (hasClassNodata) {
						isVisibleNodata = $(v).is(':visible');
						return false;
					}
				});

				if (isVisibleNodata) {
					$.each($th, function (i, v) {
						var sId = $(v).attr('id');
						if (sId.substring(sId.length - 3) == '_cb') {
							$(v).css('width', '25px');
						}
					});
				} else {
					var eTr = hasClassNodata ? $tr.get(1) : $tr.get(0);
					var $td = $(eTr).find('td:visible');
					$.each($th, function (i, v) {
						var sId = $(v).attr('id');
						if (sId.substring(sId.length - 3) == '_cb') {
							$(v).css('width', '25px');
						} else {
							$(v).css('width', $($td[i]).css('width'));
						}
					});
				}
			}
		},
		pagenation: function (selector, data, params) {
			$('#paginate-' + selector).empty();
			$('#paginate-' + selector).html($('<ul/>', {
				'id': 'pagination-' + selector,
				'class': 'paging pagination-sm'
			}));

			var nVisiblePages = 4;
			if (typeof params != 'undefined') {
				if (typeof params.visiblePages != 'undefiend' && !isNaN(params.visiblePages)) {
					nVisiblePages = params.visiblePages;
				}
			}

			$('#pagination-' + selector).twbsPagination({
				startPage: isNaN(data.page) ? 1 : data.page,
				totalPages: isNaN(data.totalPages) ? 1 : data.totalPages,
				visiblePages: nVisiblePages,
				onPageClick: function (event, page) {
					oTvoCmn.jqGrid.reload(selector, page);
				},
				first: '<i class="fas fa-angle-double-left"></i>',
				prev: '<i class="fas fa-angle-left"></i>',
				next: '<i class="fas fa-angle-right"></i>',
				last: '<i class="fas fa-angle-double-right"></i>'
			});
		},
		reload: function (selector, page, params) {
			//$('article div.col').empty();	// 상세화면 비우기
			var oGridParam = {
				page: page
			}
			if (typeof params != 'undefined') {
				oGridParam.postData = params;
			}

			$('#grid-' + selector).setGridParam(oGridParam).trigger("reloadGrid");
		},
		checkNodata: function (selector, data) {
			if (!data.totalRows || !data.rows.length) {
				oTvoCmn.jqGrid.setNodata(selector);
				return false;
			}
			var $jqgrowNodata = $('#grid-' + selector + ' tr.jqgrow-nodata');
			if ($jqgrowNodata.length) {
				$jqgrowNodata.hide();
			}
		},
		setNodata: function (selector, text) {
			if (typeof text == 'undefined') {
				text = '데이터가 없습니다.';
			}
			var $jqgrowNodata = $('#grid-' + selector + ' tr.jqgrow-nodata');
			if ($jqgrowNodata.length) {
				$jqgrowNodata.find('td').text(text);
				$jqgrowNodata.show();
			} else {
				var td = $('<td/>', {
					'role': 'gridcell',
					'style': 'text-align:center; padding: 5px 3px 5px 3px;',
					'class': 'jqgrid_cursor_pointer',
					'text': text
				})
				var tr = $('<tr/>', {
					'role': 'row',
					'tabindex': '-1',
					'class': 'ui-widget-content jqgrow ui-row-ltr jqgrow-nodata'
				});
				var tbody = $('<tbody/>');
				tr.append(td);
				tbody.append(tr);
				$('#grid-' + selector).html(tbody);
			}
		}
	};

	this.download = {
		// oTvoCmn.download.paperFile()
		paperFile: function() {
			var sViewRqstNo     = $('#view-rqst-dtl-viewRqstNo').val();
			var sViewRqstYmdhms = $('#view-rqst-dtl-viewRqstYmdhms').val();
			var sPaperNm        = $('#view-rqst-dtl-paperNm').val();
			var sPaperFileNm    = $('#view-rqst-dtl-paperFileNm').val();
			//let sPaperFileNm = person.details.data.person['paperFileNm'] || '';
			if (sPaperFileNm != '') {
				//oCommon.modalConfirm('modal-confirm', '확인', '공문을 다운로드하시겠습니까?', () => {
					let $formDownload = $('#form-paperFile');
					if ($formDownload.length) {
						$('#form-paperFile').attr('action',contextRoot + '/tvo/downloadPaperFile.do');
						$('#form-paperFile input[name=viewRqstNo]').val(sViewRqstNo);
						$('#form-paperFile input[name=viewRqstYmdhms]').val(sViewRqstYmdhms);
						$('#form-paperFile input[name=paperNm]').val(sPaperNm);
						$('#form-paperFile input[name=paperFileNm]').val(sPaperFileNm);
					} else {
						$formDownload = $('<form/>', {
							'id': 'form-paperFile'	, 'method': 'POST'			, 'action': contextRoot + '/tvo/downloadPaperFile.do'	});
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'viewRqstNo'		, 'value': sViewRqstNo						}));
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'viewRqstYmdhms'	, 'value': sViewRqstYmdhms					}));
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'paperNm'			, 'value': sPaperNm							}));
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'paperFileNm'		, 'value': sPaperFileNm						}));
						$formDownload.appendTo(document.body);
					}
					$formDownload.submit();
				//}, undefined);
			} else {
				oCommon.modalAlert('modal-notice', '알림', '공문이 없습니다.');
			}
		},
		// oTvoCmn.download.playerFile()
		playerFile: function() {
			//	oCommon.modalConfirm('modal-confirm', '확인', '암호화영상 플레이어를 다운로드하시겠습니까?', () => {
			//	}, undefined);
					let $formDownload = $('#form-playerFile');
					if ($formDownload.length) {
						$('#form-playerFile').attr('action',contextRoot + '/tvo/downloadPlayerFile.do');
					} else {
						$formDownload = $('<form/>', {
							'id': 'form-playerFile',	'method': 'POST',	'action': contextRoot + '/tvo/downloadPlayerFile.do'	});
						$formDownload.appendTo(document.body);
					}
					$formDownload.submit();
		},
		// oTvoCmn.download.maskerFile()
		maskerFile: function() {
			//	oCommon.modalConfirm('modal-confirm', '확인', '원본영상 마스킹툴을 다운로드하시겠습니까?', () => {
			//	}, undefined);
					let $formDownload = $('#form-maskerFile');
					if ($formDownload.length) {
						$('#form-maskerFile').attr('action',contextRoot + '/tvo/downloadMaskerFile.do');
					} else {
						$formDownload = $('<form/>', {
							'id': 'form-maskerFile',	'method': 'POST',	'action': contextRoot + '/tvo/downloadMaskerFile.do'	});
						$formDownload.appendTo(document.body);
					}
					$formDownload.submit();
		},
	};

	// oTvoCmn.thirdPartyPw
	this.thirdPartyPw = {
		id: 'modal-third-party-pw',
		element: null,
		// oTvoCmn.thirdPartyPw.open()
		open: function (pw) {
			this.element = document.getElementById(this.id);
			if (this.element == null) {
				oDiv.openDiv(this.id, {
					target: 'tvoBlank/out/div/thirdPartyPw'		// 제3자재생암호
				}, function() {
					$('#out-third-party-pw').val(pw);
				});
			} else {
				$(this.element).modal('show');
				$('#out-third-party-pw').val(pw);
			}
		},
		// oTvoCmn.thirdPartyPw.close()
		//close: function() {
		//	$('#' + oTvoCmn.thirdPartyPw.id).modal('hide');
		//},
	};




};
