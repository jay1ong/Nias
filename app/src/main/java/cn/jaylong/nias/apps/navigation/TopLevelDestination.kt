package cn.jaylong.nias.apps.navigation

import cn.jaylong.nias.apps.R
import cn.jaylong.nias.core.designsystem.icon.Icon
import cn.jaylong.nias.core.designsystem.icon.NiaIcons
import cn.jaylong.nias.feature.bookmarks.R as bookmarksR
import cn.jaylong.nias.feature.foryou.R as forYouR
import cn.jaylong.nias.feature.interests.R as interestR


/**
 * 顶级路由
 *
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/10
 */
enum class TopLevelDestination(
    val selectIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    FOR_YOU(
        selectIcon = Icon.DrawableResourceIcon(NiaIcons.Upcoming),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.UpcomingBorder),
        iconTextId = forYouR.string.for_you,
        titleTextId = R.string.app_name
    ),
    BOOKMARKS(
        selectIcon = Icon.DrawableResourceIcon(NiaIcons.Bookmarks),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.BookmarksBorder),
        iconTextId = bookmarksR.string.saved,
        titleTextId = bookmarksR.string.saved
    ),
    INTERESTS(
        selectIcon = Icon.ImageVectorIcon(NiaIcons.Grid3x3),
        unselectedIcon = Icon.ImageVectorIcon(NiaIcons.Grid3x3),
        iconTextId = interestR.string.interests,
        titleTextId = interestR.string.interests
    )


}