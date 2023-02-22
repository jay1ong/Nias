package cn.jaylong.nias.feature.interests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.jaylong.nias.core.designsystem.component.DynamicAsyncImage
import cn.jaylong.nias.core.designsystem.component.NiaIconToggleButton
import cn.jaylong.nias.core.designsystem.icon.NiaIcons

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
@Composable
fun InterestsItem(
    name: String,
    following: Boolean,
    topicImageUrl: String,
    onClick: () -> Unit,
    onFollowButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    description: String = "",
    itemSeparation: Dp = 16.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() }
                .padding(vertical = itemSeparation)
        ) {
            InterestsIcon(topicImageUrl = topicImageUrl, iconModifier.size(64.dp))
            Spacer(modifier = Modifier.width(16.dp))
            InterestContent(name = name, description = description)
        }

        NiaIconToggleButton(checked = following, onCheckedChange = onFollowButtonClick,
            icon = {
                Icon(
                    imageVector = NiaIcons.Add,
                    contentDescription = stringResource(id = R.string.card_follow_button_content_desc)
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = NiaIcons.Check,
                    contentDescription = stringResource(id = R.string.card_unfollow_button_content_desc)
                )
            }
        )
    }
}

@Composable
private fun InterestContent(name: String, description: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(
                    vertical = if (description.isEmpty()) 0.dp else 4.dp
                ),
        )
        if (description.isNotEmpty()) {
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun InterestsIcon(topicImageUrl: String, modifier: Modifier = Modifier) {
    if (topicImageUrl.isEmpty()) {
        Icon(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp),
            imageVector = NiaIcons.Person,
            contentDescription = null
        )
    } else {
        DynamicAsyncImage(imageUrl = topicImageUrl, contentDescription = null, modifier = modifier)
    }
}