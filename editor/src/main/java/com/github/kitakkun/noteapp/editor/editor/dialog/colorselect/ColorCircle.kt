package com.github.kitakkun.noteapp.editor.editor.dialog.colorselect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer

@Composable
fun StaticColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(color = color)
            .border(
                width = when (isSelected) {
                    true -> 3.dp
                    else -> 1.dp
                },
                color = when (isSelected) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.onBackground
                },
                shape = CircleShape
            )
    )
}

@Preview
@Composable
private fun StaticColorCirclePreview() = PreviewContainer {
    StaticColorCircle(
        color = Color.Red,
        isSelected = true,
        onClick = {},
    )
}
