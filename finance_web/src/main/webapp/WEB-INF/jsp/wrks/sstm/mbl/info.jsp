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
var rcvStartHh = [
			   		<c:forEach var="val" begin="00" end="24" step="1">
						{"CD_ID": "${val}", "CD_NM_KO" : "${val}"},
					</c:forEach>
				  	];
var rcvEndHh = [
					<c:forEach var="val" begin="00" end="24" step="1">
						{"CD_ID": "${val}", "CD_NM_KO" : "${val}"},
					</c:forEach>
					];

$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url: '<c:url value='/'/>wrks/sstm/mbl/info/list.json',
		datatype: "json",
		autowidth: true,
		postData: {
			moblNo : $("#sMoblNo").val(),
			moblKndCd : $("#sMoblKndCd").val(),
			userNm : $("#sUserNm").val(),
			useTyCd : $("#sUseTyCd").val()
		},
		colNames: [	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
						'No',  '모바일기기 아이디', '모바일번호', '모바일종류코드', '모바일종류', '모바일OS코드', '모바일OS명',
						'성명', '소속',		  'Email',   '사용유무코드',   '사용유형',  '푸쉬ID',	  'UUID',	'기타정보'
				   ],
		colModel: [
					{ name:'check', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{ name:'rk'			, index:'RK'			, align:'center', sortable: false, hidden:true},
					{ name:'moblId'		, index:'MOBL_ID'		, 'hidden':true},
					{ name:'moblNo'		, index:'MOBL_NO'		, width:150		, align:'center'},
					{ name:'moblKndCd'	, index:'MOBL_KND_CD'	, 'hidden':true},
					{ name:'moblKndNm'  , index:'MOBL_KND_NM'	, 'hidden':true},
					{ name:'moblOsTyCd' , index:'MOBL_OS_TY_CD'	, 'hidden':true},
					{ name:'moblOsTyNm' , index:'MOBL_OS_TY_NM'	, 'hidden':true},
					{ name:'userNm'		, index:'USER_NM'		, width:100		, align:'center'},
					{ name:'userPsitnNm', index:'USER_PSITN_NM'	, width:150		, align:'center'},
					{ name:'userEmail'  , index:'USER_EMAIL'	, width:220		, align:'center'},
					{ name:'useTyCd'	, index:'USE_TY_CD'		, 'hidden':true},
					{ name:'useTyNm'	, index:'USE_TY_NM'		, width:50		, align:'center'},
					{ name:'pushId'		, index:'PUSH_ID'		, 'hidden':true},
					{ name:'moblUuid'   , index:'MOBL_UUID'		, 'hidden':true},
					{ name:'etcInfo'	, index:'ETC_INFO'		, 'hidden':true}
				],
		 pager: '#pager',
		 rowNum: $('#rowPerPageList').val(),
		 height : 390,
		 sortname:'MOBL_ID',
		 sortorder: 'DESC',
		 viewrecords: true,
		 multiselect: false,
		 loadonce:false,
		 jsonReader: {
		 	id: "MOBL_ID",
		 	root: function(obj) { return obj.rows; },
		 	page: function(obj) { return 1; },
		 	total: function(obj) {
		 		if(obj.rows.length > 0) {
		 			var page  = obj.rows[0].rowcnt / rowNum;
		 			if( (obj.rows[0].rowcnt % rowNum) > 0)
		 				page++;
		 			return page;
		 		}
		 		else
		 			return 1;
		 	},
		 	//records: function(obj) { $('#rowCnt').text(obj.rows.length); return obj.rows.length; }
		 	records: function(obj) { return $.showCount(obj); }
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
				if(iCol == 0) return false;

				var list = jQuery("#grid").getRowData(rowid);

				$("#dMoblNo").html(list.moblNo);
				$("#dMoblKndCd").html(list.moblKndCd);
				$("#dMoblKndNm").html(list.moblKndNm);
				$("#dMoblOsTyNm").html(list.moblOsTyNm);
				$("#dUserNm").html(list.userNm);
				$("#dUserPsitnNm").html(list.userPsitnNm);
				$("#dUserEmail").html(list.userEmail);
				$("#dUseTyNm").html(list.useTyNm);
				$("#dPushId").html(list.pushId);
				$("#dMoblUuid").html(list.moblUuid);
				$("#dEtcInfo").html(list.etcInfo);

				$("#moblId").val(list.moblId);
 				$("#iMoblNo").val(list.moblNo);
				$.selectBarun("#iMoblKndCd", list.moblKndCd);
				$.selectBarun("#iMoblOsTyCd", list.moblOsTyCd);
				$("#iUserNm").val(list.userNm);
				$("#iUserPsitnNm").val(list.userPsitnNm);
				$("#iUserEmail").val(list.userEmail);
				$.selectBarun("#iUseTyCd", list.useTyCd);
				$("#iPushId").val(list.pushId);
				$("#iMoblUuid").val(list.moblUuid);
				$("#iEtcInfo").html(list.etcInfo);

				$("#sMoblId").val(list.moblId);

				/* 메세지 수신시간 상세 */
				var detailPostData = $("#grid_mobl_rcv_detail").jqGrid('getGridParam', 'postData');
				detailPostData.moblId = $("#sMoblId").val();
				$("#grid_mobl_rcv_detail").trigger("reloadGrid");


				/* 메세지 수신시간 수정 */
				var updPostData = $("#grid_mobl_rcv_time").jqGrid('getGridParam', 'postData');
				updPostData.moblId = $("#sMoblId").val();
				$("#grid_mobl_rcv_time").trigger("reloadGrid");

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
	// jqgrid end


	$.jqGrid($('#grid_mobl_rcv_time'), {
		url: '<c:url value='/'/>wrks/sstm/mbl/info/rcvTime.json',
		datatype: "json",
		postData: {
			moblId : $("#sMoblId").val(),
			div : ""
		},
		colNames: [ '모바일기기 아이디'	, '요일'		, '요일코드'		, '수신시작시간'	, '수신종료시간'
				  , '등록자아이디'		, '등록일'	, '수정자아이디'	, '수정일'
				   ],
		colModel: [ { name:'moblId'		, index:'MOBL_ID'		, width:50 , align:'center', sortable: false, hidden:true}
				  , { name:'dayTy'		, index:'DAY_TY'		, width:180, align:'center'}
				  , { name:'cdId'		, index:'CD_ID'			, width:100, align:'center', hidden:true}
				  , { name:'rcvStartHh'	, index:'RCV_START_HH'	, width:280, align:'center', editable: true, edittype:"select"
				   	, editoptions:{value:""}, useroption: rcvStartHh, formatter: $.GridSelectBox}
				  , { name:'rcvEndHh'	, index:'RCV_END_HH'	, width:280, align:'center', editable: true, edittype:"select"
				   	, editoptions:{value:""}, useroption: rcvEndHh, formatter: $.GridSelectBox}
				  , { name:'rgsUserId'	, index:'RGS_USER_ID'	, width:150, align:'center', hidden:true}
				  , { name:'rgsDate'	, index:'RGS_DATE'		, width:100, align:'center', hidden:true}
				  , { name:'updUserId'	, index:'UPD_USER_ID'	, width:150, align:'center', hidden:true}
				  , { name:'updDate'	, index:'UPD_DATE'		, width:100, align:'center', hidden:true}
				],
		 pager: '#pager',
		 rowNum: $('#rowPerPageList').val(),
		 sortname:'MOBL_ID',
		 sortorder: 'DESC',
		 viewrecords: true,
		 multiselect: false,
		 loadonce:false,
		 jsonReader: {
		 	/* id: "MOBL_ID",
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
		 	//records: function(obj) { $('#rowCnt').text(obj.rows.length); return obj.rows.length; }
		 	records: function(obj) { return $.showCount(obj); } */
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
				if(iCol == 0) return false;
				//$.showDetail();
		},
		loadComplete : function (data) {
			$('#grid_mobl_rcv_time SELECT').selectBox();
        },
        beforeRequest : function() {
       	   var oParams = $('#grid_mobl_rcv_time').getGridParam();
       	   if (oParams.hasOwnProperty('url') && oParams.url == '') {
       	      return false;
       	   }
		}
	});

	$.jqGrid($('#grid_mobl_rcv_detail'), {
		url: '<c:url value='/'/>wrks/sstm/mbl/info/rcvTime.json',
		datatype: "json",
		postData: {
			moblId : $("#sMoblId").val(),
			div : "div"
		},
		colNames: [ '모바일기기 아이디'	, '요일'		, '요일코드'		, '수신시작시간'	, '수신종료시간'	, '수신시간'
				  , '등록자아이디'		, '등록일'	, '수정자아이디'	, '수정일'
				   ],
		colModel: [ { name:'moblId'		, index:'MOBL_ID'		, width:50 , align:'center', sortable: false, hidden:true}
				  , { name:'dayTy'		, index:'DAY_TY'		, width:200, align:'center'}
				  , { name:'cdId'		, index:'CD_ID'			, width:100, align:'center', hidden:true}
				  , { name:'rcvStartHh'	, index:'RCV_START_HH'	, width:200, align:'center', hidden:true}
				  , { name:'rcvEndHh'	, index:'RCV_END_HH'	, width:200, align:'center', hidden:true}
				  , { name:'rcvtime'	, index:'RCVTIME'		, width:550, align:'center'}
				  , { name:'rgsUserId'	, index:'RGS_USER_ID'	, width:150, align:'center', hidden:true}
				  , { name:'rgsDate'	, index:'RGS_DATE'		, width:100, align:'center', hidden:true}
				  , { name:'updUserId'	, index:'UPD_USER_ID'	, width:150, align:'center', hidden:true}
				  , { name:'updDate'	, index:'UPD_DATE'		, width:100, align:'center', hidden:true}
				],
		 pager: '#pager',
		 rowNum: $('#rowPerPageList').val(),
		 sortname:'MOBL_ID',
		 sortorder: 'DESC',
		 viewrecords: true,
		 multiselect: false,
		 loadonce:false,
		 jsonReader: {
		 	/* id: "MOBL_ID",
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
		 	//records: function(obj) { $('#rowCnt').text(obj.rows.length); return obj.rows.length; }
		 	records: function(obj) { return $.showCount(obj); } */
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
				if(iCol == 0) return false;
				//$.showDetail();
		},
		beforeRequest : function() {
			var oParams = $('#grid_mobl_rcv_detail').getGridParam();
			if (oParams.hasOwnProperty('url') && oParams.url == '') {
				return false;
			}
		}
	});

	$.jqGrid($('#grid_user_list'), {
        url : '<c:url value='/'/>wrks/sstm/mbl/info/userList.json',
        datatype : "json",
        postData : {
        	userId : $("#sUserId").val(),
        	grpId : $("#sGrpId").val()
        },
        colNames : [  '' , '사용자명' , '사용자아이디' , '연락처' , '그룹명' , '그룹레벨명' , '기관' , '이메일'
                   ],
        colModel : [ { name: 'check',		   width: 30,		   align: 'center',   sortable: false,
				       editable: true,     edittype: 'radio',
				       editoptions: {  value: "True:False"    },
				       formatter: function (cellValue, option) {
				           return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
				       }
					}
        			, { name : 'userNm', index : 'USER_NM', width:80, align:'center', sortable: false}
                    , { name : 'userId', index : 'USER_ID', width:80, align:'center'}
                    , { name : 'moblNo', index : 'MOBL_NO', width:100, align:'center'}
                    , { name : 'grpNm', index : 'GRP_NM', width:70, align:'center'}
                    , { name : 'authLvlNm', index : 'AUTH_LVL_NM', width:50, align:'center'}
                    , { name : 'insttNm', index : 'INSTT_NM', width:120, align:'center'}
                    , { name : 'email', index : 'EMAIL', width:50, align:'center', hidden:true}
				],
		pager : '#pager',
		rowNum : -1,
		sortname: 'USER_NM',
		sortorder: 'DESC',
		height: 500,
		scrollOffset: 0,
		viewrecords:true,
	    multiselect: false,
	    loadonce:false,
		jsonReader: {},
		onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;
		},
		onSelectRow : function(rowid, status, e) {
			$("#grid_user_list input[type=radio]").get(rowid - 1).checked = true;
		},
		loadComplete : function (data) {
			let $rows = $('#grid_user_list tr.jqgrow[role=row]');
            if ($rows.length) {
                $('#grid_user_list').jqGrid('setSelection', $rows.get(0).id);
            }
		},
        beforeRequest : function() {
        	$('#grid_user_list').clearGridData();
        }

	});

	// jqgrid end
	$(".btnS").bind("click",function(){
		$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
		var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
		myPostData.moblNo = $("#sMoblNo").val();
		myPostData.moblKndCd = $("#sMoblKndCd").val();
		myPostData.userNm = $("#sUserNm").val();
		myPostData.useTyCd = $("#sUseTyCd").val();

		$("#grid").trigger("reloadGrid");
	});

    $('.btnUserList').on('click', function() {
    	$('.layer').hide();
    	$('#divUserList').show();
    	oCommon.jqGrid.resize('#grid_user_list');
    	oCommon.jqGrid.resizeHeader('user_list');
    });

    $('#btnClose').on('click', function(){
    	$('.layerRegister').show();
    	$('#divUserList').hide();
    });

    $('#btnSelect').on('click', function(){
    	$('.layerRegister').show();
    	$('#divUserList').hide();

    	var selectRowId = $.getSelRowRadio('#grid_user_list');
		var rowData = $("#grid_user_list").jqGrid("getRowData",selectRowId);
		$('#iMoblNo').val(rowData.moblNo);
		$('#iUserNm').val(rowData.userNm);
		$('#iUserPsitnNm').val(rowData.insttNm);
		$('#iUserEmail').val(rowData.email);

    });

    $('#searchUserList').on('click', function(){
    	var userListPostData = $("#grid_user_list").jqGrid('getGridParam', 'postData');
    	userListPostData.userId = $("#sUserId").val();
    	userListPostData.grpId = $("#sGrpId").val();
    	$("#grid_user_list").trigger("reloadGrid");
    });

});
// document ready end

