<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {

	});

	function isNumber(num) {
		re = /^[\+-]?[0-9]*[0-9]$/;
		if (re.test(num)) {
			return true;
		}
		return false;
	}

	function fn_cctvRadiusMerge() {
		if (confirm('저장하시겠습니까?')) {
			var radsRoute = $("#radsRoute").val();
			var radsClmt = $("#radsClmt").val();
			var cctvViewRads = $("#cctvViewRads").val();
			var mntrLeftWidth = $("#mntrLeftWidth").val();
			var mntrRightWidth = $("#mntrRightWidth").val();
			var mntrBottomHeight = $("#mntrBottomHeight").val();
			var popWidth = $("#popWidth").val();
			var popHeight = $("#popHeight").val();
			var autoEndTime = $("#autoEndTime").val();

			var basePlaybacktime = $("#basePlaybacktime").val();
			var maxbfPlaybacktime = $("#maxbfPlaybacktime").val();
			var maxafPlaybacktime = $("#maxafPlaybacktime").val();
			var playTime = $("#playTime").val();

			$("#gisViewTy").val($("input[name=gisViewKnd]:checked").val());
			$("#gisIconTy").val($("input[name=gisIconKnd]:checked").val());

			var gisLabelViewScale = $('#gisLabelViewScale').val();
			var gisFeatureViewScale = $('#gisFeatureViewScale').val();

			if (isNumber(radsRoute) && isNumber(radsClmt) && isNumber(cctvViewRads) && isNumber(mntrLeftWidth) && isNumber(mntrRightWidth) && isNumber(mntrBottomHeight)
					&& isNumber(popWidth) && isNumber(popHeight) && isNumber(autoEndTime) && isNumber(gisLabelViewScale) && isNumber(gisFeatureViewScale)) {

				gisLabelViewScale = parseInt(gisLabelViewScale);
				gisFeatureViewScale = parseInt(gisFeatureViewScale);

				if (gisLabelViewScale > gisFeatureViewScale) {
					alert('레이블표시 축척 값은 아이콘표시 축척 값 보다 작거나 같아야 합니다.');
					return false;
				}

				document.frm.action = "<c:url value='/wrks/sstm/rdus/cctvRadiusMerge.do'/>";
				document.frm.submit();
			}
			else {
				alert("숫자만 입력 가능합니다.");
			}
		}
	}
