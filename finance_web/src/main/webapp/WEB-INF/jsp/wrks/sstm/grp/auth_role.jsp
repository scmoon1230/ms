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
    
	$.jqGrid('#grid_grp', {
        url: '<c:url value='/'/>wrks/sstm/grp/auth_role/list_grp.json',
        datatype: "json",
        postData: { 
        	dstrtCd : $("#sDstrtCd").val(),
        	grpNmKo : $("#sGrpNmKo").val(),
        	authLvlNm : $("#sAuthLvlNm").val(),
        	useTyCd : $("#sUseTyCd").val()
        },
        colNames: [
                        '지구명',
                        '그룹아이디',
                        '그룹명',
                        '권한레벨코드',
                        '권한레벨',
                        '사용유무'
                   ],
        colModel: [
                  	{ name: 'DSTRT_NM', width:110, align:'center'},
                  	{ name: 'GRP_ID', width:100, align:'center', hidden:true},
                  	{ name: 'GRP_NM_KO', width:150, align:'center'},
                  	{ name: 'AUTH_LVL', width:115, align:'center', hidden:true},
                  	{ name: 'AUTH_LVL_NM', width:110, align:'center'},
                  	{ name: 'USE_TY_NM', width:90, align:'center'},
          ],
          
          
        pager: '#pager',
        rowNum : 1000,
        sortname: 'GRP_NM_KO',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		loadonce:false,		 
        jsonReader: {
        },
        
        onCellSelect : function(rowid, iCol, cellcontent, e){
			var list = jQuery("#grid_grp").getRowData(rowid);
			
			$("#dGrpNmKo").html(list.GRP_NM_KO);
			$("#dAuthLvlNm").html(list.AUTH_LVL_NM);
			
			$("#grid_role").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/grp/auth_role/list_role.json"});
	    	var myPostData = $("#grid_role").jqGrid('getGridParam', 'postData');
	    	myPostData.grpId = list.GRP_ID;
	    	myPostData.authLvl = list.AUTH_LVL;
	    	$("#grid_role").trigger("reloadGrid");
			
        },
        beforeProcessing : function(data, status, xhr){
        	$("#dGrpNmKo").html("");
        	$("#dAuthLvlNm").html("");
        	$("#grid_role").clearGridData();
        }
  	});
	
	$.jqGrid('#grid_role', {
        //url: '<c:url value='/'/>wrks/sstm/grp/auth_role/list_role.json',
        datatype: "json",
        postData: { 
        	grpId : "",
        	authLvl : ""
        },
        colNames: [
						'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_role\', this, event);">',
						'롤아이디',
						'롤명',
						'롤타입',
                        '롤설명',
                        '롤 정렬순서',
                        '권한레벨',
                        '등록자',
                        '등록일',
                        '수정자',
                        '수정일'
                   ],
        colModel: [
					{ name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{ name: 'ROLE_ID', width:110, align:'center', hidden:true},
                  	{ name: 'ROLE_NM', width:100, align:'center'},
                  	{ name: 'ROLE_TY', width:100, align:'center'},
                  	{ name: 'ROLE_PTTRN', width:120, align:'center'},
                  	{ name: 'SORT_ORDR', width:50, align:'center', hidden:true},
                  	{ name: 'AUTH_LVL_NM', width:100, align:'center'},
                  	{ name: 'RGS_USER_ID', width:140, align:'center', hidden:true},
                  	{ name: 'RGS_DATE', width:140, align:'center', hidden:true},
                  	{ name: 'UPD_USER_ID', width:140, align:'center', hidden:true},
                  	{ name: 'UPD_DATE', width:140, align:'center', hidden:true}
          			],
        pager: '#pager',
        rowNum : 1000,
        sortname: 'ROLE_NM',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		loadonce:false,		 
        jsonReader: {
        	
        },
        
        onCellSelect : function(rowid, iCol, cellcontent, e){
			var list = jQuery("#grid_role").getRowData(rowid);
			
			$("#dRoleId").html(list.ROLE_ID);
			$("#dRoleNm").html(list.ROLE_NM);
			$("#dRolePttrn").html(list.ROLE_PTTRN);
			$("#dRoleTy").html(list.ROLE_TY);
			$("#dSortOrdr").html(list.SORT_ORDR);
			$("#dRgsUserId").html(list.RGS_USER_ID);
			$("#dRgsDate").html(list.RGS_DATE);
			$("#dUpdUserId").html(list.UPD_USER_ID);
			$("#dUpdDate").html(list.UPD_DATE);
			
			$.showDetail();
        }
  	});
	
	$.jqGrid('#grid_popup', {
        //url: '<c:url value='/'/>wrks/sstm/grp/auth_role/list_popup.json',
        datatype: "json",
        postData: {
        	grpId : "",
        	authLvl : ""
        },
        colNames: [
					'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_popup\', this, event);">',
                    '롤 ID',
                    '롤 명',
                    '롤 타입',
                    '롤 정렬순서',
                    '롤 설명',
        ],
	    colModel: [
				{ name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
				{ name: 'ROLE_ID', width:150, align:'center'},
				{ name: 'ROLE_NM', width:150, align:'center'},
				{ name: 'ROLE_TY', width:80, align:'center'},
				{ name: 'SORT_ORDR', width:100, align:'center'},
				{ name: 'ROLE_PTTRN', width:200, align:'center'},
	    ],
        pager: '#pager',
        rowNum : 1000,
        sortname: 'ROLE_ID',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		loadonce:false,		 
        jsonReader: {
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;
        }
         
  	});
	
	$(".btnS").bind("click",function(){
    	var myPostData = $("#grid_grp").jqGrid('getGridParam', 'postData');
    	
    	myPostData.dstrtCd = $("#sDstrtCd").val();
    	myPostData.grpNmKo = $("#sGrpNmKo").val();
    	myPostData.authLvlNm = $("#sAuthLvlNm").val();
    	myPostData.useTyCd = $("#sUseTyCd").val();
    	
    	$("#grid_grp").trigger("reloadGrid");
	});
	
	$(".btnRgt1").bind("click",function(){
	    	
    	if($.getCurrentRowValue("#grid_grp", "GRP_ID") == "") {
    		alert("그룹을 선택하세요.");
    		return false;
    	}else if($.getCurrentRowValue("#grid_grp", "AUTH_LVL") == "") {
    		alert("선택하신 그룹의 권한레벨을 먼저 등록하셔야 합니다. '그룹별 권한레벨 관리'에서 권한레벨 등록 후 사용하세요.");
    		return false;
    	}else {
    		resetAction();
    		$(".layerRegister").show();
    		
            $(".mask").remove();
            $("body").append("<div class='mask'></div>");

            try{
        		$('.layer SELECT').selectBox("destroy");
        		$('.layer SELECT').selectBox();
        	} catch(e) {}
    	}
	});
	
	$(".btnSv2").bind("click",function(){
    	try{
       		if(insertAction() == false)	return false;
      
    	}catch(e) {}
	    	
		$(".mask").remove();
   		$(".layer").hide();
    
    	return false;
	});
	
	$(".btnMultiDe1").bind("click",function(){
    	try{
    		deleteMultiAction();
    	}catch(e) {}
    	
    	insertFlag = false;
    	return false;
    });

   
	
});

