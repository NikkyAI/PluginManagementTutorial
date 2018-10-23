pluginManagement {
    repositories {
        eap() // custom repo loaded from buildSrc
        maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
        mavenLocal()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}

rootProject.name = "pluginManagementSample"
