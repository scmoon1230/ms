<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap-select.min.css' />">
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

#tbody-fclt td {
	vertical-align: middle;
}

#remarks {
	position: absolute;
	display: block;
	left: 10px;
	bottom: 10px;
	width: auto;
	height: auto;
	padding: 5px;
	z-index: 29;
	background: white;
	opacity: 1;
	border: 1px solid #000;
	font-weight: bolder;
}

#remarks li {
	padding: 3px;
}
</style>
<article id="article-grid">
	<ol class="breadcrumb">
		<li>정보현황</li>
		<li>분포도</li>
		<li class="active">${common.title}</li>
	</ol>

	<div id="searchBar" class="row">
		<div class="tit">
			<h4>${common.title}</h4>
		</div>

		<div class="searchBox">
			<div class="form-inline">
				<div class="form-group">
					<select class="form-control input-sm tooltip-notice" name="searchSigunguCd" id="searchSigunguCd" title="시군구를 선택합니다. 읍면동 중복 선택 불가.">
						<option value="">시군구</option>
					</select>
				</div>
				<div class="form-group">
					<select class="form-control input-sm tooltip-notice" name="searchLgDongCd" id="searchLgDongCd" title="읍면동을 선택합니다. 시군구 중복 선택 불가.">
						<option value="">읍면동</option>
					</select>
				</div>
				<div class="form-group">
					<select class="form-control input-sm" id="sel-icon-size">
						<option value="10">아이콘사이즈</option>
						<option value="10">10</option>
						<option value="8">8</option>
						<option value="6">6</option>
						<option value="4">4</option>
					</select>
				</div>
				<div class="form-group">
					<button class="btn btn-default btn-sm" id="btn-search">조회</button>
				</div>
			</div>
		</div>
		<div class="table-responsive">
			<table class="table" id="tb-fclt">
				<thead>
					<tr>
						<th>
							<div class="checkbox">
								<label>
									<input type="checkbox" id="chk-fclt-used-ty-cds-all" value="" aria-label="" />
								</label>
							</div>
						</th>
						<th>유형</th>
						<th>용도</th>
						<th>모양</th>
						<th>색상</th>
					</tr>
				</thead>
				<tbody id="tbody-fclt"></tbody>
			</table>
		</div>
	</div>
</article>
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-select.min.js' />"></script>
<script src="<c:url value='/js/mntr/fclt/fcltUsedDistributionMap.js' />"></script>
