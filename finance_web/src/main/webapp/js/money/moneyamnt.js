
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/money/amnt/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd        : $("#stanYmd").val().replaceAll("-", "")
			, acctGb       : $("#acctGb").val()
			, moneyCode    : $("#moneyCode").val()
			, worshipCode  : $("#worshipCode").val()
			, userIdYn     : $('#userIdYn').is(':checked')
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','acctGb','재정','moneyCode','헌금','worshipCode','예배'
					,'userId','등록자','금액'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'      , index: 'STAN_YMD'      , width:100, align:'center'
					}, { name: 'acctGb'       , index: 'ACCT_GB'       , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME'  , width:70 , align:'center'
					}, { name: 'moneyCode'    , index: 'MONEY_CODE'    , hidden:true
					}, { name: 'moneyName'    , index: 'MONEY_NAME'    , width:100, align:'center'
					}, { name: 'worshipCode'  , index: 'WORSHIP_CODE'  , hidden:true
					}, { name: 'worshipName'  , index: 'WORSHIP_NAME'  , width:70 , align:'center'
					}, { name: 'userId'       , index: 'USER_ID'       , hidden:true
					}, { name: 'userName'     , index: 'USER_NAME'     , width:70 , align:'center'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'USER_NAME',
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
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);

			$("#dMemberId").html(list.memberId);
			$("#dMemberNo").html(list.memberNo);
			$("#dMemberName").html(list.memberName);
			$("#dSexType").html(list.sexType);
			$("#dTelNo").html(list.telNo);
			$("#dHphoneNo").html(list.hphoneNo);
			$("#dPositionCode").html(list.positionName);
			$("#dDeptCode").html(list.deptName);
			$("#dRegionCode").html(list.regionName);
			$("#dAddr").html(list.addr);
			$("#dFamilyRemark").html(list.familyRemark);

			$("#iMemberId").val(list.memberId);
			$("#iMemberNo").val(list.memberNo);
			$("#iMemberName").val(list.memberName);
			$("#iSexType").val(list.sexType);
			$("#iTelNo").val(list.telNo);
			$("#iHphoneNo").val(list.hphoneNo);
			$("#iPositionCode").val(list.positionCode);
			$("#iDeptCode").val(list.deptCode);
			$("#iRegionCode").val(list.regionCode);
			$("#iAddr").val(list.addr);
			$("#iFamilyRemark").val(list.familyRemark);

			$.showDetail();
		},
		loadComplete : function (data) {
			
			var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
				params += "&acctGb=" + $("#acctGb").val();
				params += "&moneyCode=" + $("#moneyCode").val();
				params += "&worshipCode=" + $("#worshipCode").val();
				params += "&userIdYn=" + $('#userIdYn').is(':checked');
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/money/amnt/getSum.json",
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
		myPostData.acctGb = $("#acctGb").val();
		myPostData.moneyCode = $("#moneyCode").val();
		myPostData.worshipCode = $("#worshipCode").val();
		myPostData.userIdYn = $('#userIdYn').is(':checked');
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function changeMoneyCode() {
	let stanYy = $("#stanYmd").val().replaceAll("-", "").substring(0,4);
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
	
	var url = contextRoot + "/money/amnt/insert.json";
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
	var url = contextRoot + "/money/amnt/update.json";
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
	var url = contextRoot + "/money/amnt/delete.json";
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
	var url = contextRoot + "/money/amnt/deleteMulti.json";
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
