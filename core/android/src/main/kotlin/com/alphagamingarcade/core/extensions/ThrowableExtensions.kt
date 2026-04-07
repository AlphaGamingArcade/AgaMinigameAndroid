package com.alphagamingarcade.core.extensions

import com.alphagamingarcade.core.utils.OneTimeEvent

/**
 * Extension function to get the stack trace of a Throwable as a string.
 *
 * @receiver Throwable The throwable instance.
 * @return A string representation of the stack trace.
 */
fun Throwable.getStackTraceString(): String {
    return stackTrace.joinToString("\n")
}

/**
 * Extension function to convert a Throwable into a OneTimeEvent.
 *
 * @receiver Throwable The throwable instance.
 * @return A OneTimeEvent containing the throwable.
 */
fun Throwable.asOneTimeEvent(): OneTimeEvent<Throwable?> {
    return OneTimeEvent(this)
}
