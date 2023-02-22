package cn.jaylong.nias.feature.foryou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.jaylong.nias.core.data.repository.UserDataRepository
import cn.jaylong.nias.core.data.util.SyncStatusMonitor
import cn.jaylong.nias.core.domain.model.UserNewsResource
import cn.jaylong.nias.core.model.data.UserData
import cn.jaylong.nias.core.ui.NewsFeedUiState
import cn.jaylong.nias.core.domain.GetFollowableTopicsUseCase
import cn.jaylong.nias.core.domain.GetUserNewsResourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
@HiltViewModel
class ForYouViewModel @Inject constructor(
    syncStatusMonitor: SyncStatusMonitor,
    private val userDataRepository: UserDataRepository,
    getUserNewsResources: GetUserNewsResourcesUseCase,
    getFollowableTopics: GetFollowableTopicsUseCase
) : ViewModel() {
    private val shouldShowOnboarding: Flow<Boolean> =
        userDataRepository.userData.map { !it.shouldHideOnboarding }

    val isSyncing = syncStatusMonitor.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val feedState: StateFlow<NewsFeedUiState> =
        userDataRepository.getFollowedUserNewsResources(getUserNewsResources)
            .map(NewsFeedUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = NewsFeedUiState.Loading,
            )

    val onboardingUiState: StateFlow<OnboardingUiState> =
        combine(
            shouldShowOnboarding,
            getFollowableTopics()
        ) { shouldShowOnboarding, topics ->
            if (shouldShowOnboarding) {
                OnboardingUiState.Shown(topics = topics)
            } else {
                OnboardingUiState.NotShown
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = OnboardingUiState.Loading
            )

    fun setTopicSelections(followedTopicIds: Set<String>) {
        viewModelScope.launch {
            userDataRepository.setFollowedTopicIds(followedTopicIds)
        }
    }

    fun updateTopicSelection(topicId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedTopicId(topicId, isChecked)
        }
    }

    fun updateNewsResourceSaved(newsResourceId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, isChecked)
        }
    }

    fun dismissOnboarding() {
        viewModelScope.launch {
            userDataRepository.setShouldHideOnboarding(true)
        }
    }

}

private fun UserDataRepository.getFollowedUserNewsResources(
    getUserNewsResources: GetUserNewsResourcesUseCase,
): Flow<List<UserNewsResource>> = userData
    .map { userData ->
        if (userData.shouldShowEmptyFeed()) {
            null
        } else {
            userData.followedTopics
        }
    }
    .distinctUntilChanged()
    .flatMapLatest { followedTopics ->
        if (followedTopics == null) {
            flowOf(emptyList())
        } else {
            getUserNewsResources(filterTopicIds = followedTopics)
        }
    }

private fun UserData.shouldShowEmptyFeed() =
    !shouldHideOnboarding && followedTopics.isEmpty()