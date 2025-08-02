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
<script src="<c:url value='/js/sheet/sheetduration.js'/>"></script>
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
					<li class="active">입출관리 > 입출보고 > 기간별 입/출전표</li>
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
								<input type="date" id="endDate" class="date8Type" value="${currentDay}"
									required="required" user-title="종료일자" onchange="$('.btnS').click()">
								<input type="hidden" name="currentDay" id="currentDay" value="${currentDay}" user-title="현재일자">
							</td>
							<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${AcctgbList}" var="val">
										<option value="${val.acctGb}" ${'1' == val.acctGb ? 'selected' : ''} ><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>수입지출</th>
							<td><select name="" id="inoutGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="수입">수입</option>
									<option value="지출">지출</option>
								</select>
							</td>
							<th>계정</th>
							<td><input type="text" name="" id="acctCode" class="txtType searchEvt" style="ime-mode:active">
							</td>
							<th>내역</th>
							<td><input type="text" name="" id="acctRemark" class="txtType searchEvt" style="ime-mode:active">
							</td>
							<td><a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
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
				<%--<div class="btnWrap btnR">
						<a href="#" class="btn btnDt btnRgt">등록</a>
						<a href="#" class="btn btnMultiDe">삭제</a>
					</div>--%>
				</div>
			</div>

		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>