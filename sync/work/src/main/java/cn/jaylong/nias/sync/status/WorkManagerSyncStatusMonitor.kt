package cn.jaylong.nias.sync.status

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import androidx.work.WorkInfo
import androidx.work.WorkManager
import cn.jaylong.nias.core.data.util.SyncStatusMonitor
import cn.jaylong.nias.sync.initialzers.SyncWorkName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
class WorkManagerSyncStatusMonitor @Inject constructor(
    @ApplicationContext context: Context,
) : SyncStatusMonitor {
        override val isSyncing: Flow<Boolean> =
        Transformations.map(
            WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SyncWorkName),
            MutableList<WorkInfo>::anyRunning,
        )
            .asFlow()
            .conflate()
//    override val isSyncing: Flow<Boolean> = flowOf(false)
}

private val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }