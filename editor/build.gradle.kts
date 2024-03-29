plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

apply(from = "${project.rootDir}/gradle/common.gradle")

android {
    namespace = "com.github.kitakkun.noteapp.editor"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":data"))
    implementation(project(":customview"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    debugImplementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material.icons.extended)

    implementation(libs.navigation.compose)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.colorpicker.compose)

    testImplementation(libs.junit)
    testImplementation(libs.junit.ext)
}
