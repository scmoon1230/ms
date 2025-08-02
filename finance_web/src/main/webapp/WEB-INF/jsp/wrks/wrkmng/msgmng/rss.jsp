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
        url: '<c:url value='/'/>wrks/wrkmng/msgmng/rss/list.json',
        datatype: "json",
        postData: {
        	dstrtCd : $("#sDstrtCd").val(),
        	useTy : $("#sUseTy").val()
        },
        colNames: [
						'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
						'RSS 아이디',
                        '제목',
                        'URL',
                        '사용유형코드',
                        '사용여부',
                        '지구코드',
                        '지구명',
                        '등록자',
                        '등록일',
                        '수정자',
                        '수정일'
                   ],
        colModel: [
				{ name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
				{ name: 'RSS_ID', width:175, align:'center', hidden:true},
				{ name: 'RSS_TITLE', width:265, align:'center'},
				{ name: 'RSS_URL', width:300, align:'center'},
				{ name: 'USE_TY', width:175, align:'center', hidden:true},
				{ name: 'USE_TY_NM', width:150, align:'center'},
				{ name: 'DSTRT_CD', width:175, align:'center', hidden:true},
				{ name: 'DSTRT_NM', width:220, align:'center'},
				{ name: 'RGS_USER_ID', width:175, align:'center', hidden:true},
				{ name: 'RGS_DATE', width:175, align:'center', hidden:true},
				{ name: 'UPD_USER_ID', width:175, align:'center', hidden:true},
				{ name: 'UPD_DATE', width:175, align:'center', hidden:true}

          ],
        pager: '#pager',
        rowNum: $('#rowPerPageList').val(),
        sortname: 'RSS_ID',
        sortorder: 'DESC',
        viewrecords: true,
        multiselect: false,
        loadonce:false,
        jsonReader: {
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
			var list = jQuery("#grid").getRowData(rowid);


			$("#dRssTitle").html(list.RSS_TITLE);
			$("#dRssUrl").html(list.RSS_URL);
			$("#dUseTyNm").html(list.USE_TY_NM);
			$("#dDstrtNm").html(list.DSTRT_NM);
			$("#dUpdUserId").html(list.UPD_USER_ID);
			$("#dUpdDate").html(list.UPD_DATE);
			$("#dRgsUserId").html(list.RGS_USER_ID);
			$("#dRgsDate").html(list.RGS_DATE);

			$("#hRssId").val(list.RSS_ID);
			$("#iRssTitle").val(list.RSS_TITLE);
			$("#iRssUrl").val(list.RSS_URL);
			$.selectBarun("#iUseTy", list.USE_TY);
			$.selectBarun("#iDstrtCd", list.DSTRT_CD);

			$.showDetail();
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

    	//검색할 조건의 값을 설정한다.
    	myPostData.dstrtCd = $("#sDstrtCd").val();
    	myPostData.useTy = $("#sUseTy").val();

    	$("#grid").trigger("reloadGrid");
    });
});

function resetAction() {
	//alert("resetAction");

	$("#iRssTitle").val("");
	$("#iRssUrl").val("");
	$("#iUseTy").get(0).selectedIndex=0;
	$("#iDstrtCd").get(0).selectedIndex=0;
}

function updateAction(obj) {
	//alert('updateAction');

    var url = "<c:url value='/'/>wrks/wrkmng/msgmng/rss/update.json";
    var params = "rssId=" + $("#hRssId").val();
   		params += "&rssTitle=" + escape(encodeURIComponent($("#iRssTitle").val()));
        params += "&rssUrl=" + escape(encodeURIComponent($("#iRssUrl").val()));
        params += "&useTy=" + escape(encodeURIComponent($("#iUseTy").val()));
        params += "&dstrtCd=" + escape(encodeURIComponent($("#iDstrtCd").val()));

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").trigger("reloadGrid");
        	//alert("저장하였습니다.");
        	alert(data.msg);
        },
        error:function(e){
            alert(e.responseText);
        }
    });
}

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function insertAction(obj) {
	//alert('insertAction');
	var url = "<c:url value='/'/>wrks/wrkmng/msgmng/rss/insert.json";
	var params = "rssTitle=" + escape(encodeURIComponent($("#iRssTitle").val()));
		params += "&rssUrl=" + escape(encodeURIComponent($("#iRssUrl").val()));
	    params += "&useTy=" + escape(encodeURIComponent($("#iUseTy").val()));
	    params += "&dstrtCd=" + escape(encodeURIComponent($("#iDstrtCd").val()));

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").trigger("reloadGrid");
        	//alert("저장하였습니다.");
        	alert(data.msg);
        },
        error:function(e){
            alert(e.responseText);
        }
    });
}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = "<c:url value='/'/>wrks/wrkmng/msgmng/rss/delete.json";
    var params = "rssId=" + $("#hRssId").val();

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").trigger("reloadGrid");
        	//alert("삭제하였습니다.");
        	alert(data.msg);
        },
        error:function(e){
            //alert(e.responseText);
        	alert(data.msg);
        }
    });
}

