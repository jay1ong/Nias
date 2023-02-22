package cn.jaylong.nias.core.datastore

import androidx.datastore.core.DataMigration

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/10
 */
object IntToStringIdsMigration : DataMigration<UserPreferences> {

    override suspend fun cleanUp() = Unit

    override suspend fun shouldMigrate(currentData: UserPreferences): Boolean =
        !currentData.hasDoneIntToStringIdMigration


    override suspend fun migrate(currentData: UserPreferences): UserPreferences =
        currentData.copy {
            deprecatedFollowedTopicIds.clear()
            deprecatedFollowedTopicIds.addAll(
                currentData.deprecatedIntFollowedTopicIdsList.map(Int::toString),
            )
            deprecatedIntFollowedTopicIds.clear()

            deprecatedFollowedAuthorIds.clear()
            deprecatedFollowedAuthorIds.addAll(
                currentData.deprecatedIntFollowedAuthorIdsList.map(Int::toString),
            )

            deprecatedIntFollowedAuthorIds.clear()

            hasDoneIntToStringIdMigration = true

        }

}