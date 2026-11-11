package com.siddiqui.reprodeck

import android.app.Application
import com.siddiqui.reprodeck.data.device.AndroidDeviceSnapshotDataSource
import com.siddiqui.reprodeck.data.local.ReproDeckDatabase
import com.siddiqui.reprodeck.data.repository.DefaultSnapshotRepository

class ReproDeckApplication : Application() {
    lateinit var snapshotRepository: DefaultSnapshotRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = ReproDeckDatabase.getInstance(this)
        snapshotRepository = DefaultSnapshotRepository(
            dao = database.snapshotDao(),
            dataSource = AndroidDeviceSnapshotDataSource(this)
        )
    }
}
