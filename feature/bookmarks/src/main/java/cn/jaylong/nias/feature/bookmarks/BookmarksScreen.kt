package cn.jaylong.nias.feature.bookmarks

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.jaylong.nias.core.designsystem.component.NiaLoadingWheel
import cn.jaylong.nias.core.designsystem.theme.LocalTintTheme
import cn.jaylong.nias.core.ui.NewsFeedUiState
import cn.jaylong.nias.core.ui.TrackScrollJank
import cn.jaylong.nias.core.ui.newsFeed

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun BookmarksRoute(
    modifier: Modifier = Modifier,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedUiState.collectAsStateWithLifecycle()
    BookmarkScreen(
        feedState = feedState,
        removeFromBookmarks = viewModel::removeFromSavedResources,
        modifier = modifier
    )
}

@VisibleForTesting
@Composable
internal fun BookmarkScreen(
    feedState: NewsFeedUiState,
    removeFromBookmarks: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (feedState) {
        NewsFeedUiState.Loading -> LoadingState(modifier)
        is NewsFeedUiState.Success -> if (feedState.feed.isNotEmpty()) {
            BookmarkGrid(
                feedState = feedState,
                removeFromBookmarks = removeFromBookmarks,
                modifier = modifier
            )
        } else {
            EmptyState(
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    NiaLoadingWheel(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .testTag("forYou:loading"),
        contentDesc = stringResource(id = R.string.saved_loading)
    )
}

@Composable
private fun BookmarkGrid(
    feedState: NewsFeedUiState,
    removeFromBookmarks: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollableState = rememberLazyGridState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "bookmarks:grid")
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        state = scrollableState,
        modifier = modifier
            .fillMaxSize()
            .testTag("bookmarks:feed")
    ) {
        newsFeed(
            feedState = feedState,
            onNewsResourcesCheckedChanged = { id, _ -> removeFromBookmarks(id) },
        )
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .testTag("bookmarks:empty"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.img_empty_bookmarks),
            colorFilter = if (iconTint != null) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.bookmarks_empty_error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.bookmarks_empty_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )


    }
}