function insertAction() {
	var s = $.getSelRow("#grid_popup");
	
	if(s.length == 0) {
		alert("롤을 선택하여 주십시오");
		return false;
	}
	
	var url = "<c:url value='/'/>wrks/sstm/grp/auth_role/insert_role.json";
	var params = "";
	
	params += "&grpId=" + escape(encodeURIComponent($.getCurrentRowValue("#grid_grp", "GRP_ID")));
	params += "&authLvl=" + escape(encodeURIComponent($.getCurrentRowValue("#grid_grp", "AUTH_LVL")));
	
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid_popup").getRowData(s[i]);
		params += "&roleId=" + escape(encodeURIComponent(list.ROLE_ID));
	}
	
	$.ajaxEx($("#grid_role"), {
		url : url,
		datatype: "json",
		data : params,
		success:function(data) {
			$("#grid_role").trigger("reloadGrid");
			alert(data.msg);
		},
		error:function(e){	
			alert(data.msg);
		}
	});
}

function deleteAction(obj) {

	var url = "<c:url value='/'/>wrks/sstm/grp/auth_role/delete.json";  
    var params = "roleId=" + escape(encodeURIComponent($.getCurrentRowValue("#grid_role", "ROLE_ID")));
    params += "&grpId=" + escape(encodeURIComponent($.getCurrentRowValue("#grid_grp", "GRP_ID")));
    params += "&authLvl=" + escape(encodeURIComponent($.getCurrentRowValue("#grid_grp", "AUTH_LVL")));
    
    $.ajaxEx($('#grid_role'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid_role").trigger("reloadGrid");
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
	var s =  $.getSelRow("#grid_role");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 롤을 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/grp/auth_role/delete_role.json";  
    var params = "";
    params += "&grpId=" + escape(encodeURIComponent($.getCurrentRowValue("#grid_grp", "GRP_ID")));
    params += "&authLvl=" + escape(encodeURIComponent($.getCurrentRowValue("#grid_grp", "AUTH_LVL")));
    for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid_role").getRowData(s[i]);
		params += "&roleId=" + escape(encodeURIComponent(list.ROLE_ID));
    }

    $.ajaxEx($("#grid_role"), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid_role").trigger("reloadGrid");
        	alert("자료를 삭제하였습니다.");
        },   
        error:function(e){  
            //alert(e.responseText);
        	alert(data.msg);
        }  
    });
    
    return true;
}

