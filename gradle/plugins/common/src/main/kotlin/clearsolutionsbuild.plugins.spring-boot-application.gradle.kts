plugins {
    id("clearsolutionsbuild.plugins.common")
    id("org.springframework.boot")
}

dependencies {

    // for additional spring dependencies support
    developmentOnly(platform("clearsolutionsbuild.platforms:spring-platform"))

    implementation("org.mapstruct:mapstruct")
    implementation("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // order of annotationProcessor is important for lombok and mapstruct to work together
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding")
    annotationProcessor("org.mapstruct:mapstruct-processor")
    annotationProcessor("org.projectlombok:lombok")

    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
}
