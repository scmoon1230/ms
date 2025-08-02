<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-primary" id="out-rqst-dtl">
	<div class="panel-heading">
		<h3 class="panel-title">반출신청상세</h3>
	</div>
	<div class="panel-body">
		<c:choose>
			<c:when test="${not empty outRqstDtl}">
				<%--<table class="table table-striped table-xs">--%>
				<table class="table table-xs">
					<colgroup>
						<col style="width: 100px;" />
						<col style="width: calc(50% - 100px);" />
						<col style="width: 100px;" />
						<col style="width: calc(50% - 100px);" />
					</colgroup>
					<tbody>
						<tr><th class="bg-info">카메라명</th>
							<td>${outRqstDtl.fcltLblNm}</td>
							<th class="bg-info">카메라주소</th>
							<td>${outRqstDtl.lotnoAdresNm}		<c:if test="${outRqstDtl.roadAdresNm ne '' }">	(${outRqstDtl.roadAdresNm})	</c:if>
								<button type="button" class="btn btn-default btn-xs" id="out-rqst-dtl-map-pin" title="발생위치로 이동" onclick="oTvoCmn.map.moveTo(this);">
									<i class="fas fa-map-pin fa-lg"></i>
								</button>
							</td>
						</tr>
						<tr><th class="bg-info">영상구간</th>
							<td><mark>${outRqstDtl.vdoYmdHmsFr.substring(0,16)}</mark>
								<span> ~ </span>
								<mark>${outRqstDtl.vdoYmdHmsTo.substring(0,16)}</mark>
								<span id="out-rqst-dtl-vdoYmdHmsDuration"></span>
								<button type="button" class="btn btn-default btn-xs hide" id="out-rqst-dtl-vmsView" title="보기"
									onclick="oVmsCommon.openVmsPlayerFrTo('${outRqstDtl.cctvId}', 'SEARCH', '${outRqstDtl.evtYmdhms}', '${outRqstDtl.vdoYmdhmsFrOrgn}', '${outRqstDtl.vdoYmdhmsToOrgn}', 'SEE');">
									<i class="fas fa-video"></i>
								</button>
							</td>
							<th class="bg-info">재생기간</th>
							<td><span id="out-rqst-dtl-playStartYmdhms"><c:if test="${outRqstDtl.playStartYmdHms ne ''}">${outRqstDtl.playStartYmdHms.substring(0,10)}</c:if></span>
								~ <span id="out-rqst-dtl-playEndYmdhms"><c:if test="${outRqstDtl.playEndYmdHms ne ''}">${outRqstDtl.playEndYmdHms.substring(0,10)}</c:if></span>
								<span id="out-rqst-dtl-playDuration"></span>
								<c:if test="${subMenu.indexOf('outRqst')!=-1}">
									&nbsp;							
									<c:if test="${outRqstDtl.outFileDelYn ne 'N'}"><span title="영상이 삭제되었습니다.">[기간연장불가]</span></c:if>
									<%-- <button type="button" class="btn btn-default btn-xs" title="기간연장" onclick="oOutFirstRqst.out.prodExtn.init('SINGLE');">기간연장</button> --%>
									<button type="button" class="btn btn-warning btn-xs hide" id="out-rqst-dtl-rqstExtn" title="기간연장"
										 onclick="oOutFirstRqst.out.prodExtn.open('SINGLE');">기간연장</button>
								</c:if>
								<c:if test="${not empty outRqstDtl.playEndYmdHms and outRqstDtl.outProdExtnCnt eq '0'}">
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="bg-info">마스킹/제3자제공</th>
							<td><span id="out-rqst-dtl-maskingYn">${outRqstDtl.maskingYn}</span>
								/
								<span id="out-rqst-dtl-thirdPartyYn">${outRqstDtl.thirdPartyYn}</span>
								<c:if test="${( subMenu.indexOf('outRqst')!=-1 or subMenu.indexOf('outAprv')!=-1 ) and ( outRqstDtl.thirdPartyYn eq 'Y' )}">
									&nbsp;
									<button type="button" class="btn btn-warning btn-xs" title="제3자재생암호"
										onclick="oTvoCmn.thirdPartyPw.open('${outRqstDtl.thirdPartyPw}');">제3자재생암호</button>
								</c:if>
							</td>
							<th class="bg-info">반출신청번호</th>
							<td>${outRqstDtl.outRqstNo}
								<c:if test="${not empty outRqstDtl and subMenu.indexOf('outAprv')!=-1 and LoginVO.userId=='sys'}">	<!-- sys계정으로만 -->
									<button type="button" class="btn btn-danger btn-xs" title="완전삭제" onclick="oOutFirstAprv.out.aprv.deleteComplete();">완전삭제</button>
								</c:if>
							</td>
						</tr>
					<%--<tr><th class="bg-info">재생회수</th>				<td><span id="out-rqst-dtl-playCnt">${outRqstDtl.playCnt}</span></td>
							<th class="bg-info">마스킹</th>				<td><span id="out-rqst-dtl-maskingYn">${outRqstDtl.maskingYn}</span></td>
							<th class="bg-info">제3자제공</th>
							<td><span id="out-rqst-dtl-thirdPartyYn">${outRqstDtl.thirdPartyYn}</span>
								&nbsp;
								<c:if test="${outRqstDtl.thirdPartyYn=='Y'}">
									<button type="button" class="btn btn-default btn-xs" title="제3자재생암호"
										 onclick="alert('${outRqstDtl.thirdPartyPw}');">제3자재생암호
									</button>
								</c:if>
							</td>
						</tr>--%>
						<tr><th class="bg-info">반출신청일시</th>			<td>${outRqstDtl.outRqstYmdHms.substring(0,16)} ( ${outRqstDtl.outRqstUserNm} )</td>
							<th class="bg-info">진행상태</th>
							<td>${outRqstDtl.tvoPrgrsNm}
							<%--<c:if test="${outRqstDtl.tvoPrgrsCd eq '30' || outRqstDtl.tvoPrgrsCd eq '50' || outRqstDtl.tvoPrgrsCd eq '51' || outRqstDtl.tvoPrgrsCd eq '70'}"></c:if>--%>
								<c:if test="${outRqstDtl.tvoPrgrsCd ne '10' and not empty outRqstDtl.tvoPrgrsYmdHms}">
									( ${outRqstDtl.tvoPrgrsYmdHms.substring(0,16)} / ${outRqstDtl.outAprvUserNm} )
								</c:if>
								<c:if test="${not empty outRqstDtl and subMenu.indexOf('outAprv')!=-1}">
									<c:if test="${ outRqstDtl.tvoPrgrsCd eq '10' }">
										<button type="button" class="btn btn-warning btn-xs" title="입수승인" onclick="oOutFirstAprv.out.aprv.open();">입수승인</button>
									</c:if>
									<c:if test="${ outRqstDtl.outChkStepCd eq '92' and ( outRqstDtl.tvoPrgrsCd eq '50' or outRqstDtl.tvoPrgrsCd eq '51')}">
										<button type="button" class="btn btn-warning btn-xs" title="반출승인" onclick="oOutFirstAprv.out.aprv.open();">반출승인</button>
									</c:if>
								</c:if>
								<c:if test="${not empty  outRqstDtl.tvoPrgrsDtl}"><br/>${outRqstDtl.tvoPrgrsDtl}</c:if>
								<button type="button" class="btn btn-warning btn-xs hide" id="out-rqst-dtl-doReject" title="반려처리" onclick="oOutFirstAprv.out.aprv.open('REJECT');">반려처리</button>
								<c:if test="${not empty outRqstDtl and subMenu.indexOf('outAprv')!=-1 and outRqstDtl.tvoPrgrsCd ne '10'}">
									&nbsp; <button type="button" class="btn btn-warning btn-xs" title="승인취소" onclick="oOutFirstAprv.out.aprv.reset();">승인취소</button>
								</c:if>
							</td>
						</tr>
					<c:if test="${not empty outRqstDtl.outChkStepCd}">
						<tr><th class="bg-info">영상처리</th>
							<td>${outRqstDtl.outChkStepNm}
								<c:if test="${outRqstDtl.outChkStepCd ne '10' and not empty outRqstDtl.outChkYmdHms}">( ${outRqstDtl.outChkYmdHms.substring(0,16)} )</c:if>
								&nbsp;
								<button type="button" class="btn btn-warning btn-xs hide" id="out-rqst-dtl-retryGetVdo" title="영상입수재시도"
									 onclick="oOutFirstAprv.out.file.getVdo();">영상입수재시도
								</button>
								<button type="button" class="btn btn-warning btn-xs hide" id="out-rqst-dtl-retryDrm" title="DRM재시도"
									 onclick="oOutFirstAprv.out.file.enc();">DRM재시도
								</button>
							<%--<button type="button" class="btn btn-warning btn-xs hide" id="out-rqst-dtl-checkHash" title="암호화영상원본대조"
									 onclick="oOutFirstAprv.out.checkHash.open('${outRqstDtl.outRqstNo}');">암호화영상원본대조
								</button>--%>
								<c:if test="${outRqstDtl.outChkStepDtl ne ''}"><br/>( ${outRqstDtl.outChkStepDtl} )			</c:if>
							</td>
							<th class="bg-info">다운로드</th>
							<td><button type="button" class="btn btn-warning btn-xs hide" id="out-rqst-dtl-downloadPlayer" title="플레이어다운로드"
									onclick="oTvoCmn.download.playerFile();"><!-- <i class="fas fa-download"></i> -->플레이어
								</button>
								<button type="button" class="btn btn-warning btn-xs hide" id="out-rqst-dtl-downloadMasker" title="마스킹툴다운로드"
									onclick="oTvoCmn.download.maskerFile();"><!-- <i class="fas fa-download"></i> -->마스킹툴
								</button>
							</td>
						</tr>
					</c:if>
						<tr style="display:none;">
							<th class="bg-info">카메라위치</th>
							<td><span>X: </span>
								<mark id="out-rqst-dtl-cctvPointX">${outRqstDtl.pointX}</mark>
								<span>Y: </span>
								<mark id="out-rqst-dtl-cctvPointY">${outRqstDtl.pointY}</mark>
								<button type="button" class="btn btn-default btn-xs" title="발생위치로 이동" onclick="oTvoCmn.map.moveTo(this);">
									<i class="fas fa-map-pin fa-lg"></i>
								</button>
							</td>
							<th></th>
							<td></td>
						</tr>
						<tr style="display:none;">
						</tr>
					</tbody>
				</table>
				<form>
					<label class="control-label sr-only">신청지구,열람신청번호,반출신청번호,반출신청일시,진행상태코드,영상처리코드</label>
					<input type="hidden" id="out-rqst-dtl-dstrtCd"       name="out-rqst-dtl-dstrtCd"       value="${outRqstDtl.dstrtCd}"       />
					<input type="hidden" id="out-rqst-dtl-viewRqstNo"    name="out-rqst-dtl-viewRqstNo"    value="${outRqstDtl.viewRqstNo}"    />
					<input type="hidden" id="out-rqst-dtl-outRqstNo"     name="out-rqst-dtl-outRqstNo"     value="${outRqstDtl.outRqstNo}"     />
					<input type="hidden" id="out-rqst-dtl-outRqstYmdhms" name="out-rqst-dtl-outRqstYmdhms" value="${outRqstDtl.outRqstYmdhms}" />
					<input type="hidden" id="out-rqst-dtl-tvoPrgrsCd"    name="out-rqst-dtl-tvoPrgrsCd"    value="${outRqstDtl.tvoPrgrsCd}"    />
					<input type="hidden" id="out-rqst-dtl-outChkStepCd"  name="out-rqst-dtl-outChkStepCd"  value="${outRqstDtl.outChkStepCd}"  />
					
					<label class="control-label sr-only">CCTV아이디,VMS CCTV아이디</label>
					<input type="hidden" id="out-rqst-dtl-cctvId"     name="out-rqst-dtl-cctvId"     value="${outRqstDtl.cctvId}"     />
					<input type="hidden" id="out-rqst-dtl-linkVmsUid" name="out-rqst-dtl-linkVmsUid" value="${outRqstDtl.linkVmsUid}" />
										
					<label class="control-label sr-only">영상시작일시,영상종료일시,영상시작일시,영상종료일시</label>
					<input type="hidden" id="out-rqst-dtl-vdoYmdhmsFr"     name="out-rqst-dtl-vdoYmdhmsFr"     value="${outRqstDtl.vdoYmdHmsFr}"     />
					<input type="hidden" id="out-rqst-dtl-vdoYmdhmsTo"     name="out-rqst-dtl-vdoYmdhmsTo"     value="${outRqstDtl.vdoYmdHmsTo}"     />
					<input type="hidden" id="out-rqst-dtl-vdoYmdhmsFrOrgn" name="out-rqst-dtl-vdoYmdhmsFrOrgn" value="${outRqstDtl.vdoYmdhmsFrOrgn}" />
					<input type="hidden" id="out-rqst-dtl-vdoYmdhmsToOrgn" name="out-rqst-dtl-vdoYmdhmsToOrgn" value="${outRqstDtl.vdoYmdhmsToOrgn}" />

					<label class="control-label sr-only">제3자재생암호,반출파일삭제여부,반출신청자아이디</label>
					<input type="hidden" id="out-rqst-dtl-thirdPartyPw"  name="out-rqst-dtl-thirdPartyPw"  value="${outRqstDtl.thirdPartyPw}"  />
					<input type="hidden" id="out-rqst-dtl-outFileDelYn"  name="out-rqst-dtl-outFileDelYn"  value="${outRqstDtl.outFileDelYn}"  />
					<input type="hidden" id="out-rqst-dtl-outRqstUserId" name="out-rqst-dtl-outRqstUserId" value="${outRqstDtl.outRqstUserId}" />
				</form>
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
	<c:if test="${subMenu.indexOf('outRqst')!=-1 and outRqstDtl.tvoPrgrsCd eq '10'}">
		<div class="panel-footer text-center">
			<button type="button" class="btn btn-secondary btn-xs" title="신청취소" onclick="oOutFirstRqst.out.rqst.cancel();">신청취소</button>
		</div>
	</c:if>
