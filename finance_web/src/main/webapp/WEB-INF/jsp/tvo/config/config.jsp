<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<style>
    aside#left {
        display: none;
    }

    .row.bg-info .form-group, .row.bg-warning .form-group {
        margin: 0;
        padding: 3px;
    }

    .row.bg-info, .row.bg-warning {
        display: flex;
        align-items: center;
    }

    .row.bg-info label, .row.bg-warning label {
        margin-bottom: 0;
    }

    .row {
        display: flex;
        align-items: center;
        min-height: 30px;
    }

    .video-search .col-xs-4 {
        padding-left: 16px;
    }

    .video-search .col-xs-4 + .col-xs-4 {
        padding-left: 20px;
        padding-right: 6px;
    }

    .video-search .col-xs-4 + .col-xs-4 + .col-xs-4 {
        padding-left: 24px;
    }
</style>
<article id="article-grid" style="overflow-y: scroll;">

				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>

	<div class="searchBox sm">
		<div class="form-inline">
		
			<div class="form-group">
				<button type="button" class="btn btn-default btn-sm" onclick="savePveConfig();">저장</button>
			</div>
		</div>
	</div>

	<div class="row bg-info">
		<label class="col-xs-2 control-label">열람</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="gisTy" class="control-label">열람 자동승인</label>
					</div>
				</div>
				<div class="col-xs-7">
					<div class="form-group">
						<input type="hidden" id="dstrtCd" name="dstrtCd" value="<prprts:value key='DSTRT_CD' />" />
						<label class="radio-inline">
							<input type="radio" id="viewAutoAprvYn1" name="viewAutoAprvYn" title="미사용" value="N" />자동승인 미사용
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="viewAutoAprvYn2" name="viewAutoAprvYn" title="일괄사용" value="Y" />일괄자동승인 사용: 긴급,일반 구분없이 자동승인처리
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="viewAutoAprvYn3" name="viewAutoAprvYn" title="긴급처리" value="U" />긴급시 자동승인 사용 (시간제약없음): 긴급시 요청시간 관계없이 자동승인처리
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="viewAutoAprvYn4" name="viewAutoAprvYn" title="긴급처리" value="T" />긴급시 자동승인 사용 (시간제약): 긴급시 요청시간내 자동승인처리
						</label>
						<br/> &nbsp; &nbsp; &nbsp;
						<select class="input-xs" id="viewAutoAprvStart" name="viewAutoAprvStart" title="시작">
							<c:forEach var="ind" begin="0" end="24" step="1">
								<option value="<fmt:formatNumber value='${ind}' pattern='00'/>" ><fmt:formatNumber value='${ind}' pattern='00'/>시</option>
							</c:forEach>
						</select>
						~
						<select class="input-xs" id="viewAutoAprvEnd" name="viewAutoAprvEnd" title="종료">
							<c:forEach var="ind" begin="0" end="24" step="1">
								<option value="<fmt:formatNumber value='${ind}' pattern='00'/>" ><fmt:formatNumber value='${ind}' pattern='00'/>시</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-xs-1">
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="gisTy" class="control-label">열람기간연장 자동승인</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="viewExtnAutoAprvYn1" name="viewExtnAutoAprvYn" title="미사용" value="N" />자동승인 미사용
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="viewExtnAutoAprvYn2" name="viewExtnAutoAprvYn" title="일괄사용" value="Y" />일괄자동승인 사용: 긴급,일반 구분없이 자동승인처리
						</label>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="viewRqstDuration" class="control-label">열람신청기간 (일) (기본값: 14)</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="viewRqstDuration" name="viewRqstDuration" min="1" max="999"
						value="<prprts:value key='VIEW_RQST_DURATION' />" size="2" title="열람신청기간">
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="영상열람신청 시 기본으로 적용되는 영상열람기간">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
		</div>
	</div>

	<div class="row bg-warning">
		<label class="col-xs-2 control-label">반출</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="iconTy" class="control-label">입수 자동승인</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="outAutoAprvYn1" name="outAutoAprvYn" title="미사용" value="N" />자동승인 미사용
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="outAutoAprvYn2" name="outAutoAprvYn" title="일괄사용" value="Y" />일괄자동승인 사용: 긴급,일반 구분없이 자동승인처리
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="outAutoAprvYn3" name="outAutoAprvYn" title="긴급처리" value="U" />긴급시 자동승인 사용 (시간제약없음): 긴급시 요청시간 관계없이 자동승인처리
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="outAutoAprvYn4" name="outAutoAprvYn" title="긴급처리" value="T" />긴급시 자동승인 사용 (시간제약): 긴급시 요청시간내 자동승인처리
						</label>
						<br/> &nbsp; &nbsp; &nbsp;
						<select class="input-xs" id="outAutoAprvStart" name="outAutoAprvStart" title="시작">
							<c:forEach var="ind" begin="0" end="24" step="1">
								<option value="<fmt:formatNumber value='${ind}' pattern='00'/>" ><fmt:formatNumber value='${ind}' pattern='00'/>시</option>
							</c:forEach>
						</select>
						~
						<select class="input-xs" id="outAutoAprvEnd" name="outAutoAprvEnd" title="종료">
							<c:forEach var="ind" begin="0" end="24" step="1">
								<option value="<fmt:formatNumber value='${ind}' pattern='00'/>" ><fmt:formatNumber value='${ind}' pattern='00'/>시</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="iconTy" class="control-label">반출 자동승인</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="outDrmAutoAprvYn1" name="outDrmAutoAprvYn" title="미사용" value="N" />자동승인 미사용
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="outDrmAutoAprvYn2" name="outDrmAutoAprvYn" title="일괄사용" value="Y" />일괄자동승인 사용: 긴급,일반 구분없이 자동승인처리
						</label>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="iconTy" class="control-label">반출기간연장 자동승인</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="outExtnAutoAprvYn1" name="outExtnAutoAprvYn" title="미사용" value="N" />자동승인 미사용
						</label>
						<br/>
						<label class="radio-inline">
							<input type="radio" id="outExtnAutoAprvYn2" name="outExtnAutoAprvYn" title="일괄사용" value="Y" />일괄자동승인 사용: 긴급,일반 구분없이 자동승인처리
						</label>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
		</div>
	</div>
							
	<div class="row bg-info">
		<label class="col-xs-2 control-label">영상파일</label>
		<div class="col-xs-10">
			<!-- 
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="iconTy" class="control-label">원본영상 자동입수</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
							<label class="radio-inline">
								<input type="radio" id="orgVdoAutoRgsYn1" name="orgVdoAutoRgsYn" title="미사용" value="N" />자동입수 미사용
							</label>
							<br/>
							<label class="radio-inline">
								<input type="radio" id="orgVdoAutoRgsYn2" name="orgVdoAutoRgsYn" title="사용" value="Y" />자동입수 사용
							</label>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			 -->
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="recommVdoDuration" class="control-label">권장영상길이 (분) (기본값: 10)</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="recommVdoDuration" name="recommVdoDuration" min="1" max="999"
						value="<prprts:value key='RECOMM_VDO_DURATION' />" size="2" title="권장영상길이">
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="영상반출 신청자에게 표시할 권장영상길이">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="outFilePlayProd" class="control-label">재생가능기간 (일) (기본값: 90)</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="outFilePlayProd" name="outFilePlayProd" min="1" max="99"
						value="<prprts:value key='OUT_FILE_PLAY_PROD' />" size="2" title="반출파일재생기간">
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="outFilePlayProdThird" class="control-label">제3자 제공시 재생가능기간 (일) (기본값: 180)</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="outFilePlayProdThird" name="outFilePlayProdThird" min="1" max="99"
						value="<prprts:value key='OUT_FILE_PLAY_PROD_THIRD' />" size="2" title="반출파일재생기간(제3자)">
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="outFilePlayCnt" class="control-label">재생횟수 (회) (기본값: 50)</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="outFilePlayCnt" name="outFilePlayCnt" min="1" max="99"
						value="<prprts:value key='OUT_FILE_PLAY_CNT' />" size="2" title="반출파일재생횟수">
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="0으로 입력 시 회수제한 없음">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="fileKeepDuration" class="control-label">보관기간 (일) (기본값: 30)</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="fileKeepDuration" name="fileKeepDuration" min="1" max="999"
						value="<prprts:value key='FILE_KEEP_DURATION' />" size="2" title="반출파일보관기간">
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="재생기간 경과 후 서버에 보관하는 기간, 0으로 입력 시 재생만료 익일 삭제">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 
	<div class="row bg-warning" style="display:none;">
		<label class="col-xs-2 control-label">새로고침</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="dashboardRefreshInterval" class="control-label">주기 (초) (기본값: 60)</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="dashboardRefreshInterval" name="dashboardRefreshInterval" min="1" max="999"
						value="<prprts:value key='DASHBOARD_REFRESH_INTERVAL' />" size="2" title="새로고침주기">
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="상황판 화면 새로고침주기">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
		</div>
	</div>
	 -->
	<!-- <div class="row bg-info"> -->
	<div class="row bg-warning">
		<label class="col-xs-2 control-label">알림</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label for="iconTy" class="control-label">승인처리알림</label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
							<label class="radio-inline">
								<input type="radio" id="approveNotifyTy1" name="approveNotifyTy" title="미사용" value="N" />알림 미사용
							</label>
							<br/>
							<label class="radio-inline">
								<input type="radio" id="approveNotifyTy2" name="approveNotifyTy" title="사용" value="Y" />알림 사용
							</label>
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="승인자레벨 사용자에게만 알림 발송">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
		</div>
	</div>

