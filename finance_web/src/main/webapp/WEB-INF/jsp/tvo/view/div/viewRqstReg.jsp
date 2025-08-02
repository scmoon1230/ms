<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-primary" id="view-rqst">
	<div class="panel-heading">
		<h3 class="panel-title">열람신청${empty viewRqstDtl ? '등록' : '수정'}</h3>
	</div>
	<div class="panel-body">
		<%--<table class="table table-striped table-xs">--%>
		<table class="table table-xs">
			<colgroup>
				<col style="width: 100px;" />
				<col style="width: calc(50% - 100px);" />
				<col style="width: 100px;" />
				<col style="width: calc(50% - 100px);" />
			</colgroup>
			<tbody>
				<tr><th class="bg-info"><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사건번호</th>
					<td><input type="text" class="form-control input-xs" id="view-rqst-evtNo" name="view-rqst-evtNo"
							placeholder="사건번호" title="사건번호" value="${viewRqstDtl.evtNo}" />
					</td>
					<th class="bg-info"><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사건명</th>
					<td><input type="text" class="form-control input-xs" id="view-rqst-evtNm" name="view-rqst-evtNm"
							placeholder="사건명" title="사건명" value="${viewRqstDtl.evtNm}" />
					</td>
				</tr>
				<tr><th class="bg-info"><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>발생일시</th>
					<td><div class="form-inline">
							<div class="form-group form-group-xs">
								<div class="input-group datetimepicker-ymdhms view-rqst-evtYmdhms">
									<input type="text" class="form-control input-xs" id="view-rqst-evtYmdhms" name="view-rqst-evtYmdhms"
										placeholder="발생일시" title="발생일시" value="${viewRqstDtl.evtYmdhms}" />
									<span class="input-group-addon input-group-addon-xs">
										<i class="far fa-calendar-alt"></i>
									</span>
								</div>
							</div>
						</div>
					</td>
					<th class="bg-info"><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>발생주소</th>
					<td style="width:500px;">
						<div class="form-inline">
							<input type="text" class="form-control input-xs" style="width:450px;" id="view-rqst-evtAddr" name="view-rqst-evtAddr"
								placeholder="발생주소" title="발생주소" readonly value="${viewRqstDtl.evtAddr}" />
							<button type="button" class="btn btn-default btn-xs" title="발생위치로 이동" onclick="oTvoCmn.map.moveTo(this);">
								<i class="fas fa-map-pin fa-sm"></i>
							</button>
						</div>
					</td>
				</tr>
				<tr><th class="bg-info"><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>신청사유</th>
					<td><label for="view-rqst-rqstRsnTyCd" class="control-label sr-only">신청사유</label>
						<select id="view-rqst-rqstRsnTyCd" name="view-rqst-rqstRsnTyCd" class="form-control input-xs" title="신청사유"></select>
					</td>
					<th class="bg-info">신청사유상세</th>
					<td><textarea class="form-control input-xs" id="view-rqst-rqstRsnDtl"
							title="신청사유상세" placeholder="신청사유상세" rows="2">${viewRqstDtl.rqstRsnDtl}</textarea>
					</td>
				</tr>
				<tr><th class="bg-info"><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>열람종료일자</th>
					<td><div class="form-inline">
							<div class="form-group form-group-xs">
								<div class="input-group datetimepicker-ymdhms view-rqst-viewEndYmdhmsWant">
									<input type="text" class="form-control input-xs" id="view-rqst-viewEndYmdhmsWant" name="view-rqst-viewEndYmdhmsWant"
										placeholder="열람종료일자" title="열람종료일자" value="${viewRqstDtl.viewEndYmdhmsWant}" />
									<span class="input-group-addon input-group-addon-xs">
										<i class="far fa-calendar-alt"></i>
									</span>
								</div>
							</div>
						</div>
					</td>
					<th class="bg-info"><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>긴급구분</th>
					<td>
						<label class="radio-inline" style="position: relative;">
							<input type="radio" name="view-rqst-emrgYn" value="Y" title="긴급"
								 onclick="oViewFirstRqst.view.rqst.request.changeEmrgYn();"> 긴급
						</label>
						<label class="radio-inline" style="position: relative;">
							<input type="radio" name="view-rqst-emrgYn" value="N" title="일반"
								 onclick="oViewFirstRqst.view.rqst.request.changeEmrgYn();"> 일반
						</label>
						<span id="spEmrgRsn" class="hide">
				  			<input type="text" class="form-control input-xs" style="margin-top: 5px;" id="view-rqst-emrgRsn" name="view-rqst-emrgRsn"
								 placeholder="긴급사유" title="긴급사유" value="${viewRqstDtl.emrgRsn}" />
						</span>
					</td>
				</tr>
				<tr><th class="bg-info">공문파일</th>
					<td><c:if test="${viewRqstDtl.paperFileNm != null && viewRqstDtl.paperFileNm != ''}">
							${viewRqstDtl.paperFileNm}
							<!--<button type="button" class="btn btn-default btn-xs" title="공문 다운로드" onclick="oTvoCmn.download.paperFile();">
								<i class="fas fa-download"></i>
							</button>-->
							<button type="button" class="btn btn-default btn-xs" title="공문 삭제" onclick="oViewFirstRqst.view.rqst.modify.removerPaperFile();">
								<i class="fas fa-times"></i>
							</button>
							<br/>(공문을 첨부하면 기존 파일은 삭제됩니다.)
						</c:if>
						<input type="file" class="form-control input-xs" style="height: 22px;padding: 0;font-size: 10px;"
							id="view-rqst-paperFileNm" title="공문첨부" />
					</td>
					<th class="bg-info">전자문서번호</th>
					<td><input type="text" class="form-control input-xs" id="view-rqst-paperNo" name="view-rqst-paperNo"
						 placeholder="전자문서번호" title="전자문서번호" value="${viewRqstDtl.paperNo}" />
					</td>
				</tr>
				<tr><th class="bg-info"><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>정보보호서약</th>
					<td colspan="3">
						<div class="form-inline" style="display: flex;">
							<input type="checkbox" id="view-rqst-agree" name="view-rqst-agree" style="vertical-align: sub;" />
							 <span style="padding:4px;">개인영상정보보호 내용을 숙지하였으며 이행을 서약합니다.</span>
								<button type="button" class="btn btn-default btn-xs" title="신청" onclick="oViewFirstRqst.view.rqst.request.agreeDisplay();">
									<i class="far fa-file"></i> <span id="view-rqst-agree-btntitle">서약보기</span>
								</button>
						</div>
						<div id="view-rqst-agree-content" style="display:none;">
							<center><u>개인영상정보보호 서약서</u></center><br/>
							나는 『영상 열람•반출 서비스』를 이용함에 있어 다음 사항을 준수할 것을 엄숙히 서약합니다.<br/>
							1. 나는 신청한 사건과 관련되지 않은 CCTV영상을 열람•반출할 경우 개인영상정보보호에 위반됨을 인정하고 제반 보안관계규정 및 지침을 성실히 수행한다.<br/>
							2. 나는 신청한 사건과 관련된 CCTV영상만으로 한정하여 열람•반출할 것이며, CCTV영상의 열람•반출 시 알게 되는 모든 정보는 오직 사건 해결에 관계된 것에만 사용할 것을 서약한다.<br/> 
							3. 나는 CCTV영상의 열람•반출 시 알게 되는 정보를 사건 해결에 관계되지 않는 것에 누설한 때에는 관계법규에 따라 엄중한 처벌을 받을 것을 서약한다.<br/><br/>
							서약자 성명(서명) : 로그인한 계정, 휴대폰번호 및 인증번호확인으로 갈음함
						</div>
					</td>
				</tr>
				<tr style="display:none;">
					<th><label for="view-rqst-viewRqstNo" class="control-label">열람신청번호,발생위치</label></th>
					<td><input type="text" id="view-rqst-viewRqstNo" name="view-rqst-viewRqstNo" value="${viewRqstDtl.viewRqstNo}" />
						<input type="text" class="form-control input-xs input-lonlat" id="view-rqst-evtPointX" name="view-rqst-evtPointX"
							maxlength="10" size="10" placeholder="경도" title="경도" value="${viewRqstDtl.evtPointX}" />
						<input type="text" class="form-control input-xs input-lonlat" id="view-rqst-evtPointY" name="view-rqst-evtPointY"
							maxlength="10" size="10" placeholder="위도" title="위도" value="${viewRqstDtl.evtPointY}" />
						<input type="text" id="view-rqst-evtDstrtCd" name="view-rqst-evtDstrtCd" value="${viewRqstDtl.evtDstrtCd}" />
					</td>
				</tr>
			</tbody>
		</table>
		<div class="alert alert-warning alert-xs" role="alert">
			<sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup> 표시 항목은 필수 입력 항목입니다.
		</div>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-12 text-center">
				<c:choose>
					<c:when test="${not empty viewRqstDtl}">
						<button type="button" class="btn btn-primary btn-xs" title="수정" onclick="oViewFirstRqst.view.rqst.modify.register();">수정</button>
						<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oViewFirstRqst.view.rqst.modify.cancel();">취소</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn btn-primary btn-xs" title="등록" onclick="oViewFirstRqst.view.rqst.request.register();">등록</button>
						<button type="button" class="btn btn-secondary btn-xs" title="취소" onclick="oViewFirstRqst.view.rqst.request.cancel();">취소</button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>