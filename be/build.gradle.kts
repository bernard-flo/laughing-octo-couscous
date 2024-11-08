plugins {
    id("buildlogic.kotlin-common-conventions")
    kotlin("jvm")
    kotlin("plugin.spring") version "2.0.21"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.serialization") version "2.0.21"
}

dependencies {

    implementation(project(":shared"))

    implementation("org.springframework.boot:spring-boot-starter-websocket") {
        exclude("org.springframework.boot", "spring-boot-starter-json")
    }

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    implementation("com.opencsv:opencsv:5.9")
}

tasks {
    val copyJsDistribution = register<Copy>("copyJsDistribution") {
        from(project(":fe").tasks.named("jsBrowserDistribution"))
        into(layout.buildDirectory.dir("resources/main/static").get())
    }
    named("processResources") {
        dependsOn(copyJsDistribution)
    }
}
