/*
    Based on Example code provided on TeamCity Plugin Forums: http://devnet.jetbrains.com/message/5463043#5463043
 */

package rhysgodfrey.teamcity.triggers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrains.buildServer.buildTriggers.BuildTriggerDescriptor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.controllers.admin.projects.triggers.ShowTriggerParametersController;
import jetbrains.buildServer.serverSide.BuildTypeSettings;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

public class DelayedBuildFinishTriggerEditSettingsController extends BaseController {
    private final ProjectManager myProjectManager;
    private String myPluginResourcesPath;

    public DelayedBuildFinishTriggerEditSettingsController(final ProjectManager projectManager,
                                                 @NotNull final PluginDescriptor pluginDescriptor,
                                                 @NotNull final WebControllerManager manager) {
        myProjectManager = projectManager;
        myPluginResourcesPath = pluginDescriptor.getPluginResourcesPath(DelayedBuildFinishedTrigger.EDIT_URL_BUILD_TRIGGER_JSP);
        manager.registerController(pluginDescriptor.getPluginResourcesPath(DelayedBuildFinishedTrigger.EDIT_URL_BUILD_TRIGGER_HTML), this);
    }

    protected ModelAndView doHandle(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) throws Exception {
        BuildTypeSettings settings = (BuildTypeSettings)request.getAttribute(ShowTriggerParametersController.BUILD_TYPE_SETTINGS_PARAM);
        BuildTriggerDescriptor triggerDescriptor =
                (BuildTriggerDescriptor)request.getAttribute(ShowTriggerParametersController.SELECTED_TRIGGER_DESCRIPTOR_PARAM);

        DelayedBuildFinishedSettingsBean data = new DelayedBuildFinishedSettingsBean(settings, triggerDescriptor, myProjectManager);

        ModelAndView mv = new ModelAndView(myPluginResourcesPath);
        mv.getModel().put("delayedFinishedBuildTriggerBean", data);
        return mv;
    }
}
