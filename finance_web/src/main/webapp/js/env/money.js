
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/money/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYy : $("#stanYy").val()
			, acctGb : $("#acctGb").val()
			, moneyName : $("#moneyName").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'헌금코드','헌금명','acctGb','재정명칭','연결계정','연결계정명','회계년도','사용여부'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'moneyCode'     , index: 'MONEY_CODE'       , width:100, align:'center'
					}, { name: 'moneyName'     , index: 'MONEY_NAME'       , width:200, align:'center'
					}, { name: 'acctGb'        , index: 'ACCT_GB'          , hidden:true
					}, { name: 'acctGbName'    , index: 'ACCT_GB_NAME'     , width:100, align:'center'
					}, { name: 'acctCode'      , index: 'ACCT_CODE'        , width:200, align:'center'
					}, { name: 'acctName'      , index: 'ACCT_NAME'        , width:200, align:'center'
					}, { name: 'stanYy'        , index: 'STAN_YY'          , width:100, align:'center'
					}, { name: 'useYn'         , index: 'USE_YN'           , width:50,  align:'center'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'MONEY_CODE',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "MONEY_CODE",
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

			$("#dMoneyCode").html(list.moneyCode);
			$("#dMoneyName").html(list.moneyName);
			$("#dAcctCode").html(list.acctName);
			$("#dAcctGb").html(list.acctGbName);

			$("#uMoneyCode").val(list.moneyCode);
			$("#uMoneyName").val(list.moneyName);
			$("#uAcctCode").val(list.acctCode);
			$("#uAcctGb").val(list.acctGb);

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
		myPostData.stanYy = $("#stanYy").val();
		myPostData.acctGb = $("#acctGb").val();
		myPostData.moneyName = $("#moneyName").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#iMoneyCode").val("");
	$("#iMoneyName").val("");
	$("#iAcctCode").val("");
	$("#iAcctGb").val("");
}

function preModifyAction() {
}

function validate() {
	if ( insertFlag ) {
		if($("#iMoneyCode").val().trim() == "") { alert("코드를 입력하세요."); $("#iMoneyCode").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	var url = contextRoot + "/env/money/insert.json";
	var params = "moneyCode=" + encodeURIComponent($("#iMoneyCode").val().trim());
		params += "&moneyName=" + encodeURIComponent($("#iMoneyName").val().trim());
		params += "&acctCode=" + encodeURIComponent($("#iAcctCode").val());
		params += "&acctGb=" + encodeURIComponent($("#iAcctGb").val());

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
	var url = contextRoot + "/env/money/update.json";
	var params = "moneyCode=" + encodeURIComponent($("#uMoneyCode").val().trim());
		params += "&moneyName=" + encodeURIComponent($("#uMoneyName").val().trim());
		params += "&acctCode=" + encodeURIComponent($("#uAcctCode").val());
		params += "&acctGb=" + encodeURIComponent($("#uAcctGb").val());

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
	var url = contextRoot + "/env/money/delete.json";
	var params = "moneyCode=" + $("#iMoneyCode").val();

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
	var url = contextRoot + "/env/money/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&moneyCode=" + list.moneyCode;
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
