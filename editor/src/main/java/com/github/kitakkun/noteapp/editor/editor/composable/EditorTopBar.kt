package com.github.kitakkun.noteapp.editor.editor.composable

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    title: String,
    undoEnabled: Boolean,
    redoEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onNavigateBeforeClick: () -> Unit,
    onSaveClick: () -> Unit,
    onClickRedo: () -> Unit,
    onClickUndo: () -> Unit,
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
            IconButton(enabled = undoEnabled, onClick = onClickUndo) {
                Icon(imageVector = Icons.Default.Undo, contentDescription = null)
            }
            IconButton(enabled = redoEnabled, onClick = onClickRedo) {
                Icon(imageVector = Icons.Default.Redo, contentDescription = null)
            }
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
        redoEnabled = false,
        undoEnabled = true,
        onSaveClick = {},
        onClickRedo = {},
        onClickUndo = {},
    )
}
