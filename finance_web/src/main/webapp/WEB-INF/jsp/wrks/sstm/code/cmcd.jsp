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
		url: contextRoot + '/wrks/sstm/code/cmcd/list.json',
		datatype: "json",
		postData: { 
			groupId : $("#groupId").val(),
			groupNm : $("#groupNm").val(),
			useTyCd : $("#useTyCd").val()
		},
		colNames: ['<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
					'그룹아이디', '그룹명', '그룹영문명', '시스템코드', '시스템명', '사용유형코드', '사용유형', '그룹설명'
		],
		colModel: [	{ name:'CHECK'  , width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{ name:'cdGrpId', index:'CD_GRP_ID', width:300, align:'center'},
					{ name:'cdNmKo' , index:'CD_NM_KO' , width:500, align:'center'},
					{ name:'cdNmEn' , index:'CD_NM_EN' , hidden:true},
					{ name:'sysCd'  , index:'SYS_CD'   , hidden:true},
					{ name:'sysNmKo', index:'SYS_NM_KO', hidden:true},
					{ name:'useTyCd', index:'USE_TY_CD', hidden:true},
					{ name:'useNm'  , index:'USE_NM'   , width:130, align:'center'},
					{ name:'cdDscrt', index:'CD_DSCRT' , hidden:true}
		  ],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		autowidth: true,
		height: $('#grid').parent().height() - 40,
		sortname:'CD_GRP_ID',
		sortorder: 'DESC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "cd_grp_id",
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
			records: function(obj) { return $.showCount(obj); }
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);
			
			$("#dCdGrpId").html(list.cdGrpId);
			$("#dCdNmKo").html(list.cdNmKo);
			$("#dCdNmEn").html(list.cdNmEn);
			$("#dSysCd").html(list.sysCd);
			$("#dSysNmKo").html(list.sysNmKo);			
			$("#dUseNm").html(list.useNm);
			$("#dCdDscrt").html(list.cdDscrt);
			
			$("#iCdGrpId").val(list.cdGrpId);
			$("#iCdNmKo").val(list.cdNmKo);
			$("#iCdNmEn").val(list.cdNmEn);
			$.selectBarun("#iSysCd", list.sysCd);
			$.selectBarun("#iUseTyCd", list.useTyCd);
			$("#iCdDscrt").val(list.cdDscrt);
			$("#cdGrpIdBk").val(list.cdGrpId);
			
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

	$(".btnS").bind("click",function(){
		$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
		var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
		
		//검색할 조건의 값을 설정한다.
		myPostData.groupId = $("#groupId").val();
		myPostData.groupNm = $("#groupNm").val();
		myPostData.useTyCd = $("#useTyCd").val();
		
		$("#grid").trigger("reloadGrid");
	});
    	
   	$(".tableType1").css('height', window.innerHeight - 350);
   	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
	  
});

function resetAction() {
	$.resetInputObject(".layerRegister .tableType2");
	//alert("resetAction");
	/*
	$("#iCdGrpId").val("");
	$("#iCdNmKo").val("");
	$("#iSysCd").get(0).selectedIndex=0;
	$("#iCdNmEn").val("");
	$("#iUseTyCd").get(0).selectedIndex=0;
	$("#iCdDscrt").val("");
	$("#cdGrpIdBk").val("");
	*/
}

