<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">
$(document).ready(function(){
    $.jqGrid($('#grid'), {
        url: '<c:url value='/'/>lpr/his/hisvw/recognize.json',
        datatype: "json",
        postData: {
        	strCarNo : $("#strCarNo").val(),
        	strPlace : $("#strPlace").val(),
        	strDateStart : $("#strDateStart").val(),
        	strDateEnd : $("#strDateEnd").val()
        },
        colNames: [
	               //     '선택',
	                    'No',
                        '발생시간',
                        '차량번호',
                        'CCTV명',
                        '발생위치'
                   ],
        colModel: [
                  //{ name: 'CHK'	,   width:100,  align:'center', sortable: false, editable:true, edittype:"checkbox",editoptions: {value:"true:false"},formatter:"checkbox",formatoptions:{disabled:false}},
                  { name: 'RK'				,   width:100,  align:'center', sortable: false},
                  { name: 'EVT_OCR_YMD_HMS'	,   width:100,  align:'center'},
                  { name: 'CAR_LICENSE_NO'	,   width:100,  align:'center'},
                  { name: 'CCTV_NM'		,   width:400,  align:'center'},
                  { name: 'AREA_NM'		,   width:100,  align:'center'}
          ],
        //pager: '#pager',
        rowNum: $('#rowPerPageList').val(),
        sortname: 'EVT_OCR_YMD_HMS',
        sortorder: 'ASC',
        viewrecords: true,
        loadonce:false,
        jsonReader: {
        	id: "EVT_OCR_YMD_HMS",
        	root: function(obj) { return obj.rows; },
        	page: function(obj) { return 1; },
        	total: function(obj) {
        		if(obj.rows.length > 0) {
        			var page  = obj.rows[0].ROWCNT / rowNum;
        			if( (obj.rows[0].ROWCNT % rowNum) > 0)
        				page++;
        			return page;
        		}
        		else
        			return 1;
        	},
        	records: function(obj) { return $.showCount(obj); }
        },
        onSelectRow: function(rowid, status, e) {
        },
        beforeRequest: function() {
        	$.loading(true);
        	rowNum = $('#rowPerPageList').val();
        },
        beforeProcessing: function(data, status, xhr){
        	$.loading(false);
        	if(typeof data.rows != "undefine" || data.row != null) {
        		$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
        	}
        }
  	});

    $(".btnS").bind("click",function(){
    	$("#grid").setGridParam({rowNum: $('#rowPerPageList').val()});
    	var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
    	myPostData.strCarNo = $("#strCarNo").val();
    	myPostData.strPlace = $("#strPlace").val();
    	myPostData.strDateStart = $("#strDateStart").val();
    	myPostData.strDateEnd = $("#strDateEnd").val();
    	$("#grid").trigger("reloadGrid");
    });
});

</script>

</head>
<body>
<div id="wrapper">
    <!-- topbar -->
	<%@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
    <!-- //topbar -->
    <!-- container -->
    <div class="container">
        <!-- leftMenu -->
		<%@include file="/WEB-INF/jsp/cmm/leftMenu.jsp"%>
        <!-- //leftMenu -->

        <!-- content -->
        <div class="contentWrap">
            <div class="topArea">
                <a href="#" class="btnOpen"><img src="<c:url value='/'/>images/btn_on_off.png" alt="열기/닫기"></a>

				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi.jsp"%>

            </div>
            <div class="content">
                <div class="titArea">
                    <h3 class="tit">차량번호 인식이력 현황</h3>
                </div>
                <style>
                .searchBox {position: relative;height: 57px;margin:-1px 0 10px; padding: 10px 0 0;border-top: 1px solid #e0e0e0;border-bottom: 1px solid #e0e0e0;box-sizing: border-box;background-color: #f9f9f9;}
                .searchBox dl dt {float: left;width: 105px;padding: 9px 0 0 20px;}
                .searchBox .btnSearch {position: absolute;right: 10px;top: 10px}
                </style>
                <div class="searchBox">
                    <dl>
                        <dt>차량번호</dt>
                        <dd><input type="text" name="" id="strCarNo" class="txtType carNum"></dd>
                        <dt>차량번호</dt>
                        <dd><input type="text" name="" id="strPlace" class="txtType carNum"></dd>
                        <dt>차량번호</dt>
                        <dd><input type="text" name="" id="strDateStart" class="txtType carNum"></dd>
                        <dt>차량번호</dt>
                        <dd><input type="text" name="" id="strDateEnd" class="txtType carNum"></dd>
                    </dl>
                    <div class="btnSearch">
                        <a href="#" class="btn btnS">검색</a>
                    </div>
                </div>
                <div class="searchArea">
                    <div class="page fL">
                        <span class="txt">페이지당</span>
                        <div class="selectBox">
							<select name="rowPerPageList" id="rowPerPageList" class="selectType1">
							    <c:forEach items="${rowPerPageList}" var="val">
							        <option value="${val.CD_ID}" ${val.CD_ID == rowPerPageSession ? 'selected' : ''}><c:out value="${val.CD_NM_KO}" ></c:out></option>
							    </c:forEach>
							</select>
                        </div>
                        <span class="totalNum">전체<em id="rowCnt"></em>건</span>
                    </div>
                </div>
                <div class="paginate">
                </div>
                <div class="tableType1">
    				<table id="grid" style="width:100%">
    				</table>
                </div>
            </div>
        </div>
        <!-- //content -->
    </div>
    <!-- //container -->
</div>

<!-- footer -->
<!-- <div id="footwrap">
    <div id="footer"><%@include file="/WEB-INF/jsp/cmm/footer.jsp"%></div>
</div> -->
<!-- //footer -->
</body>
</html>