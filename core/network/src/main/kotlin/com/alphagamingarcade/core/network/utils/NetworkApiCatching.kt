package com.alphagamingarcade.core.network.utils

import com.alphagamingarcade.core.common.exception.ApiException
import com.alphagamingarcade.core.common.result.FieldError
import com.alphagamingarcade.core.network.model.ApiResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.util.concurrent.CancellationException

private val networkJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

suspend fun <T> networkApiCatching(
    block: suspend () -> ApiResponse<T>
): ApiResponse<T> {
    return try {
        val response = block()

        if (!response.success) {
            throw ApiException(
                message = response.message,
                statusCode = response.statusCode,
                errors = response.errors?.map { error ->
                    FieldError(
                        type = error.type,
                        message = error.message,
                        field = error.field
                    )
                }.orEmpty()
            )
        }

        response

    } catch (cancellationException: CancellationException) {
        throw cancellationException

    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()

        val parsed = errorBody?.let {
            runCatching {
                networkJson.decodeFromString<ApiResponse<Nothing>>(it)
            }.getOrNull()
        }

        throw ApiException(
            message = parsed?.message ?: e.message(),
            statusCode = parsed?.statusCode ?: e.code(),
            errors = parsed?.errors?.map { error ->
                FieldError(
                    type = error.type,
                    message = error.message,
                    field = error.field
                )
            }.orEmpty()
        )
    }
}