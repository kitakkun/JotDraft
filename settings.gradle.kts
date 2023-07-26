pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "NoteApp"
include(":app")
include(":finder")
include(":data")
include(":customview")
include(":navigation")
include(":editor")
