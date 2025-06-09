plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.junit5)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.alexallafi.app.data"
    compileSdk = 34

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
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {

    implementation(projects.app.domain)

    implementation(libs.kotlinx.serialization)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.core.ktx)

    testImplementation(libs.junit5.api)
    testImplementation(libs.junit5.engine)
    testImplementation(libs.junit5.params)
    testImplementation(libs.mockk)
    testImplementation(libs.assertk)

    androidTestImplementation(libs.junit5.api)
    androidTestImplementation(libs.junit5.engine)
    androidTestImplementation(libs.junit5.params)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.assertk)
    androidTestImplementation(libs.coroutines.test)
}