package com.github.kitakkun.noteapp.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.navigation.SubPage

@Composable
fun SettingPage(
    viewModel: SettingViewModel,
    navController: NavController,
) {
    val isAutoSaveEnabled = viewModel.isAutoSaveEnabled.collectAsState()
    SettingView(
        isAutoSaveEnabled = isAutoSaveEnabled.value,
        onChangedAutoSaveEnabled = viewModel::setAutoSaveEnabled,
        onClickOSSLicense = { navController.navigate(SubPage.License.route) }
    )
}