</script>
</head>
<body>
<div id="wrapper">
	<!-- topbar -->
	<%@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
	<!-- //topbar -->
	<!-- container -->
	<div class="container">
		<!-- leftMenu -->
		<%@include file="/WEB-INF/jsp/cmm/leftMenu.jsp"%>
		<!-- //leftMenu -->
		<!-- content -->
		<div class="contentWrap">
			<br>
			<div class="topArea">
				<a href="#" class="btnOpen">
					<img src="<c:url value='/'/>images/btn_on_off.png" alt="열기/닫기">
				</a>
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi.jsp"%>
			</div>
			<div class="content">
				<div class="titArea">
					<h3 class="tit">환경설정</h3>
				</div>
				<br />
				<form name="frm" action="<c:url value='/wrks/sstm/rdus/cctvRadiusMerge.do'/>" method="post">
					<input type="hidden" id="dstrtCd" name="dstrtCd" value="<c:out value="${dstrtCd}" />" /> <input type="hidden" id="userId" name="userId" value="<c:out value="${userId}" />" />
					<!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->
					<input type="hidden" id="child" name="child" value="<c:out value='${child}'/>" /> <input type="hidden" id="top" name="top" value="<c:out value='${top}'/>" /> <input type="hidden" id="left" name="left" value="<c:out value='${left}'/>" /> <input type="hidden" id="gisViewTy" name="gisViewTy" value="<c:out value='${radus.GIS_TY}'/>" /> <input type="hidden" id="gisIconTy" name="gisIconTy" value="<c:out value='${radus.ICON_TY}'/>" />
					<div class="tableTypeFree">
						<div class="tit">
							<h1>지도보기기준</h1>
						</div>
						<table>
							<caption>지도보기기준</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>지도보기기준</th>
								<td><input type="radio" name="gisViewKnd" id="gisViewKnd" value="0" ${radus.GIS_TY == 0 ? 'checked="checked"' : ''} user-title="지도보기기준" /> 일반 <input type="radio" name="gisViewKnd" id="gisViewKnd" value="1" ${radus.GIS_TY == 1 ? 'checked="checked"' : ''} user-title="지도보기기준" /> 항공</td>
							</tr>
						</table>
						<br />
						<div class="tit">
							<h1>축척설정</h1>
						</div>
						<table>
							<caption>축척설정</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>레이블표시 축척</th>
								<td><input type="number" name="gisLabelViewScale" id="gisLabelViewScale" class="date8Type" maxlength="6" value="<c:out value="${radus.GIS_LABEL_VIEW_SCALE}"/>" user-title="레이블표시 축척" /> (기본값: 4000)</td>
								<th>아이콘표시 축척</th>
								<td><input type="number" name="gisFeatureViewScale" id="gisFeatureViewScale" class="date8Type" maxlength="6" value="<c:out value="${radus.GIS_FEATURE_VIEW_SCALE}"/>" user-title="아이콘표시 축척" /> (기본값: 7000)</td>
							</tr>
						</table>
						<br />
						<div class="tit">
							<h1>아이콘타입</h1>
						</div>
						<table>
							<caption>아이콘타입</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>아이콘타입</th>
								<td style="width: 295px;"><input type="radio" name="gisIconKnd" id="gisIconKndA" value="A" ${radus.ICON_TY eq 'A' ? 'checked="checked"' : ''} user-title="아이콘타입A" /> TypeA <input type="radio" name="gisIconKnd" id="gisIconKndB" value="B" ${radus.ICON_TY eq 'B' ? 'checked="checked"' : ''} user-title="아이콘타입B" /> TypeB</td>
								<th>아이콘크기</th>
								<td><select name="gisIconSize" id="gisIconSize" user-title="아이콘크기">
										<option value="30" ${radus.ICON_SIZE eq '30' ? 'selected="selected"' : ''}>30x30</option>
										<option value="40" ${radus.ICON_SIZE eq '40' ? 'selected="selected"' : ''}>40x40</option>
										<option value="50" ${radus.ICON_SIZE eq '50' ? 'selected="selected"' : ''}>50x50</option>
								</select></td>
							</tr>
						</table>
						<br />
						<div class="tit">
							<h1>상황 자동종료 설정</h1>
						</div>
						<table>
							<caption>상황 자동종료 설정</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>자동종료시간</th>
								<td><input type="text" name="autoEndTime" id="autoEndTime" class="date8Type" maxlength="5" value="<c:out value="${radus.AUTO_END_TIME}"/>" user-title="자동종료시간" /> (분)</td>
							</tr>
						</table>
						<br />
						<div class="tit">
							<h1>반경설정</h1>
						</div>
						<table>
							<caption>반경설정</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>경로상 주변 표시 반경</th>
								<td><input type="text" name="radsRoute" id="radsRoute" class="date8Type" maxlength="5" value="<c:out value="${radus.RADS_ROUTE}"/>" user-title="경로상 주변 표시 반경" /> (M)</td>
								<th>발생위치 주변 표시 반경</th>
								<td><input type="text" name="radsClmt" id="radsClmt" class="date8Type" maxlength="5" value="<c:out value="${radus.RADS_CLMT}"/>" user-title="발생위치 주변 표시 반경" /> (M)</td>
							</tr>
						</table>
						<br />
						<div class="tit">
							<h1>CCTV팝업창크기</h1>
						</div>
						<table>
							<caption>CCTV팝업창크기</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>넓이</th>
								<td><input type="text" name="popWidth" id="popWidth" class="date8Type" maxlength="5" value="<c:out value="${radus.POP_WIDTH}"/>" user-title="넓이" /></td>
								<th>높이</th>
								<td><input type="text" name="popHeight" id="popHeight" class="date8Type" maxlength="5" value="<c:out value="${radus.POP_HEIGHT}"/>" user-title="높이" /></td>
							</tr>
						</table>
						<br />
						<div class="tit">
							<h1>CCTV보기 반경설정 (이벤트위치기준)</h1>
						</div>
						<table>
							<caption>반경</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>반경</th>
								<td><input type="text" name="cctvViewRads" id="cctvViewRads" class="date8Type" maxlength="5" value="<c:out value="${empty radus.CCTV_VIEW_RADS ? 3 : radus.CCTV_VIEW_RADS}"/>" user-title="반경" /> (M)</td>
							</tr>
						</table>
						<br />
						<div class="tit">
							<h1>관제화면폭설정</h1>
						</div>
						<table>
							<caption>관제화면폭설정</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
								<col style="width: 180px;" />
								<col style="width: *" />
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>좌측넓이</th>
								<td><input type="text" name="mntrLeftWidth" id="mntrLeftWidth" class="date8Type" maxlength="5" value="<c:out value="${radus.MNTR_VIEW_LEFT}"/>" user-title="좌측넓이" /></td>
								<th>우측넓이</th>
								<td><input type="text" name="mntrRightWidth" id="mntrRightWidth" class="date8Type" maxlength="5" value="<c:out value="${radus.MNTR_VIEW_RIGHT}"/>" user-title="우측넓이" /></td>
								<th>하단높이</th>
								<td><input type="text" name="mntrBottomHeight" id="mntrBottomHeight" class="date8Type" maxlength="5" value="<c:out value="${radus.MNTR_VIEW_BOTTOM}"/>" user-title="하단높이" /></td>
							</tr>
						</table>
						<br />
						<div class="tit">
							<h1>timeback설정</h1>
						</div>
						<table>
							<caption>timeback설정</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>기본 playbacktime</th>
								<td><input type="text" name="basePlaybacktime" id="basePlaybacktime" class="date8Type" maxlength="5" value="<c:out value="${radus.BASE_PLAYBACKTIME}"/>" user-title="기본 playbacktime" /> (분) 발생일시기준.</td>
							</tr>
						</table>
						<br />
						<table>
							<caption>timeback설정</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>최대 playbacktime</th>
								<td><input type="text" name="maxbfPlaybacktime" id="maxbfPlaybacktime" class="date8Type" maxlength="5" value="<c:out value="${radus.MAX_BF_PLAYBACKTIME}"/>" user-title="최대 playbacktime" /> (분) 발생일시기준, 이전</td>
							</tr>
						</table>
						<br />
						<table>
							<caption>timeback설정</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>최대 playbacktime</th>
								<td><input type="text" name="maxafPlaybacktime" id="maxafPlaybacktime" class="date8Type" maxlength="5" value="<c:out value="${radus.MAX_AF_PLAYBACKTIME}"/>" user-title="최대 playbacktime" /> (분) 발생일시기준, 이후</td>
							</tr>
						</table>
						<br />
						<table>
							<caption>기본재생시간</caption>
							<colgroup>
								<col style="width: 180px;" />
								<col style="width: *" />
							</colgroup>
							<tr>
								<th>기본재생시간</th>
								<td><input type="text" name="playTime" id="playTime" class="date8Type" maxlength="5" value="<c:out value="${radus.PLAY_TIME}"/>" user-title="최대 playbacktime" /> (분) 재생시간기준, *0이면 중지시까지.</td>
							</tr>
						</table>
						<br />
					</div>
					<div class="btnR">
						<a href="#" onclick="fn_cctvRadiusMerge();" class="btn btnSv">저장</a>
					</div>
				</form>
			</div>
			<!-- //content -->
		</div>
		<!-- //container -->
	</div>
	<!-- footer -->
	<div id="footwrap">
		<div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
	</div>
	<!-- //footer -->
</div>
</body>
</html>