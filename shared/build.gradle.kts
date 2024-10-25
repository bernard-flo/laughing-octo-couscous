plugins {
    id("buildlogic.kotlin-common-conventions")
    kotlin("multiplatform")
}

kotlin {

    jvm()

    js {
        browser()
    }

}
