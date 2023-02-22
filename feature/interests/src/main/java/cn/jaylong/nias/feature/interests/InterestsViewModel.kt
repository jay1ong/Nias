package cn.jaylong.nias.feature.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.jaylong.nias.core.data.repository.UserDataRepository
import cn.jaylong.nias.core.domain.model.FollowableTopic
import cn.jaylong.nias.core.domain.GetFollowableTopicsUseCase
import cn.jaylong.nias.core.domain.TopicSortField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
@HiltViewModel
class InterestsViewModel @Inject constructor(
    val userDataRepository: UserDataRepository,
    getFollowableTopicsUseCase: GetFollowableTopicsUseCase,
) : ViewModel() {
    val uiState: StateFlow<InterestsUiState> =
        getFollowableTopicsUseCase(sortBy = TopicSortField.NAME).map(
            InterestsUiState::Interests
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InterestsUiState.Loading,
        )

    fun followTopic(followableTopicId: String, followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedTopicId(followableTopicId, followed)
        }
    }
}

sealed interface InterestsUiState {
    object Loading : InterestsUiState
    object Empty : InterestsUiState
    data class Interests(
        val topics: List<FollowableTopic>,
    ) : InterestsUiState
}