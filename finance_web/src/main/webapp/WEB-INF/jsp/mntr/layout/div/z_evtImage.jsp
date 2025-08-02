<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp" id="div-evt-image"
		data-evt-ocr-no="${divEvent.evtOcrNo}"
		data-evt-ocr-ymd-hms="${divEvent.evtOcrYmdHms}"
>
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<div id="container-div-image">
			<c:set var="scmpImg" value="http://${configProp['Globals.ServerIp']}:${configProp['Globals.ServerPort']}/" scope="request" />
			<c:if test="${!empty scmpImg and !empty ipMapping.scmpImgMp}">
				<c:set var="scmpImg" value="${fn:replace(scmpImg, ipMapping.scmpImg, ipMapping.scmpImgMp)}" scope="request" />
			</c:if>
			<c:choose>
				<c:when test="${not empty divImage}">
					<c:forEach var="image" items="${divImage}">
						<div class="row" style="margin-left: -15px; margin-right: -15px;">
							<div class="col-xs-12">
                                <c:set var="dirEventImg" scope="request"><prprts:value key="DIR_EVENT_IMG"/></c:set>
                                <c:set var="dirEss" scope="request"><prprts:value key="DIR_ESS"/></c:set>
								<c:if test="${image.imgTy ne 'COM'}">
									<c:set var="imgSrc" value="${scmpImg}${dirEventImg}${image.imgPath}${image.imgUrl}" scope="request" />
								</c:if>
								<c:if test="${image.imgTy eq 'COM'}">
									<c:set var="imgSrc" value="${scmpImg}${dirEss}${image.imgPath}${image.imgUrl}" scope="request" />
								</c:if>
								<a href="javascript:oCommon.popupImage('${imgSrc}');" class="thumbnail">
									<c:if test="${image.imgTy eq 'OCR'}">
										<img src="${imgSrc}" alt="${image.evtOcrNo}" class="ocr" data-evt-ocr-no="${image.evtOcrNo}" data-evt-ocr-ymd-hms="${image.evtOcrYmdHms}" data-img-path="${image.imgPath}"
											data-img-url="${image.imgUrl}" onerror="javascript:oCommon.notFoundImage(this);">
									</c:if>
									<c:if test="${image.imgTy ne 'OCR'}">
										<img src="${imgSrc}" alt="${image.evtOcrNo}" onerror="javascript:oCommon.notFoundImage(this);">
									</c:if>
								</a>
							</div>
						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="row" style="margin-left: -15px; margin-right: -15px;">
						<div class="col-xs-12">
							<div class="alert alert-danger" role="alert">이미지가 없습니다.</div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<c:if test="${configProp['ivx.useYn'] eq 'Y' and divEvent.sysCd ne 'LPR'}">
	<div class="well well-sm">
		<button type="button" class="btn btn-default btn-xs" id="btn-ivx-rqst" onclick="oEvtImage.requestIvx(this);">
			<c:choose>
				<c:when test="${fn:length(videoSeaReqList) > 0 }">
			지능형 재검색 요청
				</c:when>
				<c:otherwise>
			지능형 검색 요청
				</c:otherwise>
			</c:choose>
		</button>
	</div>
</c:if>
<script>
	$(function() {
		oEvtImage = new evtImage();
	});

	function evtImage() {
		<c:if test="${configProp['ivx.useYn'] eq 'Y' and divEvent.sysCd ne 'LPR'}">
		this.requestIvx = function(element) {
			var sText = $(element).text().trim();
			if (confirm(sText + ' 하시겠습니까?')) {
				var $containerDivImage = $('#container-div-image');
				var $img = $containerDivImage.find('img.ocr');
				if ($img.length) {
					var oData = $($img.get(0)).data();
					var oParams = {
						'searchType' : 'face',
						'evtOcrNo' : oData.evtOcrNo,
						'evtOcrYmdHms' : oData.evtOcrYmdHms,
						'imgUrl' : oConfigure.imageEvent + oData.imgPath + oData.imgUrl
					};
					$.ajax({
						url : contextRoot + '/mntr/insertVideoSeaReq.json',
						async : false,
						type : 'POST',
						data : oParams,
						dataType : 'json',
						success : function(data) {
							alert('요청되었습니다.');
							$('#btn-ivx-rqst').text('지능형 재검색 요청');
						}
					});
				} else {
					alert('이미지가 없습니다.');
				}
			}
		};
		</c:if>
	}
</script>