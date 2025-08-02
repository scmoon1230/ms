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
	        url: '<c:url value='/'/>wrks/cop/rss/list.json',
	        datatype: "json",
	        postData: {
	        	rssTitle : $("#sRssTitle").val(),
	        	rssRcvTitle : $("#sRssRcvTitle").val()
	        },
	        colNames: [
							'<input type="checkbox" name="hcheckbox" onchange="$.GridHeaderCheckBoxChange(\'#grid\', this, event);">',
							'SEQ_NO',
							'RSS 아이디',
	                        'RSS명',
	                        '제목',
	                        '일시',
	                        '내용',
	                        'RSS_RCV_LINK',
	                        'RSS_RCV_SMR',
	                        'RSS_RCV_AUTHOR'
	                   ],
	        colModel: [
	                    
	                    
					{ name: 'CHECK', width:50, align:'center', editable:true, edittype:'checkbox', editoptions: { value:"True:False" }, sortable: false, formatter: $.GridCheckBox},
					{ name: 'SEQ_NO', width:0, align:'center', hidden:true},
					{ name: 'RSS_ID', width:0, align:'center', hidden:true},
					{ name: 'RSS_TITLE', width:290, align:'center'},
					{ name: 'RSS_RCV_TITLE', width:450, align:'center'},
					{ name: 'RSS_RCV_DATE', width:200, align:'center'},
					{ name: 'RSS_RCV_DATA', width:0, align:'center', hidden:true},
					{ name: 'RSS_RCV_LINK', width:0, align:'center', hidden:true},
					{ name: 'RSS_RCV_SMR', width:0, align:'center', hidden:true},
					{ name: 'RSS_RCV_AUTHOR', width:0, align:'center', hidden:true}
					
					
	          ],
	        pager: '#pager',
	        rowNum: $('#rowPerPageList').val(),
	        sortname: 'RSS_RCV_DATE',
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
				
				$("#dSeqNo").val(list.SEQ_NO);	//제목
				$("#dRssRcvTitle").html(list.RSS_RCV_TITLE);	//제목
				$("#dRssRcvDate").html(list.RSS_RCV_DATE);		//일시
	
				var strContent = "";
	
				 
				if(list.RSS_RCV_LINK.toUpperCase().indexOf("HTTP") >= 0){
					//strContent = "<br>- 링크 : <a href='javascript:$.openMenuCenter(\"" + list.RSS_RCV_LINK + "\",\"" + list.RSS_RCV_TITLE + "\", 1024, 800)'>" + list.RSS_RCV_LINK + "</a>";
					strContent = "<br>- 링크 : <a href='javascript:$.openMenuCenter(\"" + list.RSS_RCV_LINK + "\",\"RSS View\", 1024, 800)'>" + list.RSS_RCV_LINK + "</a>";
				}else{
					strContent = "<br>- 링크 : " + list.RSS_RCV_LINK; 
				}
				
				
				strContent = strContent +  "<br>- 작성자 : " + list.RSS_RCV_AUTHOR; 	
				strContent = strContent +  "<br>- 작성일자 : " + list.RSS_RCV_DATE; 	
				strContent = strContent +  "<br>- 요약내용 : " + list.RSS_RCV_SMR + "....."; 
		
				if(list.RSS_RCV_LINK.toUpperCase().indexOf("HTTP") >= 0){
					strContent = strContent +  "<a href='javascript:$.openMenuCenter(\"" + list.RSS_RCV_LINK + "\",\"RSS View\", 1024, 800)'>더보기</a>";
				}
				
				$("#dRssRcvData").html(strContent);		
				
				/* 
				var strContentF = list.RSS_RCV_DATA;
				strContentF = strContentF.replace("&lt;", "<");
				strContentF = strContentF.replace("&gt;", ">");
				strContentF = strContentF.replace("&quot;", "\"");
				strContentF = strContentF.replace("&amp;nbsp;", "&nbsp;");		
				
				$("#dRssRcvDataF").html(strContentF);
				 */
				
	
				
	
				
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
	    	myPostData.rssTitle = $("#sRssTitle").val();
	    	myPostData.rssRcvTitle = $("#sRssRcvTitle").val();
	    	
	    	$("#grid").trigger("reloadGrid");
	    });
	});
	
	function deleteAction(obj) {
	
		var url = "<c:url value='/'/>wrks/cop/rss/delete.json";  
	    var params = "seqNo=" + $("#dSeqNo").val();
	
	    $.ajaxEx($('#grid'), {
		    url : url,
		    datatype: "json",
		    data: params,
	        success:function(data){
	
	        	$("#grid").trigger("reloadGrid");
	        	alert(data.msg);
	        },   
	        error:function(e){  
	        	alert(data.msg);
	        }  
	    });
	}
	
	function deleteMultiAction(obj) {
	
		var s =  $.getSelRow("#grid");
		if(s.length == 0){
			alert("삭제할 데이터를 선택하여 주십시오.");
			return false;
		}
	
		if(confirm("선택된 자료를 삭제하시겠습니까?") == false) return false;
	    var url = "<c:url value='/'/>wrks/cop/rss/deleteMulti.json";  
	    var params = "";  
	
	    for(var i = 0; i < s.length; i++) {
			var list = jQuery("#grid").getRowData(s[i]);
			
		    params += "&seqNo=" + list.SEQ_NO;
	    }
	
	
	    $.ajaxEx($('#grid'), {
		    url : url,
		    datatype: "json",
		    data: params,
	        success:function(data){
	
	        	$("#grid").trigger("reloadGrid");
	
	        	alert(data.msg);
	        },   
	        error:function(e){  
	
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
                    <h3 class="tit">RSS정보</h3>
                </div>
                <div class="tableTypeHalf seachT">
                    <table>
                        <caption>RSS정보</caption>
                        <tbody>
                        <tr>
 							<th>RSS명</th>
	                        <td><input type="text" name="" id="sRssTitle" class="txtType txtType100 searchEvt" style="ime-mode:active"></td>
	                        <th>제목/내용</th>
	                        <td><input type="text" name="" id="sRssRcvTitle" class="txtType txtType70 searchEvt" style="ime-mode:active">
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
                    <a href="#" class="btn btnMultiDe">삭제</a>
                </div>                
            </div>
            
            <!-- 레이어팝업 상세 -->
            <div class="layer layerDetail" id="div_drag_1">
            	<input type="hidden" id="dSeqNo" />
                <div class="tit"><h4>RSS 상세내용</h4></div>
                <div class="layerCt">
                    <div class="tableType2">
                        <table>
                            <caption>RSS 상세내용</caption>
                            <tbody>
                            <tr>
                                <th>제목</th>
                                <td id="dRssRcvTitle" colspan="3"></td>
                            </tr>
                            <tr>
                            	<th>일시</th>
                                <td id="dRssRcvDate" colspan="3"></td>
                            </tr>
                            <tr>
                            	<th colspan="4" style="text-align:center">내용</th>
                            </tr>
                            <tr>
                                <!-- <td colspan="3"><textarea class="textArea" id="dRssRcvData" maxlength="4000" style="ime-mode:active" readonly></textarea></td></td> -->
                            	<td colspan="3"><div id="dRssRcvData" style="width:100%;height:200px; overflow-y:scroll; overflow-x:hidden" ></div></td></td>
                            	
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="btnCtr">
                        <a href="#" class="btn btnDe">삭제</a>
                        <a href="#" class="btn btnC">닫기</a>
                    </div>
                </div>
            </div>
            <!-- //레이어팝업 상세 -->
            
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
