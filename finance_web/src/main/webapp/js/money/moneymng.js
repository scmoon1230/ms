
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/money/mng/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd        : $("#stanYmd").val().replaceAll("-", "")
			, startYmd     : $("#startYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", "")
			, endYmd       : $("#endYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", "")
			, acctGb       : $("#acctGb").val()
			, moneyCode    : $("#moneyCode").val()
			, worshipCode  : $("#worshipCode").val()
			, memberNo     : $("#memberNo").val()
			, memberName   : $("#memberName").val()
			, userId       : $("userId").val()
			, userIdYn     : $('#userIdYn').is(':checked')
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','acctGb','재정','moneyCode','헌금분류','순번','worshipCode','예배','금액','신도성명','신도아이디','신도번호'
					,'positionCode','직분','deptCode','교구','regionCode','구역','마감','userId','등록자','등록일시'
				   ],
		colModel: [{ name: 'CHECK', width:20 , align:'center', editable:true, edittype:'checkbox'
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'      , index: 'STAN_YMD'     , width:70 , align:'center'
						//, formatter : function(cellvalue, options, rowObject) {
						//	return rowObject.stanYmd.toString().replaceAll('-', '');
						//}
					}, { name: 'acctGb'	      , index: 'ACCT_GB'      , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME' , width:40 , align:'center'
					}, { name: 'moneyCode'    , index: 'MONEY_CODE'   , hidden:true
					}, { name: 'moneyName'    , index: 'MONEY_NAME'   , width:80, align:'center'
					}, { name: 'detSeq'       , index: 'DET_SEQ'      , width:40 , align:'center'
					}, { name: 'worshipCode'  , index: 'WORSHIP_CODE' , hidden:true
					}, { name: 'worshipName'  , index: 'WORSHIP_NAME' , width:90 , align:'center'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'    , width:70 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'memberName'   , index: 'MEMBER_NAME'  , width:80 , align:'center'
					}, { name: 'memberId'     , index: 'MEMBER_ID'    , width:100, align:'center'
					}, { name: 'memberNo'     , index: 'MEMBER_NO'    , width:60 , align:'center'
					}, { name: 'positionCode' , index: 'POSITION_CODE' , hidden:true
					}, { name: 'positionName' , index: 'POSITION_NAME' , width:50 , align:'center'
					}, { name: 'deptCode'     , index: 'DEPT_CODE'     , hidden:true
					}, { name: 'deptName'     , index: 'DEPT_NAME'     , width:50 , align:'center'
					}, { name: 'regionCode'   , index: 'REGION_CODE'   , hidden:true
					}, { name: 'regionName'   , index: 'REGION_NAME'   , width:50 , align:'center'
					}, { name: 'closeYn'      , index: 'CLOSE_YN'      , width:30 , align:'center'
					}, { name: 'userId'       , index: 'USER_ID'       , hidden:true
					}, { name: 'userName'     , index: 'USER_NAME'     , width:60 , align:'center'
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
		sortname: 'INPUT_TIME DESC, DET_SEQ',
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
			$("#dMemberNo").html(list.memberNo);
			$("#dMemberName").html(list.memberName);
			$("#dMoneyAmt").html(list.moneyAmt);
			$("#dMoneyCode").val(list.moneyCode);
			
			if ( "N"==list.closeYn ) {	
				$(".btnMd").removeClass('hide');
				$(".btnDe").removeClass('hide');
			} else {
				$(".btnMd").addClass('hide');
				$(".btnDe").addClass('hide');
			}
			
			if ( list.userId != $("#loginUserId").val() ) {	// 본인이 등록한 건만 수정한다.
				$(".btnMd").addClass('hide');
				$(".btnDe").addClass('hide');
			}

			if ( '관리' == $("#loginUserGb").val() ) {	// 관리자는 항상 수정한다.
				$(".btnMd").removeClass('hide');
				$(".btnDe").removeClass('hide');
				if ( "Y"==list.closeYn ) {
					$('#uMoneyAmt').attr('readonly','true');
				}
			}
			
			$("#uStanYmd").val(list.stanYmd);
			$("#nStanYmd").val(list.stanYmd);
			$("#uMoneyCode").val(list.moneyCode);
			$("#nMoneyCode").val(list.moneyCode);
			$("#uDetSeq").html(list.detSeq);
			$("#uWorshipCode").val(list.worshipCode);
			$("#uMemberId").html(list.memberId);
			$("#uMemberNo").val(list.memberNo);
			$("#uMemberName").val(list.memberName);
			$("#uMoneyAmt").val(list.moneyAmt);

			$.showDetail();
		},
		loadComplete : function (data) {
			
			var params = "stanYmd="      + $("#stanYmd").val().replaceAll("-", "");
				params += "&startYmd="   + $("#startYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", "");
				params += "&endYmd="     + $("#endYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", "");
				params += "&acctGb="     + $("#acctGb").val();
				params += "&moneyCode="  + $("#moneyCode").val();
				params += "&worshipCode="+ $("#worshipCode").val();
				params += "&memberNo="   + $("#memberNo").val();
				params += "&memberName=" + $("#memberName").val();
				params += "&userId="     + $("#userId").val();
				params += "&userIdYn="   + $('#userIdYn').is(':checked');
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/money/mng/getSum.json",
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

		//alert($("#startYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", ""));

		//검색할 조건의 값을 설정한다.
		myPostData.stanYmd      = $("#stanYmd").val().replaceAll("-", "");
		myPostData.startYmd     = $("#startYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", "");
		myPostData.endYmd       = $("#endYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", "");
		myPostData.acctGb       = $("#acctGb").val();
		myPostData.moneyCode	= $("#moneyCode").val();
		myPostData.worshipCode  = $("#worshipCode").val();
		myPostData.memberNo     = $("#memberNo").val();
		myPostData.memberName   = $("#memberName").val();
		myPostData.userId       = $("#userId").val();
		myPostData.userIdYn     = $('#userIdYn').is(':checked');
		
		$("#grid").trigger("reloadGrid");
		
		$("#iMoneyCode").val($("#moneyCode").val());
		$("#iWorshipCode").val($("#worshipCode").val());
	});

	//$(".tableType1").css('height', window.innerHeight - 210);
	//$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 250);
	$(".tableType1").css('height', window.innerHeight - 240);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 280);
	
	
	
	
	
	//헌금자선택
	$.jqGrid('#grid_member_popup', {
		url:contextRoot + '/env/member/list.json',
		datatype: "json",
		postData: {
			memberQuery : '',
			useYn       : ''
		},
		colNames: ['NO','','memberId','헌금자번호','헌금자성명','직분','교구','구역','가족사항'
			   ],
		colModel: [{ name:'rk'             , index:'RK'           , width:40, align:'center'
					},{ name:'CHECK', width:30, align:'center', editable:true, edittype:'radio'
						, editoptions: { value:"True:False"}, sortable: false
						, formatter:function (cellValue, option) {
							return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
						}
					},{ name:'memberId'    , index:'MEMBER_ID'    , hidden:true
					},{ name:'memberNo'    , index:'MEMBER_NO'    , width:90, align:'center'
					},{ name:'memberName'  , index:'MEMBER_NAME'  , width:100, align:'center'
					},{ name:'positionName', index:'POSITION_NAME', width:70, align:'center'
					},{ name:'deptName'    , index:'DEPT_NAME'    , width:70, align:'center'
					},{ name:'regionName'  , index:'REGION_NAME'  , width:70, align:'center'
					},{ name:'familyRemark', index:'FAMILY_REMARK', width:200, align:'center'
					}
	 			],
		pager: '#pager',
		rowNum : 1000,
		height: $("#grid_member_popup").parent().height()-40,
		sortname: 'MEMBER_NO, MEMBER_NAME',
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
			$("#grid_member_popup input[type=radio]").get(rowid - 1).checked = true;
			$("#grid_member_popup input[type=radio]").get(rowid - 1).focus();
		},
		onCellSelect: function(rowid, iCol, cellcontent, e){
			
		},
		ondblClickRow: function(rowid, iRow, iCol, e){
			var list = jQuery("#grid_member_popup").getRowData(rowid);
			
			$('#iMemberId_'+ind).val(list.memberId);
			$('#iMemberNo_'+ind).val(list.memberNo);
			$('#iMemberName_'+ind).val(list.memberName);
			if ( list.familyRemark == '') {
				$('#iMemberInfo_'+ind).html(list.positionName);
			} else {
				$('#iMemberInfo_'+ind).html(list.positionName+' / '+list.familyRemark);
			}

			$('#iMoneyAmt_'+ind).focus();
			
			$(".layerPop .btnPopC").click();
		},
		beforeRequest: function () {
				//var oParams = $('#grid_cctv_used_popup').getGridParam();
				//if (oParams.hasOwnProperty('url') && oParams.url == '') { return false; }
		},
		beforeProcessing: function(data, status, xhr){

		},
		loadComplete: function () {
			$("#grid_member_popup input[type=radio]").change(function(){
				$("#grid_member_popup").jqGrid('setSelection',$(this).val(),true);
			});
			
			// 첫번째 열 선택
			setTimeout(function() {
				let $rows = $('#grid_member_popup tr.jqgrow[role=row]');
				if ($rows.length) {
					$('#grid_member_popup').jqGrid('setSelection', $rows.get(0).id);	
				} else {
					alert("정보가 없습니다.");
					$(".layerPop .btnPopC").click();
				}
			}, 500);
		}
 	});

	// 헌금자선택창에서 숫자 입력
	$(document).on('keydown', function (e) {
		let serStyle = $(".layerPopSearch").attr("style");	//alert(serStyle);
		if ( serStyle != undefined && serStyle.indexOf("block") != -1 ) {
			if ( 0<=e.key && e.key<=9 ) {		//alert(e.key+" , "+e.keyCode);
				$('#grid_member_popup').jqGrid('setSelection', e.key);	
			}
		}
	});

	// 헌금자선택 후 엔터
	$(document).on('keydown', 'input[type="radio"]', function (e) {
		if (e.key === 'Enter') {
			e.preventDefault(); // Prevent default behavior
			
			const isChecked = $(this).is(':checked');
			if (!isChecked) {
				$(this).prop('checked', true);
			}
			
			var list = jQuery("#grid_member_popup").getRowData($(this).val());

			if ( $('#div_drag_2').attr("style").indexOf("block") != -1 ) {		//console.log('등록');
				$('#iMemberId_'+ind).val(list.memberId);
				$('#iMemberNo_'+ind).val(list.memberNo);
				$('#iMemberName_'+ind).val(list.memberName);
				if ( list.familyRemark == '') {
					$('#iMemberInfo_'+ind).html(list.positionName);
				} else {
					$('#iMemberInfo_'+ind).html(list.positionName+' / '+list.familyRemark);
				}
				$('#iMoneyAmt_'+ind).focus();
							
			} else  if ( $('#div_drag_3').attr("style").indexOf("block") != -1 ) {	//console.log('수정');
				$('#uMemberId').html(list.memberId);
				$('#uMemberNo').val(list.memberNo);
				$('#uMemberName').val(list.memberName);				
			}

			$(".layerPop .btnPopC").click();
		}
	});

	//팝업 적용버튼
	$(".layerPop .btnPopApply").bind("click",function(){
		
		let rqstVal = '';
		var $radio = $('input[type="radio"]');
		$.each($radio, function (j, v) {
			if ( $(v).is(':checked') )
				rqstVal = $(v).val();
		});
		
		var list = jQuery("#grid_member_popup").getRowData(rqstVal);

		if ( $('#div_drag_2').attr("style").indexOf("block") != -1 ) {		//console.log('등록');
			$('#iMemberId_'+ind).val(list.memberId);
			$('#iMemberNo_'+ind).val(list.memberNo);
			$('#iMemberName_'+ind).val(list.memberName);
			if ( list.familyRemark == '') {
				$('#iMemberInfo_'+ind).html(list.positionName);
			} else {
				$('#iMemberInfo_'+ind).html(list.positionName+' / '+list.familyRemark);
			}
			$('#iMoneyAmt_'+ind).focus();
						
		} else  if ( $('#div_drag_3').attr("style").indexOf("block") != -1 ) {	//console.log('수정');
			$('#uMemberId').html(list.memberId);
			$('#uMemberNo').val(list.memberNo);
			$('#uMemberName').val(list.memberName);				
		}

		$(".layerPop .btnPopC").click();
	});
		
	//등록 시 헌금자정보 검색
	$(".searchMem").bind("keydown", function(e) {
		if(e.keyCode == 13) { searchMemberInfo($(this)); }
	});
	//수정 시 헌금자정보 검색
	$(".searchMem2").bind("keydown", function(e) {
		if(e.keyCode == 13) { searchMemberInfo($(this)); }
	});
		
	//헌금액수 입력
	$(".checkMoney").bind("focus", function(e) {
		checkAutoAct($(this));
	});
	$(".checkMoney").bind("keydown", function(e) {
		if(e.keyCode == 13) { checkMoneyAmt($(this)); }
	});
	$(".txtType").bind("keydown", function(e) {
		if(e.keyCode == 13) {
			let idVal = $(this).attr("id");
			let idVals = idVal.split('_');
			if ( "uMoneyAmt" == idVal ) {
				val = $(this).val().trim().replaceAll(',','');
				if ( val == '' || isNaN(val) ) {
					alert('숫자를 입력하십시오.');	$(this).focus();
				} else {
					$('#btnMod').focus();
				}
			} else if ( idVals.length == 2 ) {
				if ( "iMemberName" == idVals[0] ) {
					$("#iMoneyAmt_"+idVals[1]).focus();
				}
			}
		 }
	});
		
	//팝업 취소버튼
	$(".layerPop .btnPopC").bind("click",function(){
		$(".maskPop").remove();
		$(".layerPopSearch").hide();
		return false;
	});

	len = $('.searchMem').length;
	
	changeUserId();
	
	$('#btnModReg').bind("click",function(){

		var s =  $.getSelRow("#grid");
		if(s.length == 0){
			alert("수정할 데이터를 선택하십시오.");
			return false;
		}
	
		if($("#uiMoneyCode").val().trim() == "") { alert("헌금을 선택하세요."); $("#uiMoneyCode").focus(); return false; }
		if($("#uiWorshipCode").val().trim() == "") { alert("예배를 선택하세요."); $("#uiWorshipCode").focus(); return false; }
		
		if(confirm("선택한 자료를 수정하시겠습니까?") == false) return false;
		var url = contextRoot + "/money/mng/modifyMulti.json";
		var params = "newMoneyCode="    + $("#uiMoneyCode").val();
			params += "&newWorshipCode="+ $("#uiWorshipCode").val();
	
		//alert(s.length);
		for(var i = 0; i < s.length; i++) {
			var list = jQuery("#grid").getRowData(s[i]);
			params += "&moneyInfo=" + list.stanYmd.replaceAll("-", "") + ":" + list.moneyCode + ":" + list.detSeq;
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
				
				$(".mask").remove();
				$(".layer").hide();
			},
			error:function(e){
				//alert(e.responseText);
				alert(data.msg);
			}
		});


	});
});

