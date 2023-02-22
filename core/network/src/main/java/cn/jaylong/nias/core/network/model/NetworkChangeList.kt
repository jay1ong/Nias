package cn.jaylong.nias.core.network.model

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/13
 */
@kotlinx.serialization.Serializable
data class NetworkChangeList(
    val id:String,
    val changeListVersion:Int,
    val isDelete:Boolean,
)