</article>

<script>

	//환경설정
	function savePveConfig() {
		
		var oParams = {
			//dstrtCd					: $('#dstrtCd').val(),
			
			VIEW_AUTO_APRV_YN			: $('input[name=viewAutoAprvYn]:checked').val(),			// 열람 자동승인
			VIEW_AUTO_APRV_START		: $("#viewAutoAprvStart option:selected").val(),
			VIEW_AUTO_APRV_END			: $("#viewAutoAprvEnd option:selected").val(),
			VIEW_EXTN_AUTO_APRV_YN		: $('input[name=viewExtnAutoAprvYn]:checked').val(),		// 열람기간연장 자동승인
			VIEW_RQST_DURATION			: $('#viewRqstDuration').val(),								// 열람신청기간
			
			OUT_AUTO_APRV_YN			: $('input[name=outAutoAprvYn]:checked').val(),				// 입수 자동승인
			OUT_AUTO_APRV_START			: $("#outAutoAprvStart option:selected").val(),
			OUT_AUTO_APRV_END			: $("#outAutoAprvEnd option:selected").val(),
			OUT_DRM_AUTO_APRV_YN		: $('input[name=outDrmAutoAprvYn]:checked').val(),			// 반출 자동승인
			OUT_EXTN_AUTO_APRV_YN		: $('input[name=outExtnAutoAprvYn]:checked').val(),			// 반출기간연장 자동승인
			
			//ORG_VDO_AUTO_RGS_YN         : $('input[name=orgVdoAutoRgsYn]:checked').val(),			// 원본영상 자동입수
			RECOMM_VDO_DURATION			: $('#recommVdoDuration').val(),							// 권장영상길이
			OUT_FILE_PLAY_PROD			: $('#outFilePlayProd').val(),								// 재생기간
			OUT_FILE_PLAY_PROD_THIRD	: $('#outFilePlayProdThird').val(),							// 제3자 제공시 재생가능기간
			OUT_FILE_PLAY_CNT			: $('#outFilePlayCnt').val(),								// 재생횟수
			FILE_KEEP_DURATION			: $('#fileKeepDuration').val(),								// 보관기간
			
		//	DASHBOARD_REFRESH_INTERVAL	: $('#dashboardRefreshInterval').val(),						// 새로고침주기
			
			APPROVE_NOTIFY_TY			: $('input[name=approveNotifyTy]:checked').val()			// 승인처리알림
		};

		var isConfirmed = confirm('저장하시겠습니까?');
		if (isConfirmed) {
			$.ajax({
				type : 'POST',
				async : false,
				dataType : 'json',
				url : contextRoot + '/tvo/config/savePveConfig.json',
				data : oParams,
				success : function(data) {
					var oData = data.result;
					if (oData.result == '1') {
						//alert('정상적으로 저장 되었습니다.');
						goHome();
					}
				},
				error : function(data, status, err) {
					console.log(data);
				}
			});
		}
	}

	function goHome() {
        $('<form/>', {
            'action': contextRoot + '/wrks/lgn/goHome.do'
        }).appendTo(document.body).submit();
	}

	
	$('input[name="viewAutoAprvYn"][value="<prprts:value key='VIEW_AUTO_APRV_YN' />"]').prop("checked",true);			// 열람 자동승인
	$("#viewAutoAprvStart").val("<prprts:value key='VIEW_AUTO_APRV_START' />").prop("selected", true);					// 열람자동승인 시작
	$("#viewAutoAprvEnd").val("<prprts:value key='VIEW_AUTO_APRV_END' />").prop("selected", true);						// 열람자동승인 종료
	$('input[name="viewExtnAutoAprvYn"][value="<prprts:value key='VIEW_EXTN_AUTO_APRV_YN' />"]').prop("checked",true);	// 열람연장 자동승인
	
	$('input[name="outAutoAprvYn"][value="<prprts:value key='OUT_AUTO_APRV_YN' />"]').prop("checked",true);				// 입수 자동승인
	$("#outAutoAprvStart").val("<prprts:value key='OUT_AUTO_APRV_START' />").prop("selected", true);					// 입수자동승인 시작
	$("#outAutoAprvEnd").val("<prprts:value key='OUT_AUTO_APRV_END' />").prop("selected", true);						// 입수자동승인 종료
	$('input[name="outDrmAutoAprvYn"][value="<prprts:value key='OUT_DRM_AUTO_APRV_YN' />"]').prop("checked",true);		// 반출 자동승인
	$('input[name="outExtnAutoAprvYn"][value="<prprts:value key='OUT_EXTN_AUTO_APRV_YN' />"]').prop("checked",true);	// 반출연장 자동승인

	//$('input[name="orgVdoAutoRgsYn"][value="<prprts:value key='ORG_VDO_AUTO_RGS_YN' />"]').prop("checked",true);		// 원본영상 자동입수
	
	$('input[name="approveNotifyTy"][value="<prprts:value key='APPROVE_NOTIFY_TY' />"]').prop("checked",true);			// 승인처리알림
	
</script>
