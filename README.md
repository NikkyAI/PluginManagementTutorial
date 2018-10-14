# Plugin Management Tutorial for Gradle and Kotlin

## Step 0

Create a new Gradle project in idea
i recommend to enable the option `Kotlin DSL buildscript` 
but it is not required for this tutorial

## Step 1

assuming your plugin block looks something like this
```kotlin
plugins {
    kotlin("jvm") version "1.2.71"
}
```

update the version of the kotlin plugin to the latest rc for 1.3


```kotlin
plugins {
    kotlin("jvm") version "1.3.0-rc-152"
}
```

now you will face the `CONFIGURE FAILED` but do not loose hope

lets look at the error in more detail

```
CONFIGURE FAILED in 2s
Plugin [id: 'org.jetbrains.kotlin.jvm', version: '1.3.0-rc-146'] was not found in any of the following sources:

- Gradle Core Plugins (plugin is not in 'org.gradle' namespace)
- Plugin Repositories (could not resolve plugin artifact 'org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.3.0-rc-146')
  Searched in the following repositories:
    Gradle Central Plugin Repository
```

we see the id of the plugin that gradle tries to find `org.jetbrains.kotlin.jvm`
the version `1.3.0-rc-146` and in which repositories gradle looks for them

but.. kotlin eap versions are not in the central plugin repo you say? correct
they live here: http://dl.bintray.com/kotlin/kotlin-eap

so all we need to do is tell gradle where to look and which arifact to use

## Step 2

create `settings.gradle.kts` or `settings.gradle` or reuse the existing settings file

add to the beginning of the file (before `rootProject.name = `)

```kotlin
pluginManagement {
    repositories {
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.jetbrains.kotlin.jvm") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
        }
    }
}
```

## Step 3

do not forget to also update your compile dependencies and repositories

```kotlin
repositories {
    maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
    mavenCentral()
}
```

## Step 4 - Other plugins

### kotlin.serialization (pre 1.3)
```kotlin
plugins {
    id("kotlinx-serialization") version "0.8.0-rc13"
}
```

```kotlin
pluginManagement {
    repositories {
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:${requested.version}")
            }
        }
    }
}
```

### Testing Plugins from mavenLocal

you cna add `mavenLocal()` to the repositories and load plugins from there too
makes testing custom plugins much less painful and sometimes faster..
when its on mavenLocal gradle alos always uses the version on htere.. no need for `SNASHOT` versions
although nobody is stopping you

### The default maven coordinates

`useModule` is only necessary if the maven coordinate is not the expected default:
`$puginId:$pluginId:version`

for the imaginary plugin `id("kotlinx-serialization") version "0.8.0-rc13"`
gradle would look for `kotlinx-serialization:kotlinx-serialization:0.8.0-rc13`

`id("my.plugin") version "1.0-SNAPSHOT"` -> `my.plugin:my.plugin:1.0-SNAPSHOT`

sidenote: since gradle 4.10.2 gradle supports `-SNAPHOT` versions on plugins
before that the plugin would potentially not update even if a new snapshot was pushed to maven

