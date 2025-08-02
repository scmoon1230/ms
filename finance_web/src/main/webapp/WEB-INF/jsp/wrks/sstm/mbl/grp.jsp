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
        url: '<c:url value='/'/>wrks/sstm/mbl/grp/list.json',
        datatype: "json",
        postData: { 
        	grpId : $("#sGrpId").val(),
        	useTyCd : $("#sUseTyCd").val(),
        },
        colNames: [
                    	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
                        'No',
                        '그룹명',
                        '모바일번호',
                        '사용자명',
                        '권한레벨',                        
                        '사용유형명',
                        '그룹아이디',
                        '모바일아이디',
                        '지구명', 
                        '사용유형', 
                        '등록자아이디',
                        '등록일자', 
                        '수정자아이디',
                        '수정일자'                        
                       
                   ],
        colModel: [
                    { name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{ name: 'RK',   		width:100,  align:'center', sortable: false},
                  	{ name: 'GRP_NM_KO',   width:200,  align:'center'},
                  	{ name: 'MOBL_NO',   	width:200,  align:'center'},
                  	{ name: 'USER_NM',   	width:200,  align:'center'},
                  	{ name: 'AUTH_LVL',    width:100,  align:'center'},                  	
					{ name: 'USE_TY_NM',   width:100,  align:'center'},
                  	{ name: 'GRP_ID',      width:100,  align:'center', 'hidden':true},
                  	{ name: 'MOBL_ID',   	width:100,  align:'center', 'hidden':true},
                  	{ name: 'DSTRT_NM',   	width:300,  align:'center', 'hidden':true},
					{ name: 'USE_TY_CD',   width:100,  align:'center', 'hidden':true},
					{ name: 'RGS_USER_ID', width:100,  align:'center', 'hidden':true},
					{ name: 'RGS_DATE',    width:100,  align:'center', 'hidden':true},
					{ name: 'UPD_USER_ID', width:100,  align:'center', 'hidden':true},
					{ name: 'UPD_DATE',    width:100,  align:'center', 'hidden':true}
          ],
          
          
        pager: '#pager',
        rowNum: $('#rowPerPageList').val(),
        sortname: 'MOBL_ID',
        sortorder: 'DESC',
        viewrecords: true,
		multiselect: false,
		loadonce:false,		 
        jsonReader: {
        	id: "RK",
        	root: function(obj) { return obj.rows; },
        	page: function(obj) { return 1; },
        	total: function(obj) {
        		if(obj.rows.length > 0) {
        			var page  = obj.rows[0].ROWCNT / rowNum;
        			if( (obj.rows[0].ROWCNT % rowNum) > 0)
        				page++;
        			return page;
        		}
        		else
        			return 1; 
        	},
        	records: function(obj) { return $.showCount(obj); }
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);

			$("#dMoblId").val(list.MOBL_ID);
			$("#dGrpIdk").val(list.GRP_ID);
			$("#dMoblNo").html(list.MOBL_NO);
			$("#dGrpNmKo").html(list.GRP_NM_KO);
			$("#dDstrtNm").html(list.DSTRT_NM);
			$("#dAuthLvl").html(list.AUTH_LVL);
			$("#dUseTyCd").html(list.USE_TY_CD);
			$("#dUseTyNm").html(list.USE_TY_NM);
			$("#dRgsUserId").html(list.RGS_USER_ID);
			$("#dRgsDate").html(list.RGS_DATE);
			$("#dUpdUserId").html(list.UPD_USER_ID);
			$("#dUpdDate").html(list.UPD_DATE);
			
			
			$.selectBarun("#iMoblId", list.MOBL_ID);
			$.selectBarun("#iGrpId", list.GRP_ID);
			//$("#iMoblNo").val(list.MOBL_NO);
			//$("#iGrpNmKo").val(list.GRP_NM_KO);
			//$("#iDstrtNm").val(list.DSTRT_NM);
			$("#iAuthLvl").val(list.AUTH_LVL);
			$.selectBarun("#iUseTyCd", list.USE_TY_CD);
			//$("#iUseTyNm").val(list.USE_TY_NM);
			$("#iRgsUserId").val(list.RGS_USER_ID);
			$("#iRgsDate").val(list.RGS_DATE);
			$("#iUpdUserId").val(list.UPD_USER_ID);
			$("#iUpdDate").val(list.UPD_DATE);
			$("#moblIdBak").val(list.MOBL_ID);
			$("#grpIdBak").val(list.GRP_ID);

			
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
    	myPostData.grpId = $("#sGrpId").val();
    	myPostData.useTyCd = $("#sUseTyCd").val();
    	
    	$("#grid").trigger("reloadGrid");
    });      
    
    
});

function resetAction() {

	$("#iMoblId").get(0).selectedIndex = 0;
	$("#iGrpId").get(0).selectedIndex = 0;
	//$("#iMoblNo").val("");
	//$("#iGrpNmKo").val("");
	//$("#iDstrtNm").val("");
	$("#iAuthLvl").val("");
	$("#iUseTyCd").get(0).selectedIndex = 0;
	//$("#iUseTyNm").val("");
	$("#iRgsUserId").val("");
	$("#iRgsDate").val("");
	$("#iUpdUserId").val("");
	$("#iUpdDate").val("");
	$("#moblIdBak").val("");
	$("#grpIdBak").val("");
	
}




