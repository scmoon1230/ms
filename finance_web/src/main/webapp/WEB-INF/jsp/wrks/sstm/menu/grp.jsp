<%--
/**
 * 그룹별프로그램 메뉴를 관리
 * @author		대전도안 김정원
 * @version		1.00	2014-01-25
 * @since		JDK 1.7.0_45(x64)
 * @revision
 * /

/**
 * ----------------------------------------------------------
 * @Class Name : grp.jsp
 * @Description : 그룹별프로그램메뉴
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 * 
 * ----------------------------------------------------------
 * */
--%>
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
	
	$.jqGrid('#grid_grp', {
        url: '<c:url value='/'/>wrks/sstm/menu/grp/list_grp.json',
        datatype: "json",
        postData: { 
        	dstrtCd : $("#sDstrtCd").val(),
        	grpNm : $("#sGrpNm").val(),
        	useTyCd : $("#sUseTyCd").val()
        	
        },
        colNames: [
                    	'',
                        '그룹아이디',
                        '그룹명',
                        '사용유형'
                   ],
        colModel: [
                    { name: 'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
                    	, formatter: function (cellValue, option) {
            				return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
        				}
                    },
                  	{ name: 'GRP_ID', width:100, align:'center'},
                  	{ name: 'GRP_NM_KO', width:160, align:'center'},
                  	{ name: 'USE_TY_NM', width:100, align:'center'}
          ],
        pager: '#pager',
        rowNum : 1000,
        sortname: 'GRP_ID',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		loadonce:false,		 
        jsonReader: {
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
        	$("#grid_grp input[type=radio]").get(rowid - 1).checked = true;
        	
			var list = jQuery("#grid_grp").getRowData(rowid);
			
        	$.loading(true);
        	
        	$("#dGrpId").text(list.GRP_ID);
        	$("#dGrpNm").text(list.GRP_NM_KO);
			$("#grid_menu").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/menu/grp/list.json"});
	    	var myPostData = $("#grid_menu").jqGrid('getGridParam', 'postData');
	    	myPostData.grpId = list.GRP_ID;
	    	$("#grid_menu").trigger("reloadGrid");
			
        },
        beforeProcessing: function(data, status, xhr){
            $("#grid_menu").clearGridData();
        }
  	});

    $.jqGrid($('#grid_menu'), {
        //url: '<c:url value='/'/>wrks/sstm/menu/grp/list.json',
        datatype: "json",
        postData: {},
        colNames: [
				  '메뉴선택'
				, ''
				, ''
				, ''
				, ''
				/*
				, '<input type="checkbox" name="hcheckbox_R" onchange="$.GridHeaderCheckBoxChange(\'#grid_menu\', this, event);" user-cmdtype="R">'
				, '<input type="checkbox" name="hcheckbox_C" onchange="$.GridHeaderCheckBoxChange(\'#grid_menu\', this, event);" user-cmdtype="C">'
				, '<input type="checkbox" name="hcheckbox_U" onchange="$.GridHeaderCheckBoxChange(\'#grid_menu\', this, event);" user-cmdtype="U">'
				, '<input type="checkbox" name="hcheckbox_D" onchange="$.GridHeaderCheckBoxChange(\'#grid_menu\', this, event);" user-cmdtype="D">'
				*/
				, 'ID'
				, 'LVL'
				, 'PCD'
				, 'ISLF'
				, 'EXPD'
        ],
        colModel: [
				 { name: 'PGM_MENU_NM_KO', width:210, align:'left', sortable: false}
				, { name: 'SEA_AUTH_YN', width:60, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxR}
				, { name: 'RGS_AUTH_YN', width:60, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxC}
				, { name: 'UPD_AUTH_YN', width:60, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxU}
				, { name: 'DEL_AUTH_YN', width:60, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxD}
				, { name: 'PGM_MENU_ID', width:200, hidden:true, key:true}
				, { name: 'LVL', width:300, hidden:true}
				, { name: 'PRNT_PGM_MENU_ID', width:300, hidden:true}
				, { name: 'ISLF', width:300, hidden:true}
				, { name: 'EXPD', width:100, hidden:true}
        ],
        treeGrid: true,
        treeGridModel: "adjacency",
        caption: "",
        ExpandColumn: "PGM_MENU_NM_KO",
		tree_root_level:0,
        rowNum: 10000,
        ExpandColClick: true,
        treeIcons: {leaf:'ui-icon-document-b'},
		treeReader:{
            leaf_field :"ISLF", //확장 화살표 여부(true:확장,false:최하단 레벨 이므로 확장 안함)
            level_field: 'LVL',        //---level값 
            parent_id_field : 'PRNT_PGM_MENU_ID',   //---부모id값 
			expanded_field: 'EXPD' //"EXPD" //열린상태로 로딩할지 여부
		},
 		
        jsonReader: {
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
			if(iCol == 0) {
				//console.log($(cellcontent));
				//console.log($(cellcontent).find(".treeclick"));
	        	//$($(cellcontent).get(0)).trigger("click");
				return true;
			}
        },
        beforeRequest: function() {
        	rowNum = 10000;
        },
        beforeProcessing: function(data, status, xhr){
        	$.loading(false);
        },
        beforeSelectRow: function (rowid, e) {
        	var className = "";
            var $this = $(this),
                isLeafName = $this.jqGrid("getGridParam", "treeReader").leaf_field,
                localIdName = $this.jqGrid("getGridParam", "localReader").id,
                localData,
                state,
                setChechedStateOfChildrenItems = function (children) {
                    $.each(children, function () {
                        $("#" + this[localIdName] + " input." + className).prop("checked", state);
                        if (!this[isLeafName]) {
                            setChechedStateOfChildrenItems($this.jqGrid("getNodeChildren", this));
                        }
                    });
                };
            if (e.target.nodeName === "INPUT") {
            	className = $(e.target).attr("class");
                state = $(e.target).prop("checked");
                localData = $this.jqGrid("getLocalRow", rowid);
                setChechedStateOfChildrenItems($this.jqGrid("getNodeChildren", localData), state);
            }
        }
        
	});
    //tree header 제거
    //$('#gview_grid_menu .ui-jqgrid-htable').hide();
    $("#gview_grid_menu .ui-jqgrid-htable th:eq(1)").hide();
    $("#gview_grid_menu .ui-jqgrid-htable th:eq(2)").hide();
    $("#gview_grid_menu .ui-jqgrid-htable th:eq(3)").hide();
    $("#gview_grid_menu .ui-jqgrid-htable th:eq(4)").hide();

	$.jqGrid('#grid_grp_to', {
        //url: '<c:url value='/'/>wrks/sstm/menu/grp/list_grp.json',
        datatype: "json",
        postData: {},
        colNames: [
                    	'',
                        '그룹아이디',
                        '그룹명',
                        '사용유형'
                   ],
        colModel: [
                    { name: 'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
                    	, formatter: function (cellValue, option) {
            				return '<input type="radio" name="radio_grp_to" value="' + option.rowId + '"/>';
        				}
                    },
                  	{ name: 'GRP_ID', width:100, align:'center'},
                  	{ name: 'GRP_NM_KO', width:160, align:'center'},
                  	{ name: 'USE_TY_NM', width:100, align:'center'}
          ],
        pager: '#pager',
        rowNum : 1000,
        sortname: 'GRP_ID',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		loadonce:false,		 
        jsonReader: {
        },
        onCellSelect : function(rowid, iCol, cellcontent, e){
        	$("#grid_grp_to input[type=radio]").get(rowid - 1).checked = true;
        },
        beforeProcessing: function(data, status, xhr){
        }
  	});

    
	$(".btnS").bind("click",function(){
    	var myPostData = $("#grid_grp").jqGrid('getGridParam', 'postData');
    	myPostData.grpNm = $("#sGrpNm").val();
    	myPostData.dstrtCd = $("#sDstrtCd").val();
    	myPostData.useTyCd = $("#sUseTyCd").val();
    	$("#grid_grp").trigger("reloadGrid");
    });

    //팝업
    $(".btnCopy").bind("click",function(){
		$("#grid_grp_to").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/menu/grp/list_grp.json"});
    	var myPostData = $("#grid_grp_to").jqGrid('getGridParam', 'postData');
    	myPostData.grpId = $.getCurrentRowValue("#grid_grp", "GRP_ID");
    	myPostData.useTyCd = 'Y';
    	$("#grid_grp_to").trigger("reloadGrid");
    	
        $(".layerCopy").show();
        $("#maskCopy").remove();
        $("body").append("<div class='maskPop' id='maskCopy'></div>");
        $.center(".layerCopy");
    });
    //레이어 저장버튼
    $(".layer .btnCopySv").bind("click",function(){
    	var grpId = $.getCurrentRowValue("#grid_grp", "GRP_ID");
    	var grpIdTo = $.getCurrentRowValue("#grid_grp_to", "GRP_ID");

    	var url = "<c:url value='/'/>wrks/sstm/menu/grp/copy.json";
        var params = "grpId=" + grpId;
        params += "&grpIdTo=" + grpIdTo;
       	
        $.ajax({
        	type : "POST",
    	    url : url,
    	    datatype: "json",
    	    data: params,
            success:function(data){
            	alert(data.msg);
            	$("#maskCopy").remove();
                $(".layerCopy").hide();
            },   
            error:function(e){  
                alert(e.responseText);  
            }  
        });
    	
        return false;
    });
    //레이어 취소버튼
    $(".layerWidthNone .btnCopyC").bind("click",function(){
        $("#maskCopy").remove();
        $(".layerCopy").hide();
        return false;
    });
	

});

