<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">
$(document).ready(function(){

    $.jqGrid($('#grid'), {
        url: '<c:url value='/'/>wrks/sstm/mbl/version/list.json',
        datatype: "json",
        postData: { 
        	moblOs : $("#sMoblOs").val(),
        	moblKndCd : $("#sMoblKndCd").val(),
        	useTyCd : $("#sUseTyCd").val()
        },	
        colNames: [
                   		'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
						'No',
                    	'모바일기기앱아이디',
                    	'모바일기기종류코드',
                        '모바일기기종류',
                    	'모바일OS종류코드',                        
                        '모바일OS종류',
/*                      	'모바일기기특성유형코드',
                        '모바일기기특성유형',
                    	'모바일기기통신유형코드',
                        '모바일기기통신유형', */ 
                        '앱버전',
                        '다운로드 URL',
                        '사용유무코드',
                        '사용유무'
                       
                   ],
        colModel: [
				  { name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
				  { name: 'RK', width:100,  align:'center', sortable: false},
				  { name: 'MOBL_APP_ID', width:100, align:'center', 'hidden':true},
                  { name: 'MOBL_KND_CD', width:100, align:'center', 'hidden':true},
                  { name: 'MOBL_KND_NM', width:220, align:'center'},
                  { name: 'MOBL_OS_TY_CD', width:100, align:'center', 'hidden':true},
                  { name: 'MOBL_OS_TY_NM', width:200, align:'center'},
/*                   { name: 'MOBL_CHARTR_TY_CD'	, width:100,  align:'center'	, 'hidden':true	},
                  { name: 'MOBL_CHARTR_TY_NM'	, width:100,  align:'center'	, 'hidden':true	},
                  { name: 'MOBL_COMM_TY_CD'	, width:100,  align:'center'	, 'hidden':true	},
                  { name: 'MOBL_COMM_TY_NM'	, width:100,  align:'center'	, 'hidden':true	}, */ 
                  { name: 'APP_VER_NO', width:210, align:'center'},
                  { name: 'APP_DWLD_URL', width:100, align:'center'	, 'hidden':true},
                  { name: 'USE_TY_CD', width:100, align:'center', 'hidden':true},
                  { name: 'USE_TY_NM', width:200, align:'center'}

          ],
          
		 pager: '#pager',
		 rowNum: $('#rowPerPageList').val(),
		 sortname: 'MOBL_APP_ID',
		 sortorder: 'DESC',
		 viewrecords: true,
	     multiselect: false,
	     loadonce:false,		 
		 jsonReader: {
		 	id: "MOBL_APP_ID",
		 	root: function(obj) { return obj.rows; },
		 	page: function(obj) { return 1; },
		 	total: function(obj) {
		 		if(obj.rows.length > 0) {
		 			var page  = obj.rows[0].ROWCNT / rowNum;
		 			if( (obj.rows[0].ROWCNT % rowNum) > 0)
		 				page++;
		 			return page;
		 		} 	else
		 			return 1; 
		 	},
		 	records: function(obj) { return $.showCount(obj); }
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;
			
			var list = jQuery("#grid").getRowData(rowid);
			
			
			$("#dMoblKndNm").html(list.MOBL_KND_NM);
			$("#dMoblOsTyNm").html(list.MOBL_OS_TY_NM);
			$("#dMoblChartrTyNm").html(list.MOBL_CHARTR_TY_NM);
			$("#dMoblCommTyNm").html(list.MOBL_COMM_TY_NM);
			$("#dAppVerNo").html(list.APP_VER_NO);
			$("#dAppDwldUrl").html(list.APP_DWLD_URL);
			$("#dUseTyNm").html(list.USE_TY_NM);
			$.selectBarun("#iMoblKndCd", list.MOBL_KND_CD);
			$.selectBarun("#iMoblOsTyCd", list.MOBL_OS_TY_CD);
/*  			$.selectBarun("#iMoblChartrTyCd", list.MOBL_CHARTR_TY_CD);
			$.selectBarun("#iMoblCommTyCd", list.MOBL_COMM_TY_CD); */ 
			$("#iAppVerNo").val(list.APP_VER_NO);
			$("#iAppDwldUrl").val(list.APP_DWLD_URL);
			$.selectBarun("#iUseTyCd", list.USE_TY_CD);
			
			$("#moblAppId").val(list.MOBL_APP_ID);
			
			$.showDetail();
		},
        beforeRequest: function() {
        	$.loading(true);
          	rowNum = $('#rowPerPageList').val();
		},
		beforeProcessing: function(data, status, xhr){
        	$.loading(false);
          	if(typeof data.rows != "undefine" || data.row != null) {
          		$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
          	}
        }
 
  });
    
    $(".btnS").bind("click",function(){
    	$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
    	var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
    	myPostData.moblOs = $("#sMoblOs").val();
    	myPostData.moblKndCd = $("#sMoblKndCd").val();
    	myPostData.useTyCd = $("#sUseTyCd").val();
    	$("#grid").trigger("reloadGrid");
    });    
    

});


function resetAction() {
	
	$("#moblAppId").val("");
	$("#iMoblKndCd").get(0).selectedIndex = 0;
	$("#iMoblOsTyCd").get(0).selectedIndex = 0;
/*  	$("#iMoblChartrTyCd").get(0).selectedIndex = 0;
	$("#iMoblCommTyCd").get(0).selectedIndex = 0; */	
	$("#iAppVerNo").val("");
	$("#iAppDwldUrl").val("");
	//$.selectBarun("#iUseTyCd", "");
	$("#iUseTyCd").get(0).selectedIndex = 0;
	
}

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function updateAction(obj) {
    var url = "<c:url value='/'/>wrks/sstm/mbl/version/update.json";  
    var params = "";
    	params += "moblAppId=" + encodeURIComponent($("#moblAppId").val());
        params += "&moblKndCd=" + encodeURIComponent($("#iMoblKndCd").val());  
        params += "&moblOsTyCd=" + encodeURIComponent($("#iMoblOsTyCd").val());  
/*         params += "&moblChartrTyCd=" + $("#iMoblChartrTyCd").val();  
        params += "&moblCommTyCd=" + $("#iMoblCommTyCd").val(); */
        params += "&appVerNo=" + encodeURIComponent($("#iAppVerNo").val());
        params += "&appDwldUrl=" + encodeURIComponent($("#iAppDwldUrl").val());
        params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());        
        params += "&updUserId=" + encodeURIComponent($("#iUpdUserId").val());  

        
    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").setGridParam({page :$("#cur-page").val()});
        	$("#grid").trigger("reloadGrid");
        	//alert("저장하였습니다.");
        	alert(data.msg);
        },   
        error:function(e){  
            //alert(e.responseText);
        	alert(data.msg);
        }  
    });
}

