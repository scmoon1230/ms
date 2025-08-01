<%
 /**
  * @Class Name : EgovArticleDetail.jsp
  * @Description : EgovArticleDetail 화면
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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%pageContext.setAttribute("crlf", "\r\n"); %>
<c:set var="pageTitle"><spring:message code="comCopBbs.articleVO.title"/></c:set>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle} <spring:message code="title.detail" /></title>
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css' />">
    <style>
        .top_line {
            margin-bottom: 60px;
        }

        .search_box {
            margin-right: 8%;
        }

        .board h1 {
            margin: 0px 0 0 2%;
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
            margin-top: 0px !important;
            margin-bottom: 0px !important;
        }

        .btn {
            margin: 0;
        }

        .bbsbtn {
            margin-top: 5px;
            margin-bottom: 5px;
        }

        div.paging {
            margin: 5px 34% 5px 34%;
        }

        div.paging ul strong, div.paging ul a {
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

        div.paging ul strong {
            z-index: 2;
            color: #fff;
            cursor: default;
            background-color: #428bca;
            border-color: #428bca;
        }

        div.paging ul a:hover {
            background-color: #eee;
        }

        .reply {
            padding: 0;
        }

        .reply .txt {
            width: calc(100% - 110px);
            display: inline-block;
        }

        .reply .bottom {
            display: inline-block;
            float: none;
            width: 105px;
            margin: 0;
        }

        table.wTable td.cnt em {
            font-style: italic;
        }
    </style>
<script type="text/javascript">
	$(document).ready(function(){
	
	});
	
	/* ********************************************************
	 * 삭제처리
	 ******************************************************** */
	 function fn_egov_delete_article(form){
		if(confirm("<spring:message code="common.delete.msg" />")){	
			// Delete하기 위한 키값을 셋팅
			form.submit();	
		}	
	}	
	
	/* ********************************************************
	 * 답글작성
	 ******************************************************** */
	 function fn_egov_reply_article() {
		document.articleForm.action = "<c:url value='/egov/com/cop/bbs/replyArticleView.do'/>";
		document.articleForm.submit();
	}
	
</script>
<!-- 댓글 작성 스크립트 -->
<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="articleComment" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
	function fn_egov_insert_commentList() {
		
		var form = document.getElementById("articleCommentVO");
	
		if (!validateArticleComment(form)){
			return;
		}
		if (confirm('<spring:message code="common.regist.msg" />')) {
			form.submit();
		}
	}
	
	function fn_egov_updt_commentList() {
		
		var form = document.getElementById("articleCommentVO");
		
		if (!validateArticleComment(form)){
			return;
		}
	
		if (confirm('<spring:message code="common.update.msg" />')) {
			form.modified.value = "true";
			form.action = "<c:url value='/egov/com/cop/cmt/updateArticleComment.do'/>";
			form.submit();
		}
	}
	
	function fn_egov_selectCommentForupdt(commentNo) {
		
		var form = document.getElementById("articleCommentVO");
		
		form.commentNo.value = commentNo;
		form.action = "<c:url value='/egov/com/cop/bbs/selectArticleDetail.do'/>";
		form.submit();
	}
	
	function fn_egov_deleteCommentList(commentNo) {
	
		var form = document.getElementById("articleCommentVO");
		
		if (confirm('<spring:message code="common.delete.msg" />')) {
			form.modified.value = "true";
			form.commentNo.value = commentNo;
			form.action = "<c:url value='/egov/com/cop/cmt/deleteArticleComment.do'/>";
			form.submit();
		}
	}
	
	/* 댓글페이징 */
	function fn_egov_select_commentList(pageNo) {
		
		var form = document.getElementById("articleCommentVO");
		
		form.subPageIndex.value = pageNo;
		form.commentNo.value = '';
		form.action = "<c:url value='/egov/com/cop/bbs/selectArticleDetail.do'/>";
		form.submit();
	}
