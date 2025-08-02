
$(document).ready(function(){

	let preTotalAmt = 0;
	let moneyTotalAmt = 0;
	let outTotalAmt = 0;
	let inTotalAmt = 0;
						
	$.jqGrid($('#grid'), {
		url:contextRoot + "/sheet/inouttotal/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd    : $("#stanYmd").val().replaceAll("-", "")
		//	, acctGb   : $("#acctGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'acctGb','구분','항목','전주이월금','금주수입','금주지출','차주이월금(시재)'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'       , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME' , width:40 , align:'center'
					}, { name: 'acctName'     , index: 'ACCT_NAME'    , width:70 , align:'center'
					}, { name: 'preAmt'       , index: 'PRE_AMT'      , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( rowObject.preAmt == null ) { return '0'; }
							else {
								preTotalAmt += rowObject.preAmt;
								return rowObject.preAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}, { name: 'moneyAmt'       , index: 'MONEY_AMT'       , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( rowObject.moneyAmt == null ) { return '0'; }
							else {
								moneyTotalAmt += rowObject.moneyAmt;
								return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}, { name: 'outAmt'        , index: 'OUT_AMT'        , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( rowObject.outAmt == null ) { return '0'; }
							else {
								outTotalAmt += rowObject.outAmt;
								return rowObject.outAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}, { name: 'inAmt'       , index: 'IN_AMT'       , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( rowObject.inAmt == null ) { return '0'; }
							else {
								let val = rowObject.preAmt + rowObject.inAmt - rowObject.outAmt;
								inTotalAmt += val;
								return val.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
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
			
			$("#preTotalAmt").text(preTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			$("#moneyTotalAmt").text(moneyTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			$("#outTotalAmt").text(outTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			$("#inTotalAmt").text(inTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
			
        },
		beforeRequest: function() {
			$.loading(true);
			rowNum = $('#rowPerPageList').val();
			
			preTotalAmt = 0;
			moneyTotalAmt = 0;
			outTotalAmt = 0;
			inTotalAmt = 0;
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

	$(".btnP").bind("click",function(){
		let popupW = 800;
		let popupH = window.screen.height;
		let left = Math.ceil((window.screen.width - popupW)/2);
		let top = 0;
		window.open('/sheet/inouttotalPrint.do','인쇄','width='+popupW+',height='+popupH+',left='+left+',top='+top);
		
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
