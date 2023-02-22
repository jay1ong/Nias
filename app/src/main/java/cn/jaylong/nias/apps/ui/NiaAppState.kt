package cn.jaylong.nias.apps.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.util.trace
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import cn.jaylong.nias.apps.navigation.TopLevelDestination
import cn.jaylong.nias.core.data.util.NetworkMonitor
import cn.jaylong.nias.core.ui.TrackDisposableJank
import cn.jaylong.nias.feature.bookmarks.navigation.bookmarksRoute
import cn.jaylong.nias.feature.bookmarks.navigation.navigateToBookmarks
import cn.jaylong.nias.feature.foryou.navigation.forYouNavigationRoute
import cn.jaylong.nias.feature.foryou.navigation.navigationToForYou
import cn.jaylong.nias.feature.interests.navigation.interestsRoute
import cn.jaylong.nias.feature.interests.navigation.navigateToInterestsGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/10
 */
@Composable
fun rememberNiaAppState(
    windowsSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): NiaAppState {
    NavigationTrackingSideEffect(navController)
    return remember(navController, coroutineScope, windowsSizeClass, networkMonitor) {
        NiaAppState(navController, coroutineScope, windowsSizeClass, networkMonitor)
    }
}

@Stable
class NiaAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowsSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            forYouNavigationRoute -> TopLevelDestination.FOR_YOU
            bookmarksRoute -> TopLevelDestination.BOOKMARKS
            interestsRoute -> TopLevelDestination.INTERESTS
            else -> null
        }

    var shouldShowSettingDialog by mutableStateOf(false)
        private set

    val shouldShowBottomBar: Boolean
        get() = windowsSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    var topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation:${topLevelDestination.name}") {
            val topLevelOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            when (topLevelDestination) {
                TopLevelDestination.FOR_YOU -> navController.navigationToForYou(topLevelOptions)
                TopLevelDestination.BOOKMARKS -> navController.navigateToBookmarks(topLevelOptions)
                TopLevelDestination.INTERESTS -> navController.navigateToInterestsGraph(
                    topLevelOptions
                )
            }
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }

    fun setShowSettingDialog(shouldShow: Boolean) {
        shouldShowSettingDialog = shouldShow
    }

}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }

    }

}