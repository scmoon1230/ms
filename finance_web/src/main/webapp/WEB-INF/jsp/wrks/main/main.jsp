<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<c:set var="sysCd" 			value="${LoginVO.sysId}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<script type="text/javascript">

$(document).ready(function(){

});



//게시판 상세 보기
function fn_egov_inqire_notice(bbsId, nttId)
{
	var frm = document.frm;
	frm.nttId.value = nttId;
	frm.bbsId.value = bbsId;
	frm.action = "<c:url value='/wrks/cop/bbs/selectBoardArticle.do'/>";
	frm.submit();
}

function fn_messenger()
{
	var mw = '${messenger.NEW_WIN_WIDTH}';
	var mh = '${messenger.NEW_WIN_HEIGHT}';
	var mLeft = (screen.width - mw) / 2;
	var mTop = (screen.height - mh) / 4;  // for 25% - devide by 4  |  for 33% - devide by 3
	var mUrl = "<c:url value='/wrks/wrkmng/msgmng/messenger.do'/>";
		mUrl += '?top=' + '${messenger.TOP_ID}';
		mUrl += '&left=' + '${messenger.LEFT_ID}';
	window.open(mUrl, "_blank", 'toolbar=yes, scrollbars=yes, resizable=yes, width=' + mw + ', height=' + mh + ', top=' + mTop + ', left=' + mLeft);
}
</script>

</head>
<body HTTP_X_FORWARDED_FOR="${HTTP_X_FORWARDED_FOR}" REMOTE_ADDR="${REMOTE_ADDR}" getRemoteAddr="${getRemoteAddr}" user-context-root="<c:url value='/'/>">

<div id="wrapper" class="main">
	<!-- topbar -->
	<%@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
	<!-- //topbar -->
	<!-- container -->
	<div class="container">
		<div class="leftMenu">
			<img src="<c:url value='/'/>images/main_left.jpg" />
		</div>
		<!-- content -->
		<div class="contentWrap">
			<div class="content">
				<!-- 상단구역 -->
				<div class="boxWrap">
					<div class="schd_con">
						<h2 class="title">공지사항</h2>
						<ul>
						<c:forEach items="${mainList}" var="val"> 
							<c:choose>
							<c:when test="${val.TAG == 'BBS'}">
							<%-- <li><a href="<c:url value='/'/>wrks/wrkmng/schd/mng.do?sdate=${val.S_DATE}&edate=${val.E_DATE}&schdid=${val.ID}"> --%>
							<li>
								<a href="javascript:fn_egov_inqire_notice('<c:out value="BBS_N_002"/>', '<c:out value="${val.ID}"/>');" >
								<strong>${val.TITLE}</strong>
								<span class="date">${val.E_DATE}</span>
								</a>
							</li>
							</c:when>
							</c:choose>
						</c:forEach>
						</ul>
					</div>
					<div class="date_con">
						<c:forEach items="${topMenuList}" var="val"> 
							<c:choose>
							<c:when test="${val.PGM_MENU_NM_KO == '업무처리'}">
								<input type="hidden" id="calTop" value="${val.PGM_MENU_ID}" />
								<input type="hidden" id="calLeft" value="f52c93a8-e50c-4dd1-bb0f-d6a194ae4819" />
							</c:when>
							</c:choose>
						</c:forEach>
						<div id="datepicker" class="datepicker0" id="invisible" class="invisible"></div>
					</div>
				</div>
				<!-- 중간구역 -->
				<div class="boxWrap">
					<div class="tbLeft100">
						<div class="tableType1">
							<table>
							<tbody>
							<tr height="300">
								<th>
								<c:choose>
									<c:when test="${not empty sysCd}">
										<img src="<c:url value='/'/>images/${sysCd}/main_middle.png" />
									</c:when>
									<c:otherwise>
										<img src="<c:url value='/'/>images/main_middle.png" />
									</c:otherwise>
								</c:choose>
								</th>
							</tr>
							</tbody>
							</table>
						</div>
					</div>
				</div>
				<!-- 하단구역 -->
				<div class="boxWrap">
					<div class="tableType2 step_area">
						<!-- 상황처리 -->
						<c:forEach items="${topMenuList}" var="val"> 
							<c:choose>
							<c:when test="${val.PGM_MENU_NM_KO == '상황처리'}">
								<a href="javascript:$.openMenuCenter('<c:url value='/'/>${val.PGM_URL}?top=${val.PGM_MENU_ID}&child=${val.CHILD_PGM_MENU_ID}','CAR_NO', ${val.NEW_WIN_WIDTH}, ${val.NEW_WIN_HEIGHT})" ><img src="<c:url value='/'/>images/main_event_process.png"></a>
							</c:when>
							</c:choose>
						</c:forEach>
						<!-- 업무일지 -->
						<c:forEach items="${topMenuList}" var="val"> 
							<c:choose>
							<c:when test="${val.PGM_MENU_NM_KO == '업무처리'}">
								<a href="<c:url value='/'/>${val.PGM_URL}?top=${val.PGM_MENU_ID}&child=${val.CHILD_PGM_MENU_ID}" id="" class="ic_dumy" ><img src="<c:url value='/'/>images/main_business_log.png"></a>
							</c:when>
							</c:choose>
						</c:forEach>
						<!-- 비상연락망 -->
						<c:forEach items="${topMenuList}" var="val"> 
							<c:choose>
							<c:when test="${val.PGM_MENU_NM_KO == '업무처리'}">
								<a href="<c:url value='/'/>wrks/wrkmng/msgmng/contact.do?top=${val.PGM_MENU_ID}&left=4d0f855f-1cd1-44aa-a60b-fbdfcd4462e7" id="" class="ic_dumy" ><img src="<c:url value='/'/>images/main_emergency.png"></a>
							</c:when>
							</c:choose>
						</c:forEach>
						<!-- 커뮤니티 -->
