<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-danger" id="view-prod-extn-aprv">
	<div class="panel-heading">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		<h3 class="panel-title">열람기간연장 승인</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs" style="margin-bottom: 0px;">--%>
		<table class="table table-xs" style="margin-bottom: 0px;">
			<colgroup>
				<col style="width: 100px;" />
				<col style="width: calc(100% - 100px);" />
			</colgroup>
			<tbody>
				<tr><th class="bg-info">승인구분</th>
					<td><label class="radio-inline" style="position: relative;">
							<input type="radio" name="view-prod-extn-aprv-tvoPrgrsCd" value="30"> 반려
						</label>
						<label class="radio-inline" style="position: relative;">
							<input type="radio" name="view-prod-extn-aprv-tvoPrgrsCd" value="50"> 승인
						</label>
					</td>
				</tr>
				<tr><th class="bg-info">상세내용</th>
					<td><label class="sr-only" for="view-prod-extn-aprv-tvoPrgrsDtl">반려사유</label>
						<input type="text" class="form-control input-xs" id="view-prod-extn-aprv-tvoPrgrsDtl"
							 name="view-prod-extn-aprv-tvoPrgrsDtl" placeholder="반려일 경우 반려사유" title="반려일 경우 반려사유" />
					</td>
				</tr>
				<tr><th class="bg-info">열람종료일시</th>
					<td><div class="form-inline">
							<div class="form-group form-group-xs">
								<div class="input-group datetimepicker-ymdhms view-prod-extn-aprv-aprvViewEndYmdhms">
									<input type="text" class="form-control input-xs" id="view-prod-extn-aprv-aprvViewEndYmdhms"
										 name="view-prod-extn-aprv-aprvViewEndYmdhms" placeholder="열람종료일시" title="열람종료일시" />
									<span class="input-group-addon input-group-addon-xs">
										<i class="far fa-calendar-alt"></i>
									</span>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-12 text-center">
				<button type="button" class="btn btn-primary btn-xs" title="신청" onclick="oViewExtnAprv.view.prodExtn.aprv.approval();">확인</button>
				<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oViewExtnAprv.view.prodExtn.aprv.close();">취소</button>
			</div>
		</div>
	</div>
</div>
