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
$(document).ready(function(){

	$.jqGrid('#grid_group', {
		url: '<c:url value='/'/>wrks/sstm/grp/auth/list.json',
		datatype: "json",
		postData: {
		 	//dstrtCd : $("#dstrtCd").val(),
		 	grpNmKo : $("#grpNmKo").val()
		},
		colNames: [	''	, '그룹아이디'	, '그룹명'	, '지구명'	, '지구코드'
		],
		colModel: [{ name:'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
						, formatter: function (cellValue, option) {
							return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
						}
					}
					, { name:'grpId'  , index:'GRP_ID'   , width:160, align:'center'}
					, { name:'grpNmKo', index:'GRP_NM_KO', width:230, align:'center'}
					, { name:'dstrtNm', index:'DSTRT_NM' , hidden:true}
					, { name:'dstrtCd', index:'DSTRT_CD' , hidden:true}
		  ],
		pager: '#pager',
		rowNum : 1000,
		autowidth: true,
		height: $("#grid_group").parent().height()-40,
		sortname:'GRP_NM_KO',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onSelectRow : function(rowid, status, e){
			$("#grid_auth_lvl").setGridParam({url:"<c:url value='/'/>wrks/sstm/grp/auth/list_auth_lvl.json"});
			$("#grid_group input[type=radio]").get(rowid - 1).checked = true;
			var list = jQuery("#grid_group").getRowData(rowid);

			$("#iGrpId").html(list.grpId);

			var myPostData = $("#grid_auth_lvl").jqGrid('getGridParam', 'postData');
			myPostData.grpId = list.grpId;
			$("#grid_auth_lvl").trigger("reloadGrid");
		},
		beforeProcessing: function(data, status, xhr){
			$("#grid_auth_lvl").clearGridData();
		},
		loadComplete : function() {
			$("#grid_group input[type=radio]").change(function(){
				$("#grid_group").jqGrid('setSelection',$(this).val(),true);
			});
		}
	});

	$.jqGrid('#grid_auth_lvl', {
		//url: '<c:url value='/'/>wrks/sstm/grp/auth/list_auth_lvl.json',
		datatype: "json",
		autowidth: true,
		postData: {
			grpId : ""
		},
		colNames: [ 	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_auth_lvl\', this, event);">'
						, '그룹아이디'	, '권한레벨'	, '권한레벨명'	, '상위권한레벨'	, '등록자아이디'	, '등록일'	, '수정자아이디'	, '수정일'
		],
		colModel: [
					{ name:'CHECK',		  width:90,  align:'center', editable:true
						, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}
					, { name:'grpId'     , index:'GRP_ID'      , width:100, align:'center', hidden:true}
					, { name:'authLvl'   , index:'AUTH_LVL'    , width:100, align:'center'}
					, { name:'authLvlNm' , index:'AUTH_LVL_NM' , width:150, align:'center'}
					, { name:'authLvlTop', index:'AUTH_LVL_TOP', width:155, align:'center', hidden:true}
					, { name:'rgsUserId' , index:'RGS_USER_ID' , width:100, align:'center', hidden:true}
					, { name:'rgsDate'   , index:'RGS_DATE'    , width:100, align:'center', hidden:true}
					, { name:'updUserId' , index:'UPD_USER_ID' , width:100, align:'center', hidden:true}
					, { name:'updDate'   , index:'UPD_DATE'    , width:100, align:'center', hidden:true}
		],
		pager: '#pager',
		rowNum : 1000,
		height: $("#grid_auth_lvl").parent().height() - 40,
		sortname:'GRP_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
 			if(iCol == 0) return false;

			var list = jQuery("#grid_auth_lvl").getRowData(rowid);

			$("#dGrpId").html(list.grpId);
			$("#dAuthLvl").html(list.authLvl);
			$("#dAuthLvlNm").html(list.authLvlNm);
			$("#dAuthLvlTop").html(list.authLvlTop);
			$("#dRgsUserId").html(list.rgsUserId);
			$("#dRgsDate").html(list.rgsDate);
			$("#dUpdtUserId").html(list.updUserId);
			$("#dUpdtDate").html(list.updDate);

			$("#iGrpId").html(list.grpId);
			$("#iAuthLvl").val(list.authLvl);
			$("#iAuthLvlNm").val(list.authLvlNm);
			$("#iAuthLvlTop").val(list.authLvlTop);

			$.showDetail();
		}
	});

	$(".btnS").bind("click",function(){
		var myPostData = $("#grid_group").jqGrid('getGridParam', 'postData');
		//myPostData.dstrtCd = $("#dstrtCd").val();
		myPostData.grpNmKo = $("#grpNmKo").val();
		$("#grid_group").trigger("reloadGrid");
	});

	$(".btnRgt1").click(function(){
		var s = $.getSelRowRadio("#grid_group");
		if(s.length == 0) {
			alert("그룹을 선택하세요.");
			return false;
		}else {
			$("#modetitle").text("추가");
			$.changeInputMode(true);
			resetAction();
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
		}
	});

	$(".btnSv1").bind("click",function(){
		if(validate() == false) {
			return false;
		}

		if(insertFlag == true) {
			insertAction();
		}else{
			updateAction();
		};

		$(".mask").remove();
 		$(".layer").hide();
 		insertFlag = false;

		return false;
	});

});

