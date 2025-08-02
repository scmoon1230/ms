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
<!-- 
    <style>
        .tableTypeHalf th {
            text-align: left;
            text-indent: 10px;
            width: 100px;
        }
    </style>
 -->
<script type="text/javascript">
$(document).ready(function(){
	$.jqGrid('#grid_grp', {
		url: '<c:url value='/'/>wrks/sstm/menu/grpauth/list_grpauth.json',
		datatype: "json",
		autowidth: true,		
		postData: {
			//dstrtCd : $("#sDstrtCd").val(),
			grpNm : $("#sGrpNm").val(),
			useTyCd : $("#sUseTyCd").val()

			},
		colNames: [	'',	'그룹아이디',	'그룹명',	'권한레벨',	'사용유형',	'권한레벨코드',	'지구명'
				   ],
		colModel: [ { name:'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
						, formatter: function (cellValue, option) {
							return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
						}
					},
				  	{ name:'grpId'    , index:'GRP_ID'     , width:80 , align:'center'},
				  	{ name:'grpNmKo'  , index:'GRP_NM_KO'  , width:100, align:'center'},
				  	{ name:'authLvlNm', index:'AUTH_LVL_NM', width:100, align:'center'},
				  	{ name:'useTyNm'  , index:'USE_TY_NM'  , width:80 , align:'center'},
				  	{ name:'authLvl'  , index:'AUTH_LVL'   , hidden:true},
				  	{ name:'dstrtNm'  , index:'DSTRT_NM'   , width:80 , align:'center'}
		  ],
		pager: '#pager',
		rowNum : 1000,
        height: $("#grid_grp").parent().height() - 40,
		sortname:'GRP_NM_KO',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onSelectRow : function(rowid, status, e){
			$("#grid_grp input[type=radio]").get(rowid - 1).checked = true;
			var list = jQuery("#grid_grp").getRowData(rowid);
			$.loading(true);
			$("#dGrpId").text(list.grpId);
			$("#dGrpNm").text(list.grpNmKo);
            $("#dGrpAuthNm").text(list.authLvlNm);
			$("#grid_menu").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/menu/grpauth/list.json"});
			var myPostData = $("#grid_menu").jqGrid('getGridParam', 'postData');
			myPostData.grpId = list.grpId;
			myPostData.authLvl = list.authLvl;
			$("#grid_menu").trigger("reloadGrid");

		},
		beforeProcessing: function(data, status, xhr){
			$("#dGrpId").text("");
			$("#dGrpNm").text("");
            $("#dGrpAuthNm").text("");
			$("#grid_menu").clearGridData();
		},
		loadComplete: function(){
			$("#grid_grp input[type=radio]").change(function(){
				$("#grid_grp").jqGrid('setSelection',$(this).val(),true);
			});
		}
  	});

	$.jqGrid($('#grid_menu'), {
		//url: '<c:url value='/'/>wrks/sstm/menu/grp/list.json',
		datatype: "json",
		autowidth: true,		
		postData: {},
		colNames: [  '메뉴선택', 'C', 'R', 'U', 'D', 'ID', 'LVL', 'PCD', 'ISLF', 'EXPD'
		],
		colModel: [ { name:'pgmMenuNmKo', index:'PGM_MENU_NM_KO'  , width:230, align:'left'  , sortable: false }
				, { name:'rgsAuthYn'    , index:'RGS_AUTH_YN'     , width:60 , align:'center',              editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxC}
				, { name:'seaAuthYn'    , index:'SEA_AUTH_YN'     , width:60 , align:'center', hidden:true, editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxR}
				, { name:'updAuthYn'    , index:'UPD_AUTH_YN'     , width:60 , align:'center', hidden:true, editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxU}
				, { name:'delAuthYn'    , index:'DEL_AUTH_YN'     , width:60 , align:'center', hidden:true, editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxD}
				, { name:'pgmMenuId'    , index:'PGM_MENU_ID'     , width:200, hidden:true, key:true}
				, { name:'lvl'          , index:'LVL'             , width:300, hidden:true }
				, { name:'prntPgmMenuId', index:'PRNT_PGM_MENU_ID', width:300, hidden:true }
				, { name:'islf'         , index:'ISLF'            , width:300, hidden:true }
				, { name:'expd'         , index:'EXPD'            , width:100, hidden:true }
		],
		treeGrid: true,
		treeGridModel: "adjacency",
		caption: "",
		ExpandColumn: "pgmMenuNmKo",
		tree_root_level:0,
		rowNum: 10000,
        height: $("#grid_menu").parent().height() - 40,
		ExpandColClick: true,
		treeIcons: {leaf:'ui-icon-document-b'},
		treeReader:{
			leaf_field :"islf", //확장 화살표 여부(true:확장,false:최하단 레벨 이므로 확장 안함)
			level_field: 'lvl',		//---level값
			parent_id_field : 'prntPgmMenuId',   //---부모id값
			expanded_field: 'expd' //"EXPD" //열린상태로 로딩할지 여부
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
		},
		loadComplete: function(){

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
		colNames: [	'',	'그룹아이디',	'그룹명',	'사용유형',	'권한',	'권한레벨'
				   ],
		colModel: [ { name:'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
						, formatter: function (cellValue, option) {
							return '<input type="radio" name="radio_grp_to" value="' + option.rowId + '"/>';
						}
					},
				  	{ name:'grpId'    , index:'GRP_ID'     , width:110, align:'center'},
				  	{ name:'grpNmKo'  , index:'GRP_NM_KO'  , width:150, align:'center'},
				  	{ name:'useTyNm'  , index:'USE_TY_NM'  , width:90 , align:'center'},
				  	{ name:'authLvlNm', index:'AUTH_LVL_NM', width:90 , align:'center'},
				  	{ name:'authLvl'  , index:'AUTH_LVL'   , width:100, align:'center', hidden:true}
		  ],
		pager: '#pager',
		rowNum : 1000,
        height: $("#grid_grp_to").parent().height() - 40,
		sortname:'GRP_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onSelectRow: function(rowid, status, e){
			$("#grid_grp_to input[type=radio]").get(rowid - 1).checked = true;
		},
		beforeProcessing: function(data, status, xhr){
		},
		loadComplete: function(){
			$("#grid_grp_to input[type=radio]").change(function(){
				$("#grid_grp_to").jqGrid('setSelection',$(this).val(),true);
			});
		}
  	});

	$(".btnS").bind("click",function(){
		var myPostData = $("#grid_grp").jqGrid('getGridParam', 'postData');
		myPostData.grpNm = $("#sGrpNm").val();
		//myPostData.dstrtCd = $("#sDstrtCd").val();
		myPostData.useTyCd = $("#sUseTyCd").val();
		$("#grid_grp").trigger("reloadGrid");
	});

	//팝업
	$(".btnCopy").bind("click",function(){
		$("#grid_grp_to").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/menu/grpauth/list_grpauth.json"});
		var myPostData = $("#grid_grp_to").jqGrid('getGridParam', 'postData');
		//myPostData.dstrtCd = $("#sDstrtCd").val();
		myPostData.grpId = $.getCurrentRowValue("#grid_grp", "grpId");
		myPostData.authLvl = $.getCurrentRowValue("#grid_grp", "authLvl");
		myPostData.useTyCd = 'Y';
		$("#grid_grp_to").trigger("reloadGrid");

		$(".layerCopy").show();
		$("#maskCopy").remove();
		$("body").append("<div class='maskPop' id='maskCopy'></div>");
		$.center(".layerCopy");
	});
	//레이어 저장버튼
	$(".layerWidthNone .btnCopySv").bind("click",function(){
		var s = $.getSelRowRadio("#grid_grp_to");
		if(s.length == 0){
			alert("그룹을 선택하여 주십시오.");
			return false;
		}
		var grpId = $.getCurrentRowValue("#grid_grp", "grpId");
		var authLvl = $.getCurrentRowValue("#grid_grp", "authLvl");
		var grpIdTo = $.getCurrentRowValue("#grid_grp_to", "grpId");
		var authLvlTo = $.getCurrentRowValue("#grid_grp_to", "authLvl");

		var url = "<c:url value='/'/>wrks/sstm/menu/grpauth/copy.json";
		var params = "grpId=" + grpId;
		params += "&authLvl=" + authLvl;
		params += "&grpIdTo=" + grpIdTo;
		params += "&authLvlTo=" + authLvlTo;

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
	var url = "<c:url value='/'/>wrks/sstm/menu/grpauth/update.json";
	var params = "";
	var rowId = $("#grid_grp").jqGrid('getGridParam', 'selrow');
	var list = $("#grid_grp").jqGrid('getRowData', rowId);

	params += "grpId=" + encodeURIComponent(list.grpId);
	params += "&authLvl=" + encodeURIComponent(list.authLvl);
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
								<caption>그룹권한별메뉴</caption>
								<tbody>
								<tr><th>그룹명</th>
									<td colspan=3>
										<input type="text" name="" id="sGrpNm" class="txtType searchEvt" style="ime-mode:active">
									</td>
									<th>사용유형</th>
									<td><select name="sUseTyCd" id="sUseTyCd" class="selectType1">
											<%--<option value="">전체</option>--%>
											<c:forEach items="${useGrpList}" var="val">
												<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											</c:forEach>
										</select>
										<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
									</td>
								<%--<th>지구</th>
									<td><select name="" id="sDstrtCd" class="selectType1">
											<option value="">전체</option>
											<c:forEach items="${listCmDstrtCdMng}" var="val">
												<option value="${val.dstrtCd}"><c:out value="${val.dstrtNm}" ></c:out></option>
											</c:forEach>
										</select>
									</td>--%>
								</tr>
								</tbody>
							</table>
						</div><br/>

						<div class="tableType1" style="height:650px;">
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
                                    <th>레벨</th>
                                    <td id="dGrpAuthNm"></td>
									<th>그룹아이디</th>
									<td id="dGrpId"></td>
								</tr>
								</tbody>
							</table>
						</div><br/>
						<div class="tableType1" style="height:650px;">
							<table id="grid_menu" style="width:100%">
							</table>
						</div>
						<div class="btnWrap btnR">
							<a href="#" class="btn btnDt btnCopy">현재메뉴를 팝업선택그룹으로 복사</a>
						<!--<a href="#" class="btn btnDt btnCheckAll" user-trigger-selector="#grid_menu input[type=checkbox]">전체선택</a>
							<a href="#" class="btn btnDt btnUnCheckAll" user-trigger-selector="#grid_menu input[type=checkbox]">전체해제</a>-->
							<a href="#" class="btn btnDt btnSv">저장</a>
						</div>
					</div>
				</div>
			</div>
			<div class="layerWidthNone layerCopy" style="z-index:100">
				<div class="tit"><h4>그룹권한 선택</h4></div>
				<div class="layerCt">
					<div class="tableType1" style="height:300px;">
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
