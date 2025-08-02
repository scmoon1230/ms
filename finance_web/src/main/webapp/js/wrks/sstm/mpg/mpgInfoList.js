$(function () {
    $.jqGrid($('#grid'), {
        url:contextRoot + '/wrks/sstm/mpg/mpgInfoListData.json',
        datatype: 'json',
        postData: {
            networkNm: $('#networkNm').val(),
            networkTy: $('#networkTy').val(),
            dstrtCd: $('#searchDstrtCd option:selected').val()
        },
        colNames: [
            '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">', '네트워크구분', 'networkTy', '네트워크아이디', '네트워크명', '네트워크주소', '망매핑주소', '지구', 'dstrtCd'
        ],
        colModel: [
            {name: 'check', width: 60, align: 'center', editable: true, edittype: 'checkbox', editoptions: {value: 'True:False'}, sortable: false, formatter: $.GridCheckBox},
            {name: 'networkTyNm', index: 'NETWORK_TY_NM', width: 120, align: 'center'},
            {name: 'networkTy', hidden: true},
            {name: 'networkId', index: 'NETWORK_ID', width: 120, align: 'center'},
            {name: 'networkNm', index: 'NETWORK_NM', width: 120, align: 'center'},
            {name: 'networkIp', index: 'NETWORK_IP', width: 250, align: 'center'},
            {name: 'networkMpIp', index: 'NETWORK_MP_IP', width: 250, align: 'center'},
            {name: 'dstrtNm', index: 'DSTRT_NM', width: 100, align: 'center'},
            {name: 'dstrtCd', hidden: true},
        ],
        rowNum: $('#rowPerPageList').val(),
        autowidth: true,
        shrinkToFit: true,
        height: $('#grid').parent().height() - 40,
        sortname: 'NETWORK_TY_NM',
        sortorder: 'ASC',
        jsonReader: {
            id: 'networkId',
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
            let oRowData = $('#grid').getRowData(rowid);
            $('#dNetworkTyNm').text(oRowData.networkTyNm);
            $('#dNetworkId').text(oRowData.networkId);
            $('#dNetworkNm').text(oRowData.networkNm);
            $('#dNetworkIp').text(oRowData.networkIp);
            $('#dNetworkMpIp').text(oRowData.networkMpIp);
            $('#dDstrtNm').text(oRowData.dstrtNm);

            $.selectBarun("#iNetworkTy", oRowData.networkTy);
            // PK
            $('#iNetworkId').val(oRowData.networkId);
            $('#iNetworkId').prop('readonly', true);
            $('#iNetworkNm').val(oRowData.networkNm);
            $('#iNetworkIp').val(oRowData.networkIp);
            $('#iNetworkMpIp').val(oRowData.networkMpIp);
            // PK
            $.selectBarun("#iDstrtCd", oRowData.dstrtCd);
            $('#iDstrtCd').prop('disabled', true);

            const $required = $('#div_drag_2 input[required="required"]');
            $required.removeClass('has-error');
            $.showDetail();
        },
        beforeRequest: function () {
            let oParams = $('#grid').getGridParam();
            if (oParams.hasOwnProperty('url') && oParams.url == '') return false;
            $.loading(true);
        },
        beforeProcessing: function (data, status, xhr) {
            $.loading(false);
            if (typeof data.rows != 'undefine' || data.row != null) $.makePager('#grid', data, $('#grid').getGridParam('page'), $('#rowPerPageList').val());
        },
        loadComplete: function (data) {
            $('th#' + this.id + '_check.ui-state-default.ui-th-column.ui-th-ltr').off('click');
            oCommon.jqGrid.loadComplete(undefined, data, {});
        }
    });

    $('.btnS').on('click', function () {
        $('#grid').setGridParam({
            rowNum: $('#rowPerPageList').val(),
            postData: {
                networkNm: $('#networkNm').val(),
                networkTy: $('#networkTy').val(),
                dstrtCd: $('#searchDstrtCd option:selected').val()
            }
        }).trigger('reloadGrid');
    });

	$('#div_drag_2 input[required="required"]').on('keyup',function(){
		fn_trim_space();
	});

});

