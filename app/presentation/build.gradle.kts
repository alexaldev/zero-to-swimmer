plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.junit5)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.alexallafi.app.presentation"
    compileSdk = 37

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(projects.app.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.viewBindingDelegate)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit5.api)
    testImplementation(libs.junit5.engine)
    testImplementation(libs.junit5.params)
    testImplementation(libs.mockk)
    testImplementation(libs.assertk)
    implementation(libs.kotlinResult)
    implementation(libs.kotlinResult.coroutines)

    androidTestImplementation(libs.junit5.api)
    androidTestImplementation(libs.junit5.engine)
    androidTestImplementation(libs.junit5.params)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.assertk)
    androidTestImplementation(libs.coroutines.test)
}
