$(function () {
	oViewFirstRqst = new viewFirstRqst();
	oViewFirstRqst.init();
});

function viewFirstRqst() {
	this.init = function () {
		collapse({
			right: true
		}, function () {
			oTvoCmn.datetimepicker.ymd('.rqst-view-viewRqstYmdhms', moment().add(-5, 'years'), moment().add(100, 'years'));		// 열람신청일
			//oTvoCmn.datetimepicker.ymd('.rqst-view-viewEndYmdhms', moment().add(-5, 'years'), moment().add(100, 'years'));	// 열람종료일
			codeInfoList('#rqst-view-rqstRsnTyCd', 'RQST_RSN_TY', '', '신청사유');
			codeInfoList('#rqst-view-tvoPrgrsCd', 'VIEW_PRGRS', '', '진행상태');
			dstrtInfoList('#rqst-view-dstrtCd', $("#dstrtCd").val(), '');
			dstrtInfoList('#rqst-view-evtDstrtCd', $("#dstrtCd").val(), '');

			oViewFirstRqst.map.init();

			oViewFirstRqst.view.rqst.grid.init();
		});
	};

	// 열람 oViewFirstRqst.view{}
	this.view = {
		// 신청 oViewFirstRqst.view.rqst{}
		rqst: {
			// 목록 oViewFirstRqst.view.rqst.grid{}
			grid: {
				id: 'rqst-view',
				page: '1',
				// oViewFirstRqst.view.rqst.grid.init()
				init: function () {
					var sId = oViewFirstRqst.view.rqst.grid.id;
					var oGridParams = oViewFirstRqst.view.rqst.grid.params();
					var sGridId = '#grid-' + oViewFirstRqst.view.rqst.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/view/selectViewRqstList.json',
						datatype: 'json',
						mtype: 'POST',
						height: 'auto',
						rowNum: 5,
						autowidth: true,
						shrinkToFit: true,
						postData: oGridParams,
						beforeRequest: function () {
						},
						loadComplete: function (data) {
							oViewFirstRqst.view.rqst.grid.page = data.page;
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames: [
							'dstrtCd', '신청지구', '신청번호', '신청일자', '신청사유', '사건번호', '사건명', '발생일시', '발생주소', 'tvoPrgrsCd', '진행상태', '열람종료', '영상열람', '활용결과', '상세', '반출'
						],
						colModel: [
							{	name:'dstrtCd'       , hidden: true
							}, {name:'dstrtNm'       , align:'center', width:60
							}, {name:'viewRqstNo'    , align:'center', width:60
							    , formatter: function (cellvalue, options, rowObject) { return rowObject.viewRqstNo.substring(5); }
							}, {name:'viewRqstYmdhms', align:'center', width:60  , cellattr: function () { return 'style="width:60px;"'; }
								, formatter: function (cellvalue, options, rowObject) {
									var strYmdhms = rowObject.viewRqstYmdhms;
									var momentYmdhms = moment(strYmdhms, 'YYYYMMDDHHmmss');
									var $span = $('<span/>', {
										'title': momentYmdhms.format('YYYY-MM-DD HH:mm:ss'),
										'text': momentYmdhms.format('YY-MM-DD')
									});
									return $span.prop('outerHTML');
								}
							}, {name:'rqstRsnTyNm'  , align:'center', width:80  , cellattr: function () { return 'style="width:80px;"';  }
							}, {name:'evtNo'        , align:'center', width:100 , cellattr: function () { return 'style="width:100px;"'; }
							}, {name:'evtNm'        , align:'left'  , width:100 , cellattr: function () { return 'style="width:100px;"'; }
							}, {name:'evtYmdhms'    , hidden: true
							}, {name:'evtAddr'      , align:'left'  , classes: 'text-ellipsis'
							}, {name:'tvoPrgrsCd'   , hidden: true
							}, {name:'tvoPrgrsNm'   , align:'center', width:60  , cellattr: function () { return 'style="width:60px;"'; }
							}, {name:'viewEndYmdhms', align:'center', width:60  , cellattr: function () { return 'style="width:60px;"'; }
							   , formatter: function (cellvalue, options, rowObject) {
									var strYmdhms = rowObject.viewEndYmdhms;
									var momentYmdhms = moment(strYmdhms, 'YYYYMMDDHHmmss');
									if (momentYmdhms.isValid()) {
										var $span = $('<span/>', {
											'title': momentYmdhms.format('YYYY-MM-DD HH:mm:ss'),
											'text': momentYmdhms.format('YY-MM-DD')
										});
										return $span.prop('outerHTML');
									} else {
										return '';
									}
								}
							}, {name:'viewPmsYnNm'   , align:'center', width:60  , cellattr: function () { return 'style="width:60px;"'; }
							}, {name:'viewResultTyNm', align:'center', width:60  , cellattr: function () { return 'style="width:60px;"'; }
							}, {name:'viewRqstDtl'   , align:'center', width:40  , cellattr: function () { return 'style="width:40px;"'; }
							   , formatter: function (cellvalue, options, rowObject) {
									var $button = $('<button/>', {
										'type': 'button',
										'class': 'btn btn-default btn-xs',
										'title': '열람신청 내용을 조회합니다.',
										'html': '보기',
										'onclick': 'javascript:oViewFirstRqst.view.rqst.detail.init("' + rowObject.dstrtCd + '", "' + rowObject.viewRqstNo + '");'
									});
									return $button.prop('outerHTML');
								}
							}, {name:'outRqst'      , align:'center', width:40  , cellattr: function () { return 'style="width:40px;"';  }
							   , formatter: function (cellvalue, options, rowObject) {
									if (rowObject.viewEndYn == 'N' && (rowObject.tvoPrgrsCd == '50' || rowObject.tvoPrgrsCd == '51')) {	// 열람기간끝나지 않음 && 승인,자동승인
										var $button = $('<button/>', {
											'type': 'button',
											'class': 'btn btn-default btn-xs',
											'title': '영상반출을 신청합니다.',
											'html': '신청',
											'onclick': 'javascript:oViewFirstRqst.view.rqst.detail.initRqstVdo("' + rowObject.dstrtCd + '", "' + rowObject.viewRqstNo + '", "' + rowObject.tvoPrgrsCd + '");'
										});
										return $button.prop('outerHTML');
									} else {
										return '';
									}
								}
							}
						],
						jsonReader: {
							root: "rows",
							total: "totalPages",
							records: "totalRows"
						},
						onSelectRow: function (rowId) {

						},
						cmTemplate: {
							sortable: false,
							resizable: false
						}
					});

					// 새로고침
					$('#btn-refresh').on('click', () => oViewFirstRqst.view.rqst.grid.search());

					// 기본화면
					$('#btn-normal-display').on('click', () => {
						$('#chk-his-yn').prop('checked', false);
						$('.container-mask').addClass('hide');
						//person.details.hide();
						//detection.history.hide();
						clearDiv();
						$('article div.col').empty();	// 상세화면 비우기
						//mntr.stop();
						//grid.search();
						oViewFirstRqst.view.rqst.grid.search();
						olMap.setCenter([oConfigure.pointX, oConfigure.pointY]);
						doDivNormal('GENERAL', 'GENERAL', null, null);
						oCurrentEvent.clear();
						event.previous = null;
						$('#ol-overlay-click').hide();
						$('#ol-overlay-click tbody').empty();
						olSwipMap.contextmenu.close();	// 위치선택 닫기
						olSwipMap.mntr.popover.close();	// 팝오버 닫기
					});

				},
				// oViewFirstRqst.view.rqst.grid.search()
				search: function (page) {
					if (typeof page != 'undefined') {
						oViewFirstRqst.view.rqst.grid.page = page;
					}

					var sFr = $('#rqst-view-viewRqstYmdhms-fr').val();
					var sTo = $('#rqst-view-viewRqstYmdhms-to').val();
					if (sFr != '' && sTo != '') {
						if ( sFr > sTo ) {	alert('기간설정에 오류가 있습니다.');	return;
						}
					}
					var sId = oViewFirstRqst.view.rqst.grid.id;
					var sPage = oViewFirstRqst.view.rqst.grid.page;		//alert(sPage);
					var oGridParams = oViewFirstRqst.view.rqst.grid.params();
					$('article div.col').empty();	// 상세화면 비우기
					oTvoCmn.jqGrid.reload(sId, sPage, oGridParams);
				},
				// oViewFirstRqst.view.rqst.grid.params()
				params: function () {
					var sViewRqstYmdhmsFr = $('#rqst-view-viewRqstYmdhms-fr').val();
					var momentViewRqstYmdhmsFr = moment(sViewRqstYmdhmsFr, 'YYYY-MM-DD');
					if (momentViewRqstYmdhmsFr.isValid()) {
						sViewRqstYmdhmsFr = momentViewRqstYmdhmsFr.format('YYYYMMDD');
					} else {
						sViewRqstYmdhmsFr = '';
					}

					var sViewRqstYmdhmsTo = $('#rqst-view-viewRqstYmdhms-to').val();
					var momentViewRqstYmdhmsTo = moment(sViewRqstYmdhmsTo, 'YYYY-MM-DD');
					if (momentViewRqstYmdhmsTo.isValid()) {
						sViewRqstYmdhmsTo = momentViewRqstYmdhmsTo.format('YYYYMMDD');
					} else {
						sViewRqstYmdhmsTo = '';
					}
					/*
					var sViewEndYmdhms = $('#rqst-view-viewEndYmdhms').val();
					var momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD');
					if (momentViewEndYmdhms.isValid()) {
						sViewEndYmdhms = momentViewEndYmdhms.format('YYYYMMDD');
					} else {
						sViewEndYmdhms = '';
					}
					*/
					var oParams = {
						viewRqstUserId : $('#userId').val(),
						viewRqstYmdhmsFr: sViewRqstYmdhmsFr,
						viewRqstYmdhmsTo: sViewRqstYmdhmsTo,
						//viewEndYmdhms: sViewEndYmdhms,
						rqstRsnTyCd: $('#rqst-view-rqstRsnTyCd option:selected').val(),
						tvoPrgrsCd: $('#rqst-view-tvoPrgrsCd option:selected').val(),
						dstrtCd : $('#rqst-view-dstrtCd option:selected').val(),
						evtDstrtCd : $('#rqst-view-evtDstrtCd option:selected').val(),
						evtNo: $('#rqst-view-evtNo').val(),
						evtNm: $('#rqst-view-evtNm').val()
					};
					return oParams;
				}
			},
			// oViewFirstRqst.view.rqst.detail{}
			detail: {
				// oViewFirstRqst.view.rqst.detail.init()
				init: function (pDstrtCd, pViewRqstNo) {

					let isVisible = $('#bottom').is(':visible');
					if (isVisible) {	collapse({bottom: isVisible});	}	// bottom 닫기

					$('article div.col').empty();	// 상세화면 비우기
					$('#modal-view-out-rqst').remove();		// 반출신청목록 제거
					$('#modal-view-prod-extn').remove();	// 열람기간연장이력 제거

					oDiv.setDiv('left', '0', {
						target: 'tvoBlank/view/div/viewRqstDtl',				// 열람신청상세
						viewRqstNo: pViewRqstNo,
						dstrtCd: pDstrtCd
					}, function () {
						oTvoCmn.datetimepicker.ymdhm('.out-rqst-cctv-vdoYmdhmsFr', moment().add(-31, 'days'), moment());
						oTvoCmn.datetimepicker.ymdhm('.out-rqst-cctv-vdoYmdhmsTo', moment().add(-31, 'days'), moment());

						// 과거영상일 경우 세팅한다.
						let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
						let sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
						const momEvtYmdhms = moment($('#view-rqst-dtl-evtYmdhms').text(), 'YYYY-MM-DD HH:mm:ss');
						oCurrentEvent.event = {dstrtCd: sDstrtCd, viewRqstNo: sViewRqstNo, evtOcrYmdHms: momEvtYmdhms.format('YYYYMMDDHHmmss')};

						$('#chk-cctv-search-yn').prop('checked', true);

						if ($('#view-rqst-dtl-viewPmsYn').val() == 'Y') {
							$('#spn-chk-designation').removeClass('hide');		// 선택영상 표시
						} else {
							$('#spn-chk-designation').addClass('hide');			// 선택영상 숨김
						}

						setTimeout(function () {
							$("#view-rqst-dtl-map-pin").trigger("click");		// 지도의 발생위치로 이동한다.
						}, 500);
					});
					
					oDiv.setDiv('left', '1', {
						target : 'tvoBlank/view/div/viewExtnList',			// 열람기간연장이력
					}, function() {
						setTimeout(function() {
							oViewFirstRqst.view.prodExtn.grid.init();
						}, 500);
					});
					oDiv.setDiv('left', '2', {
						target: 'tvoBlank/out/div/outRqstList'				// 반출신청목록
					}, function() {
						setTimeout(function() {
							oViewFirstRqst.out.rqst.grid.init();
						}, 500);
					});
				/*	oDiv.setDiv('left', '3', {
						target: 'tvoBlank/rqst/div/viewRqstCctvSearch'		// 발생위치부근카메라조회
					}, function() {
						setTimeout(function() {
							oViewFirstRqst.cctv.grid.init();
						}, 500);
					});
				*/
				},
				// oViewFirstRqst.view.rqst.detail.initRqstVdo()
				initRqstVdo: function (pDstrtCd, pViewRqstNo, sTvoPrgrsCd) {
					$.ajax({
						type: 'POST',
						async: false,
						dataType: 'json',
						url:contextRoot + '/tvo/rqst/viewRqst/selectViewRqstDtl.json',	// 열람신청 상세
						data: {
							dstrtCd: pDstrtCd,
							viewRqstNo: pViewRqstNo
						},
						success: function (data) {
							console.log(data.viewRqst);
							if (data.viewRqst.paperFileNm == '' && data.viewRqst.paperNo == '') {
								//oCommon.modalConfirm('modal-confirm', '알림', '공문파일 또는 전자문서번호가 필요합니다.', function() {
								//oViewFirstRqst.view.rqst.detail.init(pViewRqstNo);
								oViewFirstRqst.view.paperFile.open(pDstrtCd, pViewRqstNo, data.viewRqst.viewRqstYmdhms);
								//});

							} else {
								let isVisible = $('#bottom').is(':visible');
								if (isVisible) {
									collapse({bottom: isVisible});
								}	// bottom 닫기

								$('article div.col').empty();	// 상세화면 비우기
								$('#modal-view-out-rqst').remove();					// 반출신청목록 제거
								$('#modal-view-prod-extn').remove();				// 열람기간연장이력 제거
								$('#spn-chk-designation').removeClass('hide');		// 선택영상 표시

								oDiv.setDiv('left', '0', {
									target: 'tvoBlank/view/div/viewRqstVdo',		// 반출영상신청
									dstrtCd: pDstrtCd,
									viewRqstNo: pViewRqstNo
								}, function () {
									oTvoCmn.datetimepicker.ymdhm('.out-rqst-cctv-vdoYmdhmsFr', moment().add(-31, 'days'), moment());
									oTvoCmn.datetimepicker.ymdhm('.out-rqst-cctv-vdoYmdhmsTo', moment().add(-31, 'days'), moment());

									// 과거영상일 경우 세팅한다.
									let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
									let sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
									const momEvtYmdhms = moment($('#view-rqst-dtl-evtYmdhms').text(), 'YYYY-MM-DD HH:mm:ss');
									oCurrentEvent.event = {dstrtCd: sDstrtCd, viewRqstNo: sViewRqstNo, evtOcrYmdHms: momEvtYmdhms.format('YYYYMMDDHHmmss')};

									$('#chk-cctv-search-yn').prop('checked', true);
									
									// 마스킹 사용할 때에만 체크박스를 표시한다.
									console.log("=== oTvoConfig.maskUseYn => "+oTvoConfig.maskUseYn);
									if ( 'Y'==oTvoConfig.maskUseYn ) {
										$("#out-rqst-cctv-maskingYn-dv").removeClass("hide");
									}

									setTimeout(function () {
										$("#view-rqst-dtl-map-pin").trigger("click");					// 지도의 발생위치로 이동한다.
									}, 500);
								});

							}
						},
						error: function (data, status, err) {
							console.log(data);
						}
					});
				},
			},
			// 요청 oViewFirstRqst.view.rqst.request{}
			request: {
				// oViewFirstRqst.view.rqst.request.init()
				init: function () {
					$('article div.col').empty();	// 상세화면 비우기
					oDiv.setDiv('left', '0', {
						target: 'tvoBlank/view/div/viewRqstReg'					// 신규신청
					}, function () {
						codeInfoList('#view-rqst-rqstRsnTyCd', 'RQST_RSN_TY', '', '신청사유');
						//codeInfoList('#view-rqst-emrgYn', 'EMRG_YN', '', '긴급구분');

						oTvoCmn.datetimepicker.ymdhm('.view-rqst-evtYmdhms', moment().add(-31, 'days'), moment());
						oTvoCmn.datetimepicker.ymd('.view-rqst-viewEndYmdhmsWant', moment(), moment().add(30, 'days'));
						
						let viewRqstDuration = Number($("#view-rqst-duration").val());		// 열람신청기간
						$("#view-rqst-viewEndYmdhmsWant").val(moment().add(viewRqstDuration, 'days').format('YYYY-MM-DD'));

						//oViewFirstRqst.cctv.grid.init();
					});
				},
				
				// oViewFirstRqst.view.rqst.request.changeEmrgYn()
				changeEmrgYn: function () {
					let emrgYn = $("input[name='view-rqst-emrgYn']:checked").val();
					if ("Y"==emrgYn) {
						$('#spEmrgRsn').removeClass('hide');
					} else {
						$('#spEmrgRsn').addClass('hide');
					}
				},
			
				// oViewFirstRqst.view.rqst.request.agreeDisplay()
				agreeDisplay: function () {
					if ($('#view-rqst-agree-content').attr("style") == "display:none;") {
						$('#view-rqst-agree-content').attr("style", "display:block;");
						$('#view-rqst-agree-btntitle').text("서약닫기");
					} else {
						$('#view-rqst-agree-content').attr("style", "display:none;");
						$('#view-rqst-agree-btntitle').text("서약보기");
						$("input:checkbox[id='view-rqst-agree']").prop("checked", true);
					}
				},
				// oViewFirstRqst.view.rqst.request.cancel()
				cancel: function () {
					oCommon.modalConfirm('modal-confirm', '알림', '열람신청을 취소하시겠습니까?', function () {
						oTvoCmn.div.clear('#view-rqst');
					});
				},
				// oViewFirstRqst.view.rqst.request.register()
				register: function () {
					olSwipMap.contextmenu.close();	// 위치선택 닫기
					
					if ($('#view-rqst-evtNo').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '사건번호를 입력하세요.');
						$('#view-rqst-evtNo').focus();
						return;
					}
					if ($('#view-rqst-evtNm').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '사건명을 입력하세요.');
						$('#view-rqst-evtNm').focus();
						return;
					}
					if ($('#view-rqst-evtYmdhms').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '발생일시를 입력하세요.');
						$('#view-rqst-evtYmdhms').focus();
						return;
					}
					if ($('#view-rqst-evtAddr').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '발생주소를 선택하세요.');
						$('#view-rqst-evtAddr').focus();
						return;
					}
					if ($('#view-rqst-rqstRsnTyCd').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '신청사유를 선택하세요.');
						$('#view-rqst-rqstRsnTyCd').focus();
						return;
					}
					if ($('#view-rqst-viewEndYmdhmsWant').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '열람종료일자를 입력하세요.');
						$('#view-rqst-viewEndYmdhmsWant').focus();
						return;
					}

					//if ( $('#view-rqst-emrgYn').val().trim() == '' )			{	oCommon.modalAlert('modal-alert', '알림', '긴급구분을 선택하세요.');		$('#view-rqst-emrgYn').focus();				return;	}
					var sEmrgYn = $('input[name="view-rqst-emrgYn"]:checked').val();
					if (!sEmrgYn) {
						oCommon.modalAlert('modal-alert', '알림', '긴급구분을 선택하세요.');
						return;

					} else if (sEmrgYn == "Y") {		// 긴급일 때
						if ( "" == $('#view-rqst-emrgRsn').val().trim() ) {
							oCommon.modalAlert('modal-alert', '알림', '긴급사유를 입력하세요.');
							return;
						}
					} else if (sEmrgYn == "N") {		// 일반일 때

						let paperNo = $('#view-rqst-paperNo').val().trim();
						let oFiles = $('#view-rqst-paperFileNm')[0].files;
						if (paperNo == '' && !oFiles.length) {
							oCommon.modalAlert('modal-alert', '알림', '긴급구분이 일반일 경우 공문파일 또는 전자문서번호가 필요합니다.');
							return;
						} else if (paperNo != '' && oFiles.length) {
							oCommon.modalAlert('modal-alert', '알림', '공문파일 또는 전자문서번호 중 하나만 입력하세요.');
							return;
						}
						
						if (oFiles.length) {
							let sz = $('#view-rqst-paperFileNm')[0].files[0].size;	//console.log(sz);
							let mx = 1024 * 1024 * 5;								//console.log(mx);
							if (mx < sz) {											// 파일 사이즈 체크
								oCommon.modalAlert('modal-alert', '알림', '5 MB 이하의 파일만 첨부가능합니다.');
								return;
							}

							let nm = $('#view-rqst-paperFileNm').val();					//console.log(nm);
							let ex = nm.substring(nm.indexOf('.') + 1).toLowerCase();	//console.log(ex);
							let ty = "hwp, hwpx, doc, docx, pdf";						//console.log(ty.indexOf(ex));
							if (ty.indexOf(ex) == -1) {									// 파일 확장자 체크
								oCommon.modalAlert('modal-alert', '알림', ty + ' 확장자를 가진 파일만 첨부가능합니다.');
								return;
							}
						}
					}

					if (!$('#view-rqst-agree').is(':checked')) {
						oCommon.modalAlert('modal-alert', '알림', '개인정보보호서약에 동의하세요.');
						$('#view-rqst-agree').focus();
						return;
					}

					var oParams = oViewFirstRqst.view.rqst.request.params();
						
					oCommon.modalConfirm('modal-confirm', '알림', '열람신청을 등록하시겠습니까?', function () {
						$.ajax({
							type: 'POST',
							enctype: 'multipart/form-data',
							cache: false,
							contentType: false,
							processData: false,
							async: false,
							//dataType: 'json',
							url:contextRoot + '/tvo/rqst/viewRqst/insertViewRqst.json',
							data: oParams,
							success: function (data) {
								if (data.result == '1') {
									oTvoCmn.jqGrid.reload('rqst-view', 1, oViewFirstRqst.view.rqst.grid.params());
									oTvoCmn.div.clear('#view-rqst');
									//oViewFirstRqst.view.rqst.detail.init(data.rqstViewVO.viewRqstNo);
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
								} else {
									if (data.errors.length) {
										var sMessage = '';
										var oErrors = data.errors;
										$.each(oErrors, function (i, v) {
											sMessage += v.defaultMessage + '\r\n';
											var sFieId = v.field;
											$('#view-rqst-' + sFieId).closest('td').addClass('has-error');
										});
										oCommon.modalAlert('modal-alert', '알림', sMessage);
									}
								}
							},
							error: function (data, status, err) {
								console.log(data);
							}
						});
					});
				},
				// oViewFirstRqst.view.rqst.request.params()
				params: function () {
					//var sEvtNo = $('#view-rqst-evtNo').val();
					//var sEvtNm = $('#view-rqst-evtNm').val();
					//var sEvtGrd = $('#view-rqst-evtGrd').val();
					//var sEvtTy = $('#view-rqst-evtTy').val();
					var sEvtYmdhms = $('#view-rqst-evtYmdhms').val();
					//var momentEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss');
					var momentEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm');
					if (momentEvtYmdhms.isValid()) {
						sEvtYmdhms = momentEvtYmdhms.format('YYYYMMDDHHmmss');
					} else {
						// TODO Error Msg.
					}

					//var sEvtAddr = $('#view-rqst-evtAddr').val();
					//var sEvtPointX = $('#view-rqst-evtPointX').val();
					//var sEvtPointY = $('#view-rqst-evtPointY').val();
					//var sRqstRsnTyCd = $('#view-rqst-rqstRsnTyCd').val();
					//var sRqstRsnDtl = $('#view-rqst-rqstRsnDtl').val();
					//var sEmrgYn = $('input[name="view-rqst-emrgYn"]:checked').val();
					//var sEmrgRsn = $('#view-rqst-emrgRsn').val();
					//var sPaperNo = $('#view-rqst-paperNo').val();
					
					var sViewEndYmdhmsWant = $('#view-rqst-viewEndYmdhmsWant').val();
					//var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD HH:mm:ss');
					var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD');
					if (momentViewEndYmdhmsWant.isValid()) {
						sViewEndYmdhmsWant = momentViewEndYmdhmsWant.format('YYYYMMDD') + '235959';
					} else {
						// TODO Error Msg.
					}

					let oParams = new FormData();
					oParams.append('evtNo', $('#view-rqst-evtNo').val());
					oParams.append('evtNm', $('#view-rqst-evtNm').val());
					//oParams.append('evtGrd', sEvtGrd);
					//oParams.append('evtTy', sEvtTy);
					oParams.append('evtYmdhms', sEvtYmdhms);
					oParams.append('evtAddr', $('#view-rqst-evtAddr').val());
					oParams.append('evtPointX', $('#view-rqst-evtPointX').val());
					oParams.append('evtPointY', $('#view-rqst-evtPointY').val());
					oParams.append('evtDstrtCd', $('#view-rqst-evtDstrtCd').val());
					oParams.append('rqstRsnTyCd', $('#view-rqst-rqstRsnTyCd').val());
					oParams.append('rqstRsnDtl', $('#view-rqst-rqstRsnDtl').val());
					oParams.append('emrgYn', $('input[name="view-rqst-emrgYn"]:checked').val());
					oParams.append('emrgRsn', $('#view-rqst-emrgRsn').val());
					oParams.append('paperNo', $('#view-rqst-paperNo').val());
					oParams.append('viewEndYmdhmsWant', sViewEndYmdhmsWant);
					
				//	let wsUrl = "ws://"+window.location.host+contextRoot;
				//	if ( window.location.protocol.indexOf('https') != -1 ) {
				//		wsUrl = "wss://"+window.location.host+contextRoot;
				//	}
				//	console.log('wsUrl = '+wsUrl);
				//	oParams.append('wsUrl', wsUrl);

					let oFiles = $('#view-rqst-paperFileNm')[0].files;
					if (oFiles.length) {
						$.each(oFiles, function (index, file) {
							oParams.append('file-' + index, file);
						});
					}

					return oParams;
				},
				// oViewFirstRqst.view.rqst.request.setEvtPoint()
				setEvtPoint: function (pointX, pointY, address) {
					var $evtPointX = $('#view-rqst-evtPointX');
					var $evtPointY = $('#view-rqst-evtPointY');
					if ($evtPointX.length && $evtPointY.length) {
						$evtPointX.val(pointX);
						$evtPointY.val(pointY);
						if (address) {
							$('#view-rqst-evtAddr').val(address);
						} else {
							$('#view-rqst-evtAddr').val('');
						}
					}
				}
			},
			// 요청 oViewFirstRqst.view.rqst.modify{}
			modify: {
				// oViewFirstRqst.view.rqst.modify.init()
				init: function () {
					let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
					var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					
					oDiv.setDiv('left', '1', {
						target: 'tvoBlank/view/div/viewRqstReg',					// 신규신청
						dstrtCd: sDstrtCd,
						viewRqstNo: sViewRqstNo

					}, function () {
						var sRqstRsnTyCd = $('#view-rqst-dtl-rqstRsnTyCd').val();
						codeInfoList('#view-rqst-rqstRsnTyCd', 'RQST_RSN_TY', sRqstRsnTyCd, '신청사유');

						var sEmrgYn = $('#view-rqst-dtl-emrgYn').val();
						//codeInfoList('#view-rqst-emrgYn', 'EMRG_YN', sEmrgYn, '긴급구분');
						if (sEmrgYn == "Y") {
							$("input:radio[name='view-rqst-emrgYn']:radio[value='Y']").prop('checked', true); // 선택하기
							$('#spEmrgRsn').removeClass('hide');
							
						} else if (sEmrgYn == "N") {
							$("input:radio[name='view-rqst-emrgYn']:radio[value='N']").prop('checked', true); // 선택하기
						}

						oTvoCmn.datetimepicker.ymdhm('.view-rqst-evtYmdhms', moment().add(-31, 'days'), moment());
						oTvoCmn.datetimepicker.ymd('.view-rqst-viewEndYmdhmsWant', moment(), moment().add(30, 'days'));
						$("#view-rqst-evtYmdhms").val($("#view-rqst-dtl-evtYmdhms").text());
						$("#view-rqst-viewEndYmdhmsWant").val($("#view-rqst-dtl-viewEndYmdhmsWant").text());
						

						$("input:checkbox[id='view-rqst-agree']").prop("checked", true);

						//oViewFirstRqst.cctv.grid.init();
					});
				},
				// oViewFirstRqst.view.rqst.modify.cancel()
				cancel: function () {
					oCommon.modalConfirm('modal-confirm', '알림', '열람신청수정을 취소하시겠습니까?', function () {
						oTvoCmn.div.clear('#view-rqst');
					});
				},
				// oViewFirstRqst.view.rqst.modify.removerPaperFile()
				removerPaperFile: function () {
					oCommon.modalConfirm('modal-confirm', '알림', '공문파일을 삭제하시겠습니까?', function () {
						var oParams = oViewFirstRqst.view.rqst.modify.params();
						$.ajax({
							type: 'POST',
							enctype: 'multipart/form-data',
							cache: false,
							contentType: false,
							processData: false,
							async: false,
							//dataType: 'json',
							url:contextRoot + '/tvo/rqst/viewRqst/deletePaperFile.json',
							data: oParams,
							success: function (data) {
								if (data.result == '1') {
									oTvoCmn.jqGrid.reload('rqst-view', 1, oViewFirstRqst.view.rqst.grid.params());
									oTvoCmn.div.clear('#view-rqst');
									oViewFirstRqst.view.rqst.detail.init(oParams.get("viewRqstNo"));
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
									setTimeout(function () {
										oViewFirstRqst.view.rqst.modify.init();
									}, 500);
								} else {
									if (data.errors.length) {
										var sMessage = '';
										var oErrors = data.errors;
										$.each(oErrors, function (i, v) {
											sMessage += v.defaultMessage + '\r\n';
											var sFieId = v.field;
											$('#view-rqst-' + sFieId).closest('td').addClass('has-error');

										});
										oCommon.modalAlert('modal-alert', '알림', sMessage);
									}
								}
							},
							error: function (data, status, err) {
								console.log(data);
							}
						});
					});
				},
				// oViewFirstRqst.view.rqst.modify.register()
				register: function () {
					if ($('#view-rqst-evtNo').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '사건번호를 입력하세요.');
						$('#view-rqst-evtNo').focus();
						return;
					}
					if ($('#view-rqst-evtNm').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '사건명을 입력하세요.');
						$('#view-rqst-evtNm').focus();
						return;
					}
					if ($('#view-rqst-evtYmdhms').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '발생일시를 입력하세요.');
						$('#view-rqst-evtYmdhms').focus();
						return;
					}
					if ($('#view-rqst-evtAddr').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '발생주소를 입력하세요.');
						$('#view-rqst-evtAddr').focus();
						return;
					}
					if ($('#view-rqst-rqstRsnTyCd').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '신청사유를 입력하세요.');
						$('#view-rqst-rqstRsnTyCd').focus();
						return;
					}
					if ($('#view-rqst-viewEndYmdhmsWant').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '열람종료일시를 입력하세요.');
						$('#view-rqst-viewEndYmdhmsWant').focus();
						return;
					}

					//if ( $('#view-rqst-emrgYn').val().trim() == '' )			{	oCommon.modalAlert('modal-alert', '알림', '긴급구분을 입력하세요.');		$('#view-rqst-emrgYn').focus();				return;	}
					var sEmrgYn = $('input[name="view-rqst-emrgYn"]:checked').val();
					if (!sEmrgYn) {
						oCommon.modalAlert('modal-alert', '알림', '긴급구분을 선택하세요.');
						return;

					} else if (sEmrgYn == "Y") {		// 긴급일 때
						if ( "" == $('#view-rqst-emrgRsn').val().trim() ) {
							oCommon.modalAlert('modal-alert', '알림', '긴급사유를 입력하세요.');
							return;
						}
					} else if (sEmrgYn == "N") {		// 일반일 때

						let dtlPaperNo = $('#view-rqst-dtl-paperNo').val();
						let dtlPaperFileNm = $('#view-rqst-dtl-paperFileNm').val();
						//alert(dtlPaperNo+" , "+dtlPaperFileNm);
						if ( dtlPaperNo == '' && dtlPaperFileNm == '' ) {
							let paperNo = $('#view-rqst-paperNo').val().trim();
							let oFiles = $('#view-rqst-paperFileNm')[0].files;
							if (paperNo == '' && !oFiles.length) {
								oCommon.modalAlert('modal-alert', '알림', '긴급구분이 일반일 경우 공문파일 또는 전자문서번호가 필요합니다.');
								return;
							} else if (paperNo != '' && oFiles.length) {
								oCommon.modalAlert('modal-alert', '알림', '공문파일 또는 전자문서번호 중 하나만 입력하세요.');
								return;
							}
							
							if (oFiles.length) {
								let sz = $('#view-rqst-paperFileNm')[0].files[0].size;	//console.log(sz);
								let mx = 1024 * 1024 * 5;								//console.log(mx);
								if (mx < sz) {											// 파일 사이즈 체크
									oCommon.modalAlert('modal-alert', '알림', '5 MB 이하의 파일만 첨부가능합니다.');
									return;
								}
	
								let nm = $('#view-rqst-paperFileNm').val();					//console.log(nm);
								let ex = nm.substring(nm.indexOf('.') + 1).toLowerCase();	//console.log(ex);
								let ty = "hwp, hwpx, doc, docx, pdf";						//console.log(ty.indexOf(ex));
								if (ty.indexOf(ex) == -1) {									// 파일 확장자 체크
									oCommon.modalAlert('modal-alert', '알림', ty + ' 확장자를 가진 파일만 첨부가능합니다.');
									return;
								}
							}
						}
						
					}

					if (!$('#view-rqst-agree').is(':checked')) {
						oCommon.modalAlert('modal-alert', '알림', '개인정보보호서약에 동의하세요.');
						$('#view-rqst-agree').focus();
						return;
					}

					oCommon.modalConfirm('modal-confirm', '알림', '열람신청을 수정하시겠습니까?', function () {
						var oParams = oViewFirstRqst.view.rqst.modify.params();
						$.ajax({
							type: 'POST',
							enctype: 'multipart/form-data',
							cache: false,
							contentType: false,
							processData: false,
							async: false,
							//dataType: 'json',
							url:contextRoot + '/tvo/rqst/viewRqst/updateViewRqst.json',
							data: oParams,
							success: function (data) {
								if (data.result == '1') {
									oTvoCmn.jqGrid.reload('rqst-view', 1, oViewFirstRqst.view.rqst.grid.params());
									oTvoCmn.div.clear('#view-rqst');
									oViewFirstRqst.view.rqst.detail.init(oParams.get("dstrtCd"), oParams.get("viewRqstNo"));
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
								} else {
									if (data.errors.length) {
										var sMessage = '';
										var oErrors = data.errors;
										$.each(oErrors, function (i, v) {
											sMessage += v.defaultMessage + '\r\n';
											var sFieId = v.field;
											$('#view-rqst-' + sFieId).closest('td').addClass('has-error');

										});
										oCommon.modalAlert('modal-alert', '알림', sMessage);
									}
								}
							},
							error: function (data, status, err) {
								console.log(data);
							}
						});
					});
				},
				// oViewFirstRqst.view.rqst.modify.params()
				params: function () {
				//	let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
				//	var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					var sViewRqstYmdhms = $('#view-rqst-dtl-viewRqstYmdhms').val();
					var sPaperFileNm = $('#view-rqst-dtl-paperFileNm').val();
				//	var sEvtNo = $('#view-rqst-evtNo').val();
				//	var sEvtNm = $('#view-rqst-evtNm').val();
					//var sEvtGrd = $('#view-rqst-evtGrd').val();
					//var sEvtTy = $('#view-rqst-evtTy').val();
					
					var sEvtYmdhms = $('#view-rqst-evtYmdhms').val();
					var momentEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss');
					if (momentEvtYmdhms.isValid()) {
						sEvtYmdhms = momentEvtYmdhms.format('YYYYMMDDHHmmss');
					} else {
						// TODO Error Msg.
					}

				//	var sEvtAddr = $('#view-rqst-evtAddr').val();
				//	var sEvtPointX = $('#view-rqst-evtPointX').val();
				//	var sEvtPointY = $('#view-rqst-evtPointY').val();
				//	var sRqstRsnTyCd = $('#view-rqst-rqstRsnTyCd').val();
				//	var sRqstRsnDtl = $('#view-rqst-rqstRsnDtl').val();
				//	var sEmrgYn = $('input[name="view-rqst-emrgYn"]:checked').val();
				//	var sEmrgRsn = $('#view-rqst-emrgRsn').val();
				//	var sPaperNo = $('#view-rqst-paperNo').val();
					
					var sViewEndYmdhmsWant = $('#view-rqst-viewEndYmdhmsWant').val();
					var momentViewEndYmdhmsWant = moment(sViewEndYmdhmsWant, 'YYYY-MM-DD HH:mm:ss');
					if (momentViewEndYmdhmsWant.isValid()) {
						sViewEndYmdhmsWant = momentViewEndYmdhmsWant.format('YYYYMMDDHHmmss');
					} else {
						// TODO Error Msg.
					}

					let oParams = new FormData();
					oParams.append('dstrtCd'       , $('#view-rqst-dtl-dstrtCd').val());
					oParams.append('viewRqstNo'    , $('#view-rqst-dtl-viewRqstNo').val());
					oParams.append('viewRqstYmdhms', sViewRqstYmdhms);
					oParams.append('paperFileNm'   , sPaperFileNm);
					oParams.append('evtNo'         , $('#view-rqst-evtNo').val());
					oParams.append('evtNm'         , $('#view-rqst-evtNm').val());
					//oParams.append('evtGrd', sEvtGrd);
					//oParams.append('evtTy', sEvtTy);
					oParams.append('evtYmdhms'  , sEvtYmdhms);
					oParams.append('evtAddr'    , $('#view-rqst-evtAddr').val());
					oParams.append('evtPointX'  , $('#view-rqst-evtPointX').val());
					oParams.append('evtPointY'  , $('#view-rqst-evtPointY').val());
					oParams.append('rqstRsnTyCd', $('#view-rqst-rqstRsnTyCd').val());
					oParams.append('rqstRsnDtl' , $('#view-rqst-rqstRsnDtl').val());
					oParams.append('emrgYn'     , $('input[name="view-rqst-emrgYn"]:checked').val());
					oParams.append('emrgRsn'    , $('#view-rqst-emrgRsn').val());
					oParams.append('paperNo'    , $('#view-rqst-paperNo').val());
					oParams.append('viewEndYmdhmsWant', sViewEndYmdhmsWant);

					let oFiles = $('#view-rqst-paperFileNm')[0].files;
					if (oFiles.length) {
						$.each(oFiles, function (index, file) {
							oParams.append('file-' + index, file);
						});
					}

					return oParams;
				}
			},
			// oViewFirstRqst.view.rqst.cancel()
			cancel: function () {
				oCommon.modalConfirm('modal-confirm', '알림', '열람신청을 취소하시겠습니까?<br/><br/>신청기록은 삭제됩니다.', function () {
					//let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
					//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					
					$.ajax({
						type: 'POST',
						async: false,
						dataType: 'json',
						url:contextRoot + '/tvo/view/removeViewRqst.json',
						data: {
							dstrtCd: $('#view-rqst-dtl-dstrtCd').val(),
							viewRqstNo: $('#view-rqst-dtl-viewRqstNo').val()
						},
						success: function (data) {
							console.log(data);
							if (data.result == '1') {
								oTvoCmn.jqGrid.reload('rqst-view', 1, oViewFirstRqst.view.rqst.grid.params());
								oTvoCmn.div.clear('#view-rqst');
								oTvoCmn.div.clear('#view-rqst-dtl');
								oViewFirstRqst.view.rqst.grid.search();			// 목록 새로고침
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
							}
						},
						error: function (data, status, err) {
							console.log(data);
						}
					});
				});
			},
			// oViewFirstRqst.view.rqst.setAddress()
			setAddress: function (type) {
				let oData = {};
				if (type == 'context') {
					oData = $('#table-contextmenu').data();
				} else if (type == 'search') {
					oData = $('#table-map-menu-search').data();
				}
				let sAddr = '';
				if (!$.isEmptyObject(oData)) {
					if (oData.hasOwnProperty('road')) sAddr += oData.road;
					if (oData.hasOwnProperty('poi')) sAddr += $.trim(oData.poi) == '' ? '' : ' ' + oData.poi;
					if (oData.hasOwnProperty('jibun')) sAddr += '(' + oData.jibun + ')';


					$('#view-rqst-evtAddr').val(sAddr);
					$('#view-rqst-evtPointX').val(parseFloat(oData.pointX).toFixed(6));
					$('#view-rqst-evtPointY').val(parseFloat(oData.pointY).toFixed(6));
					$('#view-rqst-evtDstrtCd').val(oData.dstrtCd);
				}
			}
		},
		// 공문첨부 oViewFirstRqst.view.paperFile{}
		paperFile: {
			id: 'modal-paper-file',
			element: null,
			// oViewFirstRqst.view.paperFile.open()
			open: function (pDstrtCd, pViewRqstNo, pViewRqstYmdhms) {
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
						target: 'tvoBlank/view/div/paperFileUpload'			// 공문첨부
					}, function () {
						$('#view-rqst-paper-dstrtCd').val(pDstrtCd);
						$('#view-rqst-paper-viewRqstNo').val(pViewRqstNo);
						$('#view-rqst-paper-viewRqstYmdhms').val(pViewRqstYmdhms);
						$('#view-rqst-paper-paperNo').val($('#view-rqst-dtl-paperNo').val());
					});
				} else {
					$(this.element).modal('show');
					$('#view-rqst-paper-dstrtCd').val(pDstrtCd);
					$('#view-rqst-paper-viewRqstNo').val(pViewRqstNo);
					$('#view-rqst-paper-viewRqstYmdhms').val(pViewRqstYmdhms);
					$('#view-rqst-paper-paperNo').val($('#view-rqst-dtl-paperNo').val());
				}
			},
			close: function () {
				$('#' + oViewFirstRqst.view.paperFile.id).modal('hide');
			},
			// oViewFirstRqst.view.paperFile.init()
			/*	init: function() {
					oDiv.setDiv('left', '1', {
						target: 'tvoBlank/rqst/div/viewPaperFile'			// 공문첨부
					}, function() {

					});
				},
				cancel: function() {
					oCommon.modalConfirm('modal-confirm', '알림', '공문첨부를 취소하시겠습니까?', function() {
						oTvoCmn.div.clear('#view-paper-file');
					});
				},	*/
			// oViewFirstRqst.view.paperFile.register()
			register: function () {
				let paperNo = $('#view-rqst-paper-paperNo').val().trim();
				let oFiles = $('#view-rqst-paper-paperFileNm')[0].files;
				if (paperNo == '' && !oFiles.length) {
					oCommon.modalAlert('modal-alert', '알림', '공문파일 또는 전자문서번호가 필요합니다.');
					return;
				}

				if (oFiles.length) {
					// 파일 사이즈 체크
					let sz = $('#view-rqst-paper-paperFileNm')[0].files[0].size;	//console.log(sz);
					let mx = 1024 * 1024 * 5;								//console.log(mx);
					if (mx < sz) {
						oCommon.modalAlert('modal-alert', '알림', '5 MB 이하의 파일만 첨부가능합니다.');
						return;
					}

					// 파일 확장자 체크
					let nm = $('#view-rqst-paper-paperFileNm').val();					//console.log(nm);
					let ex = nm.substring(nm.indexOf('.') + 1).toLowerCase();	//console.log(ex);
					let ty = "hwp, hwpx, doc, docx, pdf";						//console.log(ty.indexOf(ex));
					if (ty.indexOf(ex) == -1) {
						oCommon.modalAlert('modal-alert', '알림', ty + ' 확장자를 가진 파일만 첨부가능합니다.');
						return;
					}
				}

				//oCommon.modalConfirm('modal-confirm', '알림', '공문을 첨부하시겠습니까?', function() {
				//});
				var oParams = oViewFirstRqst.view.paperFile.params();
				$.ajax({
					type: 'POST',
					enctype: 'multipart/form-data',
					cache: false,
					contentType: false,
					processData: false,
					async: false,
					//dataType: 'json',
					url:contextRoot + '/tvo/rqst/viewRqst/insertPaperFile.json',
					data: oParams,
					success: function (data) {
						if (data.result == '1' || data.result == '2') {
							oCommon.modalAlert('modal-alert', '알림', '성공하였습니다.');
						} else {
							oCommon.modalAlert('modal-alert', '알림', '실패하였습니다.');
							return false;
						}
						oTvoCmn.div.clear('#view-paper-file');

						if ($('#view-rqst-dtl-viewRqstNo').val()) {
							oViewFirstRqst.view.rqst.detail.init(oParams.get("viewRqstNo"));
						} else {
							oViewFirstRqst.view.rqst.detail.initRqstVdo(oParams.get("viewRqstNo"), oParams.get("tvoPrgrsCd"));
						}

						oViewFirstRqst.view.rqst.grid.search();			// 목록 새로고침

					},
					error: function (data, status, err) {
						console.log(data);
					}
				});
				$('#' + oViewFirstRqst.view.paperFile.id).modal('hide');
				$('#modal-paper-file').remove();
			},
			params: function () {
				let sDstrtCd    = $('#view-rqst-paper-dstrtCd').val();
				//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				var sViewRqstNo = $('#view-rqst-paper-viewRqstNo').val();
				var sViewRqstYmdhms = $('#view-rqst-paper-viewRqstYmdhms').val();
				var sPaperNo = $('#view-rqst-paper-paperNo').val();
				//var sTvoPrgrsCd = $('#view-rqst-dtl-tvoPrgrsCd').val();
				//var sPaperFileNm = $('#view-rqst-dtl-paperFileNm').val();	// 이미 등록되어 있는 파일명

				let oParams = new FormData();
				oParams.append('dstrtCd', sDstrtCd);
				oParams.append('viewRqstNo', sViewRqstNo);
				oParams.append('viewRqstYmdhms', sViewRqstYmdhms);
				oParams.append('paperNo', sPaperNo);
				//oParams.append('tvoPrgrsCd', sTvoPrgrsCd);
				//oParams.append('paperFileNm', sPaperFileNm);

				let oFiles = $('#view-rqst-paper-paperFileNm')[0].files;
				if (oFiles.length) {
					$.each(oFiles, function (index, file) {
						oParams.append('file-' + index, file);
					});
				}

				return oParams;
			}
		},
		// 기간연장 oViewFirstRqst.view.prodExtn{}
		prodExtn: {
			id: 'modal-view-prod-extn',
			element: null,
			// oViewFirstRqst.view.prodExtn.open()
			open: function () {
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
							target: 'tvoBlank/view/div/viewExtnList',		// 열람기간연장이력
						}, function () {
							setTimeout(function () {
								oViewFirstRqst.view.prodExtn.grid.init();
							}, 500);
						},
						1000, 500);
				} else {
					$(this.element).modal('show');
					setTimeout(function () {
						oViewFirstRqst.view.prodExtn.grid.init();
					}, 500);
				}
			},

			grid: {
				id: 'view-prod-extn-his',
				// oViewFirstRqst.view.prodExtn.grid.init()
				init: function () {
					console.log("=== oViewFirstRqst.view.prodExtn.grid.init()");
					var sId = oViewFirstRqst.view.prodExtn.grid.id;
					var oGridParams = oViewFirstRqst.view.prodExtn.grid.params();
					var sGridId = '#grid-' + oViewFirstRqst.view.prodExtn.grid.id;
					console.log("=== oViewFirstRqst.view.prodExtn.grid.init(), {}", sGridId);
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/rqst/viewRqst/selectViewProdExtnHisList.json',
						datatype: 'json',
						mtype: 'POST',
						height: 'auto',
						rowNum: 5,
						autowidth: true,
						shrinkToFit: true,
						postData: oGridParams,
						beforeRequest: function () {
						},
						loadComplete: function (data) {
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames: [
							'#', 'viewRqstNo', '신청일시', '신청된 종료일자', '승인된 종료일자', '사유', '승인'
						],
						colModel: [
							{
								name:'rnum',
								align:'center',
								cellattr: function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:20px;"';
								},
								width: 20
							}, {
								name:'viewRqstNo',
								hidden: true
							}, {
								name:'viewExtnRqstYmdhms',
								classes: 'text-ellipsis',
								formatter: function (cellvalue, options, rowObject) {
									var sViewExtnRqstYmdhms = rowObject.viewExtnRqstYmdhms;
									var momentExtnRqstYmdhms = moment(sViewExtnRqstYmdhms, 'YYYYMMDDHHmmss');
									var $span = $('<span/>', {
										'title': momentExtnRqstYmdhms.format('YYYY-MM-DD HH:mm:ss'),
										'text': momentExtnRqstYmdhms.format('YY-MM-DD HH:mm:ss')
									});
									return $span.prop('outerHTML');
								},
								align:'center'
							}, {
								name:'rqstViewEndYmdhms',
								classes: 'text-ellipsis',
								formatter: function (cellvalue, options, rowObject) {
									var sRqstViewEndYmdhms = rowObject.rqstViewEndYmdhms;
									var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
									var $span = $('<span/>', {
										'title': momentRqstViewEndYmdhms.format('YYYY-MM-DD HH:mm:ss'),
										'text': momentRqstViewEndYmdhms.format('YY-MM-DD')
									});
									return $span.prop('outerHTML');
								},
								align:'center'
							}, {
								name:'aprvViewEndYmdhms',
								classes: 'text-ellipsis',
								formatter: function (cellvalue, options, rowObject) {
									var sAprvViewEndYmdhms = rowObject.aprvViewEndYmdhms;
									var momentAprvViewEndYmdhms = moment(sAprvViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
									if (momentAprvViewEndYmdhms.isValid()) {
										var $span = $('<span/>', {
											'title': momentAprvViewEndYmdhms.format('YYYY-MM-DD HH:mm:ss'),
											'text': momentAprvViewEndYmdhms.format('YY-MM-DD')
										});
										return $span.prop('outerHTML');
									} else {
										return '';
									}
								},
								align:'center'
							}, {
								name:'viewExtnRqstRsn',
								classes: 'text-ellipsis',
							}, {
								name:'tvoPrgrsNm',
								align:'center',
								cellattr: function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:60px;"';
								},
								width: 60
							}
						],
						jsonReader: {
							root: "rows",
							total: "totalPages",
							records: "totalRows"
						},
						onSelectRow: function (rowId) {

						},
						cmTemplate: {
							sortable: false,
							resizable: false
						}
					});
				},
				params: function () {
					//let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
					//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					//var sViewRqstUserId = $('#view-rqst-dtl-viewRqstUserId').val();
					var oParams = {
						dstrtCd    : $('#view-rqst-dtl-dstrtCd').val(),
						viewRqstNo : $('#view-rqst-dtl-viewRqstNo').val()
						//, viewExtnRqstUserId: sViewRqstUserId
					};
					return oParams;
				}
			},
			rqst: {
				id: 'modal-prod-extn-rqst',
				element: null,
				// oViewFirstRqst.view.prodExtn.rqst.open()
				open: function () {
					this.element = document.getElementById(this.id);
					if (this.element == null) {
						oDiv.openDiv(this.id, {
							target: 'tvoBlank/view/div/viewExtnReg'		// 기간연장신청
						}, function () {
							var sViewEndYmdhms = $('#view-rqst-dtl-viewEndYmdhms').text().trim();
							if ('' != sViewEndYmdhms) {
								//var momentMinDate = moment(sViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
								var momentMinDate = moment(sViewEndYmdhms, 'YYYY-MM-DD');
								if (momentMinDate.isValid()) {
									var momentMaxDate = momentMinDate.clone().add(30, 'days');
									oTvoCmn.datetimepicker.ymd('.view-prod-extn-rqst-rqstViewEndYmdhms', momentMinDate, momentMaxDate);
									$('#view-prod-extn-rqst-rqstViewEndYmdhms').val(sViewEndYmdhms);
								}
							}
						});
					} else {
						$(this.element).modal('show');
						var sViewEndYmdhms = $('#view-rqst-dtl-viewEndYmdhms').text().trim();
						if ('' != sViewEndYmdhms) {
							//var momentMinDate = moment(sViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
							var momentMinDate = moment(sViewEndYmdhms, 'YYYY-MM-DD');
							if (momentMinDate.isValid()) {
								var momentMaxDate = momentMinDate.clone().add(30, 'days');
								oTvoCmn.datetimepicker.ymd('.view-prod-extn-rqst-rqstViewEndYmdhms', momentMinDate, momentMaxDate);
								$('#view-prod-extn-rqst-rqstViewEndYmdhms').val(sViewEndYmdhms);
							}
						}
					}
				},
				// oViewFirstRqst.view.prodExtn.rqst.init()
				/*	init: function() {
						var sViewEndYmdhms = $('#view-rqst-dtl-viewEndYmdhms').text().trim();
						if ('' != sViewEndYmdhms) {
							var momentMinDate = moment(sViewEndYmdhms, 'YYYYMMDDHHmmss');
							if (momentMinDate.isValid()) {
								oDiv.setDiv('right', '2', {
									//oDiv.setDiv('left', '0',
									target: 'tvoBlank/rqst/div/viewProdExtnRqst'		// 기간연장신청
								}, function() {
									var momentMaxDate = momentMinDate.clone().add(30, 'days');
									oTvoCmn.datetimepicker.ymdhm('.view-prod-extn-rqst-rqstViewEndYmdhms', momentMinDate, momentMaxDate);
									$('#view-prod-extn-rqst-rqstViewEndYmdhms').val(sViewEndYmdhms);
								});
							}
						}
					},	*/
				cancel: function () {
					oCommon.modalConfirm('modal-confirm', '알림', '열람기간연장 신청을 취소하시겠습니까?', function () {
						oTvoCmn.div.clear('#view-prod-extn-rqst');
					});
				},
				close: function () {
					$('#' + oViewFirstRqst.view.prodExtn.rqst.id).modal('hide');
				},
				// oViewFirstRqst.view.prodExtn.rqst.request()
				request: function () {
					if ($('#view-rqst-dtl-viewEndYmdhms').text().trim() == $('#view-prod-extn-rqst-rqstViewEndYmdhms').val()) {
						oCommon.modalAlert('modal-alert', '알림', '종료일자를 재지정하세요.');
						return;
					}
					if ($('#view-prod-extn-rqst-viewExtnRqstRsn').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '연장신청사유를 입력하세요.');
						$('#view-prod-extn-rqst-viewExtnRqstRsn').focus();
						return;
					}
					
					//let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
					//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					//var sViewExtnRqstRsn = $('#view-prod-extn-rqst-viewExtnRqstRsn').val();
					
					var sRqstViewEndYmdhms = $('#view-prod-extn-rqst-rqstViewEndYmdhms').val();
					//var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
					var momentRqstViewEndYmdhms = moment(sRqstViewEndYmdhms, 'YYYY-MM-DD');
					if (momentRqstViewEndYmdhms.isValid()) {
						//sRqstViewEndYmdhms = momentRqstViewEndYmdhms.format('YYYYMMDDHHmmss');
						sRqstViewEndYmdhms = momentRqstViewEndYmdhms.format('YYYYMMDD') + '235959';
					}

				//	let sWsUrl = "ws://"+window.location.host+contextRoot;
				//	if ( window.location.protocol.indexOf('https') != -1 ) {
				//		sWsUrl = "wss://"+window.location.host+contextRoot;
				//	}
				//	console.log('wsUrl = '+sWsUrl);

					var oParams = {
						dstrtCd   : $('#view-rqst-dtl-dstrtCd').val(),
						viewRqstNo: $('#view-rqst-dtl-viewRqstNo').val(),
						rqstViewEndYmdhms: sRqstViewEndYmdhms,
						viewExtnRqstRsn: $('#view-prod-extn-rqst-viewExtnRqstRsn').val(),
				//		wsUrl: sWsUrl
					};
					//console.log(oParams);

					oCommon.modalConfirm('modal-confirm', '알림', '열람기간연장을 신청하시겠습니까?', function () {
						$.ajax({
							type: 'POST',
							async: false,
							dataType: 'json',
							url:contextRoot + '/tvo/rqst/viewRqst/insertViewProdExtn.json',
							data: oParams,
							success: function (data) {
								if (data.result == '1') {
									oTvoCmn.jqGrid.reload('view-prod-extn-his', 1, oViewFirstRqst.view.prodExtn.grid.params());
									oTvoCmn.div.clear('#view-prod-extn-rqst');
									oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
								} else {
									oCommon.modalAlert('modal-alert', '알림', '실패하였습니다.');
								}
							},
							error: function (data, status, err) {
								console.log(data);
							}
						});
						$('#' + oViewFirstRqst.view.prodExtn.rqst.id).modal('hide');
					});
				}
			}
		},
		// 활용결과 oViewFirstRqst.view.useRslt{}
		useRslt: {
			id: 'modal-use-rslt',
			element: null,
			// oViewFirstRqst.view.useRslt.open()
			open: function () {
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
						target: 'tvoBlank/view/div/useResultReg'			// 결과등록
					}, function () {
						var sViewResultTyCd = $('#view-rqst-dtl-viewResultTyCd').val();
						codeInfoList('#view-use-rslt-viewResultTyCd', 'VIEW_RESULT_TY', sViewResultTyCd, '활용결과');

						//var sViewResultTy = $('#view-rqst-dtl-viewResultTy').val();
						var sViewResultTyDtl = $('#view-rqst-dtl-viewResultTyDtl').val();

						//if (sViewResultTy != '') {
						//	if (sViewResultTy == 'Y') {
						//		$('#view-use-rslt-viewResultTy-Y').prop('checked', true);
						//	} else if (sViewResultTy == 'N') {
						//		$('#view-use-rslt-viewResultTy-N').prop('checked', true);
						//	}
						//}
						if (sViewResultTyDtl != '') {
							$('#view-use-rslt-viewResultTyDtl').val(sViewResultTyDtl);
						}
					});
				} else {
					$(this.element).modal('show');
					var sViewResultTyCd = $('#view-rqst-dtl-viewResultTyCd').val();
					codeInfoList('#view-use-rslt-viewResultTyCd', 'VIEW_RESULT_TY', sViewResultTyCd, '활용결과');

					//var sViewResultTy = $('#view-rqst-dtl-viewResultTy').val();
					var sViewResultTyDtl = $('#view-rqst-dtl-viewResultTyDtl').val();

					//if (sViewResultTy != '') {
					//	if (sViewResultTy == 'Y') {
					//		$('#view-use-rslt-viewResultTy-Y').prop('checked', true);
					//	} else if (sViewResultTy == 'N') {
					//		$('#view-use-rslt-viewResultTy-N').prop('checked', true);
					//	}
					//}
					if (sViewResultTyDtl != '') {
						$('#view-use-rslt-viewResultTyDtl').val(sViewResultTyDtl);
					}
				}
			},
			// oViewFirstRqst.view.useRslt.init()
			/*	init: function() {
					oDiv.setDiv('right', '2', {
						target: 'tvoBlank/rqst/div/viewUseRslt'			// 결과등록
					}, function() {
						var sViewResultTyCd = $('#view-rqst-dtl-viewResultTyCd').val();
						codeInfoList('#view-use-rslt-viewResultTyCd', 'VIEW_RESULT_TY', sViewResultTyCd, '활용결과');

						var sViewResultTy = $('#view-rqst-dtl-viewResultTy').val();
						var sViewResultTyDtl = $('#view-rqst-dtl-viewResultTyDtl').val();

						if (sViewResultTy != '') {
							if (sViewResultTy == 'Y') {
								$('#view-use-rslt-viewResultTy-Y').prop('checked', true);
							} else if (sViewResultTy == 'N') {
								$('#view-use-rslt-viewResultTy-N').prop('checked', true);
							}
						}

						if (sViewResultTyDtl != '') {
							$('#view-use-rslt-viewResultTyDtl').val(sViewResultTyDtl);
						}
					});
				},	*/
			cancel: function () {
				oCommon.modalConfirm('modal-confirm', '알림', '열람활용결과 등록을 취소하시겠습니까?', function () {
					oTvoCmn.div.clear('#view-use-rslt');
				});
			},
			close: function () {
				$('#' + oViewFirstRqst.view.useRslt.id).modal('hide');
			},
			// oViewFirstRqst.view.useRslt.register()
			register: function () {
				oCommon.modalConfirm('modal-confirm', '알림', '열람활용결과를 등록하시겠습니까?', function () {
					var oParams = oViewFirstRqst.view.useRslt.params();
					$.ajax({
						type: 'POST',
						async: false,
						dataType: 'json',
						url:contextRoot + '/tvo/rqst/viewRqst/registerViewResult.json',
						data: oParams,
						success: function (data) {
							if (data.result == '1' || data.result == '2') {
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
							} else {
								oCommon.modalAlert('modal-alert', '알림', '실패하였습니다.');
								return false;
							}
							oTvoCmn.div.clear('#view-use-rslt');
							oViewFirstRqst.view.rqst.detail.init(oParams.dstrtCd, oParams.viewRqstNo);
							oViewFirstRqst.view.rqst.grid.search();			// 목록 새로고침
						},
						error: function (data, status, err) {
							console.log(data);
						}
					});
					$('#' + oViewFirstRqst.view.useRslt.id).modal('hide');
				});
			},
			params: function () {
				//let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
				//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				//var sTvoPrgrsCd = $('#view-rqst-dtl-tvoPrgrsCd').val();
				//var sViewResultTyCd = $('#view-use-rslt-viewResultTyCd').val();
				//var sViewResultTyDtl = $('#view-use-rslt-viewResultTyDtl').val();

				//var sViewResultTy = $('input[name="view-use-rslt-viewResultTy"]:checked').val();
				//if (typeof sViewResultTy == 'undefined') {
				//	sViewResultTy = "Y";
				//}

				var oParams = {
					dstrtCd   : $('#view-rqst-dtl-dstrtCd').val(),
					viewRqstNo: $('#view-rqst-dtl-viewRqstNo').val(),
					tvoPrgrsCd: $('#view-rqst-dtl-tvoPrgrsCd').val(),
					//viewResultTy: sViewResultTy,
					viewResultTyCd: $('#view-use-rslt-viewResultTyCd').val(),
					viewResultTyDtl: $('#view-use-rslt-viewResultTyDtl').val()
				};

				return oParams;
			}
		},

	};

	// 반출 oViewFirstRqst.out{}
	this.out = {
		// 신청 oViewFirstRqst.out.rqst
		rqst: {
			id: 'modal-view-out-rqst',
			element: null,
			// oViewFirstRqst.out.rqst.open()
			open: function () {
				this.element = document.getElementById(this.id);
				if (this.element == null) {
					oDiv.openDiv(this.id, {
							target: 'tvoBlank/out/div/outRqstList'					// 반출신청목록
						}, function () {
							setTimeout(function () {
								oViewFirstRqst.out.rqst.grid.init();
							}, 500);
						},
						1000, 500);
				} else {
					$(this.element).modal('show');
					setTimeout(function () {
						oViewFirstRqst.out.rqst.grid.init();
					}, 500);
				}
			},

			grid: {
				id: 'out-rqst',
				// oViewFirstRqst.out.rqst.grid.init()
				init: function () {
					var sId = oViewFirstRqst.out.rqst.grid.id;
					var oGridParams = oViewFirstRqst.out.rqst.grid.params();
					var sGridId = '#grid-' + oViewFirstRqst.out.rqst.grid.id;
					$(sGridId).jqGrid({
						url:contextRoot + '/tvo/out/selectOutRqstList.json',
						datatype: 'json',
						mtype: 'POST',
						height: 'auto',
						rowNum: 5,
						autowidth: true,
						shrinkToFit: true,
						postData: oGridParams,
						beforeRequest: function () {
						},
						loadComplete: function (data) {
							oTvoCmn.jqGrid.loadComplete(sId, data, oGridParams);
						},
						colNames: [		//'#',
							'viewRqstNo', 'pointX', 'pointY', '신청일자', '카메라명', '영상구간', '마스킹', '제3자', '재생종료일자', '승인'
						],
						colModel: [
							{	/*	name:'rnum',
								align:'center',
								cellattr : function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:20px;"';
								},
								width:20
							}, {	*/
								name:'viewRqstNo', hidden: true
							}, {
								name:'pointX', hidden: true
							}, {
								name:'pointY', hidden: true
							}, {
								name:'outRqstYmdhms', align:'center', classes: 'text-ellipsis', width: 70
								, cellattr: function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:70px;"';
								}
								, formatter: function (cellvalue, options, rowObject) {
									var sOutRqstYmdhms = rowObject.outRqstYmdhms;
									var momentOutRqstYmdhms = moment(sOutRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
									//var sYmdhms = momentOutRqstYmdhms.format('YY-MM-DD');
									var $span = $('<span/>', {
										'title': momentOutRqstYmdhms.format('YYYY-MM-DD HH:mm:ss'),
										'text': momentOutRqstYmdhms.format('YY-MM-DD')
									});
									return $span.prop('outerHTML');
								}
							}, {
								name:'fcltLblNm', classes: 'text-ellipsis'
								, formatter: function (cellvalue, options, rowObject) {
									var sFcltUsedTyNm = rowObject.fcltUsedTyNm;
									var sFcltLblNm = rowObject.fcltLblNm;
									var sPortalAdres = rowObject.portalAdres;
									//return '[' + rowObject.fcltUsedTyNm + ']' + rowObject.fcltLblNm;
									//return rowObject.fcltLblNm + ' / ' + sPortalAdres;
									return rowObject.fcltLblNm;
								}
							}, {
								name:'vdoYmdhms', align:'center', classes: 'text-ellipsis', width: 170
								, cellattr: function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:170px;"';
								}
								, formatter: function (cellvalue, options, rowObject) {
									var sVdoYmdhmsFr = rowObject.vdoYmdhmsFr;
									var momentVdoYmdhmsFr = moment(sVdoYmdhmsFr, 'YYYY-MM-DD HH:mm:ss');
									//var sFrom = momentVdoYmdhmsFr.format('YY-MM-DD HH:mm');

									var sVdoYmdhmsTo = rowObject.vdoYmdhmsTo;
									var momentVdoYmdhmsTo = moment(sVdoYmdhmsTo, 'YYYY-MM-DD HH:mm:ss');
									//var sTo = momentVdoYmdhmsTo.format('YY-MM-DD HH:mm');

									var $span = $('<span/>', {
										'title': momentVdoYmdhmsFr.format('YYYY-MM-DD HH:mm:ss') + ' ~ ' + momentVdoYmdhmsTo.format('YYYY-MM-DD HH:mm:ss'),
										'text': momentVdoYmdhmsFr.format('YY-MM-DD HH:mm') + ' ~ ' + momentVdoYmdhmsTo.format('YY-MM-DD HH:mm')
									});
									//$span.append(sFrom);
									//$span.append('<br>');
									//$span.append(' ~ ');
									//$span.append(sTo);
									return $span.prop('outerHTML');
								}
							}, {
								name:'maskingYn', align:'center', width: 40
								, cellattr: function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:40px;"';
								}
							}, {
								name:'thirdPartyYn', align:'center', width:40
								, cellattr: function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:40px;"';
								}
							}, {
								name:'playEndYmdhms', align:'center', classes: 'text-ellipsis', width:70
								, cellattr: function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:70px;"';
								}
								, formatter: function (cellvalue, options, rowObject) {
									var sPlayEndYmdhms = rowObject.playEndYmdhms;
									var momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
									//var sYmdhms = momentPlayEndYmdhms.format('YY-MM-DD');
									var $span = $('<span/>', {
										'title': momentPlayEndYmdhms.format('YYYY-MM-DD HH:mm:ss'),
										'text': momentPlayEndYmdhms.format('YY-MM-DD')
									});
									return $span.prop('outerHTML');
								}
							}, {
								name:'tvoPrgrsNm', align:'center', width:70
								, cellattr: function (rowId, val, rawObject, cm, rdata) {
									return 'style="width:70px;"';
								}
							}
						],
						jsonReader: {
							root: "rows",
							total: "totalPages",
							records: "totalRows"
						},
						onSelectRow: function (rowId) {
							var oRowData = $('#grid-out-rqst').getRowData(rowId);
							var oPoint = convertByWGS84(oRowData.pointX, oRowData.pointY);
							oTvoMap.features.remove.previousSelected();
							oTvoMap.features.previousSelected = oTvoMap.features.add.previousSelected(oPoint, '', '', false, true, true);
						},
						cmTemplate: {
							sortable: false,
							resizable: false
						}
					});
				},
				// oViewFirstRqst.out.rqst.grid.params()
				params: function () {
					//let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
					//var sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
					var oParams = {
						dstrtCd   : $('#view-rqst-dtl-dstrtCd').val(),
						viewRqstNo: $('#view-rqst-dtl-viewRqstNo').val()
					};
					return oParams;
				}
			},

			// 등록 oViewFirstRqst.out.rqst.changeThirdPartyYn()
			changeThirdPartyYn: function () {
				if ($('#out-rqst-cctv-thirdPartyYn').is(':checked')) {
					$('#out-rqst-cctv-playEndYmdhms').addClass('hide');
					$('#out-rqst-cctv-playEndYmdhmsThird').removeClass('hide');
				} else {
					$('#out-rqst-cctv-playEndYmdhms').removeClass('hide');
					$('#out-rqst-cctv-playEndYmdhmsThird').addClass('hide');
				}
			},
			
			// 등록 oViewFirstRqst.out.rqst.register()
			register: function () {

				var sEmrgYn = $('#view-rqst-dtl-emrgYn').val();				//alert(sEmrgYn);

				let paperNo = $('#view-rqst-dtl-paperNo').val();
				let paperFileNm = $('#view-rqst-dtl-paperFileNm').val();
				if (paperNo == '' && paperFileNm == '') {
					oCommon.modalAlert('modal-alert', '알림', '공문파일 또는 전자문서번호가 필요합니다.');
					return;
				}

				if ($('#out-rqst-cctv-thirdPartyYn').is(':checked')) {
					if ($('#out-rqst-cctv-thirdPartyPw').val().trim() == '') {
						oCommon.modalAlert('modal-alert', '알림', '제3자재생암호를 지정하세요.');
						$('#out-rqst-cctv-thirdPartyPw').focus();
						return;
					}
				} else {
					$('#out-rqst-cctv-thirdPartyPw').val('');
				}

				var $checkbox = $('#out-rqst-cctv tbody input[type="checkbox"]');
				if (!$checkbox.length) {
					oCommon.modalAlert('modal-alert', '알림', '반출신청대상목록에 등록된 카메라가 없습니다.');
					return;
				}

				let isLongVdo = false;
				var oCctvList = '';
				$.each($checkbox, function (i, v) {
					let vdoInfo = $(v).val();
					
					if ( ! isLongVdo ) {
						isLongVdo = oViewFirstRqst.out.rqst.isLongVdo(vdoInfo);		// 영상의 길이가 권장보다 긴 지 확인
					}
					
					if (i == 0) {
						oCctvList += vdoInfo;
					} else {
						oCctvList += ',' + vdoInfo;
					}
				});
				
				if (oCctvList.indexOf("::") != -1) {
					oCommon.modalAlert('modal-alert', '알림', '영상시간을 지정하세요.');
					return;
				}

				let sDstrtCd    = $('#view-rqst-dtl-dstrtCd').val();
				let sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
				var sTvoPrgrsCd = $('#view-rqst-dtl-tvoPrgrsCd').val();
				/*
				var sVdoYmdhmsFr = $('#out-rqst-cctv-vdoYmdhmsFr').val();
				var momentVdoYmdhmsFr = moment(sVdoYmdhmsFr, 'YYYY-MM-DD HH:mm:ss');
				if (momentVdoYmdhmsFr.isValid()) {
					sVdoYmdhmsFr = momentVdoYmdhmsFr.format('YYYYMMDDHHmmss');
				} else {
					// TODO Error Msg.
				}

				var sVdoYmdhmsTo = $('#out-rqst-cctv-vdoYmdhmsTo').val();
				var momentVdoYmdhmsTo = moment(sVdoYmdhmsTo, 'YYYY-MM-DD HH:mm:ss');
				if (momentVdoYmdhmsTo.isValid()) {
					sVdoYmdhmsTo = momentVdoYmdhmsTo.format('YYYYMMDDHHmmss');
				} else {
					// TODO Error Msg.
				}
				*/
				var sPlayStartYmdhms = $('#out-rqst-cctv-playStartYmdhms').text();	// 재생시작일
				var momentPlayStartYmdhms = moment(sPlayStartYmdhms, 'YYYY-MM-DD');
				if (momentPlayStartYmdhms.isValid()) {
					sPlayStartYmdhms = momentPlayStartYmdhms.format('YYYYMMDD') + '000000';
				} else {
					// TODO Error Msg.
				}

				var sPlayEndYmdhms = $('#out-rqst-cctv-playEndYmdhms').text();		// 재생종료일
				if ($('#out-rqst-cctv-thirdPartyYn').is(':checked')) {
					sPlayEndYmdhms = $('#out-rqst-cctv-playEndYmdhmsThird').text();	// 3자재생종료일
				}
				var momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD');
				if (momentPlayEndYmdhms.isValid()) {
					sPlayEndYmdhms = momentPlayEndYmdhms.format('YYYYMMDD') + '235959';
				} else {
					// TODO Error Msg.
				}
				
				var sMaskingYn = $('#out-rqst-cctv-maskingYn').is(':checked') ? 'Y' : 'N';
				var sThirdPartyYn = $('#out-rqst-cctv-thirdPartyYn').is(':checked') ? 'Y' : 'N';
				var sThirdPartyPw = $('#out-rqst-cctv-thirdPartyPw').val().trim();

			//	let sWsUrl = "ws://"+window.location.host+contextRoot;
			//	if ( window.location.protocol.indexOf('https') != -1 ) {
			//		sWsUrl = "wss://"+window.location.host+contextRoot;
			//	}
			//	console.log('wsUrl = '+sWsUrl);

				// TODO 유효성 체크
				var oParams = {
					dstrtCd: sDstrtCd,
					viewRqstNo: sViewRqstNo,
					emrgYn: sEmrgYn,
					cctvList: oCctvList,
					//vdoYmdhmsFr: sVdoYmdhmsFr,
					//vdoYmdhmsTo: sVdoYmdhmsTo,
					playStartYmdhms: sPlayStartYmdhms,
					playEndYmdhms: sPlayEndYmdhms,
					maskingYn: sMaskingYn,
					thirdPartyYn: sThirdPartyYn,
					thirdPartyPw: sThirdPartyPw,
					tvoPrgrsCd: '10',	// 신청
					outChkStepCd: '10',	// 체크전
				//	wsUrl: sWsUrl
				};
				//console.log(oParams);

				let msg = '반출을 신청하시겠습니까?';
				if ( isLongVdo ) {
					msg = '영상의 길이가 길수록 반출에 많은 시간이 소요되므로<br/>카메라당 반출영상의 길이는 "'+oTvoConfig.recommVdoDuration+'분"으로 권장합니다.<br/><br/>'+msg;
				}
				oCommon.modalConfirm('modal-confirm', '알림', msg, function () {
					$.ajax({
						type: 'POST',
						async: false,
						dataType: 'json',
						url:contextRoot + '/tvo/rqst/outRqst/insertOutRqst.json',
						data: oParams,
						success: function (data) {
							if ($checkbox.length == parseInt(data.result)) {
								oCommon.modalAlert('modal-alert', '알림', '정상적으로 처리하였습니다.');
								oViewFirstRqst.cctv.emptyTable();

								oViewFirstRqst.view.rqst.detail.init(oParams.dstrtCd, oParams.viewRqstNo);
								//	oDiv.setDiv('right', '0', {
								//		target: 'tvoBlank/rqst/div/outRqst'		// 반출신청목록
								//	}, function() {
								//		setTimeout(function() {
								//			oViewFirstRqst.out.rqst.grid.init();
								//		}, 500);
								//	});
							} else {
								oCommon.modalAlert('modal-alert', '알림', '실패하였습니다.');
							}
						},
						error: function (data, status, err) {
							console.log(data);
						}
					});
				});
			},
			isLongVdo: function (vdoInfo) {
				console.log("=== oTvoConfig.recommVdoDuration => "+oTvoConfig.recommVdoDuration);
				let recommVdoDuration = oTvoConfig.recommVdoDuration;	// 권장 반출영상 길이(분)
				
				let info = vdoInfo.split(":");
				var momentVdoYmdhmsFr = moment(info[1], 'YYYYMMDDHHmmss');
				var momentVdoYmdhmsTo = moment(info[2], 'YYYYMMDDHHmmss');
				let vdoYmdHmsDuration = momentVdoYmdhmsTo.diff(momentVdoYmdhmsFr,"minutes");
				console.log("=== vdoYmdHmsDuration => "+vdoYmdHmsDuration);
				if ( recommVdoDuration < Number(vdoYmdHmsDuration) ) {
					return true;
				}
				return false;
			},
			registerFromPlayer: function () {
				
			}
		},

	};

	// 카메라 oViewFirstRqst.cctv{}
	this.cctv = {

		// oViewFirstRqst.cctv.plus()
		plus: function (map) {
			console.log('- oViewFirstRqst.cctv.plus() => o%', map);
			for (key in map) {
				console.log(key, map[key]);
			}
			
			let recommVdoDuration = oTvoConfig.recommVdoDuration;	// 권장 반출영상 길이(분)
			let momentVdoYmdhmsFr = moment(map.get('sYmdhmsFr'), 'YYYYMMDDHHmmss');
			let momentVdoYmdhmsTo = moment(map.get('sYmdhmsTo'), 'YYYYMMDDHHmmss');
			let sYmdhmsFrShow = momentVdoYmdhmsFr.format('YYYY-MM-DD HH:mm');
			let sYmdhmsToShow = momentVdoYmdhmsTo.format('YYYY-MM-DD HH:mm');
			
			let vdoYmdHmsDuration = momentVdoYmdhmsTo.diff(momentVdoYmdhmsFr,"minutes");
			let isLong = false;
			if ( recommVdoDuration < Number(vdoYmdHmsDuration) ) {
				isLong = true;
			}
			vdoYmdHmsDuration = "("+Math.floor(vdoYmdHmsDuration/60)+":"+vdoYmdHmsDuration%60+")";

			var $tbody = $('#out-rqst-cctv tbody');
			if ($tbody.find('tr.tr-no-data').length) {
				$tbody.empty();
			}

			var $rowNum = $('#out-rqst-cctv tbody td.row-num');

			var $tr = $('<tr/>', {
				'data-fclt-id': map.get('sFcltId'), 'data-fclt-lbl-nm': map.get('sFcltLblNm'),
				'data-point-x': map.get('sPointX'), 'data-point-y': map.get('sPointY'),
			});

			var $inputCheckbox = $('<input/>', {'type': 'checkbox', 'value': map.get('sFcltId') + ":" + map.get('sYmdhmsFr') + ":" + map.get('sYmdhmsTo')});

			var $btnMoveTo = $('<button/>', {
				'type': 'button', 'class': 'btn btn-default btn-xs', 'title': '이동',
				'onclick': 'javascript:oTvoCmn.map.select(this);',
				'html': '<i class="fas fa-map-pin"></i>'
			});

			var $btnCctv = $('<button/>', {
				'type': 'button', 'class': 'btn btn-default btn-xs', 'title': '보기',
				'onclick': 'javascript:oVmsCommon.openVmsPlayerFrTo("' + map.get('sFcltId') + '", "SEARCH", "' + map.get('sEvtYmdhms') + '", "' + map.get('sYmdhmsFr') + '", "' + map.get('sYmdhmsTo') + '", "Y");',
				'html': '<i class="fas fa-video"></i>'
			});

			var $btnRemove = $('<button/>', {
				'type': 'button', 'class': 'btn btn-default btn-xs', 'title': '제외',
				'onclick': 'javascript:oViewFirstRqst.cctv.minus("' + map.get('sFcltId') + ":" + map.get('sYmdhmsFr') + ":" + map.get('sYmdhmsTo') + '");',
				'html': '<i class="fas fa-minus"></i>'
			});

			$tr.append($('<td/>', {'class': 'text-center', 'html': $inputCheckbox}));
			$tr.append($('<td/>', {'class': 'row-num text-center', 'text': $rowNum.length + 1}));
			$tr.append($('<td/>', {'class': 'text-ellipsis', 'title': map.get('sFcltLblNm'), 'html': map.get('sFcltLblNm')}));
			if ( isLong ) {
			$tr.append($('<td/>', {'class': 'text-center', 'style':'color:red'
									, 'title': sYmdhmsFrShow + " ~ " + sYmdhmsToShow + " " + vdoYmdHmsDuration
									, 'html': sYmdhmsFrShow + " ~ " + sYmdhmsToShow + " " + vdoYmdHmsDuration
									}));
			} else {
			$tr.append($('<td/>', {'class': 'text-center'
									, 'title': sYmdhmsFrShow + " ~ " + sYmdhmsToShow + " " + vdoYmdHmsDuration
									, 'html': sYmdhmsFrShow + " ~ " + sYmdhmsToShow + " " + vdoYmdHmsDuration
									}));
			}
			$tr.append($('<td/>', {'class': 'text-center', 'title': '이동', 'html': $btnMoveTo}));
			$tr.append($('<td/>', {'class': 'text-center', 'title': '보기', 'html': $btnCctv}));
			$tr.append($('<td/>', {'class': 'text-center', 'title': '제외', 'html': $btnRemove}));
			$tbody.append($tr);

		},

		// oViewFirstRqst.cctv.minus()
		minus: function (vdoData) {
			console.log('- oViewFirstRqst.cctv.minus(), vdoData => ', vdoData);
			oViewFirstRqst.cctv.removeVdo(vdoData);			// 선택한 영상신청정보를 삭제한다.

			let sFcltId = vdoData.split(":")[0];
			let cctv = oCurrentEvent.cctv;
			let sYmdhmsFr = moment(cctv.from).format('YYYYMMDDHHmmss');
			let sYmdhmsTo = moment(cctv.to).format('YYYYMMDDHHmmss');
			let stdData = sFcltId + ":" + sYmdhmsFr + ":" + sYmdhmsTo;		// 지도폴대팝업카메라정보
			console.log('- oViewFirstRqst.cctv.minus(), stdData => ' + stdData);

			if (vdoData == stdData) {	// 지도폴대카메라의 plus버튼 클릭 시 추가되는 영상신청정보와 같을 때
				var $btn = $('#table-click-fclt tbody .fa-minus');
				$.each($btn, function (j, v) {
					let $buttn = $(v).parent();
					let dat = $buttn.data();
					console.log(" == [" + j + "] " + sFcltId + " == " + dat.fcltId);

					if (sFcltId == dat.fcltId) {		// 선택한 카메라일 때 minus 버튼을 plus 버튼으로 변경한다.
						$buttn.html('<i class="fas fa-plus"></i>');
						$buttn.attr('title', '반출신청대상목록에 추가');
						$buttn.attr('onclick', 'javascript:oViewFirstRqst.cctv.add(this);');
					}
				});
			}
		},

		//oViewFirstRqst.cctv.removeVdo(vdoData)
		removeVdo: function (vdoData) {
			var $checkbox = $('#out-rqst-cctv tbody input[type="checkbox"]');
			$.each($checkbox, function (j, v) {
				let rqstVal = $(v).val();				//console.log(vdoData+" == "+rqstVal);
				if (vdoData == rqstVal) {
					$(v).closest("tr").remove();
					oViewFirstRqst.cctv.setRowNum();
				}
			});
		},

		// oViewFirstRqst.cctv.checkIsExist(map)
		checkIsExist: function (map) {
			let isExist = false;
			let vdoData = map.get('sVdoData');
			let $checkbox = $('#out-rqst-cctv tbody input[type="checkbox"]');
			$.each($checkbox, function (j, v) {
				let rqstVal = $(v).val();
				//console.log("--- oViewFirstRqst.cctv.checkIsExist() => "+vdoData+" == "+rqstVal);
				if (vdoData == rqstVal) {
					isExist = true;
				}
			});
			
			if ( ! isExist ) {
				let datas = vdoData.split(":");
				$.ajax({
					type: 'POST',
					async: false,
					dataType: 'json',
					url:contextRoot + '/tvo/out/selectOutRqstListTotCnt.json',
					data: {
						dstrtCd   : $('#view-rqst-dtl-dstrtCd').val(),
						viewRqstNo: $('#view-rqst-dtl-viewRqstNo').val(),
						cctvId: datas[0],
						vdoYmdhmsFr: datas[1],
						vdoYmdhmsTo: datas[2]
					},
					success: function (data) {
						if (data.totalRows != '0') {
							isExist = true;
							//oCommon.modalAlert('modal-alert', '알림', '이미 반출신청한 영상입니다.');
						}
					},
					error: function (data, status, err) {
						console.log(data);
					}
				});
			}
			console.log("--- oViewFirstRqst.cctv.checkIsExist(), isExist => " + isExist);
			return isExist;
		},

		// oViewFirstRqst.cctv.add(this)
		add: function (element) {

			var oEleData = $(element).data();
			let cctv = oCurrentEvent.cctv;
			let sYmdhmsFr = moment(cctv.from).format('YYYYMMDDHHmmss');
			let sYmdhmsTo = moment(cctv.to).format('YYYYMMDDHHmmss');
			let vdoData = oEleData.fcltId + ":" + sYmdhmsFr + ":" + sYmdhmsTo;		// 신청대상정보
			console.log('--- vdoData : ' + vdoData);

			let $ele = $(element);					//console.log('- html : '+$ele.html());
			if ($ele.find('.fa-plus').length) {
				console.log('-- act : add');

				let map = new Map();	map.set('sVdoData', vdoData);
				if (oViewFirstRqst.cctv.checkIsExist(map)) {
					oCommon.modalAlert('modal-alert', '알림', '이미 반출신청한 영상입니다.');
				} else {
					var sEvtYmdhms = $('#view-rqst-dtl-evtYmdhms').text();
					var momentEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss');
					sEvtYmdhms = momentEvtYmdhms.format('YYYYMMDDHHmmss');

					let map = new Map();
					map.set('sFcltId', oEleData.fcltId);
					map.set('sFcltLblNm', oEleData.fcltLblNm);
					map.set('sPointX', oEleData.pointX);
					map.set('sPointY', oEleData.pointY);
					map.set('sEvtYmdhms', sEvtYmdhms);
					map.set('sYmdhmsFr', sYmdhmsFr);
					map.set('sYmdhmsTo', sYmdhmsTo);
					oViewFirstRqst.cctv.plus(map);
				}
				$(element).html('<i class="fas fa-minus"></i>');
				$(element).attr('title', '반출신청대상목록에서 제거');
				$(element).attr('onclick', 'javascript:oViewFirstRqst.cctv.remove(this);');
			}
		},

		// oViewFirstRqst.cctv.remove(this)
		remove: function (element) {

			let sFormatterForParsing = 'YYYY-MM-DD HH:mm:ss';
			let cctv = oCurrentEvent.cctv;
			let videoSearchYmdhmsFrVal = moment(cctv.from).format(sFormatterForParsing);
			let videoSearchYmdhmsToVal = moment(cctv.to).format(sFormatterForParsing);
			let sYmdhmsFr = videoSearchYmdhmsFrVal.replaceAll(' ', '').replaceAll('-', '').replaceAll(':', '');
			let sYmdhmsTo = videoSearchYmdhmsToVal.replaceAll(' ', '').replaceAll('-', '').replaceAll(':', '');

			var oEleData = $(element).data();
			let vdoData = oEleData.fcltId + ":" + sYmdhmsFr + ":" + sYmdhmsTo;		// 신청대상정보
			console.log('- vdoData : ' + vdoData);

			let $ele = $(element);					//console.log('- html : '+$ele.html());
			if ($ele.find('.fa-plus').length) {

			} else {
				console.log('-- act : remove');
				oViewFirstRqst.cctv.removeVdo(vdoData);			// 선택한 영상신청정보를 삭제한다.

				$(element).html('<i class="fas fa-plus"></i>');
				$(element).attr('title', '반출신청대상목록에 추가');
				$(element).attr('onclick', 'javascript:oViewFirstRqst.cctv.add(this);');
			}
		},

		// oViewFirstRqst.cctv.selectAll()
		selectAll: function (element) {
			var isChecked = $(element).is(':checked');
			var $checkbox = $('#out-rqst-cctv tbody input[type="checkbox"]');
			$checkbox.prop('checked', isChecked);
		},

		// oViewFirstRqst.cctv.delSelect()
		delSelect: function () {
			var $checkbox = $('#out-rqst-cctv tbody input[type="checkbox"]');

			if (!$checkbox.is(':checked')) {
				oCommon.modalAlert('modal-alert', '알림', '선택된 카메라가 없습니다.');
				return false;
			}

			$.each($checkbox, function (i, v) {
				var isChecked = $(v).is(':checked');
				if (isChecked) {
					$(v).closest('tr').remove();
				}
			});

			var $rowNum = $('#out-rqst-cctv tbody td.row-num');
			$.each($rowNum, function (i, v) {
				$(v).text(i + 1);
			});
			oViewFirstRqst.cctv.checkTableEmpty();
		},

		// oViewFirstRqst.cctv.setRowNum()
		setRowNum: function () {
			var $rowNum = $('#out-rqst-cctv tbody td.row-num');
			$.each($rowNum, function (i, v) {
				$(v).text(i + 1);
			});
			oViewFirstRqst.cctv.checkTableEmpty();
		},

		checkTableEmpty: function () {
			var $tbody = $('#out-rqst-cctv tbody');
			if (!$tbody.find('tr').length) {
				$tbody.html($('<tr/>', {
					'class': 'tr-no-data',
					'html': '<td colspan="7" class="text-center">선택된 카메라가 없습니다.</td>'
				}));
			}
		},

		emptyTable: function () {
			var $tbody = $('#out-rqst-cctv tbody');
			$tbody.empty();
			$tbody.html($('<tr/>', {
				'class': 'tr-no-data',
				'html': '<td colspan="7" class="text-center">선택된 카메라가 없습니다.</td>'
			}));
		},

		// oViewFirstRqst.cctv.pole()
		pole: (fcltId) => {
			// 폴대 카메라 그룹영상 재생하기
			oVmsCommon.group(fcltId, undefined, undefined, (data) => {
				olSwipMap.overlays.click.setPosition(undefined);
				const $searchDeck = $('#search-deck');
				const $webRtcView = $('#web-rtc-view');
				const $webRtcViewContainer = $('.web-rtc-view-container');

				if (!$searchDeck.hasClass('full')) {
					$webRtcView.addClass('full');
					$searchDeck.addClass('full');
				}

				$webRtcViewContainer.removeClass('one');
				$webRtcViewContainer.removeClass('four');
				$webRtcViewContainer.removeClass('six');

				if (data.length <= 1) {			$webRtcViewContainer.addClass('one');
				} else if (data.length <= 4) {	$webRtcViewContainer.addClass('four');
				} else if (data.length <= 6) {	$webRtcViewContainer.addClass('six');
				}

				const $buttons = $('.web-rtc-view-container .web-rtc-view-container-buttons button.extra');
				if ($buttons.length) {
					for (let element of $buttons) {
						let fcltId = $(element).data('fcltId');
						for (let cctv of data) {
							if (fcltId === cctv.fcltId) {
								let sFrom = moment($('#params-video-search-ymdhms-fr').val(), 'YYYY-MM-DD HH:mm').format('YYYYMMDDHHmmss');
								let sTo = moment($('#params-video-search-ymdhms-to').val(), 'YYYY-MM-DD HH:mm').format('YYYYMMDDHHmmss');
								let vdoData = fcltId + ":" + sFrom + ":" + sTo;
								console.log('--- vdoData : '+vdoData);
																			
								let $outRqstCctv = $('#out-rqst-cctv');
								if ($outRqstCctv.length) {	// 반출신청대상을 추가할 때
									var eOutRqstCctv = document.getElementById('out-rqst-cctv');
									var eTbody = $(eOutRqstCctv).find('tbody');				//console.log("=== eTbody.length : "+eTbody.length);
									if (eTbody.length) {	// 추가된 반출대상이 있을 때
										let map = new Map();	map.set('sVdoData', vdoData);
										if(!oViewFirstRqst.cctv.checkIsExist(map)) {
											$(element).removeClass('hide');
										}
									} else {
										$(element).removeClass('hide');
									}
								}

								//$(element).attr('title', '반출대상추가');
								$(element).text('반출대상추가');
								$(element).on('click', () => {
									var eEvtYmdhms = document.getElementById('view-rqst-dtl-evtYmdhms');
									var sEvtYmdhms = $(eEvtYmdhms).text();
									sEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss').format('YYYYMMDDHHmmss');
						
									let rFrom = moment($('#params-video-search-ymdhms-fr').val(), 'YYYY-MM-DD HH:mm').format('YYYYMMDDHHmmss');
									let rTo = moment($('#params-video-search-ymdhms-to').val(), 'YYYY-MM-DD HH:mm').format('YYYYMMDDHHmmss');
								
									let map = new Map();
									map.set('sFcltId', cctv.fcltId);
									map.set('sFcltLblNm', cctv.fcltLblNm);
									map.set('sPointX', cctv.pointX);
									map.set('sPointY', cctv.pointY);
									map.set('sEvtYmdhms', sEvtYmdhms);
									map.set('sYmdhmsFr', rFrom);
									map.set('sYmdhmsTo', rTo);
									oViewFirstRqst.cctv.plus(map);
									oCommon.modalAlert('modal-alert', '알림', '등록하였습니다.');
									$(element).addClass('hide');
								});
							}
						}
					}
				}
			});
		},
		assign: (event) => {
			let $target = $(event.target);
			if($target.val() !== '') {
				oVmsCommon.assign($target.data('fcltId'), parseInt($target.val()), (data) => {
					const $searchDeck = $('#search-deck');
					const $webRtcView = $('#web-rtc-view');
					if (!$searchDeck.hasClass('full')) {
						$webRtcView.addClass('full');
						$searchDeck.addClass('full');
					}

					const $webRtcViewContainer = $('.web-rtc-view-container');
					$webRtcViewContainer.removeClass('one');
					$webRtcViewContainer.removeClass('four');
					$webRtcViewContainer.removeClass('six');

					const $buttons = $('.web-rtc-view-container .web-rtc-view-container-buttons button.extra');
					if ($buttons.length) {
						for (let element of $buttons) {
							let fcltId = $(element).data('fcltId');
							for (let cctv of data) {
								if (fcltId === cctv.fcltId) {
									let sFrom = moment($('#params-video-search-ymdhms-fr').val(), 'YYYY-MM-DD HH:mm').format('YYYYMMDDHHmmss');
									let sTo = moment($('#params-video-search-ymdhms-to').val(), 'YYYY-MM-DD HH:mm').format('YYYYMMDDHHmmss');
									let vdoData = fcltId + ":" + sFrom + ":" + sTo;
									console.log('--- vdoData : '+vdoData);
											
									let $outRqstCctv = $('#out-rqst-cctv');
									if ($outRqstCctv.length) {	// 반출신청대상을 추가할 때
										var eOutRqstCctv = document.getElementById('out-rqst-cctv');
										var eTbody = $(eOutRqstCctv).find('tbody');				//console.log("=== eTbody.length : "+eTbody.length);
										if (eTbody.length) {	// 추가된 반출대상이 있을 때
											let map = new Map();	map.set('sVdoData', vdoData);
											if(!oViewFirstRqst.cctv.checkIsExist(map)) {
												$(element).removeClass('hide');	// 추가버튼 보이기
											}
										} else {
											$(element).removeClass('hide');		// 추가버튼 보이기
										}									
									}
									//$(element).attr('title', '반출대상추가');
									$(element).text('반출대상추가');
									$(element).on('click', () => {
										var eEvtYmdhms = document.getElementById('view-rqst-dtl-evtYmdhms');
										var sEvtYmdhms = $(eEvtYmdhms).text();
										sEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss').format('YYYYMMDDHHmmss');
							
										let map = new Map();
										map.set('sFcltId', cctv.fcltId);
										map.set('sFcltLblNm', cctv.fcltLblNm);
										map.set('sPointX', cctv.pointX);
										map.set('sPointY', cctv.pointY);
										map.set('sEvtYmdhms', sEvtYmdhms);
										map.set('sYmdhmsFr', vdoData.split(":")[1]);
										map.set('sYmdhmsTo', vdoData.split(":")[2]);
										oViewFirstRqst.cctv.plus(map);
										oCommon.modalAlert('modal-alert', '알림', '등록하였습니다.');
										$(element).addClass('hide');
									});
								}
							}
						}
					}
				});
				$target.val('');
			}
		},
	};

	// 지도 oViewFirstRqst.map{}
	this.map = {
		// oViewFirstRqst.map.init()
		init: function () {
			// 1. BASE, SATELLITE 배경지도 생성.
			olSwipMap.init({
				bookmark: false,
				dropdown: true
			});

			setTimeout(function () {
				olSwipMap.layers.fclt.data.searchTvoTrgtYn = 'Y';			// 영상반출대상여부
				olSwipMap.layers.fclt.init();

				olSwipMap.events.feature.pointermove = oViewFirstRqst.map.featurePopup.handler;
				olSwipMap.events.feature.click = oViewFirstRqst.map.featurePopup.handler;

				olSwipMap.listeners.feature.pointermove = olMap.map.on('pointermove', olSwipMap.events.feature.pointermove);
				olSwipMap.listeners.feature.click = olMap.map.on('click', olSwipMap.events.feature.click);
			}, 500);

		},
		featurePopup: {
			handler: function (event) {
				let oOptOptions = {
					hitTolerance: 10
				};

				var oFeaturesFclt = [];
				olMap.map.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
					if (layer != null) {
						if (olMap.layers.fclt === layer) {
							var olFeatures = feature.get('features');
							if (typeof olFeatures == 'undefined') {
								oFeaturesFclt.push(feature.getProperties());
							} else {
								$.each(olFeatures, function (i, f) {
									oFeaturesFclt.push(f.getProperties());
								});
							}
                        } else if (olMap.layers.angle === layer) {
                            oFeaturesFclt.push(feature.getProperties());
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
					var elTbody = $olOverlay.find('#table-' + event.type + '-fclt tbody');
					if (elTbody.length == 1) {
						var $tbody = $(elTbody[0]);
						$tbody.empty();
						var nMaxCntFclt = 30;
						if (oFeaturesFclt.length <= nMaxCntFclt) {

							$.each(oFeaturesFclt, function (i, v) {
								//console.log("== handler, v => %o", v);

								let sFormatterForParsing = 'YYYY-MM-DD HH:mm:ss';
								let cctv = oCurrentEvent.cctv;
								let videoSearchYmdhmsFrVal = moment(cctv.from).format(sFormatterForParsing);
								let videoSearchYmdhmsToVal = moment(cctv.to).format(sFormatterForParsing);
								let sYmdhmsFr = videoSearchYmdhmsFrVal.replaceAll(' ', '').replaceAll('-', '').replaceAll(':', '');
								let sYmdhmsTo = videoSearchYmdhmsToVal.replaceAll(' ', '').replaceAll('-', '').replaceAll(':', '');

								let vdoData = v.fcltId + ":" + sYmdhmsFr + ":" + sYmdhmsTo;		// 신청대상정보
								// console.log('-- vdoData : ' + vdoData);
								
								const oAg = olMap.layers.angle.getSource().getFeatureById('AG_' + v.fcltId);
								let nCctvOsvtAg = NaN;
								if (oAg != null) {
									nCctvOsvtAg = oAg.getProperties().cctvOsvtAg;
									if (nCctvOsvtAg < 0) nCctvOsvtAg = 360 + nCctvOsvtAg;
									nCctvOsvtAg = Math.round(nCctvOsvtAg / 10) * 10;
								}
								let sFcltKndDtlCd = v.fcltKndDtlCd;
								let sFcltKndDtlNm = '';
								if (sFcltKndDtlCd === 'RT') {
									sFcltKndDtlNm = '<i class="fas fa-sync-alt" title="회전형CCTV" data-toggle="tooltip" data-placement="top"></i> ';
								} else if (sFcltKndDtlCd === 'FT' && !isNaN(nCctvOsvtAg)) {
									sFcltKndDtlNm = `<i class="fas fa-arrow-up" title="고정형CCTV(${nCctvOsvtAg}˚)" style="transform: rotate(${nCctvOsvtAg}deg)" data-toggle="tooltip" data-placement="top"></i> `;
								}

								var $tr = $('<tr/>');
								$tr.append($('<td/>', {
									'title': v.fcltLblNm,
									'html': sFcltKndDtlNm + v.fcltLblNm
								}));

								$tr.append($('<td/>', {
									'text': v.fcltKndNm + '(' + v.fcltUsedTyNm + ')'
								}));

								//var sFcltGdsdtNm = (v.fcltGdsdtTy == '0') ? '주' : '보조';
								//var sFcltKndDtlNm = (v.fcltKndDtlCd == 'RT') ? '회전' : '고정';
								//$tr.append($('<td/>', {
								//	'text': sFcltGdsdtNm + '(' + sFcltKndDtlNm + ')'
								//}));

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
									'html': $fcltSttus
								}));

								if (event.type == 'click') {

									var isExist = false;
									let map = new Map();	map.set('sVdoData', vdoData);
									if(oViewFirstRqst.cctv.checkIsExist(map)) {	isExist = true;	}
									// console.log('- isExist : ' + isExist);

									let viewEndYn = $("#view-rqst-dtl-viewEndYn").val();	// 열람종료여부
									// console.log("## viewEndYn = " + viewEndYn);

									if (viewEndYn == 'N' && v.fcltKndCd == 'CTV' && (v.viewerTyCd == 'VMS' || v.viewerTyCd == 'RTSP')) {

										const $tdReq = $('<td/>');

										delete v.geometry;

										const $btnAdd = $('<button/>', {
											'class': 'btn btn-primary btn-xs',
											'title': '반출신청대상목록에 추가',
											'data-point-x': v.pointX,
											'data-point-y': v.pointY,
											'data-fclt-id': v.fcltId,
											'data-fclt-lbl-nm': v.fcltLblNm,
											'html': '<i class="fas fa-plus"></i>',
											'onclick': 'javascript:oViewFirstRqst.cctv.add(this);'
										});
										if (isExist) {
											$btnAdd.html('<i class="fas fa-minus"></i>');
											$btnAdd.attr('title', '반출신청대상목록에서 제거');
											$btnAdd.attr('onclick', 'javascript:oViewFirstRqst.cctv.remove(this);');
										}

										/*	const $btnCastnet = $('<button/>', {
												'type': 'button',
												'class': 'btn btn-default btn-xs btn-castnet btn-tooltip',
												'onclick': 'javascript:olSwipMap.mntr.castnet(this);',
												'title': '지점에서 가장 가까운 주 CCTV들의 영상을 재생합니다.',
												'text': '투망',
												'data-toggle': 'tooltip',
												'data-placement': 'left',
											});
											$btnCastnet.data(v);	*/

										const $btnPole = $('<button/>', {
											'type': 'button',
											'class': 'btn btn-default btn-xs btn-pole btn-tooltip',
											'onclick': 'javascript:oViewFirstRqst.cctv.pole("' + v.fcltId + '");',
											'title': '폴에 설치된 CCTV들의 영상을 재생합니다.',
											'text': '폴',
											'data-toggle': 'tooltip',
											'data-placement': 'left',
										});

										const $btnPopover = $('<button/>', {
											'type': 'button',
											'class': 'btn btn-default btn-xs btn-popover btn-tooltip',
											'onclick': 'javascript:olSwipMap.mntr.popover.open(this);',
											'title': '해당 CCTV의 영상을 팝오버 형태로 띄워 재생합니다.',
											'text': '팝오버',
											'data-toggle': 'tooltip',
											'data-placement': 'left',
										});
										$btnPopover.data(v);
										$btnPopover.data('projection', 'EPSG:4326');

										//let sEvtYmdhms = $('#view-rqst-dtl-evtYmdhms').text();
										//const momentEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss');
										//sEvtYmdhms = momentEvtYmdhms.format('YYYYMMDDHHmmss');
										let sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();
										let sDstrtCd  = $('#view-rqst-dtl-dstrtCd').val();
										
										
										// TODO... oVmsCommon.openVmsPlayer() 함수 수정
										
										const $btnNewWindow = $('<button/>', {
											'type': 'button',
											'class': 'btn btn-default btn-xs btn-new-window btn-tooltip',
											//'onclick': 'javascript:oVmsCommon.openVmsPlayer("' + v.fcltId + '", "SEARCH", "' + sEvtYmdhms + '");',	// 카메라 과거영상 재생(새창)
											'onclick': 'javascript:oVmsCommon.openVmsPlayer("' + v.fcltId + '", "SEARCH", "' + sDstrtCd + '", "' + sViewRqstNo + '");',	// 카메라 과거영상 재생(새창)
											'title': '해당 CCTV의 영상을 새창으로 분리해 큰화면으로 재생합니다.',
											'text': '새창',
											'data-toggle': 'tooltip',
											'data-placement': 'left',
										});

										const $selAssign = $('<select/>', {
											'class': 'form-control input-xs inline-block',
											'title': '지정된 슬롯에 영상 재생',
										});

										$selAssign.append($('<option/>', {
											'value': '',
											'text': '선택',
										}));

										for (let j = 1; j < 10; j++) {
											$selAssign.append($('<option/>', {
												'value': j,
												'text': j,
												'title': j + '번째 슬롯에 영상 재생',
											}));
										}

										$selAssign.data(v);
										$selAssign.on('change', oViewFirstRqst.cctv.assign);

										let showCctvListTf = false;
										// console.log("== oConfigure => %o", oConfigure);
										//if (oConfigure.cctvAccessYn === 'Y' && oConfigure.ptzCntrTy !== 'NO') {
										//	showCctvListTf = false;
										//}
										if (oConfigure.cctvAccessYn === 'Y' && oConfigure.ptzCntrTy !== 'NO') {
											showCctvListTf = true;
										}

										const $tvoPrgrsCd = $('#view-rqst-dtl-tvoPrgrsCd');
										
										if ($tvoPrgrsCd.val() == '10') {		// 신청
											showCctvListTf = true;
										}

										//if ( $('#view-rqst-dtl-viewEndYn').val()=='Y' ) showCctvListTf = true;

										if (showCctvListTf) {
											/*
											if ($('#chk-designation').is(':checked')) {	// 선택영상체크했을 때
												for (let j = 1; j < 6; j++) {
													$tdReq.append($('<button/>', {
														'type': 'button',
														'class': 'btn btn-primary btn-xs btn-designation btn-tooltip',
														'onclick': 'javascript:oVmsCommon.assign("' + v.fcltId + '", ' + j + ');',
														'title': j + '번째 슬롯에 영상 재생',
														'text': j,
														'data-toggle': 'tooltip',
														'data-placement': 'left',
													}));
												}
											} else {
											*/
												let $outRqstCctv = $('#out-rqst-cctv');
												if ($outRqstCctv.length) {	// 반출신청대상을 추가할 때
													$tdReq.append($btnAdd);
												}
												//$tdReq.append($btnCastnet);
												$tdReq.append($btnPole);
												$tdReq.append($btnPopover);
												$tdReq.append($btnNewWindow);
												$tdReq.append($selAssign);
											/*
											}
											*/
										}

										$tr.append($tdReq);
									}
								} else {
									//$tr.append($('<td/>'));
								}
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
	};

}
