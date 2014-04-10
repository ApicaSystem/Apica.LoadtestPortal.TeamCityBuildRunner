<%-- 
    Document   : editLtpSerlServiceSettings
    Created on : Apr 7, 2014, 3:57:15 PM
    Author     : andras.nemes
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="propertiesBean" type="jetbrains.buildServer.controllers.BasePropertiesBean" scope="request"/>
<jsp:useBean id="constants" class="com.apicasystem.ltpselfservice.LtpSelfServiceContanstsBean" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<l:settingsGroup title="LTP Self Service Installation">
    <tr>
        <th><label for="${constants.ltpUserName}">LTP user name: <l:star /></label></th>
        <td><props:textProperty id="${constants.ltpUserName}" name="${constants.ltpUserName}" className="longField"/>
            <span class="error" id="error_${constants.ltpUserName}" />
            <span class="smallNote" >Your LTP login name</span>
        </td>        
    </tr>
    <tr class="auth password">
        <th><label for="${constants.ltpPassword}">LTP password: <l:star /></label></th>
        <td><props:passwordProperty name="${constants.ltpPassword}" className="longField" />
            <span class="error" id="error_${constants.ltpPassword}" />
            <span class="smallNote" >Your LTP password</span>
        </td>
    </tr>
    <tr>
        <th><label for="${constants.ltpPresetName}">Preset name: <l:star /></label></th>
        <td><props:textProperty id="${constants.ltpPresetName}" name="${constants.ltpPresetName}" className="longField"/>
            <span class="error" id="error_${constants.ltpPresetName}" />
            <span class="smallNote" >The name of the load test preset as you saved it in LTP</span>            
        </td>        
    </tr>
    <tr>
        <th><label for="${constants.ltpRunnableFileName}">Load test file: <l:star /></label></th>
        <td><props:textProperty id="${constants.ltpRunnableFileName}" name="${constants.ltpRunnableFileName}" className="longField"/>
            <span class="error" id="error_${constants.ltpRunnableFileName}" />
            <span class="smallNote" >The name of the load test preset as you saved it in LTP</span>            
        </td>        
    </tr>
</l:settingsGroup>