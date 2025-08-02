
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/acctcode/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYy : $("#stanYy").val()
			, acctGb : $("#acctGb").val()
			, acctLevel : $("#acctLevel").val()
			, acctName : $("#acctName").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'연도','acctGb','재정명칭','계정코드','계정명칭','출력명칭'
					,'acctLevel','구분','sumYn','합산여부','inoutGb','수입/지출','inType','수입계정유형'
					,'상위계정코드','상위계정명칭','연결계정코드','연결계정명칭','연결재정코드','연결재정명칭','useYn','사용여부'
				   ],
		colModel: [{ name: 'CHECK', width:40 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYy'        , index: 'STAN_YY'          , width:30 , align:'center'
					}, { name: 'acctGb'        , index: 'ACCT_GB'          , hidden:true
					}, { name: 'acctGbName'    , index: 'ACCT_GB_NAME'     , width:40 , align:'center'
					}, { name: 'acctCode'      , index: 'ACCT_CODE'        , width:40 , align:'center'
					}, { name: 'acctName'      , index: 'ACCT_NAME'        , width:100, align:'left'
					}, { name: 'printName'     , index: 'PRINT_NAME'       , width:120, align:'left'
					   , formatter : function(cellvalue, options, rowObject) {
							if ( '소' == rowObject.acctLevel ) return '&nbsp; &nbsp; &nbsp; &nbsp;' + rowObject.printName;
							else if ( '중' == rowObject.acctLevel ) return '&nbsp; &nbsp;' + rowObject.printName;
							else return rowObject.printName;
						}
					}, { name: 'acctLevel'     , index: 'ACCT_LEVEL'       , width:20 , align:'center', hidden:true
					}, { name: 'acctLevelNm'   , index: 'ACCT_LEVEL_NM'    , width:30 , align:'center'
					}, { name: 'sumYn'         , index: 'SUM_YN'           , width:40 , align:'center', hidden:true
					}, { name: 'sumYnNm'       , index: 'SUM_YN_NM'        , width:40 , align:'center'
					}, { name: 'inoutGb'       , index: 'INOUT_GB'         , width:40 , align:'center', hidden:true
					}, { name: 'inoutGbNm'     , index: 'INOUT_GB_NM'      , width:40 , align:'center'
					}, { name: 'inType'        , index: 'IN_TYPE'          , width:30 , align:'center', hidden:true
					}, { name: 'inTypeNm'      , index: 'IN_TYPE_NM'       , width:50 , align:'center'
					}, { name: 'acctUp'        , index: 'ACCT_UP'          , width:50 , align:'center'
					}, { name: 'acctUpName'    , index: 'ACCT_UP_NAME'     , width:100, align:'left'
					}, { name: 'linkAcctCode'  , index: 'LINK_ACCT_CODE'   , width:50 , align:'center'
					}, { name: 'linkAcctName'  , index: 'LINK_ACCT_NAME'   , width:100, align:'left'
					}, { name: 'linkAcctGb'    , index: 'LINK_ACCT_GB'     , width:50 , align:'center', hidden:true
					}, { name: 'linkAcctGbName', index: 'LINK_ACCT_GB_NAME', width:50 , align:'center', hidden:true
					}, { name: 'useYn'         , index: 'USE_YN'           , width:30 , align:'center', hidden:true
					}, { name: 'useYnNm'       , index: 'USE_YN'           , width:30 , align:'center'
					   , formatter : function(cellvalue, options, rowObject) {
							if ( 'Y' == rowObject.useYn ) return '사용';
							else return '미사용';
						}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'STAN_YY, ACCT_GB, ACCT_CODE',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "MONEY_CODE",
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

			$("#dStanYy").html(list.stanYy);
			$("#dAcctGb").html(list.acctGbName);
			$("#dAcctUp").html(list.acctUp);
			$("#dAcctUpInfo").text(list.acctUpName);
			$("#dAcctCode").html(list.acctCode);
			$("#dAcctName").html(list.acctName);
			$("#dPrintName").html(list.printName);
			$("#dAcctLevel").html(list.acctLevelNm);
			$("#dSumYn").html(list.sumYnNm);
			$("#dInoutGb").html(list.inoutGbNm);
			$("#dInType").html(list.inTypeNm);
			//$("#dLinkAcctGb").html(list.linkAcctGbName);
			$("#dLinkAcctCode").html(list.linkAcctName);
			$("#dUseYn").html(list.useYnNm);

			$("#uStanYy").val(list.stanYy);
			$("#uAcctGb").val(list.acctGb);
			$("#uAcctUp").val(list.acctUp);
			$("#uAcctUpInfo").text(list.acctUpName);
			$("#uAcctCode").val(list.acctCode);
			$("#uAcctName").val(list.acctName);
			$("#uPrintName").val(list.printName.replaceAll('&nbsp;','').trim());
			$("#uAcctLevel").val(list.acctLevel);
			$("#uSumYn").val(list.sumYn);
			$("#uInoutGb").val(list.inoutGb);
			$("#uInType").val(list.inType);
			//$("#uLinkAcctGb").val(list.linkAcctGb);
			$("#uLinkAcctCode").val(list.linkAcctCode);
			$("#uUseYn").val(list.useYn);

			$.showDetail();
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
		myPostData.acctLevel = $("#acctLevel").val();
		myPostData.acctName = $("#acctName").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
	
	
	
	
	
	
	
	
	
	
	
	//계정과목선택
	$.jqGrid('#grid_acctcode_popup', {
		url:contextRoot + '/env/acctcode/list.json',
		datatype: "json",
		postData: {
			stanYy      : $("#stanYy").val()
			, acctGb    : $("#acctGb").val()
			, acctLevel : ''
			, acctCode  : ''
			, useYn     : 'Y'
			, acctQuery : ''
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
			
			let acctPopStyle = $("#div_acctcode").attr("style");	//alert(acctPopStyle);
			if ( acctPopStyle != undefined && acctPopStyle.indexOf("block") != -1 ) {
				var list = jQuery("#grid_acctcode_popup").getRowData($(this).val());
				if ( isRegister ) {
					$('#iAcctUp').val(list.acctCode);
					$('#iAcctUpInfo').html(' &nbsp; '+list.acctUpName+' &nbsp; > &nbsp; '+list.acctName+' &nbsp; ['+list.inoutGb+']');
					//$('#btnReg').focus();
				} else {
					$('#uAcctUp').val(list.acctCode);
					$('#uAcctUpInfo').html(' &nbsp; '+list.acctUpName+' &nbsp; > &nbsp; '+list.acctName+' &nbsp; ['+list.inoutGb+']');
					//$('#btnMod').focus();
				}
				
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
	









	
	
});






function resetAction() {
	$("#iStanYy").val("");
	$("#iAcctGb").val("");
	$("#iAcctCode").val("");
	$("#iAcctName").val("");
	$("#iPrintName").val("");
	$("#iAcctLevel").val("");
	$("#iSumYn").val("");
	$("#iInoutGb").val("");
	$("#iInType").val("");
	$("#iAcctUp").val("");
	$("#iLinkAcctGb").val("");
	$("#iLinkAcctCode").val("");
	$("#iUseYn").val("");
}

function preModifyAction() {
}

function validate() {
	if ( insertFlag ) {
		if($("#iStanYy").val().trim()   == ""  ) { alert("회계년도를 입력하세요."); $("#iStanYy").focus();    return false; }
		if($("#iAcctGb").val()          == null) { alert("재정을 선택하세요."); $("#iAcctGb").focus();    return false; }
		if($("#iAcctLevel").val()       == null) { alert("구분을 선택하세요.");    $("#iAcctLevel").focus(); return false; }
		if($("#iAcctLevel").val()       != '대') {
			if($("#iAcctUp").val().trim() == "" ) { alert("상위계정을 입력하세요."); $("#iAcctUp").focus();    return false; }
		}
		if($("#iAcctCode").val().trim() == ""  ) { alert("계정코드를 입력하세요."); $("#iAcctCode").focus();  return false; }
		if($("#iAcctName").val().trim() == ""  ) { alert("계정명청을 입력하세요."); $("#iAcctName").focus();  return false; }
	}
	return true;
}

function insertAction(obj) {
	if ('' == acctCodeDuplFlag )	{	alert('계정코드 중복검사를 하세요.');	$('#iAcctCode').focus();		return false;	}
	if ('IMPOSS' == acctCodeDuplFlag )	{	alert('이미 사용중인 아이디입니다.');	$('#iAcctCode').focus();		return false;	}
	
	var url = contextRoot + "/env/acctcode/insert.json";
	var params = "stanYy=" + encodeURIComponent($("#iStanYy").val().trim());
		params += "&acctGb=" + encodeURIComponent($("#iAcctGb").val().trim());
		params += "&acctLevel=" + encodeURIComponent($("#iAcctLevel").val().trim());
		params += "&acctUp=" + encodeURIComponent($("#iAcctUp").val().trim());
		params += "&acctCode=" + encodeURIComponent($("#iAcctCode").val().trim());
		params += "&acctName=" + encodeURIComponent($("#iAcctName").val().trim());
		params += "&printName=" + encodeURIComponent($("#iPrintName").val().trim());
		params += "&sumYn=" + encodeURIComponent($("#iSumYn").val());
		params += "&inoutGb=" + encodeURIComponent($("#iInoutGb").val());
		params += "&inType=" + encodeURIComponent($("#iInType").val());
		//params += "&linkAcctGb=" + encodeURIComponent($("#iLinkAcctGb").val().trim());
		params += "&linkAcctCode=" + encodeURIComponent($("#iLinkAcctCode").val().trim());
		params += "&useYn=" + encodeURIComponent($("#iUseYn").val());

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
	var url = contextRoot + "/env/acctcode/update.json";
	var params = "stanYy=" + encodeURIComponent($("#uStanYy").val().trim());
		params += "&acctGb=" + encodeURIComponent($("#uAcctGb").val().trim());
		params += "&acctLevel=" + encodeURIComponent($("#uAcctLevel").val().trim());
		params += "&acctUp=" + encodeURIComponent($("#uAcctUp").val().trim());
		params += "&acctCode=" + encodeURIComponent($("#uAcctCode").val().trim());
		params += "&acctName=" + encodeURIComponent($("#uAcctName").val().trim());
		params += "&printName=" + encodeURIComponent($("#uPrintName").val().trim());
		params += "&sumYn=" + encodeURIComponent($("#uSumYn").val());
		params += "&inoutGb=" + encodeURIComponent($("#uInoutGb").val());
		params += "&inType=" + encodeURIComponent($("#uInType").val());
		//params += "&linkAcctGb=" + encodeURIComponent($("#uLinkAcctGb").val().trim());
		params += "&linkAcctCode=" + encodeURIComponent($("#uLinkAcctCode").val().trim());
		params += "&useYn=" + encodeURIComponent($("#uUseYn").val());

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
	if(confirm("자료를 삭제하시겠습니까?") == false) return false;

	var url = contextRoot + "/env/acctcode/delete.json";
	var params = "stanYy=" + encodeURIComponent($("#uStanYy").val().trim());
		params += "&acctGb=" + encodeURIComponent($("#uAcctGb").val().trim());
		params += "&acctCode=" + encodeURIComponent($("#uAcctCode").val().trim());

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
	var url = contextRoot + "/env/acctcode/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&acctCodeInfo=" + list.stanYy + ":" + list.acctGb + ":" + list.acctCode;
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





let acctCodeDuplFlag = '';
function checkAcctCode() {
	if ( '' == $("#iAcctCode").val().trim() ) {	alert('계정코드를 입력하세요.');		$('#iAcctCode').focus();		return false;	}
	
	var params = "stanYy=" + encodeURIComponent($("#iStanYy").val().trim());
		params += "&acctGb=" + encodeURIComponent($("#iAcctGb").val());
		params += "&acctCode=" + encodeURIComponent($("#iAcctCode").val().trim());

	$.ajaxEx($('#grid'), {
		url : contextRoot + "/env/acctcode/checkAcctCode.json",
		datatype: "json",
		data: params,
		success:function(data){
			//alert(data.msg);
			if( 0 == data.msg ) {
				alert('사용가능한 계정코드입니다.');		acctCodeDuplFlag = 'POSS';
			} else {
				alert('이미 사용중인 계정코드입니다.');		acctCodeDuplFlag = 'IMPOSS';
			}
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
	
}













function checkAcctLevel() {
	$("#iAcctUp").val("");
	$('#iAcctUpInfo').html("");
	let acctLevel = $('#iAcctLevel').val();
	if ( ''==acctLevel || '대'==acctLevel ) {
		$('#iAcctUp').addClass('hide');
	} else {
		$('#iAcctUp').removeClass('hide');
	}
}

























function clickSrchAcctCode() {
	let stanYy = '';
	let acctGb = '';
	let acctLevel = '';
	let acctCode = '';
	if ( $('#div_drag_2').attr("style").indexOf("block") != -1 ) {		//console.log('등록');
		stanYy = $("#iStanYy").val();
		acctGb = $("#iAcctGb").val();			
		acctLevel = $('#iAcctLevel').val();
		acctCode = $('#iAcctUp').val().trim();
	} else if ( $('#div_drag_3').attr("style").indexOf("block") != -1 ) {	//console.log('수정');
		stanYy = $("#uStanYy").val();
		acctGb = $("#uAcctGb").val();
		acctLevel = $('#uAcctLevel').val();
		acctCode = $('#uAcctUp').val().trim();
	}
			
	if ( stanYy == null || '' == stanYy )		{	alert("회계년도를 입력하세요.");	return;	}
	if ( acctGb == null || '' == acctGb )		{	alert("재정을 선택하세요.");		return;	}
	if ( acctLevel == null || '' == acctLevel )	{	alert("구분을 선택하세요.");		return;	}
	//console.log('acctGb = '+acctGb);
	//console.log('acctLevel = '+acctLevel);
	
	var myPostData = $("#grid_acctcode_popup").jqGrid('getGridParam', 'postData');
	
	if ('소'==acctLevel )         {	acctLevel = '중';	// 상위계정검색
	} else if  ('중'==acctLevel ) {	acctLevel = '대';	// 상위계정검색
	} else {
	}
	
	myPostData.stanYy   = stanYy;
	myPostData.acctGb    = acctGb;
	myPostData.acctLevel = acctLevel;
	//myPostData.acctCode  = acctCode;
	myPostData.acctQuery  = acctCode;
	
	
	$("#grid_acctcode_popup").trigger("reloadGrid");
	
	$("#div_acctcode").show();
	var layerH = $("#div_acctcode").height();
	$("#div_acctcode").css({"margin-top": -(layerH/2)+"px"});
	
	$("body").append("<div class='maskPop'></div>");
}



function makeNextFromPrev() {
	let stanYy = $("#stanYy").val();
	let msg = (stanYy)+"년도 계정과목을 복사하여 "+(stanYy-1+2)+"년도 계정과목을 만드시겠습니까?";
	if(confirm(msg) == false) return false;

	var url = contextRoot + "/env/acctcode/makeNextFromPrev.json";
	var params = "prevStanYy=" + (stanYy);
		params += "&nextStanYy=" + (stanYy-1+2);
	//alert(params);
	
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
