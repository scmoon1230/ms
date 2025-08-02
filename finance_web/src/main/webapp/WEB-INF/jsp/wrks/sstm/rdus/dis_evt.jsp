<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {

		$.jqGrid('#grid_event', {
			url: '<c:url value='/'/>wrks/sstm/rdus/dis_evt/list_event.json',
			datatype: "json",
			postData: {
				sysCd : $("#sysCd").val()
			},
			colNames: [
					'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_event\', this, event);">', '이벤트명', '세부항목명', '등록일자', '이벤트아이디',
					'이벤트세부분류코드', '시스템코드', '그룹아이디', '지구코드'
			],
			colModel: [
					{
						name: 'CHECK',
						width : 50,
						align : 'center',
						editable : true,
						edittype : 'checkbox',
						editoptions : {
							value : "True:False"
						},
						sortable : false,
						formatter : $.GridCheckBox
					}, {
						name: 'EVT_NM',
						width : 300,
						align : 'center'
					}, {
						name: 'EVT_ID_SUB_NM',
						width : 300,
						align : 'center'
					}, {
						name: 'RGS_DATE',
						width : 300,
						align : 'center'
					}, {
						name: 'EVT_ID',
						width : 200,
						align : 'center',
						'hidden' : true
					}, {
						name: 'EVT_ID_SUB_CD',
						width : 200,
						align : 'center',
						'hidden' : true
					}, {
						name: 'SYS_CD',
						width : 200,
						align : 'center',
						'hidden' : true
					}, {
						name: 'GRP_ID',
						width : 100,
						align : 'left',
						'hidden' : true
					}, {
						name: 'DSTRT_CD',
						width : 100,
						align : 'left',
						'hidden' : true
					}
			],
			pager: '#pager',
			rowNum : 1000,
			sortname: 'EVT_NM',
			sortorder : 'ASC',
			viewrecords : true,
			multiselect : false,
			loadonce : false,
			jsonReader : {},
			onCellSelect : function(rowid, iCol, cellcontent, e) {
				if (iCol == 0) return false;
			},
			beforeProcessing : function(data, status, xhr) {
				$("#grid_event").clearGridData();
			},
			loadComplete : function() {
				$("#grid_event input[type=radio]").change(function() {
					$("#grid_event").jqGrid('setSelection', $(this).val(), true);
				});
			}
		});

		$.jqGrid('#grid_event_popup', {
			datatype: "json",
			postData: {
				sEvtId : $("#sEvtId option:selected").val()
			},
			colNames: [
					'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_event_popup\', this, event);">', '이벤트명', '세부분류명', '이벤트아이디',
					'세부분류코드', '시스템코드'
			],
			colModel: [
					{
						name: 'CHECK',
						width : 100,
						align : 'center',
						editable : true,
						edittype : 'checkbox',
						editoptions : {
							value : "True:False"
						},
						sortable : false,
						formatter : $.GridCheckBox
					}, {
						name: 'EVT_NM',
						width : 280,
						align : 'center'
					}, {
						name: 'EVT_ID_SUB_NM',
						width : 280,
						align : 'center'
					}, {
						name: 'EVT_ID',
						width : 100,
						align : 'center',
						'hidden' : true
					}, {
						name: 'EVT_ID_SUB_CD',
						width : 100,
						align : 'center',
						'hidden' : true
					}, {
						name: 'SYS_CD',
						width : 100,
						align : 'center',
						'hidden' : true
					}
			],
			pager: '#pager',
			rowNum : 1000,
			sortname: 'EVT_NM',
			sortorder : 'ASC',
			viewrecords : true,
			multiselect : false,
			loadonce : false,
			jsonReader : {},
			onCellSelect : function(rowid, iCol, cellcontent, e) {
				if (iCol == 0) return false;
			}

		});

		//이벤트 콤보 change시 grid_event_popup 재조회
		//$("#sEvtId").bind("change", function(){
		//	var myPostData = $("#grid_event_popup").jqGrid('getGridParam', 'postData');
		//alert("sEvtId="+$("#sEvtId").val());
		//	myPostData.sEvtId = $("#sEvtId").val();
		//	$("#grid_event_popup").trigger("reloadGrid");
		//});

	});

	function resetAction(area, callBack) {
		$.resetInputObject(".layerRegister .tableType1." + area);

		if (area == "event") {
			var myPostData = $("#grid_" + area + "_popup").jqGrid('getGridParam', 'postData');
		}
		$("#grid_" + area + "_popup").jqGrid('setGridParam', {
			url : "<c:url value='/'/>wrks/sstm/rdus/dis_evt/list_" + area + "_popup.json"
		});

		$("#grid_" + area + "_popup").jqGrid('setGridParam', {
			beforeProcessing : callBack
		});
		$("#grid_" + area + "_popup").trigger("reloadGrid");
	}

	function validate(area) {
		return $.validate(".layerRegister .tableType2." + area);
	}

	function insertAction(area, src) {
		var s = $.getSelRow("#grid_" + area + "_popup");
		var url = "<c:url value='/'/>wrks/sstm/rdus/dis_evt/insert_" + area + ".json";
		var params = "";

		if (s.length == 0) {
			alert("데이터를 선택해 주십시오");
			return false;
		}

		for (var i = 0; i < s.length; i++) {
			var list = jQuery("#grid_" + area + "_popup").getRowData(s[i]);
			if (area == "event") {
				params += "&evtId=" + encodeURIComponent(list.EVT_ID);
				params += "&evtIdSubCd=" + encodeURIComponent(list.EVT_ID_SUB_CD);
				//params += "&sysCd=" + encodeURIComponent(list.SYS_CD);
				params += "&sysCd=" + encodeURIComponent($("#sysCd").val()); //sysCd는 DUC로 고정됨.
			}
		}

		$.ajaxEx($("#grid_" + area), {
			url : url,
			datatype: "json",
			data : params,
			success : function(data) {

				$("#grid_" + area).trigger("reloadGrid");
				//alert("삭제하였습니다.");
				alert(data.msg);
			},
			error : function(e) {
				//alert(e.responseText);
				alert(data.msg);
			}
		});
	}

	function deleteMultiAction(area) {
		var s = $.getSelRow("#grid_" + area);
		if (s.length == 0) {
			alert("삭제할 데이터를 선택하여 주십시오.");
			return false;
		}

		if (confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
		var url = "<c:url value='/'/>wrks/sstm/rdus/dis_evt/delete_" + area + ".json";
		var params = "";

		for (var i = 0; i < s.length; i++) {
			var list = jQuery("#grid_" + area).getRowData(s[i]);

			if (area == "event") {
				params += "&evtId=" + encodeURIComponent(list.EVT_ID);
				params += "&evtIdSubCd=" + encodeURIComponent(list.EVT_ID_SUB_CD);
				params += "&sysCd=" + encodeURIComponent(list.SYS_CD);
			}
		}

		$.ajaxEx($("#grid_" + area), {
			url : url,
			datatype: "json",
			data : params,
			success : function(data) {

				$("#grid_" + area).trigger("reloadGrid");
				alert("자료를 삭제하였습니다.");
			},
			error : function(e) {
				//alert(e.responseText);
				alert(data.msg);
			}
		});

		return true;
	}

	//이벤트 selectBox change 검색
	function search1() {
		gridReload("event", getGridParams());
	}

	function getGridParams() {
		var params = {
			sEvtId : $("#sEvtId").val()
		};
		return params;
	}

	/* 그리드 갱신 */
	function gridReload(area, params) {
		$("#grid_" + area + "_popup").setGridParam({
			postData : params
		}).trigger("reloadGrid");
	}
</script>
</head>
<body>
<div id="wrapper">
	<!-- topbar -->
	<%@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
	<!-- //topbar -->
	<!-- container -->
	<div class="container">
		<!-- leftMenu -->
		<%@include file="/WEB-INF/jsp/cmm/leftMenu.jsp"%>
		<!-- //leftMenu -->

		<!-- content -->
		<div class="contentWrap">
			<div class="topArea">
				<a href="#" class="btnOpen">
					<img src="<c:url value='/'/>images/btn_on_off.png" alt="열기/닫기">
				</a>
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi.jsp"%>
			</div>
			<div class="content">
				<div class="titArea">
					<h3 class="tit">재난 세부분류별 표출이벤트</h3>
				</div>
				<div class="boxWrap">
					<div class="tbLeft150">
						<div class="searchBox50">
							<dl>
								<dt>세부분류별 표출이벤트</dt>
								<input type="hidden" id="sysCd" name="sysCd" value="<c:out value="${sysCd}" />" />
							</dl>
						</div>
						<div class="tableType1" style="height: 500px; width: 200%; overflow-y: scroll; overflow-x: hidden">
							<table id="grid_event" style="width: 200%">
							</table>
						</div>
						<div class="btnWrap btnR">
							<a href="#" class="btn btnMultiDe" area="event">선택삭제</a>
							<a href="#" class="btn btnDt btnRgt" area="event">추가</a>
						</div>
					</div>
				</div>
			</div>

			<!-- //레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
				<div class="tit">
					<h4>이벤트세부분류 추가</h4>
				</div>
				<div class="layerCt">
					<div class="tableType2 seachT">
						<table>
							<caption>이벤트세부분류 등록</caption>
							<colgroup>
								<col style="width: 100px;">
								<col style="width: 100px;">
								<col style="width: 100px;">
								<col style="width: 100px;">
								<col style="width: 100px;">
								<col style="width: 100px;">
							</colgroup>
							<tbody>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td><select name="sEvtId" id="sEvtId" class="selectType1" maxlength="1" onchange="javascript:search1();">
											<option value="" style="text-align: center">::: 전체 :::</option>
											<c:forEach items="${ListCmEvt}" var="val">
												<option value="${val.EVT_ID}" ${val.EVT_ID == sEvtId ? 'selected' : ''}><c:out value="${val.EVT_NM}"></c:out></option>
											</c:forEach>
									</select></td>
								</tr>
							</tbody>
						</table>
					</div>
					<!--이벤트세부분류 리스트 -->
					<div class="tableType1 event" user-title="이벤트세부분류 추가" style="height: 500px; overflow-y: scroll; overflow-x: hidden">
						<table id="grid_event_popup" style="width: 100%">
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