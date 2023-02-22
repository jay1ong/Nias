package cn.jaylong.nias.core.ui

import android.net.Uri
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import cn.jaylong.nias.core.domain.model.UserNewsResource

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
fun LazyListScope.userNewsResourcesCardItem(
    items: List<UserNewsResource>,
    onToggleBookmark: (item: UserNewsResource) -> Unit,
    onItemClick: ((item: UserNewsResource) -> Unit)? = null,
    itemModifier: Modifier = Modifier,
) = items(
    items = items,
    key = { it.id },
    itemContent = { userNewsResource ->
        val resourceUrl = Uri.parse(userNewsResource.url)
        val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
        val context = LocalContext.current

        NewsResourceCardExpanded(
            userNewsResource = userNewsResource,
            isBookmarked = userNewsResource.isSaved,
            onToggleBookmark = { onToggleBookmark(userNewsResource) },
            onClick = {
                when (onItemClick) {
                    null -> launchCustomChromeTab(context, resourceUrl, backgroundColor)
                    else -> onItemClick(userNewsResource)
                }
            },
            modifier = itemModifier
        )
    }
)