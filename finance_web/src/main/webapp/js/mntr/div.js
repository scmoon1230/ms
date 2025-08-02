function div(divId, divLc, divLcSrlNo) {
    this.divId = divId;
    this.divLc = divLc;
    this.divLcSrlNo = divLcSrlNo;
}

function fn_div() {
    this.isHideDiv = false;
    this.articleRight = null;
    this.articleBottom = null
    this.articleLeft = null;
    this.articleRightOrigin = null;
    this.articleBottomOrigin = null;
    this.articleLeftOrigin = null;
    this.hideDiv = function () {
        collapse({
            left: true,
            right: true,
            bottom: true,
            force: true
        });
        $('#toggleLeft').hide();
        $('#toggleRight').hide();
        $('#toggleBottom').hide();
        oDiv.isHideDiv = true;
    };
    this.setDivHdnVal = function () {
        let sChkDivHdnLeft = $('#left').is(':visible') ? '1' : '0';
        let sChkDivHdnRight = $('#right').is(':visible') ? '1' : '0';
        let sChkDivHdnBottom = $('#bottom').is(':visible') ? '1' : '0';
        let sBin = sChkDivHdnLeft.concat(sChkDivHdnRight).concat(sChkDivHdnBottom);
        let nDec = parseInt(sBin, 2);

        $.ajax({
            type: 'POST',
            url:contextRoot + '/mntr/saveMngDisplay.json',
            dataType: 'json',
            data: {
                leftDivHdnYn: nDec
            },
            success: function (data) {
                if (data.session == 1) {
                    console.log('saveMngDisplay success.');
                    oConfigure.leftDivHdnYn = nDec;
                }
            },
            error: function (xhr, status, error) {
                console.log('saveMngDisplay failure.');
            }
        });
    };

    this.setDiv = function (position, index, data, callback) {
        var $cols = $('#article-' + position + ' div.col');
        $.ajax({
            type: 'POST',
            url:contextRoot + '/tvo/viewTargetDivision.do',
            data: data,
            async: true,
            success: function (data) {
                if ($('#' + position).length) {
                    const obj = {};
                    obj[position] = false;
                    collapse(obj);
                }

                if (index == 'evtMntr') {
                    var div = $('#article-' + position + ' div.colfix')[0];
                    $(div).append(data);

                    if (typeof evtOcrNo != 'undefined' && evtOcrNo != '') {
                        setTimeout(function () {
                            var $popDiv = $('table[id^="grid-evtMntr"]');
                            if ($popDiv.length) {
                                ePopDiv = $popDiv.get(0);
                                var oRowData = $(ePopDiv).getRowData();
                                var nCnt = oRowData.length;
                                if (nCnt) {
                                    for (var i = 0; i < nCnt; i++) {
                                        var oData = oRowData[i];
                                        if (oData.evtOcrNo == evtOcrNo) {
                                            $(ePopDiv).setSelection(i + 1);
                                        }
                                    }
                                }
                            }
                        }, 1000);
                    }
                } else if (!isNaN(index)) {
                    var eTarget = $cols.get(index);
                    $(eTarget).html(data);
                    if (typeof callback == 'function') {
                        callback();
                    }
                }
            },
            error: function (jqXHR, text, error) {
                console.log('error');
            }
        });
    };

    this.openDiv = function (id, data, callback, popWidth, popHeight) {
		console.log("=== popWidth X popHeight = "+popWidth+" X "+popHeight);
        $.ajax({
            type: 'POST',
            url:contextRoot + '/tvo/viewTargetDivision.do',
            data: data,
            async: true,
            success: function (data) {
				if ( popWidth == undefined ) popWidth = 500;		console.log("=== popWidth : "+popWidth);
				if ( popHeight == undefined ) popHeight = 300;		console.log("=== popHeight : "+popHeight);
				
                mLeft = (screen.availWidth - popWidth) / 2;
                mTop = (screen.availHeight - popHeight) / 2;
                const $dialog = $('<div/>', {
                    'class': 'modal',
                    'id': id,
                    'tabindex': '-1',
                    'role': 'dialog',
                    'style': 'display: block;width: ' + popWidth + 'px;margin-left: ' + mLeft + 'px;margin-top: ' + mTop + 'px',
                });
                $dialog.modal('show');
                //$dialog.draggable();      // 이동 가능, jquery ui
                $dialog.html(data);
                $('body').append($dialog);

                if (typeof callback == 'function') {
                    callback();
                }
            },
            error: function (jqXHR, text, error) {
                console.log('error');
            }
        });
    };

    const intervals = {};

    this.setInterval = function (id, interval) {
        if (typeof intervals[id] !== 'undefined') {
            console.log('-- clearInterval: %s', id);
            clearInterval(intervals[id]);
        }
        intervals[id] = interval;
        console.log('-- setInterval: %s', id);
    }

    this.clearInterval = function (id) {
        if (typeof intervals[id] !== 'undefined') {
            clearInterval(intervals[id]);
            intervals[id] = undefined;
            delete intervals[id];
        }
        console.log('-- clearInterval: %s', id);
    }

    this.clearAllInterval = function () {
        if (typeof oVmsCommon !== 'undefined') {
            if (oVmsCommon.enchainPlay != null) {
                clearTimeout(oVmsCommon.enchainPlay);
                oVmsCommon.enchainPlay = null;
                console.log('-- clearTimeout: enchainPlay');
            }
            if (oVmsCommon.cnOsvtPlaytimeStop != null) {
                clearTimeout(oVmsCommon.cnOsvtPlaytimeStop);
                oVmsCommon.cnOsvtPlaytimeStop = null;
                console.log('-- clearTimeout: cnOsvtPlaytimeStop');
            }
        }

        for (const [key, value] of Object.entries(intervals)) {
            clearInterval(value);
            intervals[key] = undefined;
            console.log('-- clearAllInterval: %s', key);
            delete intervals[key];
        }
        console.log('-- clearAllInterval =====');
    }
}

