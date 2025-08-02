
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/env/member/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			memberId       : $("#memberId").val()
			, memberNo     : $("#memberNo").val()
			, memberName   : $("#memberName").val()
			, positionCode : $("#positionCode").val()
			, deptCode     : $("#deptCode").val()
			, regionCode   : $("#regionCode").val()
			, useYn        : $("#useYn").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'No','신도아이디','신도번호','신도명','성별','연락처','핸드폰','직분','직분'
					,'교구','교구','구역','구역','가족사항','주소','사용여부','사용여부','등록일'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'rk'           , index: 'RK'            , hidden:true
					}, { name: 'memberId'     , index: 'MEMBER_ID'     , width:150, align:'center'
					}, { name: 'memberNo'     , index: 'MEMBER_NO'     , width:100, align:'center'
					}, { name: 'memberName'   , index: 'MEMBER_NAME'   , width:100, align:'center'
					}, { name: 'sexType'      , index: 'SEX_TYPE'      , width:50 , align:'center'
					}, { name: 'telNo'        , index: 'TEL_NO'        , width:100, align:'center'
					}, { name: 'hphoneNo'     , index: 'HPHONE_NO'     , width:100, align:'center'
					}, { name: 'positionCode' , index: 'POSITION_CODE' , hidden:true
					}, { name: 'positionName' , index: 'POSITION_NAME' , width:100, align:'center'
					}, { name: 'deptCode'     , index: 'DEPT_CODE'     , hidden:true
					}, { name: 'deptName'     , index: 'DEPT_NAME'     , width:100, align:'center'
					}, { name: 'regionCode'   , index: 'REGION_CODE'   , hidden:true
					}, { name: 'regionName'   , index: 'REGION_NAME'   , width:100, align:'center'
					}, { name: 'familyRemark' , index: 'FAMILY_REMARK' , width:200, align:'center'
					}, { name: 'addr'         , index: 'ADDR'          , width:200, align:'center'
					}, { name: 'useYn'        , index: 'USE_YN'        , hidden:true
					}, { name: 'useYnName'    , index: 'USE_YN'        , width:70 , align:'center'
						   , formatter : function(cellvalue, options, rowObject) {
								if ( 'Y' == rowObject.useYn ) return '사용';
								else return '미사용';
							}
					}, { name: 'regDate'      , index: 'REG_DATE'      , hidden:true
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'MEMBER_ID',
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
			$("#dUseYn").html(list.useYnName);

			$("#uMemberId").val(list.memberId);
			$("#uMemberNo").val(list.memberNo);
			$("#uMemberName").val(list.memberName);
			$("#uSexType").val(list.sexType);
			$("#uTelNo").val(list.telNo);
			$("#uHphoneNo").val(list.hphoneNo);
			$("#uPositionCode").val(list.positionCode);
			$("#uDeptCode").val(list.deptCode);
			$("#uRegionCode").val(list.regionCode);
			$("#uAddr").val(list.addr);
			$("#uFamilyRemark").val(list.familyRemark);
			$("#uUseYn").val(list.useYn);

			$.showDetail();
		},
		loadComplete : function (data) {

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
		myPostData.memberId = $("#memberId").val();
		myPostData.memberNo = $("#memberNo").val();
		myPostData.memberName = $("#memberName").val();
		myPostData.positionCode = $("#positionCode").val();
		myPostData.deptCode = $("#deptCode").val();
		myPostData.regionCode = $("#regionCode").val();
		myPostData.useYn = $("#useYn").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".btnE").bind("click",function(){
		//if ( $("#rowCnt").html() >= 10 ) { alert("10건 이상은 다운로드할 수 업습니다."); return; }
		
		$("#paraMemberId").val($("#memberId").val());
		$("#paraMemberNo").val($("#memberNo").val());
		$("#paraMemberName").val($("#memberName").val());
		$("#paraPositionCode").val($("#positionCode").val());
		$("#paraDeptCode").val($("#deptCode").val());
		$("#paraRegionCode").val($("#regionCode").val());
		$("#paraUseYn").val($("#useYn").val());

		$("#sidx").val("MEMBER_ID");
		$("#sord").val("DESC");
		$("#titleKey").val("memberId|memberNo|memberName|sexType|telNo|hphoneNo|positionName|deptName|regionName|familyRemark|addr|useYnName");
		$("#titleHeader").val(encodeURIComponent("신도아이디|신도번호|신도명|성별|연락처|핸드폰|직분|교구|구역|가족사항|주소|사용여부"));
		url = "/env/member/excel.do";
		$("#dataForm").attr("action", url);
		$("#dataForm").attr("method", "post");
		$("#dataForm").submit();
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});

