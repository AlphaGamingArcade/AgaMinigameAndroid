package com.alphagamingarcade.core.utils

import java.util.concurrent.atomic.AtomicBoolean

// Event wrapper for Single Events
// Details here: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150

/**
 * A wrapper for data that is exposed via a LiveData that represents an event.
 *
 * @param T The type of the content.
 * @property content The content of the event.
 */
class OneTimeEvent<T>(private val content: T) {
    private var hasBeenHandled = AtomicBoolean(false)

    /**
     * Returns the content if it has not been handled yet, and marks it as handled.
     *
     * @return The content if it has not been handled, otherwise null.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled.compareAndSet(false, true)) content else null
    }

    /**
     * Returns the content, even if it has already been handled.
     *
     * @return The content.
     */
    fun peekContent(): T = content
}
