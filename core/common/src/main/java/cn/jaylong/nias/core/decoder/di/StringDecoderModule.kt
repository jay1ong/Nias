package cn.jaylong.nias.core.decoder.di

import cn.jaylong.nias.core.decoder.StringDecoder
import cn.jaylong.nias.core.decoder.UriDecoder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class StringDecoderModule {

    @Binds
    abstract fun bindsStringDecoder(uriDecoder: UriDecoder): StringDecoder
}