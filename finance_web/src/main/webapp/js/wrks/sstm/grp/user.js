let oUser = new user();

$(function () {
	// 그룹목록
	$.jqGrid('#grid_group', {
		url:contextRoot + '/wrks/sstm/grp/user/list_group.json',
		datatype: "json",
		autowidth: true,
		shrinkToFit: true,
		scrollOffset: 0,
		viewrecords: true,
		postData: {
			grpId: $("#sGrpId").val(),
			grpNm: $("#sGrpNm").val()
		},
		colNames: [	'',	  '그룹아이디',			'그룹명',			'그룹영문',			'권한레벨',			'권한레벨명',			'지구명'
		],
		colModel: [
			{name: 'CHECK', width: 50, align: 'center', editable: true, edittype: 'radio', editoptions: {value: "True:False"}, sortable: false
				, formatter: function (cellValue, option) {
					return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
				}
			},
			{name: 'grpId', index: 'GRP_ID', align: 'center'},
			{name: 'grpNmKo', index: 'GRP_NM_KO', align: 'center'},
			{name: 'grpNmEn', 'hidden': true},
			{name: 'authLvl', index: 'AUTH_LVL', width: 70, align: 'center', sortable: false},
			{name: 'authLvlNm', index: 'AUTH_LVL_NM', align: 'center', sortable: false},
			{name: 'dstrtNm', index: 'DSTRT_NM', align: 'center', sortable: false}
		],
		pager: '#pager',
		rowNum: 1000,
		height: $("#grid_group").parent().height() - 40,
		sortname: 'GRP_NM_KO',
		sortorder: 'ASC',
		viewrecords: true,
		multiselect: false,
		loadonce: false,
		jsonReader: {},
		onSelectRow: function (rowid, status, e) {
			$("#grid_user").setGridParam({url:contextRoot + '/wrks/sstm/grp/user/list_user.json'});
			$("#grid_group input[type=radio]").get(rowid - 1).checked = true;
			var list = jQuery("#grid_group").getRowData(rowid);

			var myPostData = $("#grid_user").jqGrid('getGridParam', 'postData');
			myPostData.grpId = list.grpId;
			myPostData.authLvl = list.authLvl;
			myPostData.userNm = '';
			myPostData.userId = '';
			$("#grid_user").trigger("reloadGrid");
		},
		beforeRequest: function () {
			var oParams = $('#grid_group').getGridParam();
			if (oParams.hasOwnProperty('url') && oParams.url == '') {
				return false;
			}
		},
		beforeProcessing: function (data, status, xhr) {
			$("#grid_user").clearGridData();
		},
		loadComplete: function (data) { /* 데이터 로딩이 끝난후 호출할 함수*/
			// 첫번째 열 선택
			let $rows = $('#grid_group tr.jqgrow[role=row]');
			if ($rows.length) $('#grid_group').jqGrid('setSelection', $rows.get(0).id);
			oCommon.jqGrid.gridComplete(this);
		}
	});

	// 사용자목록
	$.jqGrid('#grid_user', {
		datatype: "json",
		autowidth: true,
		shrinkToFit: true,
		scrollOffset: 0,
		postData: {
			//userNm: $('#sUserNmKo').val(),
			userId: $('#sUserId').val()
		},
		colNames: [	'아이디',	'사용자명',	'그룹명/권한레벨명',	'사용유형',	'휴대전화',	'부서명',	'담당업무',	'이메일',	'지구코드',	'지구명'
		],
		colModel: [
			{name: 'userId', index: 'USER_ID', align: 'center'},
			{name: 'userNmKo', index: 'USER_NM_KO', align: 'center'},
			{name: 'grpNmKo', align: 'center', sortable: false, formatter: (cellvalue, options, rowObject) => {
					let sGrpNmKo = rowObject.grpNmKo || '';
					let sAuthLvlNm = rowObject.authLvlNm || '';
					return sGrpNmKo + '/' + sAuthLvlNm;
				}
			},
			{name: 'useNm', index: 'USE_NM', align: 'center'},
			{name: 'moblNo', index: 'MOBL_NO', align: 'center', sortable: false},
			{name: 'deptNm', index: 'DEPT_NM', align: 'center', sortable: false},
			{name: 'rpsbWork', index: 'RPSB_WORK', align: 'center', sortable: false},
			{name: 'email', index: 'EMAIL', align: 'center', sortable: false},
			{name: 'dstrtCd', 'hidden': true},
			{name: 'dstrtNm', index: 'DSTRT_NM', align: 'center'},
		],
		pager: '#pager',
		rowNum: 1000,
		height: $("#grid_user").parent().height() - 40,
		sortname: 'USER_NM_KO',
		sortorder: 'DESC',
		rownumbers: true,
		rownumWidth: 50,
		viewrecords: true,
		multiselect: false,
		loadonce: false,
		jsonReader: {},
		onCellSelect: function (rowid, iCol, cellcontent, e) {
		},
		beforeRequest: function () {
			var oParams = $('#grid_user').getGridParam();

			if (oParams.hasOwnProperty('postData') && !oParams.postData.hasOwnProperty('grpId')) {
				return false;
			}
		},
		loadComplete: function (data) {
			oCommon.jqGrid.gridComplete(this);
			oCommon.jqGrid.checkNodata('user', data);
		}
	});

	// 그룹검색
	$(".btnS").bind("click", function () {
		var myPostData = $("#grid_group").jqGrid('getGridParam', 'postData');
		//myPostData.dstrtCd = $("#sDstrtCd").val();
		myPostData.grpId = $("#sGrpId").val();
		myPostData.grpNm = $("#sGrpNm").val();
		$("#grid_group").trigger("reloadGrid");
	});

	// 사용자검색
	$(".btnS2").bind("click", function () {
		var myPostData = $("#grid_user").jqGrid('getGridParam', 'postData');
		var oParams = $('#grid_user').getGridParam();
		//myPostData.userNm = $("#sUserNmKo").val();
		myPostData.userId = $("#sUserId").val();
		myPostData.authLvl = oParams.postData.authLvl;
		myPostData.grpId = oParams.postData.grpId;
		$("#grid_user").trigger("reloadGrid");
	});

	oUser.init();
});