function resetAction() {
	
	$("#grid_popup").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/grp/auth_role/list_popup.json"});
	var myPostData = $("#grid_popup").jqGrid('getGridParam', 'postData');
	myPostData.grpId = $.getCurrentRowValue("#grid_grp", "GRP_ID");
	myPostData.authLvl = $.getCurrentRowValue("#grid_grp", "AUTH_LVL");
	$("#grid_popup").trigger("reloadGrid");
	
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
                    <h3 class="tit">그룹별권한레벨별롤</h3>
                </div> 
                <div class="boxWrap">
	                <div class="tbLeft50">
		                <div class="tableTypeFree seachT">
		                    <table>
		                        <tbody>
		                        <tr>
		                        	<th>지구</th>
		                            <td>
			                        	<select name="" id="sDstrtCd" class="selectType1">
											<option value="">전체</option>
										    <c:forEach items="${listCmDstrtCdMng}" var="val"> 
										        <option value="${val.DSTRT_CD}"><c:out value="${val.DSTRT_NM}" ></c:out></option>   
										    </c:forEach>                     
										</select>
	                        		</td>
	                        		<th>사용유형</th>
			                        <td>
			                        	<select name="" id="sUseTyCd" class="selectType1" maxlength="1">
			                        		<c:forEach items="${useGrpList}" var="val">
			                        			<option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}"></c:out></option>
			                        		</c:forEach>
			                        	</select>
			                        </td>
								</tr>
				                <tr>
				                	<th>그룹명</th>
		                            <td>
		                            	<input type="text" name="" id="sGrpNmKo" class="txtType date8Type searchEvt" style="ime-mode:active">
			                        </td>
			                        <th>권한레벨</th>
		                            <td>
		                            	<input type="text" name="" id="sAuthLvlNm" class="txtType date8Type searchEvt" style="ime-mode:active">
		                            	<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
			                        </td>
				                </tr>    
		                        </tbody>
		                    </table>
		                </div><br/>
		                <div class="tableType1" style="height:550px; overflow-y:scroll; overflow-x:hidden">
		    				<table id="grid_grp" style="width:100%">
		    				</table>
		                </div>
	                </div>
	                <div class="tbRight50">
	                	<div class="searchBox50">
		                    <dl>
		                        <dt>선택된 그룹</dt>
		                    </dl>
		                </div>
		                <div class="tableTypeFree seachT">
		                    <table>
		                        <caption>선택된 그룹</caption>
		                        <tbody>
		                        <tr>
			                        <th>그룹명</th>
			                        <td id="dGrpNmKo"></td>
			                        <th>권한레벨</th>
			                        <td id="dAuthLvlNm"></td>
		                        </tr>
		                        </tbody>
		                    </table>
		                </div><br/>
		                <div class="tableType1" style="height:550px; overflow-y:scroll; overflow-x:hidden">
		    				<table id="grid_role" style="width:100%">
		    				</table>
		                </div>
		                <div class="btnWrap btnR">
	                    	<a href="#" class="btn btnDt btnRgt1">추가</a>
	                        <a href="#" class="btn btnMultiDe1">삭제</a>
                    	</div>
	                </div>
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
                                <th>롤 설명</th>
                                <td id="dRolePttrn" colspan="3"></td>
                            </tr>
                            <tr>
                                <th>롤 타입</th>
                                <td id="dRoleTy"></td>
                                <th>롤 정렬순서</th>
                                <td id="dSortOrdr"></td>
                            </tr>
                            <tr>
                                <th>수정자</th>
                                <td id="dUpdUserId"></td>
                                <th>수정일시</th>
                                <td id="dUpdDate"></td>
                            </tr>
                            <tr>
                            	<th>등록자</th>
                                <td id="dRgsUserId"></td>
                                <th>등록일</th>
                                <td id="dRgsDate"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="btnCtr">
                    	<a href="#" class="btn btnDe">삭제</a>
                        <a href="#" class="btn btnC">닫기</a>
                    </div>
                </div>
            </div>
            <!-- //레이어팝업 상세 -->
			
			<!-- //레이어팝업 등록 -->
            <div class="layer layerRegister" id="div_drag_2">
                <div class="tit"><h4>그룹별권한레벨별롤 추가</h4></div>  
                <div class="layerCt">
	                <div class="tableType1 popup" area="1" user-title="그룹별권한레벨별롤 추가" style="height:300px; overflow-y:scroll; overflow-x:hidden">
	    				<table id="grid_popup" style="width:100%">
	    				</table>
	                </div>
                    <div class="btnCtr">
                        <a href="#" class="btn btnSv2">저장</a>
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
