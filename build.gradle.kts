// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//    repositories {
//        mavenCentral()
//        google()
//        maven { url 'https://jitpack.io'}
//    }
//    dependencies {
//        classpath ("com.android.tools.build:gradle:7.0.4") // Replace with the desired version
//    }
//}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.0.4") // Replace with the desired version
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0") // Replace with the desired version
    }
}



plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}