$(function () {
    oDiv = new fn_div();
    oMenu = $('#main-nav').data();

    $('div#wrapper>div[id^="toggle"]').mouseover(function () {
        let url = $(this).css('background-image');
        let res = url.replace('.png', '_hover.png');
        $(this).css('background-image', res);
        $(this).css('background-color', '#ff9300');
    });

    $('div#wrapper>div[id^="toggle"]').mouseout(function () {
        let url = $(this).css('background-image');
        let res = url.replace('_hover.png', '.png');
        $(this).css('background-image', res);
        $(this).css('background-color', '#ecf0f1');
    });

    $('div#wrapper>div[id^="toggle"]').on('click', function () {
        let sId = $(this).attr('id');
        if (sId == 'toggleLeft') {
            let isVisible = $('#left').is(':visible');
            collapse({
                left: isVisible
            });
        } else if (sId == 'toggleRight') {
            let isVisible = $('#right').is(':visible');
            collapse({
                right: isVisible
            });
        } else if (sId == 'toggleBottom') {
            let isVisible = $('#bottom').is(':visible');
            collapse({
                bottom: isVisible
            });
        }
        // oDiv.setDivHdnVal();
    });

    collapse({
        left: false,
        right: true,
        bottom: true
    });
});

/*
 * Left, Right, Bottom 영역 펴고 접기
 */
