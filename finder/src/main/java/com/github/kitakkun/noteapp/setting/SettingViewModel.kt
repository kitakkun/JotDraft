package com.github.kitakkun.noteapp.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.noteapp.data.store.SettingDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingDataStore: SettingDataStore,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(SettingUiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingDataStore.showLineNumberFlow.collect { isEnabled ->
                mutableUiState.update { it.copy(isShowLineNumberEnabled = isEnabled) }
            }
        }
        viewModelScope.launch {
            settingDataStore.autoSaveEnabledFlow.collect { isEnabled ->
                mutableUiState.update { it.copy(isAutoSaveEnabled = isEnabled) }
            }
        }
    }

    fun setAutoSaveEnabled(isEnabled: Boolean) = viewModelScope.launch {
        settingDataStore.setAutoSaveEnabled(isEnabled)
    }

    fun setShowLineNumber(isEnabled: Boolean) = viewModelScope.launch {
        settingDataStore.setShowLineNumber(isEnabled)
    }
}
