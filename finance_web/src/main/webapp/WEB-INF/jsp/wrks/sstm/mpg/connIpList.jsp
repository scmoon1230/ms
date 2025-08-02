<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="prprts" uri="/WEB-INF/tld/prprts.tld" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="java.net.*"%>
<%@ page import="java.io.*"%>
<%
Log log = LogFactory.getLog(getClass());
try {
	InetAddress thisIp =InetAddress.getLocalHost();
	log.debug("IP:"+thisIp.getHostAddress());
}catch(Exception e){
	e.printStackTrace();
}
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<script type="text/javascript" >
$(document).ready(function(){

	$.jqGrid($('#grid'), {
		  url: '<c:url value='/'/>wrks/sstm/mpg/connIpListData.json'
		, datatype: "json"
		, postData: {
			  connIp : $("#connIP").val()
			, connDesc : $("#connDesc").val()
		}
		, colNames: ['<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">'
					, 'No',		'접속IP(사용자PC IP)',		'접속권한레벨',		'비고',		'수정일'
		]
		, colModel: [{ name: 'CHECK', width:60, align:'center', editable:true, edittype:'checkbox'
						, editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox
					},{ name: 'rk'	     , index: 'RK'	      , width:60 , align:'center', sortable: false
					},{ name: 'connIp'   , index: 'CONN_IP'   , width:400, align:'center'
					},{ name: 'authLvl'  , index: 'AUTH_LVL'  , width:150, align:'center'
					},{ name: 'connDesc' , index: 'CONN_DESC' , width:160, align:'center'
					},{ name: 'updDate'  , index: 'UPD_DATE'  , width:150, align:'center'
					}
		  ]
		  , pager: '#pager'
		  , rowNum: $('#rowPerPageList').val()
		  , autowidth: true
		  , height: $("#grid").parent().height()-40
		  , sortname: 'CONN_IP'
		  , sortorder: 'ASC'
		  , viewrecords: true
		  , multiselect: false
		  , loadonce:false
		  , jsonReader: {
				id: "connIp",
				root: function(obj) { return obj.rows; }
			  , page: function(obj) { return 1; }
			  , total: function(obj) {
				if(obj.rows.length > 0) {
					var page  = obj.rows[0].rowcnt / rowNum;
					if( (obj.rows[0].rowcnt % rowNum) > 0)
						page++;
					return page;
				} else {
					return 1;
				}
		  }
		  , records: function(obj) { return $.showCount(obj); }
		}

		, onCellSelect: function(rowid, iCol, cellcontent, e)
		{
			if(iCol == 0)				return false;
				var list = jQuery("#grid").getRowData(rowid);
				
				$("#dConnIp").html(list.connIp);
				$("#dAuthLvl").html(list.authLvl);
				$("#dConnDesc").html(list.connDesc);
				$("#dUpdDate").html(list.updDate);

				$("#iConnIp").val(list.connIp);
				$("#iAuthLvl").val(list.authLvl);
				$("#iConnDesc").val(list.connDesc);
				$("#iUpdDate").val(list.updDate);

				$.showDetail();
			}
		, beforeRequest: function()
		{
			$.loading(true);
			rowNum = $('#rowPerPageList').val();
		}
		, beforeProcessing: function(data, status, xhr)
		{
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
			myPostData.connIp = $("#connIp").val();
			myPostData.connDesc = $("#connDesc").val();

		$("#grid").trigger("reloadGrid");
	});
	
	

	/*
	$.jqGrid($('#grid_ip'), {
		url : '<c:url value='/'/>wrks/sstm/mpg/mpgInfoListData.json',
		datatype : "json",
		autowidth : true,
		shrinkToFit : true,
		scrollOffset : 0,
		postData : {},
		colNames : [
				'','네트워크아이디', '네트워크구분', '네트워크구분', '네트워크명', '네트워크주소', '망 매핑주소'
		],
		colModel : [
				{	name : 'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
					, formatter: function (cellValue, option) { return '<input type="radio" name="radio" value="' + option.rowId + '"/>';}
				}, {name : 'networkId',		index : 'NETWORK_ID',		width : 150,	align : 'center'
				}, {name : 'networkTyNm',	index : 'NETWORK_TY_NM',	width : 150,	align : 'center'
				}, {name : 'networkTy',		index : 'NETWORK_TY',		width : 120,	align : 'center',	hidden : true
				}, {name : 'networkNm',		index : 'NETWORK_NM',		width : 120,	align : 'center'
				}, {name : 'networkIp',		index : 'NETWORK_IP',		width : 250,	align : 'center'
				}, {name : 'networkMpIp',	index : 'NETWORK_MP_IP',	width : 250,	align : 'center'
				}
		],
		pager : '#pager',
		rowNum : $('#rowPerPageList').val(),
		height: $("#grid_ip").parent().height()-40,
		sortname : 'NETWORK_ID',
		sortorder : 'ASC',
		viewrecords : true,
		loadonce : false,
		jsonReader : {},
		onCellSelect : function(rowid, iCol, cellcontent, e) {
		},
		onSelectRow : function(rowid, status, e){
			$("#grid_ip input[type=radio]").get(rowid - 1).checked = true;
		},
		beforeSelectRow : function(rowid, e) {
			var data = jQuery("#grid_ip").getRowData(rowid);
				$('#iNetworkId').val(data.networkId);
			$('#grid_ip').jqGrid('resetSelection');
			return true;
		},
		beforeRequest : function() {
		},
		beforeProcessing : function(data, status, xhr) {
		},
		loadComplete : function(data) {
			var myGrid = $("#grid_ip");
			$("#cb_"+myGrid[0].id).hide();
			oCommon.jqGrid.loadComplete(undefined, data, {});
		}
	});
	
	$(".addRpt").bind("click",function(){
		$(".layerRegisterItem").show();
		$.center(".layerRegisterItem");
		$("#grid_ip").trigger("reloadGrid");
		$("#grid_ip").setGridWidth(parseInt(document.getElementsByClassName("tableType1")[1].offsetWidth));
		});

	$(".btnDstrt").bind("click",function(){
		$(".layerRegister").show();
		$(".layerRegisterItem").hide();
	});

	$(".btnRgt").bind("click",function(){
		$(".sNetworkId").show();
	});
	
	$(".dstrtChk").bind("click",function(){
		var networkId = $("#iNetworkId").val();
		if (networkId == '') {
			alert('네트워크아이디를 선택해주세요.');
				$(".layerRegisterItem").show();
			return false;
		}
	});
	*/
});


function resetAction() {
	//alert("resetAction");
	$("#iConnIp").val("");
	$("#iAuthLvl").val("");
	$("#iConnDesc").val("");
}

function updateAction(obj) {
	//alert('updateAction');
	var url = "<c:url value='/'/>wrks/sstm/mpg/connIpList/update.json";
	var params = "connIp=" + $("#iConnIp").val();
	params += "&authLvl=" + $("#iAuthLvl").val();
	params += "&connDesc=" + $("#iConnDesc").val();

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

function validate() {
	return $.validate(".layerRegister .tableType2");
}

function ipCheck()
{
	var ip=$("#iConnIp").val();
	var reVal = true;
	if(!ip.match(/^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/)){
		alert("네트워크 IP 정보가 정확하지 않습니다.");
		reVal = false;
	}
	return reVal;
}

function insertAction(obj)
{
	if ( ipCheck())
	{
		var url = "<c:url value='/'/>wrks/sstm/mpg/connIpList/insert.json";
		var params = "&connIp=" + $("#iConnIp").val();
		params += "&authLvl=" + $("#iAuthLvl").val();
		params += "&connDesc=" + $("#iConnDesc").val();

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

	var url = "<c:url value='/'/>wrks/sstm/mpg/connIpList/delete.json";
	var params = "connIp=" + $("#iConnIp").val();

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
	var s =  $.getSelRow("#grid");		//alert(s.length);
	if(s.length == 0){
		alert("삭제할 데이터를 선택하여 주십시오.");
		return false;
	}

	if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	var url = "<c:url value='/'/>wrks/sstm/mpg/connIpList/deleteMulti.json";
	var params = "";
	for(var i = 0; i < s.length; i++) {
		var list = jQuery("#grid").getRowData(s[i]);

		params += "&connIp=" + list.connIp;
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
						<caption>접속 IP</caption>
						<tbody>
						<tr><th>접속IP</th>
							<td><input type="text" name="connIp" id="connIp" class="txtType searchEvt" style="ime-mode:active"></td>
							<th>비고</th>
							<td><input type="text" name="connDesc" id="connDesc" class="txtType searchEvt" style="ime-mode:active">
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
				<div class="tit"><h4>접속 IP</h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							<caption>기본 정보</caption>
							<tbody>
								<tr><th>접속 IP</th>				<td id="dConnIp"></td>		</tr>
								<tr><th>접속권한레벨</th>			<td id="dAuthLvl"></td>		</tr>
								<tr><th>비고</th>					<td id="dConnDesc" ></td>	</tr>
								<tr><th>수정일</th>				<td id="dUpdDate" ></td>	</tr>
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
				<div class="tit"><h4>접속 IP <span id="modetitle">추가</span></h4></div>
				<div class="layerCt">
					<div class="tableType2">
						<table>
							 <caption>기본 정보</caption>
							<tbody>
								<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>접속IP</th>
									<td><input type="text" id="iConnIp" class="txtType" maxlength="100" required="required" user-required="insert" user-title="네트워크 IP" /></td>
								</tr>
								<tr><th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>접속권한레벨</th>
									<td><input type="text" id="iAuthLvl" class="txtType" maxlength="100" user-title="접속권한레벨" />
										&nbsp; ( 1:관리, 2:승인, 신청자는 항상 접근가능 )
									</td>
									<!-- <td class="sNetworkId" width="100"><a href="#" class="btn addRpt">네트워크 아이디</a></td> -->
								</tr>
								<tr><th>비고</th>
									<td><input type="text" id="iConnDesc" class="txtType" maxlength="100" style="width:100%;"/></td>
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
			
				<!-- 
				<div class="layer layerRegisterItem">
				<div class="tit"><h4>네트워크 아이디</h4></div>
				<div class="layerCt">
					<div class="tableType1" style="height:462px;">
						<table id="grid_ip" style="width:100%">
						</table>
					</div>
					<div class="btnCtr update">
						<a href="#" class="btn btnDstrt dstrtChk">선택</a>
						<a href="#" class="btn btnDstrt">닫기</a>
					</div>
				</div>
				 -->
			
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