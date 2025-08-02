
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		url : contextRoot + '/wrks/sstm/usr/approve/list.json',
		datatype : "json",
		postData : {
			userId : $("#userId").val(),
			userNmKo : $("#userNmKo").val(),
			useTyCd : $("#useTyCd").val(),
			dstrtCd : $("#dstrtCd").val(),
			insttCd : $("#insttCd").val(),
			userApproveTy: $("#userApproveTy").val(),
			grpId : loginUserGrpId
		},
		colNames : ['<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, '아이디'   , '비밀번호',  '사용자명',   '영문이름',  '이메일',	 '사무실전화'
					, '기관코드' , '기관명',   '부서명',		'직급명',   '담당업무',	'비고',	'휴대전화'
					, '그룹명'   , '그룹아이디', '권한레벨명',  '지구',	'사용유형코드', '사용유형'
					, '승인구분' , '승인일시',  'IP주소', 'ipTyCd',  'ipCd',  '등록자아이디', '등록자'
				   ],
		colModel : [
				{	name:'CHECK',                          width:60, align:'center', editable:true,
					edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
				},{	name:'userId'           , index:'USER_ID'             , width:250, align:'center'
				},{	name:'password'         , index:'PASSWORD'            , width:100, align:'center', hidden:true
				},{	name:'userNmKo'         , index:'USER_NM_KO'          , width:250, align:'center'
				},{	name:'userNmEn'         , index:'USER_NM_EN'          , width:300, align:'center', hidden:true
				},{	name:'email'            , index:'EMAIL'               , width:200, align:'center', hidden:true
				},{	name:'offcTelNo'        , index:'OFFC_TEL_NO'         , width:200, align:'center', hidden:true
				},{	name:'insttCd'          , index:'INSTT_CD'            , width:200, align:'center', hidden:true
				},{	name:'insttNm'          , index:'INSTT_NM'            , width:200, align:'center'
				},{	name:'deptNm'           , index:'DEPT_NM'             , width:200, align:'center'
				},{	name:'rankNm'           , index:'RANK_NM'             , width:200, align:'center', hidden:true
				},{	name:'rpsbWork'         , index:'RPSB_WORK'           , width:250, align:'center'
				},{	name:'remark'           , index:'REMARK'              , width:200, align:'center', hidden:true
				},{	name:'moblNo'           , index:'MOBL_NO'             , width:200, align:'center'
				},{	name:'grpNmKo'          , index:'GRP_NM_KO'           , width:200, align:'center'
				},{	name:'grpId'            , index:'GRP_ID'              , width:200, align:'center', hidden:true
				},{	name:'authLvlNm'        , index:'AUTH_LVL_NM'         , width:200, align:'center'
				},{	name:'dstrtNm'          , index:'DSTRT_NM'            , width:200, align:'center'
				},{	name:'useTyCd'          , index:'USE_TY_CD'           , width:180, align:'center', hidden:true
				},{	name:'useTyNm'          , index:'USE_TY_NM'           , width:170, align:'center'
				},{ name:'userApproveTyNm'  , index:'USER_APPROVE_TY_NM'  , width:200, align:'center'
				},{ name:'userApproveYmdHms', index:'USER_APPROVE_YMD_HMS', width:200, align:'center'
				},{	name:'ipAdres'          , index:'IP_ADRES'            , width:200, align:'center', hidden:true
				},{	name:'ipTyCd'           , index:'IP_TY_CD'            , width:200, align:'center', hidden:true
				},{	name:'ipCd'             , index:'IP_CD'               , width:200, align:'center', hidden:true
				},{	name:'rgsUserId'        , index:'RGS_USER_ID'         , width:170, align:'center', hidden:true
				},{	name:'rgsUserNm'        , index:'RGS_USER_NM'         , width:170, align:'center'
				}
			  ],
		//pager : '#pager',
		rowNum : $('#rowPerPageList').val(),
		sortname:'USER_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		shrinkToFit: true,
		scrollOffset: 0,
		autowidth: true,
		height: 390,
		loadonce:false,
		jsonReader: {
			id: "userId",
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

			/* 휴대전화 */
			var mo = list.moblNo;
			var moblNo = mo.split("-");

			/* 이메일 */
			var em = list.email;
			var email = em.split("@");

			/* 사무실번호 */
			var op = list.offcTelNo;
			var offcTelNo = op.split("-");

			$("#dUserId").html(list.userId);
			//$("#dPassword").html(list.password);
			$("#dUserNmKo").html(list.userNmKo);
			$("#dUserNmEn").html(list.userNmEn);
			$("#dMoblNo").html(list.moblNo);
			$("#dEmail").html(list.email);
			$("#dOffcTelNo").html(list.offcTelNo);
			$("#dUseTyCd").html(list.useTyNm);
			$("#dInsttNm").html(list.insttNm);
			$("#dDeptNm").html(list.deptNm);
			$("#dRankNm").html(list.rankNm);
			$("#dRpsbWork").html(list.rpsbWork);
			$("#dRemark").html(list.remark);
			//$("#dIpAdres").html(list.ipAdres);
			//$("#dIpTyCd").html(list.ipTyCd);
			//$("#dIpCd").html(list.ipCd);

			$("#iUserId").val(list.userId);
			//$("#iPassword").val(list.password);
			$("#iUserNmKo").val(list.userNmKo);
			$("#iUserNmEn").val(list.userNmEn);
			$.selectBarun("#iMoblNo1", moblNo[0]);
			$("#iMoblNo2").val(moblNo[1]);
			$("#iMoblNo3").val(moblNo[2]);
			$("#iEmail1").val(email[0]);
			$("#iEmail2").val(email[1]);
			$.selectBarun("#iOffcTelNo1", offcTelNo[0]);
			$("#iOffcTelNo2").val(offcTelNo[1]);
			$("#iOffcTelNo3").val(offcTelNo[2]);
			
			$.selectBarun("#iUseTyCd", list.useTyCd);
			$.selectBarun('#iInsttCd', list.insttCd);
			//$("#iInsttNm").val(list.insttNm);
			$("#iDeptNm").val(list.deptNm);
			$("#iRankNm").val(list.rankNm);
			$("#iRpsbWork").val(list.rpsbWork);
			$("#iRemark").val(list.remark);
			//$("#iIpAdres").val(list.ipAdres);
			//$("#iIpTyCd").val(list.ipTyCd);
			//$("#iIpCd").val(list.ipCd);
			//$.selectBarun("#iIpTyCd", list.ipTyCd);
			//$.selectBarun("#iIpCd", list.ipCd);

			//ipTyCdList(list.IP_CD);
			
			// 사용자그룹 상세보기
			  var list = jQuery("#grid").getRowData(rowid);
			$("#grid_user_grp_detail").jqGrid('setGridParam', {url:contextRoot + "/wrks/sstm/usr/approve/list_user_grp.json"});
			var myPostData = $("#grid_user_grp_detail").jqGrid('getGridParam', 'postData');
			myPostData.userId = list.userId;
			$("#grid_user_grp_detail").trigger("reloadGrid");

			// 사용자그룹 수정
			  //등록된 사용자그룹
			//var list = jQuery("#grid").getRowData(rowid);
			$("#grid_user_grp_update").jqGrid('setGridParam', {url:contextRoot + "/wrks/sstm/usr/approve/list_user_grp.json"});
			myPostData = $("#grid_user_grp_update").jqGrid('getGridParam', 'postData');
			myPostData.userId = list.userId;
			$("#grid_user_grp_update").trigger("reloadGrid");

			//등록된 항목을 제외한 사용자그룹 항목
			$("#grid_user_grp_list_update").jqGrid('setGridParam', {url:contextRoot + "/wrks/sstm/usr/approve/list_grp.json"});
			  myPostData = $("#grid_user_grp_list_update").jqGrid('getGridParam', 'postData');
			  myPostData.userId = list.userId;
			  myPostData.grpId = loginUserGrpId;
			$("#grid_user_grp_list_update").trigger("reloadGrid");

			let canModify = false;		// 수정가능여부
			if ( 'PVECENTER' == loginUserGrpId ) {	// 영상반출 관제센터 그룹 일 때
				canModify = true;
			} else if ( list.grpId == loginUserGrpId ) {	// 같은 그룹일 때
				canModify = true;
			}
			//alert(canModify);
			if ( canModify ) {
				//$("#btn-init-password").removeClass("hide");
				$("#btn-modify-user").removeClass("hide");
				$("#btn-remove-user").removeClass("hide");
				if ( list.useTyCd == 'W') {		// 승인대기
					$("#btn-approve-user").removeClass("hide");
				} else {
					$("#btn-approve-user").addClass("hide");
				}
			
			} else {
				$("#btn-init-password").addClass("hide");
				$("#btn-modify-user").addClass("hide");
				$("#btn-remove-user").addClass("hide");
				$("#btn-approve-user").addClass("hide");
			}

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

	/* 사용자그룹 */
	//등록된 사용자 그룹(상세)
	$.jqGrid('#grid_user_grp_detail', {
		//url : contextRoot + '/wrks/sstm/usr/approve/list_user_grp.json',
		datatype : "json",
		postData : {
			userId : $.getCurrentRowValue("#grid", "userId")
		},
		colNames : [	 //'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_user_grp_detail\', this, event);">',
						'No',		'지구코드',	'지구',		'그룹아이디',		'그룹명',		'권한레벨',	'사용유형'
				   ],
		colModel : [//{name:'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{   name:'rk'	    , index:'RK'         , width:50 , align:'center', sortable: false
					},{ name:'dstrtCd'  , index:'DSTRT_CD'   , width:80 , align:'center', hidden:true
					},{ name:'dstrtNm'  , index:'DSTRT_NM'   , width:100, align:'center'
					},{ name:'grpId'	, index:'GRP_ID'     , width:200, align:'center'
					},{ name:'grpNmKo'  , index:'GRP_NM_KO'  , width:240, align:'center'
					},{ name:'authLvlNm', index:'AUTH_LVL_NM', width:240, align:'center'
					},{ name:'useTyNm'  , index:'USE_TY_NM'  , width:90 , align:'center'
					}
				  ],
		pager : '#pager',
		rowNum : 1000,
		sortname:'GRP_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
		},
		beforeProcessing: function(data, status, xhr){
		}
	  });

	//등록된 사용자 그룹(수정)
	$.jqGrid('#grid_user_grp_update', {
		//url : contextRoot + '/wrks/sstm/usr/approve/list_user_grp.json',
		datatype : "json",
		postData : {
			userId : $.getCurrentRowValue("#grid", "userId")
		},
		colNames : [	//'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_user_grp_update\', this, event);">',
						'',			'No',		'지구코드',		'지구',			'그룹아이디',		'그룹명',		'권한레벨',		'사용유형',		'USER_ID'
				   ],
		colModel : [//{name:'CHECK', width:20, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{name:'radio', index:'RADIO', width:20, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false,
						formatter: function (cellvalue, options, rowObject) {
							let checked = rowObject.checkedYn == 'Y' ? 'checked' : '';
							return "<input type='radio' name='radio' value='" + options.rowId + "' ${checked} >";
						}
					},{ name:'rk'           , index:'RK'             , width:30 , align:'center', sortable: false
					},{ name:'dstrtCd'      , index:'DSTRT_CD'       , width:100, align:'center', hidden:true
					},{ name:'dstrtNm'      , index:'DSTRT_NM'       , width:100, align:'center'
					},{ name:'grpId'		, index:'GRP_ID'         , width:200, align:'center'
					},{ name:'grpNmKo'	    , index:'GRP_NM_KO'      , width:240, align:'center'
					},{ name:'authLvlSelect', index:'AUTH_LVL_SELECT', width:240, align:'center', editable: true
					  , edittype:"select", editoptions:{value:""}, formatter: $.GridSelectBox2
					},{ name:'useTyNm'      , index:'USE_TY_NM'      , width:80 , align:'center'
					},{ name:'userId'       , index:'USER_ID'        , width:100, align:'center', hidden:true
					}
				  ],
		pager : '#pager',
		rowNum : 1000,
		sortname:'GRP_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
		},
		beforeProcessing: function(data, status, xhr){
		},
		loadComplete : function (data) {
			$('#grid_user_grp_update SELECT').selectBox("destroy");
			$('#grid_user_grp_update SELECT').selectBox();
		}
	  });

	//그룹(수정_등록된 항목을 제외한 리스트)
	$.jqGrid('#grid_user_grp_list_update', {
		//url : contextRoot + '/wrks/sstm/usr/approve/list_grp.json',
		datatype : "json",
		postData : {
			userId : ""
		},
		colNames : [	//'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_user_grp_list_update\', this, event);">',
						'',			'No',		'지구코드',	'지구',			'그룹아이디',		'그룹명',		'권한레벨',	'사용유형'
				   ],
		colModel : [/*
					{name:'CHECK', width:50 , align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{name:'RK', width:50 ,  align:'center', sortable: false},
					{name:'GRP_ID', width:100, align:'center'},
					{name:'GRP_NM_KO', width:150, align:'center'},
					  {name:'DSTRT_NM', width:150, align:'center'},
					  {name:'USE_TY_NM', width:100, align:'center'},
					  {name:'AUTH_LVL_SELECT', width:100, align:'center', editable: true, edittype:"select", editoptions:{value:""}, formatter: $.GridSelectBox2}
					   */
					//{name:'CHECK', width:30, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{name:'radio', index:'RADIO', width:20, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false,
						formatter: function (cellvalue, options, rowObject) {
							let checked = rowObject.checkedYn == 'Y' ? 'checked' : '';
							return "<input type='radio' name='radio' value='" + options.rowId + "' ${checked} >";
						}
					},
					{name:'rk'		   , index:'RK',			  width:50 ,  align:'center', sortable: false},
					  {name:'dstrtCd'	  , index:'DSTRT_CD',		width:100, align:'center', hidden:true},
					  {name:'dstrtNm'	  , index:'DSTRT_NM',		width:100, align:'center'},
					{name:'grpId'		, index:'GRP_ID',		  width:200, align:'center'},
					{name:'grpNmKo'	  , index:'GRP_NM_KO',	   width:220, align:'center'},
					  {name:'authLvlSelect', index:'AUTH_LVL_SELECT', width:220, align:'center', editable: true, edittype:"select", editoptions:{value:""}, formatter: $.GridSelectBox2},
					  {name:'useTyNm'	  , index:'USE_TY_NM',	   width:80,  align:'center'}
					  ],
		pager : '#pager',
		rowNum : 1000,
		height: $("#grid_user_grp_list_update").parent().height()-40,
		sortname:'GRP_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
		},
		beforeProcessing: function(data, status, xhr){
		},
		loadComplete : function (data) {
			$('#grid_user_grp_list_update SELECT').selectBox("destroy");
			$('#grid_user_grp_list_update SELECT').selectBox();
		}
	  });

	//그룹 전체(입력)
	$.jqGrid('#grid_user_grp_list_insert', {
		url : contextRoot + '/wrks/sstm/usr/approve/list_grp.json',
		datatype : "json",
		postData : {
			userId : "",
			grpId : loginUserGrpId
		},
		colNames : [	//'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid_user_grp_list_insert\', this, event);">',
						 '',				'No',		'지구코드',		'지구',			'그룹아이디',			'그룹명',		'권한레벨',		'사용유형'
				   ],
		colModel : [//{name:'CHECK', width:20, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{name:'radio', index:'RADIO', width:20, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false,
						formatter: function (cellvalue, options, rowObject) {
							let checked = rowObject.checkedYn == 'Y' ? 'checked' : '';
							return "<input type='radio' name='radio' value='" + options.rowId + "' ${checked} >";
						}
					},
					{name:'rk'		   , index:'RK',			  width:30,  align:'center', sortable: false},
					  {name:'dstrtCd'	  , index:'DSTRT_CD',		width:100, align:'center', hidden:true},
					  {name:'dstrtNm'	  , index:'DSTRT_NM',		width:100, align:'center'},
					{name:'grpId'		, index:'GRP_ID',		  width:200, align:'center'},
					{name:'grpNmKo'	  , index:'GRP_NM_KO',	   width:240, align:'center'},
					{name:'authLvlSelect', index:'AUTH_LVL_SELECT', width:240, align:'center', editable: true, edittype:"select", editoptions:{value:""}, formatter: $.GridSelectBox2},
					  {name:'useTyNm'	  , index:'USE_TY_NM',	   width:80,  align:'center'}
					  ],
		pager : '#pager',
		rowNum : 1000,
		sortname:'GRP_ID',
		sortorder: 'ASC',
		viewrecords:true,
		multiselect: false,
		loadonce:false,
		jsonReader: {
		},
		onCellSelect : function(rowid, iCol, cellcontent, e){
		},
		beforeProcessing: function(data, status, xhr){
		}
	  });

	// 검색
	$(".btnS").bind("click",function(){
		$("#grid").setGridParam({rowNum : $('#rowPerPageList').val()});
		var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
		myPostData.userId = $("#userId").val();
		myPostData.userNmKo = $("#userNmKo").val();
		myPostData.useTyCd = $("#useTyCd").val();
		myPostData.dstrtCd = $("#dstrtCd").val();
		myPostData.insttCd = $("#insttCd").val();
		myPostData.userApproveTy = $("#userApproveTy").val();
		myPostData.grpId   = loginUserGrpId;
		$("#grid").trigger("reloadGrid");
	});

	$('.btnNotApprove').bind("click", notapproveAction);
	
	$('.btnApprove').bind("click", approveAction);
	
	/* 사용자그룹 버튼 이벤트 */
	//그룹추가
	$(".btnAg").bind("click",function(){
		 //$(".layerDetail").hide();
		//$(".layerRegister").hide();

		var rows = $("#grid_user_grp_update").jqGrid('getGridParam', 'records');
		if (rows > 0) {
			alert("이미 추가된 그룹이 있습니다. 그룹변경시 삭제후 추가 하십시요.");
			return false;
		}

		$(".layerRegisterGrp").show();

		var layerH = $(".layerRegisterGrp").height();
		//$(".layerRegister").css({"top": (layerH/2)+"px"});
		$(".layerRegisterGrp").css({'margin-top':''});
		$(".layerRegisterGrp").css({"top": (($(document).height() - layerH)/2)+"px"});


		$("#maskPrgm").remove();
		$("body").append("<div class='maskPop' id='maskPrgm'></div>");

		$('.layerRegisterGrp SELECT').selectBox("destroy");
		$('.layerRegisterGrp SELECT').selectBox();
	});

	//그룹삭제
	$(".btnDg").bind("click",function(){
		deleteMultiActionUserGrp();
	});
	
	//그룹저장
	$(".btnSvGrp").bind("click",function(){
		var params = "";
		params += "&userId=" + encodeURIComponent($("#iUserId").val());

		//var s =  $.getSelRow("#grid_user_grp_list_update");
		var s =  $.getSelRowRadio("#grid_user_grp_list_update");
		if(s.length == 0){		alert("선택된 그룹이 없습니다.");			return false;		}
		// 하나의 그룹만 추가 가능 20190124
		if(s.length > 1){		alert("하나의 그룹만 선택 할 수 있습니다.");	return false;		}

		//사용자그룹 추가시 지구명 중복 입력 막음  2018.08.30추가
		var arr = [];
		for(var i = 0; i < s.length; i++) {
			var list = jQuery("#grid_user_grp_list_update").getRowData(s[i]);
			arr[i] = encodeURIComponent(list.dstrtCd);
		}

		var tempArr = [];
		for (var i = 0; i < arr.length; i++) {
			if (tempArr.length == 0) {
				tempArr.push(arr[i]);
			} else {
				var duplicatesFlag = true;
				for (var j = 0; j < tempArr.length; j++) {
					if (tempArr[j] == arr[i]) {
						duplicatesFlag = false;
						alert("선택한 그룹들에 지구명이 중복되었습니다. 확인바랍니다.");
						return false;
					}
				}
				if (duplicatesFlag) {
					tempArr.push(arr[i]);
				}
			}
		}

		//사용자그룹 기입력된 지구명 중복 입력 막음
		//사용자그룹수정 grid 지구명들을 배열에 저장
		var grpDstrtArr = [];
		var rows = $("#grid_user_grp_update").jqGrid('getGridParam', 'records');
		for(var i=0; i<rows; i++){
			  var list = jQuery("#grid_user_grp_update").getRowData(i + 1);
			  grpDstrtArr[i] = encodeURIComponent(list.dstrtCd);
			  //alert("grpDstrtArr["+i+"]="+encodeURIComponent(list.DSTRT_CD));
		}

		for (var i = 0; i < grpDstrtArr.length; i++) {

			for (var j = 0; j < arr.length; j++) {
				//alert("grpDstrtArr["+i+"]="+grpDstrtArr[i]+"  |  arr["+j+"]="+arr[j]);
				if (grpDstrtArr[i] == arr[j]) {
					alert("선택한 그룹 중에 이미 지구명이 중복되었습니다. 확인바랍니다.");
					return false;
				}

			}
		}
	  //여기까지 사용자그룹 추가시 지구명 중복 입력 막음  2018.08.30추가

		if(confirm("선택된 그룹을 추가하시겠습니까?") == false) return false;

		var url = contextRoot + "/wrks/sstm/usr/approve/insert_grp.json";
		var paramsItem = ""; //""&grpId=null";

		for(var i = 0; i < s.length; i++) {
			var list = jQuery("#grid_user_grp_list_update").getRowData(s[i]);
			paramsItem += "&grpId=" + encodeURIComponent(list.grpId);
			paramsItem += "&dstrtCd=" + encodeURIComponent(list.dstrtCd);
			//paramsItem += "&authLvl=" + encodeURIComponent($.getCustomObjectValue("#grid_user_grp_list_update", "select", "AUTH_LVL_SELECT", i + 1));
			paramsItem += "&authLvl=" + encodeURIComponent($.getCustomObjectValue("#grid_user_grp_list_update", "select", "authLvlSelect", list.rk));
		}

		params += paramsItem;

		$.ajaxEx($('#grid_user_grp_update'), {
			type : "POST",
			url : url,
			dataType : "json",
			data: params,
			success:function(data){
				$("#grid_user_grp_update").trigger("reloadGrid");
				$("#grid_user_grp_list_update").trigger("reloadGrid");
				alert(data.msg);
			},
			error:function(e){
				alert(data.msg);
			}
		});

		$(".layerRegister").show();
		$(".layerRegisterGrp").hide();
		$("#maskPrgm").remove();

	});

	//그룹취소
	$(".btnCGrp").bind("click",function(){
		$(".layerRegister").show();
		$(".layerRegisterGrp").hide();
		$("#maskPrgm").remove();
	});

	/* 비밀번호 확인 */
	$("#iPasswordCk").change(function(){
		if($("#iPassword").val() != $("#iPasswordCk").val()){
			$("#ckPs").show();
		}else{
			$("#ckPs").hide();
		}
	});
		
   	$(".tableType1.userList").css('height', window.innerHeight - 350);
	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 300);
	
});

