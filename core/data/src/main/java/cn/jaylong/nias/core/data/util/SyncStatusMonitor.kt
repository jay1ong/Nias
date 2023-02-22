package cn.jaylong.nias.core.data.util

import kotlinx.coroutines.flow.Flow

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
interface SyncStatusMonitor {
    val isSyncing: Flow<Boolean>
}