$(function () {
    $.jqGrid('#grid_event', {
        url:contextRoot + '/wrks/sstm/mbl/list_event.json',
        datatype: 'json',
        autowidth: true,
        shrinkToFit: true,
        rownumbers: false,
        scrollOffset: 0,
        viewrecords: true,
        postData: {},
        colNames: [
            '이벤트아이디'
            , '이벤트명'
            , '사용유형코드'
            , '사용유형'
            , '시스템명'
        ],
        colModel: [
            {name: 'evtId', index: 'EVT_ID', width: 140, align: 'center'}
            , {name: 'evtNm', index: 'EVT_NM', width: 170, align: 'center'}
            , {name: 'useTyCd', 'hidden': true}
            , {name: 'useTyNm', index: 'USE_TY_NM', width: 170, align: 'center'}
            , {name: 'sysNmKo', 'hidden': true}
        ],
        pager: '#pager',
        rowNum: 1000,
        height: $("#grid_event").parent().height() - 40,
        sortname: 'EVT_NM',
        sortorder: 'ASC',
        viewrecords: true,
        multiselect: true,
        loadonce: false,
        jsonReader: {},
        beforeRequest: function () {
        },
        onCellSelect: function (rowid, iCol, cellcontent, e) {
        },
        beforeProcessing: function (data, status, xhr) {
        },
        beforeSelectRow: function (rowid, e) {
            var $myGrid = $(this);
            var list = $myGrid.getRowData(rowid);
            var postDataGridEventNm = $("#grid_eventNm").jqGrid('getGridParam', 'postData');
            postDataGridEventNm.evtId = list.evtId;
            $("#grid_eventNm").trigger("reloadGrid");

            var postDataGridEventNmPopup = $("#grid_eventNm_popup").jqGrid('getGridParam', 'postData');
            postDataGridEventNmPopup.evtId = list.evtId;
            $("#grid_eventNm_popup").trigger("reloadGrid");

            $("#moblAccTitle").html("모바일리스트 [ " + list.evtNm + " ]");

            // XXX 멀티셀렉트 중복 선택

            if (!$(e.target).closest('td').length) return false;
            var i = $.jgrid.getCellIndex($(e.target).closest('td')[0]);
            var cm = $myGrid.jqGrid('getGridParam', 'colModel');
            if (cm[i].name !== 'cb') {
                var oSelected = $myGrid.jqGrid('getGridParam', 'selarrrow');
                $.each(oSelected, function (i, v) {
                    $myGrid.jqGrid("setSelection", v);
                    $myGrid.jqGrid("resetSelection", v);
                });
            }
            $myGrid.jqGrid("setSelection", rowid);
        },
        loadComplete: function () {
            let $rows = $('#grid_event tr.jqgrow[role=row]');
            if ($rows.length) $('#grid_event').jqGrid('setSelection', $rows.get(0).id);
            var $myGrid = $(this);
            if ($rows.length == 0) {
                return false;
            } else {
                var list = $myGrid.getRowData($rows.get(0).id);
            }
            var postDataGridEventNm = $("#grid_eventNm").jqGrid('getGridParam', 'postData');
            postDataGridEventNm.evtId = list.evtId;
            var postDataGridEventNmPopup = $("#grid_eventNm_popup").jqGrid('getGridParam', 'postData');
            postDataGridEventNmPopup.evtId = list.evtId;
            $("#grid_eventNm_popup").trigger("reloadGrid");
            $("#moblAccTitle").html("모바일리스트 [ " + list.evtNm + " ]");
            $("#grid_eventNm").trigger("reloadGrid");
        }
    });

    $(".searchBtn").bind('click', function () {
        var myPostData = $("#grid_event").jqGrid('getGridParam', 'postData');
        myPostData.evtId = $("#sEvtId").val();
        myPostData.evtNm = $("#sEvtNm").val();
        $("#grid_event").trigger("reloadGrid");
    });

    $.jqGrid('#grid_eventNm', {
        url:contextRoot + '/wrks/sstm/mbl/list_eventNm.json',
        datatype: "json",
        autowidth: true,
        shrinkToFit: true,
        rownumbers: false,
        scrollOffset: 0,
        viewrecords: true,
        postData: {},
        colNames: [
            '이벤트아이디' , '모바일아이디' , '모바일번호' , '이름' , '소속명' , 'PUSH' , 'SMS' , '사용여부' , '그룹아이디'
        ],
        colModel: [
            {name: 'evtId', 'hidden': true}
            , {name: 'moblId', 'hidden': true}
            , {name: 'moblNo', index: 'MOBL_NO', width: 120, align: 'center'}
            , {name: 'userNm', index: 'USER_NM', width: 70, align: 'center'}
            , {name: 'userPsitnNm', index: 'USER_PSITN_NM', width: 80, align: 'center'}
            , {name: 'pushSendTyCd', index: 'PUSH_SEND_TY_CD', width: 100, align: 'center', editable: true, edittype: "select", editoptions: {value: ""}, useroption: sendTyCd, formatter: $.GridSelectBox}
            , {name: 'smsSendTyCd', index: 'SMS_SEND_TY_CD', width: 100, align: 'center', editable: true, edittype: "select", editoptions: {value: ""}, useroption: sendTyCd, formatter: $.GridSelectBox}
            , {name: 'useYn', index: 'USE_YN', width: 100, align: 'center', editable: true, edittype: "select", editoptions: {value: ""}, useroption: useTyCd, formatter: $.GridSelectBox}
            , {name: 'grpId', width: 80, align: 'left', 'hidden': true},
        ],
        pager: '#pager',
        rowNum: 1000,
        height: $("#grid_eventNm").parent().height() - 40,
        sortname: 'USER_NM',
        sortorder: 'ASC',
        viewrecords: true,
        multiselect: true,
        loadonce: false,
        jsonReader: {},
        onCellSelect: function (rowid, iCol, cellcontent, e) {
        },
        beforeRequest: function () {
            var oParams = $('#grid_eventNm').getGridParam();
            if (oParams.hasOwnProperty('postData') && !oParams.postData.hasOwnProperty('evtId')) {
                return false;
            }
        },
        beforeProcessing: function (data, status, xhr) {
            oCommon.jqGrid.setSelectionOnChangeSelect(this, 'beforeProcessing');
        },
        loadComplete: function (data) {
            $('#grid_eventNm SELECT').selectBox();
            $('.selectBox').css('width', '100px');
            $('.selectBox-label').css('width', '100px');
            oCommon.jqGrid.setSelectionOnChangeSelect(this, 'loadComplete');
        },
        beforeSelectRow: function (rowId, e) {
            return  oCommon.jqGrid.preventSetSelectionOnSelectRow(this, rowId, e);
        }
    });

    $.jqGrid('#grid_eventNm_popup', {
        // url:contextRoot + '/wrks/sstm/mbl/list_eventNm_popup.json',
        datatype: "json",
        autowidth: true,
        shrinkToFit: true,
        rownumbers: false,
        scrollOffset: 0,
        viewrecords: true,
        postData: {},
        colNames: [
            '이벤트아이디' , '모바일아이디' , '모바일번호' , '이름' , '소속명' , 'PUSH' , 'SMS' , '사용여부' , '그룹아이디'
        ],
        colModel: [
            {name: 'evtId', 'hidden': true}
            , {name: 'moblId', 'hidden': true}
            , {name: 'moblNo', index: 'MOBL_NO', width: 150, align: 'left'}
            , {name: 'userNm', index: 'USER_NM', width: 100, align: 'left'}
            , {name: 'userPsitnNm', index: 'USER_PSITN_NM', width: 100, align: 'left'}
            , {name: 'pushSendTyCd', sortable: false, index: 'PUSH_SEND_TY_CD', width: 120, align: 'center', editable: true, edittype: "select", editoptions: {value: ""}, useroption: sendTyCd, formatter: $.GridSelectBox}
            , {name: 'smsSendTyCd', sortable: false, index: 'SMS_SEND_TY_CD', width: 120, align: 'center', editable: true, edittype: "select", editoptions: {value: ""}, useroption: sendTyCd, formatter: $.GridSelectBox}
            , {name: 'useYn', sortable: false, index: 'USE_YN', width: 120, align: 'center', editable: true, edittype: "select", editoptions: {value: ""}, useroption: useTyCd, formatter: $.GridSelectBox}
            , {name: 'grpId', 'hidden': true}
        ],
        pager: '#pager',
        rowNum: 1000,
        height: $("#grid_eventNm_popup").parent().height() - 40,
        sortname: 'MOBL_NO',
        sortorder: 'ASC',
        viewrecords: true,
        multiselect: true,
        loadonce: false,
        jsonReader: {},
        onCellSelect: function (rowid, iCol, cellcontent, e) {
        },
        beforeRequest: function () {
            var oParams = $('#grid_eventNm').getGridParam();
            if (oParams.hasOwnProperty('postData') && !oParams.postData.hasOwnProperty('evtId')) {
                return false;
            }
        },
        beforeProcessing: function (data, status, xhr) {
        },
        loadComplete: function (data) {
            $('#grid_eventNm_popup SELECT').selectBox();
            $('.selectBox').css('width', '100px');
            $('.selectBox-label').css('width', '100px');
        },
        beforeSelectRow: function (rowId, e) {
            return  oCommon.jqGrid.preventSetSelectionOnSelectRow(this, rowId, e);
        }
    });

    $(".btnupdate").bind('click', function () {
        var oSelarrrow = $("#grid_eventNm").jqGrid('getGridParam', 'selarrrow');
        var oEventCheck = $("#grid_event").jqGrid('getGridParam', 'selarrrow');

        var oRowEvtId = [];
        for (var i = 0; i < oEventCheck.length; i++) {
            var list = jQuery("#grid_event").getRowData(oEventCheck[i]);
            oRowEvtId.push(list.evtId);
        }
        if (oEventCheck.length == 0) {
            alert('이벤트를 선택해주세요.');
        }
        if (oSelarrrow.length == 0) {
            alert('모바일리스트를 선택해주세요.');
        }
        if (oEventCheck.length == 0 && oSelarrrow.length >= 1) {
            return false;
        }
        if (oSelarrrow.length == 0 && oEventCheck.length >= 1) {
            return false;
        } else {
            var oParams = [];
            $.each(oSelarrrow, function (index, rowId) {
                const oRowData = $("#grid_eventNm").jqGrid('getRowData', rowId);
                var sPushSendTyCd = $.getCustomObjectValue("#grid_eventNm", "select", "pushSendTyCd", rowId);
                var sSmsSendTyCd = $.getCustomObjectValue("#grid_eventNm", "select", "smsSendTyCd", rowId);
                var sUseTy = $.getCustomObjectValue("#grid_eventNm", "select", "useYn", rowId);

                oRowData.pushSendTyCd = encodeURIComponent(sPushSendTyCd);
                oRowData.smsSendTyCd = encodeURIComponent(sSmsSendTyCd);
                oRowData.useYn = encodeURIComponent(sUseTy);
                oParams.push(oRowData);
            });
            $.ajax({
                url: 'updatembl.json',
                type: 'POST',
                traditional: true,
                datatype: 'json',
                data: {
                    data: JSON.stringify(oParams)
                    , oRowEvtId: JSON.stringify(oRowEvtId)
                },
                success: function (data) {
                    $("#grid_eventNm").trigger("reloadGrid");
                    alert('수정 되었습니다.');
                },
                error: function (data) {
                    console.log(data);
                },
                beforeSend: function (xhr) {
                }
            });
        }
    });
    $(".btndelete").bind('click', function () {
        var oEventCheck = $("#grid_event").jqGrid('getGridParam', 'selarrrow');
        var oSelarrrow = $("#grid_eventNm").jqGrid('getGridParam', 'selarrrow');

        var oRowEvtId = [];
        for (var i = 0; i < oEventCheck.length; i++) {
            var list = jQuery("#grid_event").getRowData(oEventCheck[i]);
            oRowEvtId.push(list.evtId);
        }

        if (oEventCheck.length == 0) {
            alert('이벤트를 선택해주세요.');
        } else if (oSelarrrow.length == 0) {
            alert('모바일리스트를 선택해주세요.');
        } else {
            var oParams = [];
            $.each(oSelarrrow, function (index, rowId) {
                const oRowData = $("#grid_eventNm").jqGrid('getRowData', rowId);
                oParams.push(oRowData);
            });
            if (confirm("정말 삭제하시겠습니까 ?") == true) {
                $.ajax({
                    url: 'deletembl.json',
                    type: 'POST',
                    traditional: true,
                    datatype: 'json',
                    data: {
                        data: JSON.stringify(oParams)
                        , oRowEvtId: JSON.stringify(oRowEvtId)
                    },
                    success: function (data) {
                        $("#grid_eventNm").trigger("reloadGrid");
                        $("#grid_eventNm_popup").trigger("reloadGrid");
                        alert('삭제 되었습니다.');
                    },
                    error: function (data) {
                        console.log(data);
                    },
                    beforeSend: function (xhr) {
                    }
                });
            } else {
                return false;
            }
        }
    });
    $(".btnRgt").bind('click', function () {
        var oEventCheck = $("#grid_event").jqGrid('getGridParam', 'selarrrow');
        if (oEventCheck.length == 0) {
            alert('이벤트를 선택해주세요.');
            $('.mask').hide();
            $('#div_drag_2').hide();
        } else {
            $('#div_drag_2').show();
        }

        var oEventCheck = $("#grid_event").jqGrid('getGridParam', 'selarrrow');
        console.log(oEventCheck.length);
        if (oEventCheck.length > 1) {
            var postDataGridEventNmPopup = $("#grid_eventNm_popup").jqGrid('getGridParam', 'postData');
            postDataGridEventNmPopup.evtId = "";
        }

        $("#grid_eventNm_popup").jqGrid('setGridParam', {
            url:contextRoot + '/wrks/sstm/mbl/list_eventNm_popup.json',
            postData: {}
        }).trigger("reloadGrid");
    });
    $('.btnSv').bind('click', function () {
        var oEventCheck = $("#grid_event").jqGrid('getGridParam', 'selarrrow');
        var oRowEvtId = [];
        for (var i = 0; i < oEventCheck.length; i++) {
            var list = jQuery("#grid_event").getRowData(oEventCheck[i]);
            oRowEvtId.push(list.evtId);
        }
        var oEvtId = $('#grid_eventNm_popup').getGridParam().postData.evtId;
        var oSelarrrow = $("#grid_eventNm_popup").jqGrid('getGridParam', 'selarrrow');

        if (oSelarrrow.length == 0) {
            alert('사용자를 선택해주세요.');
        } else {
            var oParams = [];
            $.each(oSelarrrow, function (index, rowId) {
                const oRowData = $("#grid_eventNm_popup").jqGrid('getRowData', rowId);
                var sPushSendTyCd = $.getCustomObjectValue("#grid_eventNm_popup", "select", "pushSendTyCd", rowId);
                var sSmsSendTyCd = $.getCustomObjectValue("#grid_eventNm_popup", "select", "smsSendTyCd", rowId);
                var sUseTy = $.getCustomObjectValue("#grid_eventNm_popup", "select", "useYn", rowId);
                oRowData.pushSendTyCd = encodeURIComponent(sPushSendTyCd);
                oRowData.smsSendTyCd = encodeURIComponent(sSmsSendTyCd);
                oRowData.useYn = encodeURIComponent(sUseTy);
                oRowData.evtId = oEvtId;
                oParams.push(oRowData);
            });
            $.ajax({
                url: 'insetMblUser.json',
                type: 'POST',
                traditional: true,
                datatype: 'json',
                data: {
                    data: JSON.stringify(oParams)
                    , oRowEvtId: JSON.stringify(oRowEvtId)
                },
                success: function (data) {
                    $("#grid_eventNm").trigger("reloadGrid");
                    $("#grid_eventNm_popup").trigger("reloadGrid");
                    alert('저장 되었습니다.');
                    $(".mask").hide();
                },
                error: function (data) {
                    console.log(data);
                },
                beforeSend: function (xhr) {
                }
            });
        }
    });
});
