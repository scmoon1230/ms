<%--
  Class Name : EgovNoticeInqire.jsp
  Description : 게시물 조회 화면
  Modification Information
 
      수정일      수정자              수정내용
     ----------  --------    ---------------------------
     2009.03.23   이삼섭        최초 생성
     2009.06.26   한성곤        2단계 기능 추가 (댓글관리, 만족도조사)
     2011.08.31   JJY       	경량환경 버전 생성
     2013.05.23   이기하       	상세보기 오류수정
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.23
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
<c:if test="${anonymous == 'true'}"><c:set var="prefix" value="/anonymous"/></c:if>
<script type="text/javascript">
    function onloading() {
        if ("<c:out value='${msg}'/>" != "") {
            alert("<c:out value='${msg}'/>");
        }
    }
    
    function fn_egov_select_noticeList(pageNo) {
        document.frm.pageIndex.value = pageNo; 
        document.frm.action = "<c:url value='/wrks/cop/bbs/selectBoardList.do'/>";
        document.frm.submit();  
    }
    
    function fn_egov_delete_notice() {
        if ("<c:out value='${anonymous}'/>" == "true" && document.frm.password.value == '') {
            alert('등록시 사용한 패스워드를 입력해 주세요.');
            document.frm.password.focus();
            return;
        }
        
        if (confirm('<spring:message code="common.delete.msg" />')) {
            document.frm.action = "<c:url value='/wrks/cop/bbs/deleteBoardArticle.do'/>";
            document.frm.submit();
        }   
    }
    
    function fn_egov_moveUpdt_notice() {
        if ("<c:out value='${anonymous}'/>" == "true" && document.frm.password.value == '') {
            alert('등록시 사용한 패스워드를 입력해 주세요.');
            document.frm.password.focus();
            return;
        }

        document.frm.action = "<c:url value='/wrks/cop/bbs/forUpdateBoardArticle.do'/>";
        document.frm.submit();          
    }
    
    function fn_egov_addReply() {
        document.frm.action = "<c:url value='/wrks/cop/bbs/addReplyBoardArticle.do'/>";
        document.frm.submit();          
    }   
</script>
<!-- 2009.06.29 : 2단계 기능 추가 -->
<c:if test="${useComment == 'true'}">
<c:import url="/wrks/cop/bbs/selectCommentList.do" charEncoding="UTF-8">
    <c:param name="type" value="head" />
</c:import>
</c:if>
<c:if test="${useSatisfaction == 'true'}">
<c:import url="/wrks/cop/bbs/selectSatisfactionList.do" charEncoding="UTF-8">
    <c:param name="type" value="head" />
</c:import>
</c:if>
<c:if test="${useScrap == 'true'}">
<script type="text/javascript">
    function fn_egov_addScrap() {
        document.frm.action = "<c:url value='/wrks/cop/bbs/addScrap.do'/>";
        document.frm.submit();          
    }
</script>
</c:if>
<!-- 2009.06.29 : 2단계 기능 추가 -->
<title><c:out value='${result.bbsNm}'/> - 글조회</title>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>

