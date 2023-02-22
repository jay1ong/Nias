package cn.jaylong.nias.core.ui

import android.content.Context
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import cn.jaylong.nias.core.domain.model.UserNewsResource

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/13
 */
fun LazyGridScope.newsFeed(
    feedState: NewsFeedUiState,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit
) {
    when (feedState) {
        NewsFeedUiState.Loading -> Unit
        is NewsFeedUiState.Success -> {
            items(feedState.feed, key = { it.id }) { userNewsResource ->
                val resourceUrl by remember {
                    mutableStateOf(Uri.parse(userNewsResource.url))
                }
                val context = LocalContext.current
                val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

                NewsResourceCardExpanded(
                    userNewsResource = userNewsResource,
                    isBookmarked = userNewsResource.isSaved,
                    onToggleBookmark = {
                        onNewsResourcesCheckedChanged(
                            userNewsResource.id,
                            !userNewsResource.isSaved
                        )

                    },
                    onClick = {
                        launchCustomChromeTab(context, resourceUrl, backgroundColor)
                    })
            }
        }
    }
}

sealed interface NewsFeedUiState {
    object Loading : NewsFeedUiState
    data class Success(
        val feed: List<UserNewsResource>
    ) : NewsFeedUiState
}

fun launchCustomChromeTab(context: Context, uri: Uri, @ColorInt toolbarColor: Int) {
    val customTabBarColor = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(toolbarColor).build()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(customTabBarColor)
        .build()

    customTabsIntent.launchUrl(context, uri)

}