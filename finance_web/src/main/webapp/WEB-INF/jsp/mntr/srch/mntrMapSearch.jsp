<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<article id="article-grid">
	<div id="searchBar">
		<div class="tit">
			<h4>${common.title}</h4>
		</div>
		<div class="grid-header row">
			<div class="col-xs-10">
				<span id="grid-search-row-cnt"></span>
			</div>
			<div class="col-xs-2 text-right">
				<button type="button" class="btn btn-default btn-sm" onclick="oMntrMapSearch.close();">닫기</button>
			</div>
		</div>
		<hr style="margin-top: 2px; margin-bottom: 2px;" />
		<table id="grid-search"></table>
		<div id="paginate-search" class="paginate text-center"></div>
	</div>
</article>
<script>
	$(function(){
		oMntrMapSearch = new mntrMapSearch();
	});

	function mntrMapSearch() {
		this.close = function() {
			if(typeof oMntrMap != 'undefined') {
				oMntrMap.search.close();
			}
			else if(typeof oTvoMap != 'undefined') {
				oTvoMap.search.close();
			}
		};
	};
</script>