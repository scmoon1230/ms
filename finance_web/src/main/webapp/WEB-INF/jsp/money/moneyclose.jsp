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
<script src="<c:url value='/js/money/moneyclose.js'/>"></script>
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
					<li class="active">헌금관리 > 헌금등록 > 헌금마감/전표처리</li>
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
                            <th>기준일자</th>
							<td><input type="date" id="stanYmd" class="date8Type" value="${currentDay}"
									required="required" user-title="기준일자" onchange="$('.btnS').click()">
								<span id="startYmd"></span> ~ <span id="endYmd"></span>
							</td>
							<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="changeMoneyCode();$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${AcctgbList}" var="val">
										<option value="${val.acctGb}" ${'1' == val.acctGb ? 'selected' : ''} ><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
						<c:if test="${LoginVO.userGb ne '일반'}">
							<td><a href="javascript:doClose();"     class="btn btnC">헌금마감처리</a>
								<a href="javascript:cancelClose();" class="btn btnC">헌금마감취소</a>
								마감과 동시에 입금전표로 자동처리됩니다.
							</td>
						</c:if>
						<%--<th>헌금</th>
							<td><select name="" id="moneyCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${moneyList}" var="val">
										<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
									</c:forEach>
								</select>
							</td>--%>
							<td>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>

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
				</div>
			</div>

		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>