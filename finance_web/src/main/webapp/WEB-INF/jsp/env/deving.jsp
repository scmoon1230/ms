<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta http-equiv="content-language" content="ko">
<%@include file="/WEB-INF/jsp/cmm/script.jsp"%>

<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >

<script type="text/javascript">
function fncGoAfterErrorPage(){
	history.back(-2);
}
alert("작업중입니다.");
history.back(-2);
</script>
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td align="center" valign="top"><br />
	<br />
	<br />
		<table width="600" border="0" cellpadding="0" cellspacing="0" background="er_images/blue_bg.jpg' />">
		<tr>
		<td align="center">
			<table width="100%" border="0" cellspacing="9" cellpadding="0">
			<tr>
			<td bgcolor="#FFFFFF">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<td align="center">
						<table width="520" border="0" cellspacing="2" cellpadding="2">
							<tr><td width="74" rowspan="2" align="center"></td>
								<td width="399" align="left" class="lt_text2">작업중입니다.</td>
							</tr>
						</table>
						<table width="500" border="0" cellspacing="2" cellpadding="2">
						</table>
					</td>
					</tr>
					<tr><td><br /><br />
						</td>
					</tr>
					<tr><td align="center">
						<a href="#LINK" onClick="fncGoAfterErrorPage();">뒤로가기</a>
						</td>
					</tr>
				</table>
				<br />
				</td>
			</tr>
			</table>
		</td>
		</tr>
		</table>
	</td>
	</tr>
</table>
</body>
</html>
