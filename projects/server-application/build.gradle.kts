import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("clearsolutionsbuild.plugins.spring-boot-application")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.getByName<BootJar>("bootJar") {
    archiveFileName.set("${archiveBaseName.get()}-${version}-boot.jar")
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
    imageName.set("clear-solutions/user-service")
    val latestTag = "clear-solutions/user-service:latest"
    val versionTag = "clear-solutions/user-service:${version}"
    tags.set(listOf(latestTag, versionTag))
    // fix a jar search path
    environment.put("BP_GRADLE_BUILD_ARTIFACT", "build/libs/*-boot.jar")

    //https://stackoverflow.com/questions/75885269/spring-boot-build-image-with-health-check
    environment.put("BP_HEALTH_CHECKER_ENABLED", "true")
    buildpacks.addAll(
        listOf(
            "urn:cnb:builder:paketo-buildpacks/java",
            "gcr.io/paketo-buildpacks/health-checker:latest"
        )
    )

}

// pass application.test properties to JavaExec task
tasks.test {
    System.getProperties().forEach { (k, v) ->
        if (k.toString().startsWith("application.test")) {
            systemProperties[k.toString()] = v
        }
    }
}

dependencies {

    // Since most of the common dependencies are already applied by plugin
    // and a version is managed by platform witch extends spring-boot-dependencies,
    // we only need to add additional dependencies even without a version,
    // like in a common spring project generated by spring initializer

    // https://mvnrepository.com/artifact/io.hypersistence/hypersistence-utils-hibernate-63
    implementation("io.hypersistence:hypersistence-utils-hibernate-63")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.flywaydb:flyway-core")
    implementation(project(":payload"))
    implementation(project(":validation"))

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.testcontainers:postgresql")
}
