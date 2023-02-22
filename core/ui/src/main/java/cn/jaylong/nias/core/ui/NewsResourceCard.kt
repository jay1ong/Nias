package cn.jaylong.nias.core.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cn.jaylong.nias.core.designsystem.component.NiaIconToggleButton
import cn.jaylong.nias.core.designsystem.component.NiaTopicTag
import cn.jaylong.nias.core.designsystem.icon.NiaIcons
import cn.jaylong.nias.core.domain.model.FollowableTopic
import cn.jaylong.nias.core.domain.model.UserNewsResource
import cn.jaylong.nias.core.model.data.NewsResourceType
import coil.compose.AsyncImage
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import cn.jaylong.nias.core.designsystem.R as DesignsystemR

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/13
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsResourceCardExpanded(
    userNewsResource: UserNewsResource,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clickActionLabel = stringResource(R.string.card_tap_action)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.semantics {
            onClick(label = clickActionLabel, action = null)
        }
    ) {
        Column {
            if (!userNewsResource.headerImageUrl.isNullOrEmpty()) {
                Row {
                    NewsResourceHeaderImage(headerImageUrl = userNewsResource.headerImageUrl)
                }
            }
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        NewsResourceTitle(
                            newsResourceTitle = userNewsResource.title,
                            modifier = Modifier.fillMaxWidth(.8f),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BookmarkButton(isBookmarked = isBookmarked, onClick = onToggleBookmark)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsResourceMetaData(
                        publishDate = userNewsResource.publishDate,
                        resourceType = userNewsResource.type,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsResourceShortDescription(newResourceShortDescription = userNewsResource.content)
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsResourceTopics(topics = userNewsResource.followableTopics)
                }
            }

        }
    }
}

@Composable
fun NewsResourceHeaderImage(
    headerImageUrl: String?,
) {
    AsyncImage(
        placeholder = if (LocalInspectionMode.current) {
            painterResource(DesignsystemR.drawable.ic_placeholder_default)
        } else {
            null
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(180.dp),
        contentScale = ContentScale.Crop,
        model = headerImageUrl,
        contentDescription = null,
    )
}

@Composable
fun NewsResourceTitle(
    newsResourceTitle: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = newsResourceTitle,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier,
    )
}


@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NiaIconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onClick() },
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(id = NiaIcons.BookmarksBorder),
                contentDescription = stringResource(
                    id = R.string.bookmark
                ),
            )
        },
        checkedIcon = {
            Icon(
                painterResource(id = NiaIcons.Bookmark),
                contentDescription = stringResource(id = R.string.unbookmark)
            )
        },
    )
}

@Composable
fun dateFormatted(
    publishDate: Instant,
): String {
    var zoneId by remember {
        mutableStateOf(ZoneId.systemDefault())
    }

    val context = LocalContext.current

    DisposableEffect(context) {
        val receiver = TimeZoneBroadcastReceiver(
            onTimeZoneChanged = { zoneId = ZoneId.systemDefault() },
        )
        receiver.register(context)
        onDispose {
            receiver.unregister(context)
        }
    }

    return DateTimeFormatter.ofPattern("MMM d, yyyy")
        .withZone(zoneId).format(publishDate.toJavaInstant())
}


@Composable
fun NewsResourceMetaData(
    publishDate: Instant,
    resourceType: NewsResourceType,
) {
    val formattedDate = dateFormatted(publishDate)
    Text(
        if (resourceType != NewsResourceType.Unknown) {
            stringResource(
                id = R.string.card_meta_data_text,
                formattedDate,
                resourceType.displayText
            )
        } else {
            formattedDate
        },
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
fun NewsResourceShortDescription(
    newResourceShortDescription: String,
) {
    Text(newResourceShortDescription, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun NewsResourceTopics(
    topics: List<FollowableTopic>,
    modifier: Modifier = Modifier,
) {
    var expandTopicId by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (followableTopic in topics) {
            NiaTopicTag(
                expanded = expandTopicId == followableTopic.topic.id,
                followed = followableTopic.isFollowed,
                onDropdownMenuToggle = { show ->
                    expandTopicId = if (show) followableTopic.topic.id else null
                },
                onFollowClick = {},
                onUnFollowClick = {},
                onBrowseClick = {},
                text = {
                    val contentDescription = if (followableTopic.isFollowed) {
                        stringResource(
                            id = R.string.topic_chip_content_description_when_followed,
                            followableTopic.topic.name
                        )
                    } else {
                        stringResource(
                            id = R.string.topic_chip_content_description_when_not_followed,
                            followableTopic.topic.name
                        )
                    }
                    Text(
                        text = followableTopic.topic.name.uppercase(Locale.getDefault()),
                        modifier = Modifier.semantics {
                            this.contentDescription = contentDescription
                        },
                    )
                },
            )
        }
    }
}