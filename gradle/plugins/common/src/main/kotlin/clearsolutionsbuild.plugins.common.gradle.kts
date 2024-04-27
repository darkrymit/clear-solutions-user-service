plugins {
    id("java")
    id("java-test-fixtures")
    id("clearsolutionsbuild.plugins.internalplatform")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    val compileOnly by getting
    val annotationProcessor by getting
    compileOnly.extendsFrom(annotationProcessor)

    val testCompileOnly by getting
    val testAnnotationProcessor by getting
    testCompileOnly.extendsFrom(testAnnotationProcessor)

    val testFixturesCompileOnly by getting
    val testFixturesAnnotationProcessor by getting
    testFixturesCompileOnly.extendsFrom(testFixturesAnnotationProcessor)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    // add common platform to the project but not actually add any dependencies
    internalPlatform(platform("clearsolutionsbuild.platforms:spring-platform"))

}

tasks.test {
    useJUnitPlatform() // Use JUnit5
}
