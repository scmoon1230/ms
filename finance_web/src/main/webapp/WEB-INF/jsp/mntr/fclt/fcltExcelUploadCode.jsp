<%@ page language="java" contentType="application/vnd.ms-excel; name='excel', text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<!doctype html>
<html>
<head>
<meta http-equive="content-type" content="text/html; charset=utf-8">
<style>
table {
	border: 1px solid black;
}

table th {
	padding: 5px;
	text-align: center;
}

table td {
	padding: 5px;
	text-align: left;
	mso-number-format: '\@';
}

table#searchResult tr th {
	background-color: #000;
	color: #fff;
}
</style>
</head>
<body>
	<form:form commandName="searchVO" name="CodeListForm" method="post">
		<table id="searchResult">
			<tbody>
				<c:forEach items="${excelDownCodeList}" var="excelList" varStatus="status">
					<c:if test="${status.index eq 0}">
						<tr>
							<c:forEach items="${excelList}" var="codeList">
								<c:if test="${codeList.key eq 'cdGrpNm'}">
									<th>구분</th>
								</c:if>
								<c:if test="${codeList.key eq 'cdId'}">
									<th>코드</th>
								</c:if>
								<c:if test="${codeList.key eq 'cdNmKo'}">
									<th>코드명</th>
								</c:if>
							</c:forEach>
							<th>설명</th>
						</tr>
					</c:if>
					<tr>
						<c:forEach items="${excelList}" var="codeList">
							<td>${codeList.value}</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form:form>
</body>
</html>