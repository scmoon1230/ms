
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/acctgb/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			acctGbName : $("#acctGbName").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '재정구분코드' , '재정구분명청' , '인쇄용명칭' , '사용여부'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'    , index: 'ACCT_GB'     , width:120, align:'center'
					}, { name: 'acctGbName', index: 'ACCT_GB_NAME', width:200, align:'center'
					}, { name: 'printName' , index: 'PRINT_NAME'  , width:200, align:'center'
					}, { name: 'useYn'     , index: 'USE_YN'      , width:200, align:'center'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'ACCT_GB',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "ACCT_GB",
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

			$("#dAcctGb").html(list.acctGb);
			$("#dAcctGbName").html(list.acctGbName);
			$("#dPrintName").html(list.printName);
			$("#dUseYn").html(list.useYn);

			$("#uAcctGb").val(list.acctGb);
			$("#uAcctGbName").val(list.acctGbName);
			$("#uPrintName").val(list.printName);
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
		myPostData.acctGbName = $("#acctGbName").val();

		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#iAcctGb").val("");
	$("#iAcctGbName").val("");
	$("#iPrintName").val("");
	$("#iUseYn").val("");
}

function validate() {
	if ( insertFlag ) {
		if($("#iAcctGb").val().trim() == "") { alert("재정구분코드를 입력하세요."); $("#iAcctGb").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	var url = contextRoot + "/env/acctgb/insert.json";
	var params = "acctGb=" + encodeURIComponent($("#iAcctGb").val().trim());
		params += "&acctGbName=" + encodeURIComponent($("#iAcctGbName").val().trim());
		params += "&printName=" + encodeURIComponent($("#iPrintName").val().trim());
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
	var url = contextRoot + "/env/acctgb/update.json";
	var params = "acctGb=" + encodeURIComponent($("#uAcctGb").val().trim());
	params += "&acctGbName=" + encodeURIComponent($("#uAcctGbName").val().trim());
	params += "&printName=" + encodeURIComponent($("#uPrintName").val().trim());
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
	var url = contextRoot + "/env/acctgb/delete.json";
	var params = "acctGb=" + $("#iAcctGb").val();

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
	var url = contextRoot + "/env/acctgb/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&acctGb=" + list.acctGb;
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
