
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/fin/trans/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd : $("#stanYmd").val().replaceAll("-", "")
			, acctGb : $("#acctGb").val()
			, inoutGb : $("#inoutGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','번호','acctGb','재정','assetGb','자산구분','자산코드','자산명','은행','계좌번호'
					,'inoutGb','입출구분','금액(원금)','이자액','acctCode','계정과목'
					,'비고','마감','전표처리','acctProcGb','처리구분'
					,'userId','등록자','등록일시'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
				}, { name: 'stanYmd'     , index: 'STAN_YMD'     , width:120, align:'center'
				}, { name: 'detSeq'      , index: 'DET_SEQ'      , width:50 , align:'center'
				}, { name: 'acctGb'      , index: 'ACCT_GB'      , hidden:true
				}, { name: 'acctGbName'  , index: 'ACCT_GB_NAME' , width:80, align:'center'
				}, { name: 'assetGb'     , index: 'ASSET_GB'     , hidden:true
				}, { name: 'assetGbName' , index: 'ASSET_GB_NAME', width:100, align:'center'
				}, { name: 'assetCode'   , index: 'ASSET_CODE'   , width:120, align:'center'
				}, { name: 'assetName'   , index: 'ASSET_NAME'   , width:120, align:'center'
				}, { name: 'bankName'    , index: 'BANK_NAME'    , width:100, align:'center'
				}, { name: 'accountNo'   , index: 'ACCOUNT_NO'   , width:200, align:'left'
				}, { name: 'inoutGb'     , index: 'INOUT_GB'     , hidden:true
				}, { name: 'inoutGbName' , index: 'INOUT_GB_NAME', width:100, align:'center'
				}, { name: 'moneyAmt'    , index: 'MONEY_AMT'    , width:120, align:'right'
					, cellattr : function() {	return 'style="padding-right: 5px;"';	}
					, formatter : function(cellvalue, options, rowObject) {
						return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
					}
				}, { name: 'intAmt'    , index: 'INT_AMT'    , width:80, align:'right'
					, cellattr : function() {	return 'style="padding-right: 5px;"';	}
					, formatter : function(cellvalue, options, rowObject) {
						return rowObject.intAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
					}
				}, { name: 'acctCode'      , index: 'ACCT_CODE'        , hidden:true
				}, { name: 'acctName'      , index: 'ACCT_NAME'        , width:150, align:'left'
				}, { name: 'remark'        , index: 'REMARK'           , width:150, align:'left'
				}, { name: 'closeYn'       , index: 'CLOSE_YN'         , width:40 , align:'center'
				}, { name: 'acctYn'        , index: 'ACCT_YN'          , width:80 , align:'center'
				}, { name: 'acctProcGb'    , index: 'ACCT_PROC_GB'     , hidden:true
				}, { name: 'acctProcGbName', index: 'ACCT_PROC_GB_NAME', width:100, align:'center'
				}, { name: 'userId'        , index: 'USER_ID'          , hidden:true
				}, { name: 'userName'      , index: 'USER_NAME'        , width:100, align:'center'
				}, { name: 'regYmdHms'     , index: 'REG_YMD_HMS'      , width:150, align:'center'
				}],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'DET_SEQ',
		sortorder: 'DESC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "ASSET_CODE",
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
			$("#dDetSeq").html(list.detSeq);
			$("#dInoutGb").html(list.inoutGbName);
			$("#dAssetCode").html(list.assetCode+' &nbsp; '+list.assetName+' &nbsp; '+list.bankName+' &nbsp; '+list.accountNo);
			$("#dMoneyAmt").html(list.moneyAmt);
			$("#dIntAmt").html(list.intAmt);
			$("#dRemark").html(list.remark);
			$("#dAcctProcGb").html(list.acctProcGbName);
			$("#dAcctCode").html(list.acctCode+' &nbsp; '+list.acctName);
			if ( "N"==list.closeYn ) {	
				$(".btnMd").removeClass('hide');
				$(".btnDe").removeClass('hide');
			} else {
				$(".btnMd").addClass('hide');
				$(".btnDe").addClass('hide');
			}

			$("#uStanYmd").val(list.stanYmd);
			$("#uDetSeq").val(list.detSeq);
			$("#uInoutGb").val(list.inoutGb);
			$("#uAssetCode").val(list.assetCode);
			$("#uAssetInfo").html(list.assetGbName+' &nbsp;'+list.bankName+' &nbsp;'+list.accountNo+' &nbsp;'+list.acctGbName);
			$("#uMoneyAmt").val(list.moneyAmt.replaceAll(',',''));
			$("#uIntAmt").val(list.intAmt.replaceAll(',',''));
			$("#uRemark").val(list.remark);
			$("#uAcctProcGb").val(list.acctProcGb);
			$("#uAcctCode").val(list.acctCode);
			$("#uAcctInfo").html(list.acctName);

			$.showDetail();
		},
		loadComplete : function (data) {
			
			var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
				params += "&acctGb=" + $("#acctGb").val();
				params += "&inoutGb=" + $("#inoutGb").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/fin/trans/getSum.json",
				datatype: "json",
				data: params,
				success:function(data){
					//console.log(data);
					if(data.session == 1){
						$("#inTotalAmnt").text(data.inTotalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#intTotalAmnt").text(data.intTotalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						$("#outTotalAmnt").text(data.outTotalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						//$("#totalAmnt").text(data.totalAmnt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
						
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
		myPostData.inoutGb = $("#inoutGb").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
	
	
	
	
	
	
	
	
	
	
	//금융자산선택
	$.jqGrid('#grid_assetcode_popup', {
		url:contextRoot + '/fin/mng/list.json',
		datatype: "json",
		postData: {
			assetCode : $("#iAssetCode").val()
			, useYn : 'Y'
		},
		colNames: [ 'NO','','자산코드','자산명','acctGb','재정','assetGb','자산구분','은행','계좌번호','발행일','만기일'
				   ],
		colModel: [{ name:'rk'             , index:'RK'           , width:40, align:'center', hidden:true
				}, { name:'CHECK', width:40, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False"}, sortable: false
					, formatter:function (cellValue, option) { return '<input type="radio" name="radio" value="' + option.rowId + '"/>'; }
				}, { name: 'assetCode'   , index: 'ASSET_CODE'   , width:80 , align:'center'
				}, { name: 'assetName'   , index: 'ASSET_NAME'   , width:80 , align:'center'
				}, { name: 'acctGb'      , index: 'ACCT_GB'      , hidden:true
				}, { name: 'acctGbName'  , index: 'ACCT_GB_NAME' , width:50 , align:'center'
				}, { name: 'assetGb'     , index: 'ASSET_GB'     , hidden:true
				}, { name: 'assetGbName' , index: 'ASSET_GB_NAME', width:90 , align:'center'
				}, { name: 'bankName'    , index: 'BANK_NAME'    , width:70 , align:'center'
				}, { name: 'accountNo'   , index: 'ACCOUNT_NO'   , width:160, align:'center'
				}, { name: 'issueYmd'    , index: 'ISSUE_YMD'    , width:100, align:'center'
				}, { name: 'mtyYmd'      , index: 'MTY_YMD'      , hidden:true
				}],
		pager: '#pager',
		rowNum : 1000,
		height: $("#grid_assetcode_popup").parent().height()-40,
		sortname: 'ASSET_CODE',
		sortorder: 'DESC',
		viewrecords:true,
		multiselect: false,
		shrinkToFit: true,
		scrollOffset: 0,
		autowidth: true,
		loadonce:false,
		jsonReader: {
		},
		onSelectRow: function(rowid, status, e){
			$("#grid_assetcode_popup input[type=radio]").get(rowid - 1).checked = true;
			$("#grid_assetcode_popup input[type=radio]").get(rowid - 1).focus();
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
			$("#grid_assetcode_popup input[type=radio]").change(function(){
				$("#grid_assetcode_popup").jqGrid('setSelection',$(this).val(),true);
			});
			
			// 첫번째 열 선택
			setTimeout(function() {
				let $rows = $('#grid_assetcode_popup tr.jqgrow[role=row]');
				if ($rows.length) {
					$('#grid_assetcode_popup').jqGrid('setSelection', $rows.get(0).id);	
				} else {
					alert("정보가 없습니다.");
					$(".layerPop .btnPopC").click();
				}
			}, 500);
		}
 	});

	//금융자산목정보 검색
	$(".searchAsset").bind("keydown", function(e) {
		if(e.keyCode == 13) {
			clickSrchAssetCode();
		/*	var myPostData = $("#grid_assetcode_popup").jqGrid('getGridParam', 'postData');
			myPostData.assetCode = $(this).val().trim();
			$("#grid_assetcode_popup").trigger("reloadGrid");
			
			$("#div_assetcode").show();
			var layerH = $("#div_assetcode").height();
			$("#div_assetcode").css({"margin-top": -(layerH/2)+"px"});
			
			$("body").append("<div class='maskPop'></div>");	*/
		}
	});









	
	
	
	
	//계정과목선택
	$.jqGrid('#grid_acctcode_popup', {
		url:contextRoot + '/env/acctcode/list.json',
		datatype: "json",
		postData: {
			stanYmd      : $("#stanYmd").val().replaceAll("-", "")
		//	, acctGb     : $("#acctGb").val()
			, acctCode   : ''
			, assetCode  : ''
			, inoutGb    : ''
			, sumYn      : 'N'
			, acctLevels : '중,소'
		//	, useYn      : 'Y'
			, acctQuery  : ''
		},
		colNames: ['NO','','계정코드','계정명칭','구분','구분','상위계정코드','상위계정명칭'
			   ],
		colModel: [{ name:'rk'             , index:'RK'           , width:40, align:'center'
					, formatter : function(cellvalue, options, rowObject) {
						return rowObject.rowcnt - rowObject.rk + 1;
					}
				}, { name:'CHECK', width:40, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False"}, sortable: false
					, formatter:function (cellValue, option) { return '<input type="radio" name="radio" value="' + option.rowId + '"/>'; }
				}, { name: 'acctCode'      , index: 'ACCT_CODE'        , width:90, align:'center'
				}, { name: 'acctName'      , index: 'ACCT_NAME'        , width:170, align:'left'
				}, { name: 'inoutGb'       , index: 'INOUT_GB'         , width:50 , align:'center'
				}, { name: 'acctLevelNm'   , index: 'ACCT_LEVEL_NM'    , width:30 , align:'center'
				}, { name: 'acctUp'        , index: 'ACCT_UP'          , width:90, align:'center'
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
			clickSrchAcctCode();
		/*	if ( '' == $("#iInoutGb").val() && '' == $("#uInoutGb").val() ) {
				alert("입출구분을 선택하세요.");	return;
			}
			
			if ( '' == $("#iAssetCode").val().trim() && '' == $("#uAssetCode").val().trim() ) {
				alert("금융자산을 선택하세요.");	return;
			}
			
			let isRegister = false;
			let registerStyle = $(".layerRegister").attr("style");	//alert(layerStyle);
			if ( registerStyle.indexOf("block") != -1 ) {
				isRegister = true;
			}
			
			var myPostData = $("#grid_acctcode_popup").jqGrid('getGridParam', 'postData');
			
			myPostData.stanYmd   = $("#stanYmd").val().replaceAll("-", "");
			//myPostData.acctGb    = $("#acctGb").val();
			myPostData.acctCode  = $(this).val().trim();
			if ( isRegister ) {
				myPostData.assetCode = $("#iAssetCode").val();
				myPostData.inoutGb = $("#iInoutGb").val();
			} else {
				myPostData.assetCode = $("#uAssetCode").val();
				myPostData.inoutGb = $("#uInoutGb").val();
			}
			
			$("#grid_acctcode_popup").trigger("reloadGrid");
			
			$("#div_acctcode").show();
			var layerH = $("#div_acctcode").height();
			$("#div_acctcode").css({"margin-top": -(layerH/2)+"px"});
			
			$("body").append("<div class='maskPop'></div>");	*/
		}
	});













	// 팝업창에서 숫자 입력
	$(document).on('keydown', function (e) {
		let assetPopStyle = $("#div_assetcode").attr("style");	//alert(assetPopStyle);
		if ( assetPopStyle != undefined && assetPopStyle.indexOf("block") != -1 ) {
			if ( 0<=e.key && e.key<=9 ) {		//alert(e.key+" , "+e.keyCode);
				$('#grid_assetcode_popup').jqGrid('setSelection', e.key);
			}
		}
		
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
			
			let isRegister = false;
			let registerStyle = $(".layerRegister").attr("style");	//alert(layerStyle);
			if ( registerStyle.indexOf("block") != -1 ) {
				isRegister = true;
			}
			
			let assetPopStyle = $("#div_assetcode").attr("style");	//alert(assetPopStyle);
			if ( assetPopStyle != undefined && assetPopStyle.indexOf("block") != -1 ) {
				var list = jQuery("#grid_assetcode_popup").getRowData($(this).val());
				if ( isRegister ) {
					$('#iAssetCode').val(list.assetCode);
					//$('#iAssetInfo').html(list.assetGbName+' &nbsp;'+list.bankName+' &nbsp;'+list.accountNo+' &nbsp;'+list.acctGbName+' &nbsp;'+list.issueYmd+'~'+list.mtyYmd);
					$('#iAssetName').html(list.assetName);
					$('#iAcctGbName').html(list.acctGbName);
					$('#iAssetGbName').html(list.assetGbName);
					$('#iBankName').html(list.bankName);
					$('#iAccountNo').html(list.accountNo);
					$('#iIssueYmd').html(list.issueYmd);
					$('#iMtyYmd').html(list.mtyYmd);
					$('#iMoneyAmt').focus();
				} else {
					$('#uAssetCode').val(list.assetCode);
					//$('#uAssetInfo').html(list.assetGbName+' &nbsp;'+list.bankName+' &nbsp;'+list.accountNo+' &nbsp;'+list.acctGbName+' &nbsp;'+list.issueYmd+'~'+list.mtyYmd);
					$('#uAssetName').html(list.assetName);
					$('#uAcctGbName').html(list.acctGbName);
					$('#uAssetGbName').html(list.assetGbName);
					$('#uBankName').html(list.bankName);
					$('#uAccountNo').html(list.accountNo);
					$('#uIssueYmd').html(list.issueYmd);
					$('#uMtyYmd').html(list.mtyYmd);
					$('#uMoneyAmt').focus();
				}
				
			}
			
			let acctPopStyle = $("#div_acctcode").attr("style");	//alert(acctPopStyle);
			if ( acctPopStyle != undefined && acctPopStyle.indexOf("block") != -1 ) {
				var list = jQuery("#grid_acctcode_popup").getRowData($(this).val());
				if ( isRegister ) {
					$('#iAcctCode').val(list.acctCode);
					$('#iAcctInfo').html(' &nbsp; '+list.acctUpName+' &nbsp; > &nbsp; '+list.acctName+' &nbsp; ['+list.inoutGb+']');
					$('#btnReg').focus();
				} else {
					$('#uAcctCode').val(list.acctCode);
					$('#uAcctInfo').html(' &nbsp; '+list.acctUpName+' &nbsp; > &nbsp; '+list.acctName+' &nbsp; ['+list.inoutGb+']');
					$('#btnMod').focus();
				}
				
			}
			
			$(".layerPop .btnPopC").click();
		}
	});
	
	//팝업 취소버튼
	$(".layerPop .btnPopC").bind("click",function(){
		$(".maskPop").remove();
		$("#div_assetcode").hide();
		$("#div_acctcode").hide();
		return false;
	});
	
	
	$(".checkMoney").bind("keydown", function(e) {
		if(e.keyCode == 13) { checkMoneyAmt($(this)); }
	});
	//$(".checkMoney").bind("blur", function(e) {
	//	checkMoneyAmt($(this));
	//});
	
});




function checkMoneyAmt(obj) {
	val = obj.val().trim().replaceAll(',','');
	if ( val == '' || isNaN(val) ) {
		alert('숫자를 입력하십시오.');	obj.focus();
	} else {
		obj.val(val.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
	}
}


function resetAction() {
	$("#iStanYmd").val($("#stanYmd").val());
	
	$("#iInoutGb").val("");
    $('#iInoutGb').selectBox('refresh');
	
	$("#iAssetCode").val("");
	//$("#iAssetInfo").html("");
	$('#iAssetName').html("");
	$('#iAcctGbName').html("");
	$('#iAssetGbName').html("");
	$('#iBankName').html("");
	$('#iAccountNo').html("");
	$('#iIssueYmd').html("");
	$('#iMtyYmd').html("");
	$("#iMoneyAmt").val("");
	$("#iIntAmt").val("");
	$("#iRemark").val("");
	
	$("#iAcctProcGb").val("");
    $('#iAcctProcGb').selectBox('refresh');
	
	$("#iAcctCode").val("");
	$('#iAcctInfo').html("");
}

function preModifyAction() {
}

function validate() {
	if ( insertFlag ) {
		if($("#iIntAmt").val().trim() == "") { $("#iIntAmt").val('0'); }
		if($("#iAssetCode").val().trim() == "") { alert("금융자산을 선택하세요."); $("#iAssetCode").focus(); return false; }
		if($("#iMoneyAmt").val().trim() == "") { alert("금액을 입력하세요."); $("#iMoneyAmt").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	
	if ( '0' != $("#iAcctProcGb").val() && '' == $("#iAcctCode").val() ) {
		alert('계정과목을 입력하세요.');	$("#iAcctCode").focus();	return false;
	}
	
	var url = contextRoot + "/fin/trans/insert.json";
	var params = "stanYmd=" + encodeURIComponent($("#iStanYmd").val().replaceAll("-", ""));
		params += "&inoutGb=" + encodeURIComponent($("#iInoutGb").val());
		params += "&assetCode=" + encodeURIComponent($("#iAssetCode").val());
		params += "&moneyAmt=" + encodeURIComponent($("#iMoneyAmt").val().replaceAll(',','').trim());
		params += "&intAmt=" + encodeURIComponent($("#iIntAmt").val().replaceAll(',','').trim());
		params += "&remark=" + encodeURIComponent($("#iRemark").val().trim());
		params += "&acctProcGb=" + encodeURIComponent($("#iAcctProcGb").val());
		params += "&acctCode=" + encodeURIComponent($("#iAcctCode").val().trim());

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
	var url = contextRoot + "/fin/trans/update.json";
	var params = "stanYmd=" + encodeURIComponent($("#uStanYmd").val().replaceAll("-", ""));
		params += "&detSeq=" + encodeURIComponent($("#uDetSeq").val());
		params += "&inoutGb=" + encodeURIComponent($("#uInoutGb").val());
		params += "&assetCode=" + encodeURIComponent($("#uAssetCode").val());
		params += "&moneyAmt=" + encodeURIComponent($("#uMoneyAmt").val().replaceAll(',','').trim());
		params += "&intAmt=" + encodeURIComponent($("#uIntAmt").val().replaceAll(',','').trim());
		params += "&remark=" + encodeURIComponent($("#uRemark").val().trim());
		params += "&acctProcGb=" + encodeURIComponent($("#uAcctProcGb").val());
		params += "&acctCode=" + encodeURIComponent($("#uAcctCode").val().trim());


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
	var url = contextRoot + "/fin/trans/delete.json";
	var params = "stanYmd=" + $("#dStanYmd").html().replaceAll("-", "");
		params += "&detSeq=" + $("#dDetSeq").html();

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
	var url = contextRoot + "/fin/trans/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&assetCode=" + list.assetCode;
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



function checkInoutGb() {
	let inoutGb = $('#iInoutGb').val();
	if ( ''==inoutGb || 'I'==inoutGb ) {
		$('#iIntAmt').addClass('hide');
		$('#iAcctProcGb').val('1');
		$('#iAcctProcGb').selectBox('refresh');
		checkAcctProcGb();
	} else {
		$('#iIntAmt').removeClass('hide');
		$('#iAcctProcGb').val('0');
		$('#iAcctProcGb').selectBox('refresh');
		checkAcctProcGb();
	}
}

function checkAcctProcGb() {
	$("#iAcctCode").val("");
	$('#iAcctInfo').html("");
	let acctProcGb = $('#iAcctProcGb').val();
	if ( ''==acctProcGb || '0'==acctProcGb ) {
		$('#iAcctCode').addClass('hide');
	} else {
		$('#iAcctCode').removeClass('hide');
	}
}


function doClose() {
	
	//alert("작업중입니다."); return false;
	//if($("#acctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#acctGb").focus(); return false; }
	//if($("#rowCnt").text().trim() == "0") { alert("등록된 거래가 없습니다."); return false; }
	
	if ( !confirm("금융금융자산거래를 마감처리하시겠습니까?") ) return;

	var url = contextRoot + "/fin/trans/doClose.json";
	var params = "stanYmd="     + $("#stanYmd").val().replaceAll("-", "");
		//params += "&acctGb="    + $("#acctGb").val();
		params += "&acctType=" + encodeURIComponent("자동");
	
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
	
	//alert("작업중입니다."); return false;
	//if($("#acctGb").val().trim() == "") { alert("재정을 선택하세요."); $("#acctGb").focus(); return false; }
	
	if ( !confirm("금융금융자산거래마감을 취소하시겠습니까?") ) return;

	var url = contextRoot + "/fin/trans/cancelClose.json";
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


function clickSrchAssetCode() {
	let val = '';
	if ( $('#div_drag_2').attr("style").indexOf("block") != -1 ) {
		console.log('등록');			
		val = $('#iAssetCode').val().trim();
	} else if ( $('#div_drag_3').attr("style").indexOf("block") != -1 ) {
		console.log('수정');
		val = $('#uAssetCode').val().trim();
	}
	
	var myPostData = $("#grid_assetcode_popup").jqGrid('getGridParam', 'postData');
	myPostData.assetCode = val;
	$("#grid_assetcode_popup").trigger("reloadGrid");
	
	$("#div_assetcode").show();
	var layerH = $("#div_assetcode").height();
	$("#div_assetcode").css({"margin-top": -(layerH/2)+"px"});
	
	$("body").append("<div class='maskPop'></div>");
}

function clickSrchAcctCode() {
	let acctCode = '';
	if ( $('#div_drag_2').attr("style").indexOf("block") != -1 ) {
		console.log('등록');			
		acctCode = $('#iAcctCode').val().trim();
	} else if ( $('#div_drag_3').attr("style").indexOf("block") != -1 ) {
		console.log('수정');
		acctCode = $('#uAcctCode').val().trim();
	}
	
			
	if ( '' == $("#iInoutGb").val() && '' == $("#uInoutGb").val() ) {
		alert("입출구분을 선택하세요.");	return;
	}
	
	if ( '' == $("#iAssetCode").val().trim() && '' == $("#uAssetCode").val().trim() ) {
		alert("금융자산을 선택하세요.");	return;
	}
	
	let isRegister = false;
	let registerStyle = $(".layerRegister").attr("style");	//alert(layerStyle);
	if ( registerStyle.indexOf("block") != -1 ) {
		isRegister = true;
	}
	
	var myPostData = $("#grid_acctcode_popup").jqGrid('getGridParam', 'postData');
	
	myPostData.stanYmd   = $("#stanYmd").val().replaceAll("-", "");
	//myPostData.acctGb    = $("#acctGb").val();
	//myPostData.acctCode  = acctCode;
	myPostData.acctQuery  = acctCode;
	
	if ( isRegister ) {
		myPostData.assetCode = $("#iAssetCode").val();
		myPostData.inoutGb = $("#iInoutGb").val();
	} else {
		myPostData.assetCode = $("#uAssetCode").val();
		myPostData.inoutGb = $("#uInoutGb").val();
	}
	
	$("#grid_acctcode_popup").trigger("reloadGrid");
	
	$("#div_acctcode").show();
	var layerH = $("#div_acctcode").height();
	$("#div_acctcode").css({"margin-top": -(layerH/2)+"px"});
	
	$("body").append("<div class='maskPop'></div>");
}
