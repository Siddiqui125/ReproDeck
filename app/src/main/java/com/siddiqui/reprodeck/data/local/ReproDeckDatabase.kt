package com.siddiqui.reprodeck.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DeviceSnapshotEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ReproDeckDatabase : RoomDatabase() {
    abstract fun snapshotDao(): DeviceSnapshotDao

    companion object {
        @Volatile
        private var instance: ReproDeckDatabase? = null

        fun getInstance(context: Context): ReproDeckDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReproDeckDatabase::class.java,
                    "reprodeck.db"
                )
                    .build()
                    .also { instance = it }
            }
    }
}
