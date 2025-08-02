/**
 * 2018-11-23
 */
$(function() {
	oVmsService = new vmsService();
	oVmsActiveX = new vmsActiveX();

	// unload 설정
	$(window).on('beforeunload', function() {
		oVmsService.destroy();
	});

	// preset move
	$('#btn-preset').click(function() {
		var sSelPreset = $('#sel-preset option:selected').val();
		oVmsService.preset(oVmsService.ptzCmd.presetMove, sSelPreset);
		console.log('-- vmsService #btn-preset.click =====');
	});

	// preset set
	$('#btn-preset-set').click(function() {
		var sSelPreset = $('#sel-preset option:selected').val();
		oVmsService.preset(oVmsService.ptzCmd.presetSet, sSelPreset);
		console.log('-- vmsService #btn-preset.click =====');
	});

	// preset clear
	$('#btn-preset-clear').click(function() {
		var sSelPreset = $('#sel-preset option:selected').val();
		oVmsService.preset(oVmsService.ptzCmd.presetClear, sSelPreset);
		console.log('-- vmsService #btn-preset.click =====');
	});

	// search speed set
	var $selSearchSpeed = $('select#params-video-search-speed');
	if ($selSearchSpeed.length) {
		$selSearchSpeed.append(oVmsCommon.createSearchSpeedOption('0.25x', '-4'));
		$selSearchSpeed.append(oVmsCommon.createSearchSpeedOption('0.5x', '-2'));
		$selSearchSpeed.append(oVmsCommon.createSearchSpeedOption('1x', '1', true));
		$selSearchSpeed.append(oVmsCommon.createSearchSpeedOption('2x', '2'));
		$selSearchSpeed.append(oVmsCommon.createSearchSpeedOption('4x', '4'));
	}

	// ptz start
	$('button.btn-ptz').mousedown(function() {
		var $btnPtz = $('button.btn-ptz');
		var nIndex = $btnPtz.index(this);
		var sSelPtz = $('#sel-ptz option:selected').val();
		switch (nIndex) {
			// moveTiltUp
			case 0:
				oVmsService.ptz(oVmsService.ptzCmd.moveTiltUp, 3);
				break;
			// moveTiltDown
			case 1:
				oVmsService.ptz(oVmsService.ptzCmd.moveTiltDown, 3);
				break;
			// movePanLeft
			case 2:
				oVmsService.ptz(oVmsService.ptzCmd.movePanLeft, 3);
				break;
			// movePanRight
			case 3:
				oVmsService.ptz(oVmsService.ptzCmd.movePanRight, 3);
				break;
			// zoomIn | focusNear
			case 4:
				if ('ZOOM' == sSelPtz) {
					oVmsService.ptz(oVmsService.ptzCmd.zoomIn, 3);
				} else if ('FOCUS' == sSelPtz) {
					oVmsService.ptz(oVmsService.ptzCmd.focusNear, 3);
				}
				break;
			// zoomOut | focusFar
			case 5:
				if ('ZOOM' == sSelPtz) {
					oVmsService.ptz(oVmsService.ptzCmd.zoomOut, 3);
				} else if ('FOCUS' == sSelPtz) {
					oVmsService.ptz(oVmsService.ptzCmd.focusFar, 3);
				}
				break;
			default:
				break;
		}
		console.log('-- vmsService btn-ptz.mousedown =====');
	});

	// ptz stop
	$('button.btn-ptz').mouseup(function() {
		var $btnPtz = $('button.btn-ptz');
		var nIndex = $btnPtz.index(this);
		var sSelPtz = $('#sel-ptz option:selected').val();
		// Pan | Tilt Stop
		if (nIndex == 0 || nIndex == 1 || nIndex == 2 || nIndex == 3) {
			oVmsService.ptz(oVmsService.ptzCmd.moveStop, 0);
		}
		// Zoom | Focus Stop
		else if (nIndex == 4 || nIndex == 5) {
			if ('ZOOM' == sSelPtz) {
				oVmsService.ptz(oVmsService.ptzCmd.zoomStop, 0);
			} else if ('FOCUS' == sSelPtz) {
				oVmsService.ptz(oVmsService.ptzCmd.focusStop, 0);
			}
		}
		console.log('-- vmsService btn-ptz.mouseup =====');
	});
});

/**
 * 함수나 변수를 추가하거나 삭제하지 않는다. 기능 추가시 다른 VMS Javascript 에도 정의 해준다.
 */
