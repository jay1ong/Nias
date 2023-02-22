package cn.jaylong.nias.feature.interests.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import cn.jaylong.nias.feature.interests.InterestsRoute

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */

private const val interestGraphRoutePattern = "interests_graph"

const val interestsRoute = "interests_route"

fun NavController.navigateToInterestsGraph(navOptions: NavOptions? = null) {
    this.navigate(interestGraphRoutePattern, navOptions)
}

fun NavGraphBuilder.interestGraph(
    navigateToTopic: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = interestGraphRoutePattern,
        startDestination = interestsRoute,
    ) {
        composable(
            route = interestsRoute
        ) {
            InterestsRoute(navigateToTopic = navigateToTopic)
        }
        nestedGraphs()
    }
}

