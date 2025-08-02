
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/yearend/hometax/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYy : $("#stanYy").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'주민(사업자)번호','성명','금액'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'juminNo'     , index: 'JUMIN_NO'     , width:300, align:'center'
					}, { name: 'memberName'   , index: 'MEMBER_NAME'   , width:250, align:'center'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:300 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'JUMIN_NO',
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
		myPostData.stanYy = $("#stanYy").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".btnE").bind("click",function(){
		var year = new Date().getFullYear();
		let day1 = prompt('기부일자를 입력하세요.', (year-1)+'-12-28');
		//let day2 = prompt('신청일자를 입력하세요.', year+'-01-05');
		
		$("#paraDay1").val(day1);
		//$("#paraDay2").val(day2);
		$("#paraType").val("1");
		$("#paraOrg").val("405");
		$("#paraStanYy").val($("#stanYy").val());
		
		$("#sidx").val("JUMIN_NO");
		$("#sord").val("ASC");
		$("#titleKey").val("juminNo|memberName|type|org|day1|moneyAmt|moneyAmt|reqAmt");
		$("#titleHeader").val(encodeURIComponent("주민(사업자)번호|성명|기부내용구분|기부단체코드|기부일자|기부금액합계|공제대상기부금액|기부장려금신청금액"));
		url = "/yearend/hometax/excel.do";
		$("#dataForm").attr("action", url);
		$("#dataForm").attr("method", "post");
		$("#dataForm").submit();
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
