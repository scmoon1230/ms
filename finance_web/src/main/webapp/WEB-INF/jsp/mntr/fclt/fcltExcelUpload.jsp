<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link href="<c:url value='/css/fileinput/fileinput.css' />" media="all" rel="stylesheet" type="text/css" />
<style>
#resultConts, #resultCnt, #resultDetail, #upload, #errDown, #loginPwd {
	display: none;
}

#orgColumn, #errorColumn {
	max-height: 150px;
	overflow: auto;
}

#well-step {
	font-weight: bolder;
}
</style>
<script src="<c:url value='/js/fileinput/fileinput.js' />"></script>
<script src="<c:url value='/js/fileinput/fileinput_locale_ko.js' />"></script>
<article id="article-grid">
	<ol class="breadcrumb">
		<li>기초정보</li>
		<li>시설물</li>
		<li class="active">${common.title}</li>
	</ol>
	<div class="well well-sm" id="well-step">Step 1. Excel 파일을 업로드 해주세요.</div>
	<form id="form-excel" name="form-excel" method="post" enctype="multipart/form-data" style="margin-bottom: 5px;">
		<div class="form-group">
			<label for="input-excel">Excel 파일 업로드 : </label>
			<input type="file" id="input-excel" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
			<p class="help-block">파일 확장자: xls, xlsx, 파일 사이즈: 1MB 이하, 데이터 건수: 1000건 미만.</p>
		</div>
		<div class="form-group">
			<label for="btn-excel-validation">오류검사 : </label>
			<button type="button" class="btn btn-primary btn-sm" id="btn-excel-validation" onclick="oFcltExcelUpload.validate();">오류검사</button>
		</div>
		<div class="form-group">
			<input type="hidden" id="columnIdUpload" name="columnIdUpload" />
			<input type="hidden" id="columnNmUpload" name="columnNmUpload" />
		</div>
	</form>
	<div id="resultConts">
		<div class="row well well-sm" id="resultCnt">
			<div class="col-xs-4">
				<label>총건수:</label>
				<span id="resultTotCnt"></span>
			</div>
			<div class="col-xs-4">
				<label>신규:</label>
				<span id="insertTotCnt"></span>
			</div>
			<div class="col-xs-4">
				<label>수정:</label>
				<span id="updateTotCnt"></span>
			</div>
		</div>
	</div>
	<div id="resultDetail">
		<div class="well well-sm">
			<div class="form-inline">
				<div class="form-group">
					<label>
						총 :
						<span id="totCnt" style="color: #0000FF;"></span>
					</label>
					<label>
						정상 :
						<span id="norCnt" style="color: #3CB371;"></span>
					</label>
					<label>
						오류 :
						<span id="errCnt" style="color: #FF0000;"></span>
					</label>
				</div>
				<div class="form-group">
					<label for="loginPwd">비밀번호</label>
					<input type="password" class="form-control input-sm" id="loginPwd" name="loginPwd" placeholder="비밀번호" />
				</div>
				<button type="button" id="upload" class="btn btn-default btn-sm" onclick="oFcltExcelUpload.upload();">업로드</button>
				<button type="button" id="errDown" class="btn btn-default btn-sm" onclick="oFcltExcelUpload.download('ERROR');">오류내역다운로드</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-6" id="uploadColumn">
				<label>업로드컬럼명 :</label>
				<div id="orgColumn"></div>
			</div>
			<div class="col-xs-6" id="errorColumn">
				<label>오류컬럼명 : </label>
				<div id="errColumn"></div>
			</div>
			<div class="col-xs-12" id="errorDiv"
				style="background-color: #ECEBFF; border: 1px solid #ccc; margin-top: 10px; height: 250px; overflow-y: auto; overflow-x: hidden; display: none;">
				<table class="table" id="errorTable" style="margin: 5px; font-size: 12px;">
					<thead>
						<tr>
							<th width="10px">행</th>
							<th width="15px">열</th>
							<th width="30px">컬럼명</th>
							<th width="30px">입력값</th>
							<th width="50px">에러메세지</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="well well-sm">
		<button type="button" class="btn btn-default btn-sm" id="btn-download-code" onclick="oFcltExcelUpload.download('CODE');">코드정보 다운로드</button>
		<button type="button" class="btn btn-default btn-sm" id="btn-download-form" onclick="oFcltExcelUpload.download('FORM');">항목정보 다운로드</button>
		<button type="button" class="btn btn-default btn-sm" id="btn-upload-form" onclick="oFcltExcelUpload.uploadExcel();">샘플 다운로드(항목선택등록)</button>
	</div>
