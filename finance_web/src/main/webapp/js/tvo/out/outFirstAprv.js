$(function() {
	oOutFirstAprv = new outFirstAprv();
	oOutFirstAprv.init();
});

function outFirstAprv() {
	this.init = function() {
        // LEFT 조절
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			right : true
		}, function() {
			oTvoCmn.datetimepicker.ymd('.aprv-out-outRqstYmdhms', '2023-01', moment().add(100, 'years'));	// 반출신청일
			//oTvoCmn.datetimepicker.ymd('.aprv-out-playEndYmdhms', '2023-01', moment().add(100, 'years'));	// 재생종료일
			codeInfoList('#aprv-out-rqstRsnTyCd', 'RQST_RSN_TY', '', '신청사유');
			codeInfoList('#aprv-out-tvoPrgrsCd', 'OUT_PRGRS', '', '진행상태');

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
			oOutFirstAprv.out.rqst.grid.init();
		});
	};

	// oOutFirstAprv.out{}
	this.out = {
		// oOutFirstAprv.out.rqst{}
		rqst : {
			// oOutFirstAprv.out.rqst.grid{}
			grid : {
				id : 'aprv-out',
				page: '1',
				// oOutFirstAprv.out.rqst.grid.init()
				init : function() {
					var sId = oOutFirstAprv.out.rqst.grid.id;
					var oGridParams = oOutFirstAprv.out.rqst.grid.params();
					var sGridId = '#grid-' + oOutFirstAprv.out.rqst.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/out/selectOutRqstAprvList.json',
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
							oOutFirstAprv.out.rqst.grid.page = data.page;
							//var $prgrs = $('#aprv-out-prgrs');
							//if ($prgrs.val() != '') {
							//	$prgrs.val('');
							//	history.replaceState({}, null, location.pathname);
							//}
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : ['' //'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#gview_grid-aprv-out\', this, event);">'
							, 'dstrtCd', '신청지구', 'viewRqstNo', '반출신청번호', '신청일자', '재생종료', '신청사유', '신청자', '사건번호', '사건명'
							, '카메라명', '마스킹', '제3자제공', '영상처리코드', '영상처리', '진행상태코드', '진행상태', '상세'
						],
						colModel : [
							{ name: 'CHECK'          , align:'center', width:20, editable:true, edittype:'checkbox'
								, editoptions: { value:"True:False" }, sortable: false	, formatter: $.GridCheckBox  , hidden: true
							}, { name:'dstrtCd'      , hidden: true
							}, { name:'dstrtNm'      , align:'center', width:60
							}, { name:'viewRqstNo'    , hidden:true
							}, { name:'outRqstNo'     , align:'center',	width:60
							    , formatter: function (cellvalue, options, rowObject) { return rowObject.outRqstNo.substring(5); }
							}, { name:'outRqstYmdhms' , align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sYmdhms = rowObject.outRqstYmdhms;
									var momentYmdhms = moment(sYmdhms, 'YYYY-MM-DD HH:mm:ss');
									if (momentYmdhms.isValid()) {
										return momentYmdhms.format('YY-MM-DD');
									} else {
										return '';
									}
								}
							}, {name:'playEndYmdhms',	align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sYmdhms = rowObject.playEndYmdhms;
									var momentYmdhms = moment(sYmdhms, 'YYYY-MM-DD HH:mm:ss');
									if (momentYmdhms.isValid()) {
										return momentYmdhms.format('YY-MM-DD');
									} else {
										return '';
									}
								}
							}, {name:'rqstRsnTyNm',		align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'outRqstUserNm',	align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'evtNo',			align:'center',	width:100,	cellattr : function() {	return 'style="width:100px;"';	}
							}, {name:'evtNm',			align:'left',	width:100,	cellattr : function() {	return 'style="width:100px;"';	}
							}, {name:'fcltLblNm',		align:'left',	width:100,	cellattr : function() {	return 'style="width:100px;"';	}
							   ,classes:'text-ellipsis'
							}, {name:'maskingYn',		align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'thirdPartyYn',	align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'outChkStepCd',	hidden:true
							}, {name:'outChkStepNm',	align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'tvoPrgrsCd',		hidden:true
							}, {name:'tvoPrgrsNm',		align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'outRqstDtl',		align:'center',	width:50 ,	cellattr : function() {	return 'style="width:50px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var $button = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs',
										'title' : '반출신청 내용을 조회합니다.',
										'html' : '보기',
										'onclick' : 'javascript:oOutFirstAprv.out.rqst.detail.init("' + rowObject.dstrtCd + '", "' + rowObject.outRqstNo + '", "' + rowObject.viewRqstNo + '");'
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
		            $('#btn-refresh').on('click', () => oOutFirstAprv.out.rqst.grid.search());
					
				},
				// oOutFirstAprv.out.rqst.grid.search()
				search : function(page) {
					if (typeof page != 'undefined') {
						oOutFirstAprv.out.rqst.grid.page = page;
					}
						
					var sFr = $('#aprv-out-outRqstYmdhms-fr').val();
					var sTo = $('#aprv-out-outRqstYmdhms-to').val();
					if ( sFr!='' && sTo!='' ) {
						if ( sFr > sTo ) {
							alert('기간설정에 오류가 있습니다.');	return;
						}
					}
					var sId = oOutFirstAprv.out.rqst.grid.id;
					var sPage = oOutFirstAprv.out.rqst.grid.page;		//alert(sPage);
					var oGridParams = oOutFirstAprv.out.rqst.grid.params();
					//if (typeof prgrs != 'undefined') {
					//	oGridParams.prgrs = prgrs;
					//}
					$('article div.col').empty();	// 상세화면 비우기
					oTvoCmn.jqGrid.reload(sId, sPage, oGridParams);
				},

				params : function() {

					var sOutRqstYmdhmsFr = $('#aprv-out-outRqstYmdhms-fr').val();
					var momentOutRqstYmdhmsFr = moment(sOutRqstYmdhmsFr, 'YYYY-MM-DD');
					if (momentOutRqstYmdhmsFr.isValid()) {
						sOutRqstYmdhmsFr = momentOutRqstYmdhmsFr.format('YYYYMMDD');
					} else {
						sOutRqstYmdhmsFr = '';
					}

					var sOutRqstYmdhmsTo = $('#aprv-out-outRqstYmdhms-to').val();
					var momentOutRqstYmdhmsTo = moment(sOutRqstYmdhmsTo, 'YYYY-MM-DD');
					if (momentOutRqstYmdhmsTo.isValid()) {
						sOutRqstYmdhmsTo = momentOutRqstYmdhmsTo.format('YYYYMMDD');
					} else {
						sOutRqstYmdhmsTo = '';
					}

					/*	var sPlayEndYmdhms = $('#aprv-out-playEndYmdhms').val();
                        momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
                        if (momentPlayEndYmdhms.isValid()) {
                            sPlayEndYmdhms = momentPlayEndYmdhms.format('YYYYMMDD');
                        } else {
                            sPlayEndYmdhms = '';
                        }	*/
					//var sPrgrs = $('#aprv-out-prgrs').val();
					//var sTvoPrgrsCd = $('#aprv-out-tvoPrgrsCd option:selected').val();
					//var sRqstRsnTyCd = $('#aprv-out-rqstRsnTyCd option:selected').val();
					//var sEvtNo = $('#aprv-out-evtNo').val();
					//var sEvtNm = $('#aprv-out-evtNm').val();
					//var sFcltLblNm = $('#aprv-out-fcltLblNm').val();

					var oParams = {
						//prgrs : sPrgrs,
						outRqstYmdhmsFr : sOutRqstYmdhmsFr,
						outRqstYmdhmsTo : sOutRqstYmdhmsTo,
						//playEndYmdhms : sPlayEndYmdhms,
						tvoPrgrsCd    : $('#aprv-out-tvoPrgrsCd option:selected').val(),
						rqstRsnTyCd   : $('#aprv-out-rqstRsnTyCd option:selected').val(),
						evtNo         : $('#aprv-out-evtNo').val(),
						evtNm         : $('#aprv-out-evtNm').val(),
						fcltLblNm     : $('#aprv-out-fcltLblNm').val(),
						outRqstUserId : $('#aprv-out-rqstUserId').val()
					};

					return oParams;
				}
			},
			// oOutFirstAprv.out.rqst.detail{}
			detail : {
				// oOutFirstAprv.out.rqst.detail.init()
				init : function(pDstrtCd, pOutRqstNo, pViewRqstNo) {
					$('article div.col').empty();	// 상세화면 비우기
					oDiv.setDiv('left', '0', {
						target : 'tvoBlank/out/div/outRqstDtl',				// 반출신청상세
						dstrtCd : pDstrtCd,
						outRqstNo : pOutRqstNo
					}, function() {
						setTimeout(function() {
							console.log("-- oTvoConfig.recommVdoDuration => "+oTvoConfig.recommVdoDuration);
							let recommVdoDuration = oTvoConfig.recommVdoDuration;	// 권장 반출영상 길이(분)
							let sVdoYmdhmsFr = $('#out-rqst-dtl-vdoYmdhmsFr').val();
							let sVdoYmdhmsTo = $('#out-rqst-dtl-vdoYmdhmsTo').val();
							var momentVdoYmdhmsFr = moment(sVdoYmdhmsFr, 'YYYYMMDDHHmmss');
							var momentVdoYmdhmsTo = moment(sVdoYmdhmsTo, 'YYYYMMDDHHmmss');
							let vdoYmdHmsDuration = momentVdoYmdhmsTo.diff(momentVdoYmdhmsFr,"minutes");
							if ( recommVdoDuration < Number(vdoYmdHmsDuration) ) {
								$('#out-rqst-dtl-vdoYmdHmsDuration').attr("style","color:red;");
							}
							vdoYmdHmsDuration = "("+Math.floor(vdoYmdHmsDuration/60)+":"+vdoYmdHmsDuration%60+")";
							$('#out-rqst-dtl-vdoYmdHmsDuration').text(vdoYmdHmsDuration);
							
							var sPlayStartYmdhms = $('#out-rqst-dtl-playStartYmdhms').text();		// 재생시작일시
							var sPlayEndYmdhms = $('#out-rqst-dtl-playEndYmdhms').text();			// 재생종료일시
							var momentPlayStartYmdhms = moment(sPlayStartYmdhms, 'YYYY-MM-DD');
							var momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
							let playDuration = momentPlayEndYmdhms.diff(momentPlayStartYmdhms,"days");
							$('#out-rqst-dtl-playDuration').text("("+playDuration+"일)");
							
							
							let tvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();	// 반출신청진행상태코드
							let outChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();	// 영상처리코드
							var sMaskingYn = $('#out-rqst-dtl-maskingYn').text().trim();
							
							if ( 'N' == $('#out-rqst-dtl-outFileDelYn').val() ) {
								$('#out-rqst-dtl-vmsView').removeClass('hide');
							}
							
							if ( '30' != tvoPrgrsCd ) {	// 반려 아닐 때
								if ( '25'==outChkStepCd || '27'==outChkStepCd ) {		// 입수중 or 영상입수실패
									$('#out-rqst-dtl-retryGetVdo').removeClass('hide');
								}
								if ( '77'==outChkStepCd ) {		// DRM실패
									$('#out-rqst-dtl-retryDrm').removeClass('hide');
								}	
								if ( '27'==outChkStepCd || '77'==outChkStepCd ) {		// 영상입수실패 or DRM실패
									$('#out-rqst-dtl-doReject').removeClass('hide');
								}
							}
							if ( '70'==tvoPrgrsCd || '71'==tvoPrgrsCd ) {		// 승인 or 자동승인
								//$('#out-rqst-dtl-checkHash').removeClass('hide');		// 암호화영상원본대조
								$('#out-rqst-dtl-downloadPlayer').removeClass('hide');
							}
							
							if (sMaskingYn == 'Y') {
								if(40 <= tvoPrgrsCd && tvoPrgrsCd <= 55) {		// 입수완료 후 반출승인 전
									if (25 <= outChkStepCd) {	// 입수중
										$('#out-rqst-dtl-downloadMasker').removeClass('hide');
									}
								}
							}
							

							oOutFirstAprv.out.file.grid.init();						// 반출파일목록

							// 재생종료일시
							var sViewEndYmdhms = $('#view-rqst-dtl-viewEndYmdhms').text();		// 승인
							if ( sViewEndYmdhms == '' ) {
								sViewEndYmdhms = $('#view-rqst-dtl-viewEndYmdhmsWant').text();	// 희망
							}
							var momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
							if (momentViewEndYmdhms.isValid()) {
								$('#out-rqst-aprv-playEndYmdhms').val(sViewEndYmdhms);
							}
							oTvoCmn.datetimepicker.ymdhm('.out-rqst-aprv-playEndYmdhms', '2023-01', moment().add(100, 'years'));

							var sPlayCnt = $('#out-rqst-dtl-playCnt').text();
							if (!isNaN(sPlayCnt)) {
								$('#out-rqst-aprv-playCnt').val(sPlayCnt);
							}
							
							//console.log("-- oTvoConfig.orgVdoAutoRgsYn => "+oTvoConfig.orgVdoAutoRgsYn);
							//console.log("-- tvoPrgrsCd => "+tvoPrgrsCd);
							if ('N' == oTvoConfig.orgVdoAutoRgsYn && ('50'==tvoPrgrsCd || '51'==tvoPrgrsCd) ) {	// 원본영상 자동등록 아닐 때 && 입수승인 or 자동입수승인 일 때
								$("#out-rqst-dtl-file-upload").removeClass('hide');
								//codeInfoList('#out-rqst-dtl-outChkStepCd-select', 'OUT_CHK_STEP', '30', '반출단계');	// 반출단계
							//} else {
								//$("#out-rqst-dtl-file-upload").addClass('hide');
							}

						}, 500);
						
						setTimeout(function() {
							$("#out-rqst-dtl-map-pin").trigger("click");		// 지도의 카메라위치로 이동한다.
						}, 500);
					});

					oDiv.setDiv('left', '1', {
						target : 'tvoBlank/out/div/outExtnList',			// 반출기간연장이력
						dstrtCd : pDstrtCd,
						outRqstNo : pOutRqstNo
					}, function() {
						setTimeout(function() {
							oOutFirstAprv.out.prodExtn.grid.init();
						}, 500);
					});
					
					oDiv.setDiv('left', '2', {
						target : 'tvoBlank/view/div/viewRqstDtl',				// 열람신청상세
						dstrtCd : pDstrtCd,
						viewRqstNo : pViewRqstNo
					});
				},
				masking : function() {

				}
			},
		},
		// oOutFirstAprv.out.prodExtn{}
		prodExtn : {
			grid : {
				id : 'out-prod-extn-his',
				// oOutFirstAprv.out.prodExtn.grid.init()
				init : function() {
					var sId = oOutFirstAprv.out.prodExtn.grid.id;
					var oGridParams = oOutFirstAprv.out.prodExtn.grid.params();
					var sGridId = '#grid-' + oOutFirstAprv.out.prodExtn.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/rqst/outRqst/selectOutProdExtnHisList.json',
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
						colNames : [	'#', 'outRqstNo', '신청일시', '재생종료', '사유', '승인'
						],
						colModel : [
								{	name:'rnum',	align:'center',	width:30,		hidden:true
								   ,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:30px;"';		}
								}, {name:'outRqstNo',								hidden:true
								}, {name:'outExtnRqstYmdhms',	align:'center',		classes:'text-ellipsis',
									formatter : function(cellvalue, options, rowObject) {
										var sOutExtnRqstYmdhms = rowObject.outExtnRqstYmdhms;
										momentYmdhms = moment(sOutExtnRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
										sOutExtnRqstYmdhms = momentYmdhms.format('YYYY-MM-DD HH:mm');
										return sOutExtnRqstYmdhms;
									}
								}, {name:'rqstPlayEndYmdhms',		align:'center',		classes:'text-ellipsis',
									formatter : function(cellvalue, options, rowObject) {
										var sRqstPlayEndYmdhms = rowObject.rqstPlayEndYmdhms;
										momentYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
										sRqstPlayEndYmdhms = momentYmdhms.format('YYYY-MM-DD');
										return sRqstPlayEndYmdhms;
									}
								}, {name:'outExtnRqstRsn',
								}, {name:'tvoPrgrsNm',		align:'center',				width:60
									,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:60px;"';	}
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
					//var sOutRqstNo = $('#out-rqst-dtl-outRqstNo').val();
					var oParams = {
						dstrtCd   : $('#out-rqst-dtl-dstrtCd').val(),
						outRqstNo : $('#out-rqst-dtl-outRqstNo').val()
					};
					return oParams;
				}
			}
		},
		// oOutFirstAprv.out.aprv{}
		aprv : {
			id: 'modal-aprv',
			element: null,
			// oOutFirstAprv.out.aprv.open()
			open: function(option) {
				console.log('-- oOutFirstAprv.out.aprv.open(), option : %s', option);
				
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
						target : 'tvoBlank/out/div/outRqstAprv',
					}, function() {
						//$('input[type=radio][name=out-rqst-aprv-yn]').prop('checked', false);
						$('input[type=radio][name=out-rqst-aprv-yn][value="Y"]').prop('checked', true);
						$('#out-rqst-aprv-tvoPrgrsDtl').val("");
						
						let tvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();
						console.log("-- tvoPrgrsCd : "+tvoPrgrsCd);
						if ( tvoPrgrsCd=='10' ) {
							$("#out-rqst-aprv .panel-heading .panel-title").text("입수승인");
						
							//var sPlayStartYmdhms = $('#out-rqst-dtl-playStartYmdhms').text();		// 재생시작일시
							var sPlayEndYmdhms = $('#out-rqst-dtl-playEndYmdhms').text();			// 재생종료일시
							var momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
							if (momentPlayEndYmdhms.isValid()) {
								$('#out-rqst-aprv-playEndYmdhms').val(sPlayEndYmdhms);
							}
							oTvoCmn.datetimepicker.ymd('.out-rqst-aprv-playEndYmdhms', moment(), moment().add(100, 'years'));
							//$('#tr-out-rqst-aprv-playEndYmdhms').attr("style","display:;");
							$('#tr-out-rqst-aprv-playEndYmdhms').removeClass('hide');
							
						} else if ( tvoPrgrsCd=='50' || tvoPrgrsCd=='51') {
							$("#out-rqst-aprv .panel-heading .panel-title").text("반출승인");
							//$('#tr-out-rqst-aprv-playEndYmdhms').attr("style","display:none;");
							$('#tr-out-rqst-aprv-playEndYmdhms').addClass('hide');
						}		
					});
				} else {
					$(this.element).modal('show');
					//$('input[type=radio][name=out-rqst-aprv-yn]').prop('checked', false);
					$('input[type=radio][name=out-rqst-aprv-yn][value="Y"]').prop('checked', true);
					$('#out-rqst-aprv-tvoPrgrsDtl').val("");
					
					let tvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();
					console.log("-- tvoPrgrsCd : "+tvoPrgrsCd);
					if ( tvoPrgrsCd=='10' ) {
						$("#out-rqst-aprv .panel-heading .panel-title").text("입수승인");

						var sPlayEndYmdhms = $('#out-rqst-dtl-playEndYmdhms').text();				// 재생종료일시
						//var momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						var momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
						if (momentPlayEndYmdhms.isValid()) {
							$('#out-rqst-aprv-playEndYmdhms').val(sPlayEndYmdhms);
						}
						oTvoCmn.datetimepicker.ymdhm('.out-rqst-aprv-playEndYmdhms', '2023-01', moment().add(100, 'years'));
						//$('#tr-out-rqst-aprv-playEndYmdhms').attr("style","display:;");
						$('#tr-out-rqst-aprv-playEndYmdhms').removeClass('hide');

					} else if ( tvoPrgrsCd=='50' || tvoPrgrsCd=='51') {
						$("#out-rqst-aprv .panel-heading .panel-title").text("반출승인");
						//$('#tr-out-rqst-aprv-playEndYmdhms').attr("style","display:none;");
						$('#tr-out-rqst-aprv-playEndYmdhms').addClass('hide');
					}
				}
				
				setTimeout(function () {
					if ( 'REJECT' == option ) {
						$("input:radio[name='out-rqst-aprv-yn']:radio[value=N]").prop('checked', true);
						$('input[type=radio][name=out-rqst-aprv-yn]').attr('disabled','true');
					} else {
						$('input[type=radio][name=out-rqst-aprv-yn]').removeAttr('disabled');
					}
				}, 500);
				
				
			/*	// 열람종료일시
				var sViewEndYmdhms = $('#view-rqst-dtl-viewEndYmdhms').text();
				var momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
				if (momentViewEndYmdhms.isValid()) {
					$('#out-rqst-aprv-viewEndYmdhms').val(sViewEndYmdhms);
				}
				oTvoCmn.datetimepicker.ymdhm('.out-rqst-aprv-viewEndYmdhms', '2023-01', moment().add(100, 'years'));

				var sPlayCnt = $('#out-rqst-dtl-playCnt').text();
				if (!isNaN(sPlayCnt)) {
					$('#out-rqst-aprv-playCnt').val(sPlayCnt);
				}	*/
				
			},
			// oOutFirstAprv.out.aprv.close()
			close : function() {
				$('#' + oOutFirstAprv.out.aprv.id).modal('hide');
			},
			// oOutFirstAprv.out.aprv.approve()
			approve : function() {
				var $aprvYn = $('input[name="out-rqst-aprv-yn"]:checked');		// 승인구분
				if (!$aprvYn.val()) {		oCommon.modalAlert('modal-alert', '알림', '승인구분을 체크해 주세요.');	return;	}
				if ( $aprvYn.val() == 'N' ) {
					if ( $('#out-rqst-aprv-tvoPrgrsDtl').val().trim() == '' ) {
						oCommon.modalAlert('modal-alert', '알림', '반려사유를 입력하세요.');	$('#out-rqst-aprv-tvoPrgrsDtl').focus();	return;
					}
				}

				var sTvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();		// 진행상태코드
				var sAprvNm = '';
				if (sTvoPrgrsCd == '10') {									// 신청
					sAprvNm = '입수를 ';
				} else if (sTvoPrgrsCd == '50' || sTvoPrgrsCd == '51') {	// 입수승인 or 자동입수승인
					sAprvNm = '반출을 ';
				}
				
				var newTvoPrgrsCd = '';		// 새로운진행상태코드
				//let sFlag = $aprvYn.parent().text().trim();	// 승인/반려
				let sFlag = '';
				if ($aprvYn.val() == 'Y' && sTvoPrgrsCd == '10') {		// 승인 and 신청
					newTvoPrgrsCd = '50';										// 입수승인
					sFlag = '승인';
				} else if ($aprvYn.val() == 'Y' && (sTvoPrgrsCd == '50' || sTvoPrgrsCd == '51')) {	// 승인 and (입수승인 or 자동입수승인)
					newTvoPrgrsCd = '70';										// 반출승인
					sFlag = '승인';
				} else {
					newTvoPrgrsCd = '30';										// 반려
					sFlag = '반려';
				}

			//	var sDstrtCd = $('#view-rqst-dtl-dstrtCd').val();
			//	var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
			//	var sOutRqstNo = $('#out-rqst-dtl-outRqstNo').val();
			//	var sOutRqstYmdhms = $('#out-rqst-dtl-outRqstYmdhms').val();
				
			//	var sTvoPrgrsDtl = $('#out-rqst-aprv-tvoPrgrsDtl').val();
			//	var sPlayCnt = $('#out-rqst-aprv-playCnt').val();
			//	var sPlayStartYmdhms = $('#out-rqst-aprv-playStartYmdhms').val();	// 재생시작일시
				var sPlayEndYmdhms = $('#out-rqst-aprv-playEndYmdhms').val();		// 재생종료일시
				if (sPlayEndYmdhms && newTvoPrgrsCd == '50') {			// 입수승인
					let momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
					sPlayEndYmdhms = momentPlayEndYmdhms.format('YYYYMMDD')+'235959';
				} else {
					sPlayEndYmdhms = '';
				}

				var oParams = {
					dstrtCd    : $('#out-rqst-dtl-dstrtCd').val(),
					viewRqstNo : $('#out-rqst-dtl-viewRqstNo').val(),
					outRqstNo  : $('#out-rqst-dtl-outRqstNo').val(),
					outRqstYmdhms : $('#out-rqst-dtl-outRqstYmdhms').val(),
					tvoPrgrsCd : newTvoPrgrsCd
				};

				if (newTvoPrgrsCd == '30') {			// 반려
					oParams.tvoPrgrsDtl = $('#out-rqst-aprv-tvoPrgrsDtl').val();
					
				} else if (newTvoPrgrsCd == '50') {		// 입수승인
					oParams.cctvId = $('#out-rqst-dtl-cctvId').val();
					oParams.linkVmsUid = $('#out-rqst-dtl-linkVmsUid').val();
					
					var momentFr = moment($('#out-rqst-dtl-vdoYmdhmsFr').val(), 'YYYY-MM-DD HH:mm:ss');
					oParams.vdoYmdhmsFr = momentFr.format('YYYYMMDDHHmmss');
					var momentTo = moment($('#out-rqst-dtl-vdoYmdhmsTo').val(), 'YYYY-MM-DD HH:mm:ss');
					oParams.vdoYmdhmsTo = momentTo.format('YYYYMMDDHHmmss');
					
					oParams.playEndYmdhms   = sPlayEndYmdhms;	// 필요없는 정보로 추측되니 확인 후 삭제 요망
					
				} else if (newTvoPrgrsCd == '70') {		// 반출승인

				}

				oCommon.modalConfirm('modal-confirm', '알림', sAprvNm + sFlag + '하시겠습니까?', function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/out/aprv/updateOutRqst.json',
						data : oParams,
						success : function(data) {
							if (data.result == '1') {
								oTvoCmn.div.clear('#out-rqst-aprv');
								oOutFirstAprv.out.rqst.grid.search();			// 목록 새로고침
								oOutFirstAprv.out.rqst.detail.init(oParams.dstrtCd, oParams.outRqstNo, oParams.viewRqstNo);
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 ' + sFlag + '하였습니다.');
							} else {
								oCommon.modalAlert('modal-alert', '알림', data.errors);
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
					$('#' + oOutFirstAprv.out.aprv.id).modal('hide');
				});
			},
			// oOutFirstAprv.out.aprv.reset()
			reset : function() {
				//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				//var sOutRqstNo = $('#out-rqst-dtl-outRqstNo').val();
				var oParams = {
					dstrtCd    : $('#out-rqst-dtl-dstrtCd').val(),
					viewRqstNo : $('#out-rqst-dtl-viewRqstNo').val(),
					outRqstNo  : $('#out-rqst-dtl-outRqstNo').val(),
					tvoPrgrsCd : '10',
					tvoPrgrsDtl : '',
					outChkStepCd : '10',
					outChkYmdhms : ''
				};
				let msg = '이미 진행된 영상처리는 되돌릴 수 없습니다.<br/><br/>승인 및 반려를 취소하고 신청상태로 변경하시겠습니까?';
				oCommon.modalConfirm('modal-confirm', '알림', msg, function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/out/aprv/resetOutRqst.json',
						data : oParams,
						success : function(data) {
							if (data.result == '1') {
								oTvoCmn.div.clear('#out-rqst-aprv');
								oOutFirstAprv.out.rqst.grid.search();			// 목록 새로고침
								oOutFirstAprv.out.rqst.detail.init(oParams.dstrtCd, oParams.outRqstNo, oParams.viewRqstNo);
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
			// oOutFirstAprv.out.aprv.deleteComplete()
			deleteComplete : function() {
				//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				//var sOutRqstNo  = $('#out-rqst-dtl-outRqstNo').val();
				//var sOutRqstUserId = $('#out-rqst-dtl-outRqstUserId').val();
				var oParams = {
					dstrtCd   : $('#out-rqst-dtl-dstrtCd').val(),
					outRqstNo : $('#out-rqst-dtl-outRqstNo').val(),
					//outRqstUserId : $('#out-rqst-dtl-outRqstUserId').val()
				};
				let msg = '삭제된 정보는 복구할 수 없습니다.<br/><br/>반출연장신청과 반출신청 및 관련 파일을 완전히 삭제하시겠습니까?';
				oCommon.modalConfirm('modal-confirm', '알림', msg, function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/out/aprv/deleteCompleteOutRqst.json',
						data : oParams,
						success : function(data) {
							if (data.result == '1') {
								oTvoCmn.div.clear('#out-rqst-aprv');
								oOutFirstAprv.out.rqst.grid.search();			// 목록 새로고침
								//oOutFirstAprv.out.rqst.detail.init(sOutRqstNo, sViewRqstNo);
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
			},
		/*	// oOutFirstAprv.out.aprv.init()
			init : function() {
				oDiv.setDiv('right', '1', {
					target : 'tvoBlank/aprv/div/outRqstAprv',
				}, function() {
					// 재생종료일시
					var sPlayEndYmdhms = $('#out-rqst-dtl-playEndYmdhms').text();
					var momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
					if (momentPlayEndYmdhms.isValid()) {
						$('#out-rqst-aprv-playEndYmdhms').val(sPlayEndYmdhms);
					}
					oTvoCmn.datetimepicker.ymdhm('.out-rqst-aprv-playEndYmdhms', '2023-01', moment().add(100, 'years'));
					// 열람종료일시
					var sViewEndYmdhms = $('#view-rqst-dtl-viewEndYmdhms').text();
					var momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
					if (momentViewEndYmdhms.isValid()) {
						$('#out-rqst-aprv-viewEndYmdhms').val(sViewEndYmdhms);
					}
					oTvoCmn.datetimepicker.ymdhm('.out-rqst-aprv-viewEndYmdhms', '2023-01', moment().add(100, 'years'));

					var sPlayCnt = $('#out-rqst-dtl-playCnt').text();
					if (!isNaN(sPlayCnt)) {
						$('#out-rqst-aprv-playCnt').val(sPlayCnt);
					}
				});
			},
			// oOutFirstAprv.out.aprv.cancel()
			cancel : function() {
				oCommon.modalConfirm('modal-confirm', '알림', '반출신청 승인 창을 닫으시겠습니까?', function() {
					oTvoCmn.div.clear('#out-rqst-aprv');
				});
			},
		*/
			// oOutFirstAprv.out.aprv.uploadOrgVdo()		원본영상 업로드
			uploadOrgVdo : function() {
				
				let oFiles = $('#orgVdo')[0].files;
				if (!oFiles.length) {	oCommon.modalAlert('modal-alert', '알림', '파일을 첨부하세요.');	return;	}
				
				// 파일 확장자 체크
				let nm = $('#orgVdo').val();					//console.log(nm);
				let ex = nm.substring(nm.indexOf('.') + 1).toLowerCase();	//console.log(ex);
				let ty = "avi, mp4";						//console.log(ty.indexOf(ex));
				if (ty.indexOf(ex) == -1) {
					oCommon.modalAlert('modal-alert', '알림', ty + ' 확장자를 가진 파일만 첨부가능합니다.');
					return;
				}
					
				//let sDstrtCd    = $('#out-rqst-dtl-dstrtCd').val();
				//var sOutRqstNo  = $('#out-rqst-dtl-outRqstNo').val();
				//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				//var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd-select').val();
				//var sCctvId = $('#out-rqst-dtl-cctvId').val();
				//var sOutVdoYmdhmsFr = $('#out-rqst-dtl-vdoYmdhmsFrOrgn').val();
				//var sOutVdoYmdhmsTo = $('#out-rqst-dtl-vdoYmdhmsToOrgn').val();
				
				let oParams = new FormData();
				oParams.append('dstrtCd'   , $('#out-rqst-dtl-dstrtCd').val()   );
				oParams.append('viewRqstNo', $('#out-rqst-dtl-viewRqstNo').val());
				oParams.append('outRqstNo' , $('#out-rqst-dtl-outRqstNo').val() );
				oParams.append('outFileSeq', '0');		// 파일을 하나만 첨부한다.
				//oParams.append('outChkStepCd', sOutChkStepCd);
				oParams.append('cctvId'        , $('#out-rqst-dtl-cctvId').val()         );
				oParams.append('outVdoYmdhmsFr', $('#out-rqst-dtl-vdoYmdhmsFrOrgn').val());
				oParams.append('outVdoYmdhmsTo', $('#out-rqst-dtl-vdoYmdhmsToOrgn').val());
				oParams.append('action', "DEL_FILE");	// 기존파일삭제
				/*
				let tvoUrl = "http://"+window.location.host+contextRoot;
				if ( window.location.protocol.indexOf('https') != -1 ) {
					tvoUrl = "https://"+window.location.host+contextRoot;
				}
				console.log('tvoUrl = '+tvoUrl);
				oParams.append('tvoUrl', tvoUrl);
				*/
				if (oFiles.length) {
					$.each(oFiles, function (index, file) {
						oParams.append('orgVdo-' + index, file);
					});
				}
				
				oCommon.modalConfirm('modal-confirm', '알림', '새로운 원본파일을 전송하면 이전 영상은 삭제됩니다.<br/><br/>계속 진행하시겠습니까?', function() {
					olSwipMap.spinner.spin(document.getElementById('out-rqst-dtl'));
					//$('#container-modal').removeClass('hide');
					setTimeout(() => {
						$.ajax({
							type: 'POST',
							enctype: 'multipart/form-data',
							cache: false,
							contentType: false,
							processData: false,
							async: false,
							//dataType: 'json',
							url:contextRoot + '/tvo/outAprv/uploadOrgVdo.json',
							data: oParams,
							beforeSend : function(xhr) {
							},
							success: function(data) {
								if (data.result == '1') {
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 전송되었습니다.');
									//oTvoCmn.div.clear('#out-rqst-aprv');
									oOutFirstAprv.out.rqst.grid.search();			// 목록 새로고침
									oOutFirstAprv.out.rqst.detail.init(oParams.dstrtCd, oParams.outRqstNo, oParams.viewRqstNo);
								} else {
									oCommon.modalAlert('modal-alert', '알림', '실패하였습니다.');
								}
							},
							error: function(data, status, err) {
								console.log(data);
							},
							complete : function(xhr, status) {
								olSwipMap.spinner.stop();
								//$('#container-modal').addClass('hide');
							}
						});			
					}, 1000);
					
				});
			}
		},
		// oOutFirstAprv.out.aprvMulti{}
		aprvMulti : {
			id: 'modal-aprv-multi',
			//val: '',
			element: null,
			// oOutFirstAprv.out.aprvMulti.open()
			open: function() {
				var selRow =  $.getSelRow('#grid-' + oOutFirstAprv.out.rqst.grid.id);		//alert(selRow.length);
				if(selRow.length == 0){
					oCommon.modalAlert('modal-alert', '알림', '승인할 신청을 선택해 주세요.');	return;
				}
				
				let iVal = "";
				let eVal = "";
				for(var i = 0; i < selRow.length; i++) {
					var list = jQuery('#grid-' + oOutFirstAprv.out.rqst.grid.id).getRowData(selRow[i]);
					if ( '10' == list.tvoPrgrsCd ) {		// 신청일 때
						iVal += "," + list.outRqstNo;
					} else if ( '50' == list.tvoPrgrsCd || '51' == list.tvoPrgrsCd ) {		// 입수승인 or 자동입수승인 일 때
						if ( '92' == list.outChkStepCd ) {				// 암호화완료일 때
							eVal += "," + list.outRqstNo;
						}	
					} else {

					}
				}
				if( '' == iVal && '' == eVal ) {
					oCommon.modalAlert('modal-alert', '알림', '진행상태가 신청 또는 입수승인이며 암호화완료인 건을 선택해 주세요.');	return;
				} else {
					let msg = '';
					if( '' != iVal ) {
						msg += '입수승인대상 '+iVal.substring(1).split(',').length+'건';
					}					
					if( '' != eVal ) {
						if ( ''!= msg ) msg += ', ';
						msg += '반출승인대상 '+eVal.substring(1).split(',').length+'건';
					}
						msg += '입니다.';
					oCommon.modalAlert('modal-alert', '알림', msg);
				}
				
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
						target : 'tvoBlank/out/div/outRqstAprvMulti',
					}, function() {
						$('input[type=radio][name=out-rqst-aprv-yn]').prop('checked', false);
						$('#out-rqst-aprv-tvoPrgrsDtl').val("");
					});
				} else {
					$(this.element).modal('show');
					$('input[type=radio][name=out-rqst-aprv-yn]').prop('checked', false);
					$('#out-rqst-aprv-tvoPrgrsDtl').val("");
				}
			},
			// oOutFirstAprv.out.aprvMulti.close()
			close : function() {
				$('#' + oOutFirstAprv.out.aprvMulti.id).modal('hide');
			},
			// oOutFirstAprv.out.aprvMulti.approve()
			approve : function() {
				let sFlag = '승인';
				var $aprvYn = $('input[name="out-rqst-aprv-yn"]:checked');		// 승인구분
				if (!$aprvYn.val()) {		oCommon.modalAlert('modal-alert', '알림', '승인구분을 체크해 주세요.');	return;	}
				if ( $aprvYn.val() == 'N' ) {
					sFlag = '반려';
					if ( $('#out-rqst-aprv-tvoPrgrsDtl').val().trim() == '' ) {
						oCommon.modalAlert('modal-alert', '알림', '반려사유를 입력하세요.');	$('#out-rqst-aprv-tvoPrgrsDtl').focus();	return;
					}
				}
				
				var selRow =  $.getSelRow('#grid-' + oOutFirstAprv.out.rqst.grid.id);		//alert(selRow.length);
				if(selRow.length == 0){
					oCommon.modalAlert('modal-alert', '알림', '승인할 신청을 선택해 주세요.');	return;
				}
				
				let sAprvInfo = '';
				for(var i = 0; i < selRow.length; i++) {
					var list = jQuery('#grid-' + oOutFirstAprv.out.rqst.grid.id).getRowData(selRow[i]);
					let isTarget = false;
					if (list.tvoPrgrsCd == '10') {		// 신청
						isTarget = true;
					} else if (list.tvoPrgrsCd == '50' || list.tvoPrgrsCd == '51') {	// 승인 and (입수승인 or 자동입수승인)
						if ( '92' == list.outChkStepCd ) {														// 암호화완료일 때
							isTarget = true;
						}
					}
					
					if ( isTarget ) {
						if ($aprvYn.val() == 'Y' && list.tvoPrgrsCd == '10') {		// 승인 and 신청
							sAprvInfo += "," + list.outRqstNo+':50';				// 입수승인
						} else if ($aprvYn.val() == 'Y' && (list.tvoPrgrsCd == '50' || list.tvoPrgrsCd == '51')) {	// 승인 and (입수승인 or 자동입수승인)
							if ( '92' == list.outChkStepCd ) {														// 암호화완료일 때
								sAprvInfo += "," + list.outRqstNo+':70';											// 반출승인
							}
						} else if ($aprvYn.val() == 'N') {				// 반려
							sAprvInfo += "," + list.outRqstNo+':30';	// 반려
						} else {
							
						}
					}
				}
				//alert(sAprvInfo.substring(1));
				
				var sTvoPrgrsDtl = $('#out-rqst-aprv-tvoPrgrsDtl').val();
				
				var oParams = {
					dstrtCd  : $('#out-rqst-dtl-dstrtCd').val(),
					aprvInfo : sAprvInfo.substring(1),
					//outRqstNo : sOutRqstNo,
					//tvoPrgrsCd : newTvoPrgrsCd,
					tvoPrgrsDtl : sTvoPrgrsDtl,
				};
				
				oCommon.modalConfirm('modal-confirm', '알림', '선택한 신청을 ' + sFlag + '하시겠습니까?', function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/out/aprv/approveOutRqstMulti.json',
						data : oParams,
						success : function(data) {
							if (data.result == '1') {
								oTvoCmn.div.clear('#out-rqst-aprv');
								oOutFirstAprv.out.rqst.grid.search();			// 목록 새로고침
								//oOutFirstAprv.out.rqst.detail.init(sOutRqstNo, sViewRqstNo);
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 ' + sFlag + '하였습니다.');
							} else {
								oCommon.modalAlert('modal-alert', '알림', data.errors);
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
					$('#' + oOutFirstAprv.out.aprvMulti.id).modal('hide');
				});
			}
		},
		// oOutFirstAprv.out.file{}
		file : {
			grid : {
				id : 'out-rqst-dtl-file',
				// oOutFirstAprv.out.file.grid.init()
				init : function() {
					var sId = oOutFirstAprv.out.file.grid.id;
					var oGridParams = oOutFirstAprv.out.file.grid.params();
					var sGridId = '#grid-' + oOutFirstAprv.out.file.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/out/selectOutFileList.json',
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
							var sMaskingYn = $('#out-rqst-dtl-maskingYn').text().trim();
							
							var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();
							var nOutChkStepCd = !isNaN(sOutChkStepCd) ? parseInt(sOutChkStepCd) : 90;
							
							var sTvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();
							var nTvoPrgrsCd = !isNaN(sTvoPrgrsCd) ? parseInt(sTvoPrgrsCd) : 60;
							
							if (nOutChkStepCd >= 20) {	// 입수대기
								oTvoCmn.grid.showCol(sId, 'outFilePlay');	//원본영상 표시
							}
							if (sMaskingYn == 'Y') {
								if(40 <= nTvoPrgrsCd && nTvoPrgrsCd <= 55) {		// 입수완료 후 반출승인 전
									if (25 <= nOutChkStepCd) {	// 입수중
										oTvoCmn.grid.showCol(sId, 'outFileMasking');
									}
								}
								//oTvoCmn.grid.showCol(sId, 'outFileMaskPlay');	// 원본파일을 마스킹하기 때문에 웹브라우저에서 재생할 수 없다.
							}
							//if (nOutChkStepCd == 92 || nOutChkStepCd == 94) {	// 암호화완료 || 승인완료
							if (nOutChkStepCd >= 75) {	// 암호화중
								oTvoCmn.grid.showCol(sId, 'outFileDownload');
								oTvoCmn.grid.showCol(sId, 'checkHash');		// 원본대조
							}
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
							'#', '상태', '영상구간', '반출파일명', '마스킹', '마스킹영상', '다운로드', '원본대조', '원본영상'
						],
						colModel : [
							{	name:'outFileInd',			align:'center',		width:20
							   ,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:20px;"';	}
							}, {name:'outFilePrgrsNm',		align:'center',		width:40
							   ,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:40px;"';	}
							}, {name:'vdoYmdHmsFrTo',		align:'center',			width:80
							   ,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:80px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var vdoYmdHmsFr = rowObject.outVdoYmdHmsFr;
									var momentYmdhms = moment(vdoYmdHmsFr, 'YYYYMMDDHHmmss');
									if (momentYmdhms.isValid()) {	vdoYmdHmsFr = momentYmdhms.format('YY-MM-DD HH:mm');	}
									
									var vdoYmdHmsTo = rowObject.outVdoYmdHmsTo;
									var momentYmdhms = moment(vdoYmdHmsTo, 'YYYYMMDDHHmmss');
									if (momentYmdhms.isValid()) {	vdoYmdHmsTo = momentYmdhms.format('YY-MM-DD HH:mm');	}
									
									return vdoYmdHmsFr+" ~ "+vdoYmdHmsTo;
								}
							}, {name:'outFileNmDrm',	align:'center',		classes:'text-ellipsis'
							}, {name:'outFileMasking',	align:'center',		width:30,		hidden:true
							   ,cellattr : function() {	return 'style="width:30px;"';		}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sOutFileDelYmdhms = rowObject.outFileDelYmdhms;		// 반출파일삭제일시
									if ( sOutFileDelYmdhms != '' ) {
										var momentYmdhms = moment(sOutFileDelYmdhms, 'YYYYMMDDHHmmss');
										if (momentYmdhms.isValid()) {	sOutFileDelYmdhms = momentYmdhms.format('YYYY-MM-DD HH:mm');	}
										var $span = $('<span/>', {
											'title' : '삭제일시 : '+sOutFileDelYmdhms,
											'html' : '[영상삭제]',
										});
										return $span.prop('outerHTML');
									}
									var sOutFileNm = rowObject.outFileNm;
									var sOutFileNmMsk = rowObject.outFileNmMsk;
									var sMaskingYn = $('#out-rqst-dtl-maskingYn').text();
									var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();
									var nOutChkStepCd = !isNaN(sOutChkStepCd) ? parseInt(sOutChkStepCd) : 90;
									var sOutFilePrgrsCd = rowObject.outFilePrgrsCd;
									var nOutFilePrgrsCd = !isNaN(sOutFilePrgrsCd) ? parseInt(sOutFilePrgrsCd) : 90;
									if (nOutFilePrgrsCd >= 40) {	// 입수완료
										//if (sMaskingYn == 'Y' && nOutChkStepCd == 30 && sOutFileNmMsk == '') {
										if (sMaskingYn == 'Y') {
											var $button = $('<button/>', {
												'type' : 'button',
												'class' : 'btn btn-default btn-xs',
												'title' : '반출파일을 마스킹합니다.',
												'html' : '<i class="fas fa-mask"></i>',
												//'onclick' : 'javascript:oOutFirstAprv.out.file.masking("' + rowObject.outRqstNo + '", "' + rowObject.outFilePath + '", "' + rowObject.outFileNmMp4 + '", "' + rowObject.outFileSeq + '");',
												'onclick' : 'javascript:oOutFirstAprv.out.file.masking("' + rowObject.outRqstNo + '", "' + rowObject.outFilePath + '", "' + rowObject.outFileNm + '", "' + rowObject.outFileSeq + '");',
											});
											return $button.prop('outerHTML');
										}
									}
									return '';
								}
							}, {name:'outFileMaskPlay',	align:'center',		width:30,		hidden:true
							   ,cellattr : function() {	return 'style="width:30px;"';		}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sOutFileDelYmdhms = rowObject.outFileDelYmdhms;		// 반출파일삭제일시
									if ( sOutFileDelYmdhms != '' ) {
										var momentYmdhms = moment(sOutFileDelYmdhms, 'YYYYMMDDHHmmss');
										if (momentYmdhms.isValid()) {	sOutFileDelYmdhms = momentYmdhms.format('YYYY-MM-DD HH:mm');	}
										var $span = $('<span/>', {
											'title' : '삭제일시 : '+sOutFileDelYmdhms,
											'html' : '[영상삭제]',
										});
										return $span.prop('outerHTML');
									}
									var sOutFileNm = rowObject.outFileNm;
									var sOutFileNmMsk = rowObject.outFileNmMsk;
									var sMaskingYn = $('#out-rqst-dtl-maskingYn').text();
									var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();
									var nOutChkStepCd = !isNaN(sOutChkStepCd) ? parseInt(sOutChkStepCd) : 90;
									
									if (sMaskingYn == 'Y' && sOutFileNmMsk != '') {
										var $button = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs',
											'title' : '마스킹파일을 플레이어로 재생합니다.',
											'html' : '<i class="fas fa-play"></i>',
											//'onclick' : 'javascript:oOutFirstAprv.out.file.play("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '", "MSK");',
											'onclick' : 'javascript:oOutFirstAprv.out.file.play("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '", "MSK", "' + rowObject.outVdoYmdhmsFr + '");',
										});
										return $button.prop('outerHTML');
									}
									return '';
								}
							}, {name:'outFileDownload',		align:'center',			width:30,		hidden:true
							   ,cellattr : function() {		return 'style="width:30px;"';			}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sOutFileDelYmdhms = rowObject.outFileDelYmdhms;		// 반출파일삭제일시
									if ( sOutFileDelYmdhms != '' ) {
										var momentYmdhms = moment(sOutFileDelYmdhms, 'YYYYMMDDHHmmss');
										if (momentYmdhms.isValid()) {	sOutFileDelYmdhms = momentYmdhms.format('YYYY-MM-DD HH:mm');	}
										var $span = $('<span/>', {
											'title' : '삭제일시 : '+sOutFileDelYmdhms,
											'html' : '[영상삭제]',
										});
										return $span.prop('outerHTML');
									}
									//var sTvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();
									//var nTvoPrgrsCd = !isNaN(sTvoPrgrsCd) ? parseInt(sTvoPrgrsCd) : 60;
									//var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();
									//var nOutChkStepCd = !isNaN(sOutChkStepCd) ? parseInt(sOutChkStepCd) : 90;
									//if (sOutChkStepCd == 92 || sOutChkStepCd == 94) {
									var sOutFilePrgrsCd = rowObject.outFilePrgrsCd;
									var nOutFilePrgrsCd = !isNaN(sOutFilePrgrsCd) ? parseInt(sOutFilePrgrsCd) : 90;
									if (nOutFilePrgrsCd == 92) {	// 암호화완료
	
										var sOutFileNmDrm = rowObject.outFileNmDrm;
										if (sOutFileNmDrm != '') {
											var $button = $('<button/>', {
												'type' : 'button',
												'class' : 'btn btn-default btn-xs',
												'title' : '반출파일을 다운로드합니다.',
												'html' : '<i class="fas fa-file-download"></i>',
												'onclick' : 'javascript:oOutFirstAprv.out.file.download("' + rowObject.outFileSeq + '");',
											});
											return $button.prop('outerHTML');
										}
									}
									return '';
								}
							}, {name:'checkHash',			align:'center',			width:30,		hidden:true
							   ,cellattr : function() {		return 'style="width:30px;"';			}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sOutFileDelYmdhms = rowObject.outFileDelYmdhms;		// 반출파일삭제일시
									if ( sOutFileDelYmdhms != '' ) {
										var momentYmdhms = moment(sOutFileDelYmdhms, 'YYYYMMDDHHmmss');
										if (momentYmdhms.isValid()) {	sOutFileDelYmdhms = momentYmdhms.format('YYYY-MM-DD HH:mm');	}
										var $span = $('<span/>', {
											'title' : '삭제일시 : '+sOutFileDelYmdhms,
											'html' : '[영상삭제]',
										});
										return $span.prop('outerHTML');
									}
									//var sTvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();
									//var nTvoPrgrsCd = !isNaN(sTvoPrgrsCd) ? parseInt(sTvoPrgrsCd) : 60;
									//var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();
									//var nOutChkStepCd = !isNaN(sOutChkStepCd) ? parseInt(sOutChkStepCd) : 90;
									//if (sOutChkStepCd == 92 || sOutChkStepCd == 94) {
									var sOutFilePrgrsCd = rowObject.outFilePrgrsCd;
									var nOutFilePrgrsCd = !isNaN(sOutFilePrgrsCd) ? parseInt(sOutFilePrgrsCd) : 90;
									if (nOutFilePrgrsCd == 92) {	// 암호화완료
	
										var sOutFileNmDrm = rowObject.outFileNmDrm;
										if (sOutFileNmDrm != '') {
											var $button = $('<button/>', {
												'type' : 'button',
												'class' : 'btn btn-default btn-xs',
												'title' : '암호화영상원본을 대조합니다.',
												'html' : '<i class="fas fa-clone"></i>',
												'onclick' : 'javascript:oOutFirstAprv.out.checkHash.open("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '");',
											});
											return $button.prop('outerHTML');
										}
									}
									return '';
								}
							}, {name:'outFilePlay',		align:'center',		width:30,		hidden:true
							   ,cellattr : function() {	return 'style="width:30px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sOutFileDelYmdhms = rowObject.outFileDelYmdhms;		// 반출파일삭제일시
									if ( sOutFileDelYmdhms != '' ) {
										var momentYmdhms = moment(sOutFileDelYmdhms, 'YYYYMMDDHHmmss');
										if (momentYmdhms.isValid()) {	sOutFileDelYmdhms = momentYmdhms.format('YYYY-MM-DD HH:mm');	}
										var $span = $('<span/>', {
											'title' : '삭제일시 : '+sOutFileDelYmdhms,
											'html' : '[영상삭제]',
										});
										return $span.prop('outerHTML');
									}
									//var sOutFilePrgrsCd = rowObject.outFilePrgrsCd;
									//var nOutFilePrgrsCd = !isNaN(sOutFilePrgrsCd) ? parseInt(sOutFilePrgrsCd) : 90;
									//if (nOutFilePrgrsCd >= 40) {	// 입수완료
									if ( '' != rowObject.outFileNmMp4 ) {	// mp4파일이 있을 때
										var $button = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs',
											'title' : '원본영상을 플레이어로 재생합니다.',
											'html' : '<i class="fas fa-play"></i>',
											'onclick' : 'javascript:oOutFirstAprv.out.file.play("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '", "MP4", "' + rowObject.outVdoYmdhmsFr + '");',
										});
										return $button.prop('outerHTML');
									}
									return '';
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
				},
				params : function() {
					//var sOutRqstNo = $('#out-rqst-dtl-outRqstNo').val();
					var oParams = {
						dstrtCd   : $('#out-rqst-dtl-dstrtCd').val(),
						outRqstNo : $('#out-rqst-dtl-outRqstNo').val()
					};
					return oParams;
				},
				search : function() {
					var sId = oOutFirstAprv.out.rqst.file.grid.id;
					var oGridParams = oOutFirstAprv.out.rqst.file.grid.params();
					oTvoCmn.jqGrid.reload(sId, 1, oGridParams);
				}
			},
			// oOutFirstAprv.out.file.play()
			play : function(outRqstNo, outFileSeq, outFileTy, vdoYmdhmsFr) {
				var url = contextRoot + '/tvo/openHtmlPlayer.do?outRqstNo=' + outRqstNo + '&outFileSeq=' + outFileSeq + '&outFileTy=' + outFileTy + '&vdoYmdhmsFr=' + vdoYmdhmsFr;
				var opener = window.open(url, 'play', 'status=no,width=' + oConfigure.popWidth + ',height=' + oConfigure.popHeight);
				oVmsCommon.player.push(opener);
			},
			// oOutFirstAprv.out.file.download()
			download : function(pOutFileSeq) {
				//oCommon.modalConfirm('modal-confirm', '확인', '영상을 다운로드하시겠습니까?', () => {
					let $formDownload = $('#form-drmFile');
					if ($formDownload.length) {
						$('#form-drmFile').attr('action',contextRoot + '/tvo/downloadDrmFile.do');
						$('#form-drmFile input[name=dstrtCd]').val($('#out-rqst-dtl-dstrtCd').val());
						$('#form-drmFile input[name=outRqstNo]').val($('#out-rqst-dtl-outRqstNo').val());
						$('#form-drmFile input[name=outFileSeq]').val(pOutFileSeq);
					} else {
						$formDownload = $('<form/>', {
							'id': 'form-drmFile'	, 'method': 'POST'		, 'action': contextRoot + '/tvo/downloadDrmFile.do'	});
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'dstrtCd'		, 'value': $('#out-rqst-dtl-dstrtCd').val()			}));
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'outRqstNo'	, 'value': $('#out-rqst-dtl-outRqstNo').val()		}));
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'outFileSeq'	, 'value': pOutFileSeq								}));
						$formDownload.appendTo(document.body);
					}
					$formDownload.submit();
				//}, undefined);
			},
			// oOutFirstAprv.out.file.masking()
			masking : function(outRqstNo, outFilePath, outFileNm, outFileSeq) {
				console.log("-- oOutFirstAprv.out.file.masking("+outRqstNo+", "+outFilePath+", "+outFileNm+", "+outFileSeq+")");
				console.log("-- oOutFirstAprv.out.file.masking(), oTvoConfig.maskModuleSupplier => %s", oTvoConfig.maskModuleSupplier);
				console.log("-- oOutFirstAprv.out.file.masking(), oTvoConfig.tvoUrl => %s", oTvoConfig.tvoUrl);
				
				if ( "markany" == oTvoConfig.maskModuleSupplier ) {
					let url = "";
					let param = '';
					if ( "first" == oTvoConfig.maskToolVersion ) {
						
						url = "mamasking:// "+param;
						// 참고사이트 => Registry를 이용한 URL 호출방법 ( https://itteamb.blogspot.com/2021/02/javascript-chrome-local-registry-url.html )
						
					} else  if ( "second" == oTvoConfig.maskToolVersion ) {
						param += ' -O ' + outRqstNo;				//-O R0000001354			// request id
						param += ' -C ' + outFileSeq;				//-C 341					// contents seq
						param += ' -R ' + oConfigure.userId;		//-R markany				// result 파일경로 ??
						param += ' -T MOV';							//-T MOV					// content type MOV, IMG
						param += ' -P ' + oTvoConfig.ftpVdoDir;		//-P /upload/contents		// ftp 경로1
						param += ' -S /' + outFilePath + '/';		//-S /20220714/				// ftp 경로2
						param += ' -N ' + outFileNm;				//-N R0000006139.avi		// 파일명
						param += ' -F ' + oTvoConfig.maskToolFtp;	//-F ftp://127.0.0.1:2121	// ftp 주소
						param += ' -U ' + oTvoConfig.maskToolUsr;	//-U tvo/smart12#$			// ftp 계정
						//param += ' -A ' + oTvoConfig.maskToolAns;	//-A http://127.0.0.1:51110/api/responseMasking.xx // 마스킹 결과 전송 rul
						param += ' -A ' + oTvoConfig.tvoUrl+oTvoConfig.maskToolAns;		//-A http://127.0.0.1:51110/api/responseMasking.xx // 마스킹 결과 전송 rul
						param += ' -M Passive';					//-M Passive				// ftp 모드
						console.log("-- masking param => {}", param);
						
						url = "mamaskingopen:// "+param;
					}
					
					var exec = document.createElement("a");
					exec.setAttribute("href", url);
					exec.click();
				}
				
			},
			
			// oOutFirstAprv.out.file.getVdo()
			getVdo : function(pOutRqstNo) {
				oCommon.modalConfirm('modal-confirm', '알림', '영상입수재시도를 요청하시겠습니까?', function() {
					olSwipMap.spinner.spin(document.getElementById('out-rqst-dtl'));
					
					setTimeout(() => {
						var oParams = {
							dstrtCd   : $('#out-rqst-dtl-dstrtCd').val(),
							outRqstNo : $('#out-rqst-dtl-outRqstNo').val()
						};
						$.ajax({
							type : 'POST',
							async : false,
							dataType : 'json',
							url:contextRoot + '/tvo/out/aprv/requestGetOrigVdo.json',
							data : oParams,
							success : function(data) {
								if (data.result == '1') {
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
									oOutFirstAprv.out.rqst.grid.search();			// 목록 새로고침
									oOutFirstAprv.out.rqst.detail.init(oParams.dstrtCd, oParams.outRqstNo);
								} else {
									oCommon.modalAlert('modal-alert', '알림', data.errors);
								}
							},
							error : function(data, status, err) {
								console.log(data);
							},
							complete : function(xhr, status) {
								olSwipMap.spinner.stop();
							}
						});
					}, 1000);
					
				});
				
			},
			
			// oOutFirstAprv.out.file.enc()
			enc : function(sOutRqstNo) {
				oCommon.modalConfirm('modal-confirm', '알림', 'DRM재시도를 요청하시겠습니까?', function() {
					olSwipMap.spinner.spin(document.getElementById('out-rqst-dtl'));
					
					setTimeout(() => {			
						var oParams = {
							dstrtCd   : $('#out-rqst-dtl-dstrtCd').val(),
							outRqstNo : $('#out-rqst-dtl-outRqstNo').val()
						};
						$.ajax({
							type : 'POST',
							async : false,
							dataType : 'json',
							url:contextRoot + '/tvo/out/aprv/requestEncOrigVdo.json',
							data : oParams,
							success : function(data) {
							//	if (data.result == '1') {
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
									oOutFirstAprv.out.rqst.grid.search();			// 목록 새로고침
									oOutFirstAprv.out.rqst.detail.init(oParams.dstrtCd, oParams.outRqstNo);
							//	} else {
							//		oCommon.modalAlert('modal-alert', '알림', data.errors);
							//	}
							},
							error : function(data, status, err) {
								console.log(data);
							},
							complete : function(xhr, status) {
								olSwipMap.spinner.stop();
							}
						});
					}, 1000);
					
				});
				
			},
			
		},
		// oOutFirstAprv.out.checkHash{}
		checkHash: {
			id: 'modal-check-hash',
			element: null,
			// oOutFirstAprv.out.checkHash.open()
			open: function(sOutRqstNo, sOutFileSeq) {
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
						target: 'tvoBlank/out/div/checkHash'			// 파일첨부
					}, function() {
						$('#out-aprv-hash-outRqstNo').val(sOutRqstNo);
						$('#out-aprv-hash-outFileSeq').val(sOutFileSeq);
					});
				} else {
					$(this.element).modal('show');
					$('#out-aprv-hash-outRqstNo').val(sOutRqstNo);
						$('#out-aprv-hash-outFileSeq').val(sOutFileSeq);
				}
			},
			close: function() {
				$('#' + oOutFirstAprv.out.checkHash.id).modal('hide');
			},
			// oOutFirstAprv.out.checkHash.register()
			register: function() {
				let oFiles = $('#out-aprv-hash-chechHashFileNm')[0].files;
				if ( !oFiles.length ) {
					oCommon.modalAlert('modal-alert', '알림', '파일을 첨부해야 합니다.');
					return;
				}

				if (oFiles.length) {
					// 파일 사이즈 체크
					let sz = $('#out-aprv-hash-chechHashFileNm')[0].files[0].size;	//console.log(sz);
					let mx = 1024 * 1024 * 5;								//console.log(mx);
					if (mx < sz) {
					//	oCommon.modalAlert('modal-alert', '알림', '5 MB 이하의 파일만 첨부가능합니다.');
					//	return;
					}

					// 파일 확장자 체크
					let nm = $('#out-aprv-hash-chechHashFileNm').val();					//console.log(nm);
					let ex = nm.substring(nm.indexOf('.') + 1).toLowerCase();	//console.log(ex);
					let ty = "hwp, hwpx, doc, docx, pdf";						//console.log(ty.indexOf(ex));
					if (ty.indexOf(ex) == -1) {
					//	oCommon.modalAlert('modal-alert', '알림', ty + ' 확장자를 가진 파일만 첨부가능합니다.');
					//	return;
					}
				}

				//oCommon.modalConfirm('modal-confirm', '알림', '파일을 체크하시겠습니까?', function() {
				//});
				olSwipMap.spinner.spin(document.getElementById('out-rqst-dtl'));
				var oParams = oOutFirstAprv.out.checkHash.params();
				
				setTimeout(() => {
					$.ajax({
						type: 'POST',
						enctype: 'multipart/form-data',
						cache: false,
						contentType: false,
						processData: false,
						async: false,
						//dataType: 'json',
						url:contextRoot + '/tvo/out/aprv/uploadChckHashFile.json',
						data: oParams,
						success: function (data) {
							if (data.result == '1') {
								oCommon.modalAlert('modal-alert', '알림', '서버에 저장된 원본파일과 일치합니다.');
							} else {
								oCommon.modalAlert('modal-alert', '알림', '서버에 저장된 원본파일과 일치하지 않습니다.');
								return false;
							}
							oTvoCmn.div.clear('#out-check-hash');
						},
						error: function (data, status, err) {
							console.log(data);
						},
						complete : function(xhr, status) {
							olSwipMap.spinner.stop();
						}
					});	
				}, 1000);
				$('#' + oOutFirstAprv.out.checkHash.id).modal('hide');
				//$('#out-check-hash').remove();
			},
			params: function() {
				//var outRqstNo = $('#out-aprv-hash-outRqstNo').val();
				//var outFileSeq = $('#out-aprv-hash-outFileSeq').val();

				let oParams = new FormData();
				oParams.append('dstrtCd'   , $('#out-rqst-dtl-dstrtCd').val()    );
				oParams.append('outRqstNo' , $('#out-aprv-hash-outRqstNo').val() );
				oParams.append('outFileSeq', $('#out-aprv-hash-outFileSeq').val());

				let oFiles = $('#out-aprv-hash-chechHashFileNm')[0].files;
				if (oFiles.length) {
					$.each(oFiles, function (index, file) {
						oParams.append('file-' + index, file);
					});
				}

				return oParams;
			}
		},

	};
}
