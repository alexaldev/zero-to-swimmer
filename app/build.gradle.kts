plugins {
    alias(libs.plugins.android.application)
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
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    implementation(projects.app.presentation)
    implementation(projects.app.data)
    implementation(projects.app.domain)
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
