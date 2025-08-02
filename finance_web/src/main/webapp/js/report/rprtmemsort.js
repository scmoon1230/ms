
$(document).ready(function(){

	if ( '1' == type ) {
		$("#subtitle").text('헌금자명단(금액순)');
	} else if ( '2' == type ) {
		$("#subtitle").text('헌금자명단(직분/금액순)');
	//} else if ( '3' == type ) {
	//	$("#subtitle").text('헌금자명단(금액헤더)');
	}

	$.jqGrid($('#grid'), {
		url:contextRoot + "/report/memsort/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd        : $("#stanYmd").val().replaceAll("-", "")
		//	, acctGb       : $("#acctGb").val()
			, moneyCode    : $("#moneyCode").val()
			, worshipCode  : $("#worshipCode").val()
		//	, positionCode : $("#positionCode").val()
		//	, deptCode     : $("#deptCode").val()
		//	, regionCode   : $("#regionCode").val()
		//	, memberNo     : $("#memberNo").val()
		//	, memberName   : $("#memberName").val()
			, idExist      : $("#idExist").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','금액','신도성명','positionCode','직분','신도아이디','신도번호'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'      , index: 'STAN_YMD'      , width:100, align:'center'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'memberName'   , index: 'MEMBER_NAME'   , width:100, align:'center'
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
			
			var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
			//	params += "&acctGb=" + $("#acctGb").val();
				params += "&moneyCode=" + $("#moneyCode").val();
				params += "&worshipCode=" + $("#worshipCode").val();
			//	params += "&positionCode=" + $("#positionCode").val();
			//	params += "&deptCode=" + $("#deptCode").val();
			//	params += "&regionCode=" + $("#regionCode").val();
			//	params += "&memberNo=" + $("#memberNo").val();
			//	params += "&memberName=" + $("#memberName").val();
				params += "&idExist=" + $("#idExist").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/report/memsort/getSum.json",
				datatype: "json",
				data: params,
				success:function(data){
					if(data.session == 1){
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
		myPostData.stanYmd      = $("#stanYmd").val().replaceAll("-", "");
	//	myPostData.acctGb       = $("#acctGb").val();
		myPostData.moneyCode    = $("#moneyCode").val();
		myPostData.worshipCode  = $("#worshipCode").val();
	//	myPostData.positionCode = $("#positionCode").val();
	//	myPostData.deptCode     = $("#deptCode").val();
	//	myPostData.regionCode   = $("#regionCode").val();
	//	myPostData.memberNo     = $("#memberNo").val();
	//	myPostData.memberName   = $("#memberName").val();
	//	myPostData.userIdYn     = $('#userIdYn').is(':checked');
		myPostData.idExist      = $("#idExist").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
