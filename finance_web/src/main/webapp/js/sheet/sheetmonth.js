
$(document).ready(function(){
	
	$.jqGrid($('#grid'), {
		url:contextRoot + "/sheet/month/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd        : $("#stanYm").val().replaceAll("-", "")+'01'
			, acctGb       : $("#acctGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'acctGb' , '재정' , 'moneyCode','헌금','월계','누계'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'       , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME' , width:100, align:'center'
					}, { name: 'acctCode'     , index: 'ACCT_CODE'    , hidden:true
					}, { name: 'printName'    , index: 'PRINT_NAME'   , width:100, align:'left'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( '대' == rowObject.acctLevel ) 		return 'style="background-color:#58D3F7"';
							else if ( '중' == rowObject.acctLevel ) 	return 'style="background-color:#E0F2F7"';
							else if ( '과' == rowObject.acctLevel ) 	return 'style="background-color:#fdffa7"';
						}
					   , formatter : function(cellvalue, options, rowObject) {
							if ( '소' == rowObject.acctLevel ) return '&nbsp; &nbsp; &nbsp; &nbsp;' + rowObject.printName;
							else if ( '중' == rowObject.acctLevel ) return '&nbsp; &nbsp;' + rowObject.printName;
							else if ( '무' == rowObject.acctLevel ) return '';
							else return rowObject.printName;
						}
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( '과' == rowObject.acctLevel ) 	return 'style="background-color:#fdffa7"';
							else return 'style="padding-right: 5px;"';
						}
					   , formatter : function(cellvalue, options, rowObject) {
							if ( '과' == rowObject.acctLevel ) 	return '';
							else if ( '무' == rowObject.acctLevel ) return '';
							else return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'ysumAmt'      , index: 'YSUM_AMT'    , width:100, align:'right'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( '과' == rowObject.acctLevel ) 	return 'style="background-color:#fdffa7"';
							else return 'style="padding-right: 5px;"';
						}
					   , formatter : function(cellvalue, options, rowObject) {
							if ( '과' == rowObject.acctLevel ) 	return '';
							else if ( '무' == rowObject.acctLevel ) return '';
							else return rowObject.ysumAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}
		  		],
		pager: '#pager',
		//rowNum: $('#rowPerPageList').val(),
		rowNum: 999,
		height: $("#grid").parent().height()-40,
		sortname: 'ACCT_CODE',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "MEMBER_ID",
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
			
		},
		loadComplete : function (data) {
			$("#jqgh_grid_moneyAmt").html("월계 ( "+data.durMonth+" )");
			$("#jqgh_grid_ysumAmt").html("누계 ( "+data.durSumm+" )");
        },
		beforeRequest: function() {
			$.loading(true);
			//rowNum = $('#rowPerPageList').val();
			rowNum = 999;
		},
		beforeProcessing: function(data, status, xhr){
			$.loading(false);
			if(typeof data.rows != "undefine" || data.row != null) {
				$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
			}
		}
  	});

	$(".btnS").bind("click",function(){
		//$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
		$("#grid").setGridParam({rowNum: 999});
		var myPostData = $("#grid").jqGrid('getGridParam', 'postData');

		//검색할 조건의 값을 설정한다.
		myPostData.stanYmd = $("#stanYm").val().replaceAll("-", "")+'01';
		myPostData.acctGb = $("#acctGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#iMemberId").val("");
	$("#iMemberNo").val("");
	$("#iMemberName").val("");
	$("#iSexType").val("");
	$("#iTelNo").val("");
	$("#iHphoneNo").val("");
	$("#iPositionCode").val("");
	$("#iDeptCode").val("");
	$("#iRegionCode").val("");
	$("#iAddr").val("");
	$("#iFamilyRemark").val("");
}

function preModifyAction() {
}

function validate() {
	if($("#iMemberId").val().trim() == "") { alert("아이디를 입력하세요."); $("#iMemberId").focus(); return false; }
	if($("#iMemberNo").val().trim() == "") { alert("번호를 입력하세요."); $("#iMemberNo").focus(); return false; }
	return true;
}

function insertAction(obj) {
	
	var url = contextRoot + "/sheet/month/insert.json";
	var params = "memberId=" + encodeURIComponent($("#iMemberId").val().trim());
		params += "&memberNo=" + encodeURIComponent($("#iMemberNo").val().trim());
		params += "&memberName=" + encodeURIComponent($("#iMemberName").val().trim());
		params += "&sexType=" + encodeURIComponent($("#iSexType").val());
		params += "&telNo=" + encodeURIComponent($("#iTelNo").val().trim());
		params += "&hphoneNo=" + encodeURIComponent($("#iHphoneNo").val().trim());
		params += "&positionCode=" + encodeURIComponent($("#iPositionCode").val());
		params += "&deptCode=" + encodeURIComponent($("#iDeptCode").val());
		params += "&regionCode=" + encodeURIComponent($("#iRegionCode").val());
		params += "&addr=" + encodeURIComponent($("#iAddr").val().trim());
		params += "&familyRemark=" + encodeURIComponent($("#iFamilyRemark").val().trim());

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
	var url = contextRoot + "/sheet/month/update.json";
	var params = "memberId=" + encodeURIComponent($("#iMemberId").val().trim());
		params += "&memberNo=" + encodeURIComponent($("#iMemberNo").val().trim());
		params += "&memberName=" + encodeURIComponent($("#iMemberName").val().trim());
		params += "&sexType=" + encodeURIComponent($("#iSexType").val());
		params += "&telNo=" + encodeURIComponent($("#iTelNo").val().trim());
		params += "&hphoneNo=" + encodeURIComponent($("#iHphoneNo").val().trim());
		params += "&positionCode=" + encodeURIComponent($("#iPositionCode").val());
		params += "&deptCode=" + encodeURIComponent($("#iDeptCode").val());
		params += "&regionCode=" + encodeURIComponent($("#iRegionCode").val());
		params += "&addr=" + encodeURIComponent($("#iAddr").val().trim());
		params += "&familyRemark=" + encodeURIComponent($("#iFamilyRemark").val().trim());

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
	var url = contextRoot + "/sheet/month/delete.json";
	var params = "memberId=" + $("#iMemberId").val();
		params += "&memberNo=" + $("#iMemberNo").val();

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
	var url = contextRoot + "/sheet/month/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&memberInfo=" + list.memberId + ":" + list.memberNo;
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
