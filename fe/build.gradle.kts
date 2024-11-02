import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    id("buildlogic.kotlin-common-conventions")
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "2.0.21"
}

kotlin {

    sourceSets {

        commonMain.dependencies {
            implementation(project(":shared"))
        }

        jsMain.dependencies {
            implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.820"))
            implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
            implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
            implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")
            implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")
            implementation("org.jetbrains.kotlin-wrappers:kotlin-mui-material")
            implementation(npm("@stomp/stompjs", "7.0.0"))

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
        }

    }

    js {
        browser()
        binaries.executable()
    }

}

tasks.withType<KotlinJsCompile>().configureEach {
    compilerOptions {
        target = "es2015"
    }
}
