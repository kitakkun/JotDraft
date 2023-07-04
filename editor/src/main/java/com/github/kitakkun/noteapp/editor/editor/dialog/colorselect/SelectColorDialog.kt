package com.github.kitakkun.noteapp.editor.editor.dialog.colorselect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
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
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer
import com.github.kitakkun.noteapp.data.model.StyleColor
import com.github.kitakkun.noteapp.data.model.static

@Composable
fun SelectColorDialog(
    availableColors: List<StyleColor>,
    selectedColor: StyleColor,
    onDismiss: () -> Unit,
    onColorSelected: (StyleColor) -> Unit,
    onAddColorClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(64.dp),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(availableColors) { color ->
                when (color) {
                    is StyleColor.Dynamic -> DynamicColorCircle(
                        lightThemeColor = color.lightValue,
                        darkThemeColor = color.darkValue,
                        isSelected = color == selectedColor,
                        onClick = { onColorSelected(color) },
                    )

                    is StyleColor.Static -> ColorCircle(
                        color = color.value,
                        isSelected = color == selectedColor,
                        onClick = { onColorSelected(color) },
                    )
                }
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
            Color.Red.static,
            Color.Green.static,
            Color.Blue.static,
            Color.Yellow.static,
            Color.Magenta.static,
            Color.Cyan.static,
        ),
        selectedColor = Color.Red.static,
        onDismiss = {},
        onColorSelected = {},
        onAddColorClick = {},
    )
}
