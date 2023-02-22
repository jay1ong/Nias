package cn.jaylong.nias.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.jaylong.nias.core.designsystem.R

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/18
 */
@Composable
fun NiaTopicTag(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    followed: Boolean,
    onDropdownMenuToggle: (show: Boolean) -> Unit = {},
    onFollowClick: () -> Unit,
    onUnFollowClick: () -> Unit,
    onBrowseClick: () -> Unit,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    followText: @Composable () -> Unit = { Text(stringResource(id = R.string.follow)) },
    unFollowText: @Composable () -> Unit = { Text(stringResource(id = R.string.unfollow)) },
    browseText: @Composable () -> Unit = { Text(stringResource(id = R.string.browse_topic)) },
) {
    Box(modifier = modifier) {
        val containerColor = if (followed) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = NiaTagDefaults.UnfollowedTopicTagContainerAlpha
            )
        }
        TextButton(
            onClick = { onDropdownMenuToggle(true) },
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                containerColor = containerColor,
                contentColor = contentColorFor(backgroundColor = containerColor),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = NiaTagDefaults.DisabledTopicTagContainerAlpha,
                ),
            )
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                text()
            }
        }


    }
}


object NiaTagDefaults {

    const val UnfollowedTopicTagContainerAlpha = 0.5f

    const val DisabledTopicTagContainerAlpha = 0.12f
}

private const val FOLLOW = 1

private const val UNFOLLOW = 2

private const val BROWSE = 3