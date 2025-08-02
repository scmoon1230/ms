<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<style type="text/css">
.ui-jqgrid-btable tr[role=row]:nth-child(odd) {
	background: #eee;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		
		$.jqGrid($('#grid'), {
			url : contextRoot + "/wrks/evtctl/stats/tvoViewList.json"
			, datatype: "json"
			, autowidth : true
			, postData: {
				searchStartDate : $("#searchStartDate").val(),
				searchEndDate : $("#searchEndDate").val(),
				searchSysCd : $("#searchSysCd option:selected").val(),
				searchOcrNo : $("#searchOcrNo").val(),
				searchUserId : $("#searchUserId").val()
			}
			, colNames :
				[ '번호', '구분', '신청일시 ', '신청자ID', '신청자', '사건번호', '사건명', '발생일시', '발생주소', '신청목적CD', '신청사유', '신청사유상세', '공문/전자문서', '열람종료일시', '진행상태'
				]
			, colModel :
				[     { name : 'rk'				, hidden : true}
				    , { name : 'gubun'			, hidden : true} 
					, { name : 'viewRqstYmdhms'	, width:80, align:'center', cellattr : function() {return 'style="width: 80px;"';}
					//	, formatter : function(cellvalue, options, rowObject) {
					//		var sViewRqstYmdhms = rowObject.viewRqstYmdhms;
					//		momentViewRqstYmdhms = moment(sViewRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');

					//		sViewRqstYmdhms = momentViewRqstYmdhms.format('YYYY-MM-DD HH:mm');
					//		return sViewRqstYmdhms;
					//	}
					  }	
					, { name : 'viewRqstUserId'	, hidden : true}
					, { name : 'viewRqstUserNm'	, width:50 , align:'center', cellattr : function() {return 'style="width: 50px;"';}}
					, { name : 'evtNo'			, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}}
					, { name : 'evtNm'			, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}}							
					, { name : 'evtYmdHms'		, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}}
					, { name : 'evtAddr'		, width:250, align:'left'  , cellattr : function() {return 'style="width: 200px;"';}}
				    , { name : 'rqstRsnTyCd'	, hidden : true} 						
					, { name : 'rqstRsnTyNm'	, width:50 , align:'center', cellattr : function() {return 'style="width: 50px;"';}}
					, { name : 'rqstRsnDtl'		, hidden : true}
					, { name : 'paperNo'		, width:120 , align:'center', cellattr : function() {return 'style="width: 80px;"';}}
					, { name : 'viewEndYmdhms'	, width:80, align:'center', cellattr : function() {return 'style="width: 100px;"';}
					//	, formatter : function(cellvalue, options, rowObject) {
					//		var sViewEndYmdhms = rowObject.viewEndYmdhms;
					//		momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
					//		if (momentViewEndYmdhms.isValid()) {
					//			sViewEndYmdhms = momentViewEndYmdhms.format('YYYY-MM-DD HH:mm');
					//		}else{
					//			sViewEndYmdhms = '';
					//		}
					//		return sViewEndYmdhms;
					//	}
					  }
					, { name : 'tvoPrgrsNm'	, width:80, align:'center'  , cellattr : function() {return 'style="width: 100px;"';}}
				]
			, rowNum: $('#rowPerPageList').val()
			, height: $('#grid').parent().height() - 40
			, sortname: 'VIEW_RQST_YMDHMS'
			, sortorder : 'DESC'
			, viewrecords : true
			, shrinkToFit : true
			, scrollOffset : 0
			, loadonce : false
			, jsonReader : {
				root : function(obj) {	return obj.rows;	},
				page : function(obj) {	return 1;			},
				total : function(obj) {
					if (obj.rows.length > 0) {
						var page = obj.rows[0].rowcnt / rowNum;
						if ((obj.rows[0].rowcnt % rowNum) > 0) {
							page++;
						}
						return page;
					} else {
						return 1;
					}
				},
				records : function(obj) {
					return $.showCount(obj);
				}
			}
			, cmTemplate: { sortable: false }
			, beforeRequest : function() {
				$.loading(true);
				rowNum = $('#rowPerPageList').val();
			}
			, beforeProcessing : function(data, status, xhr) {
				$.loading(false);
				if (typeof data.rows != "undefined" || data.row != null) {
					$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
				}
			}
			, loadComplete : function(data) {
				oCommon.jqGrid.checkNodata('', data);
				oCommon.jqGrid.gridComplete(this);
			} // End loadComplete
		}); // End jqGrid

		$(".btnS").bind("click", function() {
			if ($.validate(".tableType2.seachT") == false) return;
			$("#grid").setGridParam({
				rowNum: $('#rowPerPageList').val()
			});
			var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
			myPostData.searchStartDate = $("#searchStartDate").val();
			myPostData.searchEndDate = $("#searchEndDate").val();
			myPostData.searchEvtNo   = $("#searchEvtNo").val();
			myPostData.searchPaperNo = $("#searchPaperNo").val();
			myPostData.searchCctvNm  = $("#searchCctvNm").val();
			myPostData.searchUserNm  = $("#searchUserNm").val();
			$("#grid").trigger("reloadGrid");
		});

		$("#searchEndDate").bind("change", function() {
			var searchEndDate = $("#searchEndDate").val();
			var currentDay = $("#currentDay").val();
			if (searchEndDate <= currentDay) {
				return true;
			}
			else {
				alert("종료일은 현재일 까지만 조회 가능 합니다.");
				$("#searchEndDate").val("${endDate}");
				return false;
			}
		});
		
		$(".tableType1").css('height', window.innerHeight - 350);
		$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
		
	});

	function excelDownAction() {
		var curPage = '1';
		if (typeof $('#cur-page').val() != "undefined") {
			curPage = $('#cur-page').val();
		}
		document.excelDownFrm.searchPageListExcel.value  = $("#rowPerPageList").val();
		document.excelDownFrm.searchCurrPageExcel.value  = curPage;
		document.excelDownFrm.searchStartDateExcel.value = $("#searchStartDate").val();
		document.excelDownFrm.searchEndDateExcel.value   = $("#searchEndDate").val();
		document.excelDownFrm.searchEvtNoExcel.value     = $("#searchEvtNo").val();
		document.excelDownFrm.searchPaperNoExcel.value   = $("#searchPaperNo").val();
		document.excelDownFrm.searchCctvNmExcel.value    = $("#searchCctvNm").val();
		document.excelDownFrm.searchUserNmExcel.value    = $("#searchUserNm").val();
		document.excelDownFrm.action = contextRoot + "/wrks/evtctl/stats/tvoViewListExcel.excel";
		document.excelDownFrm.submit();
	}
