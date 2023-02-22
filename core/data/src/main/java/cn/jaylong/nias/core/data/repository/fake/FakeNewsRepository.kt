package cn.jaylong.nias.core.data.repository.fake

import cn.jaylong.nias.core.network.Dispatcher
import cn.jaylong.nias.core.network.NiaDispatchers
import cn.jaylong.nias.core.data.Synchronizer
import cn.jaylong.nias.core.data.model.asEntity
import cn.jaylong.nias.core.data.repository.NewsRepository
import cn.jaylong.nias.core.database.model.NewsResourceEntity
import cn.jaylong.nias.core.database.model.asExternalModel
import cn.jaylong.nias.core.model.data.NewsResource
import cn.jaylong.nias.core.network.fake.FakeNiaNetworkDataSource
import cn.jaylong.nias.core.network.model.NetworkNewsResources
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
class FakeNewsRepository @Inject constructor(
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val datasource: FakeNiaNetworkDataSource
) : NewsRepository {
    override fun getNewsResources(): Flow<List<NewsResource>> =
        flow {
            emit(
                datasource.getNewsResources()
                    .map(NetworkNewsResources::asEntity)
                    .map(NewsResourceEntity::asExternalModel)
            )
        }.flowOn(ioDispatcher)

    override fun getNewsResources(filterTopicIds: Set<String>): Flow<List<NewsResource>> =
        flow {
            emit(
                datasource.getNewsResources()
                    .filter { it.topics.intersect(filterTopicIds).isNotEmpty() }
                    .map(NetworkNewsResources::asEntity)
                    .map(NewsResourceEntity::asExternalModel)
            )
        }.flowOn(ioDispatcher)

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean = true
}