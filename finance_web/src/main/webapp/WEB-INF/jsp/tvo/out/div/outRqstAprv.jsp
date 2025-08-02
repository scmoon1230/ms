<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-danger" id="out-rqst-aprv">
	<div class="panel-heading">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		<h3 class="panel-title">입수/반출 승인</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs">--%>
		<table class="table table-xs" style="margin-bottom: 0px;">
			<colgroup>
				<col style="width: 100px;" />
				<col style="width: calc(100% - 100px);" />
			</colgroup>
			<tbody>
				<tr><th class="bg-info">승인구분</th>
					<td><label class="radio-inline" style="position: relative;">
							<input type="radio" name="out-rqst-aprv-yn" value="N"> 반려
						</label>
						<label class="radio-inline" style="position: relative;">
							<input type="radio" name="out-rqst-aprv-yn" value="Y"> 승인
						</label>
					</td>
				</tr>
				<tr><th class="bg-info">상세내용</th>
					<td><label class="sr-only" for="out-rqst-aprv-tvoPrgrsDtl">반려사유</label>
						<input type="text" class="form-control input-xs" id="out-rqst-aprv-tvoPrgrsDtl" name="out-rqst-aprv-tvoPrgrsDtl" placeholder="반려일 경우 반려사유"
							title="반려일 경우 반려사유" />
					</td>
				</tr>
				<tr id="tr-out-rqst-aprv-playEndYmdhms">
					<th class="bg-info">재생종료일시</th>
					<td><div class="form-inline">
							<div class="form-group form-group-xs">
								<div class="input-group datetimepicker-ymdhms out-rqst-aprv-playEndYmdhms">
									<input type="text" class="form-control input-xs" id="out-rqst-aprv-playEndYmdhms" name="out-rqst-aprv-playEndYmdhms"
										placeholder="재생종료일시" title="재생종료일시" />
									<span class="input-group-addon input-group-addon-xs">
										<i class="far fa-calendar-alt"></i>
									</span>
								</div>
							</div>
						</div>
					</td>
				</tr>
			<%--<tr><th class="bg-info">재생횟수</th>
					<td><label class="sr-only" for="out-rqst-aprv-playCnt">재생횟수</label>
						<input type="number" class="form-control input-xs" id="out-rqst-aprv-playCnt" name="out-rqst-aprv-playCnt" placeholder="재생횟수" title="재생횟수" />
					</td>
				</tr>--%>
			</tbody>
		</table>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-12 text-center">
				<button type="button" class="btn btn-primary btn-xs" title="확인" onclick="oOutFirstAprv.out.aprv.approve();">확인</button>
			<%--<button type="button" class="btn btn-default btn-xs" title="취소" onclick="oOutFirstAprv.out.aprv.cancel();">취소</button>--%>
				<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oOutFirstAprv.out.aprv.close();">취소</button>
			</div>
		</div>
	</div>
</div>

