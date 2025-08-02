<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-danger" id="out-prod-extn-rqst">
	<div class="panel-heading">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		<h3 class="panel-title">반출기간연장신청</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs" style="margin-bottom: 0px;">--%>
		<table class="table table-xs" style="margin-bottom: 0px;">
			<tbody>
				<tr><th class="bg-info">종료일자</th>
					<td><div class="form-inline">
							<div class="form-group form-group-xs">
								<label for="out-prod-extn-rqst-type" class="control-label sr-only">기간연장타입</label>
								<input type="hidden" id="out-prod-extn-rqst-type" name="out-prod-extn-rqst-type" />
							</div>
							<div class="form-group form-group-xs">
								<div class="input-group datetimepicker-ymdhms out-prod-extn-rqst-rqstPlayEndYmdhms">
									<input type="text" class="form-control input-xs" id="out-prod-extn-rqst-rqstPlayEndYmdhms"
										name="out-prod-extn-rqst-rqstPlayEndYmdhms" placeholder="재생종료일시" title="재생종료일시" />
									<span class="input-group-addon input-group-addon-xs">
										<i class="far fa-calendar-alt"></i>
									</span>
								</div>
							</div>
						</div>
					</td>
				</tr>
				<tr><th class="bg-info">연장사유</th>
					<td><input type="text" class="form-control input-xs" id="out-prod-extn-rqst-outExtnRqstRsn"
						 name="out-prod-extn-rqst-outExtnRqstRsn" placeholder="반출기간연장사유" title="반출기간연장사유" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-12 text-center">
				<button type="button" class="btn btn-primary btn-xs" title="신청" onclick="oOutFirstRqst.out.prodExtn.request();">신청</button>
				<%-- <button type="button" class="btn btn-default btn-xs" title="취소" onclick="oOutFirstRqst.out.prodExtn.cancel();">취소</button> --%>
				<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oOutFirstRqst.out.prodExtn.close();">취소</button>
			</div>
		</div>
	</div>
</div>
