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

	$.jqGrid('#grid_event', {
		url: '<c:url value='/'/>wrks/sstm/rdus/auto_dis/list_event.json',
		datatype: "json",
		postData: {
        	dstrtCd : $("#dstrtCd").val(),
        	grpNm : $("#sGrpNm").val()
		},
		colNames: [
		            	'선택',
		            	'이벤트아이디',
		            	'이벤트명',
		            	'그룹아이디',
		            	'그룹명',
		            	'지구코드',
		],
   		colModel: [
					{ name: 'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
							, formatter: function (cellValue, option) {
							return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
							}
					}
					,{ name: 'EVT_ID', 	width:100, align:'center'}
					,{ name: 'EVT_NM', 	width:100, align:'center'}	
					,{ name: 'GRP_ID', 	width:100, align:'left'}
					,{ name: 'GRP_NM_KO', 	width:100, align:'left'}
					,{ name: 'DSTRT_CD', 	width:100, align:'left'}	
	     ],	
	        pager: '#pager',
	        rowNum : 1000,
	        sortname: 'EVT_NM',
	        sortorder: 'ASC',
	        viewrecords: true,
			multiselect: false,
			shrinkToFit: true,
	        scrollOffset: 0,
	        autowidth: true,			
			loadonce:false,		 
	        jsonReader: {
	        },
	        onSelectRow : function(rowid, status, e){
	        	$("#grid_event input[type=radio]").get(rowid - 1).checked = true;

				var list = jQuery("#grid_event").getRowData(rowid);

		    	var myPostData = $("#grid_detail").jqGrid('getGridParam', 'postData');
		    	myPostData.evtId = list.EVT_ID;
		    	myPostData.evtNm = list.EVT_NM;
		    	myPostData.grpId = list.GRP_ID;
		    	myPostData.dstrtCd = list.DSTRT_CD;
		    	$("#grid_detail").trigger("reloadGrid");	
		    	
		    	//타이틀
		    	$("#sGrp").val(list.GRP_NM_KO);
		    	$("#sEvnt").val(list.EVT_NM);
		    	$("#userAccTitle").html("자동표출사용자 ");  	
	        },
	        beforeProcessing: function(data, status, xhr){
		    	$("#userAccTitle").html("자동표출사용자 ");//자동표출사용자 
	        	
	        	$("#grid_detail").clearGridData();
	        	$('#grid_user_acc').clearGridData();
	        },	        
	        loadComplete: function(){
	        	$("#grid_event input[type=radio]").change(function(){
	        		$("#grid_event").jqGrid('setSelection',$(this).val(),true);
	        	});
	        }
	});
	
	$.jqGrid('#grid_detail', {
        url: '<c:url value='/'/>wrks/sstm/rdus/auto_dis/list_detail.json',
        datatype: "json",
        postData: { 
        	grpId : $.getCurrentRowValue("#grid_event", "GRP_ID"),
        	evtId : $.getCurrentRowValue("#grid_event", "EVT_ID"),
        	dstrtCd : $.getCurrentRowValue("#grid_event", "DSTRT_CD")
        },
        colNames: [
                    	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_detail\', this, event);">'
                        ,'세부항목코드'
                        ,'세부항목명'
                        ,'이벤트아이디'
                   ],
        colModel: [
					 { name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox}
                  	 ,{ name: 'EVT_ID_SUB_CD', width:200, align:'center'}
                  	 ,{ name: 'EVT_ID_SUB_NM', width:200, align:'center'}
                  	 ,{ name: 'EVT_ID', width:100, align:'center', 'hidden':true}
          ],

        pager: '#pager',
        rowNum : 1000,
        sortname: 'EVT_ID_SUB_CD',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		shrinkToFit: true,
        scrollOffset: 0,
        autowidth: true,		
		loadonce:false,		 
        jsonReader: {
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
        	if(iCol == 0) return false;

			var list_detail = jQuery("#grid_detail").getRowData(rowid);
	    	var myPostData = $("#grid_user_acc").jqGrid('getGridParam', 'postData');
	    	myPostData.grpId = $.getCurrentRowValue("#grid_event", "GRP_ID");
	    	myPostData.evtId = list_detail.EVT_ID;
	    	//myPostData.evtNm = list.EVT_NM;
	    	myPostData.evtSubCd = list_detail.EVT_ID_SUB_CD;
	    	myPostData.evtSubNm = list_detail.EVT_ID_SUB_NM;
	    	myPostData.dstrtCd = $.getCurrentRowValue("#grid_event", "DSTRT_CD");

	    	$("#grid_user_acc").trigger("reloadGrid");
			
	    	//타이틀
	    	$("#sEvnt").val($.getCurrentRowValue("#grid_event", "EVT_NM"));
	    	$("#sEvntSub").val(list_detail.EVT_ID_SUB_NM);
	    	$("#userAccTitle").html("자동표출사용자 [ " + $("#sGrp").val() + " > " + $("#sEvnt").val() + " > " + $("#sEvntSub").val() + " ]");
        	
        },
        beforeProcessing: function(data, status, xhr){
        	$('#grid_user_acc').jqGrid('clearGridData');
        }
  	});	

    $.jqGrid('#grid_user_acc', {
        url: '<c:url value='/'/>wrks/sstm/rdus/auto_dis/list_user_acc.json',
        datatype: "json",
        postData: { 
        	grpId : $.getCurrentRowValue("#grid_event", "GRP_ID"),
        	evtId : $.getCurrentRowValue("#grid_event", "EVT_ID"),
        	dstrtCd : $.getCurrentRowValue("#grid_event", "DSTRT_CD")        	
        },
        colNames: [
                   	 	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_user_acc\', this, event);">',
                        '아이디',
                        '사용자',
                        '세부항목코드',
                        '세부항목명', 
                        '이벤트아이디',
                        '그룹아이디',
                        '지구코드'
                   ],
        colModel: [
                    { name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false
                    	, formatter: $.GridCheckBox},
                  	{ name: 'USER_ID', width:100, align:'left'},
                  	{ name: 'USER_NM_KO', width:100, align:'left'},
                  	{ name: 'EVT_ID_SUB_CD', width:350, align:'center'},
                  	{ name: 'EVT_ID_SUB_NM', width:350, align:'center'},
                  	{ name: 'EVT_ID', width:180, align:'center', 'hidden':true},
                  	{ name: 'GRP_ID', 	width:100, align:'center', 'hidden':true},
                  	{ name: 'DSTRT_CD', width:180, align:'center', 'hidden':true}
          			],
        pager: '#pager',
        rowNum : 1000,
        sortname: 'USER_ID',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		shrinkToFit: true,
        scrollOffset: 0,
        autowidth: true,
		loadonce:false,		 
        jsonReader: {
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;
        },
        loadComplete : function (data) {
            $('#grid_user_acc SELECT').selectBox();
        }
         
  	});	

    $.jqGrid('#grid_detail_popup', {
        datatype: "json",
        postData: { 
        	dstrtCd : $.getCurrentRowValue("#grid_event", "DSTRT_CD"),
        	evtId : $.getCurrentRowValue("#grid_event", "EVT_ID"),
        	evtSubCd : $.getCurrentRowValue("#grid_detail", "EVT_ID_SUB_CD"),
        	dstrtCd : $.getCurrentRowValue("#grid_detail", "DSTRT_CD"),        	
        },
        colNames: [
                   	 	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_detail_popup\', this, event);">',
                        '세부분류코드',
                        '세부분류명',
                        '이벤트아이디',
                        '지구코드'
                   ],
        colModel: [
                    { name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
                  	{ name: 'EVT_ID_SUB_CD', width:350, align:'center'},
                  	{ name: 'EVT_ID_SUB_NM', width:350, align:'center'},
                  	{ name: 'EVT_ID', width:180, align:'center', 'hidden':true},
                  	{ name: 'DSTRT_CD', width:180, align:'center', 'hidden':true}
          			],
        pager: '#pager',
        rowNum : 1000,
        sortname: 'EVT_ID_SUB_CD',
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
    
    $.jqGrid('#grid_user_acc_popup', {
        datatype: "json",
        postData: { 
            	grpId : $.getCurrentRowValue("#grid_event", "GRP_ID"),
            	evtId : $.getCurrentRowValue("#grid_event", "EVT_ID"),
            	evtSubCd : $.getCurrentRowValue("#grid_detail", "EVT_ID_SUB_CD"),
            	dstrtCd : $.getCurrentRowValue("#grid_event", "DSTRT_CD"),            	
            	
            	userNmKo : $("#sUserNmKo").val(),
		    	moblNo : $("#sMoblNo").val(),
		    	deptNm : $("#sDeptNm").val(),
		    	rpsbWork : $("#sRpsbWork").val()
        },
        colNames: [
                   	 	'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_user_acc_popup\', this, event);">',
                        '아이디',
                        '사용자명',
                        '사용여부'                     
                   ],
        colModel: [
                    { name: 'CHECK', width:100, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
                  	{ name: 'USER_ID', width:160, align:'left'},
                  	{ name: 'USER_NM_KO', width:160, align:'left'},
                  	{ name: 'USE_TY_NM', width:150, align:'center'}               	
          			],
        pager: '#pager',
        rowNum : 1000,
        sortname: 'USER_ID',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		loadonce:false,		 
        jsonReader: {
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;
        },
        loadComplete : function (data) {
            $('#grid_user_acc_popup SELECT').selectBox();
        }
         
  	});    
	
    $(".btnS").bind("click",function(){
    	var myPostData = $("#grid_event").jqGrid('getGridParam', 'postData');
    	myPostData.dstrtCd = $("#sDstrtCd").val(); 
    	$("#grid_event").trigger("reloadGrid");
	});
    
    
    $(".btnS2").bind("click",function(){
    	var myPostData = $("#grid_user_acc_popup").jqGrid('getGridParam', 'postData');
    	
    	myPostData.userNmKo = $("#sUserNmKo").val();
    	myPostData.moblNo = $("#sMoblNo").val();
    	myPostData.deptNm = $("#sDeptNm").val();
    	myPostData.rpsbWork = $("#sRpsbWork").val();
    	
    	$("#grid_user_acc_popup").trigger("reloadGrid");
	});
    
}); 

function resetAction(area, callBack) {
	var flag;
	var url;
	
	$.resetInputObject(".layerRegister .tableType1." + area);

	if($.getCurrentRowValue("#grid_event", "EVT_ID") == "") {
		alert("이벤트을 선택하세요.");
		return false;
	}
	
	if(typeof flag == "undefined" || flag == null || flag == ""){
		
		
		if(area == "user_acc") {
			if($.getCurrentRowValue("#grid_detail", "EVT_ID_SUB_CD") == "") {
				alert("세부항목코드를 선택하세요.");
				return false;
			}
		}	
		
		if(area == "detail") {
	    	var myPostData = $("#grid_" + area + "_popup").jqGrid('getGridParam', 'postData');
	    	myPostData.dstrtCd = $.getCurrentRowValue("#grid_event", "DSTRT_CD");
	    	myPostData.evtId = $.getCurrentRowValue("#grid_event", "EVT_ID");
	    	
		}
		
		if(area == "user_acc") {
	    	var myPostData = $("#grid_" + area + "_popup").jqGrid('getGridParam', 'postData');
	    	myPostData.grpId = $.getCurrentRowValue("#grid_event", "GRP_ID");
	    	myPostData.evtId = $.getCurrentRowValue("#grid_event", "EVT_ID");
	    	myPostData.evtSubCd = $.getCurrentRowValue("#grid_detail", "EVT_ID_SUB_CD");
	    	myPostData.dstrtCd = $.getCurrentRowValue("#grid_event", "DSTRT_CD");    	
	    	
	    	myPostData.userNmKo = $("#sUserNmKo").val();
	    	myPostData.moblNo = $("#sMoblNo").val();
	    	myPostData.deptNm = $("#sDeptNm").val();
	    	myPostData.rpsbWork = $("#sRpsbWork").val();
		}
	}	
	
	$("#grid_" + area + "_popup").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/rdus/auto_dis/list_" + area + "_popup.json"});

	$("#grid_" + area + "_popup").jqGrid('setGridParam', {
		beforeProcessing: callBack
	});
	$("#grid_" + area + "_popup").trigger("reloadGrid");	
}

function validate(area) {
	return $.validate(".layerRegister .tableType2." + area);
}

function insertAction(area, src) {

	var s =  $.getSelRow("#grid_" + area + "_popup");

    var url = "<c:url value='/'/>wrks/sstm/rdus/auto_dis/insert_" + area + ".json";  
    var params = "";

	var eventSelCnt =  $.getSelRow("#grid_detail");
    
    if(s.length == 0){
    	alert("데이터를 선택해 주십시오");
    	return false;
    }	
	var evtId = "";     	
		
    for(var i = 0; i < s.length; i++) {

		var list = jQuery("#grid_" + area + "_popup").getRowData(s[i]);

		params += "&grpId=" + encodeURIComponent($.getCurrentRowValue("#grid_event", "GRP_ID"));
		params += "&dstrtCd=" + encodeURIComponent($.getCurrentRowValue("#grid_event", "DSTRT_CD"));
		params += "&evtId=" + encodeURIComponent($.getCurrentRowValue("#grid_event", "EVT_ID"));
		
		if(area == "detail"){
	    	params += "&evtIdSubCd=" + encodeURIComponent(list.EVT_ID_SUB_CD);
		}else if(area == "user_acc") {

			params += "&evtIdSubCd=" + encodeURIComponent($.getCurrentRowValue("#grid_detail", "EVT_ID_SUB_CD"));
	    	params += "&userId=" + encodeURIComponent(list.USER_ID);
		}
 
    }

    $.ajaxEx($("#grid_" + area), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid_" + area).trigger("reloadGrid");
        	//alert("삭제하였습니다.");
        	alert(data.msg);
        },   
        error:function(e){  
            //alert(e.responseText);
        	alert(data.msg);
        }  
    });
}

