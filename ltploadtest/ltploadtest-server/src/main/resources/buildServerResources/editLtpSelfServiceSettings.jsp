<%@ page import="java.util.Map" %>
<%@ page import="com.apicasystem.ltpselfservice.TestResultConstants" %>
<%@ page import="com.apicasystem.ltpselfservice.ApicaSettings" %>
<%@ page import="com.apicasystem.ltpselfservice.resources.DelayUnit" %>
<%@ page import="com.apicasystem.ltpselfservice.resources.LtpEnvironmentType" %>

<%@page contentType="text/html" pageEncoding="windows-1252"%>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="propertiesBean" type="jetbrains.buildServer.controllers.BasePropertiesBean" scope="request"/>
<jsp:useBean id="constants" class="com.apicasystem.ltpselfservice.LtpSelfServiceContanstsBean" />
<jsp:useBean id="json" class="com.apicasystem.ltpselfservice.JsonGenerator"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%--
    Initialize JSP
--%>
<%    
    
    Map<String, String> settings = propertiesBean.getProperties();
    json.setSettings(settings);

    String chosenTestConfiguration = settings.containsKey(TestResultConstants.testConfigurationId_key)
                                     ? settings.get(TestResultConstants.testConfigurationId_key)
                                     : "";
    
    String chosenTestEnvironment = settings.containsKey(TestResultConstants.testEnvironmentKey)
                                     ? settings.get(TestResultConstants.testEnvironmentKey)
                                     : "";                             

    request.setAttribute("chosenTestConfiguration", chosenTestConfiguration);
    request.setAttribute("chosenTestEnvironment", chosenTestEnvironment);
    
    request.setAttribute("delayUnits", DelayUnit.values());
    request.setAttribute("environments", LtpEnvironmentType.values());
%>

<%--
    HTML Templates
--%>
<div id="option" style="display: none;">
    <option value="{0}" data-title="" {2}>{1}</option>
</div>

<div id="threshold" style="display: none;">
    <div id="threshold_{0}" class="threshold" style="display: none;">
        <strong>IF</strong>
        <select id="threshold_{0}_metric" name="prop:threshold.{0}.metric" class="LI_dropList"> {1} </select>
        <select id="threshold_{0}_operator" name="prop:threshold.{0}.operator" class="LI_dropList"> {2} </select>
        <input type="text" id="threshold_{0}_value" name="prop:threshold.{0}.value" value="{3}" class="LI_numericField"/>
        <span id="threshold_{0}_unit" class="LI_unit">XX</span>
        <strong>THEN</strong> mark as
        <select id="threshold_{0}_result" name="prop:threshold.{0}.result"  class="LI_dropList"> {4} </select>
        <a href="#" id="threshold_{0}_remove" class="LI_removeButton">X</a>
    </div>
</div>

<%--
    Styles
--%>
<style type="text/css">
    .LI_dropList {
        padding:0; margin:0;
    }
    .LI_numericField {
        text-align: right;
        width: 6em;
    }
    .LI_addButton {
        cursor: pointer;
        background: url("/img/add.png") no-repeat left;
        padding-left: 1.5em;
        white-space: nowrap;
        text-decoration: none;
    }
    .LI_removeButton {
        color: red;
        font-weight: bold;
        cursor: pointer;
        text-decoration: none;
    }
    .LI_reloadButton {
        color: #008000;
        font-weight: bold;
        cursor: pointer;
        text-decoration: none;
    }
    .LI_unit {
        font-style: italic;
        width: 3em;
        overflow: hidden;
        display: inline-block;
        vertical-align: text-bottom;
    }
</style>

<%--
    JavaScript Functions
