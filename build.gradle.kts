import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    // only for pre 1.3 necessary
//    id("kotlinx-serialization") version "0.8.0-rc13"
}

group = "moe.nikky"
version = "1.0-SNAPSHOT"

repositories {
    maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8", Versions.kotlin))

    compile(group = "org.jetbrains.kotlinx", name ="kotlinx-serialization-runtime", version = Versions.serialization)
    compile(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = Versions.coroutines)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}