plugins {
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.jacoco")
    id("nowinandroid.android.hilt")
}

android {
    namespace = "cn.jaylong.nias.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}