<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/fileinput/fileinput.min.css' />" />
<script type="text/javascript" src="<c:url value='/js/fileinput/fileinput.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/fileinput/fileinput_locale_ko.js' />"></script>
<article id="article-grid">
	<ol class="breadcrumb">
		<li>시설물</li>
		<li class="active">${common.title}</li>
	</ol>

	<div class="row">
		<div class="tit">
			<h4>${common.title}</h4>
		</div>
	</div>

	<div style="margin-top: 20px; margin-right: 50px; margin-left: 50px; vertical-align: middle;">
		<div class="searchBox sm" style="margin-bottom: 30px; border-radius: 5px;"; text-align:center;">

		</div>
	</div>
</article>
<script>
	$(function() {
		oFcltRegExcel = new fcltRegExcel();
		oFcltRegExcel.init();
	});

	function fcltRegExcel() {
		this.init = function() {
			collapse({
				left : true
			});
		}
	}
</script>

