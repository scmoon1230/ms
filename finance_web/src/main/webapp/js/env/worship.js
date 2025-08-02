
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/worship/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			worshipName : $("#worshipName").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '예배코드' , '예배명칭' , '요일' , '요일'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'worshipCode'    , index: 'WORSHIP_CODE', width:120, align:'center'
					}, { name: 'worshipName'    , index: 'WORSHIP_NAME', width:200, align:'center'
					}, { name: 'worshipDay'     , index: 'WORSHIP_DAY' , width:200, align:'center', hidden:true
					}, { name: 'worshipDayName' , index: 'WORSHIP_DAY' , width:200, align:'center'
					   , formatter : function(cellvalue, options, rowObject) {
							return rowObject.worshipDay+'요일';
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'WORSHIP_CODE',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "WORSHIP_CODE",
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

			$("#dWorshipCode").html(list.worshipCode);
			$("#dWorshipName").html(list.worshipName);
			$("#dWorshipDay").html(list.worshipDayName);

			$("#uWorshipCode").val(list.worshipCode);
			$("#uWorshipName").val(list.worshipName);
			$("#uWorshipDay").val(list.worshipDay);

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
		myPostData.worshipName = $("#worshipName").val();

		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#iWorshipCode").val("");
	$("#iWorshipName").val("");
	$("#iWorshipDay").val("");
}

function validate() {
	if ( insertFlag ) {
		if($("#iWorshipCode").val().trim() == "") { alert("예배코드를 입력하세요."); return false; }
	}
	return true;
}

function insertAction(obj) {
	var url = contextRoot + "/env/worship/insert.json";
	var params = "worshipCode=" + encodeURIComponent($("#iWorshipCode").val().trim());
		params += "&worshipName=" + encodeURIComponent($("#iWorshipName").val().trim());
		params += "&worshipDay=" + encodeURIComponent($("#iWorshipDay").val().trim());

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
	var url = contextRoot + "/env/worship/update.json";
	var params = "worshipCode=" + encodeURIComponent($("#uWorshipCode").val().trim());
   		params += "&worshipName=" + encodeURIComponent($("#uWorshipName").val().trim());
		params += "&worshipDay=" + encodeURIComponent($("#uWorshipDay").val().trim());

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
	var url = contextRoot + "/env/worship/delete.json";
	var params = "worshipCode=" + $("#iWorshipCode").val();

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
	var url = contextRoot + "/env/worship/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&worshipCode=" + list.worshipCode;
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
