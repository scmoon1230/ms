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
<script src="<c:url value='/js/fin/fintrans.js'/>"></script>
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
					<li class="active">입출관리 > 금융자산관리 > 금융자산거래</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>금융자산거래</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
							<th>기준일자</th>
							<td><input type="date" id="stanYmd" class="date8Type" value="${currentDay}"
									required="required" user-title="종료일자" onchange="$('.btnS').click()">
							</td>	 
							<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${acctgbList}" var="val">
										<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>입출구분</th>
							<td><select name="" id="inoutGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="I">입금</option>
									<option value="O">출금</option>
								</select>
							</td>
						<c:if test="${LoginVO.userGb ne '일반'}">
							<td><a href="javascript:doClose();"     class="btn btnC">금융자산거래마감처리</a>
								<a href="javascript:cancelClose();" class="btn btnC">금융자산거래마감취소</a>
							</td>
						</c:if>
							<td>
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
						입금합계 : <span id="inTotalAmnt"></span> 원 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						이자합계 : <span id="intTotalAmnt"></span> 원 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						출금합계 : <span id="outTotalAmnt"></span> 원
						<!-- &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 전체합계 : <span id="totalAmnt"></span> 원 -->
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
				<div class="tit"><h4>거래정보 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>거래정보 상세</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>기준일자</th>		<td id="dStanYmd"></td>
									<th>detSeq</th>		<td id="dDetSeq"></td>
								</tr>
								<tr><th>입출구분</th>		<td id="dInoutGb" colspan="3"></td>
								</tr>
								<tr><th>금융자산</th>		<td id="dAssetCode" colspan="3"></td>
								</tr>
								<tr><th>금액</th>			<td id="dMoneyAmt"></td>
									<th>이자액</th>		<td id="dIntAmt"></td>
								</tr>
								<tr><th>비고</th>			<td id="dRemark" colspan="3"></td>
								</tr>
								<tr><th>전표처리구분</th>	<td id="dAcctProcGb" colspan="3"></td>
								</tr>
								<tr><th>계정과목</th>		<td id="dAcctCode" colspan="3"></td>
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
			<%--<div class="tit"><h4>거래정보 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>거래정보 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>기준일자</th>
									<td><input type="date" id="uStanYmd" class="date8Type" value="" required="required" user-title="기준일자"></td>
									<th>detSeq</th>
									<td><input type="text" id="uDetSeq" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>입출구분</th>
									<td colspan="3">
										<select name="" id="uInoutGb" class="selectType1" maxlength="1">
											<option value="">선택</option>
											<option value="I">입금(예금)</option>
											<option value="O">출금(상환)</option>
										</select>
									</td>
								<%--<td colspan="2">
										<input type="checkbox" id="uNewYn" /> 신규개설
									</td>--%>
								</tr>
								<tr><th><a href="javascript:clickSrchAssetCode();">금융자산</a></th>
									<td><input type="text" name="" id="uAssetCode" class="txtType searchAsset" style="ime-mode:active">
										<!-- <br/><span id="uAssetInfo"></span> -->
									</td>
									<th>자산명</th>		<td><span id="uAssetName"></span></td>
								</tr>
								<tr><th>재정구분</th>		<td><span id="uAcctGbName"></span></td>
									<th>자산구분</th>		<td><span id="uAssetGbName"></span></td>
								</tr>
								<tr><th>은행</th>			<td><span id="uBankName"></span></td>
									<th>계좌</th>			<td><span id="uAccountNo"></span></td>
								</tr>
								<tr><th>개설일</th>		<td><span id="uIssueYmd"></span></td>
									<th>만기일</th>		<td><span id="uMtyYmd"></span></td>
								</tr>
								<tr><th>금액</th>	
									<td><input type="text" id="uMoneyAmt" class="txtType checkMoney" style="ime-mode:active;text-align: right;" maxlength="200"/></td>
									<th>이자액</th>
									<td><input type="text" id="uIntAmt" class="txtType checkMoney" style="ime-mode:active;text-align: right;" maxlength="200"/></td>
								</tr>
								<tr><th>비고</th>
									<td colspan="3"><input type="text" id="uRemark" class="txtType" style="width: 540px;" maxlength="200"/></td>
								</tr>
								<tr><th>전표처리구분</th>
									<td colspan="3">
										<select name="" id="uAcctProcGb" class="selectType1" maxlength="1">
											<option value="">선택</option>
											<option value="0">해당없음</option>
											<option value="1">원금</option>
											<option value="2">이자</option>
											<option value="3">원금/이자</option>
										</select>
									</td>
								</tr>
								<tr><th><a href="javascript:clickSrchAcctCode();">계정과목</a></th>
									<td colspan="3">
										<input type="text" name="" id="uAcctCode" class="txtType searchAcct" style="ime-mode:active">
										&nbsp; <span id="uAcctInfo"></span>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnSvEc" id="btnMod">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 수정 -->
			
			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
			<%--<div class="tit"><h4>거래정보 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>거래정보 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: 250px;" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>기준일자</th>
									<td colspan="3"><input type="date" id="iStanYmd" class="date8Type" value="" required="required" user-title="기준일자"></td>
								</tr>
								<tr><th>입출구분</th>
									<td colspan="3">
										<select name="" id="iInoutGb" class="selectType1" maxlength="1" onchange="checkInoutGb();">
											<!-- <option value="">선택</option> -->
											<option value="I">입금(예금)</option>
											<option value="O">출금(상환)</option>
										</select>
									</td>
								<%--<td colspan="2">
										<input type="checkbox" id="iNewYn" /> 신규개설
									</td>--%>
								</tr>
								<tr><th><a href="javascript:clickSrchAssetCode();">금융자산</a></th>
									<td><input type="text" name="" id="iAssetCode" class="txtType searchAsset" style="ime-mode:active">
										<!-- <br/><span id="iAssetInfo"></span> -->
									</td>
									<th>자산명</th>		<td><span id="iAssetName"></span></td>
								</tr>
								<tr><th>재정구분</th>		<td><span id="iAcctGbName"></span></td>
									<th>자산구분</th>		<td><span id="iAssetGbName"></span></td>
								</tr>
								<tr><th>은행</th>			<td><span id="iBankName"></span></td>
									<th>계좌</th>			<td><span id="iAccountNo"></span></td>
								</tr>
								<tr><th>개설일</th>		<td><span id="iIssueYmd"></span></td>
									<th>만기일</th>		<td><span id="iMtyYmd"></span></td>
								</tr>
								<tr><th>금액</th>	
									<td><input type="text" id="iMoneyAmt" class="txtType checkMoney" style="ime-mode:active;text-align: right;" maxlength="200"/></td>
									<th>이자액</th>
									<td><input type="text" id="iIntAmt" class="txtType checkMoney" style="ime-mode:active;text-align: right;" maxlength="200"/></td>
								</tr>
								<tr><th>비고</th>
									<td colspan="3"><input type="text" id="iRemark" class="txtType" style="width: 540px;" maxlength="200"/></td>
								</tr>
								<tr><th>전표처리구분</th>
									<td colspan="3">
										<select name="" id="iAcctProcGb" class="selectType1" maxlength="1" onchange="checkAcctProcGb();">
											<!-- <option value="">선택</option> -->
											<option value="0">해당없음</option>
											<option value="1">원금</option>
											<option value="2">이자</option>
											<!-- <option value="3">원금/이자</option> -->
										</select>
									</td>
								</tr>
								<tr><th><a href="javascript:clickSrchAcctCode();">계정과목</a></th>
									<td colspan="3">
										<input type="text" name="" id="iAcctCode" class="txtType searchAcct" style="ime-mode:active">
										&nbsp; <span id="iAcctInfo"></span>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnSvEc" id="btnReg">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 등록 -->
			
			<!-- 자산선택 -->
			<div class="layerPop layerPopSearch" id="div_assetcode">
				<div class="layerCt">
					<div class="tableType1" style="height: 500px;">
						<table id="grid_assetcode_popup" style="width:100%">
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnPopC">닫기</a>
					</div>
				</div>
			</div>
			<!-- //자산선택 -->
			
			<!-- 계정선택 -->
			<div class="layerPop layerPopSearch" id="div_acctcode">
				<div class="layerCt">
					<div class="tableType1" style="height: 500px;">
						<table id="grid_acctcode_popup" style="width:100%">
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnPopC">닫기</a>
					</div>
				</div>
			</div>
			<!-- //계정선택 -->
			
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>