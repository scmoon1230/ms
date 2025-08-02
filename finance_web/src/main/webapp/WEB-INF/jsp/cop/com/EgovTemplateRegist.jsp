<%--
  Class Name : EgovTemplateRegist.jsp
  Description : 템플릿 속성 등록화면
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
<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="templateInf" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
    function fn_egov_regist_tmplatInfo(){
        if (confirm('<spring:message code="common.regist.msg" />')) {
            document.templateInf.action = "<c:url value='/wrks/cop/com/insertTemplateInf.do'/>";
            document.templateInf.submit();
        }
    }
    
    function fn_egov_select_tmplatInfo(){
        document.templateInf.action = "<c:url value='/wrks/cop/com/selectTemplateInfs.do'/>";
        document.templateInf.submit();  
    }

    function fn_egov_selectTmplatType(obj){
        if (obj.value == 'TMPT01') {
            document.getElementById('sometext').innerHTML = "게시판 템플릿은 CSS만 가능합니다.";
        } else if (obj.value == '') {
            document.getElementById('sometext').innerHTML = "";
        } else {
            document.getElementById('sometext').innerHTML = "템플릿은 JSP만 가능합니다.";
        }       
    }

    function fn_egov_previewTmplat() {
        var frm = document.templateInf;

        var url = frm.tmplatCours.value + "&child=" + frm.child.value + "&top=" + frm.top.value + "&left=" + frm.left.value;
        var target = "";

        if (frm.tmplatSeCode.value == 'BBS') {
            target = "<c:url value='/wrks/cop/bbs/previewBoardList.do'/>";
            width = "1024";
        } else {
            alert('<spring:message code="cop.tmplatCours" /> 지정 후 선택해 주세요.');
        }

        if (target != "") {
            window.open(target + "?searchWrd="+url, "preview", "width=" + width + "px, height=500px;");
        }
    }
</script>
<title>템플릿 등록</title>

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
			<div class="content">
				<div class="titArea"><h3 class="tit">템플릿 등록</h3></div>
				<!-- table add start -->
				<div class="tableTypeWide">
                <form:form commandName="templateInf" name="templateInf" method="post" >
                    <input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>" />
                    	
                    <input type="hidden" name="child" value="<c:out value='${child}'/>" />
                    <input type="hidden" name="top" value="<c:out value='${top}'/>" />
                    <input type="hidden" name="left" value="<c:out value='${left}'/>" />

                    <table >
                      <tr> 
                        <th width="20%" height="23" class="required_text" nowrap >
                            <label for="tmplatNm">
                                <spring:message code="cop.tmplatNm" />
                            </label>    
                        </th>
                        <td width="80%" nowrap="nowrap">
                          <input name="tmplatNm" type="text" size="60" value="" maxlength="60" style="width:100%" id="tmplatNm"  title="템플릿명">
                          <br/><form:errors path="tmplatNm" /> 
                        </td>
                      </tr>
                      
                      <tr> 
                        <th height="23" class="required_text" >
                            <label for="tmplatSeCode">  
                                <spring:message code="cop.tmplatSeCode" />
                            </label>    
                        </th>
                        <td>
                         <select name="tmplatSeCode" class="select" onchange="fn_egov_selectTmplatType(this)" id="tmplatSeCode" title="템플릿구분">
                               <option selected value=''>--선택하세요--</option>
                            <c:forEach var="result" items="${resultList}" varStatus="status">
                                <option value='<c:out value="${result.CD_ID}"/>'><c:out value="${result.CD_NM_KO}"/></option>
                            </c:forEach>    
                        </select>
                        &nbsp;&nbsp;&nbsp;<span id="sometext"></span>
                           <br/><form:errors path="tmplatSeCode" />
                        </td>
                      </tr> 
                      <tr> 
                        <th width="20%" height="23" class="required_text" nowrap >
                            <label for="tmplatCours">   
                                <spring:message code="cop.tmplatCours" />
                            </label>    
                        </th>
                        <td width="80%" nowrap="nowrap">
                          <input name="tmplatCours" type="text" size="60" value="" maxlength="60" style="width:100%" id="tmplatCours"  title="템플릿경로">
                          <br/><form:errors path="tmplatCours" /> 
                        </td>
                      </tr>
                      <tr> 
                        <th width="20%" height="23" class="required_text" nowrap >
                            <label for="useAt"> 
                                <spring:message code="cop.useAt" />
                            </label>    
                        </th>
                        <td width="80%" nowrap="nowrap">
                            Y : <input type="radio" name="useAt" class="radio2" value="Y"  checked>&nbsp;
                            N : <input type="radio" name="useAt" class="radio2" value="N">
                            <br/><form:errors path="useAt" />
                        </td>
                      </tr>  
                    </table>
                </form:form>
				</div>
				</br></br> 
				<div class="btnWrap btnR">
					<a href="#" onclick="fn_egov_regist_tmplatInfo();" class="btn btnDt btnRgt">저장</a>
					<a href="#" onclick="fn_egov_select_tmplatInfo();" class="btn btnDt btnRgt">목록</a>
					<a href="#" onclick="fn_egov_previewTmplat();" class="btn btnDt btnRgt">미리보기</a>
				</div>
			<!-- //페이지 네비게이션 끝 -->  
			</div>
		</div>
		<!-- contentWrap End -->
	</div>
	<!-- container End -->
</div>

<!-- //전체 레이어 끝 -->
</body>
</html>

