plugins {
    id("clearsolutionsbuild.plugins.spring-boot-library")
}

dependencies {
    implementation(project(":validation"))
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")

    testImplementation("org.springframework.boot:spring-boot-starter-json")
    // https://mvnrepository.com/artifact/nl.jqno.equalsverifier/equalsverifier
    testImplementation("nl.jqno.equalsverifier:equalsverifier")

}
