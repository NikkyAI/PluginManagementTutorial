import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec

fun PluginDependenciesSpec.kotlinJvm() {
    kotlin("jvm").version(Versions.kotlin)
}

// pre 113
@Suppress("unused")
fun PluginDependenciesSpec.serialization() {
    id("kotlinx-serialization").version(Versions.serialization)
}