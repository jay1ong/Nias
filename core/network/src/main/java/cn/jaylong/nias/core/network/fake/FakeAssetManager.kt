package cn.jaylong.nias.core.network.fake

import java.io.InputStream

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
fun interface FakeAssetManager {

    fun open(fileName: String): InputStream
}