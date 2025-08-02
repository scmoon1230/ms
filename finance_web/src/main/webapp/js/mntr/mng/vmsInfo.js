// 숫자,쉼표,콜론 제외 공백처리(JSP에서 onkeyup으로 사용)
function inputPlaybackSpeed(ele) {
	ele.value = ele.value.replace(/[^0-9\,\:]/g, '');
}

// 선택상자 첫번째 값으로 리셋
function selectBoxReset(selector) {
	$(`#${selector}`).find('option:eq(0)').prop('selected', true);
}

// 특정 라디오버튼 제외 모든 라디오버튼 리셋
function allRadioBtnResetExpectBtn(expectBtn) {
	var radio_list = [];
	var arr = [];    
	var radio = $("input[type=radio]");

	$.each(radio, function (key, value) {
		radio_list.push($(value).attr('name'));    
	});
	
	arr = $.unique(radio_list.filter((element) => element !== `${expectBtn}`).sort());
	
	for (var i = 0; i < arr.length; i++) {        
		$('input[name="' + arr[i] + '"]').removeAttr('checked');
		$('input[name="' + arr[i] + '"]')[0].checked = true;
	}
}

function attrClass() {
	var elements = document.querySelectorAll('#iDstrtNm');

	elements.forEach(function(element) {
	    if (element.tagName === 'INPUT') {
	        $('#iDstrtNm').val($('#dstrtNmBk').val());
	    } else if (element.tagName === 'SELECT') {
	        $('#iDstrtNm').val($('#dstrtCdBk').val());
	    }
	});
}

function resetAction() {
	$.resetInputObject('.layerRegister .tableType2');
	
	attrClass();
}

function validate() {
	return $.validate('.layerRegister .tableType2');
}

function insertAction(obj) {
	var url = contextRoot + '/mntr/mng/vmsinfo/insert.json';
	var params = 'dstrtCd=' + encodeURIComponent($('#iDstrtNm').val());
	params += '&vmsId=' + encodeURIComponent($('#iVmsId').val());
	params += '&vmsNm=' + encodeURIComponent($('#iVmsNm').val());
	params += '&playBackUseYnLfp=' + encodeURIComponent($('input[name=iPlayBackUseYnLfp]:checked').val() || 'Y');
	params += '&recordingTy=' + encodeURIComponent($('input[name=iRecordingTy]:checked').val() || 'ALL');
	
	if ($('#iPlaybackSpeed').val()) {
    	let aPlaybackSpeed = $('#iPlaybackSpeed').val().replace(/[^0-9\,]/g, '').split(',').map(Number).filter((ele) => ele != 0);
		let $basicPlaybackSpeed = $('#iBasicPlaybackSpeed').val();
		const regex = /^(\d+(,\d+)*)?$/;

	    if (!aPlaybackSpeed.includes(1)) {
	        aPlaybackSpeed.unshift(1);
	    }

		if(!$basicPlaybackSpeed){
    		alert('기본재생배속을 입력해주세요.');
    		return false;
    	}
        	
	    if(!aPlaybackSpeed.includes(Number($basicPlaybackSpeed))){
	    	alert('입력한 기본재생배속이 영상재생지원배속에 존재하지 않습니다.');
	    	return false;
	    }
    	    
	    if(!regex.test(aPlaybackSpeed.toString())){
	    	alert('올바른 형식의 영상재생지원배속을 입력해주세요.');
	    	return false;
	    }

	    params += '&playbackSpeed=' + encodeURIComponent(aPlaybackSpeed.sort((a, b) => a - b).join(',') + ':' + $('#iBasicPlaybackSpeed').val());
	} else {
		params += '&playbackSpeed=' + encodeURIComponent("");
	}
	
	$.ajaxEx($('#grid'), {
		url: url,
		dataType: 'json',
		data: params,
		success: function (data) {
			if (data.session == 1) {
				$('#grid').trigger('reloadGrid');
				alert(data.msg);
				$.hideInsertForm();
			} else {
				alert(data.msg);
			}
			if (typeof data.errors != 'undefined') {
				if (data.errors.length) {
					let sErrorsMsg = '';
					for (let error of data.errors) {
						sErrorsMsg += error.defaultMessage + '\r\n';
					}
					alert(sErrorsMsg);
				}
			}
		},
		error: function (e) {
			alert(e.responseText);
		}
	});
}

