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
import jetbrains.buildServer.log.LogUtil;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DelayedBuildFinishedTriggeringPolicy extends PolledBuildTrigger {

    private static final Logger LOG = Loggers.SERVER;

    private ProjectManager _projectManager;

    public DelayedBuildFinishedTriggeringPolicy(ProjectManager projectManager) {
        _projectManager = projectManager;
    }

    @Override
    public void triggerActivated(@NotNull PolledTriggerContext context) throws BuildTriggerException{
        SaveLastFinishedBuild(context);
    }

    @Override
    public void triggerBuild(@NotNull PolledTriggerContext context) throws BuildTriggerException {
        String lastTriggeredId = GetLastTriggeredBuild(context);

        SFinishedBuild lastTriggerBuild = GetLastTriggerBuild(context);

        if (lastTriggerBuild != null) {
            if (!lastTriggeredId.equalsIgnoreCase(Long.toString(lastTriggerBuild.getBuildId()))) {
                Date triggerTime = new Date(lastTriggerBuild.getFinishDate().getTime() + WaitTime(context) * 60 * 1000L);

                if (triggerTime.before(new Date())) {
                    SQueuedBuild queued = context.getBuildType().addToQueue("Delayed build trigger : " + lastTriggerBuild.getFullName());
                    context.getCustomDataStorage()
                            .putValue(DelayedBuildFinishTriggerConstants.LastBuildIdKey, Long.toString(lastTriggerBuild.getBuildId()));
                }
            }
        }
    }

    private String TriggerConfigurationId(@NotNull PolledTriggerContext context) {
        return context.getTriggerDescriptor().getProperties()
                .get(DelayedBuildFinishTriggerConstants.TriggerConfigurationProperty);
    }

    private Boolean SuccessfulBuildsOnly(@NotNull PolledTriggerContext context) {
        return Boolean.parseBoolean(context.getTriggerDescriptor().getProperties()
                .get(DelayedBuildFinishTriggerConstants.AfterSuccessfulBuildOnlyProperty));
    }

    private Integer WaitTime(@NotNull PolledTriggerContext context) {
        return Integer.parseInt(context.getTriggerDescriptor().getProperties()
                .get(DelayedBuildFinishTriggerConstants.WaitTimeProperty));
    }

    private String GetLastTriggeredBuild(@NotNull PolledTriggerContext context) {
        return context.getCustomDataStorage().getValue(DelayedBuildFinishTriggerConstants.LastBuildIdKey);
    }

    private void SaveLastFinishedBuild(@NotNull PolledTriggerContext context) {
        SFinishedBuild lastBuild = GetLastTriggerBuild(context);

        if (lastBuild == null) {
            context.getCustomDataStorage().putValue(DelayedBuildFinishTriggerConstants.LastBuildIdKey, "");
            return;
        }

        context.getCustomDataStorage()
                .putValue(DelayedBuildFinishTriggerConstants.LastBuildIdKey, Long.toString(lastBuild.getBuildId()));
    }

    private SFinishedBuild GetLastTriggerBuild(@NotNull PolledTriggerContext context) {
        String triggerBuildId = TriggerConfigurationId(context);

        SBuildType currentBuild = context.getBuildType();

        SBuildType triggerBuild = GetTriggerBuild(triggerBuildId);

        if (triggerBuild != null) {
            if(SuccessfulBuildsOnly(context)) {
                return triggerBuild.getLastChangesSuccessfullyFinished();
            }
            else {
                return triggerBuild.getLastChangesFinished();
            }
        }

        return null;
    }

    private SBuildType GetTriggerBuild(String triggerBuildId) {
        SBuildType byInternalId = _projectManager.findBuildTypeById(triggerBuildId);

        if (byInternalId != null) {
            return byInternalId;
        }

        return _projectManager.findBuildTypeByExternalId(triggerBuildId);
    }
}
