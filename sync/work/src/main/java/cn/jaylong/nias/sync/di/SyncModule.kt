package cn.jaylong.nias.sync.di

import cn.jaylong.nias.core.data.util.SyncStatusMonitor
import cn.jaylong.nias.sync.status.WorkManagerSyncStatusMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/14
 */
@Module
@InstallIn(SingletonComponent::class)
interface SyncModule {

    @Binds
    fun bindsSyncStatusMonitor(
        syncStatusMonitor: WorkManagerSyncStatusMonitor,
    ): SyncStatusMonitor
}