// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "2.3.12" apply false
    id("androidx.room3") version "3.0.3" apply false
    id("com.google.dagger.hilt.android") version "2.60.1" apply false
}