plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.library.jacoco")
}

android {
    namespace = "cn.jaylong.nias.feature.bookmarks"
}

dependencies {
    implementation(libs.androidx.compose.material3.windowSizeClass)
}