function insertAction(obj) {
    var url = "<c:url value='/'/>wrks/sstm/mbl/version/insert.json";  

    var params = "";
        params += "&moblKndCd=" + encodeURIComponent($("#iMoblKndCd").val());  
        params += "&moblOsTyCd=" + encodeURIComponent($("#iMoblOsTyCd").val());  
/*         params += "&moblChartrTyCd=" + $("#iMoblChartrTyCd").val();  
        params += "&moblCommTyCd=" + $("#iMoblCommTyCd").val(); */
        params += "&appVerNo=" + encodeURIComponent($("#iAppVerNo").val());
        params += "&appDwldUrl=" + encodeURIComponent($("#iAppDwldUrl").val());
        params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());        
        params += "&rgsUserId=" + encodeURIComponent($("#iRgsUserId").val());  

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").trigger("reloadGrid");
        	//alert("저장하였습니다.");
        	alert(data.msg);
        },   
        error:function(e){  
            //alert(e.responseText);
        	alert(data.msg);
        }  
    });
}

function deleteAction(obj) {
    var url = "<c:url value='/'/>wrks/sstm/mbl/version/delete.json";  
    var params = "";
    	params += "moblAppId=" + $("#moblAppId").val();

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").setGridParam({page :$("#cur-page").val()});
        	$("#grid").trigger("reloadGrid");
        	//alert("삭제하였습니다.");
        	alert(data.msg);
        },   
        error:function(e){  
            //alert(e.responseText);
        	alert(data.msg);
        }  
    });
}

