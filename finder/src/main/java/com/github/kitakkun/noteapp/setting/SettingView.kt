package com.github.kitakkun.noteapp.setting

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer

@Composable
fun SettingView(
    isAutoSaveEnabled: Boolean,
    onChangedAutoSaveEnabled: (Boolean) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Auto-Save")
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = isAutoSaveEnabled, onCheckedChange = onChangedAutoSaveEnabled)
            }
        }
    }
}

@Preview
@Composable
private fun SettingViewPreview() = PreviewContainer {
    SettingView(
        isAutoSaveEnabled = true,
        onChangedAutoSaveEnabled = {},
    )
}
