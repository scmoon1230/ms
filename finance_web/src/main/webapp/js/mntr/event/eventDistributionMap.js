$(function() {
	oEventDistributionMap = new eventDistributionMap();
	oEventDistributionMap.init();

	if (typeof Object.assign != 'function') {
		Object.assign = function(target) {
			'use strict';
			if (target == null) {
				throw new TypeError('Cannot convert undefined or null to object');
			}

			target = Object(target);
			for (var index = 1; index < arguments.length; index++) {
				var source = arguments[index];
				if (source != null) {
					for ( var key in source) {
						if (Object.prototype.hasOwnProperty.call(source, key)) {
							target[key] = source[key];
						}
					}
				}
			}
			return target;
		};
	}
	// 검색 조건 보이기 / 숨기기
	$('input[name="rdo-search-type"]').on('change', function() {
		var sRdoSearchType = $(this).val();
		if ('year' == sRdoSearchType) {
			$('#sel-year').closest('div.form-group').show();
			$('#sel-month').closest('div.form-group').hide();
			$('.datetimepicker').hide();
		}
		else if ('month' == sRdoSearchType) {
			$('#sel-year').closest('div.form-group').show();
			$('#sel-month').closest('div.form-group').show();
			$('.datetimepicker').hide();
		}
		else if ('period' == sRdoSearchType) {
			$('#sel-year').closest('div.form-group').hide();
			$('#sel-month').closest('div.form-group').hide();
			$('.datetimepicker').show();
		}
	});
	// Datetimepicker From
	$('#search-picker-from').datetimepicker({
		format: 'YYYY-MM',
		locale: 'ko',
		minDate: '2010-01',
		maxDate: 'now'
	});
	// Datetimepicker To
	$('#search-picker-to').datetimepicker({
		format: 'YYYY-MM',
		locale: 'ko',
		minDate: '2010-01',
		maxDate: 'now'
	});
	// Datetimepicker 팝업 가려지는 현상 해결.
	$('#search-picker-to').on('dp.show', function(e) {
		$('.bootstrap-datetimepicker-widget.dropdown-menu').css({
			position: 'fixed',
			top: '256px',
			left: '160px'
		});
	});
	// 시군구 읍면동 둘다 선택하지 못하도록 설정.
	$('#searchSigunguCd').on('change', function() {
		$('#searchLgDongCd').val('');
	});
	// 시군구 읍면동 둘다 선택하지 못하도록 설정.
	$('#searchLgDongCd').on('change', function() {
		$('#searchSigunguCd').val('');
	});
	// 조회
	$('#btn-search').on('click', function() {
		oEventDistributionMap.search();
	});
	// check all
	$('#chk-evt-ids-all').on('change', function() {
		var isChecked = $(this).is(':checked');
		if (!isChecked) {
			$('input[name="chk-evt-ids"]').prop('checked', false);
		}
		else {
			$('input[name="chk-evt-ids"]').prop('checked', true);
		}
	});
	// 시스템 선택
	$('#sel-system').on('change', function() {
		var sVal = $(this).val();
		var $tbodyEventTr = $('#tbody-event tr');
		var $tbodyEventTrSel = $('#tbody-event tr[data-sys-cd="' + sVal + '"]');
		if ('ALL' != sVal) {
			$tbodyEventTr.hide();
			$tbodyEventTrSel.show();
		}
		else {
			$tbodyEventTr.show();
		}
	});
});

