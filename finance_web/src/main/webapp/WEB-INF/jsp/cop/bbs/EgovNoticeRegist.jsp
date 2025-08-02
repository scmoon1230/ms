<%--
  Class Name : EgovNoticeRegist.jsp
  Description : 게시물  생성 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.24   이삼섭              최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.24
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
    function fn_egov_validateForm(obj) {
        return true;
    }
    
    function fn_egov_regist_notice()
    {
        if (confirm('<spring:message code="common.regist.msg" />')) {
            document.board.action = "<c:url value='/wrks/cop/bbs/insertBoardArticle.do'/>";
            document.board.submit();
        }
    }
    
    function fn_egov_select_noticeList() {
        document.board.action = "<c:url value='/wrks/cop/bbs/selectBoardList.do'/>"+ "?bbsId=" +"<c:out value='${bdMstr.bbsId}'/>";
        document.board.submit();
    }   
</script>
<style type="text/css">
.noStyle {background:ButtonFace; BORDER-TOP:0px; BORDER-bottom:0px; BORDER-left:0px; BORDER-right:0px;}
  .noStyle th{background:ButtonFace; padding-left:0px;padding-right:0px}
  .noStyle td{background:ButtonFace; padding-left:0px;padding-right:0px}
</style>
<title><c:out value='${bdMstr.bbsNm}'/> - 게시글쓰기</title>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>


</head>

