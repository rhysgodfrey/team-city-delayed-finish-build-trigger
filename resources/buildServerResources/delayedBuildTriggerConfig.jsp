<%@ page import="java.io.File" %>
<%@include file="/include.jsp" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<table class="runnerFormTable" style="width: 99%;">
    <tbody>
        <tr>
          <td colspan="2"><em>Delayed Finished Build Trigger will add a build to the queue a specific period after a build finishes in the selected configuration.</em></td>
        </tr>
        <tr>
            <td style="vertical-align: top;">
                <label for="trigger_configuration">Build configuration Id (must be a snapshot dependency):</label>
            </td>
            <td style="vertical-align: top;">
                <props:textProperty name="trigger_configuration"/>
            </td>
        </tr>
        <tr>
            <td class="noBorder">&nbsp;</td>
            <td class="noBorder">
                <props:checkboxProperty name="afterSuccessfulBuildOnly" />
                <label for="afterSuccessfulBuildOnly">Trigger after successful build only</label>
            </td>
        </tr>
        <tr>
            <td style="vertical-align: top;">
                <label for="wait_time">Time to wait (in minutes):</label>
            </td>
            <td style="vertical-align: top;">
                <props:textProperty name="wait_time"/>
            </td>
        </tr>
    </tbody>
</table>