package com.github.kitakkun.noteapp.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
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
    onClickOSSLicense: () -> Unit,
) {
    val itemPadding = PaddingValues(all = 16.dp)
    LazyColumn {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(itemPadding),
            ) {
                Text(text = "Auto-Save")
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = isAutoSaveEnabled, onCheckedChange = onChangedAutoSaveEnabled)
            }
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        onClickOSSLicense()
                    }
                    .padding(itemPadding),
            ) {
                Text(text = "OSS Licenses")
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
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
        onClickOSSLicense = {},
    )
}
