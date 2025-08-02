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
<script src="<c:url value='/js/env/member.js'/>"></script>
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
					<li class="active">공통정보 > 신도정보</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>신도</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
							<th>신도아이디</th>
							<td><input type="text" name="" id="memberId" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>신도번호</th>
							<td><input type="text" name="" id="memberNo" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>신도명</th>
							<td><input type="text" name="" id="memberName" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>직분</th>
							<td><select name="" id="positionCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${positionList}" var="val">
										<option value="${val.positionCode}"><c:out value="${val.positionName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>교구</th>
							<td><select name="" id="deptCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${deptList}" var="val">
										<option value="${val.deptCode}"><c:out value="${val.deptName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>구역</th>
							<td><select name="" id="regionCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${regionList}" var="val">
										<option value="${val.regionCode}"><c:out value="${val.regionName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>사용여부</th>
							<td><select name="" id="useYn" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="Y" selected>사용</option>
									<option value="N">미사용</option>
								</select>
							</td>
							<td>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
								<a href="javascript:;" class="btn btnRight btnE">엑셀</a>
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
				<div class="tit"><h4>신도 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>신도 상세</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>신도아이디</th>			<td id="dMemberId"></td>
									<th>신도번호(생일)</th>	<td id="dMemberNo"></td>
								</tr>
								<tr><th>신도명</th>		<td id="dMemberName"></td>
									<th>핸드폰</th>		<td id="dHphoneNo"></td>
								</tr>
								<tr><th>직분</th>			<td id="dPositionCode"></td>
									<th>연락처</th>		<td id="dTelNo"></td>
								</tr>
								<tr><th>교구</th>			<td id="dDeptCode"></td>
									<th>구역</th>			<td id="dRegionCode"></td>
								</tr>
								<tr><th>성별</th>			<td id="dSexType"></td>
									<th>사용여부</th>		<td id="dUseYn"></td>
								</tr>
								<tr><th>가족사항</th>		<td id="dFamilyRemark" colspan="3"></td>
								</tr>
								<tr><th>주소</th>			<td id="dAddr" colspan="3"></td>
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

			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
				<div class="tit"><h4>신도 등록</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>신도 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>신도아이디</th>
									<td>[자동생성]</td>
									<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>신도번호(생일)</th>
									<td><input type="text" id="iMemberNo" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
								</tr>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>신도명</th>
									<td><input type="text" id="iMemberName" class="txtType" maxlength="200" style="ime-mode:active"/></td>
									<th>핸드폰</th>
									<td><input type="text" id="iHphoneNo" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>직분</th>
									<td><select name="" id="iPositionCode" class="selectType1" maxlength="1">
											<c:forEach items="${positionList}" var="val">
												<option value="${val.positionCode}"><c:out value="${val.positionName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>연락처</th>
									<td><input type="text" id="iTelNo" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>교구</th>
									<td><select name="" id="iDeptCode" class="selectType1" maxlength="1">
											<c:forEach items="${deptList}" var="val">
												<option value="${val.deptCode}"><c:out value="${val.deptName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>구역</th>
									<td><select name="" id="iRegionCode" class="selectType1" maxlength="1">
											<c:forEach items="${regionList}" var="val">
												<option value="${val.regionCode}"><c:out value="${val.regionName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>성별</th>
									<td><select name="" id="iSexType" class="selectType1" maxlength="1">
											<option value="남">남</option>
											<option value="여">여</option>
										</select>
									</td>
									<th>사용여부</th>
									<td><select name="" id="iUseYn" class="selectType1" maxlength="1">
											<option value="Y">사용</option>
											<option value="N">미사용</option>
										</select>
									</td>
								</tr>
								<tr><th>가족사항</th>
									<td colspan="3"><input type="text" id="iFamilyRemark" class="txtType" style="width:90%" maxlength="200"/></td>
								</tr>
								<tr><th>주소</th>
									<td colspan="3"><input type="text" id="iAddr" class="txtType" style="width:90%" maxlength="200"/></td>
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
			
			<!-- 레이어팝업 수정 -->
			<div class="layer layerModify" id="div_drag_3">
				<div class="tit"><h4>신도 수정</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>신도 수정</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>신도아이디</th>
									<td><input type="text" id="uMemberId" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
									<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>신도번호</th>
									<td><input type="text" id="uMemberNo" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
								</tr>
								<tr><th>신도명</th>
									<td><input type="text" id="uMemberName" class="txtType" maxlength="200"/></td>
									<th>핸드폰</th>
									<td><input type="text" id="uHphoneNo" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>직분</th>
									<td><select name="" id="uPositionCode" class="selectType1" maxlength="1">
											<c:forEach items="${positionList}" var="val">
												<option value="${val.positionCode}"><c:out value="${val.positionName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>연락처</th>
									<td><input type="text" id="uTelNo" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>교구</th>
									<td><select name="" id="uDeptCode" class="selectType1" maxlength="1">
											<c:forEach items="${deptList}" var="val">
												<option value="${val.deptCode}"><c:out value="${val.deptName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>구역</th>
									<td><select name="" id="uRegionCode" class="selectType1" maxlength="1">
											<c:forEach items="${regionList}" var="val">
												<option value="${val.regionCode}"><c:out value="${val.regionName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>성별</th>
									<td><select name="" id="uSexType" class="selectType1" maxlength="1">
											<option value="남">남</option>
											<option value="여">여</option>
										</select>
									</td>
									<th>사용여부</th>
									<td><select name="" id="uUseYn" class="selectType1" maxlength="1">
											<option value="Y">사용</option>
											<option value="N">미사용</option>
										</select>
									</td>
								</tr>
								<tr><th>가족사항</th>
									<td colspan="3"><input type="text" id="uFamilyRemark" class="txtType" style="width:90%" maxlength="200"/></td>
								</tr>
								<tr><th>주소</th>
									<td colspan="3"><input type="text" id="uAddr" class="txtType" style="width:90%" maxlength="200"/></td>
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
			
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>

<form id="dataForm" name="dataForm" method="post">
	<input type="hidden" name="paraMemberId"     id="paraMemberId"     value="">
	<input type="hidden" name="paraMemberNo"     id="paraMemberNo"     value="">
	<input type="hidden" name="paraMemberName"   id="paraMemberName"   value="">
	<input type="hidden" name="paraPositionCode" id="paraPositionCode" value="">
	<input type="hidden" name="paraDeptCode"     id="paraDeptCode"     value="">
	<input type="hidden" name="paraRegionCode"   id="paraRegionCode"   value="">
	<input type="hidden" name="paraUseYn"        id="paraUseYn"        value="">
	<input type="hidden" name="sidx" id="sidx" value="">
	<input type="hidden" name="sord" id="sord" value="">
	<input type="hidden" name="titleKey"    id="titleKey"    value="">
	<input type="hidden" name="titleHeader" id="titleHeader" value="">
</form>

</body>
</html>