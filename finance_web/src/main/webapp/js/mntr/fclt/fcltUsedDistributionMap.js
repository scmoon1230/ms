$(function() {
	oFcltUsedDistributionMap = new fcltUsedDistributionMap();
	oFcltUsedDistributionMap.init();
});

function fcltUsedDistributionMap() {
	this.init = function() {
		// LEFT 조절
		oConfigure.mntrViewLeft = 500;
		$('aside#left').css('width', oConfigure.mntrViewLeft);
		$('section#body').css('left', oConfigure.mntrViewLeft + 10);
		$('#toggleLeft').css('left', oConfigure.mntrViewLeft);

		collapse({
			left : false
		});
		if (oGis.olVersion == 5) {
			// 1. BASE, SATELLITE 배경지도 생성.
			olSwipMap.init({
				dropdown : false,
				bookmark : false
			});

			if (typeof olMap.layers.distribution == 'undefined') {
				olSwipMap.layers.distribution.init();
			} else {
				olMap.layers.distribution.getSource().clear();
			}

			olSwipMap.layers.lineDong.init(true);
			olSwipMap.layers.lineLi.init(true);
		} else {
			/* 1. 시설레이어 생성 */
			oFcltLayer = new OpenLayers.Layer.Vector("fcltLayer", {
				projection : new OpenLayers.Projection(oGis.projection),
				strategies : [
					new OpenLayers.Strategy.Fixed()
				],
				protocol : new OpenLayers.Protocol.HTTP({
					url : contextRoot + '/mntr/fcltUsedDistributionGeoData.json',
					params : {
						searchFcltUsedTyCd : [],
						searchSigunguCd : '',
						searchLgDongCd : ''
					},
					readWithPOST : true,
					format : new OpenLayers.Format.GeoJSON()
				})
			});
			oFcltLayer.setVisibility(true);
			_map.addLayer(oFcltLayer);
			/* 2. 시설레이어 로딩이 끝나면 범례 생성 */
			oFcltLayer.events.on({
				'loadend' : function() {
					var $checked = $('input[name="chk-fclt-used-ty-cds"]:checked');
					var $ul = $('<ul/>');
					$.each($checked, function(i, v) {
						var oData = $(v).data();
						var oFeatures = oFcltLayer.getFeaturesByAttribute('fcltUsedTyCd', oData.fcltUsedTyCd);
						var sFcltUsedTyCdType = $('#sel-type-' + oData.fcltUsedTyCd + ' option:selected').val();
						var sFcltUsedTyCdColor = $('#sel-color-' + oData.fcltUsedTyCd + ' option:selected').val();
						var $li = $('<li/>');
						var $img = $('<img/>', {
							'src' : contextRoot + '/images/mntr/gis/point/' + sFcltUsedTyCdType + '_' + sFcltUsedTyCdColor + '.png',
							'alt' : oData.fcltKndNm + '(' + oData.fcltUsedTyNm + ')',
							'title' : oData.fcltKndNm + '(' + oData.fcltUsedTyNm + ')'
						});
						$li.append($img);
						$li.append(' ' + oData.fcltKndNm + '(' + oData.fcltUsedTyNm + '): ' + oFeatures.length);
						$ul.append($li);
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

					$('#remarks').append($('<h6/>', {
						'text' : sLabel,
						'class' : 'text-center'
					}));
					$('#remarks').append($ul);
				}
			});
		}

		var $remarks = $('<div/>', {
			'id' : 'remarks',
		});

		if (oGis.olVersion == 5) {
			$('#container-map').append($remarks);
		} else {
			$('#wrapperMap').append($remarks);
		}

		/* 3. 시군구 검색조건 생성 - emdYn: 'N' 옵션 */
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : contextRoot + '/mntr/sggEmdBoundList.json',
			data : {
				emdYn : 'N'
			},
			success : function(data, status, jqXHR) {
				var oList = data.list;
				if (oList.length) {
					if (oList.length != 1) {
						var oSigunguNm = oList[0].sigunguNm.split(' ');
						var sSigunguNm = oList[0].sigunguNm;
						if (oSigunguNm.length != 1) {
							sSigunguNm = oSigunguNm[0];
						}

						$('#searchSigunguCd').append($('<option/>', {
							'value' : oConfigure.sigunguCd,
							'text' : sSigunguNm + ' 전체',
							'data-min-x' : oGis.boundsLeft,
							'data-min-y' : oGis.boundsBottom,
							'data-max-x' : oGis.boundsRight,
							'data-max-y' : oGis.boundsTop,
							'selected' : 'selected'
						}));

						$.each(oList, function(i, v) {
							$('#searchSigunguCd').append($('<option/>', {
								'value' : v.sigunguCd,
								'text' : v.sigunguNm,
								'data-min-x' : v.minX,
								'data-min-y' : v.minY,
								'data-max-x' : v.maxX,
								'data-max-y' : v.maxY
							}));
						});
					} else {
						$('#searchSigunguCd').append($('<option/>', {
							'value' : oList[0].sigunguCd,
							'text' : oList[0].sigunguNm + ' 전체',
							'data-min-x' : oList[0].minX,
							'data-min-y' : oList[0].minY,
							'data-max-x' : oList[0].maxX,
							'data-max-y' : oList[0].maxY,
							'selected' : 'selected'
						}));
					}
				}
			},
			error : function(jqXHR, status, error) {
				console.log(error);
			}
		});
		/* 3. 시군구 검색조건 생성 - emdYn: 'Y' 옵션 */
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : contextRoot + '/mntr/sggEmdBoundList.json',
			data : {
				emdYn : 'Y'
			},
			success : function(data, status, jqXHR) {
				var oList = data.list;
				if (oList.length) {
					$.each(oList, function(i, v) {
						var oSigunguNm = v.sigunguNm.split(' ');
						var sLgEmdNm = v.lgEmdNm;
						if (oSigunguNm.length != 1) {
							sLgEmdNm = oSigunguNm[1] + ' ' + sLgEmdNm;
						}

						$('#searchLgDongCd').append($('<option/>', {
							'value' : v.lgDongCd,
							'text' : sLgEmdNm,
							'data-min-x' : v.minX,
							'data-min-y' : v.minY,
							'data-max-x' : v.maxX,
							'data-max-y' : v.maxY
						}));
					});
				}
			},
			error : function(jqXHR, status, error) {
				console.log(error);
			}
		});
		/* 4. 시설 유형별 목록 - 레이어 목록 가져오는 기능이지만 재사용 가능 */
		$.ajax({
			type : 'POST',
			url : contextRoot + '/mntr/selectFcltUsedDistributionFcltUsedTyList.json',
			data : {
				searchIncludeFcltUsedTyCdYn : 'Y'
			},
			async : false,
			success : function(data, status, jqXHR) {
				var oLayerList = data.layerList;

				var $tr = $('<tr/>');
				var $th = $('<th/>', {
					'scope' : 'row'
				});
				var $td = $('<td/>');
				var $label = $('<label/>');
				var $input = $('<input/>', {
					'type' : 'checkbox',
					'name' : 'chk-fclt-used-ty-cds',
					//	'checked': 'checked',
					'value' : '',
					'aria-label' : '',
				});
				// 모양
				var oType = [
						'circle', 'triangle', 'square', 'diamond'
				];
				var $selectType = $('<select/>', {
					'class' : 'form-control selectpicker'
				});

				$.each(oType, function(i, v) {
					$selectType.append($('<option/>', {
						'data-content' : '<img src="' + contextRoot + '/images/mntr/gis/point/' + v + '.png" />',
						'value' : v
					}));
				});

				// 색상
				var oColor = [
						'red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet'
				];
				var $selectColor = $('<select/>', {
					'class' : 'form-control selectpicker'
				});

				$.each(oColor, function(i, v) {
					$selectColor.append($('<option/>', {
						'data-content' : '<img src="' + contextRoot + '/images/mntr/gis/point/square_' + v + '.png" />',
						'value' : v
					}));
				});

				var $tbodyFclt = $('#tbody-fclt');
				$.each(oLayerList, function(i, v) {
					var sLayerGrpId = v.layerGrpId;
					var sLayerGrpNm = v.layerGrpNm;
					var sLayerId = v.layerId;
					var sLayerNm = v.layerNm;

					var $trC = $tr.clone();
					var $inputC = $input.clone().attr({
						'id' : 'chk-' + sLayerId,
						'value' : sLayerId,
						'aria-label' : sLayerId,
						'data-fclt-used-ty-cd' : sLayerId,
						'data-fclt-used-ty-nm' : sLayerNm,
						'data-fclt-knd-cd' : sLayerGrpId,
						'data-fclt-knd-nm' : sLayerGrpNm
					});
					var $thC = $th.clone().append($label.clone().append($inputC));
					$trC.append($thC);
					$trC.append($td.clone().text(sLayerGrpNm));
					$trC.append($td.clone().text(sLayerNm));

					var $selectTypeC = $selectType.clone();
					var oTypes = $selectTypeC.find('option');
					if (oTypes.length) {
						var nTypesRandom = Math.floor((Math.random() * oTypes.length));
						var eOption = oTypes[nTypesRandom];
						$(eOption).prop('selected', true);
					}
					$selectTypeC.attr({
						'id' : 'sel-type-' + sLayerId
					});
					$trC.append($td.clone().append($selectTypeC));

					var $selectColorC = $selectColor.clone();
					var oColors = $selectColorC.find('option');
					if (oColors.length) {
						var nColorsRandom = Math.floor((Math.random() * oColors.length));
						var eOption = oColors[nColorsRandom];
						$(eOption).prop('selected', true);
					}

					$selectColorC.attr({
						'id' : 'sel-color-' + sLayerId
					});
					$trC.append($td.clone().append($selectColorC));

					$tbodyFclt.append($trC);
				});
			},
			error : function(jqXHR, status, error) {
				console.log(error);
			}
		});

		if (oGis.olVersion == 5) {

		} else {
			setTimeout(function() {
				if (typeof oFcltAngleLayer !== 'undefined' && oFcltAngleLayer !== null) {
					oFcltAngleLayer.setVisibility(false);
				}
			}, 500);
		}

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
			oFcltUsedDistributionMap.search();
		});
		// check all
		$('#chk-fclt-used-ty-cds-all').on('change', function() {
			var isChecked = $(this).is(':checked');
			if (!isChecked) {
				$('input[name="chk-fclt-used-ty-cds"]').prop('checked', false);
			} else {
				$('input[name="chk-fclt-used-ty-cds"]').prop('checked', true);
			}
		});

		$('.tooltip-notice').tooltip();
	};

	this.search = function() {
		var oParams = oFcltUsedDistributionMap.params();
		if (oParams.isValid) {
			if (oGis.olVersion == 5) {
				var olLayer = olMap.layers.distribution;
				var olSource = olLayer.getSource();
				olSource.clear();

				var oCoordinates = [];
				oCoordinates.push([
						oParams.searchMinX, oParams.searchMaxY
				]);
				oCoordinates.push([
						oParams.searchMaxX, oParams.searchMaxY
				]);
				oCoordinates.push([
						oParams.searchMaxX, oParams.searchMinY
				]);
				oCoordinates.push([
						oParams.searchMinX, oParams.searchMinY
				]);

				var olPolygon = new ol.geom.Polygon([
					oCoordinates
				]);

				olPolygon.transform('EPSG:4326', oGis.projection);

				olMap.map.getView().fit(olPolygon.getExtent(), {
					nearest : true
				});

				var olFeature = new ol.Feature({
					geometry : olPolygon
				});

				olFeature.setStyle(new ol.style.Style({
					stroke : new ol.style.Stroke({
						color : '#ffcc33',
						width : 2
					})
				}));
				olSource.addFeature(olFeature);

				if (oParams.searchFcltUsedTyCd.length) {
					oParams.searchFcltUsedTyCd = oParams.searchFcltUsedTyCd.toString();
				}

				$.ajax({
					type : 'POST',
					url : contextRoot + '/mntr/fcltUsedDistributionGeoData.json',
					datatype : 'json',
					data : oParams,
					beforeSend : function(xhr) {
						olSwipMap.spinner.spin(document.getElementById('map'));
					},
					success : function(data, status, jqXHR) {
						var olLayer = olMap.layers.distribution;
						var olSource = olLayer.getSource();
						var olFeatures = new ol.format.GeoJSON().readFeatures(data);
						var nLength = olFeatures.length;
						olLayer.setStyle(function(feature) {
							var oProperties = feature.getProperties();

							var sSize = parseInt($('#sel-icon-size option:selected').val());
							var nIconSize = 10;
							var sPrefix = 'point/';
							var sSuffix = '.png';
							var sFcltUsedTyCdType = $('#sel-type-' + oProperties.fcltUsedTyCd + ' option:selected').val();
							var sFcltUsedTyCdColor = $('#sel-color-' + oProperties.fcltUsedTyCd + ' option:selected').val();

							var nIconScale = parseInt(sSize) / nIconSize;
							nIconScale = nIconScale.toFixed(2);

							var oAnchor = [
									0, 0
							];

							var sRank = oProperties.rank;
							if (sRank == '2') {
								oAnchor[0] = (-1 * nIconSize) / 2;
								oAnchor[1] = (-1 * nIconSize) / 2;
							} else if (sRank == '3') {
								oAnchor[0] = (-1 * nIconSize) / 2;
								oAnchor[1] = nIconSize / 2;
							} else if (sRank == '4') {
								oAnchor[0] = nIconSize / 2;
								oAnchor[1] = nIconSize / 2;
							} else if (sRank == '5') {
								oAnchor[0] = nIconSize / 2
								oAnchor[1] = (-1 * nIconSize) / 2;
							} else if (sRank == '6') {
								oAnchor[0] = 0;
								oAnchor[1] = (-1 * nIconSize) / 2;
							} else if (sRank == '7') {
								oAnchor[0] = (-1 * nIconSize) / 2;
								oAnchor[1] = 0;
							} else if (sRank == '8') {
								oAnchor[0] = 0;
								oAnchor[1] = nIconSize / 2;
							} else if (sRank == '9') {
								oAnchor[0] = nIconSize / 2;
								oAnchor[1] = 0;
							}

							return new ol.style.Style({
								image : new ol.style.Icon(({
									anchor : oAnchor,
									anchorXUnits : 'pixels',
									anchorYUnits : 'pixels',
									size : [
											nIconSize, nIconSize
									],
									scale : nIconScale,
									opacity : 0.7,
									src : contextRoot + '/images/mntr/gis/' + sPrefix + sFcltUsedTyCdType + '_' + sFcltUsedTyCdColor + sSuffix
								}))
							});
						});

						// Add Features
						olSource.addFeatures(olFeatures);

						// Remarks Section
						var $checked = $('input[name="chk-fclt-used-ty-cds"]:checked');
						var $ul = $('<ul/>');
						$.each($checked, function(i, v) {
							var oData = $(v).data();
							var oFeatures = olSwipMap.util.getFeaturesByProperties(olMap.layers.distribution, 'fcltUsedTyCd', oData.fcltUsedTyCd);
							var sFcltUsedTyCdType = $('#sel-type-' + oData.fcltUsedTyCd + ' option:selected').val();
							var sFcltUsedTyCdColor = $('#sel-color-' + oData.fcltUsedTyCd + ' option:selected').val();
							var $li = $('<li/>');
							var $img = $('<img/>', {
								'src' : contextRoot + '/images/mntr/gis/point/' + sFcltUsedTyCdType + '_' + sFcltUsedTyCdColor + '.png',
								'alt' : oData.fcltKndNm + '(' + oData.fcltUsedTyNm + ')',
								'title' : oData.fcltKndNm + '(' + oData.fcltUsedTyNm + ')'
							});
							$li.append($img);
							$li.append(' ' + oData.fcltKndNm + '(' + oData.fcltUsedTyNm + '): ' + oFeatures.length);
							$ul.append($li);
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

						$('#remarks').append($('<h6/>', {
							'text' : sLabel,
							'class' : 'text-center'
						}));
						$('#remarks').append($ul);
					},
					complete : function(xhr, status) {
						olSwipMap.spinner.stop();
					}
				});

			} else {
				var style = {
					strokeColor : "#ffcc33",
					strokeWidth : 2,
					fillOpacity : 0
				};

				var olBounds = new OpenLayers.Bounds(oParams.searchMinX, oParams.searchMinY, oParams.searchMaxX, oParams.searchMaxY);
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
				pointLayer.addFeatures([
					pf
				]);

				oFcltLayer.protocol.params.searchFcltUsedTyCd = oParams.searchFcltUsedTyCd;
				oFcltLayer.protocol.params.searchSigunguCd = oParams.searchSigunguCd;
				oFcltLayer.protocol.params.searchLgDongCd = oParams.searchLgDongCd;
				oFcltLayer.protocol.params.searchMinX = oParams.searchMinX;
				oFcltLayer.protocol.params.searchMaxY = oParams.searchMaxY;
				oFcltLayer.protocol.params.searchMaxX = oParams.searchMaxX;
				oFcltLayer.protocol.params.searchMinY = oParams.searchMinY;

				var nIconSize = parseInt($('#sel-icon-size option:selected').val());
				var newStyle = new OpenLayers.Style({
					cursor : 'pointer',
					externalGraphic : '${getIcon}',
					pointRadius : nIconSize,
					graphicOpacity : 0.7,
					graphicXOffset : '${getXOffset}',
					graphicYOffset : '${getYOffset}',
					graphicWidth : nIconSize,
					graphicHeight : nIconSize
				}, {
					context : {
						getIcon : function(f) {
							var sPrefix = 'point/';
							var sSuffix = '.png';
							var sFcltUsedTyCdType = $('#sel-type-' + f.attributes.fcltUsedTyCd + ' option:selected').val();
							var sFcltUsedTyCdColor = $('#sel-color-' + f.attributes.fcltUsedTyCd + ' option:selected').val();
							var markImage = sPrefix + sFcltUsedTyCdType + '_' + sFcltUsedTyCdColor + sSuffix;
							return getMarkImage(markImage);
						},
						getXOffset : function(f) {
							var nSize = 0;
							var sRank = f.attributes.rank;
							if (sRank == '2') {
								nSize = nIconSize / 2;
							} else if (sRank == '3') {
								nSize = nIconSize / 2;
							} else if (sRank == '4') {
								nSize = (-1 * nIconSize) / 2;
							} else if (sRank == '5') {
								nSize = (-1 * nIconSize) / 2
							} else if (sRank == '6') {
								nSize = 0;
							} else if (sRank == '7') {
								nSize = nIconSize / 2;
							} else if (sRank == '8') {
								nSize = 0;
							} else if (sRank == '9') {
								nSize = (-1 * nIconSize) / 2;
							}
							return nSize;
						},
						getYOffset : function(f) {
							var nSize = 0;
							var sRank = f.attributes.rank;
							if (sRank == '2') {
								nSize = nIconSize / 2;
							} else if (sRank == '3') {
								nSize = (-1 * nIconSize) / 2;
							} else if (sRank == '4') {
								nSize = (-1 * nIconSize) / 2;
							} else if (sRank == '5') {
								nSize = nIconSize / 2;
							} else if (sRank == '6') {
								nSize = nIconSize / 2;
							} else if (sRank == '7') {
								nSize = 0;
							} else if (sRank == '8') {
								nSize = (-1 * nIconSize) / 2;
							} else if (sRank == '9') {
								nSize = 0;
							}
							return nSize;
						}
					}
				});

				var sty = OpenLayers.Util.applyDefaults(newStyle, OpenLayers.Feature.Vector.style["default"]);
				var sm = new OpenLayers.StyleMap({
					'default' : sty
				});
				oFcltLayer.styleMap = sm;

				if (oFcltLayer.visibility) {
					oFcltLayer.setVisibility(true);
					oFcltLayer.refresh({
						force : true
					});
				} else {
					oFcltLayer.refresh({
						force : true
					});
				}
			}
		}
	};

	this.params = function() {
		var $searchSigunguCd = $('#searchSigunguCd option:selected');
		var $searchLgDongCd = $('#searchLgDongCd option:selected');
		var oData = {};

		var oParams = {
			isValid : true,
			searchFcltUsedTyCd : null,
			searchSigunguCd : null,
			searchLgDongCd : null,
			searchMinX : null,
			searchMinY : null,
			searchMaxX : null,
			searchMaxY : null
		};

		if ($searchSigunguCd.val() != '') {
			oData = $searchSigunguCd.data();
		} else if ($searchLgDongCd.val() != '') {
			oData = $searchLgDongCd.data();
		} else {
			alert('시군구 또는 읍명동 을(를) 선택해 주세요.');
			oParams.isValid = false;
		}

		var nRevision = 0.003;
		var nLeft = oData.minX - nRevision;
		var nTop = oData.maxY + nRevision;
		var nRight = oData.maxX + nRevision;
		var nBottom = oData.minY - nRevision;

		var $checked = $('input[name="chk-fclt-used-ty-cds"]:checked');
		if ($checked.length) {
			oParams.searchFcltUsedTyCd = [];
			$.each($checked, function(i, v) {
				oParams.searchFcltUsedTyCd.push($(v).val());
			});
		}

		if (oParams.searchFcltUsedTyCd == null || !oParams.searchFcltUsedTyCd.length) {
			alert('선택된 시설물이 없습니다.');
			oParams.isValid = false;
		}

		oParams.searchMinX = nLeft;
		oParams.searchMaxY = nTop;
		oParams.searchMaxX = nRight;
		oParams.searchMinY = nBottom;
		oParams.searchSigunguCd = $searchSigunguCd.val();
		oParams.searchLgDongCd = $searchLgDongCd.val();

		//if (oConfigure.exeEnv = 'DEV')
		console.log('-- oParams => %o',oParams);
		
		return oParams;
	};
}
