
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/fin/change/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd : $("#stanYmd").val().replaceAll("-", "")
		//	, acctGb : $("#acctGb").val()
		//	, inoutGb : $("#inoutGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, 'acctGb', '재정', 'assetGb', '자산구분'
					, '증감', '내역', '금액'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'      , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'  , index: 'ACCT_GB_NAME' , width:100, align:'center'
					}, { name: 'assetGb'     , index: 'ASSET_GB'     , hidden:true
					}, { name: 'assetGbName' , index: 'ASSET_GB_NAME', width:200, align:'center'
					}, { name: 'inoutGb'     , index: 'INOUT_GB'     , width:200, align:'center'
						, formatter : function(cellvalue, options, rowObject) {
							if ( 'I'== rowObject.inoutGb ) {	return '증가';
							} else {							return '감소';
							}
						}
					}, { name: 'remark'      , index: 'REMARK'       , width:200, align:'center'
					}, { name: 'moneyAmt'    , index: 'MONEY_AMT'    , width:200, align:'right'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( 'I'== rowObject.inoutGb ) {	return 'style="padding-right: 5px;"';
							} else {							return 'style="padding-right: 5px;color:red;"';
							}
						}
						, formatter : function(cellvalue, options, rowObject) {
							if ( 'I'== rowObject.inoutGb ) {	return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							} else {						return '- '+rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'ACCT_GB, INOUT_GB ASC, MONEY_AMT',
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

		},
		loadComplete : function (data) {
			let moneyAmtSum = 0;
			$.each(data.rows, function(index, item){
				if ( 'I'== item.inoutGb ) {		moneyAmtSum += item.moneyAmt;
				} else {						moneyAmtSum -= item.moneyAmt;
				}
			});
			//console.log(prevAmtSum, increSum, decreSum, currAmtSum);
			$("#moneyAmtSum").text(moneyAmtSum.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
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
		myPostData.stanYmd = $("#stanYmd").val().replaceAll("-", "");
	//	myPostData.acctGb = $("#acctGb").val();
	//	myPostData.inoutGb = $("#inoutGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
