<%--
  Class Name : EgovBoardMstrRegist.jsp
  Description : 게시판 생성 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.12   이삼섭              최초 생성
     2009.06.26   한성곤          2단계 기능 추가 (댓글관리, 만족도조사)
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
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<link href="<c:url value='/button.css' />" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<c:url value='/js/egov/EgovBBSMng.js' />"></script>
<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="boardMaster" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="<c:url value='/js/egov/showModalDialog.js'/>" ></script>
<script type="text/javascript">
    function fn_egov_regist_brdMstr(){
        if (confirm('<spring:message code="common.regist.msg" />')) {
            form = document.boardMaster;
            form.action = "<c:url value='/wrks/cop/bbs/insertBBSMasterInf.do'/>";
            form.submit();
        }
    }
    
    function fn_egov_select_brdMstrList(){
        form = document.boardMaster;
        form.action = "<c:url value='/wrks/cop/bbs/SelectBBSMasterInfs.do'/>";
        form.submit();  
    }
    
    function fn_egov_inqire_tmplatInqire(){
        form = document.boardMaster;
        var retVal;
        //var url = "<c:url value='/wrks/cop/com/openPopup.do?requestUrl=/wrks/cop/com/selectTemplateInfsPop.do&typeFlag=BBS&width=850&height=360'/>";
        var url = "<c:url value='/' />wrks/cop/com/openPopup.do?requestUrl=/wrks/cop/com/selectTemplateInfsPop.do&typeFlag=BBS&width=850&height=360&child=" + form.child.value + "&top=" + form.top.value + "&left=" + form.left.value;
        var openParam = "dialogWidth: 890px; dialogHeight: 400px; resizable: 0, scroll: 1, center: 1";
         
        retVal = window.showModalDialog(url,"p_tmplatInqire", openParam);
        if(retVal != null){
            var tmp = retVal.split("|");
            form.tmplatId.value = tmp[0];
            form.tmplatNm.value = tmp[1];
        }
    }
    
    function showModalDialogCallback(retVal) {
    	if(retVal != null){
            var tmp = retVal.split("|");
            form.tmplatId.value = tmp[0];
            form.tmplatNm.value = tmp[1];
        }
    }
    
</script>

