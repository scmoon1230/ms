<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<c:set var="ftpYn" scope="request"><prprts:value key='VMS_FTP_YN' /></c:set>
<%-- <c:set var="snapshotYn" value="${configProp['vms.snapshotYn']}" scope="request"/> --%>
<c:set var="searchYn" scope="request"><prprts:value key='VMS_SEARCH_YN' /></c:set>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>${data.fcltLblNm}(${data.fcltId})</title>
	<link type="text/css" rel="stylesheet" href="<c:url value='/css/mntr/resetCss.css' />">
	<link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap.min.css' />">
	<link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap-dialog.min.css' />">
	<link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap-toggle.min.css' />">
	<link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
	<link type="text/css" rel="stylesheet" href="<c:url value='/js/fontawesome/v5/css/all.min.css' />">
	<link type="text/css" rel="stylesheet" href="<c:url value='/css/mntr/vms/vmsPlayer.css' />">
	<link type="text/css" rel="stylesheet" href="<c:url value='/js/jquery-ui-1.11.4.custom/jquery-ui.min.css' />">
    <script src="<c:url value='/js/jquery/jquery-3.7.0.min.js' />"></script>
    <script src="<c:url value='/js/jquery/jquery-migrate-3.4.0.min.js' />"></script>
    <script>$.uiBackCompat = false;</script>
    <script src="<c:url value='/js/jquery-ui/jquery-ui.min.js' />"></script>
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/jquery-ui/jquery-ui.min.css' />">
</head>
<body>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>


<article id="view-vms"></article>
<div id="container-video-time">
    <span class="pull-left" id="video-time"></span>
	<div class="pull-left hide" id="container-step">
		<button class="btn btn-default btn-xs step step-backward" title="N초 전"><i class="fas fa-step-backward"></i></button>
		<select class="form-control input-xs" id="step-seconds" title="초 선택">
			<option value="10">10</option>
			<option value="30">30</option>
			<option value="60">60</option>
			<option value="300">300</option>
			<option value="600">600</option>
		</select>
		<button class="btn btn-default btn-xs step step-forward" title="N초 후"><i class="fas fa-step-forward"></i></button>
	</div>
	<div class="hide" id="wrapper-slider">
		<div id="slider">
			<div id="custom-handle" class="ui-slider-handle"></div>
		</div>
	</div>
