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
        url: '<c:url value='/'/>wrks/sstm/code/sycd/list.json',
        datatype: "json",
		autowidth: true,
        postData: {
        	usvcGrp : $("#usvcGrp").val(),
        	sysNm : $("#sysNm").val(),
        	useTyCd : $("#useTyCd").val()
        },
        colNames: [ '<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
                     'U-서비스그룹', '시스템한글명', '시스템코드', '시스템영문명', '설명', 'U-서비스그룹코드', '사용유형코드', '사용유형',
                     '등록자', '등록일', '수정자', '수정일'
                   ],
        colModel: [
                  { name: 'CHECK', width:60, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
                  { name: 'cdNmKo', index : 'CD_NM_KO', width:300, align:'center'},
                  { name: 'sysNmKo', index : 'SYS_NM_KO', width:300, align:'center'},
                  { name: 'sysCd', index : 'SYS_CD', width:220, align:'center'},
                  { name: 'sysNmEn', index : 'SYS_NM_EN', width:300, align:'center', hidden:true},
                  { name: 'dscrt', index : 'DSCRT', width:300, align:'center', hidden:true},
                  { name: 'usvcGrpCd', index : 'USVC_GRP_CD', width:300, align:'center', hidden:true},
                  { name: 'useTyCd', index : 'USE_TY_CD', width:100, align:'center', hidden:true},
                  { name: 'useNm', index : 'USE_NM', width:100, align:'center'},
                  { name: 'rgsUserId', index : 'RGS_USER_ID', width:125, align:'center', hidden:true},
                  { name: 'rgsDate', index : 'RGS_DATE', width:200, align:'center', hidden:true},
                  { name: 'updUserId', index : 'UPD_USER_ID', width:125, align:'center', hidden:true},
                  { name: 'updDate', index : 'UPD_DATE', width:200, align:'center', hidden:true}
          ],
        pager: '#pager',
        rowNum: $('#rowPerPageList').val(),
        height: $("#grid").parent().height()-40,
        sortname: 'CD_NM_KO, SYS_NM_KO',
        sortorder: 'ASC',
        viewrecords: true,
        multiselect: false,
        loadonce:false,
        jsonReader: {
        	id: "SYS_CD",
        	root: function(obj) { return obj.rows; },
        	page: function(obj) { return 1; },
        	total: function(obj) {
        		if(obj.rows.length > 0) {
        			var page  = obj.rows[0].rowcnt / rowNum;
        			if( (obj.rows[0].rowcnt % rowNum) > 0)
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

			$("#dSysCd").html(list.sysCd);
			$("#dSysNmKo").html(list.sysNmKo);
			$("#dSysNmEn").html(list.sysNmEn);
			$("#dDscrt").html(list.dscrt);
			//$("#dUsvcGrpNm").html(list.USVC_GRP_NM);
			$("#dUseTyCd").html(list.useNm);
			$("#dRgsUserId").html(list.rgsUserId);
			$("#dRgsDate").html(list.rgsDate);
			$("#dUpdUserId").html(list.updUserId);
			$("#dUpdDate").html(list.updDate);

			$("#iSysCd").val(list.sysCd);
			$("#sysCdBk").val(list.sysCd);
			$("#iSysNmKo").val(list.sysNmKo);
			$("#iSysNmEn").val(list.sysNmEn);
			$("#iDscrt").val(list.dscrt);
			$.selectBarun("#iUsvcGrpCd", list.usvcGrpCd);
			$.selectBarun("#iUseTyCd", list.useTyCd);
			$("#iRgsUserId").val(list.rgsUserId);
			$("#iRgsDate").val(list.rgsDate);
			$("#iUpdUserId").val(list.updUserId);
			$("#iUpdDate").val(list.updDate);

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
    	myPostData.usvcGrp = $("#usvcGrp").val();
    	myPostData.sysNm = $("#sysNm").val();
    	myPostData.useTyCd = $("#useTyCd").val();

    	$("#grid").trigger("reloadGrid");
    });
    	
   	$(".tableType1").css('height', window.innerHeight - 350);
   	$(".ui-jqgrid-bdiv").css('height', window.innerHeight - 400);
    	
});

function resetAction() {
	//alert("resetAction");

	$("#iSysCd").val("");
	$("#iSysNmKo").val("");
	$("#iSysNmEn").val("");
	$("#iDscrt").val("");
	$("#iUsvcGrpCd").get(0).selectedIndex=0;
	$("#iUseTyCd").get(0).selectedIndex=0;
	$("#sysCdBk").val("");
}

function updateAction(obj) {
	//alert('updateAction');

    var url = "<c:url value='/'/>wrks/sstm/code/sycd/update.json";
    var params = "sysCd=" + encodeURIComponent($("#iSysCd").val());
   		params += "&sysNmKo=" + encodeURIComponent($("#iSysNmKo").val());
        params += "&sysNmEn=" + encodeURIComponent($("#iSysNmEn").val());
        params += "&dscrt=" + encodeURIComponent($("#iDscrt").val());
        params += "&usvcGrpCd=" + encodeURIComponent($("#iUsvcGrpCd").val());
        params += "&useTyCd=" + $("#iUseTyCd").val();
        params += "&sysCdBk=" + $("#sysCdBk").val();

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	$("#grid").setGridParam({page :$("#cur-page").val()});
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
	if($("#iSysCd").val().trim() == "") {
		alert("시스템코드를 입력하세요.");
		return false;
	}

	return true;
}

