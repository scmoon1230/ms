
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/money/region/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd        : $("#stanYmd").val().replaceAll("-", "")
			, moneyCode    : $("#moneyCode").val()
			, worshipCode  : $("#worshipCode").val()
			, deptCode     : $("#deptCode").val()
			, regionCode   : $("#regionCode").val()
			, userIdYn     : $('#userIdYn').is(':checked')
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','순번','헌금자아이디','금액','moneyCode','헌금','worshipCode','예배'
					,'deptCode','교구','regionCode','구역','마감','userId','등록자','등록일시'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'      , index: 'STAN_YMD'      , width:100, align:'center'
					}, { name: 'detSeq'       , index: 'DET_SEQ'       , width:30 , align:'center'
					}, { name: 'memberId'     , index: 'MEMBER_ID'     , width:150, align:'center'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyCode'    , index: 'MONEY_CODE'    , hidden:true
					}, { name: 'moneyName'    , index: 'MONEY_NAME'    , width:100, align:'center'
					}, { name: 'worshipCode'  , index: 'WORSHIP_CODE'  , hidden:true
					}, { name: 'worshipName'  , index: 'WORSHIP_NAME'  , width:100, align:'center'
					}, { name: 'deptCode'     , index: 'DEPT_CODE'     , hidden:true
					}, { name: 'deptName'     , index: 'DEPT_NAME'     , width:70 , align:'center'
					}, { name: 'regionCode'   , index: 'REGION_CODE'   , hidden:true
					}, { name: 'regionName'   , index: 'REGION_NAME'   , width:70 , align:'center'
					}, { name: 'closeYn'      , index: 'CLOSE_YN'      , width:40 , align:'center'
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
		sortname: 'DET_SEQ',
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
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);

			$("#dStanYmd").html(list.stanYmd);
			$("#dMoneyName").html(list.moneyName);
			$("#dDetSeq").html(list.detSeq);
			$("#dWorshipName").html(list.worshipName);
			$("#dMemberId").html(list.memberId);
			$("#dDeptCode").html(list.deptName);
			$("#dRegionCode").html(list.regionName);
			$("#dMoneyAmt").html(list.moneyAmt);
			$("#dMoneyCode").val(list.moneyCode);
			if ( "N"==list.closeYn ) {	
				$(".btnMd").removeClass('hide');
				$(".btnDe").removeClass('hide');
			} else {
				$(".btnMd").addClass('hide');
				$(".btnDe").addClass('hide');
			}

			$("#uStanYmd").html(list.stanYmd);
			$("#uMoneyName").html(list.moneyName);
			$("#uDetSeq").html(list.detSeq);
			$("#uWorshipName").html(list.worshipName);
			$("#uMemberId").html(list.memberId);
			$("#uDeptCode").html(list.deptName);
			$("#uRegionCode").html(list.regionName);
			$("#uMoneyCode").val(list.moneyCode);
			$("#uMoneyAmt").val(list.moneyAmt);

			$.showDetail();
		},
		loadComplete : function (data) {
				
			var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
				params += "&moneyCode=" + $("#moneyCode").val();
				params += "&worshipCode="  + $("#worshipCode").val();
				params += "&deptCode=" + $("#deptCode").val();
				params += "&regionCode=" + $("#regionCode").val();
				params += "&userIdYn="     + $('#userIdYn').is(':checked');
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/money/region/getSum.json",
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
		myPostData.moneyCode    = $("#moneyCode").val();
		myPostData.worshipCode  = $("#worshipCode").val();
		myPostData.deptCode     = $("#deptCode").val();
		myPostData.regionCode   = $("#regionCode").val();
		myPostData.userIdYn     = $('#userIdYn').is(':checked');
		
		$("#grid").trigger("reloadGrid");
		
		$("#iMoneyCode").val($("#moneyCode").val());
		$("#iWorshipCode").val($("#worshipCode").val());
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
		
	
	
	
	//헌금액수 입력
	$(".checkMoney").bind("focus", function(e) {
		if ( $('#autoYn').is(':checked') && $("#autoAct1").is(":checked") ) {
			$(this).val($('#autoMoney').val());
		}
	});
//	$(".checkMoney").bind("keydown", function(e) {
//		if(e.keyCode == 13) { checkMoneyAmt($(this)); }
//	});
	$(".txtType").bind("keydown", function(e) {
		if(e.keyCode != 13) {
			if ( 0<=e.key && e.key<=9 ) {
				if ( "iDeptCode" == $(this).attr("id") ) {
					if ( $(this).val().length == 2 ) {
						$('#iRegionCode').focus();
					}
				} else if ( "iRegionCode" == $(this).attr("id") ) {
					if ( $(this).val().length == 3 ) {
						$('#iMoneyAmt').focus();
					}
				}
			}
		} else if(e.keyCode == 13) {
			if ( "iDeptCode" == $(this).attr("id") ) {
				if ( "" != $(this).val() )	$("#iRegionCode").focus();	
			} else if ( "iRegionCode" == $(this).attr("id") ) {
				if ( "" != $(this).val() )	$("#iMoneyAmt").focus();	
			} else if ( "iMoneyAmt" == $(this).attr("id") ) {
				val = $(this).val().trim().replaceAll(',','');
				if ( val == '' || isNaN(val) ) {
					alert('숫자를 입력하십시오.');	$(this).focus();
				} else {
					if ( $('#autoYn').is(':checked') && $("#autoAct2").is(":checked") ) {
						val = val * Number($('#autoMoney').val());
					}
					$(this).val(val.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
					$('#btnReg').focus();
				}
			} else if ( "uMoneyAmt" == $(this).attr("id") ) {
				val = $(this).val().trim().replaceAll(',','');
				if ( val == '' || isNaN(val) ) {
					alert('숫자를 입력하십시오.');	$(this).focus();
				} else {
					$('#btnMod').focus();
				}
			}
		 }
	});
		
});

