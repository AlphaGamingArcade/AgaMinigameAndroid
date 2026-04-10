plugins {
    alias(libs.plugins.jetpack.library)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.dokka)
}

android {
    namespace = "com.alphagamingarcade.sync"
}

dependencies {
    // ... Modules
    implementation(project(":core:android"))
    implementation(project(":core:data"))

    // ... Work Manager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
}