</div>
<article id="handle-vms" class="form-group form-inline">
	<c:if test="${configure.cctvSearchYn eq 'Y'}">
		<div id="play-mode" class="form-group form-inline">
			<label class="radio-inline">
				<input type="radio" name="vms-mode" value="PLAY" checked="checked">
				현재
			</label>
			<label class="radio-inline">
				<input type="radio" name="vms-mode" value="SEARCH">
				과거
			</label>
			<label class="sr-only">
				<input type="checkbox" class="hide" id="chk-cctv-search-yn">
			</label>
		</div>
	</c:if>
	<c:if test="${configure.ptzCntrTy ne 'VIEW'}">
		<div id="play-deck" class="form-group form-inline hide">
			<c:if test="${configure.ptzCntrTy ne 'PRESET'}">
				<c:if test="${data.fcltKndDtlCd ne 'FT'}">
					<button type="button" class="btn btn-default btn-ptz" aria-label="moveTiltUp" title="상">
						<span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
					</button>
					<button type="button" class="btn btn-default btn-ptz" aria-label="moveTiltDown" title="하">
						<span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
					</button>

					<button type="button" class="btn btn-default btn-ptz" aria-label="movePanLeft" title="좌">
						<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
					</button>
					<button type="button" class="btn btn-default btn-ptz" aria-label="movePanRight" title="우">
						<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					</button>

					<select id="sel-ptz" class="form-control" title="줌 | 포커스">
						<option value="ZOOM">Zoom</option>
						<option value="FOCUS">Focus</option>
					</select>

					<button type="button" class="btn btn-default btn-ptz" aria-label="in" title="확대">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>

					<button type="button" class="btn btn-default btn-ptz" aria-label="out" title="축소">
						<span class="glyphicon glyphicon-minus" aria-hidden="true"></span>
					</button>
				</c:if>
			</c:if>
			<c:set var="vmsPresetYn" scope="request"><prprts:value key="VMS_PRESET_YN"/></c:set>
			<c:if test="${vmsPresetYn ne 'N'}">
				<c:if test="${data.fcltKndDtlCd ne 'FT'}">
					<select id="sel-preset" class="form-control" title="프리셋번호">
						<c:if test="${not empty data.presetBdwStartNum}">
							<c:set var="startPreNum" value="${data.presetBdwStartNum}" scope="page"></c:set>
						</c:if>
						<c:forEach begin="0" end="9" step="1" varStatus="status">
							<option value="<c:out value="${startPreNum + status.index}" />"><c:out value="${startPreNum + status.index}"/></option>
						</c:forEach>
					</select>
					<button id="btn-preset" type="button" class="btn btn-default btn-preset" aria-label="PresetGo" title="프리셋이동">GO</button>
					<!-- <button id="btn-preset-set" type="button" class="btn btn-default btn-preset" aria-label="PresetSet" title="프리셋설정">SET</button> -->
				</c:if>
			</c:if>
		</div>
	</c:if>
	<c:if test="${configure.cctvSearchYn eq 'Y'}">
		<div id="search-deck" class="form-group form-inline hide">
			<label class="control-label text-muted sr-only" for="params-video-search-ymdhms-fr">시작</label>
			<div class="form-group">
				<div class="input-group date datetimepicker">
					<input type="text" class="form-control" id="params-video-search-ymdhms-fr" name="params-video-search-ymdhms-fr" size="12" maxlength="12" title="시작일시" placeholder="시작일시" readonly="readonly">
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar" title="시작일시"></span></span>
				</div>
			</div>
			~
			<label class="control-label text-muted sr-only" for="params-video-search-ymdhms-to">종료</label>
			<div class="form-group">
				<div class="input-group date datetimepicker">
					<input type="text" class="form-control" id="params-video-search-ymdhms-to" name="params-video-search-ymdhms-to" size="12" maxlength="12" title="종료일시" placeholder="종료일시" readonly="readonly">
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar" title="종료일시"></span></span>
				</div>
			</div>
			
			<label class="control-label text-muted" id="label-video-search-dtctnYmdhms">발생일시</label>
			<label class="control-label text-primary" id="params-video-search-dtctnYmdhms"></label>
		
			<c:if test="${not empty playbackSpeedList and playbackSpeedList.size() > 1}">
				<label class="control-label text-muted" for="params-video-search-speed">배속</label>
				<select class="form-control" id="params-video-search-speed" title="배속">
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
			</c:if>
		</div>
	</c:if>
    <button type="button" id="btn-play" class="btn btn-default" aria-label="Play" title="재생">
        <span class="glyphicon glyphicon-play" aria-hidden="true"></span>
    </button>
    <button type="button" id="btn-stop" class="btn btn-default" aria-label="Stop" title="정지">
        <span class="glyphicon glyphicon-stop" aria-hidden="true"></span>
    </button>

	<button type="button" id="btn-screen" class="btn btn-default" aria-label="Fullscreen" title="전체화면">
		<span id="btn-screen-icon" class="glyphicon glyphicon-resize-full" aria-hidden="true"></span>
	</button>
	<button type="button" id="btn-cctv-modify" class="btn btn-default hide" aria-label="cctvModify" title="대상변경"
			onclick="oVmsPlayer.cctv.modify();">대상변경
	</button>
	<button type="button" id="btn-cctv-add" class="btn btn-default hide" aria-label="cctvAdd" title="대상추가"
		onclick="oVmsPlayer.cctv.add();">대상추가
	</button>
	
</article>

<section>
	<ul>
		<li><input id="cctvInfo" type="hidden"
				data-fclt-id				="<c:out value='${data.fcltId}' />"
				data-gw-vms-uid				="<c:out value='${data.gwVmsUid}' />"
				data-preset-bdw-start-num	="<c:out value='${data.presetBdwStartNum}' />"
				data-vrs-webrtc-addr		="<c:out value='${data.vrsWebrtcAddr}' />"
				data-vrs-rtsp-addr			="<c:out value='${data.vrsRtspAddr}' />"
				data-ptz-api-addr			="<c:out value='${data.ptzApiAddr}' />"
				data-ptz-api-key			="<c:out value='${data.ptzApiKey}' />"
				data-ptz-api-sys-cd			="<c:out value='${data.ptzApiSysCd}' />"
				data-ptz-auth-yn			="<c:out value='${data.ptzAuthYn}' />"
				data-fclt-used-ty-cd		="<c:out value='${data.fcltUsedTyCd}' />"
				data-fclt-lbl-nm			="<c:out value='${data.fcltLblNm}' />"
				data-fclt-knd-dtl-cd		="<c:out value='${data.fcltKndDtlCd}' />"
				data-fclt-used-ty-nm		="<c:out value='${data.fcltUsedTyNm}' />"
				data-road-adres-nm			="<c:out value='${data.roadAdresNm}' />"
				data-lotno-adres-nm			="<c:out value='${data.lotnoAdresNm}' />"
				data-point-x				="<c:out value='${data.pointX}' />"
				data-point-y				="<c:out value='${data.pointY}' />"
				data-dstrt-cd				="<c:out value='${data.dstrtCd}'/>"/>
		</li>
        <li><input id="eventInfo" type="hidden"
       			data-evt-id				="<c:out value='${event.evtId}' />"
       			data-evt-ocr-no			="<c:out value='${event.evtOcrNo}' />"
       			data-evt-ocr-ymd-hms	="<c:out value='${event.evtYmdHms}' />"
       			data-dtctn-ymdhms		="<c:out value='${event.dtctnYmdhms}' />"/>
        </li>
	</ul>