function updateAction(obj) {
	var url = contextRoot + '/mntr/mng/vmsinfo/update.json';
	var params = 'dstrtCd=' + encodeURIComponent($('#iDstrtCd').val());
	params += '&vmsId=' + encodeURIComponent($('#iVmsId').val());
	params += '&playBackUseYnLfp=' + encodeURIComponent($('input[name=iPlayBackUseYnLfp]:checked').val() || 'Y');
	params += '&recordingTy=' + encodeURIComponent($('input[name=iRecordingTy]:checked').val() || 'ALL');
	
	if ($('#iPlaybackSpeed').val()) {
    	let aPlaybackSpeed = $('#iPlaybackSpeed').val().replace(/[^0-9\,]/g, '').split(',').map(Number).filter((ele) => ele != 0);
		let $basicPlaybackSpeed = $('#iBasicPlaybackSpeed').val();
		const regex = /^(\d+(,\d+)*)?$/;

	    if (!aPlaybackSpeed.includes(1)) {
	        aPlaybackSpeed.unshift(1);
	    }

		if(!$basicPlaybackSpeed){
    		alert('기본재생배속을 입력해주세요.');
    		return false;
    	}
        	
	    if(!aPlaybackSpeed.includes(Number($basicPlaybackSpeed))){
	    	alert('입력한 기본재생배속이 영상재생지원배속에 존재하지 않습니다.');
	    	return false;
	    }
    	    
	    if(!regex.test(aPlaybackSpeed.toString())){
	    	alert('올바른 형식의 영상재생지원배속을 입력해주세요.');
	    	return false;
	    }

	    params += '&playbackSpeed=' + encodeURIComponent(aPlaybackSpeed.sort((a, b) => a - b).join(',') + ':' + $('#iBasicPlaybackSpeed').val());
	} else {
		params += '&playbackSpeed=' + encodeURIComponent("");
	}
	
	$.ajaxEx($('#grid'), {
	url: url,
	dataType: 'json',
	data: params,
	success: function (data) {
		$('#grid').trigger('reloadGrid');
		alert(data.msg);
		if (typeof data.errors != 'undefined') {
			if (data.errors.length) {
				let sErrorsMsg = '';
				for (let error of data.errors) {
					sErrorsMsg += error.defaultMessage + '\r\n';
				}
				alert(sErrorsMsg);
			}
		}
		},
		error: function (e) {
			alert(e.responseText);
		}
	});
}

function deleteAction(obj) {
	var url = contextRoot + '/mntr/mng/vmsinfo/delete.json';
	var params = 'dstrtCd=' + encodeURIComponent($('#dDstrtCd').text());
	params += '&vmsId=' + encodeURIComponent($('#dVmsId').text());

	$.ajaxEx($('#grid'), {
		url: url,
		dataType: 'json',
		data: params,
		success: function (data) {
			$('#grid').trigger('reloadGrid');
			alert(data.msg);
		},
		error: function (e) {
			alert(e.responseText);
		}
	});
}

