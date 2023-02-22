package cn.jaylong.nias.core.data.di

import cn.jaylong.nias.core.data.repository.NewsRepository
import cn.jaylong.nias.core.data.repository.TopicsRepository
import cn.jaylong.nias.core.data.repository.UserDataRepository
import cn.jaylong.nias.core.data.repository.fake.FakeNewsRepository
import cn.jaylong.nias.core.data.repository.fake.FakeTopicsRepository
import cn.jaylong.nias.core.data.repository.fake.FakeUserDataRepository
import cn.jaylong.nias.core.data.util.ConnectivityManagerNetworkMonitor
import cn.jaylong.nias.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/10
 */
@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindTopicRepository(
        topicsRepository: FakeTopicsRepository
    ): TopicsRepository

    @Binds
    fun bindNewsResourceRepository(
        newsRepository: FakeNewsRepository
    ): NewsRepository

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: FakeUserDataRepository
    ): UserDataRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor

}