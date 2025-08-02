<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<style>
th {
	font-weight: bold;
}
</style>
<article id="article-grid">
<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp"%>
	<div class="row">
		<!-- <div class="tit">
			<h4>${common.title}</h4>
		</div> -->

		<!-- <h4>시설물 유형별 용도별 현황</h4> -->
		<table class="table table-bordered table-striped table-ucp">
			<tr>
				<th>유형</th>
				<th>용도</th>
				<th>합계</th>
				<th>정상건수</th>
				<th>고장건수</th>
				<th>미측정건수</th>
			</tr>
			<c:forEach items="${totCnt}" var="list">
				<c:set var="cssClass" value="" />
				<c:if test="${not empty list.fcltKndCd and empty list.fcltUsedTyCd}">
					<c:set var="cssClass" value="info" />
				</c:if>
				<c:if test="${empty list.fcltKndCd and empty list.fcltUsedTyCd}">
					<c:set var="cssClass" value="danger" />
				</c:if>
				<tr class="${cssClass}">
					<td>
						<c:choose>
							<c:when test="${not empty list.fcltKndCd and empty list.fcltUsedTyCd}">
								<strong>${list.fcltKndNm}(${list.fcltKndCd})</strong>
							</c:when>
							<c:when test="${empty list.fcltKndCd and empty list.fcltUsedTyCd}">
								<strong>전체</strong>
							</c:when>
							<c:otherwise>${empty list.fcltKndNm ? '유형명없음' : list.fcltKndNm}(${list.fcltKndCd})</c:otherwise>
						</c:choose>

					</td>
					<td>
						<c:choose>
							<c:when test="${empty list.fcltUsedTyCd}">
								<strong>전체</strong>
							</c:when>
							<c:otherwise>${empty list.fcltUsedTyNm ? '용도명없음' : list.fcltUsedTyNm}(${list.fcltUsedTyCd})</c:otherwise>
						</c:choose>

					</td>
					<td>${list.fcltSttusCntTot}</td>
					<td>
						<a href="#" onclick="oFcltStatus.popup('${list.fcltKndCd}', '${list.fcltUsedTyCd}', 0);">${list.fcltSttusCntZero}</a>
					</td>
					<td>
						<a href="#" onclick="oFcltStatus.popup('${list.fcltKndCd}', '${list.fcltUsedTyCd}', 1);">${list.fcltSttusCntOne}</a>
					</td>
					<td>
						<a href="#" onclick="oFcltStatus.popup('${list.fcltKndCd}', '${list.fcltUsedTyCd}', 2);">${list.fcltSttusCntTwo}</a>
					</td>
				</tr>
			</c:forEach>
		</table>
</article>
<script>
	$(function() {
		oFcltStatus = new fcltStatus();
		oFcltStatus.init();
	});

	function fcltStatus() {
		this.init = function() {
			collapse({
				left : true,
				right : true,
				bottom : true
			});
		};

		this.popup = function(searchFcltKndCd, searchFcltUsedTyCd, searchFcltSttus) {
			if (searchFcltKndCd == '0') searchFcltKndCd = '';
			if (searchFcltUsedTyCd == '0') searchFcltUsedTyCd = '';

			var sttus = '';
			if (searchFcltSttus == '0') {
				sttus = '정상'
			} else if (searchFcltSttus == '1') {
				sttus = '고장'
			} else if (searchFcltSttus == '2') {
				sttus = '미측정'
			}

			BootstrapDialog.show({
				type : BootstrapDialog.TYPE_INFO,
				title : '시설물 현황 - (' + sttus + ')',
				cssClass : 'popupFcltStatus',
				message : $('<div></div>').load(contextRoot + '/mntr/fcltStatusPopup.do', {
					searchFcltKndCd : searchFcltKndCd,
					searchFcltUsedTyCd : searchFcltUsedTyCd,
					searchFcltSttus : searchFcltSttus
				})
			});
		};
	};
</script>