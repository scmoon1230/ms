<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

	$.jqGrid($('#grid'), {
        url: '<c:url value='/'/>wrks/sstm/grp/role/list.json',
        datatype: "json",
        postData: {
        	roleNm : $("#roleNm").val()
        },
        colNames: [
						'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
                        '롤 ID',
                        '롤 명',
                        '롤 코드',
                        '롤 타입',
                        '롤 Sort',
                        '롤 패턴',
                        '등록자',
                        '등록일자',
                        '수정자',
                        '수정일'
                   ],
        colModel: [
				{ name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
				{ name: 'ROLE_ID', width:200, align:'center'},
				{ name: 'ROLE_NM', width:230, align:'center'},
				{ name: 'ROLE_TY_CD', width:175, align:'center', hidden:true},
				{ name: 'ROLE_TY', width:160, align:'center'},
				{ name: 'SORT_ORDR', width:160, align:'center'},
				{ name: 'ROLE_PTTRN', width:175, align:'center', hidden:true},
				{ name: 'RGS_USER_ID', width:175, align:'center', hidden:true},
				{ name: 'RGS_DATE', width:180, align:'center'},
				{ name: 'UPD_USER_ID', width:175, align:'center', hidden:true},
				{ name: 'UPD_DATE', width:175, align:'center', hidden:true}
				
          ],
        pager: '#pager',
        rowNum: $('#rowPerPageList').val(),
        sortname: 'ROLE_NM',
        sortorder: 'ASC',
        viewrecords: true,
        multiselect: false,
        loadonce:false,
        jsonReader: {
        	id: "ROLE_ID",
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
        onSelectRow: function(rowid, status, e) {
			var list = jQuery("#grid").getRowData(rowid);
			
			$("#dRoleId").html(list.ROLE_ID);
			$("#dRoleNm").html(list.ROLE_NM);
			$("#dRolePttrn").html(list.ROLE_PTTRN);
			$("#dRoleTy").html(list.ROLE_TY);
			$("#dSortOrdr").html(list.SORT_ORDR);
			$("#dRgsUserId").html(list.RGS_USER_ID);
			$("#dRgsDate").html(list.RGS_DATE);
			$("#dUpdUserId").html(list.UPD_USER_ID);
			$("#dUpdDate").html(list.UPD_DATE);
			
			$("#iRoleId").val(list.ROLE_ID);
			$("#iRoleNm").val(list.ROLE_NM);
			$("#iRolePttrn").val(list.ROLE_PTTRN);
			$.selectBarun("#iRoleTyCd", list.ROLE_TY_CD);
			$("#iSortOrdr").val(list.SORT_ORDR);
			
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
    	
    	//검색할 조건의 값을 설정한다.
    	myPostData.roleNm = $("#roleNm").val();
    	
    	$("#grid").trigger("reloadGrid");
    });
});

function resetAction() {
	//alert("resetAction");
	
	$("#iRoleId").val("");
	$("#iRoleNm").val("");
	$("#iRolePttrn").val("");
	$("#iRoleTyCd").get(0).selectedIndex=0;
	$("#iSortOrdr").val("");
}

function updateAction(obj) {
	//alert('updateAction');
	
    var url = "<c:url value='/'/>wrks/sstm/grp/role/update.json";
    var params = "roleId=" + escape(encodeURIComponent($("#iRoleId").val()));
   		params += "&roleNm=" + escape(encodeURIComponent($("#iRoleNm").val()));
        params += "&rolePttrn=" + escape(encodeURIComponent($("#iRolePttrn").val()));
        params += "&roleTyCd=" + escape(encodeURIComponent($("#iRoleTyCd").val()));
        params += "&sortOrdr=" + escape(encodeURIComponent($("#iSortOrdr").val()));


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
            alert(e.responseText);  
        }  
    });
}

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function insertAction(obj) {
	//alert('insertAction');
	var url = "<c:url value='/'/>wrks/sstm/grp/role/insert.json";
	 var params = "roleId=" + escape(encodeURIComponent($("#iRoleId").val()));
		 params += "&roleNm=" + escape(encodeURIComponent($("#iRoleNm").val()));
	     params += "&rolePttrn=" + escape(encodeURIComponent($("#iRolePttrn").val()));
	     params += "&roleTyCd=" + escape(encodeURIComponent($("#iRoleTyCd").val()));
	     params += "&sortOrdr=" + escape(encodeURIComponent($("#iSortOrdr").val()));
	    

	    
    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	if(data.session == 1){
	        	$("#grid").trigger("reloadGrid");
            	alert(data.msg);
            	
                $.hideInsertForm();
                
        	}else{
        		alert(data.msg);
        	} 

        },   
        error:function(e){  
            alert(e.responseText);  
        }  
    });
}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = "<c:url value='/'/>wrks/sstm/grp/role/delete.json";  
    //var params = "cdGrpId=" + $("#dCdGrpId").text();  
    var params = "roleId=" + $("#iRoleId").val();

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

