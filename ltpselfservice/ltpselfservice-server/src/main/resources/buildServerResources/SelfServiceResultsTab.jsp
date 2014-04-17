<%-- 
    Document   : SelfServiceResultsTab
    Created on : Apr 16, 2014, 8:46:51 AM
    Author     : andras.nemes
--%>

<%@ page import="java.util.Enumeration" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>

<div id="self-service-trends">
    <img src="${teamcityPluginResourcesPath}image/apica-loadtest-logo.png">
    <h2>Apica Load Test Trends</h2>
    <c:if test="${historyLoadFailed}">
        <p>Load test history failed to load. Exception: ${historyLoadException}</p>
    </c:if>
    
    <c:if test="${historyLoaded}">
        <p>${rawHistory}</p> 
    </c:if>
</div>
