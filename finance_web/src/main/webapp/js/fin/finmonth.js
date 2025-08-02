
$(document).ready(function(){

	let prevTotalAmt = 0;
	let plusTotalAmt = 0;
	let minusTotalAmt = 0;
	let lastTotalAmt = 0;
						
	$.jqGrid($('#grid'), {
		url:contextRoot + "/fin/month/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd    : $("#stanYmd").val().replaceAll("-", "")
		//	, acctGb   : $("#acctGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'acctGb','구분','항목','전월잔액','증가','감소','금월잔액'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'       , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME' , width:70 , align:'center'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( rowObject.acctGbName == '전체' )		return 'style="background-color:#58D3F7"';
						}
					}, { name: 'assetGbName'     , index: 'ASSET_GB_NAME'    , width:40 , align:'center'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( rowObject.acctGbName == '전체' )			return 'style="background-color:#58D3F7"';
							else if ( rowObject.assetGbName == '소계' )	return 'style="background-color:#E0F2F7"';
						}
					}, { name: 'prevAmt'       , index: 'PREV_AMT'      , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( rowObject.prevAmt == null ) { return '0'; }
							else {
								prevTotalAmt += rowObject.prevAmt;
								return rowObject.prevAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}, { name: 'plusAmt'       , index: 'PLUS_AMT'       , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( rowObject.plusAmt == null ) { return '0'; }
							else {
								plusTotalAmt += rowObject.plusAmt;
								return rowObject.plusAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}, { name: 'minusAmt'        , index: 'MINUS_AMT'        , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( rowObject.minusAmt == null ) { return '0'; }
							else {
								minusTotalAmt += rowObject.minusAmt;
								return rowObject.minusAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}, { name: 'lastAmt'       , index: 'LAST_AMT'       , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( rowObject.lastAmt == null ) { return '0'; }
							else {
								lastTotalAmt += rowObject.lastAmt;
								return rowObject.lastAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'ACCT_GB, ASSET_GB',
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
			
			$("#prevTotalAmt").text(prevTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			$("#plusTotalAmt").text(plusTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			$("#minusTotalAmt").text(minusTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			$("#lastTotalAmt").text(lastTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			
        },
		beforeRequest: function() {
			$.loading(true);
			rowNum = $('#rowPerPageList').val();
			
			prevTotalAmt = 0;
			plusTotalAmt = 0;
			minusTotalAmt = 0;
			lastTotalAmt = 0;
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
	//	myPostData.acctGb  = $("#acctGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
