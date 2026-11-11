package com.siddiqui.reprodeck.data.device

data class DeviceSnapshotDraft(
    val manufacturer: String,
    val model: String,
    val androidRelease: String,
    val apiLevel: Int,
    val securityPatch: String,
    val reproDeckVersionName: String,
    val reproDeckVersionCode: Long,
    val screenResolution: String,
    val densityDpi: Int,
    val fontScale: Float,
    val localeTag: String,
    val timeZoneId: String,
    val networkTransport: String,
    val internetValidated: Boolean,
    val batteryPercent: Int?,
    val isCharging: Boolean?,
    val isPowerSaveMode: Boolean
)

fun interface DeviceSnapshotDataSource {
    fun capture(): DeviceSnapshotDraft
}