</section>

<!-- common -->
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap.min.js' />"></script>
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-toggle.min.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/moment.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/ko.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.dev.js' />"></script>
<script src="<c:url value='/js/mntr/cmm.js' />"></script>

<!-- vms -->
<c:set var="vmsWebRtcYn" scope="request"><prprts:value key="VMS_WEBRTC_YN"/></c:set>
<%--<c:set var="vmsSoftware" scope="request"><prprts:value key="VMS_SOFTWARE"/></c:set>--%>
<c:choose>
	<%--<c:when test="${configProp['vms.webRtcYn'] eq 'Y' and configProp['vms.software'] eq 'HIVE'}">--%>
	<c:when test="${vmsWebRtcYn eq 'Y' and vmsSoftware eq 'HIVE'}">
		<script src="<c:url value='/js/mntr/vms/vmsCommon.js' />"></script>
		<script src="<c:url value='/js/mntr/vms/${vmsSoftware}/webrtcadapter.js' />"></script>
		<!-- <script src="<c:url value='/js/mntr/vms/${vmsSoftware}/HiVeWebRtcPlayer_DEV.js' />"></script> -->
        <script src="<c:url value='/js/mntr/vms/${vmsSoftware}/HiVeWebRtcPlayerV2_DEV.js' />"></script>
		<script src="<c:url value='/js/mntr/vms/${vmsSoftware}/${vmsSoftware}_WebRTC.js' />"></script>
        <form id="form-web-rtc">
            <input type="hidden" name="fcltId"     value="${params.fcltId}"/>
			<input type="hidden" name="viewRqstNo" value="${params.viewRqstNo}"/>
            <input type="hidden" name="mode"       value="${params.mode}"/>
            <input type="hidden" name="ymdhms"     value="${params.ymdhms}"/>
            <input type="hidden" name="from"       value="${params.from}"/>
            <input type="hidden" name="to"         value="${params.to}"/>
			<input type="hidden" name="start"      value="${params.start}"/>
            <input type="hidden" name="speed"      value="${params.speed}"/>
            <input type="hidden" name="action"     value="${params.action}"/>
            <input type="hidden" name="modifyYn"   value="${params.modifyYn}"/>
        </form>
	</c:when>
</c:choose>

<script src="<c:url value='/js/mntr/vms/webRtcPlayer.js' />"></script>

<c:if test="${configure.ptzCntrTy ne 'VIEW'}">
	<script>
		$(function () {
			<c:if test="${configure.ptzCntrTy ne 'PRESET'}">
			var $btnPtz = $('#play-deck button.btn-ptz');
			$btnPtz.mousedown(function () {
				var nIndex = $btnPtz.index(this);
				var sType = $('#sel-ptz option:selected').val();

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
						if (sType == 'ZOOM') {
							oVmsService.api.ptz(oVmsService.api.type.ptz.zoom, oVmsService.api.cmd.ptz.zoomIn);
						} else if (sType == 'FOCUS') {
							oVmsService.api.ptz(oVmsService.api.type.ptz.focus, oVmsService.api.cmd.ptz.focusNear);
						}
						break;
					case 5:
						if (sType == 'ZOOM') {
							oVmsService.api.ptz(oVmsService.api.type.ptz.zoom, oVmsService.api.cmd.ptz.zoomOut);
						} else if (sType == 'FOCUS') {
							oVmsService.api.ptz(oVmsService.api.type.ptz.focus, oVmsService.api.cmd.ptz.focusFar);
						}
						break;
					default:
						break;
				}
			});

			$btnPtz.mouseup(function () {
				var nIndex = $btnPtz.index(this);
				var sType = $('#sel-ptz option:selected').val();
				if (nIndex == 0 || nIndex == 1 || nIndex == 2 || nIndex == 3) {
					oVmsService.api.ptz(oVmsService.api.type.ptz.pt, oVmsService.api.cmd.ptz.stop);
				} else if (nIndex == 4 || nIndex == 5) {
					if (sType == 'ZOOM') {
						oVmsService.api.ptz(oVmsService.api.type.ptz.zoom, oVmsService.api.cmd.ptz.zoomStop);
					} else if (sType == 'FOCUS') {
						oVmsService.api.ptz(oVmsService.api.type.ptz.focus, oVmsService.api.cmd.ptz.focusStop);
					}
				}
			});
			</c:if>
			var $btnPreset = $('#play-deck button.btn-preset');
			$btnPreset.click(function () {
				var sPresetNo = $('#sel-preset option:selected').val();
				var nPresetNo = parseInt(sPresetNo);
				var nIndex = $btnPreset.index(this);
				switch (nIndex) {
					case 0:
						oVmsService.api.preset(oVmsService.api.type.preset.move, nPresetNo);
						break;
					case 1:
						oVmsService.api.preset(oVmsService.api.type.preset.set, nPresetNo);
						break;
					default:
						break;
				}
			});
		});
	</script>
</c:if>
</body>
</html>
