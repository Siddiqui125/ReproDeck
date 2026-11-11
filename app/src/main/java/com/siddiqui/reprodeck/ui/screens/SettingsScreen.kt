package com.siddiqui.reprodeck.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.siddiqui.reprodeck.ui.theme.Mint
import com.siddiqui.reprodeck.ui.theme.Panel
import com.siddiqui.reprodeck.ui.theme.TextMuted

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Privacy & about", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("You control what leaves your device.", color = TextMuted)
        }

        PrivacyCard(
            icon = { Icon(Icons.Outlined.CloudOff, contentDescription = null, tint = Mint) },
            title = "Offline-first storage",
            body = "Snapshots are saved in a local Room database. ReproDeck does not upload them to a server."
        )
        PrivacyCard(
            icon = { Icon(Icons.Outlined.Security, contentDescription = null, tint = Mint) },
            title = "No sensitive device identifiers",
            body = "The first version does not collect IMEI, serial number, advertising ID, contacts, location or call history."
        )
        PrivacyCard(
            icon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Mint) },
            title = "Minimal permissions",
            body = "The app requests network-state access only, to identify the active transport and whether Android reports validated internet connectivity."
        )
        PrivacyCard(
            icon = { Icon(Icons.Outlined.Info, contentDescription = null, tint = Mint) },
            title = "ReproDeck 0.1.0",
            body = "An open-source portfolio project for comparing Android test environments and producing reproducible diagnostic reports."
        )
    }
}

@Composable
private fun PrivacyCard(
    icon: @Composable () -> Unit,
    title: String,
    body: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            icon()
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(body, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }
    }
}
