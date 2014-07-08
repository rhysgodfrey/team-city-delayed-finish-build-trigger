/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rhysgodfrey.teamcity.triggers;

import jetbrains.buildServer.buildTriggers.BuildTriggerDescriptor;
import jetbrains.buildServer.buildTriggers.BuildTriggerService;
import jetbrains.buildServer.buildTriggers.BuildTriggeringPolicy;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class DelayedBuildFinishedTrigger extends BuildTriggerService {
    public static final String EDIT_URL_BUILD_TRIGGER = "delayedBuildTriggerConfig";
    public static final String EDIT_URL_BUILD_TRIGGER_JSP = EDIT_URL_BUILD_TRIGGER + ".jsp";
    public static final String EDIT_URL_BUILD_TRIGGER_HTML = EDIT_URL_BUILD_TRIGGER + ".html";

    private PluginDescriptor descriptor;
    private ProjectManager projectManager;

    public DelayedBuildFinishedTrigger(@NotNull PluginDescriptor descriptor, @NotNull ProjectManager projectManager) {
        super();
        this.descriptor = descriptor;
        this.projectManager = projectManager;
    }

    @NotNull
    @Override
    public String getName() {
        return "DelayedFinishedBuildTrigger";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Delayed Finished Build Trigger";
    }

    @NotNull
    @Override
    public String describeTrigger(@NotNull BuildTriggerDescriptor trigger) {
        Integer waitTime = DelayedBuildFinishedConfiguration.getWaitTime(trigger.getProperties());

        String minutesString = "minutes";

        if (waitTime == 1) {
            minutesString = "minute";
        }

        String triggerDescription = "Wait " + waitTime + " " + minutesString + " after a ";

        if (DelayedBuildFinishedConfiguration.getSuccessfulBuildsOnlyConfiguration(trigger.getProperties())) {
            triggerDescription += "successful ";
        }

        String buildId = DelayedBuildFinishedConfiguration.getDependentBuildConfigurationId(trigger.getProperties());

        triggerDescription += "build in " + getDependentBuild(buildId).getFullName();

        return triggerDescription;
    }

    @NotNull
    @Override
    public BuildTriggeringPolicy getBuildTriggeringPolicy() {
        return new DelayedBuildFinishedTriggeringPolicy(projectManager);
    }

    @Nullable
    @Override
    public java.lang.String getEditParametersUrl() {
        return descriptor.getPluginResourcesPath(EDIT_URL_BUILD_TRIGGER_HTML);
    }

    @Override
    public PropertiesProcessor getTriggerPropertiesProcessor() {
        return new PropertiesProcessor() {
            public Collection<InvalidProperty> process(Map<String, String> properties) {
                final ArrayList<InvalidProperty> invalidProps = new ArrayList<InvalidProperty>();
                final String triggerBuildId = properties.get(DelayedBuildFinishTriggerConstants.TRIGGER_CONFIGURATION_PROPERTY);
                if (StringUtil.isEmptyOrSpaces(triggerBuildId)) {
                    invalidProps.add(new InvalidProperty(DelayedBuildFinishTriggerConstants.TRIGGER_CONFIGURATION_PROPERTY, "Trigger Build must be specified"));
                }

                final SBuildType build = getDependentBuild(properties.get(DelayedBuildFinishTriggerConstants.TRIGGER_CONFIGURATION_PROPERTY));
                if (build == null) {
                    invalidProps.add(new InvalidProperty(DelayedBuildFinishTriggerConstants.TRIGGER_CONFIGURATION_PROPERTY, "Trigger build must exist"));
                }

                final String waitTime = properties.get(DelayedBuildFinishTriggerConstants.WAIT_TIME_PROPERTY);
                if (StringUtil.isEmptyOrSpaces(triggerBuildId)) {
                    invalidProps.add(new InvalidProperty(DelayedBuildFinishTriggerConstants.WAIT_TIME_PROPERTY, "Wait Time must be specified"));
                }

                if(!StringUtil.isNumber(properties.get(DelayedBuildFinishTriggerConstants.WAIT_TIME_PROPERTY))) {
                    invalidProps.add(new InvalidProperty(DelayedBuildFinishTriggerConstants.WAIT_TIME_PROPERTY, "Wait Time must be a number"));
                }

                return invalidProps;
            }
        };
    }

    private SBuildType getDependentBuild(String triggerBuildId) {
        SBuildType byInternalId = projectManager.findBuildTypeById(triggerBuildId);

        if (byInternalId != null) {
            return byInternalId;
        }

        return projectManager.findBuildTypeByExternalId(triggerBuildId);
    }
}
