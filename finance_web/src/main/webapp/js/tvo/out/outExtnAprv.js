$(function() {
	oOutExtnAprv = new outExtnAprv();
	oOutExtnAprv.init();
});

function outExtnAprv() {
	this.init = function() {
        // LEFT 조절
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			right : true
		}, function() {
			oTvoCmn.datetimepicker.ymd('.aprv-out-prod-extn-outExtnRqstYmdhms', '2023-01', moment().add(100, 'years'));	// 반출기간연장신청일
			oTvoCmn.datetimepicker.ymd('.aprv-out-prod-extn-playEndYmdhms', '2023-01', moment().add(100, 'years'));		// 재생종료일
			codeInfoList('#aprv-out-prod-extn-tvoPrgrsCd', 'OUT_PRGRS', '', '진행상태');
			//codeInfoList('#aprv-out-prod-extn-tvoPrgrsCd', 'TVO_APRV', '', '진행상태');

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
			oOutExtnAprv.out.prodExtn.grid.init();
		});
	};

	// oOutExtnAprv.out{}
	this.out = {
		
		prodExtn : {
			
			grid : {
				id : 'aprv-out-prod-extn',
				page: '1',
				
				init : function() {
					var sId = oOutExtnAprv.out.prodExtn.grid.id;
					var oGridParams = oOutExtnAprv.out.prodExtn.grid.params();
					var sGridId = '#grid-' + oOutExtnAprv.out.prodExtn.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/out/selectOutExtnAprvList.json',
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
							oOutExtnAprv.out.prodExtn.grid.page = data.page;
							var $prgrs = $('#aprv-out-prod-extn-prgrs');
							if ($prgrs.val() != '') {
								$prgrs.val('');
								history.replaceState({}, null, location.pathname);
							}
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
								'열람신청번호', '반출신청번호', '연장신청일시', '종료일자', '신청자', '사건번호', '사건명', '카메라명'
								, 'tvoPrgrsCd', '진행상태', '상세'
						],
						colModel : [
							{	name:'viewRqstNo'       , hidden:true
							}, {name:'outRqstNo'        , align:'center', width:70
							}, {name:'outExtnRqstYmdhms', align:'center', width:100, cellattr : function() {	return 'style="width:100px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var strYmdhms = rowObject.outExtnRqstYmdhms;
									momYmdhms = moment(strYmdhms, 'YYYY-MM-DD HH:mm:ss');
									return momYmdhms.format('YY-MM-DD HH:mm');
								}
							}, {name:'rqstPlayEndYmdhms',		align:'center', width:100, cellattr : function() {	return 'style="width:60px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var strYmdhms = rowObject.rqstPlayEndYmdhms;
									momYmdhms = moment(strYmdhms, 'YYYY-MM-DD HH:mm:ss');
									if (momYmdhms.isValid()) {		
										return momYmdhms.format('YY-MM-DD');
									}
								}
							}, {name:'outExtnRqstUserNm' , align:'center', width:60, cellattr : function() {	return 'style="width:60px;"';		}
							}, {name:'evtNo'             , align:'center', classes:'text-ellipsis'
							}, {name:'evtNm'             , align:'left'  , classes:'text-ellipsis'
							}, {name:'fcltLblNm'         , width:200     , classes:'text-ellipsis', cellattr : function() {	return 'style="width:200px;"';		}
							}, {name:'tvoPrgrsCd'        , hidden:true
							}, {name:'tvoPrgrsNm'        , align:'center', width:60, cellattr : function() {	return 'style="width:60px;"';		}
							}, {name:'outProdExtnDtl'    , align:'center', width:50 , cellattr : function() {	return 'style="width:50px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var $button = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs',
										'title' : '반출연장신청 내용을 조회합니다.',
										//'html' : '<i class="far fa-file-alt"></i> 보기',
										'html' : '보기',
										'onclick' : 'javascript:oOutExtnAprv.out.prodExtn.detail.init("' + rowObject.outRqstNo + '", "' + rowObject.viewRqstNo + '", "' + rowObject.outExtnRqstYmdhms + '");'
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
		            $('#btn-refresh').on('click', () => oOutExtnAprv.out.prodExtn.grid.search());
					
				},
				// oOutExtnAprv.out.prodExtn.grid.search()
				search : function(page) {
					if (typeof page != 'undefined') {
						oOutExtnAprv.out.prodExtn.grid.page = page;
					}
					
					var sId = oOutExtnAprv.out.prodExtn.grid.id;
					var sPage = oOutExtnAprv.out.prodExtn.grid.page;		//alert(sPage);
					var oGridParams = oOutExtnAprv.out.prodExtn.grid.params();
				//	if (typeof prgrs != 'undefined') {
				//		oGridParams.prgrs = prgrs;
				//	}
					$('article div.col').empty();	// 상세화면 비우기
					oTvoCmn.jqGrid.reload(sId, sPage, oGridParams);
				},
				params : function() {
					var sPrgrs = $('#aprv-out-prod-extn-prgrs').val();
					var sOutExtnRqstYmdhms = $('#aprv-out-prod-extn-outExtnRqstYmdhms').val();
					momYmdhms = moment(sOutExtnRqstYmdhms, 'YYYY-MM-DD');
					if (momYmdhms.isValid()) {
						sOutExtnRqstYmdhms = momYmdhms.format('YYYYMMDD');
					}

					var sPlayEndYmdhms = $('#aprv-out-prod-extn-playEndYmdhms').val();
					momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
					if (momentPlayEndYmdhms.isValid()) {
						sPlayEndYmdhms = momentPlayEndYmdhms.format('YYYYMMDD');
					}

					var sTvoPrgrsCd = $('#aprv-out-prod-extn-tvoPrgrsCd option:selected').val();
					var sOutExtnRqstRsn = $('#aprv-out-prod-extn-outExtnRqstRsn').val();

					var sEvtNo = $('#aprv-out-prod-extn-evtNo').val();
					var sFcltLblNm = $('#aprv-out-prod-extn-fcltLblNm').val();

					var oParams = {
						prgrs : sPrgrs,
						outExtnRqstYmdhms : sOutExtnRqstYmdhms,
						playEndYmdhms : sPlayEndYmdhms,
						tvoPrgrsCd : sTvoPrgrsCd,
						outExtnRqstRsn : sOutExtnRqstRsn,
						evtNo : sEvtNo,
						fcltLblNm : sFcltLblNm
					};
					return oParams;
				}
			},
			detail : {
				// oOutExtnAprv.out.prodExtn.detail.init()
				init : function(outRqstNo, viewRqstNo, outExtnRqstYmdhms) {
					$('article div.col').empty();	// 상세화면 비우기
					oDiv.setDiv('left', '0', {
						//target : 'tvoBlank/aprv/div/outProdExtnDtl',			// 반출기간연장신청상세
						target : 'tvoBlank/out/div/outExtnDtl',			// 반출기간연장신청상세
						
						outRqstNo : outRqstNo,
						outExtnRqstYmdhms : outExtnRqstYmdhms
					}, function() {
						var sRqstPlayEndYmdhms = $('#out-prod-extn-dtl-rqstPlayEndYmdhms').text();
						//var momentRqstPlayEndYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						var momentRqstPlayEndYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD');
						if (momentRqstPlayEndYmdhms.isValid()) {
							$('#out-prod-extn-aprv-aprvPlayEndYmdhms').val(sRqstPlayEndYmdhms);
						}
						oTvoCmn.datetimepicker.ymdhm('.out-prod-extn-aprv-aprvPlayEndYmdhms', moment(), moment().add(30, 'days'));
					});
					oDiv.setDiv('left', '1', {
						target : 'tvoBlank/out/div/outRqstDtl',				// 반출신청상세
						outRqstNo : outRqstNo
					}, function() {
						setTimeout(function() {
							let tvoPrgrsCd = $('#out-extn-dtl-tvoPrgrsCd').val();	// 반출기간연장신청진행상태
							if ( '50'==tvoPrgrsCd || '51'==tvoPrgrsCd ) {		// 승인 or 자동승인
								oOutExtnAprv.out.file.grid.init();				// 반출파일목록표시
								$('#out-rqst-dtl-downloadPlayer').removeClass('hide');
							}
						}, 500);
						
						setTimeout(function() {
							$("#out-rqst-dtl-map-pin").trigger("click");		// 지도의 카메라위치로 이동한다.
						}, 500);
					});
				/*	oDiv.setDiv('left', '2', {
						target : 'tvoBlank/view/div/viewRqstDtl',				// 열람신청상세
						viewRqstNo : viewRqstNo
					});	*/
				}
			},
			// oOutExtnAprv.out.prodExtn.aprv{}
			aprv : {
				id: 'modal-aprv',
				element: null,
				// oOutExtnAprv.out.prodExtn.aprv.open()
				open : function() {
					this.element = document.getElementById(this.id);
					if (this.element == null) {
						oDiv.openDiv(this.id, {
							//target : 'tvoBlank/aprv/div/outProdExtnAprv',
							target : 'tvoBlank/out/div/outExtnAprv',
						}, function() {
							//$('input[type=radio][name=out-prod-extn-aprv-tvoPrgrsCd]').prop('checked', false);
							$('input[type=radio][name=out-prod-extn-aprv-tvoPrgrsCd][value="50"]').prop('checked', true);
							$('#out-prod-extn-aprv-tvoPrgrsDtl').val("");
							var sRqstPlayEndYmdhms = $('#out-prod-extn-dtl-rqstPlayEndYmdhms').text();
							//var momentRqstPlayEndYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
							var momentRqstPlayEndYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD');
							if (momentRqstPlayEndYmdhms.isValid()) {
								$('#out-prod-extn-aprv-aprvPlayEndYmdhms').val(sRqstPlayEndYmdhms);
							}
							oTvoCmn.datetimepicker.ymd('.out-prod-extn-aprv-aprvPlayEndYmdhms', moment(), moment().add(100, 'years'));	
						});
					} else {
						$(this.element).modal('show');
						//$('input[type=radio][name=out-prod-extn-aprv-tvoPrgrsCd]').prop('checked', false);
						$('input[type=radio][name=out-prod-extn-aprv-tvoPrgrsCd][value="50"]').prop('checked', true);
						$('#out-prod-extn-aprv-tvoPrgrsDtl').val("");
						var sRqstPlayEndYmdhms = $('#out-prod-extn-dtl-rqstPlayEndYmdhms').text();
						//var momentRqstPlayEndYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						var momentRqstPlayEndYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD');
						if (momentRqstPlayEndYmdhms.isValid()) {
							$('#out-prod-extn-aprv-aprvPlayEndYmdhms').val(sRqstPlayEndYmdhms);
						}
						oTvoCmn.datetimepicker.ymd('.out-prod-extn-aprv-aprvPlayEndYmdhms', moment(), moment().add(100, 'years'));
					}
				},
				// oOutExtnAprv.out.prodExtn.aprv.init()
				/* init : function() {
					oDiv.setDiv('right', '3', {
						target : 'tvoBlank/aprv/div/outProdExtnAprv',
					}, function() {
						var sRqstPlayEndYmdhms = $('#out-prod-extn-dtl-rqstPlayEndYmdhms').text();
						var momentRqstPlayEndYmdhms = moment(sRqstPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						if (momentRqstPlayEndYmdhms.isValid()) {
							$('#out-prod-extn-aprv-aprvPlayEndYmdhms').val(sRqstPlayEndYmdhms);
						}
						oTvoCmn.datetimepicker.ymdhm('.out-prod-extn-aprv-aprvPlayEndYmdhms', moment(), moment().add(100, 'years'));
					});

				}, */
				cancel : function() {
					oCommon.modalConfirm('modal-confirm', '알림', '반출기간연장 승인 창을 닫으시겠습니까?', function() {
						oTvoCmn.div.clear('#out-prod-extn-aprv');
					});
				},
				close : function() {
					$('#' + oOutExtnAprv.out.prodExtn.aprv.id).modal('hide');
				},
				// oOutExtnAprv.out.prodExtn.aprv.approval()
				approval : function() {
					var sTvoPrgrsCd = $('input[name="out-prod-extn-aprv-tvoPrgrsCd"]:checked').val();
					if (!sTvoPrgrsCd) {		oCommon.modalAlert('modal-alert', '알림', '승인구분을 체크해 주세요.');	return;		}
					if ( sTvoPrgrsCd == '30' ) {
						if ( $('#out-prod-extn-aprv-tvoPrgrsDtl').val().trim() == '' ) {
							oCommon.modalAlert('modal-alert', '알림', '반려사유를 입력하세요.');	$('#out-prod-extn-aprv-tvoPrgrsDtl').focus();	return;
						}
					}
					
					//var sOutRqstNo = $('#out-rqst-dtl-outRqstNo').val();
					//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					//var sTvoPrgrsDtl = $('#out-prod-extn-aprv-tvoPrgrsDtl').val();
					
					var sOutExtnRqstYmdhms = $('#out-prod-extn-dtl-outExtnRqstYmdhms').text();
					if (sOutExtnRqstYmdhms) {
						var momYmdhms = moment(sOutExtnRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
						sOutExtnRqstYmdhms = momYmdhms.format('YYYYMMDDHHmmss');
					}

					var sAprvPlayEndYmdhms = $('#out-prod-extn-aprv-aprvPlayEndYmdhms').val();
					if (sAprvPlayEndYmdhms) {
						//var momentAprvPlayEndYmdhms = moment(sAprvPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						//sAprvPlayEndYmdhms = momentAprvPlayEndYmdhms.format('YYYYMMDDHHmmss');
						var momentAprvPlayEndYmdhms = moment(sAprvPlayEndYmdhms, 'YYYY-MM-DD');
						sAprvPlayEndYmdhms = momentAprvPlayEndYmdhms.format('YYYYMMDD')+'235959';
					}

					var oParams = {
						dstrtCd    : $('#out-rqst-dtl-dstrtCd').val(),
						outRqstNo  : $('#out-rqst-dtl-outRqstNo').val(),
						viewRqstNo : $('#view-rqst-dtl-viewRqstNo').val(),
						outExtnRqstYmdhms : sOutExtnRqstYmdhms,
						tvoPrgrsCd  : sTvoPrgrsCd,
						tvoPrgrsDtl : $('#out-prod-extn-aprv-tvoPrgrsDtl').val(),
						aprvPlayEndYmdhms : sAprvPlayEndYmdhms,
					};

					var sFlag = $('input[name="out-prod-extn-aprv-tvoPrgrsCd"]:checked').parent().text().trim();
					oCommon.modalConfirm('modal-confirm', '알림', '반출기간연장신청을 ' + sFlag + '하시겠습니까?', function() {
						$.ajax({
							type : 'POST',
							async : false,
							dataType : 'json',
							url:contextRoot + '/tvo/aprv/outProdExtn/updateOutExtn.json',
							data : oParams,
							success : function(data) {
								if (data.result == '1') {
									oTvoCmn.div.clear('#out-prod-extn-aprv');
									oOutExtnAprv.out.prodExtn.grid.search();			// 목록 새로고침
									oOutExtnAprv.out.prodExtn.detail.init(oParams.outRqstNo, oParams.viewRqstNo, oParams.outExtnRqstYmdhms);
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 ' + sFlag + '하였습니다.');
								} else {
									oCommon.modalAlert('modal-alert', '알림', data.errors);
								}
							},
							error : function(data, status, err) {
								console.log(data);
							}
						});
						$('#' + oOutExtnAprv.out.prodExtn.aprv.id).modal('hide');
					});
				}
			}
		},
		file : {
			grid : {
				id : 'out-rqst-dtl-file',
				// oOutExtnAprv.out.file.grid.init()
				init : function() {
					var sId = oOutExtnAprv.out.file.grid.id;
					var oGridParams = oOutExtnAprv.out.file.grid.params();
					var sGridId = '#grid-' + oOutExtnAprv.out.file.grid.id;
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
								oTvoCmn.grid.showCol(sId, 'outFileMasking');
							}
							if ((nTvoPrgrsCd == 70 || nTvoPrgrsCd == 71) && (sOutChkStepCd == 92 || sOutChkStepCd == 94)) {
								oTvoCmn.grid.showCol(sId, 'outFileDownload');
								//oTvoCmn.grid.showCol(sId, 'checkHash');
							}
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
								'#', '영상구간', '반출파일명', '다운로드', '원본대조', '원본영상'
						],
						colModel : [
							{	name:'outFileInd',		align:'center',			width:20
							   ,cellattr : function(rowId, val, rawObject, cm, rdata) {	return 'style="width:20px;"';	}
							}, {name:'vdoYmdHmsFrTo',	align:'center',			width:80
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
							}, {name:'outFileDownload',		align:'center',		width:40,		hidden:true
							   ,cellattr : function() {		return 'style="width:40px;"';		}
							   ,formatter : function(cellvalue, options, rowObject) {
								
									var $button = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs',
										'title' : '반출파일을 다운로드합니다.',
										'html' : '<i class="fas fa-file-download"></i>',
										'onclick' : 'javascript:oOutExtnAprv.out.file.download("' + rowObject.outFileSeq + '");'
									});
									return $button.prop('outerHTML');
									
								}
							}, {name:'checkHash',			align:'center',			width:40,		hidden:true
							   ,cellattr : function() {		return 'style="width:40px;"';			}
							   ,formatter : function(cellvalue, options, rowObject) {
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
												'onclick' : 'javascript:oOutExtnAprv.out.checkHash.open("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '");',
											});
											return $button.prop('outerHTML');
										}
									}
									return '';
								}
							}, {name:'outFilePlay',		align:'center',		width:40
							   ,cellattr : function() {	return 'style="width:40px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sOutFilePrgrsCd = rowObject.outFilePrgrsCd;
									var nOutFilePrgrsCd = !isNaN(sOutFilePrgrsCd) ? parseInt(sOutFilePrgrsCd) : 90;
									
									//if (nOutFilePrgrsCd >= 40) {	// 입수완료
									if ( '' != rowObject.outFileNmMp4 ) {	// mp4파일이 있을 때
										var $button = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs',
											'title' : '원본영상을 플레이어로 재생합니다.',
											'html' : '<i class="fas fa-play"></i>',
											'onclick' : 'javascript:oOutExtnAprv.out.file.play("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '", "MP4", "' + rowObject.vdoYmdhmsFr + '");'
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
					var sOutRqstNo = $('#out-rqst-dtl-outRqstNo').val();
					var oParams = {
						outRqstNo : sOutRqstNo
					};
					return oParams;
				},
				search : function() {
					var sId = oOutExtnAprv.out.file.grid.id;
					var oGridParams = oOutExtnAprv.out.file.grid.params();
					oTvoCmn.jqGrid.reload(sId, 1, oGridParams);
				}
			},
			// oOutExtnAprv.out.file.play()
			play : function(outRqstNo, outFileSeq, outFileTy, vdoYmdhmsFr) {
				var url = contextRoot + '/tvo/openHtmlPlayer.do?outRqstNo=' + outRqstNo + '&outFileSeq=' + outFileSeq + '&outFileTy=' + outFileTy + '&vdoYmdhmsFr=' + vdoYmdhmsFr;
				var opener = window.open(url, 'play', 'status=no,width=' + oConfigure.popWidth + ',height=' + oConfigure.popHeight);
				oVmsCommon.player.push(opener);
			},
			// oOutExtnAprv.out.file.download()
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
			}
		},
		// oOutExtnAprv.out.checkHash{}
		checkHash: {
			id: 'modal-check-hash',
			element: null,
			// oOutExtnAprv.out.checkHash.open()
			open: function(sOutRqstNo, sOutFileSeq) {
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
						target: 'tvoBlank/out/div/checkHashExtn'		// 파일첨부
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
				$('#' + oOutExtnAprv.out.checkHash.id).modal('hide');
			},
			// oOutExtnAprv.out.checkHash.register()
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
				var oParams = oOutExtnAprv.out.checkHash.params();
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
				$('#' + oOutExtnAprv.out.checkHash.id).modal('hide');
				//$('#out-check-hash').remove();
			},
			params: function() {
				var outRqstNo = $('#out-aprv-hash-outRqstNo').val();
				var outFileSeq = $('#out-aprv-hash-outFileSeq').val();

				let oParams = new FormData();
				oParams.append('outRqstNo', outRqstNo);
				oParams.append('outFileSeq', outFileSeq);

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
