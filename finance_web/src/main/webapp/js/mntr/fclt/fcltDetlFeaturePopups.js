	var fpCctvDetailControl = new OpenLayers.Control.FeaturePopups({
		boxSelectionOptions : {},
		popupOptions : {
			hover : {
				followCursor : false
			},
			hoverList : {
				followCursor : false
			},
			list: {
				panMapIfOutOfView: false
			},
			listItem: {
				panMapIfOutOfView: false
			}
		},
		layers : [
			[
			cctvInfoLayer, {
				templates : {
					single : function(feature) {
						var fcltLblNm = feature.attributes.cctvNm;
						var mngSn = feature.attributes.mngSn;
						if(mngSn) fcltLblNm = '<strong style="color: #00f;">[' + mngSn + ']</strong>' + fcltLblNm;
						var fcltId = feature.attributes.cctvId;
						var pointX = feature.attributes.pointX;
						var pointY = feature.attributes.pointY;
						var w84f = convertByWGS84(pointX, pointY);

						if(pageGbn == 'castNetMntrgList'){  //투망감시관리-리스트
							doCctvPopUp(fcltId , fcltLblNm);
						} else if(pageGbn == 'castNetMntrgRgs'){ //투망감시관리-등록
							selectCctv(fcltId);
						} else if(pageGbn == 'presetMntrgList'){ //프리셋관리 - 리스트
							selectCctvPreset(fcltId, w84f.x, w84f.y);
						} else if(pageGbn == 'presetMntrg'){ //프리셋관리 - 리스트
							selectCctvPreset(fcltId, w84f.x, w84f.y);
						} else if(pageGbn == 'mntrgAreaRgs'){ //순환감시관리 - 등록
							selectAreaReg(fcltId, w84f.x, w84f.y);
						} else if(pageGbn == 'srchAddr'){ //통합검색-공통
							selectCctvLcInqierPopup(fcltId);
						} else {

						}

					},
					list : function(layer) {
						try {
							var tr = layer.html;

							if( $(tr).size()  > 1) {
								var fcltId = $(tr).first().prop('id');
								var feature =  cctvInfoLayer.getFeaturesByAttribute('cctvId', fcltId)[0];
								var geometry = feature.geometry;
								var lonLat = geometry.getBounds().getCenterLonLat();
								var table = '<caption>총 <b style="color: red;">' + layer.count + '</b>개의 시설물이 있습니다.</caption>'
								+ '<table class="table table-striped table-condensed"><thead><tr class="warning"><th>[관리번호]시설물명</th><th>종류</th><th>상태</th></tr></thead><tbody>'
								+ layer.html
								+ '</tbody></table>';
								return table;
							}
						}
						catch(e) {

						}
					},
					item : function(feature) {
						var fcltLblNm = feature.attributes.cctvNm;
						var mngSn = feature.attributes.mngSn;
						if(mngSn) fcltLblNm = '<strong style="color: #00f;">[' + mngSn + ']</strong>' + fcltLblNm;
						var sttus = feature.attributes.fcltSttusNm;
						var fcltKndNm = feature.attributes.cctvKndNm;
						var fcltId = feature.attributes.cctvId;
						var pointX = feature.attributes.pointX;
						var pointY = feature.attributes.pointY;
						var w84f = convertByWGS84(pointX, pointY);

						var span = '';
						if(sttus == '정상') {
							span = '<span style="color: green;">' + sttus + '</span>';
						}
						else if(sttus == '미확인') {
							span = '<span style="color: orange;">' + sttus + '</span>';
						}
						else {
							span = '<span style="color: red;">' + sttus + '</span>';
						}

						var tr = '';
						if(pageGbn == 'castNetMntrgList'){  //투망감시관리-리스트
							tr = '<tr id="' + fcltId + '"><th scope="row"><a href="javascript:doCctvPopUp(\''+ fcltId + '\', \''+ fcltLblNm + '\')">' + fcltLblNm + '</a></th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						} else if(pageGbn == 'castNetMntrgRgs'){ //투망감시관리-등록
							tr = '<tr id="' + fcltId + '"><th scope="row"><a href="javascript:selectCctv(\''+ fcltId + '\')">' + fcltLblNm + '</a></th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						} else if(pageGbn == 'presetMntrgList'){ //프리셋관리 - 리스트
							tr = '<tr id="' + fcltId + '"><th scope="row"><a href="javascript:selectCctvPreset(\''+ fcltId + '\', \''+ w84f.x + '\', \''+ w84f.y + '\')">' + fcltLblNm + '</a></th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						} else if(pageGbn == 'presetMntrg'){ //프리셋관리 - 등록
							tr = '<tr id="' + fcltId + '"><th scope="row"><a href="javascript:selectCctv(\''+ fcltId + '\', \''+ w84f.x + '\', \''+ w84f.y + '\')">' + fcltLblNm + '</a></th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						} else if(pageGbn == 'mntrgAreaRgs'){ //순환감시관리 - 등록
							tr = '<tr id="' + fcltId + '"><th scope="row"><a href="javascript:selectAreaReg(\''+ fcltId + '\', \''+ w84f.x + '\', \''+ w84f.y + '\')">' + fcltLblNm + '</a></th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						} else if(pageGbn == 'srchAddr'){ //통합검색-공통
							tr = '<tr id="' + fcltId + '"><th scope="row"><a href="javascript:selectCctvLcInqierPopup(\''+ fcltId + '\')">' + fcltLblNm + '</a></th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						} else {
							tr = '<tr id="' + fcltId + '"><th scope="row">' + fcltLblNm + '</th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						}

						return tr;
					},
					hover : function(feature) {
						var fcltLblNm = feature.attributes.cctvNm;
						var mngSn = feature.attributes.mngSn;
						if(mngSn) fcltLblNm = '<strong style="color: #00f;">[' + mngSn + ']</strong>' + fcltLblNm;
						var fcltKndNm = feature.attributes.cctvKndNm;
						var sttus = feature.attributes.fcltSttusNm;

						var span = '';
						if(sttus == '정상') {
							span = '<span style="color: green;">' + sttus + '</span>';
						}
						else if(sttus == '미확인') {
							span = '<span style="color: orange;">' + sttus + '</span>';
						}
						else {
							span = '<span style="color: red;">' + sttus + '</span>';
						}

						var tr = '<tr><th scope="row">' + fcltLblNm + '</th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						var table = '<table class="table table-striped table-condensed"><thead><tr class="warning"><th>[관리번호]시설물명</th><th>종류</th><th>상태</th></tr></thead><tbody>'
							+ tr
							+ '</tbody></table>';
						return table;
					},
					hoverList : function(layer) {
						var table = '<caption>총 <b style="color: red;">' + layer.count + '</b>개의 시설물이 있습니다.</caption>'
							+ '<table class="table table-striped table-condensed"><thead><tr class="warning"><th>[관리번호]시설물명</th><th>종류</th><th>상태</th></tr></thead><tbody>'
							+ layer.html
							+ '</tbody></table>';
						return table;
					},
					hoverItem : function(feature) {
						var fcltLblNm = feature.attributes.cctvNm;
						var mngSn = feature.attributes.mngSn;
						if(mngSn) fcltLblNm = '<strong style="color: #00f;">[' + mngSn + ']</strong>' + fcltLblNm;
						var sttus = feature.attributes.fcltSttusNm;
						var fcltKndNm = feature.attributes.cctvKndNm;

						var span = '';
						if(sttus == '정상') {
							span = '<span style="color: green;">' + sttus + '</span>';
						}
						else if(sttus == '미확인') {
							span = '<span style="color: orange;">' + sttus + '</span>';
						}
						else {
							span = '<span style="color: red;">' + sttus + '</span>';
						}

						var tr = '<tr><th scope="row">' + fcltLblNm + '</th><td>' + fcltKndNm + '</td><td>' + span + '</td></tr>';
						return tr;
					}
				}
			}
			]
		]
	});

	//cctv레이어 클러스터
	_map.addControl(fpCctvDetailControl);
	fpCctvDetailControl.activate();
