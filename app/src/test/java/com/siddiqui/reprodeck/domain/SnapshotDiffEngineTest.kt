package com.siddiqui.reprodeck.domain

import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SnapshotDiffEngineTest {
    @Test
    fun identicalSnapshotsHaveNoDifferences() {
        val snapshot = sampleSnapshot()
        assertTrue(SnapshotDiffEngine.compare(snapshot, snapshot.copy(id = 2)).isEmpty())
    }

    @Test
    fun onlyChangedFieldsAreReported() {
        val baseline = sampleSnapshot()
        val comparison = baseline.copy(
            id = 2,
            androidRelease = "16",
            apiLevel = 36,
            networkTransport = "Wi-Fi",
            batteryPercent = 64
        )

        val differences = SnapshotDiffEngine.compare(baseline, comparison)

        assertEquals(
            listOf("Android release", "API level", "Network transport", "Battery level"),
            differences.map { it.field }
        )
    }

    private fun sampleSnapshot() = DeviceSnapshotEntity(
        id = 1,
        capturedAtEpochMillis = 1_700_000_000_000,
        manufacturer = "Google",
        model = "Pixel",
        androidRelease = "15",
        apiLevel = 35,
        securityPatch = "2025-01-05",
        reproDeckVersionName = "1.0",
        reproDeckVersionCode = 1,
        screenResolution = "1080 × 2400",
        densityDpi = 420,
        fontScale = 1f,
        localeTag = "en-US",
        timeZoneId = "America/New_York",
        networkTransport = "Mobile data",
        internetValidated = true,
        batteryPercent = 90,
        isCharging = false,
        isPowerSaveMode = false
    )
}
