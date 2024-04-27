plugins {
    `kotlin-dsl`
}

dependencies {
    // for spring support
    implementation(platform("clearsolutionsbuild.platforms:spring-platform"))

    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin")

    implementation(project(":internal-platform"))
}