function deleteMultiAction(area) {
	
	var s =  $.getSelRow("#grid_" + area);
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	var msg=" ";
	
	if(confirm(msg+" 선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/rdus/auto_dis/delete_" + area + ".json";  
    var params = "";

    for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid_" + area).getRowData(s[i]);
		params += "&grpId=" + $.getCurrentRowValue("#grid_detail", "GRP_ID");

		if(area == "detail"){
			params += "&dstrtCd=" + list.DSTRT_CD;
			params += "&evtId=" + list.EVT_ID;
	    	params += "&evtIdSubCd=" + list.EVT_ID_SUB_CD;
		}else if(area == "user_acc"){
			params += "&dstrtCd=" + list.DSTRT_CD;
			params += "&userId=" + list.USER_ID;
			params += "&evtId=" + list.EVT_ID;
	    	params += "&evtIdSubCd=" + list.EVT_ID_SUB_CD;			
		}
    }

    $.ajaxEx($("#grid_" + area), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid_" + area).trigger("reloadGrid");
        	alert("자료를 삭제하였습니다.");
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
					<h3 class="tit">자동표출관리</h3>
				</div>
            	<div class="boxWrap">
	                <div class="tbLeft50">
						<div class="searchBox50">
		                    <dl>
		                        <dt>이벤트 리스트</dt>
			                    <input type="hidden" id="dstrtCd" name="dstrtCd" value="<c:out value="${dstrtCd}" />" />
			                    <input type="hidden" id="lgnId" name="lgnId" value="<c:out value="${lgnId}" />" />
		                    </dl>
		                </div>
		                <div class="tableType1" style="height:250px;width:100%; overflow-y:scroll; overflow-x:hidden">
		    				<table id="grid_event" style="width:100%">
		    				</table>
		                </div>
	                </div>
	                <div class="tbRight50">
						<div class="searchBox50">
		                    <dl>
		                        <dt>자동표출 이벤트세부분류</dt>
		                    </dl>
		                </div>
		                <div class="tableType1" style="height:250px;width:100%; overflow-y:scroll; overflow-x:hidden">
		    				<table id="grid_detail" style="width:100%">
		    				</table>
		                </div>
		                <div class="btnWrap btnR">
		                    <!--<a href="#" class="btn btnDt btnRgt" area="detail">추가</a>-->
		                	<!-- <a href="#" class="btn btnMultiDe" area="detail">선택삭제</a> -->
		                </div>                
	                </div>
		            <div class="tbLeft50"  style="width:100%">
			           	<input type="hidden" id="sGrp" />
			           	<input type="hidden" id="sEvnt" />
			           	<input type="hidden" id="sEvntSub" />	            
						<div class="searchBox50">
		                   <dl>
		                       <dt id="userAccTitle">자동표출사용자</dt>
		                   </dl>
		               </div>
		               <div class="tableType1" style="height:250px;width:100%; overflow-y:scroll; overflow-x:hidden">
							<table id="grid_user_acc" style="width:100%">
							</table>
		               </div>
		               <div class="btnWrap btnR">
							<a href="#" class="btn btnDt btnRgt" area="user_acc">추가</a>
							<a href="#" class="btn btnMultiDe" area="user_acc">선택삭제</a>
		               </div>                
		           </div>
                </div>
			</div>

            <!-- //레이어팝업 등록 -->
            <div class="layer layerRegister"  id="div_drag_2">
                <div class="tit"><h4></h4></div>
                <div class="layerCt">
	                <div class="tableType1 detail" user-title="자동표출 이벤트세부분류 추가" style="height:500px; overflow-y:scroll; overflow-x:hidden">
	    				<table id="grid_detail_popup" style="width:100%">
	    				</table>
	                </div>
		            <div class="tableType1 user_acc" user-title="사용자 추가" style="height:500px; overflow-y:scroll; overflow-x:hidden">
		            	<div class="tableTypeFree seachT">
		                 <table>
		                     <caption>사용자 등록</caption>
		                     <tbody>
			                   <tr>
			                   	<th>사용자</th>
									<td><input type="text" name="" id="sUserNmKo" class="txtType searchEvt2" style="width: 70px; ime-mode:active"></td>
									<th>연락처</th>
									<td><input type="text" name="" id="sMoblNo" class="txtType searchEvt2" style="width: 100px; ime-mode:active"></td>
									<th>부서명</th>
									<td><input type="text" name="" id="sDeptNm" class="txtType searchEvt2" style="width: 100px; ime-mode:active"></td>
									<th>담당업무</th>
									<td><input type="text" name="" id="sRpsbWork" class="txtType searchEvt2" style="width: 100px; ime-mode:active">
									<a href="javascript:;" class="btn btnRight btnS2 searchBtn2">검색</a>
									</td>
			                   </tr>
		                     </tbody>
		                 </table>
		             </div><br/>
						<table id="grid_user_acc_popup" style="width:100%">
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