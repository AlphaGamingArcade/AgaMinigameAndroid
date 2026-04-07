package com.alphagamingarcade.feature.auth.ui.checkyouremail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.components.JetpackButton
import com.alphagamingarcade.core.ui.components.JetpackOutlinedButton
import com.alphagamingarcade.core.ui.components.JetpackTextButton
import com.alphagamingarcade.core.ui.utils.PreviewDevices
import com.alphagamingarcade.core.ui.utils.PreviewThemes
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.feature.auth.R

/**
 * Check your email screen.
 *
 * @param email Email address the reset link was sent to.
 * @param onBackToSignInClick Navigate back to sign in screen.
 * @param onShowSnackbar Show Snackbar.
 * @param checkYourEmailViewModel [CheckYourEmailViewModel].
 */
@Composable
internal fun CheckYourEmailScreen(
    email: String,
    onBackToSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    checkYourEmailViewModel: CheckYourEmailViewModel = hiltViewModel(),
) {
    val checkYourEmailState by checkYourEmailViewModel.checkYourEmailUiState.collectAsStateWithLifecycle()

    StatefulComposable (
        state = checkYourEmailState,
        onShowSnackbar = onShowSnackbar,
    ) { checkYourEmailData ->
        CheckYourEmailScreen(
            screenData = checkYourEmailData,
            email = email,
            onResendEmailClick = { checkYourEmailViewModel.resendEmail(email) },
            onBackToSignInClick = onBackToSignInClick,
        )
    }
}

/**
 * Check your email screen.
 *
 * @param screenData [CheckYourEmailData].
 * @param email Email address the reset link was sent to.
 * @param onResendEmailClick Callback when resend email is clicked.
 * @param onBackToSignInClick Callback when back to sign in is clicked.
 */
@Composable
private fun CheckYourEmailScreen(
    screenData: CheckYourEmailData,
    email: String,
    onResendEmailClick: () -> Unit,
    onBackToSignInClick: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.check_your_email),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.check_your_email_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(
                text = stringResource(R.string.check_your_email_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        JetpackButton(
            onClick = { uriHandler.openUri("googlegmail://") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            text = { Text(stringResource(R.string.open_email_app)) },
        )
        Spacer(modifier = Modifier.height(12.dp))
        JetpackOutlinedButton(
            onClick = onResendEmailClick, // 👈 fixed — no longer called immediately
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            text = { Text(stringResource(R.string.resend_email)) },
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.remember_your_password))
            JetpackTextButton(onClick = onBackToSignInClick) {
                Text(
                    text = stringResource(R.string.sign_in),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
@PreviewThemes
@PreviewDevices
private fun CheckYourEmailScreenPreview() {
    CheckYourEmailScreen(
        screenData = CheckYourEmailData(),
        email = "user@email.com",
        onResendEmailClick = {},
        onBackToSignInClick = {},
    )
}