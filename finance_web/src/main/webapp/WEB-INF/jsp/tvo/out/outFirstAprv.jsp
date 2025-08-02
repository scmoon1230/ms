<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/tvoMap.css'/>" />
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/view/viewRqst.css'/>"/>
<article id="article-left">
	<div class="row">
		<div class="col-xs-12">
			<ol class="breadcrumb">
				<li>반출승인</li>
				<li class="active">${common.title}</li>
			</ol>
		</div>
	</div>
	<div class="panel panel-primary" id="aprv-out">
		<div class="panel-heading">
			<h3 class="panel-title">반출신청목록</h3>
		</div>
		<div class="panel-body">
			<div class="well well-xs">
				<div class="form-inline">
					<div class="form-group">
						<label for="aprv-out-outRqstYmdhms" class="control-label">반출신청일</label>
						<div class="input-group datetimepicker-ymd aprv-out-outRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="aprv-out-outRqstYmdhms-fr"
								 name="aprv-out-outRqstYmdhms-fr" title="반출신청일" placeholder="반출신청일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
						~
						<div class="input-group datetimepicker-ymd aprv-out-outRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="aprv-out-outRqstYmdhms-to"
								 name="aprv-out-outRqstYmdhms-to" title="반출신청일" placeholder="반출신청일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
					</div>
				<%--<div class="form-group">
						<label for="aprv-out-playEndYmdhms" class="control-label">재생종료일</label>
						<div class="input-group datetimepicker-ymd aprv-out-playEndYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="aprv-out-playEndYmdhms"
								 name="aprv-out-playEndYmdhms" title="재생종료일" placeholder="재생종료일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
					</div>--%>
					<div class="form-group">
						<label for="aprv-out-rqstRsnTyCd" class="control-label">신청사유</label>
						<select class="form-control input-xs" id="aprv-out-rqstRsnTyCd" name="aprv-out-rqstRsnTyCd" title="신청사유"
							onchange="javascript:oOutFirstAprv.out.rqst.grid.search(1);"></select>
					</div>
					<div class="form-group">
						<label for="aprv-out-tvoPrgrsCd" class="control-label">진행상태</label>
						<select class="form-control input-xs" id="aprv-out-tvoPrgrsCd" name="aprv-out-tvoPrgrsCd" title="진행상태"
							onchange="javascript:oOutFirstAprv.out.rqst.grid.search(1);"></select>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-10">
						<div class="form-inline">
							<div class="form-group">
								<label for="aprv-out-evtNo" class="control-label">사건번호</label>
								<input type="text" class="form-control input-xs" id="aprv-out-evtNo"
									name="aprv-out-evtNo" title="사건번호" placeholder="사건번호"
									onkeydown="if (window.event.keyCode == 13) { oOutFirstAprv.out.rqst.grid.search(1); }" />
							</div>
							<div class="form-group">
								<label for="aprv-out-evtNm" class="control-label">사건명</label>
								<input type="text" class="form-control input-xs" id="aprv-out-evtNm"
									name="aprv-out-evtNm" title="사건명" placeholder="사건명"
									onkeydown="if (window.event.keyCode == 13) { oOutFirstAprv.out.rqst.grid.search(1); }" />
							</div>
							<div class="form-group">
								<label for="aprv-out-fcltLblNm" class="control-label">카메라명</label>
								<input type="text" class="form-control input-xs" id="aprv-out-fcltLblNm"
									name="aprv-out-fcltLblNm" title="카메라명" placeholder="카메라명"
									onkeydown="if (window.event.keyCode == 13) { oOutFirstAprv.out.rqst.grid.search(1); }" />
							</div>
							<div class="form-group">
								<label for="aprv-out-rqstUserId" class="control-label">신청자아이디</label>
								<input type="text" class="form-control input-xs" id="aprv-out-rqstUserId"
									name="aprv-out-rqstUserId" title="신청자아이디" placeholder="신청자아이디"
									onkeydown="if (window.event.keyCode == 13) { oOutFirstAprv.out.rqst.grid.search(1); }" />
							</div>
						<%--<div class="form-group">
								<label for="aprv-out-prgrs" class="control-label sr-only">검색조건</label>
								<input type="hidden" id="aprv-out-prgrs" name="aprv-out-prgrs" title="검색조건" value="${common.prgrs}" />
							</div>--%>
						</div>
					</div>
					<div class="col-xs-2 text-right">
						<button type="button" class="btn btn-default btn-xs" id="btn-refresh" title="목록을 갱신합니다."
							data-toggle="tooltip" data-placement="top">새로고침</button>
						<!--<button type="button" class="btn btn-primary btn-xs" title="선택한 신청을 승인합니다."
							onclick="oOutFirstAprv.out.aprvMulti.open();">선택승인</button>-->
						<button type="button" class="btn btn-primary btn-xs btn-search" title="목록을 검색합니다."
							onclick="oOutFirstAprv.out.rqst.grid.search(1);">검색</button>
					</div>
				</div>
			</div>
			<table id="grid-aprv-out"></table>
			<div id="paginate-aprv-out" class="paginate text-center"></div>
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
<script src="<c:url value='/js/tvo/out/outFirstAprv.js'/>"></script>
