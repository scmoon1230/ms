<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp" %>
<article id="article-right">
    <div class="col"></div>
    <div class="col">
        <div class="panel panel-info" id="panel-trgt-cctv-list">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-xs-8"><span>영상저장CCTV리스트</span></div>
                </div>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-xs-4" style="padding-right: 1px;">
                        <select class="form-control input-xs sel-fclt-used-ty-cd"></select>
                    </div>
                    <%--
                    <div class="col-xs-1 text-center" style="padding: 0px;">
                        <label>
                            <input type="checkbox" id="chk-always-y"> <span style="position: relative; top: -3px;">상시</span>
                        </label>
                    </div>
                    --%>
                    <div class="col-xs-5" style="padding-left: 1px;">
                        <input type="text" class="form-control input-xs txt-keyword" maxlength="32" size="32" title="검색 키워드" placeholder="시설물명 | 주소 를 입력하세요." data-grid-id="trgt-cctv-list"/>
                    </div>
                    <div class="col-xs-2 text-right">
                        <button class="btn btn-default btn-xs btn-search" data-grid-id="trgt-cctv-list">조회</button>
                    </div>
                </div>
            </div>
            <table id="grid-trgt-cctv-list"></table>
            <div id="paginate-trgt-cctv-list" class="paginate text-center"></div>
            <div class="panel-footer">
                <div class="row">
                    <div class="col-xs-8 text-danger">
                        <span id="title-trgt-cctv-list" style="font-size: 1.2em; font-weight: bolder; line-height: 22px;"></span>
                    </div>
                    <%--
                    <div class="col-xs-4 text-right">
                        <button type="button" class="btn btn-info btn-xs" id="btn-send" data-toggle="tooltip" data-placement="top" title="영상분석시스템전송">영상분석시스템전송</button>
                    </div>
                     --%>
                </div>
            </div>
        </div>
    </div>
    <%--
    <div class="row">
        <div class="col-xs-12">
            <h5 class="text-primary text-right" id="notice-trgt-cctv-list">
                <i class="fas fa-exclamation-circle"></i> 영상분석시스템과 시스템에서 일 정기 동기화
            </h5>
            <h5 class="text-primary text-right">
                <i class="fas fa-info-circle"></i> 영상분석시스템에 즉시 적용시 영상분석시스템전송 처리
            </h5>
        </div>
    </div>
     --%>
</article>
