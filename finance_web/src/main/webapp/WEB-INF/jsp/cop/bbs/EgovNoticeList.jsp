<%--
  Class Name : EgovNoticeList.jsp
  Description : 게시물 목록화면
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

<c:if test="${anonymous == 'true'}"><c:set var="prefix" value="/anonymous"/></c:if>
<script type="text/javascript" src="<c:url value='/js/egov/EgovBBSMng.js' />" ></script>
<script type="text/javascript">
	$(document).ready(function(){
	
	});

    function press(event) {
        if (event.keyCode==13) {
            fn_egov_select_noticeList('1');
        }
    }

    function fn_egov_addNotice() {
    	var frm = document.frm;
        frm.action = "<c:url value='/wrks/cop/bbs/addBoardArticle.do'/>";
        frm.submit();
    }
    
    function fn_egov_select_noticeList(pageNo) {
        document.frm.pageIndex.value = pageNo;
        document.frm.action = "<c:url value='/wrks/cop/bbs/selectBoardList.do'/>";
        document.frm.submit();  
    }

	// 게시판 상세 보기
	function fn_egov_inqire_notice(bbsId, nttId)
	{
		//var frm = document.subForm;
		var frm = document.frm;
		frm.nttId.value = nttId;
		frm.bbsId.value = bbsId;
		frm.action = "<c:url value='/wrks/cop/bbs/selectBoardArticle.do'/>";
		frm.submit();
	}

</script>

