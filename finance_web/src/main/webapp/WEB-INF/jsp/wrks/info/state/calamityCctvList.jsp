<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>지원업무</title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
	
		$.jqGrid($('#grid'), {
			//  url: '<c:url value='/'/>wrks/info/his/cctvPtzHisListData.json'
			  url: '<c:url value='/'/>wrks/info/state/calamityCctvListData.json'
			, datatype: "json"
			, autowidth: true
			, postData :
			{ 
				  clmtNo : $("#clmtNo").val()
				, cctvNm : $("#cctvNm").val()
				, strDateStart : $("#strDateStart").val()
				, strDateEnd : $("#strDateEnd").val()
			}
			, colNames :
			[
			 	  '발생번호'
				, '유형(상세내용)'
				, '카메라명'
				, '접속일시'
			]
			, colModel :
			[
				  { name: 'EVT_OCR_NO', width:150, align:'center'}
				, { name: 'EVT_DTL', width:340, align:'center'}
				, { name: 'FCLT_NM', width:340, align:'center'}
				, { name: 'CONN_DATE', width:150, align:'center'}
			]
			, pager: '#pager'
			, rowNum: $('#rowPerPageList').val()
			, sortname: 'EVT_OCR_NO'
			, sortorder: 'DESC'
			, viewrecords: true
			, multiselect: false
			, loadonce:false
			, jsonReader:
			{
				  id: "EVT_OCR_NO"
				, root: function(obj) { return obj.rows; }
				, page: function(obj) { return 1; }
				, total: function(obj)
				{
					if(obj.rows.length > 0) {
						var page  = obj.rows[0].ROWCNT / rowNum;
						if( (obj.rows[0].ROWCNT % rowNum) > 0)
							page++;
						return page;
					} else {
						return 1;
					}
				}
				, records: function(obj) { return $.showCount(obj); }
			}
	/* 
			, onCellSelect: function(rowid, iCol, cellcontent, e)
			{
				if(iCol == 0) {
					return false;
				} else if(iCol == 1) {
					var list = jQuery("#grid").getRowData(rowid);
					$("#dFcltId").html(list.FCLT_ID);
					
					$.showDetail();	
				}
			}
	*/
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
		});
	
		$(".btnS").bind("click",function(){
			if ($.validate(".tableTypeHalf.seachT") == false) return;
			$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
			var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
			
			//검색할 조건의 값을 설정한다.
				myPostData.clmtNo = $("#clmtNo").val()
				myPostData.cctvNm = $("#cctvNm").val()
				myPostData.strDateStart = $("#strDateStart").val();
				myPostData.strDateEnd = $("#strDateEnd").val();
				
			$("#grid").trigger("reloadGrid");
		});
	});
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
			<!-- 재난발생 -->
			<div class="content">
				<div class="titArea">
					<!-- <h3 class="tit">CCTV접속현황</h3> -->
					<h3 class="tit"><%=menu_nm%></h3>
				</div>
				<div class="tableTypeHalf seachT">
					<table>
					<caption>CCTV접속현황</caption>
					<colgroup>
						<col style="width: 100px;" />
						<col style="width: 150px" />
						<col style="width: 100px;" />
						<col style="width: 150px" />
						<col style="width: 100px;" />
						<col style="width: *" />
					</colgroup>
					<tbody>
					<tr>
						<th>발생번호</th>
						<td>
							<input type="text" name="clmtNo" id="clmtNo" class="txtType txtType100 searchEvt" style="ime-mode:active" maxlength="100"/>
						</td>
						<th>카메라명</th>
						<td>
							<input type="text" name="cctvNm" id="cctvNm" class="txtType txtType100 searchEvt" style="ime-mode:active" maxlength="100"/>
						</td>
						<th>기간</th>
						<td>
							<input type="text" name="" id="strDateStart" class="date8Type datepicker1" value="${firstDay}" required="required" user-title="시작일자" user-data-type="betweendate" user-ref-id="strDateEnd">
							<span class="bl">~</span>
							<input type="text" name="" id="strDateEnd" class="date8Type datepicker2" value="${currentDay}" required="required" user-title="종료일자">
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
				<div class="tableType1" style="overflow-y:hidden; overflow-x:hidden">
					<table id="grid" style="width:100%"></table>
				</div>
				<div class="paginate"></div>
			</div>
		<!-- //content -->
		</div>
	<!-- //container -->
	</div>
</div>
<!-- footer -->
<!-- <div id="footwrap">
	<div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->
</body>
</html>