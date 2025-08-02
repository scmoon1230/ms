
$(document).ready(function(){

	let totalAmnt = 0;
	
	$.jqGrid($('#grid'), {
		url:contextRoot + "/fin/mast/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd : $("#stanYmd").val().replaceAll("-", "")
			, acctGb : $("#acctGb").val()
			, assetGb : $("#assetGb").val()
			, assetName : $("#assetName").val()
			, accountNo : $("#accountNo").val()
			//, useYn : $("#useYn").val()
			, showYn : 'Y'
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'자산코드','자산명','acctGb','재정','assetGb','자산구분','은행','계좌번호','발행일','만기일','잔액','사용여부','비고'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
				}, { name: 'assetCode'   , index: 'ASSET_CODE'   , width:120, align:'center'
				}, { name: 'assetName'   , index: 'ASSET_NAME'   , width:100, align:'center'
				}, { name: 'acctGb'      , index: 'ACCT_GB'      , hidden:true
				}, { name: 'acctGbName'  , index: 'ACCT_GB_NAME' , width:80, align:'center'
				}, { name: 'assetGb'     , index: 'ASSET_GB'     , hidden:true
				}, { name: 'assetGbName' , index: 'ASSET_GB_NAME', width:100, align:'center'
				}, { name: 'bankName'    , index: 'BANK_NAME'    , width:100, align:'center'
				}, { name: 'accountNo'   , index: 'ACCOUNT_NO'   , width:200, align:'center'
				}, { name: 'issueYmd'    , index: 'ISSUE_YMD'    , width:100, align:'center'
				}, { name: 'mtyYmd'      , index: 'MTY_YMD'      , width:100, align:'center'
				}, { name: 'totalAmt'    , index: 'MONEY_AMT'    , width:150, align:'right'
					, cellattr : function() {	return 'style="padding-right: 5px;"';	}
					, formatter : function(cellvalue, options, rowObject) {
						totalAmnt += rowObject.totalAmt;
						return rowObject.totalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
					}
				}, { name: 'useYn'       , index: 'USE_YN'       , width:50 , align:'center'
				   , formatter : function(cellvalue, options, rowObject) {
						if ( 'Y' == rowObject.useYn ) return '사용';
						else return '미사용';
					}
				}, { name: 'remark'      , index: 'REMARK'       , width:250, align:'left'
				}],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'FI.ACCT_GB, FI.ASSET_NAME, FI.BANK_NAME, FI.ACCOUNT_NO',
		sortorder: 'ASC',
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
			
		},
		loadComplete : function (data) {
			
			$("#totalAmnt").text(totalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			
        },
		beforeRequest: function() {
			$.loading(true);
			rowNum = $('#rowPerPageList').val();
			
			totalAmnt = 0;
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
		myPostData.stanYmd   = $("#stanYmd").val().replaceAll("-", "");
		myPostData.acctGb    = $("#acctGb").val();
		myPostData.assetGb   = $("#assetGb").val();
		myPostData.assetName = $("#assetName").val();
		myPostData.accountNo = $("#accountNo").val();
		//myPostData.useYn = $("#useYn").val();
		myPostData.showYn = 'Y';
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
