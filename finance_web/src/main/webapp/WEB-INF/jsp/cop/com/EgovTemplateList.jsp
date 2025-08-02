<%--
  Class Name : EgovTemplateList.jsp
  Description : 템플릿 목록화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.18   이삼섭              최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.18
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

<script type="text/javascript">
	$(document).ready(function(){
	
	});
	
	function press(event) {
        if (event.keyCode==13) {
            fn_egov_select_tmplatInfo('1');
        }
    }

    function fn_egov_insert_addTmplatInfo(){
		document.frm.action = "<c:url value='/wrks/cop/com/addTemplateInf.do'/>";
		document.frm.submit();
    }
    
    function fn_egov_select_tmplatInfo(pageNo){
        document.frm.pageIndex.value = pageNo; 
        document.frm.action = "<c:url value='/wrks/cop/com/selectTemplateInfs.do'/>";
        document.frm.submit();  
    }
    
    function fn_egov_inqire_tmplatInfor(tmplatId){
        document.frm.tmplatId.value = tmplatId;
        document.frm.action = "<c:url value='/wrks/cop/com/selectTemplateInf.do'/>";
        //document.frm.submit();
    }
</script>
<title>템플릿 목록</title>
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
				<!-- 검색 필드 박스 시작 -->
				<div class="titArea"><h3 class="tit">템플릿 목록</h3></div>
				<div class="tableTypeHalf seachT">
                    <form name="frm" action ="<c:url value='/wrks/cop/com/selectTemplateInfs.do'/>" method="post">
                        <input type="hidden" name="tmplatId" value="" />
                        <input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
                        <input type="submit" id="invisible" class="invisible"/>
                        <!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->

                        <input type="hidden" name="child" value="<c:out value='${child}'/>" />
                        <input type="hidden" name="top" value="<c:out value='${top}'/>" />
                        <input type="hidden" name="left" value="<c:out value='${left}'/>" />
                    
					<table>
					<caption>공통코드그룹</caption>
					<tbody>
					<tr>
						<th>
                        <select name="searchCnd" title="검색조건" class="select">
                           <option value="0" <c:if test="${searchVO.searchCnd == '0'}">selected="selected"</c:if> >템플릿명</option>
                           <option value="1" <c:if test="${searchVO.searchCnd == '1'}">selected="selected"</c:if> >템플릿구분</option>   
                        </select>
						</th>
						<td>
							<input name="searchWrd" type="text" size="35" class="txtType txtType100 searchEvt"  value='<c:out value="${searchVO.searchWrd}"/>' maxlength="35" onkeypress="press(event);" title="검색어 입력"> 
						</td>
						<td>
							<a href="#noscript" onclick="fn_egov_select_tmplatInfo('1'); return false;" class="btn btnRight btnS searchBtn">검색 </a>
						</td>
					</tr>
					</tbody>
					</table>
					</form>
					</br></br> 
				</div>
				<!-- //검색 필드 박스 끝 -->
				<div id="page_info"><div id="page_info_align"></div></div>
				<!-- table add start -->
				<div class="tableType1">
                    <table summary="번호,게시판명,사용 커뮤니티 명,사용 동호회 명,등록일시,사용여부   목록입니다" cellpadding="0" cellspacing="0">
                    <caption>게시판 템플릿 목록</caption>
                    <colgroup>
                    <col width="10%" >
                    <col width="15%" >  
                    <col width="10%" >
                    <col width="32%" >
                    <col width="10%" >
                    <col width="10%" >
                    </colgroup>
                    <thead>
                    <tr>
                        <th scope="col" class="f_field" nowrap="nowrap">번호</th>
                        <th scope="col" nowrap="nowrap">템플릿명</th>
                        <th scope="col" nowrap="nowrap">템플릿구분</th>
                        <th scope="col" nowrap="nowrap">템플릿경로</th>
                        <th scope="col" nowrap="nowrap">사용여부</th>
                        <th scope="col" nowrap="nowrap">등록일자</th>
                    </tr>
                    </thead>
                    <tbody>                 

                    <c:forEach var="result" items="${resultList}" varStatus="status">
                    <!-- loop 시작 -->                                
                      <tr>
                        <td class="listCenter" nowrap="nowrap"><c:out value="${(searchVO.pageIndex-1) * searchVO.pageSize + status.count}"/></td>            
                        <td class="listLeft" nowrap="nowrap">
                            <!-- a href="<c:url value='/wrks/cop/com/selectTemplateInf.do'/>?tmplatId=<c:out value='${result.tmplatId}'/>" -->
                            <a href="<c:url value='/wrks/cop/com/selectTemplateInf.do'/>?tmplatId=<c:out value='${result.tmplatId}'/>&child=<c:out value='${child}'/>&top=<c:out value='${top}'/>&left=<c:out value='${left}'/>">
                                <c:out value="${result.tmplatNm}"/>
                            </a>
                        </td>
                        <td nowrap="nowrap"><c:out value="${result.tmplatSeCodeNm}"/></td>
                        <td nowrap="nowrap"><c:out value="${result.tmplatCours}"/></td>
                        <td nowrap="nowrap">
                            <c:if test="${result.useAt == 'N'}"><spring:message code="button.notUsed" /></c:if>
                            <c:if test="${result.useAt == 'Y'}"><spring:message code="button.use" /></c:if>
                        </td>  
                        <td nowrap="nowrap"><c:out value="${result.frstRegisterPnttm}"/></td    >       
                      </tr>
                     </c:forEach>     
                     <c:if test="${fn:length(resultList) == 0}">
                      <tr>
                        <td nowrap colspan="5" ><spring:message code="common.nodata.msg" /></td>  
                      </tr>      
                     </c:if>
                    </tbody>
                    </table>
				</div>
				<div class="paginate">
					<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="fn_egov_select_tmplatInfo"  />
				</div>
				<div class="btnWrap btnR">
					<a href="#" onclick="fn_egov_insert_addTmplatInfo();" class="btn btnDt btnRgt">신규</a>
				</div>
			<!-- //페이지 네비게이션 끝 -->  
			</div>
			<!-- content End -->
		</div>
		<!-- contentWrap End -->
	</div>
	<!-- container End -->
</div>
</body>
</html>