package cn.jaylong.nias.feature.interests

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.jaylong.nias.core.designsystem.component.NiaLoadingWheel

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun InterestsRoute(
    navigateToTopic: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InterestsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InterestsScreen(
        uiState = uiState,
        followTopic = viewModel::followTopic,
        navigateToTopic = navigateToTopic,
        modifier = modifier
    )
}

@Composable
internal fun InterestsScreen(
    uiState: InterestsUiState,
    followTopic: (String, Boolean) -> Unit,
    navigateToTopic: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            InterestsUiState.Loading ->
                NiaLoadingWheel(
                    modifier = modifier,
                    contentDesc = stringResource(id = R.string.loading)
                )
            is InterestsUiState.Interests ->
                TopicsTabContent(
                    topics = uiState.topics,
                    onTopicClick = navigateToTopic,
                    onFollowableTopicClick = followTopic,
                    modifier = modifier
                )
            is InterestsUiState.Empty -> InterestEmptyScreen()
        }
    }
}

@Composable
private fun InterestEmptyScreen() {
    Text(text = stringResource(id = R.string.empty_header))
}