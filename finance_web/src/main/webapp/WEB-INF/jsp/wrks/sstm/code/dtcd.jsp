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
$(document).ready(function() {

	$.jqGrid($('#grid_cmcd'), {
		url : contextRoot + '/wrks/sstm/code/dtcd/cmcd/list.json',
		datatype: "json",
		postData: {
			groupId : $("#sGroupId").val(),
			groupNm : $("#sGroupNm").val(),
			useTyCd : $('input:radio[name = "sUseTyCdF"]:checked').val()
		},
		colNames: [	'', '그룹아이디', '그룹명', '사용유형', '그룹영문명', '시스템코드', '시스템명', '사용유형코드', '그룹설명'
		],
		colModel: [{ name:'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
					, formatter:function (cellValue, option) { return '<input type="radio" name="radio" value="' + option.rowId + '"/>'; }
					},{ name:'cdGrpId', index:'CD_GRP_ID', width:150, align:'center'
					},{ name:'cdNmKo' , index:'CD_NM_KO' , width:150, align:'center'
					},{ name:'useNm'  , index:'USE_NM'   , width:100, align:'center'
					},{ name:'cdNmEn' , index:'CD_NM_EN' , hidden:true
					},{ name:'sysCd'  , index:'SYS_CD'   , hidden:true
					},{ name:'sysNmKo', index:'SYS_NM_KO', hidden:true
					},{ name:'useTyCd', index:'USE_TY_CD', hidden:true
					},{ name:'cdDscrt', index:'CD_DSCRT' , hidden:true
					}
				],
		pager: '#pager',
		rowNum : 1000,
		autowidth: true,
		height: $("#grid_cmcd").parent().height() - 40,
		sortname:'CD_GRP_ID',
		sortorder: 'DESC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onSelectRow : function(rowid, status, e){

			$("#grid_cmcd input[type=radio]").get(rowid - 1).checked = true;
			var list = jQuery("#grid_cmcd").getRowData(rowid);
			$("#sCdGrpId").val(list.cdGrpId);

			$("#grid").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/code/dtcd/list.json"});
			var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
			myPostData.cdGrpId = $("#sCdGrpId").val();
			myPostData.useTyCd = "Y";

			$("#grid").trigger("reloadGrid");

		},
		beforeProcessing: function(data, status, xhr){
			$("#grid").clearGridData();
		},
		loadComplete: function(){
			$("#grid_cmcd input[type=radio]").change(function(){
				$("#grid_cmcd").jqGrid('setSelection',$(this).val(),true);
			});
		}
	});

	$.jqGrid($('#grid'), {
		//url: '<c:url value='/'/>wrks/sstm/code/dtcd/list.json',
		datatype: "json",
		postData: {
			cdGrpId : $("#sCdGrpId").val(),
			useTyCd : $('input:radio[name = "sUseTyCdS"]:checked').val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
					 '그룹명', '코드ID', '코드명', '사용유형', '정렬순서', '그룹ID', '코드영문명', '사용여부코드', '코드설명'
				 ],
		colModel: [
				{ name:'check'    , width:50 , align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
				{ name:'cdGrpNmKo', index:'CD_GRP_NM_KO', width:105, align:'center'},
				{ name:'cdId'	  , index:'CD_ID'       , width:80 , align:'center'},
				{ name:'cdNmKo'   , index:'CD_NM_KO'    , width:105, align:'center'},
				{ name:'useNm'	  , index:'USE_NM'      , width:60 , align:'center'},
				{ name:'sortOrdr' , index:'SORT_ORDR'   , width:60 , align:'center'},
				{ name:'cdGrpId'  , hidden:true},
				{ name:'cdNmEn'   , hidden:true},
				{ name:'useTyCd'  , hidden:true},
				{ name:'cdDscrt'  , hidden:true}
		],
		pager: '#pager',
		rowNum : 1000,
		autowidth: true,
		height: 444,
		sortname:'SORT_ORDR',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);

			//$("#dCdGrpId").html(list.cdGrpId);
			$("#dCdGrpId").val(list.cdGrpId);
			$("#dCdGrpNmKo").html(list.cdGrpNmKo);

			$("#dCdId").html(list.cdId);
			$("#dCdNmKo").html(list.cdNmKo);
			$("#dCdNmEn").html(list.cdNmEn);
			$("#dUseNm").html(list.useNm);
			$("#dCdDscrt").html(list.cdDscrt);
			$("#dSortOrdr").html(list.sortOrdr);

			//$("#uCdGrpId").val(list.cdGrpId);
			$.selectBarun("#iCdGrpNmKo", list.cdGrpId);
			$("#iCdId").val(list.cdId);
			$("#iCdNmKo").val(list.cdNmKo);
			$("#iCdNmEn").val(list.cdNmEn);
			$.selectBarun("#iUseNm", list.useTyCd);
			$("#iCdDscrt").val(list.cdDscrt);

			$("#bCdGrpId").val(list.cdGrpId);
			$("#bCdId").val(list.cdId);
			$("#iSortOrdr").val(list.sortOrdr);

			$.showDetail();
		},
		beforeRequest: function() {
			/*
			$.loading(true);
			rowNum = $('#rowPerPageList').val();
			 */
		},
		beforeProcessing: function(data, status, xhr){
			/*
			$.loading(false);
			if(typeof data.rows != "undefine" || data.row != null) {
				$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
			}
			 */
		}
	});

	$(".btnS1").bind("click",function(){

		var myPostData = $("#grid_cmcd").jqGrid('getGridParam', 'postData');

		//검색할 조건의 값을 설정한다.
		myPostData.groupId = $("#sGroupId").val();
		myPostData.groupNm = $("#sGroupNm").val();
		myPostData.useTyCd = $('input:radio[name = "sUseTyCdF"]:checked').val();

		$("#grid_cmcd").trigger("reloadGrid");
	});

	$('input:radio[name = "sUseTyCdS"]').bind("change",function(){
		$("#grid").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/code/dtcd/list.json"});

		var myPostData = $("#grid").jqGrid('getGridParam', 'postData');

		myPostData.cdGrpId = $("#sCdGrpId").val();
		//myPostData.cdGrpNmKo = $("#sCdGrpNmKo").val();
		myPostData.useTyCd = $('input:radio[name = "sUseTyCdS"]:checked').val();

		$("#grid").trigger("reloadGrid");

	});

	$(".btnRgt1").click(function(){
		var s = $.getSelRowRadio("#grid_cmcd");
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

	$(".tableType1").css('height', window.innerHeight - 350);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
});

