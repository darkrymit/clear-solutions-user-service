plugins {
    id("java-platform")
}

// allow the definition of dependencies to other platforms like the Spring Boot BOM
javaPlatform.allowDependencies()

dependencies {

    api(platform(libs.spring.boot.dependencies))

    constraints {
        api(libs.spring.boot.gradle.plugin)
        api(libs.hypersistence.utils.hibernate)
        api(libs.mapstruct)
        api(libs.mapstruct.processor)
        api(libs.lombok.mapstruct.binding)
        api(libs.equalsverifier)
    }

}
