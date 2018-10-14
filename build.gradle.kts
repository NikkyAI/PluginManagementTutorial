import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.0-rc-146"
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
    compile(kotlin("stdlib-jdk8"))

    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.8.0-rc13")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}