function resetAction() {
	$.resetInputObject(".layerRegister .tableType2");
}

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function insertAction() {
	var url = "<c:url value='/'/>wrks/sstm/grp/auth/insert_auth_lvl.json";

	var rows = $("#grid_auth_lvl").jqGrid('getGridParam', 'records');

	for(var i=1; i<=rows; i++){
		var list = jQuery("#grid_auth_lvl").getRowData(i);
		if(list.AUTH_LVL == $("#iAuthLvl").val()){
			alert("이미 등록된 권한레벨입니다. 다시 확인하여주십시오.");
			return false;
		};
	};

	var params = "&grpId=" + encodeURIComponent($("#iGrpId").text());
	 	params += "&authLvl=" + encodeURIComponent($("#iAuthLvl").val());
	 	params += "&authLvlNm=" + encodeURIComponent($("#iAuthLvlNm").val());
	 	params += "&authLvlTop=" + encodeURIComponent($("#iAuthLvlTop").val());

	$.ajaxEx($("#grid_auth_lvl"), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid_auth_lvl").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			alert(data.msg);
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
}

function updateAction() {
	var url = "<c:url value='/'/>wrks/sstm/grp/auth/update_auth_lvl.json";

	var params = "&grpId=" + encodeURIComponent($("#iGrpId").text());
	 	params += "&authLvl=" + encodeURIComponent($("#iAuthLvl").val());
	 	params += "&authLvlNm=" + encodeURIComponent($("#iAuthLvlNm").val());
	 	params += "&authLvlTop=" + encodeURIComponent($("#iAuthLvlTop").val());

	$.ajaxEx($("#grid_auth_lvl"), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid_auth_lvl").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			alert(data.msg);
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = "<c:url value='/'/>wrks/sstm/grp/auth/delete_auth_lvl.json";
	//var params = "cdGrpId=" + $("#dCdGrpId").text();
	var params = "&grpId=" + $("#dGrpId").text();
		params += "&authLvl="+ $("#dAuthLvl").text();

	$.ajaxEx($('#grid_auth_lvl'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid_auth_lvl").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			alert(data.msg);
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
}

function deleteMultiAction(area) {
	var s =  $.getSelRow("#grid_auth_lvl");
	if(s.length == 0){
		alert("삭제할 데이터를 선택해주세요");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/grp/auth/deleteMulti_auth_lvl.json";
	var params = "";

	for(var i=0; i<s.length; i++){
		var list = jQuery("#grid_auth_lvl").getRowData(s[i]);

		params += "&grpId="+ list.grpId;
		params += "&authLvl="+ list.authLvl;
	}

	$.ajaxEx($("#grid_auth_lvl"), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid_auth_lvl").trigger("reloadGrid");
			alert("자료를 삭제하였습니다.");
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
				<div class="boxWrap">
					<div class="tbLeft50">
						<div class="tableTypeFree seachT">
							<table>
								<caption>그룹별권한레벨관리</caption>
								<tbody>
								<tr><th>그룹명</th>
									<td><input type="text" name="" id="grpNmKo" class="txtType grpNm searchEvt" style="ime-mode:active">
										<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
									</td>
								<%--<th>지구</th>
									<td><select name="" id="dstrtCd" class="selectType1">
											<option value="">전체</option>
											<c:forEach items="${listCmDstrtCdMng}" var="val">
												<option value="${val.dstrtCd}"><c:out value="${val.dstrtNm}" ></c:out></option>
											</c:forEach>
										</select>
									</td>--%>
								</tr>
								</tbody>
							</table>
						</div><br/>
						<div class="tableType1" style="height:550px; overflow-y:scroll; overflow-x:hidden">
							<table id="grid_group" style="width:100%">
							</table>
						</div>
					</div>

					<div class="tbRight50">
						<div class="searchBox50">
							<dl>
								<dt>권한레벨</dt>
							</dl>
						</div>
						<div class="tableType1" style="height:550px; overflow-y:scroll; overflow-x:hidden">
							<table id="grid_auth_lvl" style="width:100%">
							</table>
						</div>
						<div class="btnWrap btnR">
							<a href="#" class="btn btnDt btnRgt1" area="">추가</a>
							<a href="#" class="btn btnMultiDe" area="">삭제</a>
						</div>
					</div>
				</div><br/>
			</div>

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail"  id="div_drag_1">
				<div class="tit"><h4>그룹별 권한레벨 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>그룹별 권한레벨 상세</caption>
							<tbody>
							<tr>
								<th>그룹아이디</th>
								<td id="dGrpId" colspan="3"></td>
							</tr>
							<tr>
								<th>권한레벨</th>
								<td id="dAuthLvl"></td>
								<th>권한레벨명</th>
								<td id="dAuthLvlNm"></td>
							</tr>
							<tr>
								<th>등록자</th>
								<td id="dRgsUserId"></td>
								<th>등록일</th>
								<td id="dRgsDate"></td>
							</tr>
							<tr>
								<th>수정자</th>
								<td id="dUpdtUserId"></td>
								<th>수정일</th>
								<td id="dUpdtDate"></td>
							</tr>
							<tr style="display:none;">
								<th>상위권한레벨</th>
								<td id="dAuthLvlTop"></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnMd">수정</a>
						<a href="#" class="btn btnDe">삭제</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

			<!-- //레이어팝업 등록 -->
			<div class="layer layerRegister"  id="div_drag_2">
				<div class="tit"><h4>그룹별 권한레벨 <span id="modetitle">등록</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>그룹별 권한레벨 상세</caption>
							<tbody>
								<tr><th>그룹아이디</th>
									<td id="iGrpId" colspan="3"></td>
								</tr>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>권한레벨</th>
									<td>
										<input type="text" id="iAuthLvl" class="txtType number" maxlength="2" required="required" user-title="권한레벨" user-required="insert"/>
									</td>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>권한레벨명</th>
									<td>
										<input type="text" id="iAuthLvlNm" class="txtType" maxlength="100" required="required" user-title="권한레벨명" style="ime-mode:active"/>
									</td>
								</tr>
								<tr style="display:none;">
									<th>상위권한레벨</th>
									<td colspan="3">
										<input type="text" id="iAuthLvlTop" class="txtType number" maxlength="2" user-title="상위권한레벨"/>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnSv1">저장</a>
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