
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + '/mntr/mng/prprts/list.json',
		datatype: "json",
		autowidth: true,
		postData: {
			prprtsId : $("#prprtsId").val(),
			prprtsTy : $("#prprtsTy").val(),
			prprtsKey : $("#prprtsKey").val().trim(),
			prprtsVal : $("#prprtsVal").val().trim(),
			dscrt : $("#dscrt").val().trim(),
        	useTyCd : $("#useTyCd").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
					 '프로퍼티 TY', '프로퍼티 ID', '프로퍼티 Key', '프로퍼티 Value', '설명', '기본 Value', '정렬순서', '사용유형코드', '사용유형'
				   ],
		colModel: [
				  { name: 'CHECK', width:60, align:'center'  , editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
				  { name: 'prprtsTy'  , index: 'PRPRTS_TY'  , width:100, align:'center'},
				  { name: 'prprtsId'  , index: 'PRPRTS_ID'  , width:100, align:'center'},
				  { name: 'prprtsKey' , index: 'PRPRTS_KEY' , width:200, align:'center'},
				  { name: 'prprtsVal' , index: 'PRPRTS_VAL' , width:200, align:'center'},
				  { name: 'dscrt'     , index: 'DSCRT'      , width:500, align:'left'},
				  { name: 'defaultVal', index: 'DEFAULT_VAL', width:200, align:'center'},
				  { name: 'sortOrdr'  , index: 'SORT_ORDR'  , width:50 , align:'center'},
                  { name: 'useTyCd'   , index: 'USE_TY_CD'  , width:100, align:'center', hidden:true},
                  { name: 'useNm'     , index: 'USE_NM'     , width:50 , align:'center'}
		  ],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'PRPRTS_ID, PRPRTS_TY, SORT_ORDR, PRPRTS_KEY',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "PRPRTS_KEY",
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
		onSelectRow: function(rowid, status, e) {
			var list = jQuery("#grid").getRowData(rowid);

			$("#dPrprtsId").html(list.prprtsId);
			$("#dPrprtsTy").html(list.prprtsTy);
			$("#dPrprtsKey").html(list.prprtsKey);
			$("#dPrprtsVal").html(list.prprtsVal);
			$("#dDefaultVal").html(list.defaultVal);
			//$("#dDscrt").html(list.dscrt);
			$("#dDscrt").html(list.dscrt.replaceAll('\n','<br/>'));
			$("#dSortOrdr").html(list.sortOrdr);
			$("#dUseTyCd").html(list.useNm);

			$("#iPrprtsId").val(list.prprtsId);
			$("#iPrprtsTy").val(list.prprtsTy);
			$("#iPrprtsKey").val(list.prprtsKey);
			$("#iPrprtsVal").val(list.prprtsVal);
			$("#iDefaultVal").val(list.defaultVal);
			$("#iDscrt").val(list.dscrt);
			$("#iSortOrdr").val(list.sortOrdr);
			$.selectBarun("#iUseTyCd", list.useTyCd);

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
		myPostData.prprtsId = $("#prprtsId").val();
		myPostData.prprtsTy = $("#prprtsTy").val();
		myPostData.prprtsKey = $("#prprtsKey").val().trim();
		myPostData.prprtsVal = $("#prprtsVal").val().trim();
		myPostData.dscrt = $("#dscrt").val().trim();
    	myPostData.useTyCd = $("#useTyCd").val();

		$("#grid").trigger("reloadGrid");
	});
	
	$(".tableType1").css('height', window.innerHeight - 250);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 300);
	
});

function resetAction() {
	//alert("resetAction");
	$("#iPrprtsId").val("");
	$("#iPrprtsTy").val("");
	$("#iPrprtsKey").val("");
	$("#iPrprtsVal").val("");
	$("#iDefaultVal").val("");
	$("#iDscrt").val("");
	$("#iSortOrdr").val("");
	$("#iUseTyCd").get(0).selectedIndex=0;
}

function updateAction(obj) {
	//alert('updateAction');
	var url = contextRoot + "/mntr/mng/prprts/update.json";
	var params = "prprtsId=" + encodeURIComponent($("#iPrprtsId").val());
   		params += "&prprtsTy=" + encodeURIComponent($("#iPrprtsTy").val());
   		params += "&prprtsKey=" + encodeURIComponent($("#iPrprtsKey").val());
   		params += "&prprtsVal=" + encodeURIComponent($("#iPrprtsVal").val());
   		params += "&defaultVal=" + encodeURIComponent($("#iDefaultVal").val());
		params += "&dscrt=" + encodeURIComponent($("#iDscrt").val());
		params += "&sortOrdr=" + encodeURIComponent($("#iSortOrdr").val());
        params += "&useTyCd=" + $("#iUseTyCd").val();

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){
			if(data.session == 1){
				$("#grid").setGridParam({page :$("#cur-page").val()});
				$("#grid").trigger("reloadGrid");
				//alert("저장하였습니다.");
				alert(data.msg);
				prprtsReload();		// 프로퍼티 적용
				
			} else {
				alert(data.msg);
			}
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}

function validate() {
	if($("#iPrprtsKey").val().trim() == "") {
		alert("프로퍼티Key를 입력하세요.");
		return false;
	}

	return true;
}

function insertAction(obj) {
	//alert('insertAction');
	var url = contextRoot + "/mntr/mng/prprts/insert.json";
	var params = "prprtsId=" + encodeURIComponent($("#iPrprtsId").val());
		params += "&prprtsTy=" + encodeURIComponent($("#iPrprtsTy").val());
		params += "&prprtsKey=" + encodeURIComponent($("#iPrprtsKey").val());
		params += "&prprtsVal=" + encodeURIComponent($("#iPrprtsVal").val());
		params += "&defaultVal=" + encodeURIComponent($("#iDefaultVal").val());
		params += "&dscrt=" + encodeURIComponent($("#iDscrt").val());
		params += "&sortOrdr=" + encodeURIComponent($("#iSortOrdr").val());
	    params += "&useTyCd=" + $("#iUseTyCd").val();

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){
			if(data.session == 1){
				$("#grid").trigger("reloadGrid");
				alert(data.msg);
				$.hideInsertForm();
				
				prprtsReload();		// 프로퍼티 적용

			}else{
				alert(data.msg);
			}
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = contextRoot + "/mntr/mng/prprts/delete.json";
	//var params = "cdGrpId=" + $("#dCdGrpId").text();

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
	//alert('deleteMultiAction');
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/mntr/mng/prprts/deleteMulti.json";
	var params = "";
	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);

	}
	//alert(params);

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

//프로퍼티 적용
function prprtsReload() {
    $.ajax({
        type: "POST",
        url:contextRoot + '/mntr/prprtsReload.json',
        dataType: "json",
        data: {
            prprts: "reload"
            , test: "test"
        },
        success: function (data) {
            if (typeof data.result != 'undefined' && data.result != null && data.result == 'ok') {
                // alert('정상적으로 처리 되었습니다.메인으로 이동합니다');
                $('<form/>', {
                    //'action': contextRoot + '/wrks/lgn/goHome.do'
                    'action': contextRoot
                }).appendTo(document.body).submit();
                
            } else {
                alert('프로퍼티 적용 중 오류가 발생하였습니다.');
            }
        },
        error: function (xhr, status, error) {
            alert('reload fail.');
        }
    });
}
