package cn.jaylong.nias.core.data

import cn.jaylong.nias.core.datastore.ChangeListVersions

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
interface Synchronizer {
    suspend fun getChangeListVersions(): ChangeListVersions

    suspend fun updateChangeListVersions(update: ChangeListVersions.() -> ChangeListVersions)

    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}