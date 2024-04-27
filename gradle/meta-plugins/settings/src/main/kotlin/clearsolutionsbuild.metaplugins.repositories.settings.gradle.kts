import clearsolutionsbuild.settings.SettingsUtils

if (SettingsUtils.isFeatureEnabled(settings, "autoconfig.autoPluginManagement.enabled", true)) {
    pluginManagement {
        // Get community plugins from the Gradle Plugin Portal
        repositories {
            gradlePluginPortal()
        }


        val paths = SettingsUtils.getList(settings, "autoconfig.autoPluginManagement.includeBuild.paths", listOf("gradle/plugins"))
        val includeBuildPrefix = SettingsUtils.getString(settings, "autoconfig.autoPluginManagement.includeBuild.namePrefix", "")

        for (str in paths) {
            val file = file(rootDir.toPath().resolve(str))
            if (file.exists()) {
                includeBuild(file) {
                    name = includeBuildPrefix + file.name
                }
            }
        }
    }
}

if (SettingsUtils.isFeatureEnabled(settings, "autoconfig.autoDependencyResolutionManagement.enabled", true)) {
    dependencyResolutionManagement {
        // Get components from Maven Central
        repositories {
            mavenCentral()
        }
        val paths = SettingsUtils.getList(settings, "autoconfig.autoDependencyResolutionManagement.includeBuild.paths", listOf("gradle/platforms"))
        val includeBuildPrefix = SettingsUtils.getString(settings, "autoconfig.autoDependencyResolutionManagement.includeBuild.namePrefix", "")
        for (str in paths) {
            val file = file(rootDir.toPath().resolve(str))
            if (file.exists()) {
                includeBuild(file) {
                    name = includeBuildPrefix + file.name
                }
            }
        }
    }
}

