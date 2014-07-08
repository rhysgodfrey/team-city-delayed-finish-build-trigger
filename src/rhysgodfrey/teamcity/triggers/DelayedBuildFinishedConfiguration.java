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

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DelayedBuildFinishedConfiguration {
    public static Integer getWaitTime(@NotNull Map<String,String> properties) {
        return Integer.parseInt(properties.get(DelayedBuildFinishTriggerConstants.WAIT_TIME_PROPERTY));
    }

    public static Boolean getSuccessfulBuildsOnlyConfiguration(@NotNull Map<String,String> properties) {
        return Boolean.parseBoolean(properties.get(DelayedBuildFinishTriggerConstants.AFTER_SUCCESSFUL_BUILD_ONLY_PROPERTY));
    }

    public static String getDependentBuildConfigurationId(@NotNull Map<String,String> properties) {
        return properties.get(DelayedBuildFinishTriggerConstants.TRIGGER_CONFIGURATION_PROPERTY);
    }
}
