import gradle.kotlin.dsl.accessors._04bd7bfe124367ea47da06e76b10d421.annotationProcessor
import gradle.kotlin.dsl.accessors._04bd7bfe124367ea47da06e76b10d421.implementation
import gradle.kotlin.dsl.accessors._04bd7bfe124367ea47da06e76b10d421.testImplementation
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java-library")
    id("clearsolutionsbuild.plugins.common")
    id("org.springframework.boot") apply false
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.withType<BootBuildImage> {
    enabled = false
}

dependencies {

    implementation("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")

    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
}