function deleteMultiAction() {

	//var s =  $("#grid").jqGrid('getGridParam', 'selarrrow');
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
    var url = "<c:url value='/'/>wrks/sstm/mbl/version/deleteMulti.json";  
    var params = "";

    for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
	    params += "&moblAppId=" + list.MOBL_APP_ID;
    }

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").setGridParam({page :$("#cur-page").val()});
        	$("#grid").trigger("reloadGrid");
        	//alert("삭제하였습니다.");
        	alert(data.msg);
        },   
        error:function(e){  
            //alert(e.responseText);
        	alert(data.msg);
        }  
    });    	
    
    return true;
}

</script>

</head>
<body>

<div id="wrapper">
    <!-- topbar -->
	<%@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
    <!-- //topbar -->
    <!-- container -->
    <div class="container">
        <!-- leftMenu -->
		<%@include file="/WEB-INF/jsp/cmm/leftMenu.jsp"%>
        <!-- //leftMenu -->
        
        <!-- content -->
        <div class="contentWrap">
            <div class="topArea">
                <a href="#" class="btnOpen"><img src="<c:url value='/'/>images/btn_on_off.png" alt="열기/닫기"></a>

				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi.jsp"%>

            </div>
            <div class="content">
                <div class="titArea">
                    <h3 class="tit">앱버전정보</h3>
                </div>
				<div class="tableTypeFree seachT">
                    <table>
                        <caption>사용자 등록</caption>
                        <tbody>
                        <tr>
	                        <th>모바일종류</th>
	                        <td>
							    <select name="sMoblKndCd" id="sMoblKndCd" class="selectType1">
								    <option value="">전체</option>
								    <c:forEach items="${mppList}" var="val"> 
								        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>   
								    </c:forEach>
								</select>
	                        </td>
	                        <th>모바일OS종류</th>
	                        <td>
							    <select name="" id="sMoblOs" class="selectType1">
								    <option value="">전체</option>
								    <c:forEach items="${mppOsList}" var="val"> 
								        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>   
								    </c:forEach>
								</select>
	                        </td>
                            <th>사용유형</th>
                            <td>
                            	<select name="" id="sUseTyCd" class="selectType1" maxlength="1">
						    		<c:forEach items="${useGrpList}" var="val"> 
						        		<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>   
						    		</c:forEach>                     
								</select>
								<a href="javascript:;" class="btn btnRight btnS">검색</a>
                            </td>	                        
                        </tr>
                        </tbody>
                    </table>
                </div><br/>
                <div class="searchArea">
                    <div class="page fL">
                        <span class="txt">페이지당</span>
                        <div class="selectBox">
							<select name="rowPerPageList" id="rowPerPageList" class="selectType1">
							    <c:forEach items="${rowPerPageList}" var="val"> 
							        <option value="${val.CD_ID}" ${val.CD_ID == rowPerPageSession ? 'selected' : ''}><c:out value="${val.CD_NM_KO}" ></c:out></option>   
							    </c:forEach>                     
							</select>	
                        </div>
                        <span class="totalNum">전체<em id="rowCnt"></em>건</span>
                    </div>
                </div>
                <div class="tableType1">
    				<table id="grid" style="width:100%">
    				</table>
                </div>
                <div class="paginate">
                </div>                
                <div class="btnWrap btnR">
                    <a href="#" class="btn btnDt btnRgt">등록</a>
                    <a href="#" class="btn btnMultiDe">삭제</a>
                </div>                
            </div>

	        <!-- 레이어팝업 상세 -->
 	        
            <div class="layer layerDetail" id="div_drag_1">
                <div class="tit"><h4>모바일기기 버전정보 상세</h4></div>
                <div class="layerCt">
                    <div class="tableType2">

                    	<input type="hidden" id="moblAppId" />
                    	<input type="hidden" id="dMoblKndCd" />
                    	<input type="hidden" id="dMoblOsTyCd" />
