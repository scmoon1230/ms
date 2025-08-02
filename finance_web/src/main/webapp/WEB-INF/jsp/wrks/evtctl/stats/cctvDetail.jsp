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

		$.jqGrid($('#grid'),
		{
			  url : "<c:url value='/wrks/evtctl/stats/cctvDetail/list.json'/>"
			, datatype: "json"
			, autowidth: true
			, postData :
			{
				  searchStartDate : $("#searchStartDate").val()
				, searchEndDate : $("#searchEndDate").val()
				, searchSysCd : $("#searchSysCd option:selected").val()
				, searchOcrNo : $("#searchOcrNo").val()
				, searchUserId : $("#searchUserId").val()
			}
			, colNames :
			[
				  '일시'
				, '사용자명'
				, '근거번호'
				, '유형'
				, 'CCTV명'
				, 'CCTV주소'
				, '접속수'
				, '제어수'
			]
			, colModel :
			[
				  { name: 'VIEW_DATE', width:90, cellattr:function(){ return 'style="width: 90px;"'}, align:'center'}
				, { name: 'USER_NM', width:120, cellattr:function(){ return 'style="width: 120px;"'}, align:'center'}
				, { name: 'EVT_OCR_NO', width:140, cellattr:function(){ return 'style="width: 140px;"'}, align:'center'}
				, { name: 'EVT_OCR_ITEM_DTL', width:80, cellattr:function(){ return 'style="width: 80px;"'}, align:'center'}
				, { name: 'FCLT_LBL_NM', width:200, cellattr:function(){ return 'style="width: 200px;"'}, align:'left'}
				, { name: 'LOTNO_ADRES_NM', width:200, cellattr:function(){ return 'style="width: 200px;"'}, align:'left'}
				, { name: 'VIEW_CNT', width:60, cellattr:function(){ return 'style="width: 60px;"'}, align:'center'}
				, { name: 'PTZ_CNT', width:60, cellattr:function(){ return 'style="width: 60px;"'}, align:'center'}
			]
			//, pager: '#pager'
			, rowNum: $('#rowPerPageList').val()
			, sortname: 'USER_ID'
			, sortorder: 'ASC'
			, viewrecords: true
			, shrinkToFit: true
			, scrollOffset: 0
			, loadonce:false
			, sortable: false
			, jsonReader:
			{
				  root: function(obj) { return obj.rows; }
				, page: function(obj) { return 1; }
				, total: function(obj)
				{
					if(obj.rows.length > 0)
					{
						var page  = obj.rows[0].ROWCNT / rowNum;
						if( (obj.rows[0].ROWCNT % rowNum) > 0) { page++; }
						return page;
					} else {
						return 1;
					}
				}
				, records: function(obj) { return $.showCount(obj); }
			}
			, onSelectRow: function(rowid, status, e) { }
			, beforeRequest: function()
			{
				$.loading(true);
				rowNum = $('#rowPerPageList').val();
			}
			, beforeProcessing: function(data, status, xhr)
			{
				$.loading(false);
				if(typeof data.rows != "undefine" || data.row != null) {
					$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
				}
			}
			, loadComplete : function (data){
				checkGridNodata('', data);
			}  // End loadComplete
		});  // End jqGrid

		$(".btnS").bind("click",function()
		{
			if($.validate(".tableType2.seachT") == false) return;
			$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
			var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
			myPostData.searchStartDate = $("#searchStartDate").val();
			myPostData.searchEndDate = $("#searchEndDate").val();
			myPostData.searchSysCd = $("#searchSysCd option:selected").val();
			myPostData.searchOcrNo = $("#searchOcrNo").val();
			myPostData.searchUserId = $("#searchUserId").val();
			$("#grid").trigger("reloadGrid");
		});

		$("#searchEndDate").bind("change",function()
		{
			var searchEndDate = $("#searchEndDate").val();
			var currentDay = $("#currentDay").val();
			if (searchEndDate < currentDay) {
				return true;
			} else {
				alert("종료일은 현재일 전일까지만 조회 가능 합니다.");
				$("#searchEndDate").val("${endDate}");
				return false;
			}
		});
	});

	function excelDownAction()
	{
		var curPage = '1';
		if (typeof $('#cur-page').val() != "undefined") {
			curPage = $('#cur-page').val();
		}
		document.excelDownFrm.searchPageList.value = $("#rowPerPageList").val();
		document.excelDownFrm.searchCurrPage.value = curPage;
		document.excelDownFrm.searchStartDate.value = $("#searchStartDate").val();
		document.excelDownFrm.searchEndDate.value = $("#searchEndDate").val();
		document.excelDownFrm.searchSysCd.value = $("#searchSysCd option:selected").val();
		document.excelDownFrm.searchEvtNm.value = $("#searchSysCd option:selected").text();
		document.excelDownFrm.searchOcrNo.value = $("#searchOcrNo").val();
		document.excelDownFrm.searchUserId.value = $("#searchUserId").val();
		document.excelDownFrm.action = "<c:url value='/wrks/evtctl/stats/cctvDetail/list.excel'/>";
		document.excelDownFrm.submit();
	}

