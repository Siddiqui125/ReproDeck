package com.siddiqui.reprodeck.domain

import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity

data class SnapshotDifference(
    val field: String,
    val baselineValue: String,
    val comparisonValue: String
)

object SnapshotDiffEngine {
    fun compare(
        baseline: DeviceSnapshotEntity,
        comparison: DeviceSnapshotEntity
    ): List<SnapshotDifference> = buildList {
        addIfDifferent("Manufacturer", baseline.manufacturer, comparison.manufacturer)
        addIfDifferent("Device model", baseline.model, comparison.model)
        addIfDifferent("Android release", baseline.androidRelease, comparison.androidRelease)
        addIfDifferent("API level", baseline.apiLevel, comparison.apiLevel)
        addIfDifferent("Security patch", baseline.securityPatch, comparison.securityPatch)
        addIfDifferent("ReproDeck build", "${baseline.reproDeckVersionName} (${baseline.reproDeckVersionCode})", "${comparison.reproDeckVersionName} (${comparison.reproDeckVersionCode})")
        addIfDifferent("Screen resolution", baseline.screenResolution, comparison.screenResolution)
        addIfDifferent("Display density", baseline.densityDpi, comparison.densityDpi)
        addIfDifferent("Font scale", baseline.fontScale, comparison.fontScale)
        addIfDifferent("Locale", baseline.localeTag, comparison.localeTag)
        addIfDifferent("Time zone", baseline.timeZoneId, comparison.timeZoneId)
        addIfDifferent("Network transport", baseline.networkTransport, comparison.networkTransport)
        addIfDifferent("Internet validated", baseline.internetValidated, comparison.internetValidated)
        addIfDifferent("Battery level", baseline.batteryPercent?.let { "$it%" } ?: "Unavailable",
            comparison.batteryPercent?.let { "$it%" } ?: "Unavailable")
        addIfDifferent("Charging", baseline.isCharging?.toString() ?: "Unavailable",
            comparison.isCharging?.toString() ?: "Unavailable")
        addIfDifferent("Power saver", baseline.isPowerSaveMode, comparison.isPowerSaveMode)
    }

    private fun MutableList<SnapshotDifference>.addIfDifferent(
        field: String,
        baseline: Any,
        comparison: Any
    ) {
        if (baseline != comparison) {
            add(SnapshotDifference(field, baseline.toString(), comparison.toString()))
        }
    }
}
