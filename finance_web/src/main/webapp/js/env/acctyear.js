
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/acctyear/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYy : $("#stanYy").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '연도' , '시작일' , '종료일'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYy' , index: 'STAN_YY' , width:120, align:'center'
					}, { name: 'fromYmd', index: 'FROM_YMD', width:200, align:'center'
					}, { name: 'endYmd' , index: 'END_YMD' , width:200, align:'center'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'STAN_YY',
		sortorder: 'DESC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "STAN_YY",
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

			$("#dStanYy").html(list.stanYy);
			$("#dFromYmd").html(list.fromYmd);
			$("#dEndYmd").html(list.endYmd);

			$("#uStanYy").val(list.stanYy);
			$("#uFromYmd").val(list.fromYmd);
			$("#uEndYmd").val(list.endYmd);

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

		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#iStanYy").val("");
	$("#iFromYmd").val("");
	$("#iEndYmd").val("");
}

function validate() {
	if ( insertFlag ) {
		if($("#iAcctyearCode").val().trim() == "") { alert("연도를 입력하세요."); $("#iAcctyearCode").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	var url = contextRoot + "/env/acctyear/insert.json";
	var params = "stanYy=" + encodeURIComponent($("#iStanYy").val().trim());
		params += "&fromYmd=" + encodeURIComponent($("#iFromYmd").val().replaceAll('-',''));
		params += "&endYmd=" + encodeURIComponent($("#iEndYmd").val().replaceAll('-',''));

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
	var url = contextRoot + "/env/acctyear/update.json";
	var params = "stanYy=" + encodeURIComponent($("#uStanYy").val().trim());
	params += "&fromYmd=" + encodeURIComponent($("#uFromYmd").val().replaceAll('-',''));
	params += "&endYmd=" + encodeURIComponent($("#uEndYmd").val().replaceAll('-',''));

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
	var url = contextRoot + "/env/acctyear/delete.json";
	var params = "stanYy=" + $("#iStanYy").val();

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
	var url = contextRoot + "/env/acctyear/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&stanYy=" + list.stanYy;
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
