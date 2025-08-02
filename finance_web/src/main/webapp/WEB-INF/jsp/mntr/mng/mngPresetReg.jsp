<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<c:set var="vmsStartPreNum" scope="request"><prprts:value key="VMS_START_PRE_NUM"/></c:set>
<article id="article-grid">
	<div id="searchBar">
		<div class="tit">
			<h4>${common.title}</h4>
		</div>
		<div class="container-search container-search-xs">
			<div class="row">
				<div class="col-xs-10 text-ellipsis">
					<h5>
						<span class="label label-default">${cctvInfo.fcltKndDtlCd eq 'RT' ? '회전형' : '고정형'}</span>
						${cctvInfo.fcltLblNm}
					</h5>
				</div>
				<div class="col-xs-2 text-right" style="line-height: 35px;">
					<button class="btn btn-primary btn-sm" type="button" onclick="oMngPresetReg.list();"><i class="fas fa-list"></i> 목록</button>
				</div>
			</div>
		</div>
		<div class="container-search container-search-xs" style="padding-bottom: 8px;">
			<h5>카메라 위치보정</h5>
			<h6 class="text-danger">프리셋 설정 전 카메라 위치확인 필수</h6>
			<h6 class="text-warning">프리셋 설정 후 카메라 위치보정 시 좌표가 틀어지므로 다시 재설정 권장</h6>
			<div class="row">
				<div class="col-xs-12">
					<div class="form-inline">
						<label title="체크 상태면 지도 클릭을 통해 카메라 위치를 보정할 수 있습니다.">
							<input id="chk-update-fclt-point" type="checkbox" title="체크 상태면 지도 클릭을 통해 카메라 위치를 보정할 수 있습니다.">
							위치보정
						</label>
						<label title="동일 좌표의 시설물도 함께 보정합니다.">
							<input type="checkbox" id="chk-include-mng-sn" title="동일 좌표의 시설물도 함께 보정합니다." disabled="disabled" checked="checked">
							동일 좌표 시설물 포함
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-8">
					<div class="form-inline">
						<input type="text" class="form-control input-xs" id="txt-pointX" placeholder="경도" readonly="readonly" size="12" maxlength="12"
							   value="<fmt:formatNumber value="${cctvInfo.pointX}" pattern=".000000" />"/>
						<input type="text" class="form-control input-xs" id="txt-pointY" placeholder="위도" readonly="readonly" size="12" maxlength="12"
							   value="<fmt:formatNumber value="${cctvInfo.pointY}" pattern=".000000" />"/>
					</div>
				</div>
				<div class="col-xs-4 text-right">
					<button type="button" class="btn btn-default btn-xs" id="btn-update-fclt-point" disabled="disabled">저장</button>
					<button type="button" class="btn btn-default btn-xs" id="btn-cancel-fclt-point" disabled="disabled">취소</button>
				</div>
			</div>
		</div>
		<div class="container-search container-search-xs">
			<c:if test="${cctvInfo.fcltKndDtlCd eq 'RT'}">
				<c:set var="presetNum" value="${empty cctvInfo.presetBdwStartNum ? vmsStartPreNum : cctvInfo.presetBdwStartNum}" scope="request"/>
				<div class="row">
					<div class="col-xs-9">
						<h5>
							프리셋대역시작번호 :
							<span class="label label-default" id="presetBdwStartNum">${presetNum}</span>
							( 기본값 :
							<span class="label label-default">${vmsStartPreNum}</span>
							)
						</h5>
					</div>
					<div class="col-xs-3 text-right" style="padding-top: 7px;">
						<select class="form-control input-xs" id="sel-preset-num" title="프리셋대역시작번호">
							<c:forEach begin="10" end="90" step="10" varStatus="status">
								<option <c:if test="${status.index eq presetNum}"> selected="selected"</c:if>>${status.index}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</c:if>
		</div>
		<div class="container-search container-search-xs">
			<c:set var="forEachEnd" value="${cctvInfo.fcltKndDtlCd eq 'RT' ? 8 : 0}"/>
			<c:forEach begin="0" end="${forEachEnd}" step="1" varStatus="status">
				<div class="form-inline">
					<div class="form-group">
						<c:if test="${status.index eq 0}">
							<h5>
								<c:out value="${cctvInfo.fcltKndDtlCd eq 'RT' ? '비상벨 위치' : '고정형 감시방향'}"/>
								<button class="btn btn-default btn-xs btn-preset-num" type="button" onclick="oMngPresetReg.deactivatePresetPoint();">체크해제</button>
							</h5>
						</c:if>
						<c:if test="${status.index eq 1}">
							<h5>
								팔방향프리셋
								<button class="btn btn-default btn-xs btn-preset-num" type="button" onclick="oMngPresetReg.deactivatePresetPoint();">체크해제</button>
							</h5>
						</c:if>
						<label style="display: inline-block; width: 80px; padding-left: 20px;">
							<c:if test="${status.index eq 0 and cctvInfo.fcltKndDtlCd eq 'FT'}">
								<input type="radio" name="rdo-preset-num" class="rdo-preset-num" checked="checked" value="${status.index}"/> ${status.index}
							</c:if>
							<c:if test="${cctvInfo.fcltKndDtlCd eq 'RT'}">
								<input type="radio" name="rdo-preset-num" class="rdo-preset-num" value="${status.index}"/> ${status.index}
								<c:if test="${status.index eq 1}">북</c:if>
								<c:if test="${status.index eq 2}">북동</c:if>
								<c:if test="${status.index eq 3}">동</c:if>
								<c:if test="${status.index eq 4}">남동</c:if>
								<c:if test="${status.index eq 5}">남</c:if>
								<c:if test="${status.index eq 6}">남서</c:if>
								<c:if test="${status.index eq 7}">서</c:if>
								<c:if test="${status.index eq 8}">북서</c:if>
							</c:if>
						</label>
						<input type="text" class="form-control input-xs" id="txt-preset-${status.index}-point-x" placeholder="경도" readonly="readonly" size="12" maxlength="12" value=""/>
						<input type="text" class="form-control input-xs" id="txt-preset-${status.index}-point-y" placeholder="위도" readonly="readonly" size="12" maxlength="12" value=""/>
						<button class="btn btn-default btn-xs btn-preset-num" type="button" onclick="oMngPresetReg.deletePresetNum('${status.index}');">삭제</button>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</article>
