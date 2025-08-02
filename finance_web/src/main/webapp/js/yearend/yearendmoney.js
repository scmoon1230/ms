
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/yearend/money/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			startDate      : $("#startDate").val().replaceAll("-", "")
			, endDate      : $("#endDate").val().replaceAll("-", "")
			//, acctGb       : $("#acctGb").val()
			//, moneyCode    : $("#moneyCode").val()
			//, worshipCode  : $("#worshipCode").val()
			//, positionCode : $("#positionCode").val()
			//, deptCode     : $("#deptCode").val()
			//, regionCode   : $("#regionCode").val()
			, memberNo     : $("#memberNo").val()
			, memberName   : $("#memberName").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'신도번호','신도성명','십일조','감사헌금','선교헌금','기타','합계'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'memberNo'     , index: 'MEMBER_NO'     , width:200, align:'center'
					}, { name: 'memberName'   , index: 'MEMBER_NAME'   , width:250, align:'center'
					}, { name: 'amt1'     , index: 'AMT1'     , width:300 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.amt1.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'amt2'     , index: 'AMT2'     , width:300 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.amt2.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'amt4'     , index: 'AMT4'     , width:300 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.amt4.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'amt9'     , index: 'AMT9'     , width:300 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.amt9.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'total'     , index: 'TOTAL'     , width:350 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.total.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'MEMBER_NO',
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
		myPostData.startDate = $("#startDate").val().replaceAll("-", "");
		myPostData.endDate   = $("#endDate").val().replaceAll("-", "");
		//myPostData.acctGb = $("#acctGb").val();
		//myPostData.moneyCode = $("#moneyCode").val();
		//myPostData.worshipCode = $("#worshipCode").val();
		//myPostData.positionCode = $("#positionCode").val();
		//myPostData.deptCode = $("#deptCode").val();
		//myPostData.regionCode = $("#regionCode").val();
		myPostData.memberNo = $("#memberNo").val();
		myPostData.memberName = $("#memberName").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$("#startDate").bind("change", function() {
		$(".btnS").trigger("click");
	});
	$("#endDate").bind("change", function() {
		var endDate = $("#endDate").val();
		var currentDay = $("#currentDay").val();
		if (endDate <= currentDay) {
			//return true;
			$(".btnS").trigger("click");
		}
		else {
			alert($("#currentDay").val()+" 까지만 조회 가능 합니다.");
			//$("#endDate").val("${endDate}");
			$("#endDate").val($("#currentDay").val());
			return false;
		}
	});
	
	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
