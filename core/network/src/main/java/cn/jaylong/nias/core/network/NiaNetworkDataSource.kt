package cn.jaylong.nias.core.network

import cn.jaylong.nias.core.network.model.NetworkChangeList
import cn.jaylong.nias.core.network.model.NetworkNewsResources
import cn.jaylong.nias.core.network.model.NetworkTopic

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
interface NiaNetworkDataSource {
    suspend fun getTopics(ids: List<String>? = null): List<NetworkTopic>

    suspend fun getNewsResources(ids: List<String>? = null): List<NetworkNewsResources>

    suspend fun getTopicChangeList(after: Int? = null): List<NetworkChangeList>

    suspend fun getNewsResourceChangeList(after: Int? = null): List<NetworkChangeList>
}