pluginManagement {
    includeBuild("../meta-plugins")
}

plugins {
    id("clearsolutionsbuild.metaplugins.settings")
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
    versionCatalogs.create("libs") {
        from(files("../libs.versions.toml"))
    }
}
rootProject.name = "platforms"