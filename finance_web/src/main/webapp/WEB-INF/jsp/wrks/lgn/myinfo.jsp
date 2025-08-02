
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<style>
#wrapperPop {
	width: auto;
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
	
		$(".btnOk").bind("click",function(){
			window.close();
		});
	
	});
</script>
</head>
<body>
<div id="wrapperPop">
	<!-- topbar -->
	<%-- <%@include file="/WEB-INF/jsp/cmm/topMenuPop.jsp"%> --%>
	<!-- //topbar -->
	<!-- container -->
	<div class="container">

		<!-- content -->
			<!-- <div class="topArea"></div> -->
			<div style="height: 30px;"></div>
			<div class="content">
				<div class="titArea">
					<h3 class="tit">사용자 상세정보</h3>
				</div>
					<div class="layerCt">
						<div class="tableTypeWide">
							<input type="hidden" id="bUserId" value="${myinfo.userId}"/>
							<table>
								<caption>사용자 상세</caption>
								<tbody>
									<tr><th>아이디</th>		<td id="dUserId">${myinfo.userId}</td>
									<!--<th>비밀번호</th>		<td id="dPassword"></td>-->
										<th>사용자명</th>		<td id="dUserNmKo">${myinfo.userNmKo}</td>
									</tr>
									<tr><th>휴대전화</th>		<td id="dMoblNo">${myinfo.moblNo}</td>
										<th>이메일</th>		<td id="dEmail">${myinfo.email}</td>
									<!--<th>영문이름</th>		<td id="dUserNmEn">${myinfo.userNmEn}</td>-->
									</tr>
									<tr><th>기관명</th>		<td id="dInsttNm">${myinfo.insttNm}</td>
										<th>부서명</th>		<td id="dDeptNm">${myinfo.deptNm}</td>
									</tr>
									<tr><th>직급명</th>		<td id="dRankNm">${myinfo.rankNm}</td>
										<th>담당업무</th>		<td id="dRpsbWork">${myinfo.rpsbWork}</td>
									</tr>
									<tr><th>사무실전화</th>	<td id="dOffcTelNo">${myinfo.offcTelNo}</td>
										<th>사용유형</th>		<td id="dUseTyCd">${myinfo.useNm}</td>
									</tr>
								<!--<tr>
										<th>IP주소체계구분</th>		<td id="dIpTyCd">${myinfo.IP_TY_CD}</td>
										<th>IP주소입력구분</th>		<td id="dIpCd">${myinfo.IP_CD}</td>
										<th>IP주소</th>			<td id="dIpAdres">${myinfo.IP_ADRES}</td>
									</tr>-->
									<tr><th>비고</th>
										<td id="dRemark" colspan="5">${myinfo.remark}</td>
									</tr>
								</tbody>
							</table>
						</div>

						<!--등록된 사용자그룹/사용자지역 리스트 -->
						<div class="boxWrap">
							<br>
							<div class="tbLeft50" style="width: 65%">
								<div class="tit">
									<h4><strong>사용자 그룹</strong></h4>
								</div>
								<!-- 등록된 사용자그룹 리스트 -->
								<div class="tableType1 update">
									<table id="table_user_grp_detail" class="table">
										<thead>
											<tr>
												<th>그룹아이디</th>
												<th>그룹명</th>
												<th>그룹레벨</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>${myinfo.grpId}</td>
												<td>${myinfo.grpNmKo}</td>
												<td>${myinfo.authLvlNm}</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="tbLeft50" style="width: 35%">
								<!-- 등록된 사용자지구 리스트  -->
								<div class="tit">
									<h4><strong>사용자 지구</strong></h4>
								</div>
								<div class="tableType1 update">
									<table id="table_user_dstrt_detail" class="table">
										<thead>
											<tr>
												<th>지구코드</th>
												<th>지구명</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>${myinfo.dstrtCd}</td>
												<td>${myinfo.dstrtNm}</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
	
						</div>
					</div>
					<br>
				<div class="btnWrap btnR">
					<a href="javascript:;" class="btn btnDt btnOk">확인</a>
			   </div>
			</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>

