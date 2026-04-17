package com.alphagamingarcade.core.datastore.utils

import androidx.datastore.core.Serializer
import com.alphagamingarcade.core.datastore.model.TokenData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object TokenDataSerializer : Serializer<TokenData> {

    override val defaultValue: TokenData = TokenData()

    override suspend fun readFrom(input: InputStream): TokenData {
        return try {
            Json.decodeFromString(
                deserializer = TokenData.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: TokenData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = TokenData.serializer(),
                value = t,
            ).encodeToByteArray()
        )
    }
}