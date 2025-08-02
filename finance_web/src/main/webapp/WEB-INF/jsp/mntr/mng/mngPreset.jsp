<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<article id="article-grid">

				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
	
	<div id="searchBar">
		<form class="container-search" name="form-search" id="form-search" method="post">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<select id="searchFcltUsedTyCd" name="searchFcltUsedTyCd" class="form-control input-xs" data-selected="<c:out value='${common.searchFcltUsedTyCd}'/>"></select>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<!--
						<select id="searchFcltGdsdtTy" name="searchFcltGdsdtTy" class="form-control input-xs">
							<option value="">물품 구분</option>
							<option value="0">주</option>
							<option value="1">보조</option>
						</select>
						 -->
						<select id="searchFcltKndDtlCd" name="searchFcltKndDtlCd" class="form-control input-xs" data-selected="<c:out value='${common.searchFcltKndDtlCd}'/>">
							<option value="">기능별 유형</option>
							<option value="RT">회전형</option>
							<option value="FT">고정형</option>
						</select>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<select id="searchPresetYn" name="searchPresetYn" class="form-control input-xs" data-selected="<c:out value='${common.searchPresetYn}'/>">
							<option value="">설정 여부</option>
							<option value="Y">설정</option>
							<option value="N">미설정</option>
						</select>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<select id="searchGbn" name="searchGbn" class="form-control input-xs" data-selected="<c:out value='${common.searchGbn}'/>">
							<option value="NM">시설물명</option>
							<option value="ID">시설물ID</option>
							<option value="ADDR">주소</option>
						</select>
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group">
						<input id="searchKeyword" name="searchKeyword" class="form-control input-xs" maxlength="30" size="27" placeholder="키워드를 입력하세요..." value="<c:out value='${common.searchKeyword}'/>" />
					</div>
				</div>
				<div class="col-xs-2 text-right">
					<div class="form-group">
						<input type="hidden" name="searchPage" id="searchPage" value="<c:out value='${common.searchPage eq 0 ? 1 : common.searchPage}'/>" />
						<button type="button" id="sendBtn" class="btn btn-default btn-xs" onclick="oMngPreset.reloadGrid();">조회</button>
					</div>
				</div>
			</div>
		</form>
		<div class="grid-header row">
			<div class="col-xs-9">
				<div class="tb_title">조회결과</div>
			</div>
			<div class="col-xs-3 text-right">
				<label>
					<span id="rowCnt" class="totalNum"></span>
					건
				</label>
			</div>
		</div>
		<table id="grid-preset"></table>
		<div id="paginate-preset" class="paginate text-center"></div>
	</div>
</article>
<script src="<c:url value='/js/mntr/mng/mngPreset.js'/>"></script>
