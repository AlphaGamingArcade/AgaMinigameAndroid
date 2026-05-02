package com.alphagamingarcade.feature.auth.ui.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.data.utils.PRIVACY_POLICY_URL
import com.alphagamingarcade.core.data.utils.TERMS_OF_SERVICE_URL
import com.alphagamingarcade.core.ui.components.DividerWithText
import com.alphagamingarcade.core.ui.components.ForgotPassword
import com.alphagamingarcade.core.ui.components.JetpackButton
import com.alphagamingarcade.core.ui.components.JetpackOutlinedButton
import com.alphagamingarcade.core.ui.components.JetpackPasswordField
import com.alphagamingarcade.core.ui.components.JetpackTextButton
import com.alphagamingarcade.core.ui.components.JetpackTextField
import com.alphagamingarcade.core.ui.utils.PreviewDevices
import com.alphagamingarcade.core.ui.utils.PreviewThemes
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.feature.auth.R

/**
 * Sign in screen.
 *
 * @param onSignUpLinkClick Navigate to sign up screen.
 * @param onShowSnackbar Show Snackbar.
 * @param signInViewModel [SignInViewModel].
 */
@Composable
internal fun SignInScreen(
    onSignUpLinkClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onForgotPasswordClick: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel(),
    onCheckYourEmail: (String) -> Unit,
    onProfileSetup: () -> Unit,
    onPrevious: () -> Unit
) {
    val signInState by signInViewModel.signInUiState.collectAsStateWithLifecycle()

    // Listen for navigation events
    LaunchedEffect(Unit) {
        signInViewModel.events.collect { event ->
            when (event) {
                is SignInEvent.NavigateToVerifyEmail -> onCheckYourEmail(event.email)
                SignInEvent.NavigateToProfileSetup -> onProfileSetup()
                SignInEvent.NavigateToPrevious -> onPrevious()
            }
        }
    }

    StatefulComposable(
        state = signInState,
        onShowSnackbar = onShowSnackbar,
    ) { homeScreenData ->
        SignInScreen(
            homeScreenData,
            onForgotPasswordClick,
            signInViewModel::updateEmail,
            signInViewModel::updatePassword,
            onSignInClick = signInViewModel::loginWithEmailAndPassword,
            onSignUpLinkClick = onSignUpLinkClick
        )
    }
}

/**
 * Sign in screen.
 *
 * @param screenData [SignInScreenData].
 * @param onEmailChange Callback when email is changed.
 * @param onPasswordChange Callback when password is changed.
 * @param onSignInClick Callback when sign up is clicked.
 */
@Composable
private fun SignInScreen(
    screenData: SignInScreenData,
    onForgotPasswordClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpLinkClick: () -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Text(stringResource(R.string.sign_in), style = MaterialTheme.typography.headlineLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.do_not_have_an_account))
            JetpackTextButton(onClick = onSignUpLinkClick) {
                Text(
                    text = stringResource(R.string.sign_up),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        JetpackTextField(
            value = screenData.email.value,
            errorMessage = screenData.email.errorMessage,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email),
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        JetpackPasswordField(
            value = screenData.password.value,
            errorMessage = screenData.password.errorMessage,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = stringResource(R.string.password),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )

        ForgotPassword(onForgotPasswordClick)

        JetpackButton(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
            text = { Text(stringResource(R.string.sign_in)) },
        )
        Spacer(modifier = Modifier.height(8.dp))
//        DividerWithText(text = R.string.or, modifier = Modifier.padding(vertical = 16.dp))
//        JetpackOutlinedButton(
//            onClick = {  },
//            text = { Text(text = "Sign In with Google") },
//            leadingIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_google),
//                    contentDescription = "Google",
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//                .height(56.dp),
//        )
//        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(R.string.agree_to_terms),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val uriHandler = LocalUriHandler.current
            JetpackTextButton(
                onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) },
            ) {
                Text(
                    text = stringResource(R.string.privacy_policy),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            JetpackTextButton(
                onClick = { uriHandler.openUri(TERMS_OF_SERVICE_URL) },
            ) {
                Text(
                    text = stringResource(R.string.terms_of_service),
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
private fun SignInScreenPreview() {
    SignInScreen(
        screenData = SignInScreenData(),
        onForgotPasswordClick = {},
        onEmailChange = {},
        onPasswordChange = {},
        onSignUpLinkClick = {},
        onSignInClick = {},
    )
}
