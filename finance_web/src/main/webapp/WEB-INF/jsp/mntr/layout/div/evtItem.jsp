<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<table class="table divInfo">
			<colgroup>
				<col style="width: 40%;">
				<col style="width: 60%;">
			</colgroup>
			<tbody>
				<c:if test="${divItem eq null}">
					<tr>
						<td class="danger" colspan="2" style="text-align: center;">세부항목이 없습니다.</td>
					</tr>
				</c:if>
				<c:if test="${divFclt.vtPointTelNo ne null || not empty divFclt.vtPointTelNo}">
					<tr>
						<th class="success">비상벨현장단말전화번호</th>
						<td>${divFclt.vtPointTelNo}</td>
					</tr>
				</c:if>
				<c:forEach items="${divItem}" var="row">
					<c:if test="${row.evtItemId ne 'ID_NO'}">
						<tr>
							<th class="success">${row.evtItemNm}</th>
							<td>${row.evtOcrItemDtl}</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>