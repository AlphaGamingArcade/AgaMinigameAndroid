package com.alphagamingarcade.core.utils

import android.net.Uri
import javax.inject.Inject

/**
 * Implementation of [StringDecoder] that uses Android's Uri.decode method for decoding strings.
 *
 * @constructor Creates a [UriDecoder] instance.
 */
class UriDecoder @Inject constructor() : StringDecoder {
    /**
     * Decodes an encoded string using Android's Uri.decode method.
     *
     * @param encodedString The string to be decoded.
     * @return The decoded string.
     */
    override fun decodeString(encodedString: String): String = Uri.decode(encodedString)
}
