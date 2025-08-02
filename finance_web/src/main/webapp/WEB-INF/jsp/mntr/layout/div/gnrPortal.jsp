<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp" id="div-portal">
	<div class="panel-heading">
		<h3 class="panel-title">${divData.divTitle}</h3>
	</div>
	<div class="panel-body">
		<div id="searchBar" class="row">
			<div class="searchBox">
				<div class="form-inline">
					<div class="col-xs-10 text-left">
						<div class="form-group" style="width: 100%;">
							<input id="searchKeyword" class="form-control input-sm" style="width: 100%;" placeholder="검색할 키워드를 입력하세요." />
						</div>
					</div>
					<div class="col-xs-2 text-right">
						<div class="form-group">
							<button class="btn btn-primary btn-sm btn-ucp" onclick="oGnrPortal.search();">검색</button>
						</div>
					</div>
				</div>
			</div>

			<table id="grid-portal"></table>

			<div id="paginate-portal" class="paginate text-center"></div>
		</div>
	</div>
</div>
<script>
	$(function() {
		oGnrPortal = new gnrPortal();
		oGnrPortal.init();
	});

	function gnrPortal() {
		this.init = function() {
			/* 이벤트 목록 */
			$('#grid-portal').jqGrid({
				url: contextRoot + '/mntr/api/keywordToCoord.json',
				datatype : 'json',
				mtype : 'POST',
				height : 'auto',
				width : parseInt(oConfigure.mntrViewRight) - 50,
				autowidth : false,
				rowNum : 5,
				postData: {
					keyword: $('#div-portal #searchKeyword').val()
				},
				beforeRequest : function() {
					var sSearchKeyword = $('#div-portal #searchKeyword').val();
					if ('' == sSearchKeyword) {
						setGridNodata('portal', '키워드를 입력하세요.');
						return false;
					}

					var oSplit = sSearchKeyword.trim().split(',');
					if (oSplit.length == 2) {
						var sLat = oSplit[0];
						var sLon = oSplit[1];
						if (!isNaN(sLon) && !isNaN(sLat)) {
							var nLon = parseFloat(sLon);
							var nLat = parseFloat(sLat);
							if (nLon > 124 && nLon < 131 && nLat > 34 && nLat < 39) {
								vectorLayer.removeAllFeatures();
								var oPoint = convertByWGS84(nLon, nLat);
								var oFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(oPoint.x, oPoint.y), {
									"cls": "pt2"
								}, {
									externalGraphic: contextRoot + "/images/gis/downArrowBlue.png",
									graphicWidth: 40,
									graphicHeight: 40,
									graphicXOffset: -20,
									graphicYOffset: -40
								});
								vectorLayer.addFeatures([ oFeature ]);
								_map.setCenter(new OpenLayers.LonLat(oFeature.geometry.x, oFeature.geometry.y), _map.getNumZoomLevels() - 1);
								return false;
							}
						}
					}
				},
				loadComplete : function(data) {
					oGnrPortal.searchPagenation(data);
				},
				colNames: [ '구분', '주소', 'road', 'jibun', '명칭', 'pointX', 'pointY', ],
				colModel: [ {
					name: 'gubn',
					align: 'center',
					width: 15,
					cellattr: function() {
						return 'style="width:15%;"'
					}
				}, {
					name: 'addr',
					formatter: function(cellvalue, options, rowObject) {

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
						$div.append($('<br/>'));
						$div.append($spanRoad);
						$div.append(road == '' ? '' : road);
						return $div[0].innerHTML;
					},
					width: 45,
					cellattr: function() {
						return 'style="width:45%;"'
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
					width: 40,
					cellattr: function() {
						return 'style="width:40%;"'
					}
				}, {
					name: 'pointX',
					hidden: true
				}, {
					name: 'pointY',
					hidden: true
				}, ],
				jsonReader: {
					root: 'result.addr',
					page: 1,
					total: function(data) {
						var nRecords = typeof data.result.addr !== 'undefined' ? data.result.addr.length : 0;
						var nRowNum = $("#grid-hot").getGridParam('rowNum');
						var nTotal = Math.ceil(nRecords / nRowNum);
						return nTotal;
					},
					records: function(data) {
						$('#hotRowCnt').text(data.result.msg);
						return typeof data.result.addr !== 'undefined' ? data.result.addr.length : 0;
					}
				},
				onSelectRow : function(rowId) {
					var rowData = $('#grid-portal').getRowData(rowId);

					if (typeof rowData.pointX != 'undefined' && typeof rowData.pointY != 'undefined') {
						var point = null;
						if ('CCTV' === rowData.gubn) {
							point = convertByWGS84(rowData.pointX, rowData.pointY);
							previousFeatureselected = featureselected(point, 'fclt', '', false, true, true);
						}
						else {
							point = convertByWGS84(rowData.pointX, rowData.pointY);
							previousFeatureselected = featureselected(point, 'point', '', false, true, true);
						}
					}
					else {
						alert('위치 정보가 없는 데이터 입니다.');
						$('#grid-portal tr.ui-widget-content.jqgrow.ui-row-ltr.ui-state-highlight').removeClass('ui-state-highlight');
					}
				},
				cmTemplate : {
					sortable : false
				},
				loadonce: true
			});

			$('#div-portal #searchKeyword').keypress(function(event) {
				if (event.which == 13) {
					oGnrPortal.search();
				}
			});
		};

		this.searchPagenation = function(data) {
			var gridAlias = 'portal';
			var nPage = 1, nTotal = 1, nRecords = 0;
			if (data.page && data.total && data.records) {
				nPage = data.page;
				nTotal = data.total;
				nRecords = data.records;
			}
			else if (data.result && data.result.addr) {
				nRecords = data.result.addr.length;
				nRowNum = $("#grid-" + gridAlias).getGridParam('rowNum');
				nTotal = Math.ceil(nRecords / nRowNum);
				$('.jqgrow-nodata').remove();
			}
			else {
				setGridNodata(gridAlias, '주소 데이터가 없습니다.');
				return false;
			}

			var $paginate = $('#paginate-' + gridAlias);
			$paginate.empty();
			$paginate.html($('<ul/>', {
				id: 'pagination-' + gridAlias,
				class: 'paging'
			}));

			$pagination = $('#pagination-' + gridAlias);
			$pagination.twbsPagination({
				startPage: nPage,
				totalPages: nTotal,
				visiblePages: 4,
				onPageClick: function(event, page) {
					$('#grid-' + gridAlias).setGridParam({
						page: page
					}).trigger('reloadGrid');
				},
				first : '<i class="fas fa-angle-double-left"></i>',
				prev : '<i class="fas fa-angle-left"></i>',
				next : '<i class="fas fa-angle-right"></i>',
				last : '<i class="fas fa-angle-double-right"></i>'
			});
		};

		this.search = function() {
			$('#grid-portal').jqGrid('GridUnload');
			oGnrPortal.init();
		};
	}
</script>