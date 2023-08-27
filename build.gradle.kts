// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.android_application) version VersionPlugins.androidGradlePlugin apply false
    id(Plugins.android_kotlin) version VersionPlugins.kotlinAndroidPlugin apply false
    id(Plugins.google_ksp) version VersionPlugins.googleKspPlugin apply false
    id(Plugins.google_daggerHilt) version VersionPlugins.googleDaggerHiltPlugin apply false
    id(Plugins.google_secret) version VersionPlugins.googleSecretPlugin apply false
    id(Plugins.android_library) version VersionPlugins.androidLibraryPlugin apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}