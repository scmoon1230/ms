$(function() {
	oMngIcon = new mngIcon();
	oMngIcon.init();
});

function mngIcon() {
	this.init = function() {
		// LEFT 조절
		oConfigure.mntrViewLeft = 500;
		$('aside#left').css('width', oConfigure.mntrViewLeft);
		$('section#body').css('left', oConfigure.mntrViewLeft + 10);
		$('#toggleLeft').css('left', oConfigure.mntrViewLeft);
		
		collapse({
			left : false
		});

		oMngIcon.list('CCTV');

		$('input[name="rdo-icon-gbn"]').on('change', function() {
			var $caption = $('table#tb-icon-selected caption');
			var $thead = $('table#tb-icon-selected thead');
			var $tbody = $('table#tb-icon-selected tbody');
			$caption.empty();
			$thead.empty();
			$tbody.empty();

			var sIconGbn = $('input[name="rdo-icon-gbn"]:checked').val();
			oMngIcon.list(sIconGbn);
		});

		$('#file-icon').on('change', function() {
			if (this.files && this.files[0]) {
				var oData = $(this).data();
				oMngIcon.upload(this);
			}
		});
	};

	this.list = function(iconGbn) {
		$.ajax({
			url : contextRoot + '/mntr/iconList.json',
			type : 'POST',
			data : {
				iconGbn : iconGbn
			},
			success : function(oData) {
				var oList = oData.list;
				var $thead = $('table#tb-icon thead');
				var $tbody = $('table#tb-icon tbody');
				$thead.empty();
				$tbody.empty();
				$('#td-icon-before').empty();

				var SUFFIX_GREEN = '_0_0';
				var SUFFIX_RED = '_0_1';
				var SUFFIX_GRAY = '_0_2';
				var SUFFIX_ROTATION = '_RT';

				if ('CCTV' == iconGbn) {
					var $trSpan = $('<tr/>');
					$trSpan.append(oMngIcon.th('선택', 1, 2));
					$trSpan.append(oMngIcon.th('이름(코드)', 1, 2));
					$trSpan.append(oMngIcon.th('그룹(코드)', 1, 2));
					$trSpan.append(oMngIcon.th('A', 6, 1));
					$trSpan.append(oMngIcon.th('B', 6, 1));
					$trSpan.appendTo($thead);

					var $tr = $('<tr/>');
					$tr.append(oMngIcon.th('정상(고정형)'));
					$tr.append(oMngIcon.th('이상(고정형)'));
					$tr.append(oMngIcon.th('미확인(고정형)'));
					$tr.append(oMngIcon.th('정상(회전형)'));
					$tr.append(oMngIcon.th('이상(회전형)'));
					$tr.append(oMngIcon.th('미확인(회전형)'));
					$tr.append(oMngIcon.th('정상(고정형)'));
					$tr.append(oMngIcon.th('이상(고정형)'));
					$tr.append(oMngIcon.th('미확인(고정형)'));
					$tr.append(oMngIcon.th('정상(회전형)'));
					$tr.append(oMngIcon.th('이상(회전형)'));
					$tr.append(oMngIcon.th('미확인(회전형)'));
					$tr.appendTo($thead);

					if (oList.length) {
						$.each(oList, function(i, v) {
							var $tr = $('<tr/>', {
								'data-icon-gbn' : iconGbn,
								'data-icon-id' : v.iconId,
								'data-icon-nm' : v.iconNm,
								'data-icon-grp-nm' : v.iconGrpNm,
								'data-icon-grp-id' : v.iconGrpId
							});
							$tr.append($('<td/>', {
								'html' : '<input type="radio" name="rdo-icon-id" value="' + v.iconId + '" />'
							}));
							$tr.append($('<td/>', {
								'text' : v.iconNm + '(' + v.iconId + ')'
							}));
							$tr.append($('<td/>', {
								'text' : v.iconGrpNm + '(' + v.iconGrpId + ')'
							}));
							$tr.append(oMngIcon.td(v, 'A', SUFFIX_GREEN));
							$tr.append(oMngIcon.td(v, 'A', SUFFIX_RED));
							$tr.append(oMngIcon.td(v, 'A', SUFFIX_GRAY));
							$tr.append(oMngIcon.td(v, 'A', SUFFIX_GREEN + SUFFIX_ROTATION));
							$tr.append(oMngIcon.td(v, 'A', SUFFIX_RED + SUFFIX_ROTATION));
							$tr.append(oMngIcon.td(v, 'A', SUFFIX_GRAY + SUFFIX_ROTATION));
							$tr.append(oMngIcon.td(v, 'B', SUFFIX_GREEN));
							$tr.append(oMngIcon.td(v, 'B', SUFFIX_RED));
							$tr.append(oMngIcon.td(v, 'B', SUFFIX_GRAY));
							$tr.append(oMngIcon.td(v, 'B', SUFFIX_GREEN + SUFFIX_ROTATION));
							$tr.append(oMngIcon.td(v, 'B', SUFFIX_RED + SUFFIX_ROTATION));
							$tr.append(oMngIcon.td(v, 'B', SUFFIX_GRAY + SUFFIX_ROTATION));
							$tr.appendTo($tbody);
						});
					} else {
						var $tr = $('<tr/>');
						$tr.append($('<td/>', {
							'text' : '목록이 없습니다.',
							'class' : 'text-center',
							'colspan' : 15
						}));
						$tr.appendTo($tbody);
					}
				} else if ('ETC' == iconGbn) {
					var $tr = $('<tr/>');
					$tr.append(oMngIcon.th('선택'));
					$tr.append(oMngIcon.th('이름(코드)'));
					$tr.append(oMngIcon.th('그룹(코드)'));
					$tr.append(oMngIcon.th('A'));
					$tr.append(oMngIcon.th('B'));
					$tr.appendTo($thead);

					if (oList.length) {
						$.each(oList, function(i, v) {
							var $tr = $('<tr/>', {
								'data-icon-gbn' : iconGbn,
								'data-icon-id' : v.iconId,
								'data-icon-nm' : v.iconNm,
								'data-icon-grp-nm' : v.iconGrpNm,
								'data-icon-grp-id' : v.iconGrpId
							});
							$tr.append($('<td/>', {
								'html' : '<input type="radio" name="rdo-icon-id" value="' + v.iconId + '" />'
							}));
							$tr.append($('<td/>', {
								'text' : v.iconNm + '(' + v.iconId + ')'
							}));
							$tr.append($('<td/>', {
								'text' : v.iconGrpNm + '(' + v.iconGrpId + ')'
							}));
							$tr.append(oMngIcon.td(v, 'A'));
							$tr.append(oMngIcon.td(v, 'B'));
							$tr.appendTo($tbody);
						});
					} else {
						var $tr = $('<tr/>');
						$tr.append($('<td/>', {
							'text' : '목록이 없습니다.',
							'class' : 'text-center',
							'colspan' : 5
						}));
						$tr.appendTo($tbody);
					}
				}

				elTr = $tbody.find('tr');
				$(elTr).on('click', function() {
					var $tr = $(this);
					var elRdo = $tr.find('input[name="rdo-icon-id"]');
					if (elRdo.length) {
						$(elRdo).prop('checked', true);
						var oData = $tr.data();
						oMngIcon.select(oData);
					}
				});
			},
			error : function(e) {

			}
		});
	};

	this.tr = function(data, label, suffix) {
		if (!suffix) suffix = '';
		if (!label) label = '';

		var ICON_PATH = contextRoot + '/images/mntr/gis/';
		var ICON_GBN = data.iconGbn == 'CCTV' ? 'fclt' : data.iconGbn.toLowerCase();
		var ICON_TYPEA = '/typeA/';
		var ICON_TYPEB = '/typeB/';
		var SUFFIX_EXTENSION = '.png?' + new Date().getTime();
		var ICON_FULL_PATH_A = ICON_PATH + ICON_GBN + ICON_TYPEA + data.iconId + suffix + SUFFIX_EXTENSION;
		var ICON_FULL_PATH_B = ICON_PATH + ICON_GBN + ICON_TYPEB + data.iconId + suffix + SUFFIX_EXTENSION;

		var $img = $('<img/>', {
			'alt' : data.iconNm,
			'title' : data.iconNm + '(' + data.iconId + ')'
		});

		var $btn = $('<button/>', {
			'text' : '수정',
			'class' : 'btn btn-default btn-xs btn-icon-upload',
			'style' : 'margin-left: 5px;',
			'data-icon-gbn' : ICON_GBN,
			'data-icon-file-nm' : data.iconId + suffix
		});

		var $tr = $('<tr/>');

		$tr.append($('<td/>', {
			'text' : label ? label : data.iconGbn
		}));
		var $tdA = $('<td/>');
		$tdA.append($img.clone().attr('src', ICON_FULL_PATH_A));
		$tdA.append($btn.clone().attr('data-icon-ty', 'typeA'));
		$tr.append($tdA);
		var $tdB = $('<td/>');
		$tdB.append($img.clone().attr('src', ICON_FULL_PATH_B));
		$tdB.append($btn.clone().attr('data-icon-ty', 'typeB'));
		$tr.append($tdB);
		return $tr;
	};

	this.th = function(text, colspan, rowspan) {
		var $th = $('<th/>', {
			'text' : text
		});

		if (colspan && colspan != 1) {
			$th.attr('colspan', colspan);
		}

		if (rowspan && rowspan != 1) {
			$th.attr('rowspan', rowspan);
		}

		return $th;
	};

	this.td = function(data, iconType, suffix) {
		if (!suffix) suffix = '';
		if (iconType == 'A') iconType = '/typeA/';
		else if (iconType == 'B') iconType = '/typeB/';
		var ICON_PATH = contextRoot + '/images/mntr/gis/';
		var ICON_GBN = data.iconGbn == 'CCTV' ? 'fclt' : data.iconGbn.toLowerCase();
		var SUFFIX_EXTENSION = '.png?' + new Date().getTime();
		var ICON_FULL_PATH = ICON_PATH + ICON_GBN + iconType + data.iconId + suffix + SUFFIX_EXTENSION;

		var $td = $('<td/>');

		var $img = $('<img/>', {
			'alt' : data.iconNm,
			'title' : data.iconNm + '(' + data.iconId + ')',
			'src' : ICON_FULL_PATH
		});
		$td.append($img);
		return $td;
	};

	this.select = function(data) {
		var $caption = $('table#tb-icon-selected caption');
		var $thead = $('table#tb-icon-selected thead');
		var $tbody = $('table#tb-icon-selected tbody');
		$caption.empty();
		$thead.empty();
		$tbody.empty();

		$caption.css({
			'font-weight' : 'bold',
			'font-size' : '12px',
		});
		$caption.html(data.iconNm + '(' + data.iconId + ') - [' + data.iconGrpNm + ']');

		var $trThead = $('<tr/>');
		$trThead.append(oMngIcon.th('구분'));
		$trThead.append(oMngIcon.th('A'));
		$trThead.append(oMngIcon.th('B'));
		$trThead.appendTo($thead);

		var SUFFIX_GREEN = '_0_0';
		var SUFFIX_RED = '_0_1';
		var SUFFIX_GRAY = '_0_2';
		var SUFFIX_ROTATION = '_RT';

		if ('CCTV' == data.iconGbn) {
			$tbody.append(oMngIcon.tr(data, '정상(고정형)', SUFFIX_GREEN));
			$tbody.append(oMngIcon.tr(data, '이상(고정형)', SUFFIX_RED));
			$tbody.append(oMngIcon.tr(data, '미확인(고정형)', SUFFIX_GRAY));
			$tbody.append(oMngIcon.tr(data, '정상(회전형)', SUFFIX_GREEN + SUFFIX_ROTATION));
			$tbody.append(oMngIcon.tr(data, '이상(회전형)', SUFFIX_RED + SUFFIX_ROTATION));
			$tbody.append(oMngIcon.tr(data, '미확인(회전형)', SUFFIX_GRAY + SUFFIX_ROTATION));
		} else if ('ETC' == data.iconGbn) {
			$tbody.append(oMngIcon.tr(data));
		}

		$('.btn-icon-upload').on('click', function() {
			var oData = $(this).data();
			$('#file-icon').data('iconGbn', oData.iconGbn);
			$('#file-icon').data('iconTy', oData.iconTy);
			$('#file-icon').data('iconFileNm', oData.iconFileNm);
			$('#file-icon').trigger('click');
		});
	};

	this.upload = function(eInput) {
		var oInputData = $(eInput).data();
		var eForm = $('#form-icon')[0];
		var oFormData = new FormData(eForm);
		oFormData.append('iconGbn', oInputData.iconGbn);
		oFormData.append('iconTy', oInputData.iconTy);
		oFormData.append('iconFileNm', oInputData.iconFileNm);

		$.ajax({
			type : "POST",
			enctype : 'multipart/form-data',
			url : contextRoot + '/mntr/iconUpd.json',
			data : oFormData,
			processData : false,
			contentType : false,
			cache : false,
			timeout : 600000,
			success : function(data) {
				alert(data.msg);
				var $rdo = $('table#tb-icon tr').find('input[name="rdo-icon-id"]:checked');
				var $tr = $rdo.closest('tr');
				var oData = $tr.data();
				setTimeout(function() {
					$('#form-icon').trigger("reset");
					$('#file-icon').data('iconGbn', '');
					$('#file-icon').data('iconTy', '');
					$('#file-icon').data('iconFileNm', '');
					$('#file-icon').val('');
					oMngIcon.select(oData);
				}, 1000);
			},
			error : function(e) {
				console.log(e);
			}
		});
	};
}
