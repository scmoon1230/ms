<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<!-- <div id ="div-evt-vms"> -->
<c:set var="vmsWebRtcYn" scope="request"><prprts:value key="VMS_WEBRTC_YN"/></c:set>
<%--
<c:set var="vmsWebRtcYn" scope="request">Y</c:set>
<c:set var="vmsPresetYn" scope="request"><prprts:value key="VMS_PRESET_YN"/></c:set>
--%>
<c:set var="vmsPresetYn" scope="request">Y</c:set>

<c:choose>

	<c:when test="${vmsWebRtcYn eq 'Y'}">
	<%--<c:if test="${configure.ptzCntrTy ne 'VIEW'}">
			<div class="web-rtc-ctrl" id="web-rtc-ctrl" style="min-width: 141px; width: 141px; display: inline-block; vertical-align: top;">
				<div class="ui">
					<div class="control">
						<c:if test="${configure.ptzCntrTy ne 'PRESET'}">
							<button type="button" class="btn-handle up">상</button>
							<button type="button" class="btn-handle down">하</button>
							<button type="button" class="btn-handle left">좌</button>
							<button type="button" class="btn-handle right">우</button>
							<button type="button" class="btn-handle zoomOut">축소</button>
							<button type="button" class="btn-handle zoomIn">확대</button>
						</c:if>
					</div>
					<div class="select">
						<div class="header">
							<c:if test="${configure.ptzCntrTy ne 'PRESET'}">
								FOCUS
								<button type="button" class="btn-handle prev focusNear">이전</button>
								<button type="button" class="btn-handle next focusFar">다음</button>
							</c:if>
						</div>
						<ul><li><button type="button" class="btn-preset">8</button></li>
							<li><button type="button" class="btn-preset">1</button></li>
							<li><button type="button" class="btn-preset">2</button></li>
							<li><button type="button" class="btn-preset">7</button></li>
							<li><button type="button" class="btn-preset center">0</button></li>
							<li><button type="button" class="btn-preset">3</button></li>
							<li><button type="button" class="btn-preset">6</button></li>
							<li><button type="button" class="btn-preset">5</button></li>
							<li><button type="button" class="btn-preset">4</button></li>
						</ul>
					</div>
				</div>
				<div class="ui-mask hide"><span>과거영상 사용불가</span></div>
			</div>
		</c:if>--%>
		<c:if test="${configure.cctvSearchYn eq 'Y'}">
			<table class="table table-condensed table-bordered hide" id="search-deck">
			<%--<colgroup>
					<col style="width: 80px;">
					<col style="width: 180px;">
				</colgroup>--%>
				<tbody>
				<tr class="tr-video-search">
					<th id="label-video-search-dtctnYmdhms" style="border: 0;">검출일시</th><!-- 발생일시 -->
					<td id="params-video-search-dtctnYmdhms" style="border: 0;"></td>
				</tr>
			<%--<tr class="tr-video-search"><td colspan="2">
						<div id="wrapper-slider">
							<div id="slider">
								<div id="custom-handle" class="ui-slider-handle"></div>
							</div>
						</div>
					</td>
				</tr>--%>
				<tr class="tr-video-search">
					<!-- <th>시작일시</th> -->
					<td style="border: 0;">
						<div class="input-group date datetimepicker">
							<input type="text" class="form-control input-xs" id="params-video-search-ymdhms-fr" name="params-video-search-ymdhms-fr" size="16"
								   maxlength="16" title="시작일시" placeholder="시작일시" readonly="readonly">
							<span class="input-group-addon input-group-addon-xs"><span class="glyphicon glyphicon-calendar" title="시작일시"></span></span>
						</div>
					</td>
				</tr>
				<tr class="tr-video-search">
					<th style="border: 0;"> ~ </th>
					<!-- <th>종료일시</th> -->
					<td style="border: 0;">
						<div class="input-group date datetimepicker">
							<input type="text" class="form-control input-xs" id="params-video-search-ymdhms-to" name="params-video-search-ymdhms-to" size="16"
								   maxlength="16" title="종료일시" placeholder="종료일시" readonly="readonly">
							<span class="input-group-addon input-group-addon-xs"><span class="glyphicon glyphicon-calendar" title="종료일시"></span></span>
						</div>
					</td>
				</tr>
				<c:if test="${not empty playbackSpeedList and playbackSpeedList.size() > 1}">
					<tr class="tr-video-search">
						<th style="border: 0;">배속</th>
						<td style="border: 0;">
							<select class="form-control input-xs" id="params-video-search-speed" title="배속" onchange="$('#btn-video-search').trigger('click')">
							<c:forEach var="option" items="${playbackSpeedList}">
								<c:choose>
									<c:when test="${option eq playbackSpeedDefault}">
										<option value="${option}" selected="selected">${option}</option>
									</c:when>
									<c:otherwise>
										<option value="${option}">${option}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
						</td>
					</tr>
				</c:if>
			<%--<tr class="tr-video-search"><th>CCTV선택</th>
					<td><label class="tooltip-personEventMntr" data-toggle="tooltip" data-placement="top" title="선택감시 버튼이 활성화 됩니다.">
							<input type="checkbox" id="chk-designation" name="chk-designation">
							<span>선택재생</span>
						</label>
					</td>
				</tr>--%>
				<tr class="tr-video-search">
					<td style="border: 0;">
						<button type="button" class="btn btn-default btn-xs" id="btn-video-search" title="재생">
							<i class="fas fa-play"></i></button>
						<button type="button" class="btn btn-default btn-xs" id="btn-video-pause" title="일시중지"
							 onclick="oEvtVms.pause();"><i class="fas fa-pause"></i></button>
						<button type="button" class="btn btn-default btn-xs" id="btn-stop" title="정지"
							 onclick="oEvtVms.stop();"><i class="fas fa-stop"></i></button>
							 
						<button type="button" class="btn btn-default btn-xs" id="btn-fullScreen" title="화면전환"
							 onclick="oEvtVms.fullScreen();"><i class="fas fa-expand"></i></button>
						<button type="button" class="btn btn-default btn-xs hide" id="btn-defaultScreen" title="화면전환"
							 onclick="oEvtVms.defaultScreen()();"><i class="fas fa-compress"></i></button>
							 
						<button type="button" class="btn btn-default btn-xs" title="닫기"
							 onclick="oEvtVms.close()"><i class="fas fa-times"></i></button>
					</td>
				</tr>
				</tbody>
			</table>
		</c:if>
	
		<!-- <div id="web-rtc-view-title"></div> -->
		<div id="web-rtc-view"></div>

		<script>
			oEvtVms = {
				init: function () {
					// CASE 1 .col을 하나만 남긴다.
					const $cols = $('#article-bottom .col');
					if (oMenu.mainMenu == 'fclt' && oMenu.subMenu == 'fclt') {
						$($cols[0]).css('width', '600px');
					} else if (oMenu.mainMenu == 'mng' && oMenu.subMenu == 'mngPreset') {
						$($cols[0]).css('width', '600px');
					} else if (oMenu.mainMenu == 'mng' && oMenu.subMenu == 'mngRotation') {
						$($cols[0]).css('width', '600px');
					} else {
						const $cols = $('#article-bottom .col');
						$.each($cols, (index, col) => {
							if (index != 0) {
								$(col).hide();
							} else {
								if ($('#search-deck').length) {
									if (!$('#search-deck').hasClass('hide')) $(col).css('width', 'calc(100% - 260px)');
								} else {
									$(col).css('width', '100vw');
								}
							}
						});
					}

					if ($('#web-rtc-ctrl').length) {
						const $btnHandle = $('#web-rtc-ctrl .btn-handle');
						const $btnPreset = $('#web-rtc-ctrl .btn-preset');
						$btnHandle.on('mousedown', event => {
							let nIndex = $btnHandle.index(event.currentTarget);
							switch (nIndex) {
								case 0:
									oVmsService.api.ptz(oVmsService.api.type.ptz.pt, oVmsService.api.cmd.ptz.up);
									break;
								case 1:
									oVmsService.api.ptz(oVmsService.api.type.ptz.pt, oVmsService.api.cmd.ptz.down);
									break;
								case 2:
									oVmsService.api.ptz(oVmsService.api.type.ptz.pt, oVmsService.api.cmd.ptz.left);
									break;
								case 3:
									oVmsService.api.ptz(oVmsService.api.type.ptz.pt, oVmsService.api.cmd.ptz.right);
									break;
								case 4:
									oVmsService.api.ptz(oVmsService.api.type.ptz.zoom, oVmsService.api.cmd.ptz.zoomOut);
									break;
								case 5:
									oVmsService.api.ptz(oVmsService.api.type.ptz.zoom, oVmsService.api.cmd.ptz.zoomIn);
									break;
								case 6:
									oVmsService.api.ptz(oVmsService.api.type.ptz.focus, oVmsService.api.cmd.ptz.focusNear);
									break;
								case 7:
									oVmsService.api.ptz(oVmsService.api.type.ptz.focus, oVmsService.api.cmd.ptz.focusFar);
									break;
								default:
									break;
							}
						});

						$btnHandle.on('mouseup', (event) => {
							let nIndex = $btnHandle.index(event.currentTarget);
							if (nIndex == 0 || nIndex == 1 || nIndex == 2 || nIndex == 3) {
								oVmsService.api.ptz(oVmsService.api.type.ptz.pt, oVmsService.api.cmd.ptz.stop);
							} else if (nIndex == 4 || nIndex == 5) {
								oVmsService.api.ptz(oVmsService.api.type.ptz.zoom, oVmsService.api.cmd.ptz.zoomStop);
							} else if (nIndex == 6 || nIndex == 7) {
								oVmsService.api.ptz(oVmsService.api.type.ptz.focus, oVmsService.api.cmd.ptz.focusStop);
							}
						});

						$btnPreset.on('click', (event) => {
							let nPresetNo = parseInt($(event.currentTarget).text());
							oVmsService.api.preset(oVmsService.api.type.preset.move, nPresetNo);
						});
					} else {
						//  $('div#web-rtc-view').css('width', '100%');
					}

				},
				pause : () => {
					//console.log('evtVms, pause()');
					const $btnVideoPause = $('#btn-video-pause');
					let sBtnResume = 'btn-default';
					let sBtnPause = 'btn-warning';
					let sAction;
					// Resume
					if ($btnVideoPause.hasClass(sBtnPause)) {
						$btnVideoPause.removeClass(sBtnPause).addClass(sBtnResume);
						sAction = 'Resume';
						//oVmsService.setTimeout();
					}
					// Pause
					else {
						$btnVideoPause.removeClass(sBtnResume).addClass(sBtnPause);
						sAction = 'Pause';
					}
					oVmsService.actionPlaylist(undefined, sAction);
				},
				stop : () => {
					//console.log('evtVms, stop()');
					oVmsService.disconnectPlaylist2();
				},
				close : () => {
					oEvtVms.defaultScreen();
					collapse({bottom: true, left: false}, oVmsService.disconnectPlaylist);
				},
				fullScreen: () => {
					$('#bottom').css({
						'width': '100%',
					});
					$('#body').addClass('hide');
					$('#toggleLeft').addClass('hide');
					$('#btn-fullScreen').addClass('hide');
					$('#btn-defaultScreen').removeClass('hide');
				},
				defaultScreen: () => {
					$('#bottom').css({
						'width': oConfigure.mntrViewLeft + 'px',
					});
					$('#body').removeClass('hide');
					$('#toggleLeft').removeClass('hide');
					$('#btn-fullScreen').removeClass('hide');
					$('#btn-defaultScreen').addClass('hide');
				},
				refreshCctvAddBtn: () => {
					let sFrom = moment($('#params-video-search-ymdhms-fr').val(), 'YYYY-MM-DD HH:mm').format('YYYYMMDDHHmmss');
					let sTo = moment($('#params-video-search-ymdhms-to').val(), 'YYYY-MM-DD HH:mm').format('YYYYMMDDHHmmss');
					let duration = sFrom + ":" + sTo;
					console.log('--- refreshCctvAddBtn(), duration => '+duration);
					

					const $buttons = $('.web-rtc-view-container .web-rtc-view-container-buttons button.extra');
					if ($buttons.length) {
						for (let element of $buttons) {
							let fcltId = $(element).data('fcltId');

							let vdoData = fcltId + ":" + duration;
							console.log('--- vdoData : '+vdoData);

							let $outRqstCctv = $('#out-rqst-cctv');
							if ($outRqstCctv.length) {	// 반출신청대상을 추가할 때
								var eOutRqstCctv = document.getElementById('out-rqst-cctv');
								var eTbody = $(eOutRqstCctv).find('tbody');				//console.log("=== eTbody.length : "+eTbody.length);
								if (eTbody.length) {	// 추가된 반출대상이 있을 때
									let map = new Map();
									map.set('sVdoData', vdoData);
									if(oViewFirstRqst.cctv.checkIsExist(map)) {
										$(element).addClass('hide');
									} else {
										$(element).removeClass('hide');
									}
								} else {
									$(element).removeClass('hide');
								}
							}
						}
					}
				}
			};

			$(function () {
				oEvtVms.init();
				
				$('.datetimepicker').on('dp.change', event => {
					oEvtVms.refreshCctvAddBtn();
				});
				
			});
			
		</script>
	</c:when>

	<c:otherwise>
		<%--
		<div id="handle-vms">
			<c:if test="${configure.ptzCntrTy ne 'VIEW'}">
				<div class="row">
					<div class="col-xs-12" id="btn-handles">
						<c:if test="${configure.ptzCntrTy ne 'PRESET'}">
							<div class="row">
								<div class="col-xs-4 text-center"></div>
								<div class="col-xs-4 text-center">
									<button type="button" class="btn btn-info btn-handle" id="tiltPlusBtn">
										<i class="fas fa-chevron-up"></i>
									</button>
								</div>
								<div class="col-xs-4 text-center"></div>
							</div>
							<div class="row">
								<div class="col-xs-4 text-center">
									<button type="button" class="btn btn-info btn-handle" id="panPlusBtn">
										<i class="fas fa-chevron-left"></i>
									</button>
								</div>
								<div class="col-xs-4 text-center">
									<c:if test="${vmsPresetYn ne 'N'}">
										<button type="button" class="btn btn-warning btn-preset">0</button>
									</c:if>
								</div>
								<div class="col-xs-4 text-center">
									<button type="button" class="btn btn-info btn-handle" id="panMinusBtn">
										<i class="fas fa-chevron-right"></i>
									</button>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-4 text-center"></div>
								<div class="col-xs-4 text-center">
									<button type="button" class="btn btn-info btn-handle" id="tiltMinusBtn">
										<i class="fas fa-chevron-down"></i>
									</button>
								</div>
								<div class="col-xs-4 text-center"></div>
							</div>
							<div class="row">
								<div class="col-xs-4 text-center">
									<button type="button" class="btn btn-success btn-handle" id="zoomOutBtn">
										<i class="fas fa-minus"></i>
									</button>
								</div>
								<div class="col-xs-4 text-center">
									<span class="handle-label">ZOOM</span>
								</div>
								<div class="col-xs-4 text-center">
									<button type="button" class="btn btn-success btn-handle" id="zoomInBtn">
										<i class="fas fa-plus"></i>
									</button>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-4 text-center">
									<button type="button" class="btn btn-success btn-handle" id="focusOutBtn">
										<i class="fas fa-minus"></i>
									</button>
								</div>
								<div class="col-xs-4 text-center">
									<span class="handle-label">FOCUS</span>
								</div>
								<div class="col-xs-4 text-center">
									<button type="button" class="btn btn-success btn-handle" id="focusInBtn">
										<i class="fas fa-plus"></i>
									</button>
								</div>
							</div>
						</c:if>
						<c:forEach var="i" begin="1" end="9" step="1">
							<c:choose>
								<c:when test="${i % 3 == 1}">
									<div class="row">
									<div class="col-xs-4 text-center">
										<c:if test="${vmsPresetYn ne 'N'}">
											<button type="button" class="btn btn-warning btn-preset">${i}</button>
										</c:if>
									</div>
								</c:when>
								<c:when test="${i % 3 == 2}">
									<div class="col-xs-4 text-center">
										<c:if test="${vmsPresetYn ne 'N'}">
											<button type="button" class="btn btn-warning btn-preset">${i}</button>
										</c:if>
									</div>
								</c:when>
								<c:when test="${i % 3 == 0}">
									<div class="col-xs-4 text-center">
										<c:if test="${vmsPresetYn ne 'N'}">
											<button type="button" class="btn btn-warning btn-preset">${i}</button>
										</c:if>
									</div>
									</div>
								</c:when>
							</c:choose>
						</c:forEach>
					</div>
				</div>
			</c:if>
		</div>
		<div id="view-vms"></div>
		<!-- </div> -->
		<script>
			$(function () {
				if (typeof oVmsService != 'undefined') {
					var $btnHandle = $('#handle-vms .btn-handle');
					var $btnPreset = $('#handle-vms .btn-preset');
					$btnHandle.removeAttr('onmouseup');
					$btnHandle.removeAttr('onmousedown');
					$btnPreset.removeAttr('onclick');

					$btnHandle.mousedown(function () {
						var nIndex = $btnHandle.index(this);
						switch (nIndex) {
							// moveTiltUp
							case 0:
								oVmsService.ptz(oVmsService.ptzCmd.moveTiltUp, 3);
								break;
							// movePanLeft
							case 1:
								oVmsService.ptz(oVmsService.ptzCmd.movePanLeft, 3);
								break;
							// movePanRight
							case 2:
								oVmsService.ptz(oVmsService.ptzCmd.movePanRight, 3);
								break;
							// moveTiltDown
							case 3:
								oVmsService.ptz(oVmsService.ptzCmd.moveTiltDown, 3);
								break;
							// zoomOut
							case 4:
								oVmsService.ptz(oVmsService.ptzCmd.zoomOut, 3);
								break;
							// zoomIn
							case 5:
								oVmsService.ptz(oVmsService.ptzCmd.zoomIn, 3);
								break;
							// focusFar
							case 6:
								oVmsService.ptz(oVmsService.ptzCmd.focusFar, 3);
								break;
							// focusNear
							case 7:
								oVmsService.ptz(oVmsService.ptzCmd.focusNear, 3);
								break;
							default:
								break;
						}
					});

					$btnHandle.mouseup(function () {
						var nIndex = $btnHandle.index(this);
						if (nIndex == 0 || nIndex == 1 || nIndex == 2 || nIndex == 3) {
							oVmsService.ptz(oVmsService.ptzCmd.moveStop, 0);
						} else if (nIndex == 4 || nIndex == 5) {
							oVmsService.ptz(oVmsService.ptzCmd.zoomStop, 0);
						} else if (nIndex == 6 || nIndex == 7) {
							oVmsService.ptz(oVmsService.ptzCmd.focusStop, 0);
						}
					});

					$btnPreset.click(function () {
						var sPresetNum = $(this).text();
						oVmsService.preset(oVmsService.ptzCmd.presetMove, sPresetNum);
					});
				}
				if (oConfigure.ptzCntrTy == 'VIEW') {
					$('#handle-vms').hide();
				}

				if (oConfigure.ptzCntrTy == 'PRESET') {
					$('.btn-handle').hide();
				}
			});
		</script>
		 --%>
	</c:otherwise>

</c:choose>
