package com.siddiqui.reprodeck.domain

import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object ReportFormatter {
    private val dateFormatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss z")
        .withZone(ZoneId.systemDefault())

    fun toMarkdown(snapshot: DeviceSnapshotEntity): String = buildString {
        appendLine("# ReproDeck diagnostic snapshot")
        appendLine()
        appendLine("- Captured: ${dateFormatter.format(Instant.ofEpochMilli(snapshot.capturedAtEpochMillis))}")
        appendLine("- Snapshot ID: ${snapshot.id}")
        appendLine()
        appendLine("## Device")
        appendLine("- Manufacturer: ${snapshot.manufacturer}")
        appendLine("- Model: ${snapshot.model}")
        appendLine("- Android: ${snapshot.androidRelease} (API ${snapshot.apiLevel})")
        appendLine("- Security patch: ${snapshot.securityPatch}")
        appendLine("- Screen: ${snapshot.screenResolution} px")
        appendLine("- Density: ${snapshot.densityDpi} dpi")
        appendLine("- Font scale: ${snapshot.fontScale}")
        appendLine()
        appendLine("## App")
        appendLine("- Version: ${snapshot.reproDeckVersionName} (${snapshot.reproDeckVersionCode})")
        appendLine()
        appendLine("## Environment")
        appendLine("- Network: ${snapshot.networkTransport}")
        appendLine("- Internet validated: ${snapshot.internetValidated}")
        appendLine("- Battery: ${snapshot.batteryPercent?.let { "$it%" } ?: "Unavailable"}")
        appendLine("- Charging: ${snapshot.isCharging?.toString() ?: "Unavailable"}")
        appendLine("- Power saver: ${snapshot.isPowerSaveMode}")
        appendLine("- Locale: ${snapshot.localeTag}")
        appendLine("- Time zone: ${snapshot.timeZoneId}")
        appendLine()
        appendLine("---")
        appendLine("Generated locally by ReproDeck. No device serial number or advertising ID is collected.")
    }

    fun toJson(snapshot: DeviceSnapshotEntity): String = buildString {
        val battery = snapshot.batteryPercent?.toString() ?: "null"
        val charging = snapshot.isCharging?.toString() ?: "null"
        appendLine("{")
        appendLine("  \"schemaVersion\": 1,")
        appendLine("  \"snapshotId\": ${snapshot.id},")
        appendLine("  \"capturedAtEpochMillis\": ${snapshot.capturedAtEpochMillis},")
        appendLine("  \"device\": {")
        appendLine("    \"manufacturer\": ${quote(snapshot.manufacturer)},")
        appendLine("    \"model\": ${quote(snapshot.model)},")
        appendLine("    \"androidRelease\": ${quote(snapshot.androidRelease)},")
        appendLine("    \"apiLevel\": ${snapshot.apiLevel},")
        appendLine("    \"securityPatch\": ${quote(snapshot.securityPatch)},")
        appendLine("    \"screenResolution\": ${quote(snapshot.screenResolution)},")
        appendLine("    \"densityDpi\": ${snapshot.densityDpi},")
        appendLine("    \"fontScale\": ${snapshot.fontScale}")
        appendLine("  },")
        appendLine("  \"app\": {")
        appendLine("    \"reproDeckVersionName\": ${quote(snapshot.reproDeckVersionName)},")
        appendLine("    \"reproDeckVersionCode\": ${snapshot.reproDeckVersionCode}")
        appendLine("  },")
        appendLine("  \"environment\": {")
        appendLine("    \"networkTransport\": ${quote(snapshot.networkTransport)},")
        appendLine("    \"internetValidated\": ${snapshot.internetValidated},")
        appendLine("    \"batteryPercent\": $battery,")
        appendLine("    \"isCharging\": $charging,")
        appendLine("    \"isPowerSaveMode\": ${snapshot.isPowerSaveMode},")
        appendLine("    \"localeTag\": ${quote(snapshot.localeTag)},")
        appendLine("    \"timeZoneId\": ${quote(snapshot.timeZoneId)}")
        appendLine("  }")
        appendLine("}")
    }.trim()

    private fun quote(value: String): String = buildString {
        append('"')
        value.forEach { character ->
            when (character) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> {
                    if (character.code < 0x20) {
                        append("\\u%04x".format(character.code))
                    } else {
                        append(character)
                    }
                }
            }
        }
        append('"')
    }
}
