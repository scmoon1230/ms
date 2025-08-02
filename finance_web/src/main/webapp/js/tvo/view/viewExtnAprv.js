$(function() {
	oViewExtnAprv = new viewExtnAprv();
	oViewExtnAprv.init();
});

function viewExtnAprv() {
	this.init = function() {
        // LEFT 조절
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			right : true
		}, function() {
			oTvoCmn.datetimepicker.ymd('.aprv-view-prod-extn-viewExtnRqstYmdhms', '2023-01', moment().add(100, 'years'));
			oTvoCmn.datetimepicker.ymd('.aprv-view-prod-extn-viewEndYmdhms', '2023-01', moment().add(100, 'years'));
			codeInfoList('#aprv-view-prod-extn-tvoPrgrsCd', 'VIEW_PRGRS', '', '진행상태');
			//codeInfoList('#aprv-view-prod-extn-tvoPrgrsCd', 'TVO_APRV', '', '진행상태');

			if (oGis.olVersion == 5) {
				// 1. BASE, SATELLITE 배경지도 생성.
				olSwipMap.init({
					bookmark : false,
					dropdown : true
				});

				setTimeout(function() {
					olSwipMap.layers.fclt.init();

					olSwipMap.events.feature.pointermove = oTvoCmn.map.featurePopup.handler;
					olSwipMap.events.feature.click = oTvoCmn.map.featurePopup.handler;

					olSwipMap.listeners.feature.pointermove = olMap.map.on('pointermove', olSwipMap.events.feature.pointermove);
					olSwipMap.listeners.feature.click = olMap.map.on('click', olSwipMap.events.feature.click);
				}, 500);
			} else {
				// CCTV
				if (typeof oTvoMap != 'undefined') {
					oTvoMap.layers.init.fclt('CTV', '', 'Y');
					oTvoCmn.map.featurePopup.init();
				}
			}
			oViewExtnAprv.view.prodExtn.grid.init();
		});
	};

	// oViewExtnAprv.view
	this.view = {
		// oViewExtnAprv.view.prodExtn{}
		prodExtn : {
			// oViewExtnAprv.view.prodExtn.grid{}
			grid : {
				id : 'aprv-view-prod-extn',
				page: '1',
				
				init : function() {
					var sId = oViewExtnAprv.view.prodExtn.grid.id;
					var oGridParams = oViewExtnAprv.view.prodExtn.grid.params();
					var sGridId = '#grid-' + oViewExtnAprv.view.prodExtn.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/view/selectViewExtnAprvList.json',
						datatype : 'json',
						mtype : 'POST',
						height : 'auto',
						rowNum : 5,
						autowidth:true,
						shrinkToFit : true,
						postData : oGridParams,
						beforeRequest : function() {
						},
						loadComplete : function(data) {
							oViewExtnAprv.view.prodExtn.grid.page = data.page;
							var $prgrs = $('#aprv-view-prod-extn-prgrs');
							if ($prgrs.val() != '') {
								$prgrs.val('');
								history.replaceState({}, null, location.pathname);
							}
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
								'dstrtCd', '신청지구', '열람신청번호', '연장신청일시', '종료일자', '신청자', '사건번호', '사건명', '연장사유', '진행상태', '상세'
						],
						colModel : [
							{	name:'dstrtCd'       , hidden: true
							}, {name:'dstrtNm'       , align:'center', width:60
							}, {name:'viewRqstNo'    , align:'center',	width:60
							    , formatter: function (cellvalue, options, rowObject) { return rowObject.viewRqstNo.substring(5); }
							}, {name:'viewExtnRqstYmdhms',	align:'center',	width:100, cellattr : function() {	return 'style="width:100px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var sViewExtnRqstYmdhms = rowObject.viewExtnRqstYmdhms;
									momentExtnRqstYmdhms = moment(sViewExtnRqstYmdhms, 'YYYY-MM-DDHHmmss');
									var $span = $('<span/>', {
										'title': momentExtnRqstYmdhms.format('YYYY-MM-DD HH:mm:ss'),
										'text': momentExtnRqstYmdhms.format('YY-MM-DD HH:mm')
									});
									return $span.prop('outerHTML');
								}
							}, {name:'viewEndYmdhms',		align:'center', width:100, cellattr : function() {	return 'style="width:100px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var sViewExtnRqstYmdhms = rowObject.viewEndYmdhms;
									momentExtnRqstYmdhms = moment(sViewExtnRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
									var $span = $('<span/>', {
										'title': momentExtnRqstYmdhms.format('YYYY-MM-DD HH:mm:ss'),
										'text': momentExtnRqstYmdhms.format('YY-MM-DD')
									});
									return $span.prop('outerHTML');
								}
							}, {name:'viewExtnRqstUserNm', align:'center', width:60, cellattr : function() {	return 'style="width:60px;"';		}
							}, {name:'evtNo'             , align:'center', classes:'text-ellipsis'
							}, {name:'evtNm'             , align:'left'  , classes:'text-ellipsis'
							}, {name:'viewExtnRqstRsn'   , align:'left'  , classes:'text-ellipsis'
							}, {name:'tvoPrgrsNm'        , align:'center', width:60, cellattr : function() {	return 'style="width:60px;"';		}
							}, {name:'viewProdExtnDtl'   , align:'center', width:60, cellattr : function() {	return 'style="width:60px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var $button = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs',
										'title' : '열람연장신청 내용을 조회합니다.',
										//'html' : '<i class="far fa-file-alt"></i> 보기',
										'html' : '보기',
										'onclick' : 'javascript:oViewExtnAprv.view.prodExtn.detail.init("' + rowObject.dstrtCd + '", "' + rowObject.viewRqstNo + '", "' + rowObject.viewExtnRqstYmdhms + '");'
									});
									return $button.prop('outerHTML');
								}
							}
						],
						jsonReader : {
							root : "rows",
							total : "totalPages",
							records : "totalRows"
						},
						onSelectRow : function(rowId) {

						},
						cmTemplate : {
							sortable : false,
							resizable : false
						}
					});
					
		            // 새로고침
		            $('#btn-refresh').on('click', () => oViewExtnAprv.view.prodExtn.grid.search());
					
				},
				// oViewExtnAprv.view.prodExtn.grid.search()
				search : function(page) {
					if (typeof page != 'undefined') {
						oViewExtnAprv.view.prodExtn.grid.page = page;
					}
						
					var sId = oViewExtnAprv.view.prodExtn.grid.id;
					var sPage = oViewExtnAprv.view.prodExtn.grid.page;		//alert(sPage);
					var oGridParams = oViewExtnAprv.view.prodExtn.grid.params();
				//	if (typeof prgrs != 'undefined') {	oGridParams.prgrs = prgrs;		}
					$('article div.col').empty();	// 상세화면 비우기
					oTvoCmn.jqGrid.reload(sId, sPage, oGridParams);
				},
				params : function() {
					var sPrgrs = $('#aprv-view-prod-extn-prgrs').val();
					var sViewExtnRqstYmdhms = $('#aprv-view-prod-extn-viewExtnRqstYmdhms').val();
					var sViewEndYmdhms = $('#aprv-view-prod-extn-viewEndYmdhms').val();
					var sViewExtnRqstRsn = $('#aprv-view-prod-extn-viewExtnRqstRsn').val();
					var sTvoPrgrsCd = $('#aprv-view-prod-extn-tvoPrgrsCd option:selected').val();
					var sEvtNo = $('#aprv-view-prod-extn-evtNo').val();

					var momentViewRqstYmdhms = moment(sViewExtnRqstYmdhms, 'YYYY-MM-DD');
					if (momentViewRqstYmdhms.isValid()) {
						sViewExtnRqstYmdhms = momentViewRqstYmdhms.format('YYYYMMDD');
					} else {
						sViewExtnRqstYmdhms = '';
					}

					var momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD');
					if (momentViewEndYmdhms.isValid()) {
						sViewEndYmdhms = momentViewEndYmdhms.format('YYYYMMDD');
					} else {
						sViewEndYmdhms = '';
					}

					var oParams = {
						prgrs : sPrgrs,
						viewExtnRqstYmdhms : sViewExtnRqstYmdhms,
						viewEndYmdhms : sViewEndYmdhms,
						viewExtnRqstRsn : sViewExtnRqstRsn,
						tvoPrgrsCd : sTvoPrgrsCd,
						evtNo : sEvtNo
					};
					return oParams;
				}
			},
			// oViewExtnAprv.view.prodExtn.detail{}
			detail : {
				// oViewExtnAprv.view.prodExtn.detail.init()
				init : function(pDstrtCd, pViewRqstNo, pViewExtnRqstYmdhms) {
					$('article div.col').empty();	// 상세화면 비우기
					oDiv.setDiv('left', '0', {
						//target : 'tvoBlank/view/div/viewExtnProdDtl',			// 열람기간연장신청상세
						target : 'tvoBlank/view/div/viewExtnDtl',			// 열람기간연장신청상세
						dstrtCd : pDstrtCd,
						viewRqstNo : pViewRqstNo,
						viewExtnRqstYmdhms : pViewExtnRqstYmdhms
					}, function() {
					/*	var sRqstViewEndYmdhms = $('#view-prod-extn-dtl-rqstViewEndYmdhms').text();
						var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						if (momentRqstViewEndYmdhms.isValid()) {
							$('#view-prod-extn-aprv-aprvViewEndYmdhms').val(sRqstViewEndYmdhms);
						}
						oTvoCmn.datetimepicker.ymdhm('.view-prod-extn-aprv-aprvViewEndYmdhms', '2023-01', moment().add(100, 'years'));	*/
					});
					oDiv.setDiv('left', '1', {
						target : 'tvoBlank/view/div/viewRqstDtl',				// 열람신청상세
						dstrtCd : pDstrtCd,
						viewRqstNo : pViewRqstNo
					}, function() {
						setTimeout(function() {
							$("#view-rqst-dtl-map-pin").trigger("click");		// 지도의 발생위치로 이동한다.
						}, 500);
					});
				}
			},
			// oViewExtnAprv.view.prodExtn.aprv{}
			aprv : {
				id: 'modal-aprv',
				element: null,
				// oViewExtnAprv.view.prodExtn.aprv.open()
				open : function() {
					this.element = document.getElementById(this.id);
					if (this.element == null) {
						oDiv.openDiv(this.id, {
							target : 'tvoBlank/view/div/viewExtnAprv',
						}, function() {
							//$('input[type=radio][name=view-prod-extn-aprv-tvoPrgrsCd]').prop('checked', false);
							$('input[type=radio][name=view-prod-extn-aprv-tvoPrgrsCd][value="50"]').prop('checked', true);
							$('#view-prod-extn-aprv-tvoPrgrsDtl').val("");
							var sRqstViewEndYmdhms = $('#view-prod-extn-dtl-rqstViewEndYmdhms').text();
							//var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
							var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD');
							if (momentRqstViewEndYmdhms.isValid()) {
								$('#view-prod-extn-aprv-aprvViewEndYmdhms').val(sRqstViewEndYmdhms);
							}
							oTvoCmn.datetimepicker.ymd('.view-prod-extn-aprv-aprvViewEndYmdhms', moment(), moment().add(100, 'years'));
						});
					} else {
						$(this.element).modal('show');
						//$('input[type=radio][name=view-prod-extn-aprv-tvoPrgrsCd]').prop('checked', false);
						$('input[type=radio][name=view-prod-extn-aprv-tvoPrgrsCd][value="50"]').prop('checked', true);
						$('#view-prod-extn-aprv-tvoPrgrsDtl').val("");
						var sRqstViewEndYmdhms = $('#view-prod-extn-dtl-rqstViewEndYmdhms').text();
						//var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD');
						if (momentRqstViewEndYmdhms.isValid()) {
							$('#view-prod-extn-aprv-aprvViewEndYmdhms').val(sRqstViewEndYmdhms);
						}
						oTvoCmn.datetimepicker.ymd('.view-prod-extn-aprv-aprvViewEndYmdhms', moment(), moment().add(100, 'years'));
					}
				},
				close : function() {
					$('#' + oViewExtnAprv.view.prodExtn.aprv.id).modal('hide');
				},
				// oViewExtnAprv.view.prodExtn.aprv.approval()
				approval : function() {
					var sTvoPrgrsCd = $('input[name="view-prod-extn-aprv-tvoPrgrsCd"]:checked').val();
					if (!sTvoPrgrsCd) {		oCommon.modalAlert('modal-alert', '알림', '승인구분을 체크해 주세요.');	return;		}
					if ( sTvoPrgrsCd == '30' ) {
						if ( $('#view-prod-extn-aprv-tvoPrgrsDtl').val().trim() == '' ) {
							oCommon.modalAlert('modal-alert', '알림', '반려사유를 입력하세요.');	$('#view-prod-extn-aprv-tvoPrgrsDtl').focus();	return;
						}
					}
					
					//let sDstrtCd = $('#view-rqst-dtl-dstrtCd').val();
					//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					//var sTvoPrgrsDtl = $('#view-prod-extn-aprv-tvoPrgrsDtl').val();
					
					var sViewExtnRqstYmdhms = $('#view-prod-extn-dtl-viewExtnRqstYmdhms').text();
					var momentExtnRqstYmdhms = moment(sViewExtnRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
					sViewExtnRqstYmdhms = momentExtnRqstYmdhms.format('YYYYMMDDHHmmss');

					
					var sAprvViewEndYmdhms = $('#view-prod-extn-aprv-aprvViewEndYmdhms').val();
					//var momentAprvViewEndYmdhms = moment(sAprvViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
					//sAprvViewEndYmdhms = momentAprvViewEndYmdhms.format('YYYYMMDDHHmmss');
					var momentAprvViewEndYmdhms = moment(sAprvViewEndYmdhms, 'YYYY-MM-DD');
					sAprvViewEndYmdhms = momentAprvViewEndYmdhms.format('YYYYMMDD')+'235959';

					var oParams = {
						dstrtCd : $('#view-rqst-dtl-dstrtCd').val(),
						viewRqstNo : $('#view-rqst-dtl-viewRqstNo').val(),
						viewExtnRqstYmdhms : sViewExtnRqstYmdhms,
						tvoPrgrsCd : sTvoPrgrsCd,
						tvoPrgrsDtl : $('#view-prod-extn-aprv-tvoPrgrsDtl').val(),
						aprvViewEndYmdhms : sAprvViewEndYmdhms
					};

					var sFlag = $('input[name="view-prod-extn-aprv-tvoPrgrsCd"]:checked').parent().text().trim();
					oCommon.modalConfirm('modal-confirm', '알림', '열람연장신청을 ' + sFlag + '하시겠습니까?', function() {
						$.ajax({
							type : 'POST',
							async : false,
							dataType : 'json',
							url:contextRoot + '/tvo/aprv/viewProdExtn/updateViewProdExtn.json',
							data : oParams,
							success : function(data) {
								if (data.result == '1') {
									oTvoCmn.div.clear('#view-prod-extn-aprv');
									oViewExtnAprv.view.prodExtn.grid.search();			// 목록 새로고침
									oViewExtnAprv.view.prodExtn.detail.init(oParams.dstrtCd, oParams.viewRqstNo, oParams.viewExtnRqstYmdhms);
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 ' + sFlag + '하였습니다.');
								}
							},
							error : function(data, status, err) {
								console.log(data);
							}
						});
						$('#' + oViewExtnAprv.view.prodExtn.aprv.id).modal('hide');
					});
				}
			/*	// oViewExtnAprv.view.prodExtn.aprv.init()
				init : function() {
					oDiv.setDiv('right', '3', {
						target : 'tvoBlank/aprv/div/viewProdExtnAprv',
					}, function() {
						var sRqstViewEndYmdhms = $('#view-prod-extn-dtl-rqstViewEndYmdhms').text();
						var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						if (momentRqstViewEndYmdhms.isValid()) {
							$('#view-prod-extn-aprv-aprvViewEndYmdhms').val(sRqstViewEndYmdhms);
						}
						oTvoCmn.datetimepicker.ymdhm('.view-prod-extn-aprv-aprvViewEndYmdhms', '2023-01', moment().add(100, 'years'));
					});

				},
				cancel : function() {
					oCommon.modalConfirm('modal-confirm', '알림', '열람연장 승인 창을 닫으시겠습니까?', function() {
						oTvoCmn.div.clear('#view-prod-extn-aprv');
					});
				}	*/	
			}
		}
	};
}
