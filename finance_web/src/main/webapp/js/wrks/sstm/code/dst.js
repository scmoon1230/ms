
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url : contextRoot + '/wrks/sstm/code/dst/list.json',
		datatype: "json",
		postData: {
			dstrtCd : $("#sDstrtCd").val(),
			dstrtNm : $("#sDstrtNm").val(),
			useTyCd : $("#sUseTyCd").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, 'No', '지구코드', '지구명', '대표연락처', 'link url', 'vrs webrtc addr', '영상재생지원배속', '기본재생배속', '지구설명', 'dstrtTy', '지구유형', '사용유형', '사용유형코드'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox'
						, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					},{ name: 'rk'            , index: 'RK'              , width:50 , align:'center', sortable: false
					},{ name: 'dstrtCd'       , index: 'DSTRT_CD'        , width:50 , align:'center'
					},{ name: 'dstrtNm'       , index: 'DSTRT_NM'        , width:100, align:'center'
					},{ name: 'repTelNo'      , index: 'REP_TEL_NO'      , width:150, align:'center'
					},{ name: 'linkUrl'       , index: 'LINK_URL'        , width:150, align:'center'
					},{ name: 'vrsWebrtcAddr' , index: 'VRS_WEBRTC_ADDR' , width:150, align:'center'
					},{ name: 'playbackSpeed' , index: 'PLAYBACK_SPEED'  , width:150, align:'center'
						, formatter: function (cellvalue, options, rowObject) {
							if (cellvalue != '' && typeof cellvalue == 'string') {
								return cellvalue.split(':')[0];
							} else {
								return '';
							}
                		}
					},{ name: 'basicPlaybackSpeed' , index: 'BASIC_PLAYBACK_SPEED'  , width:150, align:'center'
						, formatter: function (cellvalue, options, rowObject) {
							if (cellvalue != '' && typeof cellvalue == 'string') {
								return cellvalue.split(':')[1];
							} else {
								return '';
							}
                		}
					},{ name: 'dstrtDscrt'    , index: 'DSTRT_DSCRT'     , width:100, align:'center'
					},{ name: 'dstrtTy'       , index: 'DSTRT_TY'        , width:50 , align:'center', 'hidden':true
					},{ name: 'dstrtTyNm'     , index: 'DSTRT_TY'        , width:50 , align:'center'
						, formatter: function (cellvalue, options, rowObject) {
							if ("WIDE" == rowObject.dstrtTy) {		return "광역";
							} else {								return "기초";
							}
						}
					},{ name: 'useTyNm'       , index: 'USE_TY_NM'       , width:50 , align:'center'
					},{ name: 'useTyCd'       , index: 'USE_TY_CD'       , width:50 , align:'center', 'hidden':true
					}
				  ],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
        autowidth: true,
		height: $("#grid").parent().height()-40,
		sortname: 'DSTRT_CD',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
			id: "DSTRT_CD",
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
		onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);

			$("#dDstrtCd").html(list.dstrtCd);
			$("#dDstrtNm").html(list.dstrtNm);
			$("#dRepTelNo").html(list.repTelNo);
			$("#dLinkUrl").html(list.linkUrl);
			$("#dVrsWebrtcAddr").html(list.vrsWebrtcAddr);
			$("#dPlaybackSpeed").html(list.playbackSpeed);
			$("#dBasicPlaybackSpeed").html(list.basicPlaybackSpeed);
			$("#dDstrtDscrt").html(list.dstrtDscrt);
			$("#dDstrtTy").html(list.dstrtTyNm);
			$("#dUseTyNm").html(list.useTyNm);

			$("#iDstrtCd").val(list.dstrtCd);
			$("#iDstrtNm").val(list.dstrtNm);
			$("#iRepTelNo").val(list.repTelNo);
			$("#iLinkUrl").val(list.linkUrl);
			$("#iVrsWebrtcAddr").val(list.vrsWebrtcAddr);
			$("#iPlaybackSpeed").val(list.playbackSpeed);
			$("#iBasicPlaybackSpeed").val(list.basicPlaybackSpeed);
			$("#iDstrtDscrt").val(list.dstrtDscrt);
			$.selectBarun("#iDstrtTy", list.dstrtTy);
			$.selectBarun("#iUseTyCd", list.useTyCd);
			
			//$("#uDstrtCd").html(list.dstrtCd);
			//$("#dstrtCdBak").val(list.dstrtCd);

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
		myPostData.dstrtCd = $("#sDstrtCd").val();
		myPostData.dstrtNm = $("#sDstrtNm").val();
		myPostData.useTyCd = $("#sUseTyCd").val();

		$("#grid").trigger("reloadGrid");
	});
    	
	$(".tableType1").css('height', window.innerHeight - 250);
   	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 300);
	    	
});

