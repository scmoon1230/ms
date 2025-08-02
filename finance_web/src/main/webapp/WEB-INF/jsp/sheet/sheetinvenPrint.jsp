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
	var sortBy = 'ACCT_GB, UP_MONEY_AMT';
	var sortOr = 'DESC';
</script>
<script src="<c:url value='/js/sheet/sheetinvenPrint.js'/>"></script>
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
						<div class="printTitle">수입/지출 명세표</div>
					</div>
				</div>
				
				<table>
					<tr><td style="height:100px;">&nbsp;</td>
					</tr>
					<tr><td style="width:50%">
						수입일: <span class="printStanYmd"></span>
						</td>
						<td></td>
					</tr>
				</table>
				
				<table>
				    <thead>
				        <tr>
				        	<!-- <th class="pBig pTh">상위계정</th> -->
					        <th class="pBig pTh">계정</th>
			            	<th class="pBig pTh">내역</th>
			            	<th class="pBig pTh">전표금액</th>
			            	<th class="pBig pTh">금액</th>
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