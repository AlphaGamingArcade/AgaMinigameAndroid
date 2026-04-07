package com.alphagamingarcade.core.utils

/**
 * Interface representing a string decoder.
 */
interface StringDecoder {
    /**
     * Decodes an encoded string.
     *
     * @param encodedString The string to be decoded.
     * @return The decoded string.
     */
    fun decodeString(encodedString: String): String
}
