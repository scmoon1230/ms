<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/tvo/tvoMap.css'/>"/>
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
	<div class="panel panel-primary" id="rqst-view">
		<div class="panel-heading">
			<h3 class="panel-title">열람신청목록</h3>
		</div>
		<div class="panel-body">
			<div class="well well-xs search">
				<div class="form-inline">
					<div class="form-group">
						<label for="rqst-view-viewRqstYmdhms" class="control-label">열람신청일</label>
						<div class="input-group datetimepicker-ymd rqst-view-viewRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="rqst-view-viewRqstYmdhms-fr"
								 name="rqst-view-viewRqstYmdhms-fr" title="열람신청일" placeholder="열람신청일"/>
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
						~
						<div class="input-group datetimepicker-ymd rqst-view-viewRqstYmdhms">
							<input type="text" class="form-control input-xs datetimepicker-ymd" id="rqst-view-viewRqstYmdhms-to"
								 name="rqst-view-viewRqstYmdhms-to" title="열람신청일" placeholder="열람신청일"/>
							<span class="input-group-addon input-group-addon-xs">
								<i class="far fa-calendar-alt"></i>
							</span>
						</div>
					</div>
					<%--<div class="form-group">
							<label for="rqst-view-viewEndYmdhms" class="control-label">열람종료일</label>
							<div class="input-group datetimepicker-ymd rqst-view-viewEndYmdhms">
								<input type="text" class="form-control input-xs datetimepicker-ymd" id="rqst-view-viewEndYmdhms"
									 name="rqst-view-viewEndYmdhms" title="열람종료일" placeholder="열람종료일" />
								<span class="input-group-addon input-group-addon-xs">
									<i class="far fa-calendar-alt"></i>
								</span>
							</div>
						</div>--%>
					<div class="form-group">
						<label for="rqst-view-rqstRsnTyCd" class="control-label">신청사유</label>
						<select class="form-control input-xs" id="rqst-view-rqstRsnTyCd" name="rqst-view-rqstRsnTyCd"
							 title="신청사유" onchange="oViewFirstRqst.view.rqst.grid.search(1);"></select>
					</div>
					<div class="form-group">
						<label for="rqst-view-tvoPrgrsCd" class="control-label">진행상태</label>
						<select class="form-control input-xs" id="rqst-view-tvoPrgrsCd" name="rqst-view-tvoPrgrsCd"
							 title="진행상태" onchange="oViewFirstRqst.view.rqst.grid.search(1);"></select>
					</div>
					<div class="form-group">
						<label for="rqst-view-dstrtCd" class="control-label">신청지구</label>
						<select class="form-control input-xs" id="rqst-view-dstrtCd" name="rqst-view-dstrtCd"
							 title="지구" onchange="oViewFirstRqst.view.rqst.grid.search(1);"></select>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-8">
						<div class="form-inline">
							<div class="form-group">
								<label for="rqst-view-evtDstrtCd" class="control-label">발생지구</label>
								<select class="form-control input-xs" id="rqst-view-evtDstrtCd" name="rqst-view-evtDstrtCd"
									 title="지구" onchange="oViewFirstRqst.view.rqst.grid.search(1);"></select>
							</div>
							<div class="form-group">
								<label for="rqst-view-evtNo" class="control-label">사건번호</label>
								<input type="text" class="form-control input-xs" id="rqst-view-evtNo" name="rqst-view-evtNo"
									title="사건번호" placeholder="사건번호"/>
							</div>
							<div class="form-group">
								<label for="rqst-view-evtNm" class="control-label">사건명</label>
								<input type="text" class="form-control input-xs" id="rqst-view-evtNm" name="rqst-view-evtNm"
									title="사건명" placeholder="사건명"/>
							</div>
							<div class="form-group">
							<%--<label for="rqst-view-prgrs" class="control-label sr-only">검색조건</label>
								<input type="hidden" id="rqst-view-prgrs" name="rqst-view-prgrs" title="검색조건" value="${common.prgrs}"/>--%>
								<input type="hidden" id="userId" name="userId" value="${LoginVO.userId}" />
								<input type="hidden" id="dstrtCd" name="dstrtCd" value="${dstrtCd}" />
								<input type="hidden" id="view-rqst-duration" name="view-rqst-duration" value="${viewRqstDuration}" />
							</div>
						</div>
					</div>
					<div class="col-xs-4 text-right">
						<button type="button" class="btn btn-default btn-xs" id="btn-normal-display" data-toggle="tooltip" data-placement="top" title="선택영상·폴 해제, DIV 초기화">기본화면</button>
				   <%-- <button type="button" class="btn btn-default btn-xs" style='margin-right:3px' id="btn-refresh" data-toggle="tooltip" data-placement="top" title="" data-original-title="목록을 갱신합니다.">새로고침</button> --%>
						<button type="button" class="btn btn-primary btn-xs btn-search pull-right" title="목록을 검색합니다." onclick="oViewFirstRqst.view.rqst.grid.search(1);">검색</button>
					</div>
				</div>
			</div>
			<table id="grid-rqst-view"></table>
			<div id="paginate-rqst-view" class="paginate text-center"></div>
		</div>
		<div class="panel-footer text-right">
			<%--
			<span id="spn-chk-designation" class="hide">
				<label class="tooltip-personEventMntr margin-zero" data-toggle="tooltip" data-placement="top" title="선택영상 버튼이 활성화 됩니다.">
					<input type="checkbox" id="chk-designation" name="chk-designation">
					<span style="position: relative; top: -3px; padding-left: 3px;">선택영상</span>
				</label>
			</span>
			--%>
			<%-- FIXME 과거영상일 경우 $('#chk-cctv-search-yn').prop('checked', true); --%>
			<input type="checkbox" class="hide" id="chk-cctv-search-yn">
			<button type="button" class="btn btn-primary btn-xs" title="신규 열람신청을 등록합니다." onclick="oViewFirstRqst.view.rqst.request.init();">신규</button>
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
<script>
	oConfigure.mntrViewLeft = 1200;
	/*
	 * Left, Right, Bottom 영역 펴고 접기
	*/
	function collapse(obj, callback) {
		let isCollapsed = false;
		let isForce = obj.hasOwnProperty('force') ? obj.force : false;

		let oToogle = {
			up: contextRoot + '/images/mntr/layout/toggle_up.png',
			down: contextRoot + '/images/mntr/layout/toggle_down.png',
			left: contextRoot + '/images/mntr/layout/toggle_left.png',
			right: contextRoot + '/images/mntr/layout/toggle_right.png',
		}

		let nHeightFooter = $('footer').height() + 1;

		let oSize = {
			left: parseInt(oConfigure.mntrViewLeft),
			right: parseInt(oConfigure.mntrViewRight),
			toogleBar: 10,
			bottomHide: nHeightFooter,
			bottomShow: parseInt(oConfigure.mntrViewBottom)
		}

		const $asideLeft = $('aside#left');
		const $asideRight = $('aside#right');
		const $sectionBottom = $('section#bottom');
		const $toggleLeft = $('#toggleLeft');
		const $toggleRight = $('#toggleRight');
		const $toggleBottom = $('#toggleBottom');
		const $body = $('#body');


		if (obj.hasOwnProperty('left')) {
			let isChkDivHdnLeft = $asideLeft.is(':visible');
			let isHidden = obj.left;
			if ((isChkDivHdnLeft && isHidden) || isForce) {
				let isChkDivHdnBottom = $sectionBottom.is(':visible');
				if(isChkDivHdnBottom) {
					oCommon.modalAlert('modal-alert', '알림', '영상재생 중 사용 불가');
					return false;
				}
				$asideLeft.hide();
				$toggleLeft.css({
					'left': 0,
					'background-image': 'url("' + oToogle.right + '")'
				});
				$body.css('left', oSize.toogleBar);
				$toggleBottom.css('width', oSize.toogleBar);
				isCollapsed = true;
			} else if ((!isChkDivHdnLeft && !isHidden) || isForce) {
				$asideLeft.show();
				$asideLeft.css('width', oSize.left);
				$toggleLeft.css({
					'left': oSize.left,
					'background-image': 'url("' + oToogle.left + '")'
				});
				$body.css('left', oSize.left + oSize.toogleBar);
				$toggleBottom.css('width', oSize.left);
				isCollapsed = true;
			} else {
				// Do nothing.
			}
		}

		if (obj.hasOwnProperty('right')) {
			let isChkDivHdnRight = $asideRight.is(':visible');
			let isHidden = obj.right;
			if ((isChkDivHdnRight && isHidden) || isForce) {
				$asideRight.hide();
				$toggleRight.css({
					'right': 0,
					'background-image': 'url("' + oToogle.left + '")'
				});
				$body.css('right', oSize.toogleBar);
				isCollapsed = true;
			} else if ((!isChkDivHdnRight && !isHidden) || isForce) {
				$asideRight.show();
				$toggleRight.css({
					'right': oSize.right,
					'background-image': 'url("' + oToogle.right + '")'
				});
				$asideRight.css('width', oSize.right);
				$body.css('right', oSize.right + oSize.toogleBar);
				isCollapsed = true;
			} else {
				// Do nothing.
			}
		}

		if (obj.hasOwnProperty('bottom')) {
			let isChkDivHdnBottom = $sectionBottom.is(':visible');
			let isHidden = obj.bottom;
			if ((isChkDivHdnBottom && isHidden) || isForce) {
				$sectionBottom.hide();
				$asideLeft.show();
				isCollapsed = true;
			} else if ((!isChkDivHdnBottom && !isHidden) || isForce) {
				$sectionBottom.show();
				$asideLeft.hide();
				isCollapsed = true;
			} else {
				// Do nothing.
			}
		}

		// jqGrid Sync
		oCommon.jqGrid.resize();
		if (typeof callback == 'function') {
			callback();
		}

		if (isCollapsed) {
			let isUpdateSize = false;
			if (typeof olMap != 'undefined' && typeof olMap.map != 'undefined' && olMap.map != null) {
				olMap.map.updateSize();
				isUpdateSize = true;
			}
			console.log('===== Collapsed : %o, %s', obj, isUpdateSize);
		}
	}
</script>
<script src="<c:url value='/js/tvo/view/viewFirstRqst.js'/>"></script>
