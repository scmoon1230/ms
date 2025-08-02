<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">

</script>

</head>
<body>
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
<div id="wrapper" class="wth100">
	<!-- container -->
	<div class="container">
		<!-- content -->
		<div class="contentWrap">
			<div class="content">
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
				<div class="tableTypeFree seachT">
					<table>
						<caption>시스템코드</caption>
						<tbody>
						<tr><th>프로퍼티 TY</th>
	                        <td><select name="prprtsTy" id="prprtsTy" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
	                        		<option value="">전체</option>
								    <c:forEach items="${prprtsTyList}" var="val">
								        <option value="${val.prprtsTy}"><c:out value="${val.prprtsTy}" ></c:out></option>
								    </c:forEach>
								</select>
	                        </td>
	                        <th>프로퍼티 ID</th>
	                        <td><select name="prprtsId" id="prprtsId" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
	                        		<option value="">전체</option>
								    <c:forEach items="${prprtsIdList}" var="val">
								        <option value="${val.prprtsId}"><c:out value="${val.prprtsId}" ></c:out></option>
								    </c:forEach>
								</select>
	                        </td>
	                        <th>프로퍼티 Key</th>
							<td><input type="text" name="" id="prprtsKey" class="txtType searchEvt" style="ime-mode:active">
							</td>
	                        <th>프로퍼티 Value</th>
							<td><input type="text" name="" id="prprtsVal" class="txtType searchEvt" style="ime-mode:active">
							</td>
	                        <th>설명</th>
							<td><input type="text" name="" id="dscrt" class="txtType searchEvt" style="ime-mode:active">
							</td>
                            <th>사용유형</th>
                            <td><select name="" id="useTyCd" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
	                        		<option value="">전체</option>
						    		<c:forEach items="${useGrpList}" var="val">
						        		<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
						    		</c:forEach>
								</select>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
                            </td>
						</tr>
						</tbody>
					</table>
				</div>

					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

				<div class="tableType1" style="height: 500px;">
					<table id="grid" style="width:100%">
					</table>
				</div>
				<div class="paginate">
				</div>
				<div class="btnWrap btnR">
					<a href="#" class="btn btnDt btnRgt">등록</a>
					<%--<a href="#" class="btn btnMultiDe">삭제</a>--%>
				</div>
			</div>

			<!-- 레이어팝업 상세 -->
			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>시스템코드 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>시스템코드 상세</caption>
							<tbody>
							<tr><th>프로퍼티 Ty</th>		<td id="dPrprtsTy">					</td>
								<th>프로퍼티 Id</th>		<td id="dPrprtsId">					</td>
							</tr>
							<tr><th>프로퍼티 Key</th>		<td id="dPrprtsKey" colspan="3">	</td>	</tr>
							<tr><th>프로퍼티 Value</th>		<td id="dPrprtsVal" colspan="3">	</td>	</tr>
							<tr><th>기본 Value</th>		<td id="dDefaultVal" colspan="3">	</td>	</tr>
							<tr><th>설명</th>				<td id="dDscrt" colspan="3">		</td>	</tr>
							<tr><th>정렬순서</th>			<td id="dSortOrdr">					</td>
								<th>사용유형</th>			<td id="dUseTyCd">					</td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="btnCtr">
						<a href="#" class="btn btnMd">수정</a>
						<!-- <a href="#" class="btn btnDe">삭제</a> -->
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 상세 -->

			<!-- 레이어팝업 등록 -->
			<div class="layer layerRegister" id="div_drag_2">
				<div class="tit"><h4>시스템코드 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>시스템코드 등록</caption>
							<tbody>
							<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>프로퍼티 Ty</th>
								<td><!-- <input type="text" id="iPrprtsTy" class="txtType" maxlength="20" required="required" user-required="insert" /> -->
									<select name="iPrprtsTy" id="iPrprtsTy" class="selectType1" maxlength="1">
	                        		<option value="">전체</option>
								    <c:forEach items="${prprtsTyList}" var="val">
								        <option value="${val.prprtsTy}"><c:out value="${val.prprtsTy}" ></c:out></option>
								    </c:forEach>
								</select>
								</td>
								<th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>프로퍼티 Id</th>
								<td><!-- <input type="text" id="iPrprtsId" class="txtType" maxlength="20" required="required" user-required="insert" /> -->
									<select name="iPrprtsId" id="iPrprtsId" class="selectType1" maxlength="1">
	                        		<option value="">전체</option>
								    <c:forEach items="${prprtsIdList}" var="val">
								        <option value="${val.prprtsId}"><c:out value="${val.prprtsId}" ></c:out></option>
								    </c:forEach>
								</select>
								</td>
							</tr>
							<tr><th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>프로퍼티 Key</th>
								<td colspan="3"><input type="text" id="iPrprtsKey" class="txtType" maxlength="50" required="required" user-required="insert" style="width:100%;"/></td>
							</tr>
							<tr><th>프로퍼티 Value</th>
								<td colspan="3"><input type="text" id="iPrprtsVal" class="txtType" maxlength="100" required="required" style="ime-mode:active;width:100%;"/></td>
							</tr>
							<tr><th>기본 Value</th>
								<td colspan="3"><input type="text" id="iDefaultVal" class="txtType" maxlength="100" required="required" style="ime-mode:active;width:100%;"/></td>
							</tr>
							<tr><th>설명</th>
								<td colspan="3"><textarea class="textArea" id="iDscrt" maxlength="1000" style="ime-mode:active;height:100px;"></textarea></td>
							</tr>
							<tr><th>정렬순서</th>
								<td><input type="text" id="iSortOrdr" class="txtType" maxlength="10" required="required" style="ime-mode:active"/></td>
								<th>사용유형</th>
								<td><select name="" id="iUseTyCd" class="selectType1" maxlength="1">
									    <c:forEach items="${useGrpList}" var="val">
									        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
									    </c:forEach>
									</select>
                                </td>
                            </tr>
							</tbody>
						</table>
					</div>
				   <div class="btnCtr">
						<a href="#" class="btn btnSvEc">저장</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>
			<!-- //레이어팝업 등록 -->
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->
</div>
<!-- footer -->
<!-- <div id="footwrap">
	<div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->
<script src="<c:url value='/js/mntr/mng/mngPrprts.js'/>"></script>
</body>
</html>