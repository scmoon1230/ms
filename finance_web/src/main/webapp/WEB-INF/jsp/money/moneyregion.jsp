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
<script src="<c:url value='/js/money/moneyregion.js'/>"></script>
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
					<li class="active">헌금관리 > 헌금등록 > 헌금입력(구역헌금)</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>헌금</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
                            <th>기준일자</th>
							<td><input type="date" id="stanYmd" class="date8Type" value="${currentDay}"
									required="required" user-title="기준일자" onchange="$('.btnS').click()">
							</td>
							<th>헌금</th>
							<td><select name="" id="moneyCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()" disabled>
									<option value="">전체</option>
									<c:forEach items="${moneyList}" var="val">
										<option value="${val.moneyCode}" ${'008' == val.moneyCode ? 'selected' : ''} >${val.moneyCode} - ${val.moneyName}</option>
									</c:forEach>
								</select>
							</td>
							<th>예배</th>
							<td><select name="" id="worshipCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()" disabled>
									<option value="">전체</option>
									<c:forEach items="${worshipList}" var="val">
										<option value="${val.worshipCode}" ${'005' == val.worshipCode ? 'selected' : ''} >${val.worshipCode} - ${val.worshipName}</option>
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
							<td>
								<input type="checkbox" id="userIdYn" onchange="$('.btnS').click()"> 내가등록한 자료만 보기
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
				<div class="tit">
					<h4 style="text-align:right; padding-right: 20px;">
						합계 : <span id="totalAmnt"></span> 원
					</h4>
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
				<div class="tit"><h4>헌금 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>헌금 상세</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>기준일자</th>		<td id="dStanYmd"></td>
									<th>헌금분류</th>		<td id="dMoneyName"></td>
								</tr>
								<tr><th>순번</th>			<td id="dDetSeq"></td>
									<th>예배</th>			<td id="dWorshipName"></td>
								</tr>
								<tr><th>교구</th>			<td id="dDeptCode"></td>
									<th>구역</th>			<td id="dRegionCode"></td>
								</tr>
								<tr><th>헌금자아이디</th>	<td id="dMemberId"></td>
									<th>금액</th>			<td id="dMoneyAmt"></td>
								</tr>
								<tr style="display:none;">
									<td><input type="text" name="" id="dMoneyCode"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnMd hide">수정</a>
						<a href="#" class="btn btnDe hide">삭제</a>
						<a href="#" class="btn btnC">닫기</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

			<!-- 레이어팝업 수정 -->
			<div class="layer layerModify" id="div_drag_3">
				<%--<div class="tit"><h4>헌금 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>헌금 수정</caption>
							<colgroup>
								<col style="width: 200px;" />
								<col style="width: *" />
								<col style="width: 200px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>기준일자</th>		<td id="uStanYmd"></td>
									<th>헌금분류</th>		<td id="uMoneyName"></td>
								</tr>
								<tr><th>순번</th>			<td id="uDetSeq"></td>
									<th>예배</th>			<td id="uWorshipName"></td>
								</tr>
								<tr><th>교구</th>			<td id="uDeptCode"></td>
									<th>구역</th>			<td id="uRegionCode"></td>
								</tr>
								<tr><th>헌금자아이디</th>	<td id="uMemberId"></td>
									<th>금액</th>
									<td><input type="text"   name="" id="uMoneyAmt" class="txtType" style="ime-mode:active;text-align: right;">
										<input type="hidden" name="" id="uMoneyCode">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnSvEc" id="btnMod">저장</a>
						<a href="#" class="btn btnC">닫기</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 수정 -->

			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
				<%--<div class="tit"><h4>헌금 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>헌금 등록</caption>
							<colgroup>
								<col style="width: 200px;" />
								<col style="width: *" />
								<col style="width: 200px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>헌금</th>
									<td><select name="" id="iMoneyCode" class="selectType1" maxlength="1"  disabled>
											<option value="">전체</option>
											<c:forEach items="${moneyList}" var="val">
												<option value="${val.moneyCode}" ${'008' == val.moneyCode ? 'selected' : ''} >${val.moneyCode} - ${val.moneyName}</option>
											</c:forEach>
										</select>
									</td>
									<th>예배</th>
									<td><select name="" id="iWorshipCode" class="selectType1" maxlength="1">
											<option value="">전체</option>
											<c:forEach items="${worshipList}" var="val">
												<option value="${val.worshipCode}" ${'005' == val.worshipCode ? 'selected' : ''} >${val.worshipCode} - ${val.worshipName}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>금액자동처리</th>
									<td colspan="3"><input type="checkbox" id="autoYn" checked />
										<select name="" id="autoMoney" class="selectType50" maxlength="1">
											<%--<option value="">전체</option>--%>
											<option value="1000" selected >1,000</option>
											<option value="10000">10,000</option>
											<option value="20000">20,000</option>
											<option value="30000">30,000</option>
											<option value="50000">50,000</option>
											<option value="100000">100,000</option>
										</select>
										<input type="radio" id="autoAct1" name="autoAct">입력
										<input type="radio" id="autoAct2" name="autoAct" checked >곱하기
									</td>
								<%--<th>저장후 닫지 않기</th>
									<td><input type="checkbox" id="closeWindowYn" /></td>--%>
								</tr>
								<tr><th>교구/구역</th>
									<td><input type="text" name="" id="iDeptCode"   class="txtType" style="ime-mode:active;width: 50px;">
									  / <input type="text" name="" id="iRegionCode" class="txtType" style="ime-mode:active;width: 50px;">
									</td>
									<th>금액</th>
									<td><input type="text" name="" id="iMoneyAmt" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
								</tr>
							</tbody>
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnSvEc" id="btnReg">저장</a>
						<a href="#" class="btn btnC">닫기</a>
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