function checkUMemberNo() {
	//if ( 6 != $('#uMemberNo').val().length ) {
	//	$('#uMemberId').html('');
	//}
}

function changeUserId() {
	resetUserId($("#stanYmd").val().replaceAll("-", ""));
}

function changeMoneyCode() {
	let stanYy = $("#stanYmd").val().replaceAll("-", "").substring(0,4);
	let acctGb = $("#acctGb").val();
	resetMoneyCode(stanYy, acctGb);
}

function checkMoneyCode() {
	if ( "008" == $("#moneyCode").val() ) {
		$(".btnRgt").hide();
	} else {
		$(".btnRgt").show();
	}
}

var len = 0;
var ind = 0;

function searchMemberInfo2() {
	let obj = $('#uMemberNo');
	if ( '' == obj.val().trim() ) {
		alert('신도번호를 입력하세요.');	obj.focus();
	} else {
		searchMemberInfo(obj);
	}
}
	
function searchMemberInfo(obj) {

	var myPostData = $("#grid_member_popup").jqGrid('getGridParam', 'postData');
		
	if ( 'uMemberNo' == obj.attr("id") ) {	// 한건수정일 때
		myPostData.memberQuery = obj.val().trim();
	} else {
		ind = obj.attr("id").split("_")[1];
		myPostData.memberQuery = $('#iMemberNo_'+ind).val().trim();
	}
	
	$("#grid_member_popup").trigger("reloadGrid");
	
	$(".layerPopSearch").show();
	var layerH = $(".layerPopSearch").height();
	$(".layerPopSearch").css({"margin-top": -(layerH/2)+"px"});
	
	$("body").append("<div class='maskPop'></div>");
}

