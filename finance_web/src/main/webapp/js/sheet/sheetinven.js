
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url:contextRoot + "/sheet/inven/list.json",
		datatype: "json",
		autowidth: true,
		postData: {
			stanYmd    : $("#stanYmd").val().replaceAll("-", "")
			, acctGb   : $("#acctGb").val()
			, inoutGb  : $("#inoutGb").val()
		},
		colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					,'acctGb','재정','전표번호','상위코드','상위계정','계정코드','계정','내역','전표금액','금액','구분','발생'
					,'userId','등록자'
				   ],
		colModel: [{ name: 'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox' , hidden:true
					, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					}, { name: 'acctGb'       , index: 'ACCT_GB'       , hidden:true
					}, { name: 'acctGbName'   , index: 'ACCT_GB_NAME'  , width:50 , align:'center'
					}, { name: 'seqNo'        , index: 'SEQ_NO'        , width:80, align:'center'
						, formatter : function(cellvalue, options, rowObject) {
							let val = rowObject.seqNo.toString();
							return val.substr(0,8)+'-'+val.substr(8);
						}
					}, { name: 'upCode'       , index: 'UP_CODE'       , width:70 , align:'center'
					}, { name: 'upName'       , index: 'UP_NAME'       , width:100, align:'left'
					}, { name: 'acctCode'     , index: 'ACCT_CODE'     , width:70 , align:'center'
					}, { name: 'acctName'     , index: 'ACCT_NAME'     , width:70 , align:'left'
					}, { name: 'acctRemark'   , index: 'ACCT_REMARK'   , width:200 , align:'left'
					}, { name: 'moneyAmt'     , index: 'MONEY_AMT'     , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
						}
					}, { name: 'upMoneyAmt'     , index: 'UP_MONEY_AMT'     , width:80 , align:'right'
						, cellattr : function() {	return 'style="padding-right: 5px;"';	}
						, formatter : function(cellvalue, options, rowObject) {
							if ( '수입' == $("#inoutGb").val() ) {
								return rowObject.moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							} else {
								return rowObject.upMoneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
							}
						}
					}, { name: 'inoutGb'      , index: 'INOUT_GB'      , width:40  , align:'center'
					}, { name: 'acctType'     , index: 'ACCT_TYPE'     , width:40  , align:'center'
					}, { name: 'userId'       , index: 'USER_ID'       , hidden:true
					}, { name: 'userName'     , index: 'USER_NAME'     , width:50 , align:'center'
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
			
			var params = "stanYmd=" + $("#stanYmd").val().replaceAll("-", "");
				params += "&acctGb=" + $("#acctGb").val();
				params += "&inoutGb=" + $("#inoutGb").val();
				
			$.ajaxEx($('#grid'), {
				url : contextRoot + "/sheet/inven/getSum.json",
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
		
		if ('수입'==$("#inoutGb").val()) {
			$("#grid").setGridParam({sortname: 'ACCT_GB, UP_MONEY_AMT DESC, MONEY_AMT'});
			$("#grid").setGridParam({sortorder: 'DESC'});
			
		} else {
			$("#grid").setGridParam({sortname: 'ACCT_GB, UP_MONEY_AMT DESC, MONEY_AMT'});
			$("#grid").setGridParam({sortorder: 'DESC'});
		}
		
		$("#grid").trigger("reloadGrid");
	});

	$(".btnP").bind("click",function(){
		let popupW = 800;
		let popupH = window.screen.height;
		let left = Math.ceil((window.screen.width - popupW)/2);
		let top = 0;
		window.open('/sheet/invenPrint.do','인쇄','width='+popupW+',height='+popupH+',left='+left+',top='+top);
		
	});

	$(".tableType1").css('height', window.innerHeight - 180);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 220);
	
});