<title>게시판 생성</title>

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
			<div class="content">
				<div class="titArea"><h3 class="tit">게시판 생성</h3></div>
				<!-- table add start -->
				<div class="tableTypeWide">
                <form:form commandName="boardMaster" name="boardMaster" method="post" action="cop/bbs/SelectBBSMasterInfs.do">
					<!-- 전자정부 패키지를 사용하기 위하여 메뉴에 대한 정보를 설정 -->
					<input type="hidden" name="child" value="<c:out value='${child}'/>" />
					<input type="hidden" name="top" value="<c:out value='${top}'/>" />
					<input type="hidden" name="left" value="<c:out value='${left}'/>" />

                    <input type="hidden" name="pageIndex"  value="<c:out value='${searchVO.pageIndex}'/>"/>

                        <table summary="게시판명,게시판소개,게시판 유형,게시판 속성,답장가능여부,파일첨부가능여부, ..  입니다">
                            <tr>
                                <th width="20%" height="23" class="required_text" nowrap >
                                    <label for="bbsNm"> 
                                        <spring:message code="cop.bbsNm" />
                                    </label>    
                                </th>
                                <td width="80%" nowrap colspan="3">
                                    <form:input title="게시판명입력" path="bbsNm" size="60" cssStyle="width:100%" />
                                    <br/><form:errors path="bbsNm" />
                                </td>
                          </tr>
                          <tr> 
                            <th height="23" class="required_text" >
                                <label for="bbsIntrcn">
                                    <spring:message code="cop.bbsIntrcn" />
                                </label>    
                            </th>
                            <td colspan="3">
                               <form:textarea title="게시판소개입력" path="bbsIntrcn" cols="75" rows="4" cssStyle="width:100%" />
                               <br/><form:errors path="bbsIntrcn" />
                            </td>
                          </tr>
                          <tr> 
                            <th width="20%" height="23" class="required_text" nowrap >
                                <label for="bbsTyCode">
                                    <spring:message code="cop.bbsTyCode" />
                                </label>    
                            </th>
                            <td width="30%" nowrap="nowrap">

								<select name="bbsTyCode" class="select" id="bbsTyCode" title="게시판유형선택">
									<option selected value=''>선택하세요</option>
									<c:forEach var="result" items="${typeList}" varStatus="status">
									<option value='<c:out value="${result.CD_ID}"/>'><c:out value="${result.CD_NM_KO}"/></option>
									</c:forEach>
								</select>

                            </td>
                            
                            <th width="20%" height="23" class="required_text" nowrap >
                                <label for="bbsAttrbCode">  
                                    <spring:message code="cop.bbsAttrbCode" />
                                </label>    
                            </th>    
                            <td width="30%" nowrap="nowrap">

								<select name="bbsAttrbCode" class="select" id="bbsAttrbCode" title="게시판속성선택">
									<option selected value=''>선택하세요</option>
									<c:forEach var="result" items="${attrbList}" varStatus="status">
									<option value='<c:out value="${result.CD_ID}"/>'><c:out value="${result.CD_NM_KO}"/></option>
									</c:forEach>
								</select>

                            </td>    
                          </tr> 
                          
                          <tr> 
                            <th width="20%" height="23" class="required_text" >
                                <label for="replyPosblAt">
                                    <spring:message code="cop.replyPosblAt" />
                                </label>    
                            </th>
                            <td width="30%" nowrap="nowrap">
                                <spring:message code="button.possible" /> : <form:radiobutton path="replyPosblAt"  value="Y" />&nbsp;
                                <spring:message code="button.impossible" /> : <form:radiobutton path="replyPosblAt"  value="N"  />
                                 <br/><form:errors path="replyPosblAt" />
                            </td>
                            
                            <th width="20%" height="23" class="required_text" >
                                <label for="fileAtchPosblAt">
                                    <spring:message code="cop.fileAtchPosblAt" />
                                </label>    
                            </th>    
                            <td width="30%" nowrap="nowrap">
                                <spring:message code="button.possible" /> : <form:radiobutton path="fileAtchPosblAt"  value="Y"  onclick="document.boardMaster.posblAtchFileNumber.disabled='';" />&nbsp;
                                <spring:message code="button.impossible" /> : <form:radiobutton path="fileAtchPosblAt"  value="N"  onclick="document.boardMaster.posblAtchFileNumber.disabled='disabled';" />
                                 <br/><form:errors path="fileAtchPosblAt" />
                            </td>    
                          </tr> 
                          
                          <tr> 
                            <th width="20%" height="23" class="required_text"  >
                                <label for="posblAtchFileNumber">
                                    <spring:message code="cop.posblAtchFileNumber" />
                                </label>    
                            </th>
                            <td width="30%" nowrap colspan="3" >
                                <form:select path="posblAtchFileNumber" title="첨부가능파일 숫자선택" >
                                   <form:option value="0"  label="선택하세요" />
                                   <form:option value='1'>1개</form:option>
                                   <form:option value='2'>2개</form:option>
                                   <form:option value='3'>3개</form:option>
                               </form:select>
                               <br/><form:errors path="posblAtchFileNumber" />
                            </td>
                          </tr>   
                          <tr> 
                            <th width="20%" height="23" class="required_text" >
                                <label for="tmplatNm">
                                    <spring:message code="cop.tmplatId" />
                                </label>    
                            </th>
                            <td width="30%" nowrap colspan="3">
                             <form:input path="tmplatNm" size="20" readonly="true" title="템플릿정보입력"/>
                             <form:hidden path="tmplatId"  />
                             &nbsp;<a href="#LINK" onclick="fn_egov_inqire_tmplatInqire(); return false;" style="selector-dummy: expression(this.hideFocus=false);">
                             <img src="<c:url value='/images/img_search.gif' />"
                                        width="15" height="15" align="middle" alt="새창" /></a>
                             <br/><form:errors path="tmplatId" />
                            </td>
                          </tr>
                            <!-- 2009.06.26 : 2단계 기능 추가 -->
                            <c:if test="${addedOptions == 'true'}">
                    
                              <tr> 
                                <th width="20%" height="23" class=""><label for="option">추가 선택사항</label></th>
                                <td width="30%" nowrap colspan="3" >
                                    <form:select path="option" title="추가선택사항선택" >
                                       <form:option value=""  label="미선택" />
                                       <form:option value='comment'>댓글</form:option>
                                       <form:option value='stsfdg'>만족도조사</form:option>
                                   </form:select>
                                </td>
                              </tr>          
                    
                            </c:if>
                            <!-- 2009.06.26 : 2단계 기능 추가 -->       
                        </table>
                        </form:form>
				</div>
				</br></br> 
				<div class="btnWrap btnR">
					<a href="#" onclick="fn_egov_regist_brdMstr();" class="btn btnDt btnRgt">저장</a>
					<a href="#" onclick="fn_egov_select_brdMstrList();" class="btn btnDt btnRgt">목록</a>
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