<!-- 
						<c:forEach items="${topMenuList}" var="val"> 
							<c:choose>
							<c:when test="${val.PGM_MENU_NM_KO == '커뮤니티'}">
								<a href="<c:url value='/'/>${val.PGM_URL}&top=${val.PGM_MENU_ID}&child=${val.CHILD_PGM_MENU_ID}" id="" class="ic_dumy" ><img src="<c:url value='/'/>images/main_community.png"></a>
							</c:when>
							</c:choose>
						</c:forEach>
-->
						<!-- 사이트맵 -->
<!-- 
						<a href="<c:url value='/'/>wrks/lgn/sitemap.do" ><img src="<c:url value='/'/>images/main_sitemap.png"></a>
-->
<!-- 메신저
						<a href="javascript:$.openMenuCenter('/wrks/wrkmng/msgmng/messenger.do?top=08511a9f-f701-4720-913b-9e0cecd3cbf5&left=51cd0a28-f10e-47c8-aeaa-983c7ebbfee5','메신저',500, 800)"><img src="<c:url value='/'/>images/main_msg.png"></a>
						<a href="javascript:fn_messenger();"><img src="<c:url value='/'/>images/main_msg.png"></a>
-->
					</div>
				</div>
			</div>
		</div>
		<!-- //content -->
	</div>
	<!-- //container -->

	<div id="board">
		<form name="frm" action ="<c:url value='/wrks/cop/bbs/selectBoardList.do'/>" method="post">
			<input type="hidden" name="bbsId" value="BBS_N_002" />
			<input type="hidden" name="nttId"  value="0" />
			<input type="hidden" name="bbsTyCode" value="N" />
			<input type="hidden" name="bbsAttrbCode" value="T" />
			<input type="hidden" name="authFlag" value="Y" />
			<input name="pageIndex" type="hidden" value="1"/>
			<input type="submit" value="실행" onclick="fn_egov_select_noticeList('1'); return false;" id="invisible" class="invisible" />

			<!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->
			<c:forEach items="${topMenuList}" var="val"> 
				<c:choose>
					<c:when test="${val.PGM_MENU_NM_KO == '커뮤니티'}">
						<input type="hidden" name="child" value="<c:out value='${val.CHILD_PGM_MENU_ID}'/>" />
						<input type="hidden" name="top" value="<c:out value='${val.PGM_MENU_ID}'/>" />
					</c:when>
				</c:choose>
			</c:forEach>
			<input type="hidden" name="left" value="<c:out value=''/>" />
		</form>
	</div>
	<!-- footer -->
	<div id="footwrap">
		<div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
	</div>
	<!-- //footer -->
</div> 
</body>
</html> 