package cn.jaylong.nias.feature.interests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import cn.jaylong.nias.core.domain.model.FollowableTopic

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
@Composable
fun TopicsTabContent(
    topics: List<FollowableTopic>,
    onTopicClick: (String) -> Unit,
    onFollowableTopicClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .testTag("interests:topics"),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        topics.forEach { followableTopic ->
            val topicId = followableTopic.topic.id
            item(key = topicId) {
                InterestsItem(
                    name = followableTopic.topic.name,
                    following = followableTopic.isFollowed,
                    topicImageUrl = followableTopic.topic.imageUrl,
                    onClick = { onTopicClick(topicId) },
                    onFollowButtonClick = { onFollowableTopicClick(topicId, it) },
                )
            }
        }
        item {
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}