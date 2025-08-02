<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-primary" id="view-prod-extn-dtl">
	<div class="panel-heading">
		<h3 class="panel-title">열람기간연장신청상세</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs">--%>
		<table class="table table-xs">
			<colgroup>
				<col style="width: 100px;" />
				<col style="width: calc(50% - 100px);" />
				<col style="width: 100px;" />
				<col style="width: calc(50% - 100px);" />
			</colgroup>
			<tbody>
				<tr><th class="bg-info">연장사유</th>				<td>${viewExtnDtl.viewExtnRqstRsn}</td>
					<th class="bg-info">열람종료일자</th>			<td>${viewExtnDtl.viewEndYmdHms.substring(0,10)}</td>
				</tr>
				<tr><th class="bg-info">신청된<br>열람종료일자</th>
					<td><span id="view-prod-extn-dtl-rqstViewEndYmdhms">${viewExtnDtl.rqstViewEndYmdHms.substring(0,10)}</span></td>
					<th class="bg-info">승인된<br>열람종료일자</th>
					<td><c:if test="${viewExtnDtl.aprvViewEndYmdHms ne ''}">${viewExtnDtl.aprvViewEndYmdHms.substring(0,10)}</c:if></td>
				</tr>
				<tr><th class="bg-info">연장신청일시</th>			<td>${viewExtnDtl.viewExtnRqstYmdHms.substring(0,16)} ( ${viewExtnDtl.viewExtnRqstUserNm} )</td>
					<th class="bg-info">진행상태</th>
					<td>${viewExtnDtl.tvoPrgrsNm}
						<c:if test="${not empty viewExtnDtl and subMenu.indexOf('viewExtnAprv')!=-1 and viewExtnDtl.tvoPrgrsCd eq '10'}">
							&nbsp; <button type="button" class="btn btn-warning btn-xs" title="승인하기" onclick="oViewExtnAprv.view.prodExtn.aprv.open();">승인하기</button>
						</c:if>
						<c:if test="${viewExtnDtl.tvoPrgrsCd eq '50'}">( ${viewExtnDtl.tvoPrgrsYmdHms.substring(0,16)} / ${viewExtnDtl.viewExtnAprvUserNm} )	</c:if>
						<c:if test="${viewExtnDtl.tvoPrgrsCd eq '51'}">( ${viewExtnDtl.tvoPrgrsYmdHms.substring(0,16)} / ${viewExtnDtl.viewExtnAprvUserNm} )	</c:if>
						<c:if test="${viewExtnDtl.tvoPrgrsCd eq '30'}">( ${viewExtnDtl.tvoPrgrsYmdHms.substring(0,16)} / ${viewExtnDtl.viewExtnAprvUserNm} )	</c:if>
						<c:if test="${viewExtnDtl.tvoPrgrsDtl ne ''}"><br/>${viewExtnDtl.tvoPrgrsDtl}															</c:if>
					</td>
				</tr>
				<tr style="display:none;">
					<th><label class="control-label sr-only">열람연장신청일시</label></th>
					<td><span id="view-prod-extn-dtl-viewExtnRqstYmdhms">${viewExtnDtl.viewExtnRqstYmdHms}</span>
					</td>
				</tr>
				<%--
				<tr><th class="bg-info"><label class="control-label sr-only">열람신청번호</label></th>
					<td><input type="hidden" id="view-prod-extn-dtl-viewRqstNo" name="view-prod-extn-dtl-viewRqstNo" value="${viewExtnDtl.viewRqstNo}" /></td>
				</tr>
				 --%>
			</tbody>
		</table>
	</div>
	<%--
			<div class="panel-footer text-center">
				<button type="button" class="btn btn-primary btn-xs" title="신청수정" onclick="">신청수정</button>
				<button type="button" class="btn btn-secondary btn-xs" title="신청취소" onclick="">신청취소</button>
			</div>
	 --%>
</div>
