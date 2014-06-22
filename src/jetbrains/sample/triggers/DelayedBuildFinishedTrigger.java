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

package jetbrains.sample.triggers;

import jetbrains.buildServer.buildTriggers.BuildTriggerDescriptor;
import jetbrains.buildServer.buildTriggers.BuildTriggerService;
import jetbrains.buildServer.buildTriggers.BuildTriggeringPolicy;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DelayedBuildFinishedTrigger extends BuildTriggerService {
    private PluginDescriptor _descriptor;

    public DelayedBuildFinishedTrigger(@NotNull PluginDescriptor descriptor) {
        super();
        _descriptor = descriptor;
    }

    @NotNull
    @Override
    public String getName() {
        return "Delayed Finished Build Trigger";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Delayed Finished Build Trigger";
    }

    @NotNull
    @Override
    public String describeTrigger(@NotNull BuildTriggerDescriptor trigger) {
        return "Finished Build Trigger with a specified delay";
    }

    @NotNull
    @Override
    public BuildTriggeringPolicy getBuildTriggeringPolicy() {
        return new DelayedBuildFinishedTriggeringPolicy();
    }

    @Nullable
    @Override
    public java.lang.String getEditParametersUrl() {
        return _descriptor.getPluginResourcesPath("delayedBuildTriggerConfig.jsp");
    }
}
