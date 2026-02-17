plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.2.0"
}

android {
    namespace = "com.dev.timeflow"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dev.timeflow"
        minSdk = 27
        targetSdk = 36
        versionCode = 5
        versionName = "1.2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            ndk {
                debugSymbolLevel = "FULL"
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.work:work-runtime-ktx:2.11.0")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("androidx.compose.material3:material3-android:1.4.0-alpha14")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom) )
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material.icons.extended)
    implementation("com.kizitonwose.calendar:compose:2.7.0")
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
    implementation("com.composables:icons-lucide:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("androidx.room:room-runtime:2.7.2")
    ksp(libs.androidx.room.compiler)
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")
    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.room:room-ktx:2.7.2")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

}