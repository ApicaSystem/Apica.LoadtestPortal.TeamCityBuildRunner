<%@ page import="java.util.Enumeration" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>

<div id="self-service-trends">
    <img src="${teamcityPluginResourcesPath}image/apica-loadtest-logo.png">
    <h2>Apica Load Test Trends</h2>
    
   <c:if test="${hasResults}">
       <c:if test="${not empty resultUrl}">
            <iframe width="1200" height="1000" src="${resultUrl}">
                 <p>Your browser does not support iframes.</p>
                 <p>Please, go directly to the <a href="${resultUrl}">results page at Apica System</a>.</p>
            </iframe>
       </c:if>
    </c:if>
    <c:if test="${not hasResults}">
        <p>${loadFailure}</p>
    </c:if> 
</div>
