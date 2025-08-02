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
<script src="<c:url value='/js/env/userlog.js'/>"></script>
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
					<li class="active">공통정보 > 사용기록조회</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>사용기록</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
                            <th>기간</th>
							<td><input type="date" id="startDate" class="date8Type" value="${startDate}"
									required="required" user-title="시작일자" onchange="$('.btnS').click()" user-data-type="betweendate" user-ref-id="endDate"/>
								<span class="bl">~</span>
								<input type="date" id="endDate" class="date8Type" value="${currentDay}"
									required="required" user-title="종료일자" onchange="$('.btnS').click()"/>
								<input type="hidden" name="currentDay" id="currentDay" value="${currentDay}" user-title="현재일자">
							</td>
							<th>성명</th>
							<td><input type="text" name="" id="userName" class="txtType searchEvt" style="ime-mode:active">
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
				</div>
			</div>

		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>