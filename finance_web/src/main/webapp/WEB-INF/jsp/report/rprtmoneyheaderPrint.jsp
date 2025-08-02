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
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<script>
	var sortBy = 'SORT_ORDER, MONEY_AMT DESC, MEMBER_NAME';
	var sortOr = 'ASC';
</script>
<script src="<c:url value='/js/report/rprtmoneyheaderPrint.js'/>"></script>
</head>
<body>
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>

<div id="wrapper" class="wth100">
	<!-- container -->
	<div class="container">
		<!-- content -->
		<div class="contentWrap">
			<div class="content">
				<div class="btnR">
					<a href="#" class="btn btnPrint">인쇄</a>
					<a href="javascript:window.close();" class="btn btnC">취소</a>
				</div>
				<div>
					<div style="margin-bottom: 10px;">
						<div class="printTitle">헌금자명단</div>
					</div>
				</div>

				<table>
					<tr><td style="width:50%">
						기준일: <span class="printStanYmd"></span>
						</td>
						<td></td>
					</tr>
				</table>
				
				<table>
				    <thead>
				        <tr><th class="pTh">금액</th>
			            	<th class="pTh">헌금자</th>
				        </tr>
				    </thead>
				    <tbody id="tableBody">
				        <!-- 동적 데이터 생성 -->
				    </tbody>
				</table>
				<!-- 
				<div class="tableType1" style="height: 500px;">
					<table id="grid" style="width:100%">
					</table>
				</div>
				<div class="tit">
					<h4 style="text-align:right; padding-right: 20px;">
						합계 : <span id="totalAmnt"></span> 원
					</h4>
				</div>

				<div style="display: flex;">
					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>
					<div class="paginate">
					</div>
					<div class="btnWrap btnR">
					<%--<a href="#" class="btn btnDt btnRgt">등록</a>
						<a href="#" class="btn btnMultiDe">삭제</a>--%>
					</div>
				</div>
				 -->
			</div>

		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>