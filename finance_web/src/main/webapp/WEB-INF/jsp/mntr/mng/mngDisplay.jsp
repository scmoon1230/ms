<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<style>
aside#left {
	display: none;
}
</style>
<article id="article-grid">

				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>

	<form:form commandName="configureVO" name="configureVO" action="/mntr/saveMngDisplay.json" method="post">
		<div class="searchBox sm">
			<div class="form-inline">
				<div class="form-group">
					<button type="button" class="btn btn-default btn-sm" onclick="oMngDisplay.save();">저장</button>
				</div>
			<%--<div class="form-group">
					<button type="button" class="btn btn-default btn-sm" onclick="oMngDisplay.cancel();">취소</button>
				</div>
				<div class="form-group">
					<button type="button" class="btn btn-default btn-sm" onclick="oMngDisplay.reset();">기본설정</button>
				</div>--%>
			</div>
		</div>

		<div class="row bg-info">
			<label class="col-xs-2 control-label">지도 설정</label>
			<div class="col-xs-10">
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="gisTy" cssClass="control-label">기본도 설정: </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="radio-inline">
								<form:radiobutton path="gisTy" value="0" title="일반" />
								일반
							</label>
							<label class="radio-inline">
								<form:radiobutton path="gisTy" value="1" title="항공" />
								항공
							</label>
						</div>
					</div>
					<div class="col-xs-4">
						<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="기본도, 항공도 선택">
							<i class="far fa-question-circle"></i>
						</button>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="gisLabelViewScale" cssClass="control-label">시설물 레이블표시 축척 (기본값: 4000): </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<form:input type="number" path="gisLabelViewScale" cssClass="form-control input-sm" title="시설물 레이블표시 축척" placeholder="시설물 레이블표시 축척"
								maxlength="6" />
						</div>
					</div>
					<div class="col-xs-4">
						<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="지도상에 시설물이 많아 표출시간이 지연될 경우를 대비, 설정한 축척에 따라 시설물 레이블을 표시">
							<i class="far fa-question-circle"></i>
						</button>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="gisFeatureViewScale" cssClass="control-label">시설물 아이콘표시 축척 (기본값: 7000): </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<form:input type="number" path="gisFeatureViewScale" cssClass="form-control input-sm" title="시설물 아이콘표시 축척" placeholder="시설물 아이콘표시 축척"
								maxlength="6" />
						</div>
					</div>
					<div class="col-xs-4">
						<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="지도상에 시설물이 많아 표출시간이 지연될 경우를 대비, 설정한 축척에 따라 시설물아이콘 표시">
							<i class="far fa-question-circle"></i>
						</button>
					</div>
				</div>
			</div>
		</div>

		<div class="row bg-warning">
			<label class="col-xs-2 control-label">아이콘 설정</label>
			<div class="col-xs-10">
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="iconTy" cssClass="control-label">아이콘타입: </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="radio-inline">
								<form:radiobutton path="iconTy" value="A" title="TypeA" />
								TypeA
							</label>
							<label class="radio-inline">
								<form:radiobutton path="iconTy" value="B" title="TypeB" />
								TypeB
							</label>
						</div>
					</div>
					<div class="col-xs-4">
						<!-- <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="Tooltip on top"><i class="far fa-question-circle"></i></button> -->
					</div>
				</div>

				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="iconSize" cssClass="control-label">아이콘 크기: </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<form:select path="iconSize" cssClass="form-control input-sm" title="아이콘크기">
								<form:option value="30"> 30 x 30 </form:option>
								<form:option value="40"> 40 x 40 </form:option>
								<form:option value="50"> 50 x 50 </form:option>
							</form:select>
						</div>
					</div>
					<div class="col-xs-4">
						<!-- <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="Tooltip on top"><i class="far fa-question-circle"></i></button> -->
					</div>
				</div>
			</div>
		</div>

		<div class="row bg-info">
			<label class="col-xs-2 control-label">상황 발생</label>
			<div class="col-xs-10">
				<%--
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="leftDivHdnYn" cssClass="control-label">사이드 영역 표시: </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="checkbox-inline">
								<input type="checkbox" id="chk-div-hdn-left" name="chk-div-hdn-left" /> 좌측
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" id="chk-div-hdn-right" name="chk-div-hdn-right" /> 우측
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" id="chk-div-hdn-bottom" name="chk-div-hdn-bottom" /> 하단
							</label>
							<form:hidden path="leftDivHdnYn" />
						</div>
					</div>
					<div class="col-xs-4">
						<!-- <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="Tooltip on top"><i class="far fa-question-circle"></i></button> -->
					</div>
				</div>
 			--%>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="evtLcMoveYn" cssClass="control-label">자동 표출: </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="radio-inline">
								<form:radiobutton path="evtLcMoveYn" value="Y" title="Y" />
								Y
							</label>
							<label class="radio-inline">
								<form:radiobutton path="evtLcMoveYn" value="N" title="N" />
								N
							</label>
						</div>
					</div>
					<div class="col-xs-4">
						<!-- <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="Tooltip on top"><i class="far fa-question-circle"></i></button> -->
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="radsClmt" cssClass="control-label">발생지점 인접CCTV검색 반경 (기본값: 500): </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<form:input type="number" path="radsClmt" cssClass="form-control input-sm" title="발생지점 인접CCTV검색 반경" placeholder="발생지점 인접CCTV검색 반경"
								maxlength="5" />
						</div>
					</div>
					<div class="col-xs-4">
						<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="발생지점 인접CCTV 조회반경, 투망감시 간 발생지점 인점CCTV DIV에서 사용">
							<i class="far fa-question-circle"></i>
						</button>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="radsRoute" cssClass="control-label">투망감시 표시 반경 (기본값: 500): </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<form:input type="number" path="radsRoute" cssClass="form-control input-sm" title="투망감시 표시 반경" placeholder="투망감시 표시 반경" maxlength="5" />
						</div>
					</div>
					<div class="col-xs-4">
						<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="투망감시 간 지도상에 표시하는 내부 반경의 크기, 외부 반경은 내부 반경의 2배">
							<i class="far fa-question-circle"></i>
						</button>
					</div>
				</div>
			</div>
		</div>

		<div class="row bg-warning">
			<label class="col-xs-2 control-label">영상 재생</label>
			<div class="col-xs-10">
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="cnOsvtPlaytimeStopYn" cssClass="control-label">일정시간동안 화면조작이 없을 경우 평시화면으로 전환 (투망감시): </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="radio-inline">
								<form:radiobutton path="cnOsvtPlaytimeStopYn" value="Y" title="Y" />
								Y
							</label>
							<label class="radio-inline">
								<form:radiobutton path="cnOsvtPlaytimeStopYn" value="N" title="N" />
								N
							</label>
						</div>
					</div>
					<div class="col-xs-4">
						<!-- <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="Tooltip on top"><i class="far fa-question-circle"></i></button> -->
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="fullScreenCloseYn" cssClass="control-label">대화면(CCTV 팝업창) 닫기: </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="radio-inline">
								<form:radiobutton path="fullScreenCloseYn" value="Y" title="Y" />
								Y
							</label>
							<label class="radio-inline">
								<form:radiobutton path="fullScreenCloseYn" value="N" title="N" />
								N
							</label>
						</div>
					</div>
					<div class="col-xs-4">
						<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="대화면(CCTV 팝업창) 창이 열린 상태에서 다른 메뉴로 전환 시 대화면(CCTV 팝업창)의 닫기여부">
							<i class="far fa-question-circle"></i>
						</button>
					</div>
				</div>
			</div>
		</div>

		<div class="row bg-info">
			<label class="col-xs-2 control-label">관제화면 설정</label>
			<div class="col-xs-10">
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="mntrViewLeft" cssClass="control-label">좌측넓이 (PX) (기본값: 400): </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<form:input type="number" path="mntrViewLeft" cssClass="form-control input-sm" title="좌측넓이" placeholder="좌측넓이" maxlength="5" />
						</div>
					</div>
					<div class="col-xs-4">
						<!-- <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="Tooltip on top"><i class="far fa-question-circle"></i></button> -->
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="mntrViewRight" cssClass="control-label">우측넓이 (PX) (기본값: 400): </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<form:input type="number" path="mntrViewRight" cssClass="form-control input-sm" title="우측넓이" placeholder="우측넓이" maxlength="5" />
						</div>
					</div>
					<div class="col-xs-4">
						<!-- <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="Tooltip on top"><i class="far fa-question-circle"></i></button> -->
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<form:label path="mntrViewBottom" cssClass="control-label">하단높이 (PX) (기본값: 300): </form:label>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<form:input type="number" path="mntrViewBottom" cssClass="form-control input-sm" title="하단높이" placeholder="하단높이" maxlength="5" />
						</div>
					</div>
					<div class="col-xs-4">
						<!-- <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="Tooltip on top"><i class="far fa-question-circle"></i></button> -->
					</div>
				</div>
			</div>
		</div>

		<!-- For Validator -->
		<form:hidden path="leftDivHdnYn" />
		<form:hidden path="popHeight" />
		<form:hidden path="popWidth" />
		<form:hidden path="cctvViewRads" />
		<form:hidden path="playTime" />
		<form:hidden path="basePlaybacktime" />
		<form:hidden path="maxPlaybacktime" />
		<form:hidden path="maxAfPlaybacktime" />
		<form:hidden path="maxBfPlaybacktime" />
		<%--
		<c:if test="${LoginVO.sysCd ne '119' and LoginVO.sysCd ne 'DUC' and LoginVO.sysCd ne 'WPS' and LoginVO.sysCd ne 'ESS'}">
			<form:hidden path="radsClmt" />
			<form:hidden path="radsRoute" />
		</c:if>
 		--%>
		<%-- Unused
		<form:hidden path="pointX" title="좌표X" />
		<form:hidden path="pointY" title="좌표Y" />
		<form:hidden path="pointZ" title="좌표Z" />
		<form:hidden path="gisLevel" title="지도레벨" />
		<form:hidden path="mapAltitud" title="지도고도" />
		<form:hidden path="mapTilt" title="지도기울기" />
		<form:hidden path="mapDirect" title="지도방위" />

		<form:hidden path="mntrTyId" title="모니터링유형아이디" />
		<form:hidden path="setTime" title="알람표출시간" />
		<form:hidden path="cnOsvtOpt" title="투망감시옵션" />
		<form:hidden path="fcltBaseItem" title="시설물기본항목" />
		<form:hidden path="popRoute" title="이동경로" />
		<form:hidden path="divMoveSet" title="화면이동설정" />
 		--%>
	</form:form>
</article>
<script src="<c:url value='/mntr/validator.do'/>"></script>
<validator:javascript formName="configureVO" staticJavascript="false" xhtml="true" cdata="false" />
<script src="<c:url value='/js/mntr/mng/mngDisplay.js'/>"></script>