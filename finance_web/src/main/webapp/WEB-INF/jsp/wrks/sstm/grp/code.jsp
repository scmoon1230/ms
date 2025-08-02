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

	$.jqGrid($('#grid'), {
		url: '<c:url value='/'/>wrks/sstm/grp/code/list.json',
		datatype: "json",
		postData: {
			//dstrtCd : $("#sDstrtCd").val(),
			grpId : $("#sGrpId").val(),
			grpNmKo : $("#sGrpNm").val(),
			useTyCd : $("#sUseTyCd").val()
		},
		colNames: [	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
						'No',		'그룹아이디',		'그룹명',			'그룹명영문',		'그룹설명',		'그룹구분',		'그룹구분',
						'지구코드',	'지구',		'시스템아이디',		'시스템',		'사용유형코드',		'사용유형'
			],
		colModel: [	{ name:'CHECK', width:70, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{ name:'rk'      , index:'RK'       , width:50 , align:'center', 'hidden':true, sortable: false},
					{ name:'grpId'   , index:'GRP_ID'   , width:150, align:'center'},
					{ name:'grpNmKo' , index:'GRP_NM_KO', width:200, align:'center'},
					{ name:'grpNmEn' , index:'GRP_NM_EN', 'hidden':true},
					{ name:'grpDscrt', index:'GRP_DSCRT', width:250, align:'center'},
					{ name:'grpTy'   , index:'GRP_TY'   , 'hidden':true},
					{ name:'grpTyNm' , index:'GRP_TY_NM', 'hidden':true},
					{ name:'dstrtCd' , index:'DSTRT_CD' , 'hidden':true},
					{ name:'dstrtNm' , index:'DSTRT_NM' , 'hidden':true},
					{ name:'sysId'   , index:'SYS_ID'   , 'hidden':true},
					{ name:'lkSysNm' , index:'LK_SYS_NM', 'hidden':true},
					{ name:'useTyCd' , index:'USE_TY_CD', 'hidden':true},
					{ name:'useTyNm' , index:'USE_TY_NM', width:80 , align:'center'}
			],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
        autowidth: true,
        height: $('#grid').parent().height() - 40,
		sortname:'GRP_NM_KO',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			//id: "GRP_ID",
			root: function(obj) { return obj.rows; },
			page: function(obj) { return 1; },
			total: function(obj) {
				if(obj.rows.length > 0) {
					var page	= obj.rows[0].rowcnt / rowNum;
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

			$("#dGrpId").html(list.grpId);
			//$("#dGrpTyNm").html(list.grpTyNm);
			//$("#dDstrtNm").html(list.dstrtNm);
			$("#dGrpNmKo").html(list.grpNmKo);
			$("#dGrpNmEn").html(list.grpNmEn);
			$("#dGrpDscrt").html(list.grpDscrt);
			$("#dUseTyNm").html(list.useTyNm);
			//$("#dLkSysNm").html(list.lkSysNm);

			$("#iGrpId").val(list.grpId);
			//$.selectBarun("#iGrpTy", list.grpTy);
			//$.selectBarun("#iDstrtCd", list.dstrtCd);
			$("#iGrpNmKo").val(list.grpNmKo);
			$("#iGrpNmEn").val(list.grpNmEn);
			$("#iGrpDscrt").val(list.grpDscrt);
			$.selectBarun("#iUseTyCd", list.useTyCd);
			$("#grpIdBak").val(list.grpId);

			//$.selectBarun("#iLkSysId", list.sysId);

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
		//myPostData.dstrtCd = $("#sDstrtCd").val();
		myPostData.grpId = $("#sGrpId").val();
		myPostData.grpNmKo = $("#sGrpNm").val();
		myPostData.useTyCd = $("#sUseTyCd").val();

		$("#grid").trigger("reloadGrid");
	});
		
	$(".tableType1").css('height', window.innerHeight - 350);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
		
});

function resetAction() {
	$("#iGrpId").removeAttr("readonly");
	$("#iGrpId").val("");
	//$.selectBarun("#iGrpTy", "");
	//$.selectBarun("#iDstrtCd", "");
	$("#iGrpNmKo").val("");
	$("#iGrpNmEn").val("");
	$("#iGrpDscrt").val("");
	$.selectBarun("#iUseTyCd", "");
	//$.selectBarun("#iLkSysId", "");
}

function preModifyAction() {
	$("#iGrpId").attr("readonly", "readonly");
}

function updateAction(obj) {
	var url = "<c:url value='/'/>wrks/sstm/grp/code/update.json";
	var params = "";
		params += "&grpId=" + encodeURIComponent($("#iGrpId").val());
		//params += "&grpTy=" + encodeURIComponent($("#iGrpTy").val());
		params += "&grpTy=9";		// 일반으로 고정
		//params += "&dstrtCd=" + encodeURIComponent($("#iDstrtCd").val());
		params += "&grpNmKo=" + encodeURIComponent($("#iGrpNmKo").val());
		params += "&grpNmEn=" + encodeURIComponent($("#iGrpNmEn").val());
		params += "&grpDscrt=" + encodeURIComponent($("#iGrpDscrt").val());
		params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
		params += "&grpIdBak=" + encodeURIComponent($("#grpIdBak").val());
		//params += "&sysId=" + encodeURIComponent($("#iLkSysId").val());
		params += "&sysId=PVE";		// 영상반출
		
	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			//alert("업데이트하였습니다.");
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

function insertAction(obj) {
	var url = "<c:url value='/'/>wrks/sstm/grp/code/insert.json";
	var params = "";
		params += "&grpId=" + encodeURIComponent($("#iGrpId").val());
		//params += "&grpTy=" + encodeURIComponent($("#iGrpTy").val());
		params += "&grpTy=9";		// 일반
		//params += "&dstrtCd=" + encodeURIComponent($("#iDstrtCd").val());
		params += "&grpNmKo=" + encodeURIComponent($("#iGrpNmKo").val());
		params += "&grpNmEn=" + encodeURIComponent($("#iGrpNmEn").val());
		params += "&grpDscrt=" + encodeURIComponent($("#iGrpDscrt").val());
		params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
		//params += "&sysId=" + encodeURIComponent($("#iLkSysId").val());
		params += "&sysId=PVE";		// 영상반출

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

function deleteAction(obj) {
	var url = "<c:url value='/'/>wrks/sstm/grp/code/delete.json";

	var params = "";
	params += "&grpIdBak=" + $("#grpIdBak").val();

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
	//var s =	$("#grid").jqGrid('getGridParam', 'selarrrow');
	var s =	$.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/grp/code/deleteMulti.json";
	var params = "";

	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&grpId=" + list.grpId;
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
				<div class="tableTypeHalf seachT">
					<table>
						<caption>그룹관리</caption>
						<tbody>
						<tr><th>그룹아이디</th>
							<td><input type="text" name="" id="sGrpId" class="txtType searchEvt" style="ime-mode:inactive"></td>
							<th>그룹명</th>
							<td><input type="text" name="" id="sGrpNm" class="txtType searchEvt" style="ime-mode:active"></td>
						<%--<th>지구</th>
							<td><select name="" id="sDstrtCd" class="selectType1">
									<option value="">전체</option>
								    <c:forEach items="${dstrtList}" var="val">
								        <option value="${val.dstrtCd}"><c:out value="${val.dstrtNm}" ></c:out></option>
								    </c:forEach>
								</select>
	                        </td>--%>
							<th>사용유형</th>
							<td><select name="" id="sUseTyCd" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<%--<option value="">전체</option>--%>
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
			<!-- 레이어팝업 등록 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>그룹정보 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<input type="hidden" id="grpIdBak" />
						<table>
							<caption>그룹정보 상세</caption>
							<tbody>
							<tr><th>그룹아이디</th>			<td id="dGrpId"></td>
								<%--<th>지구명</th>				<td id="dDstrtNm"></td>--%>
							</tr>
							<tr><th>그룹명(한글)</th>			<td id="dGrpNmKo"></td>
								<th>그룹명(영문)</th>			<td id="dGrpNmEn"></td>
							</tr>
							<tr><th>그룹설명</th>				<td id="dGrpDscrt" colspan="3" ></td>
							</tr>
							<tr><th>사용유형</th>				<td id="dUseTyNm"></td>
							</tr>
						<%--<tr><th>시스템구분</th>			<td id="dLkSysNm"></td>
							</tr>
							<tr><th>그룹구분</th>				<td id="dGrpTyNm"></td>
							</tr>--%>
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
			<!-- //레이어팝업 등록 -->
			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
				<div class="tit"><h4>그룹정보 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>그룹정보 등록</caption>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>그룹아이디</th>
									<td><input type="text" name="" id="iGrpId" class="txtType carNum" maxlength="40" required="required" user-title="그룹아이디" user-required="insert" style="ime-mode:inactive"></td>
								<%--<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>소속지구명</th>
									<td><select name="" id="iDstrtCd" class="selectType1">
											<c:forEach items="${dstrtList}" var="val">
												<option value="${val.dstrtCd}"><c:out value="${val.dstrtNm}" ></c:out></option>
											</c:forEach>
										</select>
									</td>--%>
								</tr>
								<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>그룹명(한글)</th>
									<td><input type="text" name="" id="iGrpNmKo" class="txtType carNum" maxlength="100" required="required" user-title="그룹명(한글)" style="ime-mode:active"></td>
									<th>그룹명(영문)</th>
									<td><input type="text" name="" id="iGrpNmEn" class="txtType carNum" maxlength="100" style="ime-mode:inactive"></td>
								</tr>
								<tr><th>그룹설명</th>
									<td colspan="3"><input type="text" name="" id="iGrpDscrt" class="txtType70" maxlength="1000" style="ime-mode:active"></td>
								</tr>
								<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용유형</th>
									<td><select name="" id="iUseTyCd" class="selectType1">
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
							<%--<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>시스템구분</th>
									<td><select name="" id="iLkSysId" class="selectType1">
											<c:forEach items="${lkSysIdList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>그룹구분</th>
									<td colspan="3"><select name="" id="iGrpTy" class="selectType1">
											<option value="9">일반</option>
											<option value="1">관리</option>
											<option value="0">시스템</option>
										</select>
									</td>
								</tr>--%>
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
<script src="<c:url value='/js/wrks/sstm/grp/code.js' />"></script>
</body>
</html>
