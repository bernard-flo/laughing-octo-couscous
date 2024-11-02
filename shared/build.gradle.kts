plugins {
    id("buildlogic.kotlin-common-conventions")
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "2.0.21"
}

kotlin {

    jvm()

    js {
        browser()
    }

    sourceSets {

        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
        }

    }

}
