<%@ page language="java" contentType="application/vnd.ms-excel; name='excel', text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<!doctype html>
<html>
<head>
<meta content="text/html; charset=utf-8">
<style>
body, p, h1, h2, h3, h4, h5, h6, ul, ol, li, dl, dt, dd, table, th, td,
	form, fieldset, legend, input, textarea, button, select, img, pre {
	margin: 0px;
	padding: 0px
}

body, input, textarea, select, table {
	font-family: Tahoma, gulim;
	font-size: 12px;
	color: #666666
}

table {
	border-collapse: collapse;
	empty-cells: show
}

.a_left {
	text-align: left !important;
}

#header {
	position: relative;
	height: 30px; repeat-x bottom;
	padding-left: 20px
}

#container {
	margin-bottom: 40px;
}

#container.scrollbar {
	padding: 0;
	overflow-x: hidden;
	overflow-y: scroll;
	height: 614px;
	background: #fff;
	scrollbar-arrow-color: #c0c0c0;
	scrollbar-face-color: #efefef;
	scrollbar-track-color: #FFFFFF;
	scrollbar-3dlight-color: #FFFFFF;
	scrollbar-highlight-color: #efefef;
	scrollbar-shadow-color: #FFFFFF;
	scrollbar-darkshadow-color: #FFFFFF;
}

#container.scrollbar_s {
	padding: 0;
	overflow-x: hidden;
	overflow-y: scroll;
	height: 517px;
	background: #fff;
	scrollbar-arrow-color: #c0c0c0;
	scrollbar-face-color: #efefef;
	scrollbar-track-color: #FFFFFF;
	scrollbar-3dlight-color: #FFFFFF;
	scrollbar-highlight-color: #efefef;
	scrollbar-shadow-color: #FFFFFF;
	scrollbar-darkshadow-color: #FFFFFF;
}

#content {
	padding: 20px
}

.spot {
	line-height: 150%;
	margin-bottom: 10px
}

.txt_small {
	font-family: dotum;
	font-size: 11px
}

.orange {
	color: #ff7301
}

.data_list {
	width: 400px
}

.data_list thead th {
	border: 1px solid #c8dbe0;
	background-color: #f3f7f8;
	color: #397b9a;
	padding: 5px;
	line-height: 150%;
	font-size: 11px;
	font-family: dotum
}

.data_list thead th.left {
	border: 1px solid #c8dbe0;
	background-color: #f3f7f8;
	color: #397b9a;
	padding: 5px;
	line-height: 150%;
	font-size: 11px;
	font-family: dotum;
	text-align: left
}

.data_list thead th.sub {
	border: 1px solid #c8dbe0;
	background-color: #fafcfd;
	color: #5f99b4;
	font-family: dotum;
	font-size: 11px;
	line-height: 13px
}

.data_list tbody th {
	border: 1px solid #c8dbe0;
	padding: 5px;
	line-height: 150%;
	color: #000;
	background-color: #fafcfd;
	font-weight: normal
}

.data_list td {
	border: 1px solid #e9e9e9;
	border-top: 0px;
	padding: 5px;
	line-height: 150%;
	text-align: center
}

.data_list td.left {
	border: 1px solid #e9e9e9;
	border-top: 0px;
	padding: 5px;
	line-height: 150%;
	text-align: left
}

.data_list td div {
	margin-bottom: 5px
}

.data_list tr.offense td {
	background-color: #FFC;
	color: #F00
}
</style>
</head>
<body>
	<%
		String getXlsFileName = request.getParameter("xlsfilename");
		String getXlsTitle = request.getParameter("xlstitle");
		String getXlsHeader = request.getParameter("xlsheader");
		String getXlsData = request.getParameter("xlsdata");

		String[] arrHeader = getXlsHeader.split("\t");
		String[] arrDataRow = getXlsData.split("\n");
	%>
	<div id="wrapper">
		<!--Header -->
		<div id="header">
			<h5><%=getXlsTitle%>
				(<%=arrDataRow.length%>건)
			</h5>
		</div>
		<!--//Header -->
		<div id="container">
			<div id="content">
				<table class="data_list" summary="<%=getXlsTitle%>">
					<thead>
						<tr>
							<%
								for (int i = 0; i < arrHeader.length; i++) {
							%>
							<th scope="col"><%=arrHeader[i]%></th>
							<%
								}
							%>
						</tr>
					</thead>
					<tbody>
						<%
							for (int i = 0; i < arrDataRow.length; i++) {
						%>
						<tr>
							<%
								String[] arrDataRowCol = arrDataRow[i].split("\t");
									for (int j = 0; j < arrDataRowCol.length; j++) {
							%>
							<td><%=arrDataRowCol[j]%></td>
							<%
								}
							%>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>