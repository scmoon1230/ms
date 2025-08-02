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
</script>

</head>
<body>
<div id="wrapper">
    <!-- topbar -->
	<%@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
    <!-- //topbar -->
    <!-- container -->
    <div class="container">
        <div class="leftMenu">
			<table>
	             <tbody>
	             <tr height="800">
	                 <!-- <th>좌측 이미지</th> -->
	             </tr>
	             </tbody>
	    	</table>
        </div>
        
        <!-- content -->
        <div class="contentWrap">
            <div class="topArea">
                <a href="#" class="btnOpen"><img src="<c:url value='/'/>images/btn_on_off.png" alt="열기/닫기"></a>
				<%-- <%@include file="/WEB-INF/jsp/cmm/pageTopNavi.jsp"%> --%>
            </div>
            <div class="content">
                <div class="titArea">
                    <h3 class="tit">사이트맵</h3>
                </div>            
                <!-- 상단구역 -->
                <div class="boxWrap">
					<c:forEach items="${menuList.topMenuList}" var="tval"> 
		                <div class="tbLeft20">
		      				<div class="tableType1" style="height:300px; overflow-y:scroll; overflow-x:hidden">
			    				<ul>
			    					<ul>
				                        <li><b>${tval.PGM_MENU_NM_KO}</b></li>
				                        <li><br></li>
				                    </ul>
				                    <ul>
										<c:forEach items="${menuList.leftMenuMap}" var="lvals">
											<c:if test="${lvals.key == tval.PGM_MENU_ID}">
												<c:forEach items="${lvals.value}" var="lval">
											        <c:if test="${lval.LVL==3}">


														<c:choose>
															<c:when test="${lval.BBS_CHK == 'BBS' && lval.PGM_MENU_VISIBLE_YN == 'Y'}">
																<c:if test="${lval.NEW_WDW_YN == 'Y'}">
																	<li><a href="javascript:$.openMenuCenter('<c:url value='/'/>${lval.PGM_URL}&top=${tval.PGM_MENU_ID}&left=${lval.PGM_MENU_ID}','', ${lval.NEW_WIN_WIDTH}, ${lval.NEW_WIN_HEIGHT})">${lval.PGM_MENU_NM_KO}</a></li>
																</c:if>
																<c:if test="${lval.NEW_WDW_YN == 'N'}">
																	<li><a href="<c:url value='/'/>${lval.PGM_URL}&top=${tval.PGM_MENU_ID}&left=${lval.PGM_MENU_ID}">${lval.PGM_MENU_NM_KO}</a></li>
																</c:if>																
															</c:when>
															<c:otherwise>
																<c:if test="${lval.NEW_WDW_YN == 'Y'}">
																	<li><a href="javascript:$.openMenuCenter('<c:url value='/'/>${lval.PGM_URL}?top=${tval.PGM_MENU_ID}&left=${lval.PGM_MENU_ID}','', ${lval.NEW_WIN_WIDTH}, ${lval.NEW_WIN_HEIGHT})">${lval.PGM_MENU_NM_KO}</a></li>
																</c:if>
																<c:if test="${lval.NEW_WDW_YN == 'N'}">
																	<li><a href="<c:url value='/'/>${lval.PGM_URL}?top=${tval.PGM_MENU_ID}&left=${lval.PGM_MENU_ID}">${lval.PGM_MENU_NM_KO}</a></li>
																</c:if>	
															</c:otherwise>
														</c:choose>										                        
						                        										                        
								                        
							                        </c:if>									        
										        </c:forEach>
										    </c:if>										
									    </c:forEach>									     
				                    </ul>
			    				</ul>
				            </div>	                
		                </div>							
				    </c:forEach>


      
            	</div>
        	</div>
       	<!-- //content -->
   		</div>
    <!-- //container -->
	</div> 
</div>
<!-- footer -->
<!-- <div id="footwrap">
    <div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->
</body>
</html> 