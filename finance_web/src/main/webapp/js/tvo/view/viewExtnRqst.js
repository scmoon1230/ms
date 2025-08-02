$(function() {
	oViewExtnRqst = new viewExtnRqst();
	oViewExtnRqst.init();
});

function viewExtnRqst() {
	this.init = function() {
        // LEFT 조절
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			right : true
		}, function() {
			oTvoCmn.datetimepicker.ymd('.rqst-view-prod-extn-viewExtnRqstYmdhms', '2023-01', moment().add(100, 'years'));
			oTvoCmn.datetimepicker.ymd('.rqst-view-prod-extn-viewEndYmdhms', '2023-01', moment().add(100, 'years'));
			codeInfoList('#rqst-view-prod-extn-tvoPrgrsCd', 'VIEW_PRGRS', '', '진행상태');
			//codeInfoList('#rqst-view-prod-extn-tvoPrgrsCd', 'TVO_APRV', '', '진행상태');

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
			oViewExtnRqst.view.prodExtn.grid.init();
		});
	};

	// oViewExtnRqst.view
	this.view = {
		// oViewExtnRqst.view.prodExtn{}
		prodExtn : {
			// oViewExtnRqst.view.prodExtn.grid{}
			grid : {
				id : 'rqst-view-prod-extn',
				page: '1',
				
				init : function() {
					var sId = oViewExtnRqst.view.prodExtn.grid.id;
					var oGridParams = oViewExtnRqst.view.prodExtn.grid.params();
					var sGridId = '#grid-' + oViewExtnRqst.view.prodExtn.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/rqst/viewProdExtn/selectViewExtnRqstList.json',
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
							oViewExtnRqst.view.prodExtn.grid.page = data.page;
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
								'dstrtCd', '신청지구', '신청번호', '신청일시', '종료일자', '사건번호', '사건명', '연장사유', '진행상태', '상세'
						],
						colModel : [
							{	name:'dstrtCd'       , hidden: true
							}, {name:'dstrtNm'       , align:'center', width:60
							}, {name:'viewRqstNo'    , align:'center',  width:60
							    , formatter: function (cellvalue, options, rowObject) { return rowObject.viewRqstNo.substring(5); }
							}, {name:'viewExtnRqstYmdhms', align:'center',		width:100,		cellattr : function() {	return 'style="width:100px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var sViewExtnRqstYmdhms = rowObject.viewExtnRqstYmdhms;
									momentExtnRqstYmdhms = moment(sViewExtnRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
									return momentExtnRqstYmdhms.format('YY-MM-DD HH:mm');
								}
							}, {name:'viewEndYmdhms',		align:'center',		width:100,		cellattr : function() {	return 'style="width:100px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var sViewExtnRqstYmdhms = rowObject.viewEndYmdhms;
									momentExtnRqstYmdhms = moment(sViewExtnRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
									return momentExtnRqstYmdhms.format('YY-MM-DD');
								}
							}, {name:'evtNo',				align:'center',		classes:'text-ellipsis'
							}, {name:'evtNm',				align:'left',		classes:'text-ellipsis'
							}, {name:'viewExtnRqstRsn',	classes:'text-ellipsis'
							}, {name:'tvoPrgrsNm',			align:'center',		width:60,		cellattr : function() {	return 'style="width:60px;"';		}
							}, {name:'viewProdExtnDtl',		align:'center',		width:60,		cellattr : function() {	return 'style="width:60px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var $button = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs',
										'title' : '열람연장신청 내용을 조회합니다.',
										//'html' : '<i class="far fa-file-alt"></i> 보기',
										'html' : '보기',
										'onclick' : 'javascript:oViewExtnRqst.view.prodExtn.detail.init("' + rowObject.dstrtCd + '", "' + rowObject.viewRqstNo + '", "' + rowObject.viewExtnRqstYmdhms + '");'
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
		            $('#btn-refresh').on('click', () => oViewExtnRqst.view.prodExtn.grid.search());
					
				},
				// oViewExtnRqst.view.prodExtn.grid.search()
				search : function(page) {
					if (typeof page != 'undefined') {
						oViewExtnRqst.view.prodExtn.grid.page = page;
					}
						
					var sId = oViewExtnRqst.view.prodExtn.grid.id;
					var sPage = oViewExtnRqst.view.prodExtn.grid.page;		//alert(sPage);
					var oGridParams = oViewExtnRqst.view.prodExtn.grid.params();
					$('article div.col').empty();	// 상세화면 비우기
					oTvoCmn.jqGrid.reload(sId, sPage, oGridParams);
				},
				params : function() {
					var sViewExtnRqstYmdhms = $('#rqst-view-prod-extn-viewExtnRqstYmdhms').val();
					var sViewEndYmdhms = $('#rqst-view-prod-extn-viewEndYmdhms').val();
					var sViewExtnRqstRsn = $('#rqst-view-prod-extn-viewExtnRqstRsn').val();
					var sTvoPrgrsCd = $('#rqst-view-prod-extn-tvoPrgrsCd option:selected').val();
					var sEvtNo = $('#rqst-view-prod-extn-evtNo').val();

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
						viewExtnRqstYmdhms : sViewExtnRqstYmdhms,
						viewEndYmdhms : sViewEndYmdhms,
						viewExtnRqstRsn : sViewExtnRqstRsn,
						tvoPrgrsCd : sTvoPrgrsCd,
						evtNo : sEvtNo
					};

					return oParams;
				}
			},
			// oViewExtnRqst.view.prodExtn.detail{}
			detail : {
				// oViewExtnRqst.view.prodExtn.detail.init()
				init : function(pDstrtCd, pViewRqstNo, pViewExtnRqstYmdhms) {
					$('article div.col').empty();	// 상세화면 비우기
					oDiv.setDiv('left', '0', {
						target : 'tvoBlank/view/div/viewExtnDtl',			// 열람기간연장신청상세
						dstrtCd : pDstrtCd,
						viewRqstNo : pViewRqstNo,
						viewExtnRqstYmdhms : pViewExtnRqstYmdhms
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
			}
			/*
			, init : function() {
				var sViewEndYmdhms = $('#view-rqst-dtl span.viewEndYmdhms').text();
				if ('' != sViewEndYmdhms) {
					oDiv.setDiv('right', '1', {
						target : 'tvoBlank/rqst/div/viewProdExtnRqst'
					}, function() {
						oTvoCmn.datetimepicker.ymdhm('#view-prod-extn-rqst-rqstViewEndYmdhms', '2023-01', moment());
					});
				}
			}
			, request : function() {
				oCommon.modalConfirm('modal-confirm', '알림', '열람기간 연장을 신청하시겠습니까?', function() {
					var sViewRqstNo = $('#view-prod-extn-dtl-viewRqstNo').val();
					var sRqstViewEndYmdhms = $('#view-prod-extn-rqst-rqstViewEndYmdhms').val();
					momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
					if (momentRqstViewEndYmdhms.isValid()) {
						sRqstViewEndYmdhms = momentRqstViewEndYmdhms.format('YYYYMMDDHHmmss');
					}
					var sViewExtnRqstRsn = $('#view-prod-extn-rqst-viewExtnRqstRsn').val();

					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/rqst/viewRqst/insertViewProdExtn.json',
						data : {
							viewRqstNo : sViewRqstNo,
							rqstViewEndYmdhms : sRqstViewEndYmdhms,
							viewExtnRqstRsn : sViewExtnRqstRsn
						},
						success : function(data) {
							if (data.result == '1') {
								gridReload('rqst-view-prod-extn', 1, oViewExtnRqst.view.prodExtn.grid.params());
								oTvoCmn.div.clear('#view-prod-extn-dtl');
								oTvoCmn.div.clear('#view-rqst-dtl');
								oTvoCmn.div.clear('#view-prod-extn-rqst');
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 열람기간 연장이 신청되었습니다.');
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
				});
			}
			, cancel : function() {
				oCommon.modalConfirm('modal-confirm', '알림', '열람기간연장 신청을 취소하시겠습니까?', function() {
					oTvoCmn.div.clear('#view-prod-extn-rqst');
				});
			}
			*/
		}
	};
}
