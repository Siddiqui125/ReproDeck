package com.siddiqui.reprodeck.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddiqui.reprodeck.data.local.DeviceSnapshotEntity
import com.siddiqui.reprodeck.data.repository.SnapshotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ReproDeckUiState(
    val snapshots: List<DeviceSnapshotEntity> = emptyList(),
    val isCapturing: Boolean = false,
    val message: String? = null
)

class ReproDeckViewModel(
    private val repository: SnapshotRepository
) : ViewModel() {
    private val isCapturing = MutableStateFlow(false)
    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ReproDeckUiState> = combine(
        repository.observeSnapshots(),
        isCapturing,
        message
    ) { snapshots, capturing, notice ->
        ReproDeckUiState(
            snapshots = snapshots,
            isCapturing = capturing,
            message = notice
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ReproDeckUiState()
    )

    fun captureSnapshot() {
        if (isCapturing.value) return
        viewModelScope.launch {
            isCapturing.value = true
            message.value = null
            try {
                withContext(Dispatchers.IO) {
                    repository.captureAndSave()
                }
                message.value = "Snapshot captured and saved on this device."
            } catch (error: Exception) {
                message.value = error.message ?: "Could not capture this snapshot."
            } finally {
                isCapturing.value = false
            }
        }
    }

    fun deleteSnapshot(snapshot: DeviceSnapshotEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(snapshot)
        }
    }

    fun clearMessage() {
        message.value = null
    }
}
