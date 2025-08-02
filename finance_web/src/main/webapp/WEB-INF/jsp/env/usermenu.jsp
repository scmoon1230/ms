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
<script src="<c:url value='/js/env/usermenu.js'/>"></script>
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
					<li class="active">공통정보 > 사용자별 메뉴관리</li>
				</ol>
				<div class="boxWrap">
					<div class="tbLeft50">
						<div class="tableTypeHalf seachT">
							<table>
								<caption>사용자별메뉴</caption>
								<tbody>
								<tr><th>사용자명</th>
									<td colspan=3>
										<input type="text" name="" id="userName" class="txtType searchEvt" style="ime-mode:active">
										<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
									</td>
								<%--
									<th>사용유형</th>
									<td><select name="sUseTyCd" id="sUseTyCd" class="selectType1">
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
									</td>
									<th>지구</th>
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

						<div class="tableType1" style="height:650px;">
							<table id="grid_user" style="width:100%">
							</table>
						</div>
					</div>
					<div class="tbRight50">
						<div class="tableTypeHalf seachT" style="height:40px;">
							<%--
							<table>
								<tbody>
								<tr>
									<th>그룹명</th>
									<td id="dGrpNm"></td>
                                    <th>레벨</th>
                                    <td id="dGrpAuthNm"></td>
									<th>그룹아이디</th>
									<td id="dGrpId"></td>
								</tr>
								</tbody>
							</table>
							 --%>
						</div>
						<br/>
						<div class="tableType1" style="height:650px;">
							<table id="grid_menu" style="width:100%">
							</table>
						</div>
						<div class="btnWrap btnR" style="width: 100%;">
							<a href="#" class="btn btnDt btnSv">저장</a>
						<%--<a href="#" class="btn btnDt btnCopy">현재메뉴를 팝업선택그룹으로 복사</a>
							<a href="#" class="btn btnDt btnCheckAll" user-trigger-selector="#grid_menu input[type=checkbox]">전체선택</a>
							<a href="#" class="btn btnDt btnUnCheckAll" user-trigger-selector="#grid_menu input[type=checkbox]">전체해제</a>--%>
						</div>
					</div>
				</div>
			</div>
			<%--
			<div class="layerWidthNone layerCopy" style="z-index:100">
				<div class="tit"><h4>그룹권한 선택</h4></div>
				<div class="layerCt">
					<div class="tableType1" style="height:300px;">
						<table id="grid_grp_to" style="width:100%">
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnCopySv">저장</a>
						<a href="#" class="btn btnCopyC">취소</a>
					</div>
				</div>
			</div>
			 --%>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>
