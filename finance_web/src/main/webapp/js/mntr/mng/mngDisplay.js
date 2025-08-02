$(function() {
	oMngDisplay = new mngDisplay();
	oMngDisplay.init();
});

function mngDisplay() {
	this.init = function() {
		collapse({
			left : true
		});
		oDiv.hideDiv();

		var sLeftDivHdn = $('#leftDivHdnYn').val();
		oMngDisplay.setDivHdnChk(sLeftDivHdn);

		$('input[id^="chk-div-hdn-"]').change(function() {
			oMngDisplay.setDivHdnVal();
		});

		$('[data-toggle="tooltip"]').tooltip({
			container : 'body',
			placement : 'top'
		});
	};

	this.reset = function() {
		$.ajax({
			type : "POST",
			url : contextRoot + '/mntr/selectUmConfigInfo.json',
			dataType : "json",
			data : {
				sysId : oConfigure.sysCd,
				userId : oConfigure.sysCd
			},
			success : function(data) {
				if (typeof data.configureVO != 'undefined' && data.configureVO != null) {
					var oConfigureVO = data.configureVO;
					// radio
					// $('#gisTy').val(oConfigureVO.gisTy);
					$('input[name="gisTy"]').filter('[value="' + oConfigureVO.gisTy + '"]').prop('checked', true);

					$('#gisLabelViewScale').val(oConfigureVO.gisLabelViewScale);
					$('#gisFeatureViewScale').val(oConfigureVO.gisFeatureViewScale);
					// radio
					// $('#iconTy').val(oConfigureVO.iconTy);
					$('input[name="iconTy"]').filter('[value="' + oConfigureVO.iconTy + '"]').prop('checked', true);

					// select
					$('#iconSize').val(oConfigureVO.iconSize);

					// checkbox
					$('#leftDivHdnYn').val(oConfigureVO.leftDivHdnYn);
					oMngDisplay.setDivHdnChk(oConfigureVO.leftDivHdnYn);

					// radio
					// $('#evtLcMoveYn').val(oConfigureVO.evtLcMoveYn);
					$('input[name="evtLcMoveYn"]').filter('[value="' + oConfigureVO.evtLcMoveYn + '"]').prop('checked', true);

					$('#radsClmt').val(oConfigureVO.radsClmt);
					$('#radsRoute').val(oConfigureVO.radsRoute);
					// radio
					// $('#cnOsvtPlaytimeStopYn').val(oConfigureVO.cnOsvtPlaytimeStopYn);
					$('input[name="cnOsvtPlaytimeStopYn"]').filter('[value="' + oConfigureVO.cnOsvtPlaytimeStopYn + '"]').prop('checked', true);

					// radio
					// $('#fullScreenCloseYn').val(oConfigureVO.fullScreenCloseYn);
					$('input[name="fullScreenCloseYn"]').filter('[value="' + oConfigureVO.fullScreenCloseYn + '"]').prop('checked', true);

					$('#mntrViewLeft').val(oConfigureVO.mntrViewLeft);
					$('#mntrViewRight').val(oConfigureVO.mntrViewRight);
					$('#mntrViewBottom').val(oConfigureVO.mntrViewBottom);
					alert('조회되었습니다.');
				} else {
					alert('해당서비스의 초기값이 없습니다. \n이 서비스를 설정하려면, 해당 서비스 아이디로 접속해 주세요.');
					$('#configureVO').trigger('reset');
				}
			},
			error : function(xhr, status, error) {
				alert('조회실패.');
			}
		});
	};

	this.setDivHdnChk = function(dec) {
		if (!isNaN(dec)) {
			dec = parseInt(dec).toString(2);
			if (dec.length <= 3) {
				dec = oMngDisplay.lpad(dec, 3, '0');
				var isChkDivHdnLeft = dec.substring(0, 1) != '0' ? true : false;
				var isChkDivHdnRight = dec.substring(1, 2) != '0' ? true : false;
				var isChkDivHdnBottom = dec.substring(2, 3) != '0' ? true : false;
				$('#chk-div-hdn-left').prop('checked', isChkDivHdnLeft);
				$('#chk-div-hdn-right').prop('checked', isChkDivHdnRight);
				$('#chk-div-hdn-bottom').prop('checked', isChkDivHdnBottom);
			} else {
				$('#chk-div-hdn-left').prop('checked', true);
				$('#chk-div-hdn-right').prop('checked', true);
				$('#chk-div-hdn-bottom').prop('checked', true);
			}
		} else {
			$('#chk-div-hdn-left').prop('checked', true);
			$('#chk-div-hdn-right').prop('checked', true);
			$('#chk-div-hdn-bottom').prop('checked', true);
			$('#leftDivHdnYn').val('7');
		}
	};

	this.setDivHdnVal = function() {
		var sChkDivHdnLeft = $('#chk-div-hdn-left').is(':checked') ? '1' : '0';
		var sChkDivHdnRight = $('#chk-div-hdn-right').is(':checked') ? '1' : '0';
		var sChkDivHdnBottom = $('#chk-div-hdn-bottom').is(':checked') ? '1' : '0';

		var sBin = sChkDivHdnLeft.concat(sChkDivHdnRight).concat(sChkDivHdnBottom);
		var nDec = parseInt(sBin, 2);

		$('#leftDivHdnYn').val(nDec);
	};

	this.save = function() {
		var isConfirmed = confirm('저장하시겠습니까?');
		if (isConfirmed) {
			var frm = document.configureVO;
			var isValid = validateConfigureVO(frm);
			if (isValid) {
				var sSerialized = $("#configureVO").serialize();
				var sUrl = contextRoot + '/mntr/saveMngDisplay.json';
				$.ajax({
					type : "POST",
					url : sUrl,
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					dataType : "json",
					data : sSerialized,
					success : function(data) {
						if (data.status == '1') {
							alert('저장되었습니다.');
						}
					},
					error : function(xhr, status, error) {
						alert("저장에 실패하였습니다.");
					}
				});
			}
		}
	};

	this.lpad = function(padTarget, padLength, padString) {
		while (padTarget.length < padLength)
			padTarget = padString + padTarget;
		return padTarget;
	};

	this.cancel = function() {
		$('<form/>', {
			'action' : contextRoot + '/mntr/main/main.do'
		}).appendTo(document.body).submit();
	};
}