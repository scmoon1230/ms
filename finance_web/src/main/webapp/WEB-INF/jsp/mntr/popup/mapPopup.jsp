<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><prprts:value key="HD_TIT" /></title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/mntr/resetCss.css' />">
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap.min.css' />">
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/bootstrap/v3/css/bootstrap-toggle.min.css' />">
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/jqgrid/ui.jqgrid.css' />"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/fontawesome/v5/css/all.min.css' />">
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/mntr/mntr.css' />"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/swip.css' />"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/mntr/gis/swipMap.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/ol5/ol.css'/>">
    <link type="text/css" rel="stylesheet" href="<c:url value='/js/ol5/ol.dev.css'/>">
    <script src="<c:url value='/js/jquery/jquery-3.1.0.min.js' />"></script>
    <script src="<c:url value='/js/jquery/jquery-migrate-3.1.0.min.js' />"></script>
</head>
<body>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<article id="container-map">
    <!-- 지도 메뉴 -->
    <nav class="navbar navbar-default" id="ol-map-menu" style="margin-bottom: 0px;">
        <div class="container-fluid">
            <!-- 기본도 -->
            <div class="navbar-form navbar-left" id="ol-map-menu-basemap" style="padding-left: 0px; padding-right: 0px;">
                <div class="btn-group btn-group-sm" role="group" aria-label="기본맵선택">
                    <!-- <button type="button" class="btn btn-default" id="ol-map-menu-base-normal" onclick="olMap.setBaseLayer('normal');">기본도</button>
                    <button type="button" class="btn btn-default" id="ol-map-menu-base-aerial" onclick="olMap.setBaseLayer('aerial');">항공도</button> -->
                    <button type="button" class="btn btn-default" id="ol-map-menu-base-normal" title="기본도" onclick="olMap.setBaseLayer('normal');"><i class="fas fa-map"></i></button>
                    <button type="button" class="btn btn-default" id="ol-map-menu-base-aerial" title="위성도" onclick="olMap.setBaseLayer('aerial');"><i class="fas fa-globe-asia"></i></button>
                </div>
            </div>
            <!-- 통합검색 -->
            <div class="navbar-form navbar-left" id="ol-map-menu-search" role="search" style="padding-left: 5px;">
                <div class="form-group">
                    <label class="sr-only" for="ol-map-menu-search-keyword">통합검색</label>
                    <input type="text" class="form-control input-sm" id="ol-map-menu-search-keyword" size="20" maxlength="255"
                           placeholder="검색할 키워드를 입력하세요.">
                </div>
                <button type="button" class="btn btn-info btn-sm" onclick="olSwipMap.search.open();">조회</button>
                <ul style="margin-bottom: 0px;">
                    <li role="presentation" class="dropdown" id="ol-map-menu-search-pannel">
                        <ul class="dropdown-menu" id="ol-map-menu-search-grid" role="menu" aria-labelledby="통합검색결과"
                            style="top: 0px; left: 0px; width: 500px; height: auto; background-color: rgba(255, 255, 255, 0.7);">
                            <li style="padding: 0px 5px 0px 5px;">
                                <div class="row" style="margin-bottom: 5px;">
                                    <div class="col-xs-10" style="line-height: 30px;">
                                        <span id="ol-map-menu-search-result"></span>
                                    </div>
                                    <div class="col-xs-2 text-right">
                                        <button type="button" class="btn btn-info btn-sm" onclick="olSwipMap.search.close(false);">닫기</button>
                                    </div>
                                </div>
                                <table id="grid-map-menu-search"></table>
                                <div id="paginate-map-menu-search" class="paginate text-center"></div>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <!-- 측정 -->
            <div class="navbar-form navbar-left" id="ol-map-menu-measure" role="measure" style="padding-left: 5px;">
                <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="측정: 해제"
                        onclick="olSwipMap.measure.clear();">
                    <i class="fas fa-sync-alt fa-lg"></i>
                </button>
                <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="측정: 거리"
                        onclick="olSwipMap.measure.init('LineString');">
                    <i class="fas fa-ruler-horizontal fa-lg"></i>
                </button>
                <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="측정: 면적"
                        onclick="olSwipMap.measure.init('Polygon');">
                    <i class="fas fa-ruler-combined fa-lg"></i>
                </button>
            </div>

            <!-- 즐겨찾기 -->
            <div class="navbar-form navbar-right ol-map-menu-bookmark">
                <div class="form-group">
                    <div class="dropdown ol-map-menu-tools" data-toggle="tooltip" title="즐겨찾기로 지정해둔 지점으로 이동">
                        <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="ol-map-menu-bookmark" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="true">
                            즐겨찾기 <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" id="ol-map-menu-bookmark-list" aria-labelledby="ol-map-menu-bookmark"></ul>
                    </div>
                </div>
                <div class="form-group">
                    <label class="sr-only">공유</label>
                    <button type="button" class="btn btn-default btn-sm ol-map-menu-tools" id="ol-map-menu-bookmark-share-yn" data-toggle="tooltip" title="공유받은 즐겨찾기도 함께 보기">
                        <i class="far fa-check-square fa-lg"></i>
                    </button>
                </div>
            </div>

            <!-- 레이어선택 -->
            <div class="navbar-form navbar-right ol-map-menu-dropdown-layers">
                <div class="form-group">
                    <div class="dropdown ol-map-menu-tools" data-toggle="tooltip" id="dropdown-layers" title="" data-original-title="레이어 선택">
                        <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="ol-map-menu-layer" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                        	<i class="fas fa-layer-group"></i>
                            <span class="caret"></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </nav>
    <div id="map"></div>
    <!-- 오버레이 : 마우스 오버 -->
    <div id="ol-overlay-pointermove" class="panel panel-primary ol-overlay" style="display: none;">
        <div class="panel-heading">
            <span class="panel-heading-title">&nbsp;</span>
        </div>
        <div class="panel-body" style="max-width: 600px;">
            <div class="row">
                <div class="col-xs-12">
                    <table class="table table-striped table-condensed" id="table-pointermove-fclt">
                        <caption>
                            <span class="label label-info">시설물</span>
                        </caption>
                        <thead>
                        <tr>
                            <th scope="col">시설물명</th>
                            <th scope="col">유형(용도)</th>
                            <th scope="col">상태</th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- 오버레이 : 마우스 클릭 -->
    <div id="ol-overlay-click" class="panel panel-primary ol-overlay" style="display: none;">
        <div class="panel-heading">
            <span class="panel-heading-title">&nbsp;</span>
            <button type="button" class="close" onclick="olSwipMap.overlays.click.setPosition(undefined);">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <div class="panel-body" style="max-height: 420px; max-width: 600px; overflow-x: hidden; overflow-y: auto;">
            <div class="row">
                <div class="col-xs-12">
                    <table class="table table-striped table-condensed" id="table-click-fclt">
                        <caption>
                            <span class="label label-info">시설물</span>
                        </caption>
                        <thead>
                        <tr>
                            <th scope="col">시설물명</th>
                            <th scope="col">유형(용도)</th>
                            <th scope="col">상태</th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</article>
