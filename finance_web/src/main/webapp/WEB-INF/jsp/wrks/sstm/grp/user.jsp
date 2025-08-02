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

<style>
	.tableType1 {
		height: 80vh;
	}
	
	.tableType2 th {
		text-align: left;
	}

	.searchBox {
		margin: 0;
		position: fixed;
		bottom: 33px;
		right: 15px;
	}
</style>
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
				<div class="boxWrap">
					<div class="tbLeft40">
						<div class="tableTypeFree seachT">
							<table>
								<caption>사용자 등록</caption>
								<tbody>
								<tr><th>그룹아이디</th>
									<td><input type="text" name="" id="sGrpId" class="txtType grpNm searchEvt" style="ime-mode:active"></td>
									<th>그룹명</th>
									<td><input type="text" name="" id="sGrpNm" class="txtType grpNm searchEvt" style="ime-mode:active">
										<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
									</td>
								<%--<th>지구</th>
									<td><select name="" id="sDstrtCd" class="selectType1">
											<option value="">전체</option>
											<c:forEach items="${listCmDstrtCdMng}" var="val">
												<option value="${val.dstrtCd}"><c:out value="${val.dstrtNm}" ></c:out></option>
											</c:forEach>
										</select>
									</td>--%>
								</tr>
								</tbody>
							</table>
						</div><br/>
						<div class="tableType1">
							<table id="grid_group" style="width:100%">
							</table>
						</div>
					</div>
					<div class=tbRight60>
						<div class="tableTypeFree seachT">
							<table>
								<caption>사용자 등록</caption>
								<tbody>
								<tr>
								<!--<th>사용자명</th>
									<td><input type="text" name="" id="sUserNmKo" class="txtType grpNm searchEvt2" style="ime-mode:active">
									</td>-->
									<th>사용자아이디</th>
									<td><input type="text" name="" id="sUserId" class="txtType grpNm searchEvt2" style="ime-mode:active">
										<a href="javascript:;" class="btn btnRight btnS2 searchBtn2">검색</a>
									</td>
								</tr>
								</tbody>
							</table>
						</div><br/>
						<div class="tableType1">
							<table id="grid_user" style="width:100%">
							</table>
						</div>
					</div>
				</div>
			</div>

			 <!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>사용자 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>사용자 상세</caption>
							<tbody>
								<tr><th>아이디</th>			<td id="dUserId" colspan="3"></td>
									<!-- <th>비밀번호</th>
									<td id="dPassword"></td> -->
								</tr>
								<tr><th>한글이름</th>			<td id="dUserNmKo"></td>
									<th>영문이름</th>			<td id="dUserNmEn"></td>
								</tr>
								<tr><th>핸드폰번호</th>		<td id="dMoblNo" colspan="3"></td>
								</tr>
								<tr><th>이메일</th>			<td id="dEmail" colspan="3"></td>
								</tr>
								<tr><th>사무실전화번호</th>		<td id="dOffcTelNo" colspan="3"></td>
								</tr>
								<tr><th>사용유형</th>			<td id="dUseTyCd"></td>
									<th>기관명</th>			<td id="dInsttNm"></td>
								</tr>
								<tr><th>부서명</th>			<td id="dDeptNm"></td>
									<th>직급명</th>			<td id="dRankNm"></td>
								</tr>
								<tr><th>담당업무</th>			<td id="dRpsbWork"></td>
									<th>IP주소</th>			<td id="dIpAdres"></td>
								</tr>
								<tr><th>비고</th>				<td id="dRemark" colspan="3"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnC">닫기</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

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
<script src="<c:url value='/js/wrks/sstm/grp/user.js' />"></script>
</body>
</html>
