package cn.jaylong.nias.core.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/16
 */
sealed interface Result<out T> {

    data class Error(val exception: Throwable? = null) :
        Result<Nothing>

    object Loading : Result<Nothing>

    data class Success<T>(val data: T) : Result<T>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}