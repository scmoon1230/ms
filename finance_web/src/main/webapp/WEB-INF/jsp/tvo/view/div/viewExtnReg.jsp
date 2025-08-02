<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-danger" id="view-prod-extn-rqst">
	<div class="panel-heading">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		<h3 class="panel-title">열람기간연장신청</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs" style="margin-bottom: 0px;">--%>
		<table class="table table-xs" style="margin-bottom: 0px;">
			<tbody>
				<tr><th class="bg-info">종료일자</th>
					<td><div class="form-inline">
					
					
					
					
							<div class="form-group form-group-xs">
								<div class="input-group datetimepicker-ymdhms view-prod-extn-rqst-rqstViewEndYmdhms">
									<input type="text" class="form-control input-xs" id="view-prod-extn-rqst-rqstViewEndYmdhms"
										name="view-prod-extn-rqst-rqstViewEndYmdhms" placeholder="열람종료일시" title="열람종료일시" />
									<span class="input-group-addon input-group-addon-xs">
										<i class="far fa-calendar-alt"></i>
									</span>
								</div>
							</div>
						</div>
					</td>
				</tr>
				<tr><th class="bg-info">연장사유</th>
					<td><input type="text" class="form-control input-xs" id="view-prod-extn-rqst-viewExtnRqstRsn"
						 name="view-prod-extn-rqst-viewExtnRqstRsn" placeholder="열람기간연장신청사유" title="열람기간연장신청사유" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-12 text-center">
				<c:if test="${subMenu.indexOf('viewRqst')!=-1}">
					<button type="button" class="btn btn-primary btn-xs" title="신청" onclick="oViewFirstRqst.view.prodExtn.rqst.request();">신청</button>
				<%-- <button type="button" class="btn btn-default btn-xs" title="취소" onclick="oViewFirstRqst.view.prodExtn.cancel();">취소</button> --%>
					<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oViewFirstRqst.view.prodExtn.rqst.close();">취소</button>
				</c:if>
				<c:if test="${subMenu.indexOf('viewRqstExtn')!=-1}">
					<button type="button" class="btn btn-primary btn-xs" title="신청" onclick="oViewExtnRqst.view.prodExtn.request();">신청</button>
				<%-- <button type="button" class="btn btn-default btn-xs" title="취소" onclick="oViewExtnRqst.view.prodExtn.cancel();">취소</button> --%>
					<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oViewExtnRqst.view.prodExtn.close();">취소</button>
				</c:if>
			</div>
		</div>
	</div>
</div>
