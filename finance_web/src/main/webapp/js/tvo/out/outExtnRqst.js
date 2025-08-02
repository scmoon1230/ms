$(function() {
	oOutExtnRqst = new outExtnRqst();
	oOutExtnRqst.init();
});

function outExtnRqst() {
	this.init = function() {
        // LEFT 조절
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			right : true
		}, function() {
			oTvoCmn.datetimepicker.ymd('.rqst-out-prod-extn-outExtnRqstYmdhms', '2023-01', moment().add(100, 'years'));	// 반출기간연장신청일
			oTvoCmn.datetimepicker.ymd('.rqst-out-prod-extn-playEndYmdhms', '2023-01', moment().add(100, 'years'));		// 재생종료일
			codeInfoList('#rqst-out-prod-extn-tvoPrgrsCd', 'OUT_PRGRS', '', '진행상태');
			//codeInfoList('#rqst-out-prod-extn-tvoPrgrsCd', 'TVO_APRV', '', '진행상태');

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
			oOutExtnRqst.out.prodExtn.grid.init();
		});
	};

	// 
	this.out = {
		
		prodExtn : {
			
			grid : {
				id : 'rqst-out-prod-extn',
				page: '1',
				
				init : function() {
					var sId = oOutExtnRqst.out.prodExtn.grid.id;
					var oGridParams = oOutExtnRqst.out.prodExtn.grid.params();
					var sGridId = '#grid-' + oOutExtnRqst.out.prodExtn.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/rqst/outProdExtn/selectOutProdExtnList.json',
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
							oOutExtnRqst.out.prodExtn.grid.page = data.page;
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
								'viewRqstNo', 'outRqstNo', '신청일시', '종료일자', '사건번호', '사건명', '카메라명'
								, 'tvoPrgrsCd', '진행상태', '상세'
						],
						colModel : [
							{	name:'viewRqstNo',				hidden:true
							}, {name:'outRqstNo',				hidden:true
							}, {name:'outExtnRqstYmdhms',	align:'center',	width:100,	cellattr : function() {	return 'style="width:100px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var strYmdhms = rowObject.outExtnRqstYmdhms;
									momentYmdhms = moment(strYmdhms, 'YYYY-MM-DD HH:mm:ss');
									strYmdhms = momentYmdhms.format('YY-MM-DD HH:mm');
									return strYmdhms;
								}
							}, {name:'rqstPlayEndYmdhms',	align:'center',		width:100,	cellattr : function() {	return 'style="width:60px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var strYmdhms = rowObject.rqstPlayEndYmdhms;
									momentYmdhms = moment(strYmdhms, 'YYYY-MM-DD HH:mm:ss');
									strYmdhms = momentYmdhms.format('YY-MM-DD');
									return strYmdhms;
								}
							}, {name:'evtNo',			align:'center',		classes:'text-ellipsis'
							}, {name:'evtNm',			align:'left',		classes:'text-ellipsis'
							}, {name:'fcltLblNm',		width:200,			classes:'text-ellipsis',		cellattr : function() {	return 'style="width:200px;"';		}
							}, {name:'tvoPrgrsCd',		hidden:true
							}, {name:'tvoPrgrsNm',		align:'center',	width:60,		cellattr : function() {	return 'style="width:60px;"';		}
							}, {name:'outProdExtnDtl',	align:'center',	width:60,		cellattr : function() {	return 'style="width:60px;"';		},
								formatter : function(cellvalue, options, rowObject) {
									var $button = $('<button/>', {
										'type' : 'button',
										'class' : 'btn btn-default btn-xs',
										'title' : '반출연장신청 내용을 조회합니다.',
										//'html' : '<i class="far fa-file-alt"></i> 보기',
										'html' : '보기',
										'onclick' : 'javascript:oOutExtnRqst.out.prodExtn.detail.init("' + rowObject.outRqstNo + '", "' + rowObject.viewRqstNo + '", "' + rowObject.outExtnRqstYmdhms + '");'
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
		            $('#btn-refresh').on('click', () => oOutExtnRqst.out.prodExtn.grid.search());
					
				},
				// oOutExtnRqst.out.prodExtn.grid.search()
				search : function(page) {
					if (typeof page != 'undefined') {
						oOutExtnRqst.out.prodExtn.grid.page = page;
					}
						
					var sId = oOutExtnRqst.out.prodExtn.grid.id;
					var sPage = oOutExtnRqst.out.prodExtn.grid.page;		//alert(sPage);
					var oGridParams = oOutExtnRqst.out.prodExtn.grid.params();
					$('article div.col').empty();	// 상세화면 비우기
					oTvoCmn.jqGrid.reload(sId, sPage, oGridParams);
				},
				params : function() {
					var sOutExtnRqstYmdhms = $('#rqst-out-prod-extn-outExtnRqstYmdhms').val();
					momentOutExtnRqstYmdhms = moment(sOutExtnRqstYmdhms, 'YYYY-MM-DD');
					if (momentOutExtnRqstYmdhms.isValid()) {
						sOutExtnRqstYmdhms = momentOutExtnRqstYmdhms.format('YYYYMMDD');
					}

					var sPlayEndYmdhms = $('#rqst-out-prod-extn-playEndYmdhms').val();
					momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
					if (momentPlayEndYmdhms.isValid()) {
						sPlayEndYmdhms = momentPlayEndYmdhms.format('YYYYMMDD');
					}

					var sTvoPrgrsCd = $('#rqst-out-prod-extn-tvoPrgrsCd option:selected').val();
					var sOutExtnRqstRsn = $('#rqst-out-prod-extn-outExtnRqstRsn').val();
					var sEvtNo = $('#rqst-out-prod-extn-evtNo').val();
					var sFcltLblNm = $('#rqst-out-prod-extn-fcltLblNm').val();
					var sMaskingEndYn = $('#rqst-out-prod-extn-maskingEndYn').is(':checked') ? 'Y' : 'N';

					var oParams = {
						outExtnRqstYmdhms : sOutExtnRqstYmdhms,
						playEndYmdhms : sPlayEndYmdhms,
						tvoPrgrsCd : sTvoPrgrsCd,
						outExtnRqstRsn : sOutExtnRqstRsn,
						evtNo : sEvtNo,
						fcltLblNm : sFcltLblNm,
						maskingEndYn : sMaskingEndYn
					};

					return oParams;
				}
			},
			detail : {
				// oOutExtnRqst.out.prodExtn.detail.init()
				init : function(outRqstNo, viewRqstNo, outExtnRqstYmdhms) {
					$('article div.col').empty();	// 상세화면 비우기
					oDiv.setDiv('left', '0', {
						target : 'tvoBlank/out/div/outExtnDtl',			// 반출기간연장신청상세
						outRqstNo : outRqstNo,
						outExtnRqstYmdhms : outExtnRqstYmdhms
					});
					oDiv.setDiv('left', '1', {
						target : 'tvoBlank/out/div/outRqstDtl',				// 반출신청상세
						outRqstNo : outRqstNo
					}, function() {
						setTimeout(function() {
							let tvoPrgrsCd = $('#out-extn-dtl-tvoPrgrsCd').val();	// 반출기간연장신청진행상태
							if ( '50'==tvoPrgrsCd || '51'==tvoPrgrsCd ) {		// 승인 or 자동승인
								oOutExtnRqst.out.file.grid.init();				// 반출파일목록표시
								$('#out-rqst-dtl-downloadPlayer').removeClass('hide');
							}
						}, 500);
						
						setTimeout(function() {
							$("#out-rqst-dtl-map-pin").trigger("click");		// 지도의 카메라위치로 이동한다.
						}, 500);
					});
				/*	oDiv.setDiv('left', '2', {
						target : 'tvoBlank/rqst/div/viewRqstDtl',				// 열람신청상세
						viewRqstNo : viewRqstNo
					});	*/
				}
			}
		},
		file : {
			grid : {
				id : 'out-rqst-dtl-file',
				// oOutExtnRqst.out.file.grid.init()
				init : function() {
					var sId = oOutExtnRqst.out.file.grid.id;
					var oGridParams = oOutExtnRqst.out.file.grid.params();
					var sGridId = '#grid-' + oOutExtnRqst.out.file.grid.id;
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
							if (sMaskingYn == 'Y') {
								oTvoCmn.grid.showCol(sId, 'outFileMasking');
							}
							if ((nTvoPrgrsCd == 70 || nTvoPrgrsCd == 71) && (sOutChkStepCd == 92 || sOutChkStepCd == 94)) {
								oTvoCmn.grid.showCol(sId, 'outFileDownload');
							}
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames : [
								'#', '영상구간', '반출파일명', '암호화영상다운로드', '원본영상'
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
									var sTvoPrgrsCd = $('#out-rqst-dtl-tvoPrgrsCd').val();
									var nTvoPrgrsCd = !isNaN(sTvoPrgrsCd) ? parseInt(sTvoPrgrsCd) : 60;
									var sOutChkStepCd = $('#out-rqst-dtl-outChkStepCd').val();
									var nOutChkStepCd = !isNaN(sOutChkStepCd) ? parseInt(sOutChkStepCd) : 90;
									if ((nTvoPrgrsCd == 70 || nTvoPrgrsCd == 71) && (sOutChkStepCd == 92 || sOutChkStepCd == 94)) {
										var $button = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs',
											'title' : '반출파일을 다운로드합니다.',
											'html' : '<i class="fas fa-file-download"></i>',
											'onclick' : 'javascript:oOutExtnRqst.out.file.download("' + rowObject.outFileSeq + '");',
										});
										return $button.prop('outerHTML');
									}
									return '';
								}
							}, {name:'outFilePlay',		align:'center',		width:40,		hidden:true
							   ,cellattr : function() {	return 'style="width:40px;"';	}
							   ,formatter : function(cellvalue, options, rowObject) {
									var sOutFilePrgrsCd = rowObject.outFilePrgrsCd;
									var nOutFilePrgrsCd = !isNaN(sOutFilePrgrsCd) ? parseInt(sOutFilePrgrsCd) : 90;
									if (nOutFilePrgrsCd >= 40) {	// 입수완료
										var $button = $('<button/>', {
											'type' : 'button',
											'class' : 'btn btn-default btn-xs',
											'title' : '원본영상을 플레이어로 재생합니다.',
											'html' : '<i class="fas fa-play"></i>',
											'onclick' : 'javascript:oOutExtnRqst.out.file.play("' + rowObject.outRqstNo + '", "' + rowObject.outFileSeq + '", "MP4", "' + rowObject.vdoYmdhmsFr + '");',
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
					var sId = oOutExtnRqst.out.file.grid.id;
					var oGridParams = oOutExtnRqst.out.file.grid.params();
					oTvoCmn.jqGrid.reload(sId, 1, oGridParams);
				}
			},
			// oOutExtnRqst.out.file.play()
			play : function(outRqstNo, outFileSeq, outFileTy, vdoYmdhmsFr) {
				var url = contextRoot + '/tvo/openHtmlPlayer.do?outRqstNo=' + outRqstNo + '&outFileSeq=' + outFileSeq + '&outFileTy=' + outFileTy + '&vdoYmdhmsFr=' + vdoYmdhmsFr;
				var opener = window.open(url, 'play', 'status=no,width=' + oConfigure.popWidth + ',height=' + oConfigure.popHeight);
				oVmsCommon.player.push(opener);
			},
			// oOutExtnRqst.out.file.download()
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
							'id': 'form-drmFile'	, 'method': 'POST'		, 'action': contextRoot + '/tvo/downloadDrmFile.do'		});
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'dstrtCd'		, 'value': $('#out-rqst-dtl-dstrtCd').val()				}));
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'outRqstNo'	, 'value': $('#out-rqst-dtl-outRqstNo').val()			}));
						$formDownload.append($('<input/>', {
							'type': 'hidden'		, 'name': 'outFileSeq'	, 'value': pOutFileSeq									}));
						$formDownload.appendTo(document.body);
					}
					$formDownload.submit();
				//}, undefined);
			}
		}
	};
}
