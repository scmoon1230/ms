<%--
  Class Name : EgovNoticeUpdt.jsp
  Description : 게시물 수정 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.19   이삼섭              최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.19
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<script type="text/javascript" src="<c:url value='/js/egov/EgovBBSMng.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egov/EgovMultiFile.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egov/EgovCalPopup.js'/>" ></script>
<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="board" staticJavascript="false" xhtml="true" cdata="false"/>
<c:if test="${anonymous == 'true'}"><c:set var="prefix" value="/anonymous"/></c:if>
<script type="text/javascript">
    function fn_egov_validateForm(obj){
        return true;
    }

	// 게시글 업데이트
	function fn_egov_regist_notice()
	{
		if (confirm('<spring:message code="common.update.msg" />'))
		{
			document.board.action = "<c:url value='/wrks/cop/bbs${prefix}/updateBoardArticle.do'/>";
			document.board.submit();
		}
	}


    function fn_egov_select_noticeList() {
        document.board.action = "<c:url value='/wrks/cop/bbs${prefix}/selectBoardList.do'/>";
        document.board.submit();    
    }
    
    function fn_egov_check_file(flag) {
        if (flag=="Y") {
            document.getElementById('file_upload_posbl').style.display = "block";
            document.getElementById('file_upload_imposbl').style.display = "none";          
        } else {
            document.getElementById('file_upload_posbl').style.display = "none";
            document.getElementById('file_upload_imposbl').style.display = "block";
        }
    }   
</script>
<title><c:out value='${bdMstr.bbsNm}'/> - 게시글 수정</title>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>

</head>

