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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.core.data.model.Profile
import com.alphagamingarcade.feature.user.ui.profile.ProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ─── Palette ─────────────────────────────────────────────────────────────────

private val AccentPurple  = Color(0xFF7B2FBE)
private val AccentGold    = Color(0xFFFFBF00)
private val AccentBlue    = Color(0xFF2563EB)
private val AccentRed     = Color(0xFFE53935)
private val SurfaceGray   = Color(0xFFF5F6FA)
private val BorderGray    = Color(0xFFEEEEF5)
private val TextPrimary   = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF8A8A9A)
private val IconBgPurple  = Color(0xFFEDE9FE)
private val IconBgGreen   = Color(0xFFD1FAE5)
private val IconBgAmber   = Color(0xFFFEF3C7)
private val IconBgBlue    = Color(0xFFDBEAFE)
private val IconBgRed     = Color(0xFFFFE4E4)

// ─── Entry Point ─────────────────────────────────────────────────────────────

@Composable
internal fun ProfileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    userViewModel: ProfileViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onTermsAndPrivacyClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onTransactionClick: () -> Unit,
) {
    val profileState by userViewModel.profileUiState.collectAsStateWithLifecycle()


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
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDailyReward by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 40.dp),
            ) {
                ProfileHero(profile = profile)

                WalletCard(
                    balance = profile.userBalance.toString(),
                    onRechargeClick = { showDailyReward = true },
                    onTransactionClick = onTransactionClick
                )
                Spacer(Modifier.height(24.dp))
                StatsRow(balance = profile.userBalance.toString())
                Spacer(Modifier.height(24.dp))
                SectionLabel(title = "Account")
                MenuCard {
                    MenuItem(
                        icon = Icons.Default.Person,
                        iconBg = IconBgPurple,
                        iconTint = AccentPurple,
                        title = "Edit Profile",
                        subtitle = "Update your personal info",
                        onClick = onEditProfileClick,
                    )
                    MenuDivider()
                    MenuItem(
                        icon = Icons.Default.Lock,
                        iconBg = IconBgGreen,
                        iconTint = Color(0xFF059669),
                        title = "Change Password",
                        subtitle = "Manage security settings",
                        onClick = onChangePasswordClick,
                    )
                    MenuDivider()
                    MenuItem(
                        icon = Icons.Default.Description,
                        iconBg = IconBgAmber,
                        iconTint = Color(0xFFD97706),
                        title = "Terms & Privacy",
                        subtitle = "Read legal documents",
                        onClick = onTermsAndPrivacyClick,
                    )
                }
                Spacer(Modifier.height(16.dp))
                SectionLabel(title = "Help & Support")
                MenuCard {
                    MenuItem(
                        icon = Icons.Default.SupportAgent,
                        iconBg = IconBgBlue,
                        iconTint = AccentBlue,
                        title = "Contact Support",
                        subtitle = "Get help from our team",
                        onClick = onContactSupportClick,
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }

    // ── Daily Reward Bottom Sheet ─────────────────────────────────────────────
    if (showDailyReward) {
        DailyRewardBottomSheet(
            sheetState = sheetState,
            onDismiss = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showDailyReward = false
                }
            },
        )
    }
}

// ─── Daily Reward Bottom Sheet ────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyRewardBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
) {
    var isClaimed by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(28800) }

    LaunchedEffect(isClaimed) {
        if (isClaimed) {
            while (remainingSeconds > 0) {
                delay(1000L)
                remainingSeconds--
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,                        // 👈 white background
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(BorderGray),                 // 👈 subtle gray handle
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)                     // 👈 white sheet
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Header label
            Text(
                text = "DAILY REWARD",
                color = AccentPurple,                        // 👈 purple on white
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                letterSpacing = 1.5.sp,
            )

            // Coin badge — purple gradient pill on white bg
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AccentPurple, Color(0xFF3A1078)), // 👈 gradient accent on white bg
                        ),
                    )
                    .padding(horizontal = 40.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = "🪙", fontSize = 48.sp)
                    Text(
                        text = "+ 500",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                    )
                    Text(
                        text = "Coins",
                        fontSize = 14.sp,
                        color = AccentGold,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            // Description
            Text(
                text = "Come back every day to claim\nyour free coins!",
                fontSize = 13.sp,
                color = TextSecondary,                       // 👈 gray on white
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
            )

            // Divider
            HorizontalDivider(color = BorderGray)

            Spacer(Modifier.height(4.dp))

            // Claim / Claimed button
            Button(
                onClick = { isClaimed = true },
                enabled = !isClaimed,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentPurple,           // 👈 purple button on white bg
                    contentColor = Color.White,
                    disabledContainerColor = SurfaceGray,    // 👈 gray when disabled
                    disabledContentColor = TextSecondary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Text(
                    text = if (isClaimed) "✅ Claimed" else "⚡ Claim Now",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            // Countdown timer — only visible after claiming
            if (isClaimed) {
                val hours = remainingSeconds / 3600
                val minutes = (remainingSeconds % 3600) / 60
                val seconds = remainingSeconds % 60
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceGray)             // 👈 subtle gray pill
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "⏱ Next claim in: %02d:%02d:%02d".format(hours, minutes, seconds),
                        fontSize = 12.sp,
                        color = TextSecondary,               // 👈 gray text on gray pill
                        fontFamily = FontFamily.Monospace,
                    )
                }
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
            .background(Color.White)
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
                        color = Color(0xFFE5E7EB),
                        shape = CircleShape,
                    )
                    .padding(3.dp)
                    .background(Color(0xFFF9FAFB), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = profile.userName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = profile.userName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF111827),
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
                    text = "Gold Member",
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
                Text(text = "Online", fontSize = 11.sp, color = Color(0xFF6B7280))
            }
        }
    }
    HorizontalDivider(color = Color(0xFFF3F4F6))
}

// ─── Stats Row ────────────────────────────────────────────────────────────────

@Composable
private fun StatsRow(balance: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatCard(
            label = "Games Played",
            value = "128",
            emoji = "🎮",
            iconBg = IconBgPurple,
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = "Favorites",
            value = "24",
            emoji = "❤️",
            iconBg = IconBgRed,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    emoji: String,
    iconBg: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderGray, RoundedCornerShape(16.dp))  // 👈 same border as MenuCard
            .background(Color.White)                               // 👈 white like MenuItem
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Icon box — same style as MenuItem
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),                               // 👈 colored bg like MenuItem
            contentAlignment = Alignment.Center,
        ) {
            Text(text = emoji, fontSize = 20.sp)
        }

        // Text stack
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,                               // 👈 same as MenuItem title
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextSecondary,                             // 👈 same as MenuItem subtitle
            )
        }
    }
}

// ─── Wallet Card ─────────────────────────────────────────────────────────────

@Composable
private fun WalletCard(
    balance: String,
    onRechargeClick: () -> Unit,
    onTransactionClick: () -> Unit
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
                text = "💳 WALLET BALANCE",
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
                text = "Coins",
                fontSize = 12.sp,
                color = AccentGold,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onRechargeClick,   // 👈 wired up
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                    ),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = "🎁 Daily Reward", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                    Text(text = "Transactions", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ─── Section Label ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = TextSecondary,
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
            .border(1.dp, BorderGray, RoundedCornerShape(20.dp))
            .background(Color.White),
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
                modifier = Modifier.size(20.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
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
        color = BorderGray,
    )
}