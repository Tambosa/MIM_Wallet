plugins {
    id(Plugins.android_library)
    id(Plugins.android_kotlin)
    id(Plugins.kotlin_kapt)
    id(Plugins.google_secret)
}

android {
    namespace = "com.example.data_network"

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
    compileOptions {
        sourceCompatibility = ConfigData.javaVerstion
        targetCompatibility = ConfigData.javaVerstion
    }
    kotlinOptions {
        jvmTarget = ConfigData.jvmTarget
    }
}

dependencies {
    implementation(project(":core_network"))

    //Dagger - Hilt
    implementation(Libs.hilt_android)
    kapt(Libs.hilt_android_compiler)
    kapt(Libs.androidx_hilt_compiler)

    // Coroutine Lifecycle Scopes
    implementation(Libs.androidx_lifecycle_viewmodel_ktx)
    implementation(Libs.androidx_lifecycle_runtime_ktx)
}