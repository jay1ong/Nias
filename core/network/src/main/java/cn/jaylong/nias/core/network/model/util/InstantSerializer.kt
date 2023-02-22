package cn.jaylong.nias.core.network.model.util

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
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
object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "Instant",
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): Instant =
        decoder.decodeString().toInstant()

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }


}