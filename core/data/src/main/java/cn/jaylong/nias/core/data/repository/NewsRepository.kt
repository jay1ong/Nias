package cn.jaylong.nias.core.data.repository

import cn.jaylong.nias.core.data.Syncable
import cn.jaylong.nias.core.model.data.NewsResource
import kotlinx.coroutines.flow.Flow

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
interface NewsRepository : Syncable {
    fun getNewsResources(): Flow<List<NewsResource>>

    fun getNewsResources(
        filterTopicIds: Set<String> = emptySet()
    ): Flow<List<NewsResource>>
}