let oDivPlaylist = null;

$(function () {
	oVmsCommon = new vmsCommon();

	$(window).unload(function () {
		if (oConfigure.fullScreenCloseYn == 'Y') {
			if (typeof oVmsCommon.player != 'undefined' && oVmsCommon.player != null) {
				$.each(oVmsCommon.player, function (i, v) {
					if (v != 'undefined' || v != null) {
						v.close();
					}
				});
			}
		}

		oVmsService.disconnectAll();
	});
});

function vmsCommon() {
	this.player = [];
	this.enchainPlay = null;
	this.cnOsvtPlaytimeStop = null;

	// VMS 영상 단일 채널 팝업
//	this.openVmsPlayer = function (pFcltId, pMode, pViewRqstNo, sendFromToSpeedYn, pData) {
//		console.log(`-- openVmsPlayer(${pFcltId}, ${pMode}, ${pViewRqstNo}, ${sendFromToSpeedYn})`);
	this.openVmsPlayer = function (pFcltId, pMode, pDstrtCd, pViewRqstNo, pData) {
		pData = pData || {};
		console.log(`========== openVmsPlayer(${pFcltId}, ${pMode}, ${pDstrtCd}, ${pViewRqstNo}, %o)`, pData);
		
		if (typeof pMode == 'undefined') {
			let oCctvSearchInfo = oCurrentEvent.cctv;
			pMode = oCctvSearchInfo.mode;
			if ( pMode == '' ) pMode='SEARCH'; 
		}
								
		let ymdhms = '';
		//if (typeof pViewRqstNo == 'undefined') {
			let sEvtYmdhms = $('#view-rqst-dtl-evtYmdhms').text();
			const momentEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss');
			ymdhms = momentEvtYmdhms.format('YYYYMMDDHHmmss');
		//}

		let sFormatter = 'YYYY-MM-DD HH:mm';
		let sFr = $('#params-video-search-ymdhms-fr').val() || '';		// 폴감시 시작시각
		let sTo = $('#params-video-search-ymdhms-to').val() || '';		// 폴감시 종료시각
		if ( sTo != '' ) {
			sFr = moment(sFr, sFormatter).valueOf();
			sTo = moment(sTo, sFormatter).valueOf();
		}
		
		let sSpeed = $('#params-video-search-speed option:selected').val() || '';
		
		let sAction = "SEE";	// 영상조회
		let $outRqstCctv = $('#out-rqst-cctv');
		if ($outRqstCctv.length) {	// 반출신청대상 추가일 때
			sAction = "ADD";	// 영상반출추가
		}

		let nStart = 0;
		if (!$.isEmptyObject(pData)) {
			const oSession = oVmsService.sessions[pData.sessionId];
			if (pMode === 'SEARCH') nStart = oSession.utcTimeStamp;
		}

		console.log(`-- openVmsPlayer() => ${pFcltId}, ${pMode}, ${pViewRqstNo}, ${ymdhms}, ${sFr}, ${sTo}, ${nStart}, ${sSpeed}, ${sAction}`);

		setTimeout(() => {
			let sName = '_blank';
			if (typeof oVms.multiYn != 'undefined' && oVms.multiYn == 'N') sName = 'VmsPlayer';

			let $form = $('#form-web-rtc');
			if ($form.length) {
				$form.find('input[type="hidden"]').val('');
				$('#form-web-rtc input[name=fcltId]').val(pFcltId);
				$('#form-web-rtc input[name=viewRqstNo]').val(pViewRqstNo);
				$('#form-web-rtc input[name=mode]').val(pMode);
				$('#form-web-rtc input[name=ymdhms]').val(ymdhms);
				$('#form-web-rtc input[name=from]').val(sFr);
				$('#form-web-rtc input[name=to]').val(sTo);
				$('#form-web-rtc input[name=start]').val(nStart);
				$('#form-web-rtc input[name=speed]').val(sSpeed);
				$('#form-web-rtc input[name=action]').val(sAction);
			} else {
				$form = $('<form/>', {
					'id': 'form-web-rtc',
					'method': 'POST',
					'target': sName,
					'action': `${contextRoot}/mntr/vms/playWebRTC.do`
				});
				$form.append($('<input/>', {'type': 'hidden', 'name': 'fcltId', 'value': pFcltId}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'viewRqstNo', 'value': pViewRqstNo}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'mode', 'value': pMode}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'ymdhms', 'value': ymdhms}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'from', 'value': sFr}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'to', 'value': sTo}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'speed', 'value': sSpeed}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'start', 'value': nStart}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'action', 'value': sAction}));
				$form.appendTo(document.body);
			}
			let sSpecs = `status=no,resizable=no,width=${oConfigure.popWidth},height=${oConfigure.popHeight}`;
			let oOpener = oCommon.window.open('about:blank', sName, sSpecs);
			oVmsCommon.player.push(oOpener);
			$form.submit();
		}, 1000);
	};

	// 반출신청대상목록에서 영상조회
	this.openVmsPlayerFrTo = function (pFcltId, pMode, ymdhms, sFr, sTo, modifyYn) {
		console.log(`-- openVmsPlayerFrTo(${pFcltId}, ${pMode}, ${ymdhms}, ${sFr}, ${sTo}, ${modifyYn})`);

		let sViewRqstNo = $('#view-rqst-dtl-viewRqstNo').val();

		if (typeof pMode == 'undefined') {
			let oCctvSearchInfo = oCurrentEvent.cctv;
			pMode = oCctvSearchInfo.mode;
		}

		if (typeof ymdhms == 'undefined') {	ymdhms = "";	}
		if (typeof sFr == 'undefined') {	sFr = "";	}	// 신청영상 시작시각
		if (typeof sTo == 'undefined') {	sTo = "";	}	// 신청영상 종료시각
		if (typeof modifyYn == 'undefined') {	modifyYn = "";	}	// 이미 추가한 정보 수정구분

		let sSpeed = $('#params-video-search-speed option:selected').val() || '';

		let sAction = "SEE";
		let $outRqstCctv = $('#out-rqst-cctv');
		if ($outRqstCctv.length) {	sAction = "ADD";		}	// 반출신청대상 추가일 때

		//let sFormatter = 'YYYY-MM-DD HH:mm:ss';
		let sFormatter = 'YYYYMMDDHHmmss';
		sFr = moment(sFr, sFormatter).valueOf();
		sTo = moment(sTo, sFormatter).valueOf();

		console.log(`-- openVmsPlayerFrTo() => ${pFcltId}, ${pMode}, ${sViewRqstNo}, ${ymdhms}, ${sFr}, ${sTo}, ${sSpeed}, ${sAction}, ${modifyYn}`);

		setTimeout(() => {
			let sName = '_blank';
			if (typeof oVms.multiYn != 'undefined' && oVms.multiYn == 'N') sName = 'VmsPlayer';

			let $form = $('#form-web-rtc');
			if ($form.length) {
				$form.find('input[type="hidden"]').val('');
				$('#form-web-rtc input[name=fcltId]').val(pFcltId);
				$('#form-web-rtc input[name=viewRqstNo]').val(sViewRqstNo);
				$('#form-web-rtc input[name=mode]').val(pMode);
				$('#form-web-rtc input[name=ymdhms]').val(ymdhms);
				$('#form-web-rtc input[name=from]').val(sFr);
				$('#form-web-rtc input[name=to]').val(sTo);
				$('#form-web-rtc input[name=speed]').val(sSpeed);
				$('#form-web-rtc input[name=action]').val(sAction);
				$('#form-web-rtc input[name=modifyYn]').val(modifyYn);
			} else {
				$form = $('<form/>', {
					'id': 'form-web-rtc',
					'method': 'POST',
					'target': sName,
					'action': `${contextRoot}/mntr/vms/playWebRTC.do`
				});
				$form.append($('<input/>', {'type': 'hidden', 'name': 'fcltId',     'value': pFcltId}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'viewRqstNo', 'value': sViewRqstNo}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'mode',       'value': pMode}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'ymdhms',     'value': ymdhms}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'from',       'value': sFr}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'to',         'value': sTo}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'speed',      'value': sSpeed}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'action',     'value': sAction}));
				$form.append($('<input/>', {'type': 'hidden', 'name': 'modifyYn',   'value': modifyYn}));
				$form.appendTo(document.body);
			}
			let sSpecs = `status=no,resizable=no,width=${oConfigure.popWidth},height=${oConfigure.popHeight}`;
			let oOpener = oCommon.window.open('about:blank', sName, sSpecs);
			oVmsCommon.player.push(oOpener);
			$form.submit();
		}, 1000);
	};

	this.openRtspPlayer = function(pFcltId) {
		var sUrl = contextRoot + '/mntr/vms/playRtsp.do?fcltId=' + pFcltId;
		if (typeof evtOcrNo != 'undefined' && evtOcrNo != '') {
			sUrl += '&evtOcrNo=' + evtOcrNo;
		}
		var oOpener = oCommon.window.open(sUrl, '_blank', 'status=no,width=' + oConfigure.popWidth + ',height=' + oConfigure.popHeight);
		oVmsCommon.player.push(oOpener);
		void (0);
	};

	this.play = function (cctvInfo, options) {
		console.log('-- oVmsCommon.play(), cctvInfo => %o',cctvInfo);
		console.log('-- oVmsCommon.play(), options => %o',options);
		
		let oCctvSearchInfo = oVmsCommon.getCctvSearchInfo();
		console.log('-- oVmsCommon.play(), oCctvSearchInfo => %o',oCctvSearchInfo);
		
		if (oCctvSearchInfo.mode == 'PLAY') {
			oVmsService.play(cctvInfo, options);
		} else if (oCctvSearchInfo.mode == 'SEARCH') {
			oVmsService.search(cctvInfo, oCctvSearchInfo.from, oCctvSearchInfo.to);
		}
	};

	this.search = function (cctvInfo) {
		oVmsService.search(cctvInfo);
	};

	this.castnet = function (playlist) {
		$('#container-modal').removeClass('hide');
		appendDiv(new div('DIV-VMS', 'B', 0), '', '', function () {
			setTimeout(function () {
				oVmsService.disconnectPlaylist();

				collapse({
					left: false,
					bottom: false
				});
				oVmsService.playlists = playlist;
				oVmsService.castnet(playlist);
				// oVmsCommon.showPlaylist();
			}, 1000);

		});

		if (oConfigure.cnOsvtPlaytimeStopYn == 'Y') {
			if (oVmsCommon.enchainPlay != null) {
				clearTimeout(oVmsCommon.enchainPlay);
				oVmsCommon.enchainPlay = null;
			}
			oVmsCommon.enchainPlay = setTimeout(function () {
				function callbackConfirm() {
					if (oVmsCommon.cnOsvtPlaytimeStop != null) {
						clearTimeout(oVmsCommon.cnOsvtPlaytimeStop);
						oVmsCommon.cnOsvtPlaytimeStop = null;
					}
					oVmsCommon.enchainPlay();
				}

				function callbackCancle() {
					if (oVmsCommon.cnOsvtPlaytimeStop != null) {
						clearTimeout(oVmsCommon.cnOsvtPlaytimeStop);
						oVmsCommon.cnOsvtPlaytimeStop = null;
					}
					stopCastNet();
				}

				oCommon.modalConfirm('modal-confirm', '기본 재생시간 초과', '기본 재생시간을 초과했습니다. 계속 재생하시겠습니까?', callbackConfirm, callbackCancle);
				oVmsCommon.cnOsvtPlaytimeStop = setTimeout(function () {
					let element = document.getElementById('modal-confirm');
					if ($(element).length) {
						$(element).modal('hide');
						stopCastNet();
					}
				}, 10000);
			}, parseInt(oVms.playTime) * 60 * 1000);
		}

		setTimeout(() => {
			$('#container-modal').addClass('hide');
		}, 1000 * 10);
	};

	// 폴대 카메라 그룹영상 재생하기
	this.group = function (pFcltId, clearDivYn, clearMntrYn, callback) {
		$('#container-modal').removeClass('hide');
		let oCallback = function () {
			setTimeout(function () {
				$.post(contextRoot + '/mntr/vms/selectMngMemberList.json', {
					fcltId: pFcltId
				}).done(function (data) {
					let oPlaylist = data.memberList;
					oVmsService.disconnectPlaylist();

					collapse({
						left: false,
						bottom: false
					});

					if (clearMntrYn !== 'N') olSwipMap.mntr.clear();

					$.each(oPlaylist, function (i, v) {
						v.assignIndex = i + 1;
						olSwipMap.cctv.addFeature(v);
					});

					oVmsService.playlists = oPlaylist;
					oVmsService.castnet(oPlaylist);		// 영상재생하기
					
					if(typeof callback === 'function') callback(data.memberList);
				});
			}, 1000);
		}

		appendDiv(new div('DIV-VMS', 'B', 0), '', '', oCallback);

		if (oConfigure.cnOsvtPlaytimeStopYn === 'Y') {
			if (oVmsCommon.enchainPlay != null) {
				clearTimeout(oVmsCommon.enchainPlay);
				oVmsCommon.enchainPlay = null;
			}
			oVmsCommon.enchainPlay = setTimeout(function () {
				function callbackConfirm() {
					if (oVmsCommon.cnOsvtPlaytimeStop != null) {
						clearTimeout(oVmsCommon.cnOsvtPlaytimeStop);
						oVmsCommon.cnOsvtPlaytimeStop = null;
					}
					oVmsCommon.enchainPlay();
				}

				function callbackCancle() {
					if (oVmsCommon.cnOsvtPlaytimeStop != null) {
						clearTimeout(oVmsCommon.cnOsvtPlaytimeStop);
						oVmsCommon.cnOsvtPlaytimeStop = null;
					}
					stopCastNet();
				}

				oCommon.modalConfirm('modal-confirm', '기본 재생시간 초과', '기본 재생시간을 초과했습니다. 계속 재생하시겠습니까?', callbackConfirm, callbackCancle);
				oVmsCommon.cnOsvtPlaytimeStop = setTimeout(function () {
					let element = document.getElementById('modal-confirm');
					if ($(element).length) {
						$(element).modal('hide');
						stopCastNet();
					}
				}, 10000);
			}, parseInt(oVms.playTime) * 60 * 1000);
		}

		setTimeout(() => {
			$('#container-modal').addClass('hide');
		}, 1000 * 5);
	};

	this.assign = function (pFcltId, index, callback) {
		$.post(contextRoot + '/mntr/fcltById.json', {
			fcltId: pFcltId
		}).done(function (data) {
			let oCctvInfo = data;
			console.log('-- vmsCommon.assign(%s, %d) ========== ', oCctvInfo.pFcltId, index);
			collapse({
				bottom: false
			});

			oCctvInfo.assignIndex = index;
			olSwipMap.cctv.addFeature(oCctvInfo);

			let $webRtcView = $('#web-rtc-view');
			if (!$webRtcView.length) {
				appendDiv(new div('DIV-VMS', 'B', 0), '', '');
			}

			setTimeout(function () {
				oVmsService.assign(oCctvInfo, index);

				if(typeof callback === 'function') callback(oVmsService.playlists);
			}, 1000);

			// setTimeout(function () {
				// oVmsCommon.showPlaylist();
			// }, 2000);
		});
	};

	this.division = function (playlist) {
		oVmsService.disconnectPlaylist();
		oVmsService.playlists = playlist;
		oVmsService.division(playlist);
	};

	this.createSearchSpeedOption = function (text, value, selected) {
		let options = {
			'text': text,
			'value': value
		};

		if (selected) {
			options.selected = true;
		}

		return $('<option/>', options);
	};

	this.syncBtnPlay = function () {
		let eSpan = $('#btn-play span').get(0);
		if (oVmsService.isPlay || oVmsService.isSearch) {
			if ($(eSpan).hasClass('glyphicon-play')) {
				$(eSpan).removeClass('glyphicon-play');
				$(eSpan).addClass('glyphicon-pause');
				$(eSpan).attr('aria-label', 'Pause');
				$(eSpan).attr('title', '일시중지');
			}
		} else {
			if ($(eSpan).hasClass('glyphicon-pause')) {
				$(eSpan).removeClass('glyphicon-pause');
				$(eSpan).addClass('glyphicon-play');
				$(eSpan).attr('aria-label', 'Play');
				$(eSpan).attr('title', '재생');
			}
		}
	};

	/*
	 * oVmsCommon.showPlaylist()
	 */
	this.showPlaylist = function (option) {
		let $article;
		if (typeof option != 'undefined') {
			if (option.hasOwnProperty('position')) {
				$article = $('article#article-' + option.position);
				if (option.position === 'left') {
					collapse({
						left: false
					});
				}
			}
		} else {
			$article = $('article#article-right');
			if ($article.length) {
				collapse({
					right: false
				});
			}
		}

		setTimeout(function () {
			import((`${contextRoot}/js/mntr/layout/div/gnrPlaylist.js`))
				.then(module => {
					const DivPlaylist = module.default;
					oDivPlaylist = new DivPlaylist();
				});

			let $divPlaylist = $('#div-gnr-playlist');
			if (!$divPlaylist.length) {
				$.post(`${contextRoot}/tvo/viewTargetDivision.do`, {
					target: 'gnrPlaylist'
				}, function (data) {
					let $div = $('<div/>', {'class': 'col'});
					$div.html(data);
					$div.appendTo($article);
					if (typeof oDivPlaylist === 'object') oDivPlaylist.init(option);
				});
			} else {
				if (typeof oDivPlaylist === 'object') oDivPlaylist.init(option);
			}
		}, 1000);
	};

	this.insertViewLog = function (params) {
		//params.evtOcrNo = 'NO_EVT_OCR_NO';
		//if (oCurrentEvent.event.evtOcrNo != '') {	params.evtOcrNo = oCurrentEvent.event.evtOcrNo;	}
		
		params.viewRqstNo = 'NO_VIEW_RQST_NO';
		if (oCurrentEvent.event.viewRqstNo !== '') {	params.viewRqstNo = oCurrentEvent.event.viewRqstNo;	}
		
		$.ajax({
			type: 'POST',
			dataType: 'json',
			url:contextRoot + '/mntr/vms/insertViewLog.json',
		/*	data: {
				fcltId: fcltId,
				//evtOcrNo: evtOcrNo,
				viewRqstNo: viewRqstNo
			},	*/
			data: params,
			success: function (data) {
				//console.log(`=== vmsCommon.insertViewLog fcltId[${fcltId}] evtOcrNo[${ocrNo}] ==========`);
				console.log(`=== vmsCommon.insertViewLog => fcltId:[${params.fcltId}], viewRqstNo:[${params.viewRqstNo}], videoSeaYmdHmsFr:[${params.videoSeaYmdHmsFr}], videoSeaYmdHmsTo:[${params.videoSeaYmdHmsTo}] ===`);
			},
			error: function () {
				console.log('-- vmsCommon.insertViewLog Error!');
				setTimeout(function () {
					oVmsCommon.insertViewLog(fcltId);
				}, 500);
			}
		});
	};

	this.insertPtzLog = function (fcltId, ptzCmd) {
		let evt = 'NO_EVT_OCR_NO';
		if (oCurrentEvent.event.evtOcrNo !== '') {
			evt = oCurrentEvent.event.evtOcrNo;
		}

		if (ptzCmd === 'pt[' + oVmsService.api.cmd.ptz.stop + ']') {
			console.log('-- vmsCommon.insertPtzLog Skip stop PTZ logs.');
			return false;
		} else if (ptzCmd === 'focus[' + oVmsService.api.cmd.ptz.focusStop + ']') {
			console.log('-- vmsCommon.insertPtzLog Skip stop PTZ logs.');
			return false;
		} else if (ptzCmd === 'zoom[' + oVmsService.api.cmd.ptz.zoomStop + ']') {
			console.log('-- vmsCommon.insertPtzLog Skip stop PTZ logs.');
			return false;
		}

		$.ajax({
			type: 'POST',
			dataType: 'json',
			url:contextRoot + '/mntr/vms/insertPtzLog.json',
			data: {
				fcltId: fcltId,
				ptzCmd: ptzCmd,
				evtOcrNo: evt
			},
			success: function (data) {
				console.log(`=== vmsCommon.insertPtzLog fcltId[${fcltId}] evtOcrNo[${evt}] ==========`);
			},
			error: function () {
				console.log('-- vmsCommon.insertPtzLog Error!');
				setTimeout(function () {
					oVmsCommon.insertPtzLog(fcltId, ptzCmd);
				}, 500);
			}
		});
	};

	this.getCctvSearchInfo = function () {
		let cctv = oCurrentEvent.cctv;

		const $searchDeck = $('#search-deck');
		if ($searchDeck.length && cctv.evtOcrYmdHms !== '' && cctv.mode === 'SEARCH') {
			// Time Formatter
			//let sFormatter = 'YYYY-MM-DD HH:mm:ss';
			let sFormatter = 'YYYY-MM-DD HH:mm';
			
			let sFormatterForParsing = 'YYYYMMDDHHmmss';
			
			// 발생일시
			let momentFrom = moment();			// 현재일시
			if ( cctv.evtOcrYmdHms !== 'Invalid date') {
				momentFrom = moment(cctv.evtOcrYmdHms, sFormatterForParsing);
			}
			
			// 검출일시
			//if (cctv.evtId.startsWith('LFP') && cctv.dtctnYmdhms != '') momentFrom = moment(cctv.dtctnYmdhms, sFormatterForParsing);
			
			let momentNow = moment();			// 현재일시
			
			// SET MIN_DATE
			let momentMinDate = momentNow.clone().subtract(oVms.playbacktimeMaxBfNow, 'minutes');
			
			let oDuration = moment.duration(momentFrom.diff(momentMinDate));
			let nSeconds = oDuration.asSeconds();
			if (nSeconds <= 0) momentMinDate = momentFrom.clone().subtract(oVms.playbacktimeMaxBf, 'minutes');
			
			// SET MAX_DATE
			let momentMaxDate = momentFrom.clone().add(oVms.playbacktimeMaxAf, 'minutes');
			if (oVms.playbacktimeMaxAf === 0) {
				// 검출일시기준 이후 최대검색시간(분) - 0이면 현재시각까지
				momentMaxDate = momentNow.clone().subtract(1, 'minutes');
			} else if (!oCommon.validate.fromToMoment(momentMinDate, momentMaxDate)) {
				// 이전 최대검색시간이 이후 최대검색시간보다 클경우
				momentMaxDate = momentMinDate.clone().add(oVms.playbacktimeMaxAf, 'minutes');
				// 이후 최대검색시간이 현재 시간보다 클경우
				if (!oCommon.validate.fromToMoment(momentMaxDate, momentNow)) momentMaxDate = momentNow.clone().subtract(1, 'minutes');
			}

			// 시작일시 종료일시 최소 ~ 최대
			console.log('-- DateTimePicker: Min[%s] ~ Max[%s]', momentMinDate.format(sFormatter), momentMaxDate.format(sFormatter));
			//console.log(`=== Datetimepicker Min ~ Max:  ${oCommon.formatter.ymdHms(momentMinDate)} ~ ${oCommon.formatter.ymdHms(momentMaxDate)}`);

			// DateTimePicker 초기화
			let sDatetimepickerSelector = '#search-deck .datetimepicker';
			const oDatetimepickerOptions = {
				format: sFormatter,
				dayViewHeaderFormat: 'YYYY-MM',
				locale: 'ko',
				minDate: momentMinDate,
				maxDate: momentMaxDate,
				ignoreReadonly: true,
				sideBySide: true,
				toolbarPlacement: 'top',
				showClose: true,
			};

			// DateTimePicker 재설정
			for (let element of $(sDatetimepickerSelector)) {
				let oDateTimePicker = $(element).data('DateTimePicker') || {};
				if (!$.isEmptyObject(oDateTimePicker)) {
					oDateTimePicker.destroy();
				}
			}
			$(sDatetimepickerSelector).datetimepicker(oDatetimepickerOptions);

			// DateTimePicker 팝업 가려지는 현상 해결.
			$(sDatetimepickerSelector).on('dp.show', (e) => {
				const $dropdownMenu = $('.bootstrap-datetimepicker-widget.dropdown-menu');
				const oOffset = $dropdownMenu.offset();
				$dropdownMenu.css({position: 'fixed', top: oOffset.top, left: oOffset.left, height: 350});
			});

			// DateTimePicker viewMode decades 비활성화.
			$(sDatetimepickerSelector).datetimepicker().on('dp.show dp.update', () => {
				$('.datepicker-years .picker-switch').removeAttr('title').on('click', e => e.stopPropagation());
			});

			// 검출일시 텍스트
			//$('#label-video-search-dtctnYmdhms').text('검출일시');
			$('#label-video-search-dtctnYmdhms').text('발생일시');
			$('#params-video-search-dtctnYmdhms').text(momentFrom.format(sFormatter));
			
			//$('#btn-cctv-add').removeClass('hide');	// 신청대상추가버튼 표시

			// 시작일시 DateTimePicker
			const $videoSearchYmdhmsFr = $('#params-video-search-ymdhms-fr');
			$videoSearchYmdhmsFr.val(moment(cctv.from).format(sFormatter));

			// 종료일시 DateTimePicker
			const $videoSearchYmdhmsTo = $('#params-video-search-ymdhms-to');
			$videoSearchYmdhmsTo.val(moment(cctv.to).format(sFormatter));

			// 재생버튼
			const $btnVideoSearch = $('#btn-video-search');
			if ($btnVideoSearch.length) {
				$btnVideoSearch.prop('disabled', false);
				$btnVideoSearch.off('click');
				$btnVideoSearch.on('click', () => {
					let $videos = $('#web-rtc-view video');
					let sFr = $videoSearchYmdhmsFr.val();
					let sTo = $videoSearchYmdhmsTo.val();
					if (oCommon.validate.fromTo(sFr, sTo, sFormatter)) {
						for (let element of $videos) {
							oVmsService.search(element, moment(sFr, sFormatter).valueOf(), moment(sTo, sFormatter).valueOf());
						}
					} else {
						oCommon.modalAlert('modal-notice', '알림', '검색 기간을 확인해 주세요.');
					}
				});
			}

		} else if ($searchDeck.length && typeof oVmsPlayer === 'undefined') {
			const sDatetimepickerSelector = '#search-deck .datetimepicker';
			for (let element of $(sDatetimepickerSelector)) {
				let oDateTimePicker = $(element).data('DateTimePicker') || {};
				if (!$.isEmptyObject(oDateTimePicker)) oDateTimePicker.destroy();
			}
			const $btnVideoSearch = $('#btn-video-search');
			if ($btnVideoSearch.length) {
				$btnVideoSearch.off('click');
				$btnVideoSearch.prop('disabled', true);
			}
		}
		return cctv;
	}
	
}
