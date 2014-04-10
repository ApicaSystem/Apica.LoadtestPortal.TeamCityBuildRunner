<%-- 
    Document   : viewtpSelfServiceSettings
    Created on : Apr 7, 2014, 3:58:48 PM
    Author     : andras.nemes
--%>

<%-- any content can be specified here e.g.: --%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="constants" class="jetbrains.buildServer.fxcop.server.FxCopConstantsBean"/>

<div class="parameter">
  Self service preset name: <strong><props:displayValue name="${constants.ltpPresetName}" emptyValue="not specified"/></strong>
</div>

<div class="parameter">
  Load test file name: <strong><props:displayValue name="${constants.ltpRunnableFileName}"/></strong>
</div>