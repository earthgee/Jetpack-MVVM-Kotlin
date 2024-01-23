plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.earthgee.puremusic"
    compileSdk = AndroidVersions.AppTargetSdk
    defaultConfig {
        applicationId = "com.earthgee.puremusic"
        minSdk = AndroidVersions.AppMinSdk
        targetSdk = AndroidVersions.AppTargetSdk
        versionCode = AppVersions.AppVersionCode
        versionName = AppVersions.AppVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val debug by getting {
            applicationIdSuffix = ".debug"
            manifestPlaceholders["APP_NAME"] = "@string/app_name_debug"
        }

        val release by getting {
            manifestPlaceholders["APP_NAME"] = "@string/app_name"
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(project(":architecture"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("org.slf4j:slf4j-android:1.7.36")
    implementation("com.sothree.slidinguppanel:library:3.4.0")
    implementation("com.github.KunMinX:Jetpack-MusicPlayer:5.2.0")
    implementation("com.github.KunMinX.KeyValueX:keyvalue:3.7.0-beta")
    kapt("com.github.KunMinX.KeyValueX:keyvalue-compiler:3.7.0-beta")
}
