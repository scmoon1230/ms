$(function() {
	oViewFirstAprv = new viewFirstAprv();
	oViewFirstAprv.init();
});

function viewFirstAprv() {
	this.init = function() {
        // LEFT 조절
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			right : true
		}, function() {
			oTvoCmn.datetimepicker.ymd('.aprv-view-viewRqstYmdhms', '2023-01', moment().add(100, 'years'));		// 열람신청일
			//oTvoCmn.datetimepicker.ymd('.aprv-view-viewEndYmdhms', '2023-01', moment().add(100, 'years'));	// 열람종료일
			codeInfoList('#aprv-view-rqstRsnTyCd', 'RQST_RSN_TY', '', '신청사유');
			codeInfoList('#aprv-view-tvoPrgrsCd', 'VIEW_PRGRS', '', '진행상태');

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
			oViewFirstAprv.view.rqst.grid.init();
		});
	};

	// oViewFirstAprv.view{}
	this.view = {
		// 신청 oViewFirstAprv.view.rqst{}
		rqst : {
			// 목록 oViewFirstAprv.view.rqst.grid{}
			grid : {
				id : 'aprv-view',
				page: '1',
				// oViewFirstAprv.view.rqst.grid.init()
				init : function() {
					var sId = oViewFirstAprv.view.rqst.grid.id;
					var oGridParams = oViewFirstAprv.view.rqst.grid.params();
					var sGridId = '#grid-' + oViewFirstAprv.view.rqst.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/view/selectViewRqstAprvList.json',
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
							oViewFirstAprv.view.rqst.grid.page = data.page;
							//var $prgrs = $('#aprv-view-prgrs');
							//if ($prgrs.val() != '') {
							//	$prgrs.val('');
							//	history.replaceState({}, null, location.pathname);
							//}
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : ['' //'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#gview_grid-aprv-view\', this, event);">'
							, 'dstrtCd', '신청지구', '열람신청번호', '열람신청일자', '신청사유코드', '신청사유', '신청자', '사건번호', '사건명', '발생일시', '발생주소'
							, '진행상태코드', '진행상태', '열람종료', '활용결과', '상세'
						],
						colModel : [
							{ name:'CHECK'          , align:'center', width:20, editable:true, edittype:'checkbox'
								, editoptions: { value:"True:False" }, sortable: false	, formatter: $.GridCheckBox  , hidden: true
							}, {name:'dstrtCd'      , hidden: true
							}, {name:'dstrtNm'      , align:'center', width:60
							}, {name:'viewRqstNo'   , align:'center',	width:60
							   , formatter: function (cellvalue, options, rowObject) { return rowObject.viewRqstNo.substring(5); }
							}, {name:'viewRqstYmdhms', align:'center',	width:70,	cellattr : function() {	return 'style="width:60px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sViewRqstYmdhms = rowObject.viewRqstYmdhms;
									var momentViewRqstYmdhms = moment(sViewRqstYmdhms, 'YYYYMMDDHHmmss');
									return momentViewRqstYmdhms.format('YY-MM-DD');
								}
							}, {name:'rqstRsnTyCd'   , hidden:true
							}, {name:'rqstRsnTyNm'   , align:'center',	width:80,	cellattr : function() {	return 'style="width:80px;"';	}
							   ,classes:'text-ellipsis'
							}, {name:'viewRqstUserNm', align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'evtNo'         , align:'center',	width:100,	cellattr : function() {	return 'style="width:100px;"';	}
							}, {name:'evtNm'         , align:'left',	width:100,	cellattr : function() {	return 'style="width:100px;"';	}
							}, {name:'evtYmdhms'     , hidden: true
							}, {name:'evtAddr'       , align:'left',	classes:'text-ellipsis'
							}, {name:'tvoPrgrsCd'    , hidden:true
							}, {name:'tvoPrgrsNm'    , align:'center',	width:80,	cellattr : function() {	return 'style="width:80px;"';	}
							}, {name:'viewEndYmdhms' , align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							   , formatter: function(cellvalue, options, rowObject) {
									var strYmdhms = rowObject.viewEndYmdhms;
									var momentYmdhms = moment(strYmdhms, 'YYYYMMDDHHmmss');
									if (momentYmdhms.isValid()) {
										return momentYmdhms.format('YY-MM-DD');
									} else {
										return '';
									}
								}
							}, {name:'viewResultTyNm',		align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'viewRqstDtl',			align:'center',	width:50 ,	cellattr : function() {	return 'style="width:50px;"';	}
							   , formatter : function(cellvalue, options, rowObject) {
									var $button = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs',
										'title' : '열람신청 내용을 조회합니다.',
										'html': '보기',
										'onclick' : 'javascript:oViewFirstAprv.view.rqst.detail.init("' + rowObject.dstrtCd + '", "' + rowObject.viewRqstNo + '");'
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
		            $('#btn-refresh').on('click', () => oViewFirstAprv.view.rqst.grid.search());
					
				},
				// oViewFirstAprv.view.rqst.grid.search()
				search : function(page) {
					if (typeof page != 'undefined') {
						oViewFirstAprv.view.rqst.grid.page = page;
					}
						
					var sFr = $('#aprv-view-viewRqstYmdhms-fr').val();
					var sTo = $('#aprv-view-viewRqstYmdhms-to').val();
					if ( sFr!='' && sTo!='' ) {
						if ( sFr > sTo ) {	alert('기간설정에 오류가 있습니다.');	return;
						}
					}
					var sId = oViewFirstAprv.view.rqst.grid.id;
					var sPage = oViewFirstAprv.view.rqst.grid.page;		//alert(sPage);
					var oGridParams = oViewFirstAprv.view.rqst.grid.params();
				//	if (typeof prgrs != 'undefined') {	oGridParams.prgrs = prgrs;	}
					$('article div.col').empty();	// 상세화면 비우기
					oTvoCmn.jqGrid.reload(sId, sPage, oGridParams);
				},
				// oViewFirstAprv.view.rqst.grid.params()
				params : function() {
					//var sPrgrs = $('#aprv-view-prgrs').val();
					
					var sViewRqstYmdhmsFr = $('#aprv-view-viewRqstYmdhms-fr').val();
					var momentViewRqstYmdhmsFr = moment(sViewRqstYmdhmsFr, 'YYYY-MM-DD');
					if (momentViewRqstYmdhmsFr.isValid()) {
						sViewRqstYmdhmsFr = momentViewRqstYmdhmsFr.format('YYYYMMDD');
					} else {
						sViewRqstYmdhmsFr = '';
					}

					var sViewRqstYmdhmsTo = $('#aprv-view-viewRqstYmdhms-to').val();
					var momentViewRqstYmdhmsTo = moment(sViewRqstYmdhmsTo, 'YYYY-MM-DD');
					if (momentViewRqstYmdhmsTo.isValid()) {
						sViewRqstYmdhmsTo = momentViewRqstYmdhmsTo.format('YYYYMMDD');
					} else {
						sViewRqstYmdhmsTo = '';
					}
										
				/*	var sViewEndYmdhms = $('#aprv-view-viewEndYmdhms').val();
					var momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD');
					if (momentViewEndYmdhms.isValid()) {
						sViewEndYmdhms = momentViewEndYmdhms.format('YYYYMMDD');
					} else {
						sViewEndYmdhms = '';
					}	*/
					//var sRqstRsnTyCd = ;
					//var sTvoPrgrsCd = ;
					//var sEvtNo = ;
					//var sEvtNm = ;

					var oParams = {
						//prgrs : sPrgrs,
						viewRqstYmdhmsFr : sViewRqstYmdhmsFr,
						viewRqstYmdhmsTo : sViewRqstYmdhmsTo,
						//viewEndYmdhms : sViewEndYmdhms,
						rqstRsnTyCd    : $('#aprv-view-rqstRsnTyCd option:selected').val(),
						tvoPrgrsCd     : $('#aprv-view-tvoPrgrsCd option:selected').val(),
						evtNo          : $('#aprv-view-evtNo').val(),
						evtNm          : $('#aprv-view-evtNm').val(),
						viewRqstUserId : $('#aprv-view-rqstUserId').val()
						
					};
					return oParams;
				}
			},
			// 상세 oViewFirstAprv.view.rqst.detail{}
			detail : {
				// 상세 oViewFirstAprv.view.rqst.detail.init()
				init : function(pDstrtCd, pViewRqstNo) {
					
					$('article div.col').empty();	// 상세화면 비우기
					
					oDiv.setDiv('left', '0', {
						target : 'tvoBlank/view/div/viewRqstDtl',				// 열람신청상세
						viewRqstNo : pViewRqstNo,
						dstrtCd: pDstrtCd
					}, function() {
						var sViewEndYmdhmsWant = $('#view-rqst-dtl-viewEndYmdhmsWant').text();
						var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD HH:mm:ss');
						if (momentViewEndYmdhmsWant.isValid()) {
							$('#view-rqst-aprv-viewEndYmdhms').val(sViewEndYmdhmsWant);
						}
						oTvoCmn.datetimepicker.ymdhm('.view-rqst-aprv-viewEndYmdhms', moment(), moment().add(30, 'days'));
						
						setTimeout(function() {
							$("#view-rqst-dtl-map-pin").trigger("click");		// 지도의 발생위치로 이동한다.
						}, 500);
					});

					oDiv.setDiv('left', '1', {
						target : 'tvoBlank/view/div/viewExtnList',			// 열람기간연장이력
					}, function() {
						setTimeout(function() {
							oViewFirstAprv.view.prodExtn.grid.init();
						}, 500);
					});
				/*	oDiv.setDiv('left', '2', {
						target: 'tvoBlank/out/div/outRqstList'				// 반출신청목록
					}, function() {
						setTimeout(function() {
							oViewFirstAprv.out.rqst.grid.init();
						}, 500);
					});
				*/
				},
			},
		},
		// 기간연장 oViewFirstAprv.view.prodExtn{}
		prodExtn : {
			grid : {
				id : 'view-prod-extn-his',
				// oViewFirstAprv.view.prodExtn.grid.init()
				init : function() {
					var sId = oViewFirstAprv.view.prodExtn.grid.id;
					var oGridParams = oViewFirstAprv.view.prodExtn.grid.params();
					var sGridId = '#grid-' + oViewFirstAprv.view.prodExtn.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/rqst/viewRqst/selectViewProdExtnHisList.json',
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
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [	'#', 'viewRqstNo', '신청일시', '종료일자', '사유'
						],
						colModel : [
								{	name:'rnum',					align:'center',			width:30,
									cellattr : function(rowId, val, rawObject, cm, rdata) {
										return 'style="width:30px;"';
									}
								}, { name:'viewRqstNo',				hidden:true
								}, { name:'viewExtnRqstYmdhms',		align:'center',			classes:'text-ellipsis',
									formatter : function(cellvalue, options, rowObject) {
										var sViewExtnRqstYmdhms = rowObject.viewExtnRqstYmdhms;
										momentExtnRqstYmdhms = moment(sViewExtnRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
										return momentExtnRqstYmdhms.format('YYYY-MM-DD HH:mm');
									}
								}, { name:'rqstViewEndYmdhms',		align:'center',			classes:'text-ellipsis',
									formatter : function(cellvalue, options, rowObject) {
										var sRqstViewEndYmdhms = rowObject.rqstViewEndYmdhms;
										momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
										return momentRqstViewEndYmdhms.format('YYYY-MM-DD');
									}
								}, { name:'viewExtnRqstRsn',	classes:'text-ellipsis',
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
				},
				params : function() {
					//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					//var sViewRqstUserId = $('#view-rqst-dtl-viewRqstUserId').val();
					var oParams = {
						dstrtCd    : $('#view-rqst-dtl-dstrtCd').val(),
						viewRqstNo : $('#view-rqst-dtl-viewRqstNo').val()
						//, viewExtnRqstUserId: sViewRqstUserId
					};
					return oParams;
				}
			}
		},
		// oViewFirstAprv.view.aprv{}
		aprv : {
			id: 'modal-aprv',
			element: null,
			// oViewFirstAprv.view.aprv.open()
			open: function() {
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
						target : 'tvoBlank/view/div/viewRqstAprv',
					}, function() {
						//$('input[type=radio][name=view-rqst-aprv-tvoPrgrsCd]').prop('checked', false);
						$('input[type=radio][name=view-rqst-aprv-tvoPrgrsCd][value="50"]').prop('checked', true);
						$('#view-rqst-aprv-tvoPrgrsDtl').val("");
						var sViewEndYmdhmsWant = $('#view-rqst-dtl-viewEndYmdhmsWant').text();
						//var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD HH:mm:ss');
						var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD');
						if (momentViewEndYmdhmsWant.isValid()) {
							$('#view-rqst-aprv-viewEndYmdhms').val(sViewEndYmdhmsWant);
						}
						oTvoCmn.datetimepicker.ymd('.view-rqst-aprv-viewEndYmdhms', moment(), moment().add(30, 'days'));
					});
				} else {
					$(this.element).modal('show');
					//$('input[type=radio][name=view-rqst-aprv-tvoPrgrsCd]').prop('checked', false);
					$('input[type=radio][name=view-rqst-aprv-tvoPrgrsCd][value="50"]').prop('checked', true);
					$('#view-rqst-aprv-tvoPrgrsDtl').val("");
					var sViewEndYmdhmsWant = $('#view-rqst-dtl-viewEndYmdhmsWant').text();
					//var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD HH:mm:ss');
					var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD');
					if (momentViewEndYmdhmsWant.isValid()) {
						$('#view-rqst-aprv-viewEndYmdhms').val(sViewEndYmdhmsWant);
					}
					oTvoCmn.datetimepicker.ymd('.view-rqst-aprv-viewEndYmdhms', moment(), moment().add(30, 'days'));
				}
			},
			// oViewFirstAprv.view.aprv.close()
			close : function() {
				$('#' + oViewFirstAprv.view.aprv.id).modal('hide');
			},
			// oViewFirstAprv.view.aprv.approve()
			approve : function() {
				var sTvoPrgrsCd = $('input[name="view-rqst-aprv-tvoPrgrsCd"]:checked').val();
				
				if (!sTvoPrgrsCd) {		oCommon.modalAlert('modal-alert', '알림', '승인구분을 체크해 주세요.');	return;		}
				if ( sTvoPrgrsCd == '30' ) {
					if ( $('#view-rqst-aprv-tvoPrgrsDtl').val().trim() == '' ) {
						oCommon.modalAlert('modal-alert', '알림', '반려사유를 입력하세요.');	$('#view-rqst-aprv-tvoPrgrsDtl').focus();	return;
					}
				}
				
				//var sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
				//let sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				//var sTvoPrgrsDtl = $('#view-rqst-aprv-tvoPrgrsDtl').val();
				
				var sViewEndYmdhms = $('#view-rqst-aprv-viewEndYmdhms').val();
				if (sViewEndYmdhms && sTvoPrgrsCd == '50') {
					//var momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
					//sViewEndYmdhms = momentViewEndYmdhms.format('YYYYMMDDHHmmss');
					var momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD');
					sViewEndYmdhms = momentViewEndYmdhms.format('YYYYMMDD')+'235959';
				} else {
					sViewEndYmdhms = '';
				}

				var oParams = {
					tvoPrgrsCd : sTvoPrgrsCd,
					dstrtCd : $('#view-rqst-dtl-dstrtCd').val(),
					viewRqstNo : $('#view-rqst-dtl-viewRqstNo').val(),
					tvoPrgrsDtl : $('#view-rqst-aprv-tvoPrgrsDtl').val(),
					viewEndYmdhms : sViewEndYmdhms
				};

				var sFlag = $('input[name="view-rqst-aprv-tvoPrgrsCd"]:checked').parent().text().trim();
				oCommon.modalConfirm('modal-confirm', '알림', '열람신청을 ' + sFlag + '하시겠습니까?', function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/aprv/viewRqst/updateViewRqst.json',
						data : oParams,
						success : function(data) {
							console.log(data);
							if (data.result == '1') {
								oTvoCmn.div.clear('#view-rqst-aprv');
								oViewFirstAprv.view.rqst.grid.search();			// 목록 새로고침
								oViewFirstAprv.view.rqst.detail.init(oParams.dstrtCd, oParams.viewRqstNo);
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 ' + sFlag + '하였습니다.');
							} else {
								
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
					$('#' + oViewFirstAprv.view.aprv.id).modal('hide');
				});
			},
			// oViewFirstAprv.view.aprv.reset()
			reset : function() {
				//let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
				//let sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				var oParams = {
					dstrtCd    : $('#view-rqst-dtl-dstrtCd').val(),
					viewRqstNo : $('#view-rqst-dtl-viewRqstNo').val(),
					viewAprvUserId : $('#userId').val(),
					tvoPrgrsCd : '10',
					tvoPrgrsDtl : '',
					viewEndYmdhms : ''
				};
				let msg = '삭제된 정보는 복구할 수 없습니다.<br/><br/>승인 및 반려를 취소하고 신청상태로 변경하시겠습니까?';
				oCommon.modalConfirm('modal-confirm', '알림', msg, function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/view/aprv/resetViewRqst.json',
						data : oParams,
						success : function(data) {
							console.log(data);

							if (data.result == '1') {
								oTvoCmn.div.clear('#view-rqst-aprv');
								oViewFirstAprv.view.rqst.grid.search();			// 목록 새로고침
								oViewFirstAprv.view.rqst.detail.init(oParams.dstrtCd, oParams.viewRqstNo);
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 취소되었습니다.');
							} else {
								oCommon.modalAlert('modal-alert', '알림', data.errors);
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
				});
			},
			// oViewFirstAprv.view.aprv.deleteComplete()
			deleteComplete : function() {
				//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				//var sViewRqstUserId = $('#view-rqst-dtl-viewRqstUserId').val();
				var oParams = {
					dstrtCd    : $('#view-rqst-dtl-dstrtCd').val(),
					viewRqstNo : $('#view-rqst-dtl-viewRqstNo').val()
				};
				let msg = '삭제된 정보는 복구할 수 없습니다.<br/><br/>반출연장신청과 반출신청, 열람연장신청과 열람신청 및 관련파일을 완전히 삭제하시겠습니까?';
				oCommon.modalConfirm('modal-confirm', '알림', msg, function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/view/aprv/deleteCompleteViewRqst.json',
						data : oParams,
						success : function(data) {
							console.log(data);

							if (data.result == '1') {
								oTvoCmn.div.clear('#view-rqst-aprv');
								oViewFirstAprv.view.rqst.grid.search();			// 목록 새로고침
								//oViewFirstAprv.view.rqst.detail.init(sViewRqstNo);
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 삭제되었습니다.');
							} else {
								oCommon.modalAlert('modal-alert', '알림', data.errors);
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
				});
			}
		/*	// oViewFirstAprv.view.aprv.init()
			, init : function() {
				oDiv.setDiv('right', '1', {
					target : 'tvoBlank/view/div/viewRqstAprv',
				}, function() {
					var sViewEndYmdhmsWant = $('#view-rqst-dtl-viewEndYmdhmsWant').text();
					var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD HH:mm:ss');
					if (momentViewEndYmdhmsWant.isValid()) {
						$('#view-rqst-aprv-viewEndYmdhms').val(sViewEndYmdhmsWant);
					}
					oTvoCmn.datetimepicker.ymdhm('.view-rqst-aprv-viewEndYmdhms', moment(), moment().add(30, 'days'));
				});
			},
			// oViewFirstAprv.view.aprv.cancel()
			cancel : function() {
				oCommon.modalConfirm('modal-confirm', '알림', '열람신청 승인 창을 닫으시겠습니까?', function() {
					oTvoCmn.div.clear('#view-rqst-aprv');
				});
			}	*/
		},
		// oViewFirstAprv.view.aprvMulti{}
		aprvMulti : {
			id: 'modal-aprv-multi',
			//val: '',
			element: null,
			// oViewFirstAprv.view.aprvMulti.open()
			open: function() {
				var selRow =  $.getSelRow('#grid-' + oViewFirstAprv.view.rqst.grid.id);		//alert(selRow.length);
				if(selRow.length == 0){
					oCommon.modalAlert('modal-alert', '알림', '승인할 신청을 선택해 주세요.');	return;
				}
				
				let tmpVal = "";
				for(var i = 0; i < selRow.length; i++) {
					var list = jQuery('#grid-' + oViewFirstAprv.view.rqst.grid.id).getRowData(selRow[i]);
					if ( '10' == list.tvoPrgrsCd ) {		// 신청일 때
						tmpVal += "," + list.viewRqstNo;
					} else {

					}
				}
				if( '' == tmpVal ) {
					oCommon.modalAlert('modal-alert', '알림', '진행상태가 신청인 건을 선택해 주세요.');	return;
				} else {
					//this.val = tmpVal.substring(1);
					oCommon.modalAlert('modal-alert', '알림', '승인대상 '+tmpVal.substring(1).split(',').length+'건입니다.');
				}
						
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
						target : 'tvoBlank/view/div/viewRqstAprvMulti',
					}, function() {
						$('input[type=radio][name=view-rqst-aprv-tvoPrgrsCd]').prop('checked', false);
						$('#view-rqst-aprv-tvoPrgrsDtl').val("");
					});
				} else {
					$(this.element).modal('show');
					$('input[type=radio][name=view-rqst-aprv-tvoPrgrsCd]').prop('checked', false);
					$('#view-rqst-aprv-tvoPrgrsDtl').val("");
				}
			},
			// oViewFirstAprv.view.aprvMulti.close()
			close : function() {
				$('#' + oViewFirstAprv.view.aprvMulti.id).modal('hide');
			},
			// oViewFirstAprv.view.aprvMulti.approve()
			approve : function() {
				let sFlag = '승인';
				var sTvoPrgrsCd = $('input[name="view-rqst-aprv-multi-tvoPrgrsCd"]:checked').val();
				if (!sTvoPrgrsCd) {		oCommon.modalAlert('modal-alert', '알림', '승인구분을 체크해 주세요.');	return;		}
				if ( sTvoPrgrsCd == '30' ) {
					sFlag = '반려';
					if ( $('#view-rqst-aprv-multi-tvoPrgrsDtl').val().trim() == '' ) {
						oCommon.modalAlert('modal-alert', '알림', '반려사유를 입력하세요.');	$('#view-rqst-aprv-multi-tvoPrgrsDtl').focus();	return;
					}
				}
				
				var selRow =  $.getSelRow('#grid-' + oViewFirstAprv.view.rqst.grid.id);		//alert(selRow.length);
				if(selRow.length == 0){
					oCommon.modalAlert('modal-alert', '알림', '승인할 신청을 선택해 주세요.');	return;
				}
				
				let sAprvInfo = "";
				for(var i = 0; i < selRow.length; i++) {
					var list = jQuery('#grid-' + oViewFirstAprv.view.rqst.grid.id).getRowData(selRow[i]);
					if ( '10' == list.tvoPrgrsCd ) {		// 신청일 때
						sAprvInfo += "," + list.viewRqstNo;
					}
				}
				//alert(sAprvInfo.substring(1));
				
				var sTvoPrgrsDtl = $('#view-rqst-aprv-multi-tvoPrgrsDtl').val();

				var oParams = {
					viewRqstNo : sAprvInfo.substring(1),
					tvoPrgrsCd : sTvoPrgrsCd,
					tvoPrgrsDtl : sTvoPrgrsDtl,
				};

				oCommon.modalConfirm('modal-confirm', '알림', '선택한 신청을 ' + sFlag + '하시겠습니까?', function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/view/aprv/approveViewRqst.json',
						data : oParams,
						success : function(data) {
							console.log(data);

							if (data.result == '1') {
								oTvoCmn.div.clear('#view-rqst-aprv');
								oViewFirstAprv.view.rqst.grid.search();			// 목록 새로고침
								//oViewFirstAprv.view.rqst.detail.init(sViewRqstNo);
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 ' + sFlag + '하였습니다.');
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
					$('#' + oViewFirstAprv.view.aprvMulti.id).modal('hide');
				});
			}
		},

	}

}