$(function () {
    $.jqGrid($('#grid'), {
        url:contextRoot + '/mntr/mng/vmsInfo/list.json',
        datatype: 'json',
        postData: {
            vmsNm: $('#vmsNm').val()
        },
        colNames: [
            '지구명', '지구코드','VMS아이디', 'VMS명', '영상재생지원배속', '영상기본재생배속','과거영상 검출 사용여부', '녹화구분'
        ],
        colModel: [
           {
                name: 'dstrtNm',
                index: 'DSTRT_NM',
                width: 300,
                align: 'center'
            },{
                name: 'dstrtCd',
                index: 'DSTRT_CD',
                width: 300,
                align: 'center',
				hidden: true
            }, {
                name: 'vmsId',
                index: 'VMS_ID',
                width: 500,
                align: 'center'
            }, {
                name: 'vmsNm',
                index: 'VMS_NM',
                width: 500,
                align: 'center'
            }, {
                name: 'playbackSpeed',
                index: 'PLAYBACK_SPEED',
                width: 500,
                align: 'center',
				formatter: function (cellvalue, options, rowObject) {
					if (cellvalue != '' && typeof cellvalue == 'string') {
						var basicPlaybackSpeed = cellvalue.split(':');
                    	if (basicPlaybackSpeed[0] == null) {
							return '';
                    	}
                    	return basicPlaybackSpeed[0];
					} else {
						return '';
					}
                }
            }, { 
				name : 'basicPlaybackSpeed', 
				index : 'BASIC_PLAYBACK_SPEED', 
				width:300, 
				align:'center',
				formatter: function (cellvalue, options, rowObject) {
					if (cellvalue != '' && typeof cellvalue == 'string') {
						var basicPlaybackSpeed = cellvalue.split(':');
                    	if (basicPlaybackSpeed[1] == null) {
							return '';
                    	}
                    	return basicPlaybackSpeed[1];
					} else {
						return '';
					}
                }
			},{
                name: 'playbackUseYnLfp',
                index: 'PLAYBACK_USE_YN_LFP',
                width: 500,
                align: 'center'
            },{
                name: 'recordingTy',
                index: 'RECORDING_TY',
                width: 300,
                align: 'center'
            },
        ],
        rowNum: 1000,
        autowidth: true,
        height: $('#grid').parent().height() - 40,
        shrinkToFit: true,
        sortname: 'DSTRT_NM',
        sortorder: 'ASC',
        jsonReader: {
            page: function (obj) {
                return 1;
            },
            root: function (obj) {
                return obj.rows;
            },
            records: function (obj) {
                return $.showCount(obj);
            }
        },
        onCellSelect: function (rowid, iCol, cellcontent, e) {
            if (iCol == 0) return false;

            var list = jQuery("#grid").getRowData(rowid);

            $('#dDstrtNm').text(list.dstrtNm);
            $('#dDstrtCd').text(list.dstrtCd);
            $('#dVmsId').text(list.vmsId);
            $('#dVmsNm').text(list.vmsNm);
            $('#dPlaybackSpeed').text(list.playbackSpeed);
            $('#dBasicPlaybackSpeed').text(list.basicPlaybackSpeed);
            $('#dPlayBackUseYnLfp').text(list.playbackUseYnLfp);
            $('#dRecordingTy').text(list.recordingTy);

            $('#iDstrtCd').val(list.dstrtCd);
            $('#iVmsId').val(list.vmsId);
            $('#iVmsNm').val(list.vmsNm);
            $('#iPlaybackSpeed').val(list.playbackSpeed);
            $('#iBasicPlaybackSpeed').val(list.basicPlaybackSpeed);

			var iDstrtNmEle = $('#iDstrtNm');

			if (iDstrtNmEle.is('select')) {
			    iDstrtNmEle.val(list.dstrtCd);
			} else if (iDstrtNmEle.is('input')) {
			    iDstrtNmEle.val(list.dstrtNm);
			}

			if(list.playbackUseYnLfp == 'Y'){
				$('input[name=iPlayBackUseYnLfp]:input[value="Y"]').prop('checked', true);
			} else {
				$('input[name=iPlayBackUseYnLfp]:input[value="N"]').prop('checked', true);
			}
			
			if(list.recordingTy == 'ALL'){
				$('input[name=iRecordingTy]:input[value="ALL"]').prop('checked', true);
			} else {
				$('input[name=iRecordingTy]:input[value="PART"]').prop('checked', true);
			}

            $.showDetail();
        },
        beforeRequest: function () {
            var oParams = $('#grid').getGridParam();
            if (oParams.hasOwnProperty('url') && oParams.url == '') return false;
            $.loading(true);
        },
        beforeProcessing: function (data, status, xhr) {
            $.loading(false);
        },
        loadComplete: function (data) {
            $('th#' + this.id + '_check.ui-state-default.ui-th-column.ui-th-ltr').off('click');
            oCommon.jqGrid.checkNodata(undefined, data);
        }
	});
});

