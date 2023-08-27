// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.google.daggerHilt) apply false
    alias(libs.plugins.google.secret) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}