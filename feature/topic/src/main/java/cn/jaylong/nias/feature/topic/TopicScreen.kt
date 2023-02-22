package cn.jaylong.nias.feature.topic

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.jaylong.nias.core.designsystem.component.DynamicAsyncImage
import cn.jaylong.nias.core.designsystem.component.NiaFilterChip
import cn.jaylong.nias.core.designsystem.component.NiaLoadingWheel
import cn.jaylong.nias.core.designsystem.icon.NiaIcons
import cn.jaylong.nias.core.domain.model.FollowableTopic
import cn.jaylong.nias.core.ui.R
import cn.jaylong.nias.core.ui.TrackScrollJank
import cn.jaylong.nias.core.ui.userNewsResourcesCardItem

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun TopicRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicViewModel = hiltViewModel(),
) {
    val topicUiState: TopicUiState by viewModel.topicUiState.collectAsStateWithLifecycle()
    val newUiState: NewsUiState by viewModel.newsUiState.collectAsStateWithLifecycle()

    TopicScreen(
        modifier = modifier,
        topicUiState = topicUiState,
        newsUiState = newUiState,
        onBackClick = onBackClick,
        onFollowClick = viewModel::followTopicToggle,
        onBookmarkChanged = viewModel::bookmarkNews
    )
}

@VisibleForTesting
@Composable
internal fun TopicScreen(
    topicUiState: TopicUiState,
    newsUiState: NewsUiState,
    onBackClick: () -> Unit,
    onFollowClick: (Boolean) -> Unit,
    onBookmarkChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    TrackScrollJank(scrollableState = state, stateName = "topic:screen")
    LazyColumn(
        state = state,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        }
        when (topicUiState) {
            TopicUiState.Loading -> item {
                NiaLoadingWheel(
                    modifier = modifier,
                    contentDesc = stringResource(id = cn.jaylong.nias.feature.topic.R.string.topic_loading)
                )
            }
            TopicUiState.Error -> TODO()
            is TopicUiState.Success -> {
                item {
                    TopicToolbar(
                        uiState = topicUiState.followableTopic,
                        onBackClick = onBackClick,
                        onFollowClick = onFollowClick
                    )
                }
                TopicBody(
                    name = topicUiState.followableTopic.topic.name,
                    description = topicUiState.followableTopic.topic.longDescription,
                    news = newsUiState,
                    imageUrl = topicUiState.followableTopic.topic.imageUrl,
                    onBookmarkChanged = onBookmarkChanged,
                )
            }
        }
        item {
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}


private fun LazyListScope.TopicBody(
    name: String,
    description: String,
    news: NewsUiState,
    imageUrl: String,
    onBookmarkChanged: (String, Boolean) -> Unit,
) {
    item {
        TopicHeader(name = name, description = description, imageUrl = imageUrl)
    }

    userNewsResourceCards(news, onBookmarkChanged)
}

@Composable
private fun TopicHeader(name: String, description: String, imageUrl: String) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        DynamicAsyncImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(216.dp)
                .padding(bottom = 12.dp)
        )
        Text(name, style = MaterialTheme.typography.displayMedium)
        if (description.isNotEmpty()) {
            Text(
                text = description,
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun LazyListScope.userNewsResourceCards(
    news: NewsUiState,
    onBookmarkChanged: (String, Boolean) -> Unit,
) {
    when (news) {
        is NewsUiState.Success -> {
            userNewsResourcesCardItem(
                items = news.news,
                onToggleBookmark = { onBookmarkChanged(it.id, !it.isSaved) },
                itemModifier = Modifier
                    .padding(24.dp)
            )
        }
        is NewsUiState.Loading -> item {
            NiaLoadingWheel(contentDesc = "Loading news")
        }
        else -> item {
            Text(text = "ERROR")
        }
    }
}

@Composable
private fun TopicToolbar(
    uiState: FollowableTopic,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFollowClick: (Boolean) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = NiaIcons.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
            )
        }
        val selected = uiState.isFollowed
        NiaFilterChip(
            selected = selected,
            onSelectedChange = onFollowClick,
            modifier = modifier.padding(end = 24.dp)
        ) {
            if (selected) {
                Text("FOLLOWING")
            } else {
                Text(text = "NOT FOLLOWING")
            }
        }
    }

}