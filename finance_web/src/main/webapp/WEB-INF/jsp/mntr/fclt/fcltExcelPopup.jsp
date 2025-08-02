<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="form-horizontal">
	<div class="form-group form-group-sm">
		<label class="col-sm-3 control-label">유형</label>
		<div class="col-sm-9">
			<select id="searchFcltKndCd-popup" name="searchFcltKndCd-popup" class="form-control input-sm"></select>
		</div>
	</div>
	<div class="form-group form-group-sm">
		<label class="col-sm-3 control-label">용도</label>
		<div class="col-sm-9">
			<select id="searchFcltUsedTyCd-popup" name="searchFcltUsedTyCd-popup" class="form-control input-sm"></select>
		</div>
	</div>
	<div class="form-group form-group-sm">
		<label class="col-sm-3 control-label">사용유형 </label>
		<div class="col-sm-9">
			<div class="form-inline">
				<div class="checkbox">
					<label class="control-label">
						<input type="checkbox" name="chkFcltUseTy" value="Y" checked>
						사용(Y)
					</label>
				</div>
				<div class="checkbox">
					<label class="control-label">
						<input type="checkbox" name="chkFcltUseTy" value="N">
						미사용(N)
					</label>
				</div>
				<div class="checkbox">
					<label class="control-label">
						<input type="checkbox" name="chkFcltUseTy" value="D">
						삭제(D)
					</label>
				</div>
			</div>
		</div>
	</div>

	<div class="form-group form-group-sm">
		<label class="col-sm-3 control-label">구분 </label>
		<div class="col-sm-9">
			<div class="checkbox">
				<label class="control-label">
					<input type="checkbox" name="excelDownTy" value="N">
					미입력항목
				</label>
			</div>

		</div>
	</div>

	<div class="form-group form-group-sm">
		<label class="col-sm-3 control-label">비밀번호 </label>
		<div class="col-sm-9">
			<input type="password" id="loginPwd" class="form-control input-sm" placeholder="비밀번호">
		</div>
	</div>

	<div class="form-group form-group-sm">
		<label class="col-sm-3 control-label">항목선택 </label>
		<div class="col-sm-9">
			<button class="btn btn-default btn-sm" id="chkDefault" onclick="chkFcltDefaultBtn();">기본선택</button>
			<button class="btn btn-default btn-sm" id="chkReset" onclick="chkAllResetBtn();" disabled="disabled">선택해제</button>
		</div>
	</div>

	<!-- 항목리스트(선택/항목명) 그리드 -->
	<table id="grid-popup"></table>

	<div class="form-group form-group-sm">
		<div style="padding-top: 30px; padding-right: 15px; float: right">
			<button class="btn btn-default btn-sm" id="excelDownloadButton" onclick="downloadFcltBtn();">시설물다운</button>
		</div>
	</div>
</div>

<form name="ExcelListForm" method="POST">
	<input type="hidden" name="searchFcltKndCd" value="" />
	<input type="hidden" name="searchFcltUsedTyCd" value="" />
	<input type="hidden" name="excelDownTy" value="" />
	<input type="hidden" name="columnName" />
	<input type="hidden" name="nullColumn" />
	<input type="hidden" name="selectFcltUsed" value="" />
	<input type="hidden" name="chkFcltUseTy" value="" />
	<input type="hidden" name="titleNm" />
