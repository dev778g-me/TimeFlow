plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.2.0"
}

android {
    namespace = "com.dev.timeflow"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dev.timeflow"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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
    //Glance Widget
    implementation(libs.androidx.glance)
    // For AppWidgets support
    implementation(libs.androidx.glance.appwidget)

    //navigation
    //lucide icons
    implementation("com.composables:icons-lucide:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("androidx.room:room-runtime:2.7.2")
    ksp(libs.androidx.room.compiler)
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")
    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.3.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.3.0")
    implementation("androidx.room:room-ktx:2.7.2")
    implementation ("app.rive:rive-android:9.6.5")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation( "androidx.startup:startup-runtime:1.1.1")
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

}