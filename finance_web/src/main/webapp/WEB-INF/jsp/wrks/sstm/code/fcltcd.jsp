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
		url: '<c:url value='/'/>wrks/sstm/code/fcltcd/list.json',
		datatype: "json",
		autowidth: true,
		postData: {
			fcltKndCd : $("#sFcltKndCd").val(),
			fcltUsedTyNm: $("#sFcltUsedTyNm").val(),
			useTyCd : $("#sUseTyCd").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '시설물종류' , '용도명' , '용도별유형코드' , '사용유형' , '수정일' , '등록자아이디' , '등록일' , '수정자아이디' , '수정일' , '시설물종류코드' , '사용유형코드'
				   ],
		colModel: [{ name: 'CHECK', width:60, align:'center', editable:true
						, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'fcltKndNm'   , index: 'FCLT_KND_NM'    , width:235, align:'center'
					}, { name: 'fcltUsedTyNm', index: 'FCLT_USED_TY_NM', width:200, align:'center'
					}, { name: 'fcltUsedTyCd', index: 'FCLT_USED_TY_CD', width:250, align:'center'
					}, { name: 'useTyNm'     , index: 'USE_TY_NM'      , width:100, align:'center'
					}, { name: 'updDate'     , index: 'UPD_DATE'       , width:130, align:'center'
					}, { name: 'rgsUserId'   , index: 'RGS_USER_ID'    , width:250, align:'center', hidden:true
					}, { name: 'rgsDate'     , index: 'RGS_DATE'       , width:100, align:'center', hidden:true
					}, { name: 'updUserId'   , index: 'UPD_USER_ID'    , width:100, align:'center', hidden:true
					}, { name: 'updDate'     , index: 'UPD_DATE'       , width:100, align:'center', hidden:true
					}, { name: 'fcltKndCd'   , index: 'FCLT_KND_CD'    , width:200, align:'center', hidden:true
					}, { name: 'useTyCd'     , index: 'USE_TY_CD'      , width:200, align:'center', hidden:true
					}
				  ],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height() - 40,
		sortname: 'FCLT_USED_TY_NM',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "FCLT_USED_TY_CD",
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
		onCellSelect: function(rowid, iCol, cellcontent, e){

			if(iCol == 0) return false;
			var list = jQuery("#grid").getRowData(rowid);

			$("#dFcltUsedTyCd").html(list.fcltUsedTyCd);
			$("#dFcltUsedTyNm").html(list.fcltUsedTyNm);
			$("#dFcltKndNm").html(list.fcltKndNm);
			$("#dUseTyNm").html(list.useTyNm);
			$("#dRgsUserId").html(list.rgsUserId);
			$("#dRgsDate").html(list.rgsDate);
			$("#dUpdUserId").html(list.updUserId);
			$("#dUpdDate").html(list.updDate);

			$("#dFcltIcon0").html("");
			$("#dFcltIcon1").html("");
			$("#dFcltIcon2").html("");

			$("#dFcltIcon0").prepend('<img src="<c:url value='/'/>images/fclt/b0_'+ list.fcltUsedTyCd +'_0.png">');
			$("#dFcltIcon1").prepend('<img src="<c:url value='/'/>images/fclt/b0_'+ list.fcltUsedTyCd +'_1.png">');
			$("#dFcltIcon2").prepend('<img src="<c:url value='/'/>images/fclt/b0_'+ list.fcltUsedTyCd +'_2.png">');

			$("#dFcltIcon0").append(" b0_"+ list.fcltUsedTyCd +"_0.png");
			$("#dFcltIcon1").append(" b0_"+ list.fcltUsedTyCd +"_1.png");
			$("#dFcltIcon2").append(" b0_"+ list.fcltUsedTyCd +"_2.png");

			$("#iFcltTyIcon0").val("b0_"+ list.fcltUsedTyCd +"_0.png");
			$("#iFcltTyIcon1").val("b0_"+ list.fcltUsedTyCd +"_1.png");
			$("#iFcltTyIcon2").val("b0_"+ list.fcltUsedTyCd +"_2.png");

			$("#iFcltUsedTyCd").val(list.fcltUsedTyCd);
			$("#iFcltUsedTyNm").val(list.fcltUsedTyNm);
			$.selectBarun("#iFcltKndCd", list.fcltKndCd);
			$.selectBarun("#iUseTyCd", list.useTyCd);

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
		myPostData.fcltKndCd = $("#sFcltKndCd").val();
		myPostData.fcltUsedTyNm = $("#sFcltUsedTyNm").val();
		myPostData.useTyCd = $("#sUseTyCd").val();

		$("#grid").trigger("reloadGrid");
	});

  	//file Element 핸들링
	$(".btnFileObject").bind("click",function(){
		var id = $(this).attr("user-ref-id");
   		$("#" + id).click();
		return false;
	});

	$(".upload").bind("change", function(){
		$.checkChangeFileObject2(this);
		return false;
	});

	
   	$(".tableType1").css('height', window.innerHeight - 350);
   	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
	    	
});

