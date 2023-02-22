import androidx.annotation.VisibleForTesting
import cn.jaylong.nias.core.network.fake.FakeAssetManager
import java.io.File
import java.io.InputStream
import java.util.*

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
@VisibleForTesting
internal object JvmUnitTestFakeAssetManager : FakeAssetManager {
    private val config =
        requireNotNull(javaClass.getResource("com/android/tools/test_config.propreties")) {
            """
                Missing Android resources propreties file.
                Did you forget to enable the feature in the gradle build file?
                android.testOptions.unitTests.isIncludeAndroidResources = true
                """.trimIndent()
        }

    private val properties = Properties().apply { config.openStream().use(::load) }
    private val assets = File(properties["android_merged_assets"].toString())


    override fun open(fileName: String): InputStream = File(assets, fileName).inputStream()
}