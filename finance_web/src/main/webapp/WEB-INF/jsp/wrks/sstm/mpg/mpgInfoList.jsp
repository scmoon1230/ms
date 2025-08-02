<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript">
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		  url: '<c:url value='/'/>wrks/sstm/mpg/mpgInfoListData.json'
		, datatype: "json"
		, postData: {
			  networkNm : $("#networkNm").val()
			, networkTy : $("#networkTy").val()
			, dstrtCd : $("#sDstrtCd").val()
		}
		, colNames: ['<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, 'No', '네트워크구분', 'networkTy', '네트워크아이디', '네트워크명', '네트워크주소', '망매핑주소', '지구', 'dstrtCd'
		]
		, colModel: [{ name: 'CHECK'                                , width:60 , align:'center', sortable: false
						, editable:true    , edittype:'checkbox', editoptions: { value:"True:False" }, formatter: $.GridCheckBox
					},{ name: 'rk',          index: 'RK',            width:60,  align:'center', sortable: false, hidden: true
					},{ name: 'networkTyNm', index: 'NETWORK_TY_NM', width:120, align:'center'
					},{ name: 'networkTy',   index: 'NETWORK_TY',    width:120, align:'center', hidden: true
					},{ name: 'networkId',   index: 'NETWORK_ID',    width:120, align:'center'
					},{ name: 'networkNm',   index: 'NETWORK_NM',    width:120, align:'center'
					},{ name: 'networkIp',   index: 'NETWORK_IP',    width:250, align:'center'
					},{ name: 'networkMpIp', index: 'NETWORK_MP_IP', width:250, align:'center'
					},{ name: 'dstrtNm',     index: 'DSTRT_NM',      width:100, align:'center'
					},{ name: 'dstrtCd',                                                         hidden: true
					},
		  ]
		
		  , pager: '#pager'
		  , rowNum: $('#rowPerPageList').val()
		  , autowidth: true
		  , height: $("#grid").parent().height()-40
		  , sortname: 'NETWORK_NM'
		  , sortorder: 'ASC'
		  , viewrecords: true
		  , multiselect: false
		  , loadonce:false
		  , jsonReader: {
				id: "networkId",
				root: function(obj) { return obj.rows; }
			  , page: function(obj) { return 1; }
			  , total: function(obj) {
					if(obj.rows.length > 0) {
						var page  = obj.rows[0].rowcnt / rowNum;
						if( (obj.rows[0].rowcnt % rowNum) > 0)
							page++;
						return page;
					} else
						return 1;
			  }
			  , records: function(obj) { return $.showCount(obj); }
			 }
		, onCellSelect: function(rowid, iCol, cellcontent, e) {
			if(iCol == 0)		return false;
				var list = jQuery("#grid").getRowData(rowid);
				
				$("#dNetworkId").html(list.networkId);
				$("#dDstrtNm").html(list.dstrtNm);
				$("#dNetworkTyNm").html(list.networkTyNm);
				$("#dNetworkNm").html(list.networkNm);
				$("#dNetworkIp").html(list.networkIp);
				$("#dNetworkMpIp").html(list.networkMpIp);

	            $.selectBarun("#iNetworkTy", list.networkTy);
	            
				$("#iNetworkId").val(list.networkId);
	            $('#iNetworkId').prop('readonly', true);
				//$("#iNetworkTy").val(list.networkTy);
				$("#iNetworkNm").val(list.networkNm);
				$("#iNetworkIp").val(list.networkIp);
				$("#iNetworkMpIp").val(list.networkMpIp);

	            $.selectBarun("#iDstrtCd", list.dstrtCd);
	            $('#iDstrtCd').prop('disabled', true);
	            
				$.showDetail();
			}
		, beforeRequest: function() {
			$.loading(true);
			rowNum = $('#rowPerPageList').val();
		}
		, beforeProcessing: function(data, status, xhr) {
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
		myPostData.networkNm = $("#networkNm").val();
		myPostData.networkTy = $("#networkTy").val();
		myPostData.dstrtCd   = $("#sDstrtCd").val();

		$("#grid").trigger("reloadGrid");
	});
});