<title><c:out value="${brdMstrVO.bbsNm}"/> 목록</title>

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
				<div class="titArea"><h3 class="tit"><c:out value="${brdMstrVO.bbsNm}"/></h3></div>
				<div class="tableTypeHalf seachT">
					<form name="frm" action ="<c:url value='/wrks/cop/bbs/selectBoardList.do'/>" method="post">
						<input type="hidden" name="bbsId" value="<c:out value='${boardVO.bbsId}'/>" />
						<input type="hidden" name="nttId"  value="0" />
						<input type="hidden" name="bbsTyCode" value="<c:out value='${brdMstrVO.bbsTyCode}'/>" />
						<input type="hidden" name="bbsAttrbCode" value="<c:out value='${brdMstrVO.bbsAttrbCode}'/>" />
						<input type="hidden" name="authFlag" value="<c:out value='${brdMstrVO.authFlag}'/>" />
						<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
						<input type="submit" value="실행" onclick="fn_egov_select_noticeList('1'); return false;" id="invisible" class="invisible" />

						<!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->
						<input type="hidden" name="child" value="<c:out value='${child}'/>" />
						<input type="hidden" name="top" value="<c:out value='${top}'/>" />
						<input type="hidden" name="left" value="<c:out value='${left}'/>" />

					<table>
					<caption>공통코드그룹</caption>
					<tbody>
					<tr>
						<th>
						<label for="search_select"></label>
						<select name="searchCnd" class="selectType1" title="검색조건 선택">
							<option value="0" <c:if test="${searchVO.searchCnd == '0'}">selected="selected"</c:if> >제목</option>
							<option value="1" <c:if test="${searchVO.searchCnd == '1'}">selected="selected"</c:if> >내용</option>
							<option value="2" <c:if test="${searchVO.searchCnd == '2'}">selected="selected"</c:if> >작성자</option>
						</select>
						</th>
						<td>
							<input name="searchWrd" type="text" size="35" class="txtType txtType100 searchEvt"  value='<c:out value="${searchVO.searchWrd}"/>' maxlength="35" onkeypress="press(event);" title="검색어 입력"> 
						</td>
						<td>
							<a href="#noscript" onclick="fn_egov_select_noticeList('1'); return false;" class="btn btnRight btnS searchBtn">검색 </a>
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
					<table summary="번호, 제목, 게시시작일, 게시종료일, 작성자, 작성일, 조회수   입니다" cellpadding="0" cellspacing="0">
					<caption>게시물 목록</caption>
					<colgroup>
						<col width="10%" >
						<col width="44%" >

						<c:if test="${brdMstrVO.bbsAttrbCode == 'BBSA01'}">
						<col width="20%" >
						<col width="20%" >
						</c:if>

						<c:if test="${anonymous != 'true'}">
						<col width="20%" >
						</c:if>

						<col width="15%" >
						<col width="8%" >
					</colgroup>
					<thead>
					<tr>
						<th scope="col" class="f_field" nowrap="nowrap">번호</th>
						<th scope="col" nowrap="nowrap">제목</th>

						<c:if test="${brdMstrVO.bbsAttrbCode == 'BBSA01'}">
						<th scope="col" nowrap="nowrap">게시시작일</th>
						<th scope="col" nowrap="nowrap">게시종료일</th>
						</c:if>

						<c:if test="${anonymous != 'true'}">
						<th scope="col" nowrap="nowrap">작성자</th>
						</c:if>

						<th scope="col" nowrap="nowrap">작성일</th>
						<th scope="col" nowrap="nowrap">조회수</th>
					</tr>
					</thead>
					<tbody>

					<c:forEach var="result" items="${resultList}" varStatus="status">
					<!-- loop 시작 -->
					<tr>
						<td class="listCenter" nowrap="nowrap"><c:out value="${paginationInfo.totalRecordCount+1 - ((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
						<td align="left" onclick="fn_egov_inqire_notice('<c:out value="${result.bbsId}"/>', '<c:out value="${result.nttId}"/>');">

							<c:if test="${result.replyLc!=0}">
							<c:forEach begin="0" end="${result.replyLc}" step="1">
							&nbsp;
							</c:forEach>
							<img src="<c:url value='/images/reply_arrow.gif'/>" alt="reply arrow">
							</c:if>

							<c:choose>
								<c:when test="${result.isExpired=='Y' || result.useAt == 'N'}">
								<c:out value="${result.nttSj}" />
								</c:when>

								<c:otherwise>
								<c:out value="${result.nttSj}"/>
								</c:otherwise>
							</c:choose>
						</td>

						<c:if test="${brdMstrVO.bbsAttrbCode == 'BBSA01'}">
						<td class="listCenter" nowrap="nowrap"><c:out value="${result.ntceBgnde}"/></td>
						<td class="listCenter" nowrap="nowrap"><c:out value="${result.ntceEndde}"/></td>
						</c:if>

						<c:if test="${anonymous != 'true'}">
						<td class="listCenter" nowrap="nowrap"><c:out value="${result.frstRegisterNm}"/></td>
						</c:if>

						<td class="listCenter" nowrap="nowrap"><c:out value="${result.frstRegisterPnttm}"/></td>
						<td class="listCenter" nowrap="nowrap"><c:out value="${result.inqireCo}"/></td>
					</tr>
					</c:forEach>
					<c:if test="${fn:length(resultList) == 0}">
					<tr> 
						<c:choose>
						<c:when test="${brdMstrVO.bbsAttrbCode == 'BBSA01'}">
						<td class="listCenter" colspan="7" ><spring:message code="common.nodata.msg" /></td>
						</c:when>

						<c:otherwise>
						<c:choose>
						<c:when test="${anonymous == 'true'}">
						<td class="listCenter" colspan="4" ><spring:message code="common.nodata.msg" /></td>
						</c:when>

						<c:otherwise>
						<td class="listCenter" colspan="5" ><spring:message code="common.nodata.msg" /></td>
						</c:otherwise>
						</c:choose>
						</c:otherwise>

						</c:choose>
					</tr>
					</c:if>
					</tbody>
					</table>
				</div>
				<div class="paginate">
					<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="fn_egov_select_noticeList" />  
				</div>
				<div class="btnWrap btnR">
					<a href="#" onclick="fn_egov_addNotice();" class="btn btnDt btnRgt">신규</a>
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