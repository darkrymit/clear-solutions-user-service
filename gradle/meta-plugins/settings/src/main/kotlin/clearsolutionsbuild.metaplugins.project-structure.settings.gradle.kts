import org.gradle.api.GradleException
import clearsolutionsbuild.settings.SettingsUtils
import java.util.regex.Pattern

if (SettingsUtils.isFeatureEnabled(settings, "autoconfig.autoInclude.enabled", true)) {

    val mode = SettingsUtils.getString(settings, "autoconfig.autoInclude.mode", "include")

    if (mode != "include" && mode != "includeBuild") {
        throw GradleException("autoconfig.autoInclude.mode must be 'include' or 'includeBuild'")
    }

    val asInclude = SettingsUtils.getList(settings, "autoconfig.autoInclude.asInclude").map { Pattern.compile(it) }

    val asIncludeBuild = SettingsUtils.getList(settings, "autoconfig.autoInclude.asIncludeBuild").map { Pattern.compile(it) }

    val exclude = SettingsUtils.getList(settings, "autoconfig.autoInclude.exclude").map { Pattern.compile(it) }

    val paths = SettingsUtils.getList(settings, "autoconfig.autoInclude.paths", listOf("."))

    val includeBuildPrefix = SettingsUtils.getString(settings, "autoconfig.autoInclude.includeBuildPrefix", "")

    val defaultInclude = { file: File ->
        if (mode == "include") {
            include(file.name)
        } else {
            includeBuild(file) {
                name = includeBuildPrefix + file.name
            }
        }
    }

    val includeIfNeeded: (File) -> Boolean = included@{ file: File ->
        for (pattern in exclude) {
            if (pattern.matcher(file.name).matches()) {
                return@included false
            }
        }

        for (pattern in asInclude) {
            if (pattern.matcher(file.name).matches()) {
                include(file.name)
                project(":${file.name}").projectDir = file
                return@included true
            }
        }

        for (pattern in asIncludeBuild) {
            if (pattern.matcher(file.name).matches()) {
                includeBuild(file) {
                    name = includeBuildPrefix + file.name
                }
                return@included true
            }
        }

        defaultInclude(file)
        if (mode == "include") {
            project(":${file.name}").projectDir = file
        }
        return@included true
    }

    for (str in paths) {
        val path = rootDir.toPath().resolve(str)
        file(path).listFiles().filter {
            it.isDirectory && file("${it.absolutePath}/build.gradle.kts").exists()
        }.forEach {
            includeIfNeeded(it)
        }
    }
}





