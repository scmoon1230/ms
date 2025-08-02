<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default" id="div-lc" data-evt-ocr-no="${divEvent.evtOcrNo}">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>시간</th>
					<th>경도</th>
					<th>위도</th>
				</tr>
			</thead>
			<tbody id="tbody-lc"></tbody>
		</table>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-xs-8 text-left" style="min-height: 20px; margin-top: 0px; margin-bottom: 0px;">
				<form class="form-inline">
					<div class="form-group">
						<label for="sel-lc-refresh" class="control-label"> 갱신주기 </label>
						<select class="form-control input-sm" name="sel-lc-refresh" id="sel-lc-refresh">
							<option value="0" selected="selected">안함</option>
							<option value="5">5초</option>
							<option value="10">10초</option>
							<option value="20">20초</option>
							<option value="30">30초</option>
							<option value="40">40초</option>
							<option value="50">50초</option>
							<option value="60">60초</option>
						</select>
					</div>
				</form>
			</div>
			<div class="col-xs-4 text-right">
				<div class="checkbox text-right" style="margin: 0px 10px 0px 0px;">
					<label>
						<input type="checkbox" id="chk-lc-visibility" checked="checked"> 경로표출
					</label>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- addr popup helper -->
<div class="panel panel-default hidden" id="panel-lc-wrap" style="margin-bottom: 0px;">
	<div class="panel-body" style="padding: 5px;">
		<span class="label label-default">도로</span>
		<span id="label-road-addr" style="padding-left: 3px;"></span>
		<br>
		<span class="label label-default">지번</span>
		<span id="label-jibun-addr" style="padding-left: 3px;"></span>
	</div>
