
$(document).ready(function(){

	if ( '1' == type ) {
		$("#subtitle").text('전표관리 > 전표등록');
	}

	$.jqGrid($('#grid'), {
		url:contextRoot + "/sheet/mng/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd    : $("#stanYmd").val().replaceAll("-", "")
			, acctGb   : $("#acctGb").val()
			, inoutGb  : $("#inoutGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','acctGb','재정','전표번호','계정','계정명','금액','내역','상위계정'
					,'구분','발생','마감','userId','등록자'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'      , index: 'STAN_YMD'     , width:70 , align:'center'
					}, { name: 'acctGb'       , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME' , width:50 , align:'center'
					}, { name: 'seqNo'        , index: 'SEQ_NO'       , width:80 , align:'center'
						, formatter : function(cellvalue, options, rowObject) {
							let val = rowObject.seqNo.toString();
							return val.substr(0,8)+'-'+val.substr(8);
						}
					}, { name: 'acctCode'     , index: 'ACCT_CODE'    , width:60 , align:'center'
					}, { name: 'acctName'     , index: 'ACCT_NAME'    , width:100, align:'left'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'    , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'acctRemark'   , index: 'ACCT_REMARK'  , width:200, align:'left'
					}, { name: 'upName'       , index: 'UP_NAME'      , width:100, align:'left'
					}, { name: 'inoutGb'      , index: 'INOUT_GB'     , width:30 , align:'center'
					}, { name: 'acctType'     , index: 'ACCT_TYPE'    , width:40 , align:'center'
					}, { name: 'closeYn'      , index: 'CLOSE_YN'     , width:40 , align:'center'
					}, { name: 'userId'       , index: 'USER_ID'      , hidden:true
					}, { name: 'userName'     , index: 'USER_NAME'    , width:50 , align:'center'
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
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);

			$("#dSeqNo").html(list.seqNo);
			$("#dAcctInfo").html(list.upName+' &nbsp; > &nbsp; '+list.acctName+' &nbsp; ['+list.inoutGb+']');
			$("#dMoneyAmt").html(list.moneyAmt);
			$("#dAcctRemark").html(list.acctRemark);
			if ( "N"==list.closeYn ) {	
				$(".btnMd").removeClass('hide');
				$(".btnDe").removeClass('hide');
			} else {
				$(".btnMd").addClass('hide');
				$(".btnDe").addClass('hide');
			}
			
			$("#uSeqNo").html(list.seqNo);
			$("#uAcctCode").val(list.acctCode);
			$("#uAcctInfo").html(list.upName+' &nbsp; > &nbsp; '+list.acctName+' &nbsp; ['+list.inoutGb+']');
			$("#uMoneyAmt").val(list.moneyAmt);
			$("#uAcctRemark").val(list.acctRemark);
			
			$.showDetail();
		},
		loadComplete : function (data) {
			
			$("#startYmd").html(data.startYmd);
			$("#endYmd").html(data.endYmd);
			
			var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
				params += "&acctGb=" + $("#acctGb").val();
				params += "&inoutGb=" + $("#inoutGb").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/sheet/mng/getSum.json",
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
		myPostData.acctGb  = $("#acctGb").val();
		myPostData.inoutGb = $("#inoutGb").val();
		
		$("#grid").trigger("reloadGrid");
		
		$("#iAcctGb").val($("#acctGb").val());
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
	
	
	
	
	
	
	//계정과목선택
	$.jqGrid('#grid_acctcode_popup', {
		url:contextRoot + '/env/acctcode/list.json',
		datatype: "json",
		postData: {
			stanYmd      : $("#stanYmd").val().replaceAll("-", "")
			, acctGb     : $("#iAcctGb").val()
			//, acctCode   : $("#iAcctCode").val()
			, acctQuery   : $("#iAcctCode").val()
			, acctLevels : '중,소'
			, sumYn      : 'N'
		//	, useYn : 'Y'
		},
		colNames: ['NO','','구분','계정코드','계정명칭','상위계정코드','상위계정명칭'
			   ],
		colModel: [{ name:'rk'             , index:'RK'           , width:40, align:'center'
				}, { name:'CHECK', width:30, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False"}, sortable: false
					, formatter:function (cellValue, option) { return '<input type="radio" name="radio" value="' + option.rowId + '"/>'; }
				}, { name: 'inoutGb'       , index: 'INOUT_GB'         , width:50 , align:'center'
				}, { name: 'acctCode'      , index: 'ACCT_CODE'        , width:100, align:'center'
				}, { name: 'acctName'      , index: 'ACCT_NAME'        , width:170, align:'left'
				}, { name: 'acctUp'        , index: 'ACCT_UP'          , width:100, align:'center'
				}, { name: 'acctUpName'    , index: 'ACCT_UP_NAME'     , width:190, align:'left'
				}],
		pager: '#pager',
		rowNum : 1000,
		height: $("#grid_acctcode_popup").parent().height()-40,
		sortname: 'ACCT_CODE',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		shrinkToFit: true,
		scrollOffset: 0,
		autowidth: true,
		loadonce:false,
		jsonReader: {
		},
		onSelectRow: function(rowid, status, e){
			$("#grid_acctcode_popup input[type=radio]").get(rowid - 1).checked = true;
			$("#grid_acctcode_popup input[type=radio]").get(rowid - 1).focus();
		},
		onCellSelect: function(rowid, iCol, cellcontent, e){
			
		},
		beforeRequest: function () {
				//var oParams = $('#grid_cctv_used_popup').getGridParam();
				//if (oParams.hasOwnProperty('url') && oParams.url == '') { return false; }
		},
		beforeProcessing: function(data, status, xhr){

		},
		loadComplete: function () {
			$("#grid_acctcode_popup input[type=radio]").change(function(){
				$("#grid_acctcode_popup").jqGrid('setSelection',$(this).val(),true);
			});
			
			// 첫번째 열 선택
			setTimeout(function() {
				let $rows = $('#grid_acctcode_popup tr.jqgrow[role=row]');
				if ($rows.length) {
					$('#grid_acctcode_popup').jqGrid('setSelection', $rows.get(0).id);	
				} else {
					alert("정보가 없습니다.");
					$(".layerPop .btnPopC").click();
				}
			}, 500);
		}
 	});

	//계정과목정보 검색
	$(".searchAcct").bind("keydown", function(e) {
		if(e.keyCode == 13) {
			var myPostData = $("#grid_acctcode_popup").jqGrid('getGridParam', 'postData');
			myPostData.stanYmd  = $("#stanYmd").val().replaceAll("-", "");
			myPostData.acctGb   = $("#iAcctGb").val();
			//myPostData.acctCode = $(this).val().trim();
			myPostData.acctQuery = $(this).val().trim();
			$("#grid_acctcode_popup").trigger("reloadGrid");
			
			$("#div_acctcode").show();
			var layerH = $("#div_acctcode").height();
			$("#div_acctcode").css({"margin-top": -(layerH/2)+"px"});
			
			$("body").append("<div class='maskPop'></div>");
		}
	});

	
	// 팝업창에서 숫자 입력
	$(document).on('keydown', function (e) {
		let acctPopStyle = $("#div_acctcode").attr("style");	//alert(acctPopStyle);
		if ( acctPopStyle != undefined && acctPopStyle.indexOf("block") != -1 ) {
			if ( 0<=e.key && e.key<=9 ) {		//alert(e.key+" , "+e.keyCode);
				$('#grid_acctcode_popup').jqGrid('setSelection', e.key);
			}
		}
	});

	// 팝업창에서 선택 후 엔터
	$(document).on('keydown', 'input[type="radio"]', function (e) {
		if (e.key === 'Enter') {
			e.preventDefault(); // Prevent default behavior
			
			const isChecked = $(this).is(':checked');
			if (!isChecked) {
				$(this).prop('checked', true);
			}
			
			var list = jQuery("#grid_acctcode_popup").getRowData($(this).val());
			
			if ( $('#div_drag_2').attr("style").indexOf("block") != -1 ) {
				console.log('등록');			
				$('#iAcctCode').val(list.acctCode);
				$('#iAcctInfo').html('['+list.inoutGb+'] '+list.acctUpName+' > '+list.acctName);
				$('#iMoneyAmt_0').focus();
			} else if ( $('#div_drag_3').attr("style").indexOf("block") != -1 ) {
				console.log('수정');
				$('#uAcctCode').val(list.acctCode);
				$('#uAcctInfo').html('['+list.inoutGb+'] '+list.acctUpName+' > '+list.acctName);
				$('#uMoneyAmt').focus();
			}
			
			$(".layerPop .btnPopC").click();
		}
	});
	
	//팝업 취소버튼
	$(".layerPop .btnPopC").bind("click",function(){
		$(".maskPop").remove();
		$("#div_acctcode").hide();
		return false;
	});
	
	
	
	$(".checkMoney").bind("keydown", function(e) {
		if(e.keyCode == 13) { checkMoneyAmt($(this)); }
	});
		
	$(".checkRem").bind("keydown", function(e) {
		if(e.keyCode == 13) { checkRemark($(this)); }
	}); 
	
	len = $('.checkMoney').length;
	
	
	// 수정화면에서 데이타 확인
	$(".txtType").bind("keydown", function(e) {
		if(e.keyCode == 13) {
			if ( "uMoneyAmt" == $(this).attr("id") ) {
				val = $(this).val().trim().replaceAll(',','');
				if ( val == '' || isNaN(val) ) {
					alert('숫자를 입력하십시오.');	$(this).focus();
				} else {
					$('#uAcctRemark').focus();
				}
			} else if ( "uAcctRemark" == $(this).attr("id") ) {
				if ( '' == $(this).val().trim() ) {
					alert('정보를 입력하십시오.');	$(this).focus();
				} else {
					$('#btnMod').focus();
				}
			}
		 }
	});
	
});

var len = 0;
var ind = 0;

function checkMoneyAmt(obj) {
	ind = obj.attr("id").split("_")[1];
	val = obj.val().trim().replaceAll(',','');
	if ( val == '' || isNaN(val) ) {
		alert('숫자를 입력하십시오.');	obj.focus();
	} else {
		obj.val(val.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
		
		if ( ind < len ) {
			$('#iAcctRemark_'+ind).focus();
		}
	}
}

function checkRemark(obj) {
	ind = obj.attr("id").split("_")[1];
	ind ++;
	if ( ind < len ) {
		$('#iMoneyAmt_'+ind).focus();
	} else {
		$('#btnReg').focus();
	}
}

function resetAction() {
	$('#iAcctCode').val('');
	$('#iAcctInfo').html('');
	for ( var i=0 ; i<len  ; i++ ) {
		$('#iMoneyAmt_'+i).val('');
		$('#iAcctRemark_'+i).val('');
	}
	$("#iAcctCode").focus();
}

function preModifyAction() {
}

function validate() {
	if ( insertFlag ) {
		if($("#iAcctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#iAcctGb").focus(); return false; }
		if($("#iAcctCode").val().trim() == "") { alert("계정과목을 입력하세요."); $("#iAcctCode").focus(); return false; }
		if($("#iAcctCode").val().trim().length < 6) { alert("계정과목코드는 6자리입니다."); $("#iAcctCode").focus(); return false; }
	}
	
	for ( var i=0 ; i<len  ; i++ ) {
		let remark = $('#iAcctRemark_'+i).val();
		if ( remark.indexOf(';') != -1 || remark.indexOf(':') != -1 ) {
			alert("; 과 : 은 사용하실 수 없습니다.");	$('#iAcctRemark_'+i).focus();	return false;
		}
	}
	return true;
}

function insertAction(obj) {
	
	var infoData = "";
	for ( var i=0 ; i<len ; i++ ) {
		if ( $('#iMoneyAmt_'+i).val().trim() != '') {
			if ( $('#iAcctRemark_'+i).val().trim() == '' ) {
				alert("정보를 입력하세요.");	$('#iAcctRemark_'+i).focus();	return;
			} else {
				infoData += ";"+$('#iMoneyAmt_'+i).val().replaceAll(',','')+":"+$('#iAcctRemark_'+i).val().trim();
			}
		}
	}
	if ( infoData == "" ) {
		alert("정보를 입력하세요.");	return;
	} else {
		infoData = infoData.substring(1);
	}
	
	var url = contextRoot + "/sheet/mng/insert.json";
	var params = "stanYmd="     + $("#stanYmd").val().replaceAll("-", "");
		params += "&acctGb="    + $("#iAcctGb").val();
		params += "&acctCode="  + $("#iAcctCode").val();
		params += "&acctType="  + encodeURIComponent("입력");
		params += "&infoData="  + encodeURIComponent(infoData);

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
}

function updateAction(obj) {
	var url = contextRoot + "/sheet/mng/update.json";
	var params = "seqNo=" + $("#uSeqNo").html().replaceAll("-", "");
		params += "&acctCode="  + $("#uAcctCode").val();
		params += "&moneyAmt=" + encodeURIComponent($("#uMoneyAmt").val().replaceAll(",", "").trim());
		params += "&acctRemark=" + encodeURIComponent($("#uAcctRemark").val().trim());

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
	var url = contextRoot + "/sheet/mng/delete.json";
	var params = "seqNo=" + $("#dSeqNo").html().replaceAll("-", "");

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

function doClose() {
	
	if($("#acctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#acctGb").focus(); return false; }
	//if($("#rowCnt").text().trim() == "0") { alert("등록된 전표가 없습니다."); return false; }
	
	if ( !confirm("전표를 마감처리하시겠습니까?") ) return;
	
	var url = contextRoot + "/sheet/mng/doClose.json";
	var params = "stanYmd="     + $("#stanYmd").val().replaceAll("-", "");
		params += "&acctGb="    + $("#acctGb").val();
	
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
	
	if($("#acctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#acctGb").focus(); return false; }
	
	if ( !confirm("전표마감을 취소하시겠습니까?") ) return;
	
	var url = contextRoot + "/sheet/mng/cancelClose.json";
	var params = "stanYmd="     + $("#stanYmd").val().replaceAll("-", "");
		params += "&acctGb="    + $("#acctGb").val();
	
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
