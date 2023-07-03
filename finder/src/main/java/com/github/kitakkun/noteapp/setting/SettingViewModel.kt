package com.github.kitakkun.noteapp.setting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingViewModel : ViewModel() {
    private val mutableUiState = MutableStateFlow(SettingUiState())
    val uiState = mutableUiState.asStateFlow()

    fun setAutoSaveEnabled(isEnabled: Boolean) {
        mutableUiState.update { it.copy(isAutoSaveEnabled = isEnabled) }
    }

    fun setShowLineNumber(isEnabled: Boolean) {
        mutableUiState.update { it.copy(isShowLineNumberEnabled = isEnabled) }
    }
}
