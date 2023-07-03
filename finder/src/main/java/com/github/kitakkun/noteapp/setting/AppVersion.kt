package com.github.kitakkun.noteapp.setting

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getAppVersion(): String {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName
}