function checkAutoAct(obj) {
	if ( $('#autoYn').is(':checked') && $("#autoAct1").is(":checked") ) {	// 자동입력
		obj.val($('#autoMoney').val());
		
		ind = obj.attr("id").split("_")[1];
		ind ++;
		if ( ind < len ) {
			if ( $('#iMemberNo_'+ind).hasClass('hide') ) {
				$('#iMemberName_'+ind).focus();
			} else {
				$('#iMemberNo_'+ind).focus();
			}
		} else {
			$('#btnReg').focus();
		}
	}
}

function checkMoneyAmt(obj) {
	ind = obj.attr("id").split("_")[1];
	val = obj.val().trim().replaceAll(',','');
	
	if ( val == '' || isNaN(val) ) {
		alert('숫자를 입력하십시오.');	obj.focus();
		
	} else {
		if ( $('#autoYn').is(':checked') && $("#autoAct2").is(":checked") ) {	// 자동곱하기
			val = val * Number($('#autoMoney').val());
		}
		obj.val(val.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","));
		
		ind ++;
		if ( ind < len ) {
			if ( $('#iMemberNo_'+ind).hasClass('hide') ) {
				$('#iMemberName_'+ind).focus();
			} else {
				$('#iMemberNo_'+ind).focus();
			}
		} else {
			$('#btnReg').focus();
		}
	}
}

