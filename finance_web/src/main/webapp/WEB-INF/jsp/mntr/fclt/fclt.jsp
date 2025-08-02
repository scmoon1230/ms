<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<article id="article-grid">
    <%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
	<div id="searchBar" class="row">
		<!-- <div class="tit">
			<h4>${common.title}</h4>
		</div> -->

		<div class="searchBox searchBox-xs">
			<div class="col-xs-4">
				<label>
					<input type="radio" name="radioRadius" id="setRadius" value="set">
					반경설정
				</label>
			</div>
			<div class="col-xs-4">
				<label>
					<input type="radio" name="radioRadius" id="unSetRadius" value="unset" checked="checked">
					반경해제
				</label>
			</div>
			<div class="col-xs-4">
				<div class="form-group">
					<input class="form-control input-xs" id="inputRadius" size="8" placeholder="설정반경" readonly="readonly">
				</div>
			</div>
		</div>

		<div class="searchBox searchBox-xs">
			<div class="col-xs-4">
				<div class="form-group">
					<select id="searchFcltKndCd" name="searchFcltKndCd" class="form-control input-xs"></select>
				</div>
			</div>
			<div class="col-xs-4">
				<div class="form-group">
					<select id="searchFcltUsedTyCd" name="searchFcltUsedTyCd" class="form-control input-xs"></select>
				</div>
			</div>
			<div class="col-xs-4">
				<div class="form-group">
					<select id="searchFcltSttus" name="searchFcltSttus" class="form-control input-xs"></select>
				</div>
			</div>
		</div>

		<div class="searchBox searchBox-xs">
			<!--
			<div class="col-xs-4">
				<div class="form-group">
					<select id="searchPlcPtrDiv" name="searchPlcPtrDiv" class="form-control input-xs"></select>
				</div>
			</div>
			<div class="col-xs-4">
				<div class="form-group">
					<select id="searchAreaCd" name="searchAreaCd" class="form-control input-xs"></select>
				</div>
			</div>
			<div class="col-xs-4">
				<div class="form-group">
					<input id="searchFcltId" name="searchFcltId" class="form-control input-xs" maxlength="32" size="32" placeholder="시설물명 | 주소 | 관리번호를 입력하세요." />
				</div>
			</div>
			 -->
			<div class="col-xs-12">
				<div class="form-group">
					<input id="searchFcltId" name="searchFcltId" class="form-control input-xs" maxlength="32" size="32" placeholder="시설물명 | 주소 를 입력하세요." />
				</div>
			</div>
		</div>

		<div class="searchBox searchBox-xs">
			<div class="col-xs-10">
				<div class="form-inline">
					<c:if test="${LoginVO.userId=='sys'}">	<!-- sys계정으로만 -->
						<c:if test="${LoginVO.gSysId=='PVEWIDE'}">	<!-- 광역일 때 -->
							<button id="sendBtn" onclick="oFclt.retrieveCctvInfoFromBase();" class="btn btn-default btn-xs">카메라정보를 기초에서 가져오기</button>
							<button id="sendBtn" onclick="oFclt.retrieveCctvStateFromBase();" class="btn btn-default btn-xs">카메라상태를 기초에서 가져오기</button>
						</c:if>
					</c:if>
					<div class="checkbox hidden">
						<label class="control-label">
							<input type="checkbox" id="includeResultToMap" title="결과 지도에 반영" checked> 결과 지도에 반영
						</label>
					</div>
					<div class="checkbox hidden">
						<label class="control-label">
							<input type="checkbox" id="searchIncludeMissingPlcPtrDiv" title="경찰지구대누락포함" checked> 경찰지구대누락포함
						</label>
					</div>
					<div class="checkbox hidden">
						<label class="control-label">
							<input type="checkbox" id="searchLprOnly" title="차량번호인식CCTV만 표출"> 차량번호인식CCTV
						</label>
					</div>
				</div>
			</div>
			<div class="col-xs-2 text-right">
				<div class="form-group">
					<button id="sendBtn" onclick="oFclt.reloadGrid();" class="btn btn-default btn-xs">조회</button>
				</div>
			</div>
		</div>

		<div class="grid-header row">
			<div class="col-xs-9">
				<div class="tb_title">조회결과</div>
			</div>
			<div class="col-xs-3 text-right">
				<span id="rowCnt" class="totalNum"></span> 건
			</div>
		</div>
		<table id="grid-fclt"></table>
		<div id="paginate-fclt" class="paginate text-center"></div>

		<br>

		<div class="searchBox searchBox-xs" style="display:none;">
			<div class="form-inline text-right" style="padding-right: 10px;">
				<div class="form-group">
					<button id="btn-fclt-reg" class="btn btn-default btn-xs tooltip-fclt" title="시설물을 단일 등록합니다." data-placement="top"
						onclick="oFclt.reg();">단일등록</button>
				</div>
				<div class="form-group">
					<button id="excelUploadButton" class="btn btn-default btn-xs tooltip-fclt" title="시설물을 목록을 Excel 파일로 업로드합니다." data-placement="top"
						onclick="oFclt.uploadExcel();">엑셀 업로드</button>
				</div>
				<div class="form-group">
					<button id="excelDownloadButton" class="btn btn-default btn-xs tooltip-fclt" title="시설물을 목록을 Excel 파일로 다운로드합니다." data-placement="top"
						onclick="oFclt.downloadExcel();">엑셀 다운로드</button>
				</div>
				<div class="form-group">
					<button id="submitButton" class="btn btn-default btn-xs tooltip-fclt" title="시설물 중 좌표나 주소가 누락된 시설물 목록을 표출합니다." data-placement="top"
						onclick="oFclt.checkFcltData();">체크리스트</button>
				</div>
			</div>
		</div>
		
	</div>
</article>
<script src="<c:url value='/js/mntr/fclt/fclt.js' />"></script>