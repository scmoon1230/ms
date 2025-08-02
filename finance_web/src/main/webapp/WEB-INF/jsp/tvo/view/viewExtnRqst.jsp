<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/tvoMap.css'/>" />
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/view/viewRqst.css'/>"/>
<article id="article-left">
	<div class="row">
		<div class="col-xs-12">
			<ol class="breadcrumb">
				<li>열람신청</li>
				<li class="active">${common.title}</li>
			</ol>
		</div>
	</div>
	<div class="panel panel-primary" id="rqst-view-prod-extn">
		<div class="panel-heading">
			<h3 class="panel-title">열람기간연장신청목록</h3>
		</div>
		<div class="panel-body">
			<div class="well well-xs">
				<div class="form-inline">
					<div class="form-group">
						<label for="rqst-view-prod-extn-viewExtnRqstYmdhms" class="control-label">연장신청일</label>
						<div class="input-group datetimepicker-ymd rqst-view-prod-extn-viewExtnRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="rqst-view-prod-extn-viewExtnRqstYmdhms"
								 name="rqst-view-prod-extn-viewExtnRqstYmdhms" title="연장신청일" placeholder="연장신청일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt fa-sm"></i>
							</span>
						</div>
					</div>
					<div class="form-group">
						<label for="rqst-view-prod-extn-viewEndYmdhms" class="control-label">열람종료일</label>
						<div class="input-group datetimepicker-ymd rqst-view-prod-extn-viewEndYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="rqst-view-prod-extn-viewEndYmdhms"
								 name="rqst-view-prod-extn-viewEndYmdhms" title="열람종료일" placeholder="열람종료일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
					</div>
					<div class="form-group">
						<label for="rqst-view-prod-extn-tvoPrgrsCd" class="control-label">진행상태</label>
						<select id="rqst-view-prod-extn-tvoPrgrsCd" name="rqst-view-prod-extn-tvoPrgrsCd" class="form-control input-xs"
							 title="진행상태" onchange="javascript:oViewExtnRqst.view.prodExtn.grid.search(1);"></select>
					</div>
					<div class="form-group">
						<label for="rqst-view-prod-extn-viewExtnRqstRsn" class="control-label">연장사유</label>
						<input type="text" class="form-control input-xs" id="rqst-view-prod-extn-viewExtnRqstRsn"
							 name="rqst-view-prod-extn-viewExtnRqstRsn" title="열람기간연장사유" placeholder="연장사유" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-10">
						<div class="form-inline">
							<div class="form-group">
								<label for="evtNo" class="control-label">사건번호</label>
								<input type="text" class="form-control input-xs" id="rqst-view-prod-extn-evtNo"
									 name="rqst-view-prod-extn-evtNo" title="사건번호" placeholder="사건번호" />
							</div>
						</div>
					</div>
					<div class="col-xs-2 text-right">
					<%-- <button type="button" class="btn btn-default btn-xs" style='margin-right:3px' id="btn-refresh" data-toggle="tooltip" data-placement="top" title="" data-original-title="목록을 갱신합니다.">새로고침</button> --%>
						<button type="button" class="btn btn-primary btn-xs btn-search pull-right" title="목록을 검색합니다." onclick="oViewExtnRqst.view.prodExtn.grid.search(1);">검색</button>
					</div>
				</div>
			</div>
			<table id="grid-rqst-view-prod-extn"></table>
			<div id="paginate-rqst-view-prod-extn" class="paginate text-center"></div>
		</div>
	</div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
</article>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/moment.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.dev.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/ko.js' />"></script>
<script src="<c:url value='/js/tvo/tvoCmn.js'/>"></script>
<script src="<c:url value='/js/tvo/view/viewExtnRqst.js'/>"></script>
