package cn.jaylong.nias.core.model.data

import kotlinx.datetime.Instant


/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
data class NewsResource(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val headerImageUrl: String?,
    val publishDate: Instant,
    val type: NewsResourceType,
    val topics: List<Topic>
)