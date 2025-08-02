let olSwipMap = {
	/**
	 * { dropdown : false, bookmark : false, search : false, measure : false, basemap : false, menu : false }
	 */
	init: function (options) {
		// Base Layers [Base, Satellite, Vector], View
		olMap.init();

		this.overlays.pointermove = new ol.Overlay({
			id: 'ol-overlay-pointermove',
			element: document.getElementById('ol-overlay-pointermove'),
			offset: [
				9, 9
			]
		});
		olMap.map.addOverlay(this.overlays.pointermove);

		this.overlays.click = new ol.Overlay({
			id: 'ol-overlay-click',
			element: document.getElementById('ol-overlay-click'),
			offset: [
				9, 9
			]
		});
		olMap.map.addOverlay(this.overlays.click);

		// 지도 밖의 overlay를 클릭 했을 경우 다 보이도록 맵을 옮겨준다.
		this.overlays.click.on('change:position', (event) => {
			setTimeout(() => {
				let $olOverlayClick = $('#ol-overlay-click');
				if ($olOverlayClick.is(':visible')) {
					let $container = $olOverlayClick.closest('.ol-overlay-container.ol-selectable'),
						$mapIndex = $('#map-index'),
						nWidthMap = $('#map').width(),
						nHeightMap = $('#map').height(),
						nWidthOverlay = $container.width(),
						nHeightOverlay = $container.height(),
						nLeftOverlay = parseFloat($container.css('left').replace('px', '')),
						nTopOverlay = parseFloat($container.css('top').replace('px', '')),
						oOffset = {x: 0, y: 0};

					if (nWidthMap - nLeftOverlay <= nWidthOverlay) oOffset.x = (nWidthOverlay - (nWidthMap - nLeftOverlay) + 40) * olMap.view.getResolution();
					if ($mapIndex.is(':visible') && oOffset.x != 0) nHeightOverlay += ($mapIndex.height() + 10);
					if (nHeightMap - nTopOverlay <= nHeightOverlay) oOffset.y = (nHeightOverlay - (nHeightMap - nTopOverlay) - 10) * olMap.view.getResolution() * -1;
					let oCoordinateCenter = olMap.view.getCenter();
					let oCoordinateCenterNew = [oCoordinateCenter[0] + oOffset.x, oCoordinateCenter[1] + oOffset.y];
					olMap.view.setCenter(oCoordinateCenterNew);
				}
			}, 500);
		});

		this.overlays.popoverEx = new ol.Overlay({
			id: 'ol-overlay-popover-extra',
			element: document.getElementById('ol-overlay-popover-extra'),
			offset: [
				9, 9
			]
		});
		olMap.map.addOverlay(this.overlays.popoverEx);

		// 팝오버 기능
		this.popover.init();
		this.mntr.popover.init();

		// 툴팁생성
		$('#ol-map-menu [data-toggle="tooltip"]').tooltip({
			container: 'body',
			placement: 'left'
		});
		
		if (typeof options != 'undefined') {
			if (options.hasOwnProperty('dropdown')) {
				if (options.dropdown) {
					$('.ol-map-menu-dropdown-layers').show();
					olSwipMap.layers.dropdownLayers('#dropdown-layers');
				} else {
					$('.ol-map-menu-dropdown-layers').hide();
				}
			} else {
				// olSwipMap.layers.dropdownLayers('#dropdown-layers');
			}

			if (options.hasOwnProperty('bookmark')) {
				if (options.bookmark) {
					// $('.ol-map-menu-bookmark').show();
					this.bookmark.init();
				} else {
					$('.ol-map-menu-bookmark').hide();
				}
			} else {
				this.bookmark.init();
			}

			if (options.hasOwnProperty('search')) {
				if (options.search) {
					// $('#ol-map-menu-search').show();
					this.search.init();
				} else {
					$('#ol-map-menu-search').hide();
				}
			} else {
				this.search.init();
			}

			if (options.hasOwnProperty('center')) {
				if (options.center) {
					$('#ol-map-menu-center').show();
				} else {
					$('#ol-map-menu-center').hide();
				}
			} else {
				$('#ol-map-menu-center').show();
			}

			if (options.hasOwnProperty('measure')) {
				if (options.measure) {
					$('#ol-map-menu-measure').show();
				} else {
					$('#ol-map-menu-measure').hide();
				}
			}

			if (options.hasOwnProperty('basemap')) {
				if (options.basemap) {
					$('#ol-map-menu-basemap').show();
				} else {
					$('#ol-map-menu-basemap').hide();
				}
			}

			if (options.hasOwnProperty('indexmap')) {
				if (options.indexmap) {
					this.indexmap.init();
					$('#map-index-container').removeClass('hide');
					$('input[type=checkbox][data-layer-id=INDEXMAP][data-layer-grp-id=ETC]').closest('a[role=menuitem]').removeClass('hide');
				} else {
					setTimeout(() => {
						$('#map-index-container').addClass('hide');
						$('input[type=checkbox][data-layer-id=INDEXMAP][data-layer-grp-id=ETC]').closest('a[role=menuitem]').addClass('hide');
					}, 500);
				}
			} else {
				//this.indexmap.init();
			}

			if (options.hasOwnProperty('menu')) {
				if (options.menu) {
					$('#ol-map-menu').show();
					$('#map').css('top', '40px');
				} else {
					$('#ol-map-menu').hide();
					$('#map').css('top', '0px');
				}
			}

			if (options.hasOwnProperty('contextmenu')) {
				if (!options.contextmenu) {
					olMap.map.getViewport().removeEventListener('contextmenu', olSwipMap.contextmenu.listener);
					olMap.map.removeOverlay(olSwipMap.contextmenu.overlay);
					olSwipMap.contextmenu.overlay = null;
					olSwipMap.contextmenu.element = null;
				} else {
					this.contextmenu.init();
				}
			} else {
				this.contextmenu.init();
			}
		} else {
			// olSwipMap.layers.dropdownLayers('#dropdown-layers');
			// 지도 검색 기능
			this.search.init();
			// 즐겨찾기 기능
			this.bookmark.init();
			// 인덱스맵
			this.indexmap.init();
			// 마우스 우클릭 기능
			this.contextmenu.init();
		}

		const $map = $(olMap.map.getTargetElement());
		$map.on('mouseleave', () => {	// 마우스커서가 지도 밖으로 나갔을 때 지도 위의 팝업을 숨긴다.
			if (olSwipMap.overlays.pointermove !== null) olSwipMap.overlays.pointermove.setPosition(undefined);
		});
		
		olSwipMap.indexmap.hide();	// 인덱스맵 숨기기
	},

	spinner: new Spinner({
		lines: 13,
		length: 20,
		width: 10,
		radius: 30,
		corners: 1,
		rotate: 0,
		direction: 1,
		color: '#000',
		speed: 1,
		trail: 60,
		shadow: false,
		hwaccel: false,
		className: 'ol-map-spinner',
		zIndex: 2e9,
		top: '50%',
		left: '50%'
	}),

	// olSwipMap.point
	/**
	 * @param isCloseable
	 *			{Boolean} true 포인트 오버레이를 삭제할 수 있는 버튼을 추가 해준다.
	 * @param isFixed
	 *			{Boolean} true : 포인트 오버레이 고정 시킬 수 있다. overlayFixed 에 등록된다, false : overlay 에 등록되며, 이전 포인트 오버레이는 삭제된다.
	 */
	point: {
		overlay: null,
		overlayFixed: {},
		set: function (coordinate, projection, url, isCenter, isFixed, isCloseable) {
			if (typeof coordinate !== 'undefined' && coordinate.length == 2) {
				let nLon = parseFloat(coordinate[0]);
				let nLat = parseFloat(coordinate[1]);
				let sProjectionCode = olMap.view.getProjection().getCode();
				if (!isNaN(nLon) && !isNaN(nLat)) {
					coordinate = [
						nLon, nLat
					];
					if (typeof projection !== 'undefined') {
						if (sProjectionCode != projection) {
							coordinate = ol.proj.transform(coordinate, projection, sProjectionCode);
						}
					} else {
						coordinate = ol.proj.transform(coordinate, 'EPSG:4326', sProjectionCode);
					}

					let oExtentDokdo = ol.proj.transformExtent([
						131.860491, 37.237794, 131.873279, 37.247975
					], 'EPSG:4326', oGis.projection);

					if ((coordinate[0] > olMap.extent[0]) && (coordinate[0] < olMap.extent[2]) && (coordinate[1] > olMap.extent[1]) && (coordinate[1] < olMap.extent[3])) {
						olMap.view.setCenter(coordinate);
					} else if ((coordinate[0] > oExtentDokdo[0]) && (coordinate[0] < oExtentDokdo[2]) && (coordinate[1] > oExtentDokdo[1]) && (coordinate[1] < oExtentDokdo[3])) {
						olMap.view.fit(oExtentDokdo);
					} else {
						coordinate = ol.proj.transform(coordinate, sProjectionCode, 'EPSG:4326');
						//alert('잘못된 좌표 값이거나 지도 범위 밖입니다. \n경도: ' + coordinate[0].toFixed(6) + ', 위도: ' + coordinate[1].toFixed(6));
						console.log('### 잘못된 좌표 값이거나 지도 범위 밖입니다. \n경도: ' + coordinate[0].toFixed(6) + ', 위도: ' + coordinate[1].toFixed(6));
						return false;
					}
				} else {
					console.log('-- olSwipMap.point.set(%o, %o)', coordinate, projection);
				}
			} else {
				console.log('-- olSwipMap.point.set(%o, %o)', coordinate, projection);
			}


			// /images/mntr/gis/selected/alert.png				경고
			// /images/mntr/gis/selected/point_peter_river.png	하늘
			// /images/mntr/gis/selected/point_alizarin.png		빨강
			// /images/mntr/gis/selected/point_carrot.png		오렌지
			// /images/mntr/gis/selected/point_select.png		박스
			/*
			if (url == 'FCLT') {
				url = contextRoot + '/images/mntr/gis/selected/point_select.png';
			} else if (url == 'EVENT') {
				url = contextRoot + '/images/mntr/gis/selected/alert.png';
			} else if (url == 'RED') {
				url = contextRoot + '/images/mntr/gis/selected/point_alizarin.png';
			} else if (url == 'BLUE') {
				url = contextRoot + '/images/mntr/gis/selected/point_peter_river.png';
			} else if (url == 'ORANGE') {
				url = contextRoot + '/images/mntr/gis/selected/point_carrot.png';
			} else if (!url) {
				url = contextRoot + '/images/mntr/gis/selected/point_peter_river.png';
			}

			let sStyle = 'width: 64px; height: 64px;';
			let oOffset = [
				-32, -32
			];
			if (url == 'EVENT') {
				sStyle = 'width: 64px; height: 50px;';
				oOffset = [
					-32, -25
				]
			}

			let elOverlay = $('<div/>', {
				'id': 'ol-overlay-point',
				'style': sStyle + ' background: transparent url("' + url + '") no-repeat;'
			})[0];
			*/
			if (url == 'FCLT') {
				url = '<span class="text-success"><i class="fas fa-caret-down fa-2x"></i></span>';
			} else if (url == 'EVENT') {
				url = '<span class="text-danger"><i class="fas fa-caret-down fa-2x"></i></span>';
			} else if (url == 'RED') {
				url = '<span class="text-danger"><i class="fas fa-caret-down fa-2x"></i></span>';
			} else if (url == 'BLUE') {
				//url = '<span class="text-info"><i class="fas fa-caret-down fa-2x"></i></span>';
				url = '<span class="text-danger"><i class="fas fa-caret-down fa-2x"></i></span>';
			} else if (url == 'ORANGE') {
				url = '<span class="text-warning"><i class="fas fa-caret-down fa-2x"></i></span>';
			} else if (!url) {
				url = '<span class="text-danger"><i class="fas fa-caret-down fa-2x"></i></span>';
			}

			let oOffset = [
				-7, -34
			];

			let elOverlay = $('<div/>', {
				'id': 'ol-overlay-point',
				'html': url,
			})[0];


			let nRandom = Math.floor(Math.random() * (100000 - 1 + 1)) + 1;
			let nId = Date.now() + nRandom;

			if (isCloseable) {
				let $btnDismiss = $('<button/>', {
					'type': 'button',
					'class': 'close',
					'style': 'position: absolute; top: -15px; right: -15px; color : #f00;',
					'html': '<i class="fas fa-times"></i>',
					'onclick': 'javascript:olSwipMap.point.clear(' + nId + ');'
				});
				$(elOverlay).append($btnDismiss);
			}

			if (isFixed) {
				this.overlayFixed[nId] = new ol.Overlay({
					element: elOverlay,
					offset: oOffset
				});
				olMap.map.addOverlay(this.overlayFixed[nId]);
				this.overlayFixed[nId].setPosition(coordinate);
			} else {
				if (this.overlay != null) {
					olMap.map.removeOverlay(this.overlay);
					this.overlay = null;
				}

				this.overlay = new ol.Overlay({
					element: elOverlay,
					offset: oOffset
				});
				olMap.map.addOverlay(this.overlay);
				this.overlay.setPosition(coordinate);
			}

			if (isCenter) {
				olMap.view.setCenter(coordinate);
			}
		},
		clear: function (id) {
			if (typeof id != 'undefined' && typeof this.overlayFixed[id] != 'undefined') {
				olMap.map.removeOverlay(this.overlayFixed[id]);
				this.overlayFixed[id] = undefined;
			} else {
				if (this.overlay != null) {
					olMap.map.removeOverlay(this.overlay);
					this.overlay = null;
				}
			}
		},
		clearAll: function () {
			if (this.overlay != null) {
				olMap.map.removeOverlay(this.overlay);
				this.overlay = null;
			}

			if (!$.isEmptyObject(this.overlayFixed)) {
				$.each(Object.values(this.overlayFixed), function (i, v) {
					if (typeof v != 'undefined') {
						olMap.map.removeOverlay(v);
					}
				});
				this.overlayFixed = {};
			}
		}
	},

	events: {
		feature: {
			click: null,
			pointermove: null
		}
	},
	listeners: {
		feature: {
			click: null,
			pointermove: null
		}
	},
	overlays: {
		click: null,
		pointermove: null,
		popoverEx: null,
	},

	// layers : olSwipMap.layers.fclt
	layers: {
		fclt: {
			cluster: false,
			source: {
				vector: null,
				cluster: null
			},
			style: {
				vector: null,
				cluster: null
			},
			listener: {
				moveend: null
			},
			event: {
				moveend: function (event) {
					let nResolution = olMap.map.getView().getResolution() * 72 * 39.37;
					let nViewScale = Math.round(nResolution);
					let nGisFeatureViewScale = parseInt(oConfigure.gisFeatureViewScale);

					let isVisibleFclt = olMap.layers.fclt.getVisible();

					let isCluster = olSwipMap.layers.fclt.cluster;

					let olSource = olMap.layers.fclt.getSource();
					let isClusterSource = ol.source.Cluster.prototype.isPrototypeOf(olSource);
					let isVectorSource = ol.source.Vector.prototype.isPrototypeOf(olSource);

					if (nViewScale < nGisFeatureViewScale && isClusterSource && isCluster) {
						// Cluster 비활성
						olSwipMap.layers.fclt.source.cluster.clear();
						olMap.layers.fclt.setSource(olSwipMap.layers.fclt.source.vector);
						olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.vector);
					} else if (nViewScale > nGisFeatureViewScale && isVectorSource && isCluster) {
						// Cluster 활성
						olMap.layers.fclt.setSource(olSwipMap.layers.fclt.source.cluster);
						olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.cluster);
						let sDistance = olMap.controls.scaleLine.renderedHTML_.split(/(\s+)/)[0];
						if ('' != sDistance && !isNaN(sDistance)) {
							let nDistance = parseInt(sDistance);
							if (nDistance < 10) {
								nDistance = nDistance * 1000;
							}
							nDistance = nDistance / 8;
							olSwipMap.layers.fclt.source.cluster.setDistance(nDistance);
						}
					} else if (nViewScale < nGisFeatureViewScale && isVectorSource && !isCluster) {
						// 시설물 Icon 보이기
						if (!isVisibleFclt) {
							olMap.layers.fclt.setVisible(true);
						}
						if (olSwipMap.layers.fclt.source.cluster.getFeatures().length) {
							olSwipMap.layers.fclt.source.cluster.clear();
						}
						olSource.clear();
					} else if (nViewScale > nGisFeatureViewScale && isVectorSource && !isCluster) {
						// 시설물 Icon 숨김
						if (isVisibleFclt) {
							olMap.layers.fclt.setVisible(false);
						}
					}
					olSwipMap.layers.angle.refresh();
				}
			},
			init: function (options) {
				if (typeof options != 'undefined') {
					if (options.hasOwnProperty('cluster')) {
						this.cluster = options.cluster;
						this.data.searchIgnoreScaleYn = 'Y';
					}
					if (options.hasOwnProperty('fcltKndCd')) {
						this.data.searchFcltKndCd = options.fcltKndCd;
					}
					if (options.hasOwnProperty('fcltUsedTyCd')) {
						this.data.searchFcltUsedTyCd = options.fcltUsedTyCd;
					}
					if (options.hasOwnProperty('ivaTy')) {
						this.data.searchIvaTy = options.ivaTy;
					}
					if (options.hasOwnProperty('dtctnReqTy')) {
						this.data.searchDtctnReqTy = options.dtctnReqTy;
					}
				}

				this.source.vector = new ol.source.Vector({
					loader: function (extent, resolution, projection) {
						$.ajax({
							type: 'POST',
							url:contextRoot + '/mntr/fcltGeoData.json',
							data: olSwipMap.layers.fclt.data,
							beforeSend: function (xhr) {
								olSwipMap.spinner.spin(document.getElementById('map'));
								if (olMap.layers.angle) olMap.layers.angle.getSource().clear();
								olSwipMap.layers.fclt.source.vector.clear();
							},
							success: function (result, status, xhr) {
								let olSource = olSwipMap.layers.fclt.source.vector;
								let olFeatures = new ol.format.GeoJSON().readFeatures(result);
								if (olSource.getFeatures().length == 0) {
									olSource.addFeatures(olFeatures);
								}
							},
							error: function (xhr, status, error) {
								console.log(xhr, status, error);
							},
							complete: function (xhr, status) {
								olSwipMap.spinner.stop();
							}
						});
					},
					strategy: ol.loadingstrategy.bbox
				});

				this.style.cluster = function (feature) {
					let nLength = feature.get('features').length;
					let nRadius = 0;

					if (nLength < 10) {
						nRadius = 10
					} else if (nLength < 50) {
						nRadius = 15
					} else if (nLength < 100) {
						nRadius = 20
					} else {
						nRadius = 25
					}

					return new ol.style.Style({
						image: new ol.style.Circle({
							radius: nRadius,
							stroke: new ol.style.Stroke({
								color: 'rgba(255, 255, 255, 0.7)',
								width: 3
							}),
							fill: new ol.style.Fill({
								color: 'rgba(128, 0, 128, 0.7)'
							})
						}),
						text: new ol.style.Text({
							text: nLength.toString(),
							fill: new ol.style.Fill({
								color: '#fff'
							})
						})
					});
				};

				this.source.cluster = new ol.source.Cluster({
					distance: 20,
					source: this.source.vector
				});

				this.style.vector = function (feature) {
					let oProperties = feature.getProperties();
					let nSize = oConfigure.iconSize;
					let nIconSize = 0;
					let sStatus = oProperties.fcltSttus;
					let sFcltUsedTyCd = oProperties.fcltUsedTyCd;
					let sKndDtlCd = oProperties.fcltKndDtlCd;
					let sPrefix = 'fclt/typeA/';
					let sSuffix = '.png';

					// ICON TYPE
					if (oConfigure.iconTy == 'A') {
						if (oProperties.fcltKndCd == 'CTV') {
							if (sKndDtlCd == 'RT') {
								sSuffix = '_RT.png';
								nIconSize = 50;
							} else {
								nIconSize = 50;
							}
						} else {
							nIconSize = 40;
						}
					} else if (oConfigure.iconTy == 'B') {
						sPrefix = 'fclt/typeB/';
						if (oProperties.fcltKndCd == 'CTV') {
							if (sKndDtlCd == 'RT') {
								sSuffix = '_RT.png';
							}
							nIconSize = 74;
						} else {
							nIconSize = 50;
						}
					}

					let nIconScale = parseFloat(oConfigure.iconSize) / nIconSize;
					nIconScale = nIconScale.toFixed(2);

					let nGisLabelViewScale = parseInt(oConfigure.gisLabelViewScale);
					let nGisFeatureViewScale = parseInt(oConfigure.gisFeatureViewScale);
					let nViewScale = Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
					// 방향각
					if (oProperties.cctvOsvtX && oProperties.cctvOsvtY && oProperties.fcltKndDtlCd == 'FT') {
						let nAg = parseInt(oProperties.cctvOsvtAg);
						if (nAg < 0) {
							nAg = nAg + 360;
						}
						nAg += 225;
						let olSource = olMap.layers.angle.getSource();
						let oCoordinate = [
							oProperties.pointX, oProperties.pointY
						];

						let sProjectionCode = olMap.map.getView().getProjection().getCode();
						oCoordinate = ol.proj.transform(oCoordinate, 'EPSG:4326', sProjectionCode);

						let olFeature = new ol.Feature({
							geometry: new ol.geom.Point(oCoordinate),
						});
						olFeature.setStyle(new ol.style.Style({
							image: new ol.style.Icon(({
								anchor: [
									0, 0
								],
								anchorXUnits: 'pixels',
								anchorYUnits: 'pixels',
								scale: 0.25,
								opacity: 1.0,
								rotation: nAg * (Math.PI / 180),
								src: `${contextRoot}/images/mntr/gis/etc/type${oConfigure.iconTy}/ANGLE.png`
							}))
						}));
						olFeature.setId('AG_' + oProperties.fcltId);
						olFeature.setProperties(oProperties);
						olSource.addFeature(olFeature);
					}
					let nAnchor = nIconSize / 2;

					// <input type="checkbox" data-layer-id="FCLTLBLNM" data-layer-grp-id="ETC">

					let olText = null;
					let $input = $('input[type=checkbox][data-layer-grp-id=ETC][data-layer-id=FCLTLBLNM]');
					if ($input.is(':checked')) {
						olText = new ol.style.Text({
							font: 'bold 11px NanumGothic',
							text: (nViewScale < nGisLabelViewScale) ? oProperties.fcltLblNm : '',
							offsetY: 40,
							fill: new ol.style.Fill({
								color: 'rgba(0, 0, 0, 0.8)'
							}),
							stroke: new ol.style.Stroke({
								color: 'rgba(251, 250, 170, 0.8)',
								width: 3
							}),
						});
					}
					return new ol.style.Style({
						image: new ol.style.Icon(({
							anchor: [
								nAnchor, nAnchor
							],
							anchorXUnits: 'pixels',
							anchorYUnits: 'pixels',
							size: [
								nIconSize, nIconSize
							],
							scale: nIconScale,
							opacity: 0.8,
							src: contextRoot + '/images/mntr/gis/' + sPrefix + sFcltUsedTyCd + '_' + 0 + '_' + sStatus + sSuffix,
						})),
						text: olText
					});
				};

				if (typeof options != 'undefined' && options.hasOwnProperty('style')) {
					if (options.style.hasOwnProperty('vector')) {
						this.style.vector = options.style.vector;
					}
				}

				olMap.layers.fclt = new ol.layer.Vector({
					renderMode: 'vector',
					zIndex: 55,
				});

				let nViewScale = Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
				let nGisFeatureViewScale = parseInt(oConfigure.gisFeatureViewScale);
				if (nViewScale < nGisFeatureViewScale) {
					olMap.layers.fclt.setSource(olSwipMap.layers.fclt.source.vector);
					olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.vector);
				} else if (nViewScale > nGisFeatureViewScale && this.cluster) {
					olMap.layers.fclt.setSource(olSwipMap.layers.fclt.source.cluster);
					olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.cluster);
				} else {
					olMap.layers.fclt.setSource(olSwipMap.layers.fclt.source.vector);
					olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.vector);
				}

				olMap.map.addLayer(olMap.layers.fclt);

				let isInit = ol.layer.Vector.prototype.isPrototypeOf(olMap.layers.angle);
				if (!isInit) {
					olSwipMap.layers.angle.init(false);
				}

				// olSwipMap.layers.fclt.event.moveend
				if (typeof options != 'undefined') {
					if (options.hasOwnProperty('event')) {
						if (options.event.hasOwnProperty('moveend')) {
							this.listener.moveend = olMap.map.on('moveend', options.event.moveend);
						} else {
							this.listener.moveend = olMap.map.on('moveend', olSwipMap.layers.fclt.event.moveend);
						}
					} else {
						this.listener.moveend = olMap.map.on('moveend', olSwipMap.layers.fclt.event.moveend);
					}
				} else {
					this.listener.moveend = olMap.map.on('moveend', olSwipMap.layers.fclt.event.moveend);
				}
			},
			data: {
				bbox: function () {
					return olMap.map.getView().calculateExtent(olMap.map.getSize());
				},
				scale: function () {
					return Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
				},
				searchIgnoreScaleYn: 'N'
			},
			refresh: function () {
				let isInitLayerFclt = ol.layer.Vector.prototype.isPrototypeOf(olMap.layers.fclt);
				if (isInitLayerFclt) {
					let nResolution = olMap.map.getView().getResolution() * 72 * 39.37;
					let nViewScale = Math.round(nResolution);
					let nGisFeatureViewScale = parseInt(oConfigure.gisFeatureViewScale);
					let isCluster = olSwipMap.layers.fclt.cluster;
					let isVisibleFclt = olMap.layers.fclt.getVisible();
					if (nViewScale < nGisFeatureViewScale) {
						olMap.layers.fclt.setSource(olSwipMap.layers.fclt.source.vector);
						olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.vector);
					} else if (nViewScale > nGisFeatureViewScale) {
						// 클러스터링 활성화 || 시설물레이어 비활성화
						if (isCluster) {
							if (!isVisibleFclt) {
								olMap.layers.fclt.setVisible(true);
							}
							olMap.layers.fclt.setSource(olSwipMap.layers.fclt.source.cluster);
							olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.cluster);
							let sDistance = olMap.controls.scaleLine.renderedHTML_.split(/(\s+)/)[0];
							if ('' != sDistance && !isNaN(sDistance)) {
								let nDistance = parseInt(sDistance);
								if (nDistance < 10) {
									nDistance = nDistance * 1000;
								}
								nDistance = nDistance / 8;
								olSwipMap.layers.fclt.source.cluster.setDistance(nDistance);
								console.log('-- fclt layer clustered : distance(%d)', nDistance);
							}
						} else {
							olMap.layers.fclt.setSource(olSwipMap.layers.fclt.source.vector);
							olMap.layers.fclt.setStyle(olSwipMap.layers.fclt.style.vector);
							if (isVisibleFclt) {
								olMap.layers.fclt.setVisible(false);
							}
						}
					}
					olSwipMap.layers.fclt.source.cluster.clear();
					olSwipMap.layers.fclt.source.vector.clear();
				}
			}
		},
		event: {
			init: function (option) {
				let oData = {
					bbox: function () {
						return olMap.map.getView().calculateExtent(olMap.map.getSize());
					},
					scale: function () {
						return Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
					}
				}

				let sUrl = contextRoot + '/mntr/unfinishedEventGeoData.json';
				if (typeof option != 'undefined') {
					if (typeof option.evtId != 'undefined') {
						oData.searchEvtId = option.evtId;
					}
					if (typeof option.evtIds != 'undefined') {
						oData.searchEvtIds = option.evtIds;
					}
					if (typeof option.userEventYn != 'undefined') {
						oData.userEventYn = option.userEventYn;
					}
					if (typeof option.lk112Yn != 'undefined') {
						oData.lk112Yn = option.lk112Yn;
					}
					if (typeof option.mntrTy != 'undefined') {
						oData.searchMntrTy = option.mntrTy;
					}
					if (typeof option.url != 'undefined') {
						sUrl = option.url;
					}
				}

				let olLayer = new ol.layer.Vector({
					renderMode: 'vector',
					zIndex: 50,
					source: new ol.source.Vector({
						loader: function (extent, resolution, projection) {
							$.ajax({
								type: 'POST',
								url: sUrl,
								data: oData,
								beforeSend: function (xhr) {
									olSwipMap.spinner.spin(document.getElementById('map'));
									if (!olMap.layers.event.getSource().isEmpty()) {
										olMap.layers.event.getSource().clear();
									}
								},
								success: function (result, status, xhr) {
									let olFeatures = new ol.format.GeoJSON().readFeatures(result);
									let olSource = olMap.layers.event.getSource();
									olSource.addFeatures(olFeatures);
								},
								error: function (xhr, status, error) {
									console.log(xhr, status, error);
								},
								complete: function (xhr, status) {
									olSwipMap.spinner.stop();
								}
							});
						},
						strategy: ol.loadingstrategy.bbox
					}),
					style: function (feature) {
						let oProperties = feature.getProperties();
						let nSize = oConfigure.iconSize;
						let nIconSize = 40;
						let sPrefix = 'event/typeA/';
						let sSuffix = '.png';

						// ICON TYPE
						if (oConfigure.iconTy == 'B') {
							sPrefix = 'event/typeB/';
							nIconSize = 50;
						}

						let nIconScale = parseFloat(oConfigure.iconSize) / nIconSize;
						nIconScale = nIconScale.toFixed(2);

						let nGisLabelViewScale = parseInt(oConfigure.gisLabelViewScale);
						let nGisFeatureViewScale = parseInt(oConfigure.gisFeatureViewScale);
						let nViewScale = Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
						let nAnchor = nIconSize / 2;
						return new ol.style.Style({
							image: new ol.style.Icon(({
								anchor: [
									nAnchor, (nAnchor - 25)
								],
								anchorXUnits: 'pixels',
								anchorYUnits: 'pixels',
								size: [
									nIconSize, nIconSize
								],
								// offset : [0, -25],
								scale: nIconScale,
								opacity: 0.7,
								src: contextRoot + '/images/mntr/gis/' + sPrefix + oProperties.evtId + sSuffix
							})),
							text: new ol.style.Text({
								font: 'bold 11px NanumGothic',
								text: (nViewScale < nGisLabelViewScale) ? oProperties.evtNm : '',
								offsetY: -10,
								fill: new ol.style.Fill({
									color: 'rgba(0, 0, 0, 0.7)'
								}),
								stroke: new ol.style.Stroke({
									color: 'rgba(251, 250, 170, 0.7)',
									width: 3
								})
							})
						});
					}
				});
				olMap.layers.event = olLayer;
				olMap.map.addLayer(olMap.layers.event);
			}
		},

		traffic: {
			init: function (isVisible) {
				if (oGis.urlUti != '') {

					//let sUcpId = oConfigure.ucpId.toString().toLowerCase();	
					let sGeoLdreg = oConfigure.dstrtCd.toString().toLowerCase();
					if ('11545' == sGeoLdreg) {
						sGeoLdreg = 'com';
					}		// 서울금천구

					let sLayers = 'cite:geo_its_link_' + sGeoLdreg;
					olMap.layers.traffic = new ol.layer.Image({
						zIndex: 30,
						visible: isVisible,
						source: new ol.source.ImageWMS({
							ratio: 1,
							url: oGis.urlUti,
							params: {
								'FORMAT': 'image/png',
								'VERSION': '1.1.1',
								'LAYERS': sLayers,
								'exceptions': 'application/vnd.ogc.se_inimage',
								'TIMESTAMP': new Date().getTime()
							},
							crossOrigin: 'anonymous',
						}),
						opacity: 0.7
					});
					olMap.map.addLayer(olMap.layers.traffic);

					setInterval(function () {
						let isVisible = olMap.layers.traffic.getVisible();
						if (isVisible) {
							olMap.layers.traffic.getSource().updateParams({
								'TIMESTAMP': new Date().getTime()
							});
						}
					}, 1000 * 60);
				}
			}
		},

		angle: {
			visible: false,
			init: function (visible) {
				// console.log('olSwipMap.layers.angle.init %s', visible);
				let olLayer = new ol.layer.Vector({
					renderMode: 'vector',
					zIndex: 10,
					visible: visible,
					source: new ol.source.Vector()
				});

				this.visible = visible;
				olMap.layers.angle = olLayer;
				olMap.map.addLayer(olMap.layers.angle);
			},
			refresh: function (visible) {
				let isInitLayerFclt = ol.layer.Vector.prototype.isPrototypeOf(olMap.layers.fclt);
				if (isInitLayerFclt) {
					// console.log('olSwipMap.layers.angle.refresh %s, %s', visible, isInitLayerFclt);
					if (typeof visible != 'undefined') this.visible = visible;
					let isCluster = olSwipMap.layers.fclt.cluster;
					let olSource = olMap.layers.fclt.getSource();
					if (ol.source.Cluster.prototype.isPrototypeOf(olSource) && isCluster) {
						olMap.layers.angle.setVisible(false);
					} else if (ol.source.Vector.prototype.isPrototypeOf(olSource) && isCluster) {
						let nGisFeatureViewScale = parseInt(oConfigure.gisFeatureViewScale);
						let nViewScale = Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
						let isVisibleFclt = olMap.layers.fclt.getVisible();
						if (isVisibleFclt && nGisFeatureViewScale >= nViewScale) {
							olMap.layers.angle.setVisible(this.visible);
						} else {
							olMap.layers.angle.setVisible(false);
						}
					} else {
						let nGisFeatureViewScale = parseInt(oConfigure.gisFeatureViewScale);
						let nViewScale = Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
						let isVisibleFclt = olMap.layers.fclt.getVisible();
						if (isVisibleFclt && nGisFeatureViewScale >= nViewScale) {
							olMap.layers.angle.setVisible(this.visible);
						} else {
							olMap.layers.angle.setVisible(false);
						}
					}
				}
			}
		},

		trace: {
			init: function () {
				let olLayer = new ol.layer.Vector({
					source: new ol.source.Vector(),
					style: function (feature) {
						let oProperties = feature.getProperties();
						let olGeometry = feature.getGeometry();
						let sType = olGeometry.getType();
						let oCoordinates = olGeometry.getCoordinates();

						let oStyles = [];

						if (sType == 'LineString') {
							oStyles.push(new ol.style.Style({
								stroke: new ol.style.Stroke({
									color: '#db7734',
									width: 5
								})
							}));

							let sId = feature.getId();
							if (typeof sId != 'undefined') {
								let oSplit = sId.split('_');
								if (oSplit.length == 3) {
									let olIconCar = new ol.style.Icon({
										src: contextRoot + '/images/mntr/gis/car/' + oSplit[1] + '.png',
										anchor: [
											0.75, 0.5
										],
										opacity: 0.8,
									});

									let olTextCar = new ol.style.Text({
										font: 'bold 11px NanumGothic',
										text: oSplit[2],
										offsetX: 40,
										offsetY: 0,
										fill: new ol.style.Fill({
											color: '#000'
										}),
										stroke: new ol.style.Stroke({
											color: '#fff',
											width: 3
										})
									});

									if (oCoordinates.length == 1) {
										oStyles.push(new ol.style.Style({
											geometry: new ol.geom.Point(oCoordinates[0]),
											image: olIconCar,
											text: olTextCar
										}));
									} else {
										let nIndex = 0;
										olGeometry.forEachSegment(function (start, end) {
											let nDx = end[0] - start[0];
											let nDy = end[1] - start[1];
											let nRotation = Math.atan2(nDx, nDy);
											if (nIndex != 0) {
												oStyles.push(new ol.style.Style({
													geometry: new ol.geom.Point(start),
													image: new ol.style.Icon({
														src: contextRoot + '/images/mntr/gis/arrow_blue.png',
														anchor: [
															0.75, 0.5
														],
														rotateWithView: true,
														rotation: nRotation + (90 * Math.PI / 180),
														opacity: 1,
													})
												}));
											} else {
												oStyles.push(new ol.style.Style({
													geometry: new ol.geom.Point(start),
													image: olIconCar,
													text: olTextCar
												}));
											}
											nIndex++;
										});
									}
								}
							} else {
								let nIndex = 0;
								olGeometry.forEachSegment(function (start, end) {
									let nDx = end[0] - start[0];
									let nDy = end[1] - start[1];
									let nRotation = Math.atan2(nDx, nDy);
									if (nIndex != 0) {
										oStyles.push(new ol.style.Style({
											geometry: new ol.geom.Point(start),
											image: new ol.style.Icon({
												src: contextRoot + '/images/mntr/gis/arrow.png',
												anchor: [
													0.75, 0.5
												],
												rotateWithView: true,
												rotation: nRotation + (90 * Math.PI / 180),
												opacity: 0.8,
											})
										}));
									}
									nIndex++;
								});
							}
						} else if (sType == 'Point') {

							let oMoment = moment(oProperties.pointYmdHms, 'YYYYMMDDHHmmss');
							let olTextCar = new ol.style.Text({
								font: 'bold 11px NanumGothic',
								text: '[' + oProperties.rowNum + '] ' + oMoment.format('MM-DD HH:mm'),
								offsetX: 40,
								offsetY: 0,
								fill: new ol.style.Fill({
									color: '#000'
								}),
								stroke: new ol.style.Stroke({
									color: '#fff',
									width: 3
								})
							});

							return new ol.style.Style({
								geometry: olGeometry,
								text: olTextCar
							});
						}
						return oStyles;
					}
				});

				olMap.layers.trace = olLayer;
				olMap.map.addLayer(olMap.layers.trace);
			}
		},

		distribution: {
			init: function () {
				let olLayer = new ol.layer.Vector({
					renderMode: 'vector',
					zIndex: 10,
					source: new ol.source.Vector()
				});

				olMap.layers.distribution = olLayer;
				olMap.map.addLayer(olMap.layers.distribution);
			}
		},

		lineSig: {
			init: function (visible) {
				if (oGis.urlWms != '') {
					visible = typeof visible != 'undefined' ? visible : true;

					let sCqlFilter = 'SIG_CD = ' + "'" + oConfigure.sigunguCd + "'";
					if (oConfigure.lgDongCdBase) sCqlFilter = 'SIG_CD IN (' + oConfigure.lgDongCdBase + ')';

					olMap.layers.lineSig = new ol.layer.Image({
						zIndex: 15,
						visible: visible,
						source: new ol.source.ImageWMS({
							ratio: 1,
							url: oGis.urlWms,
							params: {
								'FORMAT': 'image/png',
								'VERSION': '1.1.1',
								'LAYERS': 'cite:geo_line_sig',
								'exceptions': 'application/vnd.ogc.se_inimage',
								'CQL_FILTER': sCqlFilter
							},
							crossOrigin: 'anonymous',
						})
					});

					olMap.map.addLayer(olMap.layers.lineSig);
				}
			}
		},

		lineDong: {
			init: function (visible) {
				if (oGis.urlWms != '') {
					visible = typeof visible != 'undefined' ? visible : true;

					let sCqlFilter = 'EMD_CD LIKE ' + "'" + oConfigure.sigunguCd + "%'";
					if (oConfigure.lgDongCdBase) {
						let oSplit = oConfigure.lgDongCdBase.toString().split(',');
						$.each(oSplit, (index, lgDongCd) => {
							if (index != 0) {
								sCqlFilter += ' OR EMD_CD LIKE ' + "'" + lgDongCd + "%'";
							} else {
								sCqlFilter = 'EMD_CD LIKE ' + "'" + lgDongCd + "%'";
							}
						});
					}

					olMap.layers.lineDong = new ol.layer.Image({
						zIndex: 20,
						visible: visible,
						source: new ol.source.ImageWMS({
							ratio: 1,
							url: oGis.urlWms,
							params: {
								'FORMAT': 'image/png',
								'VERSION': '1.1.1',
								'LAYERS': 'cite:geo_line_dong',
								'exceptions': 'application/vnd.ogc.se_inimage',
								'CQL_FILTER': sCqlFilter
							},
							crossOrigin: 'anonymous',
						})
					});

					olMap.map.addLayer(olMap.layers.lineDong);
				}
			}
		},
		lineLi: {
			init: function (visible) {
				if (oGis.urlWms != '') {
					visible = typeof visible != 'undefined' ? visible : true;

					let sCqlFilter = 'LI_CD LIKE ' + "'" + oConfigure.sigunguCd + "%'";
					if (oConfigure.lgDongCdBase) {
						let oSplit = oConfigure.lgDongCdBase.toString().split(',');
						$.each(oSplit, (index, lgDongCd) => {
							if (index != 0) {
								sCqlFilter += ' OR LI_CD LIKE ' + "'" + lgDongCd + "%'";
							} else {
								sCqlFilter = 'LI_CD LIKE ' + "'" + lgDongCd + "%'";
							}
						});
					}

					olMap.layers.lineLi = new ol.layer.Image({
						zIndex: 25,
						visible: visible,
						source: new ol.source.ImageWMS({
							ratio: 1,
							url: oGis.urlWms,
							params: {
								'FORMAT': 'image/png',
								'VERSION': '1.1.1',
								'LAYERS': 'cite:geo_line_li',
								'exceptions': 'application/vnd.ogc.se_inimage',
								'CQL_FILTER': sCqlFilter
							},
							crossOrigin: 'anonymous',
						})
					});
					olMap.map.addLayer(olMap.layers.lineLi);
				}
			}
		},

		dropdownLayers: function (selector, layerGrpId, callback) {
			if (typeof layerGrpId === 'undefined') {
				layerGrpId = '';
			}
			let $dropdownLayers = $(selector);
			if ($dropdownLayers.length) {
				$.ajax({
					type: 'POST',
					url:contextRoot + '/mntr/configure/selectLayerMngList.json',
					async: false,
					data: {
						searchFcltKndCd: layerGrpId
					},
					success: function (data) {
						let oLayerGrpList = data.layerGrpList;
						let oLayerList = data.layerList;
						let $ul = $('<ul/>', {
							'role': 'menu',
							'id': 'layerList',
							'class': 'dropdown-menu',
							'aria-labelledby': 'Layers'
						});
						let $divider = $('<li/>', {
							'role': 'separator',
							'class': 'divider'
						});

						if (oLayerGrpList.length == 1 && oLayerGrpList[0].layerGrpId == 'ETC') {
							// Do Nothing
						} else {
							$ul.append($divider.clone());

							$ul.append($('<li/>', {
								'role': 'presentation',
								'class': 'dropdown-header',
								'text': '시설물 레이어'
							}));

							let $liAll = $('<li/>', {
								'role': 'presentation',
								'class': 'item-all'
							});

							let sButtons = '';
							sButtons += '<div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">';
							sButtons += '		<div class="btn-group" role="group" aria-label="First group">';
							sButtons += '			<button type="button" class="btn btn-default btn-sm" onclick="javascript:olSwipMap.layers.showFclt(\'Y\');">전체선택</button>';
							sButtons += '			<button type="button" class="btn btn-default btn-sm" onclick="javascript:olSwipMap.layers.showFclt(\'N\');">전체해제</button>';
							sButtons += '		</div>';
							sButtons += '</div>';
							$liAll.append(sButtons);

							$liAll.appendTo($ul);
						}

						if (typeof oLayerGrpList !== 'undefined' || oLayerGrpList !== null) {
							$.each(oLayerGrpList, function (index, oLayerGrp) {
								let sLayerGrpNm = oLayerGrp.layerGrpNm;
								let sLayerGrpId = oLayerGrp.layerGrpId;

								let $liHeader = $('<li/>', {
									'role': 'presentation',
									'class': 'dropdown-header',
									'text': sLayerGrpNm + ' (' + sLayerGrpId + ')'
								});
								$divider.clone().appendTo($ul);
								$liHeader.appendTo($ul);

								let $liContainer = $('<li/>', {
									'role': 'presentation',
									'id': sLayerGrpId,
									'class': sLayerGrpId == 'ETC' ? 'item-etc' : 'item-fclt'
								});
								$liContainer.appendTo($ul);
							});

							$.each(oLayerList, function (index, oLayer) {
								let sLayerGrpNm = oLayer.layerGrpNm;
								let sLayerGrpId = oLayer.layerGrpId;
								let sLayerId = oLayer.layerId;
								let sLayerNm = oLayer.layerNm;
								let sCheckYn = oLayer.checkYn;

								let $anchor = $('<a/>', {
									'role': 'menuitem',
									'href': 'javascript:void(0);'
								});

								if ('Y' == sCheckYn) {
									$anchor.append($('<input/>', {
										'type': 'checkbox',
										'data-layer-id': sLayerId,
										'data-layer-grp-id': sLayerGrpId,
										'checked': 'checked'
									}));
								} else {
									$anchor.append($('<input/>', {
										'type': 'checkbox',
										'data-layer-id': sLayerId,
										'data-layer-grp-id': sLayerGrpId
									}));
								}

								if ('ETC' == sLayerGrpId) {
									if (sLayerId == 'TRAFFIC') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fas fa-road fa-2x"></i>'
										}));
									} else if (sLayerId == 'LINESIG') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fas fa-bezier-curve fa-2x"></i>'
										}));
									} else if (sLayerId == 'LINEDONG') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fab fa-hubspot fa-2x"></i>'
										}));
									} else if (sLayerId == 'LINELI') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fab fa-yandex-international fa-2x"></i>'
										}));
									} else if (sLayerId == 'INDEXMAP') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fas fa-map-marked-alt fa-2x"></i>'
										}));
									} else if (sLayerId == 'ANGLE') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fas fa-location-arrow fa-2x"></i>'
										}));
									} else if (sLayerId == 'OVERVIEWMAP') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fa fa-map fa-2x"></i>'
										}));
									} else if (sLayerId == 'CLUSTERFCLT') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fas fa-th-list fa-2x"></i>'
										}));
									} else if (sLayerId == 'FCLTLBLNM') {
										$anchor.append($('<span/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px;',
											'html': '<i class="fas fa-font fa-2x"></i>'
										}));
									} else {
										$anchor.append($('<img/>', {
											'alt': sLayerNm,
											'title': sLayerNm,
											'style': 'margin: 0px 5px; width: 30px; height: 30px;',
											'src': contextRoot + '/images/mntr/gis/etc/type' + oConfigure.iconTy + '/' + sLayerId + '.png'
										}));
									}
									let isChecked = oLayer.checkYn == 'Y' ? true : false;
									olSwipMap.layers.showEtc(sLayerId, isChecked);
								} else {
									$anchor.append($('<img/>', {
										'alt': sLayerNm,
										'title': sLayerNm,
										'style': sLayerGrpId == 'CTV' ? 'width: 40px; height: 40px;' : 'width: 30px; height: 30px; margin: 0px 5px;',
										'src': contextRoot + '/images/mntr/gis/fclt/type' + (oConfigure.iconTy == 'A' ? 'A' : oConfigure.iconTy) + '/' + sLayerId + '_0_0.png'
									}));
								}

								$anchor.append($('<span/>', {
									'class': 'lbl',
									'text': sLayerNm
								}));

								let $li = $ul.find('li#' + sLayerGrpId);
								/*
								if ($li.find('a').length > 0 & $li.find('a').length % 3 == 0) {
									// $li.append('<br/>');
								}
								*/
								$anchor.appendTo($li);
							});

							$ul.append($divider.clone());
							$dropdownLayers.append($ul);

							$ul.on('change', 'input:checkbox', function () {
								let oData = $(this).data();
								let isChecked = $(this).prop('checked');
								let sLayerGrpId = oData.layerGrpId;
								let sLayerId = oData.layerId;

								$.post(contextRoot + '/mntr/configure/updateLayerMng.json', {
									layerId: sLayerId,
									checkYn: isChecked ? 'Y' : 'N'
								}, function () {
									if (sLayerGrpId == 'ETC') {
										olSwipMap.layers.showEtc(sLayerId, isChecked);
										if (sLayerId == 'INDEXMAP' && isChecked) {
											olSwipMap.layers.showEtc('OVERVIEWMAP', false);
											$('a[role="menuitem"] input[data-layer-id="OVERVIEWMAP"]').prop('checked', false);
										} else if (sLayerId == 'OVERVIEWMAP' && isChecked) {
											olSwipMap.layers.showEtc('INDEXMAP', false);
											$('a[role="menuitem"] input[data-layer-id="INDEXMAP"]').prop('checked', false);
										}
									} else {
										if (olMap.layers.angle) olMap.layers.angle.getSource().clear();

										if (olSwipMap.layers.fclt.source.vector) {
											olSwipMap.layers.fclt.source.vector.clear();
										}
									}
								});
							});
						}

						// CALLBACK
						if (typeof callback == 'function') {
							callback();
						}

						// EVENTS
						$('ul#layerList').on('click', function (event) {
							event.stopPropagation();
						});
					},
					error: function () {
						alert("레이어 정보를 가져오지 못했습니다.");
					}
				});
			} else {
				// TODO Hide INDEX MAP
			}
		},
		showEtc: function (layerId, isChecked) {
			if (layerId == 'TRAFFIC') {
				if (ol.layer.Image.prototype.isPrototypeOf(olMap.layers.traffic)) {
					olMap.layers.traffic.setVisible(isChecked);
				} else {
					olSwipMap.layers.traffic.init(isChecked);
				}
			} else if (layerId == 'LINESIG') {
				if (ol.layer.Image.prototype.isPrototypeOf(olMap.layers.lineSig)) {
					olMap.layers.lineSig.setVisible(isChecked);
				} else {
					olSwipMap.layers.lineSig.init(isChecked);
				}
			} else if (layerId == 'LINEDONG') {
				if (ol.layer.Image.prototype.isPrototypeOf(olMap.layers.lineDong)) {
					olMap.layers.lineDong.setVisible(isChecked);
				} else {
					olSwipMap.layers.lineDong.init(isChecked);
				}
			} else if (layerId == 'LINELI') {
				if (ol.layer.Image.prototype.isPrototypeOf(olMap.layers.lineLi)) {
					olMap.layers.lineLi.setVisible(isChecked);
				} else {
					olSwipMap.layers.lineLi.init(isChecked);
				}
			} else if (layerId == 'INDEXMAP' & isChecked) {
				olSwipMap.indexmap.show();
				if ($('li.item-etc input[data-layerid=OVERVIEWMAP]').length) {
					$('li.item-etc input[data-layerid=OVERVIEWMAP]').prop('checked', false);
					olSwipMap.layers.showEtc('OVERVIEWMAP', false);
				}
			} else if (layerId == 'INDEXMAP' & !isChecked) {
				olSwipMap.indexmap.hide();
			} else if (layerId == 'ANGLE') {
				setTimeout(() => {
					olSwipMap.layers.angle.refresh(isChecked);
				}, 1000);
			} else if (layerId == 'OVERVIEWMAP') {
				if (isChecked) {
					olMap.controls.overviewmap = new ol.control.OverviewMap({
						view: new ol.View({
							projection: oGis.projection,
							extent: olMap.extent,
							minZoom: olMap.zoom.min,
							maxZoom: olMap.zoom.max
						}),
						layers: [
							olMap.layers.base
						],
						label: $('<i/>', {
							'class': 'far fa-caret-square-left'
						})[0],
						collapseLabel: $('<i/>', {
							'class': 'far fa-caret-square-right'
						})[0],
						collapsed: false,
						tipLabel: '접기/펴기'
					});
					olMap.map.addControl(olMap.controls.overviewmap);
					$('.ol-overviewmap').css({
						'left': 'inherit',
						'right': '.5em'
					});
					if ($('li.item-etc input[data-layerid=INDEXMAP]').length) {
						$('li.item-etc input[data-layerid=INDEXMAP]').prop('checked', false);
						olSwipMap.layers.showEtc('INDEXMAP', false);
					}
				} else {
					olMap.map.removeControl(olMap.controls.overviewmap);
					olMap.controls.overviewmap = null;
				}
			} else if (layerId == 'CLUSTERFCLT') {
				let isCluster = olSwipMap.layers.fclt.cluster;
				if (isChecked && !isCluster) {
					olSwipMap.layers.fclt.cluster = true;
					olSwipMap.layers.fclt.data.searchIgnoreScaleYn = 'Y';
				} else {
					olSwipMap.layers.fclt.cluster = false;
					olSwipMap.layers.fclt.data.searchIgnoreScaleYn = 'N';
				}
				olSwipMap.layers.fclt.refresh();
			} else if (layerId == 'FCLTLBLNM') {
				olSwipMap.layers.fclt.refresh();
			}
		},
		showFclt: function (checkYn) {
			$.post(contextRoot + '/mntr/configure/updateAllLayerMng.json', {
				checkYn: checkYn
			}, function () {
				let $itemFclt = $('#layerList .item-fclt input[type=checkbox]');
				if (checkYn == 'Y') {
					$itemFclt.prop('checked', true);
				} else {
					$itemFclt.prop('checked', false);
				}
				if (olMap.layers.angle) olMap.layers.angle.getSource().clear();
				olSwipMap.layers.fclt.source.vector.clear();
			});
		}
	},

	mntr: {
		// overlays : [],
		clear: function () {
			// Vector Layer 정리
			let olSource = olMap.layers.vector.getSource();
			olSource.clear();
			olSwipMap.point.clearAll();
		},
		/**
		 * @description 투망감시 기능.
		 * @alias olSwipMap.mntr.castnet
		 *
		 * <pre>
		 * HTML Elements Tag 작성시 attribute 에 해당 항목을 추가 해줘야한다.
		 * data-point-x : 좌표 x
		 * data-point-y : 좌표 y
		 * data-projection : EPSG:4326, ...
		 * data-fclt-id : 시설물 ID
		 * data-evt-ocr-no : 이벤트 발생 번호
		 * </pre>
		 *
		 * @param element
		 *			{Object} Elements Object
		 */
		castnet: function (element) {
			if (typeof element === 'object') {
				let oData = element;
				if (element instanceof HTMLElement) oData = $(element).data();

				delete oData.geometry;

				let sPointX = oData.pointX || '';
				let sPointY = oData.pointY || '';
				let sProjection = oData.projection || 'EPSG:4326';
				let sEvtId = oData.evtId || '';
				let sEvtOcrNo = oData.evtOcrNo || '';
				let sFcltId = oData.fcltId || '';
				let sOcrFcltId = oData.ocrFcltId || '';
				if (sOcrFcltId != '') {
					sFcltId = sOcrFcltId;
				}
				let sWideShareRds = oData.wideShareRds || oConfigure.wideShareRds;
				if (oConfigure.sysId == 'WIDE') {
					oConfigure.cctvViewRads = sWideShareRds;
					console.log('set castnet wideShareRds %s', sWideShareRds)
				}

				if (sEvtId && sEvtOcrNo) {
					doDivSituation(sEvtId, sEvtOcrNo, 'N');
				}

				if (sFcltId && sEvtOcrNo) {
					$.post(contextRoot + '/mntr/fcltById.json', {
						fcltId: sFcltId
					}).done(function (data) {
						sPointX = data.pointX;
						sPointY = data.pointY;
						sProjection = 'EPSG:4326';
					});
				}

				if ((!sPointX || !sPointY || !sProjection) && sEvtOcrNo) {
					$.post(contextRoot + '/mntr/eventById.json', {
						evtOcrNo: sEvtOcrNo
					}).done(function (data) {
						sPointX = data.pointX;
						sPointY = data.pointY;
						sProjection = 'EPSG:4326';
					});
				}

				if (!sPointX || !sPointY || !sProjection) {
					oCommon.modalAlert('modal-notice', '알림', '좌표 정보가 없습니다.<br>경도 : ' + sPointX + ', 위도 : ' + sPointY);
					return false;
				}

				let nPointX = parseFloat(sPointX);
				let nPointY = parseFloat(sPointY);
				if (!isNaN(nPointX) && !isNaN(nPointY) && sProjection) {
					if ((nPointX > oGis.boundsLeft) && (nPointX < oGis.boundsRight) && (nPointY > oGis.boundsBottom) && (nPointY < oGis.boundsTop)) {
						let oCoordinate = [
							nPointX, nPointY
						];

						let oCoordinateWgs84 = [];

						let sProjectionCode = olMap.view.getProjection().getCode();
						// 좌표 변환
						if (sProjection != sProjectionCode) {
							oCoordinate = ol.proj.transform(oCoordinate, sProjection, sProjectionCode);
						}

						if (sProjection == 'EPSG:4326') {
							oCoordinateWgs84 = [
								nPointX, nPointY
							];
						} else {
							oCoordinateWgs84 = ol.proj.transform([
								nPointX, nPointY
							], sProjection, 'EPSG:4326');
						}

						if (oConfigure.ptzCntrTy == 'NO') {
							olSwipMap.point.set(oCoordinate, sProjectionCode, 'RED', true, false, true);
							return false;
						}

						$.post(contextRoot + '/mntr/castNetCctvList.json', {
							pointX: oCoordinateWgs84[0],
							pointY: oCoordinateWgs84[1],
							fcltId: sFcltId,
						}).done(function (data) {
							if (data.cctv.length) {
								// PLAY VIDEO
								oVmsCommon.castnet(data.cctv);

								olSwipMap.mntr.clear();
								let olSource = olMap.layers.vector.getSource();
								// 반경 그리기

								let nRadsRoute = oConfigure.radsRoute;
								if (isNaN(nRadsRoute) || nRadsRoute == 0) {
									nRadsRoute = 500;
								}

								olSwipMap.draw.circle(oCoordinate, sProjectionCode, nRadsRoute * 2);
								olSwipMap.draw.circle(oCoordinate, sProjectionCode, nRadsRoute);
								olSwipMap.draw.circle(oCoordinate, sProjectionCode, 50);

								let olFeaturesCctv = new ol.format.GeoJSON().readFeatures(data.geoJson);
								$.each(olFeaturesCctv, function (i, v) {
									const oProperties = v.getProperties();
									oProperties.assignIndex = i + 1;
									olSwipMap.cctv.addFeature(oProperties);

									const oCoordinateCctv = v.getGeometry().getCoordinates();
									olSwipMap.draw.circle(oCoordinateCctv, sProjectionCode, 50, 0, undefined, new ol.style.Style({
										stroke: new ol.style.Stroke({
											color: new ol.color.asArray([
												0, 0, 0, 0.5
											]),
											width: 1,
											lineDash: [
												4, 4
											]
										})
									}), oProperties);

								});
								olMap.setCenter(oCoordinate, sProjectionCode);
								if (typeof data.preset != 'undefined') {
									const olFeaturesPreset = new ol.format.GeoJSON().readFeatures(data.preset);
									$.each(olFeaturesPreset, function (i, v) {
										const oProperties = v.getProperties();
										v.setId(oProperties.fcltId + '_' + oProperties.presetNum);
										if (oProperties.hasOwnProperty('geometry')) {
											delete oProperties.geometry;
										}
										const oCoordinatePreset = v.getGeometry().getCoordinates();

										let olFillColor = [255, 255, 255, 1];
										let olStrokeColor = [0, 0, 0, 0.2];

										if (oProperties.rank == 1) {
											olStrokeColor = [143, 188, 143, 1];
										}

										v.setStyle(new ol.style.Style({
											geometry: new ol.geom.Point(oCoordinatePreset),
											image: new ol.style.Circle({
												radius: 10,
												fill: new ol.style.Fill({
													color: new ol.color.asArray(olFillColor)
												}),
												stroke: new ol.style.Stroke({
													color: new ol.color.asArray(olStrokeColor),
													width: 5
												})
											}),
											text: new ol.style.Text({
												font: 'bold 11px',
												fill: new ol.style.Fill({
													color: '#000'
												}),
												text: (parseInt(oProperties.presetNum) % 10).toString(),
												offsetX: 0,
												offsetY: 0,
												rotation: 0
											})
										}));

										const oCoordinateCctv = ol.proj.transform([
											oProperties.cctvX, oProperties.cctvY
										], 'EPSG:4326', sProjectionCode);


										const olLineFeature = new ol.Feature({
											geometry: new ol.geom.LineString([
												oCoordinateCctv, oCoordinatePreset
											])
										});

										olLineFeature.setStyle(new ol.style.Style({
											stroke: new ol.style.Stroke({
												color: new ol.color.asArray(olStrokeColor),
												width: 3
											})
										}));

										olLineFeature.setProperties(oProperties);
										olSource.addFeatures([v, olLineFeature]);
									});
								}
							} else {
								collapse({
									bottom: true
								}, () => {
									oCommon.modalAlert('modal-notice', '알림', 'CCTV 정보가 없습니다.');
									olSwipMap.mntr.clear();
								});
							}
						});
					} else {
						collapse({
							bottom: true
						}, () => {
							oCommon.modalAlert('modal-notice', '알림', '좌표 범위가 벗어납니다.<br>경도 : ' + nPointX + ', 위도 : ' + nPointY);
							olSwipMap.mntr.clear();
						});
					}
				} else {
					collapse({
						bottom: true
					}, () => {
						oCommon.modalAlert('modal-notice', '알림', '좌표 정보가 없습니다.');
						olSwipMap.mntr.clear();
					});
				}
			}
		},
		/**
		 * @description 궤적감시 기능.
		 * @alias olSwipMap.mntr.linestring
		 */
		linestring: {
			layer: null,
			source: null,
			sketch: null,
			helpTooltipElement: null,
			helpTooltip: null,
			interaction: {
				draw: null,
				modify: null,
				snap: null
			},
			listeners: {
				sketch: null,
				pointermove: null,
				mouseout: null
			},
			init: function () {
				if (this.layer == null) {
					this.source = new ol.source.Vector();
					this.layer = new ol.layer.Vector({
						zIndex: 60,
						source: this.source,
						style: new ol.style.Style({
							fill: new ol.style.Fill({
								color: 'rgba(255, 255, 255, 0.2)'
							}),
							stroke: new ol.style.Stroke({
								color: '#9e83e3',
								width: 3
							}),
							image: new ol.style.Circle({
								radius: 7,
								fill: new ol.style.Fill({
									color: '#ffcc33'
								})
							})
						})
					});
					olMap.map.addLayer(this.layer);
				}
				this.createHelpTooltip();
			},
			draw: function () {
				if (this.layer == null) {
					this.init();
				} else {
					this.clear(false);
				}

				if (olSwipMap.measure.isEnabled) {
					alert('지도 측정 기능 해제 후 진행하세요.');
					return false;
				}

				if (this.interaction.draw == null) {
					this.interaction.draw = new ol.interaction.Draw({
						source: this.source,
						type: 'LineString'
					});
					olMap.map.addInteraction(this.interaction.draw);

					this.interaction.draw.on('drawstart', function (event) {
						let oLinestring = olSwipMap.mntr.linestring;
						oLinestring.sketch = event.feature;
						oLinestring.listeners.sketch = oLinestring.sketch.getGeometry().on('change', function (event) {
							let oLinestring = olSwipMap.mntr.linestring;
							let sHtml = '클　　릭 : 경유지 설정.<br>더블클릭 : 경로 설정 완료.';
							oLinestring.helpTooltipElement.innerHTML = sHtml;
						});
					}, this);

					this.interaction.draw.on('drawend', function (event) {
						let oLinestring = olSwipMap.mntr.linestring;
						oLinestring.helpTooltipElement.classList.add('hidden');
						oLinestring.sketch = null;
						ol.Observable.unByKey(oLinestring.listeners.sketch);
						ol.Observable.unByKey(oLinestring.listeners.mouseout);
						ol.Observable.unByKey(oLinestring.listeners.pointermove);
						oLinestring.listeners.sketch = null;
						oLinestring.listeners.mouseout = null;
						oLinestring.listeners.pointermove = null;
						setTimeout(function () {
							olMap.map.removeInteraction(oLinestring.interaction.draw);
							oLinestring.interaction.draw = null;
							oLinestring.modifyEnd();
						}, 500);
					}, this);

					this.listeners.mouseout = olMap.map.getViewport().addEventListener('mouseout', function () {
						let oLinestring = olSwipMap.mntr.linestring;
						if (oLinestring.helpTooltipElement != null) {
							oLinestring.helpTooltipElement.classList.add('hidden');
						}
					});

					this.listeners.pointermove = olMap.map.on('pointermove', function (event) {
						if (event.dragging) {
							return;
						}
						let oLinestring = olSwipMap.mntr.linestring;
						if (oLinestring.source.isEmpty()) {
							oLinestring.helpTooltipElement.classList.remove('hidden');
							oLinestring.helpTooltipElement.innerHTML = '클릭 : 출발지 설정.';
							oLinestring.helpTooltip.setPosition(event.coordinate);
						}
					});
				}
			},
			modify: function () {
				if (olSwipMap.measure.isEnabled) {
					alert('지도 측정 기능 해제 후 진행하세요.');
					return false;
				}

				if (this.source == null) {
					this.init();
				}

				if (this.source.isEmpty()) {
					alert('설정된 경로가 없습니다. 먼저 경로 설정을 해주세요.');
				} else {
					this.interaction.modify = new ol.interaction.Modify({
						source: this.source
					});
					olMap.map.addInteraction(this.interaction.modify);

					this.interaction.snap = new ol.interaction.Snap({
						source: this.source
					});
					olMap.map.addInteraction(this.interaction.snap);

					this.listeners.mouseout = olMap.map.getViewport().addEventListener('mouseout', function () {
						let oLinestring = olSwipMap.mntr.linestring;
						if (oLinestring.helpTooltipElement != null) {
							oLinestring.helpTooltipElement.classList.add('hidden');
						}
					});

					this.listeners.pointermove = olMap.map.on('pointermove', function (event) {
						if (event.dragging) {
							return;
						}
						let oLinestring = olSwipMap.mntr.linestring;
						if (!oLinestring.source.isEmpty()) {
							oLinestring.helpTooltipElement.innerHTML = '지도 상의 경로를 드래그 하여 수정.<br>완료 버튼을 눌러 CCTV 재선별.';
							oLinestring.helpTooltip.setPosition(event.coordinate);
							oLinestring.helpTooltipElement.classList.remove('hidden');
						}
					});
				}
			},
			modifyEnd: function () {
				olSwipMap.mntr.clear();

				if (this.source == null) {
					this.init();
				}

				// 2. 궤적 구역 획득 -> 구역 확장
				// [minx, miny, maxx, maxy]
				if (!this.source.isEmpty()) {
					let olExtent = this.source.getFeatures()[0].getGeometry().getExtent();

					let oCoordinateMin = [
						olExtent[0], olExtent[1]
					];
					let oCoordinateMax = [
						olExtent[2], olExtent[3]
					];

					// 2-1. 위경도 좌표로 변환
					let sProjectionCode = olMap.map.getView().getProjection().getCode();
					let oCoordinateMinWgs84 = ol.proj.transform(oCoordinateMin, sProjectionCode, 'EPSG:4326');
					let oCoordinateMaxWgs84 = ol.proj.transform(oCoordinateMax, sProjectionCode, 'EPSG:4326');
					let oCoordinateMinWgs84New = olSwipMap.util.calcLonLatByDegreeMeters(oCoordinateMinWgs84, 225, 200);
					let oCoordinateMaxWgs84New = olSwipMap.util.calcLonLatByDegreeMeters(oCoordinateMaxWgs84, 45, 200);

					let oParam = {
						left: oCoordinateMinWgs84New[0],
						bottom: oCoordinateMinWgs84New[1],
						right: oCoordinateMaxWgs84New[0],
						top: oCoordinateMaxWgs84New[1]
					};

					// 3. 궤적 구역 내 CCTV 획득
					$.post(contextRoot + '/mntr/fcltGeoDataByBounds.json', oParam, function (data) {
						if (data.cctv.length) {

							let olFlatCoordinates = olSwipMap.mntr.linestring.source.getFeatures()[0].getGeometry().getFlatCoordinates();
							let olLineString = new ol.geom.LineString(olFlatCoordinates, 'XY').clone().transform(sProjectionCode, 'EPSG:4326');

							// 3-1. 궤적과 CCTV 거리 측정
							$.each(data.cctv, function (i, v) {
								let oCoordinateFclt = [
									v.pointX, v.pointY
								];
								let oCoordinateClosest = olLineString.getClosestPoint(oCoordinateFclt);
								let nDistance = olSwipMap.util.getDistance(oCoordinateFclt, oCoordinateClosest);
								v.distance = nDistance;
							});
							// 3-2. CCTV 거리 순으로 정렬
							data.cctv.sort(function (a, b) {
								return a.distance > b.distance ? 1 : -1
							});
							// 3-3. CCTV 거리 순으로 8개 획득
							let oSlice = data.cctv;
							if (data.cctv.length > 8) {
								oSlice = data.cctv.slice(0, 8);
							}
							// 3-4. x 좌표 순으로 정렬
							oSlice.sort(function (a, b) {
								return a.pointX > b.pointX ? 1 : -1
							});

							// 3-5. 지도에 표출
							$.each(oSlice, function (i, v) {
								let olStyle = new ol.style.Style({
									fill: new ol.style.Fill({
										color: new ol.color.asArray([
											0, 0, 0, 0.1
										]),
									}),
									stroke: new ol.style.Stroke({
										color: new ol.color.asArray([
											0, 0, 0, 0.5
										]),
										width: 2,
										lineDash: [
											4, 4
										]
									})
								});
								let sLabel = i < 5 ? 'B' + i : 'R' + (i - 5);
								olSwipMap.draw.circle([
									v.pointX, v.pointY
								], 'EPSG:4326', 50, 180, sLabel, olStyle);

								v.presetNum = sLabel;
							});

							oVmsService.playlists = oSlice;
							if (typeof oMntrLineString != 'undefined') {
								oMntrLineString.cctv.playlists = oSlice;
							}
							// oVmsCommon.showPlaylist({position: 'left'});
						}
					});

					if (this.listeners.mouseout != null) {
						ol.Observable.unByKey(this.listeners.mouseout);
						this.listeners.mouseout = null;
					}
					if (this.listeners.pointermove != null) {
						ol.Observable.unByKey(this.listeners.pointermove);
						this.listeners.pointermove = null;
					}
					if (this.interaction.snap != null) {
						olMap.map.removeInteraction(this.interaction.snap);
						this.interaction.snap = null;
					}
					if (this.interaction.modify != null) {
						olMap.map.removeInteraction(this.interaction.modify);
						this.interaction.modify = null;
					}
				}
			},
			clear: function (isClearLayer) {
				olSwipMap.mntr.clear();
				if (this.source != null) {
					this.source.clear();
				}
				if (this.sketch != null) {
					this.sketch = null;
				}
				if (this.listeners.sketch != null) {
					ol.Observable.unByKey(this.listeners.sketch);
					this.listeners.sketch = null;
				}
				if (this.listeners.mouseout != null) {
					ol.Observable.unByKey(this.listeners.mouseout);
					this.listeners.mouseout = null;
				}
				if (this.listeners.pointermove != null) {
					ol.Observable.unByKey(this.listeners.pointermove);
					this.listeners.pointermove = null;
				}
				if (this.interaction.draw) {
					olMap.map.removeInteraction(this.interaction.draw);
					this.interaction.draw = null;
				}
				if (this.interaction.snap) {
					olMap.map.removeInteraction(this.interaction.snap);
					this.interaction.snap = null;
				}
				if (this.interaction.modify) {
					olMap.map.removeInteraction(this.interaction.modify);
					this.interaction.modify = null;
				}

				if (isClearLayer) {
					if (this.helpTooltipElement != null) {
						this.helpTooltipElement = null;
					}

					if (this.helpTooltip != null) {
						olMap.map.removeOverlay(this.helpTooltip);
						this.helpTooltip = null;
					}

					if (this.layer != null) {
						olMap.map.removeLayer(this.layer);
						this.source = null;
						this.layer = null;
					}
				}

				oVmsService.playlists = [];
				if (typeof oMntrLineString != 'undefined') {
					oMntrLineString.cctv.playlists = [];
				}
				// oVmsCommon.showPlaylist({position: 'left'});
			},
			createHelpTooltip: function () {
				if (this.helpTooltipElement) {
					this.helpTooltipElement.parentNode.removeChild(this.helpTooltipElement);
				}
				this.helpTooltipElement = document.createElement('div');
				this.helpTooltipElement.className = 'ol-tooltip hidden';
				this.helpTooltip = new ol.Overlay({
					element: this.helpTooltipElement,
					offset: [
						15, 0
					],
					positioning: 'center-left'
				});
				olMap.map.addOverlay(this.helpTooltip);
			},
		},
		popover: {
			overlay: null,
			element: null,
			init: function () {
				this.element = $('<div/>', {
					'id': 'ol-overlay-mntr-popover',
				})[0];

				this.overlay = new ol.Overlay({
					element: this.element,
					autoPan: true
				});

				olMap.map.addOverlay(this.overlay);
			},
			open: function (element) {
				if ($(element).is('button')) {
					this.close();
					let oData = $(element).data();
					console.log('-- open(), oData => %o',oData);

					let olView = olMap.map.getView();
					let sProjectionCode = olView.getProjection().getCode();

					if (typeof oData.pointX === 'string' && !isNaN(oData.pointX) && typeof oData.pointY === 'string' && !isNaN(oData.pointY)) {
						oData.pointX = parseFloat(oData.pointX);
						oData.pointY = parseFloat(oData.pointY);
					}

					let oCoordinate = ol.proj.transform([
						oData.pointX, oData.pointY
					], 'EPSG:4326', sProjectionCode);

					olView.setCenter(oCoordinate);

					let $title = $('<div/>', {
						'id': 'ol-overlay-mntr-popover-title'
					});

					$title.append($('<span/>', {
						'text': oData.fcltLblNm
					}));

					$title.append($('<button/>', {
						'type': 'button',
						'class': 'close ol-overlay-close',
						'title': '닫기',
						'onclick': 'javascript:olSwipMap.mntr.popover.close();',
						'html': '<i class="far fa-window-close"></i>'
					}));

					$title.append($('<button/>', {
						'type': 'button',
						'class': 'close ol-overlay-new-window',
						'title': '새창',
						'onclick': 'javascript:oVmsCommon.openVmsPlayer("' + oData.fcltId + '");',
						'html': '<i class="far fa-window-restore"></i>'
					}));

					setTimeout(function () {
						let $container = oVmsService.createVideoElement(oData);
						let elVideo = $container.find('video')[0];
						let nSessionId = $(elVideo).data('sessionId');
						let oPopover = olSwipMap.mntr.popover;
						let elPopover = oPopover.element;
						$(elPopover).data('sessionId', nSessionId);
						oPopover.overlay.setPosition(oCoordinate);
						$(elPopover).popover({
							placement: 'top',
							animation: false,
							html: true,
							title: $title.prop('outerHTML'),
							content: $container
						});
						$(elPopover).popover('show');
						oVmsCommon.play(elVideo);
					}, 500);
				}
			},
			close: function () {
				let nSessionId = $(this.element).data('sessionId');
				if (nSessionId) {
					oVmsService.disconnect(nSessionId);
				}

				$(this.element).popover('destroy');
				this.overlay.setPosition(undefined);
			},
		},
		emergency: function () {
			let oData = $('#table-contextmenu').data();
			let $formEmergency = $('#form-emergency');
			if ($formEmergency.length) {
				$('#form-emergency input[name=pointX]').val();
				$('#form-emergency input[name=pointY]').val();
				$('#form-emergency input[name=jibun]').val();
				$('#form-emergency input[name=road]').val();
				$('#form-emergency input[name=poi]').val();
			} else {
				$formEmergency = $('<form/>', {
					'id': 'form-emergency',
					'method': 'POST',
					'target': 'form-emergency',
					'action': contextRoot + '/mntr/emergency.do',
				});

				$formEmergency.append($('<input/>', {
					'type': 'hidden',
					'name': 'pointX',
					'value': oData.pointX
				}));
				$formEmergency.append($('<input/>', {
					'type': 'hidden',
					'name': 'pointY',
					'value': oData.pointY
				}));
				$formEmergency.append($('<input/>', {
					'type': 'hidden',
					'name': 'jibun',
					'value': oData.jibun
				}));
				$formEmergency.append($('<input/>', {
					'type': 'hidden',
					'name': 'road',
					'value': oData.road
				}));
				$formEmergency.append($('<input/>', {
					'type': 'hidden',
					'name': 'poi',
					'value': oData.poi
				}));

				$formEmergency.appendTo(document.body);
			}
			window.open('about:blank', 'form-emergency', 'status=no,width=' + 600 + ',height=' + 265);
			$formEmergency.submit();
		},
	},

	util: {
		/**
		 * @description 한 경위도 좌표에서 특정 각도로 특정 미터 만큼 이동한 경위도 좌표 값를 얻을 수 있다.
		 * @alias olSwipMap.util.calcLonLatByDegreeMeters
		 * @param coordinate
		 *			{Array} 경도, 위도
		 * @param degree
		 *			{Number} 각도 0˚ ~ 360˚
		 * @param meters
		 *			{Number} 미터 (m)
		 * @return {Array} 계산된 경위도 좌표 값
		 */
		calcLonLatByDegreeMeters: function (coordinate, degree, meters) {
			let nDistance = meters / 1000;
			let nRadiusEarth = 6371.0088;
			let nBrng = degree * (Math.PI / 180);
			// 위도
			let nLatRad = coordinate[1] * (Math.PI / 180);
			let nLatRadNew = Math.sin(nLatRad) * Math.cos(nDistance / nRadiusEarth);
			nLatRadNew += Math.cos(nLatRad) * Math.sin(nDistance / nRadiusEarth) * Math.cos(nBrng);
			nLatRadNew = Math.asin(nLatRadNew);
			// 경도
			let nLatDeg = nLatRadNew * 180 / Math.PI;
			let nLonRad = coordinate[0] * (Math.PI / 180);
			let nA = Math.sin(nBrng) * Math.sin(nDistance / nRadiusEarth) * Math.cos(nLatRad);
			let nB = Math.cos(nDistance / nRadiusEarth) - Math.sin(nLatRad) * Math.sin(nLatRadNew);
			let nLonRadNew = Math.atan2(nA, nB);
			nLonRadNew += nLonRad;
			let nLonDeg = nLonRadNew * 180 / Math.PI;
			return [
				nLonDeg, nLatDeg
			];
		},

		/**
		 * @description 두점 사이의 거리를 측정한다.
		 * @alias olSwipMap.util.getDistance
		 * @return {Number} 계산된 경위도 좌표 값
		 */
		getDistance: function (c1, c2) {
			let nDistance = ol.sphere.getDistance(c1, c2);
			return nDistance;
		},

		/**
		 * @description 파라미터로 받은 meters 만큼 지도상의 반지름으로 변환.
		 * @alias olSwipMap.util.getRadius
		 * @param meters
		 *			{Number} 미터 (m)
		 * @return {Number} the radius of the circle.
		 */
		getRadius: function (meters) {
			let olView = olMap.map.getView();
			let sProjectionCode = olView.getProjection().getCode();
			let nResolution = olView.getResolution();
			let oCenter = olView.getCenter();
			let nPointResolution = ol.proj.getPointResolution(sProjectionCode, nResolution, oCenter);
			let nResolutionFactor = nResolution / nPointResolution;
			let nRadius = (meters / ol.proj.Units.METERS_PER_UNIT['m']) * nResolutionFactor;
			return nRadius;
		},

		/**
		 * @description Feature 검색 기능.
		 * @alias olSwipMap.util.getFeaturesByProperties
		 * @param layer
		 *			{Object} 레이어.
		 * @param propName
		 *			{String} 프로퍼티 키.
		 * @param propValue
		 *			{Object} 프로퍼티 값.
		 * @return {Object} 결과 오브젝트.
		 */
		getFeaturesByProperties: function (layer, propName, propValue) {
			let olSource = layer.getSource();
			let olFeatures = olSource.getFeatures();
			let oFeaturesResult = [];

			if (olFeatures.length) {
				$.each(olFeatures, function (i, v) {
					let oProperties = v.getProperties();
					let sPropValue = oProperties[propName];
					if (typeof propValue === 'undefined') {
						oFeaturesResult.push(v);
					} else if (sPropValue === propValue) {
						oFeaturesResult.push(v);
					}
				});
			}

			return {
				propName: propName,
				propValue: propValue,
				length: oFeaturesResult.length,
				features: oFeaturesResult
			}
		},
		/**
		 * @description Feature 검색 기능.
		 * @alias olSwipMap.util.removeFeaturesByProperties
		 * @param layer
		 *			{Object} 레이어.
		 * @param propName
		 *			{String} 프로퍼티 키.
		 * @param propValue
		 *			{Object} 프로퍼티 값.
		 * @return {Object} 결과 오브젝트.
		 */
		removeFeaturesByProperties: function (layer, propName, propValue) {
			const oFeatures = this.getFeaturesByProperties(layer, propName, propValue);
			if (oFeatures.length) {
				$.each(oFeatures.features, function (index, feature) {
					layer.getSource().removeFeature(feature);
				});
			}
		},
		getAddressByBbox: function (coordinate, projection) {
			console.log('getAddressByBbox() >>>>> %o, %s', coordinate, projection);
			let nMinX = coordinate[0] - 0.000005;
			let nMaxX = coordinate[0] + 0.000005;
			let nMinY = coordinate[1] - 0.000005;
			let nMaxY = coordinate[1] + 0.000005;
			let oBbox = [
				nMinX, nMinY, nMaxX, nMaxY, projection
			];

			//let sUcpId = oConfigure.ucpId.toString().toLowerCase();
			let sGeoLdreg = oConfigure.dstrtCd.toString().toLowerCase();
			if ('11545' == sGeoLdreg) {
				sGeoLdreg = 'com';
			}		// 서울금천구

			// 광역 서비스 일 경우
			//if (oConfigure.sysId == 'WIDE' || oConfigure.sysId == 'BASE') {
			//	sUcpId = sUcpId.substr(0, 2) + '000';
			//}

			$.ajax({
				type: 'GET',
				dataType: 'json',
				url: oGis.urlWfs,
				async: false,
				data: {
					service: 'WFS',
					version: '1.1.0',
					request: 'GetFeature',
					typename: 'cite:geo_ldreg_' + sGeoLdreg,
					srsname: projection,
					outputFormat: 'application/json',
					bbox: oBbox.toString()
				},
				success: function (data) {
					console.log("### data => %o", data);

					if (data.hasOwnProperty('features') && data.features.length) {
						let oFeature = data.features[0];
						if (oFeature.hasOwnProperty('properties') && oFeature.properties.hasOwnProperty('PNU')) {
							let sPnu = oFeature.properties.PNU;
							if (sPnu.length == 19) {
								let oData = {
									pnu: sPnu
								};
								$.ajax({
									type: 'POST',
									dataType: 'json',
									async: false,
									url:contextRoot + '/mntr/api/addrListByPnu.json',
									data: oData,
									success: function (data) {
										if (data.hasOwnProperty('result') && data.result.hasOwnProperty('addr')) {
											let oAddr = data.result.addr;
											$.each(oAddr, function (index, addr) {
												addr.buldNm = addr.buldNm || '';
												addr.roadAddr = addr.roadAddr || '';
												//if (oConfigure.exeEnv == 'DEV')
												console.log('-- addr => %s',addr);
											});
										}
									},
									error: function (data, status, err) {
										console.log(data);
									}
								});
							}
						} else {
							console.log('-- NOT FOUND ADDRESS');
						}
					} else {
						console.log('-- NOT FOUND ADDRESS');
					}
				},
				error: function (data, status, err) {
					console.log(data);
				}
			});

		},
		export: function () {
			olMap.map.once('rendercomplete', function () {
				const elCanvas = document.createElement('canvas');
				const oSize = olMap.map.getSize();
				elCanvas.width = oSize[0];
				elCanvas.height = oSize[1];
				const context = elCanvas.getContext('2d');
				Array.prototype.forEach.call(
					document.querySelectorAll('#map canvas'),
					function (canvas) {
						if (canvas.width > 0) {
							const opacity = canvas.parentNode.style.opacity;
							context.globalAlpha = opacity === '' ? 1 : Number(opacity);
							CanvasRenderingContext2D.prototype.setTransform.apply(context);
							context.drawImage(canvas, 0, 0);
						}
					}
				);

				const sFileNm = 'map_' + moment().format('YYYYMMDDHHmmss') + '.png';

				if (navigator.msSaveBlob) {
					navigator.msSaveBlob(elCanvas.msToBlob(), sFileNm);
				} else {
					const elLink = document.getElementById('ol-map-image-download');
					elLink.href = elCanvas.toDataURL();
					elLink.download = sFileNm;
					elLink.click();
				}
			});
			olMap.map.renderSync();
		},
		calcOffset: function (degree, pixel) {
			let nOffsetX = Math.round(pixel * Math.sin(degree * (Math.PI / 180)));
			let nOffsetY = Math.round(pixel * Math.cos(degree * (Math.PI / 180))) * -1;
			return {x: nOffsetX, y: nOffsetY};
		}
	},

	draw: {
		/**
		 * @description 특정한 지점에 원을 그린다.
		 * @alias olSwipMap.draw.circle
		 * @param coordinate(Array) :
		 *			경도, 위도
		 * @param projection(String) :
		 *			EPSG CODE
		 * @param meters(Number) :
		 *			미터 (m)
		 * @param label(String) :
		 *			라벨
		 * @param style(ol.style.Style) :
		 *			스타일
		 */
		circle: function (coordinate, projection, meters, degree, label, style, properties) {
			let sProjectionCode = olMap.map.getView().getProjection().getCode();
			let oCoordinate = [];

			if (typeof coordinate[0] === 'string' && !isNaN(coordinate[0])) coordinate = [parseFloat(coordinate[0]), parseFloat(coordinate[1])];

			if (projection == sProjectionCode) {
				oCoordinate = coordinate;
			} else {
				oCoordinate = ol.proj.transform(coordinate, projection, sProjectionCode);
			}
			let oCoordinateWgs84 = ol.proj.transform(oCoordinate, sProjectionCode, 'EPSG:4326');

			if (typeof properties != 'undefined' && properties.hasOwnProperty('geometry')) {
				delete properties.geometry;
			}

			let nRadius = olSwipMap.util.getRadius(meters);
			let olCircle = new ol.geom.Circle(oCoordinate, nRadius);
			let olCircleFeature = new ol.Feature({
				geometry: olCircle
			});

			if (typeof style != 'undefined') {
				olCircleFeature.setStyle(style);
			} else {
				olCircleFeature.setStyle(new ol.style.Style({
					fill: new ol.style.Fill({
						color: new ol.color.asArray([
							241, 196, 15, 0.1
						])
					}),
					stroke: new ol.style.Stroke({
						color: new ol.color.asArray([
							255, 0, 0, 1
						]),
						width: 2,
						lineDash: [
							4, 4
						]
					})
				}));
			}

			if (isNaN(degree)) {
				degree = 0;
			}
			let oPoint = null;
			// properties
			if (typeof label != 'undefined') {
				oPoint = olSwipMap.util.calcLonLatByDegreeMeters(oCoordinateWgs84, degree, 0);
			} else {
				oPoint = olSwipMap.util.calcLonLatByDegreeMeters(oCoordinateWgs84, degree, meters);
			}

			if (typeof properties != 'undefined') {
				let oLabel = properties.label || {};
				if (!$.isEmptyObject(oLabel)) {
					let sPosition = oLabel.position;
					oPoint = olSwipMap.util.calcLonLatByDegreeMeters(oCoordinateWgs84, degree, meters);
				}
			}

			let olPointFeature = new ol.Feature({
				geometry: new ol.geom.Point(ol.proj.transform(oPoint, 'EPSG:4326', sProjectionCode))
			});

			if (typeof label != 'undefined') {
				if (label != null) {
					olPointFeature.setStyle(new ol.style.Style({
						text: new ol.style.Text({
							font: 'bold 11px NanumGothic',
							fill: new ol.style.Fill({
								color: '#fff'
							}),
							backgroundFill: new ol.style.Fill({
								color: '#000'
							}),
							padding: [
								2, 2, 2, 2
							],
							text: ' ' + label + ' ',
							offsetY: -30
						})
					}));
				} else {
					olPointFeature.setStyle(new ol.style.Style({
						text: new ol.style.Text({
							text: '',
						})
					}));
				}
			} else {
				let sLabel = meters + 'm';
				if (typeof properties != 'undefined') {
					let oLabel = properties.label || {};
					if (!$.isEmptyObject(oLabel)) {
						let sText = oLabel.text || '';
						if (sText != '') sLabel = sText;
					}
				}

				olPointFeature.setStyle(new ol.style.Style({
					text: new ol.style.Text({
						font: 'bold 11px NanumGothic',
						fill: new ol.style.Fill({
							color: '#fff'
						}),
						stroke: new ol.style.Stroke({
							color: '#000',
							width: 6
						}),
						text: sLabel,
					})
				}));
			}

			if (typeof properties != 'undefined') {
				if (properties.hasOwnProperty('geometry')) {
					delete properties.geometry;
				}
				if (typeof properties != 'undefined' && properties.hasOwnProperty('fcltId')) {
					olCircleFeature.setId('CF_' + properties.fcltId);
					olPointFeature.setId('PF_' + properties.fcltId);
				}
				olCircleFeature.setProperties(properties);
				olPointFeature.setProperties(properties);
			} else {
				if (typeof label != 'undefined') {
					olCircleFeature.setProperties({
						label: label
					});
					olPointFeature.setProperties({
						label: label
					});
				}
			}
			let olFeatures = [
				olCircleFeature, olPointFeature
			];

			olMap.layers.vector.getSource().addFeatures(olFeatures);
			return olFeatures;
		},
	},

	/**
	 * @description 측정도구
	 * @alias olSwipMap.measure
	 */
	measure: {
		isEnabled: false,
		lineStringLength: 2,
		layer: null,
		source: null,
		draw: null,
		sketch: null,
		listeners: {
			sketch: null,
			pointermove: null,
			mouseout: null
		},
		overlays: [],
		helpTooltipElement: null,
		helpTooltip: null,
		measureTooltipElement: null,
		measureTooltip: null,
		continuePolygonMsg: '지도를 더블클릭하여 측정을 완료합니다.',
		continueLineMsg: '지도를 더블클릭하여 측정을 완료합니다.',
		init: function (type) {
			if (olSwipMap.mntr.linestring.layer != null) {
				alert('궤적감시 설정이나 수정 기능을 해제 후 진행하세요.');
				return false;
			}

			if (this.source == null) {
				this.source = new ol.source.Vector();
			}

			if (this.layer == null) {
				this.layer = new ol.layer.Vector({
					renderMode: 'vector',
					zIndex: 10,
					source: this.source,
					style: new ol.style.Style({
						fill: new ol.style.Fill({
							color: 'rgba(255, 255, 255, 0.2)'
						}),
						stroke: new ol.style.Stroke({
							color: '#ffcc33',
							width: 4
						}),
						image: new ol.style.Circle({
							radius: 7,
							fill: new ol.style.Fill({
								color: '#ffcc33'
							})
						})
					})
				});
				olMap.map.addLayer(this.layer);
			}

			olSwipMap.overlays.pointermove.setPosition(undefined);
			olSwipMap.overlays.click.setPosition(undefined);

			if (this.draw != null) {
				olMap.map.removeInteraction(this.draw);
			}

			let oDrawOpt = {
				source: this.layer.getSource(),
				type: type,
				style: new ol.style.Style({
					fill: new ol.style.Fill({
						color: 'rgba(255, 255, 255, 0.2)'
					}),
					stroke: new ol.style.Stroke({
						color: 'rgba(0, 0, 0, 0.5)',
						lineDash: [
							10, 10
						],
						width: 2
					}),
					image: new ol.style.Circle({
						radius: 5,
						stroke: new ol.style.Stroke({
							color: 'rgba(0, 0, 0, 0.7)'
						}),
						fill: new ol.style.Fill({
							color: 'rgba(255, 255, 255, 0.2)'
						})
					})
				})
			};

			if (type == 'LineString') {
				oDrawOpt.geometryFunction = function (coords, geom) {
					let oMeasure = olSwipMap.measure;
					if (!geom) {
						geom = new ol.geom.LineString([]);
					}
					geom.setCoordinates(coords);
					if (coords.length > oMeasure.lineStringLength) {
						let nLength = coords.length;
						oMeasure.lineStringLength = nLength;
						let olLineString = new ol.geom.LineString([]);
						olLineString.appendCoordinate(coords[nLength - 2]);
						olLineString.appendCoordinate(coords[nLength - 3]);
						let sLength = oMeasure.formatLength(olLineString);
						let sLengthSum = oMeasure.formatLength(geom);
						let sText = sLength + '\n' + sLengthSum;

						let olPointFeature = new ol.Feature({
							geometry: new ol.geom.Point(coords[nLength - 2])
						});

						if (sLength == sLengthSum) {
							sText = sLength;
						}

						olPointFeature.setStyle(new ol.style.Style({
							text: new ol.style.Text({
								font: '11px NanumGothic',
								fill: new ol.style.Fill({
									color: '#fff'
								}),
								stroke: new ol.style.Stroke({
									color: '#000',
									width: 4
								}),
								offsetY: 20,
								text: sText
							})
						}));

						oMeasure.layer.getSource().addFeature(olPointFeature);
					}
					return geom;
				};
			}

			this.draw = new ol.interaction.Draw(oDrawOpt);

			olMap.map.addInteraction(this.draw);

			this.createMeasureTooltip();
			this.createHelpTooltip();

			this.draw.on('drawstart', function (event) {
				// set sketch
				let oMeasure = olSwipMap.measure;
				oMeasure.sketch = event.feature;

				let oTooltipCoord = event.coordinate;
				oMeasure.listeners.sketch = oMeasure.sketch.getGeometry().on('change', function (event) {
					let oMeasure = olSwipMap.measure;
					let olGeom = event.target;
					let sOutput = '';
					let oTooltipCoord = null;
					if (olGeom instanceof ol.geom.Polygon) {
						sOutput = oMeasure.formatArea(olGeom);
						oTooltipCoord = olGeom.getInteriorPoint().getCoordinates();
					} else if (olGeom instanceof ol.geom.LineString) {
						sOutput = oMeasure.formatLength(olGeom);
						oTooltipCoord = olGeom.getLastCoordinate();
					}
					oMeasure.measureTooltipElement.innerHTML = sOutput;
					oMeasure.measureTooltip.setPosition(oTooltipCoord);
				});
			}, this);

			this.draw.on('drawend', function () {
				let oMeasure = olSwipMap.measure;
				oMeasure.lineStringLength = 2;
				oMeasure.measureTooltipElement.className = 'ol-tooltip tooltip-static';
				oMeasure.measureTooltip.setOffset([
					0, -7
				]);
				oMeasure.sketch = null;
				oMeasure.measureTooltipElement = null;
				oMeasure.createMeasureTooltip();
				ol.Observable.unByKey(oMeasure.listeners.sketch);
			}, this);

			if (this.listeners.mouseout == null) {
				this.listeners.mouseout = olMap.map.getViewport().addEventListener('mouseout', function () {
					let oMeasure = olSwipMap.measure;
					if (oMeasure.helpTooltipElement != null) {
						oMeasure.helpTooltipElement.classList.add('hidden');
					}
				});
			}

			if (olSwipMap.listeners.feature.click != null) {
				ol.Observable.unByKey(olSwipMap.listeners.feature.click);
				olSwipMap.listeners.feature.click = null;
			}

			if (olSwipMap.listeners.feature.pointermove != null) {
				ol.Observable.unByKey(olSwipMap.listeners.feature.pointermove);
				olSwipMap.listeners.feature.pointermove = null;
			}

			if (this.listeners.pointermove == null) {
				this.listeners.pointermove = olMap.map.on('pointermove', function (event) {
					if (event.dragging) {
						return;
					}
					let sHelpMsg = '지도를 클릭하여 측정을 시작합니다.';
					let oMeasure = olSwipMap.measure;
					if (oMeasure.sketch) {
						let olGeom = (oMeasure.sketch.getGeometry());
						if (olGeom instanceof ol.geom.Polygon) {
							sHelpMsg = oMeasure.continuePolygonMsg;
						} else if (olGeom instanceof ol.geom.LineString) {
							sHelpMsg = oMeasure.continueLineMsg;
						}
					}
					oMeasure.helpTooltipElement.innerHTML = sHelpMsg;
					oMeasure.helpTooltip.setPosition(event.coordinate);
					oMeasure.helpTooltipElement.classList.remove('hidden');
				});
			}
			this.isEnabled = true;
		},
		formatLength: function (line) {
			let nLength = ol.sphere.getLength(line);
			let sOutput = '';
			if (nLength > 1000) {
				nLength = nLength / 1000;
				sOutput = nLength.toFixed(2);
				sOutput += ' ' + 'km';
			} else {
				sOutput = nLength.toFixed(0);
				sOutput += ' ' + 'm';
			}
			return sOutput;
		},
		formatArea: function (polygon) {
			let nArea = ol.sphere.getArea(polygon);
			let sOutput = '';
			if (nArea > 1000000) {
				nArea = nArea / 1000000;
				sOutput = nArea.toFixed(2);
				sOutput += ' ' + 'km<sup>2</sup>';
			} else {
				nArea = nArea;
				sOutput = nArea.toFixed(0);
				sOutput += ' ' + 'm<sup>2</sup>';
			}
			return sOutput;
		},
		/**
		 * Creates a new help tooltip
		 */
		createHelpTooltip: function () {
			if (this.helpTooltipElement) {
				this.helpTooltipElement.parentNode.removeChild(this.helpTooltipElement);
			}
			this.helpTooltipElement = document.createElement('div');
			this.helpTooltipElement.className = 'ol-tooltip hidden';
			this.helpTooltip = new ol.Overlay({
				element: this.helpTooltipElement,
				offset: [
					15, 0
				],
				positioning: 'center-left'
			});
			olMap.map.addOverlay(this.helpTooltip);
			this.overlays.push(this.helpTooltip);
		},
		/**
		 * Creates a new measure tooltip
		 */
		createMeasureTooltip: function () {
			if (this.measureTooltipElement) {
				this.measureTooltipElement.parentNode.removeChild(this.measureTooltipElement);
			}
			this.measureTooltipElement = document.createElement('div');
			this.measureTooltipElement.className = 'ol-tooltip tooltip-measure';
			this.measureTooltip = new ol.Overlay({
				element: this.measureTooltipElement,
				offset: [
					0, -15
				],
				positioning: 'bottom-center'
			});
			olMap.map.addOverlay(this.measureTooltip);
			this.overlays.push(this.measureTooltip);
		},
		clear: function () {
			// PATH 삭제
			if (this.source != null) {
				this.source.clear();
				this.source = null;
			}

			if (this.listeners.sketch != null) {
				ol.Observable.unByKey(this.listeners.sketch);
				this.listeners.sketch = null;
				this.sketch = null;
			}

			if (this.listeners.pointermove != null) {
				ol.Observable.unByKey(this.listeners.pointermove);
				this.listeners.pointermove = null;
			}

			if (this.listeners.mouseout != null) {
				ol.Observable.unByKey(this.listeners.mouseout);
				this.listeners.mouseout = null;
			}

			if (this.draw != null) {
				olMap.map.removeInteraction(this.draw);
				this.draw = null;
			}

			this.helpTooltipElement = null;
			this.helpTooltip = null;
			this.measureTooltipElement = null;
			this.measureTooltip = null;

			// ToolTip 삭제
			if (this.overlays.length) {
				$.each(this.overlays, function (i, v) {
					olMap.map.removeOverlay(v);
				});
				this.overlays = [];
			}

			olMap.map.removeLayer(this.layer);
			this.layer = null;

			// TODO 임시
			if (olSwipMap.events.feature.pointermove != null) {
				olSwipMap.listeners.feature.pointermove = olMap.map.on('pointermove', olSwipMap.events.feature.pointermove);
			}

			if (olSwipMap.events.feature.click != null) {
				olSwipMap.listeners.feature.click = olMap.map.on('click', olSwipMap.events.feature.click);
			}

			this.isEnabled = false;
		}
	},

	/**
	 * @description 검색
	 * @alias olSwipMap.search
	 */
	search: {
		overlay: null,
		element: null,
		init: function () {
			this.element = $('<div/>', {
				'id': 'ol-overlay-search'
			})[0];

			this.overlay = new ol.Overlay({
				element: this.element
			});

			olMap.map.addOverlay(this.overlay);

			$('#ol-map-menu-search-keyword').on('keypress', function (event) {
				if (event.which == 13) {
					olSwipMap.search.open();
				}
			});
		},
		open: function () {
			let sKeyword = $('#ol-map-menu-search-keyword').val();
			let oSplit = sKeyword.trim().split(',');
			if (oSplit.length == 2) {
				let sLat = oSplit[0];
				let sLon = oSplit[1];
				let nPointX = parseFloat(sLon);
				let nPointY = parseFloat(sLat);
				if (!isNaN(nPointX) && !isNaN(nPointY)) {
					let oCoordinate = ol.proj.transform([
						nPointX, nPointY
					], 'EPSG:4326', oGis.projection);
					olSwipMap.point.set([
						nPointX, nPointY
					], 'EPSG:4326', 'BLUE', true, true, true);
				}
				return false;
			}

			let hasClass = $('#ol-map-menu-search-pannel').hasClass('open')
			if (!hasClass) {
				$('#ol-map-menu-search-pannel').addClass('open');
			}

			let isEmpty = $('#grid-map-menu-search').is(':empty');
			if (!isEmpty) {
				$('#grid-map-menu-search').jqGrid('GridUnload');
			}

			this.grid(sKeyword);

			$('#ol-map-menu-search-grid').css('z-index', '1100');
		},
		close: function (popover) {
			if ($('#ol-map-menu-search-pannel').hasClass('open')) {
				$('#ol-map-menu-search-pannel').removeClass('open');
				$('#ol-map-menu-search-keyword').val('');
			}
			if (popover) {
				$(this.element).popover('destroy');
				this.overlay.setPosition(undefined);
			}
		},
		grid: function (keyword) {
			let sId = 'map-menu-search';
			$('#grid-' + sId).jqGrid({
				url:contextRoot + '/mntr/api/keywordToCoord.json',
				datatype: 'json',
				mtype: 'POST',
				height: 'auto',
				autowidth: true,
				rowNum: 10,
				postData: {
					keyword: keyword,
				},
				colNames: [
					'구분', '주소', 'road', 'jibun', '명칭', 'pointX', 'pointY',
				],
				colModel: [
					{
						name: 'gubn',
						align: 'center',
						width: 15,
						cellattr: function () {
							return 'style="width:15px;"'
						}
					}, {
						name: 'addr',
						formatter: function (cellvalue, options, rowObject) {

							let road = rowObject.roadAddr;
							let jibun = rowObject.jibunAddr;

							let $div = $('<div/>');
							let $spanJibun = $('<span/>', {
								'class': 'label label-default',
								'text': '지번',
								'style': 'margin-right: 3px;'
							});
							let $spanRoad = $('<span/>', {
								'class': 'label label-default',
								'text': '도로',
								'style': 'margin-right: 3px;'
							});
							$div.append($spanRoad);
							$div.append(road == '' ? '' : road);
							$div.append(' <br>');
							$div.append($spanJibun);
							$div.append(jibun == '' ? '' : jibun);
							return $div[0].innerHTML;
						},
						width: 45,
						cellattr: function () {
							return 'style="width:45px;"'
						}
					}, {
						name: 'roadAddr',
						hidden: true
					}, {
						name: 'jibunAddr',
						hidden: true
					}, {
						name: 'poi',
						align: 'center',
						width: 40,
						cellattr: function () {
							return 'style="width:40px;"'
						}
					}, {
						name: 'pointX',
						hidden: true
					}, {
						name: 'pointY',
						hidden: true
					},
				],
				jsonReader: {
					root: "result.addr",
					page: 1,
					total: function (data) {
						let nRecords = typeof data.result.addr !== 'undefined' ? data.result.addr.length : 0;
						let nRowNum = $('#grid-' + sId).getGridParam('rowNum');
						let nTotal = Math.ceil(nRecords / nRowNum);
						return nTotal;
					},
					records: function (data) {
						$('#ol-map-menu-search-result').text(data.result.msg);
						return typeof data.result.addr !== 'undefined' ? data.result.addr.length : 0;
					}
				},
				loadonce: true,
				cmTemplate: {sortable: false},
				loadComplete: function (data) {
					oCommon.jqGrid.resize(sId);
					olSwipMap.search.pagenation(data);
					oCommon.jqGrid.gridComplete(this);
				},
				onSelectRow: function (id) {
					let oData = $('#grid-' + sId).jqGrid('getRowData', id);
					if (!isNaN(oData.pointX) && !isNaN(oData.pointY)) {
						let nLon = parseFloat(oData.pointX);
						let nLat = parseFloat(oData.pointY);
						let sProjectionCode = olMap.map.getView().getProjection().getCode();

						let $content = $('<div/>');

						let $table = $('<table/>', {
							'class': 'table table-striped table-condensed',
							'style': 'margin: 0px;'
						});

						let $tbody = $('<tbody/>');
						$tbody.appendTo($table);

						let $tr = $('<tr/>');

						let $th = $('<th/>', {
							'scope': 'row'
						})

						let $td = $('<td/>');

						let $trLon = $tr.clone();
						$trLon.append($th.clone().text('경도'));
						$trLon.append($td.clone().text(nLon.toFixed(6)));
						$trLon.appendTo($tbody);

						let $trLat = $tr.clone();
						$trLat.append($th.clone().text('위도'));
						$trLat.append($td.clone().text(nLat.toFixed(6)));
						$trLat.appendTo($tbody);

						let $trAddr = $tr.clone();
						$trAddr.append($th.clone().text('주소'));
						$trAddr.append($td.clone().html(oData.addr));
						$trAddr.appendTo($tbody);

						if (oData.poi && oData.poi != '') {
							let $trBuldNm = $tr.clone();
							$trBuldNm.append($th.clone().text('명칭'));
							$trBuldNm.append($td.clone().text(oData.poi));
							$trBuldNm.appendTo($tbody);
						}

						// close button
						$content.append('<button type="button" class="close ol-overlay-close" title="닫기" onclick="javascript:olSwipMap.search.close(true);"><i class="fas fa-times"></i></button>');
						$content.append($table);

						let oCoordinate = ol.proj.transform([
							nLon, nLat
						], 'EPSG:4326', oGis.projection);
						let oResolution = olMap.view.getResolution();
						let nPixel = $('aside#right').is(':visible') ? 250 : 100;
						olMap.view.setCenter([oCoordinate[0] - nPixel * oResolution, oCoordinate[1]]);

						// contents
						setTimeout(function () {
							let oSearch = olSwipMap.search;
							oSearch.events.change.position([nLon.toFixed(6), nLat.toFixed(6)], $tbody.get(0));

							let elSearch = oSearch.element;
							$(elSearch).popover('destroy');
							let oCoordinate = ol.proj.transform([
								nLon, nLat
							], 'EPSG:4326', sProjectionCode);
							oSearch.overlay.setPosition(oCoordinate);
							$(elSearch).popover({
								placement: 'top',
								animation: false,
								html: true,
								content: $content.prop('outerHTML')
							});
							$(elSearch).popover('show');

							olSwipMap.point.set(oCoordinate, oGis.projection, 'BLUE', false, false, true);
						}, 500);
					} else {
						alert('좌표값이 없는 데이터입니다.');
					}
				},
			});
		},
		pagenation: function (data) {
			let sId = 'map-menu-search';
			let nPage = 1, nTotal = 1, nRecords = 0;
			let $paginate = $('#paginate-' + sId);
			$paginate.empty();

			if (data.page && data.total && data.records) {
				nPage = data.page;
				nTotal = data.total;
				nRecords = data.records;
			} else if (data.result && data.result.addr) {
				nRecords = data.result.addr.length;
				nRowNum = $("#grid-" + sId).getGridParam('rowNum');
				nTotal = Math.ceil(nRecords / nRowNum);
			} else {
				oCommon.jqGrid.setNodata(sId, '주소 데이터가 없습니다.');
				return false;
			}

			if ($('#grid-alert-' + sId).length) {
				$('#grid-alert-' + sId).hide();
			}

			$paginate.html($('<ul/>', {
				'id': 'pagination-' + sId,
				'class': 'paging'
			}));

			let $pagination = $('#pagination-' + sId);
			$pagination.twbsPagination({
				startPage: nPage,
				totalPages: nTotal,
				visiblePages: 4,
				onPageClick: function (event, page) {
					$('#grid-' + sId).setGridParam({
						page: page
					}).trigger('reloadGrid');
				},
				first: '<i class="fas fa-angle-double-left"></i>',
				prev: '<i class="fas fa-angle-left"></i>',
				next: '<i class="fas fa-angle-right"></i>',
				last: '<i class="fas fa-angle-double-right"></i>'
			});
		},
		events: {
			change: {
				position: function (coordinate, element) {
				}
			}
		}
	},

	contextmenu: {
		overlay: null,
		element: null,
		init: function (options) {
			this.element = $('<div/>', {
				'id': 'ol-overlay-contextmenu'
			})[0];

			this.overlay = new ol.Overlay({
				element: this.element
			});

			olMap.map.addOverlay(this.overlay);
			olMap.map.getViewport().addEventListener('contextmenu', this.listener);
			/*
			olMap.map.getViewport().addEventListener('touchstart', (event) => {
				timer = setTimeout(() => {
					timer = null;
					olSwipMap.contextmenu.listener(event);
				}, 1000);
			});

			function cancel() {
				clearTimeout(timer);
			}

			olMap.map.getViewport().addEventListener('touchend', cancel);
			olMap.map.getViewport().addEventListener('touchmove', cancel);
			*/
		},
		close: function () {
			$(this.element).popover('destroy');
			this.overlay.setPosition(undefined);
		},
		events: {
			change: {
				position: function (coordinate, element) {
				}
			}
		},
		listener: function (event) {
			event.preventDefault();
			let oContextmenu = olSwipMap.contextmenu;
			let oCoordinate = olMap.map.getEventCoordinate(event);
			let sProjectionCode = olMap.map.getView().getProjection().getCode();
			let oCoordinateWgs84 = ol.proj.transform(oCoordinate, sProjectionCode, 'EPSG:4326');

			let nLon = parseFloat(oCoordinateWgs84[0].toFixed(6));
			let nLat = parseFloat(oCoordinateWgs84[1].toFixed(6));

			let $content = $('<div/>');

			let $table = $('<table/>', {
				'class': 'table table-striped table-condensed',
				'id': 'table-contextmenu',
				'style': 'margin : 0px;',
				'data-point-x': nLon,
				'data-point-y': nLat,
			});

			let $tbody = $('<tbody/>');
			$tbody.appendTo($table);

			let $tr = $('<tr/>');

			let $th = $('<th/>', {
				'scope': 'row'
			})

			let $td = $('<td/>');

			// 경도
			let $trLon = $tr.clone();
			$trLon.append($th.clone().text('경도'));
			$trLon.append($td.clone().text(nLon));
			$trLon.appendTo($tbody);

			// 위도
			let $trLat = $tr.clone();
			$trLat.append($th.clone().text('위도'));
			$trLat.append($td.clone().text(nLat));
			$trLat.appendTo($tbody);

			console.log("### oGis.urlWfs => " + oGis.urlWfs);
			if (oGis.urlWfs != '') {
				let nResolutionForZoom = olMap.view.getResolutionForZoom(olMap.view.getZoom());
				let nMinX = oCoordinate[0] - 0.5 * nResolutionForZoom;
				let nMaxX = oCoordinate[0] + 0.5 * nResolutionForZoom;
				let nMinY = oCoordinate[1] - 0.5 * nResolutionForZoom;
				let nMaxY = oCoordinate[1] + 0.5 * nResolutionForZoom;
				let oBbox = [
					nMinX, nMinY, nMaxX, nMaxY, oGis.projection
				];

				//let sUcpId = oConfigure.ucpId.toString().toLowerCase();
				let sGeoLdreg = oConfigure.dstrtCd.toString().toLowerCase();
				if ('11545' == sGeoLdreg) {
					sGeoLdreg = 'com';
				}		// 서울금천구
				console.log("### sGeoLdreg => " + sGeoLdreg);

				// 광역 서비스 일 경우
				//if (oConfigure.sysId == 'WIDE' || oConfigure.sysId == 'BASE') {
				//	sUcpId = sUcpId.substr(0, 2) + '000';
				//}
				$.ajax({
					type: 'GET',
					dataType: 'json',
					url: oGis.urlWfs,
					async: false,
					data: {
						service: 'WFS',
						version: '1.1.0',
						request: 'GetFeature',
						typename: 'cite:geo_ldreg_' + sGeoLdreg,
						srsname: oGis.projection,
						outputFormat: 'application/json',
						bbox: oBbox.toString()
					},
					success: function (data) {
						console.log("### data => %o", data);

						if (data.hasOwnProperty('features') && data.features.length) {
							let oFeature = data.features[0];
							console.log("### oFeature => " + oFeature);

							if (oFeature.hasOwnProperty('properties') && oFeature.properties.hasOwnProperty('PNU')) {
								let sPnu = oFeature.properties.PNU;
								console.log("### sPnu => " + sPnu);

								if (sPnu.length == 19) {
									$.ajax({
										type: 'POST',
										dataType: 'json',
										async: false,
										url:contextRoot + '/mntr/api/addrListByPnu.json',
										data: {
											pnu: sPnu
										},
										success: function (data) {
											if (data.hasOwnProperty('result') && data.result.hasOwnProperty('addr')) {
												let oAddr = data.result.addr;
												$.each(oAddr, function (index, addr) {
													let sBuldNm = addr.buldNm == null ? '' : addr.buldNm;
													if (index == 0) {
														if (!addr.roadAddr) {
															addr.roadAddr = '';
														}
														// 주소
														let $trAddr = $tr.clone();
														$trAddr.append($th.clone().text('주소'));
														$trAddr.append($td.clone().html(
															'<span class="label label-default">도로</span> ' + addr.roadAddr + '<br><span class="label label-default">지번</span> ' + addr.jibunAddr));
														$trAddr.appendTo($tbody);

														if (sBuldNm != '') {
															// 명칭
															let $trBuldNm = $tr.clone();
															$trBuldNm.append($th.clone().text('명칭'));
															$trBuldNm.append($td.clone().text(sBuldNm));
															$trBuldNm.appendTo($tbody);
														}

														setTimeout(function () {
															if ($('#btn-contextmenu-addr').length) {
																$('#btn-contextmenu-addr').data('jibun', addr.jibunAddr);
																$('#btn-contextmenu-addr').data('road', addr.roadAddr);
																$('#btn-contextmenu-addr').data('poi', sBuldNm);
															}

															$('#table-contextmenu').data({
																'dstrtCd': addr.dstrtCd,
																'jibun': addr.jibunAddr,
																'road': addr.roadAddr,
																'poi': sBuldNm,
															});
														}, 500);
													}
													console.log('-- ADDRESS: %o', addr);
												});
											}
										},
										error: function (data, status, err) {
											console.log(data);
										}
									});
								}
							}
						}
					},
					error: function (data, status, err) {
						console.log(data);
					}
				});

			} else {
				$.ajax({
					type: 'POST',
					async: false,
					dataType: 'json',
					url:contextRoot + '/mntr/api/coordToAddr.json',
					data: {
						pointX: nLon,
						pointY: nLat
					},
					success: function (data) {
						if (data.hasOwnProperty('result') && data.result.hasOwnProperty('addr')) {
							let oAddr = data.result.addr;
							let sLgEmdNm = oAddr.lgEmdNm;
							let sJustnAdmDong = oAddr.justnAdmDong;
							let sLgLiNm = oAddr.lgLiNm == null ? '' : ' ' + oAddr.lgLiNm;
							let sIsMntn = oAddr.isMntn == '1' ? ' 산 ' : ' ';
							let sJibunMainNo = oAddr.jibunMainNo;
							let sJibunSubNo = oAddr.jibunSubNo == 0 ? '' : '-' + oAddr.jibunSubNo;
							let sRoadNm = oAddr.roadNm;
							let sIsUndgrnd = oAddr.isUndgrnd == '1' ? ' 지하 ' : ' ';
							let sBuldMainNo = oAddr.buldMainNo;
							let sBuldSubNo = oAddr.buldSubNo == 0 ? '' : '-' + oAddr.buldSubNo
							let sBuldNm = oAddr.buldNm == null ? '' : oAddr.buldNm;

							let sRoad = '';
							if (sRoadNm) {
								sRoad = sRoadNm + sIsUndgrnd + sBuldMainNo + sBuldSubNo;
								if (sLgEmdNm.slice(-1) != '동') {
									sRoad = sLgEmdNm + ' ' + sRoad;
								}
							}

							let sJibun = '';
							if (sLgEmdNm) {
								sJibun = sLgEmdNm + sLgLiNm + sIsMntn + sJibunMainNo + sJibunSubNo;
							}

							// 주소
							let $trAddr = $tr.clone();
							$trAddr.append($th.clone().text('주소'));
							$trAddr.append($td.clone().html('<span class="label label-default">도로</span> ' + sRoad + '<br><span class="label label-default">지번</span> ' + sJibun));
							$trAddr.appendTo($tbody);

							if (sBuldNm != '') {
								// 명칭
								let $trBuldNm = $tr.clone();
								$trBuldNm.append($th.clone().text('명칭'));
								$trBuldNm.append($td.clone().text(sBuldNm));
								$trBuldNm.appendTo($tbody);
							}

							setTimeout(function () {
								let oData = {
									'jibun': sJibun,
									'road': sRoad,
									'poi': sBuldNm,
									'pointX': nLon,
									'pointY': nLat
								};
								if ($('#btn-contextmenu-addr').length) $('#btn-contextmenu-addr').data(oData);
								$('#table-contextmenu').data(oData);
							}, 500);
						}
					},
					error: function (data, status, err) {
						console.log(data);
					}
				});
			}

			let $tfoot = $('<tfoot/>');
			$tfoot.appendTo($table);

			let $trButtons = $tr.clone();
			$trButtons.append($th.clone().text('기능'));

			let $tdButtons = $td.clone();
			$tdButtons.attr('id', 'td-contextmenu-buttons');
			$tdButtons.appendTo($trButtons);

			if (typeof oMapPopup != 'undefined') {
				// 지도 팝업
				let $btnDefault = $('<button/>', {
					'type': 'button',
					'class': 'btn btn-default btn-xs',
					'id': 'btn-contextmenu-addr',
					'title': '해당 내용을 선택합니다.',
					'html': '선택',
					'data-gubn': '일반주소',
					'data-name': '',
					'data-jibun': '',
					'data-road': '',
					'data-point-x': nLon,
					'data-point-y': nLat,
					'onclick': 'javascript:oMapPopup.callback(this);',
				});
				$tdButtons.append($btnDefault);
			} else {
				// 기본 위치로 설정
				let $btnDefault = $('<button/>', {
					'type': 'button',
					'class': 'btn btn-default btn-xs btn-contextmenu',
					'onclick': 'javascript:olSwipMap.contextmenu.saveMngDisplay(' + nLon + ', ' + nLat + ');',
					'title': '기본화면 위치로 설정',
					'html': '<i class="fas fa-map-marker-alt"></i>',
					'data-toggle': 'tooltip',
					'data-placement': 'top'
				});
				//$tdButtons.append($btnDefault);
			}

			if (olSwipMap.bookmark.overlay != null) {
				let $btnBookmark = $('<button/>', {
					'class': 'btn btn-default btn-xs btn-contextmenu',
					'onclick': 'javascript:olSwipMap.bookmark.add(' + nLon + ', ' + nLat + ');',
					'title': '즐겨찾기 추가',
					'html': '<i class="fas fa-bookmark"></i>',
					'data-toggle': 'tooltip',
					'data-placement': 'top'
				});

				$tdButtons.append($btnBookmark);
			}

			// 투망감시
			if (typeof oMenu != 'undefined') {
				if (oMenu.subMenu == 'main' || oMenu.subMenu == 'mtmdaReport') {
					const $btnCastnet = $('<button/>', {
						'class': 'btn btn-default btn-xs btn-contextmenu',
						'onclick': 'javascript:olSwipMap.mntr.castnet(this);',
						'title': '투망',
						'html': '<i class="fas fa-video"></i>',
						'style': 'margin-left:3px',
						'data-point-x': nLon,
						'data-point-y': nLat,
						'data-projection': 'EPSG:4326',
						'data-toggle': 'tooltip',
						'data-placement': 'top'
					});

					// TODO WIDE_SHARE_RDS
					if (oConfigure.cctvAccessYn == 'Y' && oConfigure.ptzCntrTy != 'NO') {
						$tdButtons.append($btnCastnet);
					} else if (oCurrentEvent.event.evtOcrNo != '' && oConfigure.ptzCntrTy != 'NO') {
						let nCctvViewRads = isNaN(oConfigure.cctvViewRads) ? 3 : parseInt(oConfigure.cctvViewRads);

						let nDistance = olSwipMap.util.getDistance([
							oCurrentEvent.event.pointX, oCurrentEvent.event.pointY
						], [
							nLon, nLat
						]);

						if (nDistance <= nCctvViewRads) {
							$tdButtons.append($btnCastnet);
						}
					}

					if (oConfigure.mtsEvtRgsYn == 'Y') {
						const $btnEmergency = $('<button/>', {
							'class': 'btn btn-default btn-xs btn-contextmenu',
							'onclick': 'javascript:olSwipMap.mntr.emergency();',
							'title': '긴급상황 등록',
							'html': '<i class="fas fa-bolt text-danger"></i>',
							'style': 'margin-left:3px',
							'data-toggle': 'tooltip',
							'data-placement': 'top'
						});
						$tdButtons.append($btnEmergency);
					}
				} else if (oMenu.subMenu == 'rqstView') {
					const $viewRrqst = $('#view-rqst');
					if ($viewRrqst.length) {
						const $btnAddr = $('<button/>', {
							'class': 'btn btn-default btn-xs btn-contextmenu',
							'onclick': 'javascript:oViewFirstRqst.view.rqst.setAddress("context");',
							'title': '열람신청에 위치와 주소 정보 적용',
							//'html': '<i class="far fa-keyboard"></i>',
							'html': '선택',
							'style': 'margin-left:3px',
						});
						$tdButtons.append($btnAddr);
					}
				}
			}

			// callback
			oContextmenu.events.change.position([nLon, nLat], $tdButtons.get(0));

			$trButtons.appendTo($tfoot);

			$content.append($('<button/>', {
				'type': 'button',
				'class': 'close ol-overlay-close',
				'title': '닫기',
				'onclick': 'javascript:olSwipMap.contextmenu.close();',
				'html': '<i class="fas fa-times"></i>'
			}));

			$content.append($('<button/>', {
				'type': 'button',
				'class': 'close ol-overlay-close',
				'style': 'margin-right : 3px;',
				'title': '좌표 클립보드로 복사',
				'onclick': 'javascript:olSwipMap.contextmenu.copy();',
				'html': '<i class="far fa-copy"></i>'
			}));

			$content.append($table);

			let sTxt = nLat + ',' + nLon;
			let $hidden = $('<input/>', {
				'type': 'text',
				'id': 'input-contextmenu-copy',
				'value': sTxt
			});
			$hidden.css({
				'position': 'absolute',
				'left': '-10000px',
				'top': '-10000px'
			});
			$content.append($hidden);

			// contents
			let elContextmenu = oContextmenu.element;
			$(elContextmenu).popover('destroy');
			oContextmenu.overlay.setPosition(oCoordinate);
			$(elContextmenu).popover({
				placement: 'top',
				animation: false,
				html: true,
				content: $content.prop('outerHTML')
			});

			$(elContextmenu).popover('show');

			$('button.btn-contextmenu').tooltip();
		},
		copy: function () {
			let elInput = $('input#input-contextmenu-copy')[0];
			elInput.select();
			elInput.setSelectionRange(0, 99999);
			document.execCommand('copy');
			//if (oConfigure.exeEnv == 'DEV')
			console.log('-- copy(), Copied the text: ' + $(elInput).val());
		},
		saveMngDisplay: function (pointX, pointY) {
			oCommon.modalConfirm('modal-confirm', '알림', '현재 위치를 기본 화면 지점으로 설정하시겠습니까?', function () {
				$.ajax({
					type: 'POST',
					url:contextRoot + '/mntr/saveMngDisplay.json',
					dataType: 'json',
					data: {
						pointX: pointX,
						pointY: pointY
					},
					success: function (data) {
						if (data.session == 1) {
							$('#modal-notice').modal('hide');

							// console.log('saveMngDisplay success.');
							oConfigure.pointX = pointX;
							oConfigure.pointY = pointY;
							setTimeout(function () {
								oCommon.modalAlert('modal-notice', '알림', '기본 화면 지점으로 설정되었습니다.');
							}, 500);
						}
					},
					error: function (xhr, status, error) {
						console.log('saveMngDisplay failure.');
					}
				});
			});
		}
	},

	bookmark: {
		layer: null,
		overlay: null,
		element: null,
		init: function () {
			this.element = $('<div/>', {
				'id': 'ol-overlay-bookmark'
			})[0];

			this.overlay = new ol.Overlay({
				element: this.element,
				autoPan: true
			});

			olMap.map.addOverlay(this.overlay);

			this.layer = new ol.layer.Vector({
				name: 'bookmark',
				renderMode: 'vector',
				renderOrder: ol.ordering.yOrdering(),
				zIndex: 60,
				source: new ol.source.Vector(),
			});
			olMap.map.addLayer(this.layer);

			this.list();

			$('#ol-map-menu-bookmark-share-yn').click(function () {
				$(this).removeClass('active');
				$(this).blur();
				let $i = $(this).find('i');
				if ($i.hasClass('fa-check-square')) {
					$i.removeClass('fa-check-square');
					$i.addClass('fa-square');
				} else if ($i.hasClass('fa-square')) {
					$i.removeClass('fa-square');
					$i.addClass('fa-check-square');
				}
				olSwipMap.bookmark.list();
			});
		},
		open: function (element, openYn) {
			if ($(element).is('a')) {
				let oData = $(element).data();
				$.ajax({
					type: 'POST',
					url:contextRoot + '/mntr/popupBookmark.do',
					data: {
						bookmarkId: oData.bookmarkId,
						role: 'R',
					},
					async: true,
					success: function (result) {
						olMap.setCenter([
							oData.pointX, oData.pointY
						]);
						if (openYn == 'Y') {
							let oCoordinate = ol.proj.transform([
								oData.pointX, oData.pointY
							], 'EPSG:4326', oGis.projection);
							// contents
							setTimeout(function () {
								let oBookmark = olSwipMap.bookmark;
								let elBookmark = oBookmark.element;
								$(elBookmark).popover('destroy');
								oBookmark.overlay.setPosition(oCoordinate);
								$(elBookmark).popover({
									placement: 'top',
									animation: false,
									html: true,
									content: result
								});
								$(elBookmark).popover('show');
							}, 500);
						}
					},
					error: function (e) {
						console.log(e);
					}
				});
			}
		},
		close: function () {
			$(this.element).popover('destroy');
			this.overlay.setPosition(undefined);
		},
		list: function () {
			const $bookmarkList = $('#ol-map-menu-bookmark-list');
			$bookmarkList.empty();

			let sShareYn = 'Y';
			const $i = $('#ol-map-menu-bookmark-share-yn i');
			if ($i.hasClass('fa-check-square')) {
				sShareYn = 'Y';
			} else if ($i.hasClass('fa-square')) {
				sShareYn = 'N';
			}

			$.ajax({
				type: 'POST',
				url:contextRoot + '/mntr/bookmarkList.json',
				data: {
					shareYn: sShareYn
				},
				success: function (data) {
					// For List
					const oList = data.list;
					// For Layer
					const olSource = olSwipMap.bookmark.layer.getSource();
					olSource.clear();
					if (oList.length == 0) {
						const $li = $('<li/>', {});
						const $anchor = $('<a/>', {
							'href': '#',
							'text': '저장된 즐겨찾기가 없습니다.',
							'title': '저장된 즐겨찾기가 없습니다.',
							'style': 'max-width: 300px;'
						});
						$li.append($anchor);
						$li.appendTo($bookmarkList);
					} else {
						$.each(oList, function (i, v) {
							const $li = $('<li/>', {});
							let sBookmarkNm = '';
							if (v.shareYn == 'Y') {
								sBookmarkNm = v.bookmarkNm + ' <i class="fas fa-share-alt" title="공유됨"></i>';
							} else {
								sBookmarkNm = v.bookmarkNm;
							}

							const $anchor = $('<a/>', {
								'href': '#',
								'class': 'text-ellipsis',
								'title': v.bookmarkNm,
								'html': sBookmarkNm,
								'data-point-x': v.pointX,
								'data-point-y': v.pointY,
								'data-bookmark-id': v.bookmarkId,
								'onclick': 'javascript:olSwipMap.bookmark.open(this, "N");',
								'style': 'max-width: 300px;'
							});
							$li.append($anchor);
							$li.appendTo($bookmarkList);
							const oCoordinate = ol.proj.transform([
								parseFloat(v.pointX), parseFloat(v.pointY)
							], 'EPSG:4326', oGis.projection);
							const olGeometry = new ol.geom.Point(oCoordinate)
							const olFeature = new ol.Feature({
								geometry: olGeometry,
							});
							// Properties
							olFeature.setProperties(v);
							// Style
							let sGlyph = 'fa-star';
							if (v.shareYn == 'Y') {
								sGlyph = 'fa-share-alt';
							}

							olFeature.setStyle(new ol.style.Style({
								image: new ol.style.FontSymbol({
									glyph: sGlyph,
									fontStyle: 'bold',
									fontSize: '0.7',
									form: 'bookmark',
									radius: 15,
									fill: new ol.style.Fill({
										color: 'navy'
									}),
									stroke: new ol.style.Stroke({
										color: '#fff',
										width: 3
									})
								}),
								text: new ol.style.Text({
									font: 'bold 11px NanumGothic',
									text: v.bookmarkNm,
									fill: new ol.style.Fill({
										color: 'navy'
									}),
									stroke: new ol.style.Stroke({
										color: '#fff',
										width: 3
									}),
									offsetY: 25
								})
							}));
							// Add
							olSource.addFeature(olFeature);
						});
					}
				},
				error: function (e) {
					console.log(e);
				}
			});
		},
		add: function (pointX, pointY) {
			$.ajax({
				type: 'POST',
				url:contextRoot + '/mntr/popupBookmark.do',
				data: {
					pointX: pointX,
					pointY: pointY,
					role: 'C',
				},
				async: true,
				success: function (result) {
					let oContextmenu = olSwipMap.contextmenu;

					$(oContextmenu.element).popover('destroy');
					oContextmenu.overlay.setPosition(undefined);

					let sProjectionCode = olMap.map.getView().getProjection().getCode();
					let oCoordinate = ol.proj.transform([
						pointX, pointY
					], 'EPSG:4326', sProjectionCode);

					// contents
					setTimeout(function () {
						let oBookmark = olSwipMap.bookmark;
						let elBookmark = oBookmark.element;
						$(elBookmark).popover('destroy');
						oBookmark.overlay.setPosition(oCoordinate);
						$(elBookmark).popover({
							placement: 'top',
							animation: false,
							html: true,
							content: result
						});
						$(elBookmark).popover('show');
					}, 500);
				},
				error: function (e) {
					console.log(e);
				}
			});
		},
		addProc: function () {
			let oParams = {
				pointX: $('#bookmark-point-x').val(),
				pointY: $('#bookmark-point-y').val(),
				bookmarkNm: $('#bookmark-nm').val(),
				bookmarkDesc: $('#bookmark-desc').val(),
				shareYn: $('#bookmark-share-yn').is(':checked') ? 'Y' : 'N'
			};

			if (!isNaN(oParams.pointX) && !isNaN(oParams.pointY) && oParams.bookmarkNm) {
				$.ajax({
					type: 'POST',
					url:contextRoot + '/mntr/addBookmark.json',
					data: oParams,
					success: function (data) {
						if (typeof data.errors != 'undefined') {
							let sMessage = data.msg;
							let oErrors = data.errors;
							$.each(oErrors, function (i, v) {
								sMessage += '\r\n' + v.defaultMessage;
							});
							oCommon.modalAlert('modal-notice', '알림', sMessage);
						} else {
							oCommon.modalAlert('modal-notice', '알림', data.msg);
						}
						let oBookmark = olSwipMap.bookmark;
						oBookmark.list();
						let elBookmark = oBookmark.element;
						$(elBookmark).popover('destroy');
						oBookmark.overlay.setPosition(undefined);
					},
					error: function (e) {
						console.log(e);
					}
				});
			} else {
				alert('즐겨찾기 등록에 필요한 항목중 누락된 항목이 있습니다.');
			}
		},
		update: function (element) {
			if ($(element).is('button')) {
				let oData = $(element).data();
				$.ajax({
					type: 'POST',
					url:contextRoot + '/mntr/popupBookmark.do',
					data: {
						bookmarkId: oData.bookmarkId,
						role: 'U',
					},
					async: true,
					success: function (result) {
						let sProjectionCode = olMap.map.getView().getProjection().getCode();
						let oCoordinate = ol.proj.transform([
							oData.pointX, oData.pointY
						], 'EPSG:4326', sProjectionCode);

						olMap.view.setCenter(oCoordinate);
						// contents
						setTimeout(function () {
							let oBookmark = olSwipMap.bookmark;
							let elBookmark = oBookmark.element;
							$(elBookmark).popover('destroy');

							oBookmark.overlay.setPosition(oCoordinate);
							$(elBookmark).popover({
								placement: 'top',
								animation: false,
								html: true,
								content: result
							});
							$(elBookmark).popover('show');
						}, 500);
					},
					error: function (e) {
						console.log(e);
					}
				});
			}
		},
		updateProc: function () {
			let oParams = {
				pointX: $('#bookmark-point-x').val(),
				pointY: $('#bookmark-point-y').val(),
				bookmarkId: $('#bookmark-id').val(),
				bookmarkNm: $('#bookmark-nm').val(),
				bookmarkDesc: $('#bookmark-desc').val(),
				shareYn: $('#bookmark-share-yn').is(':checked') ? 'Y' : 'N'
			};

			if (!isNaN(oParams.pointX) && !isNaN(oParams.pointY) && oParams.bookmarkNm) {
				$.ajax({
					type: 'POST',
					url:contextRoot + '/mntr/updateBookmark.json',
					data: oParams,
					success: function (data) {
						if (typeof data.errors != 'undefined') {
							let sMessage = data.msg;
							let oErrors = data.errors;
							$.each(oErrors, function (i, v) {
								sMessage += '\r\n' + v.defaultMessage;
							});
							oCommon.modalAlert('modal-notice', '알림', sMessage);
						} else {
							oCommon.modalAlert('modal-notice', '알림', data.msg);
						}
						let oBookmark = olSwipMap.bookmark;
						$(oBookmark.element).popover('destroy');
						oBookmark.overlay.setPosition(undefined);
						oBookmark.list();
					},
					error: function (e) {
						console.log(e);
					}
				});
			} else {
				alert('즐겨찾기 수정에 필요한 항목중 누락된 항목이 있습니다. 좌표정보, 제목');
			}
		},
		delProc: function (element) {
			if ($(element).is('button')) {
				let oData = $(element).data();

				$.ajax({
					type: 'POST',
					url:contextRoot + '/mntr/delBookmark.json',
					data: {
						bookmarkId: oData.bookmarkId
					},
					success: function (data) {
						oCommon.modalAlert('modal-notice', '알림', data.msg);
						let oBookmark = olSwipMap.bookmark;
						$(oBookmark.element).popover('destroy');
						oBookmark.overlay.setPosition(undefined);
						oBookmark.list();
					},
					error: function (e) {
						console.log(e);
					}
				});
			}
		},
		check: function (event, feature, layer) {
			if (olSwipMap.bookmark.layer === layer) {
				const oProperties = feature.getProperties();
				delete oProperties.geometry;
				const $anchor = $('<a/>');
				$anchor.data(oProperties);
				if (event.type == 'click') {
					olSwipMap.bookmark.open($anchor, 'Y');
				}
			}
		}
	},

	// olSwipMap.popover
	popover: {
		overlay: null,
		element: null,
		init: function () {
			this.element = $('<div/>', {
				'id': 'ol-overlay-popover',
			})[0];

			this.overlay = new ol.Overlay({
				element: this.element
			});

			olMap.map.addOverlay(this.overlay);
		},
		open: function (element) {
			this.close();

			let oData = $(element).data();

			let nPointX = parseFloat(oData.pointX);
			let nPointY = parseFloat(oData.pointY);

			let oCoordinate = [
				nPointX, nPointY
			];

			let $title = $('<div/>', {
				'id': 'ol-overlay-popover-title'
			});

			$title.append($('<span/>', {
				'text': oData.title
			}));

			$title.append($('<button/>', {
				'type': 'button',
				'class': 'close ol-overlay-close',
				'title': '닫기',
				'onclick': 'javascript:olSwipMap.popover.close();',
				'html': '<i class="fas fa-times"></i>'
			}));

			let olView = olMap.map.getView();
			olView.setCenter(oCoordinate);

			setTimeout(function () {
				let oPopover = olSwipMap.popover;
				let elPopover = oPopover.element;
				oPopover.overlay.setPosition(oCoordinate);
				$(elPopover).popover({
					placement: 'top',
					animation: false,
					html: true,
					title: $title.prop('outerHTML'),
					content: element
				});
				$(elPopover).popover('show');
			}, 500);
		},
		close: function () {
			$(this.element).popover('destroy');
			this.overlay.setPosition(undefined);
		},
		extra: function (event, feature, layer) {
			if (feature && layer && olMap.layers.angle === layer) {
				const oProperties = feature.getProperties();
				delete oProperties.geometry;
				if (event.type == 'pointermove') {
					$('#ol-overlay-popover-extra').show();
					$('#ol-overlay-popover-extra').text(oProperties.fcltLblNm);
					olSwipMap.overlays.popoverEx.setPosition(event.coordinate);
				}
			} else {
				$('#ol-overlay-popover-extra').hide();
				$('#ol-overlay-popover-extra').text('');
				olSwipMap.overlays.popoverEx.setPosition(undefined);
			}
		},
	},

	indexmap: {
		map: null,
		view: null,
		center: null,
		zoom: 12,
		layers: {
			lineSig: null,
			lineDong: null,
			lineLi: null,
		},
		visible: {
			lineSig: true,
			lineDong: true,
			lineLi: false,
		},
		init: function () {
			this.map = new ol.Map({
				interactions: ol.interaction.defaults({
					doubleClickZoom: false,
					dragAndDrop: false,
					dragPan: false,
					keyboardPan: false,
					keyboardZoom: false,
					mouseWheelZoom: false,
					pointer: false,
					select: false
				}),
				controls: ol.control.defaults({
					attribution: false,
					rotate: false,
					zoom: false,
				}),
				target: 'map-index'
			});

			let nPointX = (olMap.extent[0] + olMap.extent[2]) / 2;
			let nPointY = (olMap.extent[1] + olMap.extent[3]) / 2;

			this.view = new ol.View({
				projection: oGis.projection,
				extent: olMap.extent,
				minZoom: this.zoom,
				maxZoom: this.zoom,
				center: [
					nPointX, nPointY
				]
			});

			this.map.setView(this.view);
			let olLayerBase = $.extend(true, {}, olMap.layers.base);
			this.map.addLayer(olLayerBase);

			this.view.fit(olMap.extent, {
				nearest: true
			});

			if (oGis.urlWms != '') {
				if (this.visible.lineLi) {
					let sCqlFilterLiCd = 'LI_CD LIKE ' + "'" + oConfigure.sigunguCd + "%'";
					if (oConfigure.lgDongCdBase) {
						let oSplit = oConfigure.lgDongCdBase.toString().split(',');
						$.each(oSplit, (index, lgDongCd) => {
							if (index != 0) {
								sCqlFilterLiCd += ' OR LI_CD LIKE ' + "'" + lgDongCd + "%'";
							} else {
								sCqlFilterLiCd = ' OR LI_CD LIKE ' + "'" + lgDongCd + "%'";
							}
						});
					}

					this.layers.lineLi = new ol.layer.Image({
						zIndex: 25,
						source: new ol.source.ImageWMS({
							ratio: 1,
							url: oGis.urlWms,
							params: {
								'FORMAT': 'image/png',
								'VERSION': '1.1.1',
								'LAYERS': 'cite:geo_line_li',
								'exceptions': 'application/vnd.ogc.se_inimage',
								'CQL_FILTER': sCqlFilterLiCd
							},
							crossOrigin: 'anonymous',
						})
					});
					this.map.addLayer(this.layers.lineLi);
				}

				let sCqlFilterEmdCd = 'EMD_CD LIKE ' + "'" + oConfigure.sigunguCd + "%'";
				if (oConfigure.lgDongCdBase) {
					let oSplit = oConfigure.lgDongCdBase.toString().split(',');
					$.each(oSplit, (index, lgDongCd) => {
						if (index != 0) {
							sCqlFilterEmdCd += ' OR EMD_CD LIKE ' + "'" + lgDongCd + "%'";
						} else {
							sCqlFilterEmdCd = 'EMD_CD LIKE ' + "'" + lgDongCd + "%'";
						}
					});
				}

				this.layers.lineDong = new ol.layer.Image({
					zIndex: 20,
					source: new ol.source.ImageWMS({
						ratio: 1,
						url: oGis.urlWms,
						params: {
							'FORMAT': 'image/png',
							'VERSION': '1.1.1',
							'LAYERS': 'cite:geo_line_dong',
							'exceptions': 'application/vnd.ogc.se_inimage',
							'CQL_FILTER': sCqlFilterEmdCd
						},
						crossOrigin: 'anonymous',
					})
				});
				this.map.addLayer(this.layers.lineDong);

				let sCqlFilter = 'SIG_CD = ' + "'" + oConfigure.sigunguCd + "'";
				if (oConfigure.lgDongCdBase) {
					sCqlFilter = 'SIG_CD IN (' + oConfigure.lgDongCdBase + ')';
				}

				this.layers.lineSig = new ol.layer.Image({
					zIndex: 15,
					visible: true,
					source: new ol.source.ImageWMS({
						ratio: 1,
						url: oGis.urlWms,
						params: {
							'FORMAT': 'image/png',
							'VERSION': '1.1.1',
							'LAYERS': 'cite:geo_line_sig',
							'exceptions': 'application/vnd.ogc.se_inimage',
							'CQL_FILTER': sCqlFilter
						},
						crossOrigin: 'anonymous',
					})
				});

				this.map.addLayer(this.layers.lineSig);
			}

			this.listeners.click = this.map.on('click', this.events.click);
			this.listeners.moveend = olMap.map.on('moveend', this.events.moveend);

			let oCoordinate = olMap.view.getCenter();
			olSwipMap.indexmap.point.set({
				coordinate: oCoordinate,
				projection: oGis.projection,
			});

		},
		listeners: {
			click: null,
			moveend: null
		},
		events: {
			click: function (event) {
				olMap.setCenter(event.coordinate, oGis.projection);
			},
			moveend: function (event) {
				let oCoordinate = event.map.getView().getCenter();
				olSwipMap.indexmap.point.set({
					coordinate: oCoordinate,
					projection: oGis.projection,
				});
			}
		},
		show: function () {
			$('#map-index-container').show();
			if (this.map != null) {
				this.map.updateSize();
			}
		},
		hide: function () {
			$('#map-index-container').hide();
		},
		collapse: function (element) {
			$('#map-index').toggle();
			let isVisible = $('#map-index').is(':visible');

			let $i = null;
			if (typeof element == 'undefined') {
				$i = $('#btn-ol-map-index-collapse').find('i');
			} else {
				$i = $(element).find('i');
			}

			if (isVisible) {
				$i.removeClass('fa-caret-square-right');
				$i.addClass('fa-caret-square-right');
			} else {
				$i.removeClass('fa-caret-square-right');
				$i.addClass('fa-caret-square-left');
			}
		},
		point: {
			overlay: null,
			set: function (data) {
				let elOverlay = $('<div/>', {
					'id': 'ol-overlay-indexmap',
					'class': 'text-danger',
					'html': '<i class="fas fa-map-marker-alt fa-lg"></i>'
				})[0];

				if (this.overlay != null) {
					olSwipMap.indexmap.map.removeOverlay(this.overlay);
					this.overlay = null;
				}

				this.overlay = new ol.Overlay({
					element: elOverlay,
					offset: [
						-6, -15
					]
				});
				olSwipMap.indexmap.map.addOverlay(this.overlay);
				this.overlay.setPosition(data.coordinate);
			},
			clear: function () {
				if (this.overlay != null) {
					olSwipMap.indexmap.map.removeOverlay(this.overlay);
					this.overlay = null;
				}
			}
		}
	},

	cctv: {
		addFeature: function (properties) {
			//console.log('olSwipMap.cctv.addFeature(), properties => %o',properties);
			if (properties.hasOwnProperty('geometry')) delete properties.geometry;
			let sProjectionCode = olMap.map.getView().getProjection().getCode();
			let olSource = olMap.layers.vector.getSource();
			let oCoordinate = ol.proj.transform([properties.pointX, properties.pointY], 'EPSG:4326', sProjectionCode);
			let olFeature = new ol.Feature({geometry: new ol.geom.Point(oCoordinate)});

			let oOffset = {x: 0, y: -30};
			if (properties.fcltKndDtlCd === 'FT' && properties.cctvOsvtX !== 0 && properties.cctvOsvtY !== 0
												 && properties.cctvOsvtX != null && properties.cctvOsvtY != null) {
				// 고정형 방향각 있음 cctvOsvtAg
				let nAg = parseInt(properties.cctvOsvtAg);
				oOffset = olSwipMap.util.calcOffset(nAg, 65);
				if (nAg < 0) nAg = nAg + 360;
				nAg += 225;
				let olFeatureAg = olFeature.clone();
				olFeatureAg.setStyle(new ol.style.Style({
					image: new ol.style.Icon(({
						anchor: [
							0, 0
						],
						anchorXUnits: 'pixels',
						anchorYUnits: 'pixels',
						scale: 0.25,
						opacity: 1.0,
						rotation: nAg * (Math.PI / 180),
						src: contextRoot + '/images/mntr/gis/etc/type' + oConfigure.iconTy + '/ANGLE_SELECTED.png'
					}))
	
				}));
				olFeatureAg.setProperties(properties);
				olSource.addFeature(olFeatureAg);
			}
			olFeature.setStyle(new ol.style.Style({
				text: new ol.style.Text({
					font: 'bold 10px NanumGothic',
					fill: new ol.style.Fill({
						color: '#fff'
					}),
					backgroundFill: new ol.style.Fill({
						color: '#000'
					}),
					padding: [
						2, 2, 2, 2
					],
					text: ' ' + properties.assignIndex + ' ',
					offsetX: oOffset.x,
					offsetY: oOffset.y
				})
			}));
			olFeature.setProperties(properties);
			//console.log('olSwipMap.cctv.addFeature(), olFeature => %o',olFeature);
			olSource.addFeature(olFeature);
		}
	}
}
