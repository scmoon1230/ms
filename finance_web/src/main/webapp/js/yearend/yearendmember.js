
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/yearend/member/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			memberNo       : $("#memberNo").val()
			, memberName   : $("#memberName").val()
			, positionCode : $("#positionCode").val()
			, useYn        : $("#useYn").val()
			, juminNoGb    : $("#juminNoGb").val()
			, agreeYn      : $("#agreeYn").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'신도아이디','신도번호','신도명','성별','연락처','핸드폰','직분','직분','교구','교구','구역','구역'
					,'주소','가족사항','useYn','재적상태','번호구분', '주민번호(사업번호)', '성명(홈택스)', '동의여부'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'memberId'     , index: 'MEMBER_ID'     , width:150, align:'center'
					}, { name: 'memberNo'     , index: 'MEMBER_NO'     , width:100, align:'center'
					}, { name: 'memberName'   , index: 'MEMBER_NAME'   , width:100, align:'center'
					}, { name: 'sexType'      , index: 'SEX_TYPE'      , width:50 , align:'center'
					}, { name: 'telNo'        , index: 'TEL_NO'        , width:100, align:'center'
					}, { name: 'hphoneNo'     , index: 'HPHONE_NO'     , width:100, align:'center'
					}, { name: 'positionCode' , index: 'POSITION_CODE' , hidden:true
					}, { name: 'positionName' , index: 'POSITION_NAME' , width:100, align:'center'
					}, { name: 'deptCode'     , index: 'DEPT_CODE'     , hidden:true
					}, { name: 'deptName'     , index: 'DEPT_NAME'     , hidden:true
					}, { name: 'regionCode'   , index: 'REGION_CODE'   , hidden:true
					}, { name: 'regionName'   , index: 'REGION_NAME'   , hidden:true
					}, { name: 'addr'         , index: 'ADDR'          , hidden:true
					}, { name: 'familyRemark' , index: 'FAMILY_REMARK' , width:200, align:'center'
					}, { name: 'useYn'        , index: 'USE_YN'        , hidden:true
					}, { name: 'useYnName'    , index: 'USE_YN'        , width:50 , align:'center'
						   , formatter : function(cellvalue, options, rowObject) {
								if ( 'Y' == rowObject.useYn ) return '재적';
								else return '이적';
							}
					}, { name: 'juminNoGb'   , index: 'JUMIN_NO_GB'   , width:100, align:'center'
						   , formatter : function(cellvalue, options, rowObject) {
								if ( '1' == rowObject.juminNoGb ) return '주민번호';
								else if ( '2' == rowObject.juminNoGb ) return '사업자번호';
								else return '';
							}
					}, { name: 'juminNo'   , index: 'JUMIN_NO'   , width:100, align:'center'
					}, { name: 'hometaxMemberName'   , index: 'HOMETAX_MEMBER_NAME'   , width:100, align:'center'
					}, { name: 'agreeYn'   , index: 'AGREE_YN'   , width:100, align:'center'
						   , formatter : function(cellvalue, options, rowObject) {
								if ( 'Y' == rowObject.agreeYn ) return '동의함';
								if ( 'N' == rowObject.agreeYn ) return '동의하지 않음';
								else return '';
							}
					}
		  		],
		pager: '#pager',
		rowNum: $('#rowPerPageList').val(),
		height: $("#grid").parent().height()-40,
		sortname: 'MEMBER_NO',
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
		myPostData.memberNo = $("#memberNo").val();
		myPostData.memberName = $("#memberName").val();
		myPostData.positionCode = $("#positionCode").val();
		myPostData.useYn = $("#useYn").val();
		myPostData.juminNoGb = $("#juminNoGb").val();
		myPostData.agreeYn = $("#agreeYn").val();
		
		$("#grid").trigger("reloadGrid");
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
