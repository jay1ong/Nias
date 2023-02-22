package cn.jaylong.nias.core.data.util

import kotlinx.coroutines.flow.Flow


/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/9
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}