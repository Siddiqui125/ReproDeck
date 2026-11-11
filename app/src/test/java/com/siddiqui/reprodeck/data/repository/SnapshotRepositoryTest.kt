package com.siddiqui.reprodeck.data.repository

import com.siddiqui.reprodeck.data.device.DeviceSnapshotDataSource
import com.siddiqui.reprodeck.data.device.DeviceSnapshotDraft
import com.siddiqui.reprodeck.data.local.DeviceSnapshotDao
import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SnapshotRepositoryTest {
    @Test
    fun captureAndSaveMapsTheDeviceDraftAndUsesTheCaptureTimestamp() = runBlocking {
        val dao = FakeSnapshotDao()
        val repository = DefaultSnapshotRepository(
            dao = dao,
            dataSource = DeviceSnapshotDataSource { sampleDraft() },
            now = { 123_456L }
        )

        val newId = repository.captureAndSave()
        val stored = repository.getSnapshot(newId)

        assertEquals(1L, newId)
        assertNotNull(stored)
        assertEquals(123_456L, stored?.capturedAtEpochMillis)
        assertEquals("Google", stored?.manufacturer)
        assertEquals("Pixel 9", stored?.model)
        assertEquals("Wi-Fi", stored?.networkTransport)
        assertEquals(82, stored?.batteryPercent)
    }

    private fun sampleDraft() = DeviceSnapshotDraft(
        manufacturer = "Google",
        model = "Pixel 9",
        androidRelease = "16",
        apiLevel = 36,
        securityPatch = "2026-10-05",
        reproDeckVersionName = "0.1.0",
        reproDeckVersionCode = 1,
        screenResolution = "1080 × 2400",
        densityDpi = 420,
        fontScale = 1f,
        localeTag = "en-US",
        timeZoneId = "America/New_York",
        networkTransport = "Wi-Fi",
        internetValidated = true,
        batteryPercent = 82,
        isCharging = false,
        isPowerSaveMode = false
    )

    private class FakeSnapshotDao : DeviceSnapshotDao {
        private val rows = MutableStateFlow<List<DeviceSnapshotEntity>>(emptyList())

        override fun observeAll(): Flow<List<DeviceSnapshotEntity>> = rows

        override suspend fun findById(id: Long): DeviceSnapshotEntity? =
            rows.value.firstOrNull { it.id == id }

        override suspend fun insert(snapshot: DeviceSnapshotEntity): Long {
            val nextId = (rows.value.maxOfOrNull { it.id } ?: 0L) + 1L
            rows.value = listOf(snapshot.copy(id = nextId)) + rows.value
            return nextId
        }

        override suspend fun delete(snapshot: DeviceSnapshotEntity) {
            rows.value = rows.value.filterNot { it.id == snapshot.id }
        }
    }
}
