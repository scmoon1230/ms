<%@page import="java.util.Map" %>
<%@page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
	String page_top = request.getParameter("top");
	if (page_top == null) page_top = "";
	String page_left = request.getParameter("left");
	if (page_left == null) page_left = "";
	String menu_id = request.getParameter("m");
	if (menu_id == null) menu_id = "";
	String menu_title = "";
	String menu_C = "";
	String menu_R = "";
	String menu_U = "";
	String menu_D = "";
	String hide = "";

	Object objTitleMenuMap = request.getAttribute("titleMenuMap");
	if (objTitleMenuMap instanceof Map) {
		Map<String, Object> titleMenuMap = (Map<String, Object>) request.getAttribute("titleMenuMap");
		if (!"".equals(menu_id)) {
			if (titleMenuMap.get(menu_id) != null) menu_title = titleMenuMap.get(menu_id).toString();
		} else if (!"".equals(menu_id)) {
			if (titleMenuMap.get(page_left) != null) menu_title = titleMenuMap.get(page_left).toString();
		} else {
			if (titleMenuMap.get(page_top) != null) menu_title = titleMenuMap.get(page_top).toString();
		}
	}
	
	//if ("".equals(menu_title)) hide = " hide";
%>
<ol class="breadcrumb<%=hide%>">
	<li class="active"><%=menu_title%>
	</li>
</ol>
