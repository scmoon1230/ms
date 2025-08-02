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
			  url : "<c:url value='/wrks/evtctl/stats/cctvTermType/list.json'/>"
			, datatype: "json"
			, autowidth: true
			, postData :
			{
				  searchStartDate : $("#searchStartDate").val()
				, searchEndDate : $("#searchEndDate").val()
				, searchSysCd : $("#searchSysCd option:selected").val()
			}
			, colNames :
			[
				  '유형구분'
				, '발생이벤트 총수'
				, 'CCTV 접속근거 이벤트 수'
				, 'CCTV 접속건수'
				, 'CCTV 제어 건수'
			]
			, colModel :
			[
				  { name: 'EVT_OCR_ITEM_DTL', width:160, cellattr:function(){ return 'style="width: 160px;"'}, align:'center'}
				, { name: 'TOT_EVT_CNT', width:200, cellattr:function(){ return 'style="width: 200px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'CONN_EVT_CNT', width:200, cellattr:function(){ return 'style="width: 200px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'CCTV_CONN_CNT', width:200, cellattr:function(){ return 'style="width: 200px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'PTZ_CONN_CNT', width:200, cellattr:function(){ return 'style="width: 200px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
			]
			//, pager: '#pager'
			, rowNum: $('#rowPerPageList').val()
			, sortname: 'EVT_OCR_ITEM_DTL'
			, sortorder: 'ASC'
			, viewrecords: true
			, shrinkToFit: true
			, scrollOffset: 0
			, loadonce:false
			, footerrow: true
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
			, loadComplete : function (data)
			{
				if(data != null)
				{
					if($("#" + this.id + " tr:last td:eq(0)").css("display") == "none") {
						$("#" + this.id + " [user-grid-data=" + this.prevCellVal.cellId + "]").attr("rowspan", this.prevCellVal.row);
					}
					$.each (data,function(index,item)
					{
						if(index == "rows") {
							dataSave = item;
						}
					});
				}
				// 합계금액
				var sum01 = $('#grid').jqGrid("getCol", "TOT_EVT_CNT", false, "sum");
				var sum02 = $('#grid').jqGrid("getCol", "CONN_EVT_CNT", false, "sum");
				var sum03 = $('#grid').jqGrid("getCol", "CCTV_CONN_CNT", false, "sum");
				var sum04 = $('#grid').jqGrid("getCol", "PTZ_CONN_CNT", false, "sum");

				// 합계급액 셋팅
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", TOT_EVT_CNT : sum01});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", CONN_EVT_CNT : sum02});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", CCTV_CONN_CNT : sum03});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", PTZ_CONN_CNT : sum04});

				// 합계 금액 css
				$('table.ui-jqgrid-ftable tr:first').children("td").css("background-color", "#D5D5D5");
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
		document.excelDownFrm.searchStartDate.value = $("#searchStartDate").val();
		document.excelDownFrm.searchEndDate.value = $("#searchEndDate").val();
		document.excelDownFrm.searchSysCd.value = $("#searchSysCd option:selected").val();
		document.excelDownFrm.searchEvtNm.value = $("#searchSysCd option:selected").text();
		document.excelDownFrm.action = "<c:url value='/wrks/evtctl/stats/cctvTermType/list.excel'/>";
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
					<!-- <h3 class="tit">CCTV접속현황(기간/유형)</h3> -->
					<h3 class="tit"><%=menu_nm%></h3>
				</div>
				<div class="tableType2 seachT">
					<table>
					<caption>CCTV접속현황(기간/유형)</caption>
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
	<input type="hidden" name="searchStartDate" id="searchStartDate" />
	<input type="hidden" name="searchEndDate" id="searchEndDate" />
	<input type="hidden" name="searchEvtNm" id="searchEvtNm" />
	<input type="hidden" name="searchSysCd" id="searchSysCd" />
</form>
</body>
</html>