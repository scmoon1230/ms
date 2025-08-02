<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">

var ptzCntrTy = [
				{"cdId":"NO", "cdNmKo":"NO"},
				{"cdId":"VIEW", "cdNmKo":"VIEW"},
				//{"CD_ID":"VIEW", "CD_NM_KO":"VIEW"},
				//{"CD_ID":"PRESET", "CD_NM_KO":"PRESET"},
				//{"CD_ID":"PTZ", "CD_NM_KO":"PTZ"},
				];

var cctvAccessYn = [
 				{"cdId":"Y", "cdNmKo":"접근"},
 				{"cdId":"N", "cdNmKo":"미접근"},
 				//{"CD_ID":"Y", "CD_NM_KO":"접근"},
 				//{"CD_ID":"N", "CD_NM_KO":"미접근"},
				   ];

var cctvSearchYn = [
				{"cdId":"Y", "cdNmKo":"조회"},
				{"cdId":"N", "cdNmKo":"불가"},
				//{"CD_ID":"Y", "CD_NM_KO":"조회"},
				//{"CD_ID":"N", "CD_NM_KO":"불가"},
				   ];

$(document).ready(function(){

	$.jqGrid('#grid_group', {
		url: '<c:url value='/'/>wrks/sstm/grp/auth_cctv/list_group.json',
		datatype: "json",
		postData: {
			//dstrtCd : $("#sDstrtCd").val(),
			grpNm : $("#sGrpNm").val()
		},
		colNames: [	'선택',		'그룹아이디',	'그룹명',		'권한레벨',	'권한레벨코드',		'지구명',		'지구코드'
				   ],
		colModel: [{ name:'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
						, formatter: function (cellValue, option) {
							return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
						}
					}
					,{ name:'grpId'    , index:'GRP_ID'     , width:130, align:'center'}
				 	,{ name:'grpNmKo'  , index:'GRP_NM_KO'  , width:130, align:'center'}
				 	,{ name:'authLvlNm', index:'AUTH_LVL_NM', width:130, align:'center'}
				 	,{ name:'authLvl'  , index:'AUTH_LVL'   , 'hidden':true }
				 	,{ name:'dstrtNm'  , index:'DSTRT_NM'   , 'hidden':true }
				 	,{ name:'dstrtCd'  , index:'DSTRT_CD'   , 'hidden':true }
		  ],

		pager: '#pager',
		rowNum : 1000,
		autowidth: true,
		height: $("#grid_group").parent().height() - 40,
		sortname: 'GRP_NM_KO',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		shrinkToFit: true,
		scrollOffset: 0,
		autowidth: true,
		loadonce:false,
		jsonReader: {
		},
		onSelectRow : function(rowid, status, e){
			$("#grid_group input[type=radio]").get(rowid - 1).checked = true;

			var list = jQuery("#grid_group").getRowData(rowid);

			//사용자 리스트
			var myPostData = $("#grid_user").jqGrid('getGridParam', 'postData');
			myPostData.grpId = list.grpId;
			myPostData.authLvl = list.authLvl;
			$("#grid_user").trigger("reloadGrid");


			//CCTV용도
			myPostData = $("#grid_cctv_used").jqGrid('getGridParam', 'postData');
			myPostData.grpId = list.grpId;
			myPostData.authLvl = list.authLvl;
			$("#grid_cctv_used").trigger("reloadGrid");


			//CCTV권한레벨
			myPostData = $("#grid_cctv_auth").jqGrid('getGridParam', 'postData');
			myPostData.grpId = list.grpId;
			myPostData.authLvl = list.authLvl;
			$("#grid_cctv_auth").trigger("reloadGrid");

			//이벤트 등록 팝업 요청시 사용
			$("#grpId").val(list.grpId);
			$("#authLvl").val(list.authLvl);

			//타이틀
			$("#sGrp").val(list.grpNmKo);
			$("#sLvl").val(list.authLvlNm);
			$("#fcltTitle").html("CCTV용도 [ " + $("#sGrp").val() + " > " + $("#sLvl").val() + " ]");
			$("#cctvAuthTitle").html("CCTV권한레벨 [ " + $("#sGrp").val() + " > " + $("#sLvl").val() + " ]");

		},
		beforeProcessing: function(data, status, xhr){
			$.setUserOption("#grid_cctv_auth", "PTZ_CNTR_TY", ptzCntrTy);
		/*	$("#grid_user").clearGridData();
			$("#grid_cctv_used").clearGridData();
			$("#grid_cctv_auth").clearGridData(); */
			$("#grid_user").jqGrid('clearGridData');
			$("#grid_cctv_used").jqGrid('clearGridData');
			$("#grid_cctv_auth").jqGrid('clearGridData');

		},
		loadComplete: function(){
			$("#grid_group input[type=radio]").change(function(){
				$("#grid_group").jqGrid('setSelection',$(this).val(),true);
			});

			// 첫번째 열 선택
			setTimeout(function() {
				let $rows = $('#grid_group tr.jqgrow[role=row]');
				if ($rows.length) $('#grid_group').jqGrid('setSelection', $rows.get(0).id);
			}, 500);
		}
 	});

	//그룹별 유저
	$.jqGrid('#grid_user', {
		url: '<c:url value='/'/>wrks/sstm/grp/auth_cctv/list_user.json',
		datatype: "json",
		postData: {
			grpId : $.getCurrentRowValue("#grid_group", "grpId"),
			authLvl : $.getCurrentRowValue("#grid_group", "authLvl")
		},
		colNames: [	//'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_user\', this, event);">',
						'사용자아이디',			'사용자명',			'그룹아이디'
				   ],
		colModel: [//{ name:'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
				 	{ name:'userId'   , index:'USER_ID'   , width:100, align:'center'},
				 	{ name:'userNmKo', index:'USER_NM_KO', width:120, align:'center'},
				 	{ name:'grpId'    , index:'GRP_ID'    , 'hidden':true}
		 			],
		pager: '#pager',
		rowNum : 1000,
		height: $("#grid_user").parent().height() - 40,
		sortname: 'USER_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		shrinkToFit: true,
		scrollOffset: 0,
		autowidth: true,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
			var list = jQuery("#grid_user").getRowData(rowid);
			$("#sUser").val(list.USER_NM_KO);
		},
		beforeProcessing: function(data, status, xhr){

		}
 	});

	//그룹별 사용유형
	$.jqGrid('#grid_cctv_used', {
		url: '<c:url value='/'/>wrks/sstm/grp/auth_cctv/list_cctv_used.json',
		datatype: "json",
		postData: {
			grpId : $.getCurrentRowValue("#grid_group", "grpId"),
			authLvl : $.getCurrentRowValue("#grid_group", "authLvl")
		},
		colNames: [	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_cctv_used\', this, event);">',
						'용도코드',		'용도명'
			   ],
		colModel: [
					{ name:'CHECK'          , width:50 , align:'center', editable:true, edittype:'checkbox'
						, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
				 	{ name:'fcltUsedTyCd', index:'FCLT_USED_TY_CD', width:100, align:'center'},
				 	{ name:'fcltUsedTyNm', index:'FCLT_USED_TY_NM', width:120, align:'center'}
	 			],
		pager: '#pager',
		rowNum : 1000,
		height: $("#grid_cctv_used").parent().height() - 40,
		sortname: 'FCLT_USED_TY_CD',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		shrinkToFit: true,
		scrollOffset: 0,
		autowidth: true,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){

		},
		beforeProcessing: function(data, status, xhr){

		}
 	});

	//그룹별 사용권한
	$.jqGrid('#grid_cctv_auth', {
		url: '<c:url value='/'/>wrks/sstm/grp/auth_cctv/list_cctv_auth.json',
		datatype: "json",
		postData: {
			grpId : $.getCurrentRowValue("#grid_group", "grpId"),
			authLvl : $.getCurrentRowValue("#grid_group", "authLvl")
		},
		colNames: [	'그룹아이디',	'그룹명',	'레벨',	'권한레벨',	'CCTV접근',	'CCTV권한',	'과거영상조회'
				   ],
		colModel: [
					{ name:'grpId'        , index:'GRP_ID'        , 'hidden':true},
					{ name:'grpNmKo'     , index:'GRP_NM_KO'     , width:200, align:'center'},
				 	{ name:'authLvl'      , index:'AUTH_LVL'      , 'hidden':true},
				 	{ name:'authLvlNm'   , index:'AUTH_LVL_NM'   , width:100, align:'center'},
				 	{ name:'cctvAccessYn', index:'CCTV_ACCESS_YN', width:200, align:'center', editable: true, edittype:"select"
				 		, editoptions:{value:""}, useroption: cctvAccessYn, formatter: $.GridSelectBox},
					{ name:'ptzCntrTy'   , index:'PTZ_CNTR_TY'   , width:200, align:'center', editable: true, edittype:"select"
				 		, editoptions:{value:""}, useroption: ptzCntrTy, formatter: $.GridSelectBox},
					{ name:'cctvSearchYn', index:'CCTV_SEARCH_YN', width:200, align:'center', editable: true, edittype:"select"
				 		, editoptions:{value:""}, useroption: cctvSearchYn, formatter: $.GridSelectBox}
		 			],
		pager: '#pager',
		rowNum : 1000,
		sortname: 'GRP_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		shrinkToFit: true,
		scrollOffset: 0,
		autowidth: true,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
		},
		beforeProcessing: function(data, status, xhr){

		},
		loadComplete: function(data){
			if(data.rows.length == 0){
				if( $.getCurrentRowValue("#grid_group", "grpId") != ""){
					authUpdateAction("cctv_auth");
				}
			}else{
				$('#grid_cctv_auth SELECT').selectBox();
			}
            oCommon.jqGrid.gridComplete(this);
		}
 	});

	//그룹별 사용유형
	$.jqGrid('#grid_cctv_used_popup', {
		url: '<c:url value='/'/>wrks/sstm/grp/auth_cctv/list_cctv_used_popup.json',
		datatype: "json",
		postData: {
			grpId : $.getCurrentRowValue("#grid_group", "grpId"),
			authLvl : $.getCurrentRowValue("#grid_group", "authLvl")
		},
		colNames: [	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_cctv_used_popup\', this, event);">',
					'용도코드',		'용도명'
			   ],
		colModel: [  { name:'CHECK', width:50, align:'center', editable:true, edittype:'checkbox'
						, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox}
					,{ name:'fcltUsedTyCd', index:'FCLT_USED_TY_CD', width:300, align:'center'}
					,{ name:'fcltUsedTyNm', index:'FCLT_USED_TY_NM', width:400, align:'center'}
	 			],
		pager: '#pager',
		rowNum : 1000,
		height: $("#grid_cctv_used_popup").parent().height()-40,
		sortname: 'FCLT_USED_TY_CD',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		shrinkToFit: true,
		scrollOffset: 0,
		autowidth: true,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;
		},
		beforeProcessing: function(data, status, xhr){

		}
 	});

	$(".btnS").bind("click",function(){
		var myPostData = $("#grid_group").jqGrid('getGridParam', 'postData');
		//myPostData.dstrtCd = $("#sDstrtCd").val();
		myPostData.grpNm = $("#sGrpNm").val();
		$("#grid_group").trigger("reloadGrid");
	});


});