</script>

</head>
<body>
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
<div id="wrapper" class="wth100">

    <!-- topbar -->
   <%//@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
    <!-- //topbar -->
    <!-- container -->
    <div class="container">
        <!-- leftMenu -->
      <%//@include file="/WEB-INF/jsp/cmm/leftMenu.jsp"%>
        <!-- //leftMenu -->
		<!-- content -->
		<div class="contentWrap">
			<div class="topArea">
				<a href="#" class="btnOpen"><img src="<c:url value='/'/>images/btn_on_off.png" alt="열기/닫기"></a>
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi.jsp"%>
			</div>
			<div class="content">
				<div class="titArea">
					<!-- <h3 class="tit">CCTV접속상세이력</h3> -->
					<h3 class="tit"><%=menu_nm%></h3>
				</div>
				<div class="tableType2 seachT">
					<table>
					<caption>CCTV접속상세이력</caption>
					<colgroup>
						<col style="width: 150px;" />
						<col style="width: *" />
						<col style="width: 150px;" />
						<col style="width: *" />
					</colgroup>
					<tbody>
					<tr>
						<tH>기간</th>
						<td>
							<input type="text" name="searchStartDate" id="searchStartDate" class="date8Type datepicker1" value="${startDate}" required="required" user-title="시작일자" user-data-type="betweendate" user-ref-id="searchEndDate">
							<span class="bl">~</span>
							<input type="text" name="searchEndDate" id="searchEndDate" class="date8Type datepicker2" value="${endDate}" required="required" user-title="종료일자">
							<input type="hidden" name="currentDay" id="currentDay" value="${currentDay}" user-title="현재일자">
						</td>
						<th>연계서비스</th>
						<td>
							<select name="searchSysCd" id="searchSysCd" class="selectType1" style="width:160px">
								<c:forEach items="${eventList}" var="val">
								<option value="${val.SYS_CD}"><c:out value="${val.EVT_NM}"></c:out></option>
								</c:forEach>
							</select>
							<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
						</td>
					</tr>
					<tr>
						<th>사용자ID(명)</th>
						<td><input type="text" name="searchUserId" id="searchUserId" class="txtType searchEvt" style="ime-mode:active"></td>
						<th>근거번호</th>
						<td><input type="text" name="searchOcrNo" id="searchOcrNo" class="txtType searchEvt" style="ime-mode:active"></td>
					</tr>
					</tbody>
					</table>
				</div>
				<div class="searchArea">
					<div class="page fL">
						<span class="txt">페이지당</span>
						<div class="selectBox">
							<select name="rowPerPageList" id="rowPerPageList" class="selectType1">
								<c:forEach items="${rowPerPageList}" var="val">
								<option value="${val.CD_ID}" ${val.CD_ID == rowPerPageSession ? 'selected' : ''}><c:out value="${val.CD_NM_KO}" ></c:out></option>
								</c:forEach>
							</select>
						</div>
						<span class="totalNum">전체<em id="rowCnt"></em>건</span>
					</div>
				</div>
				<div class="tableType1" style="overflow-y:hidden; overflow-x:scroll">
					<table id="grid" style="width:100%"></table>
				</div>
				<div class="paginate"></div>
				<div class="btnWrap btnR">
					<a href="javascript:;" class="btn btnDt btnExcel">엑셀저장</a>
				</div>
			</div>  <!-- End content -->
		</div>  <!-- End contentWrap -->
	</div> <!-- End container -->
</div>
<form name="excelDownFrm" method="post">
	<input type="hidden" name="searchPageList" id="searchPageList" />
	<input type="hidden" name="searchCurrPage" id="searchCurrPage" />
	<input type="hidden" name="searchStartDate" id="searchStartDate" />
	<input type="hidden" name="searchEndDate" id="searchEndDate" />
	<input type="hidden" name="searchEvtNm" id="searchEvtNm" />
	<input type="hidden" name="searchSysCd" id="searchSysCd" />
	<input type="hidden" name="searchOcrNo" id="searchOcrNo" />
	<input type="hidden" name="searchUserId" id="searchUserId" />
</form>
</body>
</html>