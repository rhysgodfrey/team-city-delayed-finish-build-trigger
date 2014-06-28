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

/*
    Based on Example code provided on TeamCity Plugin Forums: http://devnet.jetbrains.com/message/5463043#5463043
 */

package rhysgodfrey.teamcity.triggers;

import java.util.ArrayList;
import java.util.List;
import jetbrains.buildServer.buildTriggers.BuildTriggerDescriptor;
import jetbrains.buildServer.serverSide.BuildTypeSettings;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuildType;
import org.jetbrains.annotations.NotNull;

public class DelayedBuildFinishedSettingsBean {
    private BuildTypeSettings mySettings;
    private BuildTriggerDescriptor myTriggerDescriptor;
    private ProjectManager myProjectManager;

    public DelayedBuildFinishedSettingsBean(final BuildTypeSettings settings,
                                       final BuildTriggerDescriptor triggerDescriptor,
                                       final ProjectManager projectManager) {
        mySettings = settings;
        myTriggerDescriptor = triggerDescriptor;
        myProjectManager = projectManager;
    }

    @NotNull
    public List<SBuildType> getAvailableActiveBuildTypes() {
        return computeAvailableBuildTypes(myProjectManager.getActiveBuildTypes());
    }

    @NotNull
    public List<SBuildType> getAvailableArchivedBuildTypes() {
        return computeAvailableBuildTypes(myProjectManager.getArchivedBuildTypes());
    }

    private List<SBuildType> computeAvailableBuildTypes(List<SBuildType> allAvailable) {

        List<SBuildType> result = new ArrayList<SBuildType>();
        for (SBuildType bt : allAvailable) {
            result.add(bt);
        }
        return result;
    }
}