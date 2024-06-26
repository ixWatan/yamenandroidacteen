plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.yamenandroidacteen"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.yamenandroidacteen"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation("com.google.firebase:firebase-messaging:21.1.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.17")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.github.krokyze:ucropnedit:2.2.8")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.libraries.places:places:3.1.0")
    implementation("com.google.android.gms:play-services-places:17.0.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.afollestad.material-dialogs:core:3.3.0")








    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}