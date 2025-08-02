<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/tvoMap.css'/>" />
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/view/viewRqst.css'/>"/>
<article id="article-left">
	<div class="row">
		<div class="col-xs-12">
			<ol class="breadcrumb">
				<li>열람승인</li>
				<li class="active">${common.title}</li>
			</ol>
		</div>
	</div>
	<div class="panel panel-primary" id="aprv-view">
		<div class="panel-heading">
			<h3 class="panel-title">열람신청목록</h3>
		</div>
		<div class="panel-body">
			<div class="well well-xs">
				<div class="form-inline">
					<div class="form-group">
						<label for="aprv-view-viewRqstYmdhms" class="control-label">열람신청일</label>
						<div class="input-group datetimepicker-ymd aprv-view-viewRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="aprv-view-viewRqstYmdhms-fr"
								 name="aprv-view-viewRqstYmdhms-fr" title="열람신청일" placeholder="열람신청일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
						~
						<div class="input-group datetimepicker-ymd aprv-view-viewRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="aprv-view-viewRqstYmdhms-to"
								 name="aprv-view-viewRqstYmdhms-to" title="열람신청일" placeholder="열람신청일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
					</div>
				<%--<div class="form-group">
						<label for="aprv-view-viewEndYmdhms" class="control-label">열람종료일</label>
						<div class="input-group datetimepicker-ymd aprv-view-viewEndYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="aprv-view-viewEndYmdhms" name="aprv-view-viewEndYmdhms" title="열람종료일"
								placeholder="열람종료일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
					</div>--%>
					<div class="form-group">
						<label for="aprv-view-rqstRsnTyCd" class="control-label">신청사유</label>
						<select class="form-control input-xs" id="aprv-view-rqstRsnTyCd" name="aprv-view-rqstRsnTyCd" title="신청사유"
							onchange="javascript:oViewFirstAprv.view.rqst.grid.search(1);"></select>
					</div>
					<div class="form-group">
						<label for="aprv-view-tvoPrgrsCd" class="control-label">진행상태</label>
						<select class="form-control input-xs" id="aprv-view-tvoPrgrsCd" name="aprv-view-tvoPrgrsCd" title="진행상태"
							onchange="javascript:oViewFirstAprv.view.rqst.grid.search(1);"></select>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-8">
						<div class="form-inline">
							<div class="form-group">
								<label for="aprv-view-evtNo" class="control-label">사건번호</label>
								<input type="text" class="form-control input-xs" id="aprv-view-evtNo"
									name="aprv-view-evtNo" title="사건번호" placeholder="사건번호"
									onkeydown="if (window.event.keyCode == 13) { oViewFirstAprv.view.rqst.grid.search(1); }" />
							</div>
							<div class="form-group">
								<label for="aprv-view-evtNm" class="control-label">사건명</label>
								<input type="text" class="form-control input-xs" id="aprv-view-evtNm"
									name="aprv-view-evtNm" title="사건명" placeholder="사건명"
									onkeydown="if (window.event.keyCode == 13) { oViewFirstAprv.view.rqst.grid.search(1); }" />
							</div>
							<div class="form-group">
								<label for="aprv-view-rqstUserId" class="control-label">신청자아이디</label>
								<input type="text" class="form-control input-xs" id="aprv-view-rqstUserId"
									name="aprv-view-rqstUserId" title="신청자아이디" placeholder="신청자아이디"
									onkeydown="if (window.event.keyCode == 13) { oViewFirstAprv.view.rqst.grid.search(1); }" />
							</div>
							<div class="form-group">
							<%--<label for="rqst-view-prgrs" class="control-label sr-only">검색조건</label>
								<input type="hidden" id="rqst-view-prgrs" name="rqst-view-prgrs" title="검색조건" value="${common.prgrs}"/>--%>
								<input type="hidden" id="userId" name="userId" value="${LoginVO.userId}" />
								<input type="hidden" id="dstrtCd" name="dstrtCd" value="${dstrtCd}" />
							</div>
						</div>
					</div>
					<div class="col-xs-4 text-right">
						<button type="button" class="btn btn-default btn-xs" id="btn-refresh" title="목록을 갱신합니다."
							data-toggle="tooltip" data-placement="top">새로고침</button>
					<!--<button type="button" class="btn btn-primary btn-xs" title="선택한 신청을 승인합니다."
							onclick="oViewFirstAprv.view.aprvMulti.open();">선택승인</button>-->
						<button type="button" class="btn btn-primary btn-xs btn-search" title="목록을 검색합니다."
							onclick="oViewFirstAprv.view.rqst.grid.search(1);">검색</button>
					</div>
				</div>
			</div>
			<table id="grid-aprv-view"></table>
			<div id="paginate-aprv-view" class="paginate text-center"></div>
		</div>
	</div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
	<div class="col"></div>
</article>
<!-- <script src="<c:url value='/js/common.js'/>"></script> -->
<script src="<c:url value='/js/bootstrap-datetimepicker/js/moment.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.dev.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/ko.js' />"></script>
<script src="<c:url value='/js/tvo/tvoCmn.js'/>"></script>
<script src="<c:url value='/js/tvo/view/viewFirstAprv.js'/>"></script>
