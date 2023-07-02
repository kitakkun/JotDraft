package com.github.kitakkun.noteapp.setting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingViewModel : ViewModel() {
    var mutableIsAutoSaveEnabled = MutableStateFlow(false)
    val isAutoSaveEnabled = mutableIsAutoSaveEnabled.asStateFlow()

    fun setAutoSaveEnabled(isEnabled: Boolean) {
        mutableIsAutoSaveEnabled.value = isEnabled
    }
}
