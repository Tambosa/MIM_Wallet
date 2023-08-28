plugins {
    id(Plugins.android_library)
    id(Plugins.android_kotlin)
}

android {
    namespace = "com.example.core_ui"

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
    //compose
    implementation(project.dependencies.platform(Libs.compose_bom))

    implementation(Libs.compose_runtime)
    implementation(Libs.compose_material3)
    implementation(Libs.compose_material)
    implementation(Libs.compose_activity)
    implementation(Libs.compose_ui_tooling)
}