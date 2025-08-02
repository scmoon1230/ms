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
<script src="<c:url value='/js/money/moneymng.js'/>"></script>
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
					<li class="active">헌금관리 > 헌금등록 > 헌금입력</li>
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
									required="required" user-title="기준일자" onchange="changeUserId();$('.btnS').click()">
							</td>
							<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="changeMoneyCode();$('.btnS').click();">
									<option value="">전체</option>
									<c:forEach items="${AcctgbList}" var="val">
										<option value="${val.acctGb}" ${'999' == val.acctGb ? 'selected' : ''} ><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>헌금분류</th>
							<td><select name="" id="moneyCode" class="selectType1" maxlength="1" onchange="checkMoneyCode();$('.btnS').click();">
									<option value="">전체</option>
									<c:forEach items="${moneyList}" var="val">
										<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
									</c:forEach>
								</select>
							</td>
							<th>예배</th>
							<td><select name="" id="worshipCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${worshipList}" var="val">
										<option value="${val.worshipCode}">${val.worshipCode} - ${val.worshipName}</option>
									</c:forEach>
								</select>
							</td>
							<th>등록자</th>
							<td><select name="" id="userId" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>신도번호</th>
							<td><input type="text" name="" id="memberNo" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>신도성명</th>
							<td><input type="text" name="" id="memberName" class="txtType searchEvt" style="ime-mode:active"></td>
                            <th>등록일시</th>
							<td colspan="4">
								<input type="datetime-local" id="startYmd" class="date12Type" value=""
									required="required" user-title="시작일자" onchange="changeUserId();$('.btnS').click()">
								~
								<input type="datetime-local" id="endYmd" class="date12Type" value=""
									required="required" user-title="종료일자" onchange="changeUserId();$('.btnS').click()">
							</td>
						<!--<td><input type="checkbox" id="userIdYn" onchange="$('.btnS').click()">내가등록한 자료만</td>-->
							<td>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
								<a href="javascript:;" class="btn btnRight btnExcel">엑셀</a>
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
						<a href="<c:url value='/'/>images/attachFile/sample_money_001.xlsx" title='십일조 업로드 샘플파일 001'>[십일조샘플]</a>
						<a href="<c:url value='/'/>images/attachFile/sample_money_002.xlsx" title='십일조 업로드 샘플파일 002'>[일반감사샘플]</a>
						<a href="<c:url value='/'/>images/attachFile/sample_money_004.xlsx" title='십일조 업로드 샘플파일 004'>[선교샘플]</a>
						<a href="<c:url value='/'/>images/attachFile/sample_money_006.xlsx" title='주일헌금 업로드 샘플파일 006'>[주일샘플]</a>
						<a href="<c:url value='/'/>images/attachFile/sample_money_049.xlsx" title='교육부헌금 업로드 샘플파일 049'>[교육부샘플]</a>
						<!-- 파일: -->
						<input type="file" id="excelFile" class="txtType filePath" style="display: none" />
						<input type="text" id="excelFileText" class="txtType filePath" readonly="readonly" />
						<a href="javascript:;" class="btn btnExcelFile">찾기</a>
						<a href="#" class="btn btnDt btnUpload"
							user-url="<c:url value='/'/>money/mng/upload.excel"
							user-grid-id="grid" user-ref-id="excelFile" user-ext=".xls,.xlsx"
							user-validate="money">업로드</a>
						<a href="#" class="btn btnMultiDe">삭제</a>
						<a href="#" class="btn btnMultiMd">수정</a>
						<a href="#" class="btn btnRgt">등록</a>
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
								<tr><th>신도번호</th>		<td id="dMemberNo"></td>
									<th>신도아이디</th>	<td id="dMemberId"></td>
								</tr>
								<tr><th>신도성명</th>		<td id="dMemberName"></td>
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
				<div class="tit"><h4>헌금 수정</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>헌금 수정</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>기준일자</th>
									<!-- <td id="uStanYmd"></td> -->
									<td><!-- <input type="date" id="nStanYmd" class="date8Type"> -->
										<input type="text" id="nStanYmd" readonly>
									</td>
									<th>헌금분류</th>
									<td><select name="" id="nMoneyCode" class="selectType1" maxlength="1">
											<option value="">선택</option>
											<c:forEach items="${moneyList}" var="val">
												<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>순번</th>	
									<td id="uDetSeq"></td>
									<th>예배</th>	
									<td><select name="" id="uWorshipCode" class="selectType1" maxlength="1">
											<option value="">선택</option>
											<c:forEach items="${worshipList}" var="val">
												<option value="${val.worshipCode}">${val.worshipCode} - ${val.worshipName}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>신도번호</th>
									<td><input type="text" name="" id="uMemberNo" class="txtType searchMem2" style="ime-mode:active;" onblur="checkUMemberNo();">
										<a href="javascript:searchMemberInfo2();" class="btn btnRight">검색</a>
										<!-- <a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a> -->
									</td>
									<th>신도아이디</th>
									<td id="uMemberId"></td>
								</tr>
								<tr><th>신도성명</th>
									<td><input type="text" name="" id="uMemberName" class="txtType" style="ime-mode:active;"></td>
									<th>금액</th>	
									<td><input type="text" name="" id="uMoneyAmt" class="txtType" style="ime-mode:active;text-align: right;"></td>
								</tr>
								<tr style="display:none;">
									<td><input type="hidden" name="" id="uStanYmd">
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

			<!-- 레이어팝업 다중수정 -->
			<div class="layer layerModifyMulti" id="div_drag_4">
				<div class="tit"><h4>헌금 수정</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>헌금 수정</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>헌금분류</th>
									<td><select name="" id="uiMoneyCode" class="selectType1" maxlength="1">
											<option value="">선택</option>
											<c:forEach items="${moneyList}" var="val">
												<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
											</c:forEach>
										</select>
									</td>
									<th>예배</th>
									<td><select name="" id="uiWorshipCode" class="selectType1" maxlength="1">
											<option value="">선택</option>
											<c:forEach items="${worshipList}" var="val">
												<option value="${val.worshipCode}">${val.worshipCode} - ${val.worshipName}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnSvEcMulti" id="btnModReg">저장</a>
						<a href="#" class="btn btnC">닫기</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 다중수정 -->

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
								<col style="width: 170px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>헌금</th>
									<td><select name="" id="iMoneyCode" class="selectType1" maxlength="1">
											<option value="">선택</option>
											<c:forEach items="${moneyList}" var="val">
												<option value="${val.moneyCode}">${val.moneyCode} - ${val.moneyName}</option>
											</c:forEach>
										</select>
									</td>
									<th>예배</th>
									<td><select name="" id="iWorshipCode" class="selectType1" maxlength="1">
											<option value="">선택</option>
											<c:forEach items="${worshipList}" var="val">
												<option value="${val.worshipCode}">${val.worshipCode} - ${val.worshipName}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>금액자동처리</th>
									<td><input type="checkbox" id="autoYn" />
										<select name="" id="autoMoney" class="selectType50" maxlength="1">
											<%--<option value="">전체</option>--%>
											<option value="1000" selected >1,000</option>
											<option value="10000">10,000</option>
											<option value="30000">30,000</option>
											<option value="50000">50,000</option>
										</select>
										<input type="radio" id="autoAct1" name="autoAct" checked >입력
										<input type="radio" id="autoAct2" name="autoAct">곱하기
									</td>
									<th>신도번호없는 헌금 입력</th>
									<td><input type="checkbox" id="memberNoYn" onchange="changeMemberNoYn()" /></td>
								</tr>
							<%--<tr><th>신도번호중복체크안함</th>
									<td><input type="checkbox" id="duplYn" />
										<select name="" id="duplMoney" class="selectType50" maxlength="1">
											<option value="10000" selected >10,000</option>
											<option value="30000">30,000</option>
											<option value="50000">50,000</option>
											<option value="100000">100,000</option>
										</select>
										이하
									</td>
									<th>헌금분류/예배순서 고정</th>
									<td><input type="checkbox" id="optionFixYn" /></td>
									<th>저장후 닫지 않기</th>
									<td><input type="checkbox" id="closeWindowYn" /></td>
								</tr>--%>
							</tbody>
						</table>
						<table>
							<caption>헌금 등록</caption>
							<colgroup>
								<col style="width: 200px;" />
								<col style="width: *" />
								<col style="width: 200px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th style="text-align:center;">신도번호</th>
									<th style="text-align:center;">신도성명</th>
									<th style="text-align:center;">금액</th>
									<th style="text-align:center;">신도정보</th>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_0" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_0"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_0" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_0"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_0"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_1" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_1"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_1" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_1"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_1"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_2" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_2"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_2" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_2"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_2"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_3" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_3"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_3" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_3"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_3"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_4" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_4"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_4" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_4"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_4"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_5" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_5"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_5" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_5"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_5"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_6" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_6"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_6" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_6"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_6"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_7" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_7"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_7" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_7"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_7"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_8" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_8"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_8" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_8"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_8"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_9" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_9"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_9" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_9"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_9"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_10" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_10"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_10" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_10"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_10"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_11" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_11"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_11" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_11"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_11"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_12" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_12"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_12" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_12"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_12"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_13" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_13"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_13" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_13"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_13"></span></td>
								</tr>
								<tr><td><input type="hidden" name="" id="iMemberId_14" class="txtType" style="ime-mode:active">
										<input type="text" name="" id="iMemberNo_14"   class="txtType searchMem" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMemberName_14" class="txtType" style="ime-mode:active"></td>
									<td><input type="text" name="" id="iMoneyAmt_14"   class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td><span id="iMemberInfo_14"></span></td>
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
			
			<!-- 신도선택 -->
			<div class="layerPop layerPopSearch" id="">
				<div class="layerCt">
					<div class="tableType1" style="height: 500px;">
						<table id="grid_member_popup" style="width:100%">
						</table>
					</div>
				   <div class="btnCtr">
					<%--<a href="#" class="btn btnSvEc">저장</a>--%>
						<a href="#" class="btn btnPopApply">적용</a>
						<a href="#" class="btn btnPopC">닫기</a>
					</div>
				</div>
			</div>
			<!-- //신도선택 -->
			
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>