function vmsService() {
	this.isInit = false;
	this.isConnected = false;
	this.isPlay = false;
	this.isSearch = false;
	this.isPause = false;
	this.playlists = null;
	this.selected = null;
	this.callback = null;
	this.temp = null;
	this.ptzCmd = {
		// Move
		moveStop : 0x100,
		movePanLeft : 0x101,
		movePanRight : 0x102,
		moveTiltUp : 0x104,
		moveTiltDown : 0x108,
		// Zoom
		zoomStop : 0x300,
		zoomIn : 0x01000301,
		zoomOut : 0x01000302,
		// Focus
		focusStop : 0x400,
		focusFar : 0x01000401,
		focusNear : 0x01000402,
		// Preset
		presetSet : 0x600,
		presetClear : 0x601,
		presetMove : 0x602
	};

	/**
	 * @description ActiveX 초기화
	 */
	this.init = function() {
		var nWidth = '100%', nHeight = '100%', sSelector = '#view-vms';
		oVmsActiveX.init(sSelector, nWidth, nHeight);
		console.log('-- vmsService.init =====');
	};

	this.destroy = function() {
		if (oVmsService.isInit && oVmsService.isConnected) {
			if (oVmsService.isPlay) {
				oVmsActiveX.stopLiveStreaming();
			}
			if (oVmsService.isSearch) {
				oVmsActiveX.stopSearchStreaming();
			}
			// oVmsActiveX.disconnectServer();
			oVmsService.isPlay = false;
			oVmsService.isSearch = false;
			oVmsService.isConnected = false;
			oVmsService.isInit = false;
			oVmsService.selected = null;
			oVmsService.playlists = null;
			if ($('#view-vms').length == 1) {
				$('#view-vms').remove();
			}
		}
	};

	/**
	 * @description 단일 CCTV 재생.
	 * @param {string}
	 *            sFcltUid
	 */
	this.play = function(oCctvInfo) {
		if (!oVmsService.isInit) {
			oVmsService.init();
			oVmsService.callback = oVmsService.play;
			oVmsService.selected = oCctvInfo;
			console.log('-- vmsService.init =====');
			return false;
		}

		if (!oVmsService.isConnected) {
			oVmsActiveX.connectServer(oVms.ip, oVms.port, oVms.id, oVms.password);
			oVmsService.callback = oVmsService.play;
			console.log('-- vmsService.connectServer =====');
			return false;
		}

		if (oCctvInfo == null) {
			oCctvInfo = oVmsService.selected;
		}

		oVmsService.stop();

		oVmsActiveX.assignVideoInView(oCctvInfo.fcltUid, 0);

		oVmsService.isPlay = true;
		oVmsService.callback = null;
		oVmsService.temp = null;
		oVmsCommon.syncBtnPlay();

		oVmsCommon.insertViewLog(oCctvInfo.fcltId);
		console.log('-- vmsService.play =====');
	};

	this.search = function(oCctvInfo) {
		if (!oVmsService.isInit) {
			oVmsService.init();
			oVmsService.callback = oVmsService.search;
			console.log('-- vmsService.init =====');
			return false;
		}

		if (!oVmsService.isConnected) {
			oVmsActiveX.connectServer(oVms.ip, oVms.port, oVms.id, oVms.password);
			oVmsService.callback = oVmsService.search;
			console.log('-- vmsService.connectServer =====');
			return false;
		}

		var momentFrom = $('#search-input-from').val();
		// var momentTo = $('#search-input-to').val();
		var momentTo = $('#search-input-ocrTime').val();
		momentFrom = moment(momentFrom, 'YYYY-MM-DD HH:mm:ss');
		momentTo = moment(momentTo, 'YYYY-MM-DD HH:mm:ss');

		if (!momentFrom.isValid() && !momentTo.isValid()) {
			alert('올바른 날짜 형식이 아닙니다.');
			return false;
		}
		oVmsService.stop();

		var nStartDate = momentFrom.valueOf();
		var nEndDate = momentTo.valueOf();
		var sSpeed = $('select#params-video-search-speed option:selected').val();
		oVmsActiveX.assignPlaybackVideoInView(oCctvInfo.fcltUid, nStartDate, nEndDate);
		if (!isNaN(sSpeed)) {
			oVmsActiveX.setPlaybackSpeed(parseInt(sSpeed));
		}
		oVmsService.isSearch = true;
		oVmsCommon.syncBtnPlay();
		console.log('-- vmsService.search =====');
	};

	this.pause = function() {
		if (oVmsService.isPlay) {
			oVmsActiveX.stopLiveStreaming();
			oVmsService.isPlay = false;
		} else if (oVmsService.isSearch) {
			oVmsActiveX.stopSearchStreaming();
			oVmsService.temp = oVmsActiveX.getFrameTime();
			oVmsService.isSearch = false;
		}
		oVmsCommon.syncBtnPlay();
		console.log('-- vmsService.pause =====');
	};

	this.resume = function(oCctvInfo) {
		if (oVmsService.isConnected) {
			var sMode = $('input[name="vms-mode"]:checked').val();
			if ('PLAY' == sMode && !oVmsService.isPlay) {
				oVmsService.play(oCctvInfo);
			} else if ('SEARCH' == sMode && !oVmsService.isSearch) {
				oVmsService.search(oCctvInfo);
			} else {
				oVmsService.play(oCctvInfo);
			}
		}
		oVmsCommon.syncBtnPlay();
		console.log('-- vmsService.resume =====');
	};

	this.stop = function() {
		if (oVmsService.isPlay) {
			oVmsActiveX.stopLiveStreaming();
			oVmsActiveX.removeView(0, 64);
			console.log('-- vmsService.stopLiveStreaming =====');
		}

		if (oVmsService.isSearch) {
			oVmsActiveX.stopSearchStreaming();
			oVmsActiveX.removeView(0, 64);
			console.log('-- vmsService.stopSearchStreaming =====');
		}

		oVmsService.isPlay = false;
		oVmsService.isSearch = false;
		oVmsCommon.syncBtnPlay();
	};

	this.castnet = function(playlists) {
		if (!oVmsService.isInit) {
			oVmsService.init();
			oVmsService.callback = oVmsService.castnet;
			if (playlists != null) {
				oVmsService.playlists = playlists;
			}
			console.log('-- vmsService.init =====');
			return false;
		}

		if (!oVmsService.isConnected) {
			oVmsActiveX.connectServer(oVms.ip, oVms.port, oVms.id, oVms.password);
			oVmsService.callback = oVmsService.castnet;
			console.log('-- vmsService.connectServer =====');
			return false;
		}

		if (playlists == null) {
			playlists = oVmsService.playlists;
		}

		if (oVmsService.isPlay) {
			oVmsActiveX.removeView(0, 5);
			oVmsActiveX.stopLiveStreaming();
		} else {
			$viewRtsp = $('#view-rtsp');
			if ($viewRtsp.length) {
				var sBottomWidth = $('#bottom').css('width').replace(/[^-\d\.]/g, '');
				var sHandleVmsWidth = $('#handle-vms').css('width').replace(/[^-\d\.]/g, '');
				var nWidth = parseInt(sBottomWidth) - parseInt(sHandleVmsWidth) - 315;
				$('#view-vms').css('width', nWidth);
			} else {
				var sBottomWidth = $('#bottom').css('width').replace(/[^-\d\.]/g, '');
				var sHandleVmsWidth = $('#handle-vms').css('width').replace(/[^-\d\.]/g, '');
				var nWidth = parseInt(sBottomWidth) - parseInt(sHandleVmsWidth);
				$('#view-vms').css('width', nWidth);
			}
		}
		oVmsActiveX.setDivisionView(playlists.length, 1);

		// assign
		oVmsService.playlists = playlists;
		$.each(playlists, function(i, v) {
			// nStreamingPath - 0: Main Stream (FHD) , 1: Second Stream (HD)
			var nStreamingPath = 1;
			oVmsActiveX.setLiveStreamingPath(v.fcltUid, nStreamingPath);
			oVmsActiveX.assignVideoInView(v.fcltUid, i);
			if (i == 0) {
				oVmsService.selected = v;
			}
		});

		setTimeout(function() {
			// 프리셋 이동
			$.each(playlists, function(i, v) {
				if (typeof v.presetNum != 'undefined' && v.presetNum != '0' && !isNaN(v.presetNum)) {
					oVmsService.preset(oVmsService.ptzCmd.presetMove, parseInt(v.presetNum), v);
				}
				oVmsCommon.insertViewLog(v.fcltId);
			});
		}, 3000);

		oVmsService.isPlay = true;
		oVmsService.callback = null;
		console.log('-- vmsService.castnet =====');
	};

	this.assign = function(oCctvInfo, nViewIndex) {
		collapse({
			bottom : false
		});
		nViewIndex = nViewIndex - 1;
		if (!oVmsService.isInit) {
			oVmsService.init();
			oVmsService.callback = oVmsService.assign;
			oVmsService.playlists = [
					{}, {}, {}, {}, {}
			];
			oVmsService.playlists[nViewIndex] = oCctvInfo;
			console.log('-- vmsService.init =====');
			return false;
		}

		if (!oVmsService.isConnected) {
			oVmsActiveX.connectServer(oVms.ip, oVms.port, oVms.id, oVms.password);
			oVmsService.callback = oVmsService.assign;
			console.log('-- vmsService.connectServer =====');
			return false;
		}

		if (!oVmsService.isPlay) {
			setTimeout(function() {
				var sBottomWidth = $('#bottom').css('width').replace(/[^-\d\.]/g, '');
				var sHandleVmsWidth = $('#handle-vms').css('width').replace(/[^-\d\.]/g, '');
				var nWidth = parseInt(sBottomWidth) - parseInt(sHandleVmsWidth)
				$('#view-vms').css('width', nWidth);
				$('section#bottom #article-bottom').css('overflow-x', 'hidden');

				oVmsActiveX.setDivisionView(5, 1);
				$.each(oVmsService.playlists, function(i, v) {
					if (!oCommon.isEmpty(v)) {
						oVmsActiveX.setLiveStreamingPath(v.fcltUid, 1);
						oVmsActiveX.assignVideoInView(v.fcltUid, i);
						oVmsCommon.insertViewLog(v.fcltId);
					}
				});
			}, 500);
		} else if (oVmsService.isPlay) {
			if (!oCommon.isEmpty(oVmsService.playlists[nViewIndex])) {
				oVmsActiveX.removeView(nViewIndex, 1);
			}

			oVmsService.playlists[nViewIndex] = oCctvInfo;
			oVmsActiveX.setLiveStreamingPath(oCctvInfo.fcltUid, 1);
			oVmsActiveX.assignVideoInView(oCctvInfo.fcltUid, nViewIndex);
			oVmsCommon.insertViewLog(oCctvInfo.fcltId);
		}

		oVmsService.isPlay = true;
		oVmsService.callback = null;
		console.log('-- vmsService.assign =====');
	};

	this.division = function(playlists) {
		if (!oVmsService.isInit) {
			oVmsService.init();
			oVmsService.callback = oVmsService.division;
			if (playlists != null) {
				oVmsService.playlists = playlists;
			}
			console.log('-- vmsService.init =====');
			return false;
		}

		if (!oVmsService.isConnected) {
			oVmsActiveX.connectServer(oVms.ip, oVms.port, oVms.id, oVms.password);
			oVmsService.callback = oVmsService.division;
			console.log('-- vmsService.connectServer =====');
			return false;
		}

		if (playlists == null) {
			playlists = oVmsService.playlists;
		}

		if (oVmsService.isPlay) {
			oVmsActiveX.removeView(0, 64);
			oVmsActiveX.stopLiveStreaming();
		}

		var nSize = playlists.length;
		if (nSize == 1) {
			oVmsActiveX.setDivision(1);
		} else if (nSize <= 4) {
			oVmsActiveX.setDivision(4);
		} else if (nSize <= 9) {
			oVmsActiveX.setDivision(9);
		} else if (nSize <= 16) {
			oVmsActiveX.setDivision(16);
		}

		$.each(playlists, function(i, v) {
			if (typeof v.fcltUid == 'undefined') {
				alert('영상재생이 불가능한 시설물[' + i + '] 입니다.');
			} else {
				oVmsActiveX.setLiveStreamingPath(v.fcltUid, 1);
				oVmsActiveX.assignVideoInView(v.fcltUid, i);
				oVmsCommon.insertViewLog(v.fcltId);
			}
		});

		oVmsService.isPlay = true;
		oVmsService.callback = null;
		console.log('-- vmsService.division =====');
	};

	this.ptz = function(nCmd, nParam, oCctvInfo) {
		if (typeof oCctvInfo == 'undefined' || oCctvInfo == null) {
			if (oVmsService.selected == undefined || oVmsService.selected == null) {
				alert('선택된 CCTV가 없습니다.');
				return false;
			}
			oCctvInfo = oVmsService.selected;
		}
		if (typeof nParam == 'undefined' || nParam == null) {
			nParam = 3;
		}

		var result = oVmsActiveX.reqPtzCmd(nCmd, nParam, oCctvInfo.fcltUid);
		oVmsCommon.insertPtzLog(oCctvInfo.fcltId, 'ptz[' + nCmd + ']');
		console.log('-- vmsService.ptz(%s, %s) =====', oCctvInfo.fcltId, nCmd);
	};

	this.preset = function(nCmd, nParam, oCctvInfo) {
		if (typeof oCctvInfo == 'undefined' || oCctvInfo == null) {
			if (oVmsService.selected == undefined || oVmsService.selected == null) {
				alert('선택된 CCTV가 없습니다.');
				return false;
			}
			oCctvInfo = oVmsService.selected;
		}
		if (!isNaN(nParam)) {
			nParam = parseInt(nParam);
			/*
			if (oConfigure.ucpId == 'OSC') {
				nParam = nParam - 1;
			}
			*/
			if (nParam < 10) {
				var sPresetBdwStartNum = oCctvInfo.presetBdwStartNum;
				var sStartPreNum = oVms.startPreNum;
				if (isNaN(sPresetBdwStartNum)) {
					nParam = nParam + parseInt(sStartPreNum);
				} else {
					nParam = nParam + parseInt(sPresetBdwStartNum);
				}
			}

			var result = oVmsActiveX.reqPtzCmd(nCmd, nParam, oCctvInfo.fcltUid);
			oVmsCommon.insertPtzLog(oCctvInfo.fcltId, 'preset[' + nParam + ']');
			console.log('-- vmsService.preset(%s, %s) =====', oCctvInfo.fcltId, nParam);
		}
	};

	this.snapshot = function() {
		if (oVms.snapshotYn == 'Y' && oVms.snapshotPath.length && oVmsService.isInit) {
			var sPath = oVms.snapshotPath + moment().format('YYYYMMDDHHmmssSSS') + '_' + oCctvInfo.fcltId + '_' + oConfigure.userId + '.jpg';
			oVmsActiveX.captureView(oVmsService.selected.fcltUid, sPath);
			alert('화면캡쳐 실행에 성공하였습니다. \n' + sPath);
			console.log('-- vmsService.snapshot =====');
		} else {
			alert('화면캡쳐 실행에 실패했습니다.');
		}
	};

	this.snapshotToFtp = function() {
		// oVmsActiveX.ftpUpload(sFtpUrl, sFilePath);
		console.log('-- vmsService.snapshotToFtp =====');
	};

	/**
	 * @description OSD 설정.
	 * @param {number}
	 *            nPosition 11 좌상 12 좌하 21 우상 22 우하
	 * @param {number}
	 *            nViewTy
	 * @param {number}
	 *            nFontSize
	 */
	this.setOsd = function(nPosition, nViewTy, nFontSize) {

	};

	this.setViewIndex = function(nIndex) {
		oVmsService.selected = oVmsService.playlists[nIndex];
		console.log('-- vmsService.setViewIndex[' + nIndex + '] =====');
	};
}

