const oVmsService = {
	selected: null,
	sessions: {},
	playlists: [],

	play: function (element) {
		console.log('-- oVmsService.play(), element => %o',element);
		//console.log('-- oVmsService.play(), $(element).is(\'video\') => %s',$(element).is('video'));
		
		if (typeof element != 'undefined' && $(element).is('video')) {
			let oData = $(element).data();
			//if (oConfigure.exeEnv === 'DEV')
			console.log('-- oVmsService.play(), oData => %o',oData);
			
			if (oData.gwVmsUid) {
				oData.gwVmsUid = (oData.gwVmsUid || '').replace('{', '').replace('}', '');
				let oConnInfo = (oData.vrsWebrtcAddr || '').replace('http://', '').split(':');		// WebRtc서버주소
				console.log('-- oVmsService.play(), oConnInfo => %s', oConnInfo);

				if (this.sessions[oData.sessionId]) this.sessions[oData.sessionId].Stop();
				let oSession = new HiVeWebRtcPlayer(element, oConnInfo[0], oConnInfo[1], oData.gwVmsUid, true);
				this.sessions[oData.sessionId] = oSession;

				setTimeout(() => {
					oSession.Play();
					if (oData?.viewLogYn !== 'N') {
						oVmsCommon.insertViewLog({
							fcltId: oData.fcltId,
							videoSrchTy: 0,
							videoSeaYmdHmsFr: moment().format('YYYYMMDDHHmmss'),
							videoSeaYmdHmsTo: ''
						});
					}
				}, 1000);
			} else {
				console.log('-- oVmsService.play(), required gwVmsUid');
			}
		}
	},

	//search: function (element, timeStart, timeStop, speed) {
	search: function (element, timeStart, timeStop) {
		if (typeof element != 'undefined' && $(element).is('video') && timeStart !== 0 && timeStop !== 0) {
			let oData = $(element).data();
			//if (oConfigure.exeEnv === 'DEV')
			console.log('search(), oData => %o',oData);
			
			if (oData.gwVmsUid) {
				let speed = 1;
				const $videoSearchSpeed = $('#params-video-search-speed');
				//if (speed === undefined || isNaN(speed)) {
				//	speed = 1;
					if ($videoSearchSpeed.length) speed = parseInt($videoSearchSpeed.val());
				//}

				oData.gwVmsUid = oData.gwVmsUid.replace('{', '').replace('}', '');
				let oConnInfo = oData.vrsWebrtcAddr.replace('http://', '').split(':');		// WebRtc서버주소
				console.log('-- search(), oConnInfo => %s ###', oConnInfo);
	
			/*	// 배속 ComboBox
				let oDuration = moment.duration(moment().diff(moment(timeStart)));
				let nSeconds = oDuration.asSeconds();
				// 시작일시가 현재시간의 60초 이내라면 1배속 고정(스트리밍 중지 방지)

				if (nSeconds <= 60) {
					$videoSearchSpeed.val('1');
					$videoSearchSpeed.prop('disabled', true);

					const momentTimeStop = moment().add(oVms.playTime, 'minutes');
					timeStop = momentTimeStop.valueOf();

					const $videoSearchYmdhmsTo = $('#params-video-search-ymdhms-to');
					if ($videoSearchYmdhmsTo.length) $videoSearchYmdhmsTo.val(momentTimeStop.format('YYYY-MM-DD HH:mm:ss'));
				} else {
					$videoSearchSpeed.prop('disabled', false);
				}	*/
				
				if (this.sessions[oData.sessionId]) this.sessions[oData.sessionId].Stop();

				let oSession = new HiVeWebRtcPlayer(element, oConnInfo[0], oConnInfo[1], oData.gwVmsUid, true, true, timeStart, timeStop, speed);
				
				oSession.onplaytime = (utcTimeStamp, utcTimeString) => {
					//if (typeof oVmsPlayer !== 'undefined') {
					if (typeof oVmsPlayer !== 'undefined' && oVmsPlayer.sessionId === oData.sessionId) {
						oVmsPlayer.setVideoTime(utcTimeStamp, utcTimeString);	
					} else {
						$(element).closest('.web-rtc-view-container').find('span.video-time').text(moment(utcTimeStamp).format('YYYY-MM-DD HH:mm:ss'));
					}
				};
				this.sessions[oData.sessionId] = oSession;

				setTimeout(() => {
					oSession.Play();
					console.log('-- vmsService.search(%s, %s, %s, %s, %s);', oData.sessionId, oData.gwVmsUid, timeStart, timeStop, speed);
					if (oData?.viewLogYn !== 'N') {
						oVmsCommon.insertViewLog({
							fcltId: oData.fcltId,
							videoSrchTy: 1,
							videoSeaYmdHmsFr: moment(timeStart).format('YYYYMMDDHHmmss'),
							videoSeaYmdHmsTo: moment(timeStop).format('YYYYMMDDHHmmss'),
						});
					}
				}, 1000);
			} else {
				console.log('-- vmsService.search(), required gwVmsUid');
			}
		}
	},

	assign: function (cctvInfo, viewIndex, callback) {
		const $webRtcViewContainer = $('.web-rtc-view-container');
		const $searchDeck = $('#search-deck');
		const $webRtcCtrl = $('#web-rtc-ctrl');

		let nLength = $webRtcViewContainer.length;
		if (!nLength || nLength < 9) {
			if (!nLength) {
				oVmsService.playlists = [];
				nLength = 0;
			}

			for (let i = nLength; i < 9; i++) {
				oVmsService.playlists.push({
					fcltId: 'NO_ID',
					fcltLblNm: '비어있음',
				});
			}

			console.log('-- assign(), create empty video element.');
		}

		viewIndex--;
		cctvInfo.rowNum = viewIndex;
		oVmsService.playlists[viewIndex] = cctvInfo;
		if (oCurrentEvent.cctv.mode === 'PLAY') {
			$webRtcCtrl.removeClass('hide');
			$searchDeck.addClass('hide');
		} else if (oCurrentEvent.cctv.mode === 'SEARCH') {
			$webRtcCtrl.addClass('hide');
			$searchDeck.removeClass('hide');
		}

		oVmsService.playlists.forEach((value) => oVmsService.disconnect(value.sessionId));
		oVmsService.castnet(oVmsService.playlists);
		if (typeof callback === 'function') callback();
	},

	// 영상재생하기
	castnet: function (playlist) {
		let $webRtcView = $('#web-rtc-view');
		$webRtcView.empty();
		
		let nLength = playlist.length;
		if (nLength) {
			let oCctvSearchInfo = oVmsCommon.getCctvSearchInfo();
			
			let fnCallback = () => {
				$.each(playlist, function (i, v) {
					let sMode = oCctvSearchInfo.mode || '';
				//	let sEvtOcrNo = oCctvSearchInfo.evtOcrNo || '';
					let sViewRqstNo = oCctvSearchInfo.viewRqstNo || '';
					let sDstrtCd = oCctvSearchInfo.dstrtCd || '';
					let oOpt = {
						'from': oCctvSearchInfo.from || '',
						'to': oCctvSearchInfo.to || '',
						'mode': oCctvSearchInfo.mode,
					//	'evtOcrNo': sEvtOcrNo,
						'viewRqstNo': sViewRqstNo,
						'dstrtCd': sDstrtCd,
						'onclick': `javascript:oVmsService.setViewIndex('${i}', this);`,
					//	'ondblclick': `javascript:oVmsCommon.openVmsPlayer('${v.fcltId}', '${sMode}', '${sEvtOcrNo}');`
						'ondblclick': `javascript:oVmsCommon.openVmsPlayer('${v.fcltId}', '${sMode}', '${sDstrtCd}', '${sViewRqstNo}');`
					};
					v.rowNum = (i + 1);
					let $container = oVmsService.createVideoElement(v, oOpt);
					let elVideo = $container.find('video')[0];
					v.sessionId = $(elVideo).data('sessionId');
					
					$webRtcView.append($container);

					if (oCctvSearchInfo.mode === 'PLAY') {
						oVmsService.play(elVideo);
						if (v.hasOwnProperty('presetNum') && v.presetNum !== '0') {
							oVmsService.api.preset(oVmsService.api.type.preset.move, v.presetNum, v);
						}
					} else if (oCctvSearchInfo.mode === 'SEARCH') {
						oVmsService.search(elVideo, oCctvSearchInfo.from, oCctvSearchInfo.to);
					}
				});

				if (oCctvSearchInfo.mode === 'PLAY') {
					$('#web-rtc-ctrl').removeClass('hide');
					$('#search-deck').addClass('hide');
				} else if (oCctvSearchInfo.mode === 'SEARCH') {
					$('#web-rtc-ctrl').addClass('hide');
					$('#search-deck').removeClass('hide');
				}
			}

			fnCallback();
		}
	},

	division: function (playlist) {
		let nLength = playlist.length;
		let $webRtcView = $('#view-vms');
		$webRtcView.empty();
		if (nLength) {
			$.each(playlist, function (i, v) {
				let oOpt = {};
				v.rowNum = (i + 1);
				let $container = oVmsService.createVideoElement(v, oOpt);
				let elVideo = $container.find('video')[0];
				v.sessionId = $(elVideo).data('sessionId');
				$webRtcView.append($container);
				oVmsService.play(elVideo);
			});
		}
	},

	getSession: function (sessionId) {
		if (this.sessions[sessionId]) {
			return this.sessions[sessionId];
		}
	},

	disconnect: function (sessionId) {
		if (this.sessions[sessionId]) {
			this.sessions[sessionId].Stop();
			let $container = $(this.sessions[sessionId].videoElement).closest('div');
			$(this.sessions[sessionId].videoElement).remove();
			this.sessions[sessionId] = undefined;
			$container.remove();
			console.log('-- vmsService.disconnect(%s);', sessionId);
		} else {
			console.log('-- vmsService.disconnect(%s); FAILURE', sessionId);
		}
	},

	disconnect2: function (sessionId) {
		if (this.sessions[sessionId]) {
			this.sessions[sessionId].Stop();
			let $container = $(this.sessions[sessionId].videoElement).closest('div');
			//$(this.sessions[sessionId].videoElement).remove();
			this.sessions[sessionId] = undefined;
			//$container.remove();
			console.log('-- vmsService.disconnect2(%s);', sessionId);
		} else {
			console.log('-- vmsService.disconnect2(%s); FAILURE', sessionId);
		}
	},

	pause: function (sessionId) {
		if (this.sessions[sessionId]) {
			this.sessions[sessionId].Pause();
			console.log('-- vmsService.pause(%s);', sessionId);
		} else {
			console.log('-- vmsService.pause(%s); FAILURE', sessionId);
		}
	},

	resume: function (sessionId) {
		if (this.sessions[sessionId]) {
			this.sessions[sessionId].Resume();
			console.log('-- vmsService.resume(%s);', sessionId);
		} else {
			console.log('-- vmsService.resume(%s); FAILURE', sessionId);
		}
	},

	actionPlaylist: function (playlists, action) {
		if (typeof playlists == 'undefined') {
			playlists = oVmsService.playlists;
		}

		//playlists.forEach((session) => this.sessions[session.sessionId][action]());
		$.each(playlists, function (i, v) {
			//console.log('========== vmsService.actionPlaylist: %s, %s', i, v.sessionId);
			if( v.sessionId.indexOf('NO_ID') == -1 ) {		// 영상아이디가 있을 때
				if ( 'Pause' == action ) {
					oVmsService.pause(v.sessionId);
				} else {
					oVmsService.resume(v.sessionId);
				}
			}
		});
		
		console.log('========== vmsService.actionPlaylist: %s', action);
	},

	disconnectPlaylist: function (playlists) {
		if (typeof playlists == 'undefined') {
			playlists = oVmsService.playlists;
		}

		$.each(playlists, function (i, v) {
			oVmsService.disconnect(v.sessionId);
		});
		const $webRtcViewContainers = $('#web-rtc-view .web-rtc-view-container');
		$webRtcViewContainers.remove();
		oVmsService.playlists = [];
		olSwipMap.util.removeFeaturesByProperties(olMap.layers.vector, 'assignIndex');
		console.log('-- vmsService.disconnectPlaylist: %d, %d', $webRtcViewContainers.length, oVmsService.playlists.length);
	},

	disconnectPlaylist2: function (playlists) {
		if (typeof playlists == 'undefined') {
			playlists = oVmsService.playlists;
		}

		$.each(playlists, function (i, v) {
			oVmsService.disconnect2(v.sessionId);
		});
		//const $webRtcViewContainers = $('#web-rtc-view .web-rtc-view-container');
		//$webRtcViewContainers.remove();
		//oVmsService.playlists = [];
		//olSwipMap.util.removeFeaturesByProperties(olMap.layers.vector, 'assignIndex');
		//console.log('-- vmsService.disconnectPlaylist2: %d, %d', $webRtcViewContainers.length, oVmsService.playlists.length);
	},

	disconnectAll: function () {
		$.each(Object.values(oVmsService.sessions), function (i, v) {
			if (typeof v != 'undefined') {
				v.Stop();
				let $container = $(v.videoElement).closest('div');
				$container.remove();
				$(v.videoElement).remove();
				v = undefined;
			}
		});

		const $webRtcViewContainers = $('#web-rtc-view .web-rtc-view-container');
		$webRtcViewContainers.remove();
		oVmsService.playlists = [];
		olSwipMap.util.removeFeaturesByProperties(olMap.layers.vector, 'assignIndex');
		console.log('-- vmsService.disconnectAll: %d, %d', $webRtcViewContainers.length, oVmsService.playlists.length);
	},

	setViewIndex: function (idx, object) {
		let oCctvInfo = null;
		if (typeof oMntrLineString != 'undefined' && oMntrLineString.cctv.playlists != null) {
			oCctvInfo = oMntrLineString.cctv.playlists[idx];
			$('#web-rtc-view-right video').removeClass('selected');
			$('#web-rtc-view-right span.web-rtc-view-title').removeClass('selected');
		} else if (oVmsService.playlists.length) {
			oCctvInfo = oVmsService.playlists[idx];
		}

		let sFcltKndDtlCd = oCctvInfo.fcltKndDtlCd || 'RT';
		let oCctvSearchInfo = oCurrentEvent.cctv;
		if (oCctvSearchInfo.mode == 'PLAY') {
			if (sFcltKndDtlCd == 'FT') {
				$('#web-rtc-ctrl .ui-mask').removeClass('hide');
				$('#web-rtc-ctrl .ui-mask span').html('고정형CCTV<br>사용불가');
			} else {
				$('#web-rtc-ctrl .ui-mask').addClass('hide');
			}
		}

		$('#web-rtc-view video').removeClass('selected');
		$('#web-rtc-view span.web-rtc-view-title').removeClass('selected');

		if (oCctvInfo != null && oCctvInfo.hasOwnProperty('sessionId')) {
			let nSessionId = oCctvInfo.sessionId;
			let elVideo = this.sessions[nSessionId].videoElement;
			$(elVideo).addClass('selected');
			$(elVideo).closest('div.web-rtc-view-container').find('span.web-rtc-view-title').addClass('selected');
			if (oDivPlaylist && $(object).is('video')) {
				oCctvInfo.idx = idx;
				oDivPlaylist.select(oCctvInfo);
			} else {
				oCctvInfo.idx = idx;
			}

			console.log('-- vmsService.setViewIndex(%o);', oCctvInfo);
			this.selected = oCctvInfo;
		}
	},

	createVideoElement: function (data, option) {
		console.log('-- vmsService.createVideoElement() data=> %o', data);
		console.log('-- vmsService.createVideoElement() option=> %o', option);
		
		let $video = $('<video/>', {
			'style': 'background: #000; margin-right: 1px; border: 1px solid #000;'
		});

		let sFcltId = 'NO_ID';
		let sFcltLblNm = 'NO_TITLE';

		if (data.hasOwnProperty('fcltLblNm')) {	sFcltLblNm = data.fcltLblNm;	}
		if (data.hasOwnProperty('fcltId')) {	sFcltId = data.fcltId;		}

		$video[0].title = sFcltLblNm;
		$video[0].muted = true;
		// $video[0].disablePictureInPicture = true;
		if (typeof option !== 'undefined') {
			if (typeof option.onclick !== 'undefined' && sFcltId !== 'NO_ID') {
				$video.attr('onclick', option.onclick);
			}

			if (typeof option.ondblclick !== 'undefined' && sFcltId !== 'NO_ID') {
				$video.attr('ondblclick', option.ondblclick);
			}
		}

		let nId = Date.now();
		data.sessionId = sFcltId + '_' + (nId + Math.floor((Math.random() * 10000)));

		$video.data(data);
		// console.log('-- vmsService.createVideoElement: %o, %o', data, option);

		let $container = $('<div/>', {		'class': 'web-rtc-view-container',		});
		let $videoTitle = $('<span/>', {	'class': 'web-rtc-view-title text-center text-ellipsis',		});

		let sPrefix = '';

		if (data.hasOwnProperty('rowNum')) {		sPrefix += data.rowNum + '. ';		}
		if (data.hasOwnProperty('fcltKndDtlNm')) {	sPrefix += '(' + data.fcltKndDtlNm + ')';		}

		sPrefix += sFcltLblNm;

		$videoTitle.html(sPrefix);
		$container.append($videoTitle);
		$container.append($video);

		if (sFcltId !== 'NO_ID') {
			let $containerButtons = $('<div/>', {
				'class': 'web-rtc-view-container-buttons',
			});

			let sAddr = data.roadAdresNm || '';
			if (data.lotnoAdresNm) sAddr += `(${data.lotnoAdresNm})`;
			if (sAddr === '') sAddr = '주소 정보 없음'
			$containerButtons.append($('<button/>', {
				'type': 'button',
				'class': 'btn btn-default btn-xs summary',
				'title': `<table class="table table-condensed margin-zero"><tr><th style="min-width: 70px;">아이디</th><td class="text-left">${data.fcltId}</td></tr><tr><th>용도</th><td class="text-left">${data.fcltUsedTyNm}</td></tr><tr><th>주소</th><td class="text-left">${sAddr}</td></tr><tr><th>위치</th><td class="text-left">경도: ${data.pointX}, 위도: ${data.pointY}</td></tr></table>`,
				'html': '<i class="fas fa-info-circle"></i>',
				'data-toggle': 'tooltip',
				'data-placement': 'auto',
			}));
			let trigger = option?.toggle?.trigger || 'hover';
			$containerButtons.find('button[data-toggle="tooltip"]').tooltip({
				container: 'body', 
				trigger: trigger, 
				html: true, 
				sanitize: false
			});
			
			if ($('#article-bottom').length) {
				$containerButtons.append($('<button/>', {
					'type': 'button',
					'class': 'btn btn-default btn-xs map',
					'title': '맵 이동',
					'html': '<i class="fas fa-map-marker-alt"></i>',
				}));

				$containerButtons.append($('<button/>', {
					'type': 'button',
					'class': 'btn btn-default btn-xs popup',
					'title': '새창 팝업',
					'html': '<i class="fas fa-external-link-alt"></i>',
				}));
			}

			$containerButtons.find('button').on('click', (event) => {
				event.preventDefault();
				event.stopPropagation();
				if ($(event.currentTarget).hasClass('map')) {
					if (data.pointX && data.pointY && typeof olSwipMap !== 'undefined') {
						olSwipMap.point.set([data.pointX, data.pointY], 'EPSG:4326', 'FCLT', true, false, true);
					}
				} else if ($(event.currentTarget).hasClass('popup')) {
					//oVmsCommon.openVmsPlayer(data.fcltId, option.mode, option.evtOcrNo, 'Y', data);
					oVmsCommon.openVmsPlayer(data.fcltId, option.mode, option.dstrtCd, option.viewRqstNo, data);
				}
			});

			if (typeof oVmsPlayer === 'undefined' && option?.from !== ''&& option?.to !== '') $containerButtons.prepend('<span class="video-time"></span>');
			$containerButtons.appendTo($container);
			
			// 반출대상추가
			$containerButtons.append($('<button/>', {
				'type': 'button',
				'class': 'btn btn-default btn-xs pull-right hide extra',
				'title': '반출대상추가',
				'html': '',
				'data-fclt-id': data.fcltId,
			}));
			
		}
		return $container;
	},

	api: {
		type: {
			ptz: {
				pt: 'pt',
				zoom: 'zoom',
				focus: 'focus'
			},
			preset: {
				move: 'move',
				set: 'set'
			}
		},
		cmd: {
			ptz: {
				leftUp: 1,
				up: 2,
				rightUp: 3,
				right: 4,
				rightDown: 5,
				down: 6,
				leftDown: 7,
				left: 8,
				stop: 0,
				zoomIn: 1,
				zoomOut: 2,
				zoomStop: 0,
				focusNear: 1,
				focusFar: 2,
				focusStop: 0,
			}
		},
		spd: 5,
		duration: 0,
		ptz: function (type, cmd) {
			let oData = null;
			if (oVmsService.selected != null) {
				oData = oVmsService.selected;
			} else {
				let $videos = $('#web-rtc-view video');
				if ($videos.length == 1) {
					let elVideo = $('#web-rtc-view video')[0];
					oData = $(elVideo).data();
					oVmsService.selected = oData;
				} else {
					alert('선택된 CCTV가 없습니다.');
					return false;
				}
			}

			oData.gwVmsUid = oData.gwVmsUid.replace('{', '').replace('}', '');

			$.ajax({
				type: 'POST',
				url:contextRoot + '/mntr/vms/ptz.json',
				data: {
					type: type,
					cmd: cmd,
					uid: oData.gwVmsUid,
					ptzApiAddr: oData.ptzApiAddr,
					ptzApiKey: oData.ptzApiKey,
					ptzApiSysCd: oData.ptzApiSysCd,
					ptzAuthYn: oData.ptzAuthYn,
					dstrtCd: oData.dstrtCd,
					spd: this.spd,
					duration: this.duration
				},
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					if (typeof data.result != 'undefined') {
						if (data.result.code == '000') {
							oVmsCommon.insertPtzLog(oData.fcltId, type + '[' + cmd + ']');
							console.log('-- vmsService.api.ptz(%s, %d, %s)', type, cmd, oData.gwVmsUid);
							console.log(data.result);
						} else {
							console.log('-- vmsService.api.ptz(%s, %s)', data.result.code, data.result.msg);
						}
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {

				}
			});
		},
		preset: function (type, presetNo, cctv) {
			let oData = {};

			if (typeof cctv != 'undefined') {
				oData = cctv;
			} else {
				if (oVmsService.selected != null) {
					oData = oVmsService.selected;
				} else {
					let $videos = $('#web-rtc-view video');
					if ($videos.length == 1) {
						let elVideo = $('#web-rtc-view video')[0];
						oData = $(elVideo).data();
						oVmsService.selected = oData;
					} else {
						alert('선택된 CCTV가 없습니다.');
						return false;
					}
				}
			}

			if (type == oVmsService.api.type.preset.move) {
				let nPresetNo = parseInt(presetNo);
				if (nPresetNo < 10) presetNo = nPresetNo + parseInt(oData.presetBdwStartNum);
			} else if (type == oVmsService.api.type.preset.set) {
				presetNo = parseInt(presetNo);
			}

			oData.gwVmsUid = oData.gwVmsUid.replace('{', '').replace('}', '');

			$.ajax({
				type: 'POST',
				url:contextRoot + '/mntr/vms/preset.json',
				data: {
					type: type,
					presetNo: presetNo,
					uid: oData.gwVmsUid,
					ptzApiAddr: oData.ptzApiAddr,
					ptzApiKey: oData.ptzApiKey,
					ptzApiSysCd: oData.ptzApiSysCd,
					ptzAuthYn: oData.ptzAuthYn,
					dstrtCd: oData.dstrtCd,
				},
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					if (typeof data.result != 'undefined') {
						if (data.result.code == '000') {
							oVmsCommon.insertPtzLog(oData.fcltId, type + '[' + presetNo + ']');
							console.log('-- vmsService.api.preset(%s, %d, %s)', type, presetNo, oData.gwVmsUid);
						} else {
							console.log('-- vmsService.api.ptz(%o)', data);
						}
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
					console.log(data);
				}
			});
		}
	}
}
