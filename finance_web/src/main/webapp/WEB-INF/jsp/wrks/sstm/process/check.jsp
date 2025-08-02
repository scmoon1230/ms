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
		url: '<c:url value='/'/>wrks/sstm/process/check/list.json',
		datatype: "json",
		postData: {
			  rcvPrcsId : $("#sRcvPrcsId").val()
		  	, rcvPrcsNm : $("#sRcvPrcsNm").val()
		  	, rcvSvrIp : $("#sRcvSvrIp").val()
		  	, rcvTrmsYn : $("#sRcvTrmsYn option:selected").val()
		},
		colNames: [	'No'	, '프로세스ID'		, '프로세스명'		, '서버 IP'		, '상태'		, '체크주기'
						, '관리자'
				   ],
		colModel: [	{ name: 'rk', index: 'RK', width:30, align:'center', sortable: false}
					, { name: 'prcsId', index: 'PRCS_ID', width:85, align:'center'}
					, { name: 'prcsNm', index: 'PRCS_NM', width:120, align:'left',classes:'tl'}
					, { name: 'prcsIp', index: 'PRCS_IP', width:75, align:'center'}
					, { name: 'trmsYn', index: 'TRMS_YN', width:50, align:'center'}
					, { name: 'checkCycle', index: 'CHECK_CYCLE', width:50, align:'center'}
					, { name: 'ichUserNm', index: 'ICH_USER_NM', width:50, align:'center'}
		  ],
		//pager: '#pager',
		rowNum: $('#rowPerPageList').val()
		, autowidth: true
		, height: $("#grid").parent().height()-40
		, sortname: 'RK'
		, sortorder: 'ASC'
		, viewrecords: true
		, shrinkToFit: true
		, scrollOffset: 0
		, autowidth: true
		, loadonce: false
		, sortable: false
		, rowNum : 100
		, jsonReader: {
			id: "RK",
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
		loadComplete : function(data)
		{
			checkGridNodata('', data);
		},
		onSelectRow: function(rowid, status, e) {
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
	$(".btnS").bind("click",function()
			{
				if($.validate(".tableTypeFree.tableType2") == false) return;
				//$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
				var myPostData = $("#grid").jqGrid('getGridParam', 'postData');

				//검색할 조건의 값을 설정한다.
				myPostData.rcvPrcsId = $("#sRcvPrcsId").val();
				myPostData.rcvPrcsNm = $("#sRcvPrcsNm").val();
				myPostData.rcvSvrIp = $('#sRcvSvrIp').val();
				myPostData.rcvTrmsYn = $("#sRcvTrmsYn option:selected").val();

				$("#grid").trigger("reloadGrid");
	});
});

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
				<div class="tableTypeFree tableType2">
					<table>
						<caption></caption>
						<colgroup>
							<col style="width:90px;" />
							<col style="width:150px;" />
							<col style="width:90px;" />
							<col style="width:160px;" />
							<col style="width:70px;" />
							<col style="width:160px;" />
							<col style="width:50px;" />
							<col style="width:auto;" />
						</colgroup>

				  	<!--   검색바디	 -->
					<tbody>
						<tr>
							<th>프로세스ID</th>
							<td><input type="text" id="sRcvPrcsId" class="txtType txtType100 searchEvt" style="ime-mode:active" maxlength="100"/></td>

							<th>프로세스명</th>
							<td><input type="text" id="sRcvPrcsNm" class="txtType txtType100 searchEvt" style="ime-mode:active" maxlength="100"/></td>

							<th>서버 IP</th>
							<td><input type="text" id="sRcvSvrIp" class="txtType txtType100 searchEvt" style="ime-mode:active" maxlength="100"/></td>

							<th>상태</th>
							<td>
								<select id="sRcvTrmsYn" class="selectType1">
									<option value="">전체</option>
									<option value="N"><c:out value="정상" ></c:out></option>
									<option value="Y"><c:out value="확인필요" ></c:out></option>
								</select>
								<a href="javascript:;" class="btn btnRight btnS">검색</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>

					<!--  건수 조회   -->
					<div class="searchArea">
						<div class="page fL">

							<!-- 페이지 표시 갯수 표출 -->
							<%--<span class="txt">페이지당</span>
                        <div class="selectBox">
							<select name="rowPerPageList" id="rowPerPageList" class="selectType1">
							    <c:forEach items="${rowPerPageList}" var="val">
							        <option value="${val.CD_ID}" ${val.CD_ID == rowPerPageSession ? 'selected' : ''}><c:out value="${val.CD_NM_KO}" ></c:out></option>
							    </c:forEach>
							</select>
                        </div>--%>

							<span class="totalNum">
								전체<em id="rowCnt"></em>건
							</span>
						</div>
					</div>

                <div class="tableType1" style="height: 445px;">
					<table id="grid" style="width:100%;">
					</table>
				</div>

				<!-- 페이지 표시 버튼 -->
			  <!--   <div class="paginate">
					</div>
				 -->

				</div>
			</div>
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