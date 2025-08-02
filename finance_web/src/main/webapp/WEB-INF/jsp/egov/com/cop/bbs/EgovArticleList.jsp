<%
 /**
  * @Class Name : EgovArticleList.jsp
  * @Description : EgovArticleList 화면
  * @Modification Information
  * @
  * @  수정일             수정자                   수정내용
  * @ -------    --------    ---------------------------
  * @ 2009.02.01   박정규              최초 생성
  *   2016.06.13   김연호              표준프레임워크 v3.6 개선
  *  @author 공통서비스팀
  *  @since 2009.02.01
  *  @version 1.0
  *  @see
  *
  */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle"><spring:message code="comCopBbs.articleVO.title"/></c:set>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle} <spring:message code="title.list" /></title>
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css' />">

    <style type="text/css">
        body {
            margin: 0;
        }

        .board {
            width: 100%;
            margin: 0;
            padding: 0;
        }

        .board h1 {
            margin: 5px 0 8px 0;
        }

        div.toggle.btn {
            margin-top: 0;
        }

        .toggle-group .btn {
            float: none;
        }

        .search_box {
            margin: 0;
        }

        .search_box a.btn {
            margin: 0;
        }

        .search_box input.s_input {
            height: 29px;
        }

        div.pagination {
            margin: 5px 0;
        }

        div.pagination ul strong, div.pagination ul a {
            position: relative;
            float: left;
            padding: 6px 10px;
            margin-left: -1px;
            line-height: 1.42857143;
            color: #428bca;
            text-decoration: none;
            background-color: #fff;
            margin: 2px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        div.pagination ul strong {
            z-index: 2;
            color: #fff;
            cursor: default;
            background-color: #428bca;
            border-color: #428bca;
        }

        div.pagination ul a:hover {
            background-color: #eee;
        }

        .articleCommentNum {
            color: rgb(120, 160, 120);
            font-weight: 600;
        }

        th, td {
            vertical-align: middle;
        }

        .board_list tbody td a.re_btn {
            float: right;
            display: flex;
            align-items: center;
            border-radius: 3px;
            height: 96px;
            background-color: #6b7b91;
            border-color: #617187;
            font-weight: normal;
            color: #fff;
        }

        input.btn-wrks {
            color: #fff !important;
            background-color: #6b7b91 !important;
            border-color: #617187 !important;
            margin-top: 0 !important;
            margin-bottom: 0 !important;
        }
    </style>

<script type="text/javascript">
	$(document).ready(function(){
	
	});
	
	/*********************************************************
	 * 초기화
	 ******************************************************** */
	function fn_egov_init(){
		// 첫 입력란에 포커스..
		document.articleForm.searchCnd.focus();
	}
	
	/*********************************************************
	 * 페이징 처리 함수
	 ******************************************************** */
	function fn_egov_select_linkPage(pageNo){
		document.articleForm.pageIndex.value = pageNo;
		document.articleForm.action = "<c:url value='/egov/com/cop/bbs/selectArticleList.do'/>";
	   	document.articleForm.submit();
	}
	/*********************************************************
	 * 조회 처리 함수
	 ******************************************************** */
	function fn_egov_search_article(){
		document.articleForm.pageIndex.value = 1;
		document.articleForm.submit();
	}
	/* ********************************************************
	 * 상세회면 처리 함수
	 ******************************************************** */
	function fn_egov_inquire_articledetail(bbsId, nttId) {
		// 사이트 키값(siteId) 셋팅.
		document.articleForm.bbsId.value = bbsId;
		document.articleForm.nttId.value = nttId;
	  	document.articleForm.action = "<c:url value='/egov/com/cop/bbs/selectArticleDetail.do'/>";
	  	document.articleForm.submit();
	}
</script>
</head>
<body onload="fn_egov_init()">
<!-- javascript warning tag -->
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
<div id="wrapper" class="wth100">
	<!-- container Start -->
	<div class="container">
		<!-- contentWrap Start -->
		<div class="contentWrap">
			<!-- content Start -->
			<div class="content">
                <%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
				<div class="board" align=center>
					<h1>${pageTitle} <spring:message code="title.list" /></h1>
					<!-- 하단 버튼 -->
					<form name="articleForm" action="<c:url value='/egov/com/cop/bbs/selectArticleList.do'/>" method="post" onSubmit="fn_egov_search_article(); return false;"> 
					<div class="search_box" title="<spring:message code="common.searchCondition.msg" />">
						<ul>
							<li>
								<select name="searchCnd" title="<spring:message code="title.searchCondition" /> <spring:message code="input.cSelect" />">
									<option value="0"  <c:if test="${searchVO.searchCnd == '0'}">selected="selected"</c:if> ><spring:message code="comCopBbs.articleVO.list.nttSj" /></option><!-- 글 제목 -->
									<option value="1"  <c:if test="${searchVO.searchCnd == '1'}">selected="selected"</c:if> ><spring:message code="comCopBbs.articleVO.list.nttCn" /></option><!-- 글 내용 -->
									<option value="2"  <c:if test="${searchVO.searchCnd == '2'}">selected="selected"</c:if> ><spring:message code="table.reger" /></option><!-- 작성자 -->
								</select>
							</li>
							<!-- 검색키워드 및 조회버튼 -->
							<li>
								<input class="s_input" name="searchWrd" type="text"  size="35" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${searchVO.searchWrd}"/>'  maxlength="155" >
								<input type="submit" class="s_btn" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
								<%--
								<span class="btn_b"><a href="<c:url value='/egov/com/cop/bbs/insertArticleView.do?bbsId=${boardMasterVO.bbsId}&left=${articleVO.left}&top=${articleVO.top}&m=${articleVO.m}' />"
								title="<spring:message code="button.create" /> <spring:message code="input.button" />"><spring:message code="button.create" /></a></span>
								--%>
							</li>
						</ul>
					</div>
					<input name="bbsId" type="hidden" value="${boardMasterVO.bbsId}">
					<input name="top" type="hidden" value="${articleVO.top}">
					<input name="left" type="hidden" value="${articleVO.left}">
					<input name="m" type="hidden" value="${articleVO.m}">
					<input name="child" type="hidden" value="${articleVO.child}">
					</form>
					
					<!-- 목록영역 -->
					<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
					<caption>${pageTitle} <spring:message code="title.list" /></caption>
					<colgroup>
						<col style="width: 9%;">
						<col style="width: 40%;">
						<col style="width: 13%;">
						<col style="width: 13%;">
						<col style="width: 13%;">
					</colgroup>
					<thead>
					<tr>
						<th style="text-align:center;"><spring:message code="table.num" /></th><!-- 번호 -->
						<th class="board_th_link"><spring:message code="comCopBbs.articleVO.list.nttSj" /></th><!--글 제목 -->
						<th style="text-align:center;"><spring:message code="table.reger" /></th><!-- 작성자명 -->
						<th style="text-align:center;"><spring:message code="table.regdate" /></th><!-- 작성시각 -->
						<th style="text-align:center;"><spring:message code="comCopBbs.articleVO.list.inqireCo" /></th><!-- 조회수 -->
					</tr>
					</thead>
					<tbody class="ov">
					<!-- 공지사항 본문 -->
					<c:forEach items="${noticeList}" var="noticeInfo" varStatus="status">
					<tr>
						<td><img src="<c:url value='/images/egovframework/com/cop/bbs/icon_notice.png'/>" alt="notice"></td>
						<td class="bold">
							<form name="subForm" method="post" action="<c:url value='/egov/com/cop/bbs/selectArticleDetail.do'/>">
							    <input name="nttId" type="hidden" value="<c:out value="${noticeInfo.nttId}"/>">
							    <input name="bbsId" type="hidden" value="<c:out value="${noticeInfo.bbsId}"/>">
							    <input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
								<input name="top" 	type="hidden" value="${articleVO.top}">
								<input name="left" 	type="hidden" value="${articleVO.left}">
								<input name="child" type="hidden" value="${articleVO.child}">
								<input name="m" 	type="hidden" value="${articleVO.m}">
							    <span class="link"><input type="submit" value="<c:out value='${fn:substring(noticeInfo.nttSj, 0, 40)}'/>" style="border:0px solid #e0e0e0;">
							    </span>
							</form>
						</td>
						<td><c:out value='${noticeInfo.frstRegisterNm}'/></td>
						<td><c:out value='${noticeInfo.frstRegisterPnttm}'/></td>
						<td><c:out value='${noticeInfo.inqireCo}'/></td>		
					</tr>
					</c:forEach>
					<!-- 게시글 본문 -->
					<c:forEach items="${resultList}" var="resultInfo" varStatus="status">
					<tr>
						<td><c:out value="${(searchVO.pageIndex-1) * searchVO.pageSize + status.count}"/></td>
						
					<c:choose>
						<c:when test="${resultInfo.sjBoldAt == 'Y'}">
						<!-- 제목 Bold인 경우 -->
						<td class="bold">
						<form name="subForm" method="post" action="<c:url value='/egov/com/cop/bbs/selectArticleDetail.do'/>">
							    <input name="nttId" type="hidden" value="<c:out value="${resultInfo.nttId}"/>">
							    <input name="bbsId" type="hidden" value="<c:out value="${resultInfo.bbsId}"/>">
							    <input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
								<input name="top" type="hidden" value="${articleVO.top}">
								<input name="left" type="hidden" value="${articleVO.left}">
								<input name="child" type="hidden" value="${articleVO.child}">
								<input name="m" type="hidden" value="${articleVO.m}">
							    <span class="link"><c:if test="${resultInfo.replyLc!=0}"><c:forEach begin="0" end="${resultInfo.replyLc}" step="1">&nbsp;	</c:forEach><img src="<c:url value='/images/egovframework/com/cop/bbs/icon_reply.png'/>" alt="secret"></c:if><input type="submit" value="<c:out value='${fn:substring(resultInfo.nttSj, 0, 40)}'/>" style="border:0px solid #e0e0e0;"></span>
						</form>
						</td>
						</c:when>	
						<c:when test="${resultInfo.secretAt == 'Y' && sessionUniqId != resultInfo.frstRegisterId}">
						<!-- 비밀글이며 작성자가 본인이 아닌 경우(클릭 불가) -->
						<td class="left">
						<c:if test="${resultInfo.replyLc!=0}">
				    		<c:forEach begin="0" end="${resultInfo.replyLc}" step="1">
				    			&nbsp;
				    		</c:forEach>
				    	</c:if>
						<img src="<c:url value='/images/egovframework/com/cop/bbs/icon_lock.png'/>" alt="secret">&nbsp;<c:out value='${fn:substring(resultInfo.nttSj, 0, 40)}'/>
						</td>
						</c:when>
						<c:otherwise>
						<!-- 나머지 경우 -->
						<td class="left">
				    	<form name="subForm" method="post" action="<c:url value='/egov/com/cop/bbs/selectArticleDetail.do'/>">
							    <input name="nttId" type="hidden" value="<c:out value="${resultInfo.nttId}"/>">
							    <input name="bbsId" type="hidden" value="<c:out value="${resultInfo.bbsId}"/>">
							    <input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
								<input name="top" type="hidden" value="${articleVO.top}">
								<input name="left" type="hidden" value="${articleVO.left}">
								<input name="child" type="hidden" value="${articleVO.child}">
								<input name="m" type="hidden" value="${articleVO.m}">
							    <span class="link"><c:if test="${resultInfo.replyLc!=0}"><c:forEach begin="0" end="${resultInfo.replyLc}" step="1">&nbsp;	</c:forEach><img src="<c:url value='/images/egovframework/com/cop/bbs/icon_reply.png'/>" alt="secret"></c:if><input type="submit" value="<c:out value='${fn:substring(resultInfo.nttSj, 0, 40)}'/>" style="border:0px solid #e0e0e0;"></span>
						</form>
						</td>
						</c:otherwise>
					</c:choose>
						<td><c:out value='${resultInfo.frstRegisterNm}'/></td>
						<td><c:out value='${resultInfo.frstRegisterPnttm}'/></td>
						<td><c:out value='${resultInfo.inqireCo}'/></td>		
					</tr>
					</c:forEach>
				
					<c:if test="${fn:length(resultList) == 0}">
					<!-- 글이 없는 경우 -->
					<tr>
						<td colspan="5"><spring:message code="common.nodata.msg" /></td>
					</tr>
					</c:if>
					</tbody>
					</table>
					
					<!-- paging navigation -->
					<div class="pagination">
						<ul>
						<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="fn_egov_select_linkPage"/>
						</ul>
					</div>
					
					<!-- 등록버튼 -->
					<!-- 
					<div class="btn">
						<span class="btn_s"><a href="<c:url value='/egov/com/cop/bbs/insertBBSMasterView.do' />"  title="<spring:message code="button.create" /> <spring:message code="input.button" />"><spring:message code="button.create" /></a></span>
					</div>
					--> 
					<div class="search_box">
						<ul>
							<li>
								<span class="btn_b"><a href="<c:url value='/egov/com/cop/bbs/insertArticleView.do?bbsId=${boardMasterVO.bbsId}&left=${articleVO.left}&top=${articleVO.top}&m=${articleVO.m}' />"
								title="<spring:message code="button.create" /> <spring:message code="input.button" />"><spring:message code="button.create" /></a></span>
							</li>
						</ul>
					</div>
					
				</div>
				
				<input name="nttId" type="hidden" value="0">
				<input name="bbsId" type="hidden" value="${boardMasterVO.bbsId}">
				<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>">
				<input name="top" type="hidden" value="${articleVO.top}">
				<input name="left" type="hidden" value="${articleVO.left}">
				<input name="m" type="hidden" value="${articleVO.m}">
				<input name="child" type="hidden" value="${articleVO.child}">

			</div>
		</div> <!-- content End -->
	</div> <!-- contentWrap End -->
</div> <!-- container End -->


</body>
</html>