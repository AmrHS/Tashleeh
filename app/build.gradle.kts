plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.autoparts.tashleeh"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.autoparts.tashleeh"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation ("com.google.firebase:firebase-core:18.0.0")
    implementation ("com.google.firebase:firebase-database:19.6.0")
    implementation ("com.google.firebase:firebase-storage:19.2.1")
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")
    implementation (platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("io.github.pilgr:paperdb:2.7.2")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-messaging")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}
