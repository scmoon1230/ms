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
			url : contextRoot + "/wrks/evtctl/stats/cctvConnList.json"
			, datatype: "json"
			, autowidth : true
			, postData: {
				searchStartDate : $("#searchStartDate").val(),
				searchEndDate : $("#searchEndDate").val(),
				searchSysCd : $("#searchSysCd option:selected").val(),
				searchOcrNo : $("#searchOcrNo").val(),
				searchUserId : $("#searchUserId").val()
			}
			, colNames: [
				'No', '접속일시', '카메라명', '사용자명', '사용자아이디', '기관명', '부서명', '시작시간', '종료시간',
				'그룹명', '그룹레벨명', '사건번호', '신청사유', '공문번호', '지구'
			]
			, colModel: [
				  {name:'rk',             index:'RK',              	align:'center',	width : 50 ,	cellattr : function() {	return 'style="width: 50px;"'	} }
				, {name:'connDate',       index:'CONN_DATE',        align:'center',	width : 150,	cellattr : function() {	return 'style="width: 150px;"'	} }
				, {name:'cctvNm',         index:'CCTV_NM',       	align:'left'  ,	width : 350,	cellattr : function() {	return 'style="width: 350px;"'	} }
				, {name:'userNm',         index:'USER_NM',       	align:'center',	width : 100,	cellattr : function() {	return 'style="width: 100px;"'	} }
				, {name:'userId',         index:'USER_ID',       	align:'center',	width : 100,	cellattr : function() {	return 'style="width: 100px;"'	} }
				, {name:'insttNm',        index:'INSTT_NM',         align:'center',	width : 150,	cellattr : function() {	return 'style="width: 150px;"'	} }
				, {name:'deptNm',         index:'DEPT_NM',       	align:'center',	width : 100,	cellattr : function() {	return 'style="width: 100px;"'	} }
				, {name:'vdoSeaYmdhmsFr', index:'VDO_SEA_YMDHMS_FR',align:'center',	width : 150,	cellattr : function() {	return 'style="width: 150px;"'	} }
				, {name:'vdoSeaYmdhmsTo', index:'VDO_SEA_YMDHMS_TO',align:'center',	width : 150,	cellattr : function() {	return 'style="width: 150px;"'	} }
				, {name:'grpNm',          index:'GRP_NM',		    align:'center',	width : 150,	cellattr : function() {	return 'style="width: 150px;"'	} }
				, {name:'authLvlNm',      index:'AUTH_LVL_NM',      hidden:true }
				, {name:'evtNo',          index:'EVT_NO',           align:'center',	width : 200,	cellattr : function() {	return 'style="width: 200px;"'	} }
				, {name:'rqstRsnTyNm',    index:'RQST_RSN_TY_NM',   align:'center',	width : 100,	cellattr : function() {	return 'style="width: 100px;"'	} }
				, {name:'paperNo',        index:'PAPER_NO',         hidden:true }
				, {name:'dstrtNm',        index:'DSTRT_NM',         align:'center',	width : 150,	cellattr : function() {	return 'style="width: 150px;"'	} }
			]
			, rowNum: $('#rowPerPageList').val()
			, height: $('#grid').parent().height() - 40
			, sortname: 'CONN_DATE'
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
			//myPostData.searchPaperNo = $("#searchPaperNo").val();
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
		//document.excelDownFrm.searchPaperNoExcel.value   = $("#searchPaperNo").val();
		document.excelDownFrm.searchCctvNmExcel.value    = $("#searchCctvNm").val();
		document.excelDownFrm.searchUserNmExcel.value    = $("#searchUserNm").val();
		document.excelDownFrm.action = contextRoot + "/wrks/evtctl/stats/cctvConnListExcel.excel";
		document.excelDownFrm.submit();
	}
	
	function sendCctvConnInfoToBase () {
		if(confirm("영상접속정보를 기초로 보내시겠습니까?") == false) return false;
		
		var params = "";
		$.ajaxEx($('#grid'), {
			type : "POST",
			url : contextRoot + "/link/cctvlogcnnct/test.xx",
			dataType : "json",
			data: params,
			success:function(data){
				alert(data.responseMsg);
			},
			error:function(e){
				alert(data.responseMsg);
			}
		});
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
								<col style="width: 100px;" />
								<col style="width: *;" />
								<col style="width: 100px;" />
								<col style="width: *;" />
								<col style="width: 100px;" />
								<col style="width: *;" />
								<col style="width: 100px;" />
								<col style="width: *;" />
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
									<th>사용자명</th>
									<td><input type="text" name="searchUserNm" id="searchUserNm" class="txtType searchEvt" style="ime-mode: active"></td>
									<th>카메라명</th>
									<td><input type="text" name="searchCctvNm" id="searchCctvNm" class="txtType searchEvt" style="ime-mode: active; width:300px;"></td>
								<!--<th>공문번호</th>
									<td><input type="text" name="searchPaperNo" id="searchPaperNo" class="txtType searchEvt" style="ime-mode: active;">
									</td>-->
									<th>사건번호</th>
									<td><input type="text" name="searchEvtNo" id="searchEvtNo" class="txtType searchEvt" style="ime-mode: active;">
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
						
						<c:if test="${LoginVO.userId=='sys'}">	<!-- sys계정으로만 -->
							<c:if test="${LoginVO.gSysId=='PVEWIDE'}">	<!-- 광역일 때 -->
								<a href="#" class="btn btnCDe" onclick="sendCctvConnInfoToBase()">영상접속정보를 기초로 보내기</a>
							</c:if>
						</c:if>
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
