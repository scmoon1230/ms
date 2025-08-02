<%--
  Class Name : EgovBoardUseInfInqire.jsp
  Description : 게시판  사용정보  조회화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.04.02   이삼섭              최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.04.02
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
<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="boardUseInf" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
    function fn_egov_updt_bbsUseInf(){
        if (!validateBoardUseInf(document.boardUseInf)){
            return;
        }
        
        document.boardUseInf.action = "<c:url value='/wrks/cop/com/updateBBSUseInf.do'/>";
        document.boardUseInf.submit();
    }
    function fn_egov_select_bbsUseInfs(){
        document.boardUseInf.action = "<c:url value='/wrks/cop/com/selectBBSUseInfs.do'/>";
        document.boardUseInf.submit();      
    }
    
</script>
<title>게시판 사용정보 상세조회 및 수정</title>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>

</head>
<body >
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
                <!-- 검색 필드 박스 시작 -->
                <div id="search_field">
                    <div id="search_field_loc"><h2><strong>게시판 사용정보 수정</strong></h2></div>
                </div>
                <form name="boardUseInf" method="post" action="<c:url value='/wrks/cop/com/updateBBSUseInf.do'/>">
					<div style="visibility:hidden;display:none;"><input name="iptSubmit" type="submit" value="전송" title="전송"></div>
					<input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>" />
					<input type="hidden" name="bbsId" value="<c:out value='${bdUseVO.bbsId}'/>" />
					<input type="hidden" name="trgetId" value="<c:out value='${bdUseVO.trgetId}'/>" />

                    <div class="modify_user" >
                        <table summary="게시판명, 커뮤니티/ 동호회명, 사용여부  입니다">
					      <tr> 
					        <th scope="col" width="20%" height="23" class="" nowrap >게시판명</th>
					        <td width="80%" nowrap colspan="3">
					        <c:out value="${bdUseVO.bbsNm}" />
					        </td>
					      </tr>
					      <tr> 
					        <th scope="col" width="20%" height="23" class="" nowrap >커뮤니티/ 동호회명</th>
					        <td width="80%" nowrap colspan="3">
					        <c:choose>
					            <c:when test="${not empty bdUseVO.cmmntyNm}">
					                <c:out value="${bdUseVO.cmmntyNm}" />
					            </c:when>
					            <c:when test="${not empty bdUseVO.clbNm}">
					                <c:out value="${bdUseVO.clbNm}" />
					            </c:when>
					            <c:otherwise>(시스템  활용)</c:otherwise>
					        </c:choose>
					        </td>
					      </tr>
					      <tr> 
					        <th scope="col" width="20%" height="23" class="required_text" nowrap >
					            <label for="useAt">
					                                       사용여부
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td width="80%" nowrap colspan="3">
					            <spring:message code="button.use" /> : <input type="radio" name="useAt" class="radio2" value="Y" <c:if test="${bdUseVO.useAt == 'Y'}"> checked="checked"</c:if>>&nbsp;
					            <spring:message code="button.notUsed" /> : <input type="radio" name="useAt" class="radio2" value="N" <c:if test="${bdUseVO.useAt == 'N'}"> checked="checked"</c:if>>
					            <br/><form:errors path="useAt" /> 
					        </td>   
					    
					      </tr>
					      <c:choose>
					      <c:when test="${not empty bdUseVO.provdUrl}">
					      <tr> 
					        <th width="20%" height="23" class="" nowrap >제공 URL</th>
					        <td width="80%" nowrap colspan="3">
					            <a href="<c:url value="${bdUseVO.provdUrl}" />" target="_new">
					                <c:url value="${bdUseVO.provdUrl}" />
					            </a>
					        </td>
					      </tr>
					      </c:when>
					      </c:choose>
                        </table>
                    </div>

                    <!-- 버튼 시작(상세지정 style로 div에 지정) -->
                    <div class="buttons" style="padding-top:10px;padding-bottom:10px;">
                      <!-- 목록/저장버튼 -->
                      <table border="0" cellspacing="0" cellpadding="0" align="center">
                        <tr>
                            <td> 
						      <a href="<c:url value='/wrks/cop/com/updateBBSUseInf2.do'/>" onclick="fn_egov_updt_bbsUseInf(); return false;">저장</a> 
						    </td>
						    <td width="10"></td>
						    <td><span class="button">
						      <a href="<c:url value='/wrks/cop/com/selectBBSUseInfs.do'/>" onclick="fn_egov_select_bbsUseInfs(); return false;">목록</a> 
                            </span></td>
                        </tr>
                      </table>
                    </div>
                    <!-- 버튼 끝 -->                           
                </form>

			<!-- content End -->
		</div>
		<!-- contentWrap End -->
	</div>
	<!-- container End -->
</div>
<!-- //전체 레이어 끝 -->
</body>
</html>