function deleteMultiAction(obj) {
	//alert('deleteMultiAction');

	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
    var url = "<c:url value='/'/>wrks/sstm/grp/role/deleteMulti.json";  
    var params = "";  
	//alert(s.length);
    for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		
	    params += "&roleId=" + list.ROLE_ID;
    }
    //alert(params);

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
                    <h3 class="tit">롤관리</h3>
                </div>
                <div class="tableType2 seachT">
                    <table>
                        <caption>롤관리</caption>
                        <tbody>
                        <tr>
	                        <th>롤 명</th>
	                        <td>
	                        	<input type="text" name="" id="roleNm" class="txtType searchEvt" style="ime-mode:active">
	                        	<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
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
            
            <!-- 레이어팝업 상세 -->
            <div class="layer layerDetail" id="div_drag_1">
                <div class="tit"><h4>롤 상세</h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                        <table>
                            <caption>롤 상세</caption>
                            <tbody>
                            <tr>
                                <th>롤 코드</th>
                                <td id="dRoleId"></td>
                                <th>롤 명</th>
                                <td id="dRoleNm"></td>
                            </tr>
                            <tr>
                                <th>롤패턴</th>
                                <td id="dRolePttrn" colspan="3"></td>
                            </tr>
                            <tr>
                                <th>롤 타입</th>
                                <td id="dRoleTy"></td>
                                <th>롤 Sort</th>
                                <td id="dSortOrdr"></td>
                            </tr>
                            <tr>
                                <th>등록일자</th>
                                <td id="dRgsDate" colspan="3"></td>
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
            
            <!-- 레이어팝업 등록 -->
            <div class="layer layerRegister" id="div_drag_2">
                <div class="tit"><h4>롤 <span id="modetitle">추가</span></h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                        <table>
                            <caption>롤 등록</caption>
                            <tbody>
                            <tr>
                                <th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>롤 코드</th>
                                <td><input type="text" id="iRoleId" class="txtType" maxlength="40" required="required" user-required="insert"  style="ime-mode:inactive" user-title="롤 코드"/></td>
                           
                                <th>롤 명</th>
                                <td><input type="text" id="iRoleNm" class="txtType" maxlength="100" user-title="롤 명" /></td>
                            </tr>
                            <tr>
                                <th>롤 패턴</th>
                                <td colspan="3"><input type="text" id="iRolePttrn" class="txtType" maxlength="300" user-title="롤 패턴" /></td>
                            </tr>
                           
                            <tr>
                            	<th>롤 타입</th>
                            	<td>
                                	<select name="" id="iRoleTyCd" class="selectType1" >
									    <c:forEach items="${useGrpList}" var="val"> 
									        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>   
									    </c:forEach>                     
									</select>
                                </td>
                                <th>롤 Sort</th>
                                <td><input type="text" id="iSortOrdr" class="txtType number" maxlength="10" user-title="롤 Sort" /></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                   <div class="btnCtr">
                        <a href="#" class="btn btnSvEc">저장</a>
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
