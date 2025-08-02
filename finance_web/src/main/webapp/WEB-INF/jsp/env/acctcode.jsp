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
<script src="<c:url value='/js/env/acctcode.js'/>"></script>
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
					<li class="active">공통정보 > 계정과목</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>계정과목</caption>
						<colgroup>
							<col style="width: 100px;" />
							<col style="width: *" />
						</colgroup>
						<tbody>
						<tr>
	                        <th>회계년도</th>
	                        <td><select name="" id="stanYy" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
	                        		<option value="">전체</option>
								    <c:forEach items="${stanYyList}" var="val">
								        <option value="${val.stanYy}" ${1 == val.rk ? 'selected' : ''} ><c:out value="${val.stanYy}" ></c:out></option>
								    </c:forEach>
								</select>
	                        </td>
							<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${acctgbList}" var="val">
										<option value="${val.acctGb}" ${'1' == val.acctGb ? 'selected' : ''} ><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>구분</th>
							<td><select name="" id="acctLevel" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<option value="대">대</option>
									<option value="중">중</option>
									<option value="소">소</option>
								</select>
							</td>
							<th>계정명</th>
							<td><input type="text" name="" id="acctName" class="txtType searchEvt" style="ime-mode:active">
							</td>
							<td><a href="javascript:makeNextFromPrev();"     class="btn btnC">다음년도 계정 생성</a>
							</td>
							<td><a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
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
				<div class="tit"><h4>계정과목 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>계정과목 상세</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th>회계년도</th>		<td id="dStanYy"></td>
									<th>재정</th>			<td id="dAcctGb"></td>
								</tr>
								<tr><th>구분</th>			<td id="dAcctLevel"></td>
								</tr>
								<tr><th>상위계정</th>		<td id="dAcctUp" colspan="3"></td>
								</tr>
								<tr><th>계정코드</th>		<td id="dAcctCode"></td>
								</tr>
								<tr><th>계정명칭</th>		<td id="dAcctName"></td>
									<th>출력명칭</th>		<td id="dPrintName"></td>
								</tr>
								<tr><th>합산여부</th>		<td id="dSumYn"></td>
								</tr>
								<tr><th>수입/지출</th>		<td id="dInoutGb"></td>
									<th>수입계정유형</th>	<td id="dInType"></td>
								</tr>
								<tr><th>종교인연결계정</th>	<td id="dLinkAcctCode" colspan="3"></td>
								</tr>
								<tr><th>사용여부</th>		<td id="dUseYn"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnMd">수정</a>
						<!-- <a href="#" class="btn btnDe">삭제</a> -->
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

			<!-- 레이어팝업 등록 -->
			<div class="layer layerModify" id="div_drag_3">
			<%--<div class="tit"><h4>계정과목 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>계정과목 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>회계년도</th>
									<td><input type="number" id="uStanYy" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
									<th>재정</th>
									<td><select name="" id="uAcctGb" class="selectType1" maxlength="1">
											<c:forEach items="${acctgbList}" var="val">
												<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th>구분</th>
									<td><select name="" id="uAcctLevel" class="selectType1" maxlength="1">
											<option value="대">대분류</option>
											<option value="중">중분류</option>
											<option value="소">소분류</option>
										</select>
									</td>
								</tr>
								<tr><th><a href="javascript:clickSrchAcctCode();">상위계정</a></th>
									<td colspan="3"><input type="text" id="uAcctUp" class="txtType searchAcct" style="ime-mode:active" maxlength="200"/>
										&nbsp; <span id="uAcctUpInfo"></span>
									</td>
								</tr>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>계정코드</th>
									<td><input type="text" id="uAcctCode" class="txtType" maxlength="40" required="required" user-required="insert"/></td>
								</tr>
								<tr><th>계정명칭</th>
									<td><input type="text" id="uAcctName" class="txtType" maxlength="200"/></td>
									<th>출력명칭</th>
									<td><input type="text" id="uPrintName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>합산여부</th>
									<td><select name="" id="uSumYn" class="selectType1" maxlength="1">
											<option value="Y">합산계정</option>
											<option value="N">입력계정</option>
										</select>
									</td>
								</tr>
								<tr><th>수입/지출</th>
									<td><select name="" id="uInoutGb" class="selectType1" maxlength="1">
											<option value="수입">수입계정</option>
											<option value="지출">지출계정</option>
										</select>
									</td><th>수입계정유형</th>
									<td><select name="" id="uInType" class="selectType1" maxlength="1">
											<option value="1">일반헌금</option>
											<option value="2">재정지원</option>
											<option value="3">이자및기타</option>
										</select>
									</td>
									
								</tr>
								<tr><th>종교인연결계정</th>
									<td><input type="text" id="uLinkAcctCode" class="txtType" maxlength="200"/></td>
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
						<a href="#" class="btn btnSvEc" id="btnMod">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 등록 -->
			
			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
			<%--<div class="tit"><h4>계정과목 <span id="modetitle">추가</span></h4></div>--%>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>계정과목 등록</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: *" />
								<col style="width: 150px;" />
								<col style="width: *" />
							</colgroup>
							<tbody>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>회계년도</th>
									<td><input type="number" id="iStanYy" class="txtType" maxlength="4" required="required" user-required="insert"/></td>
									<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>재정</th>
									<td><select name="" id="iAcctGb" class="selectType1" maxlength="1">
											<c:forEach items="${acctgbList}" var="val">
												<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>구분</th>
									<td><select name="" id="iAcctLevel" class="selectType1" maxlength="1" onchange="checkAcctLevel();">
											<option value="대">대분류</option>
											<option value="중">중분류</option>
											<option value="소">소분류</option>
										</select>
									</td>
								</tr>
								<tr><th><a href="javascript:clickSrchAcctCode();">상위계정</a></th>
									<td colspan="3"><input type="text" id="iAcctUp" class="txtType searchAcct" style="ime-mode:active" maxlength="200"/>
										&nbsp; <span id="iAcctUpInfo"></span>
									</td>
								</tr>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>계정코드</th>
									<td><input type="text" id="iAcctCode" class="txtType" maxlength="40" required="required" user-required="insert"/>
										<a href="javascript:checkAcctCode();" class="btn">중복검사</a>
									</td>
								</tr>
								<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>계정명칭</th>
									<td><input type="text" id="iAcctName" class="txtType" maxlength="200"/></td>
									<th>출력명칭</th>
									<td><input type="text" id="iPrintName" class="txtType" maxlength="200"/></td>
								</tr>
								<tr><th>합산여부</th>
									<td><select name="" id="iSumYn" class="selectType1" maxlength="1">
											<option value="Y">합산계정</option>
											<option value="N">입력계정</option>
										</select>
									</td>
								</tr>
								<tr><th>수입/지출</th>
									<td><select name="" id="iInoutGb" class="selectType1" maxlength="1">
											<option value="수입">수입계정</option>
											<option value="지출">지출계정</option>
										</select>
									</td>
									<th>수입계정유형</th>
									<td><select name="" id="iInType" class="selectType1" maxlength="1">
											<option value="1">일반헌금</option>
											<option value="2">재정지원</option>
											<option value="3">이자및기타</option>
										</select>
									</td>
								</tr>
								<tr><th>종교인연결계정</th>
									<td><input type="text" id="iLinkAcctCode" class="txtType" maxlength="200"/>
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
						<a href="#" class="btn btnSvEc" id="btnReg">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 등록 -->
			
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