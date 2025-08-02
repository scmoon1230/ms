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
<META HTTP-EQUIV="contentType" CONTENT="text/html;charset=UTF-8">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><prprts:value key="HD_TIT" /></title>

<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/bootstrap/v3/css/bootstrap-select.min.css' />">
<script src="<c:url value='/js/bootstrap/v3/js/bootstrap-select.min.js' />"></script>
<script type="text/javascript">
$(document).ready(function(){

	var grid_param = {
	        url : "<c:url value='/'/>wrks/sstm/menu/menu/list.json",
	        datatype: "json",
	        postData: {},
	        colNames: [ '프로그램메뉴 아이디'	, 'U-서비스그룹코드'		, '시스템코드'					, '프로그램 아이디'
		        	, '메뉴 목록'          , '메뉴제목'              , '프로그램메뉴 영문명'		, '부모프로그램메뉴 아이디'
		        	, '정렬순서'			, '정렬순서2'				, '프로그램메뉴 설명'			, '프로그램메뉴 가시화여부'
	        		, '사용유형코드'		, '프로그램메뉴 ON 이미지경로'	, '프로그램메뉴 OFF 이미지경로'	, '프로그램메뉴 이미지경로'
					, '등록자'			, '등록일'				, '수정자'					, '수정일'
					, 'LVL'				, 'ISLF'				, 'PGM_URL'		, '새창 여부'	, '새창 넓이'	, '새창 높이'
	        ],
	        colModel: [ { name:'pgmMenuId'     , index:'PGM_MENU_ID'         , width:200, align:'center', hidden:true, key:true}
					, { name:'usvcGrpCd'       , index:'USVC_GRP_CD'         , width:300, align:'center', hidden:true}
					, { name:'sysCd'           , index:'SYS_CD'              , width:300, align:'center', hidden:true}
					, { name:'pgmId'           , index:'PGM_ID'              , width:300, align:'center', hidden:true}
					, { name:'pgmMenuTit'      , index:'PGM_MENU_TIT'        , width:300, align:'left'  , sortable: false}
					, { name:'pgmMenuNmKo'     , index:'PGM_MENU_NM_KO'      , width:100, align:'center', hidden:true}
					, { name:'pgmMenuNmEn'     , index:'PGM_MENU_NM_EN'      , width:100, align:'center', hidden:true}
					, { name:'prntPgmMenuId'   , index:'PRNT_PGM_MENU_ID'    , width:125, align:'center', hidden:true}
					, { name:'sortOrdr'        , index:'SORT_ORDR'           , width:125, align:'center', hidden:true}
					, { name:'sortOrdr2'       , index:'SORT_ORDR2'          , width:125, align:'center', hidden:true}
					, { name:'pgmMenuDscrt'    , index:'PGM_MENU_DSCRT'      , width:200, align:'center', hidden:true}
					, { name:'pgmMenuVisibleYn', index:'PGM_MENU_VISIBLE_YN' , width:100, align:'center', hidden:true}
					, { name:'useTyCd'         , index:'USE_TY_CD'           , width:300, align:'center', hidden:true}
					, { name:'pgmMenuOnImgCrs' , index:'PGM_MENU_ON_IMG_CRS' , width:300, align:'center', hidden:true}
					, { name:'pgmMenuOffImgCrs', index:'PGM_MENU_OFF_IMG_CRS', width:300, align:'center', hidden:true}
					, { name:'pgmMenuImgCrs'   , index:'PGM_MENU_IMG_CRS'    , width:100, align:'center', hidden:true}
					, { name:'rgsUserId'       , index:'RGS_USER_ID'         , width:125, align:'center', hidden:true}
					, { name:'rgsDate'         , index:'RGS_DATE'            , width:125, align:'center', hidden:true}
					, { name:'updUserId'       , index:'UPD_USER_ID'         , width:200, align:'center', hidden:true}
					, { name:'updDate'         , index:'UPD_DATE'            , width:100, align:'center', hidden:true}
					, { name:'lvl'             , index:'LVL'                 , width:300, hidden:true}
					, { name:'islf'            , index:'ISLF'                , width:300, hidden:true}
					, { name:'pgmUrl'          , index:'PGM_URL'             , width:300, hidden:true}
					, { name:'newWdwYn'        , index:'NEW_WDW_YN'          , width:300, hidden:true}
					, { name:'newWinWidth'     , index:'NEW_WIN_WIDTH'       , width:300, hidden:true}
					, { name:'newWinHeight'    , index:'NEW_WIN_HEIGHT'      , width:300, hidden:true}

	        ],
	        treeGrid: true,
	        treeGridModel: "adjacency",
	        caption: "",
	        ExpandColumn: "pgmMenuTit",
			tree_root_level:0,
	        rowNum: 10000,
	        autowidth: true,
			height: $("#grid").parent().height()-40,
	        ExpandColClick: true,
	        treeIcons: {leaf:'ui-icon-document-b'},
			treeReader:{
	            leaf_field :'islf', //확장 화살표 여부(true:확장,false:최하단 레벨 이므로 확장 안함)
	            level_field: 'lvl',        //---level값
	            parent_id_field : 'prntPgmMenuId',   //---부모id값
				expanded_field: 'expd' //열린상태로 로딩할지 여부
			},
	        onSelectRow: function(rowid, status, e) {
				var list = jQuery("#grid").getRowData(rowid);

				$("#dPgmMenuId").val(list.pgmMenuId);
				$("#bPgmMenuId").val(list.pgmMenuId);
				$.selectBarun("#dUsvcGrpCd", list.usvcGrpCd);
				$.selectBarun("#dSyscd", list.sysCd);
				$("#dPgmId").val(list.pgmId);
				$("#dPgmURL").text(list.pgmUrl);
				$("#dPgmMenuNmKo").val(list.pgmMenuNmKo);
				$("#dPgmMenuNmEn").val(list.pgmMenuNmEn);
				$("#dPrntPgmMenuId").val(list.prntPgmMenuId);
				$("#dSortOrdr").val(list.sortOrdr);
				$("#dSortOrdr2").val(list.sortOrdr2);
				$("#dPgmMenuDscrt").val(list.pgmMenuDscrt);
				$.selectBarun("#dPgmMenuVisibleYn", list.pgmMenuVisibleYn);
				$.selectBarun("#dUseTyCd", list.useTyCd);

				var fileNm = list.pgmMenuOnImgCrs;

				$("#onImgCrs > img").remove();
				if(fileNm != ""){
					$("#onImgCrs").prepend('<img src="<c:url value='/'/>images/menu/'+ fileNm + '\" style=\'width: 30px; height: 30px\'/>');
				}
				$("#dPgmMenuOnImgCrs").val(fileNm);

				fileNm = list.pgmMenuOffImgCrs;

				$("#offImgCrs > img").remove();
				if(fileNm != ""){
					$("#offImgCrs").prepend('<img src="<c:url value='/'/>images/menu/'+ fileNm + '\" style=\'width: 30px; height: 30px\'/>');
				}
				$("#dPgmMenuOffImgCrs").val(fileNm);

				fileNm = list.pgmMenuImgCrs;
				/*
				$("#menuImgCrs > img").remove();
				if(fileNm != ""){
					$("#menuImgCrs > i").remove();
					$("#menuImgCrs").prepend('<img src="<c:url value='/'/>images/menu/'+ fileNm + '\" style=\'width: 30px; height: 30px\'/>');
				}
				$("#dPgmMenuImgCrs").val(fileNm);
				*/

				// $("#menuImgCrs > i").remove();
				if(fileNm != ""){
				//	$("#menuImgCrs").prepend('<i class="' + fileNm + '"></i>');
					$("#dPgmMenuImgCrs").selectpicker('val', fileNm);
				}
				else {
					$("#dPgmMenuImgCrs").selectpicker('val', '');
				}

				var lvl = list.LVL;
				if(lvl != '2') {
					$('tr.lvl-2-only').hide();
				}
				else {
					$('tr.lvl-2-only').show();
				}

				$("#dRgsUserId").html(list.rgsUserId);
				$("#dRgsDate").html(list.rgsDate);
				$("#dUpdUserId").html(list.updUserId);
				$("#dUpdDate").html(list.updDate);

				$.selectBarun("#dNewWdwYn", list.newWdwYn);
				$("#dNewWinWidth").val(list.newWinWidth);
				$("#dNewWinHeight").val(list.newWinHeight);

				$("#bPrntPgmMenuId").val(list.prntPgmMenuId);
				$("#iPrntPgmMenuId").html(list.pgmId);
				$("#iPgmMenuId").val("");

				$("#dPgmMenuOnImgCrsFile").val("");
				$("#dPgmMenuOffImgCrsFile").val("");
				$("#dPgmMenuImgCrsFile").val("");

				$("#dPgmMenuOnImgCrsFile").attr("user-data-bak", list.pgmMenuOnImgCrs);
				$("#dPgmMenuOffImgCrsFile").attr("user-data-bak", list.pgmMenuOffImgCrs);
				$("#dPgmMenuImgCrsFile").attr("user-data-bak", list.pgmMenuImgCrs);

				$('input:radio[name="parentsChild"]').attr("disabled", false);
	        },
	        beforeRequest: function() {
	        	$.loading(true);
	        	rowNum = $('#rowPerPageList').val();
	        },
	        beforeProcessing: function(data, status, xhr){
	        	if(typeof data.rows != "undefine" || data.row != null) {
	        		$.makePager("#grid", data, $("#grid").getGridParam('page'), $('#rowPerPageList').val());
	        	}
	        },
	        loadComplete : function() { /* 데이터 로딩이 끝난후 호출할 함수*/
	        	//$.bindTreeExpand("#grid", grid_param);
       			$.loading(false);
       			
       			
	        }
		};

	$.jqGrid($('#grid'), grid_param);

	var grid_param = {
	        url : "<c:url value='/'/>wrks/sstm/menu/menu/list_prgm.json",
	        datatype: "json",
	        postData: {},
	        colNames: [ '' , 'No' , '프로그램아이디' , '프로그램' , '아이디' , 'URL' , 'LVL' , 'ISLF' , 'PRNT_PGM_ID'
	        ],
	        colModel: [ { name:'CHECK', width:50, align:'center', editable:true, edittype:'radio', editoptions: { value:"True:False" }, sortable: false
							, formatter: function (cellValue, option) {
								return '<input type="radio" name="radio" value="' + option.rowId + '"/>';
							}
						}
					, { name:'rk'       , index:'RK'         , width:50 , align:'center', sortable: false, hidden:true}
					, { name:'pgmId'    , index:'PGM_ID'     , width:100, align:'center', hidden:true, key:true}
					, { name:'pgmNm'    , index:'PGM_NM'     , width:300, align:'left', sortable: false}
					, { name:'pgmId'    , index:'PGM_ID'     , width:100, align:'center'}
					, { name:'pgmUrl'   , index:'PGM_URL'    , width:300, align:'center', sortable: false}
					, { name:'lvl'      , index:'LVL'        , width:300, hidden:true}
					, { name:'islf'     , index:'ISLF'       , width:300, hidden:true}
					, { name:'prntPgmId', index:'PRNT_PGM_ID', width:300, hidden:true}
	        ],
	        treeGrid: true,
	        treeGridModel: "adjacency",
	        caption: "",
	        ExpandColumn: "pgmNm",
			tree_root_level:0,
	        rowNum: 10000,
	        autowidth: true,
			height: $("#grid_prgm").parent().height()-40,
	        ExpandColClick: true,
	        treeIcons: {leaf:'ui-icon-document-b'},
			treeReader:{
	            leaf_field :'islf', //확장 화살표 여부(true:확장,false:최하단 레벨 이므로 확장 안함)
	            level_field: 'lvl',        //---level값
	            parent_id_field : 'prntPgmId',   //---부모id값
				expanded_field: 'expd' //열린상태로 로딩할지 여부
			}
	        ,
	        onSelectRow: function(rowid, status, e) {
				var list = jQuery("#grid_prgm").getRowData(rowid);
	        	$("#grid_prgm input[type=radio]").get(list.RK-1).checked = true;
	        },
	        loadComplete: function(){
	        	$("#grid_prgm input[type=radio]").change(function(){
	        		$("#grid_prgm").jqGrid('setSelection',$(this).val(),true);
	        	});
	        }
	};

	$.jqGrid($('#grid_prgm'), grid_param);

    //등록저장버튼
    $(".btnSvLocal").bind("click",function(){
       	try{
           	if(validate() == false)
           		return false;
       	}catch(e) {}

    	var params = "";
	    params += "&usvcGrpCd=" + encodeURIComponent($("#iUsvcGrpCd").val());
	    params += "&syscd=" + encodeURIComponent($("#iSyscd").val());
	    params += "&pgmId=" + encodeURIComponent($("#iPgmId").val());
	    params += "&pgmMenuNmKo=" + escape(encodeURIComponent($("#iPgmMenuNmKo").val()));
	    params += "&pgmMenuNmEn=" + escape(encodeURIComponent($("#iPgmMenuNmEn").val()));
		params += "&sortOrdr=" + encodeURIComponent($("#iSortOrdr").val());
		params += "&sortOrdr2=" + encodeURIComponent($("#iSortOrdr2").val());
	    params += "&pgmMenuDscrt=" + escape(encodeURIComponent($("#iPgmMenuDscrt").val()));
	    params += "&pgmMenuVisibleYn=" + encodeURIComponent($("#iPgmMenuVisibleYn").val());
	    params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
	    // params += "&pgmMenuOnImgCrs=" + encodeURIComponent($("#iPgmMenuOnImgCrs").val());
	    params += "&pgmMenuOnImgCrs=" + encodeURIComponent("");
	    // params += "&pgmMenuOffImgCrs=" + encodeURIComponent($("#iPgmMenuOffImgCrs").val());
	    params += "&pgmMenuOffImgCrs=" + encodeURIComponent("");
	    params += "&pgmMenuImgCrs=" + encodeURIComponent($("#iPgmMenuImgCrs option:selected").val());
	    params += "&newWdwYn="+ encodeURIComponent($("#iNewWdwYn").val());
	    params += "&newWinWidth="+ encodeURIComponent($("#iNewWinWidth").val());
	    params += "&newWinHeight="+ encodeURIComponent($("#iNewWinHeight").val());

	    if($('input:radio[name="parentsChild"]:checked').val() == "p")
	    	params += "&prntPgmMenuId=";
	    else
	    	params += "&prntPgmMenuId=" + encodeURIComponent($("#dPgmMenuId").val());
	    try{
    		if($.multiFileUpload2(".layerRegister", "<c:url value='/'/>wrks/sstm/menu/menu/image.upload", params, function()
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

    //수정저장버튼
	$(".btnModi").bind("click",function(){
		if($.validate(".tableTypeFree") == false) return false;

	    var params = "pgmMenuId=" + encodeURIComponent($("#bPgmMenuId").val());
        params += "&usvcGrpCd=" + encodeURIComponent($("#dUsvcGrpCd").val());
        params += "&syscd=" + encodeURIComponent($("#dSyscd").val());
        params += "&pgmId=" + encodeURIComponent($("#dPgmId").val());
        params += "&pgmMenuNmKo=" + escape(encodeURIComponent($("#dPgmMenuNmKo").val()));
        params += "&pgmMenuNmEn=" + escape(encodeURIComponent($("#dPgmMenuNmEn").val()));
        params += "&prntPgmMenuId=" + encodeURIComponent($("#dPrntPgmMenuId").val());
    	params += "&sortOrdr=" + encodeURIComponent($("#dSortOrdr").val());
    	params += "&sortOrdr2=" + encodeURIComponent($("#dSortOrdr2").val());
        params += "&pgmMenuDscrt=" + escape(encodeURIComponent($("#dPgmMenuDscrt").val()));
        params += "&pgmMenuVisibleYn=" + encodeURIComponent($("#dPgmMenuVisibleYn").val());
        params += "&useTyCd=" + encodeURIComponent($("#dUseTyCd").val());
        // params += "&pgmMenuOnImgCrs=" + encodeURIComponent($("#dPgmMenuOnImgCrs").val());
        params += "&pgmMenuOnImgCrs=" + encodeURIComponent("");
        // params += "&pgmMenuOffImgCrs=" + encodeURIComponent($("#dPgmMenuOffImgCrs").val());
        params += "&pgmMenuOffImgCrs=" + encodeURIComponent("");
        params += "&pgmMenuImgCrs=" + encodeURIComponent($("#dPgmMenuImgCrs option:selected").val());
        params += "&newWdwYn=" + $("#dNewWdwYn").val();
	    params += "&newWinWidth="+ encodeURIComponent($("#dNewWinWidth").val());
	    params += "&newWinHeight="+ encodeURIComponent($("#dNewWinHeight").val());

        if($.multiFileUpload2(".tableTypeFree", "<c:url value='/'/>wrks/sstm/menu/menu/image.upload", params
        		, function() {
        			/* var pgmMenuId = $.getCurrentRowValue("#grid", "pgmMenuId");
        			var list = jQuery("#grid").getRowData(pgmMenuId);

        			var fileNm = list.PGM_MENU_ON_IMG_CRS;

        			$("#onImgCrs > img").remove();
    				if(fileNm != ""){
    					$("#onImgCrs").prepend('<img src="<c:url value='/'/>cmm/getImage.image?fileNm='+ fileNm + '\" style=\'width: 40px; height: 40px\'/>');
    					alert($("#onImgCrs > img").attr("src"));
    				}
    				$("#dPgmMenuOnImgCrs").val(fileNm);
    				location.reload(true); */
    				$("#grid").trigger("reloadGrid");
  				}) == false) {
			return false;
		}
    });

	if($("#bPrntPgmMenuId").val() == null || $("#bPrntPgmMenuId").val() == ""){
		$('input:radio[name="parentsChild"]').attr("disabled", true);
	}

    //프로그램팝업
    $(".btnPrgm").bind("click",function(){
		$("#grid_prgm").jqGrid('setGridParam', {url: "<c:url value='/'/>wrks/sstm/menu/menu/list_prgm.json"});
    	$("#grid_prgm").trigger("reloadGrid");

        $(".layerPrgm").show();
        $("#maskPrgm").remove();
        $("body").append("<div class='maskPop' id='maskPrgm'></div>");
        $.center(".layerPrgm");
    });

    //레이어 저장버튼
    $(".layer .btnPrgmSv").bind("click",function(){
    	var s =  $.getSelRowRadio("#grid_prgm");
    	//alert("s="+s+"[s.length="+s.length+"]");
    	if(s.length == 0){
    		alert("프로그램을 선택하세요.");
    		return false;
    	}
    	var list = jQuery("#grid_prgm").getRowData(s[0]);

    	var pgmId = list.pgmId;
    	var pgmUrl = list.pgmUrl;
    	var pgmNm = list.pgmNm;
    	var prntPgmId = list.prntPgmId;
    	var sortOrdr;

    	if(pgmId.length>=9){
    		var str = pgmId.substr(0, pgmId.length-1);
    		var str1 = pgmId.substr(pgmId.length-1, pgmId.length);
    		var sortInt = parseInt(str1)+1;
    		sortOrdr = pgmId.substr(0, pgmId.length-1) + String(sortInt);
    	}

		//alert("pgmId="+list.pgmId +"[sortOrdr= "+ sortOrdr+"]");
		/*
    	var pgmId = $.getCurrentRowValue("#grid_prgm", "pgmId");
    	var pgmUrl = $.getCurrentRowValue("#grid_prgm", "pgmUrl");
    	var pgmNm = $.getCurrentRowValue("#grid_prgm", "pgmNm");
		 */

    	if($(".layerRegister").css("display") == "none") {
    		$("#dPgmId").val(pgmId);
	    	$("#dPgmURL").text(pgmUrl);

	    	if (pgmId != '0' && pgmId != '9998' && pgmId != '9999') {
	    			$("#dPgmMenuNmKo").val(pgmNm);
	    	}
    	}
    	else {
	    	$("#iPgmId").val(pgmId);
	    	$("#iPgmURL").text(pgmUrl);
	    	$("#iSortOrdr").val(sortOrdr);
	    	$("#iSortOrdr2").val(sortOrdr);

	    	if (pgmId != '0' && pgmId != '9998' && pgmId != '9999') {
    			$("#iPgmMenuNmKo").val(pgmNm);
    	    }
    	}
    	$("#maskPrgm").remove();
        $(".layerPrgm").hide();
        return false;
    });
    //레이어 취소버튼
    $(".layer .btnPrgmC").bind("click",function(){
        $("#maskPrgm").remove();
        $(".layerPrgm").hide();
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

    $("#dUsvcGrpCd").bind("change", function(){
    	var url = "<c:url value='/'/>wrks/sstm/code/cmcd/sysList.json";
        var params = "usvcGrpCd=" + $("#dUsvcGrpCd").val();

        $.ajax({
        	type : "POST",
    	    url : url,
    	    datatype: "json",
    	    data: params,
            success:function(data){
            	//타켓 select, 전체여부(Y/N), code컬럼명, nm컬럼명, rows
				$.selectAdd("#dSyscd", 'N', "sysCd", "sysNm", data.sysInfo);
            },
            error:function(e){
                alert(e.responseText);
            }
        });
    });


    $("#iUsvcGrpCd").bind("change", function(){
    	var url = "<c:url value='/'/>wrks/sstm/code/cmcd/sysList.json";
        var params = "usvcGrpCd=" + $("#iUsvcGrpCd").val();

        $.ajax({
        	type : "POST",
    	    url : url,
    	    datatype: "json",
    	    data: params,
            success:function(data){
            	//타켓 select, 전체여부(Y/N), code컬럼명, nm컬럼명, rows
				$.selectAdd("#iSyscd", 'N', "sysCd", "sysNm", data.sysInfo);
				$("#iSysCd").get(0).selectedIndex = 0;
            },
            error:function(e){
                alert(e.responseText);
            }
        });
    });

    var oFontAwesome = [
    	''
    	, 'fas fa-address-book'
    	, 'far fa-address-book'
    	, 'fas fa-address-card'
    	, 'far fa-address-card'
    	, 'fas fa-ambulance'
    	, 'fas fa-anchor'
    	, 'fab fa-android'
    	, 'fab fa-apple'
    	, 'fas fa-asterisk'
    	, 'fas fa-ban'
    	, 'fas fa-bed'
    	, 'fas fa-beer'
    	, 'fas fa-bezier-curve'
    	, 'fab fa-bluetooth'
    	, 'fab fa-bluetooth-b'
    	, 'fas fa-bookmark'
    	, 'far fa-bookmark'
    	, 'fas fa-briefcase'
    	, 'fas fa-briefcase-medical'
    	, 'fas fa-broadcast-tower'
    	, 'fas fa-building'
    	, 'far fa-building'
    	, 'fas fa-bullhorn'
    	, 'fas fa-burn'
    	, 'fas fa-bus'
    	, 'fas fa-bus-alt'
    	, 'fas fa-business-time'
    	, 'fas fa-calculator'
    	, 'fas fa-calendar'
    	, 'far fa-calendar'
    	, 'fas fa-calendar-alt'
    	, 'far fa-calendar-alt'
    	, 'fas fa-calendar-check'
    	, 'far fa-calendar-check'
    	, 'fas fa-calendar-minus'
    	, 'far fa-calendar-minus'
    	, 'fas fa-calendar-plus'
    	, 'far fa-calendar-plus'
    	, 'fas fa-calendar-times'
    	, 'far fa-calendar-times'
    	, 'fas fa-camera'
    	, 'fas fa-camera-retro'
    	, 'fas fa-capsules'
    	, 'fas fa-car'
    	, 'fas fa-car-alt'
    	, 'fas fa-car-crash'
    	, 'fas fa-chart-area'
    	, 'fas fa-chart-bar'
    	, 'far fa-chart-bar'
    	, 'fas fa-chart-line'
    	, 'fas fa-chart-pie'
    	, 'fas fa-child'
    	, 'fas fa-city'
    	, 'fas fa-clipboard'
    	, 'far fa-clipboard'
    	, 'fas fa-clipboard-check'
    	, 'fas fa-clipboard-list'
    	, 'fas fa-clock'
    	, 'far fa-clock'
    	, 'fas fa-cloud'
    	, 'fas fa-cloud-download-alt'
    	, 'fas fa-cloud-meatball'
    	, 'fas fa-cloud-moon'
    	, 'fas fa-cloud-moon-rain'
    	, 'fas fa-cloud-rain'
    	, 'fas fa-cloud-showers-heavy'
    	, 'fas fa-cloud-sun'
    	, 'fas fa-cloud-sun-rain'
    	, 'fas fa-cloud-upload-alt'
    	, 'fas fa-code'
    	, 'fas fa-code-branch'
    	, 'fas fa-cog'
    	, 'fas fa-cogs'
    	, 'fas fa-coins'
    	, 'fas fa-columns'
    	, 'fas fa-comment'
    	, 'far fa-comment'
    	, 'fas fa-comment-alt'
    	, 'far fa-comment-alt'
    	, 'fas fa-comment-dollar'
    	, 'fas fa-comment-dots'
    	, 'far fa-comment-dots'
    	, 'fas fa-comment-slash'
    	, 'fas fa-comments'
    	, 'far fa-comments'
    	, 'fas fa-comments-dollar'
    	, 'fas fa-compass'
    	, 'far fa-compass'
    	, 'fas fa-compress'
    	, 'fas fa-copy'
    	, 'far fa-copy'
    	, 'fas fa-database'
    	, 'fas fa-edit'
    	, 'far fa-edit'
    	, 'fas fa-envelope'
    	, 'far fa-envelope'
    	, 'fas fa-envelope-open'
    	, 'far fa-envelope-open'
    	, 'fas fa-envelope-open-text'
    	, 'fas fa-envelope-square'
    	, 'fas fa-eye'
    	, 'far fa-eye'
    	, 'fas fa-eye-slash'
    	, 'far fa-eye-slash'
    	, 'fas fa-female'
    	, 'fas fa-file'
    	, 'far fa-file'
    	, 'fas fa-file-alt'
    	, 'far fa-file-alt'
    	, 'fas fa-file-archive'
    	, 'far fa-file-archive'
    	, 'fas fa-file-audio'
    	, 'far fa-file-audio'
    	, 'fas fa-file-code'
    	, 'far fa-file-code'
    	, 'fas fa-file-contract'
    	, 'fas fa-file-csv'
    	, 'fas fa-file-download'
    	, 'fas fa-file-excel'
    	, 'far fa-file-excel'
    	, 'fas fa-file-export'
    	, 'fas fa-file-image'
    	, 'far fa-file-image'
    	, 'fas fa-file-import'
    	, 'fas fa-file-invoice'
    	, 'fas fa-file-invoice-dollar'
    	, 'fas fa-file-medical'
    	, 'fas fa-file-medical-alt'
    	, 'fas fa-file-pdf'
    	, 'far fa-file-pdf'
    	, 'fas fa-file-powerpoint'
    	, 'far fa-file-powerpoint'
    	, 'fas fa-file-prescription'
    	, 'fas fa-file-signature'
    	, 'fas fa-file-upload'
    	, 'fas fa-file-video'
    	, 'far fa-file-video'
    	, 'fas fa-file-word'
    	, 'far fa-file-word'
    	, 'fas fa-first-aid'
    	, 'fas fa-globe'
    	, 'fas fa-home'
    	, 'fas fa-hospital'
    	, 'far fa-hospital'
    	, 'fas fa-hospital-alt'
    	, 'fas fa-hotel'
    	, 'fab fa-hotjar'
    	, 'fas fa-house-damage'
    	, 'fab fa-houzz'
    	, 'fas fa-id-badge'
    	, 'far fa-id-badge'
    	, 'fas fa-id-card'
    	, 'far fa-id-card'
    	, 'fas fa-id-card-alt'
    	, 'fas fa-industry'
    	, 'fas fa-infinity'
    	, 'fas fa-info'
    	, 'fas fa-info-circle'
    	, 'fas fa-landmark'
    	, 'fas fa-laptop'
    	, 'fas fa-laptop-code'
    	, 'fas fa-link'
    	, 'fas fa-male'
    	, 'fas fa-map'
    	, 'far fa-map'
    	, 'fas fa-map-marked'
    	, 'fas fa-map-marked-alt'
    	, 'fas fa-map-marker'
    	, 'fas fa-map-marker-alt'
    	, 'fas fa-mountain'
    	, 'fas fa-network-wired'
    	, 'fas fa-ship'
    	, 'fas fa-sitemap'
    	, 'fas fa-skull'
    	, 'fas fa-skull-crossbones'
    	, 'fas fa-subway'
    	, 'fas fa-taxi'
    	, 'fas fa-traffic-light'
    	, 'fas fa-train'
    	, 'fas fa-truck'
    	, 'fas fa-university'
    	, 'fas fa-video'
    	, 'fas fa-video-slash'
    	, 'fab fa-whmcs'
	];
	var $selectType = $('<select/>', {
		'class' : 'form-control selectpicker'
	});

	$.each(oFontAwesome, function(i, v) {
		if(i != 0) {
			var $option = $('<option/>', {
				'data-content' : '<i class="' + v + '"></i> ' + v,
				'value' : v
			});
			$option.addClass('fa-lg');
			$selectType.append($option);
		}
		else {
			var $option = $('<option/>', {
				'data-content' : '선택된 이미지가 없습니다',
				'value' : ''
			});
			$selectType.append($option);
		}
	});

	$('#container-pgm-menu-img-crs-i').append($selectType.clone().attr('id', 'iPgmMenuImgCrs'));
	$('#container-pgm-menu-img-crs-d').append($selectType.clone().attr('id', 'dPgmMenuImgCrs'));
});

	function resetAction() {
		//alert("resetAction");

		$("#iPgmMenuId").val("");
		$("#iUsvcGrpCd").get(0).selectedIndex = 0;
		$("#iSyscd").get(0).selectedIndex = 0;
		$("#iPgmId").val("");
		$("#iPgmMenuNmKo").val("");
		$("#iPgmMenuNmEn").val("");
		$("#iPrntPgmMenuId").html($("#dPgmMenuId").val());
		$("#iSortOrdr").val("");
		$("#iSortOrdr2").val("");
		$("#iPgmMenuDscrt").val("");
		$("#iPgmMenuVisibleYn").get(0).selectedIndex = 0;
		$("#iUseTyCd").get(0).selectedIndex = 0;
		$("#iNewWdwYn").get(0).selectedIndex = 1;
		$("#iNewWinWidth").val("");
		$("#iNewWinHeight").val("");
		$("#iPgmMenuOnImgCrs").val("");
		$("#iPgmMenuOffImgCrs").val("");
		$("#iPgmMenuImgCrs").val("");
		$("#iPgmMenuOnImgCrsFile").val("");
		$("#iPgmMenuOffImgCrsFile").val("");
		$("#iPgmMenuImgCrsFile").val("");
	}

	function updateAction(obj) {
		//alert('updateAction');

		var url = "<c:url value='/'/>wrks/sstm/menu/menu/update.json";
		var params = "pgmMenuId=" + $("#dPgmMenuId").val();
		var params = "bPgmMenuId=" + $("#bPgmMenuId").val();
		params += "&usvcGrpCd=" + $("#dUsvcGrpCd").val();
		params += "&syscd=" + $("#dSyscd").val();
		params += "&pgmId=" + $("#dPgmId").val();
		params += "&pgmMenuNmKo=" + $("#dPgmMenuNmKo").val();
		params += "&pgmMenuNmEn=" + $("#dPgmMenuNmEn").val();
		params += "&prntPgmMenuId=" + $("#dPrntPgmMenuId").val();
		params += "&sortOrdr=" + $("#dSortOrdr").val();
		params += "&sortOrdr2=" + $("#dSortOrdr2").val();
		params += "&pgmMenuDscrt=" + $("#dPgmMenuDscrt").val();
		params += "&pgmMenuVisibleYn=" + $("#dPgmMenuVisibleYn").val();
		params += "&useTyCd=" + $("#dUseTyCd").val();
		// params += "&pgmMenuOnImgCrs=" + $("#dPgmMenuOnImgCrs").val();
		params += "&pgmMenuOnImgCrs=" + "";
		// params += "&pgmMenuOffImgCrs=" + $("#dPgmMenuOffImgCrs").val();
		params += "&pgmMenuOffImgCrs=" + "";
		params += "&pgmMenuImgCrs=" + $("#dPgmMenuImgCrs option:selected").val();

		$.ajaxEx($('#grid'), {
			url : url,
			datatype: "json",
			data : params,
			success : function(data) {

				$("#grid").trigger("reloadGrid");
				//alert("저장하였습니다.");
				alert(data.msg);
			},
			error : function(e) {
				alert(e.responseText);
			}
		});
	}

	function validate() {
		return $.validate(".layerRegister .tableType2");
	}

	function insertAction(obj) {
//		alert('insertAction');

		var url = "<c:url value='/'/>wrks/sstm/menu/menu/insert.json";
		var params = "pgmMenuId=" + $("#iPgmMenuId").val();
		params += "&usvcGrpCd=" + $("#iUsvcGrpCd").val();
		params += "&syscd=" + $("#iSyscd").val();
		params += "&pgmId=" + $("#iPgmId").val();
		params += "&pgmMenuNmKo=" + $("#iPgmMenuNmKo").val();
		params += "&pgmMenuNmEn=" + $("#iPgmMenuNmEn").val();
		params += "&sortOrdr=" + $("#iSortOrdr").val();
		params += "&sortOrdr2=" + $("#iSortOrdr2").val();
		params += "&pgmMenuDscrt=" + $("#iPgmMenuDscrt").val();
		params += "&pgmMenuVisibleYn=" + $("#iPgmMenuVisibleYn").val();
		params += "&useTyCd=" + $("#iUseTyCd").val();
		// params += "&pgmMenuOnImgCrs=" + $("#iPgmMenuOnImgCrs").val();
		params += "&pgmMenuOnImgCrs=" + "";
		// params += "&pgmMenuOffImgCrs=" + $("#iPgmMenuOffImgCrs").val();
		params += "&pgmMenuOffImgCrs=" + "";
		params += "&pgmMenuImgCrs=" + $("#iPgmMenuImgCrs option:selected").val();

		if ($('input:radio[name="parentsChild"]:checked').val() == "p") params += "&prntPgmMenuId=";
		else params += "&prntPgmMenuId=" + $("#dPgmMenuId").val();
		//params += "&divLevel=" + $('input:radio[name="parentsChild"]:checked').val();

		$.ajaxEx($('#grid'), {
			url : url,
			datatype: "json",
			data : params,
			success : function(data) {

				$("#grid").trigger("reloadGrid");
				//alert("저장하였습니다.");
				alert(data.msg);
			},
			error : function(e) {
				alert(e.responseText);
			}
		});
	}

	function deleteAction(obj) {
		//alert('deleteAction');
		if ($.getCurrentRowValue("#grid", "islf") != "true") {
			alert("최하위 메뉴부터 삭제할 수 있습니다.");
			return false;
		}

		var url = "<c:url value='/'/>wrks/sstm/menu/menu/delete.json";
		//var params = "cdGrpId=" + $("#dCdGrpId").text();
		var params = "pgmMenuId=" + $("#dPgmMenuId").val();

		$.ajaxEx($('#grid'), {
			url : url,
			datatype: "json",
			data : params,
			success : function(data) {

				$("#grid").trigger("reloadGrid");
				//alert("삭제하였습니다.");
				alert(data.msg);
			},
			error : function(e) {
				//alert(e.responseText);
				alert(data.msg);
			}
		});
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
                <div class="tbLeft30">
	                <div class="tableType1" style="height:700px;">
	    				<table id="grid" style="width:100%">
	    				</table>
	                </div>
                </div>
                <div class="tbRight80">
                <!-- 레이어팝업 상세 -->
		            <div>
		                <div class="layerCt">
		                    <div class="tableTypeFree">
		                    	<input type="hidden" id="bPgmMenuId"/>
		                        <table>
		                            <caption>프로그램정보 상세</caption>
		                            <tbody>
		                            <tr>
		                                <th colspan="4" style="text-align:center">선택된 메뉴</th>
		                            </tr>
		                            <tr>
		                                <th>아이디</th>
		                                <td><input type="text" class="txtType100" id="dPgmMenuId" maxlength="40" readonly="readonly" required="required" user-title="프로그램메뉴아이디"/></td>
		                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>U-서비스그룹코드</th>
		                                <td><select name="" id="dUsvcGrpCd" class="selectType1" maxlength="1">
												<c:forEach items="${uServiceGrp}" var="val">
											        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
											    </c:forEach>
										    </select>
										</td>
		                            </tr>
		                            <tr>
		                                <th>부모메뉴 아이디</th>
		                                <td><input type="text" class="txtType100" id="dPrntPgmMenuId" maxlength="40" /></td>
		                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>시스템코드</th>
		                                <td><select name="" id="dSyscd" class="selectType1" maxlength="1">
			                                	<c:forEach items="${sysNmList}" var="val">
											        <option value="${val.sysCd}"><c:out value="${val.sysNmKo}" ></c:out></option>
											    </c:forEach>
											 </select>
		                                </td>
		                            </tr>
		                            <tr>
		                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>아이디</th>
		                                <td colspan="3"><input type="text" class="txtType70" id="dPgmId" readonly="readonly" maxlength="40" required="required" user-title="프로그램아이디"/> <a href="#" class="btn btnPrgm">불러오기</a></td>
		                            </tr>
		                            <tr>
		                                <th>URL</th>
		                                <td colspan="3" id="dPgmURL"></td>
		                            </tr>
		                            <tr>
		                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>한글명</th>
		                                <td><input type="text" class="txtType100" id="dPgmMenuNmKo" maxlength="100" required="required" user-title="프로그램메뉴 한글명" style="ime-mode:active"/></td>
		                                <th>영문명</th>
		                                <td><input type="text" class="txtType100" id="dPgmMenuNmEn" maxlength="100" style="ime-mode:inactive"/></td>
		                            </tr>
		                            <tr>
		                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>정렬순서</th>
		                                <td><input type="text" class="txtType number" id="dSortOrdr" maxlength="12" required="required" user-data-min="1" user-title="정렬순서"/></td>
		                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용유형코드</th>
		                                <td><select name="" id="dUseTyCd" class="selectType1" maxlength="1">
			                                	<%-- <c:forEach items="${useGrpList}" var="val">
											        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
											    </c:forEach> --%>
										        <option value="Y">사용</option>
										        <option value="N">미사용</option>
										    </select>
		                                </td>
		                            </tr>
		                            <tr>
		                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>정렬순서2</th>
		                                <td><input type="text" class="txtType number" id="dSortOrdr2" maxlength="12" required="required" user-data-min="1" user-title="정렬순서"/></td>
		                            </tr>
		                            <tr>
		                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>가시화 여부</th>
		                                <td><select name="" id="dPgmMenuVisibleYn" class="selectType1" maxlength="1">
										        <option value="Y">Y</option>
										        <option value="N">N</option>
											</select>
		                                </td>
		                                <th>새창 여부</th>
		                                <td><select name="" id="dNewWdwYn" class="selectType1" maxlength="1">
										        <option value="Y">Y</option>
										        <option value="N"  selected>N</option>
											</select>
		                                </td>
		                            </tr>
		                            <tr>
		                                <th>새창 넓이</th>
		                                <td><input type="text" class="txtType number" id="dNewWinWidth" maxlength="5"  user-title="새창넓이"/></td>
		                                <th>새창 높이</th>
		                                <td><input type="text" class="txtType number" id="dNewWinHeight" maxlength="5" user-title="새창높이"/></td>
		                            </tr>
									<!--
		                            <tr>
		                                <th>ON 이미지경로</th>
		                                <td colspan="3" id="onImgCrs">
		                                	<input type="text" class="txtType70" id="dPgmMenuOnImgCrs" maxlength="255" readonly="readonly" user-title="ON이미지경로"/>
		                                	<input type="file" id="dPgmMenuOnImgCrsFile" class="txtType upload" style="display:none" user-ref-id="dPgmMenuOnImgCrs" user-ext=".png" user-col-name="PGM_MENU_ON_IMG_CRS" />
		                                	<a href="#" class="btn btnFileObject" user-ref-id="dPgmMenuOnImgCrsFile">파일찾기</a>
		                                </td>
		                            </tr>
		                            <tr>
		                                <th>OFF 이미지경로</th>
		                                <td colspan="3" id="offImgCrs">
		                                	<input type="text" class="txtType70" id="dPgmMenuOffImgCrs" maxlength="255" readonly="readonly" user-title="OFF이미지경로"/>
		                                	<input type="file" id="dPgmMenuOffImgCrsFile" class="txtType upload" style="display:none" user-ref-id="dPgmMenuOffImgCrs" user-ext=".png" user-col-name="PGM_MENU_OFF_IMG_CRS"/>
		                                	<a href="#" class="btn btnFileObject" user-ref-id="dPgmMenuOffImgCrsFile">파일찾기</a>
		                                </td>
		                            </tr>
		                            -->
		                            <tr class="lvl-2-only">
		                                <th>이미지선택</th>
		                                <td colspan="3">
		                                	<div class="row">
		                                		<div id="container-pgm-menu-img-crs-d" class="col-xs-6"></div>
		                                	</div>
		                                	<!-- <input type="text" class="txtType70" id="dPgmMenuImgCrs" maxlength="255" readonly="readonly" user-title="이미지경로"/> -->
		                                	<!-- <input type="file" id="dPgmMenuImgCrsFile" class="txtType upload" style="display:none" user-ref-id="dPgmMenuImgCrs" user-ext=".png" user-col-name="PGM_MENU_IMG_CRS"/> -->
		                                	<!-- <a href="#" class="btn btnFileObject" user-ref-id="dPgmMenuImgCrsFile">파일찾기</a> -->
		                                </td>
		                            </tr>
		                            <!--
		                            <tr>
		                                <th>등록자</th>
		                                <td id="dPgmDscrt"></td>
		                                <th>등록일</th>
		                                <td id="dPgmDscrt"></td>
		                            </tr>
		                            <tr>
		                                <th>수정자</th>
		                                <td id="dUpdUserId"></td>
		                                <th>수정일</th>
		                                <td id="dUpdDate"></td>
		                            </tr>
		                             -->
		                            <tr>
	                                <th>설명</th>
	                                <td colspan="3"><textarea class="textArea" id="dPgmMenuDscrt" maxlength="1000" style="ime-mode:active"></textarea></td>
	                            </tr>
		                            </tbody>
		                        </table>
		                    </div>
		                    <br/>
		                    <div class="btnCtr">
		                    	<a href="#" class="btn btnDt btnRgt">등록</a>
		                        <a href="#" class="btn btnModi">저장</a>
		                        <a href="#" class="btn btnDe">삭제</a>
		                    </div>
		                </div>
		            </div>
		        </div>
		            <!-- //레이어팝업 상세 -->
            </div>

            <!-- 레이어팝업 등록 -->
            <div class="layer layerRegister" id="div_drag_1">
                <div class="tit"><h4>프로그램정보 <span id="modetitle">추가</span></h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                    	<input type="hidden" id="bPgmMenuId" />
                    	<input type="hidden" id="bPrntPgmMenuId" />
                        <table>
                            <caption>프로그램정보 등록</caption>
                            <tbody>
                            <tr>
                                <th> 프로그램메뉴아이디</th>
                                <td>*자동생성*
                                	<!-- <input type="text" class="txtType" id="iPgmMenuId" maxlength="40" required="required" user-title="프로그램메뉴아이디"/>-->
                                </td>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>U-서비스그룹코드</th>
                                <td><select name="" id="iUsvcGrpCd" class="selectType1" maxlength="1">
										<c:forEach items="${uServiceGrp}" var="val">
									        <option value="${val.cdId}"><c:out value="${val.cdNmKo}" ></c:out></option>
									    </c:forEach>
								    </select>
								</td>
                            </tr>
                            <tr>
                                <th>부모프로그램메뉴 아이디</th>
                                <td id="iPrntPgmMenuId"></td>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>시스템코드</th>
                                <td><select name="" id="iSyscd" class="selectType1" maxlength="1">
	                                	<c:forEach items="${sysNmList}" var="val">
									        <option value="${val.sysCd}"><c:out value="${val.sysNmKo}" ></c:out></option>
									    </c:forEach>
									 </select>
                                </td>
                            </tr>
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>프로그램아이디</th>
                                <td colspan="3"><input type="text" class="txtType70" id="iPgmId" readonly="readonly" maxlength="40" required="required" user-title="프로그램아이디"/> <a href="#" class="btn btnPrgm">불러오기</a></td>
                            </tr>
                            <tr>
                                <th> 프로그램URL</th>
                                <td colspan="3" id="iPgmURL"></td>
                            </tr>
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>프로그램메뉴 한글명</th>
                                <td><input type="text" class="txtType" id="iPgmMenuNmKo" maxlength="100" required="required" user-title="프로그램메뉴 한글명" style="ime-mode:active"/></td>
                                <th>프로그램메뉴 영문명</th>
                                <td><input type="text" class="txtType" id="iPgmMenuNmEn" maxlength="100" style="ime-mode:inactive"/></td>
                            </tr>
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>정렬순서</th>
                                <td><input type="text" class="txtType number" id="iSortOrdr" maxlength="12" required="required" user-data-min="1" user-title="정렬순서"/></td>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>사용유형코드</th>
                                <td><select name="" id="iUseTyCd" class="selectType1" maxlength="1">
	                                	<%-- <c:forEach items="${useGrpList}" var="val">
									        <option value="${val.CD_ID}"><c:out value="${val.CD_NM_KO}" ></c:out></option>
									    </c:forEach> --%>
								        <option value="Y">사용</option>
								        <option value="N">미사용</option>
								    </select>
                                </td>
                            </tr>
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>정렬순서2</th>
                                <td><input type="text" class="txtType number" id="iSortOrdr2" maxlength="12" required="required" user-data-min="1" user-title="정렬순서"/></td>
                            </tr>
                            <tr>
                                <th><sup><i class="fas fa-asterisk fa-xs text-danger"></i></sup>가시화 여부</th>
                                <td><select name="" id="iPgmMenuVisibleYn" class="selectType1" maxlength="1">
								        <option value="Y">Y</option>
								        <option value="N">N</option>
									</select>
                                </td>
                                <th>새창 여부</th>
                                <td><select name="" id="iNewWdwYn" class="selectType1" maxlength="1">
								        <option value="Y">Y</option>
								        <option value="N">N</option>
									</select>
                                </td>
                            </tr>
                            <tr>
                                <th>새창 넓이</th>
                                <td><input type="text" class="txtType number" id="iNewWinWidth" maxlength="5"  user-title="새창넓이"/></td>
                                <th>새창 높이</th>
                                <td><input type="text" class="txtType number" id="iNewWinHeight" maxlength="5" user-title="새창높이"/></td>
                            </tr>
                            <!--
                            <tr>
                                <th>ON 이미지경로</th>
                                <td colspan="3">
                                	<input type="text" class="txtType70" id="iPgmMenuOnImgCrs" maxlength="255" readonly="readonly" user-title="ON이미지경로"/>
                                	<input type="file" id="iPgmMenuOnImgCrsFile" class="txtType upload" style="display:none" user-title="ON이미지경로" user-ref-id="iPgmMenuOnImgCrs" user-ext=".png" user-col-name="PGM_MENU_ON_IMG_CRS" />
                                	<a href="#" class="btn btnFileObject" user-ref-id="iPgmMenuOnImgCrsFile">파일찾기</a>
                                </td>
                            </tr>
                            <tr>
                                <th>OFF 이미지경로</th>
                                <td colspan="3">
                                	<input type="text" class="txtType70" id="iPgmMenuOffImgCrs" maxlength="255" readonly="readonly"  user-title="OFF이미지경로"/>
                                	<input type="file" id="iPgmMenuOffImgCrsFile" class="txtType upload" style="display:none" user-title="OFF이미지경로" user-ref-id="iPgmMenuOffImgCrs" user-ext=".png" user-col-name="PGM_MENU_OFF_IMG_CRS" />
                                	<a href="#" class="btn btnFileObject" user-ref-id="iPgmMenuOffImgCrsFile">파일찾기</a>
                                </td>
                            </tr>
                            -->
                            <tr class="lvl-2-only">
                                <th>이미지선택</th>
                                <td colspan="3">
                                	<div class="row">
                                		<div id="container-pgm-menu-img-crs-i" class="col-xs-6"></div>
                                	</div>
                                	<!-- <input type="text" class="txtType70" id="iPgmMenuImgCrs" maxlength="255" readonly="readonly" user-title="이미지경로"/> -->
                                	<!-- <input type="file" id="iPgmMenuImgCrsFile" class="txtType upload" style="display:none" user-title="이미지경로" user-ref-id="iPgmMenuImgCrs" user-ext=".png" user-col-name="PGM_MENU_IMG_CRS" /> -->
                                	<!-- <a href="#" class="btn btnFileObject" user-ref-id="iPgmMenuImgCrsFile">파일찾기</a> -->
                                </td>
                            </tr>
                            <tr>
                                <th>프로그램메뉴 설명</th>
                                <td colspan="3"><textarea class="textArea" id="iPgmMenuDscrt" maxlength="1000" style="ime-mode:active"></textarea></td>
                            </tr>
                            <tr>
                            	<th>추가구분</th>
                            	<td colspan="3">
	                            	최상위그룹 : <input type="radio" id="p" name="parentsChild" value="p"/> &nbsp;&nbsp;&nbsp;
	                            	하위그룹 : <input type="radio" id="c" name="parentsChild" checked="true"  value="c"/>
	                            </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                   <div class="btnCtr">
                        <a href="#" class="btn btnSvLocal">저장</a>
                        <a href="#" class="btn btnC">취소</a>
                    </div>
                </div>
            </div>
            <div class="layer layerPrgm" style="z-index:100">
                <div class="tit"><h4>프로그램 선택</h4></div>
                <div class="layerCt">
	                <div class="tableType1" style="height:300px;">
	    				<table id="grid_prgm" style="width:100%">
	    				</table>
	                </div>
                    <div class="btnCtr">
                        <a href="#" class="btn btnPrgmSv">저장</a>
                        <a href="#" class="btn btnPrgmC">취소</a>
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