function resetAction() {
	$("#iUserId").removeAttr("readonly");
	$("#ckPs").hide();
	$.resetInputObject(".layerRegister .tableTypeFree");

	/* 사용자그룹 등록*/
	  var myPostData = $("#grid_user_grp_list_update").jqGrid('getGridParam', 'postData');
	myPostData.userId = "";
	$("#grid_user_grp_list_update").trigger("reloadGrid");

	  var myPostData = $("#grid_user_grp_update").jqGrid('getGridParam', 'postData');
	myPostData.userId = "";
	$("#grid_user_grp_update").trigger("reloadGrid");


	//ipTyCdList();

	// 추가
	$("#iPassword").attr("required", "required");
	$("#iPasswordCk").attr("required", "required");

	$("dStar").remove();
	$(".star").before("<dStar style='color: red'>*</dStar>");

	$(".layerRegister .tableType1.insert").show();
	$(".layerRegister .tableType1.update").hide();
	$(".btn.btnAg").hide();
	$(".btn.btnDg").hide();
	$(".btn.btnDa").hide();
	
	$("#btn-check-user-id").removeClass('hide');
}

function preModifyAction() {
	$("#iUserId").attr("readonly", "readonly");
	$("#iPassword").val("");
	$("#iPasswordCk").val("");
	$("#adminPw").val("");

	$("#ckPs").hide();

	$("dStar").remove();
	$("#iPassword").removeAttr("required");
	$("#iPasswordCk").removeAttr("required");

	$(".layerRegister .tableType1.insert").hide();
	$(".layerRegister .tableType1.update").show();
	$(".btn.btnAg").show();
	$(".btn.btnDg").show();
	$(".btn.btnDa").show();
	
	$("#btn-check-user-id").addClass('hide');
}

