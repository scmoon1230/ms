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
        url: '<c:url value='/'/>wrks/sstm/menu/icon/list.json',
        datatype: "json",
        postData: {
        	iconTy : ""
        },
        colNames: [
                    '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
                    '연번',
                    'ON',
                    'OFF',
                    'ON 이미지',
                    'OFF 이미지',
                    'icon_ty',
                    '구분',
                    '등록일',
                    '등록자아이디',
                    '수정일',
                    '수정자아이디'
                   ],
        colModel: [
                  { name: 'CHECK'      , width:50 , align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
                  { name: 'SEQ_NO'     , width:100, align:'center'},
                  { name: 'ON_ICON'    , width:0, align:'center', hidden:true},
                  { name: 'OFF_ICON'   , width:0, align:'center', hidden:true},
                  { name: 'ON_ICON_VIEW', width:200, align:'center', formatter: function (cellValue) {
                      return "<img src='<c:url value='/'/>images/menu/" + cellValue + "' alt='' style='cursor:pointer' />";
                  }},
                  { name: 'OFF_ICON_VIEW', width:200, align:'center', formatter: function (cellValue) {
                      return "<img src='<c:url value='/'/>images/menu/" + cellValue + "' alt='' style='cursor:pointer' />";
                  }},

                  { name: 'ICON_TY'    , width:0, align:'center', hidden:true},
                  { name: 'ICON_TY_NM'  , width:200, align:'center'},
                  { name: 'RGS_DATE'   , width:0, align:'center', hidden:true},
                  { name: 'RGS_USER_ID', width:0, align:'center', hidden:true},
                  { name: 'UPD_DATE'   , width:200, align:'center'},
                  { name: 'UPD_USER_ID', width:0, align:'center', hidden:true}
          ],
        pager: '#pager',
        rowNum: $('#rowPerPageList').val(),
        sortname: 'seq_no',
        sortorder: 'DESC',
        viewrecords: true,
        multiselect: false,
        loadonce:false,
        shrinkToFit: true,
        scrollOffset: 0,
        autowidth: true,
        jsonReader: {
        	id: "seq_no",
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
        onCellSelect : function(rowid, iCol, cellcontent, e){
			if(iCol == 0) return false;

			var list = jQuery("#grid").getRowData(rowid);


			//Detail
			$("#dSeqNo").html(list.SEQ_NO);
			//$("#dOnIcon").html(list.ON_ICON);
			//$("#dOffIcon").html(list.OFF_ICON);
			$("#dIconTy").html(list.ICON_TY_NM);

			$.selectBarun("#iIconTy", list.ICON_TY);

			var onIcon = ""
			var offIcon = ""

			onIcon = list.ON_ICON;
			offIcon = list.OFF_ICON;

			$("#dOnIcon > img").remove();
			if(onIcon != ""){
				$("#dOnIcon").prepend('<img src="<c:url value='/'/>images/menu/'+ onIcon + '\" style=\'width: 30px; height: 30px\'/>');
			}

			$("#dOffIcon > img").remove();
			if(offIcon != ""){
				$("#dOffIcon").prepend('<img src="<c:url value='/'/>images/menu/'+ offIcon + '\" style=\'width: 30px; height: 30px\'/>');
			}


			//Update

			$("#iSeqNo").val(list.SEQ_NO);
			//$("#iIconTy").html(list.ICON_TY_NM);
			$.selectBarun("#iIconTy", list.ICON_TY);


			$("#iOnIcon").val(onIcon);
			$("#iOffIcon").val(offIcon);

			$("#iOnIconFile").val("");
			$("#iOffIconFile").val("");

			$("#iOnIconFile").attr("user-data-bak", onIcon);
			$("#iOffIconFile").attr("user-data-bak", offIcon);




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
    	myPostData.iconTy = $("#iconTy").val();

    	$("#grid").trigger("reloadGrid");
    });

    //등록저장버튼
    $(".btnSvEc").bind("click",function(){
       	try{
           	if(validate() == false)
           		return false;
       	}catch(e) {}

    	var params = "";
		    params += "&seqNo="          + encodeURIComponent($("#iSeqNo").val());
		    params += "&onIcon="         + encodeURIComponent($("#iOnIcon").val());
		    params += "&offIcon="        + encodeURIComponent($("#iOffIcon").val());
		    params += "&iconTy="         + encodeURIComponent($("#iIconTy").val());
	    try{
    		if($.multiFileUpload2(".layerRegister", "<c:url value='/'/>wrks/sstm/menu/icon/image.upload", params, function()
    				{
		    			$("#grid").trigger("reloadGrid");
    					$(".mask").remove();
            			$(".layer").hide();
            			insertFlag = false;
					}) == false) {
				return false;
			}
       	}catch(e) {}

        return false;
    });

  //file Element 핸들링
   $(".btnFileObject").bind("click",function(){
   	var id = $(this).attr("user-ref-id");
  		$("#" + id).click();
       return false;
   });

   $(".upload").bind("change", function(){
   	$.checkChangeFileObject2(this);
       return false;
   });
});

function resetAction() {
	$.resetInputObject(".layerRegister .tableType2");
}

