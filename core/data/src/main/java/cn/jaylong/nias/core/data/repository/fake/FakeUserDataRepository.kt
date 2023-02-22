package cn.jaylong.nias.core.data.repository.fake

import cn.jaylong.nias.core.data.repository.UserDataRepository
import cn.jaylong.nias.core.datastore.NiaPreferencesDataSource
import cn.jaylong.nias.core.model.data.DarkThemeConfig
import cn.jaylong.nias.core.model.data.ThemeBrand
import cn.jaylong.nias.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/9
 */
class FakeUserDataRepository @Inject constructor(
    private val niaPreferencesDataSource: NiaPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        niaPreferencesDataSource.userData

    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) {
        niaPreferencesDataSource.setFollowedTopicIds(followedTopicIds)
    }

    override suspend fun toggleFollowedTopicId(followedTopicId: String, followed: Boolean) {
        niaPreferencesDataSource.toggleFollowedTopicId(followedTopicId, followed)
    }

    override suspend fun updateNewsResourceBookmark(newsResourceId: String, bookmarked: Boolean) {
        niaPreferencesDataSource.toggleNewsResourceBookmark(newsResourceId, bookmarked)
    }

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        niaPreferencesDataSource.setThemeBrand(themeBrand)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        niaPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        niaPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        niaPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
    }
}