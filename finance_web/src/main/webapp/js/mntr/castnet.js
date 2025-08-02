function doCastNetByEvtOcrNo(evtOcrNo) {
    console.log('-- doCastNetByEvtOcrNo 1. %s', evtOcrNo);
    let rtn = true;
    if (typeof evtOcrNo == 'undefined' || evtOcrNo == '') {
        if (oCurrentEvent.event.evtOcrNo != '') {
            evtOcrNo = oCurrentEvent.event.evtOcrNo;
            console.log('-- doCastNetByEvtOcrNo 2. %s', evtOcrNo);
        }

        if (typeof evtOcrNo == 'undefined' || evtOcrNo == '') {
            alert('설정된 발생번호가 없습니다.');
            return false;
        }
    }

    $.ajax({
        type: 'POST',
        url:contextRoot + '/mntr/eventById.json',
        async: false,
        data: {
            evtOcrNo: evtOcrNo,
        },
        success: function (data) {
            if (data != null && typeof data == 'object') {
                oCurrentEvent.event = data;
                var $btn = $('<button/>');
                $btn.data('evtId', data.evtId);
                $btn.data('evtOcrNo', data.evtOcrNo);
                $btn.data('projection', 'EPSG:4326');
                $btn.data('pointX', data.pointX);
                $btn.data('pointY', data.pointY);
                $btn.data('fcltId', data.ocrFcltId);
                olSwipMap.mntr.castnet($btn[0]);
            } else {
                oEvtMntr.castnet.stop();
            }
        },
        error: function () {
            alert('이벤트 정보를 가져오지 못했습니다.');
            rtn = false;
        }
    });
    return rtn;
}

function stopCastNet() {
    oCurrentEvent.clear();
	if (typeof oVmsService != 'undefined' && oVmsService.isConnected) {
		oVmsService.stop();
		/**
		 * <pre>
		 * if (oVmsCommon.cnOsvtPlaytimeStop != null) {
		 * 	clearTimeout(oVmsCommon.cnOsvtPlaytimeStop);
		 * 	oVmsCommon.cnOsvtPlaytimeStop = null;
		 * }
		 * </pre>
		 */
		oVmsService.playlists = null;
		oVmsService.selected = null;
	}

    oVmsService.disconnectPlaylist();
    doDivNormal('GENERAL', 'GENERAL', null, null);
    olSwipMap.mntr.clear();
}
