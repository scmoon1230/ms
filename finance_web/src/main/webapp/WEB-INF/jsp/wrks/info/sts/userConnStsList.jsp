<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />">
<style type="text/css">
.ui-jqgrid-btable tr[role=row]:nth-child(odd) {
	background: #eee;
}
</style>
</head>
<body>
	<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp"%>
	<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
	<div id="wrapper" class="wth100">
		<!-- container -->
		<div class="container">
			<!-- content -->
			<div class="contentWrap">
				<div class="content">
					<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp"%>
					<form id="listForm" name="listForm" method="post">
						<div class="tableTypeHalf seachT">
							<table>
								<caption>사용자접속 통계</caption>
								<colgroup>
									<col style="width: 150px;" />
									<col style="width: *;" />
									<col style="width: 100px" />
								</colgroup>
								<tbody>
									<tr>
										<th>검색구분</th>
										<td><label> <input type="radio" name="search-gbn" value="M" onchange="javascript:changeFormatDatePicker('YYYY');" /> 월별
											</label>
											<label> <input type="radio" name="search-gbn" value="D" onchange="javascript:changeFormatDatePicker('YYYY-MM');" checked="checked" /> 일별
											</label>
										</td>
										<th>날짜선택</th>
										<td><div class="form-group form-inline">
												<div class="input-group date" id="search-date-picker">
													<input type="text" class="form-control" id="input-date-picker" readonly="readonly" />
												</div>
												<img class="ui-datepicker-trigger" src="<c:out value='${pageContext.request.contextPath}' />/images/icon_calendar.png" onclick="toggleDatePicker();">
											</div>
										</td>
										<td><a class="btn btnRight btnS searchBtn">검색</a></td>
									</tr>
								</tbody>
							</table>
						</div>
					</form>

					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

					<div class="tableType1" style="height: 495px;">
						<table id="grid"></table>
					</div>
					<div class="paginate"></div>
					<div class="btnWrap btnR">
						<a href="javascript:;" class="btn btnDt btnExcel">엑셀저장</a>
					</div>
				</div>
				<!-- //content -->
			</div>
			<!-- //container -->
		</div>
	</div>
	<form name="excelDownFrm" method="post">
		<input type="hidden" name="searchUserType" id="dstrtCdBak" />
		<input type="hidden" name="searchYear" id="dstrtNmBak" />
		<input type="hidden" name="searchMonth" id="evtOcrYearBak" />
		<input type="hidden" name="titleKey" />
		<input type="hidden" name="titleHeader" />
	</form>
	<!--  footer -->
	<footer>
		<%@include file="/WEB-INF/jsp/mntr/layout/footer.jsp"%>
	</footer>
	<!-- //footer -->
	<script src="<c:url value='/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.dev.js' />"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.btnS').bind('click', function() {
				if ($.validate('.tableTypeHalf.seachT') == false) return;
				$('#grid').jqGrid('GridUnload');
				oGrid.init();
			});

			$('#input-date-picker').datetimepicker({
				ignoreReadonly : true,
				format : 'YYYY-MM',
				showTodayButton : true,
				maxDate: moment()
			});

			$('#input-date-picker').val(moment().format('YYYY-MM'));

			oGrid.init();
		});

		/** 그리드설정 */
		var oGrid = {
			init : function() {
				var sRowPerPageList = $('#rowPerPageList').val();
				var nRowPerPageList = parseInt(sRowPerPageList);
				var sSearchYear = '';
				var sSearchMonth = '';
				var sDatePicker = $('#input-date-picker').val();
				var sChecked = $('input[name=search-gbn]:checked').val();
				var sFormat = 'YYYY-MM';
				if (sChecked == 'M') sFormat = 'YYYY';
				var momentSearch = moment(sDatePicker, sFormat);
				sSearchYear = momentSearch.format('YYYY');
				if (sChecked == 'D') sSearchMonth = momentSearch.format('MM');
				var sTitleHeader = '사용자명';
				var sTitleKey = 'userNm';
				var nDaysInMonth = moment(sSearchYear + sSearchMonth, 'YYYYMM').daysInMonth();

				var oColNames = [
					'사용자명(아이디)'
				];

				var oColModel = [
					{
						name: 'userNm',
						index : 'USER_NM',
						align : 'center',
						width : 400,
						formatter : function(cellvalue, options, rowObject) {
							if (rowObject.rk == 1) {
								options.colModel.classes = 'bg-info';
							} else {
								options.colModel.classes = '';
							}
							return cellvalue;
						}
					}
				];

				if (sSearchMonth == '') {
					// 월별
					for (var i = 1; i <= 12; i++) {
						var sMonth = i < 10 ? '0' + i : i;
						oColNames.push(sMonth + '월');
						oColModel.push({
							name: 'm' + sMonth,
							index : 'M_' + sMonth,
							align : 'right',
							width : 100,
							formatter : function(cellvalue, options, rowObject) {
								if (rowObject.rk == 1) {
									options.colModel.classes = 'bg-info padding-right-5px';
								} else {
									options.colModel.classes = 'padding-right-5px';
								}
								return (cellvalue || 0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
							}
						});
						sTitleKey += '|m' + sMonth;
						sTitleHeader += '|' + sMonth + '월';
					}
					oColNames.push('합계');
					oColModel.push({
						name: 'mTt',
						index : 'M_TT',
						align : 'right',
						width : 65,
						classes : 'bg-info padding-right-5px',
						formatter : function(cellvalue, options, rowObject) {
							return (cellvalue || 0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
						}
					});
					sTitleKey += '|mTt';
					sTitleHeader += '|합계';
				} else {
					// 일별
					for (var i = 1; i <= nDaysInMonth; i++) {
						var sDay = i < 10 ? '0' + i : i;
						oColNames.push(sDay + '일');
						oColModel.push({
							name: 'day' + sDay,
							index : 'DAY_' + sDay,
							align : 'right',
							width : 45,
							formatter : function(cellvalue, options, rowObject) {
								if (rowObject.rk == 1) {
									options.colModel.classes = 'bg-info padding-right-5px';
								} else {
									options.colModel.classes = 'padding-right-5px';
								}
								return (cellvalue || 0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
							}
						});
						sTitleKey += '|day' + sDay;
						sTitleHeader += '|' + sDay + '일';
					}
					oColNames.push('합계');
					oColModel.push({
						name: 'dayTt',
						index : 'DAY_TT',
						align : 'right',
						width : 65,
						classes : 'bg-info padding-right-5px',
						formatter : function(cellvalue, options, rowObject) {
							return (cellvalue || 0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
						}
					});
					sTitleKey += '|dayTt';
					sTitleHeader += '|합계';
				}

				$('form[name=excelDownFrm] input[name=titleKey]').val(sTitleKey);
				$('form[name=excelDownFrm] input[name=titleHeader]').val(sTitleHeader);

				$('#grid').jqGrid({
					url: '<c:url value='/'/>wrks/info/sts/userConnStsList/getUserConnStatsData.json',
					datatype : 'json',
					postData: {
						searchYear : sSearchYear,
						searchMonth : sSearchMonth
					},
					colNames : oColNames,
					colModel : oColModel,
					rowNum : nRowPerPageList,
					height : 'auto',
					sortname: 'USER_NM',
					sortorder : 'ASC',
					viewrecords : true,
					scrollOffset : 0,
					loadonce : false,
					beforeRequest : function() {
						$.loading(true);
					},
					jsonReader : {
						root : function(obj) {
							return obj.rows;
						},
						page : function(obj) {
							return 1;
						},
						total : function(obj) {
							if (obj.rows.length > 0) {
								var page = obj.rows[0].rowcnt / nRowPerPageList;
								if ((obj.rows[0].rowcnt % nRowPerPageList) > 0) page++;
								return page;
							}
							else return 1;
						},
						records : function(obj) {
							return $.showCount(obj);
						}
					},
					cmTemplate: { sortable: false },
					beforeProcessing : function(data, status, xhr) {
						$.loading(false);
						if (typeof data.rows != "undefined" || data.rows != null) {
							$.makePager("#grid", data, $("#grid").getGridParam('page'), nRowPerPageList);
						}
					},
					loadComplete : function(data) {
						oCommon.jqGrid.resize();
						oCommon.jqGrid.checkNodata(undefined, data);
						oCommon.jqGrid.gridComplete(this);
					},
				})
			}
		};

		function excelDownAction() {
			if ($.validate('.tableType2.seachT') == false) return;

			var sSearchYear = '';
			var sSearchMonth = '';
			var sDatePicker = $('#input-date-picker').val();
			var sFormat = 'YYYY-MM';
			var sChecked = $('input[name=search-gbn]:checked').val();
			if (sChecked == 'M') sFormat = 'YYYY';
			var momentSearch = moment(sDatePicker, sFormat);
			sSearchYear = momentSearch.format('YYYY');
			if (sChecked == 'D') sSearchMonth = momentSearch.format('MM');

			document.excelDownFrm.searchYear.value = sSearchYear;
			document.excelDownFrm.searchMonth.value = sSearchMonth;
			document.excelDownFrm.action = '<c:url value='/'/>wrks/info/sts/userConnStsList/list.excel';
			document.excelDownFrm.submit();
		}

		function toggleDatePicker() {
			$('#input-date-picker').data('DateTimePicker').toggle();
		}

		function changeFormatDatePicker(format) {
			// Picker
			var $picker = $('#input-date-picker');
			// 일별
			var $inputD = $('input[name=search-gbn][value=D]');
			// 월별
			var $inputM = $('input[name=search-gbn][value=M]');

			var sDate = $picker.val();
			if (sDate.length == 7) {
				var momentDate = moment(sDate, 'YYYY-MM');
				if (momentDate.isValid()) {
					$inputD.data('date', sDate);
				}
			}
			else if (sDate.length == 4) {
				var momentDate = moment(sDate, 'YYYY');
				if (momentDate.isValid()) {
					$inputM.data('date', sDate);
				}
			}

			if (format == 'YYYY-MM') {
				var oData = $inputD.data();
				if (oData.hasOwnProperty('date')) {
					$('#input-date-picker').val(oData.date);
				}
				else {
					$('#input-date-picker').val(moment().format('YYYY-MM'));
				}
			}
			else if (format == 'YYYY') {
				var oData = $inputM.data();
				if (oData.hasOwnProperty('date')) {
					$('#input-date-picker').val(oData.date);
				}
				else {
					$('#input-date-picker').val(moment().format('YYYY'));
				}
			}

			$('#input-date-picker').data('DateTimePicker').destroy();
			$('#input-date-picker').datetimepicker({
				ignoreReadonly : true,
				format : format,
				showTodayButton : true,
				maxDate: moment()
			});
		}
	</script>

</body>
</html>
