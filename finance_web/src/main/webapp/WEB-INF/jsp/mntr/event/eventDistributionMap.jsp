<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap/v3/css/bootstrap-select.min.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
<style>
#article-grid ul.dropdown-menu li a {
	text-align: left;
}

.bootstrap-select button {
	padding: 5px 10px;
	font-size: 12px;
	line-height: 1.5;
	border-radius: 3px;
	border-color: #ccc;
	background-color: #fff;
	color: #333;
}

.bootstrap-select .dropdown-menu.open {
	width: 78px;
}

#tbody-event td {
	vertical-align: middle;
}

#remarks {
	position: absolute;
	display: block;
	right: 0px;
	bottom: 10px;
	width: auto;
	height: auto;
	padding: 5px;
	z-index: 29;
	background: white;
	opacity: 1;
	border: 1px solid #000;
	font-weight: bolder;
	overflow-y: auto;
}

#remarks ul {
	height: auto;
	max-height: 500px;
	overflow: auto;
}

#remarks li {
	padding: 3px;
}

.datetimepicker {
	padding: 0px;
	margin: 0px;
}
</style>
<article id="article-grid">
	<div id="searchBar" class="row">
		<div class="tit">
			<h4>${common.title}</h4>
		</div>
		<h4>이벤트발생분포도</h4>
		<div class="searchBox sm">
			<div class="form-inline">
				<div class="form-group">
					<select class="form-control input-sm" name="searchSigunguCd" id="searchSigunguCd">
						<option value="">시군구</option>
					</select>
				</div>
				<div class="form-group">
					<select class="form-control input-sm" name="searchLgDongCd" id="searchLgDongCd">
						<option value="">읍면동</option>
					</select>
				</div>
				<div class="form-group">
					<select class="form-control input-sm" id="sel-icon-size">
						<option value="20">아이콘사이즈</option>
						<option value="20">20</option>
						<option value="10">10</option>
						<option value="5">5</option>
					</select>
				</div>
				<div class="form-group">
					<button class="btn btn-default btn-sm" id="btn-search">조회</button>
				</div>
			</div>
		</div>

		<div class="searchBox sm">
			<label class="radio-inline">
				<input type="radio" name="rdo-search-type" id="rdo-search-type-year" value="year" checked="checked"> 년도별
			</label>
			<label class="radio-inline">
				<input type="radio" name="rdo-search-type" id="rdo-search-type-month" value="month"> 월별
			</label>
			<label class="radio-inline">
				<input type="radio" name="rdo-search-type" id="rdo-search-type-period" value="period"> 기간별
			</label>
		</div>

		<jsp:useBean id="date" class="java.util.Date" />
		<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
		<fmt:formatDate value="${date}" pattern="MM" var="currentMonth" />
		<div class="searchBox sm">
			<div class="form-inline">
				<div class="form-group datetimepicker" style="display: none;">
					<div class="input-group date" id="search-picker-from">
						<input type="text" class="form-control input-sm" id="input-picker-from" size="7" maxlength="7" placeholder="2010-01" />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar" title="부터"></span>
						</span>
					</div>
					<span> ~ </span>
					<div class="input-group date" id="search-picker-to">
						<input type="text" class="form-control input-sm" id="input-picker-to" size="7" maxlength="7" placeholder="${currentYear}-${currentMonth}" />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar" title="까지"></span>
						</span>
					</div>
				</div>
				<div class="form-group">
					<select class="form-control input-sm" id="sel-year">
						<c:forEach begin="2010" end="${currentYear}" step="1" var="year">
							<c:choose>
								<c:when test="${currentYear eq year}">
									<option selected="selected" value="${year}">${year}년</option>
								</c:when>
								<c:otherwise>
									<option value="${year}">${year}년</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</div>
				<div class="form-group" style="display: none;">
					<select class="form-control input-sm" id="sel-month">
						<c:forEach begin="1" end="12" step="1" var="month">
							<c:choose>
								<c:when test="${currentMonth eq month}">
									<option selected="selected" value="${month}">${month}월</option>
								</c:when>
								<c:otherwise>
									<option value="${month}">${month}월</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<select class="form-control input-sm" id="sel-system" style="width: 150px;">
						<option value="ALL">전체</option>
					</select>
				</div>
			</div>
		</div>

		<div class="table-responsive">
			<table class="table" id="tb-event">
				<thead>
					<tr>
						<th>
							<div class="checkbox">
								<label>
									<input type="checkbox" id="chk-evt-ids-all" value="" aria-label="" />
								</label>
							</div>
						</th>
						<th>시스템명</th>
						<th>이벤트명</th>
						<th>아이콘</th>
					</tr>
				</thead>
				<tbody id="tbody-event"></tbody>
			</table>
		</div>
	</div>
</article>
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-select.min.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/moment.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/ko.js' />"></script>
<script src="<c:url value='/js/mntr/event/eventDistributionMap.js' />"></script>
