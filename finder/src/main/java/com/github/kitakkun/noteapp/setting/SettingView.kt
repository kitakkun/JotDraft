package com.github.kitakkun.noteapp.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer
import com.github.kitakkun.noteapp.finder.R

@Composable
fun SettingView(
    isAutoSaveEnabled: Boolean,
    onChangedAutoSaveEnabled: (Boolean) -> Unit,
    onClickOSSLicense: () -> Unit,
    onClickDeveloper: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        item {
            SettingHeader(
                label = stringResource(R.string.editor),
                icon = Icons.Default.EditNote,
            )
        }
        item {
            SettingItem(
                label = stringResource(R.string.auto_save),
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(checked = isAutoSaveEnabled, onCheckedChange = onChangedAutoSaveEnabled)
            }
        }
        item {
            SettingItem(
                label = stringResource(R.string.show_line_number),
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(checked = isAutoSaveEnabled, onCheckedChange = onChangedAutoSaveEnabled)
            }
        }
        item {
            SettingHeader(
                label = stringResource(R.string.about),
                icon = Icons.Default.Info,
            )
        }
        item {
            SettingItem(
                label = stringResource(id = R.string.oss_licenses),
                modifier = Modifier
                    .clickable {
                        onClickOSSLicense()
                    }
                    .fillMaxWidth(),
            ) {
                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
            }
        }
        item {
            SettingItem(
                label = stringResource(R.string.version),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "1.0.0")
            }
        }
        item {
            SettingItem(
                label = stringResource(R.string.developer),
                modifier = Modifier
                    .clickable {
                        onClickDeveloper()
                    }
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.developer_name))
                Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null)
            }
        }
    }
}

@Composable
private fun SettingHeader(
    label: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            modifier = modifier.padding(16.dp)
        )
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}

@Composable
private fun SettingItem(
    label: String,
    modifier: Modifier = Modifier,
    control: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(80.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        if (control != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                control()
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
        onClickDeveloper = {},
    )
}
