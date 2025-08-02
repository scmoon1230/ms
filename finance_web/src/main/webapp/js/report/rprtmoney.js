
$(document).ready(function(){
	
	if ( '1' == type ) {
		$("#subtitle").text('개인별헌금확인(유ID)');
	} else if ( '2' == type ) {
		$("#subtitle").text('개인별헌금확인(무ID포함)');
//	} else if ( '3' == type ) {
//		$("#subtitle").text('고액헌금자명단');
	}

	$.jqGrid($('#grid'), {
		url:contextRoot + "/report/money/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			startDate      : $("#startDate").val().replaceAll("-", "")
			, endDate      : $("#endDate").val().replaceAll("-", "")
			, acctGb       : $("#acctGb").val()
			, moneyCode    : $("#moneyCode").val()
			, worshipCode  : $("#worshipCode").val()
			, positionCode : $("#positionCode").val()
			, deptCode     : $("#deptCode").val()
			, regionCode   : $("#regionCode").val()
			, memberNo     : $("#memberNo").val()
			, memberName   : $("#memberName").val()
			, memberNoFlag : $("#memberNoFlag").val()
		//	, moneyAmt     : $("#moneyAmt").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','신도아이디','신도번호','신도성명','금액','acctGb','재정','moneyCode','헌금','worshipCode','예배'
					,'positionCode','직분','deptCode','교구','regionCode','구역','가족사항','userId','등록자','등록일시'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'      , index: 'STAN_YMD'      , width:100, align:'center'
					}, { name: 'memberId'     , index: 'MEMBER_ID'     , width:120, align:'center'
					}, { name: 'memberNo'     , index: 'MEMBER_NO'     , width:80, align:'center'
					}, { name: 'memberName'   , index: 'MEMBER_NAME'   , width:80, align:'center'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'acctGb'       , index: 'ACCT_GB'       , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME'  , width:70 , align:'center'
					}, { name: 'moneyCode'    , index: 'MONEY_CODE'    , hidden:true
					}, { name: 'moneyName'    , index: 'MONEY_NAME'    , width:100, align:'center'
					}, { name: 'worshipCode'  , index: 'WORSHIP_CODE'  , hidden:true
					}, { name: 'worshipName'  , index: 'WORSHIP_NAME'  , width:70 , align:'center'
					}, { name: 'positionCode' , index: 'POSITION_CODE' , hidden:true
					}, { name: 'positionName' , index: 'POSITION_NAME' , width:50 , align:'center'
					}, { name: 'deptCode'     , index: 'DEPT_CODE'     , hidden:true
					}, { name: 'deptName'     , index: 'DEPT_NAME'     , width:50 , align:'center'
					}, { name: 'regionCode'   , index: 'REGION_CODE'   , hidden:true
					}, { name: 'regionName'   , index: 'REGION_NAME'   , width:50 , align:'center'
					}, { name: 'familyRemark' , index: 'FAMILY_REMARK' , width:150 , align:'left'
					}, { name: 'userId'       , index: 'USER_ID'       , hidden:true
					}, { name: 'userName'     , index: 'USER_NAME'     , width:70 , align:'center'
					}, { name: 'inputTime'    , index: 'INPUT_TIME'    , width:120, align:'center'
						, formatter : function(cellvalue, options, rowObject) {
							var strYmdhms = rowObject.inputTime;
							var momentYmdhms = moment(strYmdhms, 'YYYYMMDDHHmmss');
							if (momentYmdhms.isValid()) {
								return momentYmdhms.format('YYYY-MM-DD HH:mm:ss');
							} else {
								return '';
							}
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: sortBy,
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
			
			var params = "startDate=" + $("#startDate").val().replaceAll("-", "");
				params += "&endDate=" + $("#endDate").val().replaceAll("-", "");
				params += "&acctGb=" + $("#acctGb").val();
				params += "&moneyCode=" + $("#moneyCode").val();
				params += "&worshipCode=" + $("#worshipCode").val();
				params += "&positionCode=" + $("#positionCode").val();
				params += "&deptCode=" + $("#deptCode").val();
				params += "&regionCode=" + $("#regionCode").val();
				params += "&memberNo=" + $("#memberNo").val();
				params += "&memberName=" + $("#memberName").val();
				params += "&memberNoFlag=" + $("#memberNoFlag").val();
			//	params += "&moneyAmt=" + $("#moneyAmt").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/report/money/getSum.json",
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
		myPostData.startDate    = $("#startDate").val().replaceAll("-", "");
		myPostData.endDate      = $("#endDate").val().replaceAll("-", "");
		myPostData.acctGb       = $("#acctGb").val();
		myPostData.moneyCode    = $("#moneyCode").val();
		myPostData.worshipCode  = $("#worshipCode").val();
		myPostData.positionCode = $("#positionCode").val();
		myPostData.deptCode     = $("#deptCode").val();
		myPostData.regionCode   = $("#regionCode").val();
		myPostData.memberNo     = $("#memberNo").val();
		myPostData.memberName   = $("#memberName").val();
		myPostData.memberNoFlag = $("#memberNoFlag").val();
	//	myPostData.moneyAmt     = $("#moneyAmt").val();
		
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
			//$(".btnS").trigger("click");
		}
		else {
			//alert($("#currentDay").val()+" 까지만 조회 가능 합니다.");
			//$("#endDate").val("${endDate}");
			//$("#endDate").val($("#currentDay").val());
			//return false;
		}
		$(".btnS").trigger("click");
	});
	
	$(".tableType1").css('height', window.innerHeight - 212);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 242);
	
});

function changeMoneyCode() {
	let stanYy = $("#startDate").val().replaceAll("-", "").substring(0,4);
	let acctGb = $("#acctGb").val();
	resetMoneyCode(stanYy, acctGb);
}

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
	
	var url = contextRoot + "/report/money/insert.json";
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
	var url = contextRoot + "/report/money/update.json";
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
	var url = contextRoot + "/report/money/delete.json";
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
	var url = contextRoot + "/report/money/deleteMulti.json";
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
