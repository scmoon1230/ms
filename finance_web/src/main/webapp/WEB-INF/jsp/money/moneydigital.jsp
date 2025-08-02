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
<script src="<c:url value='/js/money/moneydigital.js'/>"></script>
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
					<li class="active">헌금관리 > 헌금등록 > 디지털헌금바구니</li>
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
							<th>구분</th>
							<td><select name="" id="stanGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="1">일반</option>
									<option value="2">청년/대학</option>
									<!-- <option value="1" selected>일반</option> -->
								</select>
							</td>
							<!-- 
							<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${AcctgbList}" var="val">
										<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							 -->
							<th>신도생일</th>
							<td><input type="text" name="" id="birthDay" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>신도성명</th>
							<td><input type="text" name="" id="memberName" class="txtType searchEvt" style="ime-mode:active">
							</td>
							<th>등록여부</th>
							<td><select name="" id="closeYn" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="Y">처리</option>
									<option value="N">미처리</option>
								</select>
							</td>
							<td><a href="javascript:doDigital();"     class="btn btnC">헌금등록처리</a>
								<a href="javascript:cancelDigital();" class="btn btnC">헌금등록취소</a>
							</td>
							<td>
								<!-- <input type="checkbox" id="noClose" onchange="$('.btnS').click()"> 미처리 자료만 보기 -->
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
					<div class="btnWrap btnR" style="width:50%;">
						<!-- <span style="color:red;">빈 셀은 반드시 . 을 넣어야 합니다.</span> -->
						<a href="<c:url value='/'/>images/attachFile/sample_digital_2.xlsx" title='디지털헌금 업로드 샘플파일'>[디지털샘플]</a>
						<!-- 파일: -->
						<input type="file" id="excelFile" class="txtType filePath" style="display: none" />
						<input type="text" id="excelFileText" class="txtType filePath" readonly="readonly" />
						<a href="javascript:;" class="btn btnExcelFile">찾기</a>
						<a href="#" class="btn btnDt btnUpload"
							user-url="<c:url value='/'/>money/digital/upload.excel"
							user-grid-id="grid" user-ref-id="excelFile" user-ext=".xls,.xlsx"
							user-validate="digital">업로드</a>
					
						<a href="#" class="btn btnMultiDe">삭제</a>
					<%--<a href="#" class="btn btnRgt">등록</a>--%>
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
								<tr><th>신도아이디</th>		<td id="dMemberId"></td>
									<th>신도성명</th>		<td id="dMemberName"></td>
								</tr>
								<tr><th>생일</th>			<td id="dBirthDay"></td>
									<th>연락처</th>		<td id="dHpNo"></td>
								</tr>
								<tr><th>종류</th>			<td id="dMoneyName"></td>
									<th>금액</th>			<td id="dMoneyAmt"></td>
								</tr>
								<tr><th>기도제목</th>		<td id="dPrayTitle" colspan="3"></td>
								<tr><th>요청사항</th>		<td id="dEtcRemark" colspan="3"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
					<%--<a href="#" class="btn btnMd">수정</a>
						<a href="#" class="btn btnDe">삭제</a>--%>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
				<%--<div class="tit"><h4>헌금 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>헌금 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>헌금</th>
									<td><select name="" id="moneyCode" class="selectType1" maxlength="1">
											<option value="">전체</option>
											<c:forEach items="${moneyList}" var="val">
												<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
											</c:forEach>
										</select>
									</td>
									<th>예배</th>
									<td><select name="" id="worshipCode" class="selectType1" maxlength="1">
											<option value="">전체</option>
											<c:forEach items="${worshipList}" var="val">
												<option value="${val.worshipCode}">${val.worshipCode} - ${val.worshipName}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>신도번호없는 헌금 입력</th>
									<td><input type="checkbox" id="noMemberNoYn" /></td>
									<th>헌금분류/예배순서 고정</th>
									<td><input type="checkbox" id="optionFixYn" /></td>
								</tr>
								<tr>
									<th>금액자동처리</th>
									<td conspan="3"><input type="checkbox" id="autoYn" />
										<select name="" id="autoMoney" class="selectType50" maxlength="1">
											<%--<option value="">전체</option>--%>
											<option value="1000">1,000</option>
											<option value="10000">10,000</option>
											<option value="30000">30,000</option>
											<option value="50000">50,000</option>
										</select>
										<input type="radio" id="autoAct1" name="autoAct">입력
										<input type="radio" id="autoAct2" name="autoAct" checked >곱하기
									</td>
									<th>저장후 닫지 않기</th>
									<td><input type="checkbox" id="closeWindowYn" /></td>
								</tr>
								<tr>
									<th>신도번호중복체크안함</th>
									<td conspan="3"><input type="checkbox" id="duplYn" />
										<select name="" id="duplMoney" class="selectType50" maxlength="1">
											<%--<option value="">전체</option>--%>
											<option value="10000">10,000</option>
											<option value="30000">30,000</option>
											<option value="50000">50,000</option>
											<option value="100000">100,000</option>
										</select>
										이하
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