function validate() {
	return $.validate(".layerRegister .tableTypeFree");
}

let userIdDuplFlag = '';
function checkUserId() {
	if ( '' == $("#iUserId").val().trim() ) {	alert('아이디를 입력하세요.');		$('#iUserId').focus();		return false;	}
	
	var params = "userId=" + encodeURIComponent($("#iUserId").val());

	$.ajaxEx($('#grid'), {
		type : "POST",
		url : contextRoot + "/wrks/sstm/usr/approve/checkUserId.json",
		dataType : "json",
		data: params,
		success:function(data){
			//alert(data.msg);
			if( 0 == data.msg ) {
				alert('사용가능한 아이디입니다.');		userIdDuplFlag = 'POSS';
			} else {
				alert('이미 사용중인 아이디입니다.');	userIdDuplFlag = 'IMPOSS';
			}
		},
		error:function(e){
			alert(data.msg);
		}
	});
}

/* 한글입력 방지 */
function removeKoreanChar(obj)
{
	//좌우 방향키, 백스페이스, 딜리트, 탭키에 대한 예외
	if(event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 39 || event.keyCode == 46 ) return;
	//obj.value = obj.value.replace(/[\a-zㄱ-ㅎㅏ-ㅣ가-힣]/g, '');
	obj.value = obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');
	
	userIdDuplFlag = '';	// 아이디 중복체크 초기화
}

