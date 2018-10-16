import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    id("kotlinx-serialization") version Versions.kotlin
}

group = "moe.nikky"
version = "1.0-SNAPSHOT"

repositories {
    maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
    maven(url = "https://kotlin.bintray.com/kotlinx")
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8", Versions.kotlin))

    compile(group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-runtime", version = Versions.serialization)
    compile(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = Versions.coroutines)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}