function insertAction(obj) {
	//alert('insertAction');
	var url = "<c:url value='/'/>wrks/sstm/code/sycd/insert.json";
	var params = "sysCd=" + encodeURIComponent($("#iSysCd").val());
		params += "&sysNmKo=" + encodeURIComponent($("#iSysNmKo").val());
	    params += "&sysNmEn=" + encodeURIComponent($("#iSysNmEn").val());
	    params += "&dscrt=" + encodeURIComponent($("#iDscrt").val());
	    params += "&usvcGrpCd=" + encodeURIComponent($("#iUsvcGrpCd").val());
	    params += "&useTyCd=" + $("#iUseTyCd").val();

    $.ajaxEx($('#grid'), {
	    url : url,
	    datatype: "json",
	    data: params,
        success:function(data){

        	if(data.session == 1){
	        	$("#grid").trigger("reloadGrid");
            	alert(data.msg);

                $.hideInsertForm();

        	}else{
        		alert(data.msg);
        	}

        },
        error:function(e){
            alert(e.responseText);
        }
    });
}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = "<c:url value='/'/>wrks/sstm/code/sycd/delete.json";
    //var params = "cdGrpId=" + $("#dCdGrpId").text();
    var params = "sysCdBk=" + $("#sysCdBk").val();

    $.ajaxEx($('#grid'), {
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
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
    var url = "<c:url value='/'/>wrks/sstm/code/sycd/deleteMulti.json";
    var params = "";
	//alert(s.length);
    for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);

	    params += "&sysCdBk=" + list.sysCd;

    }
    //alert(params);

    $.ajaxEx($('#grid'), {
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
    <!-- container -->
    <div class="container">
        <!-- content -->
        <div class="contentWrap">
            <div class="content">
                <%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
                <div class="tableTypeFree seachT">
                    <table>
                        <caption>시스템코드</caption>
                        <tbody>
                        <tr>
	                        <th>U-서비스그룹</th>
	                        <td><select name="usvcGrp" id="usvcGrp" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
	                        		<option value="">전체</option>
								    <c:forEach items="${uServiceGrp}" var="val">
								        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
								    </c:forEach>
								</select>
	                        </td>
	                        <th>시스템한글명</th>
	                        <td><input type="text" name="" id="sysNm" class="txtType searchEvt" style="ime-mode:active"></td>
                            <th>사용유형</th>
                            <td><select name="" id="useTyCd" class="selectType1" maxlength="1" onchange="$('.btnS').click()">
						    		<c:forEach items="${useGrpList}" var="val">
						        		<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
						    		</c:forEach>
								</select>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

                <div class="tableType1" style="height: 500px;">
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
                <div class="tit"><h4>시스템코드 상세</h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                        <table>
                            <caption>시스템코드 상세</caption>
                            <tbody>
                            <tr>
                                <th>시스템코드</th>
                                <td id="dSysCd"></td>
                                <th>서비스그룹</th>
                                <td id="dUsvcGrpNm"></td>
                            </tr>
                            <tr>
                                <th>시스템한글명</th>
                                <td id="dSysNmKo"></td>
                                <th>시스템영문명</th>
                                <td id="dSysNmEn"></td>
                            </tr>
                            <tr>
                                <th>사용유형</th>
                                <td id="dUseTyCd" colspan="3"></td>
                            </tr>
                            <tr>
                                <th>코드설명</th>
                                <td id="dDscrt" colspan="3"></td>
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
                <div class="tit"><h4>시스템코드 <span id="modetitle">추가</span></h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                    	<input type="hidden" id="sysCdBk" />
                        <table>
                            <caption>시스템코드 등록</caption>
                            <tbody>
                            <tr>
                                <th><sup user-required="insert"><i class="fas fa-asterisk fa-xs text-danger"></i></sup>시스템코드</th>
                                <td>
                                	<input type="text" id="iSysCd" class="txtType" maxlength="10" required="required" user-required="insert" />
                                </td>
                                <th>U-서비스그룹</th>
                                <td>
									<select name="" id="iUsvcGrpCd" class="selectType1" maxlength="40">
									    <c:forEach items="${uServiceGrp}" var="val">
									        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
									    </c:forEach>
									</select>
								</td>
                            </tr>
                            <tr>
                                <th>시스템한글명</th>
                                <td><input type="text" id="iSysNmKo" class="txtType" maxlength="100" style="ime-mode:active"/></td>
                                <th>시스템영문명</th>
                                <td><input type="text" id="iSysNmEn" class="txtType" maxlength="100" style="ime-mode:inactive"/></td>
                            </tr>
                            <tr>
                                <th>사용유형</th>
								<td colspan="3">
                                	<select name="" id="iUseTyCd" class="selectType1" maxlength="1">
									    <c:forEach items="${useGrpList}" var="val">
									        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
									    </c:forEach>
									</select>
                                </td>
                            </tr>
                            <tr>
                                <th>코드설명</th>
                                <td colspan="3"><textarea class="textArea" id="iDscrt" maxlength="1000" style="ime-mode:active"></textarea></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                   <div class="btnCtr">
                        <a href="#" class="btn btnSvEc">저장</a>
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