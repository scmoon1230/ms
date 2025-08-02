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

	var grid_param = {
			url : "<c:url value='/'/>wrks/sstm/menu/info/list.json",
			datatype: "json",
			postData: {
				pgmNm : $("#pgmNm").val(),
				pgmUrl : $("#pgmUrl").val(),
				useTyCd : $("#useTyCd").val(),
				pgmDscrt : $("#pgmDscrt").val()
			},
			colNames: [
					'프로그램아이디', '메뉴 목록', '메뉴제목', '부모프로그램아이디', '프로그램기능유형코드', '프로그램URL', '프로그램설명', '사용유형코드'
					, '등록자', '등록일', '수정자', '수정일', 'LVL', 'ISLF'
			],
			colModel: [	{ name:'pgmId', index:'PGM_ID', width:200, align:'center', hidden:true, key:true}
					, { name:'pgmNm', index:'PGM_NM', width:470, align:'left', hidden:true, sortable: false}
					, { name:'pgmTit', index:'PGM_TIT', width:470, align:'left', sortable: false}
					, { name:'prntPgmId', index:'PRNT_PGM_ID', width:125, align:'center', hidden:true}
					, { name:'pgmFnctTyCd', index:'PGM_FNCT_TY_CD', width:300, align:'center', hidden:true}
					, { name:'pgmUrl', index:'PGM_URL', width:100, hidden:true}
					, { name:'pgmDscrt', index:'PGM_DSCRT', width:200, align:'center', hidden:true}
					, { name:'useTyCd', index:'USE_TY_CD', width:300, align:'center', hidden:true}
					, { name:'rgsUserId', index:'RGS_USER_ID', width:125, align:'center', hidden:true}
					, { name:'rgsDate', index:'RGS_DATE', width:125, align:'center', hidden:true}
					, { name:'updUserId', index:'UPD_USER_ID', width:200, align:'center', hidden:true}
					, { name:'updDate', index:'UPD_DATE', width:100, align:'center', hidden:true}
					, { name:'lvl', index:'LVL', width:300, hidden:true}
					, { name:'islf', index:'ISLF', width:300, hidden:true}
			],
			treeGrid: true,
			treeGridModel: "adjacency",
			caption: "",
			//ExpandColumn: "PGM_TIT",
			ExpandColumn: "pgmTit",
			tree_root_level:0,
			rowNum: 10000,
	        autowidth: true,
			height: $("#grid").parent().height()-40,
			ExpandColClick: true,
			treeIcons: {leaf:'ui-icon-document-b'},
			treeReader:{
				leaf_field :'islf', //확장 화살표 여부(true:확장,false:최하단 레벨 이므로 확장 안함)
				level_field: 'lvl',		//---level값
				parent_id_field : 'prntPgmId',   //---부모id값
				expanded_field: 'expd' //열린상태로 로딩할지 여부
			},
			onSelectRow: function(rowid, status, e) {
				var list = jQuery("#grid").getRowData(rowid);

				$("#dPgmId").val(list.pgmId);
				$.selectBarun("#dPgmFnctTyCd", list.pgmFnctTyCd);
				$("#dPrntPgmId").val(list.prntPgmId);
				$("#dPgmUrl").val(list.pgmUrl);
				$("#dPgmNm").val(list.pgmNm);
				$("#dPgmDscrt").val(list.pgmDscrt);
				$.selectBarun("#dUseTyCd", list.useTyCd);
				$("#dUpdUserId").html(list.updUserId);
				$("#dUpdDate").html(list.updDate);
				$("#dRgsUserId").html(list.updUserId);
				$("#dRgsDate").html(list.updDate);
				$("#pgmIdBk").val(list.pgmId);
				$("#iPrntPgmId").val(list.pgmId);
			},
			beforeRequest: function() {
				$.loading(true);
				rowNum = $('#rowPerPageList').val();
			},
			beforeProcessing: function(data, status, xhr){
				if(typeof data.rows != "undefine" || data.row != null) {
					$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
				}
			},
			loadComplete : function() { /* 데이터 로딩이 끝난후 호출할 함수*/
				//$.bindTreeExpand("#grid", grid_param);
				$.loading(false);
			}
		};

	$.jqGrid($('#grid'), grid_param);

	$(".btnS").bind("click",function(){
		$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
		var myPostData = $("#grid").jqGrid('getGridParam', 'postData');

		//검색할 조건의 값을 설정한다.
		myPostData.pgmNm = $("#pgmNm").val();
		myPostData.pgmUrl = $("#pgmUrl").val();
		myPostData.useTyCd = $("#useTyCd").val();
		myPostData.pgmDscrt = $("#pgmDscrt").val();

		$("#grid").trigger("reloadGrid");
	});

	//수정저장버튼
	$(".btnModi").bind("click",function(){

		if($("#dPgmId").val() == "") return false;

		var url = "<c:url value='/'/>wrks/sstm/menu/info/update.json";
		var params = "pgmId=" + encodeURIComponent($("#dPgmId").val());
			params += "&pgmFnctTyCd=" + encodeURIComponent($("#dPgmFnctTyCd").val());
			params += "&pgmUrl=" + encodeURIComponent($("#dPgmUrl").val());
			params += "&pgmNm=" + encodeURIComponent($("#dPgmNm").val());
			params += "&useTyCd=" + encodeURIComponent($("#dUseTyCd").val());
			params += "&pgmDscrt=" + encodeURIComponent($("#dPgmDscrt").val());
			params += "&prntPgmId=" + encodeURIComponent($("#dPrntPgmId").val());
			params += "&pgmIdBk=" + encodeURIComponent($("#pgmIdBk").val());

		$.ajaxEx($('#grid'), {
			url : url,
			datatype: "json",
			data: params,
			success:function(data){

				$("#grid").setGridParam({page :$("#cur-page").val()});
				$("#grid").trigger("reloadGrid");
				alert(data.msg);

			},
			error:function(e){
				alert(e.responseText);
			}
		});


	});
});

