
$(document).ready(function(){

	if ( '1' == type ) {
		$("#subtitle").text('마감원장(종교인과세) > 전표마(종교인과세)');
	} else if ( '2' == type ) {
		$("#subtitle").text('');
		$(".btnWrap").hide();
	}

	$.jqGrid($('#grid'), {
		url:contextRoot + "/sheet2/mng/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd    : $("#stanYmd").val().replaceAll("-", "")
			//, acctGb   : $("#acctGb").val()
			, acctGb   : '1'
			, inoutGb  : $("#inoutGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','acctGb','재정','전표번호','계정','계정명','금액'
					,'연결계정','연결계정명','내역','구분','userId','등록자'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'      , index: 'STAN_YMD'     , width:70 , align:'center'
					}, { name: 'acctGb'       , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME' , hidden:true
					}, { name: 'seqNo'        , index: 'SEQ_NO'       , width:80 , align:'center'
						, formatter : function(cellvalue, options, rowObject) {
							let val = rowObject.seqNo.toString();
							return val.substr(0,8)+'-'+val.substr(8);
						}
					}, { name: 'acctCode'     , index: 'ACCT_CODE'    , width:50 , align:'center'
					}, { name: 'acctName'     , index: 'ACCT_NAME'    , width:70 , align:'left'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'    , width:70 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'linkAcctCode' , index: 'LINK_ACCT_CODE', width:50, align:'center'
					}, { name: 'linkName'     , index: 'LINK_NAME'    , width:100, align:'left'
					}, { name: 'acctRemark'   , index: 'ACCT_REMARK'  , width:200, align:'left'
					}, { name: 'inoutGb'      , index: 'INOUT_GB'     , width:30 , align:'center'
					}, { name: 'userId'       , index: 'USER_ID'      , hidden:true
					}, { name: 'userName'     , index: 'USER_NAME'    , width:50 , align:'center'
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: sortBy,
		sortorder: 'DESC',
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
			
			$("#startYmd").html(data.startYmd);
			$("#endYmd").html(data.endYmd);
			
			var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
				//params += "&acctGb=" + $("#acctGb").val();
				params += "&acctGb=" + '1';
				params += "&inoutGb=" + $("#inoutGb").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/sheet2/mng/getSum.json",
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
		myPostData.stanYmd = $("#stanYmd").val().replaceAll("-", "");
		//myPostData.acctGb  = $("#acctGb").val();
		myPostData.acctGb  = '1';
		myPostData.inoutGb = $("#inoutGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
/*
function doClose() {
	
	//if($("#acctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#acctGb").focus(); return false; }
	if($("#rowCnt").text().trim() == "0") { alert("등록된 전표가 없습니다."); return false; }
	
	if ( !confirm("전표를 마감처리하시겠습니까?") ) return;
	
	var url = contextRoot + "/sheet2/mng/doClose.json";
	var params = "stanYmd="     + $("#stanYmd").val().replaceAll("-", "");
		//params += "&acctGb="    + $("#acctGb").val();
	
	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){
			if(data.session == 1){
				$("#grid").trigger("reloadGrid");
				alert(data.msg);
				//$.hideInsertForm();
				//resetAction();
			}else{
				alert(data.msg);
			}
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}

function cancelClose() {
	
	//if($("#acctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#acctGb").focus(); return false; }
	
	if ( !confirm("전표마감을 취소하시겠습니까?") ) return;
	
	var url = contextRoot + "/sheet2/mng/cancelClose.json";
	var params = "stanYmd="     + $("#stanYmd").val().replaceAll("-", "");
		//params += "&acctGb="    + $("#acctGb").val();
	
	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){
			if(data.session == 1){
				$("#grid").trigger("reloadGrid");
				alert(data.msg);
				//$.hideInsertForm();
				//resetAction();
			}else{
				alert(data.msg);
			}
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}
*/