package com.alphagamingarcade.core.utils

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.Continuation
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

/**
 * Suspends the current coroutine until the specified block is completed or the timeout is reached.
 *
 * @param timeout The duration to wait for the block to complete.
 * @param block The block to execute.
 * @return The result of the block.
 */
suspend inline fun <T> suspendCoroutineWithTimeout(
    timeout: Duration,
    crossinline block: (Continuation<T>) -> Unit,
): T {
    return withTimeout(timeout) {
        suspendCancellableCoroutine(block)
    }
}

/**
 * Suspends the current coroutine until the specified block is completed or the timeout is reached.
 *
 * @param timeMillis The time in milliseconds to wait for the block to complete.
 * @param block The block to execute.
 * @return The result of the block.
 */
suspend inline fun <T> suspendCoroutineWithTimeout(
    timeMillis: Long,
    crossinline block: (CancellableContinuation<T>) -> Unit,
): T {
    return withTimeout(timeMillis) {
        suspendCancellableCoroutine(block)
    }
}

/**
 * Runs the specified block and returns the result as a [Result].
 *
 * @param block The block to execute.
 * @return The result of the block.
 */
suspend inline fun <T> suspendRunCatching(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}
