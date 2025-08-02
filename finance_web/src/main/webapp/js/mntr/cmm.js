
if (typeof String.prototype.startsWith != 'function') {
	String.prototype.startsWith = function (str) {
		return this.indexOf(str) == 0;
	};
}

if (typeof String.prototype.replaceAll != 'function') {
	String.prototype.replaceAll = function (org, dest) {
		return this.split(org).join(dest);
	};
}

$.fn.exists = function () {
	return this.length > 0;
};

$.jqGrid = function (gridObj, params) {
	// gridObj.jqGrid("GridUnload");
	gridObj.jqGrid(params);
};

$.ajaxEx = function (gridObj, params) {
	$.ajax({
		type: "POST",
		url: params.url,
		dataType: params.dataType,
		data: params.data,
		success: function (data) {
			if (data.session == null || data.session == -1) {
				alert("세션이 끊어졌으므로 로그아웃하고 첫페이지로 보냄!!!");
				return;
			}

			params.success(data);
		},
		error: function (e) {
			params.error(e);
		}
	});
};

var console = console || {
	log: function () {
	},
	warn: function () {
	},
	error: function () {
	}
};

String.prototype.replaceAt = function (index, replacement) {
	return this.substring(0, index) + replacement + this.substring(index + replacement.length);
}

/**
 * selectBox 공통 함수
 * @param selector : select 객체
 * @param url :	  호출 메소드 Request Url
 * @param param :	json 타입의 파라미터
 * @param selected : selected 속성을 갖을 값
 */
function getSelectboxList(selector, url, param, selected, lbl) {
	$(selector + ' option').remove();

	if (selected == null) selected = "";
	$.ajax({
		type: 'POST',
		url:contextRoot + url,
		data: param,
		beforeSend: function () {
		},
		success: function (data) {
			$(selector).val(selected);
			var codeInfoList = data.list;

			if (typeof lbl != 'undefined' && lbl != null) {

			} else {
				lbl = '::: 전체 :::';
			}

			$(selector).append("<option value=''> " + lbl + " </option>");
			for (var i = 0; i < codeInfoList.length; i++) {
				if (typeof codeInfoList[i].cd == 'undefined') {
					$(selector).append("<option value='" + codeInfoList[i].cdId + "'>" + codeInfoList[i].cdNmKo + "</option>");
				} else {
					$(selector).append("<option value='" + codeInfoList[i].cd + "'>" + codeInfoList[i].nm + "</option>");
				}
			}
			$(selector).val(selected);
		},
		error: function () {
			alert("정보를 가져오지 못했습니다.");
		}
	});
}

// 코드목록을 가져온다.
function codeInfoList(selector, code, selected, lbl, callback) {
	
	$(selector).empty();

	if (selected == null) {
		  selected = '';
	}
	$.ajax({
		type: 'POST',
		url:contextRoot + '/mntr/cmm/selectCodeList.json',
		data: {
			gCodeId: code
		},
		beforeSend: function () {
		},
		success: function (data) {
			if (typeof lbl != 'undefined' && lbl != null) {
			} else {
				lbl = '::: 전체 :::';
			}

			var codeInfoList = data.list;

			$(selector).append('<option value=""> ' + lbl + ' </option>');

			for (var i = 0; i < codeInfoList.length; i++) {
				if (selected == codeInfoList[i].cdId) {
					$(selector).append('<option value="' + codeInfoList[i].cdId + '" selected>' + codeInfoList[i].cdNmKo + '</option>');
				} else {
					$(selector).append('<option value="' + codeInfoList[i].cdId + '">' + codeInfoList[i].cdNmKo + '</option>');
				}
			}

			if (typeof callback === 'function') callback();
		},
		error: function () {
			alert("정보를 가져오지 못했습니다.");
		}
	});
}

// 지구 목록을 가져온다.
function dstrtInfoList(selector, selected, callback) {
	if ($(selector).length) {
		if (typeof selected == 'undefined') selected = '';
		$.ajax({
			type: 'POST',
			url:contextRoot + '/mntr/cmm/selectDistrictList.json',
			data: {},
			beforeSend: function () {
			},
			success: function (data) {
				var codeInfoList = data.list;
				$(selector).append("<option value=''>지구</option>");
				for (var i = 0; i < codeInfoList.length; i++) {
					$(selector).append("<option value='" + codeInfoList[i].cd + "'>" + codeInfoList[i].nm + "</option>");
				}
				$(selector).val(selected);

				if (typeof callback == 'function') callback();
			},
			error: function () {
				alert("정보를 가져오지 못했습니다.");
			}
		});
	}
}

// 시설물유형 목록을 가져온다.
function fcltKindInfoList(selector, selected, callback) {
	if ($(selector).length) {
		if (typeof selected == 'undefined') selected = '';
		$.ajax({
			type: 'POST',
			url:contextRoot + '/mntr/cmm/selectFcltKindList.json',
			data: {},
			beforeSend: function () {
			},
			success: function (data) {
				var codeInfoList = data.list;
				$(selector).append("<option value=''>시설물유형</option>");
				for (var i = 0; i < codeInfoList.length; i++) {
					$(selector).append("<option value='" + codeInfoList[i].cdId + "'>" + codeInfoList[i].cdNmKo + "</option>");
				}
				$(selector).val(selected);

				if (typeof callback == 'function') callback();
			},
			error: function () {
				alert("정보를 가져오지 못했습니다.");
			}
		});
	}
}