</head>
<body onload="onloading();">
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
				<div class="titArea"><h3 class="tit">글조회</h3></div>
				<!-- table add start -->
				<div class="tableTypeWide">
	                <form name="frm" method="post" action="<c:url value='/wrks/cop/bbs/selectBoardList.do'/>">
	                    <input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>">
	                    <input type="hidden" name="bbsId" value="<c:out value='${result.bbsId}'/>" >
	                    <input type="hidden" name="nttId" value="<c:out value='${result.nttId}'/>" >
	                    <input type="hidden" name="parnts" value="<c:out value='${result.parnts}'/>" >
	                    <input type="hidden" name="sortOrdr" value="<c:out value='${result.sortOrdr}'/>" >
	                    <input type="hidden" name="replyLc" value="<c:out value='${result.replyLc}'/>" >
	                    <input type="hidden" name="nttSj" value="<c:out value='${result.nttSj}'/>" >
	                    <input type="submit" id="invisible" class="invisible"/>
						<!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->
						<input type="hidden" name="child" value="<c:out value='${child}'/>" />
						<input type="hidden" name="top" value="<c:out value='${top}'/>" />
						<input type="hidden" name="left" value="<c:out value='${left}'/>" />

                    <table>
                      <tr> 
                        <th width="15%" height="23" nowrap >제목</th>
                        <td width="85%" colspan="5" nowrap="nowrap"><c:out value="${result.nttSj}" />
                        </td>
                      </tr>
                      <tr> 
                        <th width="15%" height="23" nowrap >작성자</th>
                        <td width="15%" nowrap="nowrap">
                        <c:choose>
                            <%-- <c:out value="${result.frstRegisterNm}" /> --%>
                            <c:when test="${anonymous == 'true'}">
                                ******
                            </c:when>
                            <c:when test="${result.ntcrNm == ''}">
                                <c:out value="${result.frstRegisterNm}" />
                            </c:when>
                            <c:otherwise>
                                <c:out value="${result.ntcrNm}" />
                            </c:otherwise>
                        </c:choose>
                
                        </td>
                        <th width="15%" height="23" nowrap >작성시간</th>
                        <td width="15%" nowrap="nowrap"><c:out value="${result.frstRegisterPnttm}" />
                        </td>
                        <th width="15%" height="23" nowrap >조회수</th>
                        <td width="15%" nowrap="nowrap"><c:out value="${result.inqireCo}" />
                        </td>
                      </tr>    
                      <tr> 
                        <th height="23" >글내용</th>
                        <td colspan="5">
                         <div id="bbs_cn">
                           <textarea id="nttCn" name="nttCn"  cols="75" rows="20"  style="width:99%" readonly="readonly" title="글내용"><c:out value="${result.nttCn}" escapeXml="true" /></textarea>
                         </div>
                        </td>
                      </tr>
                      <c:if test="${not empty result.atchFileId}">
                          <c:if test="${result.bbsAttrbCode == 'BBSA02'}">
                          <tr> 
                            <th height="23" >첨부이미지</th>
                            <td colspan="5">
                                    <c:import url="/egov/com/cmm/fms/selectImageFileInfs.do" charEncoding="UTF-8">
                                        <c:param name="atchFileId" value="${result.atchFileId}" />
                                    </c:import>
                            </td>
                          </tr>
                          </c:if>
                          <tr> 
                            <th height="23">첨부파일 목록</th>
                            <td colspan="5">
                                <c:import url="/wrks/cop/bbs/selectFileInfs.do" charEncoding="UTF-8">
                                    <c:param name="param_atchFileId" value="${result.atchFileId}" />
                                    <c:param name="updateFlag" value="N" />
                                </c:import>
                            </td>
                          </tr>
                      </c:if>
                      <c:if test="${anonymous == 'true'}">
                      <tr> 
                        <th height="23"><label for="password"><spring:message code="cop.password" /></label></th>
                        <td colspan="5">
                            <input name="password" title="암호" type="password" size="20" value="" maxlength="20" >
                        </td>
                      </tr>
                      </c:if>   
                    </table>
                    </form>
				</div>
				</br></br> 
				<div class="btnWrap btnR">
					<c:if test="${result.frstRegisterId == sessionUniqId}">
					<a href="#" onclick="fn_egov_moveUpdt_notice();" class="btn btnDt btnRgt">수정</a>
					<a href="#" onclick="fn_egov_delete_notice();" class="btn btnDt btnRgt">삭제</a>
					</c:if>

					<c:if test="${result.replyPosblAt == 'Y'}">
					<a href="#" onclick="fn_egov_addReply();" class="btn btnDt btnRgt">답글작성</a>
					</c:if>
					<a href="#" onclick="fn_egov_select_noticeList('1');" class="btn btnDt btnRgt">목록</a>
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