</script>

</head>
<body>
	<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp"%>
	<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
	<div id="wrapper" class="wth100">
		<!-- container -->
		<div class="container">
			<!-- content -->
			<div class="contentWrap">
				<div class="content">
					<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp"%>
					<div class="tableType2 seachT">
						<table>
							<caption>영상접속이력</caption>
							<colgroup>
								<col style="width: 100px;" />	<col style="width: *;" />
								<col style="width: 100px;" />	<col style="width: *;" />
								<col style="width: 100px;" />	<col style="width: *;" />
								<col style="width: 100px;" />	<col style="width: *;" />
							</colgroup>
							<tbody>
								<tr>
									<tH>기간</th>
									<td><input type="text" name="searchStartDate" id="searchStartDate" class="date8Type datepicker1" value="${startDate}"
											required="required" user-title="시작일자" user-data-type="betweendate" user-ref-id="searchEndDate">
										<span class="bl">~</span>
										<input type="text" name="searchEndDate" id="searchEndDate" class="date8Type datepicker2" value="${currentDay}"
											required="required" user-title="종료일자">
										<input type="hidden" name="currentDay" id="currentDay" value="${currentDay}" user-title="현재일자">
									</td>
									<th>신청자</th>
									<td><input type="text" name="searchUserNm" id="searchUserNm" class="txtType searchEvt" style="ime-mode: active"></td>
								<!--<th>카메라명</th>
									<td><input type="text" name="searchCctvNm" id="searchCctvNm" class="txtType searchEvt" style="ime-mode: active; width:300px;"></td>-->
									<th>사건번호</th>
									<td><input type="text" name="searchEvtNo" id="searchEvtNo" class="txtType searchEvt" style="ime-mode: active;"></td>
									<th>공문/전자문서</th>
									<td><input type="text" name="searchPaperNo" id="searchPaperNo" class="txtType searchEvt" style="ime-mode: active;">
										<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
									</td>
								</tr>
							</tbody>
						</table>
					</div>

					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

					<div class="tableType1" style="height: 495px;">
						<table id="grid"></table>
					</div>
					<div class="paginate"></div>
					<div class="btnWrap btnR">
						<a href="javascript:;" class="btn btnDt btnExcel">엑셀저장</a>
					</div>
				</div>
				<!-- End content -->
			</div>
			<!-- End contentWrap -->
		</div>
		<!-- End container -->
	</div>
	<form name="excelDownFrm" method="post">
		<input type="hidden" name="searchPageListExcel"  id="searchPageListExcel" />
		<input type="hidden" name="searchCurrPageExcel"  id="searchCurrPageExcel" />
		<input type="hidden" name="searchStartDateExcel" id="searchStartDateExcel" />
		<input type="hidden" name="searchEndDateExcel"   id="searchEndDateExcel" />
		<input type="hidden" name="searchEvtNoExcel"     id="searchEvtNoExcel" />
		<input type="hidden" name="searchPaperNoExcel"   id="searchPaperNoExcel" />
		<input type="hidden" name="searchCctvNmExcel"    id="searchCctvNmExcel" />
		<input type="hidden" name="searchUserNmExcel"    id="searchUserNmExcel" />
	</form>
</body>
</html>
