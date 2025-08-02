
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/fin/mng/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			acctGb : $("#acctGb").val()
			, assetGb : $("#assetGb").val()
			, assetName : $("#assetName").val()
			, accountNo : $("#accountNo").val()
			, useYn : $("#useYn").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'자산코드','자산명','acctGb','재정','assetGb','자산구분','은행','계좌번호','발행일','만기일','useYn','사용여부','비고'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'assetCode'   , index: 'ASSET_CODE'   , width:120, align:'center'
					}, { name: 'assetName'   , index: 'ASSET_NAME'   , width:150, align:'center'
					}, { name: 'acctGb'      , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'  , index: 'ACCT_GB_NAME' , width:100, align:'center'
					}, { name: 'assetGb'     , index: 'ASSET_GB'     , hidden:true
					}, { name: 'assetGbName' , index: 'ASSET_GB_NAME', width:100, align:'center'
					}, { name: 'bankName'    , index: 'BANK_NAME'    , width:100, align:'center'
					}, { name: 'accountNo'   , index: 'ACCOUNT_NO'   , width:200, align:'center'
					}, { name: 'issueYmd'    , index: 'ISSUE_YMD'    , width:150, align:'center'
					}, { name: 'mtyYmd'      , index: 'MTY_YMD'      , width:150, align:'center'
					}, { name: 'useYn'       , index: 'USE_YN'       , hidden:true
					}, { name: 'dUseYn'      , index: 'USE_YN'       , width:50 , align:'center'
					   , formatter : function(cellvalue, options, rowObject) {
							if ( 'Y' == rowObject.useYn ) return '사용';
							else return '미사용';
						}
					}, { name: 'remark'      , index: 'REMARK'       , width:200, align:'left'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'ASSET_CODE',
		sortorder: 'DESC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "ASSET_CODE",
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

			$("#dAssetCode").html(list.assetCode);
			$("#dAssetName").html(list.assetName);
			$("#dAcctGb").html(list.acctGbName);
			$("#dAssetGb").html(list.assetGbName);
			$("#dBankName").html(list.bankName);
			$("#dAccountNo").html(list.accountNo);
			$("#dIssueYmd").html(list.issueYmd);
			$("#dMtyYmd").html(list.mtyYmd);
			$("#dRemark").html(list.remark);
			$("#dUseYn").html(list.dUseYn);

			$("#uAssetCode").val(list.assetCode);
			$("#uAssetName").val(list.assetName);
			$("#uAcctGb").val(list.acctGb);
			$("#uAssetGb").val(list.assetGb);
			$("#uBankName").val(list.bankName);
			$("#uAccountNo").val(list.accountNo);
			$("#uIssueYmd").val(list.issueYmd);
			$("#uMtyYmd").val(list.mtyYmd);
			$("#uRemark").val(list.remark);
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
		myPostData.acctGb = $("#acctGb").val();
		myPostData.assetGb = $("#assetGb").val();
		myPostData.assetName = $("#assetName").val();
		myPostData.accountNo = $("#accountNo").val();
		myPostData.useYn = $("#useYn").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#iAssetCode").val(currentDay.replaceAll("-", ""));
	$("#iAssetName").val("");
	$("#iAcctGb").val("");
	$("#iAssetGb").val("");
	$("#iBankName").val("");
	$("#iAccountNo").val("");
	$("#iIssueYmd").val("");
	$("#iMtyYmd").val("");
	$("#iRemark").val("");
	$("#iUseYn").val("Y");
}

function preModifyAction() {
}

function validate() {
	if ( insertFlag ) {
		if($("#iAssetCode").val().trim() == "") { alert("코드를 입력하세요."); $("#iAssetCode").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	
	var url = contextRoot + "/fin/mng/insert.json";
	var params = "assetCode=" + encodeURIComponent($("#iAssetCode").val().trim());
		params += "&assetName=" + encodeURIComponent($("#iAssetName").val().trim());
		params += "&acctGb=" + encodeURIComponent($("#iAcctGb").val());
		params += "&assetGb=" + encodeURIComponent($("#iAssetGb").val());
		params += "&bankName=" + encodeURIComponent($("#iBankName").val().trim());
		params += "&accountNo=" + encodeURIComponent($("#iAccountNo").val().trim());
		params += "&issueYmd=" + encodeURIComponent($("#iIssueYmd").val().replaceAll("-", ""));
		params += "&mtyYmd=" + encodeURIComponent($("#iMtyYmd").val().replaceAll("-", ""));
		params += "&remark=" + encodeURIComponent($("#iRemark").val().trim());
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
	var url = contextRoot + "/fin/mng/update.json";
	var params = "assetCode=" + encodeURIComponent($("#uAssetCode").val().trim());
		params += "&assetName=" + encodeURIComponent($("#uAssetName").val().trim());
		params += "&acctGb=" + encodeURIComponent($("#uAcctGb").val());
		params += "&assetGb=" + encodeURIComponent($("#uAssetGb").val());
		params += "&bankName=" + encodeURIComponent($("#uBankName").val().trim());
		params += "&accountNo=" + encodeURIComponent($("#uAccountNo").val().trim());
		params += "&issueYmd=" + encodeURIComponent($("#uIssueYmd").val().replaceAll("-", ""));
		params += "&mtyYmd=" + encodeURIComponent($("#uMtyYmd").val().replaceAll("-", ""));
		params += "&remark=" + encodeURIComponent($("#uRemark").val().trim());
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
	var url = contextRoot + "/fin/mng/delete.json";
	var params = "assetCode=" + $("#uAssetCode").val();

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
	var url = contextRoot + "/fin/mng/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&assetCode=" + list.assetCode;
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
