
$(document).ready(function(){

	let preTotalAmt = 0;
	let moneyTotalAmt = 0;
	let outTotalAmt = 0;
	let inTotalAmt = 0;
						
	$.jqGrid($('#grid'), {
		url:contextRoot + "/sheet/yeardiffer/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd    : $("#stanYmd").val().replaceAll("-", "")
			, acctGb   : $("#acctGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'acctGb','구분','항목','전주이월금','금주수입','금주지출','차주이월금(시재)'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'       , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME' , width:70 , align:'center'
					}, { name: 'acctName'     , index: 'ACCT_NAME'    , width:40 , align:'center'
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
			
		/*	var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/sheet/inouttotal/getSum.json",
				datatype: "json",
				data: params,
				success:function(data){
					if(data.session == 1){
						$("#preTotalAmt").text(data.preTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#moneyTotalAmt").text(data.moneyTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#outTotalAmt").text(data.outTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#inTotalAmt").text(data.inTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
					}else{
						alert(data.msg);
					}
				},
				error:function(e){
					alert(e.responseText);
				}
			});	*/
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
		myPostData.acctGb  = $("#acctGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	$("#iMemberId").val("");
	$("#iMemberNo").val("");
	$("#iMemberName").val("");
	$("#iSexType").val("");
	$("#iTelNo").val("");
	$("#iHphoneNo").val("");
	$("#iPositionCode").val("");
	$("#iDeptCode").val("");
	$("#iRegionCode").val("");
	$("#iAddr").val("");
	$("#iFamilyRemark").val("");
}

function preModifyAction() {
}

function validate() {
	if($("#iMemberId").val().trim() == "") { alert("아이디를 입력하세요."); $("#iMemberId").focus(); return false; }
	if($("#iMemberNo").val().trim() == "") { alert("번호를 입력하세요."); $("#iMemberNo").focus(); return false; }
	return true;
}

function insertAction(obj) {
	
	var url = contextRoot + "/sheet/yeardiffer/insert.json";
	var params = "memberId=" + encodeURIComponent($("#iMemberId").val().trim());
		params += "&memberNo=" + encodeURIComponent($("#iMemberNo").val().trim());
		params += "&memberName=" + encodeURIComponent($("#iMemberName").val().trim());
		params += "&sexType=" + encodeURIComponent($("#iSexType").val());
		params += "&telNo=" + encodeURIComponent($("#iTelNo").val().trim());
		params += "&hphoneNo=" + encodeURIComponent($("#iHphoneNo").val().trim());
		params += "&positionCode=" + encodeURIComponent($("#iPositionCode").val());
		params += "&deptCode=" + encodeURIComponent($("#iDeptCode").val());
		params += "&regionCode=" + encodeURIComponent($("#iRegionCode").val());
		params += "&addr=" + encodeURIComponent($("#iAddr").val().trim());
		params += "&familyRemark=" + encodeURIComponent($("#iFamilyRemark").val().trim());

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			if(data.session == 1){
				$("#grid").trigger("reloadGrid");
				alert(data.msg);

				$.hideInsertForm();

			}else{
				alert(data.msg);
			}
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}

function updateAction(obj) {
	var url = contextRoot + "/sheet/yeardiffer/update.json";
	var params = "memberId=" + encodeURIComponent($("#iMemberId").val().trim());
		params += "&memberNo=" + encodeURIComponent($("#iMemberNo").val().trim());
		params += "&memberName=" + encodeURIComponent($("#iMemberName").val().trim());
		params += "&sexType=" + encodeURIComponent($("#iSexType").val());
		params += "&telNo=" + encodeURIComponent($("#iTelNo").val().trim());
		params += "&hphoneNo=" + encodeURIComponent($("#iHphoneNo").val().trim());
		params += "&positionCode=" + encodeURIComponent($("#iPositionCode").val());
		params += "&deptCode=" + encodeURIComponent($("#iDeptCode").val());
		params += "&regionCode=" + encodeURIComponent($("#iRegionCode").val());
		params += "&addr=" + encodeURIComponent($("#iAddr").val().trim());
		params += "&familyRemark=" + encodeURIComponent($("#iFamilyRemark").val().trim());

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			//alert("저장하였습니다.");
			alert(data.msg);
		},
		error:function(e){
			alert(e.responseText);
		}
	});
}

function deleteAction(obj) {
	var url = contextRoot + "/sheet/yeardiffer/delete.json";
	var params = "memberId=" + $("#iMemberId").val();
		params += "&memberNo=" + $("#iMemberNo").val();

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			alert(data.msg);
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
}

function deleteMultiAction(obj) {
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하십시오.");
		return false;
	}

	if(confirm("선택한 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/sheet/yeardiffer/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&memberInfo=" + list.memberId + ":" + list.memberNo;
	}

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			alert(data.msg);
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);

		}
	});

	return true;
}
