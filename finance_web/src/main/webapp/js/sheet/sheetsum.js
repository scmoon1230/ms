
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/sheet/sum/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd    : $("#stanYmd").val().replaceAll("-", "")
			, acctGb   : $("#acctGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'재정','acctCode','내역','금액'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'       , index: 'ACCT_GB'       , width:50  , align:'center'
					}, { name: 'acctCode'     , index: 'ACCT_CODE'     , hidden:true
					}, { name: 'acctName'     , index: 'ACCT_NAME'     , width:200 , align:'center'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( null != rowObject.acctName && null == rowObject.acctCode )
								return 'style="background-color:#58D3F7"';
							else if ( 'PREV' == rowObject.acctCode )  return 'style="background-color:#E0F2F7"';
							else if ( 'NEXT' == rowObject.acctCode )  return 'style="background-color:#E0F2F7"';
							else if ( 'ASSET' == rowObject.acctCode ) return 'style="background-color:#fdffa7"';
							else if ( 'IN' == rowObject.acctCode )    return 'style="background-color:#58D3F7"';
							else if ( 'OUT' == rowObject.acctCode )   return 'style="background-color:#58D3F7"';
						}
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:80 , align:'right'
						, cellattr : function(cellvalue, options, rowObject) {
								return 'style="padding-right: 5px;"';
						}
						, formatter : function(cellvalue, options, rowObject) {
							if ( null == rowObject.acctCode ) return '';
							else return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'IO_ORDER , ORD',
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
		myPostData.acctGb  = $("#acctGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".btnP").bind("click",function(){
		let popupW = 800;
		let popupH = window.screen.height;
		let left = Math.ceil((window.screen.width - popupW)/2);
		let top = 0;
		window.open('/sheet/sumPrint.do','인쇄','width='+popupW+',height='+popupH+',left='+left+',top='+top);
		
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
