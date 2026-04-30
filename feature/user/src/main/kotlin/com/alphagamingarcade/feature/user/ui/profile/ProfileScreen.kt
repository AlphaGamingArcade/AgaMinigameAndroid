package com.alphagamingarcade.feature.user.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.core.data.model.Profile
import com.alphagamingarcade.core.ui.utils.CurrencyFormatter
import kotlinx.coroutines.launch
import com.alphagamingarcade.feature.user.R

// ─── Palette ─────────────────────────────────────────────────────────────────

private val AccentGold    = Color(0xFFFFBF00)
private val AccentRed     = Color(0xFFE53935)
private val TextPrimary   = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF8A8A9A)

// ─── Entry Point ─────────────────────────────────────────────────────────────

@Composable
internal fun ProfileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onTermsAndPrivacyClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onTransactionClick: () -> Unit,
) {
    val profileState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val freeDepositStatus by profileViewModel.freeDeposit.collectAsStateWithLifecycle()
    val isFreeDepositClaimed = freeDepositStatus.data.claimed
    val freeDepositAmount = freeDepositStatus.data.amount
    val freeDepositCurrency = freeDepositStatus.data.currency


    StatefulComposable(
        state = profileState,
        onShowSnackbar = onShowSnackbar,
    ) { profile ->
        ProfileScreen(
            profile = profile,
            onEditProfileClick = onEditProfileClick,
            onChangePasswordClick = onChangePasswordClick,
            onTermsAndPrivacyClick =  onTermsAndPrivacyClick,
            onContactSupportClick = onContactSupportClick,
            onTransactionClick = onTransactionClick,
            onSignOut = profileViewModel::signOut,
            freeDepositAmount = freeDepositAmount,
            freeDepositCurrency = freeDepositCurrency,
            isFreeDepositClaimed = isFreeDepositClaimed,
            onClaimFreeDeposit = profileViewModel::claimFreeDeposit
        )
    }
}

