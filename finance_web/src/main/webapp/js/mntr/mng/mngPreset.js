$(function () {
    oMngPreset = new mngPreset();
    oMngPreset.init();
});

function mngPreset() {
    this.init = function () {
        oConfigure.mntrViewLeft = 600;
        $('aside#left').css('width', oConfigure.mntrViewLeft);
        $('section#body').css('left', oConfigure.mntrViewLeft + 10);
        $('#toggleLeft').css('left', oConfigure.mntrViewLeft);
        collapse({
            left: false
        });

        fcltUsedTyList('#searchFcltUsedTyCd', 'CTV', $('#searchFcltUsedTyCd').data('selected'));
        $('#searchFcltKndDtlCd').val($('#searchFcltKndDtlCd').data('selected'));
        $('#searchPresetYn').val($('#searchPresetYn').data('selected'));
        $('#searchGbn').val($('#searchGbn').data('selected') || 'NM');

        oMngPreset.grid();
        $('#searchKeyword').keypress(function (event) {
            if (event.which == 13) {
                oMngPreset.reloadGrid();
                return false;
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
                hitTolerance: 10
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
                    } else if (olMap.layers.angle === layer) {
                        oFeaturesFclt.push(feature.getProperties());
                    } else {
                        olSwipMap.bookmark.check(event, feature, layer);
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
                                var $button = $('<button/>', {
                                    'class': 'btn btn-default btn-xs',
                                    'text': '설정',
                                    'onclick': 'javascript:oMngPreset.preset("' + v.fcltId + '");'
                                });

                                $tr.append($('<td/>', {
                                    html: $button.prop('outerHTML')
                                }));
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
    };

    this.list = function (feature) {
        var eTable = $('<table/>', {
            'id': 'tbFcltInfo',
            'class': 'table table-striped table-condensed',
        });
        var eCaption = $('<caption/>', {
            'html': '총 <strong style="color: red;">' + feature.count + '</strong>개의 시설물이 있습니다.'
        });
        var eThead = $('<thead/>');
        var eTheadTr = $('<tr/>', {
            'class': 'warning'
        });
        var eTh = $.parseHTML('<th>[관리번호]시설물명</th><th>물품구분</th><th>기능별유형</th><th>부가기능</th>');
        var eTbody = $.parseHTML(feature.html);
        eTheadTr.append(eTh);
        eThead.append(eTheadTr);
        eTable.append(eCaption);
        eTable.append(eThead);
        eTable.append(eTbody);
        return eTable.prop('outerHTML');
    };

    this.item = function (feature) {
        return oMngPreset.templates(feature, 'ITEM');
    };

    this.single = function (feature) {
        return oMngPreset.templates(feature, 'SINGLE');
    };

    this.templates = function (feature, type) {
        var oAttributes = feature.attributes;
        var sFcltId = oAttributes.fcltId;
        var sFcltLblNm = oAttributes.fcltLblNm;
        var sFcltGdsdtNm = (oAttributes.fcltGdsdtTy == '0') ? '주' : '보조';
        var sFcltKndDtlNm = (oAttributes.fcltKndDtlCd == 'RT') ? '회전' : '고정';

        var eTbodyTr = $('<tr/>');
        eTbodyTr.append($('<td/>', {
            'scope': 'row',
            'html': sFcltLblNm
        }));
        eTbodyTr.append($('<td/>', {
            'text': sFcltGdsdtNm
        }));
        eTbodyTr.append($('<td/>', {
            'text': sFcltKndDtlNm
        }));

        var eButton = $('<button/>', {
            'class': 'btn btn-default btn-xs',
            'style': 'height: 20px; padding: 0px 5px 0px 5px;',
            'text': '설정'
        });
        eButton.attr('onclick', 'javascript:oMngPreset.preset("' + sFcltId + '");');

        eTbodyTr.append($('<td/>', {
            'html': eButton
        }));

        var sOuterHTML = eTbodyTr.prop('outerHTML');
        if (type == 'SINGLE') {
            var eTable = $('<table/>', {
                'id': 'tbFcltInfo',
                'class': 'table table-striped table-condensed',
            });

            var eCaption = $('<caption/>', {
                'html': '&nbsp;'
            });

            var eThead = $('<thead/>');
            var eTheadTr = $('<tr/>', {
                'class': 'warning'
            });
            var eTh = $.parseHTML('<th>[관리번호]시설물명</th><th>주</th><th>회전</th><th>부가기능</th>');
            var eTbody = $('<tbody/>');
            eTheadTr.append(eTh);
            eThead.append(eTheadTr);
            eTable.append(eCaption);
            eTable.append(eThead);
            eTbody.append(eTbodyTr);
            eTable.append(eTbody);
            sOuterHTML = eTable.prop('outerHTML');
        }

        return sOuterHTML;
    };

    this.preset = function (fcltId) {
        const $form = $('#form-search');
        $form.attr('action', contextRoot + '/mntr/mngPresetReg.do');
        $form.append($('<input/>', {'type': 'hidden', 'name': 'fcltId', 'id': 'fcltId', 'value': fcltId}));
        $form.submit();
    };

    this.grid = function () {
        // CCTV 목록
        $('#grid-preset').jqGrid({
            url:contextRoot + '/mntr/selectMngPresetList.json',
            datatype: 'json',
            mtype: 'POST',
            height: 'auto',
            autowidth: true,
            rowNum: 10,
            page: parseInt($('#searchPage').val()),
            beforeRequest: function () {
                // validate check here!
            },
            postData: oMngPreset.getGridParams(),
            colNames: [
                '시설물아이디', '시설물명', 'X좌표', 'Y좌표', '지번', '도로명', '도로명(지번)', '시설물용도', '물품구분', '기능별유형', '방향각', '대역대', '설정개수', '설정여부', '설정'
            ],
            colModel: [
                {
                    name: 'fcltId',
                    hidden: true
                }, {
                    name: 'fcltLblNm',
                    align: 'left',
                    formatter: function (cellvalue, options, rowObject) {
                        var sFcltLblNm = rowObject.fcltLblNm;
                        return sFcltLblNm;
                    },
                    classes: 'text-ellipsis tooltip-preset',
                    cellattr: function () {
                        return 'style="width: 100px;"';
                    },
                    width: 115
                }, {
                    name: 'pointX',
                    hidden: true
                }, {
                    name: 'pointY',
                    hidden: true
                }, {
                    name: 'lotnoAdresNm',
                    hidden: true
                }, {
                    name: 'roadAdresNm',
                    hidden: true
                }, {
                    name: 'address',
                    align: 'left',
                    classes: 'text-ellipsis tooltip-preset',
                    formatter: function (cellvalue, options, rowObject) {
                        var sAddrRoad = rowObject.roadAdresNm ? rowObject.roadAdresNm : '-';
                        var sAddrJibun = rowObject.lotnoAdresNm ? rowObject.lotnoAdresNm : '-';
                        return sAddrRoad + '(' + sAddrJibun + ')';
                    },
                    cellattr: function () {
                        return 'style="width: 85px;"';
                    },
                    width: 100
                }, {
                    name: 'fcltUsedTyNm',
                    align: 'center',
                    cellattr: function () {
                        return 'style="width: 85px;"';
                    },
                    width: 90
                }, {
                    name: 'fcltGdsdtTyNm',
                    hidden: true
                }, {
                    name: 'fcltKndDtlNm',
                    align: 'center',
                    cellattr: function () {
                        return 'style="width: 65px;"';
                    },
                    width: 70
                }, {
                    name: 'cctvOsvtYn',
                    align: 'center',
                    hidden: true
                }, {
                    name: 'presetBdwStartNum',
                    hidden: true
                }, {
                    name: 'presetBdwStartCnt',
                    hidden: true
                }, {
                    name: 'presetYn',
                    align: 'center',
                    formatter: function (cellvalue, options, rowObject) {
                        var sPresetYn = '';
                        var sCctvOsvtYn = rowObject.cctvOsvtYn;
                        var sPresetBdwStartCnt = rowObject.presetBdwStartCnt;
                        var sFcltKndDtlCd = rowObject.fcltKndDtlCd;

                        if (sFcltKndDtlCd == 'FT' && sCctvOsvtYn == 'Y') {
                            sPresetYn = '<span class="text-success">설정</span>';
                        } else if (sFcltKndDtlCd == 'RT' && sPresetBdwStartCnt > 0) {
                            sPresetYn = '<span class="text-success">설정 (' + sPresetBdwStartCnt + ')';
                        } else {
                            sPresetYn = '<span class="text-danger">미설정</span>';
                        }
                        return sPresetYn;
                    },
                    cellattr: function () {
                        return 'style="width: 55px;"';
                    },
                    width: 60
                }, {
                    name: 'btnPreset',
                    align: 'center',
                    formatter: function (cellvalue, options, rowObject) {
                        var sFcltId = rowObject.fcltId;
                        var $button = $('<button/>', {
                            'class': 'btn btn-default btn-sm',
                            'style': 'height: 20px; padding: 0px 5px 0px 5px;',
                            'text': '설정',
                            'onclick': 'oMngPreset.preset("' + sFcltId + '")'
                        });

                        return $button.prop('outerHTML');
                    },
                    cellattr: function () {
                        return 'style="width: 55px;"';
                    },
                    width: 60
                }
            ],
            jsonReader: {
                root: "rows",
                total: "totalPages",
                records: function (obj) {
                    $('#rowCnt').text(obj.totalRows);
                    return obj.totalRows;
                }
            },
            cmTemplate: { sortable: false },
            loadComplete: function (data) {
                oCommon.jqGrid.loadComplete('preset', data, oMngPreset.getGridParams());
                $('td.tooltip-preset').tooltip({
                    container: 'body',
                    placement: 'right'
                });
                $('#searchPage').val(data.page);
                oCommon.jqGrid.gridComplete(this);
            },
            onSelectRow: function (row) {
                var oRowData = $('#grid-preset').getRowData(row);
                olSwipMap.point.set([
                    oRowData.pointX, oRowData.pointY
                ], 'EPSG:4326', 'FCLT', true, false, true);
            },
        });
    };

    this.reloadGrid = function () {
        oCommon.jqGrid.reload('preset', 1, oMngPreset.getGridParams());
    };

    this.getGridParams = function () {
        var sSearchFcltUsedTyCd = $('#searchFcltUsedTyCd option:selected').val();
        var sSearchFcltKndDtlCd = $('#searchFcltKndDtlCd option:selected').val();
        // var sSearchFcltGdsdtTy = $('#searchFcltGdsdtTy option:selected').val();
        var sSearchPresetYn = $('#searchPresetYn option:selected').val();
        var sSearchGbn = $('#searchGbn option:selected').val();
        var sSearchKeyword = $('#searchKeyword').val();

        var params = {
            searchFcltUsedTyCd: sSearchFcltUsedTyCd,
            searchFcltKndDtlCd: sSearchFcltKndDtlCd,
            // searchFcltGdsdtTy: sSearchFcltGdsdtTy,
            searchPresetYn: sSearchPresetYn,
            searchKeyword: sSearchKeyword,
            searchGbn: sSearchGbn
        };
        return params;
    };
};
