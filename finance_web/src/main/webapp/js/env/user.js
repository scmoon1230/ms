
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/user/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			userName : $("#userName").val()
			, positionCode : $("#positionCode").val()
			, acctGb : $("#acctGb").val()
			, userGb : $("#userGb").val()
			, useYn : $("#useYn").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '사용자아이디' , '사용자명' , '연락처' , '직분' , '직분' , '재정' , '재정' , '구분' , '사용여부' , '사용여부'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'userId'      , index: 'USER_ID'      , width:120, align:'center'
					}, { name: 'userName'    , index: 'USER_NAME'    , width:200, align:'center'
					}, { name: 'telNo'       , index: 'TEL_NO'       , width:200, align:'center'
					}, { name: 'positionCode', index: 'POSITION_CODE', width:200, align:'center'
					}, { name: 'positionName', index: 'POSITION_NAME', width:200, align:'center'
					}, { name: 'acctGb'      , index: 'ACCT_GB'      , width:200, align:'center'
					}, { name: 'acctGbName'  , index: 'ACCT_GB_NAME' , width:200, align:'center'
					}, { name: 'userGb'      , index: 'USER_GB'      , width:200, align:'center'
					}, { name: 'useYn'       , index: 'USE_YN'       , width:200, align:'center', hidden:true
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
		sortname: 'USER_ID',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "USER_ID",
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

			$("#dUserId").html(list.userId);
			$("#dUserName").html(list.userName);
			$("#dTelNo").html(list.telNo);
			$("#dPositionCode").html(list.positionName);
			$("#dAcctGb").html(list.acctGbName);
			$("#dUserGb").html(list.userGb);
			$("#dUseYn").html(list.useYnName);

			$("#uUserId").val(list.userId);
			$("#uUserName").val(list.userName);
			$("#uTelNo").val(list.telNo);
			$("#uPositionCode").val(list.positionCode);
			$("#uAcctGb").val(list.acctGb);
			$("#uUserGb").val(list.userGb);
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
		myPostData.userName = $("#userName").val();
		myPostData.positionCode = $("#positionCode").val();
		myPostData.acctGb = $("#acctGb").val();
		myPostData.userGb = $("#userGb").val();
		myPostData.useYn = $("#useYn").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#btn-check-user-id").removeClass('hide');
	$("#iUserId").val("");
	$("#iUserName").val("");
	$("#iUserPwd").val("");
	$("#iTelNo").val("");
	$("#iPositionCode").val("");
	$("#iAcctGb").val("");
	$("#iUserGb").val("");
	$("#iUseYn").val("");
}

function preModifyAction() {
	$("#btn-check-user-id").addClass('hide');
}

function validate() {
	if ( insertFlag ) {
		if($("#iUserId").val().trim() == "") { alert("아이디를 입력하세요."); $("#iUserId").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	if ('' == userIdDuplFlag )	{	alert('아이디 중복검사를 하세요.');	$('#iUserId').focus();		return false;	}
	if ('IMPOSS' == userIdDuplFlag )	{	alert('이미 사용중인 아이디입니다.');	$('#iUserId').focus();		return false;	}
	
	var url = contextRoot + "/env/user/insert.json";
	var params = "userId=" + encodeURIComponent($("#iUserId").val().trim());
		params += "&userName=" + encodeURIComponent($("#iUserName").val().trim());
		params += "&userPwd=" + encodeURIComponent($("#iUserPwd").val().trim());
		params += "&telNo=" + encodeURIComponent($("#iTelNo").val().trim());
		params += "&positionCode=" + encodeURIComponent($("#iPositionCode").val());
		params += "&acctGb=" + encodeURIComponent($("#iAcctGb").val());
		params += "&userGb=" + encodeURIComponent($("#iUserGb").val());
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
	var url = contextRoot + "/env/user/update.json";
	var params = "userId=" + encodeURIComponent($("#uUserId").val().trim());
   		params += "&userName=" + encodeURIComponent($("#uUserName").val().trim());
		params += "&userPwd=" + encodeURIComponent($("#uUserPwd").val().trim());
		params += "&telNo=" + encodeURIComponent($("#uTelNo").val().trim());
		params += "&positionCode=" + encodeURIComponent($("#uPositionCode").val());
		params += "&acctGb=" + encodeURIComponent($("#uAcctGb").val());
		params += "&userGb=" + encodeURIComponent($("#uUserGb").val());
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
	var url = contextRoot + "/env/user/delete.json";
	var params = "userId=" + $("#iUserId").val();

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
	var url = contextRoot + "/env/user/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&userId=" + list.userId;
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

let userIdDuplFlag = '';
function checkUserId() {
	if ( '' == $("#iUserId").val().trim() ) {	alert('아이디를 입력하세요.');		$('#iUserId').focus();		return false;	}
	
	var params = "userId=" + encodeURIComponent($("#iUserId").val().trim());

	$.ajaxEx($('#grid'), {
		type : "POST",
		url : contextRoot + "/env/usr/checkUserId.json",
		dataType : "json",
		data: params,
		success:function(data){
			//alert(data.msg);
			if( 0 == data.msg ) {
				alert('사용가능한 아이디입니다.');		userIdDuplFlag = 'POSS';
			} else {
				alert('이미 사용중인 아이디입니다.');	userIdDuplFlag = 'IMPOSS';
			}
		},
		error:function(e){
			alert(data.msg);
		}
	});
}