function resetAction(area, callBack) {
	$.resetInputObject(".layerRegister .tableType1." + area);

	if($.getCurrentRowValue("#grid_group", "grpId") == "") {
		alert("그룹을 선택하세요.");
		return false;
	}

	if(area == "cctv_used") {
		var myPostData = $("#grid_" + area + "_popup").jqGrid('getGridParam', 'postData');
		myPostData.grpId = $.getCurrentRowValue("#grid_group", "grpId");
		myPostData.authLvl = $.getCurrentRowValue("#grid_group", "authLvl");
	}

	$("#grid_" + area + "_popup").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/grp/auth_cctv/list_" + area + "_popup.json"});
	$("#grid_" + area + "_popup").jqGrid('setGridParam', {
		beforeProcessing: callBack
	});

	$("#grid_" + area + "_popup").trigger("reloadGrid");

}

function validate(area) {
	return $.validate(".layerRegister .tableType2." + area);
}

function insertAction(area, src) {

	var s =  $.getSelRow("#grid_" + area + "_popup");
	var url = "<c:url value='/'/>wrks/sstm/grp/auth_cctv/insert_" + area + ".json";
	var params = "";

	if(s.length == 0){
		alert("데이터를 선택해 주십시오");
		return false;
	}

	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid_" + area + "_popup").getRowData(s[i]);
		params += "&grpId=" + encodeURIComponent($.getCurrentRowValue("#grid_group", "grpId"));
		params += "&authLvl=" + encodeURIComponent($.getCurrentRowValue("#grid_group", "authLvl"));
		if(area == "cctv_used"){
			params += "&fcltUsedTyCd=" + encodeURIComponent(list.fcltUsedTyCd);
		}else if(area == "cctv_auth"){
			params += "&ptzCntrTy=" + encodeURIComponent($("#ptzCntrTy").val());
			params += "&cctvAccessYn=" + encodeURIComponent($("#cctvAccessYn").val());
			params += "&cctvSearchYn=" + encodeURIComponent($("#cctvSearchYn").val());
		}
	}

	$.ajaxEx($("#grid_" + area), {
		type : "POST",
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid_" + area).trigger("reloadGrid");
			alert(data.msg);
		},
		error:function(e){
			alert(data.msg);
		}
	});
}