function updateAction(obj) {
    var url = "<c:url value='/'/>wrks/sstm/mbl/grp/update.json";  
    var params = "";
    	params += "&grpId=" + encodeURIComponent($("#iGrpId").val());
        params += "&moblId=" + encodeURIComponent($("#iMoblId").val());  
        params += "&authLvl=" + encodeURIComponent($("#iAuthLvl").val());  
        params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());  
        params += "&updUserId=" + encodeURIComponent($("#iUpdUserId").val());
    	params += "&grpIdBak=" + encodeURIComponent($("#grpIdBak").val());
        params += "&moblIdBak=" + encodeURIComponent($("#moblIdBak").val());        
        
        
    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").trigger("reloadGrid");
        	//alert("업데이트하였습니다.");
        	alert(data.msg);
        },   
        error:function(e){  
            //alert(e.responseText);
        	alert(data.msg);
        }  
    });
}

function insertAction(obj) {
    var url = "<c:url value='/'/>wrks/sstm/mbl/grp/insert.json";  
    var params = "";
    	params += "&grpId=" + encodeURIComponent($("#iGrpId").val());
        params += "&moblId=" + encodeURIComponent($("#iMoblId").val());  
        params += "&authLvl=" + encodeURIComponent($("#iAuthLvl").val());  
        params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());  
        params += "&rgsUserId=" + encodeURIComponent($("#iRgsUserId").val());
        
        
        
    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){
        	$("#grid").trigger("reloadGrid");
        	
    		alert(data.msg);
/*     		
        	if(data.session){
        		alert("저장하였습니다.");
        	}else{
        		alert("중복된 데이타 입니다.");
        	}
        	 */
        },   
        error:function(e){  
            //alert(e.responseText);
        	alert(data.msg);
        }  
    });
}

function deleteAction(obj) {
    var url = "<c:url value='/'/>wrks/sstm/mbl/grp/delete.json";  
    
    var params = "";
    params += "&grpIdBak=" + $("#grpIdBak").val();
    params += "&moblIdBak=" + $("#moblIdBak").val();      
    
    
    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

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
    var url = "<c:url value='/'/>wrks/sstm/mbl/grp/deleteMulti.json";  
    var params = "";

    for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
	    params += "&grpId=" + list.GRP_ID;
	    params += "&moblId=" + list.MOBL_ID;   	    
	    
    }

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

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
                    <h3 class="tit">모바일그룹관리</h3>
                </div>
                <div class="tableType2 seachT">
                    <table>
                        <caption>사용자 등록</caption>
                        <colgroup>
                            <col style="width: 150px;" />
                            <col style="width: *" />
                            <col style="width: 150px;" />
                            <col style="width: *" />
                            <col style="width: 150px;" />
                            <col style="width: *" />
                        </colgroup>
                        <tbody>
                        <tr>
	                        <th>그룹명</th>
	                        <td>
							    <select name="sGrpId" id="sGrpId" class="selectType1">
								    <option value="">전체</option>
								    <c:forEach items="${grpList}" var="val"> 
								        <option value="${val.GRP_ID}"><c:out value="${val.GRP_NM_KO}" ></c:out></option>   
								    </c:forEach>
								</select>
							</td>
							<th>사용유형</th>
	                        <td>
							    <select name="sUseTyCd" id="sUseTyCd" class="selectType1">
								    <%--<option value="">전체</option>--%>
								    <c:forEach items="${useGrpList}" var="val"> 
								        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>   
								    </c:forEach>
								</select>
								<a href="javascript:;" class="btn btnRight btnS">검색</a>
							</td>						
                        </tr>
                        </tbody>
                    </table>
                </div>
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


	        <!-- 레이어팝업 등록 -->
 	        
            <div class="layer layerDetail" id="div_drag_1">
                <div class="tit"><h4>그룹정보 상세</h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                    	<input type="hidden" id="moblIdBak" />
                    	<input type="hidden" id="grpIdBak" />
                        <table>
                            <caption>그룹정보 상세</caption>
                            <tbody>
                            <tr>
                                <th>모바일그룹</th>
                                <td id="dGrpNmKo"></td>
                                <th>모바일번호</th>
                                <td id="dMoblNo"></td>
                            </tr>                            
                            <tr>
                                <th>권한레벨</th>
                                <td id="dAuthLvl"></td>
                                <th>사용유형</th>
                                <td id="dUseTyNm"></td>
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
             
            <!-- //레이어팝업 등록 -->
            <!-- //레이어팝업 등록 -->
             
            <div class="layer layerRegister" id="div_drag_2">
                <div class="tit"><h4>그룹정보 <span id="modetitle">추가</span></h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                        <table>
                            <caption>그룹정보 등록</caption>
                            
                            <tbody>
                            <tr>
                                <th>모바일 그룹</th>
                                <td>
                                	<select name="" id="iGrpId" class="selectType1">
									    <c:forEach items="${grpList}" var="val"> 
									        <option value="${val.GRP_ID}"><c:out value="${val.GRP_NM_KO}" ></c:out></option>   
									    </c:forEach>                     
									</select>
                                </td>
                                <th>모바일 번호</th>
                                <td>
                                	<select name="" id="iMoblId" class="selectType1">
									    <c:forEach items="${moblNoList}" var="val"> 
									        <option value="${val.MOBL_ID}"><c:out value="${val.MOBL_NO} [${val.USER_NM}]" ></c:out></option>   
									    </c:forEach>        
									</select>
								</td>
                            </tr>                            
                            <tr>
                                <th>권한 레벨</th>
                                <td><input type="text" name="" id="iAuthLvl" class="txtType" maxlength="2"></td>
                                <th>사용유형</th>
                                <td>
                                	<select name="" id="iUseTyCd" class="selectType1">
									    <c:forEach items="${useGrpList}" var="val"> 
									        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>   
									        <%-- <option value="${val.CD_ID}" ${val.CD_ID == list.MOBL_OS_TY_CD ? 'selected' : ''}><c:out value="${val.CD_NM_KO}" ></c:out></option> --%>
									    </c:forEach>                     
									</select>
								</td>
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
