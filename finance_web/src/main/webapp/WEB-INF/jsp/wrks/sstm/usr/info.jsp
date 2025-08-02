<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script src="<c:url value='/js/wrks/sstm/usr/info.js'/>"></script>

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
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>사용자</caption>
						<colgroup>
							<col style="width: 150px;" />
							<col style="width: *" />
							<col style="width: 150px;" />
							<col style="width: *" />
							<col style="width: 150px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr><th>아이디</th>
							<td><input type="text" name="" id="userId" class="txtType searchEvt" style="ime-mode:inactive"></td>
							<th>사용자명</th>
							<td><input type="text" name="" id="userName" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>사용유형</th>
							<td><select class="selectType1" name="" id="useYn" maxlength="1" onchange="$('.btnS').click()">
									<%--<option value="">전체</option>--%>
									<option value="Y">Y</option>
									<option value="N">N</option>
								</select>
								<a href="javascript: void(0);" class="btn btnRight btnS searchBtn">검색</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>
				
					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

				<div class="tableType1 userList">
					<table id="grid" style="width:100%">
					</table>
				</div>
				<div class="paginate">
				</div>
				<div class="btnWrap btnR">
					<a href="#" class="btn btnDt btnRgt">등록</a>
					<%--<a href="#" class="btn btnMultiDe">삭제</a>--%>
					
					<c:if test="${LoginVO.userId=='sys'}">	<!-- sys계정으로만 -->
						<a href="#" class="btn" onclick="crypto('ENC')">암호화</a>
						<a href="#" class="btn" onclick="crypto('DEC')">복호화</a>
						<c:if test="${LoginVO.gSysId=='PVEWIDE'}">	<!-- 광역일 때 -->
							<a href="#" class="btn btnCDe" onclick="sendUserInfoToBase()">사용자정보를 기초로 보내기</a>
						</c:if>
					</c:if>
				</div>
			</div>

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1" style="width:1000px">
				<div class="tit"><h4>사용자 상세</h4></div>
				<div class="layerCt">
					<div class="tableTypeWide">
						<table>
							<caption>사용자 상세</caption>
							<tbody>
								<tr><th>아이디</th>			<td id="dUserId"></td>
									<th>사용자명</th>			<td id="dUserName"></td>
								</tr>
								<tr><th>사용자구분</th>		<td id="dUserGb"></td>
									<th>연락처</th>			<td id="dTelNo"></td>
								</tr>
								<tr><th>재정구분</th>			<td id="dAcctGbName"></td>
									<th>직분</th>				<td id="dPositionName"></td>
								</tr>
								<tr><th>사용여부</th>			<td id="dUseYn"></td>
								</tr>
							</tbody>
						</table>
					</div>

					<!--등록된 사용자그룹/사용자지역 리스트
					<div class="boxWrap">
						<br>
						<div class="tbLeft50"  style="width:100%">
							<div class="tableType1 update" style="height:200px; overflow-y:scroll; overflow-x:hidden">
								<table id="grid_user_grp_detail" style="width:100%">
								</table>
							</div>
						</div>
					</div>
					 -->

					<div class="btnCtr">
						<a href="#" class="btn hide" id="btn-approve-user" onclick="approveUser();">사용승인처리</a>
						<a href="#" class="btn hide" id="btn-init-password" onclick="initPassword();">비밀번호초기화</a>
						<a href="#" class="btn hide btnMd" id="btn-modify-user">수정</a>
						<a href="#" class="btn hide btnDe" id="btn-remove-user">삭제</a>
						<a href="#" class="btn btnC">닫기</a>
						
						<c:if test="${LoginVO.userId=='sys'}">	<!-- sys계정으로만 -->
							<a href="#" class="btn btnCDe" onclick="deleteComplete()">완전삭제</a>
						</c:if>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

			<!-- //레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2" style="width:1000px">
				<div class="tit"><h4>사용자 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableTypeFree">
						<table>
							<caption>사용자 등록</caption>
							<tbody id="ind">
								<tr>
									<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>아이디</th>
									<td><input type="text" name="" id="iUserId" class="txtType" maxlength="40" required="required"
										user-title="아이디" user-data-type="id" user-data-min="5" user-data-max="20" onkeydown="removeKoreanChar(this);" style="ime-mode:disabled;"/>
										<a href="#" class="btn hide" id="btn-check-user-id" onclick="checkUserId();">중복검사</a>
									</td>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용자명</th>
									<td><input type="text" name="" id="iUserName" class="txtType" maxlength="100" required="required"
										user-title="사용자명" style="ime-mode:active"/></td>
								</tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>비밀번호</th>
									<td><input type="password" name="" id="iUserPwd" class="txtType" maxlength="64" required=""
										user-title="비밀번호" user-data-type="password" user-data-min="8" user-data-max="20"/></td>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>비밀번호 확인</th>
									<td><input type="password" name="" id="iUserPwdCk" class="txtType" maxlength="64" required=""
										user-title="비밀번호 확인"  user-data-type="password"/></td>
								</tr>
								<tr id="ckPs" class="text-danger" style="display: none;">
									<td></td>
									<td colspan='3'><b>* 비밀번호가 일치하지 않습니다. 다시 확인해 주세요</b></td>
								</tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>연락처</th>
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
									<th>이메일</th>
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
									<td id="iOffcTelNo">
										<input type="text" class="date8Type" id="iOffcTelNo1" name="iOffcTelNo1" user-data-type="number" maxlength="3"/>
										- <input type="text" class="date8Type" id="iOffcTelNo2" name="iOffcTelNo2" user-data-type="number" maxlength="4"/>
										- <input type="text" class="date8Type" id="iOffcTelNo3" name="iOffcTelNo3" user-data-type="number" maxlength="4"/>
									</td>
									<th>사용유형</th>
									<td><select name="" id="iUseTyCd" class="selectType1" maxlength="1">
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>	
								</tr>
								<tr>
									<th>비고</th>
									<td colspan="3"><textarea class="textArea2" id="iRemark" maxlength="255" style="ime-mode:active"></textarea></td>
								</tr>
								<tr>
									<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>관리자 비밀번호</th>
									<td colspan="3"><input type="password" class="txtType" id="adminPw" maxlength="64" required="required" user-title="관리자 비밀번호"/></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnSvEc" >저장</a>
						<a href="#" class="btn btnC">닫기</a>
					</div>
  					<div class="divLine1"></div>

					<!--사용자그룹/사용자지역 추가 입력모드 -->
					<div class="boxWrap">
						<div class="tbLeft50"  style="width:100%">
						 	<!-- 	사용자그룹 리스트 -->
							<div class="tableType1 insert" style="height:200px; overflow-y:scroll; overflow-x:hidden">
								<table id="grid_user_grp_list_insert" style="width:100%">
								</table>
							</div>
						</div>

					</div>
 					<!--사용자그룹/사용자지역 추가 수정모드 -->
					<div class="boxWrap">
						<div class="tbLeft50" style="width:100%">
							<!-- 등록된 사용자그룹 리스트 -->
							<div class="tableType1 update" style="height:200px; overflow-y:scroll; overflow-x:hidden">
								<table id="grid_user_grp_update" style="width:100%">
								</table>
							</div>
						<div class="btnCtr">
							<a href="#" class="btn btnAg">그룹추가</a>
							<a href="#" class="btn btnDg">그룹삭제</a>
						</div>
						</div>
					</div>
					
				</div>
			</div>
			<!-- //레이어팝업 등록 -->
			<!-- 레이어팝업 사용자그룹추가 -->
			<div class="layer layerRegisterGrp" id="div_drag_3" style="z-index:100; width:1000px">
				<div class="tit"><h4>사용자그룹 추가</h4></div>
				<div class="layerCt">
				 	<!-- 그룹 리스트 -->
					<div class="tableType1" style="height:600px; overflow-y:scroll; overflow-x:hidden">
						<table id="grid_user_grp_list_update" style="width:100%">
						</table>
					</div>
					<div class="btnCtr update">
						<a href="#" class="btn btnSvGrp">저장</a>
						<a href="#" class="btn btnCGrp">닫기</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 사용자그룹추가 -->

		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
<!-- footer -->
<!-- <div id="footwrap">
	<div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->
</body>
</html>