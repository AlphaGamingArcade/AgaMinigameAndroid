package com.alphagamingarcade.core.ui.utils

/**
 * Data class representing the state of a text field.
 *
 * @property value The current value of the text field.
 * @property errorMessage An optional error message associated with the text field.
 */
data class TextFieldData(
    val value: String,
    val errorMessage: String? = null,
)
