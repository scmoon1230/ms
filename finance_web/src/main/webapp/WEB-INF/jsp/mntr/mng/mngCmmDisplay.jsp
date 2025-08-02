<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<style>
    aside#left {
        display: none;
    }

    .row.bg-info .form-group, .row.bg-warning .form-group {
        margin: 0;
        padding: 3px;
    }

    .row.bg-info, .row.bg-warning {
        display: flex;
        align-items: center;
    }

    .row.bg-info label, .row.bg-warning label {
        margin-bottom: 0;
    }

    .row {
        display: flex;
        align-items: center;
        min-height: 30px;
    }

    .video-search .col-xs-4 {
        padding-left: 16px;
    }

    .video-search .col-xs-4 + .col-xs-4 {
        padding-left: 20px;
        padding-right: 6px;
    }

    .video-search .col-xs-4 + .col-xs-4 + .col-xs-4 {
        padding-left: 24px;
    }
</style>
<article id="article-grid" style="overflow-y: scroll;">

			<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
	
	<div class="searchBox sm">
		<div class="form-inline">
		<%--<div class="form-group">
				<select path="sysId" class="form-control input-sm">
					<option value="TVO" <c:if test="${configure.sysId eq 'TVO'}">selected</c:if>>영상반출</option>
				</select>
			</div>--%>	
		<%--<div class="form-group">
				<button type="button" class="btn btn-default btn-sm" onclick="oMngCmmDisplay.search();">조회</button>
			</div>--%>
		<%--<div class="form-group">
				<button type="reset" class="btn btn-default btn-sm">리셋</button>
			</div>
			<div class="form-group">
				<button type="button" class="btn btn-default btn-sm" onclick="oMngCmmDisplay.cancel();">취소</button>
			</div>--%>
			<div class="form-group">
				<button type="button" class="btn btn-default btn-sm" onclick="oMngCmmDisplay.save();">저장</button>
			</div>
		</div>
	</div>

	<div class="row bg-info">
		<label class="col-xs-2 control-label">대화면(CCTV 팝업창)</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">넓이 X 높이: </label>
					</div>
				</div>
				<div class="col-xs-2">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="popWidth" name="popWidth"
						 value="<prprts:value key='POP_WIDTH' />" title="팝업넓이" placeholder="팝업넓이" maxlength="4" />
					</div>
				</div>
				<div class="col-xs-2">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="popHeight" name="popHeight"
						 value="<prprts:value key='POP_HEIGHT' />" title="팝업높이" placeholder="팝업높이" maxlength="4" />
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="대화면(CCTV 팝업창) 넓이">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 
	<div class="row bg-warning" style="display:none;">
		<label class="col-xs-2 control-label">상황 발생</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						   <label class="control-label">발생위치기준 CCTV검색최대반경 (M): </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="" name="" value="cctvViewRads" title="CCTV 검색 반경" placeholder="CCTV 검색 반경" maxlength="5" />
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="발생위치을 기점으로 감시에 참여할 CCTV 검색 반경을 설정">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
			<div class="row" style="display:none;">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">자동 표출: </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<radiobutton path="evtLcMoveYn" value="Y" title="Y" /> Y
						</label>
						<label class="radio-inline">
							<radiobutton path="evtLcMoveYn" value="N" title="N" /> N
						</label>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			   <div class="row">
				   <div class="col-xs-4">
					   <div class="form-group">
						   <label class="control-label">이벤트 알람소리여부: <span class="text-danger"> <i
								   class="fas fa-exclamation-triangle"></i> 시스템전반적으로 변경됨</span></label>
					   </div>
				   </div>
				   <div class="col-xs-4">
					   <div class="form-group">
						   <label class="radio-inline">
							   <radiobutton path="webSocketSoundUseYn" value="Y" title="Y"/> Y
						   </label>
						   <label class="radio-inline">
							   <radiobutton path="webSocketSoundUseYn" value="N" title="N"/> N
						   </label>
					   </div>
				   </div>
				   <div class="col-xs-4">
					   <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="PC경고음울림여부, 이벤트별 경고음소거는 이벤트기본정보에서처리">
						   <i class="far fa-question-circle"></i>
					   </button>
				   </div>
			   </div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						   <label class="control-label">발생위치기준 인접CCTV(DIV)검색 반경 (권장값: 500): </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="" name="" value="radsClmt" title="발생지점 인접CCTV검색 반경" placeholder="발생지점 인접CCTV검색 반경" maxlength="5" />
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="발생지점 인접CCTV 조회반경, 발생지점 인점CCTV DIV에서 사용">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
			<div class="row" style="display:none;">
				<div class="col-xs-4">
					<div class="form-group">
						   <label class="control-label">투망감시 표시 반경 (권장값: 500): </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="" name="" value="radsRoute" title="투망감시 표시 반경" placeholder="투망감시 표시 반경" maxlength="5" />
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
	 -->
	<div class="row bg-warning">
		<label class="col-xs-2 control-label">영상 재생</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">기본재생시간 (분): </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="playTime" name="playTime"
						 value="<prprts:value key='PLAY_TIME' />" title="기본재생시간" placeholder="기본재생시간" maxlength="3" />
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="현재영상 기본 재생시간">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
			<!-- 
			<div class="row" style="display:none;">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">일정시간동안 화면조작이 없을 경우 평시화면으로 전환: </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<radiobutton path="cnOsvtPlaytimeStopYn" value="Y" title="Y" /> Y
						</label>
						<label class="radio-inline">
							<radiobutton path="cnOsvtPlaytimeStopYn" value="N" title="N" /> N
						</label>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			 -->
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">대화면(CCTV 팝업창) 닫기: </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="fullScreenCloseYn1" name="fullScreenCloseYn" title="Y" value="Y" />Y
						</label>
						<label class="radio-inline">
							<input type="radio" id="fullScreenCloseYn2" name="fullScreenCloseYn" title="N" value="N" />N
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
		<label class="col-xs-1 control-label">과거영상</label>
		   <div class="col-xs-11">
			   <div class="row">
				   <label class="col-xs-1 control-label">시작</label>
				   <div class="col-xs-11 video-search">
					<div class="row">
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label">
									<strong class="text-primary">발생일시</strong>기준 <strong class="text-primary">이전</strong> 기본시작시간 (분):
								</label>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<input type="number" class="form-control input-sm" id="basePlaybacktime" name="basePlaybacktime"
								 value="<prprts:value key='BASE_PLAYBACKTIME' />" title="발생일시기준 이전 기본시작시간(분)" placeholder="발생일시기준 이전 기본시작시간(분)"/>
							</div>
						</div>
						<div class="col-xs-4">
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label" for="maxBfPlaybacktimeNow">
									<strong class="text-danger">현재시각</strong>기준 <strong class="text-primary">이전</strong> 최대검색시간 (분):
								</label>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<input type="number" class="form-control input-sm" id="maxBfPlaybacktimeNow" name="maxBfPlaybacktimeNow"
								 value="<prprts:value key='MAX_BF_PLAYBACKTIME_NOW' />" title="현재시각기준 이전 최대검색시간(분)" placeholder="현재시각기준 이전 최대검색시간(분)"/>
							</div>
						</div>
						<div class="col-xs-4">
							<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="영상보관일수,설정범위(현재시각기준최대30일, 43200분), 발생일시가 이전최대검색시간보다 이전이면 [영상열람]불가">
								<i class="far fa-question-circle"></i>
							</button>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label" for="maxBfPlaybacktime">
									<strong class="text-primary">발생일시</strong>기준 <strong class="text-primary">이전</strong> 최대검색시간 (분):
								 </label>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<input type="number" class="form-control input-sm" id="maxBfPlaybacktime" name="maxBfPlaybacktime"
								 value="<prprts:value key='MAX_BF_PLAYBACKTIME' />" title="발생일시기준 이전 최대검색시간(분)" placeholder="발생일시기준 이전 최대검색시간(분)" maxlength="3"/>
								<hidden id="maxPlaybacktime" name="maxPlaybacktime" value="<prprts:value key='MAX_PLAYBACKTIME' />"/>
							</div>
						</div>
						<div class="col-xs-4">
							<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="(현재시각 - 현재시각기준이전 최대검색시간) > 발생일시보다 클경우 적용">
								<i class="far fa-question-circle"></i>
							</button>
						</div>
					</div>
				</div>
			   </div>
			   <div class="row" style="border-top: 1px solid #eee;">
				   <label class="col-xs-1 control-label">종료</label>
				   <div class="col-xs-11 video-search">
		  				<div class="row">
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label" for="maxAfPlaybacktime">
									<strong class="text-primary">발생일시</strong>기준 <strong class="text-danger">이후</strong> 최대검색시간 (분):
								 </label>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<input type="number" class="form-control input-sm" id="maxAfPlaybacktime" name="maxAfPlaybacktime"
								 value="<prprts:value key='MAX_AF_PLAYBACKTIME' />" title="발생일시기준 이후 최대검색시간(분)" placeholder="발생일시기준 이후 최대검색시간(분)" maxlength="3"/>
							</div>
						</div>
						<div class="col-xs-4">
							<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="0일 경우 제한 없음">
								<i class="far fa-question-circle"></i>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row bg-warning">
	   <label class="col-xs-2 control-label">GIS</label>
	   <div class="col-xs-10">
		   <div class="row">
			   <div class="col-xs-4">
				   <div class="form-group">
					   <label class="control-label">배경지도 선택 <span class="text-danger"> <i class="fas fa-exclamation-triangle"></i></span> </label>
				   </div>
			   </div>
			   <div class="col-xs-4">
				   <div class="form-group">
					   <select class="form-control input-sm" id="gisEngine" name="gisEngine" title="배경지도 선택" data-selected="<prprts:value key='GIS_ENGINE' />">
						   <option value="VWORLD" >브이월드</option>
						   <option value="KAKAOMAP" >카카오맵</option>
					   </select>
				   </div>
			   </div>
			   <div class="col-xs-4">
                   	<button type="button" class="btn btn-default btn-sm" onclick="oMngCmmDisplay.saveProjection();">좌표변환</button>
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="지도선택. 좌표변환-필요시 지도엔진의 좌표업데이트.">
						<i class="far fa-question-circle"></i>
					</button>
			   </div>
		   </div>
	   </div>
   </div>

	<div class="row bg-info">
		<label class="col-xs-2 control-label">지도 설정</label>
			<div class="col-xs-10">
			   <div class="row">
				   <div class="col-xs-4">
					   <div class="form-group">
						   <label class="control-label">기본화면 위치: </label>
					   </div>
				   </div>
				   <div class="col-xs-2">
					   <div class="form-group">
						   <input type="number" class="form-control input-sm" id="pointX" name="pointX"
							value="<prprts:value key='POINT_X' />" pattern=".000000" title="경도" placeholder="경도" maxlength="10"/>
					   </div>
				   </div>
				   <div class="col-xs-2">
					   <div class="form-group">
						   <input type="number" class="form-control input-sm" id="pointY" name="pointY"
							value="<prprts:value key='POINT_Y' />" pattern=".000000" title="위도" placeholder="위도" maxlength="10"/>
					   </div>
				   </div>
				   <div class="col-xs-4">
					   <button type="button" class="btn btn-default btn-sm" id="btn-map-popup" data-toggle="tooltip" title="지도상에서 좌표를 선택합니다.">지도에서 찾기</button>
				   </div>
				</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">기본도 설정: </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="gisTy1" name="gisTy" title="일반" value="0" />일반
						</label>
						<label class="radio-inline">
							<input type="radio" id="gisTy2" name="gisTy" title="항공" value="1" />항공
						</label>
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="기본도, 항공도 선택">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
			<!-- 
			<div class="row" style="display:none;">
				<div class="col-xs-4">
					<div class="form-group">
						   <label class="control-label">시설물명 표시 해상도 (권장값: 4000): </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="" name="" value="gisLabelViewScale" title="시설물 레이블표시 축척" placeholder="시설물 레이블표시 축척"
							maxlength="6" />
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="지도상에 시설물이 많아 표출시간이 지연될 경우를 대비, 설정한 축척에 따라 시설물 레이블을 표시">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
			<div class="row" style="display:none;">
				<div class="col-xs-4">
					<div class="form-group">
						   <label class="control-label">시설물아이콘 표시 해상도 (권장값: 7000): </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="" name="" value="gisFeatureViewScale" title="시설물 아이콘표시 축척" placeholder="시설물 아이콘표시 축척"
							maxlength="6" />
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="지도상에 시설물이 많아 표출시간이 지연될 경우를 대비, 설정한 축척에 따라 시설물아이콘 표시">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
			 -->
		</div>
	</div>
	<!-- 
	   <div class="row bg-info" style="display:none;">
		<label class="col-xs-2 control-label">아이콘 설정</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">아이콘타입: </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<radiobutton path="iconTy" value="A" title="TypeA" /> TypeA
						</label>
						<label class="radio-inline">
							<radiobutton path="iconTy" value="B" title="TypeB" /> TypeB
						</label>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>

			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">아이콘 크기: </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<select class="form-control input-sm" id="" name="" value="iconSize" title="아이콘크기">
							<option value="30"> 30 x 30 </option>
							<option value="40"> 40 x 40 </option>
							<option value="50"> 50 x 50 </option>
						</select>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
		</div>
	</div>
	 -->
	<div class="row bg-warning">
		<label class="col-xs-2 control-label">관제화면 설정</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
					   <label class="control-label">좌측넓이 (PX) (권장값: 1000) / 우측넓이 (PX) (권장값: 400): </label>
					</div>
				</div>
				<div class="col-xs-2">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="mntrViewLeft" name="mntrViewLeft"
						 value="<prprts:value key='MNTR_VIEW_LEFT' />" title="좌측넓이" placeholder="좌측넓이" maxlength="5" />
					</div>
				</div>
				<div class="col-xs-2">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="mntrViewRight" name="mntrViewRight"
						 value="<prprts:value key='MNTR_VIEW_RIGHT' />" title="우측넓이" placeholder="우측넓이" maxlength="5" />
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
					   <label class="control-label">하단높이 (PX) (권장값: 300): </label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<input type="number" class="form-control input-sm" id="mntrViewBottom" name="mntrViewBottom"
						 value="<prprts:value key='MNTR_VIEW_BOTTOM' />" title="하단높이" placeholder="하단높이" maxlength="5" />
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
		</div>
	</div>
	
    <div class="row bg-info">
        <label class="col-xs-2 control-label">메뉴</label>
        <div class="col-xs-10">
            <div class="row">
				<div class="col-xs-4">
                    <div class="form-group">
                        <label path="menuOrdrTy" cssClass="control-label">메뉴유형: </label>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
                        <label class="radio-inline">
							<input type="radio" id="menuOrdrTy1" name="menuOrdrTy" title="좌" value="1"  />시스템(좌)-기본
                        </label>
                        <label class="radio-inline">
							<input type="radio" id="menuOrdrTy2" name="menuOrdrTy" title="우" value="2" />시스템(우)
                        </label>
                    </div>
                </div>
                <div class="col-xs-4">
                    <button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" title="자주 사용하는 메뉴를 좌 또는 우측에 배치">
                        <i class="far fa-question-circle"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
    
	<div class="row bg-warning">
		<label class="col-xs-2 control-label">사용자</label>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">승인: <span class="text-danger">
						<i class="fas fa-exclamation-triangle"></i></span></label>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="userApproveYn1" name="userApproveYn" title="사용" value="Y" />사용
						</label>
						<label class="radio-inline">
							<input type="radio" id="userApproveYn2" name="userApproveYn" title="미사용" value="N" />미사용
						</label>
					</div>
				</div>
				<div class="col-xs-4">
					<button type="button" class="btn btn-default btn-sm" data-toggle="tooltip" data-html="true" title="사용자 승인 사용 : 사용자 등록 후, 승인자가 승인 후 사용 가능.<br>사용자 승인 미사용 : 사용자 등록 후, 승인자 승인 없이 사용 가능">
						<i class="far fa-question-circle"></i>
					</button>
				</div>
			</div>
		</div>
		
		<!-- For Validator -->
		<hidden id="" name="" value="leftDivHdnYn" />
		<%-- <hidden id="" name="" value="autoEndTime" title="자동종료시각" /> --%>
	
	</div>
	
	<%-- </form> --%>
</article>
<%--
<script src="<c:url value='/mntr/validator.do'/>"></script>
<validator:javascript formName="configureVO" staticJavascript="false" xhtml="true" cdata="false" />
 --%>
<script src="<c:url value='/js/mntr/mng/mngCmmDisplay.js'/>"></script>
