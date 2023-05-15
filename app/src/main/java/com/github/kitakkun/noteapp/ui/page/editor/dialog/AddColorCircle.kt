package com.github.kitakkun.noteapp.ui.page.editor.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@Composable
fun AddColorCircle(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable {
                onClick()
            }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center),
        )
    }
}

@Preview
@Composable
private fun AddColorCirclePreview() = PreviewContainer {
    AddColorCircle(
        onClick = {},
    )
}
