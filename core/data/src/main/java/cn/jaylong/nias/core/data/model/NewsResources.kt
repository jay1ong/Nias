package cn.jaylong.nias.core.data.model

import cn.jaylong.nias.core.database.model.NewsResourceEntity
import cn.jaylong.nias.core.database.model.NewsResourceTopicCrossRef
import cn.jaylong.nias.core.database.model.TopicEntity
import cn.jaylong.nias.core.network.model.NetworkNewsResourceExpanded
import cn.jaylong.nias.core.network.model.NetworkNewsResources

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/13
 */
fun NetworkNewsResources.asEntity() = NewsResourceEntity(
    id = id,
    title = title,
    content = content,
    url = url,
    headerImageUrl = headerImageUrl,
    publishDate = publishDate,
    type = type
)

fun NetworkNewsResourceExpanded.asEntity() = NewsResourceEntity(
    id = id,
    title = title,
    content = content,
    url = url,
    headerImageUrl = headerImageUrl,
    publishDate = publishDate,
    type = type
)

fun NetworkNewsResources.topicEntityShells() =
    topics.map { topicId ->
        TopicEntity(
            id = topicId,
            name = "",
            url = "",
            imageUrl = "",
            shortDescription = "",
            longDescription = "",
        )
    }

fun NetworkNewsResources.topicCrossReferences(): List<NewsResourceTopicCrossRef> =
    topics.map { topicId ->
        NewsResourceTopicCrossRef(
            newsResourceId = id,
            topicId = topicId
        )
    }