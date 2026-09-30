plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.dev.timeflow"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.dev.timeflow"
        minSdk = 27
        targetSdk = 36
        versionCode = 7
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
        debug {
            manifestPlaceholders["appName"] = "Timeflow(Debug)"
            applicationIdSuffix = ".debug"
            buildConfigField(
                type = "String",
                name = "VERSION_CODE",
                value = "\"7\""
            )

            buildConfigField(
                type = "String",
                name = "VERSION_NAME",
                value = "\"1.2.1\""
            )

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.kolor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom) )
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.compose)
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.icons.lucide)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.core.splashscreen.v100)
    implementation(libs.coil.compose)

}