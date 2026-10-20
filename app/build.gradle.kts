plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.alexallafi.zerotoswimmer"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.alexallafi.zerotoswimmer"
        minSdk = 26
        targetSdk = 37
        versionCode =
            libs.versions.projectVersionCode
                .get()
                .toInt()
        versionName = libs.versions.projectVersionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        debug {
            isProfileable = true
        }

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

    implementation(projects.app.presentation)
    implementation(projects.app.data)
    implementation(projects.app.domain)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.splashscreen)
    implementation(libs.koin.test)
    implementation(libs.kotlinResult)
    implementation(libs.kotlinResult.coroutines)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.viewBindingDelegate)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
