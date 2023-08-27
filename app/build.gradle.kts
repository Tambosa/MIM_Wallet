plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.daggerHilt)
    alias(libs.plugins.google.secret)
    id("kotlin-kapt")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aroman.mimwallet"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                argument(key = "room.schemaLocation", value = "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    namespace = "com.aroman.mimwallet"
}

dependencies {
    //modules
    implementation(project(":core_ui"))

    //compose
    implementation(project.dependencies.platform(libs.compose.bom))

    implementation(libs.compose.runtime)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.compose.activity)
    implementation(libs.compose.navigation)
    implementation(libs.compose.ui.tooling)

    //3rd party compose
    implementation(libs.core)
    implementation(libs.clock)
    implementation(libs.lottie.compose)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}