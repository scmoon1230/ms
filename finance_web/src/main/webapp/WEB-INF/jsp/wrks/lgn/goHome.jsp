<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@include file="/WEB-INF/jsp/cmm/script_nomenu.jsp"%>
<script type="text/javaScript">
    alert("속성값이 변경되어 자동로그인합니다.");
//    document.location.href="<c:url value='/mntr/main/main.do'/>";

	var url = "<c:url value='/wrks/lgn/login.json'/>";
	var params = "";

	$.ajaxEx(null, {
		url : url,
		datatype: "json",
		data : params,
		success : function(data) {
			if (data.ret == 1) {				//alert("11");
				location.href = "<c:url value='/'/>" + data.redirect;
			} else {
				alert(data.msg);				//alert("22");
				//location.href = "<c:url value='/'/>" + "wrks/lgn/login.do";
			}
		},
		error : function(e) {
			alert("접속이 지연되고 있습니다. 잠시후 시도해주십시오");
		}
	}
	);

</script>
