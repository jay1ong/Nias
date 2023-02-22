package cn.jaylong.nias.feature.foryou.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cn.jaylong.nias.feature.foryou.ForYouRoute

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/10
 */

const val forYouNavigationRoute = "for_you_route"

fun NavController.navigationToForYou(navOptions: NavOptions? = null) {
    this.navigate(forYouNavigationRoute, navOptions)
}

fun NavGraphBuilder.forYouScreen() {
    composable(route = forYouNavigationRoute) {
        ForYouRoute()
    }
}