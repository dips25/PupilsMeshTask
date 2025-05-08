import de.undercouch.gradle.tasks.download.Download

plugins {

    id ("de.undercouch.download")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")


}

extra.set("ASSET_DIR", "$projectDir/src/main/assets")


tasks.register<Download>("downloadModelFile") {
    println("✅ Running downloadModelFile task...")

    src("https://storage.googleapis.com/mediapipe-models/face_detector/blaze_face_short_range/float16/1/blaze_face_short_range.tflite")
    dest(file("${extra["ASSET_DIR"]}/face_detection_short_range.tflite"))
    overwrite(false)
}



android {
    namespace = "com.task.pupilsmeshtask"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.task.pupilsmeshtask"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

// import DownloadModels task


//apply(from = "download_models.gradle")

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.tasks.vision)

    implementation (libs.androidx.camera.core.v130)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle.v130)

    implementation (libs.androidx.camera.view)

    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

// Gson converter for Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.google.android.material:material:1.2.0-alpha06")
}

//tasks.named("preBuild").configure {
//    dependsOn("downloadModelFile")
//}