function updateAction(obj) {


	var params = "";
	    params += "&seqNo="          + encodeURIComponent($("#iSeqNo").val());
	    params += "&onIcon="         + encodeURIComponent($("#iOnIcon").val());
	    params += "&offIcon="        + encodeURIComponent($("#iOffIcon").val());
	    params += "&iconTy="         + encodeURIComponent($("#iIconTy").val());
	    params += "&state=U";

    try{
		if($.multiFileUpload2(".layerRegister", "<c:url value='/'/>wrks/sstm/menu/icon/image.upload", params, function()
				{
					$("#grid").setGridParam({page :$("#cur-page").val()});
	    			$("#grid").trigger("reloadGrid");
					$(".mask").remove();
        			$(".layer").hide();
				}) == false) {
				return false;
		}
   	}catch(e) {}

    return true;


}

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function insertAction(obj) {

	var params = "";
	    params += "&seqNo="          + encodeURIComponent($("#iSeqNo").val());
	    params += "&onIcon="         + encodeURIComponent($("#iOnIcon").val());
	    params += "&offIcon="        + encodeURIComponent($("#iOffIcon").val());
	    params += "&iconTy="         + encodeURIComponent($("#iIconTy").val());
	    params += "&state=I";

    try{
		if($.multiFileUpload2(".layerRegister", "<c:url value='/'/>wrks/sstm/menu/icon/image.upload", params, function()
				{
	    			$("#grid").trigger("reloadGrid");
					$(".mask").remove();
        			$(".layer").hide();
				}) == false) {
			return false;
		}
   	}catch(e) {}

    return true;

}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = "<c:url value='/'/>wrks/sstm/menu/icon/delete.json";
    //var params = "cdGrpId=" + $("#dCdGrpId").text();
    var params = "seqNo=" + $("#iSeqNo").val();

    $.ajaxEx($('#grid'), {
    	type : "POST",
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").setGridParam({page :$("#cur-page").val()});
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


	//var s =  $("#grid").jqGrid('getGridParam', 'selarrrow');
	//s = $("grid").getGridParam('selarrrow');
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
    var url = "<c:url value='/'/>wrks/sstm/menu/icon/deleteMulti.json";
    var params = "";
	//alert(s.length);
    for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);

	    params += "&seqNo=" + list.SEQ_NO;

    }
    //alert(params);

    $.ajaxEx($('#grid'), {
    	type : "POST",
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").setGridParam({page :$("#cur-page").val()});
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
<%@include file="/WEB-INF/jsp/mntr/cmm/commonVariables.jsp" %>
<%@include file="/WEB-INF/jsp/mntr/layout/header.jsp"%>
<div id="wrapper" class="wth100">

    <!-- topbar -->
   <%//@include file="/WEB-INF/jsp/cmm/topMenu.jsp"%>
    <!-- //topbar -->
    <!-- container -->
    <div class="container">
        <!-- leftMenu -->
      <%//@include file="/WEB-INF/jsp/cmm/leftMenu.jsp"%>
        <!-- //leftMenu -->

        <!-- content -->
        <div class="contentWrap">
            <div class="topArea">
                <a href="#"><div class="btnOpen"></div></a>

		<%@include file="/WEB-INF/jsp/cmm/pageTopNavi.jsp"%>

            </div>
            <div class="content">
                <div class="titArea">
                    <h3 class="tit">메뉴아이콘</h3>
                </div>
                <div class="tableTypeHalf seachT">
                    <table>
                        <caption>메뉴아이콘</caption>
                        <tbody>
                        <tr>
                            <th>구분</th>
	                        <td>
	                            <select name="" id="iconTy" class="selectType1" maxlength="1">
						    		<option value="">전체</option>
						    		<c:forEach items="${codeTyList}" var="val">
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
                    <a href="#" class="btn btnDt btnRgt">신규</a>
                    <a href="#" class="btn btnMultiDe">삭제</a>
                </div>
            </div>

            <!-- 레이어팝업 상세 -->
            <div class="layer layerDetail" id="div_drag_1">
                <div class="tit"><h4>아이콘 상세</h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                        <table>
                            <caption>아이콘 상세</caption>
                            <tbody>
                            <tr>
                                <th>연번</th>
                                <td id="dSeqNo"></td>
                                <th>아이콘구분</th>
                                <td id="dIconTy"></td>
                            </tr>
                            <tr>
                                <th>ON 이미지</th>
                                <td id="dOnIcon"></td>
                                <th>OFF 이미지</th>
                                <td id="dOffIcon"></td>
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
            <div class="layer layerRegister"  id="div_drag_2">
                <div class="tit"><h4>아이콘 <span id="modetitle">추가</span></h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                    	<input type="hidden" id="iSeqNo" />
                        <table>
                            <caption>아이콘 등록</caption>
                            <tbody>
                            <tr>
                                <th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>ON 이미지</th>
                                <td>
                                	<input type="text" class="txtType70" id="iOnIcon" maxlength="255" readonly="readonly" user-title="ON이미지"  required="required"/>
                                	<input type="file" id="iOnIconFile" class="txtType upload" style="display:none" user-title="ON이미지" user-ref-id="iOnIcon" user-ext=".png" user-col-name="ON_ICON" />
                                	<a href="#" class="btn btnFileObject" user-ref-id="iOnIconFile">파일찾기</a>
                                </td>
                            </tr>
                            <tr>
                                <th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>OFF 이미지</th>
                                <td>
                                	<input type="text" class="txtType70" id="iOffIcon" maxlength="255" readonly="readonly" user-title="OFF이미지"  required="required"/>
                                	<input type="file" id="iOffIconFile" class="txtType upload" style="display:none" user-title="OFF이미지" user-ref-id="iOffIcon" user-ext=".png" user-col-name="OFF_ICON" />
                                	<a href="#" class="btn btnFileObject" user-ref-id="iOffIconFile">파일찾기</a>
                                </td>
                            </tr>
                            <tr>
                                <th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>아이콘 구분</th>
                                <td colspan="3">
                                	<select name="" id="iIconTy" class="selectType1" maxlength="1">
							    		<c:forEach items="${codeTyList}" var="val">
							        		<option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
							    		</c:forEach>
									</select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                   <div class="btnCtr">
                   		<input type="hidden" name="svTag" id="svTag" />
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