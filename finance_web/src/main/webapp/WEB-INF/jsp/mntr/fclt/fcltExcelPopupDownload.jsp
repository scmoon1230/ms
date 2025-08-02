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
	<form:form commandName="searchVO">
		<table id="searchResult">
			<tbody>
				<tr>
					<c:forEach var="titleNm" items="${searchVO.titleNm}">
						<th>${titleNm}</th>
					</c:forEach>
				</tr>
				<c:forEach items="${list}" var="excelList">
					<tr>
						<c:forEach var="titleNm" items="${searchVO.titleNm}">
							<c:set var="name">${titleNm}</c:set>
							<c:choose>
								<c:when test="${name eq '좌표X'}">
									<td>${excelList['좌표x']}</td>
								</c:when>
								<c:when test="${name eq '좌표Y'}">
									<td>${excelList['좌표y']}</td>
								</c:when>
								<c:otherwise>
									<td>${excelList[name]}</td>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form:form>
</body>
</html>