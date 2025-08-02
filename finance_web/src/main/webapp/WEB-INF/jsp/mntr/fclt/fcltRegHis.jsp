<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
<article id="article-grid">

				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
	
	<div style="margin-top: 20px; margin-right: 50px; margin-left: 50px; vertical-align: middle;">
		<div class="searchBox sm" style="margin-bottom: 30px; border-radius: 5px;"; text-align:center;">
			<div class="form-inline">
				<div class="form-group" style="margin-right: 10px">
					<select id="searchCondition" name="searchCondition" class="form-control input-sm">
						<option value="">::: 전체 :::</option>
						<option value="ID">아이디</option>
						<option value="NM">이름</option>
					</select>
				</div>
				<div class="form-group" style="margin-right: 10px">
					<input id="searchKeyword" name="searchKeyword" maxlength="30" class="form-control input-sm" style="width: 200px;" />
				</div>

				<!--
				<div class="form-group">
					<div class="input-group date form_datetime">
						<input class="form-control input-sm" type="text" id="searchTermStart" name="searchTermStart" style="width: 200px;" />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-th"></span>
						</span>
					</div>
				</div>

				<div class="form-group">
					<p class="form-control-static">~</p>
				</div>

				<div class="form-group" style="margin-right: 10px">
					<div class="input-group date form_datetime">
						<input class="form-control input" type="text" id="searchTermEnd" name="searchTermEnd" style="width: 200px;" />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-th"></span>
						</span>
					</div>
				</div>
				-->

				<div class="form-group form-inline">
					<div class="input-group date" id="search-picker-from">
						<input type="text" class="form-control search-input" id="searchTermStart" readonly="readonly" />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar" title="시작일시"></span>
						</span>
					</div>
					<span> ~ </span>
					<div class="input-group date" id="search-picker-to">
						<input type="text" class="form-control search-input" id="searchTermEnd" readonly="readonly" />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar" title="종료일시"></span>
						</span>
					</div>
				</div>
				<button id="sendBtn" onclick="oFcltRegHis.reloadGrid();" class="btn btn-default">조회</button>
			</div>
		</div>
		<div class="grid-header">
			<div class="col-xs-9">
				<div class="tb_title">조회결과</div>
			</div>
			<div class="col-xs-3 text-right">
				<label>
					<span id="rowCnt" class="totalNum"></span>
					건
				</label>
			</div>
		</div>
		<table id="grid-his" style="width: 100%"></table>
		<div id="paginate-his" class="paginate text-center"></div>
	</div>
</article>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/moment.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/ko.js' />"></script>
<script src="<c:url value='/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />"></script>
<script>
	$(function() {
		oFcltRegHis = new fcltRegHis();
		oFcltRegHis.init();
	});

	function fcltRegHis() {
		this.init = function() {
			collapse({
				left : true,
				right : true,
				bottom : true
			});

			var oMoment = moment();
			var oMomentFrom = oMoment.clone().add(-7, 'days');
			var oMomentTo = oMoment.clone();
			var sFrom = oMomentFrom.format('YYYY/MM/DD');
			var sTo = oMomentTo.format('YYYY/MM/DD');

			$('#search-picker-from').datetimepicker({
				locale : 'ko',
				ignoreReadonly : true,
				format : 'YYYY/MM/DD',
				maxDate : oMomentTo
			});

			$('#search-picker-to').datetimepicker({
				locale : 'ko',
				ignoreReadonly : true,
				format : 'YYYY/MM/DD',
				maxDate : oMomentTo
			});

			$('#searchTermStart').val(sFrom);
			$('#searchTermEnd').val(sTo);

			$('#searchKeyword').keypress(function(event) {
				if (event.which == 13) {
					oFcltRegHis.reloadGrid();
				}
			});

			oFcltRegHis.grid();
		};

		this.grid = function() {
			$('#grid-his').jqGrid({
				url : contextRoot + '/mntr/fcltRegHisList.json',
				datatype : 'json',
				mtype : 'POST',
				height : 'auto',
				autowidth : true,
				postData: {
					searchTermStart : $("#searchTermStart").val(),
					searchTermEnd : $("#searchTermEnd").val()
				},
				rowNum : 15,
				emptyrecords : "데이터가 없습니다.",
				colNames: [
						'업로드일시', '등록자아이디', '등록자명', '전체건수', '등록건수', '수정건수', '업로드항목', '일련번호'
				],
				colModel: [
						{
							name: 'rgsDate',
							index : 'RGS_DATE',
							align : 'center',
							cellattr : function() {
								return 'style="width: 30px;"'
							},
							width : 30
						}, {
							name: 'rgsUserId',
							index : 'RGS_USER_ID',
							align : 'center',
							cellattr : function() {
								return 'style="width: 25px;"'
							},
							width : 25
						}, {
							name: 'userNm',
							index : 'USER_NM',
							align : 'center',
							cellattr : function() {
								return 'style="width: 25px;"'
							},
							width : 25
						}, {
							name: 'allCnt',
							index : 'ALL_CNT',
							align : 'center',
							cellattr : function() {
								return 'style="width: 30px;"'
							},
							width : 30
						}, {
							name: 'regCnt',
							index : 'REG_CNT',
							align : 'center',
							cellattr : function() {
								return 'style="width: 30px;"'
							},
							width : 30
						}, {
							name: 'updCnt',
							index : 'UPD_CNT',
							align : 'center',
							cellattr : function() {
								return 'style="width: 30px;"'
							},
							width : 30
						}, {
							name: 'uplCol',
							index : 'UPL_COL',
							align : 'left',
						}, {
							name: 'srlNo',
							index : 'SRL_NO',
							hidden : true
						}
				],
				jsonReader : {
					root : "rows",
					total : "totalPages",
					records : function(obj) {
						$('#rowCnt').text(obj.totalRows);
						return obj.totalRows;
					}
				},
				loadComplete : function(data) {
					// common.js 적용
					oCommon.jqGrid.loadComplete('his', data, oFcltRegHis.getGridParams());
				},
				cmTemplate : {
					sortable : false
				}
			});

			$(".ui-jqgrid-sortable").css("cursor", "default");
		};

		this.getGridParams = function() {
			var params = {
				searchCondition : $("#searchCondition").val(),
				searchKeyword : $("#searchKeyword").val(),
				searchTermStart : $("#searchTermStart").val(),
				searchTermEnd : $("#searchTermEnd").val()
			};
			return params;
		}

		this.reloadGrid = function() {
			if ($("#searchTermStart").val() > $("#searchTermEnd").val()) {
				alert('시작일이 종료일보다 큽니다.');
				return false;
			}
			gridReload('his', 1, oFcltRegHis.getGridParams());
		}
	}
</script>

