package cn.jaylong.nias.feature.bookmarks.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cn.jaylong.nias.feature.bookmarks.BookmarksRoute

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
const val bookmarksRoute = "bookmarks_route"

fun NavController.navigateToBookmarks(navOptions: NavOptions? = null) {
    this.navigate(bookmarksRoute, navOptions)
}

fun NavGraphBuilder.bookmarksScreen() {
    composable(route = bookmarksRoute) {
        BookmarksRoute()
    }
}