function resetAction() {
	//alert("resetAction");
	$("#iDstrtCd").val("");
	$("#iDstrtNm").val("");
	$("#iRepTelNo").val("");
	$("#iLinkUrl").val("");
	$("#iVrsWebrtcAddr").val("");
	$("#iPlaybackSpeed").val("");
	$("#iBasicPlaybackSpeed").val("");
	$("#iDstrtDscrt").val("");
	$("#iDstrtTy").get(0).selectedIndex=0;
	$("#iUseTyCd").get(0).selectedIndex=0;
	//$("#dstrtCdBak").val("");
}

function updateAction(obj) {
	//alert('updateAction');

	var url = contextRoot + "/wrks/sstm/code/dst/update.json";
	var params = "";
		params += "dstrtCd="        + encodeURIComponent($("#iDstrtCd").val());
		params += "&dstrtNm="       + encodeURIComponent($("#iDstrtNm").val());
		params += "&repTelNo="      + encodeURIComponent($("#iRepTelNo").val());
		params += "&linkUrl="       + encodeURIComponent($("#iLinkUrl").val());
		params += "&vrsWebrtcAddr=" + encodeURIComponent($("#iVrsWebrtcAddr").val());
		params += "&playbackSpeed=" + encodeURIComponent($("#iPlaybackSpeed").val())
		                            +":"+ encodeURIComponent($("#iBasicPlaybackSpeed").val());
		params += "&dstrtDscrt="    + encodeURIComponent($("#iDstrtDscrt").val());
		params += "&dstrtTy="       + $("#iDstrtTy").val();
		params += "&useTyCd="       + $("#iUseTyCd").val();
		//params += "&dstrtCdBak=" + $("#dstrtCdBak").val();

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

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function insertAction(obj) {
	//alert('insertAction');

	var url = contextRoot + "/wrks/sstm/code/dst/insert.json";
	var params = "";
		params += "dstrtCd="        + encodeURIComponent($("#iDstrtCd").val());
		params += "&dstrtNm="       + encodeURIComponent($("#iDstrtNm").val());
		params += "&repTelNo="      + encodeURIComponent($("#iRepTelNo").val());
		params += "&linkUrl="       + encodeURIComponent($("#iLinkUrl").val());
		params += "&vrsWebrtcAddr=" + encodeURIComponent($("#iVrsWebrtcAddr").val());
		params += "&playbackSpeed=" + encodeURIComponent($("#iPlaybackSpeed").val())
		                            +":"+ encodeURIComponent($("#iBasicPlaybackSpeed").val());
		params += "&dstrtDscrt="    + encodeURIComponent($("#iDstrtDscrt").val());
		params += "&dstrtTy="       + $("#iDstrtTy").val();
		params += "&useTyCd="       + $("#iUseTyCd").val();

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

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
	//alert('deleteAction');

	var url = contextRoot + "/wrks/sstm/code/dst/delete.json";
	var params = "dstrtCd=" + $("#iDstrtCd").val();

	$.ajaxEx($('#grid'), {
		url : url,
		datatype: "json",
		data: params,
		success:function(data){

			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			//alert("삭제하였습니다.");
			alert(data.msg);
			alert($("#iDstrtCd").val());
		},
		error:function(e){
			//alert(e.responseText);
			alert(data.msg);
		}
	});
}

function deleteMultiAction(obj) {
	//alert('deleteMultiAction');

	//var s =  $("#grid").jqGrid('getGridParam', 'selarrrow');
	//s = $("grid").getGridParam('selarrrow');
	//alert(s);
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	
	var url = contextRoot + "/wrks/sstm/code/dst/deleteMulti.json";
	var params = "";
	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);

		params += "&dstrtCd=" + list.dstrtCd;

	}
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

	return true;

}
