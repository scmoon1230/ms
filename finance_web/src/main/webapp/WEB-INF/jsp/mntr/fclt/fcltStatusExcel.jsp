<%@ page language="java" contentType="application/vnd.ms-excel; name='excel', text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<META HTTP-EQUIVE="CONTENT-TYPE" CONTENT="TEXT/HTML; CHARSET=UTF-8">
<style>
table {
	border: 1px solid black;
}

table th {
	height: 30px;
	padding: 0 10px 0 0;
	background-color: #f6f6f6;
	text-align: right;
	border-bottom: 1px solid #e0e0e0;
	text-align: center;
}

table td {
	padding: 5px 0 5px 10px;
	border-bottom: 1px solid #e0e0e0;
	border-right: 1px solid #e0e0e0;
	text-align: center;
	mso-number-format: "\@"
}
</style>

<form:form commandName="searchVO" name="listForm" method="post">
	<h2>시설물현황</h2>
	<div>
		<h3>검색조건</h3>
		<pre>
		유형 : ${vo.searchFcltKndCd }
		용도 : ${vo.searchFcltUsedTyCd }
		상태 : ${vo.searchFcltSttus }
		</pre>
	</div>
	<br>
	<h3>검색결과</h3>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>시설물명</th>
				<th>유형</th>
				<th>용도</th>
				<th>상태</th>
				<th>주소</th>
				<th>UID</th>
				<th>중계서버UID</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="excelList">
				<tr>
					<td>${excelList.fcltId}</td>
					<td>${excelList.fcltLblNm}</td>
					<td>${excelList.fcltKndNm}</td>
					<td>${excelList.fcltUsedTyNm}</td>
					<td>${excelList.fcltSttusNm}</td>
					<td>${excelList.addr}</td>
					<td>${excelList.fcltUid}</td>
					<td>${excelList.linkVmsUid}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form:form>