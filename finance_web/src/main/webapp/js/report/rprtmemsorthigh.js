
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/report/memsorthigh/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			startDate      : $("#startDate").val().replaceAll("-", "")
			, endDate      : $("#endDate").val().replaceAll("-", "")
			, moneyCode    : $("#moneyCode").val()
			, worshipCode  : $("#worshipCode").val()
			, idExist      : $("#idExist").val()
			, moneyAmt     : $("#moneyAmt").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'No','기준일자','moneyCode','헌금','신도성명','금액','positionCode','직분','신도아이디','신도번호'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'rk'           , index: 'RK'            , hidden:true
					}, { name: 'stanYmd'      , index: 'STAN_YMD'      , width:100, align:'center'
					}, { name: 'moneyCode'    , index: 'MONEY_CODE'    , hidden:true
					}, { name: 'moneyName'    , index: 'MONEY_NAME'    , width:100, align:'center'
					}, { name: 'memberName'   , index: 'MEMBER_NAME'   , width:100, align:'center'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'positionCode' , index: 'POSITION_CODE' , hidden:true
					}, { name: 'positionName' , index: 'POSITION_NAME' , width:50 , align:'center'
					}, { name: 'memberId'     , index: 'MEMBER_ID'     , width:150, align:'center'
					}, { name: 'memberNo'     , index: 'MEMBER_NO'     , width:100, align:'center'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: sortBy,
		sortorder: sortOr,
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
		myPostData.startDate    = $("#startDate").val().replaceAll("-", "");
		myPostData.endDate      = $("#endDate").val().replaceAll("-", "");
		myPostData.moneyCode    = $("#moneyCode").val();
		myPostData.worshipCode  = $("#worshipCode").val();
		myPostData.idExist      = $("#idExist").val();
		myPostData.moneyAmt     = $("#moneyAmt").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".btnE").bind("click",function(){
		$("#paraStartDate").val($("#startDate").val().replaceAll("-", ""));
		$("#paraEndDate").val($("#endDate").val().replaceAll("-", ""));
		$("#paraMoneyCode").val($("#moneyCode").val());
		$("#paraWorshipCode").val($("#worshipCode").val());
		$("#paraIdExist").val($("#idExist").val());
		$("#paraMoneyAmt").val($("#moneyAmt").val());
		
		$("#sidx").val(sortBy);
		$("#sord").val("ASC");
		$("#titleKey").val("stanYmd|moneyName|memberName|moneyAmt");
		$("#titleHeader").val(encodeURIComponent("일자|헌금|성명|금액"));
		url = "/report/memsorthigh/excel.do";
		$("#dataForm").attr("action", url);
		$("#dataForm").attr("method", "post");
		$("#dataForm").submit();
	});

	$(".btnP").bind("click",function(){
		let popupW = 800;
		let popupH = window.screen.height;
		let left = Math.ceil((window.screen.width - popupW)/2);
		let top = 0;
		window.open('/report/memsorthighPrint.do','인쇄','width='+popupW+',height='+popupH+',left='+left+',top='+top);
		
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