function resetAction() {
	//$("#iMemberId").val("");
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
	$("#iUseYn").val("Y");
}

function preModifyAction() {
}

function validate() {
	if ( insertFlag ) {
		//if($("#iMemberId").val().trim() == "") { alert("아이디를 입력하세요."); $("#iMemberId").focus(); return false; }
		if($("#iMemberNo").val().trim() == "") { alert("번호를 입력하세요."); $("#iMemberNo").focus(); return false; }
		if($("#iMemberName").val().trim() == "") { alert("신도명을 입력하세요."); $("#iMemberName").focus(); return false; }
	}
	return true;
}

function insertAction(obj) {
	
	var url = contextRoot + "/env/member/insert.json";
	//var params = "memberId=" + encodeURIComponent($("#iMemberId").val().trim());
	var params = "memberId=";
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
	var url = contextRoot + "/env/member/update.json";
	var params = "memberId="       + encodeURIComponent($("#uMemberId").val().trim());
		params += "&memberNo="     + encodeURIComponent($("#uMemberNo").val().trim());
		params += "&memberName="   + encodeURIComponent($("#uMemberName").val().trim());
		params += "&sexType="      + encodeURIComponent($("#uSexType").val());
		params += "&telNo="        + encodeURIComponent($("#uTelNo").val().trim());
		params += "&hphoneNo="     + encodeURIComponent($("#uHphoneNo").val().trim());
		params += "&positionCode=" + encodeURIComponent($("#uPositionCode").val());
		params += "&deptCode="     + encodeURIComponent($("#uDeptCode").val());
		params += "&regionCode="   + encodeURIComponent($("#uRegionCode").val());
		params += "&addr="         + encodeURIComponent($("#uAddr").val().trim());
		params += "&familyRemark=" + encodeURIComponent($("#uFamilyRemark").val().trim());
		params += "&useYn="        + encodeURIComponent($("#uUseYn").val());

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
	//var url = contextRoot + "/env/member/delete.json";
	var url = contextRoot + "/env/member/update.json";
	var params = "memberId=" + $("#uMemberId").val();
		params += "&memberNo=" + $("#uMemberNo").val();
		
		params += "&memberName="   + encodeURIComponent($("#uMemberName").val().trim());
		params += "&sexType="      + encodeURIComponent($("#uSexType").val());
		params += "&telNo="        + encodeURIComponent($("#uTelNo").val().trim());
		params += "&hphoneNo="     + encodeURIComponent($("#uHphoneNo").val().trim());
		params += "&positionCode=" + encodeURIComponent($("#uPositionCode").val());
		params += "&deptCode="     + encodeURIComponent($("#uDeptCode").val());
		params += "&regionCode="   + encodeURIComponent($("#uRegionCode").val());
		params += "&addr="         + encodeURIComponent($("#uAddr").val().trim());
		params += "&familyRemark=" + encodeURIComponent($("#uFamilyRemark").val().trim());
		params += "&useYn=" + "N";

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			//alert(data.msg);
			alert("미사용으로 수정하였습니다.");
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
}
/*
function deleteMultiAction(obj) {
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하십시오.");
		return false;
	}

	if(confirm("선택한 자료를 삭제하시겠습니까?") == false) return false;
	var url = contextRoot + "/env/member/deleteMulti.json";
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
*/
