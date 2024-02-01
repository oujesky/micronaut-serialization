plugins {
    id("io.micronaut.build.internal.serde-module")
}

configurations.all {
    exclude("io.micronaut", "micronaut-jackson-databind")
    exclude("io.micronaut", "micronaut-jackson-core")
}

dependencies {
    annotationProcessor(mn.micronaut.inject.java)
    annotationProcessor(projects.micronautSerdeProcessor)

    api(libs.managed.jakarta.json.api)
    api(mn.micronaut.context)
    api(projects.micronautSerdeApi)
    implementation(projects.micronautSerdeSupport)
    implementation(libs.managed.eclipse.parsson)
    compileOnly(libs.graal.svm)
    compileOnly(mn.micronaut.jackson.databind)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testAnnotationProcessor(projects.micronautSerdeProcessor)

    testImplementation(mn.jackson.annotations)
    testImplementation(libs.managed.jakarta.json.bindApi)
    testImplementation(projects.micronautSerdeProcessor)
    testImplementation(projects.micronautSerdeTck)
    testImplementation(mn.micronaut.inject.java.test)
    testImplementation(mnTest.micronaut.test.junit5)
    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(mn.micronaut.http.client)
    testRuntimeOnly(
        "org.junit.jupiter:junit-jupiter-engine"
    )
    testCompileOnly(mn.micronaut.inject.groovy)
    testImplementation(mnTest.micronaut.test.spock)
    testImplementation(mnReactor.micronaut.reactor)
    testRuntimeOnly("org.yaml:snakeyaml")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
