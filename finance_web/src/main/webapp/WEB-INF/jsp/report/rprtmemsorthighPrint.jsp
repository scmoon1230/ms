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
	var sortBy = 'STAN_YMD, MONEY_CODE, MEMBER_ORDER, MONEY_AMT DESC, MEMBER_NAME ASC, MEMBER_TYPE';
	var sortOr = 'ASC';
</script>
<script src="<c:url value='/js/report/rprtmemsorthighPrint.js'/>"></script>
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
						<div class="printTitle">고액헌금자명단</div>
					</div>
				</div>

				<table>
					<tr><td style="width:50%">
						기준일: <span class="printStartDate"></span> ~ <span class="printEndDate"></span>
						</td>
						<td></td>
					</tr>
				</table>
				
				<table>
				    <thead>
				        <tr><th class="pTh">헌금</th>
			            	<th class="pTh">신도성명</th>
			            	<th class="pTh">금액</th>
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