function insertAction()
{	//alert(loginUserGrpId+" , "+loginUserAuthLvl+" , "+GlobalsRqstAuthLvl );

	if ( '1' != loginUserAuthLvl ) {	// 현재 사용자가 센터관리자가 아닐 때
		$("#iUseTyCd").val("W");			// 대기
		$.selectBarun("#iUseTyCd", "W");	// 대기
	}
	
	var iUserId = $("#iUserId").val().trim();
	if ('' == iUserId )			{	alert('아이디를 입력하세요.');		$('#iUserId').focus();		return false;	}
	if ('' == userIdDuplFlag )	{	alert('아이디 중복검사를 하세요.');	$('#iUserId').focus();		return false;	}
	if ('IMPOSS' == userIdDuplFlag )	{	alert('이미 사용중인 아이디입니다.');	$('#iUserId').focus();		return false;	}
	//if( iUserId.length < 5 ) {	alert("아이디는 5자 이상 입력하세요.");		return false;	}
	var useNotId = "\^test|^demo";	// test, demo 로 시작하는 아이디 사용할 수 없음 - 2017.03.02 - seungJun
	if ((new RegExp(useNotId, "i")).test(iUserId)) {		alert('test, demo로 시작되는 아이디는 등록할 수 없습니다.');	return false;	}

	if ('' == $("#iUserNmKo").val().trim() )			 {	alert('사용자명을 입력하세요.');		$('#iUserNmKo').focus();	return false;	}
	if ('' == $("#iPassword").val().trim() )			 {	alert('비밀번호를 입력하세요.');		$('#iPassword').focus();	return false;	}
	if($("#iPassword").val() != $("#iPasswordCk").val()) {	alert("비밀번호가 일치하지 않습니다.");	$("#ckPs").show();		return false;
	} else {		$("#ckPs").hide();	}	// 비밀번호 확인

	if ($("#iMoblNo1").val().trim()=="" || $("#iMoblNo2").val().trim()=="" || $("#iMoblNo3").val().trim()=="")	{	alert("휴대전화를 입력하세요.");		return false;	}
	//if ($("#iOffcTelNo1").val().trim()=="" || $("#iOffcTelNo2").val().trim()=="" || $("#iOffcTelNo3").val().trim()=="")	{	alert("사무실전화를 입력하세요.");		return false;	}

	if ($("#iInsttCd").val().trim()=="" && $("#iInsttNmInput").val().trim()=="")	{	alert("기관명을 선택하거나 입력하세요.");	$('#iInsttCd').focus();	return false;	}
	//if ($("#iDeptNm").val().trim()=="")											{	alert("부서명을 입력하세요.");			$('#iDeptNm').focus();	return false;	}
	//if ($("#iRankNm").val().trim()=="")											{	alert("직급명을 입력하세요.");			$('#iRankNm').focus();	return false;	}
	if (!$('#adminPw').val().trim())		{	alert('관리자 비밀번호를 입력하세요.');	$('#adminPw').focus();	return false;	}
	
	var moblNo = $("#iMoblNo1").val() +"-"+ $("#iMoblNo2").val() +"-"+ $("#iMoblNo3").val();
	var offcTelNo = $("#iOffcTelNo1").val() +"-"+ $("#iOffcTelNo2").val() +"-"+ $("#iOffcTelNo3").val();
	var email = $("#iEmail1").val() +"@"+ $("#iEmail2").val();
		
	var params = "userId=" + encodeURIComponent(iUserId);
		params += "&password=" + encodeURIComponent($("#iPassword").val());
		params += "&userNmKo=" + encodeURIComponent($("#iUserNmKo").val());
		params += "&userNmEn=" + encodeURIComponent($("#iUserNmEn").val());
		params += "&moblNo=" + encodeURIComponent(moblNo);
		params += "&offcTelNo=" + encodeURIComponent(offcTelNo);
		params += "&email=" + encodeURIComponent(email);
		params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
		//params += "&insttNm=" + encodeURIComponent($("#iInsttNm").val());
		params += "&insttCd=" + encodeURIComponent($("#iInsttCd").val());
		params += "&insttNmInput=" + encodeURIComponent($("#iInsttNmInput").val());
		params += "&deptNm=" + encodeURIComponent($("#iDeptNm").val());
		params += "&rankNm=" + encodeURIComponent($("#iRankNm").val());
		params += "&rpsbWork=" + encodeURIComponent($("#iRpsbWork").val());
		params += "&remark=" + encodeURIComponent($("#iRemark").val());
		//params += "&ipAdres=" + encodeURIComponent($("#iIpAdres").val());
		//params += "&ipTyCd=" + encodeURIComponent($("#iIpTyCd").val());
		//params += "&ipCd=" + encodeURIComponent($("#iIpCd").val());
		params += "&adminPw="+ encodeURIComponent($("#adminPw").val());

	// 그룹
	//var s =  $.getSelRow("#grid_user_grp_list_insert");
	var s =  $.getSelRowRadio("#grid_user_grp_list_insert");
	if(s.length == 0){
		//if(confirm("그룹 추가없이 진행하시겠습니까?") == false) return false;
		alert("그룹을 선택하세요.");	return false;
	}

	let paraAuthLvl = "";
	var paramsItem = ""; // "&grpId=null";
	for(var i = 0; i < s.length; i++)
	{	var list = jQuery("#grid_user_grp_list_insert").getRowData(s[i]);
		paramsItem += "&dstrtCd=" + encodeURIComponent(list.dstrtCd);
		paramsItem += "&grpId=" + encodeURIComponent(list.grpId);
		
		//paramsItem += "&authLvl=" + $.getCustomObjectValue("#grid_user_grp_list_insert", "select", "AUTH_LVL_SELECT", i + 1);
		paraAuthLvl = encodeURIComponent($.getCustomObjectValue("#grid_user_grp_list_insert", "select", "authLvlSelect", list.rk))
		paramsItem += "&authLvl=" + paraAuthLvl;
	}
	params += paramsItem;
	//alert(params);
	
	if ( '1' != loginUserAuthLvl ) {	// 현재 사용자가 센터관리자가 아닐 때
		let flag = false;
		let rqstLvl = GlobalsRqstAuthLvl.split(",");
		for ( var i=0 ; i<rqstLvl.length ; i++ ) {
			if ( rqstLvl[i] == paraAuthLvl ) {	// 신청 권한의 새로운 사용자를 등록할 때
				flag = true;
			}		
		}
		if ( !flag ) {		// 신청 권한이외의 새로운 사용자를 등록하려 할 때
			alert('권한레벨을 "신청"으로만 선택할 수 있습니다.');		return false;
		}
	}

	if ( confirm('사용자를 등록하시겠습니까?') ) {
		$.ajaxEx($('#grid'), {
			type : "POST"
			, url : contextRoot + "/wrks/sstm/usr/approve/insert.json"
			, dataType : "json"
			, data: params
			, success:function(data){
				if (data.session == 1) {
					$("#grid").trigger("reloadGrid");
					alert(data.msg);
					$.hideInsertForm();
				} else {
					alert(data.msg);
				}
			}
			, error:function(e) {
				alert(data.msg);
			}
		});
	}
	
}

