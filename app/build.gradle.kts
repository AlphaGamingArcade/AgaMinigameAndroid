@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

val keystorePropertiesFile: File = rootProject.file("keystore.properties")

plugins {
    alias(libs.plugins.jetpack.application)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.dokka)
}

android {
    // ... Application Version ...
    val majorUpdateVersion = 1
    val minorUpdateVersion = 2
    val patchVersion = 3

    val mVersionCode = majorUpdateVersion.times(10_000)
        .plus(minorUpdateVersion.times(100))
        .plus(patchVersion)

    val mVersionName = "$majorUpdateVersion.$minorUpdateVersion.$patchVersion"
    val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_a")
    val currentTime = LocalDateTime.now().format(formatter)

    defaultConfig {
        versionCode = mVersionCode
        versionName = mVersionName
        applicationId = "com.alphagamingarcade.compose"
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            applicationVariants.all {
                outputs.all {
                    (this as BaseVariantOutputImpl).outputFileName =
                        rootProject.name.replace(" ", "_") + "_" +
                                (buildType.name + "_v") +
                                (versionName + "_") +
                                "${currentTime}.apk"
                    println(outputFileName)
                }
            }
            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                println(
                    "keystore.properties file not found. Using debug key. Read more here: " +
                            "######",
                )
                signingConfigs.getByName("debug")

            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    androidResources {
        generateLocaleConfig = true
    }

    namespace = "com.alphagamingarcade.compose"
}

dependencies {
    // ... Core
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:data"))

    // ... Features
    implementation(project(":feature:auth"))
    implementation(project(":feature:games"))
    implementation(project(":feature:gamedetail"))
    implementation(project(":feature:browse"))
    implementation(project(":feature:favorite"))
    implementation(project(":feature:user"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:legal"))
    implementation(project(":feature:play"))

    // ... Sync
    implementation(project(":sync"))

    // ... Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // ... OSS Licenses
    implementation(libs.google.oss.licenses)

    // ... LeakCanary
    // TODO: Comment out the following line to disable LeakCanary
    debugImplementation(libs.leakcanary.android)
    implementation(libs.material)
}