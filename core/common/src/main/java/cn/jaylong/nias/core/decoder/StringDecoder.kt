package cn.jaylong.nias.core.decoder

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
interface StringDecoder {

    fun decodeString(encodedString: String): String
}