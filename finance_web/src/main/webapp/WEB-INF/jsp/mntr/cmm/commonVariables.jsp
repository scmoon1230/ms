<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<section id="variables-container">
	<ul class="variables">
		<%--<li>
			<input id="ipmapping" type="hidden"
				data-gis			="<c:out value='${ipMapping.gis}' />"
				data-gis-mp			="<c:out value='${ipMapping.gisMp}' />"
				data-websocket		="<c:out value='${ipMapping.websocket}' />"
				data-websocket-mp	="<c:out value='${ipMapping.websocketMp}' />"
				data-scmp-img		="<c:out value='${ipMapping.scmpImg}' />"
				data-scmp-img-mp	="<c:out value='${ipMapping.scmpImgMp}' />"
				data-scmp			="<c:out value='${ipMapping.scmp}' />"
				data-scmp-mp		="<c:out value='${ipMapping.scmpMp}' />"
				data-vms			="<c:out value='${ipMapping.vms}' />"
				data-vms-mp			="<c:out value='${ipMapping.vmsMp}' />"
				data-rtsp-ip-ary	="<c:out value='${ipMapping.rtspIp}' />"
				data-rtsp-ip-mp-ary	="<c:out value='${ipMapping.rtspIpMp}' />"
			/>
		</li>--%>
		<li>
			<input id="configure" type="hidden"
			<%-- data-exe-env				="<c:out value='${configProp["Globals.exeEnv"]}' />" --%>
				data-ssl						="<c:out value='${LoginVO.ssl}' />"
				data-sys-id						="<c:out value='${LoginVO.sysId}' />"
				data-sys-cd						="<c:out value='${LoginVO.sysId}' />"
				data-grp-id						="<c:out value='${LoginVO.grpId}' />"
				data-user-id					="<c:out value='${LoginVO.userId}' />"
				data-user-nm					="<c:out value='${LoginVO.userNmKo}' />"
				data-sigungu-cd					="<c:out value='${cmConfig.sigunguCd}' />"
				data-ucp-id						="<prprts:value key='UCP_ID' />"
				data-ucp-Nm						="<c:out value='${configure.ucpNm}' />"
				data-dstrt-cd					="<prprts:value key='DSTRT_CD' />"
				data-mntr-ty-id					="<prprts:value key='MNTR_TY_ID' />"
				data-set-time					="<prprts:value key='SET_TIME' />"
				data-point-x					="<prprts:value key='POINT_X' />"
				data-point-y					="<prprts:value key='POINT_Y' />"
				data-point-z					="<prprts:value key='POINT_Z' />"
				data-gis-level					="<prprts:value key='GIS_LEVEL' />"
				data-map-altitud				="<prprts:value key='MAP_ALTITUD' />"
				data-map-tilt					="<prprts:value key='MAP_TILT' />"
				data-map-direct					="<prprts:value key='MAP_DIRECT' />"
				data-div-move-set				="<prprts:value key='DIV_MOVE_SET' />"
				data-evt-lc-move-yn				="<prprts:value key='EVT_LC_MOVE_YN' />"
				data-left-div-hdn-yn			="<prprts:value key='LEFT_DIV_HDN_YN' />"
				data-rads-clmt					="<prprts:value key='RADS_CLMT' />"
				data-rads-route					="<prprts:value key='RADS_ROUTE' />"
				data-mntr-view-left				="<prprts:value key='MNTR_VIEW_LEFT' />"
				data-mntr-view-right			="<prprts:value key='MNTR_VIEW_RIGHT' />"
				data-mntr-view-bottom			="<prprts:value key='MNTR_VIEW_BOTTOM' />"
				data-pop-route					="<prprts:value key='POP_ROUTE' />"
				data-pop-width					="<prprts:value key='POP_WIDTH' />"
				data-pop-height					="<prprts:value key='POP_HEIGHT' />"
				data-gis-ty						="<prprts:value key='GIS_TY' />"
				data-auto-end-time				="<prprts:value key='AUTO_END_TIME' />"
				data-cctv-view-rads				="<prprts:value key='CCTV_VIEW_RADS' />"
				data-cn-osvt-opt				="<prprts:value key='CN_OSVT_OPT' />"
				data-fclt-base-item				="<prprts:value key='FCLT_BASE_ITEM' />"
				data-icon-ty					="<prprts:value key='ICON_TY' />"
				data-icon-size					="<prprts:value key='ICON_SIZE' />"
				data-header-ty					="<prprts:value key='HEADER_TY' />"
				data-gis-label-view-scale		="<prprts:value key='GIS_LABEL_VIEW_SCALE' />"
				data-gis-feature-view-scale		="<prprts:value key='GIS_FEATURE_VIEW_SCALE' />"
				data-cn-osvt-playtime-stop-yn	="<prprts:value key='CN_OSVT_PLAYTIME_STOP_YN' />"
				data-full-screen-close-yn		="<prprts:value key='FULL_SCREEN_CLOSE_YN' />"
				data-menu-ordr-ty				="<prprts:value key='MENU_ORDR_TY' />"
				data-user-approve-yn			="<prprts:value key='USER_APPROVE_YN' />"
				data-ptz-cntr-ty				="<c:out value='${configure.ptzCntrTy}' />"		<%-- CCTV권한 --%>
				data-cctv-access-yn				="<c:out value='${configure.cctvAccessYn}' />"	<%-- CCTV권한 --%>
				data-cctv-search-yn				="<c:out value='${configure.cctvSearchYn}' />"	<%-- CCTV권한 --%>
			<%--data-conf-mntr-car-lc-tm		="<c:out value='${configProp["mntrCarLcT"]}' />"
				data-conf-mntr-car-lc-trace-cnt	="<c:out value='${configProp["mntrCarLcTraceCnt"]}' />"
				data-image-ess					="<prprts:value key='DIR_ESS' />"
				data-image-event				="<prprts:value key='DIR_EVENT_IMG' />"--%>
				data-network-ip					="<c:out value='${configure.networkIp}' />"
			/>
		</li>
		<%--<li>
			<c:set var="websocket"	value="${urlMapping.websocket}" scope="request"/>
			<c:set var="scmpImg"	value="${urlMapping.scmpImg}" scope="request"/>
			<c:set var="scmpMsg"	value="${urlMapping.scmpMsg}" scope="request"/>
			<c:set var="serverIp"	value="${urlMapping.serverIp}" scope="request"/>
			<c:set var="linkoutUrl"	value="${urlMapping.linkoutUrl}" scope="request"/>
			<input id="url" type="hidden"
				data-websocket		="<c:out value='${websocket}' />"
				data-scmp-img		="<c:out value='${scmpImg}' />"
				data-scmp-msg		="<c:out value='${scmpMsg}' />"
				data-server			="<c:out value='${serverIp}' />"
				data-server-ip		="<c:out value='${serverIp}' />"
				data-linkout-url	="<c:out value='${linkoutUrl}' />"
			/>
		</li>--%>
		<li>
			<input id="gis" type="hidden"
				data-engine				="<prprts:value key='GIS_ENGINE' />"
			<%--data-ip					="<c:out value='${urlMapping.gisUrl}' />"--%>
				data-url				="<c:out value='${urlMapping.gisUrl}' />"
				data-url-aerial			="<c:out value='${urlMapping.gisUrlAerial}' />"
				data-url-uti			="<c:out value='${urlMapping.gisUrlUti}' />"
				data-url-wfs			="<c:out value='${urlMapping.gisUrlWfs}' />"
				data-url-wms			="<c:out value='${urlMapping.gisUrlWms}' />"
				data-projection			="<c:out value='${urlMapping.gisProjection}' />"
				data-ol-version			="<prprts:value key='GIS_OL_VER' />"
				data-aerial-yn			="<prprts:value key='GIS_AERIAL_YN' />"
				data-search-yn			="<prprts:value key='GIS_SEARCH_BAR_YN' />"
				data-gis-ty				="<prprts:value key='GIS_TY' />"
				data-bounds-left		="<prprts:value key='GIS_EXTENT_LEFT' />"
				data-bounds-bottom		="<prprts:value key='GIS_EXTENT_BOTTOM' />"
				data-bounds-right		="<prprts:value key='GIS_EXTENT_RIGHT' />"
				data-bounds-top			="<prprts:value key='GIS_EXTENT_TOP' />"
				data-proxy-yn			="<prprts:value key='GIS_PROXY_YN' />"
				data-proxy-url			="<prprts:value key='GIS_PROXY_URL' />"
				data-proxy-url-add		="<prprts:value key='GIS_PROXY_URL_ADD' />"
				data-label-view-scale	="<prprts:value key='GIS_LABEL_VIEW_SCALE' />"
				data-feature-view-scale	="<prprts:value key='GIS_FEATURE_VIEW_SCALE' />"
			<%--data-zoom-index			="<c:out value='${cmConfig.gisZoomIndex}' />"
				data-zoom-min			="<c:out value='${cmConfig.gisZoomMin}' />"
				data-zoom-max			="<c:out value='${cmConfig.gisZoomMax}' />"--%>
			/>
		</li>
		<li>
		<%--<c:set var="vmsIp"		value="${urlMapping.webRtcUrl}" scope="request"/>--%>
			<c:set var="webRtcUrl"	value="${urlMapping.webRtcUrl}" scope="request"/>
			<input id="vms" type="hidden"
				data-software				="<prprts:value key='VMS_SOFTWARE' />"
			<%--data-ip						="<c:out value='${vmsIp}' />"
				data-port					="<prprts:value key='VMS_PORT' />"
				data-id						="<prprts:value key='VMS_ID' />"
				data-password				="<prprts:value key='VMS_PASSWORD' />"--%>
				data-web-rtc-yn				="<prprts:value key='VMS_WEBRTC_YN' />"
				data-web-rtc-url			="<c:out value='${webRtcUrl}' />"
				data-play-time				="<prprts:value key='PLAY_TIME' />"
				data-playbacktime-base		="<prprts:value key='BASE_PLAYBACKTIME' />"
				data-playbacktime-max		="<prprts:value key='MAX_PLAYBACKTIME' />"
				data-playbacktime-max-bf-now="<prprts:value key='MAX_BF_PLAYBACKTIME_NOW' />"
				data-playbacktime-max-bf	="<prprts:value key='MAX_BF_PLAYBACKTIME' />"
				data-playbacktime-max-af	="<prprts:value key='MAX_AF_PLAYBACKTIME' />"
			<%--data-ftp-yn					="<prprts:value key='VMS_FTP_YN' />"
				data-link-yn				="<prprts:value key='VMS_LINK_YN' />"
				data-snapshot-yn			="<prprts:value key='VMS_SNAPSHOT_YN' />"
				data-snapshot-path			="<prprts:value key='VMS_SNAPSHOT_PATH' />"
				data-preset-yn				="<prprts:value key='VMS_PRESET_YN' />"
				data-test-fclt-uid			="<prprts:value key='VMS_TEST' />"--%>
				data-multi-yn				="<prprts:value key='VMS_MULTI_YN' />"
			/>
		</li>
		<li>
			<input id="tvoConfig" type="hidden"
				data-tvo-url					="<prprts:value key='PVE_URL' />"						<%-- 영상반출시스템 URL --%>
				data-view-auto-aprv-yn			="<prprts:value key='VIEW_AUTO_APRV_YN' />"				<%-- 열람자동승인 --%>
				data-view-auto-aprv-start		="<prprts:value key='VIEW_AUTO_APRV_START' />"			<%-- 열람자동승인 시작 --%>
				data-view-auto-aprv-end			="<prprts:value key='VIEW_AUTO_APRV_END' />"			<%-- 열람자동승인 종료 --%>
				data-view-extn-auto-aprv-yn		="<prprts:value key='VIEW_EXTN_AUTO_APRV_YN' />"		<%-- 열람기간연장 자동승인 --%>
				data-view-rqst-duration			="<prprts:value key='VIEW_RQST_DURATION' />"			<%-- 열람신청기간 --%>
				data-out-auto-aprv-yn			="<prprts:value key='OUT_AUTO_APRV_YN' />"				<%-- 입수자동승인 --%>
				data-out-auto-aprv-start		="<prprts:value key='OUT_AUTO_APRV_START' />"			<%-- 입수자동승인 시작 --%>
				data-out-auto-aprv-end			="<prprts:value key='OUT_AUTO_APRV_END' />"				<%-- 입수자동승인 종료 --%>
				data-out-drm-auto-aprv-yn		="<prprts:value key='OUT_DRM_AUTO_APRV_YN' />"			<%-- 반출자동승인 --%>
				data-out-extn-auto-aprv-yn		="<prprts:value key='OUT_EXTN_AUTO_APRV_YN' />"			<%-- 반출기간연장 자동승인 --%>
				data-org-vdo-auto-rgs-yn		="<prprts:value key='ORG_VDO_AUTO_RGS_YN' />"			<%-- 원본영상 자동등록 --%>
				data-recomm-vdo-duration		="<prprts:value key='RECOMM_VDO_DURATION' />"			<%-- 권장영상길이 --%>
				data-out-file-play-prod			="<prprts:value key='OUT_FILE_PLAY_PROD' />"			<%-- 재생기간 --%>
				data-out-file-play-prod-third	="<prprts:value key='OUT_FILE_PLAY_PROD_THIRD' />"		<%-- 제3자 제공시 재생가능기간 --%>
				data-out-file-play-cnt			="<prprts:value key='OUT_FILE_PLAY_CNT' />"				<%-- 재생횟수 --%>
				data-file-keep-duration			="<prprts:value key='FILE_KEEP_DURATION' />"			<%-- 보관기간 --%>
				data-approve-notify-yn			="<prprts:value key='APPROVE_NOTIFY_TY' />"				<%-- 승인알림구분 --%>
				data-approve-notify-mute-yn		="<prprts:value key='APPROVE_NOTIFY_MUTE_YN' />"		<%-- 승인알림무음여부 --%>
				data-dashboard-refresh-interval	="<prprts:value key='DASHBOARD_REFRESH_INTERVAL' />"	<%-- 상황판 새로고침 주기 --%>
				data-mask-use-yn				="<prprts:value key='MASK_USE_YN' />"				<%-- 마스킹사용여부 --%>
				data-enc-module-supplier		="<prprts:value key='ENC_MODULE_SUPPLIER' />"		<%-- 암호화모듈 공급자 --%>
				data-mask-module-supplier		="<prprts:value key='MASK_MODULE_SUPPLIER' />"		<%-- 마스킹툴 공급자 --%>
				data-mask-tool-version			="<prprts:value key='MASK_TOOL_VERSION' />"			<%-- 마스킹툴 버전 --%>
				data-mask-tool-ftp				="<prprts:value key='MASK_TOOL_FTP' />"				<%-- 마스킹용 ftp 접속 url --%>
				data-mask-tool-usr				="<prprts:value key='MASK_TOOL_USR' />"				<%-- 마스킹용 ftp 접속 아이디/패스워드 --%>
				data-mask-tool-ans				="<prprts:value key='MASK_TOOL_ANS' />"				<%-- 마스킹 상태 회신용 api --%>
				data-ftp-vdo-dir				="<prprts:value key='FTP_VDO_DIR' />"				<%-- ftp 파일 rootpath--%>
			/>
		</li>
	</ul>
