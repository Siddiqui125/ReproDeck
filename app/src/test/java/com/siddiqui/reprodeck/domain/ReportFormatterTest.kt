package com.siddiqui.reprodeck.domain

import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import org.junit.Assert.assertTrue
import org.junit.Test

class ReportFormatterTest {
    @Test
    fun markdownReportContainsKeyDiagnosticSections() {
        val report = ReportFormatter.toMarkdown(sampleSnapshot())

        assertTrue(report.contains("# ReproDeck diagnostic snapshot"))
        assertTrue(report.contains("## Device"))
        assertTrue(report.contains("## Environment"))
        assertTrue(report.contains("Android: 15 (API 35)"))
    }

    @Test
    fun jsonReportHasSchemaVersionAndEnvironment() {
        val report = ReportFormatter.toJson(sampleSnapshot())

        assertTrue(report.contains("\"schemaVersion\": 1"))
        assertTrue(report.contains("\"networkTransport\": \"Wi-Fi\""))
    }


    @Test
    fun jsonReportEscapesQuotesAndNewlines() {
        val report = ReportFormatter.toJson(sampleSnapshot().copy(model = "Pixel \"Pro\"\n"))
        assertTrue(report.contains("Pixel \\\"Pro\\\"\\n"))
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
        networkTransport = "Wi-Fi",
        internetValidated = true,
        batteryPercent = 90,
        isCharging = false,
        isPowerSaveMode = false
    )
}