</article>
<iframe id="iframe-form-download" name="iframe-form-download"></iframe>
<script>
	$(function() {
		oFcltExcelUpload = new fcltExcelUpload();
		oFcltExcelUpload.init();
	});

	function fcltExcelUpload() {
		this.isValid = false;
		this.errorList = [];

		this.init = function() {
			oDiv.hideDiv();
			$('#input-excel').fileinput({
				language : 'ko',
				showCaption : true,
				dropZoneEnabled : false,
				allowedFileExtensions : [
						'xls', 'xlsx'
				],
				overwriteInitial : false,
				maxFileSize : 1024,
				maxFilesNum : 1,
				minFileCount : 1,
				maxFileCount : 1,
				showUpload : false,
				showPreview : false,
				slugCallback : function(filename) {
					return filename.replace('(', '_').replace(']', '_');
				}
			});

			$('#input-excel').on('fileerror', function(event, data, msg) {
				oCommon.modalAlert('modal-excel', '오류발생', msg);
				setTimeout(function() {
					$('#input-excel').fileinput('reset');
					$('#well-step').text('Step 1. Excel 파일을 선택해 주세요.');
				}, 1000);
			});

			$('#input-excel').on('change', function(event) {
				$('#well-step').text('Step 2. 오류검사 버튼을 눌러 주세요.');
			});

			$('#input-excel').on('filereset', function(event) {
				oFcltExcelUpload.reset();
			});

			$('#input-excel').on('filecleared', function(event) {
				oFcltExcelUpload.reset();
			});
		};

		this.validate = function() {
			if (!$('#input-excel')[0].files.length) {
				oCommon.modalAlert('modal-excel', '오류발생', 'Excel 파일을 선택해 주세요.');
				return false;
			}

			var sUrl = contextRoot + '/mntr/fcltExcelUploadValidation.json';
			var oFormData = new FormData(document.getElementById('form-excel'));
			$.each($('#input-excel')[0].files, function(i, file) {
				oFormData.append('file-' + i, file);
			});

			$.ajax({
				url : sUrl,
				type : 'POST',
				data : oFormData,
				dataType : 'json',
				mimeType : 'multipart/form-data',
				contentType : false,
				cache : false,
				processData : false,
				beforeSend : function() {
					oFcltExcelUpload.errorList = [];
					oFcltExcelUpload.isValid = false;
					$('#well-step').text('Step 2. 오류검사 중입니다. Step 3. 메시지가 나올때까지 기다려주세요.');
				},
				complete : function() {
				},
				success : function(data) {
					oFcltExcelUpload.validateResult(data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(jqXHR, textStatus, errorThrown);
				}
			});
		};

		this.validateResult = function(data) {
			$('#resultConts').show();

			$('#totCnt').text(data.vo.totCnt);
			$('#norCnt').text(data.vo.norCnt);
			$('#errCnt').text(data.vo.errCnt);

			if (data.vo.orgColumn) {
				var orgColumn = data.vo.orgColumn;
				var orgColumnArr = orgColumn.split(',');
				if (orgColumnArr.length) {
					var $ul = $('<ol/>');
					$.each(orgColumnArr, function(i, v) {
						$ul.append($('<li/>', {
							'text' : v
						}));
					});
					$('#orgColumn').html($ul);
				} else {
					$('#orgColumn').empty();
				}
			}

			if (data.vo.errColumn) {
				var errColumn = data.vo.errColumn;
				var errColumnArr = errColumn.split(',');
				if (errColumnArr.length) {
					var $ul = $('<ol/>');
					$.each(errColumnArr, function(i, v) {
						$ul.append($('<li/>', {
							'text' : v
						}));
					});
					$('#errColumn').html($ul);
				} else {
					$('#errColumn').empty();
				}
			}

			$('#uploadColumn').hide();
			$('#errColumn').hide();
			$('#errorDiv').hide();
			$('#resultCnt').hide();
			$('#resultDetail').hide();
			$('#upload').hide();
			$('#errDown').hide();

			$('#columnIdUpload').val(data.vo.columnIdUpload);
			$('#columnNmUpload').val(data.vo.orgColumn);

			var result = data.result;

			if (data.vo.errorList != null && data.vo.errorList.length) {
				oFcltExcelUpload.errorList = data.vo.errorList;
			}

			switch (result) {
				case '1':
					$('#uploadColumn').show();
					$('#resultDetail').show();
					$('#upload').show();
					$('#loginPwd').show();
					// oFcltExcelUpload.upchkExportExcel(data);
					oFcltExcelUpload.isValid = true;
					$('#well-step').text('Step 3. 비밀번호 입력 후 업로드 버튼을 눌러주세요.');
					break;
				case '-1':
					$('#well-step').text('오류발생 : 파일 전송에 실패하였습니다. 관리자에게 문의해 주세요. 삭제 버튼을 누르면 초기화 됩니다.');
					break;
				case '-2':
					$('#well-step').text('오류발생 : 파일 전송에 실패하였습니다. 관리자에게 문의해 주세요. 삭제 버튼을 누르면 초기화 됩니다.');
					break;
				case '-3':
					$('#well-step').text('오류발생 : 업로드할 데이터가 존재 하지 않습니다. 확인 후 다시 시도해 주세요. 삭제 버튼을 누르면 초기화 됩니다.');
					break;
				case '-4':
					$('#uploadColumn').show();
					$('#resultDetail').show();
					$('#well-step').text('오류발생 : 업로드할 데이터가 존재 하지 않습니다. 확인 후 다시 시도해 주세요. 삭제 버튼을 누르면 초기화 됩니다.');
					break;
				case '-5':
					$('#well-step').text('오류발생 : 업로드할 데이터에 오류가 있습니다. 확인 후 다시 시도해 주세요. 삭제 버튼을 누르면 초기화 됩니다.');
					$('#uploadColumn').show();
					$('#errDown').show();
					$('#resultDetail').show();
					$('#errorDiv').show();

					var titleNmi = data.vo.orgColumn;
					var tabIdsi = data.vo.orgColumnId;

					var size = 0;
					if (data.vo.errorList != null && data.vo.errorList.length) {
						size = data.vo.errorList.length;
					}
					for (var i = 0; i < size; i++) {
						var obj = data.vo.errorList[i];
						$('#errorTable > tbody:last').append('<tr><td>' + (obj.rowNum + 1) + '</td><td>' + obj.cellNum + '</td><td>' + obj.columnName + '</td><td>' + obj.columnValue + '</td><td>' + obj.errorMsg + '</td></tr>');
					}
					// oFcltExcelUpload.upchkExportExcel(data); //upchk Table Excel로 내려받음.
					break;
				case '-6':
					$('#well-step').text('오류발생 : 데이터베이스 반영 작업중 에러가 발생하였습니다. 관리자에게 문의해 주세요. 삭제 버튼을 누르면 초기화 됩니다.');
					break;
				case '-7':
					$('#well-step').text('오류발생 : 파일 전송에 실패하였습니다. 등록구분은 신규, 수정 값만 입력되어야 합니다. 삭제 버튼을 누르면 초기화 됩니다.');
					break;
				case '-11':
					$('#well-step').text('오류발생 : 업로드할 데이터에 오류가 있습니다. 확인 후 다시 시도해 주세요. 삭제 버튼을 누르면 초기화 됩니다.');
					$('#resultDetail').show();
					$('#errDown').show();
					$('#errorDiv').show();
					var errorList = data.vo.errorList;
					var size = (errorList.length) ? errorList.length : 0;
					for (var i = 0; i < size; i++) {
						var obj = errorList[i];
						$('#errorTable > tbody:last').append('<tr><td>' + obj.rowNum + '</td><td>' + obj.cellNum + '</td><td>' + obj.columnName + '</td><td>' + obj.columnValue + '</td><td>' + obj.errorMsg + '</td></tr>');
					}
					break;
				default:
					break;
			}
		};

		this.upchkExportExcel = function(data) {
			var titleNms = data.vo.orgColumn; //엑셀에서 헤더명(한글)
			var titleNmChks = data.vo.orgColumnChk; //엑셀에서 오류 컬럼 색깔 표시
			var tabIds = data.vo.orgColumnId; //쿼리에서 사용되는 컬럼ID
			var errorList = data.vo.errorList; //쿼리에서 사용되는 컬럼ID
			var size = errorList == null ? 0 : errorList.length;
			var err = [];
			for (var i = 0; i < size; i++) {
				var obj = errorList[i];
				err.push(obj.rowNum + ':' + obj.columnName);
			}

			var $formUploadChk = $('#form-upload-chk');

			if ($formUploadChk.length) {
				$('#titleNm').val(titleNm);
				$('#titleNmChks').val(titleNmChks);
				$('#tabIds').val(tabIds);
				$('#errorList').val(err);
				$formUploadChk.submit();
			} else {
				var $form = $('<form/>', {
					'method' : 'POST',
					'id' : 'form-upload-chk',
					'action' : contextRoot + '/mntr/fcltExcelUploadChk.do',
					'target' : 'iframe-form-download',
				});

				$form.append($('<input/>', {
					'type' : 'hidden',
					'id' : 'titleNm',
					'name' : 'titleNm',
					'value' : titleNms
				}));

				$form.append($('<input/>', {
					'type' : 'hidden',
					'id' : 'titleNmChk',
					'name' : 'titleNmChk',
					'value' : titleNmChks
				}));

				$form.append($('<input/>', {
					'type' : 'hidden',
					'id' : 'tabIds',
					'name' : 'tabIds',
					'value' : tabIds
				}));

				$form.append($('<input/>', {
					'type' : 'hidden',
					'id' : 'errorList',
					'name' : 'errorList',
					'value' : err
				}));

				$form.appendTo(document.body).submit();
			}
		};

		this.uploadExcel = function() {
			BootstrapDialog.show({
				type : BootstrapDialog.TYPE_INFO,
				cssClass : 'popupDisplay',
				title : '엑셀 업로드',
				message : $('<div id="displayOption"></div>').load(contextRoot + '/mntr/fcltExcelPopupUpload.do')
			});
		};

		this.upload = function() {
			//비밀번호 체크
			var sPwd = $('#loginPwd').val();
			// 사용자 비밀번호와 입력한 비밀번호 비교하는 부분(DB 결과값은 'O' 이거나 'X')
			if (sPwd.length) {
				$.ajax({
					type : 'POST',
					url : contextRoot + '/mntr/fcltExcelUserValidation.json',
					data : {
						pwd : sPwd
					},
					success : function(data) {
						if (data.reVal == 'O') {
							oFcltExcelUpload.uploadProc();
						} else {
							oCommon.modalAlert('modal-excel', '오류발생', '비밀번호가 일치하지 않습니다.');
						}
					},
					error : function() {
						oCommon.modalAlert('modal-excel', '오류발생', '비밀번호 체크 오류입니다.');
					}
				});
			} else {
				oCommon.modalAlert('modal-excel', '오류발생', '비밀번호를 입력해 주세요.');
			}
			$('#loginPwd').val('');
		};

		this.uploadProc = function() {
			var sSerialize = $('#form-excel').serialize();
			$.ajax({
				type : 'POST',
				url : contextRoot + '/mntr/fcltExcelUploadProc.json',
				dataType : 'json',
				data : sSerialize,
				success : function(data) {
					var result = data.result;
					if (result == '1') {
						oCommon.modalAlert('modal-excel', '성공', '일괄 업로드가 완료되었습니다.');
						$('#well-step').text('Step 4. 일괄 업로드가 완료되었습니다. 삭제 버튼을 누르면 초기화 됩니다.');
						$('#beforeConts').text('');
						$('#beforeConts').hide();
						$('#resultConts').show();

						$('#resultTotCnt').text(data.vo.allCnt + '건  ');
						$('#insertTotCnt').text(data.vo.regCnt + '건  ');
						$('#updateTotCnt').text(data.vo.updCnt + '건  ');

						$('#errColumn').hide();
						$('#resultCnt').hide();
						$('#resultDetail').hide();
						$('#upload').hide();
						$('#errUpload').hide();
						$('#errDown').hide();
						//$('#errorList').hide();
						$('#errorDiv').hide();

						var result = data.result;

						switch (result) {
							case '1':
								$('#uploadColumn').show();
								$('#resultCnt').show();
								break;
							case '-1':
								$('#well-step').text('오류발생 : 업로드에 실패하였습니다. 관리자에게 문의해 주세요.');
								break;
							case '-2':
								$('#well-step').text('오류발생 : 업로드에 실패하였습니다. 관리자에게 문의해 주세요.');
								break;
							case '-3':
								$('#well-step').text('오류발생 : 업로드할 데이터가 존재 하지 않습니다. 확인 후 다시 시도해 주세요.');
								break;
							case '-4':
								$('#well-step').text('오류발생 : 데이터베이스 반영 작업중 에러가 발생하였습니다. 관리자에게 문의해 주세요.');
								break;
							case '-5':
								$('#well-step').text('오류발생 : 데이터베이스 반영 작업중 에러가 발생하였습니다. 관리자에게 문의해 주세요.');
								break;
							case '-6':
								$('#well-step').text('오류발생 : 데이터베이스 반영 작업중 에러가 발생하였습니다. 관리자에게 문의해 주세요.');
								break;
							case '-11':
								$('#well-step').text('오류발생 : 데이터베이스 반영 작업중 에러가 발생하였습니다. 관리자에게 문의해 주세요.');
								break;
							default:
								break;
						}
					} else {
						oCommon.modalAlert('modal-excel', '오류발생', '일괄 업로드에 실패하였습니다.');
					}
				},
				error : function(xhr, status, error) {
					oCommon.modalAlert('modal-excel', '오류발생', '일괄 업로드에 실패하였습니다.');
				}
			});
		};

		this.download = function(type) {
			var sUrl = '';
			if (type == 'CODE') {
				sUrl = contextRoot + '/mntr/downloadExcelUploadCode.do';
			} else if (type == 'FORM') {
				sUrl = contextRoot + '/mntr/downloadExcelUploadForm.do';
			} else if (type == 'ERROR') {
				sUrl = contextRoot + '/mntr/downloadExcelErrorList.do';
			} else {
				oCommon.modalAlert('modal-excel', '오류발생', '다운로드 형식이 일치하지 않습니다.');
				return false;
			}

			var $formDownload = $('#form-download');
			if ($formDownload.length) {
				$formDownload.attr('action', sUrl);
			} else {
				$formDownload = $('<form/>', {
					'id' : 'form-download',
					'method' : 'POST',
					'action' : sUrl,
					'target' : 'iframe-form-download',
				});
				$formDownload.appendTo(document.body);
			}

			if (type == 'FORM') {
				var elInputFilePath = $formDownload.find('input#filePath');
				if (!elInputFilePath.length) {
					//var sFilePath = 'file.upload.path.form';
					var sFilePath = 'FILE_UPLOAD_PATH_FORM';
					$formDownload.append($('<input/>', {
						'type' : 'hidden',
						'id' : 'filePath',
						'name' : 'filePath',
						'value' : sFilePath
					}));
				}
				var elInputFileName = $formDownload.find('input#fileName');
				if (!elInputFileName.length) {
					//var sFileName = 'file.upload.fclt.form';
					var sFileName = 'FILE_UPLOAD_FCLT_FORM';
					$formDownload.append($('<input/>', {
						'type' : 'hidden',
						'id' : 'fileName',
						'name' : 'fileName',
						'value' : sFileName
					}));
				}
			}

			if (type == 'ERROR') {
				if (!oFcltExcelUpload.errorList.length) {
					oCommon.modalAlert('modal-excel', '오류발생', '다운로드 받을 데이터가 없습니다.');
					return false;
				}

				var elInputXlsheader = $formDownload.find('input#xlsheader');
				if (!elInputXlsheader.length) {
					var sXlsheader = "행\t" + "열\t" + "컬럼명\t" + "입력값\t" + "에러메세지\n";
					$formDownload.append($('<input/>', {
						'type' : 'hidden',
						'id' : 'xlsheader',
						'name' : 'xlsheader',
						'value' : sXlsheader
					}));

					$formDownload.append($('<input/>', {
						'type' : 'hidden',
						'id' : 'xlsfilename',
						'name' : 'xlsfilename',
						'value' : '시설물일괄업로드_오류내역'
					}));

					$formDownload.append($('<input/>', {
						'type' : 'hidden',
						'id' : 'xlstitle',
						'name' : 'xlstitle',
						'value' : '오류내역'
					}));
				}

				var xlsdata = '';
				$.each(oFcltExcelUpload.errorList, function(i, v) {
					xlsdata += (v.rowNum + 1) + "\t" + v.cellNum + "\t" + v.columnName + "\t" + v.columnValue + "\t" + v.errorMsg + "\n";
				});

				console.log(xlsdata);

				var elInputXlsdata = $formDownload.find('input#xlsdata');
				if (!elInputXlsdata.length) {
					$formDownload.append($('<input/>', {
						'type' : 'hidden',
						'id' : 'xlsdata',
						'name' : 'xlsdata',
						'value' : xlsdata
					}));
				} else {
					$('#xlsdata').val(xlsdata);
				}

			}

			$formDownload.submit();
		};

		this.reset = function() {
			$('#resultConts').hide();
			$('#resultCnt').hide();
			$('#resultDetail').hide();
			$('#upload').hide();
			$('#errDown').hide();
			$('#loginPwd').hide();
			$('#totCnt').empty();
			$('#norCnt').empty();
			$('#errCnt').empty();
			$('#resultTotCnt').empty();
			$('#insertTotCnt').empty();
			$('#updateTotCnt').empty();
			$('#well-step').text('Step 1. Excel 파일을 선택해 주세요.');
		};
	}
</script>
