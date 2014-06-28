Team City Delayed Build Finished Trigger
===========================

Similar to the built in "Finish Build Trigger", this plugin adds an extra build trigger that allows you to specify a time period after the build has finished to wait before triggering the build.

This can be useful in scenarios where you can't run the next step in a build chain immediately, e.g. running tests against an environment that needs time to "warm up" or for deployment to complete.

The plugin has been tested on Team City 8.0.3, but should work with any version of Team City 8 or above.

### Installation

1. Download 'delayed-finish-build-trigger.zip' from the [latest release](https://github.com/rhysgodfrey/team-city-delayed-finish-build-trigger/releases/latest)
2. Copy this file into the _[Team City Data Directory]\plugins_ directory on the Team City Server, by default this is _C:\ProgramData\JetBrains\TeamCity\plugins_
3. Restart the Team City server

The "Delayed Finish Build Trigger" should be available in Team City

### Building the plugin

Follow the instructions in the TeamCity documentation for using the [bundled development plugin](http://confluence.jetbrains.com/display/TCD8/Bundled+Development+Package) using the code from this repository, rather than _samplePlugin-src.zip_