function resetAction() {
	//alert("resetAction");
    $.selectBarun("#iNetworkTy", '');

	$("#iNetworkId").val("");
	//$("#iNetworkTy").val("");
	$("#iNetworkNm").val("");
	$("#iNetworkIp").val("");
	$("#iNetworkMpIp").val("");

    $.selectBarun("#iDstrtCd", '');
    $('#iDstrtCd').prop('disabled', false);
}

function fn_trim_space() {
	if (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 39 || event.keyCode == 46) return;
    event.target.value = event.target.value.replaceAll(' ', '');
}

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function updateAction(obj) {
	//alert('updateAction');
	var url = "<c:url value='/'/>wrks/sstm/mpg/mpgInfoList/update.json";
	var params = "networkId=" + $("#iNetworkId").val();
		//params += "&networkTy=" + $("#iNetworkTy").val();
		params += "&networkTy=" + $('#iNetworkTy option:selected').val();
		params += "&networkNm=" + $("#iNetworkNm").val();
		params += "&networkIp=" + $("#iNetworkIp").val();
		params += "&networkMpIp=" + $("#iNetworkMpIp").val();
		params += "&dstrtCd=" + $('#iDstrtCd option:selected').val(); 

	$.ajaxEx($('#grid'), {
		type : "POST",
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

function ipCheck()
{
	var ip = $("#iNetworkIp").val();
	var mIp = $("#iNetworkMpIp").val();
	var reVal = true;
	if ( !ip.match(/^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/) ) {
		alert("네트워크주소 정보가 정확하지 않습니다.");
		reVal = false;
	} else if ( !mIp.match(/^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/) ) {
		alert("매핑 네트워크주소 정보가 정확하지 않습니다.");
		reVal = false;
	}
	return reVal;
}

function insertAction(obj)
{
	//if ( ipCheck() )
	{
		var url = "<c:url value='/'/>wrks/sstm/mpg/mpgInfoList/insert.json";
		var params = "networkId=" + $("#iNetworkId").val();
			//params += "&networkTy=" + $("#iNetworkTy").val();
			params += "&networkTy=" + $('#iNetworkTy option:selected').val();
			params += "&networkNm=" + $("#iNetworkNm").val();
			params += "&networkIp=" + $("#iNetworkIp").val();
			params += "&networkMpIp=" + $("#iNetworkMpIp").val();
			params += "&dstrtCd=" + $('#iDstrtCd option:selected').val();
			//alert(params);

		$.ajaxEx($('#grid'), {
			type : "POST"
			, url : url
			, datatype: "json"
			, data: params
			, success:function(data)
			{
				if(data.session == 1){
					$("#grid").trigger("reloadGrid");
					alert(data.msg);
					$.hideInsertForm();
				} else {
					alert(data.msg);
				}
			}
			, error:function(e){
				alert(e.responseText);
			}
		});
	}
}

function deleteAction(obj) {
	//alert('deleteAction');

	var url = "<c:url value='/'/>wrks/sstm/mpg/mpgInfoList/delete.json";
	var params = "networkId=" + $("#iNetworkId").val();
	params += "&dstrtCd=" + $('#iDstrtCd option:selected').val(); 

	$.ajaxEx($('#grid'), {
		type : "POST",
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

	//var s =  $("#grid").jqGrid('getGridParam', 'selarrrow');
	//s = $("grid").getGridParam('selarrrow');
	var s =  $.getSelRow("#grid");
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/mpg/mpginfoList/deleteMulti.json";
	var params = "";
	//alert(s.length);
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);

		params += "&networkId=" + list.networkId + '&dstrtCd=' + list.dstrtCd;
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
	<!-- container -->
	<div class="container">
		<!-- content -->
		<div class="contentWrap">
			<div class="content">
				<%@include file="/WEB-INF/jsp/cmm/pageTopNavi2.jsp" %>
				<div class="tableTypeFree seachT">
					<table>
						<caption>망연계 기본 매핑정보</caption>
						<tbody>
						<tr>
							<th>네트워크구분</th>
							<td><select name="" id="networkTy" class="selectType1" >
									<option value="">전체</option>
									<c:forEach items="${networkTy}" var="val">
										<option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<th>네트워크명</th>
							<td><input type="text" name="networkNm" id="networkNm" class="txtType searchEvt" style="ime-mode:active">
							</td>
							<th>지구</th>
							<td><select name="" id="sDstrtCd" class="selectType1">
									<option value="">전체</option>
								    <c:forEach items="${dstrtList}" var="val">
								        <option value="${val.dstrtCd}"><c:out value="${val.dstrtNm}" ></c:out></option>
								    </c:forEach>
								</select>
								<a href="javascript:;" class="btn btnRight btnS searchBtn">검색</a>
	                        </td>
						</tr>
						</tbody>
					</table>
				</div>

					<c:import url="/WEB-INF/jsp/cmm/rowPerPageList.jsp"/>

                <div class="tableType1" style="height: 445px;">
					<table id="grid" style="width:100%">
					</table>
				</div>
				<div class="paginate">
				</div>
				<div class="btnWrap btnR">
					<a href="#" class="btn btnDt btnRgt">등록</a>
					<a href="#" class="btn btnMultiDe">선택삭제</a>
				</div>
			</div>
			 <!-- 레이어팝업 상세 -->

			<div class="layer layerDetail" id="div_drag_1">
				<div class="tit"><h4>망연계 기본매핑 정보 상세</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>망연계 기본매핑 정보</caption>
							<tbody>
								<tr><th>네트워크아이디</th>	<td id="dNetworkId" ></td>
									<th>지구</th>			<td id="dDstrtNm"></td>
								</tr>
								<tr><th>네트워크명</th>	<td id="dNetworkNm"></td>
									<th>네트워크구분</th>	<td id="dNetworkTyNm" ></td>
								</tr>
								<tr><th>네트워크주소</th>	<td id="dNetworkIp" colspan="3"></td>
								</tr>
								<tr><th>망패핑주소</th>	<td id="dNetworkMpIp" colspan="3"></td>
								</tr>
							</tbody>
						</table>
					</div>
					</br>
					<div class="btnCtr">
						<a href="#" class="btn btnMd">수정</a>
						<a href="#" class="btn btnDe">삭제</a>
						<a href="#" class="btn btnC">취소</a>
					</div>
				</div>
			</div>

			<!-- // 레이어팝업 상세 -->

			<!-- 레이어팝업 등록 -->

			<div class="layer layerRegister" id="div_drag_2">
				<div class="tit"><h4>망연계 기본매핑 정보 <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>망연계 기본매핑 정보(추가)</caption>
							<tbody>
							<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>네트워크아이디</th>
								<td><input type="text" id="iNetworkId" class="txtType" maxlength="100" required="required" user-required="insert" user-title="네트워크아이디"   /></td>
                                <th>지구</th>
                                <td><select class="selectType1" id="iDstrtCd" name="iDstrtCd">
                                        <c:forEach items="${dstrtList}" var="val">
                                            <option value="${val.dstrtCd}"><c:out value="${val.dstrtNm}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
							</tr>
							<tr><th>네트워크명</th>
								<td ><input type="text" id="iNetworkNm" class="txtType" maxlength="100" style="ime-mode:active" /></td>
								<th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>네트워크구분</th>
                                <td><select class="selectType1" id="iNetworkTy" name="iNetworkTy">
                                        <c:forEach items="${networkTy}" var="val">
                                            <option value="${val.cdId}"><c:out value="${val.cdNmKo}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
							</tr>
							<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>네트워크주소</th>
								<td colspan="3"><input type="text" id="iNetworkIp" class="txtType" maxlength="100" required="required" user-title="네트워크주소" style="width: 100%;ime-mode:disabled" /></td>
							</tr>
							<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>망패핑주소</th>
								<td colspan="3"><input type="text" id="iNetworkMpIp" class="txtType" maxlength="100" required="required" user-title="망간연계IP" style="width: 100%;"/></td>
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