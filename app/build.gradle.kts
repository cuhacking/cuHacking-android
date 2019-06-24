import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlinx-serialization")
    id("io.fabric")
    id("com.squareup.sqldelight")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(ProjectVersions.COMPILE_SDK)
    defaultConfig {
        applicationId = "com.cuhacking.app"
        minSdkVersion(ProjectVersions.MIN_SDK)
        targetSdkVersion(ProjectVersions.TARGET_SDK)
        versionCode = 1
        versionName = "git describe --tag".execute().text.trim()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    implementation("androidx.appcompat:appcompat:${ProjectVersions.APPCOMPAT}")
    implementation("androidx.core:core-ktx:${ProjectVersions.KTX}")
    implementation("androidx.constraintlayout:constraintlayout:${ProjectVersions.CONSTRAINT_LAYOUT}")
    implementation("com.google.android.material:material:${ProjectVersions.MATERIAL}")
    implementation("androidx.fragment:fragment-ktx:${ProjectVersions.FRAGMENT}")
    implementation("androidx.lifecycle:lifecycle-extensions:${ProjectVersions.LIFECYCLE}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${ProjectVersions.LIFECYCLE}")
    implementation("androidx.navigation:navigation-fragment-ktx:${ProjectVersions.NAVIGATION}")
    implementation("androidx.navigation:navigation-ui-ktx:${ProjectVersions.NAVIGATION}")

    implementation("com.google.firebase:firebase-core:${ProjectVersions.FIREBASE_CORE}")
    implementation("com.google.firebase:firebase-common-ktx:${ProjectVersions.FIREBASE_CORE}")
    implementation("com.google.firebase:firebase-auth:${ProjectVersions.FIREBASE_AUTH}")
    implementation("com.google.firebase:firebase-messaging:${ProjectVersions.FIREBASE_CLOUD_MESSAGING}")
    implementation("com.google.firebase:firebase-ml-vision:${ProjectVersions.FIREBASE_VISION}")
    implementation("com.crashlytics.sdk.android:crashlytics:${ProjectVersions.FIREBASE_CRASHLYTICS}")

    implementation("com.squareup.sqldelight:android-driver:${ProjectVersions.SQLDELIGHT}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${ProjectVersions.COROUTINES}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${ProjectVersions.COROUTINES}")
    implementation("com.squareup.retrofit2:retrofit:${ProjectVersions.RETROFIT}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${ProjectVersions.RETROFIT_SERIALIZATION}")
    implementation("com.squareup.okhttp3:okhttp:${ProjectVersions.OKHTTP}")
    implementation("com.squareup.okhttp3:logging-interceptor:${ProjectVersions.OKHTTP}")
    implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:${ProjectVersions.MAPBOX}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${ProjectVersions.SERIALIZATION}")
    implementation("org.koin:koin-android:${ProjectVersions.KOIN}")
    implementation("org.koin:koin-androidx-scope:${ProjectVersions.KOIN}")
    implementation("org.koin:koin-androidx-viewmodel:${ProjectVersions.KOIN}")

    testImplementation("junit:junit:${ProjectVersions.JUNIT}")
    testImplementation("androidx.arch.core:core-testing:${ProjectVersions.LIFECYCLE}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${ProjectVersions.COROUTINES}")
    testImplementation("com.squareup.sqldelight:sqlite-driver:${ProjectVersions.SQLDELIGHT}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${ProjectVersions.OKHTTP}")
    testImplementation("org.koin:koin-test:${ProjectVersions.KOIN}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${ProjectVersions.ESPRESSO_CORE}")
    androidTestImplementation("androidx.test:rules:${ProjectVersions.TEST_RULES}")
    androidTestImplementation("androidx.test.ext:junit:${ProjectVersions.TEST_JUNIT}")
}

apply(plugin = "com.google.gms.google-services")