--%>
<script type="text/javascript">
    // Populate JSON data from the server
    var metrics = ${json.metrics};
    var operators = ${json.operators};
    var actions = ${json.actions};
    var thresholds = ${json.thresholds};
    var chosenTestConfiguration = "${chosenTestConfiguration}";
    var nextMetricId = 1;

    String.prototype.format = function() {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] != 'undefined' ? args[number] : match;
        });
    };

    function mkOptions(tmpl, items, selected) {
        var result = "";
        for (var k = 0; k < items.length; ++k) {
            var item = items[k];
            result += tmpl.format(item.name, item.label, (item.name == selected) ? "SELECTED=''" : "");
        }
        return result;
    }

    function mkThreshold(id, metric, operator, value, action) {
        var optionTmpl = $j("#option").html();
        var metricItems = mkOptions(optionTmpl, metrics, metric);
        var operatorItems = mkOptions(optionTmpl, operators, operator);
        var actionItems = mkOptions(optionTmpl, actions, action);

        var thresholdTmpl = $j("#threshold").html();
        var thresholdHtml = thresholdTmpl.format(id, metricItems, operatorItems, value, actionItems);

        return thresholdHtml;
    }

    function insertThreshold(id, metric, operator, value, action) {
        var thresholdHtml = mkThreshold(id, metric, operator, value, action);

        // Append HTML to threshold DIV
        var thresholdsDiv = $j("#thresholds");
        $j(thresholdHtml).appendTo(thresholdsDiv).slideDown("slow");

        // Attach click-handler to Remove button
        $j("#threshold_" + id + "_remove").click(function() {
            var target = $j("#threshold_" + id);
            target.slideUp("slow", function() {
                target.remove();
            });
            return false;
        });

        // Attach change-handler to metrics drop-list
        $j("#threshold_" + id + "_metric").change(function() {
            var valueSelected = $j(this).find("option:selected").val();
            for (var k = 0; k < metrics.length; ++k) {
                if (metrics[k].name == valueSelected) {
                    $j("#threshold_" + id + "_unit").text(metrics[k].unit);
                    break;
                }
            }
            return false;
        }).change();
    }

    $j(document).ready(function() {
        console.log("[Apica] Initializing ...");
        console.log("[Apica] metrics: " + JSON.stringify(metrics, null, 2));
        console.log("[Apica] operators: " + JSON.stringify(operators, null, 2));
        console.log("[Apica] actions: " + JSON.stringify(actions, null, 2));
        console.log("[Apica] thresholds: " + JSON.stringify(thresholds, null, 2));
        console.log("[Apica] chosenTestConfiguration: " + chosenTestConfiguration);

        // Attach click-handler to the 'Add Threshold' button
        $j("#addThreshold").click(function() {
            insertThreshold(nextMetricId++, "average_page_response_time", "greaterThan", 5000, "failed");
            return false;
        });

        // Populate stored thresholds
        for (var k = 0; k < thresholds.length; ++k) {
            var t = thresholds[k];
            insertThreshold(nextMetricId++, t.metric, t.operator, t.value, t.action);
        }        

        console.log("[Apica] Initialized");
    });

</script>

<l:settingsGroup title="Load Test Environment">
    <tr>
        <th>Test Environment</th>
        <td>
            <props:selectProperty name="loadtest.environment">
                <c:forEach items="${environments}" var="unit">
                    <props:option value="${unit.id}">${unit.displayName}</props:option>
                </c:forEach>
            </props:selectProperty>
            <span class="smallNote" >Select the appropriate LTP environment</span>
        </td>
    </tr>
    <tr>
        <th>&nbsp;</th>
        <td>
            <span class="error" id="error_${constants.ltpEnvironment}" />
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Installation">
    <tr>
        <th><label for="${constants.ltpApiAuthToken}">LTP authentication token: <l:star /></label></th>
        <td><props:textProperty id="${constants.ltpApiAuthToken}" name="${constants.ltpApiAuthToken}" className="longField"/>
            <span class="error" id="error_${constants.ltpApiAuthToken}" />
            <span class="smallNote" >Your LTP authentication token</span>
        </td>        
    </tr>
    <tr>
        <th><label for="${constants.ltpPresetName}">Preset name: <l:star /></label></th>
        <td><props:textProperty id="${constants.ltpPresetName}" name="${constants.ltpPresetName}" className="longField"/>
            <span class="error" id="error_${constants.ltpPresetName}" />
            <span class="error" id="error_${constants.ltpPresetTestInstanceId}" />
            <span class="smallNote" >The name of the load test preset as you saved it in LTP.</span>            
        </td>        
    </tr>
    <tr>
        <th><label for="${constants.ltpRunnableFileName}">Load test scenario: <l:star /></label></th>
        <td><props:textProperty id="${constants.ltpRunnableFileName}" name="${constants.ltpRunnableFileName}" className="longField"/>
            <span class="error" id="error_${constants.ltpRunnableFileName}" />
            <span class="smallNote" >The name of the load test scenario to run the loadtest with e.g myscenario.class or scenario1.zip</span>            
        </td>        
    </tr>
</l:settingsGroup>



<l:settingsGroup title="Thresholds">
    <tr>
        <th>
            <a href="#" id="addThreshold" class="btn"><span class="addNew">Add threshold</span></a>
        </th>
        <td><span class="smallNote">
                You can configure one or more thresholds, which determine if a load-test should be marked as failed.
            </span>
        </td>

    </tr>
    <tr>
        <th>&nbsp;</th>
        <td>
            <span class="error" id="error_${constants.ltpThresholdSettings}" />
        </td>
    </tr>
    <tr>
        <th>&nbsp;</th>
        <td id="thresholds"></td>
    </tr>

</l:settingsGroup>