</form>
<iframe id="if_Hidden" name="if_Hidden" width="0" height="0" marginHeight="0" marginWidth="0" frameborder="0" scrolling="no"></iframe>
<script>
	$(function() {
		fcltKindInfoList('#searchFcltKndCd-popup', '${param.searchFcltKndCd}');
		fcltUsedTyList('#searchFcltUsedTyCd-popup', '${param.searchFcltKndCd}', '${param.searchFcltUsedTyCd}');
		$('#searchFcltKndCd-popup').change(function() {
			fcltUsedTyList('#searchFcltUsedTyCd-popup', $(this).val(), '');
		});

		$('#grid-popup').jqGrid({
			url : contextRoot + '/mntr/fcltExcelColumnList.json',
			datatype : 'json',
			mtype : 'POST',
			width : "380",
			height : "200",
			multiselect : true,
			loadonce : true,
			data : {},
			rowNum : 100,
			success : function(data) {
				if (data.ret == 1) {
					location.href = contextRoot + data.redirect;
				}
				else {
					alert(data.msg);
				}
			},
			colNames: [
					'선택', '항목명'
			],
			colModel: [
					{
						name: 'columnName',
						index : 'COLUMN_NAME',
						width : 190,
						align : 'center',
						hidden : true
					}, {
						name: 'comments',
						index : 'COMMENTS',
						width : 190,
						align : 'center'
					}
			],
			jsonReader : {
				root : "rows",
			},
			loadComplete : function() {
				oCommon.jqGrid.resize('popup');
			}
		});
		$(".ui-jqgrid-sortable").css("cursor", "default");
	});

	/* 용도  selectFcltUsed*/
	function reloadFcltUsed(selected) {
		$.ajax({
			type : 'POST',
			url : contextRoot + '/mntr/selectFcltUsed.json',
			data : {
				fcltKndCd : selected
			},
			success : function(data) {
				var getFcltUsedList = data.list;
				$('#selectFcltUsed').empty();
				$('#selectFcltUsed').append("<option value=''> 전체 </option>");

				for (var i = 0; i < getFcltUsedList.length; i++) {
					$('#selectFcltUsed').append("<option value='" + getFcltUsedList[i].fcltUsedTyCd + "'>" + getFcltUsedList[i].fcltUsedTyNm + "</option>");
				}
			},
			error : function() {
				alert("용도를 가져오지 못했습니다.");
			}
		});
	}

	/* 2017 시설물 엑셀다운로드 : 필수로 작성할 시설물 기본항목 조회 */
	function chkFcltDefaultBtn() {
		$("#grid-popup").jqGrid('resetSelection'); //체크박스 전체해제

		$.ajax({
			type : 'POST',
			url : contextRoot + '/mntr/fcltDefaultColumns.json',
			success : function(data) {
				var allColumn = $("#grid-popup").jqGrid('getDataIDs'); // 모든 행 번호 가져옴 1~69개
				var column = new Array(); // 69개의 항목명을 담을 배열 선언

				for (var i = 0; i < allColumn.length; i++) {
					column = $("#grid-popup").jqGrid('getCell', allColumn[i], 'comments'); //행 번호에 맞는 항목명을 가져옴

					for (var j = 0; j < data.columnList.length; j++) {
						if (column == data.columnList[j]) { //그리드의 항목명과 DB에 저장된 기본항목명이 일치하는지 비교
							$("#grid-popup").jqGrid('setSelection', i + 1); // 일치하는 항목명만 체크박스 선택
						}
					}
				}
			},
			error : function() {
				alert("오류");
			}
		});

		$('#chkReset').attr('disabled', false);
	}

	function chkAllResetBtn() {
		$("#grid-popup").jqGrid('resetSelection'); //체크박스 전체해제

		var rowArray = $("#grid-popup").jqGrid('getDataIDs');
		$("#grid-popup").jqGrid('setSelection', rowArray[0], true); //시설물 아이디 기본 체크
	}

	/* 2017 시설물 엑셀다운로드 : validation 체크 */
	function downloadFcltBtn() {
		// 사용유형 validation 체크
		if ($("input:checkbox[name=chkFcltUseTy]:checked").is(":checked") == false) {
			alert("사용유형을 선택해 주세요.");
			return;
		}

		// 사용자 비밀번호와 입력한 비밀번호 비교하는 부분(DB 결과값은 'O' 이거나 'X')
		var pwd = $("#loginPwd").val();
		var updUserId = $("#updUserId").val();
		if (null != pwd && "" != pwd) {
			$.ajax({
				type : 'POST',
				url : contextRoot + '/mntr/fcltExcelUserValidation.json',
				data : {
					pwd : pwd
				},
				success : function(data) {
					if (data.reVal == 'O') {
						downloadToExcel();
					}
					else {
						alert("비밀번호가 일치하지 않습니다.");
					}
				},
				error : function() {
					alert("비밀번호 체크 오류입니다.");
				}
			});
		}
		else {
			alert("비밀번호를 입력해 주세요.");
		}
	}

	/* 2017 시설물 엑셀다운로드 : 그리드에서 체크한 항목만 다운로드 */
	function downloadToExcel() {
		// 선택한 용도 select box 값을 가져오는 부분
		var selectResult = $('#searchFcltUsedTyCd-popup option:selected').val();

		// 체크한 사용유형을 가져오는 부분
		var chkResult = new Array();
		$("input:checkbox[name=chkFcltUseTy]:checked").each(function() {
			chkResult.push($(this).val());
		});

		// 구분 라디오 버튼의 value 값을 가져오는 부분
		var excelDownTy = $("input:checkbox[name=excelDownTy]:checked").val();

		var obj = $("#grid-popup");
		var idx = obj.jqGrid('getGridParam', 'selarrrow');

		// 그리드 항목 validation 체크
		if (idx.length == 0) {
			alert("항목을 선택해 주세요.");
			return;
		}
		// 체크한 항목을 타이틀로 사용
		var titleNm = '등록구분,';
		for (var i = 0; i < idx.length; i++) {
			titleNm += obj.jqGrid('getCell', idx[i], 'comments') + ',';
		}
		titleNm = titleNm.substring(0, titleNm.lastIndexOf(','));

		// 체크한 항목을 가져오는 부분
		var tabIds = '\'수정\' AS \'등록구분\',';
		for (var i = 0; i < idx.length; i++) {
			tabIds += obj.jqGrid('getCell', idx[i], 'columnName') + ' AS \'' + obj.jqGrid('getCell', idx[i], 'comments') + '\',';
		}
		tabIds = tabIds.substring(0, tabIds.lastIndexOf(','));

		// 미입력항목을 체크하기 위해 항목을 다시 가져오는 부분
		var nullColumn = '';
		for (var i = 0; i < idx.length; i++) {

			nullColumn += obj.jqGrid('getCell', idx[i], 'columnName') + ' IS NULL ' + 'OR ';
			if (obj.jqGrid('getCell', idx[i], 'columnName') == "POINT_X") nullColumn += 'POINT_X = 0 ' + 'OR ';
			if (obj.jqGrid('getCell', idx[i], 'columnName') == "POINT_Y") nullColumn += 'POINT_Y = 0 ' + 'OR ';
		}

		nullColumn = nullColumn.substring(0, nullColumn.lastIndexOf('OR'));

		// 그리드에서 항목명이 없는 경우 체크하는 부분
		var chkComments = 0;
		var test = jQuery("#grid-popup").jqGrid('getGridParam', 'selarrrow');
		for (var i = 0; i < test.length; i++) {
			if (jQuery("#grid-popup").jqGrid('getCell', test[i], 'comments') == null) { //선택한 항목명이 null일 경우
				chkComments = 1;
			}
		}
		if (chkComments == 1) {
			alert("항목명을 확인하세요.");
			$('#chkDefault').prop('disabled', true);
			$('#codeDown').prop('disabled', true);
			$('#excelDownloadButton').prop('disabled', true);
			return;
		}

		document.ExcelListForm.searchFcltKndCd.value = $('#searchFcltKndCd-popup option:selected').val();
		document.ExcelListForm.searchFcltUsedTyCd.value = $('#searchFcltUsedTyCd-popup option:selected').val();
		document.ExcelListForm.excelDownTy.value = excelDownTy;
		document.ExcelListForm.columnName.value = tabIds;
		document.ExcelListForm.nullColumn.value = nullColumn;
		document.ExcelListForm.selectFcltUsed.value = selectResult;
		document.ExcelListForm.chkFcltUseTy.value = chkResult;
		document.ExcelListForm.target = 'if_Hidden';
		document.ExcelListForm.titleNm.value = titleNm;
		document.ExcelListForm.action = '<c:url value="/mntr/fcltExcelPopupDownload.do" />';
		document.ExcelListForm.submit();

		$('#loginPwd').val('');
	}
</script>