function collapse(obj, callback) {
    let isCollapsed = false;
    let isForce = obj.hasOwnProperty('force') ? obj.force : false;

    let oToogle = {
        up: contextRoot + '/images/mntr/layout/toggle_up.png',
        down: contextRoot + '/images/mntr/layout/toggle_down.png',
        left: contextRoot + '/images/mntr/layout/toggle_left.png',
        right: contextRoot + '/images/mntr/layout/toggle_right.png',
    }

    let nHeightFooter = $('footer').height() + 1;

    let oSize = {
        left: parseInt(oConfigure.mntrViewLeft),
        right: parseInt(oConfigure.mntrViewRight),
        toogleBar: 10,
        bottomHide: nHeightFooter,
        bottomShow: parseInt(oConfigure.mntrViewBottom)
    }

    if (obj.hasOwnProperty('left')) {
        let isChkDivHdnLeft = $('aside#left').is(':visible') ? true : false;
        let isHidden = obj.left;
        if ((isChkDivHdnLeft && isHidden) || isForce) {
            $('aside#left').hide();
            $('#toggleLeft').css({
                'left': 0,
                'background-image': 'url("' + oToogle.right + '")'
            });
            $('#body').css('left', oSize.toogleBar);
            isCollapsed = true;
        } else if ((!isChkDivHdnLeft && !isHidden) || isForce) {
            $('aside#left').show();
            $('#toggleLeft').css({
                'left': oSize.left,
                'background-image': 'url("' + oToogle.left + '")'
            });
            $('#body').css('left', oSize.left + oSize.toogleBar);
            $('aside#left').css('width', oSize.left);
            isCollapsed = true;
        } else {
            // Do nothing.
        }
    }

    if (obj.hasOwnProperty('right')) {
        let isChkDivHdnRight = $('aside#right').is(':visible') ? true : false;
        let isHidden = obj.right;
        if ((isChkDivHdnRight && isHidden) || isForce) {
            $('aside#right').hide();
            $('#toggleRight').css({
                'right': 0,
                'background-image': 'url("' + oToogle.left + '")'
            });
            $('#body').css('right', oSize.toogleBar);
            isCollapsed = true;
        } else if ((!isChkDivHdnRight && !isHidden) || isForce) {
            $('aside#right').show();
            $('#toggleRight').css({
                'right': oSize.right,
                'background-image': 'url("' + oToogle.right + '")'
            });
            $('#body').css('right', oSize.right + oSize.toogleBar);
            $('aside#right').css('width', oSize.right);
            isCollapsed = true;
        } else {
            // Do nothing.
        }
    }

    if (obj.hasOwnProperty('bottom')) {
        let isChkDivHdnBottom = $('section#bottom').is(':visible') ? true : false;
        let isHidden = obj.bottom;
        if ((isChkDivHdnBottom && isHidden) || isForce) {
            $('section#bottom').hide();
            $('#toggleBottom').css({
                'bottom': oSize.bottomHide,
                'background-image': 'url("' + oToogle.up + '")'
            });
            $('aside#left').css('bottom', oSize.bottomHide + oSize.toogleBar);
            $('aside#right').css('bottom', oSize.bottomHide + oSize.toogleBar);
            $('#body').css('bottom', oSize.bottomHide + oSize.toogleBar);
            $('#toggleLeft').css('bottom', oSize.bottomHide + oSize.toogleBar);
            $('#toggleRight').css('bottom', oSize.bottomHide + oSize.toogleBar);
            isCollapsed = true;
        } else if ((!isChkDivHdnBottom && !isHidden) || isForce) {
            $('section#bottom').show();
            $('#toggleBottom').css({
                'bottom': oSize.bottomShow + nHeightFooter,
                'background-image': 'url("' + oToogle.down + '")'
            });
            $('aside#left').css('bottom', oSize.bottomShow + oSize.toogleBar + nHeightFooter);
            $('aside#right').css('bottom', oSize.bottomShow + oSize.toogleBar + nHeightFooter);
            $('#body').css('bottom', oSize.bottomShow + oSize.toogleBar + nHeightFooter);
            $('section#bottom').css({
                'bottom': oSize.bottomHide,
                'height': oSize.bottomShow
            });
            $('#toggleLeft').css('bottom', oSize.bottomShow + oSize.toogleBar + nHeightFooter);
            $('#toggleRight').css('bottom', oSize.bottomShow + oSize.toogleBar + nHeightFooter);
            isCollapsed = true;
        } else {
            // Do nothing.
        }
    }

    // jqGrid Sync
    oCommon.jqGrid.resize();
    if (typeof callback == 'function') {
        callback();
    }

    if (isCollapsed) {
        let isUpdateSize = false;
        if (typeof olMap != 'undefined' && typeof olMap.map != 'undefined' && olMap.map != null) {
            olMap.map.updateSize();
            isUpdateSize = true;
        }
        console.log('-- Collapsed : %o, %s', obj, isUpdateSize);
    }
}

/* 이벤트 상황 DIV 배치 */
function doDivSituation(evtId, evtOcrNo, hdnBottomYn) {
    clearDiv();
    $.ajax({
        type: 'POST',
        url:contextRoot + '/mntr/divSituationPosition.json',
        async: false,
        data: {
            evtId: evtId
        },
        success: function (data) {
            let rows = data.rows;
            let oCollapse = {
                left: false,
                right: true,
                bottom: true
            }
            if (rows.length) {
                for (let i = 0; i < rows.length; i++) {
                    appendDiv(rows[i], evtOcrNo, '');
                    if (rows[i].divLc == 'B') {
                        oCollapse.bottom = false;
                    } else if (rows[i].divLc == 'R') {
                        oCollapse.right = false;
                    } else if (rows[i].divLc == 'L') {
                        oCollapse.left = false;
                    }
                }
                if (hdnBottomYn == 'Y') oCollapse.bottom = true;
                if (oConfigure.ptzCntrTy == 'NO') oCollapse.bottom = true;
                collapse(oCollapse);
            } else {
                doDivNormal('EVENT', evtId, evtOcrNo, hdnBottomYn);
            }
        },
        error: function () {
            oCommon.modalAlert('modal-notice', '알림', '이벤트 상황 DIV를 가져오지 못했습니다.');
        }
    });
}

