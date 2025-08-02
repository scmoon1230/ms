<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-primary" id="view-rqst-dtl">
	<div class="panel-heading">
		<h3 class="panel-title">열람신청상세</h3>
	</div>
	<div class="panel-body">
		<c:choose>
			<c:when test="${not empty viewRqstDtl}">
				<%--<table class="table table-striped table-xs">--%>
				<table class="table table-xs">
					<colgroup>
						<col style="width: 100px;" />
						<col style="width: calc(50% - 100px);" />
						<col style="width: 100px;" />
						<col style="width: calc(50% - 100px);" />
					</colgroup>
					<tbody>
						<tr><th class="bg-info">사건번호</th>				<td>${viewRqstDtl.evtNo}</td>
							<th class="bg-info">사건명</th>				<td>${viewRqstDtl.evtNm}</td>
						</tr>
						<tr><th class="bg-info">발생일시</th>				<td><span id="view-rqst-dtl-evtYmdhms">${viewRqstDtl.evtYmdHms.substring(0,16)}</span></td>
							<th class="bg-info">발생주소</th>
							<td>${viewRqstDtl.evtAddr}
								<button type="button" class="btn btn-default btn-xs" id="view-rqst-dtl-map-pin" title="발생위치로 이동" onclick="oTvoCmn.map.moveTo(this);">
									<i class="fas fa-map-pin"></i>
								</button>
							</td>
						</tr>
						<tr>
							<th class="bg-info">신청사유</th>
							<td>${viewRqstDtl.rqstRsnTyNm}
								<input type="hidden" id="view-rqst-dtl-rqstRsnTyCd" name="view-rqst-dtl-rqstRsnTyCd" value="${viewRqstDtl.rqstRsnTyCd}" />
							</td>
							<th class="bg-info">신청사유상세</th>			<td>${viewRqstDtl.rqstRsnDtl}</td>
						</tr>
						<tr><th class="bg-info">열람종료일자</th>
							<td><span>(신청)</span> <span id="view-rqst-dtl-viewEndYmdhmsWant">${viewRqstDtl.viewEndYmdHmsWant.substring(0,10)}</span>
								<c:if test="${not empty viewRqstDtl.viewEndYmdHms}">
									<br><span>(승인)</span> <span id="view-rqst-dtl-viewEndYmdhms">${viewRqstDtl.viewEndYmdHms.substring(0,10)}</span>
								</c:if>
								<c:if test="${viewRqstDtl.viewPmsYn eq 'N'}">	<%-- 열람허용불가일 때 --%>
									[열람불가]
								</c:if>
								<c:if test="${viewRqstDtl.viewEndYn eq 'Y'}">	<%-- 열람기간종료일 때 --%>
								</c:if>
								<c:if test="${subMenu.indexOf('viewRqst')!=-1 and not empty viewRqstDtl.viewEndYmdHms and (viewRqstDtl.tvoPrgrsCd eq '50' or viewRqstDtl.tvoPrgrsCd eq '51')}">
								&nbsp;
								<%--<button type="button" class="btn btn-default btn-xs" title="기간연장" onclick="oViewFirstRqst.view.prodExtn.rqst.init();">기간연장</button>--%>
									<button type="button" class="btn btn-warning btn-xs" title="기간연장" onclick="oViewFirstRqst.view.prodExtn.rqst.open();">기간연장</button>
								</c:if>
							</td>
							<th class="bg-info">긴급구분</th>
							<td>${viewRqstDtl.emrgYnNm}
								<input type="hidden" id="view-rqst-dtl-emrgYn" name="view-rqst-dtl-emrgYn" value="${viewRqstDtl.emrgYn}" />
								<c:if test="${viewRqstDtl.emrgRsn ne ''}">
									<br/>${viewRqstDtl.emrgRsn}
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="bg-info">공문파일</th>
							<td><c:if test="${viewRqstDtl.paperFileNm == null || viewRqstDtl.paperFileNm == ''}">
									<c:if test="${subMenu.indexOf('viewRqst')!=-1 and (viewRqstDtl.tvoPrgrsCd eq '50' or viewRqstDtl.tvoPrgrsCd eq '51')}">
									<%--<button type="button" class="btn btn-default btn-xs" title="공문첨부" onclick="oViewFirstRqst.view.paperFile.init();">공문첨부</button>--%>
										<button type="button" class="btn btn-warning btn-xs" title="공문첨부"
											 onclick="oViewFirstRqst.view.paperFile.open('${viewRqstDtl.viewRqstNo}','${viewRqstDtl.viewRqstYmdhms}');">공문첨부</button>
									</c:if>
								</c:if>
								<c:if test="${viewRqstDtl.paperFileNm != null && viewRqstDtl.paperFileNm != ''}">
									${viewRqstDtl.paperFileNm}
									<button type="button" class="btn btn-default btn-xs" title="공문 다운로드" onclick="oTvoCmn.download.paperFile();">
										<i class="fas fa-download"></i>
									</button>
								</c:if>
								<input type="hidden" id="view-rqst-dtl-paperNm" name="view-rqst-dtl-paperNm" value="${viewRqstDtl.paperNm}" />
								<input type="hidden" id="view-rqst-dtl-paperFileNm" name="view-rqst-dtl-paperFileNm" value="${viewRqstDtl.paperFileNm}" />
							</td>
							<th class="bg-info">전자문서번호</th>
							<td>${viewRqstDtl.paperNo}
								<input type="hidden" id="view-rqst-dtl-paperNo" name="view-rqst-dtl-paperNo" value="${viewRqstDtl.paperNo}" />
							</td>
						</tr>
						<tr><th class="bg-info">발생지구</th>				<td>${viewRqstDtl.evtDstrtCd}</td>
							<th class="bg-info">신청지구</th>				<td>${viewRqstDtl.dstrtCd}</td>
						</tr>
						<tr><th class="bg-info">열람활용결과</th>
							<td><span>
									<c:choose>
										<c:when test="${not empty viewRqstDtl.viewResultTyCd and not empty viewRqstDtl.viewResultTyNm}">
											[${viewRqstDtl.viewResultTyNm}]
										</c:when>
										<c:otherwise>[미등록]</c:otherwise>
									</c:choose>
									<c:if test="${not empty viewRqstDtl.viewResultTyDtl}"><br/> ${viewRqstDtl.viewResultTyDtl}</c:if>
									<input type="hidden" id="view-rqst-dtl-viewResultTyCd" name="view-rqst-dtl-viewResultTyCd" value="${viewRqstDtl.viewResultTyCd}" />
									<input type="hidden" id="view-rqst-dtl-viewResultTyDtl" name="view-rqst-dtl-viewResultTyDtl" value="${viewRqstDtl.viewResultTyDtl}" />
									<!-- <input type="hidden" id="view-rqst-dtl-viewResultTy" name="view-rqst-dtl-viewResultTy" value="${viewRqstDtl.viewResultTy}" /> -->
								</span>
								&nbsp;
								<c:if test="${subMenu.indexOf('viewRqst')!=-1 and (viewRqstDtl.tvoPrgrsCd eq '50' or viewRqstDtl.tvoPrgrsCd eq '51')}">
									<%-- <button type="button" class="btn btn-default btn-xs" title="열람활용결과" onclick="oViewFirstRqst.view.useRslt.init();">결과등록</button> --%>
									<button type="button" class="btn btn-warning btn-xs" title="열람활용결과" onclick="oViewFirstRqst.view.useRslt.open();">결과등록</button>
								</c:if>
							</td>
							<th class="bg-info">열람신청번호</th>
							<td>${viewRqstDtl.viewRqstNo}
								<c:if test="${not empty viewRqstDtl and subMenu.indexOf('viewAprv')!=-1 and LoginVO.userId=='sys'}">	<!-- sys계정으로만 -->
									<button type="button" class="btn btn-danger btn-xs" title="완전삭제" onclick="oViewFirstAprv.view.aprv.deleteComplete();">완전삭제</button>
								</c:if>
							</td>
						</tr>
						<tr><th class="bg-info">열람신청일시</th>		<td>${viewRqstDtl.viewRqstYmdHms.substring(0,16)} ( ${viewRqstDtl.viewRqstUserNm} )</td>
							<th class="bg-info">진행상태</th>
							<td>${viewRqstDtl.tvoPrgrsNm}
								<c:if test="${not empty viewRqstDtl and subMenu.indexOf('viewAprv')!=-1 and viewRqstDtl.tvoPrgrsCd eq '10'}">
									&nbsp; <button type="button" class="btn btn-warning btn-xs" title="승인하기" onclick="oViewFirstAprv.view.aprv.open();">승인하기</button>
								</c:if>
								<c:if test="${viewRqstDtl.tvoPrgrsCd eq '50'}">( ${viewRqstDtl.tvoPrgrsYmdHms.substring(0,16)} / ${viewRqstDtl.viewAprvUserNm} )	</c:if>
								<c:if test="${viewRqstDtl.tvoPrgrsCd eq '51'}">( ${viewRqstDtl.tvoPrgrsYmdHms.substring(0,16)} )									</c:if>
								<c:if test="${viewRqstDtl.tvoPrgrsCd eq '30'}">( ${viewRqstDtl.tvoPrgrsYmdHms.substring(0,16)} / ${viewRqstDtl.viewAprvUserNm} )	</c:if>
								<c:if test="${viewRqstDtl.tvoPrgrsDtl ne ''}"><br/>${viewRqstDtl.tvoPrgrsDtl}														</c:if>
								<c:if test="${not empty viewRqstDtl and subMenu.indexOf('viewAprv')!=-1 and viewRqstDtl.tvoPrgrsCd ne '10'}">
									&nbsp; <button type="button" class="btn btn-warning btn-xs" title="승인취소" onclick="oViewFirstAprv.view.aprv.reset();">승인취소</button>
								</c:if>
							</td>
						</tr>
						<tr style="display:none;">
							<th><label class="control-label sr-only">신청지구,열람신청번호,열람신청일시,상태코드,열람종료여부,열람가능여부</label></th>
							<td><input type="text" id="view-rqst-dtl-dstrtCd"        name="view-rqst-dtl-dstrtCd"        value="${viewRqstDtl.dstrtCd}"        />
								<input type="text" id="view-rqst-dtl-viewRqstNo"     name="view-rqst-dtl-viewRqstNo"     value="${viewRqstDtl.viewRqstNo}"     />
								<input type="text" id="view-rqst-dtl-viewRqstYmdhms" name="view-rqst-dtl-viewRqstYmdhms" value="${viewRqstDtl.viewRqstYmdhms}" />
								<input type="text" id="view-rqst-dtl-tvoPrgrsCd"     name="view-rqst-dtl-tvoPrgrsCd"     value="${viewRqstDtl.tvoPrgrsCd}"     />
								<input type="text" id="view-rqst-dtl-viewEndYn"      name="view-rqst-dtl-viewEndYn"      value="${viewRqstDtl.viewEndYn}"      />
								<input type="text" id="view-rqst-dtl-viewPmsYn"      name="view-rqst-dtl-viewPmsYn"      value="${viewRqstDtl.viewPmsYn}"      />
								<input type="text" id="view-rqst-dtl-viewRqstUserId" name="view-rqst-dtl-viewRqstUserId" value="${viewRqstDtl.viewRqstUserId}" />
							</td>
							<th>발생위치</th>
							<td><span>X: </span> <mark id="view-rqst-dtl-evtPointX">${viewRqstDtl.evtPointX}</mark>
								<span>Y: </span> <mark id="view-rqst-dtl-evtPointY">${viewRqstDtl.evtPointY}</mark>
								<button type="button" class="btn btn-default btn-xs" title="발생위치로 이동" onclick="oTvoCmn.map.moveTo(this);">
									<i class="fas fa-map-pin"></i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</c:when>
			<c:otherwise>
				<div class="alert alert-danger" role="alert" style="margin-bottom: 0px;">
					<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
					<span class="sr-only">Error:</span>
					<c:out value="${msg}" />
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<c:if test="${subMenu.indexOf('viewRqst')!=-1 and viewRqstDtl.tvoPrgrsCd eq '10'}">
		<c:if test="${viewRqstDtl.viewResultTyCd eq null}">
			<div class="panel-footer text-center">
				<button type="button" class="btn btn-primary btn-xs" title="신청수정" onclick="oViewFirstRqst.view.rqst.modify.init();">신청수정</button>
				<button type="button" class="btn btn-secondary btn-xs" title="신청취소" onclick="oViewFirstRqst.view.rqst.cancel();">신청취소</button>
			</div>
		</c:if>
	</c:if>

	<%--	
	<c:if test="${subMenu.indexOf('viewRqst')!=-1 and (viewRqstDtl.tvoPrgrsCd eq '50' or viewRqstDtl.tvoPrgrsCd eq '51')}">
	</c:if>
	<c:if test="${viewRqstDtl.tvoPrgrsCd eq '50' or viewRqstDtl.tvoPrgrsCd eq '51'}">
		<div class="panel-footer text-right">
			<button type="button" class="btn btn-success btn-xs" title="반출신청목록" onclick="oViewFirstRqst.out.rqst.open();">반출신청목록</button>
			<button type="button" class="btn btn-info btn-xs" title="열람기간연장이력" onclick="oViewFirstRqst.view.prodExtn.open();">열람기간연장이력</button>
		</div>
	</c:if>
	 --%>
</div>
