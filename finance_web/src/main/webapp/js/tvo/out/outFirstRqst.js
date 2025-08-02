$(function() {
	oOutFirstRqst = new outFirstRqst();
	oOutFirstRqst.init();
});

function outFirstRqst() {
	this.init = function() {
        // LEFT 조절
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			right : true
		}, function() {
			oTvoCmn.datetimepicker.ymd('.out-rqst-outRqstYmdhms', '2023-01', moment().add(100, 'years'));	// 반출신청일
			//oTvoCmn.datetimepicker.ymd('.rqst-out-playEndYmdhms', '2023-01', moment().add(100, 'years'));	// 재생종료일
			codeInfoList('#out-rqst-rqstRsnTyCd', 'RQST_RSN_TY', '', '신청사유');
			codeInfoList('#out-rqst-tvoPrgrsCd', 'OUT_PRGRS', '', '진행상태');

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
			oOutFirstRqst.out.rqst.grid.init();
			
			oOutFirstRqst.out.rqst.calculate();
			setInterval(() => {
				oOutFirstRqst.out.rqst.calculate();
			}, 60000);	// 60초 반복
		});
	};

	// oOutFirstRqst.out{}
	this.out = {
		// oOutFirstRqst.out.rqst{}
		rqst : {
			// oOutFirstRqst.out.rqst.grid{}
			grid : {
				id : 'rqst-out',
				page: '1',
				// oOutFirstRqst.out.rqst.grid.init()
				init : function() {
					var sId = oOutFirstRqst.out.rqst.grid.id;
					var oGridParams = oOutFirstRqst.out.rqst.grid.params();
					var sGridId = '#grid-' + oOutFirstRqst.out.rqst.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/out/selectOutRqstList.json',
						datatype : 'json',
						mtype : 'POST',
						height : 'auto',
						rowNum : 5,
						autowidth:true,
						shrinkToFit : true,		//multiselect : true,	//multiboxonly : true,
						postData : oGridParams,
						beforeRequest : function() {
						},
						loadComplete : function(data) {
							oOutFirstRqst.out.rqst.grid.page = data.page;
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
							'dstrtCd', '신청지구', 'viewRqstNo', '신청번호', '신청일자', '재생종료', '신청사유', '사건번호', '사건명', '카메라명'
							, '마스킹', '제3자제공', '영상처리', '진행상태코드', '진행상태', '상세'
						],
						colModel : [
							{	name:'dstrtCd'      , hidden: true
							}, {name:'dstrtNm'      , align:'center', width:60
							}, {name:'viewRqstNo'   , hidden:true
							}, {name:'outRqstNo'    , align:'center', width:60
							    , formatter: function (cellvalue, options, rowObject) { return rowObject.outRqstNo.substring(5); }
							}, {name:'outRqstYmdhms', align:'center', width:60,	cellattr : function() {	return 'style="width:60px;"';	}
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
							}, {name:'rqstRsnTyNm',		align:'center',	width:80,	cellattr : function() {	return 'style="width:80px;"';	}
							}, {name:'evtNo',			align:'center',	width:100,	cellattr : function() {	return 'style="width:100px;"';	}
							}, {name:'evtNm',			align:'left',	width:100,	cellattr : function() {	return 'style="width:100px;"';	}
							}, {name:'fcltLblNm',		align:'left',	classes:'text-ellipsis'
							}, {name:'maskingYn',		align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'thirdPartyYn',	align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'outChkStepNm',	align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'tvoPrgrsCd',		hidden:true
							}, {name:'tvoPrgrsNm',		align:'center',	width:60,	cellattr : function() {	return 'style="width:60px;"';	}
							}, {name:'outRqstDtl',		align:'center',	width:40,	cellattr : function() {	return 'style="width:40px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var $button = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs',
										'title' : '반출신청 내용을 조회합니다.',
										'html': '보기',
										'onclick' : 'javascript:oOutFirstRqst.out.rqst.detail.init("' + rowObject.dstrtCd + '", "' + rowObject.outRqstNo + '", "' + rowObject.viewRqstNo + '", "' + rowObject.tvoPrgrsCd + '");'
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
		            $('#btn-refresh').on('click', () => oOutFirstRqst.out.rqst.grid.search());
					
				},
				// oOutFirstRqst.out.rqst.grid.search()
				search : function(page) {
					if (typeof page != 'undefined') {
						oOutFirstRqst.out.rqst.grid.page = page;
					}
					
					var sFr = $('#out-rqst-outRqstYmdhms-fr').val();
					var sTo = $('#out-rqst-outRqstYmdhms-to').val();
					if ( sFr!='' && sTo!='' ) {
						if ( sFr > sTo ) {
							alert('기간설정에 오류가 있습니다.');	return;
						}
					}
					var sId = oOutFirstRqst.out.rqst.grid.id;
					var sPage = oOutFirstRqst.out.rqst.grid.page;		//alert(sPage);
					var oGridParams = oOutFirstRqst.out.rqst.grid.params();
					$('article div.col').empty();	// 상세화면 비우기
					oTvoCmn.jqGrid.reload(sId, sPage, oGridParams);
				},
				// oOutFirstRqst.out.rqst.grid.params()
				params : function() {
					var sOutRqstYmdhmsFr = $('#out-rqst-outRqstYmdhms-fr').val();
					var momYmdhmsFr = moment(sOutRqstYmdhmsFr, 'YYYY-MM-DD');
					if (momYmdhmsFr.isValid()) {
						sOutRqstYmdhmsFr = momYmdhmsFr.format('YYYYMMDD');
					} else {
						sOutRqstYmdhmsFr = '';
					}

					var sOutRqstYmdhmsTo = $('#out-rqst-outRqstYmdhms-to').val();
					var momYmdhmsTo = moment(sOutRqstYmdhmsTo, 'YYYY-MM-DD');
					if (momYmdhmsTo.isValid()) {
						sOutRqstYmdhmsTo = momYmdhmsTo.format('YYYYMMDD');
					} else {
						sOutRqstYmdhmsTo = '';
					}

					/*	var sPlayEndYmdhms = $('#rqst-out-playEndYmdhms').val();
                        momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
                        if (momentPlayEndYmdhms.isValid()) {
                            sPlayEndYmdhms = momentPlayEndYmdhms.format('YYYYMMDD');
                        } else {
                            sPlayEndYmdhms = '';
                        }	*/

					var sTvoPrgrsCd = $('#out-rqst-tvoPrgrsCd option:selected').val();
					var sRqstRsnTyCd = $('#out-rqst-rqstRsnTyCd option:selected').val();
					var sEvtNo = $('#rqst-out-evtNo').val();
					var sEvtNm = $('#rqst-out-evtNm').val();
					var sFcltLblNm = $('#rqst-out-fcltLblNm').val();
					//var sMaskingEndYn = $('#rqst-out-maskingEndYn').is(':checked') ? 'Y' : 'N';

					var oParams = {
						outRqstYmdhmsFr : sOutRqstYmdhmsFr,
						outRqstYmdhmsTo : sOutRqstYmdhmsTo,
						//playEndYmdhms : sPlayEndYmdhms,
						tvoPrgrsCd : sTvoPrgrsCd,
						rqstRsnTyCd : sRqstRsnTyCd,
						evtNo : sEvtNo,
						evtNm : sEvtNm,
						fcltLblNm : sFcltLblNm
						//, maskingEndYn : sMaskingEndYn
					};

					return oParams;
				}
			},
			// oOutFirstRqst.out.rqst.detail{}
			detail : {
				// oOutFirstRqst.out.rqst.detail.init()
				init : function(pDstrtCd, outRqstNo, viewRqstNo, tvoPrgrsCd) {
					$('article div.col').empty();	// 상세화면 비우기
					oDiv.setDiv('left', '0', {
						target : 'tvoBlank/out/div/outRqstDtl',				// 반출신청상세
						dstrtCd : pDstrtCd,
						outRqstNo : outRqstNo
					}, function() {
						setTimeout(function() {
							console.log("=== oTvoConfig.recommVdoDuration => "+oTvoConfig.recommVdoDuration);
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
												
							if ( 'N' == $('#out-rqst-dtl-outFileDelYn').val() ) {
								//if ( '50'==tvoPrgrsCd || '51'==tvoPrgrsCd || '70'==tvoPrgrsCd || '71'==tvoPrgrsCd ) {		// 승인 or 자동승인
								if ( '70'==tvoPrgrsCd || '71'==tvoPrgrsCd ) {		// 승인 or 자동승인
									$('#out-rqst-dtl-rqstExtn').removeClass('hide');
								}
								$('#out-rqst-dtl-vmsView').removeClass('hide');
							}
							
							if ( '70'==tvoPrgrsCd || '71'==tvoPrgrsCd ) {		// 승인 or 자동승인
								$('#out-rqst-dtl-downloadPlayer').removeClass('hide');
							}
									
							oOutFirstRqst.out.file.grid.init();					// 반출파일목록
							
						}, 500);
						
						setTimeout(function() {
							$("#out-rqst-dtl-map-pin").trigger("click");		// 지도의 카메라위치로 이동한다.
						}, 500);
					});
					if (tvoPrgrsCd == '70' || tvoPrgrsCd == '71') {				// 반출승인 or 반출자동승인
						oDiv.setDiv('left', '1', {
							target : 'tvoBlank/out/div/outExtnList',		// 반출기간연장이력
							dstrtCd : pDstrtCd,
							outRqstNo : outRqstNo
						}, function() {
							setTimeout(function() {
								oOutFirstRqst.out.prodExtn.grid.init();
							}, 500);
						});
					}
					oDiv.setDiv('left', '2', {
						target : 'tvoBlank/view/div/viewRqstDtl',				// 열람신청상세
						dstrtCd : pDstrtCd,
						viewRqstNo : viewRqstNo
					}, function() {
						setTimeout(function() {
							var sViewEndYmdhms = $('#view-rqst-dtl-viewEndYmdhms').text();
							$('#out-rqst-aprv-playEndYmdhms').val(sViewEndYmdhms);
						}, 500);
					});
				}
			},
			// oOutFirstRqst.out.rqst.cancel()
			cancel : function() {
				oCommon.modalConfirm('modal-confirm', '알림', '반출신청을 취소하시겠습니까?', function() {
					//var sOutRqstNo = $('#out-rqst-dtl-outRqstNo').val();
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/out/deleteOutRqst.json',
						data : {
							outRqstNo : $('#out-rqst-dtl-outRqstNo').val()
						},
						success : function(data) {
							if (data.result == '1') {
								oTvoCmn.jqGrid.reload(oOutFirstRqst.out.rqst.grid.id, 1, oOutFirstRqst.out.rqst.grid.params());
								oTvoCmn.div.clear('#view-rqst-dtl');
								oTvoCmn.div.clear('#out-rqst-dtl');
								oOutFirstRqst.out.rqst.grid.search();			// 목록 새로고침
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
				});
			},
			// oOutFirstRqst.out.rqst.calculate()
			calculate : function() {
				$.ajax({
					type : 'POST',
					async : false,
					dataType : 'json',
					url:contextRoot + '/tvo/out/calculate.json',
					data : '',
					success : function(data) {
						console.log('-- calculate(), ingCnt:%s, contMin:%s',data.ingCnt,data.costMin);
						$("#rqst-out-ingCnt").text(data.ingCnt);
						$("#rqst-out-drmCnt").text(data.drmCnt);
						$("#rqst-out-costMin").text(data.costMin);
					},
					error : function(data, status, err) {
						console.log(data);
					}
				});
			}
		},
		// oOutFirstRqst.out.prodExtn{}
		prodExtn : {
            id: 'modal-prod-extn',
            element: null,
			// oOutFirstRqst.out.prodExtn.open()
			open : function(type) {
                this.element = document.getElementById(this.id);
				var sPlayEndYmdhms = $('#out-rqst-dtl-playEndYmdhms').text();
				
				if (type == 'SINGLE' && '' != sPlayEndYmdhms) {		// 한건 연장 일 때
					//var momentMinDate = moment(sPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
					var momentMinDate = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
					if (momentMinDate.isValid()) {
						if (this.element == null) {
							oDiv.openDiv(this.id, {
								target : 'tvoBlank/out/div/outExtnReg'		// 기간연장
							}, function() {
								var momentMaxDate = momentMinDate.clone().add(30, 'days');
								oTvoCmn.datetimepicker.ymd('.out-prod-extn-rqst-rqstPlayEndYmdhms', momentMinDate, momentMaxDate);
								$('#out-prod-extn-rqst-rqstPlayEndYmdhms').val(sPlayEndYmdhms);
								$('#out-prod-extn-rqst-type').val(type);
							});
						} else {
							$(this.element).modal('show');
								var momentMaxDate = momentMinDate.clone().add(30, 'days');
								oTvoCmn.datetimepicker.ymd('.out-prod-extn-rqst-rqstPlayEndYmdhms', momentMinDate, momentMaxDate);
								$('#out-prod-extn-rqst-rqstPlayEndYmdhms').val(sPlayEndYmdhms);
								$('#out-prod-extn-rqst-type').val(type);
						}
					} else {
						oCommon.modalAlert('modal-alert', '알림', '정보에 오류가 있습니다.');
						return false;
					}
				} else {												// 여러건 연장 일 때
					if (this.element == null) {
						oDiv.openDiv(this.id, {
							target : 'tvoBlank/out/div/outExtnReg'		// 기간연장
						}, function() {
							oTvoCmn.datetimepicker.ymdhm('.out-prod-extn-rqst-rqstPlayEndYmdhms', moment(), moment().add(30, 'days'));
							$('#out-prod-extn-rqst-type').val(type);
						});
					} else {
						$(this.element).modal('show');
							oTvoCmn.datetimepicker.ymdhm('.out-prod-extn-rqst-rqstPlayEndYmdhms', moment(), moment().add(30, 'days'));
							$('#out-prod-extn-rqst-type').val(type);
					}
				}
			},
			// oOutFirstRqst.out.prodExtn.init()
			/* init : function(type) {
				var sPlayEndYmdhms = $('#out-rqst-dtl-playEndYmdhms').text();
				if (type == 'SINGLE' && '' != sPlayEndYmdhms) {
					var momentMinDate = moment(sPlayEndYmdhms, 'YYYYMMDDHHmmss');
					if (momentMinDate.isValid()) {
						oDiv.setDiv('right', '2', {
						//oDiv.setDiv('left', '0', 
							target : 'tvoBlank/out/div/outExtnReg'		// 기간연장
						}, function() {
							var momentMaxDate = momentMinDate.clone().add(30, 'days');
							oTvoCmn.datetimepicker.ymdhm('.out-prod-extn-rqst-rqstPlayEndYmdhms', momentMinDate, momentMaxDate);
							$('#out-prod-extn-rqst-rqstPlayEndYmdhms').val(sPlayEndYmdhms);
							$('#out-prod-extn-rqst-type').val(type);
						});
					}
				} else {
					oDiv.setDiv('right', '2', {
					//oDiv.setDiv('left', '0', 
						target : 'tvoBlank/out/div/outExtnReg'		// 기간연장
					}, function() {
						oTvoCmn.datetimepicker.ymdhm('.out-prod-extn-rqst-rqstPlayEndYmdhms', moment(), moment().add(30, 'days'));
						$('#out-prod-extn-rqst-type').val(type);
					});
				}
			}, */
			// oOutFirstRqst.out.prodExtn.request()
			request : function() {
				if ( $('#out-rqst-dtl-playEndYmdhms').text() == $('#out-prod-extn-rqst-rqstPlayEndYmdhms').val() ) {
					oCommon.modalAlert('modal-alert', '알림', '종료일자를 재지정하세요.');	return;
				}
				if ( $('#out-prod-extn-rqst-outExtnRqstRsn').val().trim() == '' ) {
					oCommon.modalAlert('modal-alert', '알림', '연장신청사유를 입력하세요.');	$('#out-prod-extn-rqst-outExtnRqstRsn').focus();	return;
				}
				
				var sType = $('#out-prod-extn-rqst-type').val();
				var sOutRqstNo = '';
				if (sType == 'SINGLE') {
					sOutRqstNo = $('#out-rqst-dtl-outRqstNo').val();
					
				} else if (sType == 'MULTIPLE') {
					var aIds = $("#grid-rqst-out").jqGrid('getGridParam', 'selarrrow');
					var nLength = aIds.length;
					var aOutRqstNo = [];
					if (nLength != 0) {
						for (var i = 0; i < nLength; i++) {
							var oRowData = $('#grid-rqst-out').getRowData(aIds[i]);
							aOutRqstNo.push(oRowData.outRqstNo);
						}
						sOutRqstNo = aOutRqstNo.toString();
					} else {
						oCommon.modalAlert('modal-alert', '알림', '선택된 반출정보가 없습니다.');
						return false;
					}
				} else {
					oCommon.modalAlert('modal-alert', '알림', '선택된 반출정보가 없습니다.');
					return false;
				}

				var sRqstPlayEndYmdhms = $('#out-prod-extn-rqst-rqstPlayEndYmdhms').val();
				//momentYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
				momentYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD');
				if (momentYmdhms.isValid()) {
					//sRqstPlayEndYmdhms = momentYmdhms.format('YYYYMMDDHHmmss');
					sRqstPlayEndYmdhms = momentYmdhms.format('YYYYMMDD')+'235959';
				}
				var sOutExtnRqstRsn = $('#out-prod-extn-rqst-outExtnRqstRsn').val();

			//	let sWsUrl = "ws://"+window.location.host+contextRoot;
			//	if ( window.location.protocol.indexOf('https') != -1 ) {
			//		sWsUrl = "wss://"+window.location.host+contextRoot;
			//	}
			//	console.log('wsUrl = '+sWsUrl);

				var oParams = {
					dstrtCd : $('#out-rqst-dtl-dstrtCd').val(),
					outRqstNo : sOutRqstNo,
					rqstPlayEndYmdhms : sRqstPlayEndYmdhms,
					outExtnRqstRsn : sOutExtnRqstRsn,
			//		wsUrl: sWsUrl
				};
				//console.log(oParams);
					
				oCommon.modalConfirm('modal-confirm', '알림', '반출기간연장을 신청하시겠습니까?', function() {
					$.ajax({
						type : 'POST',
						async : false,
						dataType : 'json',
						url:contextRoot + '/tvo/rqst/outRqst/insertOutProdExtn.json',
						data : oParams,
						success : function(data) {
							if (sType == 'SINGLE') {
								var sDstrtCd    = $('#out-rqst-dtl-dstrtCd').val();
								var sOutRqstNo  = $('#out-rqst-dtl-outRqstNo').val();
								var sViewRqstNo = $('#out-rqst-dtl-viewRqstNo').val();
								var sTvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();
								oOutFirstRqst.out.rqst.detail.init(sDstrtCd, sOutRqstNo, sViewRqstNo, sTvoPrgrsCd);
							}

							if (parseInt(data.result)) {
								oTvoCmn.div.clear('#out-prod-extn-rqst');
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
							} else {
								oCommon.modalAlert('modal-alert', '알림', '실패하였습니다.');
							}
						},
						error : function(data, status, err) {
							console.log(data);
						}
					});
					$('#'+oOutFirstRqst.out.prodExtn.id).modal('hide');
				});
			},
			// oOutFirstRqst.out.prodExtn.cancel()
			cancel : function() {
				oCommon.modalConfirm('modal-confirm', '알림', '반출기간연장 신청을 취소하시겠습니까?', function() {
					var $outProdExtnRqst = $('#out-prod-extn-rqst');
					if ($outProdExtnRqst.length) {
						var $col = $outProdExtnRqst.closest('.col');
						$col.empty();
					}
				});
			},
			// oOutFirstRqst.out.prodExtn.close()
			close: function() {
				$('#'+oOutFirstRqst.out.prodExtn.id).modal('hide');
			},
			grid : {
				id : 'out-prod-extn-his',
				// oOutFirstRqst.out.prodExtn.grid.init()
				init : function() {
					var sId = oOutFirstRqst.out.prodExtn.grid.id;
					var oGridParams = oOutFirstRqst.out.prodExtn.grid.params();
					var sGridId = '#grid-' + oOutFirstRqst.out.prodExtn.grid.id;
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
							{	name:'rnum',		align:'center',		width:30,		hidden:true
							   ,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:30px;"';	}
							}, {name:'outRqstNo',				hidden:true
							}, {name:'outExtnRqstYmdhms',		align:'center',		classes:'text-ellipsis'
							   ,formatter : function(cellvalue, options, rowObject) {
									var sOutExtnRqstYmdhms = rowObject.outExtnRqstYmdhms;
									momentYmdhms = moment(sOutExtnRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
									sOutExtnRqstYmdhms = momentYmdhms.format('YYYY-MM-DD HH:mm');
									return sOutExtnRqstYmdhms;
								}
							}, {name:'rqstPlayEndYmdhms',			align:'center',		classes:'text-ellipsis'
							   ,formatter : function(cellvalue, options, rowObject) {
									var sRqstPlayEndYmdhms = rowObject.rqstPlayEndYmdhms;
									momentYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
									sRqstPlayEndYmdhms = momentYmdhms.format('YYYY-MM-DD');
									return sRqstPlayEndYmdhms;
								}
							}, {name:'outExtnRqstRsn',			classes:'text-ellipsis'
							}, {name:'tvoPrgrsNm',			align:'center',				width:60
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
						outRqstNo : $('#out-rqst-dtl-outRqstNo').val()
					};
					return oParams;
				}
			}
		},
		// oOutFirstRqst.out.rqst.file{}
		file : {
			grid : {
				id : 'out-rqst-dtl-file',
				// oOutFirstRqst.out.file.grid.init()
				init : function() {
					var sId = oOutFirstRqst.out.file.grid.id;
					var oGridParams = oOutFirstRqst.out.file.grid.params();
					var sGridId = '#grid-' + oOutFirstRqst.out.file.grid.id;
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
							
							//if (nOutChkStepCd >= 20) {	// 입수대기
							//	oTvoCmn.grid.showCol(sId, 'outFilePlay');	//원본영상 표시
							//}
							//if (sMaskingYn == 'Y') {
							//	oTvoCmn.grid.showCol(sId, 'outFileMasking');	// 원본파일을 마스킹하기 때문에 웹브라우저에서 재생할 수 없다.
							//}
							//if ((nTvoPrgrsCd == 70 || nTvoPrgrsCd == 71) && (nOutChkStepCd == 92 || nOutChkStepCd == 94)) {	// (반출승인 || 자동반출승인) && (암호화완료 || 승인완료)
							if ((nTvoPrgrsCd == 70 || nTvoPrgrsCd == 71) && (nOutChkStepCd == 94)) {
								oTvoCmn.grid.showCol(sId, 'outFileDownload');
							}
							
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
								'#', '영상구간', '반출파일명', '마스킹영상', '암호화영상다운로드', '원본영상'
						],
						colModel : [
							{	name:'outFileInd',		align:'center',		width:20
							   ,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:20px;"';	}
							}, {name:'vdoYmdHmsFrTo',	align:'center',		width:80
							   ,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:80px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var vdoYmdHmsFr = rowObject.outVdoYmdHmsFr;
									var momentYmdhms = moment(vdoYmdHmsFr, 'YYYYMMDDHHmmss');
									if (momentYmdhms.isValid()) {	vdoYmdHmsFr = momentYmdhms.format('YYYY-MM-DD HH:mm');	}
									
									var vdoYmdHmsTo = rowObject.outVdoYmdHmsTo;
									var momentYmdhms = moment(vdoYmdHmsTo, 'YYYYMMDDHHmmss');
									if (momentYmdhms.isValid()) {	vdoYmdHmsTo = momentYmdhms.format('YYYY-MM-DD HH:mm');	}
									
									return vdoYmdHmsFr+" ~ "+vdoYmdHmsTo;
								}
							}, {name:'outFileNmDrm',	align:'center',		classes:'text-ellipsis'
							}, {name:'outFileMasking',		align:'center',		width:40,		hidden:true
							   ,cellattr : function() {	return 'style="width:40px;"';		}
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
									var sOutFileNmMsk = rowObject.outFileNmMsk;
									var sMaskingYn = $('#out-rqst-dtl-maskingYn').text();
									var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();
									var nOutChkStepCd = !isNaN(sOutChkStepCd) ? parseInt(sOutChkStepCd) : 90;
									if (sMaskingYn == 'Y' && nOutChkStepCd == 20 && sOutFileNmMsk == '') {
										var $button = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs',
											'title' : '반출파일을 마스킹합니다.',
											'html' : '<i class="fas fa-mask"></i>',
											'onclick' : 'javascript:oOutFirstRqst.out.file.masking("' + rowObject.outRqstNo + '", "' + rowObject.outFilePath + '", "' + rowObject.outFileNm + '", "' + rowObject.outFileSeq + '");',
										});
										return $button.prop('outerHTML');
									} else if (sMaskingYn == 'Y' && sOutFileNmMsk != '') {
										var $button = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs',
											'title' : '마스킹파일을 플레이어로 재생합니다.',
											'html' : '<i class="fas fa-play"></i>',
											//'onclick' : 'javascript:oOutFirstRqst.out.file.play("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '", "MSK");',
											'onclick' : 'javascript:oOutFirstRqst.out.file.play("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '", "MSK", "' + rowObject.outVdoYmdhmsFr + '");',
										});
										return $button.prop('outerHTML');
									}
									return '';
								}
							}, {name:'outFileDownload',			align:'center',			width:40,		hidden:true
							   ,cellattr : function() {	return 'style="width:40px;"';		}
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
									var sTvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();
									var nTvoPrgrsCd = !isNaN(sTvoPrgrsCd) ? parseInt(sTvoPrgrsCd) : 60;
									var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();
									var nOutChkStepCd = !isNaN(sOutChkStepCd) ? parseInt(sOutChkStepCd) : 90;
									//if ((nTvoPrgrsCd == 70 || nTvoPrgrsCd == 71) && (sOutChkStepCd == 92 || sOutChkStepCd == 94)) {
									if ((nTvoPrgrsCd == 70 || nTvoPrgrsCd == 71) && (sOutChkStepCd == 94)) {
										var $button = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs',
											'title' : '반출파일을 다운로드합니다.',
											'html' : '<i class="fas fa-file-download"></i>',
											'onclick' : 'javascript:oOutFirstRqst.out.file.download("' + rowObject.outFileSeq + '");',
										});
										return $button.prop('outerHTML');
									}
									return '';
								}
							}, {name:'outFilePlay',		align:'center',		width:40,		hidden:true
							   ,cellattr : function() {	return 'style="width:40px;"';	}
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
											'onclick' : 'javascript:oOutFirstRqst.out.file.play("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '", "MP4", "' + rowObject.outVdoYmdhmsFr + '");',
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
						outRqstNo : $('#out-rqst-dtl-outRqstNo').val()
					};
					return oParams;
				},
				search : function() {
					var sId = oOutFirstRqst.out.file.grid.id;
					var oGridParams = oOutFirstRqst.out.file.grid.params();
					oTvoCmn.jqGrid.reload(sId, 1, oGridParams);
				}
			},
			// oOutFirstRqst.out.file.play()
			play : function(outRqstNo, outFileSeq, outFileTy, vdoYmdhmsFr) {
				var url = contextRoot + '/tvo/openHtmlPlayer.do?outRqstNo=' + outRqstNo + '&outFileSeq=' + outFileSeq + '&outFileTy=' + outFileTy + '&vdoYmdhmsFr=' + vdoYmdhmsFr;
				var opener = window.open(url, 'play', 'status=no,width=' + oConfigure.popWidth + ',height=' + oConfigure.popHeight);
				oVmsCommon.player.push(opener);
			},
			// oOutFirstRqst.out.file.download()
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
			// oOutFirstRqst.out.file.masking()
			masking : function(outRqstNo, outFilePath, outFileNm, outFileSeq) {
				console.log(outRqstNo+", "+outFilePath+", "+outFileNm+", "+outFileSeq);

				//var sCmd = oTvoConfig.maskingToolPath;		// 설치경로
				var sCmd = '';
				sCmd += ' -L S';
				sCmd += ' -O ' + outRqstNo;				// -O 반출신청번호
				sCmd += ' -R ' + oConfigure.userId;		// -R 사용자ID
				sCmd += ' -T MOV';						// -T MOV
				sCmd += ' -P ' + oTvoConfig.ftpVdoDir;		// -P 대상경로 ROOT
				sCmd += ' -S ' + outFilePath;			// -S 대상경로 PATH
				sCmd += ' -N ' + outFileNm;				// -N 반출파일명
				sCmd += ' -C ' + outFileSeq;			// -C 반출파일연번
				sCmd += ' -U ' + oTvoConfig.maskToolUsr;		// -U FTP ID/PWD
				sCmd += ' -F ' + oTvoConfig.maskToolFtp;		// -F FTP IP:PORT
				sCmd += ' -A ' + oTvoConfig.maskToolAns;		// -A CALLBACK URL
				//sCmd += ' -F ' + '127.0.0.1:51121';
				//sCmd += ' -U ' + 'tvo/qkscnf!2!2';
				sCmd += ' -M PASSIVE';					// -M FTP MODE ("PASSIVE")
				sCmd += ' -RN ' + outRqstNo;
				sCmd += ' -FS ' + outFileSeq;
				console.log(sCmd);
			//	try {
			//		var objShell = new ActiveXObject('WScript.Shell');
			//		var retutn = objShell.Run(sCmd, 1, false);
			//	} catch (e) {
			//		console.log(e);
			//	}
				// 참고사이트 => Registry를 이용한 URL 호출방법 ( https://itteamb.blogspot.com/2021/02/javascript-chrome-local-registry-url.html )
				var url = "mask:// "+sCmd;
				var exec = document.createElement("a");
				exec.setAttribute("href", url);
				exec.click();
			}
			
		},

	};

}
