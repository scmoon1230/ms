<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title><prprts:value key="HD_TIT" /></title>
	<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
</head>
<body>
	<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp"%>
	<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
	<div id="wrapper" class="wth100">
		<!-- container -->
		<div class="container">
			<!-- content -->
			<div class="contentWrap">
				<div class="content">
					<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp"%>
					<div class="boxWrap">
						<div class="tbLeft50">
							<div class="tableTypeFree seachT">
								<table>
									<tbody>
										<tr>
											<th>이벤트아이디</th>
											<td><input type="text" name="" id="sEvtId" class="txtType grpNm searchEvt" style="ime-mode: active"></td>
											<th>이벤트명</th>
											<td>
												<input type="text" name="" id="sEvtNm" class="txtType grpNm searchEvt" style="ime-mode: active">
												<a href="javascript:;" class="btn btnRight btnS searchBtn">조회</a>
											</td>
										</tr>
									</tbody>
								</table>
							</div><br/>
							<div class="tableType1" style="height: 500px;">
								<table id="grid_event"></table>
							</div>
						</div>
						<div class=tbRight50>
							<div class="searchBox50">
								<dl><dt id="moblAccTitle">모바일리스트</dt></dl>
							</div>
							<div class="tableType1" style="height: 500px;">
								<table id="grid_eventNm"></table>
							</div>
						</div>
					</div>
					<div class="btnWrap btnR">
						<a href="#" class="btn btnDt btnRgt">등록</a>
						<a href="#" class="btn btndelete">삭제</a>
						<a href="#" class="btn btnupdate">수정</a>
					</div>
				</div>
				<!-- 레이어팝업 상세 -->
				<div class="layer layerRegister" id="div_drag_2">
					<div class="tit"><h4>모바일리스트 추가</h4></div>
					<div class="layerCt">
						<div class="tableType1" user-title="모바일 사용자 추가" style="height: 500px;">
							<table id="grid_eventNm_popup"></table>
						</div>
						<div class="btnCtr">
							<a href="#" class="btn btnSv">저장</a>
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
	<!--  footer -->
	<footer>
		<%@include file="/WEB-INF/jsp/mntr/layout/footer.jsp"%>
	</footer>
	<!-- //footer -->
	<script>
		const sendTyCd = [
			<c:forEach items="${sendTyCd}" var="val">
				{"cdId": "${val.cdId}", "cdNmKo" : "${val.cdNmKo}"},
			</c:forEach>
		];

		const useTyCd = [
			{ "cdId": "Y", "cdNmKo": "Y" },
			{ "cdId": "N", "cdNmKo": "N" }
		];
	</script>
	<script src="<c:url value='/js/wrks/sstm/mbl/mbl_evt.js'/>"></script>
</body>
</html>
