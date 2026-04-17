package com.alphagamingarcade.feature.auth.ui.signup

import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.extensions.getActivity
import com.alphagamingarcade.core.ui.components.DividerWithText
import com.alphagamingarcade.core.ui.components.JetpackButton
import com.alphagamingarcade.core.ui.components.JetpackOutlinedButton
import com.alphagamingarcade.core.ui.components.JetpackPasswordField
import com.alphagamingarcade.core.ui.components.JetpackTextButton
import com.alphagamingarcade.core.ui.components.JetpackTextField
import com.alphagamingarcade.core.ui.utils.PreviewDevices
import com.alphagamingarcade.core.ui.utils.PreviewThemes
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.core.data.utils.PRIVACY_POLICY_URL
import com.alphagamingarcade.core.data.utils.TERMS_OF_SERVICE_URL
import com.alphagamingarcade.feature.auth.R
import com.alphagamingarcade.feature.auth.ui.signin.SignInEvent
import java.util.Calendar

/**
 * Composable function for the SignUp screen.
 *
 * @param onShowSnackbar Callback to show a snackbar with a message, action, and optional error.
 * @param signUpViewModel ViewModel for the SignUp screen, default is provided by Hilt.
 */
@Composable
internal fun SignUpScreen(
    onSignInLinkClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    onCheckYourEmail: (String) -> Unit,
    onProfileSetup: () -> Unit,
    onPrevious: () -> Unit
) {
    val signUpState by signUpViewModel.signUpUiState.collectAsStateWithLifecycle()

    // Listen for navigation events
    LaunchedEffect(Unit) {
        signUpViewModel.events.collect { event ->
            when (event) {
                is SignInEvent.NavigateToVerifyEmail -> onCheckYourEmail(event.email)
                SignInEvent.NavigateToProfileSetup -> onProfileSetup()
                SignInEvent.NavigateToPrevious -> onPrevious()
            }
        }
    }


    StatefulComposable(
        state = signUpState,
        onShowSnackbar = onShowSnackbar,
    ) { authScreenData ->
        SignUpScreen(
            screenData = authScreenData,
            onEmailChange = signUpViewModel::updateEmail,
            onPasswordChange = signUpViewModel::updatePassword,
            onConfirmPasswordChange = signUpViewModel::updateConfirmPassword,
            onSignUpClick = signUpViewModel::registerWithEmailAndPassword,
            onSignInLinkClick = onSignInLinkClick
        )
    }
}

/**
 * Composable function for the SignUp screen.
 *
 * @param screenData [SignUpScreenData].
 * @param onEmailChange Callback to update the email.
 * @param onPasswordChange Callback to update the password.
 * @param onConfirmPasswordChange Callback to update the confirm password.
 * @param onSignInLinkClick Callback to navigate to sign in.
 */
@Composable
private fun SignUpScreen(
    screenData: SignUpScreenData,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignInLinkClick: () -> Unit,
    onSignUpClick: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val activity = context.getActivity()

    // DatePickerDialog setup
    val calendar = Calendar.getInstance()
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _, year, month, dayOfMonth ->
//            val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
//            onDobChange(formattedDate)
//        },
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH),
//    ).apply {
//        // Restrict future dates
//        datePicker.maxDate = calendar.timeInMillis
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Text(
            text = stringResource(id = R.string.sign_up),
            style = MaterialTheme.typography.headlineLarge,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.already_have_an_account))
            JetpackTextButton(onClick = onSignInLinkClick) {
                Text(
                    text = stringResource(R.string.sign_in),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Account Field
//        JetpackTextField(
//            value = screenData.account.value,
//            errorMessage = screenData.account.errorMessage,
//            onValueChange = onAccountChange,
//            label = { Text("Account") },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "Account",
//                )
//            },
//        )

        // Nickname Field
//        JetpackTextField(
//            value = screenData.nickname.value,
//            errorMessage = screenData.nickname.errorMessage,
//            onValueChange = onNicknameChange,
//            label = { Text("Nickname") },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = "Nickname",
//                )
//            },
//        )

        // Date of Birth Field
//        JetpackTextField(
//            value = screenData.dob.value,
//            errorMessage = screenData.dob.errorMessage,
//            onValueChange = {},
//            readOnly = true,
//            label = { Text("Date of Birth") },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.CalendarToday,
//                    contentDescription = "Date of Birth",
//                )
//            },
//            trailingIcon = {
//                IconButton(onClick = { datePickerDialog.show() }) {
//                    Icon(
//                        imageVector = Icons.Default.CalendarToday,
//                        contentDescription = "Pick Date",
//                    )
//                }
//            },
//        )

        // Email Field
        JetpackTextField(
            value = screenData.email.value,
            errorMessage = screenData.email.errorMessage,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email),
                )
            },
        )

        // Password Field
        JetpackTextField(
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

        // Confirm Password Field
        JetpackPasswordField(
            value = screenData.confirmPassword.value,
            errorMessage = screenData.confirmPassword.errorMessage,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = "Confirm Password",
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sign Up Button
        JetpackButton(
            onClick = {
                focusManager.clearFocus()
                onSignUpClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
            text = { Text(stringResource(R.string.sign_up)) },
        )

//        DividerWithText(text = R.string.or, modifier = Modifier.padding(vertical = 16.dp))
//
//        // Google Sign Up Button
//        JetpackOutlinedButton(
//            onClick = { activity?.run { /* TODO: trigger Google sign up */ } },
//            text = { Text(text = stringResource(R.string.sign_up_with_google)) },
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

        Spacer(modifier = Modifier.height(40.dp))

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
private fun SignUpScreenPreview() {
    SignUpScreen(
        screenData = SignUpScreenData(),
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onSignInLinkClick = {},
        onSignUpClick = {}
    )
}