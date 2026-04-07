import org.gradle.kotlin.dsl.compileOnly
import org.gradle.kotlin.dsl.gradlePlugin
import org.gradle.kotlin.dsl.libs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.alphagamingarcade.build.logic"

val javaVersion = libs.versions.java.get().toInt()

java {
    sourceCompatibility = JavaVersion.values()[javaVersion - 1]
    targetCompatibility = JavaVersion.values()[javaVersion - 1]
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.valueOf("JVM_$javaVersion"))
    }
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("library") {
            id = "com.alphagamingarcade.library"
            implementationClass = "LibraryConventionPlugin"
        }
        register("uiLibrary") {
            id = "com.alphagamingarcade.ui.library"
            implementationClass = "UiLibraryConventionPlugin"
        }
        register("application") {
            id = "com.alphagamingarcade.application"
            implementationClass = "ApplicationConventionPlugin"
        }
        register("daggerHilt") {
            id = "com.alphagamingarcade.dagger.hilt"
            implementationClass = "DaggerHiltConventionPlugin"
        }
        register("firebase") {
            id = "com.alphagamingarcade.firebase"
            implementationClass = "FirebaseConventionPlugin"
        }
        register("dokka") {
            id = "com.alphagamingarcade.dokka"
            implementationClass = "DokkaConventionPlugin"
        }
    }
}