// ─── Content ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    profile: Profile,
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onTermsAndPrivacyClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onTransactionClick: () -> Unit,
    onSignOut: () -> Unit,
    freeDepositAmount: Double,
    freeDepositCurrency: String,
    isFreeDepositClaimed: Boolean,
    onClaimFreeDeposit: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDailyReward by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 40.dp),
            ) {
                ProfileHero(profile = profile)

                WalletCard(
                    balance = CurrencyFormatter.format(profile.balance, profile.currency),
                    onRechargeClick = { showDailyReward = true },
                    onTransactionClick = onTransactionClick,
                    isFreeDepositClaimed = isFreeDepositClaimed
                )
                Spacer(Modifier.height(24.dp))
                SectionLabel(title = stringResource(R.string.account))
                MenuCard {
                    MenuItem(
                        icon = Icons.Default.Person,
                        iconBg = Color.Transparent,
                        iconTint = MaterialTheme.colorScheme.onSurface,
                        title = stringResource(R.string.edit_profile),
                        subtitle = stringResource(R.string.edit_profile_sub_title),
                        onClick = onEditProfileClick,
                    )
                    MenuDivider()
                    MenuItem(
                        icon = Icons.Default.Lock,
                        iconBg = Color.Transparent,
                        iconTint = MaterialTheme.colorScheme.onSurface,
                        title = stringResource(R.string.change_password),
                        subtitle = stringResource(R.string.change_password_sub_title),
                        onClick = onChangePasswordClick,
                    )
                    MenuDivider()
                    MenuItem(
                        icon = Icons.Default.Description,
                        iconBg = Color.Transparent,
                        iconTint = MaterialTheme.colorScheme.onSurface,
                        title = stringResource(R.string.terms_and_privacy),
                        subtitle = stringResource(R.string.terms_and_privacy_sub_title),
                        onClick = onTermsAndPrivacyClick,
                    )
                }
                Spacer(Modifier.height(16.dp))
                SectionLabel(title =  stringResource(R.string.help_an_support))
                MenuCard {
                    MenuItem(
                        icon = Icons.Default.SupportAgent,
                        iconBg = Color.Transparent,
                        iconTint = MaterialTheme.colorScheme.onSurface,
                        title = stringResource(R.string.contact_support),
                        subtitle = stringResource(R.string.contact_support_sub_title),
                        onClick = onContactSupportClick,
                    )
                }
                Spacer(Modifier.height(16.dp))
                SectionLabel(title =  stringResource(R.string.account_actions))
                MenuCard {
                    MenuItem(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        iconBg = Color.Transparent,
                        iconTint = AccentRed,
                        title = stringResource(R.string.sign_out),
                        subtitle = stringResource(R.string.sign_out_sub_title),
                        onClick = { showDialog = true },
                    )
                }
            }
        }
    }

    // ── Daily Reward Bottom Sheet ─────────────────────────────────────────────
    if (showDailyReward) {
        DailyRewardBottomSheet(
            sheetState = sheetState,
            isClaimed = isFreeDepositClaimed,
            freeDepositAmount = freeDepositAmount,
            freeDepositCurrency = freeDepositCurrency,
            onClaimFreeDeposit = onClaimFreeDeposit,
            onDismiss = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showDailyReward = false
                }
            },
        )
    }

    // Logout dialog
    // Inside ProfileScreen, after DailyRewardBottomSheet
    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = { showDialog = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 4.dp)
                        .size(width = 40.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.sign_out).uppercase(),
                    color = AccentRed,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.5.sp,
                )

                Text(
                    text = stringResource(R.string.sign_out_confirm),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(Modifier.height(4.dp))

                Button(
                    onClick = onSignOut,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed,
                        contentColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                ) {
                    Text(
                        text = stringResource(R.string.sign_out),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }

                OutlinedButton(
                    onClick = { showDialog = false },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextSecondary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

val SurfaceGray = Color(0xFFEEEEEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyRewardBottomSheet(
    sheetState: SheetState,
    freeDepositAmount: Double,
    freeDepositCurrency: String,
    onClaimFreeDeposit: () -> Unit,
    isClaimed: Boolean,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.outlineVariant),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.CardGiftcard,
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = stringResource(R.string.daily_reward),
                    color = AccentGold,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.5.sp,
                )
            }

            // Coin amount card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Column {
                    Text(
                        text = CurrencyFormatter.format(freeDepositAmount, freeDepositCurrency),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace,
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Claim / Claimed button
            Button(
                onClick = onClaimFreeDeposit,
                enabled = !isClaimed,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF061C20),
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Icon(
                    imageVector = if (isClaimed) Icons.Rounded.CheckCircle else Icons.Rounded.Redeem,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isClaimed) stringResource(R.string.claimed) else stringResource(R.string.claim_now),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            // Come back tomorrow notice — only visible after claiming
            // Otherwise show the daily description
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(15.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = if (isClaimed)
                        stringResource(R.string.claimed_information)
                    else
                        stringResource(R.string.claim_now_information),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}


// ─── Profile Hero ─────────────────────────────────────────────────────────────

@Composable
private fun ProfileHero(profile: Profile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .border(
                        width = 1.5.dp,
                        color = Color.Transparent,
                        shape = CircleShape,
                    )
                    .padding(3.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = profile.nickname.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = profile.nickname,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFEF9C3))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "★", fontSize = 10.sp, color = Color(0xFFCA8A04))
                Text(
                    text = stringResource(R.string.member),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFCA8A04),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF22C55E), CircleShape),
                )
                Text(text = stringResource(R.string.online), fontSize = 11.sp, color = Color(0xFF6B7280))
            }
        }
    }
}

// ─── Wallet Card ─────────────────────────────────────────────────────────────
@Composable
private fun WalletCard(
    balance: String,
    onRechargeClick: () -> Unit,
    onTransactionClick: () -> Unit,
    isFreeDepositClaimed: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF061C20), Color(0xFF0A3535)),
                ),
            )
            .padding(20.dp),
    ) {
        Column {
            Text(
                text = stringResource(R.string.wallet_balance),
                color = AccentGold,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                letterSpacing = 1.5.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = balance,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
            )
            Text(
                text = stringResource(R.string.coins),
                fontSize = 12.sp,
                color = AccentGold,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onRechargeClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                    ),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.daily_reward),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onTransactionClick,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White.copy(alpha = 0.8f),
                    ),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.transactions),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
@Composable
private fun SectionLabel(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 8.dp),
    )
}

// ─── Menu Card ────────────────────────────────────────────────────────────────

@Composable
private fun MenuCard(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(content = content)
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(25.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 72.dp, end = 18.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant,
    )
}