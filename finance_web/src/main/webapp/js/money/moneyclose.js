
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/money/close/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd        : $("#stanYmd").val().replaceAll("-", "")
			, acctGb       : $("#acctGb").val()
			, moneyCode    : $("#moneyCode").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','acctGb','재정','moneyCode','헌금','금액','등록건수','마감건수','연결계정','연결계정명'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'    , index: 'STAN_YMD'     , width:70 , align:'center'
					}, { name: 'acctGb'	    , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName' , index: 'ACCT_GB_NAME' , width:70 , align:'center'
					}, { name: 'moneyCode'  , index: 'MONEY_CODE'   , hidden:true
					}, { name: 'moneyName'  , index: 'MONEY_NAME'   , width:100, align:'center'
					}, { name: 'moneyAmt'   , index: 'MONEY_AMT'    , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'totalCnt'   , index: 'TOTAL_CNT'    , width:40 , align:'right'
					}, { name: 'closeCnt'   , index: 'CLOSE_CNT'    , width:40 , align:'right'
					}, { name: 'acctCode'   , index: 'ACCT_CODE'    , width:40 , align:'center'
					}, { name: 'acctName'   , index: 'ACCT_NAME'    , width:40 , align:'center'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'STAN_YMD, MONEY_CODE',
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
			
			$("#startYmd").html(data.startYmd);
			$("#endYmd").html(data.endYmd);
			
			var params = "stanYmd="     + $("#stanYmd").val().replaceAll("-", "");
				params += "&startYmd="  + $("#startYmd").html().replaceAll("-", "");
				params += "&endYmd="    + $("#endYmd").html().replaceAll("-", "");
				params += "&acctGb="	+ $("#acctGb").val();
			//	params += "&moneyCode=" + $("#moneyCode").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/money/close/getSum.json",
				datatype: "json",
				data: params,
				success:function(data){
					if(data.session == 1){
						$("#totalAmnt").text(data.totalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
					}else{
						alert(data.msg);
					}
				},
				error:function(e){
					alert(e.responseText);
				}
			});
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
		myPostData.stanYmd      = $("#stanYmd").val().replaceAll("-", "");
		myPostData.acctGb       = $("#acctGb").val();
		myPostData.moneyCode	= $("#moneyCode").val();
		
		$("#grid").trigger("reloadGrid");
		
		$("#iMoneyCode").val($("#moneyCode").val());
		$("#iWorshipCode").val($("#worshipCode").val());
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
	changeMoneyCode();
	
});

function changeMoneyCode() {
	let stanYy = $("#stanYmd").val().replaceAll("-", "").substring(0,4);
	let acctGb = $("#acctGb").val();
	//resetMoneyCode(stanYy, acctGb);
}

function doClose() {
	
	if($("#acctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#acctGb").focus(); return false; }
	
	if ( !confirm("헌금을 마감처리하시겠습니까?") ) return;
	
	var url = contextRoot + "/money/close/doClose.json";
//	var params = "stanYmd="    + $("#stanYmd").val().replaceAll("-", "");
	var params = "startYmd="   + $("#startYmd").html().replaceAll("-", "");
		params += "&endYmd="   + $("#endYmd").html().replaceAll("-", "");
		params += "&acctGb="   + $("#acctGb").val();
		params += "&inoutGb="  + encodeURIComponent("수입");
		params += "&acctType=" + encodeURIComponent("자동");
	
	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){
			if(data.session == 1){
				$("#grid").trigger("reloadGrid");
				alert(data.msg);
				//$.hideInsertForm();
				//resetAction();
			}else{
				alert(data.msg);
			}
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}

function cancelClose() {
	
	if($("#acctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#acctGb").focus(); return false; }
	
	if ( !confirm("헌금마감을 취소하시겠습니까?") ) return;
	
	var url = contextRoot + "/money/close/cancelClose.json";
//	var params = "stanYmd="    + $("#stanYmd").val().replaceAll("-", "");
	var params = "startYmd="   + $("#startYmd").html().replaceAll("-", "");
		params += "&endYmd="   + $("#endYmd").html().replaceAll("-", "");
		params += "&acctGb="   + $("#acctGb").val();
	//	params += "&inoutGb="  + encodeURIComponent("수입");
	//	params += "&acctType=" + encodeURIComponent("자동");
	
	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){
			if(data.session == 1){
				$("#grid").trigger("reloadGrid");
				alert(data.msg);
				//$.hideInsertForm();
				//resetAction();
			}else{
				alert(data.msg);
			}
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}
