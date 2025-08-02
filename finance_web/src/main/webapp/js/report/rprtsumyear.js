
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/report/sumyear/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYy         : $("#stanYy").val()
			, acctGb       : $("#acctGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'acctGb' , '재정' , 'moneyCode','헌금','12월','1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월'
					,'누계'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'       , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME' , width:50 , align:'center'
					}, { name: 'moneyCode'    , index: 'MONEY_CODE'   , hidden:true
					}, { name: 'moneyName'    , index: 'MONEY_NAME'   , width:100, align:'center'
					}, { name: 'moneyAmtMonth0', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth0.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth1', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth1.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth2', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth2.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth3', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth3.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth4', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth4.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth5', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth5.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth6', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth6.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth7', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth7.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth8', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth8.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth9', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth9.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth10', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth10.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtMonth11', index: 'MONEY_AMT'    , width:100, align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmtMonth11.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyAmtSumm', index: 'MONEY_AMT'    , hidden:true
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
			
			$("#jqgh_grid_moneyAmtSumm").html("합계 ( "+data.durYear+" )");
			$("#durYear").html(data.durYear);
			
			var params = "stanYy=" + $("#stanYy").val();
				params += "&acctGb=" + $("#acctGb").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/report/sumyear/getSum.json",
				datatype: "json",
				data: params,
				success:function(data){
					if(data.session == 1){
						$("#monthAmnt0").text(data.monthAmnt0.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt1").text(data.monthAmnt1.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt2").text(data.monthAmnt2.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt3").text(data.monthAmnt3.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt4").text(data.monthAmnt4.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt5").text(data.monthAmnt5.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt6").text(data.monthAmnt6.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt7").text(data.monthAmnt7.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt8").text(data.monthAmnt8.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt9").text(data.monthAmnt9.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt10").text(data.monthAmnt10.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#monthAmnt11").text(data.monthAmnt11.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
					//	$("#totalAmnt").text(data.totalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
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
		myPostData.stanYy = $("#stanYy").val();
		myPostData.acctGb = $("#acctGb").val();
		
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
	
	var url = contextRoot + "/report/sumyear/insert.json";
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
	var url = contextRoot + "/report/sumyear/update.json";
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
	var url = contextRoot + "/report/sumyear/delete.json";
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
	var url = contextRoot + "/report/sumyear/deleteMulti.json";
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
