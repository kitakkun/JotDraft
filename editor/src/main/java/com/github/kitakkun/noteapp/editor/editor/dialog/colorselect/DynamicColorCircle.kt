package com.github.kitakkun.noteapp.editor.editor.dialog.colorselect

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer

@Composable
fun DynamicColorCircle(
    lightThemeColor: Color,
    darkThemeColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .border(
                color = when (isSelected) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.onBackground
                },
                width = when (isSelected) {
                    true -> 3.dp
                    false -> 1.dp
                },
                shape = CircleShape,
            )
    ) {
        fillWithAlphaTile()
        rotate(30f) {
            drawRect(
                color = if (isDarkTheme) darkThemeColor else lightThemeColor,
                topLeft = Offset(x = 0f, y = 0f),
                size = size.copy(width = size.width * 0.65f),
                style = Fill,
            )
            drawRect(
                color = if (isDarkTheme) lightThemeColor else darkThemeColor,
                topLeft = Offset(x = size.width * 0.65f, y = 0f),
                size = size,
                style = Fill,
            )
        }
    }
}

private fun DrawScope.fillWithAlphaTile(
    tileCount: Int = 6,
) {
    val tileSize = Size(
        width = size.width / tileCount,
        height = size.height / tileCount,
    )
    for (x in 0 until tileCount) {
        for (y in 0 until tileCount) {
            val tileColor = when ((x + y) % 2) {
                0 -> Color.White
                else -> Color.LightGray
            }
            val tileOffset = Offset(
                x = x * tileSize.width,
                y = y * tileSize.height,
            )
            drawRect(
                color = tileColor,
                topLeft = tileOffset,
                size = tileSize,
                style = Fill,
            )
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DynamicColorCirclePreviewTransparent() = PreviewContainer {
    DynamicColorCircle(
        lightThemeColor = Color.Transparent,
        darkThemeColor = Color.White,
        isSelected = true,
        onClick = {},
    )
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DynamicColorCirclePreview() = PreviewContainer {
    DynamicColorCircle(
        lightThemeColor = Color.Black,
        darkThemeColor = Color.White,
        isSelected = true,
        onClick = {},
    )
}
