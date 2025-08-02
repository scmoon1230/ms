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
<script src="<c:url value='/js/fin/finmng.js'/>"></script>
<script>
	let currentDay = "${currentDay}";
</script>
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
					<li class="active">입출관리 > 금융자산관리 > 금융자산정보</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>금융자산정보</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
							<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${acctgbList}" var="val">
										<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>자산</th>
							<td><select name="" id="assetGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="1">정기예적금</option>
									<option value="2">일반예금</option>
									<option value="3">대여금</option>
									<option value="9">기타</option>
								</select>
							</td>
							<th>자산명</th>
							<td><input type="text" name="" id="assetName" class="txtType searchEvt" style="ime-mode:active">
							</td>
							<th>계좌번호</th>
							<td><input type="text" name="" id="accountNo" class="txtType searchEvt" style="ime-mode:active">
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
				<div class="tit"><h4>자산정보 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>자산정보 상세</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>자산코드</th>		<td id="dAssetCode"></td>
									<th>자산명</th>		<td id="dAssetName"></td>
								</tr>
								<tr><th>재정</th>			<td id="dAcctGb"></td>
									<th>자산구분</th>		<td id="dAssetGb"></td>
								</tr>
								<tr><th>은행</th>			<td id="dBankName"></td>
									<th>계좌번호</th>		<td id="dAccountNo"></td>
								</tr>
								<tr><th>발행일</th>		<td id="dIssueYmd"></td>
									<th>만기일</th>		<td id="dMtyYmd"></td>
								</tr>
								<tr><th>비고</th>			<td id="dRemark" colspan="3"></td>
								</tr>
								<tr><th>사용여부</th>		<td id="dUseYn" colspan="3"></td>
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
			<%--<div class="tit"><h4>자산정보 <span id="modetitle">수정</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>자산정보 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>자산코드</th>
									<td><input type="text" id="uAssetCode" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
									<th>자산명</th>
									<td><input type="text" id="uAssetName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>재정</th>
									<td><select name="" id="uAcctGb" class="selectType1" maxlength="1">
											<c:forEach items="${acctgbList}" var="val">
												<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>자산구분</th>
									<td><select name="" id="uAssetGb" class="selectType1" maxlength="1">
											<option value="1">정기예적금</option>
											<option value="2">일반예금</option>
											<option value="3">대여금</option>
											<option value="9">기타</option>
										</select>
									</td>
								</tr>
								<tr><th>은행</th>	
									<td><input type="text" id="uBankName" class="txtType" maxlength="200"/></td>
									<th>계좌번호</th>
									<td><input type="text" id="uAccountNo" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>발행일</th>
									<td><input type="date" id="uIssueYmd" class="date8Type" value=""
										required="required" user-title="발행일">
									</td>
									<th>만기일</th>
									<td><input type="date" id="uMtyYmd" class="date8Type" value=""
										required="required" user-title="발행일">
									 </td>
								</tr>
								<tr><th>비고</th>
									<td colspan="3"><input type="text" id="uRemark" class="txtType" style="width: 540px;" maxlength="200"/></td>
								</tr>
								<tr><th>사용여부</th>
									<td colspan="3">
										<select name="" id="uUseYn" class="selectType1" maxlength="1">
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
			<%--<div class="tit"><h4>자산정보 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>자산정보 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>자산코드</th>
									<td><input type="text" id="iAssetCode" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
									<th>자산명</th>
									<td><input type="text" id="iAssetName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>재정</th>
									<td><select name="" id="iAcctGb" class="selectType1" maxlength="1">
											<c:forEach items="${acctgbList}" var="val">
												<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>자산구분</th>
									<td><select name="" id="iAssetGb" class="selectType1" maxlength="1">
											<option value="1">정기예적금</option>
											<option value="2">일반예금</option>
											<option value="3">대여금</option>
											<option value="9">기타</option>
										</select>
									</td>
								</tr>
								<tr><th>은행</th>	
									<td><input type="text" id="iBankName" class="txtType" maxlength="200"/></td>
									<th>계좌번호</th>
									<td><input type="text" id="iAccountNo" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>발행일</th>
									<td><input type="date" id="iIssueYmd" class="date8Type" value=""
										required="required" user-title="발행일">
									</td>
									<th>만기일</th>
									<td><input type="date" id="iMtyYmd" class="date8Type" value=""
										required="required" user-title="발행일">
									 </td>
								</tr>
								<tr><th>비고</th>
									<td colspan="3"><input type="text" id="iRemark" class="txtType" style="width: 540px;" maxlength="200"/></td>
								</tr>
								<tr><th>사용여부</th>
									<td colspan="3">
										<select name="" id="iUseYn" class="selectType1" maxlength="1">
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