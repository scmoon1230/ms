<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="content-language" content="ko">
<title>ERROR</title>
</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="100%" height="100%" align="center" valign="middle" style="padding-top: 150px;"><table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<span style="font-family: Tahoma; font-weight: bold; color: #000000; line-height: 150%; width: 440px; height: 70px;">
							<c:choose>
								<c:when test="${!empty msg}">
									오류발생 알림화면(<c:out value="${msg}" />)
								</c:when>
								<c:otherwise>
									오류발생 알림화면(허용되지 않는 요청을 하셨습니다)
								</c:otherwise>
							</c:choose>
							</span>
						</td>
					</tr>
				</table></td>
		</tr>
	</table>
</body>
</html>