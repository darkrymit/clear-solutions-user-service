plugins {
    id("java")
}

val internalPlatform = configurations.create("internalPlatform") {
    isCanBeConsumed = false
    isCanBeResolved = false
}

configurations.compileClasspath.get().extendsFrom(internalPlatform)
configurations.runtimeClasspath.get().extendsFrom(internalPlatform)
configurations.testCompileClasspath.get().extendsFrom(internalPlatform)
configurations.testRuntimeClasspath.get().extendsFrom(internalPlatform)
configurations.annotationProcessor.get().extendsFrom(internalPlatform)
configurations.testAnnotationProcessor.get().extendsFrom(internalPlatform)

try {
    configurations.getByName("testFixturesCompileClasspath").extendsFrom(internalPlatform)
}catch (e: UnknownConfigurationException) {
    // ignore since the testFixturesCompileClasspath configuration is not available
}

try {
    configurations.getByName("testFixturesRuntimeClasspath").extendsFrom(internalPlatform)
}catch (e: UnknownConfigurationException) {
    // ignore since the testFixturesRuntimeClasspath configuration is not available
}

try {
    configurations.getByName("testFixturesAnnotationProcessor").extendsFrom(internalPlatform)
}catch (e: UnknownConfigurationException) {
    // ignore since the testFixturesAnnotationProcessor configuration is not available
}

