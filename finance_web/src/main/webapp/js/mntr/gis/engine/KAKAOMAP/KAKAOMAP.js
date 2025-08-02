(function () {
    if (typeof olMap != 'undefined') {
        // VIEW 설정 - 맵 범위 설정.
        this.extent = ol.proj.transformExtent([
            parseFloat(oGis.boundsLeft), parseFloat(oGis.boundsBottom), parseFloat(oGis.boundsRight), parseFloat(oGis.boundsTop)
        ], 'EPSG:4326', oGis.projection);
        // this.extent = [-30000, -60000, 494288, 988576];

        // 기본도 설정
        var oMapOriginBase = [
            -30000, -60000
        ];
        var oMapResolutionsBase = [
            2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 0.5, 0.25
        ];

        olMap.layers.base = new ol.layer.Tile({
            zIndex: 5,
            visible: true,
            source: new ol.source.XYZ({
                tileSize: 256,
                projection: new ol.proj.get('EPSG:5181'),
                tileUrlFunction: function (coordinate) {
                    if (coordinate === null) return undefined;
                    var s = Math.floor(Math.random() * 4);  // 0 ~ 3
                    //var z = 'L' + (oMapResolutionsBase.length - coordinate[0]);
                    let z = (oGis.url.includes('map_2d') ? 'L' : '') + (oMapResolutionsBase.length - coordinate[0]);
                    var x = coordinate[1];
                    var y = coordinate[2];
                    oGis.url.replace('https://map', 'https://map' + s);
                    return oGis.url + '/' + z + '/' + y + '/' + x + '.png';
                },
                tileGrid: new ol.tilegrid.TileGrid({
                    origin: oMapOriginBase,
                    resolutions: oMapResolutionsBase,
                    tileSize: [256, 256],
                }),
                crossOrigin: 'anonymous',
            }),
        });

        olMap.layers.satellite = new ol.layer.Tile({
            zIndex: 10,
            visible: false,
            source: new ol.source.XYZ({
                tileSize: 256,
                projection: new ol.proj.get('EPSG:5181'),
                tileUrlFunction: function (coordinate) {
                    if (coordinate === null) return undefined;
                    var s = Math.floor(Math.random() * 4);  // 0 ~ 3
                    var z = 'L' + (oMapResolutionsBase.length - coordinate[0]);
                    var x = coordinate[1];
                    var y = coordinate[2];
                    oGis.urlAerial.replace('https://map', 'https://map' + s);
                    return oGis.urlAerial + '/' + z + '/' + y + '/' + x + '.jpg?v=160114';
                },
                tileGrid: new ol.tilegrid.TileGrid({
                    origin: oMapOriginBase,
                    resolutions: oMapResolutionsBase,
                    tileSize: [256, 256],
                }),
                crossOrigin: 'anonymous',
            }),
        });

        if (!isNaN(oGis.zoomIndex) && !isNaN(oGis.zoomMin) && !isNaN(oGis.zoomMax)) {
            olMap.zoom.min = parseInt(oGis.zoomMin);
            olMap.zoom.max = parseInt(oGis.zoomMax);
            olSwipMap.indexmap.zoom = parseInt(oGis.zoomIndex);
        }
    }
})();
