<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="searchArea">
    <div class="page fL">
        <span class="txt">페이지당</span>
        <div class="selectBox">
            <select name="rowPerPageList" id="rowPerPageList" class="selectType1" onchange="$('.btnS').click()">
            <%--
                <c:forEach items="${rowPerPageList}" var="val">
                    <option value="${val.cdId}" ${val.cdId == rowPerPageSession ? 'selected' : ''}><c:out value="${val.cdNmKo}"></c:out></option>
                </c:forEach>
             --%>
                    <option value="40" ${50 == rowPerPageSession ? 'selected' : ''}>50</option>
                    <option value="100" ${100 == rowPerPageSession ? 'selected' : ''}>100</option>
                    <option value="200" ${200 == rowPerPageSession ? 'selected' : ''}>200</option>
                    <option value="500" ${500 == rowPerPageSession ? 'selected' : ''}>500</option>
                    <option value="1000" ${1000 == rowPerPageSession ? 'selected' : ''}>1000</option>
                    <option value="2000" ${2000 == rowPerPageSession ? 'selected' : ''}>2000</option>
                    <option value="5000" ${5000 == rowPerPageSession ? 'selected' : ''}>5000</option>
            </select>
        </div>
        <span class="totalNum">전체<em id="rowCnt"></em>건</span>
    </div>
</div>
