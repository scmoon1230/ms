<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<article id="article-grid">
	<ol class="breadcrumb">
		<li>기초정보</li>
		<li>시설물</li>
		<li class="active">${common.title}</li>
	</ol>
	<div class="tit">
		<h4>${common.title}</h4>
	</div>
	<form:form commandName="fcltVO" method="post">
		<div class="searchBox sm">
			<div class="form-inline">
				<div class="form-group">
					<button type="button" class="btn btn-default btn-sm" onclick="oFcltReg.save();">저장</button>
				</div>
				<div class="form-group">
					<button type="button" class="btn btn-default btn-sm" onclick="oFcltReg.list();">취소</button>
				</div>
			</div>
		</div>
		<table class="table table-bordered table-striped">
			<tr>
				<th width="15%">시설물ID</th>
				<td width="35%">
					<div class="col-xs-12">
						<form:input path="fcltId" maxlength="30" cssClass="form-control input-sm" readonly="true" title="시설물ID는 자동생성되며 변경이 불가능 합니다." />
					</div>
				</td>
				<th>센터코드</th>
				<td id="dCtrCd">
					<div class="col-xs-12">
						<form:select path="ctrCd" maxlength="30" cssClass="form-control input-sm" data-selected="${fcltVO.ctrCd}"></form:select>
					</div>
				</td>
			</tr>
			<tr>
				<th>지구</th>
				<td id="dDstrtCd">
					<div class="col-xs-12">
						<form:select path="dstrtCd" maxlength="30" cssClass="form-control input-sm" data-selected="${fcltVO.dstrtCd}"></form:select>
					</div>
				</td>
				<th><font color="red">*</font>시군구</th>
				<td>
					<div class="col-xs-12">
						<form:select path="sigunguCd" maxlength="30" cssClass="form-control input-sm" data-selected="${fcltVO.sigunguCd}"></form:select>
					</div>
				</td>
			</tr>
			<%--
			<tr>
				<th>구역</th>
				<td>
					<div class="col-xs-12">
						<form:select path="areaCd" maxlength="30" cssClass="form-control input-sm" data-selected="${fcltVO.areaCd}"></form:select>
					</div>
				</td>
				<th>경찰지구대</th>
				<td>
					<div class="col-xs-12">
						<form:select path="plcPtrDivCd" maxlength="30" cssClass="form-control input-sm" data-selected="${fcltVO.plcPtrDivCd}"></form:select>
					</div>
				</td>
			</tr>
			 --%>
			<tr>
				<th><font color="red">*</font>시스템명</th>
				<td>
					<div class="col-xs-12">
						<form:select path="sysCd" maxlength="30" cssClass="form-control input-sm" data-selected="${fcltVO.sysCd}"></form:select>
					</div>
				</td>
				<th>관리번호</th>
				<td>
					<div class="col-xs-12">
						<form:input path="mngSn" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th><font color="red">*</font>시설물명</th>
				<td colspan="3">
					<div class="col-xs-12">
						<form:input path="fcltLblNm" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th><font color="red">*</font>유형</th>
				<td colspan="3">
					<div class="col-xs-6">
						<form:select path="fcltKndCd" cssClass="form-control input-sm" data-selected="${fcltVO.fcltKndCd}"></form:select>
					</div>
					<div class="col-xs-6">
						<form:select path="fcltUsedTyCd" cssClass="form-control input-sm" data-selected="${fcltVO.fcltUsedTyCd}"></form:select>
					</div>
				</td>
			</tr>
			<tr>
				<th title="직접입력하거나 통합검색을 통해서 지도를 클릭합니다.">설치장소</th>
				<td colspan="3">
					<div class="form-group">
						<label for="roadAdresNm" class="control-label" id="lblRoadAdresNm">
							<span class="label label-default">도로</span>
							${fcltVO.roadAdresNm}
						</label>
						<form:input path="roadAdresNm" maxlength="150" cssClass="form-control input-sm" />
					</div>
					<div class="form-group">
						<label for="lotnoAdresNm" class="control-label" id="lblLotnoAdresNm">
							<span class="label label-default">지번</span>
							${fcltVO.lotnoAdresNm}
						</label>
						<form:input path="lotnoAdresNm" maxlength="150" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th title="직접입력하거나 통합검색을 통해서 지도를 클릭합니다."><font color="red">*</font>좌표X</th>
				<td>
					<div class="col-xs-12">
						<label for="pointX" class="control-label" id="lblPointX">경도 : ${fcltVO.pointX}</label>
						<input type="number" id="pointX" name="pointX" maxlength="10" class="form-control input-sm" value="${fcltVO.pointX}" />
					</div>
				</td>
				<th title="직접입력하거나 통합검색을 통해서 지도를 클릭합니다."><font color="red">*</font>좌표Y</th>
				<td>
					<div class="col-xs-12">
						<label for="pointY" class="control-label" id="lblPointY">위도 : ${fcltVO.pointY}</label>
						<input type="number" id="pointY" name="pointY" maxlength="10" class="form-control input-sm" value="${fcltVO.pointY}" />
					</div>
				</td>
			</tr>
			<tr>
				<th>고유식별번호</th>
				<td>
					<div class="col-xs-12">
						<form:input path="fcltUid" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
				<th>중계VMS UID</th>
				<td>
					<div class="col-xs-12">
						<form:input path="linkVmsUid" maxlength="40" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>프리셋대역시작번호</th>
				<td>
					<div class="col-xs-12">
						<form:select path="presetBdwStartNum" cssClass="form-control input-sm">
							<form:option value="10">10</form:option>
							<form:option value="20">20</form:option>
							<form:option value="30">30</form:option>
							<form:option value="40">40</form:option>
							<form:option value="50">50</form:option>
							<form:option value="60">60</form:option>
							<form:option value="70">70</form:option>
							<form:option value="80">80</form:option>
							<form:option value="90">90</form:option>
						</form:select>
					</div>
				</td>
				<th>CCTV채널</th>
				<td>
					<div class="col-xs-12">
						<form:input path="cctvChannel" maxlength="5" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>완제품구분</th>
				<td>
					<div class="col-xs-12">
						<form:select path="cpltPrdtTy" cssClass="form-control input-sm">
							<form:option value="">완제품구분</form:option>
							<form:option value="P">부품</form:option>
							<form:option value="S">완제품</form:option>
						</form:select>
					</div>
				</td>
				<th></th>
				<td></td>
			</tr>
			<tr>
				<th><font color="red">*</font>사용유형</th>
				<td>
					<div class="col-xs-12">
						<form:select path="useTyCd" cssClass="form-control input-sm" data-selected="${fcltVO.useTyCd}"></form:select>
					</div>
				</td>
				<th>기능별유형</th>
				<td colspan="3">
					<div class="col-xs-12">
						<form:select path="fcltKndDtlCd" maxlength="30" cssClass="form-control input-sm" data-selected="${fcltVO.fcltKndDtlCd}"></form:select>
					</div>
				</td>
			</tr>
			<tr>
				<th>대표상태</th>
				<td>
					<div class="col-xs-12">
						<form:select path="fcltSttus" maxlength="30" cssClass="form-control input-sm" data-selected="${fcltVO.fcltSttus}"></form:select>
					</div>
				</td>
				<th>대표시설물ID</th>
				<td id="dPrntFcltId">
					<div class="col-xs-12">
						<form:input path="prntFcltId" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>센터번호</th>
				<td>
					<div class="col-xs-12">
						<form:input path="vtCenterTelNo" maxlength="26" cssClass="form-control input-sm" />
					</div>
				</td>
				<th>비상벨번호</th>
				<td id="dPrntFcltId">
					<div class="col-xs-12">
						<form:input path="vtPointTelNo" maxlength="26" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>물품구분</th>
				<td>
					<div class="col-xs-12">
						<form:select path="fcltGdsdtTy" cssClass="form-control input-sm">
							<form:option value="">시설물물품기능구분</form:option>
							<form:option value="0">주시설물</form:option>
							<form:option value="1">보조시설물</form:option>
							<form:option value="2">장치확인용</form:option>
						</form:select>
					</div>
				</td>

				<th>관리자연락처</th>
				<td colspan="3">
					<div class="col-xs-12">
						<form:input path="fcltMngrTelNo" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>접속IP</th>
				<td id="dConnIp">
					<div class="col-xs-12">
						<form:input path="connIp" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
				<th>접속포트</th>
				<td id="dConnPort">
					<div class="col-xs-12">
						<form:input path="connPort" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>접속ID</th>
				<td id="dConnId">
					<div class="col-xs-12">
						<form:input path="connId" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
				<th>접속비밀번호</th>
				<td id="dConnPw">
					<div class="col-xs-12">
						<form:input path="connPw" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>시설물MAC주소</th>
				<td id="dFcltMacAdres">
					<div class="col-xs-12">
						<form:input path="fcltMacAdres" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
				<th>G/W</th>
				<td id="dGateWay">
					<div class="col-xs-12">
						<form:input path="gateWay" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>SUBNET</th>
				<td id="dSubnet">
					<div class="col-xs-12">
						<form:input path="subnet" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
				<th>설치일</th>
				<td id="dFcltInstlYmd">
					<div class="col-xs-12">
						<form:input path="fcltInstlYmd" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
			</tr>
			<tr>
				<th>서버IP</th>
				<td id="dSvrConnIp">
					<div class="col-xs-12">
						<form:input path="svrConnIp" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
				<th>서버포트</th>
				<td id="dSvrConnPort">
					<div class="col-xs-12">
						<form:input path="svrConnPort" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
			</tr>
			<tr>
				<th>서버접속ID</th>
				<td id="dSvrConnId">
					<div class="col-xs-12">
						<form:input path="svrConnId" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
				<th>서버접속비번</th>
				<td id="dSvrConnPw">
					<div class="col-xs-12">
						<form:input path="svrConnPw" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
			</tr>
			<tr>
				<th>장치확인IP</th>
				<td id="dDvcSeeCctvIp">
					<div class="col-xs-12">
						<form:input path="dvcSeeCctvIp" maxlength="255" cssClass="form-control input-sm" />
					</div>

				</td>
				<th>장치확인포트</th>
				<td id="dDvcSeeCctvPort">
					<div class="col-xs-12">
						<form:input path="dvcSeeCctvPort" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
			</tr>
			<tr>
				<th>장치확인ID</th>
				<td id="dDvcSeeCctvId">
					<div class="col-xs-12">
						<form:input path="dvcSeeCctvId" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
				<th>장치확인비번</th>
				<td id="dDvcSeeCctvPw">
					<div class="col-xs-12">
						<form:input path="dvcSeeCctvPw" maxlength="30" cssClass="form-control input-sm" />
					</div>

				</td>
			</tr>
			<tr>
				<th>교통링크ID</th>
				<td id="dTraLinkId">
					<div class="col-xs-12">
						<form:input path="traLinkId" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
				<th>이전시설물ID</th>
				<td>
					<div class="col-xs-12">
						<form:input path="beforeFcltId" maxlength="30" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
			<tr>
				<th>번호인식<br />CCTV여부
				</th>
				<td id="dLprCctvYn">
					<div class="col-xs-12">
						<form:select path="lprCctvYn" cssClass="form-control input-sm">
							<form:option value="N">N</form:option>
							<form:option value="Y">Y</form:option>
						</form:select>
					</div>
				</td>
				<th>아이콘GIS<br />표출여부
				</th>
				<td id="dIconGisDspYn">
					<div class="col-xs-12">
						<form:select path="iconGisDspYn" cssClass="form-control input-sm">
							<form:option value="Y">Y</form:option>
							<form:option value="N">N</form:option>
						</form:select>
					</div>
				</td>
			</tr>
			<tr>
				<th>기타</th>
				<td id="dEtc" colspan="3">
					<div class="col-xs-12">
						<form:input path="etc" maxlength="300" cssClass="form-control input-sm" />
					</div>
				</td>
			</tr>
		</table>
	</form:form>
</article>
<script src="<c:url value='/mntr/validator.do'/>"></script>
<validator:javascript formName="fcltVO" staticJavascript="false" xhtml="true" cdata="false" />
<script src="<c:url value='/js/mntr/fclt/fcltReg.js' />"></script>