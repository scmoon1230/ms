<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-danger" id="out-check-hash">
	<div class="panel-heading">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		<h3 class="panel-title">암호화영상원본대조</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs">--%>
		<table class="table table-xs" style="margin-bottom: 0px;">
			<tbody>
				<tr><th class="bg-info">원본대조파일</th>
					<td><input type="file" class="form-control input-xs" style="height: 22px;padding: 0;font-size: 10px;"
											id="out-aprv-hash-chechHashFileNm" title="원본대조파일">
						<input type="hidden" id="out-aprv-hash-outRqstNo" name="out-aprv-hash-outRqstNo" value="" />
						<input type="hidden" id="out-aprv-hash-outFileSeq" name="out-aprv-hash-outFileSeq" value="" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-12 text-center">
				<button type="button" class="btn btn-primary btn-xs" title="등록" onclick="oOutFirstAprv.out.checkHash.register();">등록</button>
				<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oOutFirstAprv.out.checkHash.close();">취소</button>
			</div>
		</div>
	</div>
</div>