/**
 * @description HiVe ActiveX SDK Object init을 제외한 API 문서 그대로 작성. 코드를 추가하지 않음.
 */
function vmsActiveX() {
	this.init = function(sSelector, nWidth, nHeight) {
		var ax = $('<object/>', {
			id : 'ActiveX',
			classid : 'clsid:1F8CB197-14F6-481B-8626-37975E0C8771',
			codebase : '/ActiveX/NexisoftActivexSdk.cab',
			width : nWidth,
			height : nHeight
		});

		$(sSelector).html(ax);
	};

	/**
	 * @deprecated 사용안함.
	 * @description SDK 초기화.
	 * @param {string}
	 *            sErrorMsg
	 * @returns {boolean}
	 */
	this.sdkInit = function(sErrorMsg) {
		return ActiveX.SDKInit(sErrorMsg);
	};

	/**
	 * @description 서버 접속 시도. callback event : OnServerConnect
	 * @param {string}
	 *            sAddress
	 * @param {string}
	 *            sPort
	 * @param {string}
	 *            sId
	 * @param {string}
	 *            sPassword
	 * @returns {boolean}
	 */
	this.connectServer = function(sAddress, sPort, sId, sPassword) {
		return ActiveX.HiVe_Connect(sAddress, sPort, sId, sPassword);
	};

	/**
	 * @description 인증서버에 등록되어 있는 서버 중 사용자가 지정한 서버에만 접속. callback event : OnServerConnect
	 * @param {string}
	 *            sAddress
	 * @param {string}
	 *            sPort
	 * @param {string}
	 *            sId
	 * @param {string}
	 *            sPassword
	 * @param {string}
	 *            sTargetAddress HiVe Server ip or NVR ip
	 * @param {string}
	 *            sTargetPort HiVe Server port or NVR port (default 20118)
	 * @returns {boolean}
	 */
	this.connectServerSingle = function(sAddress, sPort, sId, sPassword, sTargetAddress, sTargetPort) {
		return ActiveX.HiVe_ConnectSingle(sAddress, sPort, sId, sPassword, sTargetAddress, sTargetPort);
	}

	/**
	 * @description 서버 접속 해제. callback event : OnServerDisconnect
	 */
	this.disconnectServer = function() {
		ActiveX.HiVe_Disconnect();
	};

	/**
	 * @description Integration 서버에 등록 된 서버 개수를 리턴.
	 * @returns {number}
	 */
	this.getServerCount = function() {
		return ActiveX.HiVe_ServerCount();
	}

	/**
	 * @description Index 에 위치한 서버의 Name을 리턴.
	 * @param {number}
	 *            nIndex
	 * @returns {string}
	 */
	this.getServerName = function(nIndex) {
		return ActiveX.HiVe_ServerName(nIndex);
	}

	/**
	 * @description Index 에 위치한 서버의 Address를 리턴.
	 * @param {number}
	 *            nIndex
	 * @returns {string}
	 */
	this.getServerAddress = function(nIndex) {
		return ActiveX.HiVe_ServerAddress(nIndex);
	}

	/**
	 * @description Index 에 위치한 서버의 Port를 리턴.
	 * @param {number}
	 *            nIndex
	 * @returns {string}
	 */
	this.getServerPort = function(nIndex) {
		return ActiveX.HiVe_ServerPort(nIndex);
	}

	/**
	 * @description 접속된 서버에 등록된 모든 비디오의 name, id, address, state, server name, server address 의 정보를 가져옴. enum VideoInState { VIDEOINSTATE_NONE = 0x000, VIDEOINSTATE_VIDEOLOSS = 0x001, 연결 끊어짐 VIDEOINSTATE_DISABLED = 0x002, 사용자 설정에 의해 “사용하지 않음” 상태 VIDEOINSTATE_COVERT = 0x004, 영상 출력
	 *              금지 상태 VIDEOINSTATE_CONNECTIONFULL = 0x008, CCTV의 최대 접속 수를 초과 (RTSP) };
	 * @returns {string} XML 형식의 문자열. CF : <VideoIns><VideoIn name=”” id=”” address =”” server_name=”” server_address=””/></VideoIns>
	 */
	this.getVideoIns = function() {
		return ActiveX.HiVe_GetVideoList();
	};

	/**
	 * @description 각 비디오의 녹화된 영상의 타임라인을 요청. callback event : OnRecTimeline
	 * @param {string}
	 *            sVideoInIds ',' 쉼표를 이용해 string array 형태로 올 수 있음.
	 * @param {number}
	 *            nStartDate {Date} getTime() milliseconds.
	 * @param {number}
	 *            nEndDate {Date} getTime() milliseconds.
	 * @returns {boolean}
	 */
	this.getRecTimeline = function(sVideoInIds, nStartDate, nEndDate) {
		return ActiveX.HiVe_ReqTimeline(sVideoInIds, nStartDate, nEndDate);
	};

	/**
	 * @description 서버에 영상이 녹화된 날짜를 요청 합니다. callback event : OnRecCalendar
	 * @param {string}
	 *            sVideoInIds ',' 쉼표를 이용해 string array 형태로 올 수 있음.
	 * @param {number}
	 *            nStartDate {Date} getTime() milliseconds.
	 * @param {number}
	 *            nEndDate {Date} getTime() milliseconds.
	 * @returns {boolean}
	 */
	this.getRecCalendar = function(sVideoInIds, nStartDate, nEndDate) {
		return ActiveX.HiVe_ReqCalendar(sVideoInIds, nStartDate, nEndDate);
	};

	/**
	 * @description 이벤트 리소스 (알람, 모션, 화재)에 연동된 이벤트 발생 이후 동작에 대한 설정을 가져옴. callback event : OnHiVeEvent_EventActionCfg
	 * @returns {boolean}
	 */
	this.getEventCfgData = function() {
		return ActiveX.HiVe_ReqEventActionCfg();
	};

	/**
	 * @description HiVe VMS Server의 Resource를 가져옴. callback event : OnHiVeEvent_CfgData
	 * @returns {boolean}
	 */
	this.getCfgResource = function() {
		return ActiveX.HiVe_ReqCfgResource();
	};

	/**
	 * @description HiVe VMS Server의 현재 녹화 설정을 얻어옴. callback event : OnHiVeEvent_CfgData
	 * @returns {boolean}
	 */
	this.getCfgRecSchedule = function() {
		return ActiveX.HiVe_ReqCfgRecordSchedule();
	};

	/**
	 * @description HiVe VMS Server의 Resource 중 AlarmIn에 대한 정보를 가져옴. callback event : OnHiVeEvent_CfgData
	 * @returns {boolean}
	 */
	this.getCfgResourceAlarmIn = function() {
		return ActiveX.HiVe_ReqCfgAlarmIn();
	};

	/**
	 * @description ActiveX 화면을 분할 합니다.
	 * @param {number}
	 *            nDivision : 1, 4, 9, 16, 25, 36, 64
	 */
	this.setDivision = function(nDivision) {
		ActiveX.HiVe_ChangeDivision(nDivision);
	};

	/**
	 * @description ActiveX 화면을 분할 합니다 (N x N) 단 64개이하.
	 * @param {number}
	 *            nCol
	 * @param {number}
	 *            nRow
	 */
	this.setDivisionView = function(nCol, nRow) {
		ActiveX.HiVe_ChangeDivision2(nCol, nRow);
	};

	/**
	 * @description ActiveX 분할 화면에서 현재 페이지에서 다음페이지로 전환.
	 */
	this.setNextPage = function() {
		ActiveX.HiVe_MoveNextPage();
	};

	/**
	 * @description ActiveX 분할 화면에서 현재 페이지에서 이전페이지로 전환.
	 */
	this.setPrevPage = function() {
		ActiveX.HiVe_MovePrevPage();
	};

	/**
	 * @description 분할 화면이 전체 화면 (현재 모니터에 꽉 차게 보임)으로 전환. ESC 키를 누르면 원래 화면으로 복구.
	 */
	this.setFullView = function() {
		ActiveX.HiVe_Fullview();
	};

	/**
	 * @description 뷰에 할당된 비디오를 삭제.
	 * @param {number}
	 *            nViewIndex
	 * @param {number}
	 *            nCount
	 */
	this.removeView = function(nViewIndex, nCount) {
		ActiveX.HiVe_RemoveView(nViewIndex, nCount);
	};

	/**
	 *
	 * @description 분할 화면의 비디오를 모두 삭제.
	 */
	this.setClearView = function() {
		ActiveX.HiVe_RemoveAllView();
	};

	/**
	 * @description 분할 화면의 비디오를 선택.
	 * @param {string}
	 *            sVideoInId
	 */
	this.selectVideoIn = function(sVideoInId) {
		ActiveX.HiVe_SelectView(sVideoInId);
	};

	/**
	 * @description 비디오의 현재 프레임을 지정된 경로에 저장. (JPG) [Video Name] + [Frame Time].jpg 으로 저장됨.
	 * @param {string}
	 *            sVideoInId
	 * @param {string}
	 *            sTargetPath
	 * @returns {number} returns == 0, returns < 0 sVideoInId 확인, returns > 0 스트리밍 중, 폴더 권한, 폴더 명명 규칙 확인
	 */
	this.captureView = function(sVideoInId, sTargetPath) {
		return ActiveX.HiVe_CaptureView(sVideoInId, sTargetPath);
	}

	/**
	 * @description 영상을 뷰에 그리는 방식을 지정.
	 * @param {boolean}
	 *            isEnable TRUE 원래 비율, FALSE(default) 뷰 크기에 맞게
	 */
	this.enableMaintainAspectRatio = function(isEnable) {
		ActiveX.HiVe_MaintainAspectRatio(isEnable);
	};

	/**
	 * @description 원하는 뷰에 비디오를 할당.
	 * @param {string}
	 *            sVideoInId
	 * @param {number}
	 *            nViewIndex
	 */
	this.assignVideoInView = function(sVideoInId, nViewIndex) {
		ActiveX.HiVe_StartLive(sVideoInId, nViewIndex);
	};

	/**
	 * @description 라이브 영상 재생을 중지. AssignVideoInView 호출 전 라이브 영상 재생이 있으면 호출하기를 권장
	 */
	this.stopLiveStreaming = function() {
		ActiveX.HiVe_StopLive();
	};

	/**
	 * @description 영상의 라이브 소스를 선택.
	 * @param {string}
	 *            sVideoInIds ',' 쉼표를 이용해 string array 형태로 올 수 있음.
	 * @param {number}
	 *            nStreamPath 0: Main Stream, 1: Second Stream
	 * @returns {boolean}
	 */
	this.setLiveStreamingPath = function(sVideoInIds, nStreamPath) {
		ActiveX.HiVe_ChangeLiveSource(sVideoInIds, nStreamPath);
	};

	/**
	 * @description 원하는 뷰에 녹화된 비디오를 할당 한다. CF : nStarDate < nEndDate - Playback nStarDate > nEndDate - Playback Reverse.
	 * @param {string}
	 *            sVideoInId
	 * @param {number}
	 *            nStartDate {Date} getTime() milliseconds.
	 * @param {number}
	 *            nEndDate {Date} getTime() milliseconds.
	 */
	this.assignPlaybackVideoInView = function(sVideoInId, nStartDate, nEndDate) {
		ActiveX.HiVe_StartPlayback(sVideoInId, nStartDate, nEndDate);
	};

	/**
	 * @description 검색 영상 재생을 중지. AssignPlaybackVideoInView 호출 전 라이브 영상 재생이 있으면 호출하기를 권장
	 */
	this.stopSearchStreaming = function() {
		ActiveX.HiVe_StopPlayback();
	};

	/**
	 * @description 검색 영상의 배속를 설정.
	 * @param {number}
	 *            nSpeed : (±) 1, 2, 4, 8, 16, 32, 64
	 */
	this.setPlaybackSpeed = function(nSpeed) {
		ActiveX.HiVe_ChangePlaybackSpeed(nSpeed);
	};

	/**
	 * @description 1개의 영상을 백업 합니다. callback event : OnBackupProgress
	 * @param {string}
	 *            sVideoInIds ',' 쉼표를 이용해 string array 형태로 올 수 있음.
	 * @param {number}
	 *            nStartDate {Date} getTime() milliseconds.
	 * @param {number}
	 *            nEndDate {Date} getTime() milliseconds.
	 * @param {string}
	 *            sBackupDir
	 * @returns {boolean}
	 */
	this.startBackup = function(sVideoInIds, nStartDate, nEndDate, sBackupDir) {
		return ActiveX.HiVe_StartBackup(sVideoInIds, nStartDate, nEndDate, sBackupDir);
	};

	/**
	 * @description 1개의 영상을 백업 합니다. callback event : OnBackupProgress
	 */
	this.cancelStartBackup = function() {
		ActiveX.HiVe_StopBackup();
	};

	/**
	 * @description 현재 재생중인 영상의 시간을 얻음.
	 * @returns {number}
	 */
	this.getFrameTime = function() {
		return ActiveX.HiVe_GetFrameTime();
	};

	/**
	 * @description PTZ 컨트롤 Command를 전달. 선택 된 영상의 CCTV를 대상.
	 * @param {number}
	 *            nCmd
	 * @param {number}
	 *            nParam Command가 Move일때는 speed값, Preset일경우 Preset번호
	 * @returns {boolean}
	 */
	this.reqPtzCmd = function(nCmd, nParam, sVideoInId) {
		return ActiveX.HiVe_SendPtzCmd(nCmd, nParam, sVideoInId);
	};

	/**
	 * @description OSD 종류를 설정.
	 * @param {number}
	 *            nFlags : OSD_NONE(0x00000000) 표시안함, OSD_NAME(0x00000001) 비디오 이름만, OSD_TIME(0x00000002) 영상 시간만, OSD_STATE(0x00000004) 비디오 연결 상태 및 녹화 상태
	 */
	this.setOsdFlags = function(nFlags) {
		ActiveX.HiVe_SetOSDFlag(nFlags);
	};

	/**
	 * @description OSD 언어 설정.
	 * @param {number}
	 *            nLanguage : 0 한글 1 영문
	 */
	this.setLanguage = function(nLanguage) {
		ActiveX.HiVe_ChangeOSDLanguage(nLanguage);
	};

	/**
	 * @description 지정된 경로의 파일 1개를 지정한 FTP 서버로 전송.
	 * @param {string}
	 *            sFtpUrl : 0 FTP 서버 경로
	 * @param {string}
	 *            sFilePath : 전송 대상 파일 경로
	 * @return {number} : 0 전송 성공, 그 이외의 값은 실패 1 ftp_url 또는 file_path 를 입력하지 않은 경우 2 ftp 전송 모듈을 생성 할 수 없음, 메모리 확인 3 file_path가 존재하지 않거나 열 수 없는 파일 인 경우
	 */
	this.ftpUpload = function(sFtpUrl, sFilePath) {
		ActiveX.HiVe_FtpUpload(sFtpUrl, sFilePath);
	};
}
