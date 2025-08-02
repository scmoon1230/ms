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
	var sortBy = 'STAN_YMD, MONEY_CODE, MEMBER_ORDER, MONEY_AMT DESC, MEMBER_NAME ASC, MEMBER_TYPE';
	var sortOr = 'ASC';
</script>
<script src="<c:url value='/js/report/rprtmemsorthigh.js'/>"></script>
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
				<ol class="breadcrumb">
					<li class="active">헌금관리 > 헌금보고 > 고액헌금자명단</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>헌금</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
                            <th>기간</th>
							<td><input type="date" id="startDate" class="date8Type" value="${startDate}"
									required="required" user-title="시작일자" onchange="$('.btnS').click()" user-data-type="betweendate" user-ref-id="endDate">
								<span class="bl">~</span>
								<input type="date" id="endDate" class="date8Type" value="${endDate}"
									required="required" user-title="종료일자" onchange="$('.btnS').click()">
								<input type="hidden" name="currentDay" id="currentDay" value="${currentDay}" user-title="현재일자">
							</td>
							<th>헌금</th>
							<td><select name="" id="moneyCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${moneyList}" var="val">
										<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
									</c:forEach>
								</select>
							</td>
							<th>예배</th>
							<td><select name="" id="worshipCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${worshipList}" var="val">
										<option value="${val.worshipCode}">${val.worshipCode} - ${val.worshipName}</option>
									</c:forEach>
								</select>
							</td>
							<th>기준금액</th>
							<td><select name="" id="moneyAmt" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="1000000">1,000,000</option>
									<option value="3000000" selected=true>3,000,000</option>
									<option value="5000000">5,000,000</option>
									<option value="7000000">7,000,000</option>
									<option value="10000000">10,000,000</option>
								</select>
							</td>
							<th>아이디여부</th>
							<td><select name="" id="idExist" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="Y">ID가 있는 헌금만 검색</option>
									<option value="N">ID가 없는 헌금만 검색</option>
								</select>
							</td>
							<td>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
								<a href="javascript:;" class="btn btnRight btnE">엑셀</a>
								<a href="javascript:;" class="btn btnRight btnP">인쇄</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>

				<div class="tableType1" style="height: 500px;">
					<table id="grid" style="width:100%">
					</table>
				</div>

				<div style="display: flex;">
					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>
					<div class="paginate">
					</div>
					<div class="btnWrap btnR">
					</div>
				</div>
			</div>
			
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>

<form id="dataForm" name="dataForm" method="post">
	<input type="hidden" name="paraStartDate"   id="paraStartDate"   value="">
	<input type="hidden" name="paraEndDate"     id="paraEndDate"     value="">
	<input type="hidden" name="paraMoneyCode"   id="paraMoneyCode"   value="">
	<input type="hidden" name="paraWorshipCode" id="paraWorshipCode" value="">
	<input type="hidden" name="paraIdExist"     id="paraIdExist"     value="">
	<input type="hidden" name="paraMoneyAmt"    id="paraMoneyAmt"    value="">
	<input type="hidden" name="sidx" id="sidx" value="">
	<input type="hidden" name="sord" id="sord" value="">
	<input type="hidden" name="titleKey"    id="titleKey"    value="">
	<input type="hidden" name="titleHeader" id="titleHeader" value="">
</form>

</body>
</html>