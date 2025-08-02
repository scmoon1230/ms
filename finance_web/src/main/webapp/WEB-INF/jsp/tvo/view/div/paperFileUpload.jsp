<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-danger" id="view-paper-file">
	<div class="panel-heading">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		<h3 class="panel-title">공문첨부</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs">--%>
		<table class="table table-xs" style="margin-bottom: 0px;">
			<tbody>
				<tr><th class="bg-info">공문파일</th>
					<td><input type="file" class="form-control input-xs" style="height: 22px;padding: 0;font-size: 10px;" id="view-rqst-paper-paperFileNm" title="공문첨부">
					</td>
				</tr>
				<tr><th class="bg-info">전자문서번호</th>
					<td><input type="text" class="form-control input-xs" id="view-rqst-paper-paperNo" name="view-rqst-paper-paperNo" placeholder="전자문서번호" title="전자문서번호" value="" />
						<input type="hidden" id="view-rqst-paper-dstrtCd" name="view-rqst-paper-dstrtCd" value="" />
						<input type="hidden" id="view-rqst-paper-viewRqstNo" name="view-rqst-paper-viewRqstNo" value="" />
						<input type="hidden" id="view-rqst-paper-viewRqstYmdhms" name="view-rqst-paper-viewRqstYmdhms" value="" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-12 text-center">
				<button type="button" class="btn btn-primary btn-xs" title="등록" onclick="oViewFirstRqst.view.paperFile.register();">등록</button>
			<%--<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oViewFirstRqst.view.paperFile.cancel();">취소</button>--%>
				<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oViewFirstRqst.view.paperFile.close();">취소</button>
			</div>
		</div>
	</div>
</div>