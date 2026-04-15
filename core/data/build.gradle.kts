plugins {
    alias(libs.plugins.jetpack.library)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.dokka)
}

android {
    namespace = "com.alphagamingarcade.core.data"
}


dependencies {
    implementation(project(":core:android"))

    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
}