<section>
	<ul>
		<li><input id="cctvInfo" type="hidden" data-fclt-id="<c:out value='${cctvInfo.fcltId}' />"
				   data-gw-vms-uid="<c:out value='${cctvInfo.gwVmsUid}' />"
				   data-fclt-knd-dtl-cd="<c:out value='${cctvInfo.fcltKndDtlCd}' />"
				   data-preset-bdw-start-num="<c:out value='${cctvInfo.presetBdwStartNum}' />"
				   data-vrs-webrtc-addr="<c:out value='${cctvInfo.vrsWebrtcAddr}' />"
				   data-vrs-rtsp-addr="<c:out value='${cctvInfo.vrsRtspAddr}' />"
				   data-ptz-api-addr="<c:out value='${cctvInfo.ptzApiAddr}' />"
				   data-ptz-api-key="<c:out value='${cctvInfo.ptzApiKey}' />"
				   data-ptz-api-sys-cd="<c:out value='${cctvInfo.ptzApiSysCd}' />"
				   data-ptz-auth-yn="<c:out value='${cctvInfo.ptzAuthYn}' />"
				   data-fclt-used-ty-cd="<c:out value='${cctvInfo.fcltUsedTyCd}' />"
				   data-fclt-lbl-nm="<c:out value='${cctvInfo.fcltLblNm}' />"
				   data-point-x="<c:out value='${cctvInfo.pointX}' />"
				   data-point-y="<c:out value='${cctvInfo.pointY}' />"
				   data-cctv-osvt-x="<c:out value='${cctvInfo.cctvOsvtX}' />"
				   data-cctv-osvt-y="<c:out value='${cctvInfo.cctvOsvtY}' />"
				   data-dstrt-cd="<c:out value='${cctvInfo.dstrtCd}'/>"/>
		</li>
		<li>
			<form name="form-search" id="form-search" method="post" action="<c:url value="/mntr/mngPreset.do?top=5b065693-8a5a-4ce7-d85425d3&left=860bf3f9-3ee2-4602-adf2ee49&m=86666d7f-9011-4ab3-f66ffba6" />">
				<input type="hidden" name="searchPage" value="<c:out value='${common.searchPage}'/>"/>
				<input type="hidden" name="searchFcltUsedTyCd" value="<c:out value='${common.searchFcltUsedTyCd}'/>"/>
				<input type="hidden" name="searchFcltKndDtlCd" value="<c:out value='${common.searchFcltKndDtlCd}'/>"/>
				<input type="hidden" name="searchPresetYn" value="<c:out value='${common.searchPresetYn}'/>"/>
				<input type="hidden" name="searchKeyword" value="<c:out value='${common.searchKeyword}'/>"/>
				<input type="hidden" name="searchGbn" value="<c:out value='${common.searchGbn}'/>"/>
			</form>
		</li>
	</ul>
</section>
<script type="text/javascript" src="<c:url value='/js/mntr/mng/mngPresetReg.js'/>"></script>
