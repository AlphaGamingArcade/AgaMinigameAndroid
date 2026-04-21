pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("com.gradle.develocity") version ("4.2.1")
}

develocity {
    buildScan {
        publishing.onlyIf { !System.getenv("CI").isNullOrEmpty() }
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}

rootProject.name = "Jetpack"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// ... App
include(":app")

// ... Core
include(":core:android")
include(":core:data")
include(":core:network")
include(":core:datastore")
include(":core:room")
include(":core:ui")
include(":core:model")

// ... Feature
include(":feature:auth")
include(":feature:browse")
include(":feature:favorite")
include(":feature:settings")
include(":feature:user")
include(":feature:changepassword")
include(":feature:gamedetail")
include(":feature:games")
include(":feature:editprofile")
include(":feature:legal")


// ... Sync
include(":sync")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    Jetpack requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}