function updateAction(obj) {
	//alert('updateAction');
	
	var url = contextRoot + "/wrks/sstm/code/cmcd/update.json";  
	var params = "cdGrpId=" + encodeURIComponent($("#iCdGrpId").val());
		params += "&cdNmKo=" + encodeURIComponent($("#iCdNmKo").val());  
		params += "&sysCd=" + $("#iSysCd").val();  
		params += "&cdNmEn=" + encodeURIComponent($("#iCdNmEn").val());  
		params += "&useTyCd=" + $("#iUseTyCd").val();  
		params += "&cdDscrt=" + encodeURIComponent($("#iCdDscrt").val());  
		params += "&cdGrpIdBk=" + $("#cdGrpIdBk").val();
		//params += "&page=" + $("#grid").getGridParam('page');
		//params += "&page=" + $("#cur-page").val();
		
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
	//alert('insertAction');
	var url = contextRoot + "/wrks/sstm/code/cmcd/insert.json";  
	var params = "cdGrpId=" + encodeURIComponent($("#iCdGrpId").val());
		params += "&cdNmKo=" + encodeURIComponent($("#iCdNmKo").val());
		params += "&sysCd=" + $("#iSysCd").val();
		params += "&cdNmEn=" + encodeURIComponent($("#iCdNmEn").val());
		params += "&useTyCd=" + $("#iUseTyCd").val();
		params += "&cdDscrt=" + encodeURIComponent($("#iCdDscrt").val());

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
	//alert('deleteAction');

	var url = contextRoot + "/wrks/sstm/code/cmcd/delete.json";  
	//var params = "cdGrpId=" + $("#dCdGrpId").text();  
	var params = "cdGrpIdBk=" + $("#cdGrpIdBk").val();

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
	//alert('deleteMultiAction');

	
	//var s =  $("#grid").jqGrid('getGridParam', 'selarrrow');
	//s = $("grid").getGridParam('selarrrow');
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/wrks/sstm/code/cmcd/deleteMulti.json";  
	var params = "";  
	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		
		params += "&cdGrpId=" + list.CD_GRP_ID;
		
	}
	//alert(params);

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
				<div class="tableTypeHalf seachT">
					<table>
						<caption>공통코드그룹</caption>
						<tbody>
						<tr>
							<th>그룹아이디</th>
							<td><input type="text" name="" id="groupId" class="txtType txtType100 searchEvt" style="ime-mode:inactive"></td>
							<th>그룹명</th>
							<td><input type="text" name="" id="groupNm" class="txtType txtType100 searchEvt" style="ime-mode:active">
							<th>사용유형</th>
							<td><select name="" id="useTyCd" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<c:forEach items="${useGrpList}" var="val"> 
										<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>   
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
					<table id="grid" style="width:100%">
					</table>
				</div>
				<div class="paginate">
				</div>
				<div class="btnWrap btnR">
					<a href="#" class="btn btnDt btnRgt">등록</a>
					<a href="#" class="btn btnMultiDe">삭제</a>
				</div>
			</div>
			
			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>공통코드 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>공통코드 상세</caption>
							<tbody>
							<tr><th>코드그룹아이디</th>							 <td id="dCdGrpId"></td>
								<th>그룹한글명</th>								<td id="dCdNmKo"></td>
							</tr>
							<tr><th>시스템명</th>								 <td id="dSysNmKo"></td>
								<th>그룹영문명</th>								<td id="dCdNmEn"></td>
							</tr>
							<tr><th>사용유형</th>								<td id="dUseNm" colspan="3"></td>
							</tr>
							<tr><th>코드설명</th>								 <td id="dCdDscrt" colspan="3"></td>
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
			
			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister"  id="div_drag_2" id="div_drag_2">
				<div class="tit"><h4>공통코드 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<input type="hidden" id="cdGrpIdBk" />
						<table>
							<caption>공통코드 등록</caption>
							<tbody>
							<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>코드그룹아이디</th>
								<td>
									<input type="text" id="iCdGrpId" class="txtType" maxlength="40" required="required" user-title="코드그룹아이디" user-required="insert"  style="ime-mode:inactive"/>				
								</td>
								<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>그룹한글명</th>
								<td><input type="text" id="iCdNmKo" class="txtType" maxlength="100" required="required" user-title="그룹한글명" style="ime-mode:active"/></td>
							</tr>
							<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>시스템명</th>
								<td>
									<select name="" id="iSysCd" class="selectType1" maxlength="10">
										<c:forEach items="${sysNmList}" var="val"> 
											<option value="${val.sysId}"><c:out value="${val.sysNmKo}" ></c:out></option>   
										</c:forEach>					 
									</select>
								</td>
								<th>그룹영문명</th>
								<td><input type="text" id="iCdNmEn" class="txtType" maxlength="100" style="ime-mode:inactive"/></td>
							</tr>
							<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용유형</th>
								<td colspan="3">
									<select name="" id="iUseTyCd" class="selectType1" maxlength="1">
										<c:forEach items="${useGrpList}" var="val"> 
											<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>   
										</c:forEach>					 
									</select>
								</td>
							</tr>
							<tr><th>코드설명</th>
								<td colspan="3"><textarea class="textArea" id="iCdDscrt" maxlength="1000" style="ime-mode:active"></textarea></td>
							</tr>
							</tbody>
						</table>
					</div>
					 <div class="btnCtr">
						<input type="hidden" name="svTag" id="svTag" />
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
<script src="<c:url value='/js/wrks/sstm/code/cmcd.js' />"></script>
</body>
</html> 