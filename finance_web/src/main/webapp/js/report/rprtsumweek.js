
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/report/sumweek/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd        : $("#stanYmd").val().replaceAll("-", "")
			, acctGb       : $("#acctGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'acctGb' , '재정' , 'moneyCode', '헌금', '지난주', '이번주', '누계'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'          , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'      , index: 'ACCT_GB_NAME' , width:100, align:'center'
					}, { name: 'moneyCode'       , index: 'MONEY_CODE'   , hidden:true
					}, { name: 'moneyName'       , index: 'MONEY_NAME'   , width:100, align:'center'
					}, { name: 'moneyAmtLastWeek', index: 'MONEY_AMT'    , width:200, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtLastWeek.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtThisWeek', index: 'MONEY_AMT'    , width:200, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtThisWeek.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtSumm'    , index: 'MONEY_AMT'    , width:200, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtSumm.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'ACCT_GB, MONEY_CODE',
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
			
			$("#jqgh_grid_moneyAmtLastWeek").html("지난주 ( "+data.durLastWeek+" )");
			$("#jqgh_grid_moneyAmtThisWeek").html("이번주 ( "+data.durThisWeek+" )");
			$("#jqgh_grid_moneyAmtSumm").html("누계 ( "+data.durSumm+" )");
			
			var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
				params += "&acctGb=" + $("#acctGb").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/report/sumweek/getSum.json",
				datatype: "json",
				data: params,
				success:function(data){
					if(data.session == 1){
						$("#lastWeekAmnt").text(data.lastWeekAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#thisWeekAmnt").text(data.thisWeekAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
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
		myPostData.stanYmd = $("#stanYmd").val().replaceAll("-", "");
		myPostData.acctGb = $("#acctGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