function resetAction() {
	//$("#iCdGrpNmKo").get(0).selectedIndex=0;
	$.selectBarun("#iCdGrpNmKo", $("#sCdGrpId").val());

	$("#iCdId").val("");
	$("#iCdNmKo").val("");
	$("#iCdNmEn").val("");
	$("#iUseNm").get(0).selectedIndex=0;
	$("#iCdDscrt").val("");
	$("#iSortOrdr").val("");
}

function preModifyAction() {}


function updateAction(obj) {
	var url = "<c:url value='/'/>wrks/sstm/code/dtcd/update.json";
	var params = "cdGrpId=" + encodeURIComponent($("#iCdGrpNmKo").val());
		params += "&cdId=" + encodeURIComponent($("#iCdId").val());
		params += "&cdNmKo=" + encodeURIComponent($("#iCdNmKo").val());
		params += "&cdNmEn=" + encodeURIComponent($("#iCdNmEn").val());
		params += "&useTyCd=" + $("#iUseNm").val();
		params += "&cdDscrt=" + encodeURIComponent($("#iCdDscrt").val());
		params += "&bCdGrpId=" + encodeURIComponent($("#bCdGrpId").val());
		params += "&bCdId=" + encodeURIComponent($("#bCdId").val());
		params += "&sortOrdr=" + encodeURIComponent($("#iSortOrdr").val());

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
			alert(e.responseText);
		}
	});
}

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function insertAction(obj) {
	var url = "<c:url value='/'/>wrks/sstm/code/dtcd/insert.json";
	var params = "cdGrpId=" + encodeURIComponent($("#iCdGrpNmKo").val());
		params += "&cdId=" + encodeURIComponent($("#iCdId").val());
		params += "&cdNmKo=" + encodeURIComponent($("#iCdNmKo").val());
		params += "&cdNmEn=" + encodeURIComponent($("#iCdNmEn").val());
		params += "&useTyCd=" + $("#iUseNm").val();
		params += "&cdDscrt=" + encodeURIComponent($("#iCdDscrt").val());
		params += "&sortOrdr=" + encodeURIComponent($("#iSortOrdr").val());

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
			alert(e.responseText);
		}
	});
}

