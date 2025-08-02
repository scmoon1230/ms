
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/sheet/sumyear/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYy         : $("#stanYy").val()
			, stanYmd      : $("#stanYy").val()+'0101'
			, acctGb       : $("#acctGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'acctGb','재정','계정','계정명','구분','수입금액','지출금액'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'       , index: 'ACCT_GB'       , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME'  , width:50 , align:'center'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( '000000' == rowObject.acctCode ) 		return 'style="background-color:#58D3F7"';
						}
					}, { name: 'acctCode'     , index: 'ACCT_CODE'     , width:70 , align:'center'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( '000000' == rowObject.acctCode ) 		return 'style="background-color:#58D3F7"';
						}
					}, { name: 'printName'    , index: 'PRINT_NAME'    , width:70 , align:'left'
						, cellattr : function(cellvalue, options, rowObject) {
							if ( '대' == rowObject.acctLevel ) 		return 'style="background-color:#58D3F7"';
							else if ( '중' == rowObject.acctLevel ) 	return 'style="background-color:#E0F2F7"';
						}
					   , formatter : function(cellvalue, options, rowObject) {
							if ( '소' == rowObject.acctLevel ) return '&nbsp; &nbsp; &nbsp; &nbsp;' + rowObject.printName;
							else if ( '중' == rowObject.acctLevel ) return '&nbsp; &nbsp;' + rowObject.printName;
							else return rowObject.printName;
						}
					}, { name: 'inoutGb'      , index: 'INOUT_GB'      , width:40  , align:'center'
					}, { name: 'inAmt'        , index: 'IN_AMT'        , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.inAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'outAmt'       , index: 'OUT_AMT'       , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.outAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
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
			
			$("#duration").html(data.duration);
			
			var params = "stanYy=" + $("#stanYy").val();
				params += "&stanYmd=" + $("#stanYy").val()+"0101";
				params += "&acctGb=" + $("#acctGb").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/sheet/sumyear/getSum.json",
				datatype: "json",
				data: params,
				success:function(data){
					console.log(data);
					if(data.session == 1){
						$("#inTotalAmnt").text(data.inTotalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#outTotalAmnt").text(data.outTotalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
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
		myPostData.stanYy = $("#stanYy").val();
		myPostData.stanYmd = $("#stanYy").val()+"0101";
		myPostData.acctGb  = $("#acctGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
