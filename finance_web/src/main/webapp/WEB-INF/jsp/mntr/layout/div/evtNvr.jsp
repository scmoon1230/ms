<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<div id="view-nvr">
			<object id="xnsSdkWindowI" width="285px" height="100%" style="height: 100%; min-height: 280px;" classid="clsid:C3F99E59-433D-4A79-A188-B36ACE8F78F8"></object>
			<object id="xnsSdkDevice" height="10px" width="10px" classid="clsid:9BED9251-E8E7-4B67-B281-ADC06BA7988D"></object>
		</div>
	</div>
</div>
<script>
var xnsWindowI = null;
var xnsDevice = null;

var hDevice;
var hMediaSource = 0;
var nControlID = 0;
var nPtzSpeed = 50;
var nError;

var nNvrUid = null;

$(function() {
	$(window).unload(function() {
		xnsDevice.ReleaseDevice(hDevice);
	});
	
	xnsWindowI = document.getElementById('xnsSdkWindowI');
	xnsDevice = document.getElementById('xnsSdkDevice');
	destroyNvr();
	
	if (typeof _map != 'undefined') {
		var wgs84 = convertToWGS84(_map.getCenter().lon, _map.getCenter().lat);
		var pointX = wgs84.x;
		var pointY = wgs84.y;
		
		$.ajax({
			type : 'POST',
			url : contextRoot + '/mntr/nearestCctv.json',
			data : {
				searchFcltViewerType : 'SNVR',
				pointX : pointX,
				pointY : pointY
			},
			success : function(result) {
				if (typeof result.fcltUid != 'undefined' && result.fcltUid != '') {
					nNvrUid = result.fcltUid;
					setTimeout(initNvr, 500);
					setTimeout(function() {
						connectNvr();
						var cctvPoint = convertByWGS84(result.pointX, result.pointY);
						featureselected(cctvPoint, 'point', 'NVR', false, false, false);
					}, 1000);
				}
			},
			error : function(data, status, err) {
				alert('NVR 재생에 실패했습니다.');
			}
		});
	}
});

function initNvr() {
	hDevice = 0;
	nError = xnsDevice.Initialize();
	console.log('InitializeDevice [' + xnsDevice.GetErrorString(nError) + ']');
	nError = xnsWindowI.Initialize(0, 0);
	console.log('InitializeWindow [' + xnsDevice.GetErrorString(nError) + ']');
}

function destroyNvr() {
	xnsDevice.ReleaseDevice(hDevice);
}

function connectNvr() {
	if (hDevice == 0) {
		hDevice = xnsDevice.CreateDevice(1);
		if (hDevice == 0) {
			return;
		}
		console.log('CreateDevice');
	}
	
	xnsDevice.SetConnectionInfo(hDevice, 'Samsung', 'Samsung NVR', 1, nvrIp, 554, 8180, nvrId, nvrPassword);
	nError = xnsDevice.ConnectNonBlock(hDevice, false, true);
	console.log('ConnectNonBlock [' + xnsDevice.GetErrorString(nError) + ']');
}

function xnsSdkDevice::OnDeviceStatusChanged(nDeviceID, nErrorCode, nDeviceStatus, nHddCondition) {
	console.log("OnDeviceStatusChanged() EVENT:: device_id=" + nDeviceID + ", status=" + nDeviceStatus + ", error=" + nErrorCode + "[" + xnsDevice.GetErrorString(nErrorCode) + "]");
	if(nErrorCode == 0 && nDeviceStatus == 1) {
		console.log("Connected...");
		nCount = xnsDevice.GetControlCount(hDevice, 8);
		console.log("GetControlCount : " + nCount);
		for(i = 0; i < nCount; i++)	{
			var nControlCapability = xnsDevice.GetControlCapability(hDevice, i + 2, 37);
			if (nControlCapability == 1) {
				hMediaSource = xnsDevice.OpenMediaEx(hDevice, i+ 2, 1, 0, 0);
				if (i == nNvrUid - 1) {
					console.log('hMediaSource : ' + hMediaSource);
					xnsWindowI.Start(hMediaSource);
				}
			}
		}
	}
}
</script>
