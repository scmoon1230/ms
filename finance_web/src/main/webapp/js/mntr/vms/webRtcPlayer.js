$(function () {
	oVmsPlayer = new vmsPlayer();
	oCctvInfo = $('#cctvInfo').data();

	oVmsService.selected = oCctvInfo;
	oCurrentEvent.event = $('#eventInfo').data();
	oVmsPlayer.init();
});

function vmsPlayer() {
	console.log("=== oVmsCommon.vmsPlayer()");
	this.sessionId = null;
	
	this.enchainPlayTimeout = null;

	let percent25;

	let isPause = false;

	let isSliderStart = false;

	let intervalRealTime = null;

	const $sectionEvent = $('#section-event');

	this.init = function () {
	console.log("=== oVmsCommon.vmsPlayer.init()");

		let sViewRqstNo = $('#form-web-rtc input[type="hidden"][name="viewRqstNo"]').val();
		let sYmdhms = $('#form-web-rtc input[type="hidden"][name="ymdhms"]').val();			// 발생시각

		let sMode = $('#form-web-rtc input[type="hidden"][name="mode"]').val();
		if (oConfigure.cctvSearchYn == 'N') sMode = 'PLAY';

		if (sMode == 'SEARCH' && sYmdhms == '') {
			sYmdhms = moment().format('YYYYMMDDHHmmss');
		}
		
		oCurrentEvent.event = { viewRqstNo: sViewRqstNo, evtOcrYmdHms: sYmdhms }

		let sAction = $('#form-web-rtc input[type="hidden"][name="action"]').val();		// 반출대상추가여부(ADD,VIEW)
		
		oVmsPlayer.activateVmsMode(sMode, sAction);
		
		var eOutRqstCctv = opener.document.getElementById('out-rqst-cctv');		//console.log("eOutRqstCctv : "+eOutRqstCctv);
		if ( eOutRqstCctv != null ) {
			if ( "ADD" == sAction ) {
				$('#btn-cctv-add').removeClass('hide');		// 신청대상추가버튼 표시
			}
			let sModifyYn = $('#form-web-rtc input[type="hidden"][name="modifyYn"]').val();
			if ( "Y" == sModifyYn ) {
				$('#btn-cctv-modify').removeClass('hide');	// 신청대상변경버튼 표시
			}
		}

		// EVENT
		$('input[name="vms-mode"]').on('change', event => oVmsPlayer.activateVmsMode($(event.target).val(), sAction));
		$('#params-video-search-speed').on('change', () => {
			const $videoTime = $('#video-time');
			let sVideoTime = $videoTime.text() || '';
			if (sVideoTime != '') {
				//oVmsPlayer.search(moment($('#video-time').text(), 'YYYY-MM-DD HH:mm:ss').valueOf());
				oVmsPlayer.search(moment($videoTime.text(), 'YYYY-MM-DD HH:mm:ss').valueOf());
			} else {
				$('#container-step').addClass('hide');
				oVmsPlayer.play('SEARCH');
			}
		});

		// fullscreen
		$('#btn-screen').on('click', event => {
			let $btn = $(event.currentTarget);
			if ('Fullscreen' == $btn.attr('aria-label')) {
				oVmsPlayer.fullScreenWindow();
				$btn.attr('aria-label', 'Restorescreen');
				$btn.attr('title', '화면복구');
				$('span#btn-screen-icon').removeClass('glyphicon-resize-full');
				$('span#btn-screen-icon').addClass('glyphicon-resize-small');
			} else {
				oVmsPlayer.defaultScreenWindow();
				$btn.attr('aria-label', 'Fullscreen');
				$btn.attr('title', '전체화면');
				$('span#btn-screen-icon').removeClass('glyphicon-resize-small');
				$('span#btn-screen-icon').addClass('glyphicon-resize-full');
			}
		});

		$('#btn-play').on('click', (event) => {
			let $span = $(event.currentTarget).find('span');
			if ($span.hasClass('glyphicon-play')) {
				if (isPause) {
					oVmsPlayer.resume();
				} else {
					oVmsPlayer.play($('input[type="radio"][name="vms-mode"]:checked').val());
				}
			} else if ($span.hasClass('glyphicon-pause')) {
				oVmsPlayer.pause();
			}
			//console.log('-- vmsService #btn-play =====', $span[0]);
		});

		$('#btn-stop').on('click', () => {
			oVmsPlayer.stop();
			
			setTimeout(()=>{
				let new_min = $('#slider').slider('option', 'min');
				//console.log('====== new_min => '+new_min);
				$('#slider').slider('value', new_min);
			},1000);
			
		});

		$('.datetimepicker').on('dp.change', event => {
			const sFormatter = 'YYYY-MM-DD HH:mm';
			const $videoSearchYmdhmsFr = $('#params-video-search-ymdhms-fr');
			const $videoSearchYmdhmsTo = $('#params-video-search-ymdhms-to');
		
			if ($videoSearchYmdhmsFr.length && $('#slider').slider('instance') !== undefined) {
	
				let new_min = moment($videoSearchYmdhmsFr.val(), sFormatter).valueOf();
				let new_max = moment($videoSearchYmdhmsTo.val(), sFormatter).valueOf();

				//$('#slider').slider('value', moment($videoSearchYmdhmsFr.val(), sFormatter).valueOf());
				$('#slider').slider('option','min', new_min);
				$('#slider').slider('option','max', new_max);			
				//console.log('-- slider.option.min => '+$('#slider').slider('option','min'));
				
				let cur_val = $('#slider').slider('value');
				//console.log('====== new_min => '+new_min);
				//console.log('====== new_max => '+new_max);
				//console.log('====== cur_val => '+cur_val);

				if( cur_val < new_min ) {	$('#slider').slider('value', new_min);	}
				if( new_max < cur_val ) {	$('#slider').slider('value', new_max);	} 
				
				//cur_val = $('#slider').slider('value');
				//console.log('====== cur_val => '+cur_val);
			}

			this.showhideBtnCctvAdd();
		});

		$(window).bind('resize', function () {
			$('#view-vms video').css({
				'width': $('#view-vms').width(),
				'height': $('#view-vms').height()
			});
		}).trigger('resize');

		this.showhideBtnCctvAdd();

	};

	this.showhideBtnCctvAdd = function () {
		console.log('--- showhideBtnCctvAdd()');
		
		var eOutRqstCctv = opener.document.getElementById('out-rqst-cctv');		//console.log("eOutRqstCctv : "+eOutRqstCctv);
		if ( eOutRqstCctv != null ) {
			let vdoData = oVmsPlayer.cctv.getCurVodData();		// fcltId + ":" + ymdhmsFr + ":" + ymdhmsTo
			console.log('--- vdoData : '+vdoData);
			let map = new Map();
			map.set('sVdoData', vdoData);
			if(opener.oViewFirstRqst.cctv.checkIsExist(map)) {
				$("#btn-cctv-add").addClass('hide');
			} else {
				$("#btn-cctv-add").removeClass('hide');
			}
		}
	}
	/*
	this.intrerval = {
		videoTime: null
	}
	*/
	this.enchainPlay = function (sTimeout) {
		oVmsPlayer.enchainPlayTimeout = setTimeout(function () {
			function callbackConfirm() {
				let element = document.getElementById('modalVms');
				if ($(element).length) {
					$(element).modal('hide');
				}

				if (oVmsCommon.cnOsvtPlaytimeStop != null) {
					clearTimeout(oVmsCommon.cnOsvtPlaytimeStop);
					oVmsCommon.cnOsvtPlaytimeStop = null;
				}
				oVmsPlayer.enchainPlay(sTimeout);
			}

			function callbackCancle() {
				if (oVmsCommon.cnOsvtPlaytimeStop != null) {
					clearTimeout(oVmsCommon.cnOsvtPlaytimeStop);
					oVmsCommon.cnOsvtPlaytimeStop = null;
				}
				self.opener = self;
				window.close();
			}

			oCommon.modalConfirm('modal-confirm', '기본 재생시간 초과', '기본 재생시간을 초과했습니다. 계속 재생하시겠습니까?', callbackConfirm, callbackCancle);
			oVmsCommon.cnOsvtPlaytimeStop = setTimeout(function () {
				self.opener = self;
				window.close();
			}, 10000);

		}, parseInt(sTimeout) * 60 * 1000);
	};

	this.activateVmsMode = function (mode, action) {
		console.log('-- oVmsPlayer.activateVmsMode('+mode+', '+action+')');
		oVmsPlayer.stop();
		if (intervalRealTime !== null) clearInterval(intervalRealTime);
		const $videoTime = $('#video-time');
		$videoTime.text('');
		$('#container-step').addClass('hide');
		
		const $start = $('#form-web-rtc input[type="hidden"][name="start"]');
		
		if (mode == 'PLAY') {		// 현재영상
			$('input[value="SEARCH"]').prop('disabled', true);	// 과거영상을 볼 수 없도록 한다.
			$("#play-mode").addClass('hide');	// 과거영상을 볼 수 없도록 한다.
				
			$('input[value="PLAY"]').prop('checked', true);
			$('input[value="SEARCH"]').prop('checked', false);
			$('#chk-cctv-search-yn').prop('checked', false);
			$('#play-deck').removeClass('hide');
			$('#search-deck').addClass('hide');
			$('#wrapper-slider').addClass('hide');
			intervalRealTime = setInterval(() => $videoTime.text(moment().format('YYYY-MM-DD HH:mm:ss')), 1000);

		} else if (mode == 'SEARCH') {		// 과거영상
			if ( action != '' ) {	// 과거영상만 보아야 할 때
				$('input[value="PLAY"]').prop('disabled', true);	// 현재영상을 볼 수 없도록 한다.
				$("#play-mode").addClass('hide');	// 현재영상을 볼 수 없도록 한다.
			}
		
			$('input[value="PLAY"]').prop('checked', false);
			$('input[value="SEARCH"]').prop('checked', true);
			$('#chk-cctv-search-yn').prop('checked', true);
			$('#play-deck').addClass('hide');
			$('#search-deck').removeClass('hide');
			$('#wrapper-slider').removeClass('hide');

			let sFormatterForParsing = 'YYYY-MM-DD HH:mm';
			const $videoSearchYmdhmsFr = $('#params-video-search-ymdhms-fr');
			const $videoSearchYmdhmsTo = $('#params-video-search-ymdhms-to');

			let cctv = oVmsCommon.getCctvSearchInfo();			console.log("=== cctv => %o",cctv);
			
			const $from = $('#form-web-rtc input[type="hidden"][name="from"]');
			const $to = $('#form-web-rtc input[type="hidden"][name="to"]');
			const $speed = $('#form-web-rtc input[type="hidden"][name="speed"]');

			if ($from.val() != '' && $to.val() != '' && $from.val() != '0' && $to.val() != '0') {
				$videoSearchYmdhmsFr.val(moment(Number($from.val())).format(sFormatterForParsing));
				$videoSearchYmdhmsTo.val(moment(Number($to.val())).format(sFormatterForParsing));
			}

			if ($speed.val() != '') $('#params-video-search-speed').val($speed.val());

			let sFrom = $videoSearchYmdhmsFr.val();
			let sTo = $videoSearchYmdhmsTo.val();
			const momentFrom = moment(sFrom, sFormatterForParsing);
			const momentTo = moment(sTo, sFormatterForParsing);
			if (!oCommon.validate.fromToMoment(momentFrom, momentTo)) {
				oCommon.modalAlert('modal-notice', '알림', '검색 기간을 확인해 주세요.');
				return false;
			}
			oVmsPlayer.slider(momentFrom, momentTo);





			let oDateTimePickerFr = $videoSearchYmdhmsFr.closest('.datetimepicker').data('DateTimePicker');

			if ($videoSearchYmdhmsFr.val() == '' && $videoSearchYmdhmsTo.val() == '') {
				if (oDateTimePickerFr === undefined) {

					let sDatetimepickerSelector = '#search-deck .datetimepicker';
					//let sRqstDatetimepickerSelector = '#rqst-deck .datetimepicker';

					const oDatetimepickerOptions = {
						format: sFormatterForParsing,
						dayViewHeaderFormat: 'YYYY-MM',
						locale: 'ko',
						minDate: moment(cctv.from),
						maxDate: moment(cctv.to),
						ignoreReadonly: true,
						sideBySide: true,
						toolbarPlacement: 'top',
						showClose: true,
					};
	
					$(sDatetimepickerSelector).datetimepicker(oDatetimepickerOptions);
					//$(sRqstDatetimepickerSelector).datetimepicker(oDatetimepickerOptions);

					// DateTimePicker 팝업 가려지는 현상 해결.
					$(sDatetimepickerSelector).on('dp.show', () => {
						const oOffset = $('.bootstrap-datetimepicker-widget.dropdown-menu').offset();
						$('.bootstrap-datetimepicker-widget.dropdown-menu').css({position: 'fixed', top: oOffset.top, left: oOffset.left, height: 300});
					});

					// DateTimePicker viewMode decades 비활성화.
					$(sDatetimepickerSelector).datetimepicker().on('dp.show dp.update', () => {
						$('.datepicker-years .picker-switch').removeAttr('title').on('click', e => e.stopPropagation());
					});

					// 검출일시 텍스트
					$('#label-video-search-dtctnYmdhms').addClass('hide');
					$('#params-video-search-dtctnYmdhms').addClass('hide');
				}
				$videoSearchYmdhmsFr.val(moment(cctv.from).format(sFormatterForParsing));
				$videoSearchYmdhmsTo.val(moment(cctv.to).format(sFormatterForParsing));

				console.log('-- oDateTimePicker 생성됨.')
			}

		}
		
		//oVmsPlayer.play(mode);
		const nStart = isNaN($start.val()) ? 0 : parseInt($start.val());
		if (nStart !== 0) {
			oVmsPlayer.play(mode, nStart);
			$start.val('0');
		} else {
			oVmsPlayer.play(mode);
		}
		
		console.log('-- vmsPlayer.activateVmsMode =====');
	};

	this.slider = (momentFrom, momentTo) => {
		const $slider = $('#slider');
		const $step = $('button.step');
		$step.off('click');
		if ($slider.slider('instance') !== undefined) {
			$slider.slider('destroy');
			$slider.html(`<div id="custom-handle" class="ui-slider-handle"></div>`);
		}
		$slider.slider({
			min: momentFrom.valueOf(),
			max: momentTo.valueOf(),
			step: 10000,
			value: momentFrom.valueOf(),
			orientation: 'horizontal',
			animate: true,
			create: function () {
				$('#custom-handle').off('keydown');
				$step.on('click', e => {
					const $videoTime = $('#video-time');
					let sVideoTime = $videoTime.text() || '';
					if (sVideoTime !== '') {
						let momentVideoTime = moment(sVideoTime, 'YYYY-MM-DD HH:mm:ss');
						if (momentVideoTime.isValid()) {
							const $button = $(e.currentTarget);
							const nStep = $('#step-seconds option:selected').val();
							if ($button.hasClass('step-backward')) {
								momentVideoTime.subtract(nStep, 'seconds');
								// 재생시작시간 보다 작을 경우
								if (momentVideoTime.valueOf() < momentFrom.valueOf()) {
									momentVideoTime = momentFrom;
								}
							} else if ($button.hasClass('step-forward')) {
								momentVideoTime.add(nStep, 'seconds');
								// 재생종료시간 보다 클 경우
								if (momentVideoTime.valueOf() > momentTo.valueOf()) {
									momentVideoTime = momentTo;
								}
							}
							$('#slider').slider('option', {'value': momentVideoTime.valueOf(), 'disabled': true});
							oVmsPlayer.search(momentVideoTime.valueOf());
						}
					} else {
						$('#container-step').addClass('hide');
						oCommon.modalAlert('modal-notice', '알림', '현재영상시각 정보가 없습니다.');
					}
				});
			},
			slide: function (event, ui) {
				$('#video-time').text(moment(ui.value).format('YYYY-MM-DD HH:mm:ss'));
			},
			change: function (event, ui) {

			},
			start: function (event, ui) {
				isSliderStart = true;
			},
			stop: function (event, ui) {
				isSliderStart = false;
				if (momentTo.valueOf() !== ui.value) {
					$('#slider').slider('disable');
					oVmsPlayer.search(ui.value);
				} else {
					oVmsPlayer.stop(true);
				}
			},
		});
	};

	this.defaultScreenWindow = function () {
		window.resizeTo(oConfigure.popWidth, oConfigure.popHeight);
		setTimeout(function () {
			$('#view-vms video').css({
				'width': $('#view-vms').width(),
				'height': $('#view-vms').height()
			});
		}, 500);
		console.log('-- vmsPlayer.defaultScreenWindow =====');
	}

	this.fullScreenWindow = function () {
		window.moveTo(0, 0);
		window.resizeTo(screen.availWidth, screen.availHeight);
		setTimeout(function () {
			$('#view-vms video').css({
				'width': $('#view-vms').width(),
				'height': $('#view-vms').height()
			});
		}, 500);
		console.log('-- vmsPlayer.fullScreenWindow =====');
	}

	this.play = (mode, timestamp, slider) => {
		console.log(`=== oVmsPlayer.play(${mode}, ${timestamp}, ${slider})`);
		timestamp = timestamp || 0;
		if ($.isEmptyObject(slider)) slider = {from: false, to: false};
		oVmsPlayer.stop();
		const $viewVms = $('#view-vms');

		const $container = oVmsService.createVideoElement(oCctvInfo, {
			'width': $('#view-vms').width(),
			'height': $('#view-vms').height(),
			'toggle': {
				'trigger': 'hover',
			},
		});
		const elVideo = $container.find('video')[0];
		console.log('-- vmsPlayer.play(), elVideo.data().gwVmsUid => '+$(elVideo).data().gwVmsUid);

		$('#view-vms').html($container);
		oVmsPlayer.sessionId = $(elVideo).data('sessionId');
		let sOpt = 'rtptransport=udp&timeout=60&width=720&height=480';

		if (mode == 'PLAY') {
			oVmsService.play(elVideo);
			console.log('-- vmsPlayer.play %s %d %o', mode, timestamp, slider);

		} else if (mode == 'SEARCH') {
			//console.log(`=== vmsPlayer.play("SEARCH")`);
			const sFormatter = 'YYYY-MM-DD HH:mm';
			const $videoSearchYmdhmsFr = $('#params-video-search-ymdhms-fr');
			const $videoSearchYmdhmsTo = $('#params-video-search-ymdhms-to');

			let momentFr = moment($videoSearchYmdhmsFr.val(), sFormatter);
			let momentFrC = momentFr.clone();
			if (timestamp !== 0) {
				momentFrC = moment(timestamp);
				$(elVideo).data('viewLogYn', 'N');
			}

			const momentTo = moment($videoSearchYmdhmsTo.val(), sFormatter);
			const oDuration = moment.duration(momentFrC.diff(momentTo));
			let nSeconds = oDuration.asSeconds();
			if (nSeconds < 0) {
				//const nPlaybackSpeed = parseInt($('#params-video-search-speed option:selected').val());
				if (slider.from && slider.to) {
					oVmsPlayer.slider(momentFrC, momentTo);
				} else if (!slider.from && slider.to) {
					oVmsPlayer.slider(momentFr, momentTo);
				}
				//oVmsService.search(elVideo, momentFrC.valueOf(), momentTo.valueOf(), nPlaybackSpeed);
				oVmsService.search(elVideo, momentFrC.valueOf(), momentTo.valueOf());
				console.log(`========== vmsPlayer.play ${momentFrC.format(sFormatter)} ~ ${momentTo.format(sFormatter)}`);
			} else if (nSeconds === 0) {
				oVmsPlayer.stop();
				$(elVideo).css('background', 'black url(/js/mntr/vms/HIVE/eov.png) center no-repeat');
				return false;
			} else {
				oCommon.modalAlert('modal-notice', '알림', '검색 기간을 확인해 주세요.', () => oVmsPlayer.stop());
				return false;
			}
		}

		const $button = $('#btn-play');
		const $span = $button.find('span');
		$span.removeClass('glyphicon-play');
		$span.addClass('glyphicon-pause');
		$button.attr('title', '일시중지');

		oVmsPlayer.enchainPlay(oVms.playTime);
	}

	this.search = timestampFr => {
		const sFormatter = 'YYYY-MM-DD HH:mm:ss';
		const $videoSearchYmdhmsFr = $('#params-video-search-ymdhms-fr');
		const $videoSearchYmdhmsTo = $('#params-video-search-ymdhms-to');
		const oDateTimePickerFr = $videoSearchYmdhmsFr.closest('.datetimepicker').data('DateTimePicker');
		let momentTo = moment($videoSearchYmdhmsTo.val(), sFormatter);
		// 시작일시가 종료일시 보다 클 경우 시작일시에 oVms.playTime 분 만큼 종료일시를 늘려준다.
		if (timestampFr >= momentTo.valueOf()) {
			momentTo = moment(timestampFr).add(oVms.playTime, 'minutes');
		}
		// 종료일시가 최대일시를 초과할 시엔 종료일시를 최대일시로 한다.
		let isValid = oCommon.validate.fromToMoment(momentTo, oDateTimePickerFr.maxDate());
		if (!isValid) momentTo = oDateTimePickerFr.maxDate();
		$videoSearchYmdhmsTo.val(momentTo.format(sFormatter));
		oVmsPlayer.play('SEARCH', timestampFr);
	}

	this.stop = (isKeepVideoTime) => {
		console.log(`=== oVmsPlayer.stop(%s)`,isKeepVideoTime);
		if (typeof isKeepVideoTime === 'undefined') isKeepVideoTime = false;
		
		if (oVmsPlayer.sessionId != null) {
			oVmsService.disconnect(oVmsPlayer.sessionId);
			oVmsPlayer.sessionId = null;
		}

		if (oVmsPlayer.enchainPlayTimeout != null) {
			clearTimeout(oVmsPlayer.enchainPlayTimeout);
			oVmsPlayer.enchainPlayTimeout = null;
		}

		let $span = $('button#btn-play').find('span');
		if ($span.hasClass('glyphicon-pause')) {
			$span.removeClass('glyphicon-pause');
			$span.addClass('glyphicon-play');
			$('button#btn-play').attr('title', '재생');
		}

		if (!isKeepVideoTime) {
			$('#video-time').text('');
			$('#container-step').addClass('hide');
		}
			
		isPause = false;
/*
		setTimeout(()=>{
			let new_min = $('#slider').slider('option', 'min');
			//console.log('====== new_min => '+new_min);
			$('#slider').slider('value', new_min);
		},1000);
*/
	}

	this.pause = function () {
		oVmsService.pause(oVmsPlayer.sessionId);

		let $span = $('button#btn-play').find('span');
		if ($span.hasClass('glyphicon-pause')) {
			$span.removeClass('glyphicon-pause');
			$span.addClass('glyphicon-play');
			$('button#btn-play').attr('title', '재생');
		}
	}

	this.resume = function () {
		oVmsService.resume(oVmsPlayer.sessionId);
		let $span = $('button#btn-play').find('span');
		if ($span.hasClass('glyphicon-play')) {
			$span.removeClass('glyphicon-play');
			$span.addClass('glyphicon-pause');
			$('#btn-play').attr('title', '일시중지');
		}
		oVmsPlayer.enchainPlay(oVms.playTime);
	}

	this.setVideoTime = (utcTimeStamp, utcTimeString) => {
		//console.log('-- oVmsPlayer.setVideoTime() => utcTimeStamp:'+utcTimeStamp+', utcTimeString:'+utcTimeString);
		if (!isSliderStart) {
			$('#slider').slider('option', {'value': utcTimeStamp, 'disabled': false});
			$('#video-time').text(moment(utcTimeStamp).format('YYYY-MM-DD HH:mm:ss'));
			$('#container-step').removeClass('hide');
		}
	}

	this.setPause = (pause) => {
		isPause = pause;
	}




	this.cctv = {

		// oVmsPlayer.cctv.getCurVodData()
		getCurVodData: function() {
			let momentYmdhms = moment($('#params-video-search-ymdhms-fr').val(), 'YYYY-MM-DD HH:mm');
			let sYmdhmsFr = momentYmdhms.format('YYYYMMDDHHmmss');
			momentYmdhms = moment($('#params-video-search-ymdhms-to').val(), 'YYYY-MM-DD HH:mm');
			let sYmdhmsTo = momentYmdhms.format('YYYYMMDDHHmmss');
			if ( sYmdhmsFr.length < 14 ) {
				sYmdhmsFr = sYmdhmsFr+'00';		sYmdhmsTo = sYmdhmsTo+'00';
			}
			return oCctvInfo.fcltId + ":" + sYmdhmsFr + ":" + sYmdhmsTo;
		},

		// oVmsPlayer.cctv.add()
		add: function () {
			console.log('--- oVmsPlayer.cctv.add()');

			let vdoData = oVmsPlayer.cctv.getCurVodData();		// fcltId + ":" + ymdhmsFr + ":" + ymdhmsTo
			console.log('--- vdoData : '+vdoData);
				
			var eOutRqstCctv = opener.document.getElementById('out-rqst-cctv');
			var eTbody = $(eOutRqstCctv).find('tbody');				//console.log("=== eTbody.length : "+eTbody.length);
			if (eTbody.length) {

				let map = new Map();
				map.set('sVdoData', vdoData);
				if(opener.oViewFirstRqst.cctv.checkIsExist(map)) {
					//alert("이미 추가된 정보입니다.");
					
				} else {
					var eEvtYmdhms = opener.document.getElementById('view-rqst-dtl-evtYmdhms');
					var sEvtYmdhms = $(eEvtYmdhms).text();
					var momentEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss');
					sEvtYmdhms = momentEvtYmdhms.format('YYYYMMDDHHmmss');

					let map = new Map();
					map.set('sFcltId', oCctvInfo.fcltId);
					map.set('sFcltLblNm', oCctvInfo.fcltLblNm);
					map.set('sPointX', oCctvInfo.pointX);
					map.set('sPointY', oCctvInfo.pointY);
					map.set('sEvtYmdhms', sEvtYmdhms);
					map.set('sYmdhmsFr', vdoData.split(":")[1]);
					map.set('sYmdhmsTo', vdoData.split(":")[2]);
					opener.oViewFirstRqst.cctv.plus(map);
				}
				$("#btn-cctv-add").addClass('hide');
				//window.close();		// 창닫기
			}
		},

		// oVmsPlayer.cctv.modify()
		modify: function () {
			console.log('--- oVmsPlayer.cctv.modify()');

			let sFrom = $('#form-web-rtc input[type="hidden"][name="from"]').val();		// 시작시각
			let sTo = $('#form-web-rtc input[type="hidden"][name="to"]').val();		// 종료시각
			sFrom = moment(Number(sFrom)).format('YYYYMMDDHHmmss');
			sTo = moment(Number(sTo)).format('YYYYMMDDHHmmss');
			
			let vdoData = oCctvInfo.fcltId + ":" + sFrom + ":" + sTo
			console.log('--- vdoData : '+vdoData);

			var eOutRqstCctv = opener.document.getElementById('out-rqst-cctv');
			var eTbody = $(eOutRqstCctv).find('tbody');				//console.log("=== eTbody.length : "+eTbody.length);
			if (eTbody.length) {

				let map = new Map();
				map.set('sVdoData', vdoData);
				if(opener.oViewFirstRqst.cctv.checkIsExist(map)) {
					//alert("이미 추가된 정보입니다.");
					opener.oViewFirstRqst.cctv.removeVdo(vdoData);
					$("#btn-cctv-modify").addClass('hide');

				//} else {
					var eEvtYmdhms = opener.document.getElementById('view-rqst-dtl-evtYmdhms');
					var sEvtYmdhms = $(eEvtYmdhms).text();
					var momEvtYmdhms = moment(sEvtYmdhms, 'YYYY-MM-DD HH:mm:ss');
					sEvtYmdhms = momEvtYmdhms.format('YYYYMMDDHHmmss');

					vdoData = oVmsPlayer.cctv.getCurVodData();

					let map = new Map();
					map.set('sFcltId', oCctvInfo.fcltId);
					map.set('sFcltLblNm', oCctvInfo.fcltLblNm);
					map.set('sPointX', oCctvInfo.pointX);
					map.set('sPointY', oCctvInfo.pointY);
					map.set('sEvtYmdhms', sEvtYmdhms);
					map.set('sYmdhmsFr', vdoData.split(":")[1]);
					map.set('sYmdhmsTo', vdoData.split(":")[2]);
					opener.oViewFirstRqst.cctv.plus(map);
				}
				$("#btn-cctv-add").addClass('hide');
				//window.close();		// 창닫기
			}
		}

	};


	// 반출 oVmsPlayer.out
	this.out = {
		// 신청 oVmsPlayer.out.rqst
		rqst: {
			/*
			// 등록 oVmsPlayer.out.rqst.register()
			register: function () {

				if ($('#out-rqst-thirdPartyYn').is(':checked')) {
					if ($('#out-rqst-thirdPartyPw').val().trim() == '') {
						alert('제3자재생암호를 지정하세요.');
						$('#out-rqst-thirdPartyPw').focus();
						return;
					}
				} else {
					$('#out-rqst-thirdPartyPw').val('');
				}

				if (confirm('반출신청을 하시겠습니까?')) {

					var sViewRqstNo = opener.document.getElementById('view-rqst-dtl-viewRqstNo').value;
					var sVdoYmdhmsFr = $('#out-rqst-vdoYmdhmsFr').val();
					momentVdoYmdhmsFr = moment(sVdoYmdhmsFr, 'YYYY-MM-DD HH:mm:ss');
					if (momentVdoYmdhmsFr.isValid()) {
						sVdoYmdhmsFr = momentVdoYmdhmsFr.format('YYYYMMDDHHmmss');
					} else {
						// TODO Error Msg.
					}

					var sVdoYmdhmsTo = $('#out-rqst-vdoYmdhmsTo').val();
					momentVdoYmdhmsTo = moment(sVdoYmdhmsTo, 'YYYY-MM-DD HH:mm:ss');
					if (momentVdoYmdhmsTo.isValid()) {
						sVdoYmdhmsTo = momentVdoYmdhmsTo.format('YYYYMMDDHHmmss');
					} else {
						// TODO Error Msg.
					}

					//var sMaskingYn = $('#out-rqst-maskingYn').is(':checked') ? 'Y' : 'N';
					//var sThirdPartyYn = $('#out-rqst-thirdPartyYn').is(':checked') ? 'Y' : 'N';
					//var sThirdPartyPw = $('#out-rqst-thirdPartyPw').val().trim();

					// TODO 유효성 체크

					var oParams = {
						viewRqstNo: sViewRqstNo,
						//vdoYmdhmsFr : sVdoYmdhmsFr,
						//vdoYmdhmsTo : sVdoYmdhmsTo,
						//maskingYn: sMaskingYn,
						//thirdPartyYn: sThirdPartyYn,
						//thirdPartyPw: sThirdPartyPw,
						cctvList: oCctvInfo.fcltId + ":" + sVdoYmdhmsFr + ":" + sVdoYmdhmsTo
					};

					$.ajax({
						type: 'POST',
						async: false,
						dataType: 'json',
						url:contextRoot + '/tvo/rqst/outRqst/insertOutRqst.json',
						data: oParams,
						success: function (data) {
							if (data.result == '1') {
								alert('정상적으로 반출신청 되었습니다.');
							}
						},
						error: function (data, status, err) {
							console.log(data);
						}
					});
				}
			}
			*/
		}
	};


}
