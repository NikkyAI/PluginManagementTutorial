pluginManagement {
    repositories {
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
        // only for pre 1.3 necessary
//        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        // mavenLocal()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.jetbrains.kotlin.jvm") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            // only for pre 1.3
//            if (requested.id.id == "kotlinx-serialization") {
//                useModule("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:${requested.version}")
//            }
        }
    }
}

rootProject.name = "pluginManagementSample"
