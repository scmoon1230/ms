$(function() {
	oMngCmmDisplay = new mngCmmDisplay();
	oMngCmmDisplay.init();
});

function mngCmmDisplay() {
	this.popup = null;

	this.init = function() {
		collapse({
			left : true
		});
		oDiv.hideDiv();

		$('[data-toggle="tooltip"]').tooltip({
			container : 'body',
			placement : 'top'
		});
		
		$('#gisEngine').val($('#gisEngine').data('selected'));

		$('#btn-map-popup').on('click', function () {
			let sFnCallback = 'oMngCmmDisplay.callbackMapPopup';
			let sUrl = contextRoot + '/mntr/api/mapPopup.do?callback=' + sFnCallback;
			oMngCmmDisplay.popUp = oCommon.window.open(sUrl, 'MapPopup', 'status=no,width=' + 1200 + ',height=' + 800);
		});

		if(typeof oWsEvt === 'undefined') oWsEvt = { getWebSocketSoundUseYn : () => 'N' };
		
		
		$('input[name="fullScreenCloseYn"][value="'+oConfigure.fullScreenCloseYn+'"]').prop("checked",true);	// 대화면(CCTV 팝업창) 닫기
		$("#gisEngine").val(oGis.engine).prop("selected", true);					// 배경지도 선택
		$('input[name="gisTy"][value="'+oConfigure.gisTy+'"]').prop("checked",true);							// 기본도 설정
		$('input[name="menuOrdrTy"][value="'+oConfigure.menuOrdrTy+'"]').prop("checked",true);					// 메뉴유형
		$('input[name="userApproveYn"][value="'+oConfigure.userApproveYn+'"]').prop("checked",true);			// 승인
		
	};

	this.search = function() {
		var sSysId = $('#sysId option:selected').val();
		if (sSysId) {
			$.ajax({
				type : "POST",
				url : contextRoot + '/mntr/selectUmConfigInfo.json',
				dataType : "json",
				data : {
					sysId : sSysId,
					userId : sSysId
				},
				success : function(data) {
					if (typeof data.configureVO != 'undefined' && data.configureVO != null) {
						var oConfigureVO = data.configureVO;
						$('#popHeight').val(oConfigureVO.popHeight);
						$('#popWidth').val(oConfigureVO.popWidth);
						$('#cctvViewRads').val(oConfigureVO.cctvViewRads);
						$('#playTime').val(oConfigureVO.playTime);
						$('#basePlaybacktime').val(oConfigureVO.basePlaybacktime);
						$('#maxPlaybacktime').val(oConfigureVO.maxPlaybacktime);
						$('#maxAfPlaybacktime').val(oConfigureVO.maxAfPlaybacktime);
						$('#maxBfPlaybacktime').val(oConfigureVO.maxBfPlaybacktime);
						alert('조회되었습니다.');
					} else {
						alert('해당서비스의 초기값이 없습니다. \n이 서비스를 설정하려면, 해당 서비스 아이디로 접속해 주세요.');
						$('#configureVO').trigger("reset");
					}
				},
				error : function(xhr, status, error) {
					alert('조회실패.');
				}
			});
		}
	};
	
	// oMngCmmDisplay.save()
	this.save = function() {
		let maxBfPlaybacktimeNow = $('#maxBfPlaybacktimeNow').val();
		let maxBfPlaybacktime = $('#maxBfPlaybacktime').val();

		if (!isNaN(maxBfPlaybacktimeNow) && !isNaN(maxBfPlaybacktime)) {
			maxBfPlaybacktimeNow = parseInt(maxBfPlaybacktimeNow);
			maxBfPlaybacktime = parseInt(maxBfPlaybacktime);
			if (maxBfPlaybacktimeNow < maxBfPlaybacktime) {
				// [검출일시기준 이전 최대검색시간]이 [현재시각기준 이전 최대검색시간]보다 클수 없음
				oCommon.modalAlert('modal-notice', '알림', `"${$('label[for="maxBfPlaybacktime"]').html().replaceAll(':', '')}" 이 "${$('label[for="maxBfPlaybacktimeNow"]').html().replaceAll(':', '')}" 보다 클 수 없습니다.`);
				return false;
			}
		}
		
		var oParams = {
			//DSTRT_CD					: $('#dstrtCd').val(),
			
			POP_WIDTH				: $('#popWidth').val(),								// 팝업넓이
			POP_HEIGHT				: $('#popHeight').val(),							// 팝업높이
			PLAY_TIME				: $('#playTime').val(),								// 기본재생시간
			FULL_SCREEN_CLOSE_YN	: $('input[name=fullScreenCloseYn]:checked').val(),	// 대화면(CCTV 팝업창) 닫기
			
			BASE_PLAYBACKTIME		: $('#basePlaybacktime').val(),						// 발생일시기준 이전 기본시작시간
			MAX_BF_PLAYBACKTIME_NOW	: $('#maxBfPlaybacktimeNow').val(),					// 현재시각기준 이전 최대검색시간
			MAX_BF_PLAYBACKTIME		: $('#maxBfPlaybacktime').val(),					// 발생일시기준 이전 최대검색시간
			MAX_AF_PLAYBACKTIME		: $('#maxAfPlaybacktime').val(),					// 발생일시기준 이후 최대검색시간
			
			GIS_ENGINE				: $("#gisEngine option:selected").val(),			// 배경지도
			POINT_X					: $('#pointX').val(),								// 기본화면
			POINT_Y					: $('#pointY').val(),								// 기본화면
			GIS_TY					: $('input[name=gisTy]:checked').val(),				// 기본도
			
			MNTR_VIEW_LEFT			: $('#mntrViewLeft').val(),							// 좌측넓이
			MNTR_VIEW_RIGHT			: $('#mntrViewRight').val(),						// 우측넓이
			MNTR_VIEW_BOTTOM		: $('#mntrViewBottom').val(),						// 하단높이
			
			MENU_ORDR_TY			: $('input[name=menuOrdrTy]:checked').val(),		// 메뉴유형
			USER_APPROVE_YN			: $('input[name=userApproveYn]:checked').val(),		// 사용자승인여부
		};
		
		var isConfirmed = confirm('저장하시겠습니까?');
		if (isConfirmed) {
			//var isValid = validateConfigureVO(configureVO);
			//if (isValid) {
				//var sSerialized = $("#configureVO").serialize();
				$.ajax({
					type : "POST",
					async : false,
					dataType : "json",
					url : contextRoot + '/mntr/saveMngCmmDisplay.json',
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					//data : sSerialized,
					data : oParams,
					success : function(data) {
						//alert(data.status);
						if (data.status == '1') {
						//	if (oGis.engine != $('#gisEngine option:selected').val() || oWsEvt.getWebSocketSoundUseYn() != $('input[type=radio][name=webSocketSoundUseYn]:checked').val()) {
						//		oMngCmmDisplay.reload();
						//	} else {
						//		alert('저장되었습니다.');
						//	}
							//oMngCmmDisplay.reload();
							oMngCmmDisplay.goHome();
						}
					},
					error : function(xhr, status, error) {
						alert("저장에 실패하였습니다.");
					}
				});
			//}
		}
	};

	// oMngCmmDisplay.saveProjection()
	this.saveProjection = function () {
		let isConfirmed = confirm('좌표 변환 하시겠습니까?');
		if (isConfirmed) {
			//let isValid = validateConfigureVO(configureVO);
			//if (isValid) {
				//let sSerialized = $('#configureVO').serialize();
				if ($('#sysId').is(':disabled')) {
					sSerialized += '&sysId=' + $('#sysId option:selected').val();
				}
				let sUrl = contextRoot + '/mntr/saveMngCmmDisplayProjection.json';
				$.ajax({
					type: 'POST',
					url: sUrl,
					contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
					dataType: "json",
					//data: sSerialized,
					success: function (data) {
						if (data.session == 1) {
							if (oGis.engine != $('#gisEngine option:selected').val() || oWsEvt.getWebSocketSoundUseYn() != $('input[type=radio][name=webSocketSoundUseYn]:checked').val()) {
								//oMngCmmDisplay.reload();
								oMngCmmDisplay.goHome();
							} else {
								alert('저장되었습니다.');
							}
						}
					},
					error: function (xhr, status, error) {
						alert('저장에 실패하였습니다.');
					}
				});
			//}
		}
	};

	this.cancel = function() {
		$('<form/>', {
			'action' : contextRoot + '/mntr/main/main.do'
		}).appendTo(document.body).submit();
	};
	
	this.reload = function () {
//		let reloadForm = $('<form></form>');
//		reloadForm.attr("name","reloadForm");
//		reloadForm.attr("method","post");
//		reloadForm.attr("action",contextRoot + '/mntr/main/main.do');
//		reloadForm.attr("target","_blank");
//		reloadForm.append($('<input/>', {type: 'hidden', name: 'prprts', value:'reload' }));
//		reloadForm.appendTo('body');
//		reloadForm.submit();

		$.ajax({
			type: "POST",
			url:contextRoot + '/mntr/prprtsReload.json',
			dataType: "json",
			data: {
				prprts: "reload"
				, test: "test"
			},
			success: function (data) {
				if (typeof data.result != 'undefined' && data.result != null && data.result == 'ok') {
					// alert('정상적으로 처리 되었습니다.메인으로 이동합니다');
					oMngCmmDisplay.goHome();
				} else {
					alert('오류가 발생하였습니다.');
				}
			},
			error: function (xhr, status, error) {
				alert('reload fail.');
			}
		});
	};

	this.goHome = function () {
		$('<form/>', {
			'action': contextRoot + '/wrks/lgn/goHome.do'
		}).appendTo(document.body).submit();
	};

	this.callbackMapPopup = function (data) {
		oMngCmmDisplay.popUp.close();
		if (typeof data.pointX != 'undefined' && typeof data.pointY != 'undefined') {
			$('#pointX').val(data.pointX);
			$('#pointY').val(data.pointY);
		}
	};

}