function resetAction() {

	$("#iPgmId").val("");
	$("#iPrntPgmId").val($("#dPgmId").val());
	$("#iPgmFnctTyCd").get(0).selectedIndex=0;
	$("#iPgmUrl").val("");
	$("#iPgmNm").val("");
	$("#iUseTyCd").get(0).selectedIndex=0;
	$("#iPgmDscrt").val("");
	$("#pgmIdBk").val("");
}

function updateAction(obj) {
	//alert('updateAction');

	var url = "<c:url value='/'/>wrks/sstm/menu/info/update.json";
	var params = "pgmId=" + encodeURIComponent($("#iPgmId").val());
		params += "&pgmFnctTyCd=" + encodeURIComponent($("#iPgmFnctTyCd").val());
		params += "&pgmUrl=" + encodeURIComponent($("#iPgmUrl").val());
		params += "&pgmNm=" + encodeURIComponent($("#iPgmNm").val());
		params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
		params += "&pgmDscrt=" + encodeURIComponent($("#iPgmDscrt").val());
		params += "&pgmIdBk=" + encodeURIComponent($("#pgmIdBk").val());

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
	if($("#iPgmId").val().trim() == "") {
		alert("프로그램아이디를 입력하세요.");
		return false;
	}
	return true;
}

function insertAction(obj) {
	//alert('insertAction');
	var url = "<c:url value='/'/>wrks/sstm/menu/info/insert.json";
	var params = "pgmId=" + encodeURIComponent($("#iPgmId").val());
			params += "&pgmFnctTyCd=" + encodeURIComponent($("#iPgmFnctTyCd").val());
			params += "&pgmUrl=" + encodeURIComponent($("#iPgmUrl").val());
			params += "&pgmNm=" + encodeURIComponent($("#iPgmNm").val());
			params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
			params += "&pgmDscrt=" + encodeURIComponent($("#iPgmDscrt").val());


			if($('input:radio[name="parentsChild"]:checked').val() == "p")
				params += "&prntPgmId=";
			else
				params += "&prntPgmId=" + encodeURIComponent($("#iPrntPgmId").val());


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
			alert(data.msg);
			//alert(e.responseText);
		}
	});
}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = "<c:url value='/'/>wrks/sstm/menu/info/delete.json";
	//var params = "cdGrpId=" + $("#dCdGrpId").text();
	var params = "pgmIdBk=" + $("#pgmIdBk").val();

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
	var url = "<c:url value='/'/>wrks/sstm/menu/info/deleteMulti.json";
	var params = "";
	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);

		params += "&pgmIdBk=" + list.PGM_ID;

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
				<div class="tbLeft50">
				<!-- <div class="tableTypeFree seachT"> -->
				<div class="tableTypeHalf seachT">
					<table>
						<caption>프로그램정보</caption>
						<tbody>
						<tr><th colspan="4" style="text-align:center">프로그램정보</th>
						</tr>
						<tr><th>프로그램명</th>
							<td><input type="text" name="" id="pgmNm" class="txtType txtType100 searchEvt" style="ime-mode:active"></td>
							<th>사용유형</th>
							<td><select name="" id="useTyCd" class="selectType1" maxlength="1">
									<c:forEach items="${useGrpList}" var="val">
										<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr><th>프로그램설명</th>
							<td colspan="3"><input type="text" name="" id="pgmDscrt" class="txtType txtType100 searchEvt" style="ime-mode:inactive"></td>
						</tr>
						<tr><th>URL</th>
							<td colspan="3">
								<input type="text" name="" id="pgmUrl" class="txtType txtType70 searchEvt" style="ime-mode:inactive">
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>

				<div class="tableType1" style="height:550px;">
					<table id="grid" style="width:100%">
					</table>
				</div>
				</div>

				<div class="tbRight50">
				<!-- 레이어팝업 상세 -->
				<div>
					<div class="layerCt">
						<div class="tableTypeFree">
							<input type="hidden" id="pgmIdBk" />
							<table>
								<caption>프로그램정보 상세</caption>
								<tbody>
								<tr><th colspan="2" style="text-align:center">상세내용</th>
								</tr>
								<tr><th>프로그램아이디</th>
									<td><input type="text" class="txtType100" id="dPgmId" maxlength="40" readonly="readonly" required="required" user-required="insert" style="ime-mode:inactive"/>
										<span class="commment text-primary">* 프로그램 아이디 기준으로 tree 표출, 아이디를 위치에 맞게 부여</span>
									</td>
								</tr>
								<tr><th>프로그램명</th>
									<td><input type="text" class="txtType100" id="dPgmNm" maxlength="100" style="ime-mode:active"/></td>
								</tr>
								<tr><th>프로그램URL</th>
									<td><input type="text" class="txtType100" id="dPgmUrl" maxlength="255" style="ime-mode:inactive"/></td>
								</tr>
								<tr><th>프로그램설명</th>
									<!-- <td><textarea class="textArea" id="dPgmDscrt" maxlength="2000" style="ime-mode:active"></textarea></td> -->
									<td><input type="text" class="txtType100" id="dPgmDscrt" maxlength="2000" style="ime-mode:active"/></td>
								</tr>
								<tr><th>사용유형</th>
									<td>
										<select name="" id="dUseTyCd" class="selectType1" maxlength="1">
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>기능유형</th>
									<td><select name="" id="dPgmFnctTyCd" class="selectType1" maxlength="40">
											<c:forEach items="${pgmFnctList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>상위프로그램아이디</th>
									<td><input type="text" class="txtType100" id="dPrntPgmId" maxlength="255" style="ime-mode:inactive"  /></td>
								</tr>
							<%--<tr><th>수정일</th>									<td id="dUpdDate"></td>
									<th>수정자</th>									<td id="dUpdUserId"></td>
								</tr>
								<tr><th>등록일</th>									<td id="dRgsDate"></td>
									<th>등록자</th>									<td id="dRgsUserId"></td>
								</tr>--%>
								</tbody>
							</table>
						</div>
						<br/>
					<div class="btnCtr">
						<a href="#" class="btn btnDt btnRgt">신규</a>
						<a href="#" class="btn btnModi">저장</a>
						<a href="#" class="btn btnDe">삭제</a>
					</div>

				</div>

				<!-- //레이어팝업 상세 -->
				</div>
			</div>
			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_1">
				<div class="tit"><h4>프로그램정보 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>프로그램정보 등록</caption>
							<tbody>
							<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>프로그램아이디</th>
								<td><input type="text" class="txtType100" id="iPgmId" maxlength="40" required="required" user-required="insert" style="ime-mode:inactive"/></td>
							</tr>
							<tr><th>프로그램명</th>
								<td><input type="text" class="txtType100" id="iPgmNm" maxlength="100" style="ime-mode:active"/></td>
							</tr>
							<tr><th>프로그램URL</th>
								<td><input type="text" class="txtType100" id="iPgmUrl" maxlength="255" style="ime-mode:inactive"/></td>
							</tr>
							<tr><th>프로그램설명</th>
								<!-- <td><textarea class="textArea" id="iPgmDscrt" maxlength="2000" style="ime-mode:active"></textarea></td> -->
								<td><input type="text" class="txtType100" id="iPgmDscrt" maxlength="2000" style="ime-mode:active"/></td>
							</tr>
							<tr><th>사용유형</th>
								<td><select name="" id="iUseTyCd" class="selectType1" maxlength="1">
										<c:forEach items="${useGrpList}" var="val">
											<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr><th>기능유형</th>
								<td><select name="" id="iPgmFnctTyCd" class="selectType1" maxlength="40">
										<c:forEach items="${pgmFnctList}" var="val">
											<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr><th>상위프로그램아이디</th>
								<td><input type="text" class="txtType100" id="iPrntPgmId" maxlength="255" style="ime-mode:inactive" readonly="readonly" /></td>
							</tr>
							<tr><th>추가구분</th>
								<td>최상위그룹 : <input type="radio" id="p" name="parentsChild" value="p"/> &nbsp;&nbsp;&nbsp;
									하위그룹 : <input type="radio" id="c" name="parentsChild" checked="true"  value="c"/>
								</td>
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
	<!-- //contentWrap -->
	</div>
	<!-- //container -->
</div>
<!-- //container -->
<!-- footer -->
<!-- <div id="footwrap">
	<div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->
</body>
</html>
