package cn.jaylong.nias.apps.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import cn.jaylong.nias.feature.bookmarks.navigation.bookmarksScreen
import cn.jaylong.nias.feature.foryou.navigation.forYouNavigationRoute
import cn.jaylong.nias.feature.foryou.navigation.forYouScreen
import cn.jaylong.nias.feature.interests.navigation.interestGraph
import cn.jaylong.nias.feature.topic.navigation.navigationToTopic
import cn.jaylong.nias.feature.topic.navigation.topicScreen

/**
 * 路由
 *
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/14
 */
@Composable
fun NiaNavHost(
    navController: NavHostController, // 控制器
    onBackClick: () -> Unit, // 返回事件
    modifier: Modifier = Modifier,
    startDestination: String = forYouNavigationRoute, // 首页
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        forYouScreen()
        bookmarksScreen()
        interestGraph(
            navigateToTopic = { topicId ->
                navController.navigationToTopic(topicId)
            },
            nestedGraphs = {
                topicScreen(onBackClick)
            },
        )
    }
}