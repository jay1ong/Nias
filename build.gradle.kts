// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
//    id("com.android.application") version "7.4.1" apply false
//    id("com.android.library") version "7.4.1" apply false
//    id("org.jetbrains.kotlin.android") version "1.8.0" apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.secrets) apply false
}