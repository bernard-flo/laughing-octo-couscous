plugins {
    id("buildlogic.kotlin-common-conventions")
    kotlin("jvm")
    kotlin("plugin.spring") version "2.0.21"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-websocket")
}
