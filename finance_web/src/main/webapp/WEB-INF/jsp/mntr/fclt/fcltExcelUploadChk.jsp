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
	<form:form commandName="searchVO" name="ExcelListForm" method="post">
		<table id="searchResult">
			<tbody>
				<tr>
					<c:forTokens var="titleNm" items="${searchVO.titleNm}" delims=",">
						<th>${titleNm}</th>
					</c:forTokens>
				</tr>
				<c:forEach items="${list}" var="excelList" varStatus="status">
					<c:forTokens var="titleNm" items="${searchVO.titleNm}" delims=",">
						<c:set var="name">${titleNm}</c:set>
						<c:set var="nameChk">${titleNm}_CHK</c:set>
						<c:choose>
							<c:when test="${nameChk eq '등록구분_CHK'}">
								<c:choose>
									<c:when test="${excelList[nameChk] eq 'error'}">
										<tr style="background: #ffff00;">
									</c:when>
									<c:otherwise>
										<tr>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${(excelList[nameChk] eq 'error') and (nameChk ne '등록구분_CHK')}">
								<td bgcolor="#FF5733">${excelList[name]}</td>
							</c:when>
							<c:otherwise>
								<td>${excelList[name]}</td>
							</c:otherwise>
						</c:choose>
					</c:forTokens>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form:form>
</body>
</html>