// 시설물용도 목록을 가져온다.
function fcltUsedTyList(selector, fcltKnd, selected) {
	if ($(selector).length) {
		$(selector + ' option').remove();
		if (typeof selected == 'undefined') selected = '';
		$.ajax({
			type: 'POST',
			url:contextRoot + '/mntr/cmm/selectFcltUsedTyList.json',
			data: {
				gCodeId: fcltKnd
			},
			async: false,
			beforeSend: function () {
			},
			success: function (data) {
				var codeInfoList = data.list;
				$(selector).append("<option value=''>유형별용도</option>");
				for (var i = 0; i < codeInfoList.length; i++) {
					$(selector).append("<option value='" + codeInfoList[i].cdId + "'>" + codeInfoList[i].cdNmKo + "</option>");
				}
				$(selector).val(selected);
			},
			error: function () {
				alert("정보를 가져오지 못했습니다.");
			}
		});
	}
}

/* ajax spinner */
var opts = {
	lines: 13, // The number of lines to draw
	length: 20, // The length of each line
	width: 10, // The line thickness
	radius: 30, // The radius of the inner circle
	corners: 1, // Corner roundness (0..1)
	rotate: 0, // The rotation offset
	direction: 1, // 1: clockwise, -1: counterclockwise
	color: '#000', // #rgb or #rrggbb or array of colors
	speed: 1, // Rounds per second
	trail: 60, // Afterglow percentage
	shadow: false, // Whether to render a shadow
	hwaccel: false, // Whether to use hardware acceleration
	className: 'spinner', // The CSS class to assign to the spinner
	zIndex: 2e9, // The z-index (defaults to 2000000000)
	top: '50%', // Top position relative to parent
	left: '50%' // Left position relative to parent
};

/* 오늘을 기준으로 period기간(일) 동안 from to를 자동으로 입력해 주는 기능 */
function setInputDate(from, to, period) {
	var inputFrom = document.getElementById(from);
	var inputTo = document.getElementById(to);

	var to = new Date();
	var yearTo = new String(to.getFullYear());
	var monthTo = new String(to.getMonth() + 1);
	var dateTo = new String(to.getDate());

	var from = new Date();
	from.setDate(to.getDate() - period);
	var yearFrom = new String(from.getFullYear());
	var monthFrom = new String(from.getMonth() + 1);
	var dateFrom = new String(from.getDate());

	if (monthTo.length == 1) {
		monthTo = '0' + monthTo;
	}
	if (dateTo.length == 1) {
		dateTo = '0' + dateTo;
	}
	if (monthFrom.length == 1) {
		monthFrom = '0' + monthFrom;
	}
	if (dateFrom.length == 1) {
		dateFrom = '0' + dateFrom;
	}

	$(inputFrom).val(yearFrom + '/' + monthFrom + '/' + dateFrom);
	$(inputTo).val(yearTo + '/' + monthTo + '/' + dateTo);
}

/* 페이징 갱신 */
function pagenationReload(selector, data, params) {
	$('#paginate-' + selector).empty();
	$('#paginate-' + selector).html($('<ul/>', {
		'id': 'pagination-' + selector,
		'class': 'paging'
	}));

	var nVisiblePages = 4;
	if (typeof params != 'undefined' && typeof params.visiblePages != 'undefined' && !isNaN(params.visiblePages)) {
		nVisiblePages = params.visiblePages;
	}

	$('#pagination-' + selector).twbsPagination({
		startPage: data.page,
		totalPages: data.totalPages,
		visiblePages: nVisiblePages,
		onPageClick: function (event, page) {
			gridReloadNoparam(selector, page);
		},
		first: '<i class="fas fa-angle-double-left"></i>',
		prev: '<i class="fas fa-angle-left"></i>',
		next: '<i class="fas fa-angle-right"></i>',
		last: '<i class="fas fa-angle-double-right"></i>'
	});
}

/* 그리드 갱신 */
function gridReload(selector, page, param) {
	// $('#grid-' + selector + ' tbody').empty();

	$('#grid-' + selector).setGridParam({
		page: page,
		postData: param
	}).trigger("reloadGrid");
}

function gridReloadNoparam(selector, page) {
	// $('#grid-' + selector + ' tbody').empty();

	$('#grid-' + selector).setGridParam({
		page: page
	}).trigger("reloadGrid");
}

/* 그리드 데이터 없음 */
function setGridNodata(selector, text) {
	if (typeof text == 'undefined') {
		text = '데이터가 없습니다.';
	}

	var td = $('<td/>', {
		'role': 'gridcell',
		'style': 'text-align:center; padding: 5px 3px 5px 3px;',
		'class': 'jqgrid_cursor_pointer',
		'text': text
	})
	var tr = $('<tr/>', {
		'role': 'row',
		'id': 'jqgrow-nodata',
		'tabindex': '-1',
		'class': 'ui-widget-content jqgrow ui-row-ltr'
	});
	var tbody = $('<tbody/>');
	tr.append(td);
	tbody.append(tr);

	$('#grid-' + selector).html(tbody);
}

function checkGridNodata(selector, data) {
	if (!data.totalRows || !data.rows.length) {
		setGridNodata(selector);
		return false;
	} else {
		if ($('#grid-' + selector + ' #jqgrow-nodata').length) {
			$('#grid-' + selector + ' #jqgrow-nodata').detach();
		} else {
			var size = $('#grid-' + selector + ' tr[id=1]').length;
			if ($('#grid-' + selector + ' tr[id=1]').length > 1) {
				var tr = $('#grid-' + selector + ' tr[id=1]');
				$(tr[0]).detach();
			}
		}
		return true;
	}
}