$('.btnAGrp').on('click', function () {
	$('#grid_grp_list').setGridParam({
		postData: {
			dstrtCd: $('#iDstrtNm').val()
		}
	}).trigger('reloadGrid');
	
	$.jqGrid('#grid_grp_list', {
        url:contextRoot + '/mntr/mng/vmsinfo/vms_grp_list.json',
        datatype: 'json',
        postData: {
			dstrtCd: $('#iDstrtNm').val()
		},
        colNames: [
            '선택', 'VMS아이디', 'VMS이름'
        ],
        colModel: [
			{
                name: 'rdo', width: 60, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    return '<input type="radio" name="radio_grp_list" data-sys-cd="' + rowObject.sysCd + '" data-sys-nm-ko="' + rowObject.sysNmKo + '" />';
                }
            },
            {name: 'sysCd', index: 'SYS_CD', align: 'center'},
            {name: 'sysNmKo', index: 'SYS_NM_KO', align: 'center'}
        ],
        rowNum: 1000,
        width: 960,
        height: $('#grid_grp_list').parent().height() - 80,
        sortname: 'SYS_CD',
        sortorder: 'ASC',
        jsonReader: {},
        beforeRequest: function () {
            const oPostData = $('#grid_grp_list').jqGrid('getGridParam', 'postData');
            if ((oPostData.hasOwnProperty('sysCd') && oPostData.userId != '')
                && (!oPostData.hasOwnProperty('sysNmKo') || oPostData.grpId == '')) {
                oCommon.jqGrid.setNodata('grp_list', '선택된 VMS 또는 권한이 없습니다.');
                return false;
            }
        },
        onSelectRow: function (rowid, status, e) {
            let $tr = $(this).find('tr#' + rowid);
            let elRadio = $tr.find('input[type="radio"][name="radio_grp_list"]')[0];
            $(elRadio).prop('checked', true);
        },
        loadComplete: function (data) {
            oCommon.jqGrid.loadComplete('grp_list', data, {});
        }
    });

	$('.layerVmsGrp').show();

	$('#maskPrgm').remove();
	$('body').append('<div class="maskPop" id="maskPrgm"></div>');
});

$('.btnSvGrp').on('click', function () {
	var $checked = $('input[name=radio_grp_list]:checked');
	if ($checked.length) {
		var data = $($checked).data();
		
		$('.dVmsId').html(data.sysCd);
		$('.dVmsNm').html(data.sysNmKo);
		$('#iVmsId').val(data.sysCd);
		$('#iVmsNm').val(data.sysNmKo);
	} else {
		alert('선택된 그룹이 없습니다.');
		return false;
	}

	$('.layerRegister').show();
	$('.layerVmsGrp').hide();
	$('#maskPrgm').remove();
});

$('.btnCGrp').on('click', function () {
	$('input[name=radio_grp_list]').removeAttr('checked');
	$('.layerRegister').show();
	$('.layerVmsGrp').hide();
	$('#maskPrgm').remove();
});

$('.btnDt, .btnC').on('click', function () {
	allRadioBtnResetExpectBtn('radio_grp_list');
	$('input[name=radio_grp_list]').removeAttr('checked');
	selectBoxReset('iDstrtNm');
	$('.btnAGrp').show();
	$("#iDstrtNm option").removeAttr("disabled"); 						// selectBox 선택 활성화
});

$('.btnMd').on('click', function () {
	$('.btnAGrp').hide();
	$("#iDstrtNm option").not(":selected").attr("disabled", "disabled"); // selectBox 선택 비활성화
});

$('#iDstrtNm').change(function() {
	$('input[name=radio_grp_list]').removeAttr('checked');
	$('#iVmsId').val('');
	$('#iVmsNm').val('');
});
