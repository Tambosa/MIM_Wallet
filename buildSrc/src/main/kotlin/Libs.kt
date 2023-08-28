object Libs {
    //compose
    const val compose_bom = "androidx.compose:compose-bom:${VersionsLibs.composeBOM}"
    const val compose_runtime = "androidx.compose.runtime:runtime"
    const val compose_material3 = "androidx.compose.material3:material3"
    const val compose_material = "androidx.compose.material:material"
    const val compose_activity = "androidx.activity:activity-compose"
    const val compose_navigation = "androidx.navigation:navigation-compose"
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling-preview"

    //3rd party compose
    const val compose_dialogs_clock =
        "com.maxkeppeler.sheets-compose-dialogs:clock:${VersionsLibs.clock}"
    const val compose_dialogs = "com.maxkeppeler.sheets-compose-dialogs:core:${VersionsLibs.clock}"
    const val lottie_compose = "com.airbnb.android:lottie-compose:${VersionsLibs.lottie_compose}"

    //retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${VersionsLibs.converter_gson}"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:${VersionsLibs.converter_gson}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${VersionsLibs.ok_http}"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${VersionsLibs.ok_http}"

    //Dagger - Hilt
    const val hilt_android = "com.google.dagger:hilt-android:${VersionsLibs.hilt_android_compiler}"
    const val hilt_android_compiler =
        "com.google.dagger:hilt-android-compiler:${VersionsLibs.hilt_android_compiler}"
    const val androidx_hilt_compiler = "androidx.hilt:hilt-compiler:${VersionsLibs.hilt_compiler}"
    const val androidx_hilt_navigation_compose =
        "androidx.hilt:hilt-navigation-compose:${VersionsLibs.hilt_navigation_compose}"

    //Navigation
    const val androidx_navigation_ui_ktx =
        "androidx.navigation:navigation-ui-ktx:${VersionsLibs.navigation_ui_ktx}"
    const val androidx_navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${VersionsLibs.navigation_ui_ktx}"

    //room
    const val androidx_room_compiler = "androidx.room:room-compiler:${VersionsLibs.room_compiler}"
    const val androidx_room_ktx = "androidx.room:room-ktx:${VersionsLibs.room_compiler}"
    const val androidx_room_runtime = "androidx.room:room-runtime:${VersionsLibs.room_compiler}"

    // Coroutine Lifecycle Scopes
    const val androidx_lifecycle_runtime_ktx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${VersionsLibs.lifecycle_runtime_ktx}"
    const val androidx_lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${VersionsLibs.lifecycle_runtime_ktx}"

    const val androidx_core_splashscreen =
        "androidx.core:core-splashscreen:${VersionsLibs.core_splashscreen}"

    const val material = "com.google.android.material:material:${VersionsLibs.material}"
    const val androidx_appcompat = "androidx.appcompat:appcompat:${VersionsLibs.appcompat}"
    const val androidx_ktx = "androidx.core:core-ktx:${VersionsLibs.ktx}"
}