<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-danger" id="view-use-rslt">
	<div class="panel-heading">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		<h3 class="panel-title">열람활용결과</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs" style="margin-bottom: 0px;">--%>
		<table class="table table-xs" style="margin-bottom: 0px;">
			<colgroup>
				<col style="width: 100px;" />
				<col style="width: calc(100% - 100px);" />
			</colgroup>
			<tbody>
				<tr><th class="bg-info">활용결과</th>
					<td><label for="view-use-rslt-viewResultTyCd" class="control-label sr-only">활용결과</label>
						<select id="view-use-rslt-viewResultTyCd" name="view-use-rslt-viewResultTyCd" class="form-control input-xs" title="활용결과"></select>
					</td>
				</tr>
				<tr><th class="bg-info">결과내용</th>
					<td><input type="text" class="form-control input-xs" id="view-use-rslt-viewResultTyDtl" name="view-use-rslt-viewResultTyDtl" placeholder="활용결과상세" title="활용결과상세" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-12 text-center">
				<button type="button" class="btn btn-primary btn-xs" title="등록" onclick="oViewFirstRqst.view.useRslt.register();">등록</button>
			<%--<button type="button" class="btn btn-default btn-xs" title="취소" onclick="oViewFirstRqst.view.useRslt.cancel();">취소</button>--%>
				<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oViewFirstRqst.view.useRslt.close();">취소</button>
			</div>
		</div>
	</div>
</div>