function deleteAction(obj) {
	var url = "<c:url value='/'/>wrks/sstm/code/dtcd/delete.json";
	//var params = "cdGrpId=" + $("#dCdGrpId").text();
	var params = "cdGrpId=" + $("#dCdGrpId").val();
		params += "&cdId="+ $("#dCdId").text();



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

function deleteMultiAction(obj) {
	//var s =  $("#grid").jqGrid('getGridParam', 'selarrrow');
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/code/dtcd/deleteMulti.json";
	var params = "";

	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&cdGrpId=" + list.cdGrpId;
		params += "&cdId=" + list.cdId;
	}
	//alert(params); return;

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
				<div class="boxWrap">
					<div class="tbLeft50">
						<div class="tableTypeFree seachT">
							<table>
								<caption>공통코드그룹</caption>
								<tbody>
								<tr>
									<th>아이디</th>
									<td><input type="text" name="" id="sGroupId" class="txtType txtType100px searchEvt" style="ime-mode:inactive"></td>
									<th>그룹명</th>
									<td><input type="text" name="" id="sGroupNm" class="txtType txtType100px searchEvt" style="ime-mode:active">
							<!-- </tr>
								<tr> -->
									<th>사용유형</th>
									<td colspan = "2">
										<label><input type="radio" name="sUseTyCdF" title="전체" value=""/> 전체 </label>
										<label><input type="radio" name="sUseTyCdF" title="사용" value="Y" checked="checked"/> 사용</label>
										<label><input type="radio" name="sUseTyCdF" title="미사용" value="N"/> 미사용</label>
										<label><input type="radio" name="sUseTyCdF" title="삭제" value="D"/> 삭제</label>
									</td>
									<td>
										<a href="javascript:;" class="btn btnRight btnS1 searchBtn">검색</a>
									</td>
								</tr>
								</tbody>
							</table>
						</div><br/>
						<div class="tableType1" style="height: 500px;">
							<table id="grid_cmcd" style="width:100%">
							</table>
						</div>
					</div>

					<div class="tbRight50">
						<div class="tableTypeFree seachT">
							<input type="hidden" id="sCdGrpId" />
							<table>
								<caption>코드상세</caption>
								<tbody>
								<tr>
									<th>사용유형</th>
									<td colspan="2">
										<label><input type="radio" name="sUseTyCdS" title="전체" value=""/> 전체 </label>
										<label><input type="radio" name="sUseTyCdS" title="사용" value="Y" checked="checked"/> 사용</label>
										<label><input type="radio" name="sUseTyCdS" title="미사용" value="N"/> 미사용</label>
										<label><input type="radio" name="sUseTyCdS" title="삭제" value="D"/> 삭제</label>
									</td>
								</tr>
								</tbody>
							</table>
						</div><br/>
						<div class="tableType1"  style="height:538px;">
							<table id="grid" style="width:100%">
							</table>
						</div>
						<div class="paginate">
						</div>
						<div class="btnWrap btnR">
							<a href="#" class="btn btnDt btnRgt1" area="">추가</a>
							<a href="#" class="btn btnMultiDe" area="">삭제</a>
						</div>
					</div>
				</div>
			</div>
			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>코드상세정보 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<input type="hidden" id="dCdGrpId" />
						<table>
							<caption>코드상세정보 상세</caption>
							<tbody>
								<tr>
									<th>그룹명</th>
									<td id="dCdGrpNmKo"></td>
									<th>코드아이디</th>
									<td id="dCdId"></td>
								</tr>
								<tr>
									<th>코드명</th>
									<td id="dCdNmKo"></td>
									<th>정렬순서</th>
									<td id="dSortOrdr"></td>
								</tr>
								<tr>
									<th>사용유형</th>
									<td id="dUseNm" colspan="3"></td>
								</tr>
								<tr>
									<th>코드설명</th>
									<td id="dCdDscrt" colspan="3"></td>
								</tr>
								<tr class="hide">
									<th>코드영문명</th>
									<td id="dCdNmEn"></td>
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
			<div class="layer layerRegister" id="div_drag_2">
				<div class="tit"><h4>코드상세정보 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<input type="hidden" id="bCdGrpId" />
						<input type="hidden" id="bCdId" />
						<table>
							<caption>코드상세정보 등록</caption>
							<tbody>
								<tr>
									<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>그룹명</th>
									<td>
										<select name="" id="iCdGrpNmKo" class="selectType1" maxlength="40">
											<c:forEach items="${cdGrpList}" var="val">
												<option value="${val.cdGrpId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>코드아이디</th>
									<td><input type="text" name="" id="iCdId" class="txtType" maxlength="40"  required="required" user-title="코드아이디" user-required="insert" style="ime-mode:inactive"/></td>
								</tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>코드명</th>
									<td><input type="text" name="" id="iCdNmKo" class="txtType" maxlength="100" required="required" user-title="코드한글명" style="ime-mode:active"/></td>
									<th>정렬순서</th>
									<td><input type="text" name="" id="iSortOrdr" class="txtType number" maxlength="10"/></td>
								</tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용유형</th>
									<td colspan="3">
										<select name="" id="iUseNm" class="selectType1">
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<th>코드설명</th>
									<td colspan="3"><textarea class="textArea" id="iCdDscrt" maxlength="1000" style="ime-mode:active"></textarea></td>
								</tr>
								<tr class="hide">
									<th>코드영문명</th>
									<td><input type="text" name="" id="iCdNmEn" class="txtType" maxlength="100" style="ime-mode:inactive"/></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnSvEc">저장</a>
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
<script src="<c:url value='/js/wrks/sstm/code/dtcd.js' />"></script>
</body>
</html>