function updateAction() {

	if ( '1' != loginUserAuthLvl ) {	// 현재 사용자가 센터관리자가 아닐 때
		$("#iUseTyCd").val("W");			// 대기
		$.selectBarun("#iUseTyCd", "W");	// 대기
	}

	if ('' == $("#iUserNmKo").val().trim() )			 {	alert('사용자명을 입력하세요.');		$('#iUserNmKo').focus();	return false;	}
	if($("#iPassword").val() != $("#iPasswordCk").val()) {	alert("비밀번호가 일치하지않습니다.");	$("#ckPs").show();		return false;
	}else{		$("#ckPs").hide();	}

	if ($("#iMoblNo1").val().trim()=="" || $("#iMoblNo2").val().trim()=="" || $("#iMoblNo3").val().trim()=="")			{	alert("휴대전화를 입력하세요.");		return false;	}
	//if ($("#iOffcTelNo1").val().trim()=="" || $("#iOffcTelNo2").val().trim()=="" || $("#iOffcTelNo3").val().trim()=="")	{	alert("사무실전화를 입력하세요.");		return false;	}
	if ($("#iInsttCd").val().trim()=="" && $("#iInsttNmInput").val().trim()=="")	{	alert("기관명을 선택하거나 입력하세요.");	$("#iInsttCd").focus();	return false;	}
	//if ($("#iDeptNm").val().trim()=="")											{	alert("부서명을 입력하세요.");			$("#iDeptNm").focus();		return false;	}
	//if ($("#iRankNm").val().trim()=="")											{	alert("직급명을 입력하세요.");			$("#iRankNm").focus();		return false;	}
	if (!$('#adminPw').val().trim())		{	alert('관리자 비밀번호를 입력하세요.');	$('#adminPw').focus();	return false;	}

	var moblNo = $("#iMoblNo1").val() +"-"+ $("#iMoblNo2").val() +"-"+ $("#iMoblNo3").val();
	var offcTelNo = $("#iOffcTelNo1").val() +"-"+ $("#iOffcTelNo2").val() +"-"+ $("#iOffcTelNo3").val();
	var email = $("#iEmail1").val() +"@"+ $("#iEmail2").val();

	var params = "userId=" + encodeURIComponent($("#iUserId").val());
		params += "&password=" + encodeURIComponent($("#iPassword").val());
		params += "&userNmKo=" + encodeURIComponent($("#iUserNmKo").val());
		params += "&userNmEn=" + encodeURIComponent($("#iUserNmEn").val());
		params += "&moblNo=" + encodeURIComponent(moblNo);
		params += "&offcTelNo=" + encodeURIComponent(offcTelNo);
		params += "&email=" + encodeURIComponent(email);
		params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
		//params += "&insttNm=" + encodeURIComponent($("#iInsttNm").val());
		params += "&insttCd=" + encodeURIComponent($("#iInsttCd").val());
		params += "&insttNmInput=" + encodeURIComponent($("#iInsttNmInput").val());
		params += "&deptNm=" + encodeURIComponent($("#iDeptNm").val());
		params += "&rankNm=" + encodeURIComponent($("#iRankNm").val());
		params += "&rpsbWork=" + encodeURIComponent($("#iRpsbWork").val());
		params += "&remark=" + encodeURIComponent($("#iRemark").val());
		//params += "&ipAdres=" + encodeURIComponent($("#iIpAdres").val());
		//params += "&ipTyCd=" + encodeURIComponent($("#iIpTyCd").val());
		//params += "&ipCd=" + encodeURIComponent($("#iIpCd").val());
		params += "&adminPw="+ encodeURIComponent($("#adminPw").val());

	/* 그룹 */
	let paraAuthLvl = "";
	 var paramsItem = ""; // "&grpId=null";
	 var rows = $("#grid_user_grp_update").jqGrid('getGridParam', 'records');

	for(var i=0; i<rows; i++){
		  var list = jQuery("#grid_user_grp_update").getRowData(i + 1);

		paramsItem += "&grpId=" + encodeURIComponent(list.grpId);
		paramsItem += "&dstrtCd=" + encodeURIComponent(list.dstrtCd);
		//paramsItem += "&authLvl=" + $.getCustomObjectValue("#grid_user_grp_update", "select", "AUTH_LVL_SELECT", i + 1);
		paraAuthLvl = encodeURIComponent($.getCustomObjectValue("#grid_user_grp_update", "select", "authLvlSelect", i + 1))
		paramsItem += "&authLvl=" + paraAuthLvl;

	}
	params += paramsItem;
	
	if ( '1' != loginUserAuthLvl ) {	// 현재 사용자가 센터관리자가 아닐 때
		let flag = false;
		let rqstLvl = GlobalsRqstAuthLvl.split(",");
		for ( var i=0 ; i<rqstLvl.length ; i++ ) {
			if ( rqstLvl[i] == paraAuthLvl ) {	// 신청 권한의 새로운 사용자를 등록할 때
				flag = true;
			}		
		}
		if ( !flag ) {		// 신청 권한이외의 새로운 사용자를 등록하려 할 때
			alert('권한레벨을 "신청"으로만 선택할 수 있습니다.');		return false;
		}
	}

	if ( confirm('사용자를 수정하시겠습니까?') ) {
		$.ajaxEx($('#grid'), {
			type : "POST",
			url : contextRoot + "/wrks/sstm/usr/approve/update.json",
			dataType : "json",
			data: params,
			success:function(data){
				$("#grid").setGridParam({page :$("#cur-page").val()});
				$("#grid").trigger("reloadGrid");
				//alert("저장하였습니다.");
				alert(data.msg);
			},
			error:function(e){
				//alert(e.responseText);
				alert(data.msg);
			}
		});
	}
}

