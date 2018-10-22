# Plugin Management Tutorial for Gradle and Kotlin

## Step 0

Create a new Gradle project in idea  
I recommend to enable the option `Kotlin DSL buildscript` 
but it is not required for this tutorial 9as long as you can translate between groovy and kotlin a little)

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

### kotlin.serialization

```kotlin
plugins {
    id("kotlinx-serialization") version "1.3.0-rc-190" // match kotlin version
}
```

```kotlin
pluginManagement {
    repositories {
        maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
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
```

### kotlin.serialization (pre 1.3)

```kotlin
plugins {
    id("kotlinx-serialization") version "0.6.2"
}
```

```kotlin
pluginManagement {
    repositories {
        maven(url = "https://kotlin.bintray.com/kotlinx")
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

### Translating buildscript to plugin

lets take as example a barebones android buildscript

```kotlin
buildscript {
    repositories {
        google()
        jcenter()
    }
    
    dependencies {
        classpath("com.android.tools.build:gradle:3.2.0")
    }
}
apply {
  plugin("com.android.application")
} 
```

this can be translated into

```kotlin
plugins {
    id("com.android.application") version "3.2.0"
}
```

and 

```kotlin

pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}
```

### Using `buildSrc` for versions and more

see https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources

TL:DR

you can put constants for versions in the submodule `buildSrc`
and access those verions anywhere in your build logic, including the plugin block, and pluginManagement

see [buildSrc](https://github.com/NikkyAI/PluginManagementTutorial/tree/master/buildSrc) for more details

you can even make one off gradle plugins for the rest of the buildLogic without ever needing to deploy it anywhere
