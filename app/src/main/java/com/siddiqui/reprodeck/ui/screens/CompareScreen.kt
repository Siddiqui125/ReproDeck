package com.siddiqui.reprodeck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import com.siddiqui.reprodeck.domain.SnapshotDiffEngine
import com.siddiqui.reprodeck.domain.SnapshotDifference
import com.siddiqui.reprodeck.ui.theme.Mint
import com.siddiqui.reprodeck.ui.theme.Panel
import com.siddiqui.reprodeck.ui.theme.PanelElevated
import com.siddiqui.reprodeck.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    baseline: DeviceSnapshotEntity,
    comparison: DeviceSnapshotEntity,
    onBack: () -> Unit
) {
    val differences = SnapshotDiffEngine.compare(baseline, comparison)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Environment diff") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets).padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CompareArrows, contentDescription = null, tint = Mint)
                        Text(
                            "${differences.size} ${if (differences.size == 1) "difference" else "differences"} found",
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        "Comparing ${baseline.manufacturer} ${baseline.model} against ${comparison.manufacturer} ${comparison.model}.",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (differences.isEmpty()) {
                Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Mint)
                        Text("No tracked environment differences were found.", modifier = Modifier.padding(start = 10.dp))
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(differences, key = { it.field }) { difference ->
                        DifferenceCard(difference)
                    }
                }
            }
        }
    }
}

@Composable
private fun DifferenceCard(difference: SnapshotDifference) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(difference.field, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ValueCell(label = "BASELINE", value = difference.baselineValue, modifier = Modifier.weight(1f))
                ValueCell(label = "COMPARISON", value = difference.comparisonValue, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ValueCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(PanelElevated, RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(label, color = TextMuted, style = MaterialTheme.typography.labelSmall)
        Text(value, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
    }
}
