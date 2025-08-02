(function () {
    if (typeof olMap != 'undefined') {

    }

    if (typeof olSwipMap != 'undefined') {
		/*
        if ('ULC' == oConfigure.ucpId) {
            // 독도버튼
            var $mapIndexCntainer = $('#map-index-container');
            if ($mapIndexCntainer.length) {
                var $button = $('<button/>', {
                    'type': 'button',
                    'id': 'btn-ol-map-index-dokdo',
                    'title': '독도 바로가기',
                    'style': 'left: 22px; font-size: 11px; padding: 0px;',
                    'onclick': 'javascript:olMap.setCenter([131.8624647,37.2429447]);',
                    'text': '독'
                });
                $mapIndexCntainer.append($button);
            }

            // 독도버튼
            olSwipMap.indexmap.collapse = function (element) {
                $('#map-index').toggle();
                var isVisible = $('#map-index').is(':visible');
                var $i = $(element).find('i');
                if (isVisible) {
                    $i.removeClass('fa-caret-square-right');
                    $i.addClass('fa-caret-square-right');
                    $('#btn-ol-map-index-dokdo').show();
                } else {
                    $i.removeClass('fa-caret-square-right');
                    $i.addClass('fa-caret-square-left');
                    $('#btn-ol-map-index-dokdo').hide();
                }
            }
        }
		*/
        if (!isNaN(oGis.zoomIndex) && !isNaN(oGis.zoomMin) && !isNaN(oGis.zoomMax)) {
            olMap.zoom.min = parseInt(oGis.zoomMin);
            olMap.zoom.max = parseInt(oGis.zoomMax);
            olSwipMap.indexmap.zoom = parseInt(oGis.zoomIndex);
        }
    }
})();
