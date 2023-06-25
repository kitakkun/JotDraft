package com.github.kitakkun.noteapp.ui.page.editor.composable

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    title: String,
    onTitleChange: (String) -> Unit,
    onNavigateBeforeClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    TopAppBar(
        title = {
            BasicTextField(
                value = title,
                onValueChange = onTitleChange,
                textStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBeforeClick) {
                Icon(imageVector = Icons.Default.NavigateBefore, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onSaveClick) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
            }
        }
    )
}

@Preview
@Composable
private fun EditorTopBarPreview() = PreviewContainer {
    EditorTopBar(
        title = "Title",
        onNavigateBeforeClick = {},
        onTitleChange = {},
        onSaveClick = {},
    )
}