<body>
<noscript>자바스크립트를 지원하지 않는 브라우저에서는 일부 기능을 사용하실 수 없습니다.</noscript>    
<div id="wrapper">
	<!-- topbar -->
	<%@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
	<!-- //topbar -->
	<!-- container Start -->
	<div class="container">
		<!-- LeftMenu Start -->
		<%@include file="/WEB-INF/jsp/cmm/leftMenu.jsp"%>
		<!-- LeftMenu End -->
		<!-- contentWrap Start -->
		<div class="contentWrap">
			<div class="topArea">
				<a href="#" class="btnOpen"><img src="<c:url value='/'/>images/btn_on_off.png" alt="열기/닫기"></a>
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi.jsp"%>
			</div>
			<!-- content Start -->
			<div class="content">
				<div class="titArea"><h3 class="tit">게시글 수정</h3></div>
				<!-- table add start -->
				<div class="tableTypeWide">
					<form:form commandName="board" name="board" method="post" enctype="multipart/form-data" >
						<!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->
						<input type="hidden" name="child" value="<c:out value='${child}'/>" />
						<input type="hidden" name="top" value="<c:out value='${top}'/>" />
						<input type="hidden" name="left" value="<c:out value='${left}'/>" />
						
						<input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>"/>
						<input type="hidden" name="returnUrl" value="<c:url value='/wrks/cop/bbs/forUpdateBoardArticle.do'/>"/>
						
						<input type="hidden" name="bbsId" value="<c:out value='${result.bbsId}'/>" />
						<input type="hidden" name="nttId" value="<c:out value='${result.nttId}'/>" />
						
						<input type="hidden" name="bbsAttrbCode" value="<c:out value='${bdMstr.bbsAttrbCode}'/>" />
						<input type="hidden" name="bbsTyCode" value="<c:out value='${bdMstr.bbsTyCode}'/>" />
						<input type="hidden" name="replyPosblAt" value="<c:out value='${bdMstr.replyPosblAt}'/>" />
						<input type="hidden" name="fileAtchPosblAt" value="<c:out value='${bdMstr.fileAtchPosblAt}'/>" />
						<input type="hidden" name="posblAtchFileNumber" value="<c:out value='${bdMstr.posblAtchFileNumber}'/>" />
						<input type="hidden" name="posblAtchFileSize" value="<c:out value='${bdMstr.posblAtchFileSize}'/>" />
						<input type="hidden" name="tmplatId" value="<c:out value='${bdMstr.tmplatId}'/>" />
						
						<input type="hidden" name="cal_url" value="<c:url value='/sym/cmm/EgovNormalCalPopup.do'/>" />
						
						<c:if test="${anonymous != 'true'}">
						<input type="hidden" name="ntcrNm" value="dummy">   <!-- validator 처리를 위해 지정 -->
						<input type="hidden" name="password" value="dummy"> <!-- validator 처리를 위해 지정 -->
						</c:if>

						<c:if test="${bdMstr.bbsAttrbCode != 'BBSA01'}">
						<input name="ntceBgnde" type="hidden" value="10000101">
						<input name="ntceEndde" type="hidden" value="99991231">
						</c:if>

						<table>
						<tr> 
						<th width="20%" height="23" nowrap ><spring:message code="cop.nttSj" /></th>
							<td width="80%" nowrap colspan="3">
							<input name="nttSj" title="<spring:message code="cop.nttSj" />" type="text" size="60" value='<c:out value="${result.nttSj}" />'  maxlength="60" >
							<br/><form:errors path="nttSj" /> 
							</td>
						</tr>
						<tr> 
							<th height="23" ><spring:message code="cop.nttCn" /></th>
							<td colspan="3">
							<textarea id="nttCn" name="nttCn" title="<spring:message code="cop.nttCn" />" class="textarea" cols="75" rows="20"  style="width:99%;"><c:out value="${result.nttCn}" escapeXml="false" /></textarea> 
							<form:errors path="nttCn" />
						</td>
						</tr>
						<c:if test="${bdMstr.bbsAttrbCode == 'BBSA01'}"> 
						<tr> 
							<th height="23" ><spring:message code="cop.noticeTerm" /></th>
							<td colspan="3">
								<input name="ntceBgnde" type="hidden" value='<c:out value="${result.ntceBgnde}" />'>
								<input name="ntceBgndeView" type="text" size="10" title="ntceBgndeView" 
								  value="${fn:substring(result.ntceBgnde, 0, 4)}-${fn:substring(result.ntceBgnde, 4, 6)}-${fn:substring(result.ntceBgnde, 6, 8)}"  readOnly
								onclick="fn_egov_NormalCalendar(document.board, document.board.ntceBgnde, document.board.ntceBgndeView,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');" >
								<img src="<c:url value='/images/calendar.gif' />"
								onclick="fn_egov_NormalCalendar(document.board, document.board.ntceBgnde, document.board.ntceBgndeView,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');" width="15" height="15" alt="calendar">
								~
								<input name="ntceEndde" type="hidden"  value='<c:out value="${result.ntceEndde}" />'>
								<input name="ntceEnddeView" type="text" size="10" title="ntceEnddeView"
								  value="${fn:substring(result.ntceEndde, 0, 4)}-${fn:substring(result.ntceEndde, 4, 6)}-${fn:substring(result.ntceEndde, 6, 8)}"  readOnly
								onclick="fn_egov_NormalCalendar(document.board, document.board.ntceEndde, document.board.ntceEnddeView,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');"  >
								<img src="<c:url value='/images/calendar.gif' />"
								onclick="fn_egov_NormalCalendar(document.board, document.board.ntceEndde, document.board.ntceEnddeView,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');" width="15" height="15" alt="calendar">
								<br/><form:errors path="ntceBgndeView" />
								<br/><form:errors path="ntceEnddeView" />
							</td>
						</tr>
						</c:if>
						<!-- 첨부파일 목록조회 Start -->
						<c:if test="${not empty result.atchFileId}">
						<tr> 
							<th height="23"><spring:message code="cop.atchFileList" /></th>
							<td colspan="3">
								<c:import url="/wrks/cop/bbs/selectFileInfs.do" charEncoding="UTF-8">
									<c:param name="param_atchFileId" value="${result.atchFileId}" />
									<c:param name="updateFlag" value="Y" />
								</c:import>
							</td>
						</tr>
						</c:if>
						<!-- 첨부파일 목록조회 End -->
						<c:if test="${bdMstr.fileAtchPosblAt == 'Y'}"> 
						<tr> 
							<th height="23"><label for="egovComFileUploader" ><spring:message code="cop.atchFile" /></label></th>
							<td colspan="3">
								<input name="file_1" id="egovComFileUploader" type="file" />
							</td>
						</tr>
						</c:if>
						
						</table>
						</form:form>
				</div>
				</br></br> 
				<div class="btnWrap btnR">
					<c:if test="${bdMstr.authFlag == 'Y'}">
					<c:if test="${result.frstRegisterId == searchVO.frstRegisterId}"> 
					<a href="#" onclick="fn_egov_regist_notice();" class="btn btnDt btnRgt">저장</a>
					</c:if>
					</c:if>
					
					<a href="#" onclick="fn_egov_select_noticeList();" class="btn btnDt btnRgt">목록</a>
				</div>
			</div>
			<!-- content End -->
		</div>
		<!-- contentWrap End -->
	</div>
	<!-- container End -->
</div>

<!-- //전체 레이어 끝 -->
</body>
</html>

