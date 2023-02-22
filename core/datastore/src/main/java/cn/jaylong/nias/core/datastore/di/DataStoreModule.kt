package cn.jaylong.nias.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import cn.jaylong.nias.core.network.Dispatcher
import cn.jaylong.nias.core.network.NiaDispatchers
import cn.jaylong.nias.core.datastore.IntToStringIdsMigration
import cn.jaylong.nias.core.datastore.UserPreferenceSerializer
import cn.jaylong.nias.core.datastore.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/9
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(NiaDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        userPreferenceSerializer: UserPreferenceSerializer
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferenceSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            migrations = listOf(
                IntToStringIdsMigration,
            )

        ) {
            context.dataStoreFile("user_preferences.pb")
        }

}