function changeMemberNoYn() {
	resetAction();
	
	if ( $('#memberNoYn').is(':checked') ) {
		for ( var i=0 ; i<len ; i++ ) {
			$('#iMemberNo_'+i).addClass('hide');
		}
		$('#iMemberName_0').focus();
	} else {
		for ( var i=0 ; i<len ; i++ ) {
			$('#iMemberNo_'+i).removeClass('hide');
		}
		$('#iMemberNo_0').focus();
	}
}

function resetAction() {
	//$("#iMoneyCode").val("");
	//$("#iWorshipCode").val("");
	for ( var i=0 ; i<len  ; i++ ) {
		$('#iMemberId_'+i).val('');
		$('#iMemberNo_'+i).val('');
		$('#iMemberName_'+i).val('');
		$('#iMoneyAmt_'+i).val('');
		$('#iMemberInfo_'+i).html('');
	}
	$("#iMemberNo_0").focus();
}

function preModifyAction() {
}

function validate() {
	if ( insertFlag ) {
		if($("#iMoneyCode").val().trim() == "") { alert("헌금을 선택하세요."); $("#iMoneyCode").focus(); return false; }
		if($("#iWorshipCode").val().trim() == "") { alert("예배를 선택하세요."); $("#iWorshipCode").focus(); return false; }
	} else {
		if($("#uMoneyAmt").val().trim() == "") { alert("금액을 입력하세요."); $("#uMoneyAmt").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	let infoType = "id";
	if ( $('#memberNoYn').is(':checked') ) { infoType = "name"; }
	console.log('infoType = '+infoType);
	console.log('len = '+len);
			
	let infoData = "";
	for ( var i=0 ; i<len ; i++ ) {
		if ( infoType == "id" ) {
			if ( $('#iMemberId_'+i).val().trim() != '' ) {
				if ( $('#iMoneyAmt_'+i).val().trim() == '') {
					alert("금액을 입력하세요.");	$('#iMoneyAmt_'+i).focus();	return;
				} else {
					infoData += ","+$('#iMemberId_'+i).val()+":"+$('#iMoneyAmt_'+i).val().replaceAll(',','');
				}
			}	
		} else {
			if ( $('#iMemberName_'+i).val() != '' ) {
				if ( $('#iMoneyAmt_'+i).val().trim() == '') {
					alert("금액을 입력하세요.");	$('#iMoneyAmt_'+i).focus();	return;
				} else {
					infoData += ","+$('#iMemberName_'+i).val().trim()+":"+$('#iMoneyAmt_'+i).val().replaceAll(',','');
				}
			}	
		}
	}
	if ( infoData == "" ) {
		alert("정보를 입력하세요.");	return;
	} else {
		infoData = infoData.substring(1);
	}
	console.log(infoData);

	var url = contextRoot + "/money/mng/insert.json";
	var params = "stanYmd="        + $("#stanYmd").val().replaceAll("-", "");
		params += "&moneyCode="    + $("#iMoneyCode").val();
		params += "&worshipCode="  + $("#iWorshipCode").val();
		params += "&infoType="     + infoType;
		params += "&infoData="     + encodeURIComponent(infoData);
	//console.log(params);
	
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
	var url = contextRoot + "/money/mng/update.json";
	var params = "stanYmd="        + $("#uStanYmd").val().replaceAll("-", "");
		params += "&moneyCode="    + $("#uMoneyCode").val();
		params += "&detSeq="       + $("#uDetSeq").html();
		
		if ( $("#uStanYmd").val() != $("#nStanYmd").val() ) {	// 기준일자 변경
			params += "&newStanYmd=" + $("#nStanYmd").val().replaceAll("-", "");
			params += "&newMoneyCode=" + $("#nMoneyCode").val();
			
		} else if ( $("#uMoneyCode").val() != $("#nMoneyCode").val() ) {	// 헌금분류만 변경
			params += "&newMoneyCode=" + $("#nMoneyCode").val();
		}
				
		params += "&worshipCode="  + $("#uWorshipCode").val();
		params += "&moneyAmt="     + encodeURIComponent($("#uMoneyAmt").val().replaceAll(',',''));
		
		if ( $('#uMemberId').html() != '' ) {	
			params += "&memberId="  + $('#uMemberId').html();
			params += "&memberName="  + encodeURIComponent($('#uMemberName').val().trim());
			params += "&idExist=Y";
		} else {
			params += "&memberId=";
			params += "&memberName="  + encodeURIComponent($('#uMemberName').val().trim());
			params += "&idExist=N";
			
		}

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
	var url = contextRoot + "/money/mng/delete.json";
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
	
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		if ( 'N' != list.closeYn ) {
			alert('마감된 건은 삭제할 수 없습니다.');	return false;
		}
		if ( list.userId != $("#loginUserId").val() ) {
			alert('본인이 등록한 건만 삭제할 수 있습니다.');	return false;
		}
	}

	if(confirm("선택한 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/money/mng/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&moneyInfo=" + list.stanYmd.replaceAll("-", "") + ":" + list.moneyCode + ":" + list.detSeq;
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

function modifyMultiAction(obj) {
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("수정할 데이터를 선택하십시오.");
		return false;
	}
	
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		if ( 'N' != list.closeYn ) {
			alert('마감된 건은 수정할 수 없습니다.');	return false;
		}
		if ( list.userId != $("#loginUserId").val() ) {
			alert('본인이 등록한 건만 수정할 수 있습니다.');	return false;
		}
	}
	
	$(".layerModifyMulti").show();
	
	$("body").append("<div class='mask'></div>");
		
	/*
	if(confirm("선택한 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/money/mng/deleteMulti.json";
	var params = "";

	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&moneyInfo=" + list.stanYmd.replaceAll("-", "") + ":" + list.moneyCode + ":" + list.detSeq;
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
	*/
}

function excelDownAction() {
	//if ( $("#rowCnt").html() >= 10 ) { alert("10건 이상은 다운로드할 수 업습니다."); return; }
	
	var sTitleKey = "stanYmd|acctGbName|moneyName|worshipName|moneyAmt|memberName|memberNo|userName|inputTime";
	var sTitleHeader  = encodeURIComponent("기준일자|재정|헌금|예배|금액|신도성명|신도번호|등록자|등록일시");
		
	let $formDownload = $('#form-excelFile');
	if ($formDownload.length) {
		$('#form-excelFile').attr('action',contextRoot + '/money/mng/excel.json');
		$('#form-excelFile input[name=stanYmd]').val($("#stanYmd").val().replaceAll("-", ""));
		$('#form-excelFile input[name=startYmd]').val($("#startYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", ""));
		$('#form-excelFile input[name=endYmd]').val($("#endYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", ""));
		$('#form-excelFile input[name=acctGb]').val($("#acctGb").val());
		$('#form-excelFile input[name=moneyCode]').val($("#moneyCode").val());
		$('#form-excelFile input[name=worshipCode]').val($("#worshipCode").val());
		$('#form-excelFile input[name=memberNo]').val($("#memberNo").val());
		$('#form-excelFile input[name=memberName]').val($("#memberName").val());
		$('#form-excelFile input[name=userId]').val($("#userId").val());
		$('#form-excelFile input[name=userIdYn]').val($('#userIdYn').is(':checked'));
		
		$('#form-excelFile input[name=sidx]').val("INPUT_TIME DESC, DET_SEQ");
		$('#form-excelFile input[name=sord]').val("DESC");
		$('#form-excelFile input[name=titleKey]').val(sTitleKey);
		$('#form-excelFile input[name=titleHeader]').val(sTitleHeader);
	} else {
		$formDownload = $('<form/>', {'id':'form-excelFile', 'method': 'POST'	 , 'action': contextRoot + '/money/mng/excel.json'	});
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'stanYmd'    , 'value':$("#stanYmd").val().replaceAll("-", "")	}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'startYmd'   , 'value':$("#startYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", "")	}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'endYmd'     , 'value':$("#endYmd").val().replaceAll("-", "").replaceAll(":", "").replaceAll("T", "")	}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'acctGb'     , 'value':$("#acctGb").val()			}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'moneyCode'  , 'value':$("#moneyCode").val()		}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'worshipCode', 'value':$("#worshipCode").val()		}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'memberNo'   , 'value':$("#memberNo").val()			}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'memberName' , 'value':$("#memberName").val()		}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'userId'     , 'value':$("#userId").val()		}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'userIdYn'   , 'value':$('#userIdYn').is(':checked')	}));
		
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'sidx'       , 'value':"INPUT_TIME DESC, DET_SEQ"	}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'sord'       , 'value':"DESC"						}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'titleKey'   , 'value':sTitleKey					}));
		$formDownload.append($('<input/>', {'type':'hidden', 'name':'titleHeader', 'value':sTitleHeader					}));
		
		$formDownload.appendTo(document.body);
	}
	$formDownload.submit();
}