function updateAction(area) {

	var url = "<c:url value='/'/>wrks/sstm/grp/auth_cctv/insert_" + area + ".json";
	var params = "";

	params += "&grpId=" + encodeURIComponent($.getCurrentRowValue("#grid_group", "grpId"));
	params += "&authLvl=" + encodeURIComponent($.getCurrentRowValue("#grid_group", "authLvl"));
	if(area == "cctv_auth"){
		params += "&ptzCntrTy=" + encodeURIComponent($.getCustomObjectValue("#grid_" + area, "select", "ptzCntrTy", 1));
		params += "&cctvAccessYn=" + encodeURIComponent($.getCustomObjectValue("#grid_" + area, "select", "cctvAccessYn", 1));
		params += "&cctvSearchYn=" + encodeURIComponent($.getCustomObjectValue("#grid_" + area, "select", "cctvSearchYn", 1));
	}


	$.ajaxEx($("#grid_" + area), {
		type : "POST",
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid_" + area).trigger("reloadGrid");
			alert(data.msg);
		},
		error:function(e){
			alert(data.msg);
		}
	});
}

function authUpdateAction(area) {

	var url = "<c:url value='/'/>wrks/sstm/grp/auth_cctv/insert_" + area + ".json";
	var params = "";

	params += "&grpId=" + encodeURIComponent($.getCurrentRowValue("#grid_group", "grpId"));
	params += "&authLvl=" + encodeURIComponent($.getCurrentRowValue("#grid_group", "authLvl"));
	//params += "&cctvAccessYn=" + encodeURIComponent($.getCurrentRowValue("#grid_cctv_auth", "CCTV_ACCESS_YN"));
	//params += "&cctvSearchYn=" + encodeURIComponent($.getCurrentRowValue("#grid_cctv_auth", "CCTV_SEARCH_YN"));
	params += "&ptzCntrTy=VIEW";
	params += "&cctvAccessYn=N";
	params += "&cctvSearchYn=N";

	$.ajaxEx($("#grid_" + area), {
		type : "POST",
		url : url,
		datatype: "json",
		data: params,
		success:function(data){
			$("#grid_" + area).trigger("reloadGrid");
		},
		error:function(e){

		}
	});
}


