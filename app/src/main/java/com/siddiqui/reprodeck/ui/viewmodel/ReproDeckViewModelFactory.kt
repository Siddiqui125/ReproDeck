package com.siddiqui.reprodeck.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.siddiqui.reprodeck.data.repository.SnapshotRepository

class ReproDeckViewModelFactory(
    private val repository: SnapshotRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReproDeckViewModel::class.java)) {
            return ReproDeckViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