function deleteAction() {
	var params = "userId=" + encodeURIComponent($("#dUserId").text());

	$.ajaxEx($('#grid'), {
		type : "POST",
		url : contextRoot + "/wrks/sstm/usr/approve/delete.json",
		dataType : "json",
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

function deleteMultiAction() {
	//var s =  $("#grid").jqGrid('getGridParam', 'selarrrow');
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	
	var params = "";
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);
		params += "&userId=" + encodeURIComponent(list.USER_ID);
	}

	$.ajaxEx($('#grid'), {
		type : "POST",
		url : contextRoot + "/wrks/sstm/usr/approve/deleteMulti.json",
		dataType : "json",
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

//그룹삭제
function deleteMultiActionUserGrp() {
	//var s =  $.getSelRow("#grid_user_grp_update");
	var s =  $.getSelRowRadio("#grid_user_grp_update");
	if(s.length == 0){
		alert("선택된 그룹이 없습니다.");		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;

	var params = "";
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid_user_grp_update").getRowData(s[i]);

		params += "&userId="+ encodeURIComponent(list.userId);
		params += "&grpId="+ encodeURIComponent(list.grpId);
		params += "&dstrtCd="+ encodeURIComponent(list.dstrtCd);
	}

	$.ajaxEx($('#grid_user_grp_update'), {
		type : "POST",
		url : contextRoot + "/wrks/sstm/usr/approve/deleteMulti_user_grp.json",
		dataType : "json",
		data: params,
		success:function(data){
			$("#grid_user_grp_update").trigger("reloadGrid");
			$("#grid_user_grp_list_update").trigger("reloadGrid");
			alert(data.msg);
		},
		error:function(e){
			alert(data.msg);
		}
	});

	return true;
}

function approveAction(obj) {
	let s = $.getSelRow('#grid');
	if (s.length == 0) {
		oCommon.modalAlert('modal-notice', '알림', '사용자를 선택해주세요.');
		return false;
	}

	oCommon.modalConfirm('modal-confirm', '확인', '선택된 사용자를 승인처리 하시겠습니까?', () => {
		let url = contextRoot + '/wrks/sstm/usr/approve/approve.json';
		let params = '';
	
		for (let i = 0; i < s.length; i++) {
			let userId = jQuery('#grid').getRowData(s[i]).userId;
			params += 'userId[]=' + encodeURIComponent(userId) + '&';
		}
		params = params.slice(0, -1);
	
		$.ajaxEx($('#grid'), {
			type: 'POST',
			url: url,
			dataType: 'json',
			data: params,
			success: function (data) {
				$('#grid').setGridParam({
					page: $('#cur-page').val()
				}).trigger('reloadGrid');
				oCommon.modalAlert('modal-notice', '알림', data.msg);
			},
			error: function (e) {
				alert(data.msg);
			}
		});
	}, undefined
)}

function notapproveAction(obj) {
	let s = $.getSelRow('#grid');
	if (s.length == 0) {
		oCommon.modalAlert('modal-notice', '알림', '사용자를 선택해주세요.');
		return false;
	}

	oCommon.modalConfirm('modal-confirm', '확인', '선택된 사용자를 미승인처리 하시겠습니까?', () => {
		let url = contextRoot + '/wrks/sstm/usr/approve/notapprove.json';
		let params = '';
	
		for (let i = 0; i < s.length; i++) {
			let userId = jQuery('#grid').getRowData(s[i]).userId;
			params += 'userId[]=' + encodeURIComponent(userId) + '&';
		}
		params = params.slice(0, -1);

		$.ajaxEx($('#grid'), {
			type: 'POST',
			url: url,
			dataType: 'json',
			data: params,
			success: function (data) {
				$('#grid').setGridParam({
					page: $('#cur-page').val()
				}).trigger('reloadGrid');
				oCommon.modalAlert('modal-notice', '알림', data.msg);
			},
			error: function (e) {
				alert(data.msg);
			}
		});
	}, undefined
)}
/*
function approveUser() {
	if(confirm("사용을 승인하시겠습니까?") == false) return false;

	var url = contextRoot + "/wrks/sstm/usr/approve/approveUser.json";
	var params = "userId=" + encodeURIComponent($("#dUserId").text())+"&useTyCd=Y";
	$.ajaxEx($('#grid'), {
		type : "POST",
		url : url,
		dataType : "json",
		data: params,
		success:function(data){
			$("#grid").setGridParam({page :$("#cur-page").val()});
			$("#grid").trigger("reloadGrid");
			alert(data.msg);
			$.hideInsertForm();
		},
		error:function(e){
			alert(data.msg);
		}
	});
}

function initPassword() {
	if(confirm("비밀번호를 초기화하시겠습니까?") == false) return false;

	var params = "userId=" + encodeURIComponent($("#dUserId").text());
	$.ajaxEx($('#grid'), {
		type : "POST",
		url : contextRoot + "/wrks/sstm/usr/approve/initPassword.json",
		dataType : "json",
		data: params,
		success:function(data){
			alert(data.msg);
		},
		error:function(e){
			alert(data.msg);
		}
	});
}
*/
