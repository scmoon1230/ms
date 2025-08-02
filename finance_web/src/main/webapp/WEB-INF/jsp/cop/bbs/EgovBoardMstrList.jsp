<%--
  Class Name : EgovBoardMstrList.jsp
  Description : 게시판 속성 목록화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.12   이삼섭              최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.12
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
<title>게시판 목록조회</title>
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript" src="<c:url value='/js/egov/EgovBBSMng.js' />"></script>
<script type="text/javascript">
	$(document).ready(function(){
	
	});
    function press(event) {
        if (event.keyCode==13) {
            fn_egov_select_brdMstr('1');
        }
    }
    
    function fn_egov_insert_addBrdMstr(){   
        document.frm.action = "<c:url value='/wrks/cop/bbs/addBBSMaster.do'/>";
        document.frm.submit();
    }
    
    function fn_egov_select_brdMstr(pageNo){
        document.frm.pageIndex.value = pageNo; 
        document.frm.action = "<c:url value='/wrks/cop/bbs/SelectBBSMasterInfs.do'/>";
        document.frm.submit();  
    }
    
    function fn_egov_inqire_brdMstr(bbsId){
        document.frm.bbsId.value = bbsId;
        document.frm.action = "<c:url value='/wrks/cop/bbs/SelectBBSMasterInf.do'/>";
        document.frm.submit();          
    }
</script>
<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>

</head>
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
			<!-- content Start -->
			<div class="content">
				<!-- 검색 필드 박스 시작 -->
				<div class="titArea"><h3 class="tit">게시판정보</h3></div>
				<div class="tableTypeHalf seachT">
					<form name="frm" action="<c:url value='/wrks/cop/bbs/SelectBBSMasterInfs.do'/>" method="post">
						<input type="hidden" name="bbsId">
						<input type="hidden" name="trgetId">
						<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
						<!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->
						<input type="hidden" name="child" value="<c:out value='${child}'/>" />
						<input type="hidden" name="top" value="<c:out value='${top}'/>" />
						<input type="hidden" name="left" value="<c:out value='${left}'/>" />
					
					<table>
					<caption>게시판관리</caption>
					<tbody>
					<tr>
						<th>
						<select name="searchCnd" title="검색유형선력">
							<option value="0" <c:if test="${searchVO.searchCnd == '0'}">selected="selected"</c:if> >게시판명</option>
							<option value="1" <c:if test="${searchVO.searchCnd == '1'}">selected="selected"</c:if> >게시판유형</option>   
						</select>
						</th>
						<td>
						<td>
							<input name="searchWrd" type="text" size="25" class="txtType txtType100 searchEvt"  value='<c:out value="${searchVO.searchWrd}"/>' maxlength="35" onkeypress="press(event);" title="검색어 입력">
						</td>
						<td>
							<a href="#noscript" onclick="fn_egov_select_brdMstr('1'); return false;" class="btn btnRight btnS searchBtn">검색 </a>
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
                    <table summary="번호,게시판명,게시판유형,게시판속성,생성일,사용여부  목록입니다" cellpadding="0" cellspacing="0">
                    <caption>사용자목록관리</caption>
                    <colgroup>
                    <col width="10%" >
                    <col width="34%" >
                    <col width="10%" >
                    <col width="10%" >
                    <col width="10%" >
                    <col width="15%" >
                    <col width="8%" >
                    </colgroup>
                    <thead>
                    <tr>
                        <th scope="col" class="f_field" nowrap="nowrap">번호</th>
                        <th scope="col" nowrap="nowrap">게시판명</th>
                        <th scope="col" nowrap="nowrap">게시판아이디</th>
                        <th scope="col" nowrap="nowrap">게시판유형</th>
                        <th scope="col" nowrap="nowrap">게시판속성</th>
                        <th scope="col" nowrap="nowrap">생성일</th>
                        <th scope="col" nowrap="nowrap">사용여부</th>
                    </tr>
                    </thead>
                    <tbody>                 

                    <c:forEach var="result" items="${resultList}" varStatus="status">
                    <!-- loop 시작 -->
                      <tr>
                        <!--td class="lt_text3" nowrap="nowrap"><strong><input type="checkbox" name="check1" class="check2"></strong></td-->
                        <td nowrap="nowrap"><strong><c:out value="${(searchVO.pageIndex-1) * searchVO.pageSize + status.count}"/></strong></td>
                        <td nowrap="nowrap">
                        	<a href="#" onclick="fn_egov_inqire_brdMstr('<c:out value="${result.bbsId}"/>');">
                            <c:out value="${result.bbsNm}"/>
                            </a>
                        </td>
                        <td nowrap="nowrap"><c:out value="${result.bbsId}"/></td>
                        <td nowrap="nowrap"><c:out value="${result.bbsTyCodeNm}"/></td>
                        <td nowrap="nowrap"><c:out value="${result.bbsAttrbCodeNm}"/></td>
                        <td nowrap="nowrap"><c:out value="${result.frstRegisterPnttm}"/></td>
                        <td nowrap="nowrap">
                            <c:if test="${result.useAt == 'N'}"><spring:message code="button.notUsed" /></c:if>
                            <c:if test="${result.useAt == 'Y'}"><spring:message code="button.use" /></c:if>
                        </td>  
                      </tr>
                    </c:forEach>      
                    <c:if test="${fn:length(resultList) == 0}">
                      <tr>
                        <td nowrap colspan="7"><spring:message code="common.nodata.msg" /></td>  
                      </tr>      
                    </c:if>
                    </tbody>
                    </table>
				</div>
				<div class="paginate">
					<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="fn_egov_select_brdMstr"  />
				</div>
				<div class="btnWrap btnR">
					<a href="#" onclick="fn_egov_insert_addBrdMstr();" class="btn btnDt btnRgt">신규</a>
				</div>
			<!-- //페이지 네비게이션 끝 -->  
			</div>
			<!-- content End -->
		</div>
		<!-- contentWrap End -->
	</div>
	<!-- container End -->
</div>
<!-- footer -->
<!-- <div id="footwrap">
    <div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer --> 
</body>
</html>