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
<title><prprts:value key="HD_TIT" /></title>
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script src="<c:url value='/js/wrks/sstm/code/dst.js' />"></script>

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
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>지구코드</caption>
						<tbody>
						<tr><th>지구코드</th>
							<td><input type="text" name="" id="sDstrtCd" class="txtType txtType100 searchEvt" style="ime-mode:active"></td>
							<th>지구명</th>
							<td><input type="text" name="" id="sDstrtNm" class="txtType txtType100 searchEvt" style="ime-mode:active"></td>
							<th>사용유형</th>
							<td><select name="" id="sUseTyCd" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<c:forEach items="${useGrpList}" var="val">
										<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
									</c:forEach>
								</select>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>

					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

				<div class="tableType1" style="height: 445px;">
					<table id="grid" style="width:100%">
					</table>
				</div>
				<div class="paginate">
				</div>
				<div class="btnWrap btnR">
					<a href="#" class="btn btnDt btnRgt">등록</a>
					<a href="#" class="btn btnMultiDe">삭제</a>
				</div>
			</div>

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>지구코드 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>지구코드 상세</caption>
							<tbody>
							<tr><th>지구코드</th>				<td id="dDstrtCd"></td>
								<th>지구명</th>				<td id="dDstrtNm"></td>
							</tr>
							<tr><th>대표연락처</th>			<td id="dRepTelNo"></td>
							</tr>
							<tr><th>영상재생지원배속</th>		<td id="dPlaybackSpeed"></td>
								<th>기본재생배속</th>			<td id="dBasicPlaybackSpeed"></td>
							</tr>
							<tr><th>link url</th>			<td id="dLinkUrl"></td>
								<th>vrs webrtc addr</th>	<td id="dVrsWebrtcAddr"></td>
							</tr>
							<tr>
								<th>지구유형</th>				<td id="dDstrtTy"></td>
								<th>사용유형</th>				<td id="dUseTyNm"></td>
							</tr>
							<tr><th>지구설명</th>
								<td id="dDstrtDscrt" colspan="3"></td>
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
				<div class="tit"><h4>지구코드 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
					<%-- <input type="hidden" id="dstrtCdBak" /> --%>
						<table>
							<caption>지구코드 등록</caption>
							<tbody>
							<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>지구코드</th>
								<td><input type="text" id="iDstrtCd" class="txtType" maxlength="40" /></td>
								<th>지구명</th>
								<td><input type="text" id="iDstrtNm" class="txtType" maxlength="100"/></td>
							</tr>
							<tr><th>대표연락처</th>
								<td><input type="text" id="iRepTelNo" class="txtType" maxlength="100"/></td>
							</tr>
							<tr><th>영상재생지원배속</th>
								<td><input type="text" id="iPlaybackSpeed" class="txtType" maxlength="100"/></td>
								<th>기본재생배속</th>
								<td><input type="text" id="iBasicPlaybackSpeed" class="txtType" maxlength="100"/></td>
							</tr>
							<tr><th>link url</th>
								<td><input type="text" id="iLinkUrl" class="txtType" maxlength="100"/></td>
								<th>vrs webrtc addr</th>
								<td><input type="text" id="iVrsWebrtcAddr" class="txtType" maxlength="100"/></td>
							</tr>
							<tr><th>지구유형</th>
								<td><select name="" id="iDstrtTy" class="selectType1">
										<option value="BASE"><c:out value="기초"></c:out></option>
										<option value="WIDE"><c:out value="광역"></c:out></option>
									</select>
								</td>
								<th>사용유형</th>
								<td><select name="" id="iUseTyCd" class="selectType1" maxlength="1">
										<c:forEach items="${useGrpList}" var="val">
											<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr><th>지구설명</th>
								<td colspan="3"><textarea class="textArea" id="iDstrtDscrt" maxlength="2000"></textarea></td>
							</tr>
							</tbody>
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnSv">저장</a>
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
<!-- footer -->
<!-- <div id="footwrap">
	<div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->
</body>
</html>