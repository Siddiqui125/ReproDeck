package com.siddiqui.reprodeck.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceSnapshotDao {
    @Query("SELECT * FROM device_snapshots ORDER BY capturedAtEpochMillis DESC")
    fun observeAll(): Flow<List<DeviceSnapshotEntity>>

    @Query("SELECT * FROM device_snapshots WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): DeviceSnapshotEntity?

    @Insert
    suspend fun insert(snapshot: DeviceSnapshotEntity): Long

    @Delete
    suspend fun delete(snapshot: DeviceSnapshotEntity)
}