<!-- common -->
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap.min.js' />"></script>
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-toggle.min.js' />"></script>
<script src="<c:url value='/js/jqgrid/jquery.jqGrid.src.js' />"></script>
<script src="<c:url value='/js/jqgrid/i18n/grid.locale-kr.js' />"></script>
<script src="<c:url value='/js/jquery/jquery.twbsPagination.min.js' />"></script>
<script src="<c:url value='/js/mntr/cmm.js' />"></script>

<script src="<c:url value='/js/ol5/ol.js'/>"></script>
<script src="<c:url value='/js/ol5/proj4.js'/>"></script>
<!-- Openlayers 에 추가로 필요한 기능들은 ol.dev.js 에 정의한다. -->
<script src="<c:url value='/js/ol5/ol.dev.js'/>"></script>
<script src="<c:url value='/js/spinner/spin.min.js'/>"></script>
<script src="<c:url value='/js/mntr/gis/ol5/mapInit.js'/>"></script>
<script src="<c:url value='/js/mntr/gis/ol5/swipMap.js'/>"></script>
<script src="<c:url value='/js/mntr/gis/engine/${gisEngine}/${gisEngine}.js'/>"></script>
<script src="<c:url value='/js/mntr/api/mapPopup.js'/>"></script>
</body>
</html>
