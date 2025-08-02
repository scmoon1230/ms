
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/userlog/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			startDate : $("#startDate").val().replaceAll("-", "")+'000000'
			, endDate   : $("#endDate").val().replaceAll("-", "")+'235959'
			, userName  : $("#userName").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '기준일' ,  '접속시각', '화면명' , '화면경로' , '성명', '사용자ID'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'  , index: 'STAN_YMD'  , width:120, align:'center', hidden:true
					}, { name: 'useTime'  , index: 'USE_TIME'  , width:200, align:'center'
					}, { name: 'sgroupNm' , index: 'SGROUP_NM' , width:200, align:'center'
					}, { name: 'progId'   , index: 'PROG_ID'   , width:200, align:'center', hidden:true
					}, { name: 'userName' , index: 'USER_NAME' , width:200, align:'center'
					}, { name: 'userId'   , index: 'USER_ID'   , width:200, align:'center'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'USE_TIME',
		sortorder: 'DESC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "POSITION_CODE",
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
		myPostData.startDate = $("#startDate").val().replaceAll("-", "")+'000000';
		myPostData.endDate   = $("#endDate").val().replaceAll("-", "")+'235959';
		myPostData.userName   = $("#userName").val();

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
