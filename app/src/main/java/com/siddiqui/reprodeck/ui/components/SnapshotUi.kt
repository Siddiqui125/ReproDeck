package com.siddiqui.reprodeck.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.NetworkCheck
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import com.siddiqui.reprodeck.ui.theme.Mint
import com.siddiqui.reprodeck.ui.theme.Panel
import com.siddiqui.reprodeck.ui.theme.PanelElevated
import com.siddiqui.reprodeck.ui.theme.TextMuted
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val captureDateFormatter = DateTimeFormatter
    .ofPattern("MMM d • h:mm a")
    .withZone(ZoneId.systemDefault())

@Composable
fun SnapshotCard(
    snapshot: DeviceSnapshotEntity,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Panel).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${snapshot.manufacturer} ${snapshot.model}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = captureDateFormatter.format(
                            Instant.ofEpochMilli(snapshot.capturedAtEpochMillis)
                        ),
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                trailing?.invoke()
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoPill(
                    icon = { Icon(Icons.Outlined.Android, contentDescription = null, tint = Mint) },
                    text = "Android ${snapshot.androidRelease}"
                )
                InfoPill(
                    icon = { Icon(Icons.Outlined.NetworkCheck, contentDescription = null, tint = Mint) },
                    text = snapshot.networkTransport
                )
            }
        }
    }
}

@Composable
fun InfoPill(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(PanelElevated)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        icon()
        Text(text = text, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
