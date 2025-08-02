<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<script>
	$(function() {
		oWsEvt = new WsEvt();
		setTimeout(() => oWsEvt.init(), 1000);
	});

	function WsEvt() {
		let socket = null;
		let setTimeoutWsEvt = null;
	<%--let approveNotifyMuteYn = '${tvoConfig.approveNotifyMuteYn}' || 'N';
		let webSocketSoundUseYn = '${configure.webSocketSoundUseYn}';          --%>
		let approveNotifyMuteYn = '<prprts:value key='APPROVE_NOTIFY_MUTE_YN' />';
		let webSocketSoundUseYn = '<prprts:value key='WEB_SOCKET_SOUND_USE_YN' />';
		
		this.init = () => {

			let protocol = "ws";
			if ( 'Y' == oConfigure.ssl ) {
				protocol = "wss";
			}
			
			//const sUrl = 'ws://' + window.location.host + '/ws/evt.do';
			const sUrl = protocol+'://' + window.location.host + contextRoot + '/ws/evt.do';
			console.log('sUrl = '+sUrl);
			
			socket = new WebSocket(sUrl);
			socket.onopen = (event) => {
				$('#dropdown-menu-event-received').closest('ul.nav').removeClass('hide');
				oWsEvt.syncEventList();
				console.log('========== WsEvt onopen. => %o', event);
			};

			socket.onclose = (event) => {
				console.log('========== WsEvt onclose. => %o', event);
			};

			socket.onmessage = (event) => {
				console.log('========== WsEvt onmessage. => %o', event);
				oWsEvt.onmessage(event);
			};
		};

		this.onmessage = (event) => {
			const oEvent = JSON.parse(event.data);
			
			if (oEvent.evtId == 'LOGOUT') {
				oCommon.modalAlert('modal-alert-no-button', '알림', '중복로그인으로 자동로그아웃됩니다.');
				setTimeout(() => {
					location.href = contextRoot + '/wrks/lgn/logout.do';
				}, 1000 * 3);
				
			} else if (oEvent.evtId == 'APPROVE') {
				if (approveNotifyMuteYn == 'N') {
					const oAudio = new Audio(contextRoot + '/sound/approve/approve_notify.mp3');
					oAudio.loop = false;
					oAudio.play();
				}
				oWsEvt.syncEventList(oEvent.data);
				oWsEvt.openEventList();

				oCommon.modalConfirm('modal-confirm', '알림', '새로운 신청이 있습니다.\n\n조회하시겠습니까?', function () {
					setTimeout(() => {
						gotoRequestList();
					}, 1000 * 1);
				});

			} else if (oEvent.evtId == 'MASK') {
				if ( $("#btn-refresh") ) {
					setTimeout(function () {
						$("#btn-refresh").click();	// 목록 새로고침
					}, 1000);
				}
				
			}
		};

		this.syncEventList = (approve) => {
			let $eventReceived = $('#dropdown-menu-event-received');
			if ($eventReceived.length) {
				$eventReceived.empty();

				let $well = $('<div/>', {
					'class': 'well well-sm'
				});

				$.post(contextRoot + '/tvo/aprv/selectAllTypeRqstList.json', {
					'page': 1,
					'rows': 10,
					'firstRecordIndex': 0
				}).done((data) => {
					let oApproveList = data?.rows || [];

					if (typeof approve !== 'undefined' && !oApproveList.length) oApproveList.push(approve);

					if (oApproveList.length) {
					/*	oApproveList.sort((a, b) => {
							if (a.approveReqYmdHms > b.approveReqYmdHms) {
								return -1;
							} else {
								return 1;
							}
						});

						if (typeof approve !== 'undefined') {
							let isDuplicated = false;
							oApproveList.forEach((element, index, object) => {
								if (approve?.prsnId === element.prsnId) {
									isDuplicated = true;
								}
							});

							if (!isDuplicated) {
								oApproveList.unshift(approve);
								oApproveList.slice(0, 10);
							}
						}	*/
						oApproveList.forEach(element => {
							
							let sText = '';
							let sMdhm = oCommon.formatter.ymdHms(element.rqstYmdhms, 'MM-DD HH:mm');
							
							sText += sMdhm;
							sText += ' ';

							if (element.ty === 'VIEW') {
								sText += "열람";
							} else if (element.ty === 'VIEWEXTN') {
								sText += "열람연장";
							} else if (element.ty === 'OUT') {
								if (element.prgrsCd === '10') {
									sText += "반출";
								}
							} else if (element.ty === 'OUTEXTN') {
								sText += "반출연장";
							} else if (element.ty === 'APPLY') {
								sText += "사용";
							}
							sText += element.prgrsNm;
						//	if (element.approveYn === 'N')		sText += '승인 대기';
						//	else if (element.approveYn === 'Y') sText += '승인 완료';
							sText += ' ('+ element.nm+ ')';

							let $span;
							if (element.ty === 'VIEW') {
								$span = $('<span/>', {
									'html': '<a href="' + contextRoot + element.url + '" class="text-decoration-none">' + sText + '</a>',
									'title': '열람승인 바로가기'
								});
							} else if (element.ty === 'VIEWEXTN') {
								$span = $('<span/>', {
									'html': '<a href="' + contextRoot + element.url + '" class="text-decoration-none">' + sText + '</a>',
									'title': '열람연장승인 바로가기'
								});
							} else if (element.ty === 'OUT') {
								$span = $('<span/>', {
									'html': '<a href="' + contextRoot + element.url + '" class="text-decoration-none">' + sText + '</a>',
									'title': '반출승인 바로가기'
								});
							} else if (element.ty === 'OUTEXTN') {
								$span = $('<span/>', {
									'html': '<a href="' + contextRoot + element.url + '" class="text-decoration-none">' + sText + '</a>',
									'title': '반출연장승인 바로가기'
								});
							} else if (element.ty === 'APPLY') {
								$span = $('<span/>', {
									'html': '<a href="' + contextRoot + element.url + '" class="text-decoration-none">' + sText + '</a>',
									'title': '사용자승인 바로가기'
								});
							}

							let $wellC = $well.clone();
							$wellC.append($span);
							$eventReceived.append($('<li/>', {
								'html': $wellC
							}));
						});
						
					} else {
						let $wellC = $well.clone();
						$wellC.addClass('text-center');
						let $span = $('<span/>', {
							'text': '데이터가 없습니다.',
							'title': '데이터가 없습니다.'
						});

						$wellC.append($span);
						$eventReceived.append($('<li/>', {
							'html': $wellC
						}));
					}
					
					//gotoRequestList();
					
				});
			}
		};

		this.openEventList = () => {
			let nSec = oConfigure.setTime * 1000 || 5000;
			const $dropdownEventEeceive = $('#dropdown-event-receive');
			$dropdownEventEeceive.addClass('open');

			if (setTimeoutWsEvt != null) {
				clearTimeout(setTimeoutWsEvt);
				setTimeoutWsEvt = null;
			}

			setTimeoutWsEvt = setTimeout(() => $dropdownEventEeceive.removeClass('open'), nSec);
		};

		this.getWebSocketSoundUseYn = function () {
			return webSocketSoundUseYn;
		};
	}
	
	function gotoRequestList() {
		let url = '';
		//let $eventReceived = $('#dropdown-menu-event-received');
		//if ($eventReceived.length) {
			$('#dropdown-menu-event-received li div span a').each(function() {
				let href = $(this).attr('href');
				//console.log('href = '+href);
				if ( url == '' ) {
					url = href;
					//console.log('url = '+url);
				}
			});
		//}
		console.log('url = '+url);
		location.href = url;
	}
</script>
