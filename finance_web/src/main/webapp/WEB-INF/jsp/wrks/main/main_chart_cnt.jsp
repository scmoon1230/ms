<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStream"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
--%>
<%--Chart FX Import --%>
<%--  
<%@page import="com.softwarefx.*"%>    
<%@page import="com.softwarefx.chartfx.server.*"%>
<%@page import="com.softwarefx.chartfx.server.dataproviders.*"%>
<%@page import="com.softwarefx.chartfx.server.galleries.*"%>
<%@page import="com.softwarefx.chartfx.server.annotation.*"%>
<%@page import="com.softwarefx.chartfx.server.adornments.*"%>

<%@page import="java.awt.*"%>
<%@page import="javax.swing.*"%>
--%>
<%--Chart FX Import --%>
<%--
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
<body>
<%
	List<Map<String, String>> statsList	 	= null;
	List<Map<String, Object>> troubleGbList = (List<Map<String, Object>>)request.getAttribute("troubleGbList");
	List<String> titleArr = new ArrayList<String>();
	ListProvider lstProvider = new ListProvider();
	JsonParser jsonParser = new JsonParser();
	JsonObject json = null;
	JsonArray jsonArr = null; 
	StringBuilder stringBuilder = new StringBuilder();

	stringBuilder.append((String)request.getParameter("rows"));
	//System.out.println(stringBuilder.toString());

	try {
		if(!stringBuilder.toString().trim().equals("")) {
			json = (JsonObject)jsonParser.parse("{\"rows\":" + stringBuilder.toString() + "}");
			jsonArr = json.getAsJsonArray("rows");
			
			//System.out.println(jsonArr);
		}
	} finally {
		
	}
	// 가로 타이틀 중복 제외 
	for(int i = 0; i < troubleGbList.size(); i++) {
	}

	///System.out.println("1111");	
	
	// 데이타 소스 설정
	String[] labels = {"금일누계", "전일실적"};	
	//labels = titleArr.toArray(new String[titleArr.size()]);

	ChartServer chart3 = new ChartServer(pageContext, request, response);

	//System.out.println(troubleGbList);
	for(int i = 0, k = 0; i < troubleGbList.size(); i++) {
		int[] series = new int[labels.length];										// 데이타셋 설정
		
		//체납
		if(((String)troubleGbList.get(i).get("CD_ID")).equals("03")) {
			series[0] = Integer.parseInt(String.valueOf(((JsonObject)jsonArr.get(0)).get("UNPAID_CNT").getAsString()));
			series[1] = Integer.parseInt(String.valueOf(((JsonObject)jsonArr.get(2)).get("UNPAID_CNT").getAsString()));

			lstProvider.add(series);												// 데이타셋 추가

			titleArr.add((String)troubleGbList.get(i).get("CD_NM_KO"));
		}
		else if(((String)troubleGbList.get(i).get("CD_ID")).equals("05")) { //대포
			series[0] = Integer.parseInt(String.valueOf(((JsonObject)jsonArr.get(1)).get("UNPAID_CNT").getAsString()));
			series[1] = Integer.parseInt(String.valueOf(((JsonObject)jsonArr.get(3)).get("UNPAID_CNT").getAsString()));

			lstProvider.add(series);												// 데이타셋 추가

			titleArr.add((String)troubleGbList.get(i).get("CD_NM_KO"));
		}
	
	}

	lstProvider.add(titleArr.toArray(new String[titleArr.size()]));												// 데이타셋 추가
	
	chart3.setDataSource(lstProvider);												
	
	//chart3.getData().setSeries(1); 										// 데이타셋 수 설정 (데이타셋에 따라 자동으로 설정, 수동으로 설정 필요시 추가 )
	//chart3.getData().setPoints(3); 										// X축 Label 수 설정 (데이타셋에 따라 자동으로 설정, 수동으로 설정 필요시 추가 )
	chart3.setGallery(Gallery.BAR); 										// 그래프 타입 : BAR, PIE
	chart3.getTitles().add(new TitleDockable("영치 건수"));		//타이틀 설정
	chart3.setWidth(295);													// 폭설정
	chart3.setHeight(220);													// 높이 설정
	chart3.getLegendBox().setVisible(true);									// 범례 유무
	chart3.getLegendBox().setDock(DockArea.BOTTOM);							// 범례 위치
	chart3.getAxisY().setStep(1);											// Y축 눈금 단위 설정		
	//chart3.setFormat(CURRENCY);
	//System.out.println(troubleGbList);
	for(int i = 0; i < labels.length; i++) {
		chart3.getSeries().get(i).setText((String)labels[i]);								//범례 설정
	}
	
	// Chart 그리기
	chart3.renderControl();
%>
<script>
parent.loadChartAmt();
</script>
</body>
</html>
--%>