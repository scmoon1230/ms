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
			  url : "<c:url value='/wrks/evtctl/stats/cctvMonType/list.json'/>"
			, datatype: "json"
			, autowidth: true
			, postData :
			{
				  searchYear : $("#searchYear option:selected").val()
				, searchSysCd : $("#searchSysCd option:selected").val()
			}
			, colNames :
			[
				  '유형구분'
				, '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'
			]
			, colModel :
			[

				  { name: 'EVT_OCR_ITEM_DTL', width:140, cellattr:function(){ return 'style="width: 140px;"'}, align:'center', sortable: false}
				, { name: 'M_01', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_02', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_03', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_04', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_05', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_06', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_07', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_08', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_09', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_10', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_11', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
				, { name: 'M_12', width:65, cellattr:function(){ return 'style="width: 65px;"'}, align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}
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
				  id: "EVT_OCR_ITEM_DTL"
				, root: function(obj) { return obj.rows; }
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
				var sum01 = $('#grid').jqGrid("getCol", "M_01", false, "sum");
				var sum02 = $('#grid').jqGrid("getCol", "M_02", false, "sum");
				var sum03 = $('#grid').jqGrid("getCol", "M_03", false, "sum");
				var sum04 = $('#grid').jqGrid("getCol", "M_04", false, "sum");
				var sum05 = $('#grid').jqGrid("getCol", "M_05", false, "sum");
				var sum06 = $('#grid').jqGrid("getCol", "M_06", false, "sum");
				var sum07 = $('#grid').jqGrid("getCol", "M_07", false, "sum");
				var sum08 = $('#grid').jqGrid("getCol", "M_08", false, "sum");
				var sum09 = $('#grid').jqGrid("getCol", "M_09", false, "sum");
				var sum10 = $('#grid').jqGrid("getCol", "M_10", false, "sum");
				var sum11 = $('#grid').jqGrid("getCol", "M_11", false, "sum");
				var sum12 = $('#grid').jqGrid("getCol", "M_12", false, "sum");

				// 합계급액 셋팅
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_01 : sum01});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_02 : sum02});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_03 : sum03});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_04 : sum04});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_05 : sum05});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_06 : sum06});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_07 : sum07});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_08 : sum08});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_09 : sum09});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_10 : sum10});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_11 : sum11});
				$('#grid').jqGrid("footerData", "set", {EVT_OCR_ITEM_DTL: "합  계", M_12 : sum12});

				// 합계 금액 css
				$('table.ui-jqgrid-ftable tr:first').children("td").css("background-color", "#D5D5D5");
			}  // End loadComplete
		});  // End jqGrid

		$(".btnS").bind("click",function()
		{
			if($.validate(".tableType2.seachT") == false) return;

			$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
			var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
			myPostData.searchYear = $("#searchYear option:selected").val();
			myPostData.searchSysCd = $("#searchSysCd option:selected").val();
			$("#grid").trigger("reloadGrid");
		});
	});

	function excelDownAction()
	{
		document.excelDownFrm.searchYear.value = $("#searchYear option:selected").val();
		document.excelDownFrm.searchSysCd.value = $("#searchSysCd option:selected").val();
		document.excelDownFrm.searchEvtNm.value = $("#searchSysCd option:selected").text();
		document.excelDownFrm.action = "<c:url value='/wrks/evtctl/stats/cctvMonType/list.excel'/>";
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
					<!-- <h3 class="tit">CCTV접속현황(월/유형)</h3> -->
					<h3 class="tit"><%=menu_nm%></h3>
				</div>
				<div class="tableType2 seachT">
					<table>
					<caption>CCTV접속현황(월/유형)</caption>
					<colgroup>
						<col style="width: 150px;" />
						<col style="width: *" />
						<col style="width: 150px;" />
						<col style="width: *" />
					</colgroup>
					<tbody>
					<tr>
						<tH>조회년도</th>
						<td>
							<select name="searchYear" id="searchYear" class="selectType1">
								<c:forEach items="${yearList}" var="val">
								<option value="${val.YYYY}"><c:out value="${val.YYYY}"></c:out></option>
								</c:forEach>
							</select>
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
	<input type="hidden" name="searchYear" id="searchYear" />
	<input type="hidden" name="searchEvtNm" id="searchEvtNm" />
	<input type="hidden" name="searchSysCd" id="searchSysCd" />
</form>
</body>
</html>