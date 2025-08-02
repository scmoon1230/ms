<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
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
<script src="<c:url value='/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.dev.js' />"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.btnS').bind('click', function() {
			if ($.validate('.tableType2.seachT') == false) return;
			$('#grid').jqGrid('GridUnload');
			oGrid.init();
		});

		$('#input-date-picker').datetimepicker({
			showTodayButton : true,
			ignoreReadonly : true,
			format : 'YYYY-MM',
			maxDate: moment()
		});

		$('#input-date-picker').val(moment().format('YYYY-MM'));

		oGrid.init();
		
		$(".tableType1").css('height', window.innerHeight - 350);
		$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
		
	});

	var oGrid = {
		init : function() {
			//var sSearchUserNm =  $('#searchUserNm').val();
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

			var nDaysInMonth = moment(sSearchYear + sSearchMonth, 'YYYYMM').daysInMonth();
			var oColNames = [
					'사용자', '아이디', '그룹명'
			];
			var oColModel = [
					{	name: 'userNm', index : 'USER_NM', align : 'left', 	width : 300
						, formatter : function(cellvalue, options, rowObject) {
							if (rowObject.rk == 1) {	options.colModel.classes = 'bg-info';
							} else {					options.colModel.classes = '';
							}
							return cellvalue;
						}
					}, { name: 'userId', index : 'USER_ID', hidden : true
					}, { name: 'grpNm' , index : 'GRP_NM' , hidden : true
					}
				];

			var sTitleHeader = '사용자|아이디';
			var sTitleKey = 'userNm|userId';

			if (sSearchMonth == '') {
				// 월별
				for (var i = 1; i <= 12; i++) {
					var sMonth = i < 10 ? '0' + i : i;
					oColNames.push(sMonth + '월');
					oColModel.push({
						name: 'm' + sMonth,
						index : 'M_' + sMonth,
						align : 'center',
						width : 100,
						formatter : function(cellvalue, options, rowObject) {
							if (rowObject.rk == 1) {
								options.colModel.classes = 'bg-info';
							} else {
								options.colModel.classes = '';
							}
							var r = 0;
							if (cellvalue != null) {
								r = (cellvalue || 0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
							}
							return r;
						}
					});
					sTitleKey += '|m' + sMonth;
					sTitleHeader += '|' + sMonth + '월';
				}
				oColNames.push('합계');
				oColModel.push({
					name: 'mTt',
					index : 'M_TT',
					align : 'center',
					width : 65,
					classes : 'bg-info',
					formatter : function(cellvalue, options, rowObject) {
						var r = 0;
						if (cellvalue != null) {
							r = (cellvalue || 0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
						}
						return r;
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
						align : 'center',
						width : 45,

						formatter : function(cellvalue, options, rowObject) {
							if (rowObject.rk == 1) {
								options.colModel.classes = 'bg-info';
							} else {
								options.colModel.classes = '';
							}
							var r = 0;
							if (cellvalue != null) {
								r = (cellvalue || 0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
							}
							return r;
						}
					});
					sTitleKey += '|day' + sDay;
					sTitleHeader += '|' + sDay + '일';
				}

				oColNames.push('합계');
				oColModel.push({
					name: 'dayTt',
					index : 'DAY_TT',
					align : 'center',
					width : 65,
					classes : 'bg-info',
					formatter : function(cellvalue, options, rowObject) {
						var r = 0;
						if (cellvalue != null) {
							r = (cellvalue || 0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
						}
						return r;
					}
				});
				sTitleKey += '|dayTt';
				sTitleHeader += '|합계';
			}



			$('form[name=excelDownFrm] input[name=titleKey]').val(sTitleKey);
			$('form[name=excelDownFrm] input[name=titleHeader]').val(sTitleHeader);

			$.jqGrid($('#grid'), {
				url : "<c:url value='/wrks/evtctl/stats/cctvConnCombined/list.json'/>",
				datatype : 'json',
				postData: {
					//searchUserNm : sSearchUserNm,
					searchYear : sSearchYear,
					searchMonth : sSearchMonth
				},
				colNames : oColNames,
				colModel : oColModel,
				pager: '#pager',
				rowNum : nRowPerPageList,
				height : 'auto',
				scrollOffset : 0,
				autowidth : true,
				loadonce : false,
				sortname: 'USER_ID',
				sortorder : 'ASC',
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
						} else return 1;
					},
					records : function(obj) {
						return $.showCount(obj);
					}
				},
				beforeRequest : function() {
					$.loading(true);
				},
				beforeProcessing : function(data, status, xhr) {
					$.loading(false);
					if (typeof data.rows != 'undefine' || data.row != null) {
						$.makePager('#grid', data, $('#grid').getGridParam('page'), nRowPerPageList);
					}
				},
				cmTemplate: { sortable: false },
				loadComplete : function(data) {
					oCommon.jqGrid.resize();
					oCommon.jqGrid.checkNodata(undefined, data);
					oCommon.jqGrid.gridComplete(this);
				}
			});
		}
	}

	function excelDownAction() {
		//var sSearchUserNm =  $('#searchUserNm').val();
		var sSearchYear = '';
		var sSearchMonth = '';
		var sDatePicker = $('#input-date-picker').val();
		var sChecked = $('input[name=search-gbn]:checked').val();
		var sFormat = 'YYYY-MM';
		if (sChecked == 'M') sFormat = 'YYYY';
		var momentSearch = moment(sDatePicker, sFormat);
		sSearchYear = momentSearch.format('YYYY');
		if (sChecked == 'D') sSearchMonth = momentSearch.format('MM');

		document.excelDownFrm.searchYear.value = sSearchYear;
		document.excelDownFrm.searchMonth.value = sSearchMonth;
		//document.excelDownFrm.searchUserNm.value = sSearchUserNm;
		document.excelDownFrm.action = "<c:url value='/wrks/evtctl/stats/cctvConnCombined/list.excel'/>";
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
		} else if (sDate.length == 4) {
			var momentDate = moment(sDate, 'YYYY');
			if (momentDate.isValid()) {
				$inputM.data('date', sDate);
			}
		}

		if (format == 'YYYY-MM') {
			var oData = $inputD.data();
			console.log(oData);
			if (oData.hasOwnProperty('date')) {
				$('#input-date-picker').val(oData.date);
			} else {
				$('#input-date-picker').val(moment().format('YYYY-MM'));
			}
		} else if (format == 'YYYY') {
			var oData = $inputM.data();
			console.log(oData);
			if (oData.hasOwnProperty('date')) {
				$('#input-date-picker').val(oData.date);
			} else {
				$('#input-date-picker').val(moment().format('YYYY'));
			}
		}

		$('#input-date-picker').data('DateTimePicker').destroy();
		$('#input-date-picker').datetimepicker({
			showTodayButton : true,
			ignoreReadonly : true,
			format : format,
			maxDate: moment()
		});
	}
