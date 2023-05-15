package com.github.kitakkun.noteapp.ui.page.editor.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@Composable
fun SelectColorDialog(
    availableColors: List<Color>,
    selectedColor: Color,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit,
    onAddColorClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(64.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        ) {
            items(availableColors) {
                ColorCircle(
                    color = it,
                    isSelected = it == selectedColor,
                    onClick = { onColorSelected(it) }
                )
            }
            item {
                AddColorCircle(
                    onClick = onAddColorClick
                )
            }
        }
    }
    }

@Preview
@Composable
private fun SelectColorDialogPreview() = PreviewContainer {
    SelectColorDialog(
        availableColors = listOf(
            Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan,
        ),
        selectedColor = Color.Red,
        onDismiss = {},
        onColorSelected = {},
        onAddColorClick = {},
    )
}