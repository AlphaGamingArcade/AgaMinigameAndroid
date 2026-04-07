package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alphagamingarcade.core.ui.R

@Composable
fun ForgotPassword(onForgotPasswordClick : () -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        JetpackTextButton(onClick = onForgotPasswordClick) {
            Text(
                text = stringResource(R.string.forgot_password),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}