<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<style type="text/css">
.ui-jqgrid-btable tr[role=row]:nth-child(odd) {
	background: #eee;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$.jqGrid($('#grid'), {
			url : '<c:url value='/'/>wrks/info/his/menuUsedListData.json',
			datatype : "json",
			scrollOffset : 0,
			postData : {
				seachTy : $("#seachTy").val(),
				seachNm : $("#seachNm").val(),
				strDateStart : $("#strDateStart").val(),
				strDateEnd : $("#strDateEnd").val()
			},
			colNames : [
					'No', '일자', '접속시간', '메뉴명', '이름', '아이디 ', '그룹명', '그룹레벨명', '기관', '부서', '최종접속일시', '사용회수'
			],
			colModel : [
					{	name : 'rk',								width : 50,		align : 'center',	sortable : false	}
					, {name : 'connectYmd',	index : 'CONNECT_YMD',	width : 100,	align : 'center'		}
					, {name : 'firstTime',	index : 'FIRST_TIME',	width : 200,	align : 'center'		}
					, {name : 'menuNm',		index : 'MENU_NM',		width : 200,	align : 'center'		}
					, {name : 'userNmKo',	index : 'USER_NM_KO',	width : 200,	align : 'center'		}
					, {name : 'userId',		index : 'USER_ID',		width : 200,	align : 'center'		}
					, {name : 'grpNm',		index : 'GRP_NM',		width : 200,	align : 'center'		}
					, {name : 'authLvlNm',	index : 'AUTH_LVL_NM',	width : 200,	align : 'center'		}
					, {name : 'insttNm',	index : 'INSTT_NM',		width : 200,	align : 'center'		}
					, {name : 'deptNm',		index : 'DEPT_NM',		width : 200,	align : 'center'		}
					, {name : 'lastTime',	hidden : true		}
					, {name : 'usedCnt',	hidden : true		}
			],
			pager : '#pager',
			rowNum : $('#rowPerPageList').val(),
			height: $('#grid').parent().height() - 40,
			sortname : 'CONNECT_YMD',
			sortorder : 'DESC',
			viewrecords : true,
			multiselect : false,
			loadonce : false,
			jsonReader : {
				id : "userId",
				root : function(obj) {
					return obj.rows;
				},
				page : function(obj) {
					return 1;
				},
				total : function(obj) {
					if (obj.rows.length > 0) {
						var page = obj.rows[0].rowcnt / rowNum;
						if ((obj.rows[0].rowcnt % rowNum) > 0) page++;
						return page;
					} else {
						return 1;
					}
				},
				records : function(obj) {
					return $.showCount(obj);
				}
			},
			cmTemplate: { sortable: false },
			beforeRequest : function() {
				$.loading(true);
				rowNum = $('#rowPerPageList').val();
			},
			beforeProcessing : function(data, status, xhr) {
				$.loading(false);
				if (typeof data.rows != "undefined" || data.row != null) {
					$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
				}
			},
			loadComplete : function(data) {
				$('#grid').jqGrid('sortGrid','firstTime','true','desc');
				oCommon.jqGrid.resize();
				oCommon.jqGrid.checkNodata(undefined, data);
				oCommon.jqGrid.gridComplete(this);
			}
		});

		$(".btnS").bind("click", function() {
			if ($.validate(".tableTypeHalf.seachT") == false) return;
			$("#grid").setGridParam({
				rowNum : $('#rowPerPageList').val()
			});
			var myPostData = $("#grid").jqGrid('getGridParam', 'postData');

			//검색할 조건의 값을 설정한다.
			myPostData.seachTy = $("#seachTy").val();
			myPostData.seachNm = $("#seachNm").val();
			myPostData.strDateStart = $("#strDateStart").val();
			myPostData.strDateEnd = $("#strDateEnd").val();

			$("#grid").trigger("reloadGrid");
		});

		$(".tableType1").css('height', window.innerHeight - 350);
		$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
				
	});
	function excelDownAction() {
		var curPage = '1';
		if (typeof $('#cur-page').val() != "undefined") {
			curPage = $('#cur-page').val();
		}
		var searchUserId = "";
		var searchUserNm = "";
		if ($("#seachTy").val() == 'ID') {
			searchUserId = $("#seachNm").val();
		} else {
			searchUserNm = $("#seachNm").val();
		}
		document.excelDownFrm.searchStartDate.value = $("#strDateStart").val();
		document.excelDownFrm.searchEndDate.value = $("#strDateEnd").val();
		document.excelDownFrm.searchUserId.value = searchUserId;
		document.excelDownFrm.searchUserNm.value = searchUserNm;
		document.excelDownFrm.action = "<c:url value='/wrks/info/his/menuUsedListExcel.excel'/>";
		document.excelDownFrm.submit();
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
					<div class="tableTypeHalf seachT">
						<table>
							<caption>사용자접속현황</caption>
							<colgroup>
								<col style="width: 150px;" />
								<col style="width: 150px;" />
								<col style="width: *;" />
								<col style="width: 150px;" />
								<col style="width: *;" />
							</colgroup>
							<tbody>
								<tr>
									<th>분류</th>
									<td>
										<select name="seachTy" id="seachTy" class="selectType1" maxlength="1">
											<option value="NM" SELECTED="SELECTED">이름</option>
											<option value="ID">아이디</option>
										</select>
									</td>
									<td>
										<input type="text" name="seachNm" id="seachNm" class="txtType txtType100 searchEvt" style="ime-mode: active" maxlength="100" />
									</td>
									<th>기간</th>
									<td>
										<input type="text" name="" id="strDateStart" class="date8Type datepicker1" value="${firstDay}" required="required" user-title="시작일자" user-data-type="betweendate"
											user-ref-id="strDateEnd">
										<span class="bl">~</span>
										<input type="text" name="" id="strDateEnd" class="date8Type datepicker2" value="${currentDay}" required="required" user-title="종료일자">
										<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
									</td>
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
				<!-- //content -->
			</div>
			<!-- //container -->
		</div>
	</div>
	<form name="excelDownFrm" method="post">
		<input type="hidden" name="searchStartDate" id="searchStartDate" />
		<input type="hidden" name="searchEndDate" id="searchEndDate" />
		<input type="hidden" name="searchUserId" id="searchUserId" />
		<input type="hidden" name="searchUserNm" id="searchUserNm" />
	</form>

	<!--  footer -->
	<footer>
		<%@include file="/WEB-INF/jsp/mntr/layout/footer.jsp"%>
	</footer>
	<!-- //footer -->
</body>
</html>
