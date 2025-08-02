<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${rootPath}/css/mntr/resetCss.css"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap.min.css' />">
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap-dialog.min.css' />">
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap-toggle.min.css' />">
    <link rel="stylesheet" type="text/css" href="${rootPath}/js/bootstrap/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" type="text/css" href="${rootPath}/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" type="text/css" href="${rootPath}/js/mntr/jqGrid/css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="${rootPath}/css/mntr/mntr.css"/>
</head>
<body>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<table id="grid-fcltStatus"></table>
<div id="paginate-fcltStatus" class="paginate text-center"></div>
<button id="btn-save-excel" onclick="oFcltStatusPopup.excel();" class="btn btn-sm btn-secondary">엑셀저장</button>
<form name="ExcellistForm" method="post">
    <input type="hidden" name="searchFcltKndCd" id="searchFcltKndCd" value="${params.searchFcltKndCd}"/>
    <input type="hidden" name="searchFcltUsedTyCd" id="searchFcltUsedTyCd" value="${params.searchFcltUsedTyCd}"/>
    <input type="hidden" name="searchFcltSttus" id="searchFcltSttus" value="${params.searchFcltSttus}"/>
    <input type="hidden" name="fcltKndNm" id="fcltKndNm" value="${params.fcltKndNm}"/>
</form>
<iframe id="if_Hidden" name="if_Hidden" width="0" height="0" marginHeight="0" marginWidth="0" frameborder="0" scrolling="no"></iframe>
<script src="${rootPath}/js/mntr/jquery/jquery.js"></script>
<script src="${rootPath}/js/mntr/jqGrid/jquery.jqGrid.src.js"></script>
<script src="${rootPath}/js/mntr/jqGrid/i18n/grid.locale-kr.js"></script>
<script src="${rootPath}/js/mntr/jquery/jquery.twbsPagination.min.js"></script>
<script src="${rootPath}/js/mntr/cmm.js"></script>
<script>
    $(function () {
        oFcltStatusPopup = new fcltStatusPopup();
        oFcltStatusPopup.grid();
    });

    function fcltStatusPopup() {
        this.grid = function () {
            var searchFcltKndCd = $('#searchFcltKndCd').val();
            var searchFcltSttus = $('#searchFcltSttus').val();
            var searchFcltUsedTyCd = $('#searchFcltUsedTyCd').val();

            $('#grid-fcltStatus').jqGrid({
                url: contextRoot + '/mntr/fcltList.json',
                datatype: 'json',
                mtype: 'POST',
                height: 'auto',
                autowidth: true,
                rowNum: 10,
                beforeRequest: function () {
                    // validate check here!
                },
                postData: {
                    searchFcltKndCd: searchFcltKndCd,
                    searchFcltUsedTyCd: searchFcltUsedTyCd,
                    searchFcltSttus: searchFcltSttus
                },
                loadComplete: function (data) {
                    checkGridNodata('fcltStatus', data);
                    pagenationReload('fcltStatus', data);
                },
                colNames: [
                    'pointX', 'pointY', 'ID', '시설물명', 'UID', '종류', '용도', '상태'
                ],
                colModel: [
                    {
                        name: 'pointX',
                        index: 'POINT_X',
                        hidden: true
                    }, {
                        name: 'pointY',
                        index: 'POINT_Y',
                        hidden: true
                    }, {
                        name: 'fcltId',
                        index: 'FCLT_ID',
                        align: 'center',
                        classes: 'jqgrid_cursor_pointer',
                        cellattr: function () {
                            return 'style="width: 20%;"'
                        },
                        width: 160
                    }, {
                        name: 'fcltLblNm',
                        index: 'FCLT_LBL_NM',
                        align: 'center',
                        classes: 'jqgrid_cursor_pointer',
                        cellattr: function () {
                            return 'style="width: 20%;"'
                        },
                        width: 160
                    }, {
                        name: 'fcltUid',
                        align: 'center',
                        classes: 'jqgrid_cursor_pointer',
                        cellattr: function () {
                            return 'style="width: 20%;"'
                        },
                        width: 160
                    }, {
                        name: 'fcltKndNm',
                        index: 'FCLT_KND_NM',
                        hidden: true
                    }, {
                        name: 'fcltUsedTyNm',
                        index: 'FCLT_USED_TY_NM',
                        align: 'center',
                        classes: 'jqgrid_cursor_pointer',
                        cellattr: function () {
                            return 'style="width: 10%;"'
                        },
                        width: 80
                    }, {
                        name: 'fcltSttusNm',
                        index: 'FCLT_STTUS_NM',
                        hidden: true
                    }
                ],
                jsonReader: {
                    root: "rows",
                    total: "totalPages",
                    records: function (obj) {
                        $('#rowCnt').text(obj.totalRows);
                        if (obj.totalRows == '0') {
                            $('#sendBtn').prop('disabled', true);
                        }
                        return obj.totalRows;
                    }
                },
                onSelectRow: function (row) {
                    var rowData = $('#grid-popupFcltStatus').getRowData(row);
                },
                cmTemplate: {
                    sortable: false
                }
            });
            $(".ui-jqgrid-sortable").css("cursor", "default");
        };

        this.excel = function () {
            document.ExcellistForm.target = "if_Hidden";
            document.ExcellistForm.action = '<c:url value="/mntr/fcltStatusExcel.do" />';
            document.ExcellistForm.submit();
        };
    }
</script>
</body>
</html>
