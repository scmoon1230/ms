<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<table class="table table-bordered divInfo">
			<colgroup>
				<col span="1" style="width: 30%;">
				<col span="1" style="width: 70%;">
			</colgroup>
			<tbody>
				<tr><th>지구</th>				<td>${divFclt.dstrtNm}</td>				</tr>
				<tr><th>카메라타입</th>		<td>${divFclt.fcltKndDtlNm}</td>					</tr>
				<tr><th>좌표</th>				<td>${divFclt.pointX}<br/>${divFclt.pointY}</td>	</tr>
				<tr><th>중계VMS UID</th>		<td>${divFclt.linkVmsUid}</td>						</tr>
				<tr><th>사용유형</th>			<td>${divFclt.useTyNm}</td>							</tr>
				<%--
				<tr><th>대표시설물여부</th>		<td>${divFclt.prntFcltId == divFclt.fcltId ? '예' : '아니요'}</td>				</tr>
				<tr><th>설치일</th>			<td>${divFclt.fcltInstlYmd}</td>					</tr>
				<tr><th>관리자연락처</th>		<td>${divFclt.fcltMngrTelNo}</td>					</tr>
				<tr><th>IP</th>				<td>${divFclt.connIp}</td>							</tr>
				<tr><th>식별번호</th>			<td>${divFclt.fcltUid}</td>							</tr>
				<tr><th>비상벨유무</th>		<td>${divFclt.egbYn == 'Y' ? '유' : '무'}</td>		</tr>
				--%>
				<c:if test="${divFclt.fcltKndCd eq 'CTV'}">
				</c:if>
			</tbody>
		</table>
	</div>
</div>