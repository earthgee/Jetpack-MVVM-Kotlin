/*
 * Copyright(2018-present KunMinX)
 *
 * Licensed(under the Apache License, Version 2.0 (the "License");)
 * you(may not use this file except in compliance with the License.)
 * You(may obtain a copy of the License at)
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless(required by applicable law or agreed to in writing, software)
 * distributed(under the License is distributed on an "AS IS" BASIS,)
 * WITHOUT(WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.)
 * See(the License for the specific language governing permissions and)
 * limitations(under the License.)
 */
plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = AndroidVersions.AppTargetSdk
    defaultConfig {
        minSdk = AndroidVersions.AppMinSdk
        targetSdk = AndroidVersions.AppTargetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
    }
}


dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //常用基础组件
    api("androidx.appcompat:appcompat:1.6.1")
    api("org.jetbrains:annotations:24.0.1")
    api("androidx.navigation:navigation-runtime:2.5.3")

    api("com.google.android.material:material:1.9.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.recyclerview:recyclerview:1.3.1")

    //常用架构组件，已按功能提取分割为多个独立库，可按需选配

    api("com.github.KunMinX:MVI-Dispatcher:7.6.0")
    api("com.github.KunMinX:UnPeek-LiveData:7.8.0")
    api("com.github.KunMinX:Smooth-Navigation:v4.0.0")
    api("com.github.KunMinX.Strict-DataBinding:binding_state:6.2.0")
    api("com.github.KunMinX.Strict-DataBinding:strict_databinding:6.2.0")
    api("com.github.KunMinX.Strict-DataBinding:binding_recyclerview:6.2.0")

    //常用数据、媒体组件

    api("com.github.bumptech.glide:glide:4.16.0")

    api("com.google.code.gson:gson:2.10.1")
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.11.0")
    api("com.squareup.okhttp3:okhttp:4.11.0")

    api("io.reactivex.rxjava2:rxandroid:2.1.1")
    api("io.reactivex.rxjava2:rxjava:2.2.21")
}