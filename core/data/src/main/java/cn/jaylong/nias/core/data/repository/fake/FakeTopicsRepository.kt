package cn.jaylong.nias.core.data.repository.fake

import cn.jaylong.nias.core.network.Dispatcher
import cn.jaylong.nias.core.network.NiaDispatchers
import cn.jaylong.nias.core.data.Synchronizer
import cn.jaylong.nias.core.data.repository.TopicsRepository
import cn.jaylong.nias.core.model.data.Topic
import cn.jaylong.nias.core.network.fake.FakeNiaNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/13
 */
class FakeTopicsRepository @Inject constructor(
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val datasource: FakeNiaNetworkDataSource,
) : TopicsRepository {
    override fun getTopics(): Flow<List<Topic>> = flow {
        emit(
            datasource.getTopics().map {
                Topic(
                    id = it.id,
                    name = it.name,
                    shortDescription = it.shortDescription,
                    longDescription = it.longDescription,
                    url = it.url,
                    imageUrl = it.imageUrl
                )
            }
        )
    }.flowOn(ioDispatcher)

    override fun getTopic(id: String): Flow<Topic> {
        return getTopics().map { it.first { topic -> topic.id == id } }
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean = true


}