</script>
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
					<div class="tableType2 seachT">
						<table>
							<colgroup>
								<col style="width: 100px;" />
								<col style="width: *;" />
								<col style="width: 100px;" />
								<col style="width: *;" />
							</colgroup>
							<tbody>
								<tr>
									<th>검색구분</th>
									<td><label>
											<input type="radio" name="search-gbn" value="M" onchange="javascript:changeFormatDatePicker('YYYY');" />
											 월별
										</label>
										<label>
											<input type="radio" name="search-gbn" value="D" onchange="javascript:changeFormatDatePicker('YYYY-MM');" checked="checked"  />
											 일별
										</label>
									</td>
									<th>년월</th>
									<td><div class="form-group form-inline">
											<div class="input-group date">
												<input type="text" class="form-control" id="input-date-picker" readonly="readonly" />
											</div>
											<img class="ui-datepicker-trigger" src="<c:out value='${pageContext.request.contextPath}' />/images/icon_calendar.png" onclick="toggleDatePicker();">
										</div>
									</td>
								<%--<th>사용자명</th>
									<td><input type="text" name="searchUserNm" id="searchUserNm" class="txtType searchEvt" style="ime-mode: active"></td>--%>
									<td><a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a></td>
								</tr>
							</tbody>
						</table>
					</div>

					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

					<div class="tableType1" style="height: 495px;">
						<table id="grid"></table>
					</div>
					<div class="paginate"></div>
					<div class="btnWrap btnR">
						<a href="javascript:;" class="btn btnDt btnExcel">엑셀저장</a>
					</div>
				</div>
				<!-- End content -->
			</div>
			<!-- End contentWrap -->
		</div>
		<!-- End container -->
	</div>
	<form name="excelDownFrm" method="post">
		<input type="hidden" name="searchYear" />
		<input type="hidden" name="searchMonth" />
		<input type="hidden" name="titleKey" />
		<input type="hidden" name="titleHeader" />
		<%--<input type="hidden" name="searchUserNm" />--%>
	</form>
	<!--  footer -->
	<footer>
		<%@include file="/WEB-INF/jsp/mntr/layout/footer.jsp"%>
	</footer>
	<!-- //footer -->
</body>
</html>
