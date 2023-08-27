plugins {
    id(Plugins.android_application)
    id(Plugins.android_kotlin)
    id(Plugins.google_ksp)
    id(Plugins.google_daggerHilt)
    id(Plugins.google_secret)
    id(Plugins.kotlin_kapt)
}

android {
    namespace = "com.aroman.mimwallet"

    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        applicationId = "com.aroman.mimwallet"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = ConfigData.javaVerstion
        targetCompatibility = ConfigData.javaVerstion
    }
    kotlinOptions {
        jvmTarget = ConfigData.jvmTarget
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ConfigData.kotlinCompilerExtensionVersion
    }
}

dependencies {
    //modules
    implementation(project(":core_ui"))

    //compose
    implementation(project.dependencies.platform(Libs.compose_bom))

    implementation(Libs.compose_runtime)
    implementation(Libs.compose_material3)
    implementation(Libs.compose_material)
    implementation(Libs.compose_activity)
    implementation(Libs.compose_navigation)
    implementation(Libs.compose_ui_tooling)

    //3rd party compose
    implementation(Libs.compose_dialogs)
    implementation(Libs.compose_dialogs_clock)
    implementation(Libs.lottie_compose)

    //retrofit
    implementation(Libs.retrofit)
    implementation(Libs.converter_gson)
    implementation(Libs.okhttp)
    implementation(Libs.logging_interceptor)

    //Dagger - Hilt
    implementation(Libs.hilt_android)
    kapt(Libs.hilt_android_compiler)
    kapt(Libs.androidx_hilt_compiler)
    implementation(Libs.androidx_hilt_navigation_compose)

    //Navigation
    implementation(Libs.androidx_navigation_fragment_ktx)
    implementation(Libs.androidx_navigation_ui_ktx)

    //room
    implementation(Libs.androidx_room_runtime)
    implementation(Libs.androidx_room_ktx)
    ksp(Libs.androidx_room_compiler)

    // Coroutine Lifecycle Scopes
    implementation(Libs.androidx_lifecycle_viewmodel_ktx)
    implementation(Libs.androidx_lifecycle_runtime_ktx)

    implementation(Libs.androidx_core_splashscreen)

    implementation(Libs.androidx_ktx)
    implementation(Libs.androidx_appcompat)
    implementation(Libs.material)
}