<!--                     	<input type="hidden" id="dMoblChartrTyCd" />
                    	<input type="hidden" id="dMoblCommTyCd" /> -->
                    	<input type="hidden" id="dUseTyCd" />

                        <table>
                            <caption>모바일기기 버전정보 상세</caption>
                            <tbody>
                            <tr>
                                <th>모바일기기 종류</th>
                                <td id="dMoblKndNm"></td>
                                <th>모바일기기 OS유형</th>
                                <td id="dMoblOsTyNm"></td>
                            </tr>                            
<!-- 
                            <tr>
                                <th>모바일기기 특성유형</th>
                                <td id="dMoblChartrTyNm"></td>
                                <th>모바일기기 통신유형</th>
                                <td id="dMoblCommTyNm"></td>
                            </tr>
 -->
                            <tr>
                                <th>앱버전정보</th>
                                <td id="dAppVerNo"></td>
                                <th>사용유형</th>
                                <td id="dUseTyNm"></td>
                            </tr>
                            <tr>
                                <th>앱 다운로드 URL</th>
                                <td id="dAppDwldUrl" colspan=3></td>
                            </tr>                            
                            </tbody>
                        </table>
                    </div>
                    <div class="btnCtr">
                        <a href="#" class="btn btnMd">수정</a>
                        <a href="#" class="btn btnDe">삭제</a>
                        <a href="#" class="btn btnC">취소</a>
                    </div>
                </div>
            </div>
             
            <!-- //레이어팝업 상세 -->
    
            <!-- //레이어팝업 등록 -->
             
            <div class="layer layerRegister" id="div_drag_2">
                <div class="tit"><h4>모바일기기 버전정보 <span id="modetitle">추가</span></h4></div>
                <div class="layerCt">
                    <div class="tableTypeFree tableType2">
                        <table>
                            <caption>모바일기기 버전정보 등록</caption>
                            <tbody>
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>모바일기기 종류</th>
                                <td>
                                	<select name="" id="iMoblKndCd" class="selectType1">
									    <c:forEach items="${mppList}" var="val"> 
									        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>   
									    </c:forEach>                     
									</select>
                                </td>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>모바일기기 OS유형</th>
                                <td>
                                	<select name="" id="iMoblOsTyCd" class="selectType1">
									    <c:forEach items="${mppOsList}" var="val"> 
									        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>   
									    </c:forEach>                     
									</select>
								</td>
                            </tr>                            
<%--     
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>모바일기기 특성유형</th>
                                <td>
                                	<select name="" id="iMoblChartrTyCd" class="selectType1">
									    <c:forEach items="${mppChartrList}" var="val"> 
									        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>   
									    </c:forEach>                     
									</select>
                                </td>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>모바일기기 통신유형</th>
                                <td>
                                	<select name="" id="iMoblCommTyCd" class="selectType1">
									    <c:forEach items="${mppCommList}" var="val"> 
									        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>   
									    </c:forEach>                     
									</select>
								</td>
                            </tr>                            
 --%>    
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>앱 버전번호</th>
                                <td><input type="text" name="" id="iAppVerNo" class="txtType" maxlength="5" required="required" user-title="앱 버전번호"></td>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용여부</th>
                                <td>
                                	<select name="" id="iUseTyCd" class="selectType1">
									    <c:forEach items="${useGrpList}" var="val"> 
									        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>   
									    </c:forEach>                     
									</select>
								</td>
                            </tr>
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>앱 다운로드 URL</th>
                                <td colspan=3><input type="text" name="" id="iAppDwldUrl" class="txtType txtType100" maxlength="255" required="required" user-title="앱 다운로드 URL"></td>                            

                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="btnCtr">
                        <a href="#" class="btn btnSv">저장</a>
                        <a href="#" class="btn btnC">취소</a>
                    </div>
                </div>
            </div>
             
            <!-- //레이어팝업 등록 -->

        </div>
        <!-- //content -->
    </div>
    <!-- //container -->
</div> 

<!-- footer -->
<!-- <div id="footwrap">
    <div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->   
   
</body>
</html>