$(function () {
	oCommon = new common();
	$(window).resize(function () {
		oCommon.jqGrid.resize();
	});
});

function common() {
	this.isEmpty = function (obj) {
		for (var key in obj) {
			if (obj.hasOwnProperty(key)) return false;
		}
		return true;
	};

	this.isIe = function () {
		if (navigator.appName == 'Microsoft Internet Explorer' || (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1)) {
			return true;
		}
		return false;
	};

	this.getUrlParameter = function (param) {
		var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL.split('&'),
			sParameterName, i;

		for (i = 0; i < sURLVariables.length; i++) {
			sParameterName = sURLVariables[i].split('=');
			if (sParameterName[0] === param) {
				return sParameterName[1] === undefined ? true : sParameterName[1];
			}
		}
	};

	this.getParameter = function (text, param) {
		var oSplit = text.split(','), sParameterName, i;

		for (i = 0; i < oSplit.length; i++) {
			sParameterName = oSplit[i].split('=');
			if (sParameterName[0] === param) {
				return sParameterName[1] === undefined ? true : sParameterName[1];
			}
		}
	};

	this.isOverlapElement = function ($elementOne, $elementTwo) {
		let offsetOne = $elementOne.offset(),
			offsetTwo = $elementTwo.offset(),
			topOne = offsetOne.top,
			topTwo = offsetTwo.top,
			leftOne = offsetOne.left,
			leftTwo = offsetTwo.left,
			widthOne = $elementOne.width(),
			widthTwo = $elementTwo.width(),
			heightOne = $elementOne.height(),
			heightTwo = $elementTwo.height();
		let leftTop = leftTwo > leftOne && leftTwo < leftOne + widthOne && topTwo > topOne && topTwo < topOne + heightOne,
			rightTop = leftTwo + widthTwo > leftOne && leftTwo + widthTwo < leftOne + widthOne && topTwo > topOne && topTwo < topOne + heightOne,
			leftBottom = leftTwo > leftOne && leftTwo < leftOne + widthOne && topTwo + heightTwo > topOne && topTwo + heightTwo < topOne + heightOne,
			rightBottom = leftTwo + widthTwo > leftOne && leftTwo + widthTwo < leftOne + widthOne && topTwo + heightTwo > topOne && topTwo + heightTwo < topOne + heightOne;
		return {
			leftTop: leftTop,
			rightTop: rightTop,
			leftBottom: leftBottom,
			rightBottom: rightBottom,
			overlap: (leftTop || rightTop || leftBottom || rightBottom)
		}
	};

	this.window = {
		open: function (url, windowName, windowFeatures) {
			var popupWidth = (typeof oConfigure != 'object') ? '1200' : oConfigure.popWidth;

			var paramWidth = oCommon.getParameter('width');
			if (!isNaN(paramWidth)) {
				popupWidth = paramWidth;
			}

			var popupHeight = (typeof oConfigure != 'object') ? '800' : oConfigure.popHeight;
			var paramHeight = oCommon.getParameter('height');
			if (!isNaN(paramHeight)) {
				popupHeight = paramHeight;
			}

			// 듀얼 모니터 기준
			var left = (screen.availWidth - popupWidth) / 2;
			if (window.screenLeft < 0) {
				left += window.screen.width * -1;
			} else if (window.screenLeft > window.screen.width) {
				left += window.screen.width;
			}

			var top = (screen.availHeight - popupHeight) / 2 - 10;
			if (typeof windowFeatures == 'undefined' || windowFeatures == '') {
				windowFeatures = 'left=' + left + ',top=' + top;
			} else {
				windowFeatures += ',left=' + left + ',top=' + top;
			}

			return window.open(url, windowName, windowFeatures);
		}
	};

	this.modalAlert = function (id, title, msg, callback) {
		var element = document.getElementById(id);
		if (!$(element).length) {
			var $dialog = $('<div/>', {
				'class': 'modal fade',
				'id': id,
				'tabindex': '-1',
				'role': 'dialog',
				'aria-labelledby': id + 'Label'
			});

			var $document = $('<div/>', {
				'class': 'modal-dialog',
				'role': 'document'
			});

			var $modalContent = $('<div/>', {
				'class': 'modal-content'
			});

			var $modalHeader = $('<div/>', {
				'class': 'modal-header'
			});

			var $btnClose = $('<button/>', {
				'type': 'button',
				'class': 'close',
				'data-dismiss': 'modal',
				'aria-label': 'Close',
				'html': '<span aria-hidden="true">&times;</span>'
			});

			var $modalTitle = $('<h5>/', {
				'class': 'modal-title',
				'id': id + 'Label',
				'text': title
			});

			var $modalBody = $('<div/>', {
				'class': 'modal-body',
				'id': id + 'Body',
				'html': '<h5>' + msg + '</h5>'
			});

			var $modalFooter = $('<div/>', {
				'class': 'modal-footer'
			});

			var $btnConfirm = $('<button/>', {
				'type': 'button',
				'class': 'btn btn-primary',
				'id': id + 'Confirm',
				'data-dismiss': 'modal',
				'text': '확인'
			});

			$document.appendTo($dialog);
			$modalContent.appendTo($document);
			$modalHeader.appendTo($modalContent);
			$modalTitle.appendTo($modalHeader);
			$modalBody.appendTo($modalContent);
			$modalFooter.appendTo($modalContent);
			if (id != 'modal-alert-no-button') {
				$btnClose.appendTo($modalHeader);
				$btnConfirm.appendTo($modalFooter);
			}

			$('body').append($dialog);

			$dialog.modal({
				backdrop: 'static',
				keyboard: false
			});
			$dialog.modal('show');
		} else {
			$('#' + id + 'Label').text(title);
			$('#' + id + 'Body').html('<h5>' + msg + '</h5>');
			$(element).modal('show');
		}

		if (typeof callback == 'function') {
			callback();
		}
	};

	this.modalConfirm = function (id, title, msg, callbackConfirm, callbackCancle) {
		var element = document.getElementById(id);
		if (!$(element).length) {
			var $dialog = $('<div/>', {
				'class': 'modal fade',
				'id': id,
				'tabindex': '-1',
				'role': 'dialog',
				'aria-labelledby': id + 'Label'
			});

			var $document = $('<div/>', {
				'class': 'modal-dialog',
				'role': 'document'
			});

			var $modalContent = $('<div/>', {
				'class': 'modal-content'
			});

			var $modalHeader = $('<div/>', {
				'class': 'modal-header'
			});

			var $btnClose = $('<button/>', {
				'type': 'button',
				'class': 'close',
				'data-dismiss': 'modal',
				'aria-label': 'Close',
				'html': '<span aria-hidden="true">&times;</span>'
			});

			var $modalTitle = $('<h5>/', {
				'class': 'margin-zero',
				'id': id + 'Label',
				'text': title
			});

			var $modalBody = $('<div/>', {
				'class': 'modal-body',
				'id': id + 'Body',
				'html': '<h5>' + msg + '</h5>'
			});

			var $modalFooter = $('<div/>', {
				'class': 'modal-footer'
			});

			var $btnCancle = $('<button/>', {
				'type': 'button',
				'class': 'btn btn-default btn-sm',
				'id': id + 'Cancle',
				'data-dismiss': 'modal',
				'text': '취소'
			});

			var $btnConfirm = $('<button/>', {
				'type': 'button',
				'class': 'btn btn-primary btn-sm',
				'id': id + 'Confirm',
				'data-dismiss': 'modal',
				'text': '확인'
			});

			if (typeof callbackConfirm == 'function') {
				$btnConfirm.on('click', function () {
					callbackConfirm();
				});
			}

			if (typeof callbackCancle == 'function') {
				$btnCancle.on('click', function () {
					callbackCancle();
				});
			}

			$document.appendTo($dialog);
			$modalContent.appendTo($document);
			$modalHeader.appendTo($modalContent);
			$btnClose.appendTo($modalHeader);
			$modalTitle.appendTo($modalHeader);
			$modalBody.appendTo($modalContent);
			$modalFooter.appendTo($modalContent);
			$btnCancle.appendTo($modalFooter);
			$btnConfirm.appendTo($modalFooter);

			$('body').append($dialog);

			$dialog.modal({
				backdrop: 'static',
				keyboard: false
			});
			$dialog.modal('show');
		} else {
			$('#' + id + 'Label').text(title);
			$('#' + id + 'Body').html('<h5>' + msg + '</h5>');
			$(element).modal('show');

			$('#' + id + 'Confirm').off('click');
			$('#' + id + 'Cancle').off('click');

			if (typeof callbackConfirm == 'function') {
				$('#' + id + 'Confirm').on('click', function () {
					callbackConfirm();
				});
			}

			if (typeof callbackCancle == 'function') {
				$('#' + id + 'Cancle').on('click', function () {
					callbackCancle();
				});
			}
		}
	};

	this.jqGrid = {
		// ex : oCommon.jqGrid.loadComplete(id, data, params)
		loadComplete: function (id, data, params) {
			oCommon.jqGrid.checkNodata(id, data);
			oCommon.jqGrid.pagenation(id, data, params);
			oCommon.jqGrid.resize(id);
		},
		// ex : oCommon.jqGrid.hideCol('out-rqst-dtl-file', 'outFileMasking');
		hideCol: function (selector, name) {
			if (selector) {
				$('#grid-' + selector).hideCol(name);
			} else {
				$('#grid').hideCol(name);
			}
		},
		// ex : oCommon.jqGrid.showCol('out-rqst-dtl-file', 'outFileMasking');
		showCol: function (selector, name) {
			if (selector) {
				$('#grid-' + selector).showCol(name);
			} else {
				$('#grid').showCol(name);
			}
		},
		// ex : oCommon.jqGrid.resize('out-rqst-dtl-file');
		resize: function (selector) {
			var $gboxes;
			if (typeof selector != 'undefined') {
				if ($('div[id="gbox_grid-' + selector + '"]').length) {
					$gboxes = $('div[id="gbox_grid-' + selector + '"]');
				} else if ($('div[id="gbox_grid_' + selector + '"]').length) {
					$gboxes = $('div[id="gbox_grid_' + selector + '"]');
				} else {
					$gboxes = $('div[id^="gbox_"]');
				}
			} else {
				$gboxes = $('div[id^="gbox_"]');
			}

			if ($gboxes.length) {
				$.each($gboxes, function (i, v) {
					var sId = $(v).attr('id');
					sId = sId.replace('gbox_', '');
					var $p = $(v).parent();
					var hasVerticalScrollbar = $p.get(0).scrollHeight > $p.get(0).clientHeight;
					var nWidth = $p.width();
					if (nWidth != 0) {
						if (typeof selector == 'undefined') {
							selector = sId.replace('grid-', '');
						} else if (sId != '') {
							selector = sId.replace('grid-', '');
						}
						var $paginate = $('#paginate-' + selector);
						if ($paginate.length) {
							nWidth = $paginate.width();
						}
						if (hasVerticalScrollbar) nWidth = nWidth - 18;
						var $grid = $('#' + sId);
						// console.log('==== oCommon.jqGrid.resize >>>>> %s %d %s', sId, nWidth, hasVerticalScrollbar);
						$grid.setGridWidth(nWidth);
						var oColNames = $grid.jqGrid('getGridParam', 'colNames');
						var $jqgfirstrow = $grid.find('.jqgfirstrow');
						var elTd = $jqgfirstrow.find('td');
						var $th = $('#gview_' + sId + ' tr.ui-jqgrid-labels th');
						$.each($th, function (i, v) {
							$(v).css('width', $(elTd[i]).css('width'));
						});
					}
				});
			}
		},
		resizeSpecified: function (selector) {
			const $gboxes = $('div[id="gbox_' + selector + '"]');
			if ($gboxes.length) {
				$.each($gboxes, function (i, v) {
					var sId = $(v).attr('id');
					sId = sId.replace('gbox_', '');
					var $p = $(v).parent();
					if ($p.width() != 0) {
						var $grid = $('#' + sId);
						$grid.setGridWidth($p.width());
						var oColNames = $grid.jqGrid('getGridParam', 'colNames');
						var $jqgfirstrow = $grid.find('.jqgfirstrow');
						var elTd = $jqgfirstrow.find('td');
						var $th = $('#gview_' + sId + ' tr.ui-jqgrid-labels th');
						$.each($th, function (i, v) {
							$(v).css('width', $(elTd[i]).css('width'));
						});
					}
				});
			}
		},
		pagenation: function (selector, data, params) {
			if ($('#paginate-' + selector).length) {
				$('#paginate-' + selector).empty();
				$('#paginate-' + selector).html($('<ul/>', {
					'id': 'pagination-' + selector,
					'class': 'paging pagination-sm'
				}));

				var nVisiblePages = 3;
				if (typeof params != 'undefined') {
					if (typeof params != 'undefined' && typeof params.visiblePages != 'undefined' && !isNaN(params.visiblePages)) {
						nVisiblePages = params.visiblePages;
					}
				}

				$('#pagination-' + selector).twbsPagination({
					startPage: isNaN(data.page) ? 1 : data.page,
					totalPages: isNaN(data.totalPages) ? 1 : data.totalPages,
					visiblePages: nVisiblePages,
					onPageClick: function (event, page) {
						oCommon.jqGrid.reload(selector, page);
					},
					first: '<i class="fas fa-angle-double-left"></i>',
					prev: '<i class="fas fa-angle-left"></i>',
					next: '<i class="fas fa-angle-right"></i>',
					last: '<i class="fas fa-angle-double-right"></i>'
				});
				$('#pagination-' + selector).data('page', isNaN(data.page) ? 1 : data.page);
			}
		},
		reload: function (selector, page, params) {
			var oGridParam = {
				page: page
			}
			if (typeof params != 'undefined') {
				oGridParam.postData = params;
			}
			$('#grid-' + selector).setGridParam(oGridParam).trigger('reloadGrid');
		},
		checkNodata: function (selector, data) {
			if (!data.hasOwnProperty('rows') || !data.rows.length) {
				oCommon.jqGrid.setNodata(selector, data.nodata);
				return false;
			}
			if (selector) {
				if ($('#grid-alert-' + selector).length) {
					$('#grid-alert-' + selector).hide();
					$('#gbox_grid-' + selector).find('.ui-jqgrid-bdiv').show();
				}
			} else {
				if ($('#grid-alert').length) {
					$('#grid-alert').hide();
					$('#gbox_grid').find('.ui-jqgrid-bdiv').show();
				}
			}
		},
		setNodata: function (selector, text) {
			if (typeof text == 'undefined') {
				text = '데이터가 없습니다.';
			}

			if (selector) {
				if ($('#grid-alert-' + selector).length) {
					$('#grid-alert-' + selector).show();
					$('#grid-alert-' + selector).text(text);
				} else {
					$('#gbox_grid-' + selector).after($('<div/>', {
						'id': 'grid-alert-' + selector,
						'class': 'alert alert-danger grid-alert',
						'role': 'alert',
						'text': text
					}));
				}
				$('#gbox_grid-' + selector).find('.ui-jqgrid-bdiv').hide();
			} else {
				if ($('#grid-alert').length) {
					$('#grid-alert').show();
					$('#grid-alert').text(text);
				} else {
					$('#gbox_grid').after($('<div/>', {
						'id': 'grid-alert',
						'class': 'alert alert-danger grid-alert',
						'role': 'alert',
						'text': text
					}));
				}
				$('#gbox_grid').find('.ui-jqgrid-bdiv').hide();
			}
		},
		clearGridData: function (selector, text) {
			$('#grid-' + selector).clearGridData();
			this.setNodata(selector, text);
			this.pagenation(selector, {}, {});
		},
		gridComplete: function (target) {
			const $uiJqgrid = $(target).closest('div.ui-jqgrid');
			if ($uiJqgrid.length) {
				const oParams = $(target).jqGrid('getGridParam');
				// onSelectRow 가 없을 경우 마우스커서 스타일 비활성화
				if (oParams.onSelectRow === null) $uiJqgrid.find('.ui-jqgrid-btable tr[role=row] td').css('cursor', 'default');

				// rownumbers: true 일 경우 Label을 No로 통일
				if (oParams.rownumbers) $(target).jqGrid('setLabel', 'rn', 'No');

				// cmTemplate : { sortable : false } 일 경우 마우스커서, 호버 스타일 비활성화
				if (oParams.cmTemplate.hasOwnProperty('sortable') && !oParams.cmTemplate.sortable) {
					let $th = $uiJqgrid.find('.ui-jqgrid-labels th.ui-th-column');
					$th.unbind('mouseenter').unbind('mouseleave');
					$th.find('div.ui-jqgrid-sortable').removeClass('ui-jqgrid-sortable');
				}
			}
		},
		/*
		 * multiselect: true,
		 * colModel select 가 있을 경우 사용한다.
		 */
		setSelectionOnChangeSelect: function (element, fn) {
			if (fn == 'loadComplete') {
				$(element).on('change', (event) => {
					let $grid = $(element);
					let $target = $(event.target);
					if ($target.prop('nodeName') == 'SELECT') {
						let $tr = $target.closest('tr');
						let sAriaSelected = $tr.attr('aria-selected') || 'false';
						// 기존 값과 선택된 값이 다르고 row 의 checkbox 가 false 일 경우
						if ($target.data('selected') != $target.find('option:selected').val() && sAriaSelected == 'false') {
							$grid.setSelection($tr.attr('id'), false);
						} else {
							// 모든 select 값이 기존과 같고 row 의 checkbox 가 true 일 경우
							let isChange = false;
							let $select = $tr.find('select');
							for (let elSelect of $select) {
								if ($(elSelect).data('selected') != $(elSelect).find('option:selected').val()) isChange = true;
							}
							if (!isChange && sAriaSelected == 'true') $grid.setSelection($tr.attr('id'), false);
						}
					}
				});
			} else if (fn == 'beforeProcessing') {
				$(element).off('change');
			}
		},
		preventSetSelectionOnSelectRow: function (element, rowid, event) {
			if (!$(event.target).closest('td').length) return false;
			const $grid = $(element),
				nIndex = $.jgrid.getCellIndex($(event.target).closest('td')[0]),
				oColModel = $grid.jqGrid('getGridParam', 'colModel');
			return (oColModel[nIndex].name === 'cb');
		},
	};

	this.notFoundImage = function (element) {
		var $div = $(element).closest('div.col-xs-12');
		$div.html('<div class="alert alert-danger" role="alert">이미지를 찾을 수 없습니다.</div>');
	};

	this.popupImage = function (url) {
		let nHeight = 500; // $('#wrapper').height() || 500;
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_INFO,
			title: '이미지',
			message: $('<div class="text-center" id="displayOption"><img id="popup-image" src="' + url + '" style="min-width: 200px;max-width: 578px; max-height: ' + nHeight + 'px" /></div>')
		});
	};

	this.panelSimple = function (title, body, footer) {
		var $panel = $('<div/>', {
			'class': 'panel panel-default panel-simple',
		});

		var $panelHeading = $('<div/>', {
			'class': 'panel-heading'
		});

		var $panelTitle = $('<h6/>', {
			'class': 'panel-title',
			'text': title
		});

		var $panelBoby = $('<div/>', {
			'class': 'panel-body',
			'html': body
		});

		$panelHeading.append($panelTitle);
		$panel.append($panelHeading);
		$panel.append($panelBoby);

		if (typeof footer != 'undefined') {
			var $panelFooter = $('<div/>', {
				'class': 'panel-footer',
				'html': footer
			});
			$panel.append($panelFooter);
		}

		return $panel.prop('outerHTML');
	};

	this.validate = {
		fromTo: function (from, to, format) {
			let isValid = false;
			if (!format) format = 'YYYY-MM-DDTHH:mm';
			if (from == '' && to == '') {
				isValid = true;
			} else {
				let momentFr = new moment(from, format);
				let momentTo = new moment(to, format);
				if (momentFr.isValid() && momentTo.isValid()) {
					let oDuration = moment.duration(momentFr.diff(momentTo));
					if (oDuration.asMilliseconds() < 0) {
						isValid = true;
					}
				}
			}
			return isValid;
		},

		fromToMoment: function (momentFr, momentTo) {
			var isValid = false;
			if (momentFr.isValid() && momentTo.isValid()) {
				var oDuration = moment.duration(momentFr.diff(momentTo));
				if (oDuration.asMilliseconds() < 0) isValid = true;
			}
			return isValid;
		},

		isMobile: function (phoneNum) {
			if (typeof phoneNum === 'string') phoneNum = phoneNum.replaceAll('-', '');
			var regExp = /(01[016789])([1-9]{1}[0-9]{2,3})([0-9]{4})$/;
			var myArray;
			if (regExp.test(phoneNum)) {
				myArray = regExp.exec(phoneNum);
				return true;
			} else {
				return false;
			}
		},

		ip: function (ipAddress) {
			let regExp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
			var myArray;
			if (regExp.test(ipAddress)) {
				myArray = regExp.exec(ipAddress);
				return true;
			} else {
				return false;
			}
		},

		// 앞으로 계속 추가할 예정
		form: function (selector, isClear) {
			let $target = $(selector);
			let sMsg = '잘못된 요청입니다.';
			if ($target.length) {
				sMsg = '';
				// 에러 초기화
				for (let element of $target) {
					let $formGroup = $(element).closest('.form-group');
					if ($formGroup.hasClass('has-error')) $formGroup.removeClass('has-error');
				}

				if (isClear) return '';

				for (let element of $target) {
					let $element = $(element);
					let isReadOnly = element.readOnly;
					if (isReadOnly) element.readOnly = false;

					let isHidden = element.type === 'hidden' ? true : false;
					if (isHidden) {
						if (!$element.hasClass('hide')) $element.addClass('hide');
						$element.attr('type', 'text');
					}
					// 공백문자 처리
					let sValue = $element.val().trim();
					$element.val(sValue);

					let isTel = element.type === 'tel' ? true : false;
					if (isTel) {
						let isMobile = oCommon.validate.isMobile(element.value);
						if (!isMobile) {
							$element.closest('.form-group').addClass('has-error');
							sMsg += element.title + ' : 휴대전화번호 형식에 맞게 입력하세요.<br>';
						}
					}

					if (!element.validity.valid && element.type !== 'tel') {
						$element.closest('.form-group').addClass('has-error');
						sMsg += element.title + ' : ' + oCommon.validate.getMessage(element) + '<br>';
					}

					if (element.hasAttribute('min') && element.hasAttribute('required')) {
						if (sValue == '' || isNaN(sValue)) {
							$element.closest('.form-group').addClass('has-error');
							sMsg += element.title + ' : 숫자 형식에 맞게 입력하세요.<br>';
						} else {
							let nMin = element.getAttribute('min');
							if (parseFloat(sValue) < nMin) {
								$element.closest('.form-group').addClass('has-error');
								sMsg += element.title + ' : ' + nMin + '이상의 값을 입력하세요.<br>';
							}
						}
					}

					if (element.hasAttribute('max') && element.hasAttribute('required')) {
						if (sValue == '' || isNaN(sValue)) {
							$element.closest('.form-group').addClass('has-error');
							sMsg += element.title + ' : 숫자 형식에 맞게 입력하세요.<br>';
						} else {
							let nMax = element.getAttribute('max');
							if (parseFloat(sValue) > nMax) {
								$element.closest('.form-group').addClass('has-error');
								sMsg += element.title + ' : ' + nMax + '이하의 값을 입력하세요.<br>';
							}
						}
					}

					if (isReadOnly) element.readOnly = true;
					if (isHidden) $element.attr('type', 'hidden');
				}
			}
			return sMsg;
		},
		/*
			const files = event.target.files;
			const nSize = 5;
			let oData = oCommon.validate.file('IMAGE,PDF,HANCOM_OFFICE,MS_OFFICE', nSize, files[0]);
		 */
		file: function (types, size, file) {
			const oMimeTypesMap = {
				IMAGE: {
					name: '이미지',
					type: ['image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/webp', 'image/svg+xml'],
					extension: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg']
				},
				PDF: {
					name: 'PDF',
					type: ['application/pdf'],
					extension: ['pdf']
				},
				HANCOM_OFFICE: {
					name: '한컴오피스',
					type: ['application/vnd.hancom.hwp', 'application/haansofthwp', 'application/x-hwp', 'application/vnd.hancom.hwpx', 'application/haansofthwpx'],
					extension: ['hwp', 'hwpx']
				},
				WORD: {
					name: '워드',
					type: ['application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'],
					extension: ['doc', 'docx']
				},
				EXCEL: {
					name: '엑셀',
					type: ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'],
					extension: ['xls', 'xlsx']
				},
				POWER_POINT: {
					name: '파워포인트',
					type: ['application/vnd.ms-powerpoint', 'application/vnd.openxmlformats-officedocument.presentationml.presentation'],
					extension: ['ppt', 'pptx']
				},
				MS_OFFICE: {
					name: 'MS오피스',
					type: [],
					extension: []
				}
				// 추가 가능
			};

			// TODO file.type 검출 안될 경우 확장자명 비교도 할지 검토
			oMimeTypesMap['MS_OFFICE'].type = oMimeTypesMap['WORD'].type.concat(oMimeTypesMap['EXCEL'].type).concat(oMimeTypesMap['POWER_POINT'].type);
			oMimeTypesMap['MS_OFFICE'].extension = oMimeTypesMap['WORD'].extension.concat(oMimeTypesMap['EXCEL'].extension).concat(oMimeTypesMap['POWER_POINT'].extension);

			let oAcceptedMimeTypes = [];
			let oAcceptedMimeExtenstions = [];
			let oAcceptedMimeNames = [];

			//console.log ("file.type = "+file.type);
			//console.log ("file.name = "+file.name);
			let ind = file.name.lastIndexOf('.');
			let fileExtension = file.name.substring(ind + 1);
			//console.log ("file.ext  = "+fileExtension);


			if (typeof types !== 'undefined' && types.length) {

				for (let accept of types.split(',')) {

					let oMap = oMimeTypesMap[accept] || {type: [], extension: [], name: []};

					oAcceptedMimeTypes = oAcceptedMimeTypes.concat(oMap.type);
					oAcceptedMimeExtenstions = oAcceptedMimeExtenstions.concat(oMap.extension);
					oAcceptedMimeNames.push(oMap.name);
				}
				/*
				return {
					name: oAcceptedMimeNames,
					accept: file && oAcceptedMimeTypes.includes(file.type),
					size: size == 0 ? true : (file.size < 1024 * 1024 * size),
					msg: '',
				};
				*/
				//console.log ("oAcceptedMimeTypes  = "+oAcceptedMimeTypes);
				//console.log ("oAcceptedMimeExtenstions  = "+oAcceptedMimeExtenstions);

				let flag = file && oAcceptedMimeTypes.includes(file.type);					//console.log ("유형 체크");
				if (flag) {
					return {
						name: oAcceptedMimeNames,
						accept: file && oAcceptedMimeTypes.includes(file.type),
						size: size == 0 ? true : (file.size < 1024 * 1024 * size),
						msg: '',
					};
				} else {
					let flag = file && oAcceptedMimeExtenstions.includes(fileExtension);	//console.log ("확장자 체크");
					return {
						name: oAcceptedMimeNames,
						accept: file && oAcceptedMimeExtenstions.includes(fileExtension),
						size: size == 0 ? true : (file.size < 1024 * 1024 * size),
						msg: '',
					};
				}


			} else {
				return {
					name: [],
					accept: false,
					size: false,
					msg: '잘못된 파일형식의 요청',
				};
			}
		},

		validityMessage: {
			badInput: '잘못된 입력입니다.',
			patternMismatch: '패턴에 맞게 입력하세요.',
			rangeOverflow: '범위를 초과하였습니다.',
			rangeUnderflow: '범위에 미달하였습니다.',
			stepMismatch: '간격에 맞게 입력하세요.',
			tooLong: '최대 글자 미만으로 입력하세요.',
			tooShort: '최소 글자 미만으로 입력하세요.',
			typeMismatch: '형식에 맞게 입력하세요.',
			valueMissing: '이 필드를 반드시 입력하세요.',
		},

		getMessage: function (element) {
			for (const key in this.validityMessage) {
				if (element.validity[key]) {
					let sMsg = this.validityMessage[key];
					if (key === 'tooLong') {
						sMsg += ' 최대길이[' + $(element).attr('maxlength') + ']';
					} else if (key === 'tooShort') {
						sMsg += ' 최소길이[' + $(element).attr('minlength') + ']';
					} else if (key === 'rangeOverflow') {
						sMsg += ' 최대값[' + $(element).attr('max') + ']';
					} else if (key === 'rangeUnderflow') {
						sMsg += ' 최소값[' + $(element).attr('min') + ']';
					}
					return sMsg;
				}
			}
		}
	};

	/**
	 * @description 문자열 바이트 길이 체크
	 * @param {string}
	 *			s 문자
	 * @return {number} 길이
	 */
	this.getByteLength = function (s, b, i, c) {
		for (b = i = 0; c = s.charCodeAt(i++); b += c >> 11 ? 3 : c >> 7 ? 2 : 1)
			;
		return b;
	};

	/**
	 * @description 문자열 바이트 길이(mb) 만큼 잘라준다.
	 * @param {string}
	 *			s 문자
	 * @param {number}
	 *			mb
	 * @return {string}
	 */
	this.cutByteLenth = function (s, mb, b, i, c) {
		for (b = i = 0; c = s.charCodeAt(i);) {
			b += c >> 7 ? 2 : 1;
			if (b > mb) break;
			i++;
		}
		return s.substring(0, i);
	};

	/**
	 * @description 휴대전화 양식 변환 01011112222 > 010-1111-2222
	 * @param {string}
	 * @return {string}
	 */
	this.tellnum = {
		mobile: {
			addHyphens: function (str) {
				if (typeof str == 'string') {
					str = str.replace(/[^0-9]/g, '');
					var tmp = '';
					if (str.length < 4) {
						return str;
					} else if (str.length < 7) {
						tmp += str.substr(0, 3);
						tmp += '-';
						tmp += str.substr(3);
						return tmp;
					} else if (str.length < 11) {
						tmp += str.substr(0, 3);
						tmp += '-';
						tmp += str.substr(3, 3);
						tmp += '-';
						tmp += str.substr(6);
						return tmp;
					} else {
						tmp += str.substr(0, 3);
						tmp += '-';
						tmp += str.substr(3, 4);
						tmp += '-';
						tmp += str.substr(7);
						return tmp;
					}
					return str;
				}
			}
		}
	};

	this.formatter = {
		numberWithCommas: function (str) {
			return str.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		},
		ymdHms: function (ymdhms, format) {
			const m = moment(ymdhms, 'YYYYMMDDHHmmss');
			let r = '';
			if (m.isValid()) {
				if (format) {
					r = m.format(format);
				} else {
					r = m.format('YYYY-MM-DD HH:mm:ss');
				}
			}
			return r;
		}
	};

	this.color = {
		/**
		 * @description css <named-color> => hex code
		 * @param {string}
		 * @return {string}
		 */
		toHex: (color) => {
			const ctx = document.createElement('canvas').getContext('2d');
			ctx.fillStyle = color;
			return ctx.fillStyle;
		},
		/**
		 * @description background color 에 어울리는 font color 선정
		 * @param {string}
		 * @return {string}
		 */
		getFontColor: (hex) => {
			const c = hex.substring(1);
			const rgb = parseInt(c, 16);
			const r = (rgb >> 16) & 0xff;
			const g = (rgb >> 8) & 0xff;
			const b = (rgb >> 0) & 0xff;
			const luma = 0.2126 * r + 0.7152 * g + 0.0722 * b; // per ITU-R BT.709
			// 색상 선택
			return luma < 127.5 ? '#ffffff' : '#000000';
		}
	}
}