function resetAction() {
	$("#moblId").val("");
	$("#iMoblNo").val("");
	//$.selectBarun("#iMoblKndCd", "");
	//$.selectBarun("#iMoblOsTyCd", "");
	$("#iMoblKndCd").get(0).selectedIndex = 0;
	$("#iMoblOsTyCd").get(0).selectedIndex = 0;
	$("#iUserNm").val("");
	$("#iUserPsitnNm").val("");
	$("#iUserEmail").val("");
	//$.selectBarun("#iUseTyCd", "");
	$("#iUseTyCd").get(0).selectedIndex = 0;
	$("#iPushId").val("");
	$("#iMoblUuid").val("");
	$("#iEtcInfo").val("");

	var updPostData = $("#grid_mobl_rcv_time").jqGrid('getGridParam', 'postData');
	updPostData.moblId = "";
	$("#grid_mobl_rcv_time").trigger("reloadGrid");
}

function updateAction() {
	var url = "<c:url value='/'/>wrks/sstm/mbl/info/update.json";
	var params = "";
		params += "&moblNo=" + encodeURIComponent($("#iMoblNo").val());
		params += "&moblKndCd=" + encodeURIComponent($("#iMoblKndCd").val());
		params += "&moblOsTyCd=" + encodeURIComponent($("#iMoblOsTyCd").val());
		params += "&userNm=" + encodeURIComponent($("#iUserNm").val());
		params += "&userPsitnNm=" + encodeURIComponent($("#iUserPsitnNm").val());
		params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
		params += "&userEmail=" + encodeURIComponent($("#iUserEmail").val());
		params += "&pushId=" + encodeURIComponent($("#iPushId").val());
		params += "&moblUuid=" + encodeURIComponent($("#iMoblUuid").val());
		params += "&updUserId=" + encodeURIComponent($("#iUpdUserId").val());
		params += "&moblId=" + encodeURIComponent($("#moblId").val());
		params += "&etcInfo=" + encodeURIComponent($("#iEtcInfo").val());

		var rows = $("#grid_mobl_rcv_time").jqGrid('getGridParam', 'records');

		for(var i=0; i<rows; i++){
			var list = jQuery("#grid_mobl_rcv_time").getRowData(i + 1);

			params += "&dayTyCd="+ encodeURIComponent(list.cdId);
			params += "&rcvStartHh="+ encodeURIComponent($.getCustomObjectValue("#grid_mobl_rcv_time", "select", "rcvStartHh", i+1));
			params += "&rcvEndHh="+ encodeURIComponent($.getCustomObjectValue("#grid_mobl_rcv_time", "select", "rcvEndHh", i+1));
		}

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
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

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function insertAction() {
	var url = "<c:url value='/'/>wrks/sstm/mbl/info/insert.json";
	var params = "";
		params += "moblId=" + encodeURIComponent($("#moblId").val());
		params += "&moblNo=" + encodeURIComponent($("#iMoblNo").val());
		params += "&moblKndCd=" + encodeURIComponent($("#iMoblKndCd").val());
		params += "&moblOsTyCd=" + encodeURIComponent($("#iMoblOsTyCd").val());
		params += "&userNm=" + encodeURIComponent($("#iUserNm").val());
		params += "&userPsitnNm=" + encodeURIComponent($("#iUserPsitnNm").val());
		params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
		params += "&userEmail=" + encodeURIComponent($("#iUserEmail").val());
		params += "&pushId=" + encodeURIComponent($("#iPushId").val());
		params += "&moblUuid=" + encodeURIComponent($("#iMoblUuid").val());
		params += "&rgsUserId=" + encodeURIComponent($("#iRgsUserId").val());
		params += "&etcInfo=" + encodeURIComponent($("#iEtcInfo").val());

	var s =  $.getSelRow("#grid_mobl_rcv_time");
	var rows = $("#grid_mobl_rcv_time").jqGrid('getGridParam', 'records');

	for(var i=0; i<rows; i++){
		var list = jQuery("#grid_mobl_rcv_time").getRowData(i + 1);

		params += "&dayTyCd="+ encodeURIComponent(list.cdId);
		params += "&rcvStartHh="+ encodeURIComponent($.getCustomObjectValue("#grid_mobl_rcv_time", "select", "rcvStartHh", i+1));
		params += "&rcvEndHh="+ encodeURIComponent($.getCustomObjectValue("#grid_mobl_rcv_time", "select", "rcvEndHh", i+1));
	}

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			if(data.session == 1){
				$("#grid").trigger("reloadGrid");
				alert(data.msg);

				$.hideInsertForm();

			}else{
				alert(data.msg);
			}

		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
}

function deleteAction() {
	var url = "<c:url value='/'/>wrks/sstm/mbl/info/delete.json";
	var params = "";
		params += "&moblId=" + $("#moblId").val();

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			alert(data.msg);
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
}

function deleteMultiAction() {

	//var s =  $("#grid").jqGrid('getGridParam', 'selarrrow');
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/mbl/info/deleteMulti.json";
	var params = "";

	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&moblId=" + list.moblId;
	}

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			alert(data.msg);
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});

	return true;
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
				<div class="tableType2 seachT">
					<table>
						<caption>사용자 등록</caption>
						<colgroup>
							<col style="width: 150px;" />
							<col style="width: *" />
							<col style="width: 150px;" />
							<col style="width: *" />
							<col style="width: 150px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
							<tr style="display:none;">
								<th>모바일종류</th>
								<td>
									<select name="sMoblKndCd" id="sMoblKndCd" class="selectType1">
										<option value="">전체</option>
										<c:forEach items="${mppList}" var="val">
											<option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<th>성명</th>
								<td><input type="text" name="" id="sUserNm" class="txtType searchEvt" maxlength="100" style="ime-mode:active"></td>
								<th>모바일번호</th>
								<td><input type="text" name="" id="sMoblNo" class="txtType searchEvt" maxlength="100"></td>
								<th>사용유형</th>
								<td><select name="" id="sUseTyCd" class="selectType1" maxlength="1">
										<option value="">전체</option>
										<c:forEach items="${useGrpList}" var="val">
											<option value="${val.cdId}" ${val.cdId eq 'Y' ? ' selected' : ''}><c:out value="${val.cdNmKo}" ></c:out></option>
										</c:forEach>
									</select>
									<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

				<div class="tableType1" style="height: 445px;">
					<table id="grid"></table>
				</div>
				<div class="paginate">
				</div>
				<div class="btnWrap btnR">
					<a href="<c:url value='/'/>images/attachFile/sample_mbl_v1_모바일계정리스트.xlsx">[ 샘플다운로드 ]</a>
					엑셀파일:
					<input type="file" id="excelFile" class="txtType carNum" style="display: none" />
					<input type="text" id="excelFileText" class="txtType carNum" readonly="readonly" />
					<a href="javascript:;" class="btn btnExcelFile">파일찾기</a>
					<a href="#" class="btn btnDt btnUpload" user-ref-id="excelFile" user-url="<c:url value='/'/>wrks/sstm/mbl/info/upload.excel" user-gridId="grid" user-ext=".xls,.xlsx" user-validate="모바일계정리스트">엑셀업로드</a>
					<a href="#" class="btn btnDt btnRgt">등록</a>
					<a href="#" class="btn btnMultiDe">삭제</a>
				</div>
			</div>

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit">
					<h4>모바일기기정보 상세</h4>
				</div>
				<div class="layerCt">
					<div class="tableType2">
						<input type="hidden" id="moblId"/>
						<input type="hidden" id="dMoblKndCd"/>
						<table>
							<caption>모바일기기 정보 상세</caption>
							<tbody>
								<tr>
									<th>모바일번호</th>			<td id="dMoblNo"></td>
									<th>OS유형</th>				<td id="dMoblOsTyNm"></td>
								</tr>
								<tr style="display:none;">
									<th>종류</th>					<td id="dMoblKndNm"></td>
								</tr>
								<tr>
									<th>성명</th>					<td id="dUserNm"></td>
									<th>소속</th>					<td id="dUserPsitnNm"></td>
								</tr>
								<tr>
									<th>Email</th>				<td id="dUserEmail"></td>
									<th>사용유형</th>				<td id="dUseTyNm"></td>
								</tr>
								<tr>
									<th>기타정보</th>				<td id="dEtcInfo" colspan="3"></td>
								</tr>
								<tr style="display:none;">
									<th>PUSH ID</th>			<td id="dPushId" style="word-break:break-all;"></td>
									<th>고유ID</th>				<td id="dMoblUuid"></td>
								</tr>
							</tbody>
						</table>

					</div>
					</br>
						<!-- 메세지 수신시간상세 -->
						<div class="tit">
							<h4>
								<b>알림 수신시간</b>
							</h4>
						</div>
							<div class="boxWrap">
								<div class="tableType1" style="height: 130px;">
									<table id="grid_mobl_rcv_detail"></table>
								</div>
							</div>

					<!-- 메세지 수신시간상세 -->
					<div class="btnCtr">
						<a href="#" class="btn btnMd">수정</a>
						<a href="#" class="btn btnDe">삭제</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- // 레이어팝업 상세 -->

			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister"  id="div_drag_2" >
				<div class="tit">
					<h4>모바일기기 정보 <span id="modetitle">추가</span></h4>
				</div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>모바일기기 정보 등록</caption>
							<tbody>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>모바일번호</th>
									<td>
										<input type="hidden" id="sMoblId"/>
										<input type="text" name="" id="iMoblNo" class="txtType" maxlength="20" required="required" user-title="모바일번호">
										<a href="#" class="btn btnUserList">사용자</a>
									</td>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>모바일기기 OS유형</th>
									<td>
										<select name="" id="iMoblOsTyCd" class="selectType1">
											<c:forEach items="${mppOsList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr class="hide">
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>모바일기기 종류</th>
									<td>
										<select name="" id="iMoblKndCd" class="selectType1">
											<c:forEach items="${mppList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>성명</th>
									<td><input type="text" name="" id="iUserNm" class="txtType" maxlength="100" style="ime-mode:active"></td>
									<th>소속</th>
									<td><input type="text" name="" id="iUserPsitnNm" class="txtType" maxlength="100" style="ime-mode:active"></td>
								</tr>
								<tr>
									<th>Email</th>
									<td><input type="text" name="" id="iUserEmail" class="txtType" maxlength="100" style="ime-mode:inactive"></td>
									<th>사용유형</th>
									<td>
										<select name="" id="iUseTyCd" class="selectType1">
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<th>기타정보</th>
									<!-- <td colspan="3"><textarea class="textArea" id="iEtcInfo" maxlength="2000" style="ime-mode:active"></textarea></td> -->
									<td colspan="3"><input type="text" name="" id="iEtcInfo" class="txtType100" maxlength="2000" style="ime-mode:active"></td>
								</tr>
								<tr style="display:none;">
									<th>PUSH ID</th>
									<td colspan="3"><input type="text" name="" id="iPushId" class="txtType" maxlength="255"></td>
								</tr>
								<tr style="display:none;">
									<th>모바일고유ID</th>
									<td colspan="3"><input type="text" name="" id="iMoblUuid" class="txtType" maxlength="40"></td>
								</tr>
							</tbody>
						</table>
					</div>
					</br>
					<div class="tit">
						<h4>
							<b>알림 수신시간 설정</b>
						</h4>
					</div>
					<!-- 메세지 수신시간 리스트 -->
						<div class="boxWrap">
							<div class="tableType1" style="height: 130px;">
								<table id="grid_mobl_rcv_time"></table>
							</div>
						</div>
					<div class="btnCtr">
						<a href="#" class="btn btnSvEc">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
					</br>

				</div>
			</div>
			<!-- //레이어팝업 등록 -->


			<!-- 사용자 리스트 팝업 -->
			<div class="layer layerUserList" id="divUserList">
				<div class="layerCt">
					<div class="tableType1">
						<div class="tableType2 seachT">
							<table>
								<tr>
									<th>사용자아이디</th>
									<td><input type="text" name="" id="sUserId" class="txtType" maxlength="100">
									</td>
									<th>그룹명</th>
									<td><input type="text" name="" id="sGrpId" class="txtType" maxlength="100">
										<a href="#" id="searchUserList" class="btn">조회</a>
									</td>
								</tr>
							</table>
						</div>
						<table id="grid_user_list"></table>
					</div>
					<div class="btnCtr">
						<a href="#" id="btnSelect" class="btn">선택</a>
						<a href="#" id="btnClose" class="btn">닫기</a>
					</div>
				</div>
			</div>

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
