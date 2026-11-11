package com.siddiqui.reprodeck.data.repository

import com.siddiqui.reprodeck.data.device.DeviceSnapshotDataSource
import com.siddiqui.reprodeck.data.local.DeviceSnapshotDao
import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import kotlinx.coroutines.flow.Flow

interface SnapshotRepository {
    fun observeSnapshots(): Flow<List<DeviceSnapshotEntity>>
    suspend fun getSnapshot(id: Long): DeviceSnapshotEntity?
    suspend fun captureAndSave(): Long
    suspend fun delete(snapshot: DeviceSnapshotEntity)
}

class DefaultSnapshotRepository(
    private val dao: DeviceSnapshotDao,
    private val dataSource: DeviceSnapshotDataSource,
    private val now: () -> Long = System::currentTimeMillis
) : SnapshotRepository {

    override fun observeSnapshots(): Flow<List<DeviceSnapshotEntity>> = dao.observeAll()

    override suspend fun getSnapshot(id: Long): DeviceSnapshotEntity? = dao.findById(id)

    override suspend fun captureAndSave(): Long {
        val draft = dataSource.capture()
        val entity = DeviceSnapshotEntity(
            capturedAtEpochMillis = now(),
            manufacturer = draft.manufacturer,
            model = draft.model,
            androidRelease = draft.androidRelease,
            apiLevel = draft.apiLevel,
            securityPatch = draft.securityPatch,
            reproDeckVersionName = draft.reproDeckVersionName,
            reproDeckVersionCode = draft.reproDeckVersionCode,
            screenResolution = draft.screenResolution,
            densityDpi = draft.densityDpi,
            fontScale = draft.fontScale,
            localeTag = draft.localeTag,
            timeZoneId = draft.timeZoneId,
            networkTransport = draft.networkTransport,
            internetValidated = draft.internetValidated,
            batteryPercent = draft.batteryPercent,
            isCharging = draft.isCharging,
            isPowerSaveMode = draft.isPowerSaveMode
        )
        return dao.insert(entity)
    }

    override suspend fun delete(snapshot: DeviceSnapshotEntity) = dao.delete(snapshot)
}