function resetAction() {
	$("#iFcltUsedTyCd").val("");
	$("#iFcltUsedTyNm").val("");
	$("#iFcltKndCd").get(0).selectedIndex=0;
	$("#iUseTyCd").get(0).selectedIndex=0;

	$("#iFcltTyIcon0").val("");
	$("#iFcltTyIcon1").val("");
	$("#iFcltTyIcon2").val("");
}


function updateAction(obj) {
	var url = "<c:url value='/'/>wrks/sstm/code/fcltcd/insert.json";
	var params = "fcltUsedTyCd=" + escape(encodeURIComponent($("#iFcltUsedTyCd").val()));
   		params += "&fcltUsedTyNm=" + escape(encodeURIComponent($("#iFcltUsedTyNm").val()));
		params += "&fcltKndCd=" + escape(encodeURIComponent($("#iFcltKndCd").val()));
		params += "&useTyCd=" + escape(encodeURIComponent($("#iUseTyCd").val()));

		params += "&div="+ escape(encodeURIComponent('UPDT'));

		try{
			if($.multiFileUpload2(".layerRegister", 	url, params, function(){
				$("#grid").setGridParam({page :$("#cur-page").val()});
				$("#grid").trigger("reloadGrid");
	   			$(".mask").remove();
	   			$(".layer").hide();
		   	   }) == false){
	   				return false;
		   	   };
		}catch(e){}
}

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function insertAction(obj) {
	var url = "<c:url value='/'/>wrks/sstm/code/fcltcd/insert.json";

	var params = "fcltUsedTyCd=" + escape(encodeURIComponent($("#iFcltUsedTyCd").val()));
		params += "&fcltUsedTyNm=" + escape(encodeURIComponent($("#iFcltUsedTyNm").val()));
		params += "&fcltKndCd=" + escape(encodeURIComponent($("#iFcltKndCd").val()));
		params += "&useTyCd=" + escape(encodeURIComponent($("#iUseTyCd").val()));

		params += "&div="+ escape(encodeURIComponent('INST'));
	try{
		if($.multiFileUpload2(".layerRegister", 	url, params, function(){
			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
   			$(".mask").remove();
   			$(".layer").hide();
	   	   }) == false){
   				return false;
	   	   };
	}catch(e){}
}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = "<c:url value='/'/>wrks/sstm/code/fcltcd/delete.json";
	var params = "fcltUsedTyCd=" + $("#iFcltUsedTyCd").val();

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

	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/code/fcltcd/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&fcltUsedTyCd=" + list.fcltUsedTyCd;
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
				<div class="tableTypeHalf seachT">
					<table>
						<caption>시설물용도별유형코드</caption>
						<colgroup>
							<col style="width: 150px;" />
							<col style="width: *" />
							<col style="width: 150px;" />
							<col style="width: *" />
							<col style="width: 150px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
							<th>시설물종류</th>
							<td><select name="" id="sFcltKndCd" class="selectType1" maxlength="1">
									<option value="">전체</option>
									<c:forEach items="${fcltKndList}" var="val">
										<option value="${val.cdId}" <c:if test="${val.cdId == 'CTV'}">selected</c:if> ><c:out value="${val.cdNmKo}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>용도명</th>
							<td><input type="text" name="sFcltUsedTyNm" id="sFcltUsedTyNm" class="txtType txtType100px searchEvt" style="ime-mode: inactive">
							</td>
							<th>사용유형</th>
							<td><select name="" id="sUseTyCd" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
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
				<!-- 
				<div class="btnWrap btnR">
					<a href="#" class="btn btnDt btnRgt">등록</a>
					<a href="#" class="btn btnMultiDe">삭제</a>
				</div>
				 -->
			</div>

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>시설물용도별유형코드 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>시설물용도별유형코드 상세</caption>
							<tbody>
								<tr>
									<th>용도별유형코드</th>
									<td id="dFcltUsedTyCd"></td>
									<th>코드명</th>
									<td id="dFcltUsedTyNm"></td>
								</tr>
								<tr>
									<th>시설물종류</th>
									<td id="dFcltKndNm"></td>
									<th>사용유형</th>
									<td id="dUseTyNm"></td>
								</tr>
								<tr>
									<th>등록자아이디</th>
									<td id="dRgsUserId"></td>
									<th>등록일시</th>
									<td id="dRgsDate"></td>
								</tr>
								<tr>
									<th>수정자아이디</th>
									<td id="dUpdUserId"></td>
									<th>수정일시</th>
									<td id="dUpdDate"></td>
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
			<div class="layer layerRegister" id="div_drag_2">
				<div class="tit"><h4>시설물용도별유형코드 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>시설물용도별유형코드 등록</caption>
							<tbody>
								<tr>
									<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>용도별유형코드</th>
									<td><input type="text" id="iFcltUsedTyCd" class="txtType" maxlength="40" required="required" user-required="insert" user-title="용도별유형코드"/></td>
									<th>코드명</th>
									<td><input type="text" id="iFcltUsedTyNm" class="txtType" maxlength="100" /></td>
								</tr>
								<tr>
									<th>시설물종류</th>
									<td>
										<select name="" id="iFcltKndCd" class="selectType1" maxlength="1">
											<c:forEach items="${fcltKndList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>사용유형</th>
									<td>
										<select name="" id="iUseTyCd" class="selectType1" maxlength="1">
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}"></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
							</tbody>
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