</script>
</head>
<body>
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
				<div class="wTableFrm">
					<!-- 타이틀 -->
					<h2>${pageTitle} <spring:message code="title.detail" /></h2>
				
					<!-- 상세조회 -->
					<table class="wTable" summary="<spring:message code="common.summary.inqire" arguments="${pageTitle}" />">
						<caption>${pageTitle} <spring:message code="title.detail" /></caption>
						<colgroup>
	                        <col style="width: 10%;"/>
	                        <col style="width: 23.3%;"/>
	                        <col style="width: 10%;"/>
	                        <col style="width: 23.3%;"/>
	                        <col style="width: 10%;"/>
	                        <col style="width: 23.3%;"/>
						</colgroup>
						<tbody>
							<!-- 글 제목 -->
							<tr>
								<th><spring:message code="comCopBbs.articleVO.detail.nttSj" /></th>
								<td colspan="5" class="left"><c:out value="${result.nttSj}"/></td>
							</tr>
							<!-- 작성자, 작성시각, 조회수 -->
							<tr>
								<th><spring:message code="table.reger" /></th>
								<td class="left"><c:out value="${result.frstRegisterNm}"/></td>
								<th><spring:message code="table.regdate" /></th>
								<td class="left"><c:out value="${result.frstRegisterPnttm}"/></td>
								<th><spring:message code="comCopBbs.articleVO.detail.inqireCo" /></th>
								<td class="left"><c:out value="${result.inqireCo}"/></td>
							</tr>
							<!-- 글 내용 -->
							<tr>
								<th class="vtop"><spring:message code="comCopBbs.articleVO.detail.nttCn" /></th>
								<td colspan="5" class="cnt">
									<c:out value="${fn:replace(result.nttCn , crlf , '<br/>')}" escapeXml="false" />
								</td>
							</tr>
							<!-- 게시일자 -->
							<tr  style="display:none;">
								<th class="vtop"><spring:message code="comCopBbs.articleVO.detail.ntceDe" /></th>
								<td colspan="5" class="left">
									<c:out value="${result.ntceBgnde} ~ ${result.ntceEndde}" escapeXml="false" />
								</td>
							</tr>
							<!-- 첨부파일 -->
							<c:if test="${not empty result.atchFileId}">
							<tr>
								<th><spring:message code="comCopBbs.articleVO.detail.atchFile" /></th>
								<td colspan="5">
									<c:import url="/egov/com/cmm/fms/selectFileInfs.do" charEncoding="UTF-8">
									<c:param name="param_atchFileId" value="${result.atchFileId}" />
								</c:import>
								</td>
							</tr>
						  	</c:if>
							
						</tbody>
					</table>
					<!-- 하단 버튼 -->
					<div class="bbsbtn">
						<c:if test="${result.ntcrId != 'anonymous'}">
						<!-- 익명글 수정/삭제 불가 -->
						<form name="articleForm" action="<c:url value='/egov/com/cop/bbs/updateArticleView.do'/>" method="post" style="float:left;">
							<input type="submit" class="s_submit" value="<spring:message code="button.update" />" title="<spring:message code="title.update" /> <spring:message code="input.button" />" />
							<input type="hidden" name="parnts" value="<c:out value='${result.parnts}'/>" >
							<input type="hidden" name="sortOrdr" value="<c:out value='${result.sortOrdr}'/>" >
							<input type="hidden" name="replyLc" value="<c:out value='${result.replyLc}'/>" >
							<input type="hidden" name="nttSj" value="<c:out value='${result.nttSj}'/>" >
							<input name="nttId" type="hidden" value="<c:out value="${result.nttId}" />">
							<input name="bbsId" type="hidden" value="<c:out value="${boardMasterVO.bbsId}" />">
							<input name="top" type="hidden" value="${articleVO.top}">
							<input name="left" type="hidden" value="${articleVO.left}">
							<input name="child" type="hidden" value="${articleVO.child}">
							<input name="m" type="hidden" value="${articleVO.m}">
						</form>
						<form name="formDelete" action="<c:url value='/egov/com/cop/bbs/deleteArticle.do'/>" method="post" style="float:left; margin:0 0 0 3px;">
							<input type="submit" class="s_submit" value="<spring:message code="button.delete" />" title="<spring:message code="button.delete" /> <spring:message code="input.button" />" onclick="fn_egov_delete_article(this.form); return false;">
							<input name="nttId" type="hidden" value="<c:out value="${result.nttId}" />">
							<input name="bbsId" type="hidden" value="<c:out value="${boardMasterVO.bbsId}" />">
							<input name="top" type="hidden" value="${articleVO.top}">
							<input name="left" type="hidden" value="${articleVO.left}">
							<input name="child" type="hidden" value="${articleVO.child}">
							<input name="m" type="hidden" value="${articleVO.m}">
						</form>
						</c:if>
						<c:if test="${boardMasterVO.replyPosblAt == 'Y' }">
						<form name="formReply" action="<c:url value='/egov/com/cop/bbs/replyArticleView.do'/>" method="post" style="float:left; margin:0 0 0 3px;">
							<input type="submit" class="s_submit" value="<spring:message code="button.reply" />">
							<input name="nttId" type="hidden" value="<c:out value="${result.nttId}" />">
							<input name="bbsId" type="hidden" value="<c:out value="${boardMasterVO.bbsId}" />">
							<input name="top" type="hidden" value="${articleVO.top}">
							<input name="left" type="hidden" value="${articleVO.left}">
							<input name="child" type="hidden" value="${articleVO.child}">
							<input name="m" type="hidden" value="${articleVO.m}">
						</form>
						</c:if>
						<form name="formList" action="<c:url value='/egov/com/cop/bbs/selectArticleList.do'/>" method="post" style="float:left; margin:0 0 0 3px;">
							<input type="submit" class="s_submit" value="<spring:message code="button.list" />">
							<input name="bbsId" type="hidden" value="<c:out value="${boardMasterVO.bbsId}" />">
							<input name="top" type="hidden" value="${articleVO.top}">
							<input name="left" type="hidden" value="${articleVO.left}">
							<input name="child" type="hidden" value="${articleVO.child}">
							<input name="m" type="hidden" value="${articleVO.m}">
						</form>
						<c:if test="'N' == 'Y' }">
						<form name="formScrap" action="<c:url value='/egov/com/cop/scp/insertArticleScrapView.do'/>" method="post" style="float:left; margin:0 0 0 3px;">
							<input type="submit" class="s_submit" value="<spring:message code="button.scrap" />">
							<input name="nttId" type="hidden" value="<c:out value="${result.nttId}" />">
							<input name="bbsId" type="hidden" value="<c:out value="${boardMasterVO.bbsId}" />">
							<input name="top" type="hidden" value="${articleVO.top}">
							<input name="left" type="hidden" value="${articleVO.left}">
							<input name="child" type="hidden" value="${articleVO.child}">
							<input name="m" type="hidden" value="${articleVO.m}">
						</form>
						</c:if>
						
					</div><div style="clear:both;"></div>
					
				</div>
				
				<!-- 댓글 -->
				<c:import url="/egov/com/cop/cmt/selectArticleCommentList.do" charEncoding="UTF-8"/>

			</div>

		</div> <!-- content End -->
	</div> <!-- contentWrap End -->
</div> <!-- container End -->


</body>
</html>
