package com.github.kitakkun.noteapp.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun SettingPage(
    viewModel: SettingViewModel,
) {
    val isAutoSaveEnabled = viewModel.isAutoSaveEnabled.collectAsState()
    SettingView(
        isAutoSaveEnabled = isAutoSaveEnabled.value,
        onChangedAutoSaveEnabled = viewModel::setAutoSaveEnabled,
    )
}
