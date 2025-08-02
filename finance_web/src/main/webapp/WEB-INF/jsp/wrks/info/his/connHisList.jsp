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
<style type="text/css">
.ui-jqgrid-btable tr[role=row]:nth-child(odd) {
	background: #eee;
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
	
		$.jqGrid($('#grid'), {
			url: '<c:url value='/'/>wrks/info/his/connHisListData.json'
			, datatype: "json"
			, scrollOffset : 0
			, autowidth: true
			, postData: {
				  seachTy : $("#seachTy").val()
				, seachTxt : $("#seachTxt").val()
				, strDateStart : $("#strDateStart").val()
				, strDateEnd : $("#strDateEnd").val()
			}
			, colNames: [
				  'No', '일자', '접속시간', '사용자명', '사용자아이디', '그룹명', '권한레벨명', '기관', '부서', '지구', '로그인', '로그아웃'
			]
			, colModel: [
				  {name:'rk',             index:'RK'               , width:50, align:'center', sortable: false}
				, {name:'connectYmd',     index:'CONNECT_YMD'      , width:215, align:'center'}
				, {name:'loginFirstTime', index:'LOGIN_FIRST_TIME' , width:250, align:'center'}
				, {name:'userNmKo',       index:'USER_NM_KO'       , width:215, align:'center'}
				, {name:'userId',         index:'USER_ID'          , width:215, align:'center'}
				, {name:'grpNm',          index:'GRP_NM'           , width:215, align:'center'}
				, {name:'authLvlNm',      index:'AUTH_LVL_NM'      , width:215, align:'center'}
				, {name:'insttNm',        index:'INSTT_DEPT_NM'    , width:200, align:'center'}
				, {name:'deptNm',         index:'INSTT_DEPT_NM'    , width:200, align:'center'}
				, {name:'dstrtNm',        index:'DSTRT_NM'         , 'hidden':true}
				, {name:'loginTime',      index:'LOGIN_TIME'       , 'hidden':true}
				, {name:'logoutTime',     index:'LOGOUT_TIME'      , 'hidden':true}
			]
			, pager: '#pager'
			, rowNum: $('#rowPerPageList').val()
			, height: $('#grid').parent().height() - 40
			, sortname: 'CONNECT_YMD'
			, sortorder: 'DESC'
			, viewrecords: true
			, multiselect: false
			, loadonce:false
			, jsonReader: {
				  id: "USER_ID"
				, root: function(obj) { return obj.rows; }
				, page: function(obj) { return 1; }
				, total: function(obj) {
					if(obj.rows.length > 0) {
						var page  = obj.rows[0].rowcnt / rowNum;
						if( (obj.rows[0].rowcnt % rowNum) > 0)
							page++;
						return page;
					} else {
						return 1;
					}
				}
				, records: function(obj) {
					return $.showCount(obj);
				}
			}
			, beforeRequest: function() {
				$.loading(true);
				rowNum = $('#rowPerPageList').val();
			}
			, beforeProcessing: function(data, status, xhr) {
				$.loading(false);
				if(typeof data.rows != "undefine" || data.row != null) {
					$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
				}
			}
			, loadComplete : function(data) {
				oCommon.jqGrid.checkNodata('', data);
				oCommon.jqGrid.gridComplete(this);
			}
		});
	
		$(".btnS").bind("click",function()
		{
			if ($.validate(".tableTypeHalf.seachT") == false) return;
			$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
			var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
	
			//검색할 조건의 값을 설정한다.
			myPostData.seachTy = $("#seachTy").val();
			myPostData.seachTxt = $("#seachTxt").val();
			myPostData.strDateStart = $("#strDateStart").val();
			myPostData.strDateEnd = $("#strDateEnd").val();
	
			$("#grid").trigger("reloadGrid");
		});
			
		$(".tableType1").css('height', window.innerHeight - 350);
		$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
			
	});

	function excelDownAction() {
		document.excelDownFrm.seachTy.value = $("#seachTy").val();
		document.excelDownFrm.seachTxt.value = $("#seachTxt").val();
		document.excelDownFrm.searchStartDate.value = $("#strDateStart").val();
		document.excelDownFrm.searchEndDate.value = $("#strDateEnd").val();
		document.excelDownFrm.action = "<c:url value='/wrks/info/his/connHisListData.excel'/>";
		document.excelDownFrm.submit();
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
							<caption>사용자접속현황</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
							<tr><th>기간</th>
								<td><input type="text" name="" id="strDateStart" class="date8Type datepicker1" value="${firstDay}" required="required" user-title="시작일자" user-data-type="betweendate" user-ref-id="strDateEnd">
									<span class="bl">~</span>
									<input type="text" name="" id="strDateEnd" class="date8Type datepicker2" value="${currentDay}" required="required" user-title="종료일자">
								</td>
								<th>분류</th>
								<td><select name="seachTy" id="seachTy" class="selectType1" maxlength="1">
										<option value="NM">사용자명</option>
										<option value="ID">사용자아이디</option>
									</select>
									<input type="text" name="seachTxt" id="seachTxt" class="txtType searchEvt" style="ime-mode:active" maxlength="100"/>
									<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
								</td>
							</tr>
							</tbody>
						</table>
					</div>
					
					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

					<div class="tableType1" style="height: 495px;">
						<table id="grid" style="width:100%"></table>
					</div>
					<div class="paginate"></div>
					<div class="btnWrap btnR">
						<a href="javascript:;" class="btn btnDt btnExcel">엑셀저장</a>
					</div>
				</div>
				<!-- //content -->
			</div>
		<!-- //container -->
		</div>
	</div>
	<form name="excelDownFrm" method="post">
		<input type="hidden" name="seachTy" id="seachTy" />
		<input type="hidden" name="seachTxt" id="seachTxt" />
		<input type="hidden" name="searchStartDate" id="searchStartDate" />
		<input type="hidden" name="searchEndDate" id="searchEndDate" />
	</form>
</body>
</html>
