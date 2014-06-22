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

import jetbrains.buildServer.buildTriggers.BuildTriggerException;
import jetbrains.buildServer.buildTriggers.PolledBuildTrigger;
import jetbrains.buildServer.buildTriggers.PolledTriggerContext;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DelayedBuildFinishedTriggeringPolicy extends PolledBuildTrigger {

    @Override
    public void triggerActivated(@NotNull PolledTriggerContext context) throws BuildTriggerException{
        context.getCustomDataStorage().putValue("RhysValue", "First!");
    }

    @Override
    public void triggerBuild(@NotNull PolledTriggerContext context) throws BuildTriggerException {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("c:\\temp\\plugin-log.txt", true)));
            out.println("Here!");
            out.println(context.getCustomDataStorage().getValue("RhysValue"));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            context.getCustomDataStorage().putValue("RhysValue", dateFormat.format(date));

            for(Object key : context.getTriggerDescriptor().getProperties().keySet()){
                out.println(key + " : " + context.getTriggerDescriptor().getProperties().get(key));
            }

            out.close();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
