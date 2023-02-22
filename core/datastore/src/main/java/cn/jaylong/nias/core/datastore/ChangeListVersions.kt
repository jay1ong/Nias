package cn.jaylong.nias.core.datastore

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/10
 */
data class ChangeListVersions(
    val topicVersion: Int = -1,
    val newsResourceVersion: Int = -1
)