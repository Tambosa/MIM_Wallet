plugins {
    id(Plugins.android_library)
    id(Plugins.android_kotlin)
    id(Plugins.google_daggerHilt)
    id(Plugins.kotlin_kapt)
}

android {
    namespace = "com.example.feature_coin_insert"
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(":data_network"))
    implementation(project(":core_ui"))

    //compose
    implementation(project.dependencies.platform(Libs.compose_bom))

    implementation(Libs.compose_runtime)
    implementation(Libs.compose_material3)
    implementation(Libs.compose_material)
    implementation(Libs.compose_activity)
    implementation(Libs.compose_ui_tooling)

    //Dagger - Hilt
    implementation(Libs.hilt_android)
    kapt(Libs.hilt_android_compiler)
    kapt(Libs.androidx_hilt_compiler)
    implementation(Libs.androidx_hilt_navigation_compose)

    // Coroutine Lifecycle Scopes
    implementation(Libs.androidx_lifecycle_viewmodel_ktx)
    implementation(Libs.androidx_lifecycle_runtime_ktx)
}