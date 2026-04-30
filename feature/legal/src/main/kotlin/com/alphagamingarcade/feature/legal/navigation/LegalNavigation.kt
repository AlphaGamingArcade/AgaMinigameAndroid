package com.alphagamingarcade.feature.legal.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.legal.ui.contactsupport.ContactSupportScreen
import com.alphagamingarcade.feature.legal.ui.termsandprivacy.TermsAndPrivacyScreen
import kotlinx.serialization.Serializable

/**
 * Contact Support route.
 */
@Serializable
data object LegalNavGraph

/**
 * Contact Support route.
 */
@Serializable
data object ContactSupport


/**
 * Terms and Privacy route.
 */
@Serializable
data object TermsAndPrivacy


/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToContactSupportScreen(navOptions: NavOptions? = null) {
    navigate(ContactSupport, navOptions)
}

/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToTermsAndPrivacyScreen(navOptions: NavOptions? = null) {
    navigate(TermsAndPrivacy, navOptions)
}


///**
// * Adds the Profile screen to the navigation graph.
// *
// * @param onShowSnackbar Lambda function to show a snackbar message.
// */
fun NavGraphBuilder.termsAndPrivacyScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit,
) {
    composable<TermsAndPrivacy> {
        TermsAndPrivacyScreen(
            onShowSnackbar = onShowSnackbar,
            onBackClick = onBackClick
        )
    }
}

///**
// * Adds the Profile screen to the navigation graph.
// *
// * @param onShowSnackbar Lambda function to show a snackbar message.
// */
fun NavGraphBuilder.contactSupportScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit,
) {
    composable<ContactSupport> {
        ContactSupportScreen(
            onShowSnackbar = onShowSnackbar,
            onBackClick = onBackClick
        )
    }
}


/**
 * Auth navigation graph.
 *
 * @param nestedNavGraphs Nested navigation graphs.
 */
fun NavGraphBuilder.legalNavGraph(
    nestedNavGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<LegalNavGraph>(startDestination = TermsAndPrivacy) {
        nestedNavGraphs()
    }
}
