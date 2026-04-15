plugins {
    alias(libs.plugins.kotlin) apply (false)
    alias(libs.plugins.android.library) apply (false)
    alias(libs.plugins.android.application) apply (false)
    alias(libs.plugins.kotlin.serialization) apply (false)
    alias(libs.plugins.dagger.hilt.android) apply (false)
    alias(libs.plugins.kotlin.compose.compiler) apply (false)
    alias(libs.plugins.secrets) apply (false)
    alias(libs.plugins.gms) apply (false)
    alias(libs.plugins.ksp) apply (false)
    alias(libs.plugins.google.oss.licenses) apply (false)
    alias(libs.plugins.dokka)
}

dependencies {
    dokka(project(":app"))

    // ... Core
    dokka(project(":core:android"))
    dokka(project(":core:network"))
    "dokka"(project(":core:datastore"))
    dokka(project(":core:room"))
    dokka(project(":core:ui"))
    dokka(project(":core:data"))

    // ... Feature
    dokka(project(":feature:auth"))
    dokka(project(":feature:user"))
    dokka(project(":feature:settings"))
    dokka(project(":feature:gamedetail"))

    // ... Sync
    dokka(project(":sync"))

    // ... Dokka Plugins
    dokkaPlugin(libs.dokka.android.plugin)
    dokkaPlugin(libs.dokka.mermaid.plugin)
}

dokka {
    pluginsConfiguration.html {
        customAssets.from("docs/assets/logo-icon.svg")
        customStyleSheets.from("docs/assets/dokka.css")
        footerMessage.set("ALpha Gaming Arcade")
    }
}