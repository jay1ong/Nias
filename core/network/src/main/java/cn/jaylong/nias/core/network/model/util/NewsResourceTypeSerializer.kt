package cn.jaylong.nias.core.network.model.util

import cn.jaylong.nias.core.model.data.NewsResourceType
import cn.jaylong.nias.core.model.data.asNewsResourceType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/11
 */
object NewsResourceTypeSerializer : KSerializer<NewsResourceType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "type",
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): NewsResourceType =
        decoder.decodeString().asNewsResourceType()

    override fun serialize(encoder: Encoder, value: NewsResourceType) =
        encoder.encodeString(value.serializedName)
}