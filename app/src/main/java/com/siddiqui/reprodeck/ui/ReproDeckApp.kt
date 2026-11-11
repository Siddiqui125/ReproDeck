package com.siddiqui.reprodeck.ui

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FolderCopy
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import com.siddiqui.reprodeck.data.repository.SnapshotRepository
import com.siddiqui.reprodeck.domain.ReportFormatter
import com.siddiqui.reprodeck.ui.screens.CompareScreen
import com.siddiqui.reprodeck.ui.screens.HomeScreen
import com.siddiqui.reprodeck.ui.screens.SessionsScreen
import com.siddiqui.reprodeck.ui.screens.SettingsScreen
import com.siddiqui.reprodeck.ui.theme.Ink
import com.siddiqui.reprodeck.ui.viewmodel.ReproDeckViewModel
import com.siddiqui.reprodeck.ui.viewmodel.ReproDeckViewModelFactory

private fun shareReport(
    context: android.content.Context,
    content: String,
    chooserTitle: String
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "ReproDeck diagnostic snapshot")
        putExtra(Intent.EXTRA_TEXT, content)
    }
    context.startActivity(Intent.createChooser(sendIntent, chooserTitle))
}

private enum class AppTab(val label: String) {
    HOME("Home"),
    SESSIONS("Sessions"),
    SETTINGS("Privacy")
}

@Composable
fun ReproDeckApp(repository: SnapshotRepository) {
    val viewModel: ReproDeckViewModel = viewModel(
        factory = ReproDeckViewModelFactory(repository)
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTab by rememberSaveable { mutableStateOf(AppTab.HOME.name) }
    var comparisonPair by remember { mutableStateOf<Pair<DeviceSnapshotEntity, DeviceSnapshotEntity>?>(null) }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    if (comparisonPair != null) {
        val pair = comparisonPair!!
        CompareScreen(
            baseline = pair.first,
            comparison = pair.second,
            onBack = { comparisonPair = null }
        )
        return
    }

    Scaffold(
        containerColor = Ink,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(containerColor = Ink) {
                AppTab.entries.forEach { tab ->
                    val selected = selectedTab == tab.name
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedTab = tab.name },
                        icon = {
                            Icon(
                                imageVector = when (tab) {
                                    AppTab.HOME -> Icons.Outlined.Dashboard
                                    AppTab.SESSIONS -> Icons.Outlined.FolderCopy
                                    AppTab.SETTINGS -> Icons.Outlined.Tune
                                },
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(padding)) {
            when (AppTab.valueOf(selectedTab)) {
                AppTab.HOME -> HomeScreen(
                    snapshots = state.snapshots,
                    isCapturing = state.isCapturing,
                    onCapture = viewModel::captureSnapshot,
                    onSeeSessions = { selectedTab = AppTab.SESSIONS.name }
                )
                AppTab.SESSIONS -> SessionsScreen(
                    snapshots = state.snapshots,
                    onCompare = { first, second -> comparisonPair = first to second },
                    onDelete = viewModel::deleteSnapshot,
                    onShareMarkdown = { snapshot ->
                        shareReport(
                            context = context,
                            content = ReportFormatter.toMarkdown(snapshot),
                            chooserTitle = "Share Markdown report"
                        )
                    },
                    onShareJson = { snapshot ->
                        shareReport(
                            context = context,
                            content = ReportFormatter.toJson(snapshot),
                            chooserTitle = "Share JSON report"
                        )
                    }
                )
                AppTab.SETTINGS -> SettingsScreen()
            }
        }
    }

}
