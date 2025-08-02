
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/money/digital/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd        : $("#stanYmd").val().replaceAll("-", "")
			, stanGb       : $("#stanGb").val()
			//, acctGb       : $("#acctGb").val()
			, birthDay     : $("#birthDay").val()
			, memberName   : $("#memberName").val()
			, closeYn      : $("#closeYn").val()
			//, noClose      : $('#noClose').is(':checked')
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'기준일자','stanGb','seqNo','구분'
					,'헌금시간','신도생일','신도성명','아이디' ,'헌금','헌금2','금액','종류번호'
					,'acctGb','재정','내부코드','순번','내부헌금명','연락처','등록'
					,'기도제목', '요청사항'
				   ],
		colModel: [{ name: 'CHECK', width:20 , align:'center', editable:true, edittype:'checkbox'
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'stanYmd'      , index: 'STAN_YMD'      , hidden:true
					}, { name: 'stanGb'       , index: 'STAN_GB'       , hidden:true
					}, { name: 'seqNo'        , index: 'SEQ_NO'        , hidden:true
					}, { name: 'stanGbNm'     , index: 'STAN_GB_NM'    , width:50 , align:'center'
					   , formatter : function(cellvalue, options, rowObject) {
							if ( '1' == rowObject.stanGb ) return '일반';
							else return '청년/대학';
						}
					}, { name: 'moneyTime'    , index: 'MONEY_TIME'    , width:100, align:'center' , hidden:true
					}, { name: 'birthDay'     , index: 'BIRTH_DAY'     , width:80 , align:'center'
					}, { name: 'memberName'   , index: 'MEMBER_NAME'   , width:60 , align:'center'
					}, { name: 'memberId'     , index: 'MEMBER_ID'     , width:80 , align:'center'
					}, { name: 'moneyName1'   , index: 'MONEY_NAME_1'  , width:80 , align:'center'
					}, { name: 'moneyName2'   , index: 'MONEY_NAME_2'  , hidden:true
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:70 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'moneyType'    , index: 'MONEY_TYPE'    , width:40 , align:'center'
					}, { name: 'acctGb'       , index: 'ACCT_GB'       , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME'  , width:40 , align:'center'
					}, { name: 'moneyCode'    , index: 'MONEY_CODE'    , width:40 , align:'center'
					}, { name: 'detSeq'       , index: 'DET_SEQ'       , width:20 , align:'center'
					}, { name: 'moneyName'    , index: 'MONEY_NAME'    , width:100, align:'center'
					}, { name: 'hpNo'         , index: 'HP_NO'         , width:70 , align:'center'
					}, { name: 'closeYn'      , index: 'CLOSE_YN'      , width:20 , align:'center'
					}, { name: 'prayTitle'    , index: 'PRAY_TITLE'    , hidden:true
					}, { name: 'etcRemark'    , index: 'ETC_REMARK'    , hidden:true
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'STAN_GB, SEQ_NO',
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
			$("#dMemberName").html(list.memberName);
			$("#dBirthDay").html(list.birthDay);
			$("#dHpNo").html(list.hpNo);
			$("#dMoneyAmt").html(list.moneyAmt);
			$("#dMoneyName").html(list.moneyName1);
			$("#dPrayTitle").html(list.prayTitle);
			$("#dEtcRemark").html(list.etcRemark);

			//$("#iMemberId").val(list.memberId);
			//$("#iMemberName").val(list.memberName);
			//$("#iHphoneNo").val(list.hphoneNo);
			//$("#iEtcRemark").val(list.familyRemark);

			$.showDetail();
		},
		loadComplete : function (data) {
			
			var params = "stanYmd="        + $("#stanYmd").val().replaceAll("-", "");
				params += "&stanGb="       + $("#stanGb").val();
				//params += "&acctGb="       + $("#acctGb").val();
				params += "&birthDay="     + $("#birthDay").val();
				params += "&memberName="   + $("#memberName").val();
				params += "&closeYn="      + $("#closeYn").val();
				//params += "&noClose="      + $('#noClose').is(':checked');
				
				console.log(params);
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/money/digital/getSum.json",
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
		myPostData.stanGb       = $("#stanGb").val();
		//myPostData.acctGb       = $("#acctGb").val();
		myPostData.birthDay     = $("#birthDay").val();
		myPostData.memberName   = $("#memberName").val();
		myPostData.closeYn      = $("#closeYn").val();
		//myPostData.noClose      = $('#noClose').is(':checked');
		
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
	
	var url = contextRoot + "/money/digital/insert.json";
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
	var url = contextRoot + "/money/digital/update.json";
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
	var url = contextRoot + "/money/digital/delete.json";
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

	var params = "";
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		if ( 'Y' == list.closeYn ) {
			alert('헌금으로 등록처리한 데이터는 삭제할 수 없습니다.');	return false;
		} else {
			params += "&moneyInfo=" + list.stanYmd.replaceAll("-", "") + ":" + list.stanGb + ":" + list.seqNo;
		}
	}
	if ( "" == params ) {	alert("삭제할 데이터가 없습니다.");	return false;	}

	if(confirm("선택한 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/money/digital/deleteMulti.json";
	
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

function doDigital() {
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("등록처리할 데이터를 선택하십시오.");
		return false;
	}

	var params = "";
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		if ( 'Y' == list.closeYn ) {
			alert('이미 헌금으로 등록처리한 데이터는 재등록할 수 없습니다.');	return false;
		} else {
			params += "&moneyInfo=" + list.stanYmd.replaceAll("-", "") + ":" + list.stanGb + ":" + list.seqNo;
		}
	}
	if ( "" == params ) {	alert("등록할 데이터가 없습니다.");	return false;	}

	if ( !confirm("헌금을 등록처리하시겠습니까?") ) return;
	var url = contextRoot + "/money/digital/register.json";
	
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

function cancelDigital() {
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("등록취소할 데이터를 선택하십시오.");
		return false;
	}
	
	var params = "";
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		if ( 'N' == list.closeYn ) {
			alert('아직 헌금으로 등록처리하지 않은 데이터는 취소할 수 없습니다.');	return false;
		} else {
			params += "&moneyInfo=" + list.stanYmd.replaceAll("-", "") + ":" + list.stanGb + ":" + list.seqNo;
		}
	}
	if ( "" == params ) {	alert("취소할 데이터가 없습니다.");	return false;	}
	
	if ( !confirm("헌금등록을 취소하시겠습니까?") ) return;
	var url = contextRoot + "/money/digital/remove.json";
	
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