</section>
<script>
	var console = window.console;
	if (!console || !console.log || !console.error) {
		console = {
			log: function () {
			},
			error: function () {
			}
		};
	}

	contextRoot = '<c:out value="${pageContext.request.contextPath}" />';
	oIpMapping = $('ul.variables input#ipmapping').data();
	oConfigure = $('ul.variables input#configure').data();
	oUrl = $('ul.variables input#url').data();
	oGis = $('ul.variables input#gis').data();
	oVms = $('ul.variables input#vms').data();
	//oTvo = $('ul.variables input#tvo').data();
	oTvoConfig = $('ul.variables input#tvoConfig').data();
	
	// proxy 상시 사용
	sPrefix = contextRoot + '/proxy/proxy.jsp?targetUrl=';
    sPrefix2 = sPrefix;
    if (oGis.proxyUrlAdd != '') sPrefix2 += oGis.proxyUrlAdd + '?targetUrl=';
	oGis.url = oGis.url == '' ? '' : sPrefix2 + oGis.url;
	oGis.urlAerial = oGis.urlAerial == '' ? '' : sPrefix2 + oGis.urlAerial;
	oGis.urlUti = oGis.urlUti == '' ? '' : sPrefix + oGis.urlUti;
	oGis.urlWms = oGis.urlWms == '' ? '' : sPrefix + oGis.urlWms;
	oGis.urlWfs = oGis.urlWfs == '' ? '' : sPrefix + oGis.urlWfs;
	
	if (typeof oMenu == 'undefined') {
		oMenu = {
			mainMenu: '',
			subMenu: ''
		};
	}

	// 테스트 fcltUid
	//if (oVms.testFcltUid.length) {
	//	oVms.testFcltUidAry = oVms.testFcltUid.split(',');
	//}

	$('section#variables-container').remove();

	<c:if test="${not empty LoginVO.userId}">
	//$.post(contextRoot + '/mntr/selectUserEventList.json').done(function (data) {
	//	oUserEventList = data.userEventList || [];
	//});
	</c:if>
	
</script>
<!-- 
<script src="<c:url value='/js/mntr/CurrentEvent.js' />"></script>
<script>
	const oCurrentEvent = new CurrentEvent();
</script>
 -->
