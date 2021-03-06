<%@ page import="java.util.Enumeration" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>

<div id="self-service-summary">
    <img src="${teamcityPluginResourcesPath}image/apica-loadtest-logo.png">
    <c:if test="${noResults}">
        <p>No results to show</p>
    </c:if>
    
    <c:if test="${hasResults}">
        <h2>Apica Load Test Summary</h2>
        <table>
            <tr>
                <th>Preset name</th>
                <td>${presetName}</td>
            </tr>
            <tr>
                <th>Job id</th>
                <td>${jobid}</td>
            </tr>
            <tr>
                <th>Link to details</th>
                <td><a href="${link}">Apica LoadTest Results</a></td>
            </tr>
            <tr>
                <th>Date UTC</th>
                <td>${dateOfInsertion}</td>
            </tr>
            <tr>
                <th>Total passed loops</th>
                <td>${totalPassedLoops}</td>
            </tr>
            <tr>
                <th>Total failed loops</th>
                <td>${totalFailedLoops}</td>
            </tr>
            <tr>
                <th>Average network throughput</th>
                <td>${averageNetworkThroughput}&nbsp;${networkThroughputUnit}</td>
            </tr>
            <tr>
                <th>Average session time per loop (s)</th>
                <td>${averageSessionTimePerLoop}</td>
            </tr>
            <tr>
                <th>Average response time per loop (s)</th>
                <td>${averageResponseTimePerLoop}</td>
            </tr>
            <tr>
                <th>Web transaction rate (Hits/s)</th>
                <td>${webTransactionRate}</td>
            </tr>
            <tr>
                <th>Average response time per page (s)</th>
                <td>${averageResponseTimePerPage}</td>
            </tr>
            <tr>
                <th>Total http(s) calls</th>
                <td>${totalHttpCalls}</td>
            </tr>
            <tr>
                <th>Avg network connect time (ms)</th>
                <td>${averageNetworkConnectTime}</td>
            </tr>
            <tr>
                <th>Total transmitted bytes</th>
                <td>${totalTransmittedBytes}</td>
            </tr>            
        </table>
    </c:if>    
        
</div>