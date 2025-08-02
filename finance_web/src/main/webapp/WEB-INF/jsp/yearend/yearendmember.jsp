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
<script src="<c:url value='/js/yearend/yearendmember.js'/>"></script>
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
					<li class="active">헌금관리 > 연말정산 > 홈택스기초정보</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>신도</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
							<th>신도번호</th>
							<td><input type="text" name="" id="memberNo" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>신도명</th>
							<td><input type="text" name="" id="memberName" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>직분</th>
							<td><select name="" id="positionCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${positionList}" var="val">
										<option value="${val.positionCode}"><c:out value="${val.positionName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>재적상태</th>
							<td><select name="" id="useYn" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="Y" selected>재적</option>
									<option value="N">이적</option>
								</select>
							</td>
							<th>번호구분</th>
							<td><select name="" id="juminNoGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="1">주민번호</option>
									<option value="2">사업자번호</option>
								</select>
							</td>
							<th>동의여부</th>
							<td><select name="" id="agreeYn" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="Y">동의함</option>
									<option value="N">동의하지 않음</option>
								</select>
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

				<div style="display: flex;">
					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>
					<div class="paginate">
					</div>
					<div class="btnWrap btnR">
						<a href="#" class="btn btnDt btnRgt">등록</a>
						<%--<a href="#" class="btn btnMultiDe">삭제</a>--%>
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