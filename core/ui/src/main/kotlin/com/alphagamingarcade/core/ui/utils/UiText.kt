/*
 * Copyright 2026 Alpha Gaming Arcade
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alphagamingarcade.core.ui.utils

import com.alphagamingarcade.core.ui.R
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alphagamingarcade.core.common.result.FieldError
import kotlin.text.lowercase

// ... UiText by Phillip Lackner
// ... https://youtu.be/mB1Lej0aDus

/**
 * A sealed class that represents a string that can be either a string resource or a dynamic string.
 */
sealed class UiText {
    /**
     * A dynamic string that can be used to represent a string that is not known at compile time.
     *
     * @param value The string value.
     */
    data class DynamicString(val value: String) : UiText()

    /**
     * A string resource that can be used to represent a string that is known at compile time.
     *
     * @param resId The string resource id.
     * @param args The string resource arguments.
     */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText()

    /**
     * Returns the string value of this [UiText].
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    /**
     * Returns the string value of this [UiText].
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}


fun FieldError.toUiText(): UiText {
    return when (code.lowercase()) {
        "invalid_credentials" -> UiText.StringResource(R.string.error_invalid_credentials)
        "email_already_exists" -> UiText.StringResource(R.string.error_email_already_exists)
        "account_already_exists" -> UiText.StringResource(R.string.error_account_already_exists)
        "nickname_unchanged" -> UiText.StringResource(R.string.error_nickname_unchanged)
        "nickname_cooldown" -> UiText.StringResource(R.string.error_nickname_cooldown)
        "current_password_incorrect" -> UiText.StringResource(R.string.error_current_password_incorrect)
        "password_unchanged" -> UiText.StringResource(R.string.error_password_unchanged)
        "password_mismatched" -> UiText.StringResource(R.string.error_password_mismatched)
        else -> UiText.StringResource(R.string.error_unknown)
    }
}