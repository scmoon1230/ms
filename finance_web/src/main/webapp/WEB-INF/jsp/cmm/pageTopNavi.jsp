<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

        <%
        String topMenuName = "";
        String navi = "<span>Home</span>";
        String page_top		 						= request.getParameter("top");
        if(page_top == null) page_top = "";
        String page_left		 					= request.getParameter("left");
        if(page_left == null)  	page_left = "";
        String menu_id		 						= request.getParameter("m");
        if(menu_id == null)  	menu_id = "";
        String menu_nm = "";
        String menu_C = "";
        String menu_R = "";
        String menu_U = "";
        String menu_D = "";
        
        List<Map<String, String>> page_topMenuList 	= (List<Map<String, String>>) request.getAttribute("topMenuList");
        for(int i = 0; i < page_topMenuList.size(); i++) {
            Map<String, String> topMenuMap = page_topMenuList.get(i);
            if(page_top.equals(topMenuMap.get("PGM_MENU_ID"))) {
            	topMenuName = topMenuMap.get("PGM_MENU_NM_KO");
            	break;
            }
        }
        
        List<Map<String, String>> page_leftMenuList 	= (List<Map<String, String>>) request.getAttribute("leftMenuList");
        boolean find = false;
        String tmpMenuGrp = "";
        //System.out.println(page_left);
        for(int i = 0; i < page_leftMenuList.size(); i++) {
            Map<String, String> leftMenuMap = page_leftMenuList.get(i);
            
            //System.out.println(leftMenuMap);
            
            String pgmMenuId 	= leftMenuMap.get("PGM_MENU_ID");
            String lvl 			= leftMenuMap.get("LVL");
            if(find == false && lvl.equals("2")) {
        		tmpMenuGrp = "  &gt; <span>" + leftMenuMap.get("PGM_MENU_NM_KO") + "</span>";

        		if(page_left.equals("")) {
            		navi += "  &gt; <span>" + leftMenuMap.get("PGM_MENU_NM_KO") + "</span>";
            		find = true;
            		continue;
				}
            	if(page_left.equals(pgmMenuId)) {
            		navi += "  &gt; <span>" + leftMenuMap.get("PGM_MENU_NM_KO") + "</span>";
            		find = true;
            		continue;
				}
            }
           
            if(lvl.equals("3")) {
            	if(page_left.equals(pgmMenuId)) {
            		if(find == true) {
            			navi += "  &gt; <span>" + leftMenuMap.get("PGM_MENU_NM_KO") + "</span>";
            		}
            		else {
            			navi += tmpMenuGrp;
            			navi += "  &gt; <span>" + leftMenuMap.get("PGM_MENU_NM_KO") + "</span>";
            		}
            		break;
            	}
            }
        }
        
        for(int i = 0; i < page_leftMenuList.size(); i++) {
            Map<String, String> menuMap = page_leftMenuList.get(i);
            String menuId 	= menuMap.get("PGM_MENU_ID");
        	if(menu_id.equals(menuId)) {
        		menu_nm = menuMap.get("PGM_MENU_NM_KO");
        		if("Y".equals(menuMap.get("RGS_AUTH_YN"))) menu_C = "C"; 
        		if("Y".equals(menuMap.get("SEA_AUTH_YN"))) menu_R = "R"; 
        		if("Y".equals(menuMap.get("UPD_AUTH_YN"))) menu_U = "U"; 
        		if("Y".equals(menuMap.get("DEL_AUTH_YN"))) menu_D = "D"; 
                navi += "  &gt; " +  menu_nm + "[" + menu_C + menu_R + menu_U + menu_D + "]";
        	}
    	}
	    %>
                <h2 class="tit"><%=topMenuName %></h2> 
                <div class="location">
                    <%=navi %>
                </div>
	    <!--
                <h2 class="tit">시스템</h2> 
                <div class="location">
                    <span>Home</span> &gt; <span>코드관리</span> &gt; <span>시스템코드</span>
                </div>
	   -->