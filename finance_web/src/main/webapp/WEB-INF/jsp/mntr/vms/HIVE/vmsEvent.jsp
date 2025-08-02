<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script for="ActiveX" event="OnHiVeEvent_Ininitalized()">
	console.log('========== ActiveX.OnHiVeEvent_Ininitalized ==========');
	oVmsService.isInit = true;
	if (oVmsService.callback != null) {
		oVmsService.callback();
	}
</script>
<script for="ActiveX" event="OnHiVeEvent_Connected(serverName, serverAddress)">
	console.log('========== ActiveX.OnHiVeEvent_Connected ==========');
</script>
<script for="ActiveX" event="OnHiVeEvent_Disconnected(serverName, serverAddress, result)">
	console.log('========== ActiveX.OnHiVeEvent_Disconnected ==========');
	oVmsService.isConnected = false;
	console.log('%s | %s | %s',serverName, serverAddress, result);
</script>
<script for="ActiveX" event="OnHiVeEvent_ChangedViewSelection(nViewPos, sVideoInId)">
	console.log('========== ActiveX.OnHiVeEvent_ChangedViewSelection(%s, %s) ==========', nViewPos, sVideoInId);
	console.log('========== ActiveX.OnHiVeEvent_ChangedViewSelection: previous(%s) ==========', oVmsService.selected.fcltUid);
	var oSelectedCctvInfo = oVmsService.selected;
	if(oSelectedCctvInfo.fcltUid != sVideoInId) {
		var oPlaylists = oVmsService.playlists;
		var nSize = oPlaylists.length;
		for(var i = 0; i < nSize; i++) {
			var oCctvInfo = oPlaylists[i];
			if(oCctvInfo.fcltUid == sVideoInId) {
				oVmsService.selected = oCctvInfo;
			}
		}
	}
</script>
<script for="ActiveX" event="OnHiVeEvent_CfgData(serverName, serverAddress, cfgData)">
	console.log('========== ActiveX.OnHiVeEvent_CfgData ==========');
	console.log(serverName);
	console.log(serverAddress);
	console.log(cfgData);
</script>
<script for="ActiveX" event="OnHiVeEvent_RecCalendar(recDays)">
	console.log('========== ActiveX.OnHiVeEvent_RecCalendar ==========');
	console.log(recDays);
</script>
<script for="ActiveX" event="OnHiVeEvent_RecTimeline(recTimeline)">
	console.log('========== ActiveX.OnHiVeEvent_RecTimeline ==========');
	console.log(recTimeline);
</script>
<script for="ActiveX" event="OnHiVeEvent_EventActionCfg(eventCfgData)">
	console.log('========== ActiveX.OnHiVeEvent_EventActionCfg ==========');
	console.log(eventCfgData);
</script>

<script for="ActiveX" event="OnHiVeEvent_BackupStatus(backupState, progress)">
	console.log('========== ActiveX.OnHiVeEvent_BackupStatus ==========');
	console.log(backupState);
	console.log(progress);
</script>

<script for="ActiveX" event="OnHiVeEvent_ConnectionFinished(integrationServerState, totalServerCnt, connectedServerCnt, disconnectedServerCnt)">
	console.log('========== ActiveX.OnHiVeEvent_ConnectionFinished ==========');
	oVmsService.isConnected = true;

	if (oVmsService.callback != null) {
		oVmsService.callback();
	}

	// 접속된 서버에 등록된 모든 CCTV의 정보를 획득
	/*
	setTimeout(function() {
		var xml = oVmsActiveX.getVideoIns();
		var videoIns = $(xml).find('VideoIn');
		var oVideoIns = [];
		$.each($(videoIns), function(i, v) {
			var obj = { id: $(v).attr('id'), serverAddress: $(v).attr('server_address'), serverName: $(v).attr('server_name'), state: $(v).attr('state'), address: $(v).attr('address'), name: $(v).attr('name') }
			oVideoIns.push(obj);
		});
		console.log('========== 3. getVideoIns ==========');
		console.log(oVideoIns);
	}, 500);
	 */
</script>