function deleteMultiAction(obj) {
	//alert('deleteMultiAction');

	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
    var url = "<c:url value='/'/>wrks/wrkmng/msgmng/rss/deleteMulti.json";
    var params = "";
	//alert(s.length);
    for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);

	    params += "&rssId=" + list.RSS_ID;
    }
    //alert(params);

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").trigger("reloadGrid");
        	//alert("삭제하였습니다.");
        	alert(data.msg);
        },
        error:function(e){
            //alert(e.responseText);
            alert(data.msg);

        }
    });

    return true;
}
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
                    <h3 class="tit">RSS정보관리</h3>
                </div>
                <div class="tableTypeHalf seachT">
                    <table>
                        <caption>RSS정보관리</caption>
                        <tbody>
                        <tr>
	                        <th>지구</th>
	                        <td>
	                        	<select name="" id="sDstrtCd" class="selectType1">
											<option value="">전체</option>
										    <c:forEach items="${listCmDstrtCdMng}" var="val">
										        <option value="${val.DSTRT_CD}"><c:out value="${val.DSTRT_NM}" ></c:out></option>
										    </c:forEach>
								</select>
	                        </td>
	                        <th>사용유형</th>
	                        <td>
	                        	<select name="" id="sUseTy" class="selectType1">
										    <c:forEach items="${useGrpList}" var="val">
										        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
										    </c:forEach>
								</select>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
	                        </td>
                        </tr>
                        </tbody>
                    </table>
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
                <div class="tableType1">
    				<table id="grid" style="width:100%">
    				</table>
                </div>
                <div class="paginate">
                </div>
                <div class="btnWrap btnR">
                    <a href="#" class="btn btnDt btnRgt">등록</a>
                    <a href="#" class="btn btnMultiDe">삭제</a>
                </div>
            </div>

            <!-- 레이어팝업 상세 -->
            <div class="layer layerDetail" id="div_drag_1">
                <div class="tit"><h4>RSS 상세</h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                        <table>
                            <caption>RSS 상세</caption>
                            <tbody>
                            <tr>
                                <th>제목</th>
                                <td id="dRssTitle" colspan="3"></td>
                            </tr>
                            <tr>
                            	<th>URL</th>
                                <td id="dRssUrl" colspan="3"></td>
                            </tr>
                            <tr>
                                <th>사용여부</th>
                                <td id="dUseTyNm"></td>
                                <th>지구</th>
                                <td id="dDstrtNm"></td>
                            </tr>
                            <tr>
                                <th>수정자</th>
                                <td id="dUpdUserId"></td>
                                <th>수정일시</th>
                                <td id="dUpdDate"></td>
                            </tr>
                            <tr>
                                <th>등록자</th>
                                <td id="dRgsUserId"></td>
                                <th>등록일시</th>
                                <td id="dRgsDate"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="btnCtr">
                        <a href="#" class="btn btnMd">수정</a>
                        <a href="#" class="btn btnDe">삭제</a>
                        <a href="#" class="btn btnC">취소</a>
                    </div>
                </div>
            </div>
            <!-- //레이어팝업 상세 -->

            <!-- 레이어팝업 등록 -->
            <div class="layer layerRegister" id="div_drag_2">
                <div class="tit"><h4>RSS <span id="modetitle">추가</span></h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                    	<input type="hidden" id="hRssId" />
                        <table>
                            <caption>RSS 등록</caption>
                            <tbody>
                            <tr>
                                <th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>제목</th>
                                <td colspan="3"><input type="text" id="iRssTitle" class="txtType" maxlength="100" required="required" style="ime-mode:active" user-title="RSS 제목"/></td>
                            </tr>
                            <tr>
                                <th>URL</th>
                                <td colspan="3"><input type="text" id="iRssUrl" class="txtType" maxlength="100"/></td>
                            </tr>
                            <tr>
                            	<th>사용여부</th>
                            	<td>
                                	<select name="" id="iUseTy" class="selectType1" >
									    <c:forEach items="${useGrpList}" var="val">
									        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
									    </c:forEach>
									</select>
                                </td>
                                <th>지구 </th>
                            	<td>
                                	<select name="" id="iDstrtCd" class="selectType1" >
									    <c:forEach items="${listCmDstrtCdMng}" var="val">
									        <option value="${val.DSTRT_CD}"><c:out value="${val.DSTRT_NM}" ></c:out></option>
									    </c:forEach>
									</select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                   <div class="btnCtr">
                        <a href="#" class="btn btnSv">저장</a>
                        <a href="#" class="btn btnC">취소</a>
                    </div>
                </div>
            </div>
            <!-- //레이어팝업 등록 -->
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
