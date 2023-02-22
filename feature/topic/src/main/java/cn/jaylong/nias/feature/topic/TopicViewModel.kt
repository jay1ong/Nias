package cn.jaylong.nias.feature.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.jaylong.nias.core.data.repository.TopicsRepository
import cn.jaylong.nias.core.data.repository.UserDataRepository
import cn.jaylong.nias.core.decoder.StringDecoder
import cn.jaylong.nias.core.domain.model.FollowableTopic
import cn.jaylong.nias.core.domain.model.UserNewsResource
import cn.jaylong.nias.core.model.data.Topic
import cn.jaylong.nias.core.result.asResult
import cn.jaylong.nias.feature.topic.navigation.TopicArgs
import cn.jaylong.nias.core.domain.GetUserNewsResourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
    private val userDataRepository: UserDataRepository,
    topicsRepository: TopicsRepository,
    getSaveableNewsResource: GetUserNewsResourcesUseCase,
) : ViewModel() {
    private val topicArgs: TopicArgs = TopicArgs(savedStateHandle, stringDecoder)

    val topicUiState: StateFlow<TopicUiState> = topicUiState(
        topicId = topicArgs.topicId,
        userDataRepository = userDataRepository,
        topicsRepository = topicsRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TopicUiState.Loading,
        )

    val newsUiState: StateFlow<NewsUiState> = newsUiState(
        topicId = topicArgs.topicId,
        userDataRepository = userDataRepository,
        getSaveableNewsResource = getSaveableNewsResource,
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewsUiState.Loading
        )

    fun followTopicToggle(followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedTopicId(topicArgs.topicId, followed)
        }
    }

    fun bookmarkNews(newResourceId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newResourceId, bookmarked)
        }
    }

}

private fun topicUiState(
    topicId: String,
    userDataRepository: UserDataRepository,
    topicsRepository: TopicsRepository,
): Flow<TopicUiState> {
    val followedTopicIds: Flow<Set<String>> =
        userDataRepository.userData
            .map { it.followedTopics }

    val topicStream: Flow<Topic> = topicsRepository.getTopic(
        id = topicId
    )

    return combine(
        followedTopicIds,
        topicStream,
        ::Pair
    )
        .asResult()
        .map { followedTopicToTopicResult ->
            when (followedTopicToTopicResult) {
                is cn.jaylong.nias.core.result.Result.Success -> {
                    val (followedTopics, topic) = followedTopicToTopicResult.data
                    val followed = followedTopics.contains(topicId)
                    TopicUiState.Success(
                        followableTopic = FollowableTopic(
                            topic = topic,
                            isFollowed = followed
                        ),
                    )
                }
                is cn.jaylong.nias.core.result.Result.Loading -> {
                    TopicUiState.Loading
                }
                is cn.jaylong.nias.core.result.Result.Error -> {
                    TopicUiState.Error
                }
            }

        }

}

private fun newsUiState(
    topicId: String,
    getSaveableNewsResource: GetUserNewsResourcesUseCase,
    userDataRepository: UserDataRepository,
): Flow<NewsUiState> {
    val newsStream: Flow<List<UserNewsResource>> = getSaveableNewsResource(
        filterTopicIds = setOf(element = topicId)
    )

    val bookmark: Flow<Set<String>> = userDataRepository.userData
        .map { it.bookmarkedNewsResources }

    return combine(
        newsStream,
        bookmark,
        ::Pair,
    )
        .asResult()
        .map { newsToBookmarksResult ->
            when (newsToBookmarksResult) {
                is cn.jaylong.nias.core.result.Result.Success -> {
                    val news = newsToBookmarksResult.data.first
                    NewsUiState.Success(news)
                }
                is cn.jaylong.nias.core.result.Result.Loading -> {
                    NewsUiState.Loading
                }
                is cn.jaylong.nias.core.result.Result.Error -> {
                    NewsUiState.Error
                }
            }
        }
}

sealed interface TopicUiState {
    object Loading : TopicUiState
    object Error : TopicUiState
    data class Success(val followableTopic: FollowableTopic) : TopicUiState
}

sealed interface NewsUiState {
    object Loading : NewsUiState
    object Error : NewsUiState
    data class Success(val news: List<UserNewsResource>) : NewsUiState
}