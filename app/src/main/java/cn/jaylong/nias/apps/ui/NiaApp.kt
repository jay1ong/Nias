package cn.jaylong.nias.apps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import cn.jaylong.nias.apps.R
import cn.jaylong.nias.apps.navigation.NiaNavHost
import cn.jaylong.nias.apps.navigation.TopLevelDestination
import cn.jaylong.nias.core.data.util.NetworkMonitor
import cn.jaylong.nias.core.designsystem.component.*
import cn.jaylong.nias.core.designsystem.icon.Icon
import cn.jaylong.nias.core.designsystem.icon.NiaIcons
import cn.jaylong.nias.core.designsystem.theme.GradientColors
import cn.jaylong.nias.core.designsystem.theme.LocalGradientColors
import cn.jaylong.nias.feature.foryou.ForYouViewModel
import cn.jaylong.nias.feature.settings.SettingsDialog
import cn.jaylong.nias.feature.settings.R as settingsR

/**
 * app
 *
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/10
 */
@OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun NiaApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    appState: NiaAppState = rememberNiaAppState(
        windowsSizeClass = windowSizeClass,
        networkMonitor = networkMonitor
    )
) {
    val shouldShowGradientBackground =
        appState.currentTopDestination == TopLevelDestination.FOR_YOU

    NiaBackground {
        NiaGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            }
        ) {
            val snackbarHostState = remember { SnackbarHostState() }

            val isOffline by appState.isOffline.collectAsStateWithLifecycle()

            val notConnectMessage = stringResource(id = R.string.not_connected)
            LaunchedEffect(isOffline) {
                if (isOffline) {
                    snackbarHostState.showSnackbar(
                        message = notConnectMessage,
                        duration = SnackbarDuration.Indefinite,
                    )
                }
            }

            if (appState.shouldShowSettingDialog) {
                val forYouViewModel: ForYouViewModel = viewModel()
                SettingsDialog(
                    onClear = { forYouViewModel.setTopicSelections(setOf()) },
                    onDismiss = { appState.setShowSettingDialog(false) })
            }

            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                bottomBar = {
                    if (appState.shouldShowBottomBar) {
                        NiaBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigationToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination,
                            modifier = Modifier.testTag("NiaBottomBar")
                        )
                    }
                },
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumedWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal
                            )
                        )
                ) {
                    if (appState.shouldShowNavRail) {
                        NiaNavRail(
                            destinations = appState.topLevelDestinations,
                            onNavigationToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination,
                            modifier = Modifier
                                .testTag("NiaNavRail")
                                .safeDrawingPadding(),
                        )
                    }
                    Column(Modifier.fillMaxSize()) {
                        val destination = appState.currentTopDestination
                        if (destination != null) {
                            NiaTopAppBar(
                                titleRes = destination.titleTextId,
                                actionIcon = NiaIcons.Settings,
                                actionIconContentDescription = stringResource(id = settingsR.string.settings_title),
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = Color.Transparent,
                                ),
                                onActionClick = { appState.setShowSettingDialog(true) },
                            )
                        }
                        NiaNavHost(
                            navController = appState.navController,
                            onBackClick = appState::onBackClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NiaNavRail(
    destinations: List<TopLevelDestination>,
    onNavigationToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    NiaNavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NiaNavigationRailItem(
                selected = selected,
                onClick = { onNavigationToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectIcon
                    } else {
                        destination.unselectedIcon
                    }
                    when (icon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null,
                        )
                        is Icon.DrawableResourceIcon -> Icon(
                            painterResource(id = icon.id),
                            contentDescription = null,
                        )
                    }
                },
                label = { Text(text = stringResource(id = destination.iconTextId)) }
            )
        }
    }
}

@Composable
private fun NiaBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigationToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    NiaNavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NiaNavigationBarItem(
                selected = selected,
                onClick = { onNavigationToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectIcon
                    } else {
                        destination.unselectedIcon
                    }
                    when (icon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null,
                        )
                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null,
                        )
                    }
                },
                label = { Text(text = stringResource(id = destination.iconTextId)) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false