
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/digitalmoney/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanGb : $("#stanGb").val()
			, moneyName : $("#moneyName").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '구분' , '구분' , '디지털헌금코드' , '디지털헌금명' , '내부헌금코드' , '재정시스템헌금명' , '사용여부' , '사용여부'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanGb'       , index: 'STAN_GB'        , width:120, align:'center', hidden:true
					}, { name: 'stanGbName'   , index: 'STAN_GB'        , width:120, align:'center'
					   , formatter : function(cellvalue, options, rowObject) {
							if ( '1' == rowObject.stanGb ) return '일반';
							else return '청년/대학';
						}
					}, { name: 'moneyType'    , index: 'MONEY_TYPE'     , width:200, align:'center'
					}, { name: 'moneyName'    , index: 'MONEY_NAME'     , width:200, align:'center'
					}, { name: 'moneyCode'    , index: 'MONEY_CODE'     , width:200, align:'center'
					}, { name: 'origMoneyName', index: 'ORIG_MONEY_NAME', width:200, align:'center'
					}, { name: 'useYn'        , index: 'USE_YN'         , width:50 , align:'center', hidden:true
					}, { name: 'useYnName'   , index: 'USE_YN'       , width:200, align:'center'
					   , formatter : function(cellvalue, options, rowObject) {
							if ( 'Y' == rowObject.useYn ) return '사용';
							else return '미사용';
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'MONEY_TYPE',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "MONEY_TYPE",
			root: function(obj) { return obj.rows; },
			page: function(obj) { return 1; },
			total: function(obj) {
				if(obj.rows.length > 0) {
					var page  = obj.rows[0].rowcnt / rowNum;
					if( (obj.rows[0].rowcnt % rowNum) > 0)
						page++;
					return page;
				}
				else
					return 1;
			},
			records: function(obj) { return $.showCount(obj); }
		},
		onCellSelect: function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);

			$("#dStanGb").html(list.stanGbName);
			$("#dMoneyType").html(list.moneyType);
			$("#dMoneyName").html(list.moneyName);
			$("#dMoneyCode").html(list.moneyCode);
			$("#dOrigMoneyName").html(list.origMoneyName);
			$("#dUseYn").html(list.useYnName);

			$("#uStanGb").val(list.stanGb);
			$("#uMoneyType").val(list.moneyType);
			$("#uMoneyName").val(list.moneyName);
			$("#uMoneyCode").val(list.moneyCode);
			$("#uUseYn").val(list.useYn);

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
		myPostData.stanGb = $("#stanGb").val();
		myPostData.moneyName = $("#moneyName").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#iStanGb").val("");
	$("#iMoneyType").val("");
	$("#iMoneyName").val("");
	$("#iMoneyCode").val("");
	$("#iUseYn").val("");
}

function preModifyAction() {
}

function validate() {
	if ( insertFlag ) {
		if($("#iStanGb").val() == undefined) { alert("구분을 선택하세요."); $("#iStanGb").focus(); return false; }
		if($("#iMoneyType").val().trim() == "") { alert("디지털헌금코드를 입력하세요."); $("#iMoneyType").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	var url = contextRoot + "/env/digitalmoney/insert.json";
	var params = "stanGb=" + encodeURIComponent($("#iStanGb").val());
		params += "&moneyType=" + encodeURIComponent($("#iMoneyType").val().trim());
		params += "&moneyName=" + encodeURIComponent($("#iMoneyName").val().trim());
		params += "&moneyCode=" + encodeURIComponent($("#iMoneyCode").val());
		params += "&useYn=" + encodeURIComponent($("#iUseYn").val());

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

function updateAction(obj) {
	var url = contextRoot + "/env/digitalmoney/update.json";
	var params = "stanGb=" + encodeURIComponent($("#uStanGb").val());
		params += "&moneyType=" + encodeURIComponent($("#uMoneyType").val().trim());
		params += "&moneyName=" + encodeURIComponent($("#uMoneyName").val().trim());
		params += "&moneyCode=" + encodeURIComponent($("#uMoneyCode").val());
		params += "&useYn=" + encodeURIComponent($("#uUseYn").val());

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
			alert(e.responseText);
		}
	});
}

function deleteAction(obj) {
	var url = contextRoot + "/env/digitalmoney/delete.json";
	var params = "stanGb=" + $("#iStanGb").val();
		params += "&moneyType=" + encodeURIComponent($("#iMoneyType").val().trim());

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

function deleteMultiAction(obj) {
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하십시오.");
		return false;
	}

	if(confirm("선택한 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/env/digitalmoney/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&moneyInfo=" + list.stanGb + ":" +list.moneyType;
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
