plugins {
    id(Plugins.android_library)
    id(Plugins.android_kotlin)
}

android {
    namespace = "com.example.core_network"

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
    //retrofit
    implementation(Libs.retrofit)
    implementation(Libs.converter_gson)
}