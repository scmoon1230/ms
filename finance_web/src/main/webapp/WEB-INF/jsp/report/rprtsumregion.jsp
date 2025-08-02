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
<script src="<c:url value='/js/report/rprtsumregion.js'/>"></script>
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
					<li class="active">헌금관리 > 헌금보고 > 구역헌금조회</li>
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
	                        <th>기준년도</th>
	                        <td><select name="" id="stanYy" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
	                        		<option value="">전체</option>
								    <c:forEach items="${stanYyList}" var="val">
								        <option value="${val.stanYy}" ${1 == val.rk ? 'selected' : ''} ><c:out value="${val.stanYy}" ></c:out></option>
								    </c:forEach>
								</select>
	                        </td>
                           <%--<th>기준일자</th>
							<td><input type="date" id="stanYmd" class="date8Type" value="${currentDay}"
									required="required" user-title="기준일자" onchange="$('.btnS').click()">
							</td>--%>
							<th>헌금</th>
							<td><select name="" id="moneyCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()" disabled>
									<option value="">전체</option>
									<c:forEach items="${moneyList}" var="val">
										<option value="${val.moneyCode}" ${'008' == val.moneyCode ? 'selected' : ''} >${val.moneyCode} - ${val.moneyName}</option>
									</c:forEach>
								</select>
							</td>
							<th>교구</th>
							<td><select name="" id="deptCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${deptList}" var="val">
										<option value="${val.deptCode}" ${'11' == val.deptCode ? 'selected' : ''} ><c:out value="${val.deptName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>구역</th>
							<td><select name="" id="regionCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${regionList}" var="val">
										<option value="${val.regionCode}"><c:out value="${val.regionName}" ></c:out></option>
									</c:forEach>
								</select>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
							</td>
						<%--<th>재정</th>
							<td><select name="" id="acctGb" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${AcctgbList}" var="val">
										<option value="${val.acctGb}"><c:out value="${val.acctGbName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>직분</th>
							<td><select name="" id="positionCode" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
									<option value="">전체</option>
									<c:forEach items="${positionList}" var="val">
										<option value="${val.positionCode}"><c:out value="${val.positionName}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>신도번호</th>
							<td><input type="text" name="" id="memberNo" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>신도성명</th>
							<td><input type="text" name="" id="memberName" class="txtType searchEvt" style="ime-mode:active">
							</td>--%>
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
						1월 : <span id="monthAmnt0"></span> 원 &nbsp; &nbsp;
						2월 : <span id="monthAmnt1"></span> 원 &nbsp; &nbsp;
						3월 : <span id="monthAmnt2"></span> 원 &nbsp; &nbsp;
						4월 : <span id="monthAmnt3"></span> 원 &nbsp; &nbsp;
						5월 : <span id="monthAmnt4"></span> 원 &nbsp; &nbsp;
						6월 : <span id="monthAmnt5"></span> 원 &nbsp; &nbsp;
						7월 : <span id="monthAmnt6"></span> 원 &nbsp; &nbsp;
						8월 : <span id="monthAmnt7"></span> 원 &nbsp; &nbsp;
						9월 : <span id="monthAmnt8"></span> 원 &nbsp; &nbsp;
						10월 : <span id="monthAmnt9"></span> 원 &nbsp; &nbsp;
						11월 : <span id="monthAmnt10"></span> 원 &nbsp; &nbsp;
						12월 : <span id="monthAmnt11"></span> 원 &nbsp; &nbsp;
						누계 : <span id="totalAmnt"></span> 원
					 </h4>
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
</body>
</html>