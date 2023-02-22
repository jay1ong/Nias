package cn.jaylong.nias.core.network

import javax.inject.Qualifier

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/9
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(
    val niaDispatcher: NiaDispatchers
)

enum class NiaDispatchers {
    IO,
}