function eventDistributionMap() {
	this.init = function() {
		/* 1. 이벤트레이어 생성 */
		olEventLayer = new OpenLayers.Layer.Vector("eventLayer", {
			projection: new OpenLayers.Projection(oGis.projection),
			strategies: [ new OpenLayers.Strategy.Cluster({
				distance: 0
			}), new OpenLayers.Strategy.BBOX({
				resFactor: 1
			}) ],
			protocol: new OpenLayers.Protocol.HTTP({
				url:contextRoot + '/mntr/eventDistributionGeoData.json',
				params: {
					searchEventIds: [],
					searchSigunguCd: '',
					searchLgDongCd: '',
					searchSigunguNm: '',
					searchLgDongNm: '',
					searchTy: '',
					searchYmd: '',
					searchYmdTo: '',
					searchMinX: '',
					searchMinY: '',
					searchMaxX: '',
					searchMaxY: ''
				},
				readWithPOST: true,
				format: new OpenLayers.Format.GeoJSON()
			})
		});
		olEventLayer.setVisibility(true);
		_map.addLayer(olEventLayer);
		/* 2. 시설레이어 로딩이 끝나면 범례 생성 */
		olEventLayer.events.on({
			'loadend': function() {
				var $checkedEvent = $('input[name="chk-evt-ids"]:checked');
				var sSelectedSystem = $('#sel-system option:selected').val();
				var $ul = $('<ul/>');
				$.each($checkedEvent, function(i, v) {
					var oEventData = $(v).data();
					var oFeatures = olEventLayer.getFeaturesByAttribute('evtId', oEventData.evtId);
					var $li = $('<li/>');
					var $img = $('<img/>', {
						'src': contextRoot + '/images/mntr/gis/event/type' + oConfigure.iconTy + '/' + oEventData.evtId + '.png',
						'alt': oEventData.evtNm + '(' + oEventData.evtId + ')',
						'title': oEventData.evtNm + '(' + oEventData.evtId + ')',
						'width': '20px',
						'height': '20px'
					});

					if (sSelectedSystem == oEventData.sysCd) {
						$li.append($img);
						$li.append(' ' + oEventData.evtNm + ': ' + oFeatures.length);
						$ul.append($li);
					}
					else if (sSelectedSystem == 'ALL') {
						$li.append($img);
						$li.append(' ' + oEventData.evtNm + ': ' + oFeatures.length);
						$ul.append($li);
					}
				});
				$('#remarks').empty();
				var $searchSigunguCd = $('#searchSigunguCd option:selected');
				var $searchLgDongCd = $('#searchLgDongCd option:selected');
				var sLabel = '';
				if ($searchSigunguCd.val() != '') {
					sLabel = $searchSigunguCd.text();
				}

				if ($searchLgDongCd.val() != '') {
					sLabel = $searchLgDongCd.text();
				}

				var $h6 = $('<h6/>', {
					'class': 'text-center'
				});
				var sRdoSearchType = $('input[name="rdo-search-type"]:checked').val();
				if(sRdoSearchType == 'year') {
					sLabel = sLabel + '(년도별)';
					var sSelectedYear = $('#sel-year option:selected').val();
					$h6.text(sSelectedYear + '년');
				}
				else if(sRdoSearchType == 'month') {
					sLabel = sLabel + '(월별)';
					var sSelectedYear = $('#sel-year option:selected').val();
					var sSelectedMonth = $('#sel-month option:selected').val();
					$h6.text(sSelectedYear + '년 ' + sSelectedMonth + '월');
				}
				else if(sRdoSearchType == 'period') {
					sLabel = sLabel + '(기간별)';
					var sInputPickerFrom = $('#input-picker-from').val();
					var sInputPickerTo = $('#input-picker-to').val();
					$h6.text(sInputPickerFrom + ' ~ ' + sInputPickerTo);
				}

				$('#remarks').append($('<h6/>', {
					'text': sLabel,
					'class': 'text-center'
				}));

				$('#remarks').append($('<h6/>', {
					'text': $('#sel-system option:selected').text(),
					'class': 'text-center'
				}));

				$('#remarks').append($h6);
				$('#remarks').append($ul);
			}
		});

		var $remarks = $('<div/>', {
			'id': 'remarks'
		});

		$('#wrapperMap').append($remarks);

		/* 3. 시군구 검색조건 생성 - emdYn: 'N' 옵션 */
		$.ajax({
			type: 'POST',
			dataType: 'json',
			url:contextRoot + '/mntr/sggEmdBoundList.json',
			data: {
				emdYn: 'N'
			},
			success: function(data, status, jqXHR) {
				var oList = data.list;
				if (oList.length) {
					if (oList.length != 1) {
						var oSigunguNm = oList[0].sigunguNm.split(' ');
						var sSigunguNm = oList[0].sigunguNm;
						if (oSigunguNm.length != 1) {
							sSigunguNm = oSigunguNm[0];
						}

						$('#searchSigunguCd').append($('<option/>', {
							'value': oConfigure.sigunguCd,
							'text': sSigunguNm + ' 전체',
							'data-min-x': oGis.boundsLeft,
							'data-min-y': oGis.boundsBottom,
							'data-max-x': oGis.boundsRight,
							'data-max-y': oGis.boundsTop
						}));

						$.each(oList, function(i, v) {
							$('#searchSigunguCd').append($('<option/>', {
								'value': v.sigunguCd,
								'text': v.sigunguNm,
								'data-min-x': v.minX,
								'data-min-y': v.minY,
								'data-max-x': v.maxX,
								'data-max-y': v.maxY
							}));
						});
					}
					else {
						$('#searchSigunguCd').append($('<option/>', {
							'value': oList[0].sigunguCd,
							'text': oList[0].sigunguNm + ' 전체',
							'data-min-x': oList[0].minX,
							'data-min-y': oList[0].minY,
							'data-max-x': oList[0].maxX,
							'data-max-y': oList[0].maxY
						}));
					}
				}
			},
			error: function(jqXHR, status, error) {
				console.log(error);
			}
		});
		/* 3. 시군구 검색조건 생성 - emdYn: 'Y' 옵션 */
		$.ajax({
			type: 'POST',
			dataType: 'json',
			url:contextRoot + '/mntr/sggEmdBoundList.json',
			data: {
				emdYn: 'Y'
			},
			success: function(data, status, jqXHR) {
				var oList = data.list;
				if (oList.length) {
					$.each(oList, function(i, v) {
						var oSigunguNm = v.sigunguNm.split(' ');
						var sLgEmdNm = v.lgEmdNm;
						if (oSigunguNm.length != 1) {
							sLgEmdNm = oSigunguNm[1] + ' ' + sLgEmdNm;
						}

						$('#searchLgDongCd').append($('<option/>', {
							'value': v.lgDongCd,
							'text': sLgEmdNm,
							'data-min-x': v.minX,
							'data-min-y': v.minY,
							'data-max-x': v.maxX,
							'data-max-y': v.maxY
						}));
					});
				}
			},
			error: function(jqXHR, status, error) {
				console.log(error);
			}
		});

		$.ajax({
			type: 'POST',
			url:contextRoot + '/mntr/eventList.json',
			async: false,
			success: function(data, status, jqXHR) {
				var oEventList = data.event;
				var oSystemList = data.system;

				$selSystem = $('#sel-system');
				$.each(oSystemList, function(i, v) {
					$selSystem.append($('<option/>', {
						'value': v.sysCd,
						'text': v.sysNm
					}));
				});

				var $tr = $('<tr/>');
				var $th = $('<th/>', {
					'scope': 'row'
				});
				var $td = $('<td/>');
				var $label = $('<label/>');
				var $input = $('<input/>', {
					'type': 'checkbox',
					'name': 'chk-evt-ids',
				//	'checked': 'checked',
					'value': '',
					'aria-label': '',
				});

				var $tbodyFclt = $('#tbody-event');
				$.each(oEventList, function(i, v) {
					var sSysNm = v.sysNm;
					var sSysCd = v.sysCd;
					var sEvtNm = v.evtNm;
					var sEvtId = v.evtId;

					var $trC = $tr.clone().attr('data-sys-cd', sSysCd);
					var $inputC = $input.clone().attr({
						'id': 'chk-' + sEvtId,
						'value': sEvtId,
						'aria-label': sEvtId,
						'data-evt-id': sEvtId,
						'data-evt-nm': sEvtNm,
						'data-sys-cd': sSysCd,
						'data-sys-nm': sSysNm
					});
					var $thC = $th.clone().append($label.clone().append($inputC));
					var $img = $('<img/>', {
						'src': contextRoot + '/images/mntr/gis/event/type' + oConfigure.iconTy + '/' + sEvtId + '.png',
						'alt': sEvtNm + '(' + sEvtId + ')',
						'title': sEvtNm + '(' + sEvtId + ')',
						'width': '20px',
						'height': '20px'
					});
					$trC.append($thC);
					$trC.append($td.clone().text(sSysNm));
					$trC.append($td.clone().text(sEvtNm));
					$trC.append($td.clone().append($img));
					$tbodyFclt.append($trC);
				})
			},
			error: function(jqXHR, status, error) {
				console.log(error);
			}
		});
	};

	this.search = function() {
		var $searchSigunguCd = $('#searchSigunguCd option:selected');
		var $searchLgDongCd = $('#searchLgDongCd option:selected');
		var oData = {};

		if ($searchSigunguCd.val() != '') {
			oData = $searchSigunguCd.data();
		}
		else if ($searchLgDongCd.val() != '') {
			oData = $searchLgDongCd.data();
		}
		else {
			alert('시군구 또는 읍명동 을(를) 선택해 주세요.');
			return false;
		}

		var style = {
			strokeColor: "#000",
			strokeOpacity: 0.1,
			strokeWidth: 1,
			fillOpacity: 0
		};

		var nRevision = 0.003;
		var nLeft = parseFloat(oData.minX) - nRevision;
		var nTop = parseFloat(oData.maxY) + nRevision;
		var nRight = parseFloat(oData.maxX) + nRevision;
		var nBottom = parseFloat(oData.minY) - nRevision;

		var olBounds = new OpenLayers.Bounds(nLeft, nTop, nRight, nBottom);
		olBounds.transform(new OpenLayers.Projection('EPSG:4326'), new OpenLayers.Projection(oGis.projection));
		_map.zoomToExtent(olBounds);

		var p1 = new OpenLayers.Geometry.Point(olBounds.left, olBounds.top);
		var p2 = new OpenLayers.Geometry.Point(olBounds.right, olBounds.top);
		var p3 = new OpenLayers.Geometry.Point(olBounds.right, olBounds.bottom);
		var p4 = new OpenLayers.Geometry.Point(olBounds.left, olBounds.bottom);

		var pnt = [];
		pnt.push(p1, p2, p3, p4);

		var ln = new OpenLayers.Geometry.LinearRing(pnt);
		var pf = new OpenLayers.Feature.Vector(ln, null, style);
		pointLayer.removeAllFeatures();
		pointLayer.addFeatures([ pf ]);

		var $checkedEvent = $('input[name="chk-evt-ids"]:checked');
		var sSelectedSystem = $('#sel-system option:selected').val();
		var oSearchEventIds = [];
		$.each($checkedEvent, function(i, v) {
			var oEventData = $(v).data();
			if (sSelectedSystem == oEventData.sysCd) {
				oSearchEventIds.push($(v).val());
			}
			else if (sSelectedSystem == 'ALL') {
				oSearchEventIds.push($(v).val());
			}
		});

		if(!oSearchEventIds.length) {
			alert('선택된 이벤트가 없습니다.');
			return false;
		}

		olEventLayer.protocol.params.searchMinX = nLeft;
		olEventLayer.protocol.params.searchMaxY = nTop;
		olEventLayer.protocol.params.searchMaxX = nRight;
		olEventLayer.protocol.params.searchMinY = nBottom;
		olEventLayer.protocol.params.searchEventIds = oSearchEventIds;
		olEventLayer.protocol.params.searchSigunguCd = $searchSigunguCd.val();
		if($searchSigunguCd.val() != '') {
			olEventLayer.protocol.params.searchSigunguNm = $searchSigunguCd.text();
		}
		olEventLayer.protocol.params.searchLgDongCd = $searchLgDongCd.val();
		if($searchLgDongCd.val() != '') {
			olEventLayer.protocol.params.searchLgDongNm = $searchLgDongCd.text();
		}
		var sRdoSearchType = $('input[name="rdo-search-type"]:checked').val();
		olEventLayer.protocol.params.searchTy = sRdoSearchType;
		if(sRdoSearchType == 'year') {
			var sSelectedYear = $('#sel-year option:selected').val();
			olEventLayer.protocol.params.searchYmd = sSelectedYear;
		}
		else if(sRdoSearchType == 'month') {
			var sSelectedYear = $('#sel-year option:selected').val();
			var sSelectedMonth = $('#sel-month option:selected').val();
			if(parseInt(sSelectedMonth) < 10) {
				sSelectedMonth = '0' + sSelectedMonth;
			}
			olEventLayer.protocol.params.searchYmd = sSelectedYear + '' + sSelectedMonth;
		}
		else if(sRdoSearchType == 'period') {
			var sInputPickerFrom = $('#input-picker-from').val();
			var sInputPickerTo = $('#input-picker-to').val();

			var oInputPickerFrom = moment(sInputPickerFrom, 'YYYY-MM');
			if(oInputPickerFrom.isValid()) {
				olEventLayer.protocol.params.searchYmd = oInputPickerFrom.format('YYYYMM');
			}
			else {
				alert('잘못된 날짜 형식입니다. YYYY-MM 형식으로 입력해주세요.');
				return false;
			}

			var oInputPickerTo = moment(sInputPickerTo, 'YYYY-MM');
			if(oInputPickerTo.isValid()) {
				olEventLayer.protocol.params.searchYmdTo = oInputPickerTo.format('YYYYMM');
			}
			else {
				alert('잘못된 날짜 형식입니다. YYYY-MM 형식으로 입력해주세요.');
				return false;
			}
		}

		var nIconSize = parseInt($('#sel-icon-size option:selected').val());
		var newStyle = new OpenLayers.Style({
			cursor: 'pointer',
			externalGraphic: '${getIcon}',
			pointRadius: nIconSize,
			graphicOpacity: 0.7,
			graphicXOffset: '${getXOffset}',
			graphicYOffset: '${getYOffset}',
			graphicWidth: nIconSize,
			graphicHeight: nIconSize
		}, {
			context: {
				getIcon: function(f) {
					var sPrefix = 'event/type' + oConfigure.iconTy + '/';
					var sSuffix = '.png';
					var markImage = sPrefix + f.attributes.evtId + sSuffix;
					return getMarkImage(markImage);
				},
				getXOffset: function(f) {
					var nSize = 0;

					return nSize;
				},
				getYOffset: function(f) {
					var nSize = 0;

					return nSize;
				}
			}
		});

		var olStyle = OpenLayers.Util.applyDefaults(newStyle, OpenLayers.Feature.Vector.style["default"]);
		var olStyleMap = new OpenLayers.StyleMap({
			'default': olStyle
		});

		olEventLayer.styleMap = olStyleMap;
		// olEventLayer.redraw();
		olEventLayer.refresh({ force : true });
	};
}