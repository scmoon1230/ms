$(function () {
    oMapPopup = new mapPopup();
    oMapPopup.init();
});

function mapPopup() {
    this.init = function () {

        var sCallback = oMapPopup.getUrlParameter('callback');

        if (!sCallback) {
            alert('매개변수를 확인해 주세요. 콜백함수명(callback) 필수.');
            window.self.close();
        }

        // 1. BASE, SATELLITE 배경지도 생성.
        olSwipMap.init({
            dropdown: false,
            bookmark: false,
            measure: false,
            contextmenu: true
        });

        // 2. 시설물 레이어 설정.
        olSwipMap.layers.fclt.init({
            cluster: true
        });

        function handler(event) {
            var oOptOptions = {
                hitTolerance: 10
            };

            var oFeaturesFclt = [];
            olMap.map.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
                if (layer != null) {
                    if (olMap.layers.fclt === layer) {
                        var olFeatures = feature.get('features');
                        if (typeof olFeatures == 'undefined') {
                            oFeaturesFclt.push(feature.getProperties());
                        } else if (olMap.layers.angle === layer) {
                            oFeaturesFclt.push(feature.getProperties());
                        } else {
                            $.each(olFeatures, function (i, f) {
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
                oFeaturesFclt.sort(function (a, b) {
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
                        $.each(oFeaturesFclt, function (i, v) {
                            const oAg = olMap.layers.angle.getSource().getFeatureById('AG_' + v.fcltId);
                            let nCctvOsvtAg = NaN;
                            if (oAg != null) {
                                nCctvOsvtAg = oAg.getProperties().cctvOsvtAg;
                                if (nCctvOsvtAg < 0) nCctvOsvtAg = 360 + nCctvOsvtAg;
                                nCctvOsvtAg = Math.round(nCctvOsvtAg / 10) * 10;
                            }

                            let sFcltKndDtlCd = v.fcltKndDtlCd;
                            let sFcltKndDtlNm = '';
                            if (sFcltKndDtlCd === 'RT') {
                                sFcltKndDtlNm = '<i class="fas fa-sync-alt" title="회전형CCTV" data-toggle="tooltip" data-placement="top"></i> ';
                            } else if (sFcltKndDtlCd === 'FT' && !isNaN(nCctvOsvtAg)) {
                                sFcltKndDtlNm = `<i class="fas fa-arrow-up" title="고정형CCTV(${nCctvOsvtAg}˚)" style="transform: rotate(${nCctvOsvtAg}deg)" data-toggle="tooltip" data-placement="top"></i> `;
                            }

                            var $tr = $('<tr/>');
                            $tr.append($('<td/>', {
                                'class': 'text-ellipsis td-fclt-lbl-nm',
                                'title': v.fcltLblNm,
                                'html': sFcltKndDtlNm + v.fcltLblNm,
                                'data-toggle': 'tooltip',
                                'data-placement': 'top',
                            }));
                            $tr.append($('<td/>', {
                                'class': 'text-ellipsis td-fclt-knd-nm',
                                'title': v.fcltKndNm + '(' + v.fcltUsedTyNm + ')',
                                'text': v.fcltKndNm + '(' + v.fcltUsedTyNm + ')',
                                'data-toggle': 'tooltip',
                                'data-placement': 'top',
                            }));

                            var $fcltSttus = $('<span/>', {
                                'text': v.fcltSttusNm
                            });

                            if (v.fcltSttus == '0') {
                                $fcltSttus.css('color', 'green');
                            } else if (v.fcltSttus == '1') {
                                $fcltSttus.css('color', 'red');
                            } else {
                                $fcltSttus.css('color', 'orange');
                            }
                            $tr.append($('<td/>', {
                                'class': 'text-center',
                                'html': $fcltSttus
                            }));
                            if (event.type == 'click') {
                                var $tdReq = $('<td/>');
                                var $btn = $('<button/>', {
                                    'type': 'button',
                                    'class': 'btn btn-default btn-xs',
                                    'title': '선택',
                                    'text': '선택',
                                    'data-gubn': v.fcltKndNm,
                                    'data-name': v.fcltLblNm,
                                    'data-jibun': v.lotnoAdresNm,
                                    'data-road': v.roadAdresNm,
                                    'data-point-x': v.pointX,
                                    'data-point-y': v.pointY,
                                    'data-toggle': 'tooltip',
                                    'data-placement': 'left',
                                    'onclick': 'javascript:oMapPopup.callback(this);'
                                });

                                $tdReq.append($btn);
                                $tr.append($tdReq);
                            } else {
                                $tr.append($('<td/>'));
                            }
                            $tbody.append($tr);
                        });
                    } else {
                        var $tr = $('<tr/>');
                        $tr.append($('<td/>', {
                            'class': 'text-center text-danger',
                            'text': '시설물 ' + nMaxCntFclt + '개 초과(지도를 확대하여 사용)',
                            'colspan': '4'
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
                    $('#ol-overlay-click [data-toggle="tooltip"]').tooltip({container: 'body'});
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

        olSwipMap.search.grid = function (keyword) {
            var sId = 'map-menu-search';
            $('#grid-' + sId).jqGrid({
                url:contextRoot + '/mntr/api/keywordToCoord.json',
                datatype: 'json',
                mtype: 'POST',
                height: 'auto',
                autowidth: true,
                rowNum: 10,
                postData: {
                    keyword: keyword
                },
                cmTemplate: { sortable: false },
                loadComplete: function (data) {
                    oCommon.jqGrid.resize(sId);
                    olSwipMap.search.pagenation(data);
                    oCommon.jqGrid.gridComplete(this);
                },
                colNames: [
                    '구분', '주소', 'road', 'jibun', '명칭', 'pointX', 'pointY', '선택'
                ],
                colModel: [
                    {
                        name: 'gubn',
                        align: 'center',
                        width: 50,
                        cellattr: function () {
                            return 'style="width:50px;"'
                        }
                    }, {
                        name: 'addr',
                        formatter: function (cellvalue, options, rowObject) {

                            var road = rowObject.roadAddr;
                            var jibun = rowObject.jibunAddr;

                            var $div = $('<div/>');
                            var $spanJibun = $('<span/>', {
                                'class': 'label label-default',
                                'text': '지번',
                                'style': 'margin-right: 3px;'
                            });
                            var $spanRoad = $('<span/>', {
                                'class': 'label label-default',
                                'text': '도로',
                                'style': 'margin-right: 3px;'
                            });
                            $div.append($spanJibun);
                            $div.append(jibun == '' ? '' : jibun);
                            $div.append(' <br>');
                            $div.append($spanRoad);
                            $div.append(road == '' ? '' : road);
                            return $div[0].innerHTML;
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
                    }, {
                        name: 'pointX',
                        hidden: true
                    }, {
                        name: 'pointY',
                        hidden: true
                    }, {
                        name: 'button',
                        align: 'center',
                        formatter: function (cellvalue, options, rowObject) {
                            var $button = $('<button/>', {
                                'type': 'button',
                                'class': 'btn btn-default btn-xs',
                                'title': '해당 내용을 선택합니다.',
                                'html': '선택',
                                'data-gubn': rowObject.gubn,
                                'data-name': rowObject.poi,
                                'data-jibun': rowObject.jibunAddr,
                                'data-road': rowObject.roadAddr,
                                'data-point-x': rowObject.pointX,
                                'data-point-y': rowObject.pointY,
                                'onclick': 'javascript:oMapPopup.callback(this);'
                            });
                            return $button.prop('outerHTML');
                        },
                        cellattr: function () {
                            return 'style="width: 50px;"';
                        },
                        width: 50
                    }
                ],
                jsonReader: {
                    root: "result.addr",
                    page: 1,
                    total: function (data) {
                        var nRecords = typeof data.result.addr !== 'undefined' ? data.result.addr.length : 0;
                        var nRowNum = $('#grid-' + sId).getGridParam('rowNum');
                        var nTotal = Math.ceil(nRecords / nRowNum);
                        return nTotal;
                    },
                    records: function (data) {
                        $('#ol-map-menu-search-result').text(data.result.msg);
                        return typeof data.result.addr !== 'undefined' ? data.result.addr.length : 0;
                    }
                },
                onSelectRow: function (id) {
                    var oData = $('#grid-' + sId).jqGrid('getRowData', id);
                    var nLon = parseFloat(oData.pointX);
                    var nLat = parseFloat(oData.pointY);
                    if (!isNaN(nLon) && !isNaN(nLat)) {
                        var sProjectionCode = olMap.map.getView().getProjection().getCode();

                        var $content = $('<div/>');

                        var $table = $('<table/>', {
                            'class': 'table table-striped table-condensed',
                            'style': 'margin: 0px;'
                        });

                        var $tbody = $('<tbody/>');
                        $tbody.appendTo($table);

                        var $tr = $('<tr/>');

                        var $th = $('<th/>', {
                            'scope': 'row'
                        })

                        var $td = $('<td/>');

                        var $trLon = $tr.clone();
                        $trLon.append($th.clone().text('경도'));
                        $trLon.append($td.clone().text(nLon.toFixed(6)));
                        $trLon.appendTo($tbody);

                        var $trLat = $tr.clone();
                        $trLat.append($th.clone().text('위도'));
                        $trLat.append($td.clone().text(nLat.toFixed(6)));
                        $trLat.appendTo($tbody);

                        var $trAddr = $tr.clone();
                        $trAddr.append($th.clone().text('주소'));
                        $trAddr.append($td.clone().html(oData.addr));
                        $trAddr.appendTo($tbody);

                        if (oData.poi && oData.poi != '') {
                            var $trBuldNm = $tr.clone();
                            $trBuldNm.append($th.clone().text('명칭'));
                            $trBuldNm.append($td.clone().text(oData.poi));
                            $trBuldNm.appendTo($tbody);
                        }

                        // close button
                        $content.append('<button type="button" class="close ol-overlay-close" title="닫기" onclick="javascript:olSwipMap.search.close(true);"><i class="fas fa-times"></i></button>');
                        $content.append($table);

                        olMap.setCenter([
                            nLon, nLat
                        ]);

                        // contents
                        setTimeout(function () {
                            var oSearch = olSwipMap.search;
                            var elSearch = oSearch.element;
                            $(elSearch).popover('destroy');
                            var oCoordinate = ol.proj.transform([
                                nLon, nLat
                            ], 'EPSG:4326', oGis.projection);
                            oSearch.overlay.setPosition(oCoordinate);
                            $(elSearch).popover({
                                placement: 'top',
                                animation: false,
                                html: true,
                                content: $content.prop('outerHTML')
                            });
                            $(elSearch).popover('show');
                        }, 500);
                    } else {
                        alert('좌표값이 없는 데이터입니다.');
                    }
                },
                loadonce: true
            });
        }
    };

    this.callback = function (element) {
        var oData = $(element).data();

        if (oData.hasOwnProperty('bs.tooltip')) {
            oData['bs.tooltip'] = undefined;
        }

        var sData = JSON.stringify(oData);
        var sCallback = oMapPopup.getUrlParameter('callback');
        $(opener.location).attr('href', 'javascript:' + sCallback + '(' + sData + ');');
    };

    this.getUrlParameter = function (param) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL.split('&'), sParameterName, i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] === param) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    };
};