function deleteMultiAction(area) {
	var s =  $.getSelRow("#grid_" + area);
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/grp/auth_cctv/delete_" + area + ".json";
	var params = "";

	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid_" + area).getRowData(s[i]);
		params += "&grpId=" + encodeURIComponent($.getCurrentRowValue("#grid_group", "grpId"));
		params += "&authLvl=" + encodeURIComponent($.getCurrentRowValue("#grid_group", "authLvl"));
		if(area == "cctv_used") {
			params += "&fcltUsedTyCd=" + encodeURIComponent(list.fcltUsedTyCd);
		}
	}

	$.ajaxEx($("#grid_" + area), {
		type : "POST",
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid_" + area).trigger("reloadGrid");
			alert("자료를 삭제하였습니다.");
		},
		error:function(e){
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
				<div class="boxWrap">
					<div class="tbLeft50">
						<div class="tableTypeFree seachT">
							<table>
								<caption>CCTV권한</caption>
								<tbody>
								<tr><th>그룹명</th>
									<td><input type="text" name="" id="sGrpNm" class="txtType grpNm searchEvt" style="ime-mode:active">
										<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
									</td>
								<%--<th>지구</th>
									<td><select name="" id="sDstrtCd" class="selectType1">
											<option value="">전체</option>
											<c:forEach items="${listCmDstrtCdMng}" var="val">
												<option value="${val.DSTRT_CD}"><c:out value="${val.DSTRT_NM}" ></c:out></option>
											</c:forEach>
										</select>
									</td>--%>
								</tr>
								</tbody>
							</table>
						</div><br/>
						<div class="tableType1" style="height:250px;">
							<table id="grid_group" style="width:100%">
							</table>
						</div>
					</div>
					<div class="tbRight50">
						<div class="searchBox50">
							<dl>
								<dt>사용자 리스트</dt>
							</dl>
						</div>
						<div class="tableType1" style="height:250px;">
							<table id="grid_user" style="width:100%">
							</table>
						</div>

					</div>
				</div><br/>
				<div class="boxWrap">
					<input type="hidden" id="sGrp" />
					<input type="hidden" id="sLvl" />
					<input type="hidden" id="sUser" />
					<div class="tbLeft50">
						<div class="searchBox50">
							<dl>
								<dt id="fcltTitle">CCTV용도</dt>
							</dl>
						</div>
						<div class="tableType1" style="height:250px;">
							<table id="grid_cctv_used" style="width:100%">
							</table>
						</div>
						<div class="btnWrap btnR">
							<a href="#" class="btn btnDt btnRgt" area="cctv_used">추가</a>
							<a href="#" class="btn btnMultiDe" area="cctv_used">삭제</a>
						</div>
					</div>
					<div class="tbRight50">
						<div class="searchBox50">
							<dl>
								<dt id="cctvAuthTitle">CCTV권한레벨</dt>
							</dl>
						</div>
						<div class="tableType1" style="height:150px;">
							<table id="grid_cctv_auth" style="width:100%">
							</table>
						</div>
						<div>
							<dl>
								<dt id="cctvAuthNotes"> * CCTV접근 : CCTV 영상조회 여부</dt>
								<dt id="cctvAuthNotes"> * CCTV권한 : CCTV 조작권한 부여</dt>
								<dt id="cctvAuthNotes"> * 과거영상조회 : 영상보기 대화면창에서 과거영상 조회 여부</dt>
							</dl>
						</div>
						<div class="btnWrap btnR">
							<a href="#" class="btn btnDt btnSv" area="cctv_auth">저장</a>
						</div>
					</div>
				</div>
			</div>

			<!-- //레이어팝업 등록 -->
			<div class="layer layerRegister"  id="div_drag_2">
				<div class="tit"><h4></h4></div>
				<div class="layerCt">
					<div class="tableType1 cctv_used" user-title="시설물용도 추가" style="height:500px;">
						<table id="grid_cctv_used_popup" style="width:100%">
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnSv">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>

			<!-- //레이어팝업 등록 -->

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
