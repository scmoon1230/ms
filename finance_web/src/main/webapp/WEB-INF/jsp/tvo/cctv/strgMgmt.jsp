<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<article id="article-left">
	<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
	<div class="colfix">
		<div class="panel panel-primary" id="panel-cctv-list">
			<div class="panel-heading">
				<h3 class="panel-title">전체CCTV리스트</h3>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-xs-4">
						<select class="form-control input-xs sel-fclt-used-ty-cd"></select>
					</div>
					<div class="col-xs-6">
						<input type="text" class="form-control input-xs txt-keyword" maxlength="32" size="32" placeholder="시설물명 | 주소 를 입력하세요." data-grid-id="cctv-list"/>
					</div>
					<div class="col-xs-2 text-right">
						<button class="btn btn-default btn-xs btn-search" data-grid-id="cctv-list">조회</button>
					</div>
				</div>
			</div>
			<table id="grid-cctv-list"></table>
			<div id="paginate-cctv-list" class="paginate text-center"></div>
			<div class="panel-footer">
				<div class="row">
                    <div class="col-xs-8 text-danger">
                        <span id="title-cctv-list" style="font-size: 1.2em; font-weight: bolder; line-height: 22px;"></span>
                    </div>
					<%--
					<div class="col-xs-4 text-right">
						<button type="button" class="btn btn-default btn-xs" data-toggle="tooltip" data-placement="top" title="초기화면으로 돌아갑니다." onclick="oCctvMgmt.mntr.stop();">기본화면
						</button>
					</div>
					--%>
				</div>
			</div>
		</div>
	</div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
</article>
<script src="<c:url value='/js/tvo/cctv/strgMgmt.js' />"></script>
<script>
	let oCctvMgmt;
	$(function () {
		oCctvMgmt = new cctvMgmt();
		oCctvMgmt.init();
	});
</script>
