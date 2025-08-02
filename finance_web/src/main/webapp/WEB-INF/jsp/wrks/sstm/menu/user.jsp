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

	$.jqGrid('#grid_user', {
        url: '<c:url value='/'/>wrks/sstm/menu/user/list_user.json',
        datatype: "json",
        postData: {
        	dstrtCd : $("#sDstrtCd").val(),
        	grpNm : $("#sGrpNm").val(),
        	userNm : $("#sUserNm").val(),
        	useTyCd : $("#sUseTyCd").val()
        },
        colNames: [   	''	, '아이디'	, '성명'		, '그룹'			, '사용'				, '휴대폰'			, '지구코드'
                   ],
        colModel: [
                    { name: 'CHECK', width:30, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
                    	, formatter: function (cellValue, option) {
            				return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
        				}
                    },
                  	{ name: 'USER_ID', width:100, align:'center'},
                  	{ name: 'USER_NM_KO', width:120, align:'center'},
                  	{ name: 'GRP_NM_KO', width:160, align:'center'},
                  	{ name: 'USE_NM', width:60, align:'center'},
                  	{ name: 'MOBL_NO', width:160, align:'center', 'hidden':true},
                  	{ name: 'DSTRT_CD', width:160, align:'center', 'hidden':true}
          ],
        pager: '#pager',
        rowNum : 1000,
        autowidth: true,
		height: $("#grid_user").parent().height()-40,
        sortname: 'USER_ID',
        sortorder: 'ASC',
        viewrecords: true,
		multiselect: false,
		loadonce:false,
        jsonReader: {
        },
        onSelectRow : function(rowid, iCol, cellcontent, e){
        	$("#grid_user input[type=radio]").get(rowid - 1).checked = true;

			var list = jQuery("#grid_user").getRowData(rowid);

        	$.loading(true);

        	$("#dUserId").text(list.USER_ID);
        	$("#dUserNm").text(list.USER_NM_KO);
        	$("#dGrpNm").text(list.GRP_NM_KO);
        	$("#dMoblNo").text(list.MOBL_NO);
			$("#grid_menu").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/menu/user/list.json"});
	    	var myPostData = $("#grid_menu").jqGrid('getGridParam', 'postData');
	    	myPostData.userId = list.USER_ID;
	    	myPostData.dstrtCd = $("#sDstrtCd").val();
	    	$("#grid_menu").trigger("reloadGrid");

        },
        beforeProcessing: function(data, status, xhr){
        	$("#dUserId").text("");
        	$("#dUserNm").text("");
        	$("#dGrpNm").text("");
        	$("#dMoblNo").text("");
            $("#grid_menu").clearGridData();
        },
        loadComplete: function(){
        	$("#grid_user input[type=radio]").change(function(){
    			$("#grid_user").jqGrid('setSelection',$(this).val(),true);
        	});
        }
  	});

    $.jqGrid($('#grid_menu'), {
        //url: '<c:url value='/'/>wrks/sstm/menu/user/list.json',
        datatype: "json",
		autowidth: true,        
        postData: {},
        colNames: [
				  '메뉴선택'
				, 'C'
				, 'R'
				, 'U'
				, 'D'
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
				 { name: 'PGM_MENU_NM_KO', width:230, align:'left'}
				, { name: 'RGS_AUTH_YN', width:60, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxC}
				, { name: 'SEA_AUTH_YN', width:60, hidden:true, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxR}
				, { name: 'UPD_AUTH_YN', width:60, hidden:true, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxU}
				, { name: 'DEL_AUTH_YN', width:60, hidden:true, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxD}
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
		height: $("#grid_menu").parent().height()-40,
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
        		if (obj.rows == null)  return 1;
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

	$.jqGrid('#grid_user_to', {
        //url: '<c:url value='/'/>wrks/sstm/menu/grp/list_grp.json',
        datatype: "json",
        postData: {},
        colNames: [
                	''
                	, '아이디'
                    , '성명'
                    , '그룹'
                    , '사용'
                    , '휴대폰'
                    , '지구코드'
                   ],
        colModel: [
                    { name: 'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
                    	, formatter: function (cellValue, option) {
            				return '<input type="radio" name="radio_grp_to" value="' + option.rowId + '"/>';
        				}
                    },
                  	{ name: 'USER_ID', width:100, align:'center'},
                  	{ name: 'USER_NM_KO', width:100, align:'center'},
                  	{ name: 'GRP_NM_KO', width:140, align:'center'},
                  	{ name: 'USE_NM', width:60, align:'center'},
                  	{ name: 'MOBL_NO', width:160, align:'center', 'hidden':true},
                  	{ name: 'DSTRT_CD', width:160, align:'center', 'hidden':true}
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
        	$("#grid_user_to input[type=radio]").get(rowid - 1).checked = true;
        },
        beforeProcessing: function(data, status, xhr){
        },
        loadComplete: function(){
        	$("#grid_user_to input[type=radio]").change(function(){
        		$("#grid_user_to").jqGrid('setSelection',$(this).val(),true);
        	});
        }
  	});
	$(".btnS").bind("click",function(){
    	var myPostData = $("#grid_user").jqGrid('getGridParam', 'postData');

    	myPostData.userNm = $("#sUserNm").val();
    	myPostData.grpNm = $("#sGrpNm").val();
    	myPostData.dstrtCd = $("#sDstrtCd").val();
    	myPostData.useTyCd = $("#sUseTyCd").val();

    	$("#grid_user").trigger("reloadGrid");
    });
    //팝업
    $(".btnCopy").bind("click",function(){
    	var s = $.getSelRowRadio("#grid_user");
    	if(s.length == 0) {
    		alert("사용자를 선택하여 주십시오.");
    		return false;
    	}
		$("#grid_user_to").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/menu/user/list_user.json"});
    	var myPostData = $("#grid_user_to").jqGrid('getGridParam', 'postData');
    	myPostData.userId = $.getCurrentRowValue("#grid_user","USER_ID");
    	myPostData.dstrtCd = $.getCurrentRowValue("#grid_user","DSTRT_CD");
    	myPostData.useTyCd = 'Y';
    	$("#grid_user_to").trigger("reloadGrid");

        $(".layerCopy").show();
        $("#maskCopy").remove();
        $("body").append("<div class='maskPop' id='maskCopy'></div>");
        $.center(".layerCopy");
    });
    //레이어 저장버튼
    $(".layerWidthNone .btnCopySv").bind("click",function(){
    	var s = $.getSelRowRadio("#grid_user_to");
    	if(s.length == 0) {
    		alert("사용자를 선택하여 주십시오.");
    		return false;
    	}
    	var userId 		= $.getCurrentRowValue("#grid_user", "USER_ID");
    	var dstrtCd 	= $.getCurrentRowValue("#grid_user", "DSTRT_CD");
    	var userIdTo 	= $.getCurrentRowValue("#grid_user_to", "USER_ID");
    	var dstrtCdTo 	= $.getCurrentRowValue("#grid_user_to", "DSTRT_CD");

    	var url = "<c:url value='/'/>wrks/sstm/menu/user/copy.json";
        var params = "userId=" + userId;
        params += "&dstrtCd=" + dstrtCd;
        params += "&userIdTo=" + userIdTo;
        params += "&dstrtCdTo=" + dstrtCdTo;

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
	$.loading(true);
    var url = "<c:url value='/'/>wrks/sstm/menu/user/update.json";
    var params = "";
    var rowId = $("#grid_user").jqGrid('getGridParam', 'selrow');
    var list = $("#grid_user").jqGrid('getRowData', rowId);

    params += "userId=" + encodeURIComponent(list.USER_ID);
    params += "&dstrtCd=" + encodeURIComponent(list.DSTRT_CD);
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
        	$.loading(false);
        },
        error:function(e){
            alert(e.responseText);
        }
    });
}

</script>

</head>
<body>
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
<div id="wrapper" class="wth100">
    <!-- container -->
    <div class="container">
        <!-- content -->
        <div class="contentWrap">
            <div class="content">
                <%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
                <div class="boxWrap">
	                <div class="tbLeft50">
		                <div class="tableTypeHalf seachT">
		                    <table>
		                        <caption>프로그램정보</caption>
		                        <tbody>
		                        <tr>
			                        <th>지구</th>
			                        <td><select name="" id="sDstrtCd" class="selectType1">
										    <c:forEach items="${listCmDstrtCdMng}" var="val">
										        <option value="${val.DSTRT_CD}"><c:out value="${val.DSTRT_NM}" ></c:out></option>
										    </c:forEach>
										</select>
			                        </td>
			                        <th>사용유형</th>
			                        <td><select name="" id="sUseTyCd" class="selectType1">
										    <c:forEach items="${useGrpList}" var="val">
										        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
										    </c:forEach>
										</select>
			                        </td>
			                    </tr>
		                        <tr><th>성명</th>
			                        <td colspan="3"><input type="text" name="" id="sUserNm" class="txtType searchEvt" style="ime-mode:active"></td>
			                    </tr>
			                    <tr><th>그룹명</th>
			                        <td colspan="3">
			                        	<input type="text" name="" id="sGrpNm" class="txtType searchEvt" style="ime-mode:active">
					                    <a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
			                        </td>
		                        </tr>
		                        </tbody>
		                    </table>
		                </div><br/>

		                <div class="tableType1" style="height:620px;">
		    				<table id="grid_user" style="width:100%">
		    				</table>
		                </div>
	                </div>
	                <div class="tbRight50">
		                <div class="tableTypeHalf seachT">
		                    <table>
		                        <tbody>
		                        <tr><th>성명</th>			                        <td id="dUserNm"></td>
			                        <th>아이디</th>		                        <td id="dUserId"></td>
			                    </tr>
			                    <tr><th>그룹</th>			                        <td id="dGrpNm"></td>
			                        <th>휴대폰</th>		                        <td id="dMoblNo"></td>
		                        </tr>
		                        </tbody>
		                    </table>
		                </div><br/>
		                <div class="tableType1" style="height:650px;">
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
                <div class="tit"><h4>사용자 선택</h4></div>
                <div class="layerCt">
	                <div class="tableType1" style="height:300px; overflow-y:scroll; overflow-x:hidden;">
	    				<table id="grid_user_to" style="width:100%">
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