</div>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/moment.js' />"></script>
<script>
	$(function() {
		oDivLc = new divLc();
		oDivLc.init();
	});

	function divLc() {
		this.refreshLcLayer = null;
		this.init = function() {
			var $trNodata = $('<tr/>');
			$trNodata.append($('<td/>', {
				'class' : 'text-center',
				'colspan' : '4',
				'text' : '위치목록이 없습니다.'
			}));
			var oData = $('#div-lc').data();
			var sEvtOcrNo = oData.evtOcrNo;
			// 이벤트번호, 맵이 존재하면,
			if (typeof sEvtOcrNo != 'undefined' && sEvtOcrNo != '' && typeof _map != 'undefined' && _map != null) {
				olLcLayer = oMntrMap.getLayerByName('lcLayer');
				// 레이어가 없으면 생성
				if (olLcLayer == null) {
					olLcLayer = new OpenLayers.Layer.Vector('lcLayer', {
						projection : new OpenLayers.Projection(oGis.projection),
						strategies : [
								new OpenLayers.Strategy.Cluster({
									distance : 0
								}), new OpenLayers.Strategy.BBOX({
									resFactor : 1
								})
						],
						protocol : new OpenLayers.Protocol.HTTP({
							url : contextRoot + '/mntr/lcInfoGeoData.json',
							params : {
								evtOcrNo : sEvtOcrNo,
								rowNum : 20
							},
							readWithPOST : true,
							format : new OpenLayers.Format.GeoJSON()
						}),
						styleMap : new OpenLayers.StyleMap(oMntrMap.style.lcLayer)
					});
					olLcLayer.protocol.params.evtOcrNo = sEvtOcrNo;
					_map.addLayer(olLcLayer);

					olLcLayer.events.on({
						'loadend' : oDivLc.loadendLcLayer
					});
				}
				// 레이어가 있으면 갱신
				else {
					pointLayer.removeAllFeatures();
					olLcLayer.protocol.params.evtOcrNo = sEvtOcrNo;
					olLcLayer.refresh({
						force : true,
						params : {
							evtOcrNo : sEvtOcrNo
						}
					});
				}
				olLcLayer.setVisibility(true);
			}
			// 널값 입력
			else {
				var $tbodyLc = $('#tbody-lc');
				$tbodyLc.html($trNodata.clone());
			}

			$('#chk-lc-visibility').change(function(event) {
				if (typeof olLcLayer !== 'undefined' && olLcLayer !== null) {
					var isChecked = $(this).is(':checked');
					olLcLayer.setVisibility(isChecked);
					console.log('olLcLayer.setVisibility(%s)', isChecked);
				}
			});

			$('#sel-lc-refresh').change(function(event) {
				var isChecked = $('#chk-lc-visibility').is(':checked');
				var eSelected = $(this).find(':selected');
				var nSelected = Number($(eSelected).val());
				if (0 != nSelected && isChecked) {
					if (typeof olLcLayer !== 'undefined' && olLcLayer !== null) {
						if(oDivLc.refreshLcLayer != null) {
							clearTimeout(oDivLc.refreshLcLayer);
							oDivLc.refreshLcLayer = null;
						}

						oDivLc.refreshLcLayer = setInterval(function() {
							olLcLayer.redraw();
						}, nSelected * 1000);
						console.log('olLcLayer.redraw() setInterval: %d', nSelected);
					}
				}
				else {
					if (typeof olLcLayer !== 'undefined' && olLcLayer !== null) {
						if(oDivLc.refreshLcLayer != null) {
							clearTimeout(oDivLc.refreshLcLayer);
							oDivLc.refreshLcLayer = null;
							console.log('olLcLayer.redraw() setInterval: null');
						}
					}
				}
			});
		};

		this.loadendLcLayer = function() {
			var $trNodata = $('<tr/>');
			$trNodata.append($('<td/>', {
				'class' : 'text-center',
				'colspan' : '4',
				'text' : '위치목록이 없습니다.'
			}));

			if (olLcLayer.features.length) {
				// 경로목록
				var $tbodyLc = $('#tbody-lc');
				$tbodyLc.empty();
				pointLayer.removeAllFeatures();

				var oPoints = [];
				var olFeatures = olLcLayer.features;
				var nSize = olFeatures.length;
				var $tr = $('<tr/>');
				for (var i = 0; i < nSize; i++) {
					var oAttributes = olFeatures[i].attributes;
					var olGeometry = olFeatures[i].geometry;
					oPoints.push(new OpenLayers.Geometry.Point(olGeometry.x, olGeometry.y));
					var oWgs84 = convertToWGS84(olGeometry.x, olGeometry.y);
					var oMoment = moment(oAttributes.pointYmdHms, 'YYYYMMDDHHmmss');
					var sMonent = '';
					if (oMoment.isValid()) {
						sMonent = oMoment.format('HH:mm:ss');
					}

					var $trC = $tr.clone();
					$trC.attr('data-point-x', olGeometry.x);
					$trC.attr('data-point-y', olGeometry.y);
					$trC.append($('<td/>', {
						// 'text': oAttributes.traceId
						'text' : oAttributes.rowNum
					}));
					$trC.append($('<td/>', {
						'text' : sMonent
					}));
					$trC.append($('<td/>', {
						'text' : oWgs84.x.toFixed(6)
					}));

					$trC.append($('<td/>', {
						'text' : oWgs84.y.toFixed(6)
					}));

					$trC.appendTo($tbodyLc);
				}

				var olLineString = new OpenLayers.Geometry.LineString(oPoints);
				var oStyle = {
					strokeColor : '#ff66ff',
					strokeOpacity : 1,
					strokeWidth : 2,
					strokeDashstyle : 'solid',
					graphicZIndex : 30
				};

				var olLineFeature = new OpenLayers.Feature.Vector(olLineString, {
					type : 'line'
				}, oStyle);

				pointLayer.addFeatures([
					olLineFeature
				]);

				$('#tbody-lc tr').click(oDivLc.clickTr);
			}
			else {
				var $tbodyLc = $('#tbody-lc');
				$tbodyLc.html($trNodata.clone());
			}
		};

		this.clickTr = function() {
			var $tr = $(this);
			var oData = $tr.data();
			var olLonLat = new OpenLayers.LonLat(oData.pointX, oData.pointY);
			var oWgs84 = convertToWGS84(oData.pointX, oData.pointY);
			var $panelLcWrap = $('#panel-lc-wrap');

			$.ajax({
				type : 'POST',
				async : false,
				dataType : 'json',
				url : contextRoot + '/mntr/api/coordToAddr.json',
				data : {
					pointX : oWgs84.x,
					pointY : oWgs84.y
				},
				success : function(data) {
					if (data.result.addr) {
						var oAddr = data.result.addr;
						var sLgEmdNm = oAddr.lgEmdNm;
						var sLgLiNm = oAddr.lgLiNm == null ? ' ' : ' ' + oAddr.lgLiNm;
						var sJibunMainNo = oAddr.jibunMainNo;
						var sJibunSubNo = oAddr.jibunSubNo == 0 ? '' : '-' + oAddr.jibunSubNo;
						var sRoadNm = oAddr.roadNm;
						var sBuldMainNo = oAddr.buldMainNo;
						var sBuldSubNo = oAddr.buldSubNo == 0 ? '' : '-' + oAddr.buldSubNo
						var sBuldNm = oAddr.buldNm == null ? '' : oAddr.buldNm;

						var sRoad = '-';
						if (sRoadNm) {
							sRoad = sRoadNm + ' ' + sBuldMainNo + sBuldSubNo + ' ' + sBuldNm;
						}

						var sJibun = '-';
						if (sLgEmdNm) {
							sJibun = sLgEmdNm + sLgLiNm + sJibunMainNo + sJibunSubNo;
						}

						$('#label-road-addr').text(sRoad);
						$('#label-jibun-addr').text(sJibun);
					}
					else {
						$('#label-road-addr').text('-');
						$('#label-jibun-addr').text('-');
					}
					Gis.MapTools.openPopup(olLonLat, $panelLcWrap.clone().removeClass('hidden').prop('outerHTML'), null);
					_map.setCenter(olLonLat);
				},
				error : function(data, status, err) {
					console.log(data);
				}
			});
		};
	}
</script>