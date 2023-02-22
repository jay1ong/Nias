package cn.jaylong.nias.feature.topic.navigation

import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cn.jaylong.nias.core.decoder.StringDecoder
import cn.jaylong.nias.feature.topic.TopicRoute

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
@VisibleForTesting
internal const val topicIdArg = "topicId"

internal class TopicArgs(val topicId: String) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
            this(stringDecoder.decodeString(checkNotNull(savedStateHandle[topicIdArg])))
}

fun NavController.navigationToTopic(topicId: String) {
    val encodeId = Uri.parse(topicId)
    this.navigate("topic_route/$encodeId")
}

fun NavGraphBuilder.topicScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "topic_route/{$topicIdArg}",
        arguments = listOf(
            navArgument(topicIdArg) { type = NavType.StringType }
        )
    ) {
        TopicRoute(onBackClick = onBackClick)
    }
}