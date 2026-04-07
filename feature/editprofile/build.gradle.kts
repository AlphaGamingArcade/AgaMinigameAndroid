plugins {
    alias(libs.plugins.jetpack.ui.library)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.dokka)
}

android {
    namespace = "com.alphagamingarcade.feature.editprofile"
}

dependencies {
    // ... Modules
    implementation(project(":core:ui"))
    implementation(project(":data"))
}