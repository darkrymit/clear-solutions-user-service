package clearsolutionsbuild.settings

import org.gradle.api.initialization.Settings

object SettingsUtils {

    fun isFeatureEnabled(settings: Settings, propertyToCheck: String, defaultValue: Boolean = false): Boolean {
        val provider = settings.providers.gradleProperty(propertyToCheck)
        if (!provider.isPresent) {
            return defaultValue
        }

        return provider.get().toBoolean()
    }

    fun getString(settings: Settings, propertyToCheck: String, defaultValue: String = ""): String {
        val provider = settings.providers.gradleProperty(propertyToCheck)
        if (!provider.isPresent) {
            return defaultValue
        }

        return provider.get()
    }

    fun getList(settings: Settings, propertyToCheck: String, defaultValue: List<String> = emptyList()): List<String> {
        val provider = settings.providers.gradleProperty(propertyToCheck)
        if (!provider.isPresent) {
            return defaultValue
        }

        return provider.get().split(',').map { it.trim() }

    }
}