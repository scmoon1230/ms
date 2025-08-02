$(function () {
    oCctvInfo = $('#cctvInfo').data();

    oCctvInfo.pointX = parseFloat(oCctvInfo.pointX);
    oCctvInfo.pointY = parseFloat(oCctvInfo.pointY);

    oMngPresetReg = new mngPresetReg();
    oMngPresetReg.init();
});

function mngPresetReg() {
    this.coordinate = null;
    this.init = function () {
        oConfigure.mntrViewLeft = 500;
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);

        appendDiv(new div('DIV-VMS', 'B', 0), '', '');

        var fnCallback = function () {
            setTimeout(function () {
                var nWidth = $('#web-rtc-view').width() - 15;
                var nHeight = parseInt(oConfigure.mntrViewBottom) - 6;
                var $container = oVmsService.createVideoElement(oCctvInfo, {
                    'width': nWidth,
                    'height': nHeight
                });

                var elVideo = $container.find('video')[0];
                oCctvInfo.sessionId = $(elVideo).data('sessionId');
                $('#web-rtc-view').append($container);
                oVmsCommon.play(elVideo);
            }, 500);
        };

        collapse({
            left: false,
            bottom: false
        }, fnCallback);
        $('input[id^="txt-preset-"]').val('');

        // 위치 수정 활성화
        $('#chk-update-fclt-point').on('change', function () {
            var isChecked = $(this).is(':checked');
            if (isChecked) {
                oMngPresetReg.activateFcltPoint();
            } else {
                oMngPresetReg.deactivateFcltPoint();
            }
        });

        // 위치 수정 처리
        $('#btn-update-fclt-point').on('click', function () {
            oMngPresetReg.updateFcltPoint();
        });

        // 위치 수정 취소
        $('#btn-cancel-fclt-point').on('click', function () {
            oMngPresetReg.deactivateFcltPoint();
        });

        $('body').on('keydown', (e) => {
            if (e.which == 8) {
                e.preventDefault();
                oMngPresetReg.list();
            }
        });

        $('#sel-preset-num').on('change', function () {
            var sPresetBdwStartNum = $('#presetBdwStartNum').text();
            var sVal = $('#sel-preset-num option:selected').val();
            if (sPresetBdwStartNum != sVal) {
                if (confirm('프리셋대역시작번호를 변경하면 기존에 저장되어 있던 프리셋대역은 사용할 수 없습니다. 계속하시겠습니까?')) {
                    $.ajax({
                        type: 'POST',
                        async: false,
                        dataType: 'json',
                        url:contextRoot + '/mntr/updatePresetBdwStartNumProc.json',
                        data: {
                            fcltId: oCctvInfo.fcltId,
                            presetBdwStartNum: sVal
                        },
                        success: function (data) {
                            $('#presetBdwStartNum').text(sVal);
                            alert('프리셋대역시작번호가 변경되었습니다.');
                            oMngPresetReg.refreshPreset();
                            oCctvInfo.presetBdwStartNum = parseInt(sVal);
                        },
                        error: function (data, status, err) {
                            console.log(data);
                        }
                    });
                } else {
                    $('#sel-preset-num').val(sPresetBdwStartNum);
                }
            }
        });

        // 지도 생성
        // 1. BASE, SATELLITE 배경지도 생성.
        olSwipMap.init();

        // 2. 시설물 레이어 설정.
        olSwipMap.layers.fclt.init();

        // 3. 드롭다운 메뉴 : 레이어 선택.
        olSwipMap.layers.dropdownLayers('#dropdown-layers');

        // 4. Feature 핸들러.
        function handler(event) {
            var oOptOptions = {
                hitTolerance: 5
            };

            var oFeaturesFclt = [];
            olMap.map.forEachFeatureAtPixel(event.pixel, function (feature, layer) {
                if (layer != null) {
                    if (olMap.layers.fclt === layer) {
                        var olFeatures = feature.get('features');
                        if (typeof olFeatures == 'undefined') {
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
                            $tr.append($('<td/>'));
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

            if (event.type == 'click') {
                oMngPresetReg.coordinate = event.coordinate;
                var oCoordinate = ol.proj.transform(event.coordinate, oGis.projection, 'EPSG:4326');
                if ($('#chk-update-fclt-point').is(':checked')) {
                    olSwipMap.point.set(event.coordinate, oGis.projection, 'ORANGE', false, false, true);
                    $('#txt-pointX').val(oCoordinate[0].toFixed(6));
                    $('#txt-pointY').val(oCoordinate[1].toFixed(6));
                }

                var $radio = $('input[type=radio].rdo-preset-num:checked');
                if ($radio.length) {
                    var sIndex = $radio[0].value;
                    // 입력
                    $('input[type=text]#txt-preset-' + sIndex + '-point-x').val(oCoordinate[0].toFixed(6));
                    $('input[type=text]#txt-preset-' + sIndex + '-point-y').val(oCoordinate[1].toFixed(6));
                    oMngPresetReg.savePresetNumForOl5(oCoordinate, sIndex);
                    console.log(oCoordinate, sIndex);
                }
            }
        }

        olSwipMap.events.feature.pointermove = handler;
        olSwipMap.events.feature.click = handler;

        olSwipMap.listeners.feature.pointermove = olMap.map.on('pointermove', olSwipMap.events.feature.pointermove);
        olSwipMap.listeners.feature.click = olMap.map.on('click', olSwipMap.events.feature.click);

        olSwipMap.point.set([
            oCctvInfo.pointX, oCctvInfo.pointY
        ], 'EPSG:4326', 'BLUE', true, true, false);

        var nMaxZoom = olMap.map.getView().getMaxZoom();
        olMap.map.getView().setZoom(nMaxZoom);
        oMngPresetReg.refreshPreset();

    };

    this.list = function () {
        const $form = $('#form-search');
        $form.submit();
    };

    this.updateFcltPoint = function () {
        var isChecked = $('#chk-include-mng-sn').is(':checked');
        var sMsg = '시설물명(' + oCctvInfo.fcltLblNm + ')의 위치정보를 수정하시겠습니까?';
        if (isChecked) {
            sMsg = '동일 좌표의 시설물도 함께 위치정보를 수정하시겠습니까?';
        }

        if (confirm(sMsg)) {
            var nPointX = parseFloat($('input[type=text]#txt-pointX').val());
            var nPointY = parseFloat($('input[type=text]#txt-pointY').val());
            $.ajax({
                type: 'POST',
                async: false,
                dataType: 'json',
                url:contextRoot + '/mntr/updateFcltPointProc.json',
                data: {
                    fcltId: oCctvInfo.fcltId,
                    pointX: nPointX,
                    pointY: nPointY,
                    // mngSn : isChecked ? oCctvInfo.mngSn : ''
                    mngSn: isChecked ? 'Y' : ''
                },
                success: function (data) {
                    alert(data.result.size + '개의 시설물 위치정보가 업데이트되었습니다.');
                    oCctvInfo.pointX = nPointX;
                    oCctvInfo.pointY = nPointY;
                    olSwipMap.layers.fclt.refresh();
                    oMngPresetReg.deactivateFcltPoint();
                },
                error: function (data, status, err) {
                    console.log(data);
                }
            });
        }
    };

    this.activateFcltPoint = function () {
        $('#btn-update-fclt-point').prop('disabled', false);
        $('#btn-cancel-fclt-point').prop('disabled', false);
        $('#chk-include-mng-sn').prop('disabled', false);
        $('#sel-preset-num').prop('disabled', true);
        $('.rdo-preset-num').prop('disabled', true);
        $('.btn-preset-num').prop('disabled', true);
    };

    this.deactivateFcltPoint = function () {
        $('#txt-pointX').val(oCctvInfo.pointX.toFixed(6));
        $('#txt-pointY').val(oCctvInfo.pointY.toFixed(6));

        $('#chk-update-fclt-point').prop('checked', false);
        $('#chk-include-mng-sn').prop('checked', true);

        $('#btn-update-fclt-point').prop('disabled', true);
        $('#btn-cancel-fclt-point').prop('disabled', true);
        $('#chk-include-mng-sn').prop('disabled', true);
        $('#sel-preset-num').prop('disabled', false);
        $('.rdo-preset-num').prop('disabled', false);
        $('.btn-preset-num').prop('disabled', false);

        olSwipMap.point.clearAll();
        olSwipMap.point.set([
            oCctvInfo.pointX, oCctvInfo.pointY
        ], 'EPSG:4326', 'BLUE', true, true, false);
    };

    this.savePresetNumForOl5 = function (coordinate, index) {
        var sPresetNum = '0';

        if (oCctvInfo.fcltKndDtlCd == 'RT') {
            sPresetNum = (oCctvInfo.presetBdwStartNum + parseInt(index)).toString();
        }

        $.ajax({
            type: 'POST',
            async: false,
            dataType: 'json',
            url:contextRoot + '/mntr/savePresetProc.json',
            data: {
                fcltId: oCctvInfo.fcltId,
                presetNum: sPresetNum,
                pointX: coordinate[0],
                pointY: coordinate[1]
            },
            success: function (data) {
                if (oCctvInfo.fcltKndDtlCd == 'RT') {
                    oVmsService.api.preset(oVmsService.api.type.preset.set, sPresetNum);
                } else {
                    oCctvInfo.cctvOsvtX = coordinate[0];
                    oCctvInfo.cctvOsvtY = coordinate[1];
                    if (olMap.layers.angle) olMap.layers.angle.getSource().clear();
                    olSwipMap.layers.fclt.source.vector.clear();
                }

                oMngPresetReg.drawPreset(coordinate, sPresetNum);
                oMngPresetReg.deactivatePresetPoint();
            },
            error: function (data, status, err) {
                console.log(data);
            }
        });

    };

    this.deletePresetNum = function (presetNum) {
        if (confirm('삭제하시겠습니까?')) {
            var $pointX = $('#txt-preset-' + presetNum + '-point-x');
            var $pointY = $('#txt-preset-' + presetNum + '-point-y');

            var sSelected = '0';
            if ($('#sel-preset-num option:selected').length) {
                sSelected = $('#sel-preset-num option:selected').val();
            }

            var nPresetNum = parseInt(presetNum) + parseInt(sSelected);
            presetNum = nPresetNum.toString();

            $.ajax({
                type: 'POST',
                async: false,
                dataType: 'json',
                url:contextRoot + '/mntr/deletePresetProc.json',
                data: {
                    fcltId: oCctvInfo.fcltId,
                    presetNum: presetNum
                },
                beforeSend: function () {
                    if ($pointX.val() == '' || $pointY.val() == '' || isNaN($pointX.val()) || isNaN($pointY.val())) {
                        alert('필드 값이 비어있거나 넘버 형식이 아닙니다.');
                        return false;
                    }
                },
                success: function (data) {
                    $pointX.val('');
                    $pointY.val('');

                    oMngPresetReg.deactivatePresetPoint();
                    let oFeatures = olSwipMap.util.getFeaturesByProperties(olMap.layers.vector, 'label', presetNum);
                    if (oFeatures.length) {
                        $.each(oFeatures.features, function (i, v) {
                            olMap.layers.vector.getSource().removeFeature(v);
                        });
                    }

                    if (oCctvInfo.fcltKndDtlCd == 'FT') {
                        oCctvInfo.cctvOsvtX = 0;
                        oCctvInfo.cctvOsvtY = 0;
                        if (olMap.layers.angle) olMap.layers.angle.getSource().clear();
                        olSwipMap.layers.fclt.source.vector.clear();
                    }
                },
                error: function (data, status, err) {
                    console.log(data);
                }
            });
        }
    };

    this.deactivatePresetPoint = function () {
        $('input[name="rdo-preset-num"]').prop('checked', false);
    };

    this.refreshPreset = function () {
        oMngPresetReg.deactivatePresetPoint();
        var olSource = olMap.layers.vector.getSource();
        olSource.clear();

        if (oCctvInfo.fcltKndDtlCd == 'RT') {
            $.ajax({
                type: 'POST',
                async: false,
                dataType: 'json',
                url:contextRoot + '/mntr/selectPresetList.json',
                data: {
                    fcltId: oCctvInfo.fcltId,
                },
                success: function (data) {
                    if (data.result.size != 0) {
                        $.each(data.result.list, function (i, v) {
                            var sChecked = v.presetNum.substring(1, 2);
                            var nPointXPreset = parseFloat(v.pointX);
                            var nPointYPreset = parseFloat(v.pointY);
                            $('#txt-preset-' + sChecked + '-point-x').val(nPointXPreset.toFixed(6));
                            $('#txt-preset-' + sChecked + '-point-y').val(nPointYPreset.toFixed(6));
                            oMngPresetReg.drawPreset([
                                nPointXPreset, nPointYPreset
                            ], v.presetNum);
                        });
                    } else {
                        $('input[id^="txt-preset-"]').val('');
                    }
                },
                error: function (data, status, err) {
                    console.log(data);
                }
            });
        } else if (oCctvInfo.fcltKndDtlCd == 'FT' && oCctvInfo.hasOwnProperty('cctvOsvtX') && oCctvInfo.hasOwnProperty('cctvOsvtY')) {
            var nPointXPreset = parseFloat(oCctvInfo.cctvOsvtX);
            var nPointYPreset = parseFloat(oCctvInfo.cctvOsvtY);
            if (!isNaN(nPointXPreset) && !isNaN(nPointYPreset) && nPointXPreset != 0 && nPointYPreset != 0) {
                $('#txt-preset-0-point-x').val(nPointXPreset.toFixed(6));
                $('#txt-preset-0-point-y').val(nPointYPreset.toFixed(6));
                oMngPresetReg.drawPreset([
                    nPointXPreset, nPointYPreset
                ], '0');
            }
        }
    };

    this.drawPreset = function (coordinate, label) {
        if (typeof label == 'number') {
            label = String(label);
        }

        var oFeatures = olSwipMap.util.getFeaturesByProperties(olMap.layers.vector, 'label', label);
        if (oFeatures.length) {
            $.each(oFeatures.features, function (i, v) {
                olMap.layers.vector.getSource().removeFeature(v);
            });
        }

        var olSource = olMap.layers.vector.getSource();
        const oCoordinateCctv = ol.proj.transform([
            oCctvInfo.pointX, oCctvInfo.pointY
        ], 'EPSG:4326', oGis.projection);

        var oCoordinatePreset = ol.proj.transform(coordinate, 'EPSG:4326', oGis.projection);

        var oColorFill = [
            255, 255, 255, 0.5
        ];

        var oColorStroke = [
            0, 0, 0, 0.5
        ];

        var olFeaturePreset = new ol.Feature({
            geometry: new ol.geom.Point(oCoordinatePreset)
        });

        olFeaturePreset.setProperties({
            label: label
        });

        olFeaturePreset.setStyle(new ol.style.Style({
            geometry: new ol.geom.Point(oCoordinatePreset),
            image: new ol.style.Circle({
                radius: 10,
                fill: new ol.style.Fill({
                    color: new ol.color.asArray(oColorFill)
                }),
                stroke: new ol.style.Stroke({
                    color: new ol.color.asArray(oColorStroke),
                    width: 5
                })
            }),
            text: new ol.style.Text({
                font: 'bold 11px NanumGothic',
                fill: new ol.style.Fill({
                    color: '#000'
                }),
                text: (parseInt(label) % 10).toString(),
                offsetX: 0,
                offsetY: 0,
                rotation: 0
            })
        }));

        var olFeatureLine = new ol.Feature({
            geometry: new ol.geom.LineString([
                oCoordinateCctv, oCoordinatePreset
            ])
        });

        olFeatureLine.setProperties({
            label: label
        });

        olFeatureLine.setStyle(new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: new ol.color.asArray(oColorStroke),
                width: 3
            })
        }));

        olSource.addFeatures([
            olFeaturePreset, olFeatureLine
        ]);
    };
};