function resetAction() {
    $.selectBarun("#iNetworkTy", '');
    // PK
    $('#iNetworkId').val('');
    $('#iNetworkId').prop('readonly', false);
    $('#iNetworkNm').val('');
    $('#iNetworkIp').val('');
    $('#iNetworkMpIp').val('');
    // PK
    $.selectBarun("#iDstrtCd", '');
    $('#iDstrtCd').prop('disabled', false);

    const $required = $('#div_drag_2 input[required="required"]');
    $required.removeClass('has-error');
}

function fn_trim_space() {
	if (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 39 || event.keyCode == 46) return;
    event.target.value = event.target.value.replaceAll(' ', '');
}

function validate() {
    let isValid = false;
    let sRequired = '';
    const $required = $('#div_drag_2 input[required="required"]');
    $required.removeClass('has-error');
    let hasFocus = false;
    $.each($required, function (index, input) {
        const sValue = $(input).val() || '';
        if (!sValue) {
            const sTitle = $(input).attr('title');
            if (sRequired != '') {
                sRequired += ', ' + sTitle;
            } else {
                sRequired = sTitle;
            }
            $(input).addClass('has-error');
            if (!hasFocus) {
                $(input).focus();
                hasFocus = true;
            }
        }
    });
    if ('' != sRequired) {
        alert(sRequired + '는 필수 항목입니다.');
    } else {
        isValid = true;
    }
    return isValid;
}

function updateAction() {
    if (confirm('저장하시겠습니까?')) {
        $.ajaxEx($('#grid'), {
            type: 'POST',
            url:contextRoot + '/wrks/sstm/mpg/mpgInfoList/update.json',
            dataType: 'json',
            data: {
                networkId: $('#iNetworkId').val(),
                networkTy: $('#iNetworkTy option:selected').val(),
                networkNm: $('#iNetworkNm').val(),
                networkIp: $('#iNetworkIp').val(),
                networkMpIp: $('#iNetworkMpIp').val(),
                dstrtCd: $('#iDstrtCd option:selected').val()
            },
            success: function (data) {
                $('#grid').setGridParam({
                    page: $('#cur-page').val()
                });
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
}

function insertAction() {
    if (confirm('저장하시겠습니까?')) {
        $.ajaxEx($('#grid'), {
            type: 'POST',
            url:contextRoot + '/wrks/sstm/mpg/mpgInfoList/insert.json',
            dataType: 'json',
            data: {
                networkId: $('#iNetworkId').val(),
                networkTy: $('#iNetworkTy option:selected').val(),
                networkNm: $('#iNetworkNm').val(),
                networkIp: $('#iNetworkIp').val(),
                networkMpIp: $('#iNetworkMpIp').val(),
                dstrtCd: $('#iDstrtCd option:selected').val()
            },
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
}

function deleteAction() {
    $.ajaxEx($('#grid'), {
        type: 'POST',
        url:contextRoot + '/wrks/sstm/mpg/mpgInfoList/delete.json',
        dataType: 'json',
        data: {
            networkId: $('#iNetworkId').val(),
            dstrtCd: $('#iDstrtCd option:selected').val()
        },
        success: function (data) {
            $('#grid').trigger('reloadGrid');
            alert(data.msg);
        },
        error: function (e) {
            alert(data.msg);
        }
    });
}

function deleteMultiAction() {
    let s = $.getSelRow('#grid');
    if (s.length == 0) {
        alert('삭제할 데이터를 선택하십시오.');
        return false;
    }

    if (confirm('선택된 자료를 삭제하시겠습니까?') == false) return false;

    let params = '';
    for (let i = 0; i < s.length; i++) {
        let list = $('#grid').getRowData(s[i]);
        params += '&networkId=' + list.networkId + '&dstrtCd=' + list.dstrtCd;
    }
    $.ajaxEx($('#grid'), {
        type: 'POST',
        url:contextRoot + '/wrks/sstm/mpg/mpginfoList/deleteMulti.json',
        dataType: 'json',
        data: params,
        success: function (data) {
            $('#grid').setGridParam({
                page: $('#cur-page').val()
            }).trigger('reloadGrid');
            alert(data.msg);
        },
        error: function (e) {
            alert(data.msg);
        }
    });
    return true;
}
