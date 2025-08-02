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
<script src="<c:url value='/js/yearend/yearendhometax.js'/>"></script>
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
					<li class="active">헌금관리 > 연말정산 > 기부금 국세청 업로드</li>
				</ol>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>헌금</caption>
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
								        <option value="${val.stanYy}" ${2 == val.rk ? 'selected' : ''} ><c:out value="${val.stanYy}" ></c:out></option>
								    </c:forEach>
								</select>
	                        </td>
							<td>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
								<a href="javascript:;" class="btn btnRight btnE">엑셀</a>
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
				<%--<div class="btnWrap btnR">
						<a href="#" class="btn btnDt btnRgt">등록</a>
						<a href="#" class="btn btnMultiDe">삭제</a>
					</div>--%>
				</div>
			</div>

		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>

<form id="dataForm" name="dataForm" method="post">
	<input type="hidden" name="paraDay1"   id="paraDay1"   value="">
<!--<input type="hidden" name="paraDay2"   id="paraDay2"   value=""> -->
	<input type="hidden" name="paraDay1"   id="paraDay1"   value="">
	<input type="hidden" name="paraType"   id="paraType"   value="">
	<input type="hidden" name="paraOrg"    id="paraOrg"    value="">
	<input type="hidden" name="paraStanYy" id="paraStanYy" value="">
	<input type="hidden" name="sidx" id="sidx" value="">
	<input type="hidden" name="sord" id="sord" value="">
	<input type="hidden" name="titleKey"    id="titleKey"    value="">
	<input type="hidden" name="titleHeader" id="titleHeader" value="">
</form>

</body>
</html>