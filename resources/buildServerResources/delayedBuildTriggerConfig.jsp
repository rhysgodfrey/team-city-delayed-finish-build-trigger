<%@ page import="java.io.File" %>
<%@include file="/include.jsp" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="delayedFinishedBuildTriggerBean" type="rhysgodfrey.teamcity.triggers.DelayedBuildFinishedSettingsBean" scope="request"/>

<table class="runnerFormTable" style="width: 99%;">
    <tbody>
        <tr>
          <td colspan="2"><em>Delayed Finished Build Trigger will add a build to the queue a specific period after a build finishes in the selected configuration.</em></td>
        </tr>
        <tr>
            <td style="vertical-align: top;">
                <label for="trigger_configuration">Build configuration:</label>
            </td>
            <td style="vertical-align: top;">
                <props:selectProperty name="trigger_configuration" style="width: 100%;" enableFilter="true">
                  <props:option value="">-- Please select a build configuration --</props:option>
                  <c:forEach items="${delayedFinishedBuildTriggerBean.availableActiveBuildTypes}" var="buildType">
                    <props:option value="${buildType.buildTypeId}"><c:out value="${buildType.extendedName}"/></props:option>
                  </c:forEach>
                  <props:option value="">-- Archived configurations --</props:option>
                  <c:forEach items="${delayedFinishedBuildTriggerBean.availableArchivedBuildTypes}" var="buildType">
                    <props:option value="${buildType.buildTypeId}"><c:out value="${buildType.extendedName}"/></props:option>
                  </c:forEach>
                </props:selectProperty>
                <span class="error" id="error_trigger_configuration"></span>
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
                <span class="error" id="error_wait_time"></span>
            </td>
        </tr>
    </tbody>
</table>