</div>
<c:if test="${not empty outRqstDtl and (outRqstDtl.tvoPrgrsCd eq '50' or outRqstDtl.tvoPrgrsCd eq '51') or (outRqstDtl.tvoPrgrsCd eq '70' or outRqstDtl.tvoPrgrsCd eq '71')}">
	<div class="panel panel-info" id="out-rqst-dtl-file">
		<div class="panel-heading">
			<h3 class="panel-title">반출파일</h3>
		</div>
		<div class="panel-body" style="max-height: 250px;overflow: auto;">
			<table id="grid-out-rqst-dtl-file"></table>
			<div id="paginate-out-rqst-dtl-file" class="paginate text-center"></div>
		</div>
	</div>
</c:if>
<c:if test="${subMenu.indexOf('outAprv')!=-1}">
	<div class="panel panel-info hide" id="out-rqst-dtl-file-upload">
		<div class="panel-heading">
			<h3 class="panel-title">원본영상업로드</h3>
		</div>
		<div class="panel-body">
			<table class="table table-xs">
				<colgroup>
					<col style="width: 100px;" />
					<col style="width: calc(50% - 100px);" />
					<col style="width: 100px;" />
					<col style="width: calc(50% - 100px);" />
				</colgroup>
				<tbody>
					<tr>
						<%--
						<th class="bg-info" style="display:none;">반출단계</th>
						<td style="display:none;">
							<label for="out-rqst-dtl-outChkStepCd-select" class="control-label sr-only">반출단계</label>
							<select id="out-rqst-dtl-outChkStepCd-select" name="out-rqst-dtl-outChkStepCd-select" class="form-control input-xs" title="반출단계"></select>
						</td>
						 --%>
						<th class="bg-info">원본파일</th>
						<td><input type="file" class="form-control input-xs" style="height: 22px;padding: 0;font-size: 10px;" id="orgVdo" title="영상영상"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="panel-footer">
			<div class="row">
				<div class="col-xs-12 text-center">
					<button type="button" class="btn btn-primary btn-xs" title="전송" onclick="oOutFirstAprv.out.aprv.uploadOrgVdo();">전송</button>
				</div>
			</div>
		</div>
	</div>
</c:if>
