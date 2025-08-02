<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title><prprts:value key="HD_TIT" /></title>
    <%@include file="/WEB-INF/jsp/cmm/script.jsp" %>
</head>
<style>	
	.layerRegister .tableType2 td {
	  height: 35px;
	  vertical-align: middle
	}
</style>
<body>
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp" %>
<div id="wrapper" class="wth100">
	<!-- container -->
	<div class="container">
		<!-- content -->
		<div class="contentWrap">
			<div class="content">
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
				<div class="tableTypeHalf seachT">
					<table>
						<caption>VMS정보</caption>
						<tbody>
							<tr>
								<th>VMS명</th>
								<td>
									<input type="text" class="txtType txtType100 searchEvt" id="vmsNm" name="vmsNm" title="VMS명"/>
								</td>
								<td>
									<a href="javascript:void(0);" class="btn btnRight btnS searchBtn">검색</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>

				<div class="tableType1" style="height: 500px;">
					<table id="grid"></table>
				</div>
				<div class="paginate"></div>
				<div class="btnWrap btnR">
					<a href="#" class="btn btnDt btnRgt">등록</a>
				</div>
			</div>
			<input type="hidden" id="dstrtCdBk" value="${list.get(0).dstrtCd}" />
			<input type="hidden" id="dstrtNmBk" value="${list.get(0).dstrtNm}" />

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit">
					<h4>VMS 상세</h4>
				</div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>VMS 상세</caption>
							<colgroup>
								<col style="width: 150px;"><col/>
								<col style="width: 150px;"><col/>
							</colgroup>
							<tbody>
								<tr>
									<th>지구명</th>
									<td id="dDstrtNm"></td>
									<td style="display: none;" id="dDstrtCd"></td>
									<th>VMS아이디</th>
									<td id="dVmsId"></td>
								</tr>
								<tr>
									<th>VMS명</th>
									<td id="dVmsNm"></td>
								</tr>
								<tr>
									<th>영상재생지원배속</th>
									<td id="dPlaybackSpeed"></td>
									<th>영상기본재생배속</th>
									<td id="dBasicPlaybackSpeed"></td>
								</tr>
								<tr>
									<th>과거영상 검출사용여부</th>
									<td id="dPlayBackUseYnLfp"></td>
									<th>녹화구분</th>
									<td id="dRecordingTy"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnMd">수정</a>
						<a href="#" class="btn btnDe">삭제</a>
						<a href="#" class="btn btnC">닫기</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

			<!-- 레이어팝업 추가 -->
			<div class="layer layerRegister" id="div_drag_2">
				<div class="tit">
					<h4>VMS정보
						<span id="modetitle">추가</span>
					</h4>
				</div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>VMS 등록</caption>
							<tbody>
								<tr>
									<th>지구명</th>
									<c:if test="${listSize == 1}"> <!-- BASE dstrt_cd_mng 값 1개 @@@여기@@@ -->
									<td>
										<input type="text" id="iDstrtNm" class="txtType" maxlength="50" user-title="지구명" required="required" readonly/>
									</td>
									</c:if>
									<c:if test="${listSize > 1}">  <!-- WIDE dstrt_cd_mng 값 2개 이상 -->
									<td>
										<select class="selectType1" id="iDstrtNm" user-title="지구명" required="required">
						                    <c:forEach items="${list}" var="val">
						                            <option value="${val.dstrtCd}"><c:out value="${val.dstrtNm}"></c:out></option>
						                    </c:forEach>
						                </select>
									</td>
									</c:if>
									<input type="hidden" id="iDstrtCd"/>
									<th>VMS아이디</th>
									<td>
										<input type="text" id="iVmsId" class="txtType" maxlength="100" required="required" user-title="VMS아이디" readonly/>
										<button class="btn btn-xs btnAGrp">찾기</button>
									</td>
								</tr>
								<tr>
									<th>VMS명</th>
									<td>
										<input type="text" id="iVmsNm" class="txtType" maxlength="100" required="required" user-title="VMS명" readonly/>
									</td>
								</tr>
								<tr>
									<th>영상재생지원배속</th>
									<td>
										<input type="text" id="iPlaybackSpeed" class="txtType" maxlength="50"
												required="required" user-title="영상재생지원배속" onkeyup="inputPlaybackSpeed(this)"/>
									</td>
									<th>영상기본재생배속</th>
									<td>
										<input type="text" id="iBasicPlaybackSpeed" class="txtType" maxlength="2"
												required="required" user-title="영상기본재생배속" onkeyup="inputPlaybackSpeed(this)"/>
									</td>
								</tr>
								<tr>
									<th>과거영상 검출사용여부</th>
									<td>
									<div class="radio">
										<label class="radio-inline">
							                <input type="radio" name="iPlayBackUseYnLfp" id="y"  value="Y"/>
							                Y
							            </label>
										<label>
							                <input type="radio" name="iPlayBackUseYnLfp" id="n" value="N"/>
							                N
							            </label>
						            </div>
									</td>
									<th>녹화구분</th>
									<td>
										<label class="radio-inline">
							                <input type="radio" name="iRecordingTy" id="all" value="ALL"/>
							                All
							            </label>
										<label class="radio-inline">
							                <input type="radio" name="iRecordingTy" id="part" value="PART"/>
							                PART
							            </label>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<input type="hidden" name="svTag" id="svTag"/>
						<a href="#" class="btn btnSv">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
					<div>
						<span class="pull-left" style="padding-top: 5px;">
                        	<br><span class="text-danger"><i class="fas fa-circle"></i></span>
                       		영상재생지원배속, 영상기본재생배속 : 새 창(대화면)에서 사용되는 지원배속 및 기본재생배속
							<br><span class="text-primary"><i class="fas fa-circle"></i></span>
							과거영상 검출사용여부 : 영상분석시스템과 연동시, N이면 제외처리
							<br><span class="text-sucess"><i class="fas fa-circle"></i></span>
							녹화구분 : ALL | PART, 영상분석시스템과 연동시 PART이면 영상구분(과거,실시간)선택에 따라 새로고침
						</span>
			      	</div>
				</div>
			</div>
			<!-- //레이어팝업 추가 -->

			<!-- 레이어팝업 VMS그룹 추가 -->
			<div class="layer layerVmsGrp" id="div_drag_3" style="z-index: 100; width: 700px; height: 780px;">
				<div class="tit">
					<h4>VMS 그룹 추가</h4>
				</div>
				<div class="layerCt">
					<!-- 그룹 리스트 -->
					<div class="tableType1" style="height: 600px;">
						<table id="grid_grp_list"></table>
					</div>
					<div class="btnCtr update">
						<a href="#" class="btn btnSvGrp">선택</a>
						<a href="#" class="btn btnCGrp">닫기</a>
					</div>
				</div>
			</div>
		<!-- //레이어팝업 VMS그룹 추가 -->
		</div>
	<!-- //content -->
	</div>
<!-- //container -->
</div>
<!--  footer -->
<!-- <footer>
	<%@include file="/WEB-INF/jsp/mntr/layout/footer.jsp" %>
</footer> -->
<!-- //footer -->
<script src="<c:url value='/js/mntr/mng/vmsInfo.js' />"></script>
</body>
</html>