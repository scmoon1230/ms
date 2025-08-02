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
<script src="<c:url value='/js/env/digitalmoney.js'/>"></script>
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
					<li class="active">공통정보 > 디지털헌금분류</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>디지털헌금분류</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
							<th>구분</th>
							<td><select name="" id="stanGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="1" selected>일반</option>
									<option value="2">청년/대학</option>
								</select>
							</td>
							<th>디지털헌금명</th>
							<td><input type="text" name="" id="moneyName" class="txtType searchEvt" style="ime-mode:active">
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
				<div class="tit"><h4>디지털헌금분류 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>디지털헌금분류 상세</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>구분</th>				<td id="dStanGb"></td>
								</tr>
								<tr><th>디지털헌금코드</th>		<td id="dMoneyType"></td>
									<th>디지털헌금명</th>		<td id="dMoneyName"></td>
								</tr>
								<tr><th>내부헌금코드</th>		<td id="dMoneyCode"></td>
									<th>재정시스템헌금명</th>	<td id="dOrigMoneyName"></td>
								</tr>
								<tr><th>사용여부</th>			<td id="dUseYn"></td>
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
			<%--<div class="tit"><h4>디지털헌금분류 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>디지털헌금분류 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>구분</th>
									<td><select name="" id="uStanGb" class="selectType1" maxlength="1">
											<option value="1">일반</option>
											<option value="2">청년/대학</option>
										</select>
									</td>
								</tr>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>디지털헌금코드</th>
									<td><input type="text" id="uMoneyType" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
									<th>디지털헌금명</th>
									<td><input type="text" id="uMoneyName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>내부헌금코드</th>
									<td></td>
									<th>재정시스템헌금명</th>
									<td><select name="" id="uMoneyCode" class="selectType1" maxlength="1">
											<c:forEach items="${moneyList}" var="val">
												<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>사용여부</th>
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
			<%--<div class="tit"><h4>디지털헌금분류 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>디지털헌금분류 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>구분</th>
									<td><select name="" id="iStanGb" class="selectType1" maxlength="1">
											<option value="1">일반</option>
											<option value="2">청년/대학</option>
										</select>
									</td>
								</tr>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>디지털헌금코드</th>
									<td><input type="text" id="iMoneyType" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
									<th>디지털헌금명</th>
									<td><input type="text" id="iMoneyName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>내부헌금코드</th>
									<td></td>
									<th>재정시스템헌금명</th>
									<td><select name="" id="iMoneyCode" class="selectType1" maxlength="1">
											<c:forEach items="${moneyList}" var="val">
												<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>사용여부</th>
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