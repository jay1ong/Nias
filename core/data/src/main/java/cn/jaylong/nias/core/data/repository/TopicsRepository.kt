package cn.jaylong.nias.core.data.repository

import cn.jaylong.nias.core.data.Syncable
import cn.jaylong.nias.core.model.data.Topic
import kotlinx.coroutines.flow.Flow

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/13
 */
interface TopicsRepository : Syncable {

    fun getTopics(): Flow<List<Topic>>

    fun getTopic(id: String): Flow<Topic>
}