<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<script src="<c:url value='/js/env/user.js'/>"></script>
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
				<ol class="breadcrumb">
					<li class="active">공통정보 > 사용자</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>사용자</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
							<th>사용자명</th>
							<td><input type="text" name="" id="userName" class="txtType searchEvt" style="ime-mode:active">
							</td>
							<th>직분</th>
							<td><select name="" id="positionCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${positionList}" var="val">
										<option value="${val.positionCode}"><c:out value="${val.positionName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${acctgbList}" var="val">
										<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>구분</th>
							<td><select name="" id="userGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="일반">일반</option>
									<option value="마감">마감</option>
									<option value="관리">관리</option>
								</select>
							</td>
							<th>사용여부</th>
							<td><select name="" id="useYn" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="Y" selected>사용</option>
									<option value="N">미사용</option>
								</select>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>

				<div class="tableType1" style="height: 500px;">
					<table id="grid" style="width:100%">
					</table>
				</div>

				<div style="display: flex;">
					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>
					<div class="paginate">
					</div>
					<div class="btnWrap btnR">
						<a href="#" class="btn btnDt btnRgt">등록</a>
						<%--<a href="#" class="btn btnMultiDe">삭제</a>--%>
					</div>
				</div>
			</div>

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>사용자 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>사용자 상세</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>사용자아이디</th>	<td id="dUserId"></td>
									<th>사용자명</th>		<td id="dUserName"></td>
								</tr>
								<tr><th>패스워드</th>		<td id=""></td>
									<th>연락처</th>		<td id="dTelNo"></td>
								</tr>
								<tr><th>직분</th>			<td id="dPositionCode"></td>
									<th>재정</th>			<td id="dAcctGb"></td>
								</tr>
								<tr><th>구분</th>			<td id="dUserGb"></td>
									<th>사용여부</th>		<td id="dUseYn"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnMd">수정</a>
						<a href="#" class="btn btnDe">삭제</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

			<!-- 레이어팝업 수정 -->
			<div class="layer layerModify" id="div_drag_3">
			<%--<div class="tit"><h4>사용자 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>사용자 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용자아이디</th>
									<td><input type="text" id="uUserId" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
									<th>사용자명</th>
									<td><input type="text" id="uUserName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>패스워드</th>
									<td><input type="password" id="uUserPwd" class="txtType" maxlength="200"/></td>
									<th>연락처</th>
									<td><input type="text" id="uTelNo" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>직분</th>
									<td><select name="" id="uPositionCode" class="selectType1" maxlength="1">
											<c:forEach items="${positionList}" var="val">
												<option value="${val.positionCode}"><c:out value="${val.positionName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>재정</th>
									<td><select name="" id="uAcctGb" class="selectType1" maxlength="1">
											<c:forEach items="${acctgbList}" var="val">
												<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>구분</th>
									<td><select name="" id="uUserGb" class="selectType1" maxlength="1">
											<option value="일반">일반</option>
											<option value="마감">마감</option>
											<option value="관리">관리</option>
										</select>
									</td>
									<th>사용여부</th>
									<td><select name="" id="uUseYn" class="selectType1" maxlength="1">
											<option value="Y">사용</option>
											<option value="N">미사용</option>
										</select>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnSvEc">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 수정 -->
			
			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
			<%--<div class="tit"><h4>사용자 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>사용자 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용자아이디</th>
									<td><input type="text" id="iUserId" class="txtType" maxlength="40" required="required" user-required="insert"/>
										<a href="#" class="btn hide" id="btn-check-user-id" onclick="checkUserId();">중복검사</a>
									</td>
									<th>사용자명</th>
									<td><input type="text" id="iUserName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>패스워드</th>
									<td><input type="password" id="iUserPwd" class="txtType" maxlength="200"/></td>
									<th>연락처</th>
									<td><input type="text" id="iTelNo" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>직분</th>
									<td><select name="" id="iPositionCode" class="selectType1" maxlength="1">
											<c:forEach items="${positionList}" var="val">
												<option value="${val.positionCode}"><c:out value="${val.positionName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>재정</th>
									<td><select name="" id="iAcctGb" class="selectType1" maxlength="1">
											<c:forEach items="${acctgbList}" var="val">
												<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>구분</th>
									<td><select name="" id="iUserGb" class="selectType1" maxlength="1">
											<option value="일반">일반</option>
											<option value="마감">마감</option>
											<option value="관리">관리</option>
										</select>
									</td>
									<th>사용여부</th>
									<td><select name="" id="iUseYn" class="selectType1" maxlength="1">
											<option value="Y">사용</option>
											<option value="N">미사용</option>
										</select>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnSvEc">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 등록 -->
			
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>