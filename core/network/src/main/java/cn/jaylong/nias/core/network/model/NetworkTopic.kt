package cn.jaylong.nias.core.network.model

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
@kotlinx.serialization.Serializable
data class NetworkTopic(
    val id: String,
    val name: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val url: String = "",
    val imageUrl: String = "",
    val followed: Boolean = false,
)