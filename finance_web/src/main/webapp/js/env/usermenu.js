
$(document).ready(function(){

	$.jqGrid('#grid_user', {
		url:contextRoot + "/env/usermenu/userlist.json",
		datatype: "json",
		autowidth: true,		
		postData: {
			userName : $("#userName").val()
			},
		colNames: [	''
					, '아이디' , '사용자명' , '연락처' , '직분' , '직분' , '재정' , '재정' , '구분' , '사용여부'
				   ],
		colModel: [ { name:'CHECK', width:50 , align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
						, formatter: function (cellValue, option) {
							return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
						}
					}, { name: 'userId'      , index: 'USER_ID'      , width:120, align:'center'
					}, { name: 'userName'    , index: 'USER_NAME'    , width:200, align:'center'
					}, { name: 'telNo'       , index: 'TEL_NO'       , width:200, align:'center'
					}, { name: 'positionCode', index: 'POSITION_CODE', width:200, align:'center'
					}, { name: 'positionName', index: 'POSITION_NAME', width:200, align:'center'
					}, { name: 'acctGb'      , index: 'ACCT_GB'      , width:200, align:'center'
					}, { name: 'acctGbName'  , index: 'ACCT_GB_NAME' , width:200, align:'center'
					}, { name: 'userGb'      , index: 'USER_GB'      , width:200, align:'center'
					}, { name: 'useYn'       , index: 'USE_YN'       , width:200, align:'center'
					}
			  ],
		pager: '#pager',
		rowNum : 1000,
        height: $("#grid_user").parent().height() - 40,
		sortname:'USER_NAME',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onSelectRow : function(rowid, status, e){
			$("#grid_user input[type=radio]").get(rowid - 1).checked = true;
			var list = jQuery("#grid_user").getRowData(rowid);
			$.loading(true);
			//$("#dGrpId").text(list.grpId);
			//$("#dGrpNm").text(list.grpNmKo);
            //$("#dGrpAuthNm").text(list.authLvlNm);
			$("#grid_menu").jqGrid('setGridParam', {url:contextRoot + "/env/usermenu/menulist.json"});
			var myPostData = $("#grid_menu").jqGrid('getGridParam', 'postData');
			myPostData.userId = list.userId;
			$("#grid_menu").trigger("reloadGrid");

		},
		beforeProcessing: function(data, status, xhr){
			//$("#dGrpId").text("");
			//$("#dGrpNm").text("");
            //$("#dGrpAuthNm").text("");
			$("#grid_menu").clearGridData();
		},
		loadComplete: function(){
		//	$("#grid_user input[type=radio]").change(function(){
		//		$("#grid_user").jqGrid('setSelection',$(this).val(),true);
		//	});
		}
  	});
	
	$.jqGrid($('#grid_menu'), {
		//url: '<c:url value='/'/>env/menu/grp/list.json',
		datatype: "json",
		autowidth: true,		
		postData: {},
		colNames: [  '메뉴선택', 'C', 'ID', 'MENU_LEVEL', 'PRNT_MENU_ID', 'ISLF', 'EXPD'
				],
		colModel: [ { name:'sgroupNm', index:'SGROUP_NM'  , width:230, align:'left'  , sortable: false
					}, { name:'yn'    , index:'YN'     , width:60 , align:'center'
						, editable:true, edittype:'checkbox', editoptions: { value:"Yes:No" }, sortable: false, formatter: $.GridCheckBoxC
					}, { name:'menuId'    , index:'MENU_ID'     , width:200, hidden:true, key:true
					}, { name:'memuLevel' , index:'MENU_LEVEL'  , width:300, hidden:true
					}, { name:'prntMenuId', index:'PRNT_MENU_ID', width:300, hidden:true
					}, { name:'islf'      , index:'ISLF'        , width:300, hidden:true
					}, { name:'expd'      , index:'EXPD'        , width:100, hidden:true
					}
				],
		treeGrid: true,
		treeGridModel: "adjacency",
		caption: "",
		ExpandColumn: "sgroupNm",
		tree_root_level:0,
		rowNum: 10000,
        height: $("#grid_menu").parent().height() - 40,
		ExpandColClick: true,
		treeIcons: {leaf:'ui-icon-document-b'},
		treeReader:{
			leaf_field :"islf",				//확장 화살표 여부(true:확장,false:최하단 레벨 이므로 확장 안함)
			level_field: 'menuLevel',		//level값
			parent_id_field : 'prntMenuId', //부모id값
			expanded_field: 'expd'			//열린상태로 로딩할지 여부
		},

		jsonReader: {
		/*	root: function(obj) { return obj.rows; },
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
		*/
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
/*
	$.jqGrid('#grid_grp_to', {
		//url: '<c:url value='/'/>env/menu/grp/list_grp.json',
		datatype: "json",
		postData: {},
		colNames: [	'',	'그룹아이디',	'그룹명',	'사용유형',	'권한',	'권한레벨'
				   ],
		colModel: [ { name:'CHECK', width:50 , align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
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
*/
	$(".btnS").bind("click",function(){
		var myPostData = $("#grid_user").jqGrid('getGridParam', 'postData');
		
		myPostData.userName = $("#userName").val();
		
		$("#grid_user").trigger("reloadGrid");
	});
/*
	//팝업
	$(".btnCopy").bind("click",function(){
		$("#grid_grp_to").jqGrid('setGridParam', {url:contextRoot + "/env/usermenu/userlist.json"});
		var myPostData = $("#grid_grp_to").jqGrid('getGridParam', 'postData');
		//myPostData.dstrtCd = $("#sDstrtCd").val();
		myPostData.grpId = $.getCurrentRowValue("#grid_user", "grpId");
		myPostData.authLvl = $.getCurrentRowValue("#grid_user", "authLvl");
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
		var grpId = $.getCurrentRowValue("#grid_user", "grpId");
		var authLvl = $.getCurrentRowValue("#grid_user", "authLvl");
		var grpIdTo = $.getCurrentRowValue("#grid_grp_to", "grpId");
		var authLvlTo = $.getCurrentRowValue("#grid_grp_to", "authLvl");

		var url = contextRoot + "/env/usermenu/copy.json";
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
*/

	$(".tableType1").css('height', window.innerHeight - 225);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 255);
	
});

function resetAction() {
}

function updateAction(obj) {
	$.loading(true);
	var url = contextRoot + "/env/usermenu/update.json";
	
	var params = "";
	var rowId = $("#grid_user").jqGrid('getGridParam', 'selrow');
	var list = $("#grid_user").jqGrid('getRowData', rowId);

	params += "userId=" + encodeURIComponent(list.userId);
	//params += "&authLvl=" + encodeURIComponent(list.authLvl);
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
			$("#grid_menu").trigger("reloadGrid");
			$.loading(false);
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}
