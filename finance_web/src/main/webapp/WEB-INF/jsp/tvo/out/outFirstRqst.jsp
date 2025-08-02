<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/tvoMap.css'/>" />
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/view/viewRqst.css'/>"/>
<article id="article-left">
	<div class="row">
		<div class="col-xs-12">
			<ol class="breadcrumb">
				<li>반출확인</li>
				<li class="active">${common.title}</li>
			</ol>
		</div>
	</div>
	<div class="panel panel-primary" id="rqst-out">
		<div class="panel-heading">
			<h3 class="panel-title">반출신청목록</h3>
		</div>
		<div class="panel-body">
			<div class="well well-xs">
				<div class="form-inline">
					<div class="form-group">
						<label for="out-rqst-outRqstYmdhms" class="control-label">반출신청일</label>
						<div class="input-group datetimepicker-ymd out-rqst-outRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="out-rqst-outRqstYmdhms-fr"
								 name="out-rqst-outRqstYmdhms-fr" title="반출신청일" placeholder="반출신청일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
						~
						<div class="input-group datetimepicker-ymd out-rqst-outRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="out-rqst-outRqstYmdhms-to"
								 name="out-rqst-outRqstYmdhms-to" title="반출신청일" placeholder="반출신청일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
					</div>
				<%--<div class="form-group">
						<label for="rqst-out-playEndYmdhms" class="control-label">재생종료일</label>
						<div class="input-group datetimepicker-ymd rqst-out-playEndYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="rqst-out-playEndYmdhms"
							 name="rqst-out-playEndYmdhms" title="재생종료일" placeholder="재생종료일" />
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
					</div>--%>
					<div class="form-group">
						<label for="out-rqst-rqstRsnTyCd" class="control-label">신청사유</label>
						<select class="form-control input-xs" id="out-rqst-rqstRsnTyCd" name="out-rqst-rqstRsnTyCd" title="신청사유"
							 onchange="javascript:oOutFirstRqst.out.rqst.grid.search(1);"></select>
					</div>
					<div class="form-group">
						<label for="out-rqst-tvoPrgrsCd" class="control-label">진행상태</label>
						<select class="form-control input-xs" id="out-rqst-tvoPrgrsCd" name="out-rqst-tvoPrgrsCd" title="진행상태"
							 onchange="javascript:oOutFirstRqst.out.rqst.grid.search(1);"></select>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-11">
						<div class="form-inline">
							<div class="form-group">
								<label for="rqst-out-evtNo" class="control-label">사건번호</label>
								<input type="text" class="form-control input-xs" id="rqst-out-evtNo" name="rqst-out-evtNo" title="사건번호" placeholder="사건번호" />
							</div>
							<div class="form-group">
								<label for="rqst-out-evtNm" class="control-label">사건명</label>
								<input type="text" class="form-control input-xs" id="rqst-out-evtNm" name="rqst-out-evtNm" title="사건명" placeholder="사건명" />
							</div>
							<div class="form-group">
								<label for="rqst-out-fcltLblNm" class="control-label">카메라명</label>
								<input type="text" class="form-control input-xs" id="rqst-out-fcltLblNm" name="rqst-out-fcltLblNm" title="카메라명" placeholder="카메라명" />
							</div>
							<div class="form-group">
								<label class="control-label" title="시스템 전체 반출대기 건수">반출대기: <span id="rqst-out-ingCnt"></span>건</label>,
								<label class="control-label" title="시스템 전체 암호화대기 건수">암호화대기: <span id="rqst-out-drmCnt"></span>건</label>,
								<label class="control-label" title="시스템 전체 반출소요 예상시간">소요시간: <span id="rqst-out-costMin"></span>분</label>
							</div>
						</div>
					<%--<div class="form-inline">
							<div class="form-group">
								<label>
									<input type="checkbox" id="rqst-out-maskingEndYn" name="rqst-out-maskingEndYn" style="vertical-align: sub;" /> 마스킹완료
								</label>
							</div>
						</div>--%>
					</div>
					<div class="col-xs-1 text-right">
					<%-- <button type="button" class="btn btn-default btn-xs" title="선택된 반출신청의 기간을 연장합니다." onclick="oOutFirstRqst.out.prodExtn.init('MULTIPLE');">선택기간연장</button> --%>
					<%-- <button type="button" class="btn btn-default btn-xs" title="선택된 반출신청의 기간을 연장합니다." onclick="oOutFirstRqst.out.prodExtn.open('MULTIPLE');">선택기간연장</button> --%>
					<%-- <button type="button" class="btn btn-default btn-xs" style='margin-right:3px' id="btn-refresh" data-toggle="tooltip" data-placement="top" title="" data-original-title="목록을 갱신합니다.">새로고침</button> --%>
						<button type="button" class="btn btn-primary btn-xs btn-search pull-right" title="목록을 검색합니다."
							onclick="oOutFirstRqst.out.rqst.grid.search(1);">검색</button>
					</div>
				</div>
			</div>
		</div>
		<table id="grid-rqst-out"></table>
		<div id="paginate-rqst-out" class="paginate text-center"></div>
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
<script src="<c:url value='/js/tvo/out/outFirstRqst.js'/>"></script>
