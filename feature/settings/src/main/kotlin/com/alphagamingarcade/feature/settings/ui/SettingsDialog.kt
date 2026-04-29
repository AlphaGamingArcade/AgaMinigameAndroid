package com.alphagamingarcade.feature.settings.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.alphagamingarcade.core.ui.components.JetpackOutlinedButton
import com.alphagamingarcade.core.ui.components.JetpackTextButton
import com.alphagamingarcade.core.ui.components.JetpackToggleOptions
import com.alphagamingarcade.core.ui.components.ToggleOption
import com.alphagamingarcade.core.ui.theme.supportsDynamicTheming
import com.alphagamingarcade.core.ui.utils.PreviewDevices
import com.alphagamingarcade.core.ui.utils.PreviewThemes
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.core.data.model.DarkThemeConfig
import com.alphagamingarcade.core.data.model.Language
import com.alphagamingarcade.core.data.model.Settings
import com.alphagamingarcade.core.data.utils.FEEDBACK_URL
import com.alphagamingarcade.core.data.utils.PRIVACY_POLICY_URL
import com.alphagamingarcade.core.data.utils.TERMS_OF_SERVICE_URL
import com.alphagamingarcade.feature.settings.R

/**
 * Settings dialog.
 *
 * @param onDismiss Callback when the dialog is dismissed.
 * @param onShowSnackbar Callback to show a snackbar.
 * @param settingsViewModel [SettingsViewModel].
 */
@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = settingsState,
        onShowSnackbar = onShowSnackbar,
    ) { settings ->
        SettingsDialog(
            settings = settings,
            onDismiss = onDismiss,
            onChangeDynamicColorPreference = settingsViewModel::updateDynamicColorPreference,
            onChangeDarkThemeConfig = settingsViewModel::updateDarkThemeConfig,
            onChangeLanguage = settingsViewModel::updateLanguagePreference
        )
    }
}

/**
 * Settings dialog.
 *
 * @param settings [Settings].
 * @param onDismiss Callback when the dialog is dismissed.
 * @param onChangeDynamicColorPreference Callback when the dynamic color preference is changed.
 * @param onChangeDarkThemeConfig Callback when the dark theme config is changed.
 * @param supportDynamicColor Whether dynamic color is supported.
 */
@Composable
private fun SettingsDialog(
    settings: Settings,
    onDismiss: () -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onChangeLanguage: (language: Language) -> Unit,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
) {
    val configuration = LocalConfiguration.current

    /**
     * usePlatformDefaultWidth = false is use as a temporary fix to allow
     * height recalculation during recomposition. This, however, causes
     * Dialog's to occupy full width in Compact mode. Therefore max width
     * is configured below. This should be removed when there's fix to
     * https://issuetracker.google.com/issues/221643630
     */
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
        },
        text = {
            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SettingsPanel(
                    settings = settings,
                    onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                    onChangeLanguage = onChangeLanguage
                )
                HorizontalDivider(Modifier.padding(top = 8.dp))
                LinksPanel()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.dismiss_dialog_button_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}
@Composable
private fun SettingsDialogLanguageRow(
    language: Language,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = language.flag,
                style = MaterialTheme.typography.titleLarge,  // bigger so flag is visible
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(language.toStringRes()),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun ColumnScope.SettingsPanel(
    settings: Settings,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onChangeLanguage: (language: Language) -> Unit
) {
    var showLanguagePicker by remember { mutableStateOf(false) }

    SettingsDialogSectionTitle(text = stringResource(R.string.language))
    SettingsDialogLanguageRow(
        language = settings.language,
        onClick = { showLanguagePicker = true },
    )

    if (showLanguagePicker) {
        LanguagePickerBottomSheet(
            selectedLanguage = settings.language,
            onLanguageSelected = onChangeLanguage,
            onDismiss = { showLanguagePicker = false },
        )
    }

    SettingsDialogSectionTitle(text = stringResource(R.string.dark_mode_preference))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_system_default),
            selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_light),
            selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_dark),
            selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) },
        )
    }
}

/**
 * Settings dialog section title.
 *
 * @param text The title text.
 */
@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

/**
 * Settings dialog theme chooser row.
 *
 * @param text The text to display.
 * @param selected Whether the row is selected.
 * @param onClick Callback when the row is clicked.
 */
@Composable
private fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

/**
 * Links panel.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LinksPanel() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        val uriHandler = LocalUriHandler.current
        JetpackTextButton(
            onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) },
        ) {
            Text(text = stringResource(R.string.privacy_policy))
        }
        JetpackTextButton(
            onClick = { uriHandler.openUri(TERMS_OF_SERVICE_URL) },
        ) {
            Text(text = stringResource(R.string.terms_of_service))
        }
        val context = LocalContext.current
        JetpackTextButton(
            onClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
        ) {
            Text(text = stringResource(R.string.licenses))
        }
        JetpackTextButton(
            onClick = { uriHandler.openUri(FEEDBACK_URL) },
        ) {
            Text(text = stringResource(R.string.feedback))
        }
    }
}

fun Language.toStringRes(): Int = when (this) {
    Language.ENGLISH -> R.string.english
    Language.KOREAN -> R.string.korean
    Language.CHINESE -> R.string.chinese
    Language.JAPANESE -> R.string.japanese
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguagePickerBottomSheet(
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
        ) {
            Text(
                text = stringResource(R.string.select_language),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            Language.entries.forEach { language ->
                val isSelected = language == selectedLanguage
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else Color.Transparent,
                        )
                        .clickable {
                            onLanguageSelected(language)
                            onDismiss()
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = language.flag,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(language.toStringRes()),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
    }
}


@Composable
@PreviewThemes
@PreviewDevices
private fun SettingsDialogPreview() {
    SettingsDialog(
        settings = Settings(),
        onDismiss = {},
        onChangeDynamicColorPreference = {},
        onChangeDarkThemeConfig = {},
        onChangeLanguage = {},
        supportDynamicColor = true,
    )
}
