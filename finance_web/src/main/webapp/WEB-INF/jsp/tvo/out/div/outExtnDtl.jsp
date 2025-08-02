<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-primary" id="out-prod-extn-dtl">
	<div class="panel-heading">
		<h3 class="panel-title">반출기간연장신청상세</h3>
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
				<tr><th class="bg-info">연장사유</th>				<td>${outProdExtn.outExtnRqstRsn}</td>
					<th class="bg-info">재생종료일자</th>			<td>${outProdExtn.playEndYmdHms.substring(0,10)}</td>
				</tr>
				<tr><th class="bg-info">신청된<br>재생종료일자</th>
					<td><span id="out-prod-extn-dtl-rqstPlayEndYmdhms">${outProdExtn.rqstPlayEndYmdHms.substring(0,10)}</span></td>
					<th class="bg-info">승인된<br>재생종료일자</th>
					<td><span id="out-prod-extn-dtl-aprvPlayEndYmdhms"><c:if test="${outProdExtn.aprvPlayEndYmdHms ne ''}">${outProdExtn.aprvPlayEndYmdHms.substring(0,10)}</c:if></span></td>
				</tr>
				<tr><th class="bg-info">연장신청일시</th>			<td>${outProdExtn.outExtnRqstYmdHms.substring(0,16)} ( ${outProdExtn.outExtnRqstUserNm} )</td>
					<th class="bg-info">진행상태</th>
					<td>${outProdExtn.tvoPrgrsNm}
						<c:if test="${not empty outProdExtn and subMenu.indexOf('outExtnAprv')!=-1 and outProdExtn.tvoPrgrsCd eq '10'}">
							&nbsp; <button type="button" class="btn btn-warning btn-xs" title="승인하기" onclick="oOutExtnAprv.out.prodExtn.aprv.open();">승인하기</button>
						</c:if>
						<c:if test="${outProdExtn.tvoPrgrsCd eq '50'}">( ${outProdExtn.tvoPrgrsYmdHms.substring(0,16)} / ${outProdExtn.outExtnAprvUserNm} )	</c:if>
						<c:if test="${outProdExtn.tvoPrgrsCd eq '51'}">( ${outProdExtn.tvoPrgrsYmdHms.substring(0,16)} / ${outProdExtn.outExtnAprvUserNm} )	</c:if>
						<c:if test="${outProdExtn.tvoPrgrsCd eq '30'}">( ${outProdExtn.tvoPrgrsYmdHms.substring(0,16)} / ${outProdExtn.outExtnAprvUserNm} )	</c:if>
						<c:if test="${outProdExtn.tvoPrgrsDtl ne ''}"><br/>${outProdExtn.tvoPrgrsDtl}														</c:if>
					</td>
				</tr>
				<tr style="display:none;">
					<th><label class="control-label sr-only">반출연장신청일시,진행상태코드</label></th>
					<td><span id="out-prod-extn-dtl-outExtnRqstYmdhms">${outProdExtn.outExtnRqstYmdHms}</span>
						<input type="hidden" id="out-extn-dtl-tvoPrgrsCd" name="out-extn-dtl-tvoPrgrsCd" value="${outProdExtn.tvoPrgrsCd}" />
					</td>
				</tr>
				<%--
				<tr style="display:none;">
					<th class="bg-info"><label for="out-prod-extn-dtl-outRqstNo" class="control-label sr-only">반출신청번호</label></th>
					<td><input type="hidden" id="out-prod-extn-dtl-outRqstNo" name="out-prod-extn-dtl-outRqstNo" value="${outProdExtn.outRqstNo}" /></td>
					<th class="bg-info"><label class="control-label sr-only">진행상태코드</label></th>
					<td>
					</td>
				</tr>
				 --%>
			</tbody>
		</table>
	</div>
</div>
