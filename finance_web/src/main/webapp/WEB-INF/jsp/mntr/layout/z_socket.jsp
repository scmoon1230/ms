<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<script type="text/javascript" src="<c:url value='/js/socket/socket.io.js' />"></script>
<script>
	$(function() {
		oSocketIo = new socketIo();
		setTimeout(function() {
			oSocketIo.init();
		}, 1000);
	});

	function socketIo() {
		this.setTimeout = null;
		this.eventListGroup = [];
		this.eventListUser = [];

		this.init = function() {
			if (typeof localStorage == 'undefined') {
				localStorage = {};
				localStorage.setItem = function(key, value) {
					console.log('========== localStorage not support ==========');
				};

				localStorage.removeItem = function(key) {
					console.log('========== localStorage not support ==========');
				};
			}

			if (typeof oMenu == 'undefined') {
				oMenu = {
					mainMenu : '',
					subMenu : ''
				};
			}

			if (oUrl.websocket != null) {
				$.post(contextRoot + '/mntr/selectAutodisUserEvtList.json').done(function(data) {
					oSocketIo.eventListGroup = data.result;
					oSocketIo.eventListUser = [];
					if (oSocketIo.eventListGroup.length) {
						$.each(oSocketIo.eventListGroup, function(i, v) {
							if (v.userId != null) {
								oSocketIo.eventListUser.push(v);
							}
						});

						var audio = new Audio(contextRoot + '/sound/alert.mp3');
						socket = io.connect(oUrl.websocket);

						socket.on('connect', function() {
							oSocketIo.syncEventList();
							console.log('========== sockect connect ==========');
						});

						socket.on('disconnect', function() {
							console.log('========== sockect disconnect ==========');
						});

						socket.on('response', function(evt) {
							console.log('========== sockect response ==========');
							var nEvtPrgrsCd = !isNaN(evt.evtPrgrsCd) ? parseInt(evt.evtPrgrsCd) : 90;

							$.each(oSocketIo.eventListGroup, function(i, v) {
								if (v.evtId == evt.evtId && v.evtIdSubCd == evt.evtIdSubCd) {
									if (oConfigure.evtLcMoveYn == 'Y' && nEvtPrgrsCd < 30 && oMenu.subMenu == 'main') {
										localStorage.setItem(evt.evtOcrNo, JSON.stringify(evt));
										if (typeof oEvtMntr != 'undefined') oEvtMntr.grid.search();
										if (typeof oEventLayer != 'undefined') oEventLayer.redraw();

										if (oSocketIo.eventListUser.length) {
											var isExist = false;
											$.each(oSocketIo.eventListUser, function(i, v) {
												if (v.evtId == evt.evtId && v.evtIdSubCd == evt.evtIdSubCd) {
													isExist = true;
												}
											});

											if (isExist) {
												if (nEvtPrgrsCd == 10) audio.play();
												doCastNetByEvtOcrNo(evt.evtOcrNo);
												console.log('리다이렉트[X] 사운드[O] 그리드[O] 지도[O] 투망감시[O] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
											} else {
												oSocketIo.openEventList();
												console.log('리다이렉트[X] 사운드[X] 그리드[O] 지도[O] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
											}
										} else {
											if (nEvtPrgrsCd == 10) audio.play();
											doCastNetByEvtOcrNo(evt.evtOcrNo);
											console.log('리다이렉트[X] 사운드[O] 그리드[O] 지도[O] 투망감시[O] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
										}
									} else if (oConfigure.evtLcMoveYn == 'N' && nEvtPrgrsCd < 30 && oMenu.subMenu == 'main') {
										localStorage.setItem(evt.evtOcrNo, JSON.stringify(evt));
										if (typeof oEvtMntr != 'undefined') oEvtMntr.grid.search();
										if (typeof oEventLayer != 'undefined') oEventLayer.redraw();
										if (nEvtPrgrsCd == 10) audio.play();
										oSocketIo.openEventList();
										console.log('리다이렉트[X] 사운드[X] 그리드[O] 지도[O] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'N' && nEvtPrgrsCd < 90 && oMenu.subMenu == 'main') {
										localStorage.setItem(evt.evtOcrNo, JSON.stringify(evt));
										// if(리다이렉트[X] 사운드[O] 그리드[O] 지도[O] 투망감시[X]){}
										// if (typeof oMain != 'undefined') oMain.gridReload();
										if (typeof oEvtMntr != 'undefined') oEvtMntr.grid.search();
										if (typeof oEventLayer != 'undefined') oEventLayer.redraw();
										oSocketIo.openEventList();
										console.log('리다이렉트[X] 사운드[X] 그리드[O] 지도[O] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'Y' && nEvtPrgrsCd >= 90 && oMenu.subMenu == 'main') {
										localStorage.removeItem(evt.evtOcrNo);
										// if (typeof oMain != 'undefined') oMain.gridReload();
										if (typeof oEvtMntr != 'undefined') oEvtMntr.grid.search();
										if (typeof oEventLayer != 'undefined') oEventLayer.redraw();
										if (evtOcrNo == evt.evtOcrNo) stopCastNet();
										console.log('리다이렉트[X] 사운드[X] 그리드[O] 지도[O] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'N' && nEvtPrgrsCd >= 90 && oMenu.subMenu == 'main') {
										localStorage.removeItem(evt.evtOcrNo);
										// if (typeof oMain != 'undefined') oMain.gridReload();
										if (typeof oEvtMntr != 'undefined') oEvtMntr.grid.search();
										if (typeof oEventLayer != 'undefined') oEventLayer.redraw();
										if (evtOcrNo == evt.evtOcrNo) stopCastNet();
										console.log('리다이렉트[X] 사운드[X] 그리드[O] 지도[O] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'Y' && nEvtPrgrsCd < 30 && oMenu.subMenu != 'main') {
										localStorage.setItem(evt.evtOcrNo, JSON.stringify(evt));
										oSocketIo.openEventList();
										if (nEvtPrgrsCd == 10) audio.play();
										console.log('리다이렉트[X] 사운드[O] 그리드[X] 지도[X] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'Y' && nEvtPrgrsCd < 90 && oMenu.subMenu != 'main') {
										localStorage.setItem(evt.evtOcrNo, JSON.stringify(evt));
										oSocketIo.openEventList();
										console.log('리다이렉트[X] 사운드[X] 그리드[X] 지도[X] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'N' && nEvtPrgrsCd < 30 && oMenu.subMenu != 'main') {
										localStorage.setItem(evt.evtOcrNo, JSON.stringify(evt));
										console.log('리다이렉트[X] 사운드[X] 그리드[X] 지도[X] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'N' && nEvtPrgrsCd < 90 && oMenu.subMenu != 'main') {
										localStorage.setItem(evt.evtOcrNo, JSON.stringify(evt));
										console.log('리다이렉트[X] 사운드[X] 그리드[X] 지도[X] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'Y' && nEvtPrgrsCd >= 90 && oMenu.subMenu != 'main') {
										localStorage.removeItem(evt.evtOcrNo);
										console.log('리다이렉트[X] 사운드[O] 그리드[X] 지도[X] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									} else if (oConfigure.evtLcMoveYn == 'N' && nEvtPrgrsCd >= 90 && oMenu.subMenu != 'main') {
										localStorage.removeItem(evt.evtOcrNo);
										console.log('리다이렉트[X] 사운드[O] 그리드[X] 지도[X] 투망감시[X] - %s, %s, %s', evt.evtOcrNo, oConfigure.evtLcMoveYn, nEvtPrgrsCd);
									}
									oSocketIo.syncEventList();
								}
							});
						});
					}
				});
			} else {
				alert('잘못된 소켓 접속 정보입니다. 이벤트 수신을 할 수 없습니다.');
			}
			// websocket end
		};

		this.syncEventList = function() {
			if (typeof (Storage) === 'undefined') {
				alert("Web Storage를 지원하지 않는 브라우져입니다. 이벤트 수신을 할 수 없습니다.");
				return false;
			}

			var $eventReceived = $('#dropdown-menu-event-received');
			if ($eventReceived.length) {
				$eventReceived.css('min-width', $('#dropdown-event-receive').width());
				$eventReceived.empty();
				var $well = $('<div/>', {
					'class' : 'well well-sm'
				});

				/*
				var $btnSync = $('<button>', {
					'type' : 'button',
					'title' : '이벤트 알림 새로고침',
					'class' : 'btn btn-default btn-xs',
					'html' : '<i class="fas fa-sync"></i>',
					'onclick' : 'javascript:oSocketIo.syncEventList();'
				});

				var $btnTrash = $('<button>', {
					'type' : 'button',
					'title' : '이벤트 알림 비우기',
					'class' : 'btn btn-default btn-xs',
					'html' : '<i class="fas fa-trash-alt"></i>',
					'onclick' : 'javascript:oSocketIo.emptyEventList();'
				});
				 */
				var $btnTimes = $('<button>', {
					'type' : 'button',
					'title' : '이벤트 알림 삭제',
					'class' : 'btn btn-default btn-xs',
					'style' : 'margin-right: 3px;',
					'html' : '<i class="fas fa-times"></i>'
				});

				if (localStorage.length) {
					var oEvents = [];
					for (var i = 0; i < localStorage.length; i++) {
						var sKey = localStorage.key(i);
						try {
							var oItem = JSON.parse(localStorage.getItem(sKey));
							if (oItem && oItem.hasOwnProperty('evtOcrNo')) {
								oEvents.push(oItem);
							}
						} catch (e) {
							console.log(e);
						}
					}

					if (oEvents.length) {
						// 발생 시간에 따라 정렬
						oEvents.sort(function(a, b) {
							if (a.evtYmdHms > b.evtYmdHms) {
								return -1;
							} else {
								return 1
							}
						});
						// 30개가 초과하면 이전 데이터는 삭제 처리.
						if (oEvents.length > 15) {
							for (var i = 15; i < oEvents.length; i++) {
								localStorage.removeItem(oEvents[i].evtOcrNo);
							}
							oEvents = oEvents.splice(0, 15);
						}
						// 알림창에 표시해준다.
						$.each(oEvents, function(i, v) {
							var $wellC = $well.clone();
							var $span = $('<span/>', {
								'text' : v.evtNm + '(' + v.evtOcrYmdHms + ')',
								'title' : ((v.evtPlace) ? v.evtPlace + ' ' : '') + ((v.evtDtl) ? v.evtDtl : '')
							});

							var $btnTimesC = $btnTimes.clone();
							$btnTimesC.attr('onclick', 'oSocketIo.removeEventList("' + v.evtOcrNo + '");');

							$wellC.append($btnTimesC);
							$wellC.append($span);
							$eventReceived.append($('<li/>', {
								'html' : $wellC
							}));
						});
					} else {
						var $wellC = $well.clone();
						var $span = $('<span/>', {
							'text' : '수신된 이벤트가 없습니다.',
							'title' : '수신된 이벤트가 없습니다.'
						});

						$wellC.append($span);
						$eventReceived.append($('<li/>', {
							'html' : $wellC
						}));
					}
				}
			}
		};

		/*
		this.emptyEventList = function() {
			if (localStorage.length) {
				for (var i = 0; i < localStorage.length; i++) {
					var sKey = localStorage.key(i);
					try {
						var oItem = JSON.parse(localStorage.getItem(sKey));
						if (oItem && typeof oItem.evtOcrNo != 'undefined') {
							localStorage.removeItem(sKey);
						}
					}
					catch (e) {
						console.log(e);
					}
				}
				oSocketIo.syncEventList();
			}
		};
		 */

		this.removeEventList = function(evtOcrNo) {
			if (localStorage.length) {
				localStorage.removeItem(evtOcrNo);
				oSocketIo.syncEventList();
			}
		};

		this.openEventList = function() {
			var nSec = parseInt(oConfigure.setTime) * 1000;
			var hasClass = $('#dropdown-event-receive').hasClass('open');
			if (!hasClass) {
				$('#dropdown-event-receive').addClass('open');
			}

			if (oSocketIo.setTimeout != null) {
				clearTimeout(oSocketIo.setTimeout);
				oSocketIo.setTimeout = null;
			}

			oSocketIo.setTimeout = setTimeout(function() {
				$('#dropdown-event-receive').removeClass('open');
			}, nSec);
		};
	}
</script>