<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<article id="article-grid">
	<ol class="breadcrumb">
		<li>기초정보</li>
		<li>시설물</li>
		<li class="active">${common.title}</li>
	</ol>

	<div class="tit">
		<h4>${common.title}상세</h4>
	</div>

	<form:form commandName="fcltSrchVO" name="fcltSrchVO" method="post">
		<input type="hidden" id="fcltId" name="fcltId" value="${result.fcltId}" />
		<input type="hidden" id="fcltData" name="fcltData" data-point-x="${result.pointX}" data-point-y="${result.pointY}" />
		<div class="searchBox">
			<div class="form-inline">
			<%--<div class="form-group">
					<button type="button" class="btn btn-default btn-sm" onclick="oFcltDetail.upd();">수정</button>
				</div>
				<div class="form-group">
					<button type="button" class="btn btn-default btn-sm" onclick="oFcltDetail.del();">삭제</button>
				</div>--%>
				<div class="form-group">
					<button type="button" class="btn btn-default btn-sm" onclick="oFcltDetail.list();">목록</button>
				</div>
			</div>
		</div>
	</form:form>

	<table class="table table-bordered table-striped">
		<colgroup>
			<col span="1" style="width: 15%;">
			<col span="1" style="width: 35%;">
			<col span="1" style="width: 15%;">
			<col span="1" style="width: 35%;">
		</colgroup>
		<tr><th>시설물아이디</th>		<td>${result.fcltId}</td>
			<th>시설물명</th>			<td>${result.fcltLblNm}</td>
		</tr>
		<tr><th>유형(용도)</th>		<td>${result.fcltKndNm}(${result.fcltUsedTyNm})</td>
			<th>카메라타입</th>		<td>${result.fcltKndDtlNm}</td>
		</tr>
		<tr><th>설치장소</th>
			<td><span class="label label-default">도로</span>		${result.roadAdresNm}<br />
				<span class="label label-default">지번</span>		${result.lotnoAdresNm}
			</td>
			<th>지구</th>						<td>${result.dstrtNm}</td>
		</tr>
		<tr><th>좌표X</th>					<td>${result.pointX}</td>
			<th>좌표Y</th>					<td>${result.pointY}</td>
		</tr>
		<tr><th>고유식별번호</th>				<td>${result.fcltUid}</td>
			<th>중계VMS UID</th>				<td>${result.linkVmsUid}</td>
		</tr>
		<tr><th>영상저장여부</th>
			<td><c:if test="${result.recordingYn eq 'Y'}">영상저장함</c:if>
				<c:if test="${result.recordingYn eq 'N'}">영상저장안함</c:if>
			</td>
			<th>반출제외여부</th>
			<td><c:if test="${result.tvoTrgtYn eq 'Y'}">반출대상임</c:if>
				<c:if test="${result.tvoTrgtYn eq 'N'}">반출대상아님</c:if>
			</td>
		</tr>
		<tr><th>시스템명</th>					<td>${result.sysNm}</td>
			<th>관리번호</th>					<td>${result.mngSn}</td>
		</tr>
		<tr><th>상태</th>						<td>${result.fcltSttusNm}</td>
			<th>사용유형</th>					<td>${result.useTyNm}</td>
		</tr>
		<%--
		<tr>
			<th>대표시설물ID</th>				<td>${result.prntFcltId}</td>
		</tr>
		<tr><th>센터코드</th>								<td id="dCtrCd">${result.ctrNm}</td>
			<th><font color="red">*</font>시군구</th>		<td>${result.sigunguNm}</td>
		</tr>
		<tr><th>구역</th>									<td>${result.areaNm}</td>
			<th>경찰지구대</th>							<td>${result.plcPtrDivNm}</td>
		</tr>
		<tr><th>프리셋대역시작번호</th>						<td>${result.presetBdwStartNum}</td>
			<th>CCTV채널</th>								<td>${result.cctvChannel}</td>
		</tr>
		<tr><th>완제품구분</th>							<td>${result.cpltPrdtNm}</td>
		</tr>
		<tr><th>센터번호</th>								<td>${result.vtCenterTelNo}</td>
			<th>비상벨번호</th>							<td>${result.vtPointTelNo}</td>
		</tr>
		<tr><th>물품구분</th>								<td>${result.fcltGdsdtNm}</td>
			<th>관리자연락처</th>							<td>${result.fcltMngrTelNo}</td>
		</tr>
		<tr><th>접속IP</th>								<td>${result.connIp}</td>
			<th>접속포트</th>								<td>${result.connPort}</td>
		</tr>
		<tr><th>접속ID</th>								<td>${result.connId}</td>
			<th>접속비밀번호</th>							<td>${result.connPw}</td>
		</tr>
		<tr><th>시설물MAC주소</th>							<td>${result.fcltMacAdres}</td>
			<th>G/W</th>								<td>${result.gateWay}</td>
		</tr>
		<tr><th>SUBNET</th>								<td>${result.subnet}</td>
			<th>설치일</th>								<td>${result.fcltInstlYmd}</td>
		</tr>
		<tr><th>서버IP</th>								<td>${result.svrConnIp}</td>
			<th>서버포트</th>								<td>${result.svrConnPort}</td>
		</tr>
		<tr><th>서버접속ID</th>							<td>${result.svrConnId}</td>
			<th>서버접속비번</th>							<td>${result.svrConnPw}</td>
		</tr>
		<tr><th>장치확인IP</th>							<td>${result.dvcSeeCctvIp}</td>
			<th>장치확인포트</th>							<td>${result.dvcSeeCctvPort}</td>
		</tr>
		<tr><th>장치확인ID</th>							<td>${result.dvcSeeCctvId}</td>
			<th>장치확인비번</th>							<td>${result.dvcSeeCctvPw}</td>
		</tr>
		<tr><th>교통링크ID</th>							<td>${result.traLinkId}</td>
			<th>이전시설물ID</th>							<td>${result.beforeFcltId}</td>
		</tr>
		<tr><th>번호인식<br />CCTV여부</th>					<td>${result.lprCctvYn}</td>
			<th>아이콘GIS<br />표출여부</th>				<td>${result.iconGisDspYn}</td>
		</tr>
		<tr><th>기타</th>
			<td colspan="3">${result.etc}</td>
		</tr>
		--%>
	</table>
</article>
<script src="<c:url value='/js/mntr/fclt/fcltDetail.js' />"></script>
