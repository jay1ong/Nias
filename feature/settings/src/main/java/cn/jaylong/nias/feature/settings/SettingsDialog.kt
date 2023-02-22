package cn.jaylong.nias.feature.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.jaylong.nias.core.designsystem.theme.supportsDynamicTheming
import cn.jaylong.nias.core.model.data.DarkThemeConfig
import cn.jaylong.nias.core.model.data.ThemeBrand

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/14
 */
@ExperimentalLifecycleComposeApi
@Composable
fun SettingsDialog(
    onClear: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsDialog(
        onClear = onClear,
        onDismiss = onDismiss,
        settingsUiState = settingsUiState,
        onChangeThemeBrand = viewModel::updateThemeBrand,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsDialog(
    settingsUiState: SettingsUiState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onClear: () -> Unit,
    onDismiss: () -> Unit,
    onChangeThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(id = R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Divider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                when (settingsUiState) {
                    SettingsUiState.Loading ->
                        Text(
                            text = stringResource(id = R.string.loading),
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    is SettingsUiState.Success -> {
                        SettingPanel(
                            settings = settingsUiState.settings,
                            supportDynamicColor = supportDynamicColor,
                            onChangeThemeBrand = onChangeThemeBrand,
                            onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                            onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                        )
                    }
                }
                Divider(Modifier.padding(top = 8.dp))
                LinksPanel()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(
                    id = R.string.dismiss_dialog_button_text
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() }
            )
        },
        dismissButton = {
            Text(
                text = stringResource(
                    id = R.string.clear_dismiss_dialog_button_text
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onClear()
                        onDismiss()
                    },

                )
        }
    )
}

@Composable
private fun SettingPanel(
    settings: UserEditableSettings,
    supportDynamicColor: Boolean,
    onChangeThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    SettingsDialogSectionTitle(text = stringResource(id = R.string.theme))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooseRow(
            text = stringResource(id = R.string.brand_default),
            selected = settings.brand == ThemeBrand.DEFAULT,
            onClick = { onChangeThemeBrand(ThemeBrand.DEFAULT) }
        )
        SettingsDialogThemeChooseRow(
            text = stringResource(id = R.string.brand_android),
            selected = settings.brand == ThemeBrand.ANDROID,
            onClick = { onChangeThemeBrand(ThemeBrand.ANDROID) }
        )
    }

    if (settings.brand == ThemeBrand.DEFAULT && supportDynamicColor) {
        SettingsDialogSectionTitle(text = stringResource(id = R.string.dynamic_color_preference))
        Column(Modifier.selectableGroup()) {
            SettingsDialogThemeChooseRow(
                text = stringResource(id = R.string.dynamic_color_yes),
                selected = settings.useDynamicColor,
                onClick = { onChangeDynamicColorPreference(true) })

            SettingsDialogThemeChooseRow(
                text = stringResource(id = R.string.dynamic_color_no),
                selected = !settings.useDynamicColor,
                onClick = { onChangeDynamicColorPreference(false) })
        }
        SettingsDialogSectionTitle(text = stringResource(id = R.string.dark_mode_preference))
        Column(Modifier.selectableGroup()) {
            SettingsDialogThemeChooseRow(
                text = stringResource(id = R.string.dark_mode_config_system_default),
                selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
                onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) })

            SettingsDialogThemeChooseRow(
                text = stringResource(id = R.string.dark_mode_config_light),
                selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
                onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) })
            SettingsDialogThemeChooseRow(
                text = stringResource(id = R.string.dark_mode_config_dark),
                selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
                onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) })
        }
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsDialogThemeChooseRow(
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
            .padding(8.dp)
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
private fun LinksPanel() {
    Row(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                TextLink(
                    text = stringResource(id = R.string.privacy_policy),
                    url = PRIVACY_POLICY_URL
                )
                Spacer(modifier = Modifier.width(16.dp))
                TextLink(
                    text = stringResource(id = R.string.licenses),
                    url = LICENSES_URL
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row {
                TextLink(
                    text = stringResource(id = R.string.brand_guidelines),
                    url = BRAND_GUIDELINES_URL
                )
                Spacer(modifier = Modifier.width(16.dp))
                TextLink(
                    text = stringResource(id = R.string.feedback),
                    url = FEEDBACK_URL
                )
            }
        }
    }
}

@Composable
private fun TextLink(text: String, url: String) {
    val launchResourceIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val context = LocalContext.current

    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clickable {
                ContextCompat.startActivity(context, launchResourceIntent, null)
            }
    )
}

private const val PRIVACY_POLICY_URL = "https://policies.google.com/privacy"
private const val LICENSES_URL =
    "https://github.com/android/nowinandroid/blob/main/app/LICENSES.md#open-source-licenses-and-copyright-notices"
private const val BRAND_GUIDELINES_URL =
    "https://developer.android.com/distribute/marketing-tools/brand-guidelines"
private const val FEEDBACK_URL = "https://goo.gle/nia-app-feedback"

@Preview
@Composable
fun SettingsDialogPreview() {
    MaterialTheme {
        SettingsDialog(
            onClear = { /*TODO*/ },
            onDismiss = { /*TODO*/ },
            settingsUiState =
            SettingsUiState.Success(
                UserEditableSettings(
                    brand = ThemeBrand.DEFAULT,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    useDynamicColor = false
                )
            ),
            onChangeThemeBrand = {},
            onChangeDynamicColorPreference = {},
            onChangeDarkThemeConfig = {})
    }
}