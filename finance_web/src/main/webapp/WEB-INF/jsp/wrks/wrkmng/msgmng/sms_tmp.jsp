<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">
$(document).ready(function(){

    $.jqGrid($('#grid'), {
        url: '<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/list.json',
        datatype: "json",
        autowidth: true,
        postData: {
            smsTrmsTyCd : $("#sSmsTrmsTyCd").val(),
            smsNtfctTyCd : $("#sSmsNtfctTyCd").val(),
            smsTrmsNm : $("#sSmsTrmsNm").val(),
            smsTrmsConts : $("#sSmsTrmsConts").val(),
            strDateStart : $("#strDateStart").val(),
            strDateEnd : $("#strDateEnd").val()
        },
        colNames: [ '상태', '전송일시', '공지구분', '전송자', '전송 내용', 'SMS아이디', 'SMS전송유형코드', 'SMS공지유형코드'
					, 'SMS송신자아이디', 'SMS송신상태', 'SMS수신자아이디', '등록자', '등록일자', '수정자', '수정일'
                   ],
        colModel: [
                { name: 'SMS_TRMS_TY_NM', width:110, align:'center'},
                { name: 'SMS_RSVT_TRMS_YMD_HMS', width:200, align:'center'},
                { name: 'SMS_NTFCT_TY_NM', width:100, align:'center'},
                { name: 'SMS_TRMS_NM', width:100, align:'center'},
                { name: 'SMS_TRMS_CONTS', width:470, align:'center'},
                { name: 'SMS_ID', width:100, align:'center', hidden:true},
                { name: 'SMS_TRMS_TY_CD', width:100, align:'center', hidden:true},
                { name: 'SMS_NTFCT_TY_CD', width:100, align:'center', hidden:true},
                { name: 'SMS_TRMS_ID', width:100, align:'center', hidden:true},
                { name: 'SMS_TRMS_STTUS', width:100, align:'center', hidden:true},
                { name: 'SMS_RCV_ID', width:100, align:'center', hidden:true},
                { name: 'RGS_USER_ID', width:100, align:'center', hidden:true},
                { name: 'RGS_DATE', width:100, align:'center', hidden:true},
                { name: 'UPD_USER_ID', width:100, align:'center', hidden:true},
                { name: 'UPD_DATE', width:100, align:'center', hidden:true}
          ],
        pager: '#pager',
        rowNum: $('#rowPerPageList').val(),
        sortname: 'SMS_RSVT_TRMS_YMD_HMS',
        sortorder: 'DESC',
        viewrecords: true,
        multiselect: false,
        shrinkToFit: true,
        scrollOffset: 0,
        autowidth: true,
        loadonce:false,
        jsonReader: {
            id: "SMS_ID",
            root: function(obj) { return obj.rows; },
            page: function(obj) { return 1; },
            total: function(obj) {
                if(obj.rows.length > 0) {
                    var page  = obj.rows[0].ROWCNT / rowNum;
                    if( (obj.rows[0].ROWCNT % rowNum) > 0)
                        page++;
                    return page;
                }
                else
                    return 1;
            },
            records: function(obj) { return $.showCount(obj); }
        },
        onSelectRow: function(rowid, status, e) {
            var list = jQuery("#grid").getRowData(rowid);
            $("#dSmsTrmsTyNm").html(list.SMS_TRMS_TY_NM);
            $("#dSmsTrmsNm").html(list.SMS_TRMS_NM);
            $("#dSmsNtfctTyNm").html(list.SMS_NTFCT_TY_NM);
            $("#dSmsRsvtTrmsYmdHms").html(list.SMS_RSVT_TRMS_YMD_HMS);
            $("#dRgsDate").html(list.RGS_DATE);
            $("#dSmsTrmsConts").html(list.SMS_TRMS_CONTS);

            $("#grid_list_rcv_temp").setGridParam({url : "<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/list_rcv.json"});
            var myPostData = $("#grid_list_rcv_temp").jqGrid('getGridParam', 'postData');
            myPostData.smsId = list.SMS_ID;
            $("#grid_list_rcv_temp").trigger("reloadGrid");

            // 등록폼에다 값 세팅
            $("#iSmsNtfctTyCd").val(list.SMS_NTFCT_TY_CD);
            $("#iSmsTrmsConts").val(list.SMS_TRMS_CONTS);
            $("#iSmsId").val(list.SMS_ID);

            //if (list.SMS_TRMS_STTUS == 'Y') {
            if (list.SMS_TRMS_TY_CD == 'C') {
            	$(".btnMd").hide();
            	$(".btnDe").hide();
            	$(".btnRealSend").hide();
            	$(".btnSmsSend").hide();
            } else {
            	$(".btnMd").show();
                $(".btnDe").show();
                $(".btnRealSend").show();
            	$(".btnSmsSend").show();

            }

            $.showDetail();
        },
        beforeRequest: function() {
            $.loading(true);
            rowNum = $('#rowPerPageList').val();
        },
        beforeProcessing: function(data, status, xhr){
            $.loading(false);
            if(typeof data.rows != "undefine" || data.row != null) {
                $.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
            }
        }
    });

    /* todo 업무담당자 목록 리스트 그리드 (요청페이지)*/
    $.jqGrid($('#grid_grp_list_inst'), {
        url: '<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/grpList.json',
        datatype: "json",
        postData: {
            checkRcvId : ""
        },
        colNames: [ /* '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_user_list_inst\', this, event);">' */
                    '' , '지구명' , '지구코드' , '부모지구코드' , '그룹아이디' , '그룹아이디KEY' , '그룹명(사용자)'
                    , '아이디 / 부서 / 직급 / 담당업무' , '권한레벨' , '권한레벨명' , 'LVL' , 'ISLF' , '전화번호' , ''
        ],
        colModel: [ /* { name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false
                          , formatter:function (cellValue, option) {
                            return '<input type="checkbox" id="'+ option.rowId +'" value="' + option.rowId + '"/>';
                        }
                    }, */
                    { name: 'TAG', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false
                          , formatter:function (cellValue, option) {
                            var checked = "";
                            if(cellValue == 'true'){ checked = "checked"; }
                            return '<input type="checkbox" id="'+ option.rowId +'" value="' + option.rowId + '" ' + checked + '/>';
                        }
                    },
                    { name: 'DSTRT_NM', width:200, align:'center', hidden:true},
                    { name: 'DSTRT_CD', width:200, align:'center', hidden:true},
                    { name: 'PRNT_GRP_ID', width:200, align:'center', hidden:true},
                    { name: 'GRP_ID', width:200, align:'center', hidden:true, key:true},
                    { name: 'GRP_ID_KEY', width:200, align:'center', hidden:true},
                    { name: 'GRP_NM_KO_MOBL_NO', width:250, align:'left'},
                    { name: 'GRP_NM_KO_DESC', width:450, align:'center'},
                    { name: 'AUTH_LVL', width:200, align:'center', hidden:true},
                    { name: 'AUTH_LVL_NM', width:200, align:'center', hidden:true},
                    { name: 'LVL', width:200, align:'center', hidden:true},
                    { name: 'ISLE', width:200, align:'center', hidden:true},
                    { name: 'MOBL_NO', width:200, align:'center', hidden:true},
                    { name: 'GRP_NM_KO', width:250, align:'left', hidden:true}
        ],
        treeGrid: true,
        treeGridModel: "adjacency",
        caption: "",
        ExpandColumn: "GRP_NM_KO_MOBL_NO",
        tree_root_level:0,
        rowNum: 10000,
        ExpandColClick: true,
        autowidth: true,
        treeIcons: {leaf:'ui-icon-document-b'},
        treeReader:{
            leaf_field :'ISLF', //확장 화살표 여부(true:확장,false:최하단 레벨 이므로 확장 안함)
            level_field: 'LVL',        //---level값
            parent_id_field : 'PRNT_GRP_ID',   //---부모id값
            expanded_field: 'EXPD' //열린상태로 로딩할지 여부
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
                $("#grid_grp_list_inst input[id="+ rowid +"]").prop("checked", true);
        },
        beforeRequest: function() {
            $.loading(true);
            rowNum = $('#rowPerPageList').val();
        },
        beforeProcessing: function(data, status, xhr){
        },
        loadComplete : function(v) { /* 데이터 로딩이 끝난후 호출할 함수*/
            //$.bindTreeExpand("#grid", grid_param);
            $.loading(false);
        }
    });

    $.jqGrid('#grid_list_rcv_temp', {
        //url: '<c:url value='/'/>wrks/wrkmng/msgmng/sms/list_rcv.json',
        datatype: "json",
        postData: {
            smsId : $("#smsId").val()
        },
        colNames: [    '아이디',		'사용자명',		'전화번호',
                   ],
        colModel: [
                    { name: 'SMS_RCV_ID', width:200, align:'center'},
                    { name: 'SMS_RCV_NM', width:200, align:'center'},
                    { name: 'SMS_RCV_MOBL_NO', width:350, align:'center'},
                    ],
        pager: '#pager',
        rowNum : 1000,
        sortname: 'SMS_RCV_NM',
        sortorder: 'ASC',
        viewrecords: true,
        autowidth: true,
        multiselect: false,
        loadonce:false,
        jsonReader: {
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
            if(iCol == 0) return false;
        }

    });

    $(".btnS").bind("click",function(){
        if ($.validate(".tableTypeHalf.seachT") == false) return;

        $("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
        var myPostData = $("#grid").jqGrid('getGridParam', 'postData');

        //검색할 조건의 값을 설정한다.
        myPostData.smsTrmsTyCd = $("#sSmsTrmsTyCd").val();
        myPostData.smsNtfctTyCd = $("#sSmsNtfctTyCd").val();
        myPostData.smsTrmsNm = $("#sSmsTrmsNm").val();
        myPostData.smsTrmsConts = $("#sSmsTrmsConts").val();
        myPostData.strDateStart = $("#strDateStart").val();
        myPostData.strDateEnd = $("#strDateEnd").val();

        $("#grid").trigger("reloadGrid");
    });

    /* 수신자선택팝업호출 */
    $(".addRpt").bind("click",function(){
        $(".layerDetail").hide(); //상세보기 레이어 숨김추가(사이즈가 틀릴경우 보임 현상 발생)_141203_송준혁
        $(".layerRegister").hide();
        $(".layerRegisterItem").show();
        $.center(".layerRegisterItem");
    });

    /* 수신자추가 취소 */
    $(".btnCItem").bind("click",function(){
        var btnType = $(this).attr("btnType");

        /* 수신자 카운트 변수 */
        var cnt = 0;

        var paramId = "";
        var paramNm = "";
        var paramMo = "";

        if(btnType == 's'){
            /* 저장 버튼일 경우 */
            $("#iWorkLogRcvNm").html("");

            var s = $.getSelRow("#grid_grp_list_inst");

            for(var i=0; i<s.length; i++){
                var list = $("#grid_grp_list_inst").getRowData(s[i]);

                /* 사용자를 선택했을경우에만 적용시킴 */
                if(list.LVL == 4){

                	var moblNo = list.MOBL_NO.replace(/-/gi, "");
                	if(moblNo == "") continue;

                	if(cnt == 0){
                        /* 처음 들어온 사용자 */
                        $("#iWorkLogRcvNm").append(list.GRP_NM_KO);

                        paramId += list.GRP_ID_KEY;
                        paramNm += list.GRP_NM_KO;
                        paramMo += list.MOBL_NO;
                    }else{
                        /* 사용자가 다수일경우 구분자를 붙여준다. */
                        $("#iWorkLogRcvNm").append(", "+ list.GRP_NM_KO);

                        paramId += ","+ list.GRP_ID_KEY;
                        paramNm += ","+ list.GRP_NM_KO;
                        paramMo += ","+ list.MOBL_NO;
                    }

                    /* 카운트 증가 */
                    cnt++;
                }
            };



            if(cnt == 0){
                alert("수신자를 선택해주십시오");
            }else{
                $("#hMsgRcvId").val(paramId);
                $("#hMsgRcvNm").val(paramNm);
                $("#hMsgRcvMo").val(paramMo);

                $(".layerRegister").show();
                $(".layerRegisterItem").hide();

            }

        }else{
            $(".layerRegister").show();
            $(".layerRegisterItem").hide();
        }


    });

  //등록버튼
    $(".btnMngInst").bind("click",function(){
        $(".btnSend").show();
        $(".btnRealSend").show();

        $("#modetitle").text("등록");
        $.changeInputMode(true);

        try{
            resetAction();
        }catch(e) {}

        $(".layerRegister").show();

        var layerH = $(".layerRegister").height();
        $(".layerRegister").css({"margin-top": -(layerH/2)+"px"});

        $(".mask").remove();
        $("body").append("<div class='mask'></div>");

        try{
            $('.layer SELECT').selectBox("destroy");
            $('.layer SELECT').selectBox();
        } catch(e) {}

        insertFlag = true;
        return false;
    });

    /* 쪽지 보내기 */
    $(".btnSend").bind("click", function(){
        if(validate() == false){
            return false;
        }

        var s = $("#iWorkLogRcvNm").html();

        if(s == ""){
            alert("수신자를 선택하여 주세요.");
            return false;
        };

        if(insertFlag == true){
            insertAction();
        }else{
            updateAction();
        };

        $(".mask").remove();
        $(".layer").hide();
        insertFlag = false;

        return false;
    });

    // 수정 팝업
    $(".modify").bind("click", function() {
    	var paramId = "";
        var paramNm = "";
        var paramMo = "";

        // 수신자 팝업 그리드
        var grpList = $("#grid_grp_list_inst").jqGrid('getDataIDs');

    	// 수신자 상세 목록 가져오기
        var rcvList = $("#grid_list_rcv_temp").jqGrid('getDataIDs');

        for(var i = 0; i < rcvList.length; i++) {
            var list = $("#grid_list_rcv_temp").jqGrid('getRowData', rcvList[i]);

            paramId += (i === 0) ? list.SMS_RCV_ID : ","+ list.SMS_RCV_ID;
            paramNm += (i === 0) ? list.SMS_RCV_NM : ","+ list.SMS_RCV_NM;
            paramMo += (i === 0) ? list.SMS_RCV_MOBL_NO : ","+ list.SMS_RCV_MOBL_NO;

            // 수신자 팝업 그리드에 세팅
            for (var j = 0; j < grpList.length; j++) {
                var grpData = $("#grid_grp_list_inst").jqGrid('getRowData', grpList[j]);

                if (list.SMS_RCV_ID == grpData.GRP_ID_KEY) {
                     $('#' + grpData.GRP_ID).find('td>input[type=checkbox]').attr("checked", true);
                }
            }
        }

        $("#hMsgRcvId").val(paramId);
        $("#hMsgRcvNm").val(paramNm);
        $("#hMsgRcvMo").val(paramMo);
        $("#iWorkLogRcvNm").html(paramNm);
    });

    // SMS 실제전송 등록
    $(".btnRealSend").bind("click", function() {
        if(validate() == false){
            return false;
        }

        var s = $("#iWorkLogRcvNm").html();

        if(s == ""){
            alert("수신자를 선택하여 주세요.");
            return false;
        };

        if(confirm("전송하시겠습니까?") == false){ return false;}

        if(insertFlag == true){
        	insertRealSmsAction();
        }else{
        	updateRealSmsAction();
        };

        $(".mask").remove();
        $(".layer").hide();
        insertFlag = false;

    });

    /* 저장과 전송 */
    $(".btnSmsSend").bind("click", function(){
        if(validate() == false){
            return false;
        }

        var rcvList = $("#grid_list_rcv_temp").jqGrid('getDataIDs');

        if(rcvList.length < 0){
            alert("수신자를 선택하여 주세요.");
            return false;
        };

    	if(confirm("전송하시겠습니까?") == false){ return false;}
    	smsSendAction();

    });


});

function resetAction() {

    $.resetInputObject(".layer.layerRegister");

    $("#iWorkLogRcvNm").html("");

    var myPostData = $("#grid_grp_list_inst").jqGrid('getGridParam', 'postData');
    myPostData.checkRcvId = "";
    $("#grid_grp_list_inst").trigger("reloadGrid");


}

function validate() {
    return $.validate(".layerRegister .tableType2");
}

function insertRealSmsAction() {
    var url = "<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/insertRealSms.json";
    var params = "smsNtfctTyCd=" + escape(encodeURIComponent($("#iSmsNtfctTyCd").val()));
    //params += "&smsTrmsSttus=" + escape(encodeURIComponent($("#iSmsTrmsSttus").val()));
    params += "&smsTrmsSttus=N";
    params += "&smsTrmsConts=" + escape(encodeURIComponent($("#iSmsTrmsConts").val()));

    var msgRcvId = $("#hMsgRcvId").val();
    var msgRcvNm = $("#hMsgRcvNm").val();
    var msgRcvMo = $("#hMsgRcvMo").val();

    var cntId = msgRcvId.split(",");
    var cntNm = msgRcvNm.split(",");
    var cntMo = msgRcvMo.split(",");
    var paramsItem = "";
    for(var i = 0; i < cntId.length; i++) {
        paramsItem += "&smsRcvId=" + escape(encodeURIComponent(cntId[i]));
        paramsItem += "&smsRcvNm=" + escape(encodeURIComponent(cntNm[i]));
        paramsItem += "&smsRcvMoblNo=" + escape(encodeURIComponent(cntMo[i]));
    }

    //alert(paramsItem);
    params += paramsItem;

    $.ajaxEx($('#grid'), {
    	type : "POST",
        url : url,
        datatype: "json",
        data: params,
        success:function(data){
            if (data.session == 1) {
            	$(".btnSend").hide();
                $(".btnRealSend").hide();
            }
            //if(data.session == 1)
            $("#grid").trigger("reloadGrid");
            //alert("저장하였습니다.");
            alert(data.msg);
        },
        error:function(e){
            //alert(e.responseText);
            alert(data.msg);
        }
    });
}

function updateRealSmsAction() {
    var url = "<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/updateRealSms.json";
    var params = "smsNtfctTyCd=" + escape(encodeURIComponent($("#iSmsNtfctTyCd").val()));
    params += "&smsTrmsSttus=Y";
    params += "&smsTrmsConts=" + escape(encodeURIComponent($("#iSmsTrmsConts").val()));
    params += "&smsId=" + escape(encodeURIComponent($("#iSmsId").val()));

    var rcvList = $("#grid_list_rcv_temp").jqGrid('getDataIDs');
    var paramsItem = "";
    for(var i = 0; i < rcvList.length; i++) {
        var list = $("#grid_list_rcv_temp").jqGrid('getRowData', rcvList[i]);

        paramsItem += "&smsRcvId=" + escape(encodeURIComponent(list.SMS_RCV_ID));
        paramsItem += "&smsRcvNm=" + escape(encodeURIComponent(list.SMS_RCV_NM));
        paramsItem += "&smsRcvMoblNo=" + escape(encodeURIComponent(list.SMS_RCV_MOBL_NO));
    }

    params += paramsItem;

    $.ajaxEx($('#grid'), {
    	type : "POST",
        url : url,
        datatype: "json",
        data: params,
        success:function(data){
            if (data.session == 1) {
            	$(".btnSend").hide();
                $(".btnRealSend").hide();
            }
            //if(data.session == 1)
            $("#grid").trigger("reloadGrid");
            //alert("저장하였습니다.");
            alert(data.msg);
        },
        error:function(e){
            //alert(e.responseText);
            alert(data.msg);
        }
    });
}

function smsSendAction() {
    var url = "<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/smsSend.json";
    var params = "smsNtfctTyCd=" + escape(encodeURIComponent($("#iSmsNtfctTyCd").val()));
    params += "&smsTrmsSttus=Y";
    params += "&smsTrmsConts=" + escape(encodeURIComponent($("#iSmsTrmsConts").val()));
    params += "&smsId=" + escape(encodeURIComponent($("#iSmsId").val()));

    var rcvList = $("#grid_list_rcv_temp").jqGrid('getDataIDs');
    var paramsItem = "";
    for(var i = 0; i < rcvList.length; i++) {
        var list = $("#grid_list_rcv_temp").jqGrid('getRowData', rcvList[i]);

        paramsItem += "&smsRcvId=" + escape(encodeURIComponent(list.SMS_RCV_ID));
        paramsItem += "&smsRcvNm=" + escape(encodeURIComponent(list.SMS_RCV_NM));
        paramsItem += "&smsRcvMoblNo=" + escape(encodeURIComponent(list.SMS_RCV_MOBL_NO));
    }

    params += paramsItem;

    $.ajaxEx($('#grid'), {
    	type : "POST",
        url : url,
        datatype: "json",
        data: params,
        success:function(data){
            //if(data.session == 1)
            $("#grid").trigger("reloadGrid");
            //alert("저장하였습니다.");
            alert(data.msg);

            if (data.session == 1) {
            	$(".btnMd").hide();
                $(".btnDe").hide();
                $(".btnRealSend").hide();
                $(".btnSmsSend").hide();
            }
        },
        error:function(e){
            //alert(e.responseText);
            alert(data.msg);
        }
    });

    $(".mask").remove();
    $(".layer").hide();
    insertFlag = false;


}

function insertAction() {
    var url = "<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/insert.json";
    var params = "smsNtfctTyCd=" + escape(encodeURIComponent($("#iSmsNtfctTyCd").val()));
    //params += "&smsTrmsSttus=" + escape(encodeURIComponent($("#iSmsTrmsSttus").val()));
    params += "&smsTrmsSttus=N";
    params += "&smsTrmsConts=" + escape(encodeURIComponent($("#iSmsTrmsConts").val()));

    var msgRcvId = $("#hMsgRcvId").val();
    var msgRcvNm = $("#hMsgRcvNm").val();
    var msgRcvMo = $("#hMsgRcvMo").val();

    var cntId = msgRcvId.split(",");
    var cntNm = msgRcvNm.split(",");
    var cntMo = msgRcvMo.split(",");
    var paramsItem = "";
    for(var i = 0; i < cntId.length; i++) {
        paramsItem += "&smsRcvId=" + escape(encodeURIComponent(cntId[i]));
        paramsItem += "&smsRcvNm=" + escape(encodeURIComponent(cntNm[i]));
        paramsItem += "&smsRcvMoblNo=" + escape(encodeURIComponent(cntMo[i]));
    }

    params += paramsItem;

    $.ajaxEx($('#grid'), {
    	type : "POST",
        url : url,
        datatype: "json",
        data: params,
        success:function(data){
            //if(data.session == 1)
            $("#grid").trigger("reloadGrid");
            //alert("저장하였습니다.");
            alert(data.msg);
        },
        error:function(e){
            //alert(e.responseText);
            alert(data.msg);
        }
    });
}

function updateAction(obj) {
	var url = "<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/update.json";
    var params = "smsNtfctTyCd=" + escape(encodeURIComponent($("#iSmsNtfctTyCd").val()));
    params += "&smsTrmsConts=" + escape(encodeURIComponent($("#iSmsTrmsConts").val()));
    params += "&smsTrmsSttus=N";
    params += "&smsId=" + escape(encodeURIComponent($("#iSmsId").val()));

    var msgRcvId = $("#hMsgRcvId").val();
    var msgRcvNm = $("#hMsgRcvNm").val();
    var msgRcvMo = $("#hMsgRcvMo").val();

    var cntId = msgRcvId.split(",");
    var cntNm = msgRcvNm.split(",");
    var cntMo = msgRcvMo.split(",");
    var paramsItem = "";
    for(var i = 0; i < cntId.length; i++) {
        paramsItem += "&smsRcvId=" + escape(encodeURIComponent(cntId[i]));
        paramsItem += "&smsRcvNm=" + escape(encodeURIComponent(cntNm[i]));
        paramsItem += "&smsRcvMoblNo=" + escape(encodeURIComponent(cntMo[i]));
    }

    params += paramsItem;

    $.ajaxEx($('#grid'), {
    	type : "POST",
        url : url,
        datatype: "json",
        data: params,
        success:function(data){
            $("#grid").trigger("reloadGrid");
            alert(data.msg);
        },
        error:function(e){
            alert(data.msg);
        }
    });
}

function deleteAction(obj) {
	var url = "<c:url value='/'/>wrks/wrkmng/msgmng/smstmp/delete.json";
    var params = "smsId=" + escape(encodeURIComponent($("#iSmsId").val()));

    $.ajaxEx($('#grid'), {
    	type : "POST",
        url : url,
        datatype: "json",
        data: params,
        success:function(data){
            $("#grid").setGridParam({page :$("#cur-page").val()});
            $("#grid").trigger("reloadGrid");
            alert(data.msg);
        },
        error:function(e){
            alert(data.msg);
        }
    });
}

</script>
</head>
<body>
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
<div id="wrapper" class="wth100">
    <!-- container -->
    <div class="container">
        <!-- content -->
        <div class="contentWrap">
            <div class="content">
                <%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
                <div class="tableTypeHalf seachT">
                    <table>
                        <caption>SMS 관리</caption>
                        <tbody>
                        <tr>
                            <th>전송유형</th>
                            <td>
                                <select name="" id="sSmsTrmsTyCd" class="selectType1">
                                    <option value="">전체</option>
                                    <c:forEach items="${smsTrmsTyList}" var="val">
                                        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
                                    </c:forEach>
                                </select>
                            </td>
                            <th>공지구분</th>
                            <td>
                                <select name="" id="sSmsNtfctTyCd" class="selectType1">
                                    <option value="">전체</option>
                                    <c:forEach items="${ntfctTyList}" var="val">
                                        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>전송기간</th>
                            <td colspan="3">
                                <input type="text" name="" id="strDateStart" class="date8Type datepicker1" value="${currentDay}" required="required" user-title="시작일자"
                                 user-data-type="betweendate" user-ref-id="strDateEnd" style="width: 92px; margin-top: 5px;">
                                <span class="bl">~</span>
                                <input type="text" name="" id="strDateEnd" class="date8Type datepicker2" value="${currentDay}" required="required" user-title="종료일자"
                                 style="width: 92px; margin-top: 5px;">
                            </td>
                        </tr>
                        <tr>
                            <th>전송자</th>
                            <td>
                                <input type="text" name="" id="sSmsTrmsNm" class="txtType txtType100px searchEvt" style="ime-mode:active">
                            </td>
                            <th>전송내용</th>
                            <td>
                                <input type="text" name="" id="sSmsTrmsConts" class="txtType txtType100px searchEvt" style="ime-mode:active">
                                <a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="searchArea">
                    <div class="page fL">
                        <span class="txt">페이지당</span>
                        <div class="selectBox">
                            <select name="rowPerPageList" id="rowPerPageList" class="selectType1">
                                <c:forEach items="${rowPerPageList}" var="val">
                                    <option value="${val.cdId}" ${val.cdId == rowPerPageSession ? 'selected' : ''}><c:out value="${val.cdNmKo}" ></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                        <span class="totalNum">전체<em id="rowCnt"></em>건</span>
                    </div>
                </div>
                <div class="tableType1">
                    <table id="grid" style="width:100%">
                    </table>
                </div>
                <div class="paginate">
                </div>
                <div class="btnWrap btnR">
                    <a href="#" class="btn btnDt btnMngInst">추가</a>
                </div>
            </div>

            <!-- 레이어팝업 상세 -->
            <div class="layer layerDetail" id="div_drag_1">
                <div class="tit"><h4>SMS 상세</h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                        <table>
                            <caption>SMS</caption>
                            <tbody>
                            <tr>
                                <th>상태</th>
                                <td id="dSmsTrmsTyNm"></td>
                                <th>전송자</th>
                                <td id="dSmsTrmsNm"></td>
                            </tr>
                            <tr>
                                <th>공지구분</th>
                                <td id="dSmsNtfctTyNm"></td>
                            </tr>
                            <tr>
                                <th>전송일시</th>
                                <td id="dSmsRsvtTrmsYmdHms"></td>
                                <th>등록일시</th>
                                <td id="dRgsDate"></td>
                            </tr>
                            <tr>
                                <th>전송내용</th>
                                <td colspan="3">
                                    <textArea id="dSmsTrmsConts" style="width:100%;height:200px" readonly></textArea>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <br>
                <div class="tit"><h1><b>수신자</b></h1></div>
                <div class="layerCt">
                    <div class="tableType2-topNone dtl rcv"  style="height:90px; overflow-y:scroll; overflow-x:hidden" >
                        <table id="grid_list_rcv_temp" style="width:100%">
                        </table>
                    </div>
                    <div class="btnCtr">
                        <a href="#" class="btn btnMd modify">수정</a>
                        <a href="#" class="btn btnDe">삭제</a>
                        <a href="#" class="btn btnSmsSend">전송</a>
                        <a href="#" class="btn btnC">닫기</a>
                    </div>
                </div>
            </div>
            <!-- //레이어팝업 상세 -->

            <!-- //레이어팝업 등록, 수정 -->
            <div class="layer layerRegister" id="div_drag_2">
                <div class="tit"><h4>SMS <span id="modetitle">전송</span></h4></div>
                <div class="laterCt">
                    <div class="tableType2">
                        <input type="hidden" id="hMsgRcvId"/>
                        <input type="hidden" id="hMsgRcvNm"/>
                        <input type="hidden" id="hMsgRcvMo"/>
                         <input type="hidden" id="iSmsId"/>
                        <table>
                            <tbody>
                                <tr>
                                    <th style="width:20%">공지구분</th>
                                    <td colspan="2">
                                        <select name="" id="iSmsNtfctTyCd" class="selectType1">
                                            <c:forEach items="${ntfctTyList}" var="val">
                                                <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>받는사람</th>
                                    <td width="110"><a href="#" class="btn addRpt">수신자선택</a></td>
                                    <td id="iWorkLogRcvNm"></td>
                                </tr>
                                <tr>
                                    <th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>전송내용</th>
                                    <td colspan="2">
                                        <textarea id="iSmsTrmsConts"  style="width:100%;height:200px; ime-mode:active" required="required" maxlength="4000" user-title=전송내용" user-required="insert"></textarea>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                <br/>
                    <div class="btnCtr">
                        <a href="#" class="btn btnSend">임시저장</a>
                        <a href="#" class="btn btnRealSend">전송</a>
                        <a href="#" class="btn btnC">취소</a>
                    </div>
                </div>
            </div>
            <!-- //레이어팝업 등록 -->
            <!-- 수신자추가 팝업 -->
            <div class="layer layerRegisterItem">
                <div class="tit"><h4>수신1자 선택</h4></div>
                <div class="layerCt">
                    <!-- 수신자 리스트 -->
                    <div class="tableType1" style="height:500px; overflow-y:scroll; overflow-x:hidden">
                        <table id="grid_grp_list_inst" style="width:100%">
                        </table>
                    </div>
                    <div class="btnCtr update">
                        <a href="#" class="btn btnCItem" btnType="s">저장</a>
                        <a href="#" class="btn btnCItem" btnType="c">취소</a>
                    </div>
                </div>
            </div>
            <!-- //수신자추가 팝업 -->
        </div>
        <!-- //content -->
    </div>
    <!-- //container -->
</div>
<!-- footer -->
<!-- <div id="footwrap">
    <div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->
</body>
</html>