function resetAction() {
	$("#iDeptCode").val("");
	$("#iRegionCode").val("");
	$("#iMoneyAmt").val("");
	$("#iDeptCode").focus();
}

function preModifyAction() {

}

function validate() {
	if ( insertFlag ) {
		if($("#iDeptCode").val().trim() == "") { alert("교구를 입력하세요."); $("#iDeptCode").focus(); return false; }
		if($("#iRegionCode").val().trim() == "") { alert("구역을 입력하세요."); $("#iRegionCode").focus(); return false; }
		if($("#iMoneyAmt").val().trim() == "") { alert("금액을 입력하세요."); $("#iMoneyAmt").focus(); return false; }
	} else {
		if($("#uMoneyAmt").val().trim() == "") { alert("금액을 입력하세요."); $("#uMoneyAmt").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	var url = contextRoot + "/money/region/insert.json";
	var params = "stanYmd="        + $("#stanYmd").val().replaceAll("-", "");
		params += "&moneyCode="    + $("#iMoneyCode").val();
		params += "&worshipCode="  + $("#iWorshipCode").val();
		params += "&deptCode="     + encodeURIComponent($("#iDeptCode").val());
		params += "&regionCode="   + encodeURIComponent($("#iRegionCode").val());
		params += "&moneyAmt="     + encodeURIComponent($("#iMoneyAmt").val().replaceAll(',',''));

	setTimeout(function() {
		$.ajaxEx($('#grid'), {
			url : url,
			datatype: "json",
			data: params,
			success:function(data){
				if(data.session == 1){
					$("#grid").trigger("reloadGrid");
					//alert(data.msg);
					//$.hideInsertForm();
					resetAction();
				}else{
					alert(data.msg);
				}
			},
			error:function(e){
				alert(e.responseText);
			}
		});
	}, 500);
}

function updateAction(obj) {
	var url = contextRoot + "/money/region/update.json";
	var params = "stanYmd="        + $("#uStanYmd").html().replaceAll("-", "");
		params += "&moneyCode="    + $("#uMoneyCode").val();
		params += "&detSeq="       + $("#uDetSeq").html();
		//params += "&worshipCode="  + $("#iWorshipCode").val();
		//params += "&deptCode="     + encodeURIComponent($("#iDeptCode").val());
		//params += "&regionCode="   + encodeURIComponent($("#iRegionCode").val());
		params += "&moneyAmt="     + encodeURIComponent($("#uMoneyAmt").val().replaceAll(',',''));

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
	var url = contextRoot + "/money/region/delete.json";
	var params = "stanYmd="     + $("#dStanYmd").html().replaceAll("-", "");
		params += "&moneyCode=" + $("#dMoneyCode").val();
		params += "&detSeq="    + $("#dDetSeq").html();

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
	var url = contextRoot + "/money/region/deleteMulti.json";
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
