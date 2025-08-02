<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<script src="<c:url value='/js/mntr/mng/mngIcon.js' />"></script>
<article id="article-grid">

				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
	
	<label>아이콘 구분</label>
	<div class="radio">
		<label class="radio-inline">
			<input type="radio" name="rdo-icon-gbn" id="rdo-icon-gbn-cctv" value="CCTV" checked="checked"> CCTV
		</label>
		<label class="radio-inline">
			<input type="radio" name="rdo-icon-gbn" id="rdo-icon-gbn-etc" value="ETC"> ETC
		</label>
	</div>

	<label>선택아이콘 정보 : <span class="text-danger">변경할 아이콘 사이즈(Pixel)는 변경전과 동일해야합니다</span></label>
	<table class="table table-striped" id="tb-icon-selected">
		<caption></caption>
		<thead></thead>
		<tbody></tbody>
	</table>
	<form id="form-icon" class="hidden" method="post" enctype="multipart/form-data">
		<input type="file" id="file-icon" name="file-icon" accept="image/png" data-icon-gbn="" data-icon-ty="" data-icon-file-nm="" />
	</form>
</article>