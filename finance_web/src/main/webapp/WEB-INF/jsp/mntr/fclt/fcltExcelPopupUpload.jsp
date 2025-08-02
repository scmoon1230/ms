<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="form-horizontal">
	<div class="form-group form-group-sm">
		<label class="col-sm-3 control-label">항목선택 </label>
		<div class="col-sm-9">
			<button class="btn btn-default btn-sm" id="chkDefault" onclick="chkFcltDefaultBtn();">기초 항목</button>
		</div>
	</div>
	<!-- 항목리스트(선택/항목명) 그리드 -->
	<table id="grid-popup"></table>
	<div class="form-group form-group-sm">
		<div style="padding-top: 30px; padding-right: 15px; float: right">
			<button class="btn btn-default btn-sm" id="excelDownloadButton" onclick="downloadToExcel();">다운로드</button>
		</div>
	</div>
</div>
<form name="ExcelListForm" method="POST">
	<input type="hidden" name="columnName" />
	<input type="hidden" name="titleNm" />
	<input type="hidden" name="chkFcltUseTy" value="X" />
</form>
<iframe id="iframe-excel-download" name="iframe-excel-download" width="0" height="0"></iframe>
<script>
	$(function() {
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
					console.log(data);
					// location.href = contextRoot + data.redirect;
				} else {
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
	});

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

		// $('#chkReset').attr('disabled', false);
	}

	/* 2017 시설물 엑셀다운로드 : 그리드에서 체크한 항목만 다운로드 */
	function downloadToExcel() {
		// 구분 라디오 버튼의 value 값을 가져오는 부분

		var $gridPopup = $("#grid-popup");
		var oIdx = $gridPopup.jqGrid('getGridParam', 'selarrrow');

		// 그리드 항목 validation 체크
		if (oIdx.length == 0) {
			alert("항목을 선택해 주세요.");
			return;
		}
		// 체크한 항목을 타이틀로 사용
		var titleNm = '등록구분,';
		for (var i = 0; i < oIdx.length; i++) {
			titleNm += $gridPopup.jqGrid('getCell', oIdx[i], 'comments') + ',';
		}
		titleNm = titleNm.substring(0, titleNm.lastIndexOf(','));

		// 체크한 항목을 가져오는 부분
		var tabIds = '\'수정\' AS 등록구분,';
		for (var i = 0; i < oIdx.length; i++) {
			tabIds += $gridPopup.jqGrid('getCell', oIdx[i], 'columnName') + ' AS ' + $gridPopup.jqGrid('getCell', oIdx[i], 'comments') + ',';
		}
		tabIds = tabIds.substring(0, tabIds.lastIndexOf(','));

		document.ExcelListForm.columnName.value = tabIds;
		document.ExcelListForm.titleNm.value = titleNm;

		document.ExcelListForm.target = 'iframe-excel-download';
		document.ExcelListForm.action = contextRoot + '/mntr/fcltExcelPopupDownload.do';
		document.ExcelListForm.submit();
	}
</script>