function resetAction() {
}

function updateAction(obj) {
    var url = "<c:url value='/'/>wrks/sstm/menu/grp/update.json";  
    var params = "";
    var rowId = $("#grid_grp").jqGrid('getGridParam', 'selrow'); 
    var list = $("#grid_grp").jqGrid('getRowData', rowId);

    params += "grpId=" + encodeURIComponent(list.GRP_ID);
    params += $.getCRUDParams("#grid_menu");

    //alert(params); return;
    
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
                    <h3 class="tit">그룹별프로그램 메뉴</h3>
                </div>
                <div class="boxWrap">
	                <div class="tbLeft50">
		                <div class="tableTypeHalf seachT">
		                    <table>
		                        <caption>프로그램정보</caption>
		                        <tbody>
		                        <tr>
			                        <th>지구</th>
			                        <td>
										<select name="" id="sDstrtCd" class="selectType1">
										    <c:forEach items="${listCmDstrtCdMng}" var="val"> 
										        <option value="${val.DSTRT_CD}"><c:out value="${val.DSTRT_NM}" ></c:out></option>   
										    </c:forEach>                     
										</select>
			                        </td>
			                        <th>사용유형</th>
			                        <td>
									    <select name="sUseTyCd" id="sUseTyCd" class="selectType1">
										    <%--<option value="">전체</option>--%>
										    <c:forEach items="${useGrpList}" var="val"> 
										        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>   
										    </c:forEach>
										</select>										
			                        </td>			                        
			                    </tr>
			                    <tr>
			                        <th>그룹명</th>
			                        <td colspan=3>
			                        	<input type="text" name="" id="sGrpNm" class="txtType searchEvt" style="ime-mode:active">
				                        <a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
			                        </td>
		                        </tr>
		                        </tbody>
		                    </table>
		                </div><br/>

		                <div class="tableType1">
		    				<table id="grid_grp" style="width:100%">
		    				</table>
		                </div>
	                </div>
	                <div class="tbRight50">
		                <div class="tableTypeHalf seachT">
		                    <table>
		                        <tbody>
		                        <tr>
			                        <th>그룹명</th>
			                        <td id="dGrpNm"></td>
			                        <th>그룹아이디</th>
			                        <td id="dGrpId"></td>
			                    </tr>
		                        </tbody>
		                    </table>
		                </div><br/>
		                <div class="tableType1" style="height:650px; overflow-y:scroll; overflow-x:hidden">
		    				<table id="grid_menu" style="width:100%">
		    				</table>
		                </div>
		                <div class="btnWrap btnR">
		                    <a href="#" class="btn btnDt btnCopy">복사</a>
		                    <a href="#" class="btn btnDt btnCheckAll" user-trigger-selector="#grid_menu input[type=checkbox]">전체선택</a>
		                    <a href="#" class="btn btnDt btnUnCheckAll" user-trigger-selector="#grid_menu input[type=checkbox]">전체해제</a>
		                    <a href="#" class="btn btnDt btnSv">저장</a>
		                </div>                
	                </div>
                </div>
        	</div>
            <div class="layerWidthNone layerCopy" style="z-index:100">
                <div class="tit"><h4>그룹 선택</h4></div>
                <div class="layerCt">
	                <div class="tableType1" style="height:300px; overflow-y:scroll; overflow-x:hidden;">
	    				<table id="grid_grp_to" style="width:100%">
	    				</table>
	                </div>
                    <div class="btnCtr">
                        <a href="#" class="btn btnCopySv">저장</a>
                        <a href="#" class="btn btnCopyC">취소</a>
                    </div>
                </div>
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
