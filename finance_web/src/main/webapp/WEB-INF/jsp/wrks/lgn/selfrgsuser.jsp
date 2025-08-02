
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>사용자 등록 신청</title>
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
			<div style="height: 10px;"></div>
			<div class="content">
				<div class="titArea">
					<h3 class="tit">사용자 등록 신청</h3>
	 				<div class="inputText firstInput">
						<p>● 사용자가 직접 사용자정보를 등록하는 화면입니다.</p>
						<%--<p>● 다수 사용자등록시 사용자관리 권한이 있는 관리자가 로그인후 사용자관리 메뉴를 이용하시기 바랍니다.</p>--%>
						<p>● 사용자 등록후 정상승인 처리되면 이용 가능합니다.</p>
						<p>● 긴급승인요청시 대표번호(<span><b><c:out value="${repTelNo}"/></b></span>)로 연락하시기 바랍니다.</p>
					</div>
	 			<%--<div class="detailHeaderText">
						<p id="onlyDetailHeaderText">● 등록(수정) 완료되었습니다.</p>
						<p>● 승인처리후 이용 가능합니다.</p>
						<p>● 긴급승인요청시 대표번호 <span><b><c:out value="${repTelNo}"/></b></span> 으로 연락하시기 바랍니다.</p>
					</div>--%>
				</div>
					<div class="layerCt">
						<div class="tableTypeFree">
							<!-- <input type="hidden" id="iDstrtCd" value="${dstrtCd}"/> -->
							<table>
								<caption>사용자 등록 신청</caption>
								<tbody>
								<tr>
									<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>아이디</th>
									<td><input type="text" name="" id="iUserId" class="txtType" maxlength="40" required="required"
										user-title="아이디" user-data-type="id" user-data-min="5" user-data-max="20" onkeydown="removeKoreanChar(this);" style="ime-mode:disabled;"/>
										<a href="#" class="btn" id="btn-check-user-id" onclick="checkUserId();">중복검사</a>
									</td>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용자명</th>
									<td><input type="text" name="" id="iUserNmKo" class="txtType" maxlength="100" required="required"
										user-title="사용자명" style="ime-mode:active"/></td>
								</tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>비밀번호</th>
									<td><input type="password" name="" id="iPassword" class="txtType" maxlength="64" required=""
										user-title="비밀번호" user-data-type="password" user-data-min="8" user-data-max="20"/></td>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>비밀번호 확인</th>
									<td><input type="password" name="" id="iPasswordCk" class="txtType" maxlength="64" required=""
										user-title="비밀번호 확인"  user-data-type="password"/></td>
								</tr>
								<tr id="ckPs" style='display:none; color:red'><td></td><td colspan='3'><b>* 비밀번호가 일치하지 않습니다. 다시 확인해 주세요</b></td></tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>휴대전화</th>
									<td><select name="" id="iMoblNo1" class="selectType1">
											<option value="">== 선택 ==</option>
											<option value="010">010</option>
											<option value="011">011</option>
											<option value="016">016</option>
											<option value="017">017</option>
										</select>
										- <input type="text" name="" id="iMoblNo2" class="date8Type" maxlength="4"/>
										- <input type="text" name="" id="iMoblNo3" class="date8Type" maxlength="4"/>
									</td>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>이메일</th>
									<td><input type="text" name="" id="iEmail1" class="txtType" maxlength="50"/>
										&nbsp;&nbsp; @ &nbsp;&nbsp;
										<input type="text" name="" id="iEmail2" class="txtType" maxlength="40"/>
									</td>
								</tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>기관명</th>
									<td>
										<!-- <input type="text" name="" id="iInsttNm" class="txtType" maxlength="100" style="ime-mode:active"/> -->
										<select class="selectType1" id="iInsttCd" name="iInsttCd" title="기관명">
											<option value="">선택</option>
											<c:forEach items="${userInsttList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}"></c:out></option>
											</c:forEach>
										</select>
										<input type="text" name="" id="iInsttNmInput" class="txtType" maxlength="100" style="ime-mode:active"
											placeholder="직접입력" title="직접입력"/>
									</td>
									<th>부서명</th>
									<td><input type="text" name="" id="iDeptNm" class="txtType" maxlength="100" style="ime-mode:active"/></td>
								</tr>
								<tr>
									<th>직급명</th>
									<td><input type="text" name="" id="iRankNm" class="txtType" maxlength="100" style="ime-mode:active"/></td>
									<th>담당업무</th>
									<td><input type="text" name="" id="iRpsbWork" class="txtType" maxlength="100" style="ime-mode:active"/></td>
								</tr>
								<tr>
									<th>사무실전화</th>
									<td colspan="3" id="iOffcTelNo">
										<input type="text" class="date8Type" id="iOffcTelNo1" name="iOffcTelNo1" user-data-type="number" maxlength="3"/>
										- <input type="text" class="date8Type" id="iOffcTelNo2" name="iOffcTelNo2" user-data-type="number" maxlength="4"/>
										- <input type="text" class="date8Type" id="iOffcTelNo3" name="iOffcTelNo3" user-data-type="number" maxlength="4"/>
									</td>
									<!-- 
									<th>사용유형</th>
									<td><select name="" id="iUseTyCd" class="selectType1" maxlength="1">
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>	
									 -->
								</tr>
								<tr>
									<th>비고</th>
									<td colspan="3"><textarea class="textArea2" id="iRemark" maxlength="255" style="ime-mode:active"></textarea></td>
								</tr>
								</tbody>
							</table>
						</div>

						<!--등록된 사용자그룹/사용자지역 리스트
						<div class="boxWrap">
							<br>
							<div class="tbLeft50" style="width: 65%">
								<div class="tit">
									<h4><strong>사용자 그룹</strong></h4>
								</div>
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
						 -->
					</div>
					<br>
				<div class="btnWrap btnR">
					<a class="btn" onclick="window.close();">닫기</a>
					<a class="btn" onclick="insertAction();">신청</a>
			   </div>
			</div>
		<!-- //content -->
	</div>
	<!-- //container -->
<script src="${pageContext.request.contextPath}/js/wrks/lgn/v2/apply_v2.js"></script>
</div>
</body>
</html>

