package com.github.kitakkun.noteapp.ui.page.editor.dialog.colorselect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer

@Composable
fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .padding(4.dp)
            .background(
                color = color,
                shape = CircleShape
            )
            .border(
                width = 2.dp,
                color = if (isSelected) Color.Black else Color.White,
                shape = CircleShape
            )
            .clip(
                CircleShape
            )
            .clickable {
                onClick()
            }
    )
}

@Preview
@Composable
private fun ColorCirclePreview() = PreviewContainer {
    ColorCircle(
        color = Color.Red,
        isSelected = true,
        onClick = {},
    )
}
