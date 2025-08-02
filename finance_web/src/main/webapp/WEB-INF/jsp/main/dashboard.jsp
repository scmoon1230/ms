<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<%@ include file="/WEB-INF/jsp/mntr/cmm/commonDefinitions.min.jsp" %>
	<link type="text/css" rel="stylesheet" href="<c:url value='/js/chart/apexcharts/dist/apexcharts.css' />">
	<link type="text/css" rel="stylesheet" href="<c:url value='/css/wrks/lgn/v2/ui_base.css' />">
	<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
</head>
<%-- <body style="overflow: hidden;"> --%>
<body>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
<div id="popWrap">
	<div class="popHeader">
		<c:set var="dstrtCd" scope="request"><prprts:value key="DSTRT_CD" /></c:set>
		<h1><strong><img src="<c:url value='/images/wrks/lgn/v2/logo_login_${dstrtCd}_1.png' />" alt="" style="width:120px;"></strong>
			<span>수입 및 지출 현황</span>
		</h1>
		<div class="rightGrp">
			<button type="button" class="btnUpdate" title="새로고침"></button>
			<%-- <a href="#" class="btnPopClose" title="팝업 닫기">팝업 닫기</a> --%>
		</div>
	</div>
	<div class="popContent">
		<div class="chartWrap">
			<div class="w33p">
				<div class="box bg1">
					<h2><strong>금일</strong> 수입 현황</h2>
					<div class="statusArea">
						<ul class="row1">
							<li class="title">열람</li>
							<li style="width:calc(38% - 7px);"><strong class="tvoViewState cntRqst"></strong><span>열람 신청</span></li>
							<li style="width:calc(38% - 7px);"><strong class="tvoViewState cntPrgrs10"></strong><span>열람 미승인</span></li>
						</ul>
						<ul class="row2">
							<li class="title">기간연장</li>
							<li style="width:calc(38% - 7px);"><strong class="tvoViewExtnState cntRqst"></strong><span>기간연장 신청</span></li>
							<li style="width:calc(38% - 7px);"><strong class="tvoViewExtnState cntPrgrs10"></strong><span>기간연장 미승인</span></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="w66p">
				<div class="box">
					<h2><strong>금월</strong> 영상 <strong1>열람</strong1> 승인 현황</h2>
					<div class="chart" id="chart-view_aprv-state-daily"></div>
					<span class="unit">단위 : 건</span>
				</div>
			</div>
			<div class="w33p">
				<div class="box bg1">
					<h2><strong>금일</strong> 지출 현황</h2>
					<div class="statusArea">
						<ul class="row3">
							<li class="title">반출</li>
							<li style="width:calc(38% - 7px);"><strong class="tvoOutState cntRqst"></strong><span>반출 신청</span></li>
							<li style="width:calc(38% - 7px);"><strong class="tvoOutState cntPrgrs10"></strong><span>반출 미승인</span></li>
						</ul>
						<ul class="row4">
							<li class="title">기간연장</li>
							<li style="width:calc(38% - 7px);"><strong class="tvoOutExtnState cntRqst"></strong><span>기간연장 신청</span></li>
							<li style="width:calc(38% - 7px);"><strong class="tvoOutExtnState cntPrgrs10"></strong><span>기간연장 미승인</span></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="w66p">
				<div class="box">
					<h2><strong>금월</strong> 영상 <strong3>반출</strong3> 승인 현황</h2>
					<div class="chart" id="chart-out_aprv-state-daily"></div>
					<span class="unit">단위 : 건</span>
				</div>
			</div>
			<div class="w33p">
				<div class="box bg1">
					<h2>인력 현황</h2>
					<div class="statusArea">
						<ul class="row5">
							<li class="title">CCTV</li>
							<li style="width:calc(38% - 7px);"><strong class="tvoCctvState cntCctv"></strong><span>전체</span></li>
							<li style="display:none;"><strong class="tvoCctvState cntIvaTy1"></strong><span>저장대상</span></li>
							<li style="width:calc(38% - 7px);"><strong class="tvoCctvState cntIvaTy2"></strong><span>반출대상</span></li>
						</ul>
						<ul class="row6">
							<li class="title">스토리지</li>
							<li style="width:calc(38% - 7px);"><strong class="storageState totalSize"></strong><span>전체</span></li>
							<li style="width:calc(38% - 7px);"><strong class="storageState freeSize"></strong><span class="freeRate"></span>&nbsp;<span>여유</span></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="w33p">
				<div class="box bg1">
					<h2><strong>금월</strong> 영상 열람승인 현황</h2>
					<div class="chart" id="chart-view_aprv-state-monthly"></div>
				</div>
			</div>
			<div class="w33p">
				<div class="box bg1">
					<h2><strong>금월</strong> 영상 반출승인 현황</h2>
					<div class="chart" id="chart-out_aprv-state-monthly"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- script -->
<script src="<c:url value='/js/chart/apexcharts/dist/apexcharts.js' />"></script>
<script src="<c:url value='/js/main/dashboard.js' />"></script>
<script>
	let contextRoot = '${pageContext.request.contextPath}';
	let oTvoDashboard;
	$(function () {
		oTvoDashboard = new tvoDashboard();
		oTvoDashboard.init();
	});
</script>
<!-- //script -->
</body>
</html>