function user() {
	this.init = function () {
		$('#btn-excel-download').off('click');
		$('#btn-excel-upload').off('click');

		$('#btn-excel-download').on('click', () => {
			excel.download();
		});

		$('#btn-excel-upload').on('click', () => {
			excel.upload();
		});
	}

	const excel = {
		upload: function () {
			let sFileExcelUpload = $('#file-excel-upload').val();
			if (sFileExcelUpload == '') {
				oCommon.modalAlert('modal-notice', '알림', '파일을 선택해주세요.');
			} else {
				oCommon.modalConfirm('modal-confirm', '확인', '업로드 하시겠습니까?', () => {
					let sRowId = $('#grid_group').getGridParam('selrow');
					let oRowData = $('#grid_group').getRowData(sRowId);

					$('#form-excel-upload input[name=grpId]').val(oRowData.grpId);
					$('#form-excel-upload input[name=grpNmKo]').val(oRowData.grpNmKo);
					$('#form-excel-upload input[name=authLvl]').val(oRowData.authLvl);
					$('#form-excel-upload input[name=authLvlNm]').val(oRowData.authLvlNm);

					let elForm = $('#form-excel-upload')[0];
					let oFormData = new FormData(elForm);

					$.ajax({
						url:contextRoot + '/wrks/sstm/grp/user/list_user.upload',
						data: oFormData,
						processData: false,
						contentType: false,
						type: 'POST',
						success: function (data) {
							if (data.session == 1) {
								oCommon.modalAlert('modal-notice', '알림', data.msg, () => {
									$('#grid_user').trigger('reloadGrid');
									$('#form-excel-download input[name=grpId]').val(oRowData.grpId);
									$('#form-excel-download input[name=grpNmKo]').val(oRowData.grpNmKo);
									$('#form-excel-download input[name=authLvl]').val(oRowData.authLvl);
									$('#form-excel-download input[name=authLvlNm]').val(oRowData.authLvlNm);
									$('#form-excel-download input[name=errorYn]').val("Y");
									$('#form-excel-download').attr('action', contextRoot + '/wrks/sstm/grp/user/list_user.excel');
									$('#form-excel-download').submit();
								});
							} else {
								oCommon.modalAlert('modal-notice', '알림', data.msg);
							}
						},
						error: function (xhr, status, error) {
							console.log(xhr, status, error);
						},
						complete: function (xhr, status) {
							$('#file-excel-upload').val('');
						}
					});
				}, undefined);
			}
		},
		download: function () {
			let sRowId = $('#grid_group').getGridParam('selrow');
			let oRowData = $('#grid_group').getRowData(sRowId);

			oCommon.modalConfirm('modal-confirm', '확인', '그룹레벨 ' + oRowData.grpNmKo + '(' + oRowData.authLvlNm + ') 사용자정보를 엑셀다운로드 하시겠습니까?', () => {
				$('#form-excel-download input[name=grpId]').val(oRowData.grpId);
				$('#form-excel-download input[name=grpNmKo]').val(oRowData.grpNmKo);
				$('#form-excel-download input[name=authLvl]').val(oRowData.authLvl);
				$('#form-excel-download input[name=authLvlNm]').val(oRowData.authLvlNm);
				$('#form-excel-download input[name=errorYn]').val("N");
				$('#form-excel-download').attr('action', contextRoot + '/wrks/sstm/grp/user/list_user.excel');
				$('#form-excel-download').submit();
			}, undefined);
		},
	}
}


