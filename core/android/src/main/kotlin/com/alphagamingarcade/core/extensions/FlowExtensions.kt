package com.alphagamingarcade.core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Returns a [StateFlow] that represents the last value emitted by the [Flow]
 *
 * @param initialValue The initial value of the [StateFlow]
 * @param scope The [CoroutineScope] to be used for the [StateFlow]
 * @return A [StateFlow] that represents the last value emitted by the [Flow]
 * */
fun <T> Flow<T>.stateInDelayed(
    initialValue: T,
    scope: CoroutineScope,
): StateFlow<T> {
    return this.stateIn(
        scope = scope,
        initialValue = initialValue,
        started = SharingStarted.WhileSubscribed(5000L),
    )
}
