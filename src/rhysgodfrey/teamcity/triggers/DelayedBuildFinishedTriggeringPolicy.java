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

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.buildTriggers.BuildTriggerException;
import jetbrains.buildServer.buildTriggers.PolledBuildTrigger;
import jetbrains.buildServer.buildTriggers.PolledTriggerContext;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.*;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class DelayedBuildFinishedTriggeringPolicy extends PolledBuildTrigger {

    private static final Logger LOG = Loggers.SERVER;

    private ProjectManager projectManager;

    public DelayedBuildFinishedTriggeringPolicy(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    @Override
    public void triggerActivated(@NotNull PolledTriggerContext context) throws BuildTriggerException{
        saveLastFinishedBuild(context);
    }

    @Override
    public void triggerBuild(@NotNull PolledTriggerContext context) throws BuildTriggerException {
        String lastTriggeredId = getLastDependentBuildIdTrigeredFor(context);

        SFinishedBuild lastTriggerBuild = getLastFinishedBuild(context);

        if (lastTriggerBuild != null) {
            if (!lastTriggeredId.equalsIgnoreCase(Long.toString(lastTriggerBuild.getBuildId()))) {
                Date triggerTime = new Date(lastTriggerBuild.getFinishDate().getTime() + getWaitTime(context) * 60 * 1000L);

                if (triggerTime.before(new Date())) {
                    context.getBuildType().addToQueue("Delayed build trigger : " + lastTriggerBuild.getFullName());
                    context.getCustomDataStorage()
                            .putValue(DelayedBuildFinishTriggerConstants.LAST_BUILD_ID_KEY, Long.toString(lastTriggerBuild.getBuildId()));
                }
            }
        }
    }

    private String getDependentBuildConfigurationId(@NotNull PolledTriggerContext context) {
        return context.getTriggerDescriptor().getProperties()
                .get(DelayedBuildFinishTriggerConstants.TRIGGER_CONFIGURATION_PROPERTY);
    }

    private Boolean getSuccessfulBuildsOnlyConfiguration(@NotNull PolledTriggerContext context) {
        return Boolean.parseBoolean(context.getTriggerDescriptor().getProperties()
                .get(DelayedBuildFinishTriggerConstants.AFTER_SUCCESSFUL_BUILD_ONLY_PROPERTY));
    }

    private Integer getWaitTime(@NotNull PolledTriggerContext context) {
        return Integer.parseInt(context.getTriggerDescriptor().getProperties()
                .get(DelayedBuildFinishTriggerConstants.WAIT_TIME_PROPERTY));
    }

    private String getLastDependentBuildIdTrigeredFor(@NotNull PolledTriggerContext context) {
        return context.getCustomDataStorage().getValue(DelayedBuildFinishTriggerConstants.LAST_BUILD_ID_KEY);
    }

    private void saveLastFinishedBuild(@NotNull PolledTriggerContext context) {
        SFinishedBuild lastBuild = getLastFinishedBuild(context);

        if (lastBuild == null) {
            context.getCustomDataStorage().putValue(DelayedBuildFinishTriggerConstants.LAST_BUILD_ID_KEY, "");
            return;
        }

        context.getCustomDataStorage()
                .putValue(DelayedBuildFinishTriggerConstants.LAST_BUILD_ID_KEY, Long.toString(lastBuild.getBuildId()));
    }

    private SFinishedBuild getLastFinishedBuild(@NotNull PolledTriggerContext context) {
        String triggerBuildId = getDependentBuildConfigurationId(context);
        SBuildType triggerBuild = getDependentBuild(triggerBuildId);

        if (triggerBuild != null) {
            if(getSuccessfulBuildsOnlyConfiguration(context)) {
                return triggerBuild.getLastChangesSuccessfullyFinished();
            }
            else {
                return triggerBuild.getLastChangesFinished();
            }
        }

        return null;
    }

    private SBuildType getDependentBuild(String triggerBuildId) {
        SBuildType byInternalId = projectManager.findBuildTypeById(triggerBuildId);

        if (byInternalId != null) {
            return byInternalId;
        }

        return projectManager.findBuildTypeByExternalId(triggerBuildId);
    }
}
