/**
 * 지도 초기 값 : VWORLD
 */
var olMap = {
    map: null,
    view: null,
    extent: null,
    controls: {
        scaleLine: null,
        overviewmap: null
    },
    layers: {
        base: null,
        satellite: null,
        vector: null
    },
    zoom: {
        min: 11,
        max: 19
    },
    init: function () {
        this.controls.scaleLine = new ol.control.ScaleLine();

        this.map = new ol.Map({
            controls: ol.control.defaults({
                zoom: true,
                zoomOptions: {
                    zoomInLabel: $('<i/>', {
                        'class': 'far fa-plus-square'
                    })[0],
                    zoomOutLabel: $('<i/>', {
                        'class': 'far fa-minus-square'
                    })[0],
                    zoomInTipLabel: '확대',
                    zoomOutTipLabel: '축소'
                }
            }).extend([
                new ol.control.MousePosition({
                    projection: 'EPSG:4326',
                    coordinateFormat: ol.coordinate.createStringXY(6)
                }), this.controls.scaleLine, new ol.control.ZoomSlider(),
            ]),
            target: 'map'
        });

        // VIEW 설정 - 맵 범위 설정.
        this.extent = ol.proj.transformExtent([
            parseFloat(oGis.boundsLeft), parseFloat(oGis.boundsBottom), parseFloat(oGis.boundsRight), parseFloat(oGis.boundsTop)
        ], 'EPSG:4326', oGis.projection);

        // 기본도 설정
        if (this.layers.base == null) {
            this.layers.base = new ol.layer.Tile({
                // extent : this.extent,
                visible: true,
                source: new ol.source.XYZ({
                    //url: oGis.url + '/service/{z}/{x}/{y}.png',
                    url: oGis.url + '/{z}/{x}/{y}.png',
                    crossOrigin: 'anonymous',
                }),
            });
        }
        this.map.addLayer(this.layers.base);

        // 위성도 설정
        if (this.layers.satellite == null) {
            this.layers.satellite = new ol.layer.Tile({
                // extent : this.extent,
                visible: false,
                source: new ol.source.XYZ({
                    //url: oGis.urlAerial + '/service/{z}/{x}/{y}.jpeg',
                    url: oGis.urlAerial + '/{z}/{x}/{y}.jpeg',
                    crossOrigin: 'anonymous',
                }),
            });
        }

        this.map.addLayer(this.layers.satellite);

        this.layers.vector = new ol.layer.Vector({
            renderMode: 'vector',
            zIndex: 60,
            source: new ol.source.Vector()
        });
        this.map.addLayer(this.layers.vector);

        // VIEW 설정 - 마지막 좌표 가져 오기.
        let sLastMapCenter = localStorage.getItem('LastMapCenter') || '{}';
        let oLastMapCenter = JSON.parse(sLastMapCenter);

        if (!document.referrer || document.referrer.indexOf('/wrks/lgn/login.do') > -1 || document.referrer.indexOf('/wrks/lgn/logout.do') > -1) {
            oLastMapCenter.baseLayer = oGis.gisTy == 0 ? 'normal' : 'aerial';
        }

        let nLon = oLastMapCenter.lon || 0;
        let nLat = oLastMapCenter.lat || 0;
        if (oLastMapCenter == null || nLon == 0 || nLat == 0) {
            var oCoordinate = ol.proj.transform([
                parseFloat(oConfigure.pointX), parseFloat(oConfigure.pointY)
            ], 'EPSG:4326', oGis.projection);

            oLastMapCenter = {
                lon: oCoordinate[0],
                lat: oCoordinate[1],
                zoom: this.zoom.min,
            }
        }

        if ((oLastMapCenter.lon < this.extent[0]) || (oLastMapCenter.lon > this.extent[2]) || (oLastMapCenter.lat < this.extent[1]) || (oLastMapCenter.lat > this.extent[3])) {
            oLastMapCenter.lon = (this.extent[0] + this.extent[2]) / 2;
            oLastMapCenter.lat = (this.extent[1] + this.extent[3]) / 2;
            console.log('-- olMap.init(): out of extent');
        }

        this.view = new ol.View({
            projection: oGis.projection,
            extent: this.extent,
            minZoom: this.zoom.min,
            maxZoom: this.zoom.max,
            center: [
                oLastMapCenter.lon, oLastMapCenter.lat
            ],
            zoom: oLastMapCenter.zoom + 12
        });

        this.map.setView(this.view);

        console.log('-- oLastMapCenter.baseLayer = '+oLastMapCenter.baseLayer);
        var sBaseLayer = typeof oLastMapCenter.baseLayer == 'undefined' ? 'normal' : oLastMapCenter.baseLayer;

        console.log('-- sBaseLayer = '+sBaseLayer);
        this.setBaseLayer(sBaseLayer);
        this.zoom.current = this.view.getZoom();

        // VIEW 설정 - 마지막 좌표 저장 하기.
        $(window).unload(function () {
            var oCenter = olMap.map.getView().getCenter();
            var nZoom = olMap.map.getView().getZoom() - 12;
            var sLastLayer = '';
            if (olMap.layers.satellite.getVisible()) {
                sLastLayer = 'aerial';
            } else if (olMap.layers.base.getVisible()) {
                sLastLayer = 'normal';
            }

            oLastMapCenter = {
                lon: oCenter[0],
                lat: oCenter[1],
                zoom: nZoom,
                baseLayer: sLastLayer,
            };
            localStorage.setItem('LastMapCenter', JSON.stringify(oLastMapCenter));
        });

        $('.ol-zoomslider-thumb').tooltip({
            container: 'body',
            placement: 'right',
            trigger: 'click',
            title: function () {
                var nZoom = olMap.view.getZoom();
                var nResolution = Math.round(olMap.map.getView().getResolution() * 72 * 39.37);
                return '줌: ' + nZoom + ', 해상도: ' + nResolution;
            }
        });

        this.map.on('moveend', function (e) {
            if (olMap.zoom.current != olMap.view.getZoom()) {
                $('.ol-zoomslider-thumb').tooltip('show');
                setTimeout(function () {
                    $('.ol-zoomslider-thumb').tooltip('hide');
                }, 2000);
            }
            olMap.zoom.current = olMap.view.getZoom();
        });
    },
    setCenter: function (coordinate, projection) {
        if (typeof coordinate !== 'undefined' && coordinate.length == 2) {
            var nLon = parseFloat(coordinate[0]);
            var nLat = parseFloat(coordinate[1]);
            var sProjectionCode = this.view.getProjection().getCode();
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

                var oExtentDokdo = ol.proj.transformExtent([
                    131.860491, 37.237794, 131.873279, 37.247975
                ], 'EPSG:4326', oGis.projection);

                if ((coordinate[0] > olMap.extent[0]) && (coordinate[0] < olMap.extent[2]) && (coordinate[1] > olMap.extent[1]) && (coordinate[1] < olMap.extent[3])) {
                    this.view.setCenter(coordinate);
                } else if ((coordinate[0] > oExtentDokdo[0]) && (coordinate[0] < oExtentDokdo[2]) && (coordinate[1] > oExtentDokdo[1]) && (coordinate[1] < oExtentDokdo[3])) {
                    this.view.fit(oExtentDokdo);
                } else {
                    coordinate = ol.proj.transform(coordinate, sProjectionCode, 'EPSG:4326');
                    alert('잘못된 좌표 값이거나 지도 범위 밖입니다. \n경도: ' + coordinate[0].toFixed(6) + ', 위도: ' + coordinate[1].toFixed(6));
                    console.log('-- olMap.setCenter(%f, %f)', nLon, nLat);
                    console.log('-- olMap.setCenter(%f, %f, %s)', coordinate[0], coordinate[1], projection);
                    return false;
                }
            } else {
                console.log('-- olMap.setCenter(%o, %o);', coordinate, projection);
            }
        } else {
            console.log('-- olMap.setCenter(%o, %o);', coordinate, projection);
        }
    },
    setBaseLayer: function (layerName) {
        var sProjectionBase = this.layers.base.getSource().getProjection().getCode();
        var sProjectionSatellite = this.layers.satellite.getSource().getProjection().getCode();
        if (layerName == 'normal') {
            this.layers.satellite.setVisible(false);
            $('#ol-map-menu-base-aerial').removeClass('active');
            $('#ol-map-menu-base-normal').addClass('active');
        } else if (layerName == 'aerial') {
            this.layers.satellite.setVisible(true);
            $('#ol-map-menu-base-normal').removeClass('active');
            $('#ol-map-menu-base-aerial').addClass('active');
        } else {
            this.layers.satellite.setVisible(false);
            $('#ol-map-menu-base-aerial').removeClass('active');
            $('#ol-map-menu-base-normal').addClass('active');
        }
    }
}
