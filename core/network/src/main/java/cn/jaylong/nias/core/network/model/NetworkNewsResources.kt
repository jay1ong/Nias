package cn.jaylong.nias.core.network.model

import cn.jaylong.nias.core.model.data.NewsResourceType
import cn.jaylong.nias.core.network.model.util.InstantSerializer
import cn.jaylong.nias.core.network.model.util.NewsResourceTypeSerializer
import kotlinx.datetime.Instant

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
@kotlinx.serialization.Serializable
data class NetworkNewsResources(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val headerImageUrl: String,
    @kotlinx.serialization.Serializable(InstantSerializer::class)
    val publishDate: Instant,
    @kotlinx.serialization.Serializable(NewsResourceTypeSerializer::class)
    val type: NewsResourceType,
    val topics: List<String> = listOf(),
)

@kotlinx.serialization.Serializable
data class NetworkNewsResourceExpanded(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val headerImageUrl: String,
    @kotlinx.serialization.Serializable
    val publishDate: Instant,
    @kotlinx.serialization.Serializable
    val type: NewsResourceType,
    val topics: List<NetworkTopic> = listOf(),
)