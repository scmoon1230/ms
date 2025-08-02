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
		url: contextRoot + "/wrks/sstm/code/sggcd/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			worshipName : $("#worshipName").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '예배코드' , '예배명칭' , '요일'
				   ],
		colModel: [{ name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'worshipCode', index : 'WORSHIP_CODE', width:120, align:'center'
					}, { name: 'worshipName', index : 'WORSHIP_NAME', width:200, align:'center'
					}, { name: 'worshipDay' , index : 'WORSHIP_DAY' , width:200, align:'center'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'WORSHIP_CODE',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "SIGUNGU_CD",
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

			$("#dWorshipCode").html(list.worshipCode);
			$("#dWorshipName").html(list.worshipName);
			$("#dWorshipDay").html(list.worshipDay);

			$("#iWorshipCode").val(list.worshipCode);
			$("#iWorshipName").val(list.worshipName);
			$("#iWorshipDay").val(list.worshipDay);

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
		myPostData.worshipName = $("#worshipName").val();

		$("#grid").trigger("reloadGrid");
	});

   	$(".tableType1").css('height', window.innerHeight - 350);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
	
});

function resetAction() {
	$("#iWorshipCode").val("");
	$("#iWorshipName").val("");
	$("#iWorshipDay").val("");
}

function updateAction(obj) {
	var url = contextRoot + "/wrks/sstm/code/sggcd/update.json";
	var params = "worshipCode=" + encodeURIComponent($("#iWorshipCode").val());
   		params += "&worshipName=" + encodeURIComponent($("#iWorshipName").val());
		params += "&worshipDay=" + encodeURIComponent($("#iWorshipDay").val());

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
	if($("#iWorshipCode").val().trim() == "") {
		alert("예배코드를 입력하세요.");
		return false;
	}

	return true;
}

function insertAction(obj) {
	var url = contextRoot + "/wrks/sstm/code/sggcd/insert.json";
	var params = "worshipCode=" + encodeURIComponent($("#iWorshipCode").val());
		params += "&worshipName=" + encodeURIComponent($("#iWorshipName").val());
		params += "&worshipDay=" + encodeURIComponent($("#iWorshipDay").val());

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
	var url = contextRoot + "/wrks/sstm/code/sggcd/delete.json";
	var params = "worshipCode=" + $("#iWorshipCode").val();

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
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택한 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/wrks/sstm/code/sggcd/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&worshipCode=" + list.worshipCode;
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
						<caption>예배분류</caption>
						<colgroup>
							<col style="width: 150px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
							<th>예배명칭</th>
							<td><input type="text" name="" id="worshipName" class="txtType searchEvt" style="ime-mode:active">
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>

					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

				<div class="tableType1" style="height: 500px;">
					<table id="grid" style="width:100%">
					</table>
				</div>
				<div class="paginate">
				</div>
				<div class="btnWrap btnR">
					<a href="#" class="btn btnDt btnRgt">등록</a>
					<%--<a href="#" class="btn btnMultiDe">삭제</a>--%>
				</div>
			</div>

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>예배분류 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>예배분류 상세</caption>
							<tbody>
								<tr><th>예배코드</th>		<td id="dWorshipCode"></td>
								</tr>
								<tr><th>예배명칭</th>		<td id="dWorshipName"></td>
								</tr>
								<tr><th>요일</th>			<td id="dWorshipDay"></td>
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
				<div class="tit"><h4>예배분류 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<input type="hidden" id="sigunguCdBk" />
						<table>
							<caption>예배분류 등록</caption>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>예배코드</th>
									<td><input type="text" id="iWorshipCode" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
								</tr>
								<tr><th>예배명칭</th>
									<td><input type="text" id="iWorshipName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>요일</th>
									<td><input type="text" id="iWorshipDay" class="txtType" maxlength="200"/></td>
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
</body>
</html>