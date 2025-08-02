$(function() {
	oFcltDetail = new fcltDetail();
	oFcltDetail.init();
});

function fcltDetail() {
	this.init = function() {
		// LEFT 조절
		oConfigure.mntrViewLeft = 700;
		$('aside#left').css('width', oConfigure.mntrViewLeft);
		$('section#body').css('left', oConfigure.mntrViewLeft + 10);
		$('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			left : false
		}, function() {
			if (oGis.olVersion == 5) {
				// 1. BASE, SATELLITE 배경지도 생성.
				olSwipMap.init({
					dropdown : true,
					bookmark : false
				});

				// 2. 시설물 레이어 설정.
				olSwipMap.layers.fclt.init();

				// 4. Feature 핸들러.
				function handler(event) {
					var oOptOptions = {
						hitTolerance : 10
					};

					var oFeaturesFclt = [];
					olMap.map.forEachFeatureAtPixel(event.pixel, function(feature, layer) {
						if (layer != null) {
							if (olMap.layers.fclt === layer) {
								var olFeatures = feature.get('features');
								if (typeof olFeatures == 'undefined') {
									oFeaturesFclt.push(feature.getProperties());
								} else {
									$.each(olFeatures, function(i, f) {
										oFeaturesFclt.push(f.getProperties());
									});
								}
							}
						}
					}, oOptOptions);

					var olOverlay = olMap.map.getOverlayById('ol-overlay-' + event.type);
					olOverlay.setPosition(event.coordinate);

					if (event.type == 'pointermove') {
						var oPosition = olSwipMap.overlays.click.getPosition();
						if (typeof oPosition != 'undefined') {
							return false;
						}
					}

					var $olOverlay = $('#ol-overlay-' + event.type);

					if (oFeaturesFclt.length) {
						// title
						var elPanelHeadingTitle = $olOverlay.find('.panel-heading-title');
						if (elPanelHeadingTitle.length == 1) {
							var $panelHeadingTitle = $(elPanelHeadingTitle[0]);
							if (oFeaturesFclt.length) {
								$panelHeadingTitle.text('시설물(' + oFeaturesFclt.length + ')');
							}
						}
						oFeaturesFclt.sort(function(a, b) {
							if (a.fcltId > b.fcltId) {
								return 1;
							}
							if (a.fcltId < b.fcltId) {
								return -1;
							}
							return 0;
						});
						// tbody
						$('#table-' + event.type + '-fclt').show();
						var elTbody = $olOverlay.find('#table-' + event.type + '-fclt tbody');
						if (elTbody.length == 1) {
							var $tbody = $(elTbody[0]);
							$tbody.empty();
							var nMaxCntFclt = 30;
							if (oFeaturesFclt.length <= nMaxCntFclt) {
								$.each(oFeaturesFclt, function(i, v) {
									var $tr = $('<tr/>');
									$tr.append($('<td/>', {
										'class' : 'text-ellipsis td-fclt-lbl-nm',
										'title' : v.fcltLblNm,
										'text' : v.fcltLblNm,
										'data-toggle' : 'tooltip',
										'data-placement' : 'top',
									}));
									$tr.append($('<td/>', {
										'class' : 'text-ellipsis td-fclt-knd-nm',
										'title' : v.fcltKndNm + '(' + v.fcltUsedTyNm + ')',
										'text' : v.fcltKndNm + '(' + v.fcltUsedTyNm + ')',
										'data-toggle' : 'tooltip',
										'data-placement' : 'top',
									}));

									var $fcltSttus = $('<span/>', {
										'text' : v.fcltSttusNm
									});

									if (v.fcltSttus == '0') {
										$fcltSttus.css('color', 'green');
									} else if (v.fcltSttus == '1') {
										$fcltSttus.css('color', 'red');
									} else {
										$fcltSttus.css('color', 'orange');
									}
									$tr.append($('<td/>', {
										'class' : 'text-center',
										'html' : $fcltSttus
									}));
									$tr.append($('<td/>'));
									$tbody.append($tr);
								});
								$('#table-click-fclt tbody [data-toggle="tooltip"]').tooltip({
									container : 'body'
								});
							} else {
								var $tr = $('<tr/>');
								$tr.append($('<td/>', {
									'class' : 'text-center text-danger',
									'text' : '시설물 ' + nMaxCntFclt + '개 초과(지도를 확대하여 사용)',
									'colspan' : '4'
								}));
								$tbody.append($tr);
							}
						}
					} else {
						$('#table-' + event.type + '-fclt').hide();
					}

					$('#table-' + event.type + '-event').hide();

					var oPositionClick = olSwipMap.overlays.click.getPosition();
					var oPositionPointermove = olSwipMap.overlays.pointermove.getPosition();

					if (!oFeaturesFclt.length) {
						if (event.type == 'click' && typeof oPositionClick != 'undefined') {
							$('#ol-overlay-click').hide();
							$('#ol-overlay-click tbody').empty();
							olSwipMap.overlays.click.setPosition(undefined);
						} else if (event.type == 'pointermove' && typeof oPositionPointermove != 'undefined') {
							$('#ol-overlay-pointermove').hide();
							$('#ol-overlay-pointermove tbody').empty();
							this.getTargetElement().style.cursor = '';
							olSwipMap.overlays.pointermove.setPosition(undefined);
						}
					} else {
						if (event.type == 'click') {
							$('#ol-overlay-click').show();
							$('#ol-overlay-pointermove').hide();
							$('#ol-overlay-pointermove tbody').empty();
							this.getTargetElement().style.cursor = '';
							olSwipMap.overlays.pointermove.setPosition(undefined);
						} else if (event.type == 'pointermove') {
							$('#ol-overlay-pointermove').show();
							this.getTargetElement().style.cursor = 'pointer';
						}
					}
				}

				olSwipMap.events.feature.pointermove = handler;
				olSwipMap.events.feature.click = handler;

				olSwipMap.listeners.feature.pointermove = olMap.map.on('pointermove', olSwipMap.events.feature.pointermove);
				olSwipMap.listeners.feature.click = olMap.map.on('click', olSwipMap.events.feature.click);

				var oFcltData = $('#fcltData').data();
				if (oFcltData.hasOwnProperty('pointX') && oFcltData.hasOwnProperty('pointY')) {
					olSwipMap.point.set([
							oFcltData.pointX, oFcltData.pointY
					], 'EPSG:4326', 'FCLT', true, false, false);
				}
			} else {
				oMntrMap.initFcltLayer('', '', 'N');

				var oFcltData = $('#fcltData').data();
				var oFcltPoint = convertByWGS84(oFcltData.pointX, oFcltData.pointY);
				var oFcltLonLat = new OpenLayers.LonLat(oFcltPoint.x, oFcltPoint.y);
				featureselected(oFcltPoint, 'fclt', '', false, true, false);
				_map.setCenter(oFcltLonLat);
				_map.zoomToScale(oConfigure.gisLabelViewScale, true);
			}
		});
	};

	this.list = function() {
		document.fcltSrchVO.action = contextRoot + '/mntr/fclt.do';
		document.fcltSrchVO.submit();
	};

	this.upd = function() {
		document.fcltSrchVO.action = contextRoot + '/mntr/fcltUpd.do';
		document.fcltSrchVO.submit();
	};

	this.del = function() {
		if (!confirm('삭제하시겠습니까?')) return;

		var sSerialized = $('#fcltSrchVO').serialize();
		$.ajax({
			type : 'POST',
			url : contextRoot + '/mntr/fcltDelProc.json',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			dataType : 'json',
			data : sSerialized,
			success : function(data) {
				var status = data.status;
				alert('삭제되었습니다.');
				oFcltDetail.list();
			},
			error : function(xhr, status, error) {
				alert('삭제에 실패하였습니다.');
			}
		});
	};
}
