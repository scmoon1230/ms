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
<script src="<c:url value='/js/sheet/sheetinouttotalPrint.js'/>"></script>
</head>
<body>
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>

<div id="wrapper" class="wth100">
	<!-- container -->
	<div class="container">
		<!-- content -->
		<div class="contentWrap">
			<div class="content">
				<div class="btnR">
					<a href="#" class="btn btnPrint">인쇄</a>
					<a href="javascript:window.close();" class="btn btnC">취소</a>
				</div>
				<div>
					<div style="margin-bottom: 10px;">
						<div class="printTitle">수입/지출 총괄표</div>
					</div>
				</div>
				
				<table>
					<tr><td style="width:100%;text-align: center;">
						<span class="printStanYmd"></span>
					</td></tr>
					<tr><td style="text-align:right">단위:천원</td></tr>
				</table>
				
				<table>
				    <thead>
				        <tr><th class="pBig pTh">구분</th>
			            	<th class="pBig pTh">항목</th>
			            	<th class="pBig pTh">전주이월금</th>
			            	<th class="pBig pTh">금주수입</th>
			            	<th class="pBig pTh">금주지출</th>
			            	<th class="pBig pTh">차주이월금(시제)</th>
				        </tr>
				    </thead>
				    <tbody id="tableBody">
				        <!-- 동적 데이터 생성 -->
				    </tbody>
				</table>
			    
			</div>
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
</body>
</html>