<!-- body onload="javascript:editor_generate('nttCn');"-->
<!-- <body onLoad="HTMLArea.init(); HTMLArea.onload = initEditor; document.board.nttSj.focus();"> -->
<body>
<noscript class="noScriptTitle">자바스크립트를 지원하지 않는 브라우저에서는 일부 기능을 사용하실 수 없습니다.</noscript>
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
				<div class="titArea"><h3 class="tit">게시글 쓰기</h3></div>
				<!-- table add start -->
				<div class="tableTypeWide">
					<form:form commandName="board" name="board" method="post" enctype="multipart/form-data" >
					
						<!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->
						<input type="hidden" name="child" value="<c:out value='${child}'/>" />
						<input type="hidden" name="top" value="<c:out value='${top}'/>" />
						<input type="hidden" name="left" value="<c:out value='${left}'/>" />
						
						<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
						<input type="hidden" name="bbsId" value="<c:out value='${bdMstr.bbsId}'/>" />
						<input type="hidden" name="bbsAttrbCode" value="<c:out value='${bdMstr.bbsAttrbCode}'/>" />
						<input type="hidden" name="bbsTyCode" value="<c:out value='${bdMstr.bbsTyCode}'/>" />
						<input type="hidden" name="replyPosblAt" value="<c:out value='${bdMstr.replyPosblAt}'/>" />
						<input type="hidden" name="fileAtchPosblAt" value="<c:out value='${bdMstr.fileAtchPosblAt}'/>" />
						<input type="hidden" name="posblAtchFileNumber" value="<c:out value='${bdMstr.posblAtchFileNumber}'/>" />
						<input type="hidden" name="posblAtchFileSize" value="<c:out value='${bdMstr.posblAtchFileSize}'/>" />
						<input type="hidden" name="tmplatId" value="<c:out value='${bdMstr.tmplatId}'/>" />
						
						<input type="hidden" name="cal_url" value="<c:url value='/sym/cmm/EgovNormalCalPopup.do'/>" />
						<input type="hidden" name="authFlag" value="<c:out value='${bdMstr.authFlag}'/>" />
						
						<c:if test="${anonymous != 'true'}">
						<input type="hidden" name="ntcrNm" value="dummy">   <!-- validator 처리를 위해 지정 -->
						<input type="hidden" name="password" value="dummy"> <!-- validator 처리를 위해 지정 -->
						</c:if>
						
						<c:if test="${bdMstr.bbsAttrbCode != 'BBSA01'}">
						<input name="ntceBgnde" type="hidden" value="10000101">
						<input name="ntceEndde" type="hidden" value="99991231">
						</c:if>

						<table >
                            <tr>
                                <th width="20%" height="23"  nowrap="nowrap"><label for="nttSj"><spring:message code="cop.nttSj" /></label>
                                </th>
                                <td width="80%" nowrap colspan="3">
                                  <input id="nttSj" name="nttSj" type="text" size="60" value=""  maxlength="60" > 
                                  <br/><form:errors path="nttSj" />
                                </td>
                            </tr>
                            <tr> 
                                <th height="23" ><label for="nttCn"><spring:message code="cop.nttCn" /></label>
                                </th>
                                <td colspan="3">
                                  <textarea id="nttCn" name="nttCn" class="textarea" cols="75" rows="20" style="width:99%;"></textarea> 
                                  <form:errors path="nttCn" />
                                </td>
                            </tr>
                            <c:choose>
                                <c:when test="${bdMstr.bbsAttrbCode == 'BBSA01'}">
                                  <tr> 
                                    <th height="23" ><label for="ntceBgndeView"><spring:message code="cop.noticeTerm" /></label><label for="ntceEnddeView" class="invisible"><spring:message code="cop.noticeTerm" /></label>
                                    <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required" />
                                    </th>
                                    <td colspan="3">
                                      <input name="ntceBgnde" type="hidden" >
                                      <input name="ntceBgndeView" title="게시시작일" type="text" size="10" value=""  readonly="readonly"
                                        onclick="fn_egov_NormalCalendar(document.board, document.board.ntceBgnde, document.board.ntceBgndeView,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');" >
                                      <img src="<c:url value='/images/calendar.gif' />"
                                        onclick="fn_egov_NormalCalendar(document.board, document.board.ntceBgnde, document.board.ntceBgndeView,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');"
                                        width="15" height="15" alt="calendar">
                                      ~
                                      <input name="ntceEndde" type="hidden"  />
                                      <input name="ntceEnddeView" title="게시종료일" type="text" size="10" value=""  readonly="readonly"
                                        onclick="fn_egov_NormalCalendar(document.board, document.board.ntceEndde, document.board.ntceEnddeView,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');"  >
                                      <img src="<c:url value='/images/calendar.gif' />"
                                        onclick="fn_egov_NormalCalendar(document.board, document.board.ntceEndde, document.board.ntceEnddeView,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');"
                                        width="15" height="15" alt="calendar">
                                        <br/><form:errors path="ntceBgnde" />
                                        <br/><form:errors path="ntceEndde" />
                                        
                                    </td>
                                  </tr>     
                                  </c:when>
                                <c:otherwise>
                                
                                </c:otherwise>
                            </c:choose>
                        
                            <c:if test="${bdMstr.fileAtchPosblAt == 'Y'}">
                              <tr>
                                <th height="23"><label for="egovComFileUploader"><spring:message code="cop.atchFile" /></label></th>
                                <td colspan="3">
                                            <input name="file_1" id="egovComFileUploader" type="file" />
                                                <div id="egovComFileList"></div>
                                </td>
                              </tr>
                          </c:if>
                        </table>

                        <c:if test="${bdMstr.fileAtchPosblAt == 'Y'}"> 
                        <script type="text/javascript">
                            var maxFileNum = document.board.posblAtchFileNumber.value;
                            if(maxFileNum==null || maxFileNum==""){
                                maxFileNum = 3;
                            } 
                            var multi_selector = new MultiSelector( document.getElementById( 'egovComFileList' ), maxFileNum );
                            multi_selector.addElement( document.getElementById( 'egovComFileUploader' ) );         
                        </script>
                        </c:if>
                        </form:form>
				</div>
				</br></br> 
				<div class="btnWrap btnR">
					<c:if test="${bdMstr.authFlag == 'Y'}">
					<a href="#" onclick="fn_egov_regist_notice();" class="btn btnDt btnRgt">저장</a>
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
</body>
</html>