/* 평시 DIV 배치 */
function doDivNormal(divTyCd, evtId, evtOcrNo, hdnBottomYn) {
    let cntB = 0;
    let cntR = 0;
    let cntL = 0;

    let oCollapse = {
        left: false,
        right: false,
        bottom: false
    }

    $.ajax({
        type: 'POST',
        url:contextRoot + '/mntr/divNormalPosition.json',
        async: false,
        data: {
            divTyCd: divTyCd,
            evtId: evtId
        },
        success: function (data) {
            clearDiv();
            $('#article-bottom div.col').removeAttr('style');
            let rows = data.rows;
            if (rows.length) {
                for (let i = 0; i < rows.length; i++) {
                    appendDiv(rows[i], evtOcrNo, '');
                    if (rows[i].divLc == 'B') {
                        cntB++;
                    } else if (rows[i].divLc == 'R') {
                        cntR++;
                    } else if (rows[i].divLc == 'L') {
                        cntL++;
                    }
                }
                if (!cntL) oCollapse.left = true;
                if (!cntR) oCollapse.right = true;
                if (!cntB) oCollapse.bottom = true;
            } else {
                if ($('#article-left div.colfix').length && $('#article-left div.colfix').is(':empty')) oCollapse.left = true;
                oCollapse.right = true;
                oCollapse.bottom = true;
            }

            if (hdnBottomYn === 'Y') oCollapse.bottom = true;
            if (oConfigure.ptzCntrTy === 'NO') oCollapse.bottom = true;
            if (!$('div.colfix').is(':empty')) oCollapse.left = false;

            collapse(oCollapse);
        },
        error: function () {
            oCommon.modalAlert('modal-notice', '알림', '이벤트기본화면 DIV를 가져오지 못했습니다.');
        }
    });
}

/* fixDiv DIV */
function doFixDiv(evtOcrNo) {
    $('#article-left div.colfix').empty();
    let isValid = true;
    if (evtMntrDivTy != '') {
        oDiv.setDiv('left', 'evtMntr', evtMntrDivTy);
    } else {
        isValid = false;
    }
    return isValid;
}

/* DIV 추가 */
function appendDiv(row, evtOcrNo, fcltId, callback) {
    let divId = row.divId;
    let divLc = row.divLc;
    let divLcSrlNo = row.divLcSrlNo;
    let position = '';
    if (divLc == 'B') {
        position = 'bottom';
    } else if (divLc == 'R') {
        position = 'right';
    } else if (divLc == 'L') {
        position = 'left';
    }

    if (typeof oVmsService != 'undefined' && divId == 'DIV-VMS' && (oVmsService.isInit || $('#web-rtc-view').length)) {
        // DIV-VMS가 생성되어 있을 경우. 재생성하지 않는다.
        if (typeof callback == 'function') {
            callback();
        }
    } else {
        if ('DIV-CASTNET' != divId) {
            $.ajax({
                type: 'POST',
                url:contextRoot + '/mntr/viewDivision.do',
                data: {
                    divId: divId,
                    evtOcrNo: evtOcrNo,
                    fcltId: fcltId
                },
                async: false,
                success: function (data) {
                    let eTarget = $('#article-' + position + ' div.col')[divLcSrlNo];
                    $(eTarget).html(data);
                    if (typeof callback == 'function' && divId == 'DIV-VMS') {
                        callback();
                    } else if (typeof callback == 'function') {
                        callback();
                    }
                },
                error: function () {
                    console.log('-- appendDiv error : %o', row);
                    oCommon.modalAlert('modal-notice', '알림', 'appendDiv를 가져오지 못했습니다.');
                }
            });
        }
    }
    // console.log('-- appendDiv(%o, %s, %s);', row, evtOcrNo, fcltId);
}

/* DIV 초기화 */
function clearDiv() {
    oVmsService.disconnectPlaylist();
    if (typeof oVmsService != 'undefined' && oVmsService.isInit) {
        clearBottomDivExcludeVms();
    } else {
        $('#article-bottom div.col').empty();
    }

    if (typeof olMap.layers.trace != 'undefined') {
        let olSource = olMap.layers.trace.getSource();
        olSource.clear();
    }

    $('#article-left div.col').empty();
    $('#article-right div.col').empty();
    $('#article-bottom div.col').removeAttr('style');

    oDiv.clearAllInterval();
}


function clearBottomDivExcludeVms() {
    let $cols = $('#article-bottom div.col');
    $.each($cols, function (i, v) {
        let result = $(v).find('div#view-vms');
        if (result.length == 0) {
            $(v).empty();
        }
    });
}
