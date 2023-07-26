plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

apply(from = "${project.rootDir}/gradle/common.gradle")

android {
    namespace = "com.github.kitakkun.noteapp.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso)

    implementation(libs.room.common)
    ksp(libs.room.compiler.ksp)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)

    // Preferences DataStore (SharedPreferences like APIs)
    implementation(libs.datastore.preferences)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.gson)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
}
