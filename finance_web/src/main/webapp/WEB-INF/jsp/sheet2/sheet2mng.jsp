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
<script>
	var type = '${type}';
	var sortBy = '';
	if ( '1' == '${type}' ) {
		sortBy = 'SEQ_NO';
	} else if ( '2' == '${type}' ) {
		sortBy = 'MONEY_AMT';
	}
</script>
<script src="<c:url value='/js/sheet2/sheet2mng.js'/>"></script>
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
					<li class="active">입출관리 > <span id="subtitle"></span></li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption></caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
                            <th>기준일자</th>
							<td><input type="date" id="stanYmd" class="date8Type" value="${currentDay}"
									required="required" user-title="기준일자" onchange="$('.btnS').click()">
								<span id="startYmd"></span> ~ <span id="endYmd"></span>
							</td>
						<!--<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${AcctgbList}" var="val">
										<c:if test="${val.acctGb == 'A'}">
											<option value="${val.acctGb}" ${'A' == val.acctGb ? 'selected' : ''} ><c:out value="${val.acctGbName}" ></c:out></option>
										</c:if>
									</c:forEach>
								</select>
							</td>-->
							<th>수입지출</th>
							<td><select name="" id="inoutGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="수입">수입</option>
									<option value="지출">지출</option>
								</select>
							</td>
						<c:if test="${LoginVO.userGb ne '일반'}">
						<!--<td><a href="javascript:doClose();"     class="btn btnC">전표마감처리</a>
								<a href="javascript:cancelClose();" class="btn btnC">전표마감취소</a>
							</td>-->
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
						합계 : <span id="totalAmnt"></span> 원
					</h4>
				</div>

				<div style="display: flex;">
					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>
					<div class="paginate">
					</div>
					<div class="btnWrap btnR">
					<%--<a href="#" class="btn btnDt btnRgt">등록</a>
						<a href="#" class="btn btnMultiDe">삭제</a>--%>
					</div>
				</div>
			</div>

			<!-- 레이어팝업 상세
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>전표 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>전표번호</th>	<td id="dSeqNo"></td>
									<th>계정과목</th>	<td id="dAcctInfo"></td>
								</tr>
								<tr><th>금액</th>		<td id="dMoneyAmt"></td>
									<th>내역</th>		<td id="dAcctRemark"></td>
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
			</div> -->
			<!-- //레이어팝업 상세 -->

			<!-- 레이어팝업 수정
			<div class="layer layerModify" id="div_drag_3">
				<div class="tit"><h4>전표 수정</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>전표번호</th>	<td id="uSeqNo"></td>
									<th>계정과목</th>	<td id="uAcctInfo"></td>
								</tr>
								<tr><th>금액</th>
									<td colspan="3"><input type="text" name="" id="uMoneyAmt" class="txtType" style="ime-mode:active;text-align: right;"></td>
								</tr>
								<tr><th>내역</th>
									<td colspan="3"><input type="text" name="" id="uAcctRemark" class="txtType" style="ime-mode:active;width:550px;"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnSvEc" id="btnMod">저장</a>
						<a href="#" class="btn btnC">닫기</a>
					</div>
				</div>
			</div> -->
			<!-- //레이어팝업 수정 -->

			<!-- 레이어팝업 등록
			<div class="layer layerRegister" id="div_drag_2">
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>전표 등록</caption>
							<colgroup>
								<col style="width: 200px;" />
								<col style="width: *" />
								<col style="width: 200px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>재정</th>
									<td><select name="" id="iAcctGb" class="selectType1" maxlength="1">
										<option value="">선택</option>
											<c:forEach items="${AcctgbList}" var="val">
												<c:if test="${val.acctGb != 'A'}">
													<option value="${val.acctGb}" ><c:out value="${val.acctGbName}" ></c:out></option>
												</c:if>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>계정과목</th>
									<td colspan="3">
										<input type="text" name="" id="iAcctCode" class="txtType searchAcct" style="ime-mode:active">
										&nbsp; <span id="iAcctInfo"></span>
									</td>
								</tr>
								<tr><th style="text-align:center;">금액</th>
									<th style="text-align:center;" colspan="3">내역</th>
								</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_0" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_0" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
									</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_1" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_1" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
								</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_2" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_2" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
								</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_3" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_3" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
								</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_4" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_4" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
								</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_5" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_5" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
									</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_6" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_6" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
								</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_7" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_7" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
								</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_8" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_8" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
								</tr>
								<tr><td><input type="text" name="" id="iMoneyAmt_9" class="txtType checkMoney" style="ime-mode:active;text-align: right;"></td>
									<td colspan="3"><input type="text" name="" id="iAcctRemark_9" class="txtType checkRem" style="ime-mode:active;width:550px;"></td>
								</tr>
							</tbody>
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnSvEc" id="btnReg">저장</a>
						<a href="#" class="btn btnC">닫기</a>
					</div>
				</div>
			</div> -->
			<!-- //레이어팝업 등록 -->
			
			<!-- 계정선택
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
			</div> -->
			<!-- //계정선택 -->
			
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>