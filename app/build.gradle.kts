plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.animetv"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.animetv"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    // Jetpack Compose for TV
    implementation("androidx.tv:tv-foundation:1.0.0-alpha11")
    implementation("androidx.tv:tv-material:1.0.0")

    // Core & Compose
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")

    // Image loading (Coil)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Media3 (ExoPlayer) for HLS & MP4 video playback
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
}
