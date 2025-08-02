$(function () {
    oHeader = new header();
    oHeader.init();




	$('#menutitle').text($(".active").text());
	$('.breadcrumb').addClass('hide');
	
	let textlist = $(".active").text().split(">");
	$('title').text(textlist[textlist.length-1].trim());
	




});

function header() {
    this.init = function () {
        oHeader.quickMenu.sync();
        oHeader.syncDimensions();

        $('li.dropdown.menu-lvl-1').on('mouseover', function (event) {
            // $(this).addClass('open');
        });

        $('li.dropdown.menu-lvl-1').on('mouseout', function (event) {
            // $(this).removeClass('open');
        });

        $('li.dropdown.menu-lvl-1.menu-all').off('mouseover');
        $('li.dropdown.menu-lvl-1.menu-all').off('mouseout');

        $('.dropdown.menu-all>ul.dropdown-menu').on('click', function (event) {
            event.stopPropagation();
        });

        $('#dropdown-user-info>ul').on('click', function (event) {
            if ($(event.target).parent().hasClass('toggle-group')) {
                $('#toggle-login-dplctn-yn').bootstrapToggle('toggle');
            }
            event.stopPropagation();
        });

        $('#anchor-login-dplctn-yn').tooltip({ container: 'body' });
        $('#toggle-login-dplctn-yn').bootstrapToggle();
        $('#toggle-login-dplctn-yn').on('change', (e) => {
            let sLoginDplctnYn = $('#toggle-login-dplctn-yn').is(':checked') ? 'Y' : 'N';
            $.ajax({
                type: 'POST',
                url:contextRoot + '/mntr/updateLoginDplctn.json',
                dataType: 'json',
                data: {
                    loginDplctnYn: sLoginDplctnYn
                },
                success: function (data) {
                    console.log(data);
                },
                error: function (xhr, status, error) {
                    console.log(xhr, status, error);
                }
            });
        });

        $(window).resize(function () {
            oHeader.syncDimensions();
        });
    };

    this.syncDimensions = function () {
        var nWidthHeader = $('#main-nav').width();
        var nHeightHeader = $('#main-nav').height();
        //var nOffsetLeftMenuAll = oHeader.getOffsetLeft($('#menu-all').get(0));
        var nOffsetLeftMenuAll = '';

        $('.dropdown.menu-all>ul.dropdown-menu').css({
            'width': nWidthHeader + 1,
            'left': nOffsetLeftMenuAll * (-1),
            'max-height': 540,
            'overflow-y': 'auto'
        });

        $('aside#left').css('top', nHeightHeader);
        $('aside#right').css('top', nHeightHeader);
        $('section#body').css('top', nHeightHeader);
        $('#toggleLeft').css('top', nHeightHeader);
        $('#toggleRight').css('top', nHeightHeader);

        $('#wrapper.wth100').css('top', nHeightHeader + 5);
    };

    this.opener = function (url, title, width, height) {
        // url에서 사용자아이디 매핑
        var n = url.indexOf("http://");
        var menuUrl = "";
        // swip_com외 타시스템이용시 메뉴사용이력 저장시임시 url체크
        if (n >= 0) {
            var ix = url.indexOf("?");
            menuUrl = url.substring(ix);
        }
        // swip_com
        if (n < 0) {
            url = contextRoot + '/' + url;
        }

        if (width && height) {
            oCommon.window.open(url, title, 'menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no,location=no,width=' + width + ',height=' + height);
        } else {
            oCommon.window.open(url, title, 'menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no,location=no,width=' + oConfigure.popWidth + ',height=' + oConfigure.popHeight);
        }

        if (menuUrl.length > 0) {
            menuUrl = contextRoot + '/swip/cmn/otherSystemMenu.json' + menuUrl;
            //		alert("menuUrl=" + menuUrl);
            $.ajax({
                type: 'POST',
                url:contextRoot + menuUrl,
                dataType: 'json',
                data: {
                    //				menuPgmMenuId : sMenuPgmMenuId
                },
                success: function (data) {
                    console.log('otherSystemMenu success = ' + url);
                },
                error: function (xhr, status, error) {
                    console.log('otherSystemMenu failure.');
                }
            });
        }
    };
	/*
    this.getOffsetLeft = function (element) {
        var offsetLeft = 0;
        do {
            if (!isNaN(element.offsetLeft)) {
                offsetLeft += element.offsetLeft;
            }
        }
        while (element = element.offsetParent);
        return offsetLeft;
    };
	*/
    this.quickMenu = {
        toggle: function (element) {
            let oData = $(element).data();
            let sMenuPgmMenuId = oData.quickMenuPgmMenuId;
            $.ajax({
                type: 'POST',
                url:contextRoot + '/mntr/saveQuickMenu.json',
                dataType: 'json',
                data: {
                    menuPgmMenuId: sMenuPgmMenuId
                },
                success: function (data) {
                    if (data.result.session == 1) {
                        var elI = $(element).find('i');
                        var hasClassFas = $(elI).hasClass('fas');
                        if (hasClassFas) {
                            $(elI).removeClass('fas');
                            $(elI).addClass('far');
                        } else {
                            $(elI).removeClass('far');
                            $(elI).addClass('fas');
                        }
                        console.log('saveQuickMenu success.');

                        oHeader.quickMenu.sync();
                    } else {
                        console.log('saveQuickMenu failure. %s', data.result.msg);
                        oCommon.modalAlert('modal-notice', '알림', data.result.msg);
                    }
                },
                error: function (xhr, status, error) {
                    console.log('saveQuickMenu failure.');
                }
            });
        },

        sync: function (isHide) {
            let $quickMenu = $('nav#main-nav a.quick-menu > i.fas.fa-star');
            let $quickMenuPanel = $('#quick-menu-panel');
            if ($quickMenu.length) {
                let $quickMenuListGroup = $('#quick-menu-list-group');
                if ($quickMenuListGroup.length) {
                    $quickMenuListGroup.empty();
                    $quickMenuPanel.show();
                } else {
                    let oQuickMenuPosition = {
                        top: '',
                        left: '',
                        bottom: '100',
                        right: '100'
                    };
                    if (localStorage.getItem('QuickMenuPosition') != null) {
                        oQuickMenuPosition = JSON.parse(localStorage.getItem('QuickMenuPosition'));
                    }

                    let sStyle = '';
                    sStyle += oQuickMenuPosition.top == '' ? '' : 'top: ' + oQuickMenuPosition.top + 'px;';
                    sStyle += oQuickMenuPosition.left == '' ? '' : 'left: ' + oQuickMenuPosition.left + 'px;';
                    sStyle += oQuickMenuPosition.bottom == '' ? '' : 'bottom: ' + oQuickMenuPosition.bottom + 'px;';
                    sStyle += oQuickMenuPosition.right == '' ? '' : 'right: ' + oQuickMenuPosition.right + 'px;';

                    $quickMenuPanel = $('<div/>', {
                        'id': 'quick-menu-panel',
                        'class': 'panel panel-default',
                        'style': sStyle
                    });

                    let $quickMenuPanelHeading = $('<div/>', {
                        'class': 'panel-heading',
                        'text': '드래그(창)-이동, 클릭(별)-접고펴기'
                    });

                    $quickMenuPanelHeading.append($('<button/>', {
                        'type': 'button',
                        'class': 'close',
                        'data-target': '#quick-menu-list-group',
                        'data-toggle': 'collapse',
                        'aria-expanded': 'true',
                        'aria-controls': 'quick-menu-list-group',
                        'html': '<i class="fas fa-star"></i>',
                        'onclick': 'javascript:oHeader.quickMenu.position();'
                    }));

                    $quickMenuPanel.append($quickMenuPanelHeading);

                    $quickMenuListGroup = $('<ul/>', {
                        'id': 'quick-menu-list-group',
                        'class': 'list-group collapse'
                    });

                    if (oQuickMenuPosition.collapse == 'in') {
                        $quickMenuListGroup.addClass('in');
                    }

                    $quickMenuPanel.append($quickMenuListGroup);

                    $quickMenuPanel.appendTo('body');
                    $quickMenuPanel.draggable({
                        containment: '#wrapper',
                        stop: function (e) {
                            console.log(e);
                            oHeader.quickMenu.position();
                        }
                    });
                }

                let $anchors = $quickMenu.closest('a');
                if ($anchors.length) {
                    $.each($anchors, function (i, v) {
                        let oData = $(v).data();
                        let $anchor = $('<a/>', {
                            'title': oData.quickMenuPgmMenuNmKo
                        });

                        if (oData.quickMenuNewWdwYn == 'Y') {
                            $anchor.html(oData.quickMenuPgmMenuNmKo + ' <i class="fas fa-external-link-alt"></i>');
                            $anchor.attr('href', '#');
                            $anchor.attr('onclick', 'javascript:oHeader.opener("' + contextRoot + oData.quickMenuPgmUrl + '", "' + oData.quickMenuPgmMenuNmKo + '", "'
                                + oData.quickMenuNewWinWidth + '", "' + oData.quickMenuNewWinHeight + '");');
                        } else {
                            $anchor.text(oData.quickMenuPgmMenuNmKo);
                            $anchor.attr('href', '/' + contextRoot + oData.quickMenuPgmUrl);
                        }

                        $quickMenuListGroup.append($('<li/>', {
                            'class': 'list-group-item',
                            'html': $anchor
                        }));
                    });
                }
            } else {
                if ($quickMenuPanel.length) {
                    $quickMenuPanel.hide();
                }
            }
        },
        position: function () {
            setTimeout(function () {
                let $quickMenuPanel = $('#quick-menu-panel');
                let $quickMenuListGroup = $('#quick-menu-list-group');

                let oRect = $quickMenuPanel.get(0).getBoundingClientRect();
                let sCollapse = '';
                if ($quickMenuListGroup.hasClass('in')) {
                    sCollapse = 'in'
                }
                let oData = {
                    top: oRect.top,
                    right: oRect.right,
                    bottom: oRect.bottom,
                    left: oRect.left,
                    collapse: sCollapse
                };
                localStorage.setItem('QuickMenuPosition', JSON.stringify(oData));
            }, 500);
        }
    };
}
