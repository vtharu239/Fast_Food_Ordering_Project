plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android") version "2.47" apply false
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.project_food"
    compileSdk = 34

    dataBinding{
        enable = true
    }
    viewBinding{
        enable = true
    }
    defaultConfig {
        applicationId = "com.example.project_food"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures{
       viewBinding =true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Recycler View (including List adapter)
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    //ViewModel & Livedata
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.7.0")
    //Navigation Component
    implementation ("androidx.navigation:navigation-fragment:2.7.7")
    implementation ("androidx.navigation:navigation-ui:2.7.7")
    //Glide Library


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-core:21.1.1")
    //implementation("com.google.firebase:firebase-firestore:24.10.3")

    //implementation("com.firebaseui:firebase-ui-database:7.1.1")
    // FirebaseUI for Firebase Realtime Database
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")

    // FirebaseUI for Cloud Firestore
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")

    // FirebaseUI for Firebase Auth
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")

    // FirebaseUI for Cloud Storage
    implementation ("com.firebaseui:firebase-ui-storage:8.0.2")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.squareup.picasso:picasso:2.5.2")

    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // Rounded ImageView
    implementation ("com.makeramen:roundedimageview:2.3.0")

    //Banner
    implementation ("com.airbnb.android:lottie:4.2.2")

    // Finger
    implementation("androidx.biometric:biometric:1.1.0")
}
