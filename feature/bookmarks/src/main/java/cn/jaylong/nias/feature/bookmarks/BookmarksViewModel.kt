package cn.jaylong.nias.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.jaylong.nias.core.data.repository.UserDataRepository
import cn.jaylong.nias.core.domain.model.UserNewsResource
import cn.jaylong.nias.core.ui.NewsFeedUiState
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
class BookmarksViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    getSaveableNewsResources: GetUserNewsResourcesUseCase,
) : ViewModel() {

    val feedUiState: StateFlow<NewsFeedUiState> = getSaveableNewsResources()
        .filterNot { it.isEmpty() }
        .map { newsResources -> newsResources.filter(UserNewsResource::isSaved) }
        .map<List<UserNewsResource>, NewsFeedUiState>(NewsFeedUiState::Success)
        .onStart { emit(NewsFeedUiState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewsFeedUiState.Loading
        )

    fun removeFromSavedResources(newsResourceId: String) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, false)
        }
    }

}