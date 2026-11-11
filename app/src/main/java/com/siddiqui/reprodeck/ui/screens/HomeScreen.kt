package com.siddiqui.reprodeck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import com.siddiqui.reprodeck.ui.components.SnapshotCard
import com.siddiqui.reprodeck.ui.theme.Amber
import com.siddiqui.reprodeck.ui.theme.Ink
import com.siddiqui.reprodeck.ui.theme.Mint
import com.siddiqui.reprodeck.ui.theme.MintSoft
import com.siddiqui.reprodeck.ui.theme.Panel
import com.siddiqui.reprodeck.ui.theme.PanelElevated
import com.siddiqui.reprodeck.ui.theme.TextMuted

@Composable
fun HomeScreen(
    snapshots: List<DeviceSnapshotEntity>,
    isCapturing: Boolean,
    onCapture: () -> Unit,
    onSeeSessions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("REPRODECK", color = Mint, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("Debug with context.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "Capture your test environment. Compare what changed.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = PanelElevated),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF1C3B4B), Color(0xFF1C2943), Color(0xFF182238))
                        )
                    )
                    .padding(22.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("NEW TEST SESSION", color = MintSoft, fontSize = 10.sp, letterSpacing = 1.8.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "Capture a\nbaseline snapshot",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color.White.copy(alpha = 0.10f)
                        ) {
                            Icon(
                                Icons.Outlined.BugReport,
                                contentDescription = null,
                                tint = Mint,
                                modifier = Modifier.padding(14.dp).size(34.dp)
                            )
                        }
                    }
                    Text(
                        "Save device, OS, display, battery and network context in one tap.",
                        color = Color(0xFFD4DFE9),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = onCapture,
                        enabled = !isCapturing,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Mint,
                            contentColor = Ink
                        )
                    ) {
                        if (isCapturing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Ink
                            )
                        } else {
                            Icon(Icons.Outlined.Fingerprint, contentDescription = null)
                        }
                        Text(
                            text = if (isCapturing) "Capturing…" else "Capture device snapshot",
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("Your workspace", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("${snapshots.size} saved ${if (snapshots.size == 1) "snapshot" else "snapshots"}", color = TextMuted, style = MaterialTheme.typography.bodySmall)
            }
            OutlinedButton(onClick = onSeeSessions, shape = RoundedCornerShape(14.dp)) {
                Text("View all")
                Icon(Icons.Outlined.History, contentDescription = null, modifier = Modifier.padding(start = 6.dp))
            }
        }

        if (snapshots.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Outlined.Devices, contentDescription = null, tint = Mint, modifier = Modifier.size(34.dp))
                    Text("No snapshots yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        "Capture your first device baseline to start building a reproducible test history.",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            SnapshotCard(snapshot = snapshots.first())
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Panel),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CompareArrows, contentDescription = null, tint = Mint)
                    Text("Compare environments", modifier = Modifier.padding(start = 9.dp), fontWeight = FontWeight.SemiBold)
                }
                Text(
                    "Choose two snapshots to see differences in OS, app version, display, connectivity and more.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Lock, contentDescription = null, tint = MintSoft, modifier = Modifier.size(16.dp))
                    Text("Private by default. Data stays on this device.", color = TextMuted, fontSize = 11.sp, modifier = Modifier.padding(start = 7.dp))
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}
