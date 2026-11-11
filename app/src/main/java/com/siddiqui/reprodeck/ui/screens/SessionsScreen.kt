package com.siddiqui.reprodeck.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import com.siddiqui.reprodeck.ui.components.SnapshotCard
import com.siddiqui.reprodeck.ui.theme.Mint
import com.siddiqui.reprodeck.ui.theme.Panel
import com.siddiqui.reprodeck.ui.theme.TextMuted

@Composable
fun SessionsScreen(
    snapshots: List<DeviceSnapshotEntity>,
    onCompare: (DeviceSnapshotEntity, DeviceSnapshotEntity) -> Unit,
    onDelete: (DeviceSnapshotEntity) -> Unit,
    onShareMarkdown: (DeviceSnapshotEntity) -> Unit,
    onShareJson: (DeviceSnapshotEntity) -> Unit
) {
    val selectedIds = remember { mutableStateListOf<Long>() }
    val compareMode = selectedIds.isNotEmpty()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Sessions", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Your saved device environments", color = TextMuted)
        }

        if (snapshots.size >= 2) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CompareArrows, tint = Mint, contentDescription = null)
                        Text("Compare snapshots", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.SemiBold)
                    }
                    Text(
                        if (compareMode) "Select exactly two snapshots (${selectedIds.size}/2 selected)." else "Select two sessions to identify environment differences.",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            enabled = selectedIds.size == 2,
                            onClick = {
                                val first = snapshots.firstOrNull { it.id == selectedIds[0] }
                                val second = snapshots.firstOrNull { it.id == selectedIds[1] }
                                if (first != null && second != null) onCompare(first, second)
                            }
                        ) {
                            Text("Compare selected")
                        }
                        OutlinedButton(onClick = { selectedIds.clear() }) {
                            Text("Clear")
                        }
                    }
                }
            }
        }

        if (snapshots.isEmpty()) {
            Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Nothing captured yet", fontWeight = FontWeight.SemiBold)
                    Text("Your saved snapshots will appear here.", color = TextMuted)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(snapshots, key = { it.id }) { snapshot ->
                    var menuExpanded by remember(snapshot.id) { mutableStateOf(false) }
                    SnapshotCard(
                        snapshot = snapshot,
                        trailing = {
                            if (snapshots.size >= 2) {
                                Checkbox(
                                    checked = snapshot.id in selectedIds,
                                    onCheckedChange = { checked ->
                                        if (checked && selectedIds.size < 2) selectedIds.add(snapshot.id)
                                        if (!checked) selectedIds.remove(snapshot.id)
                                    }
                                )
                            }
                            Box {
                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(Icons.Outlined.MoreVert, contentDescription = "Session actions", tint = TextMuted)
                                }
                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Share Markdown report") },
                                        onClick = {
                                            menuExpanded = false
                                            onShareMarkdown(snapshot)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Share JSON report") },
                                        onClick = {
                                            menuExpanded = false
                                            onShareJson(snapshot)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete snapshot") },
                                        onClick = {
                                            menuExpanded = false
                                            onDelete(snapshot)
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
