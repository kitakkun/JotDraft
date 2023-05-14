package com.github.kitakkun.noteapp.ui.page.editor.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@Composable
fun TextStyleControlRow(
    config: TextStyleConfig,
    onBoldChange: (Boolean) -> Unit,
    onItalicChange: (Boolean) -> Unit,
    onBaseTextFormatClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = config.baseStyle.toString(),
            modifier = Modifier
                .width(200.dp)
                .border(
                    color = MaterialTheme.colorScheme.primary,
                    width = 1.dp,
                    shape = CircleShape,
                )
                .clip(CircleShape)
                .clickable {

                }
                .padding(8.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
        )
        IconButton(onClick = onBaseTextFormatClick) {
            Icon(imageVector = Icons.Default.FormatColorText, contentDescription = null)
        }
        IconToggleButton(checked = config.bold, onCheckedChange = onBoldChange) {
            Icon(imageVector = Icons.Default.FormatBold, contentDescription = null)
        }
        IconToggleButton(checked = config.bold, onCheckedChange = onItalicChange) {
            Icon(imageVector = Icons.Default.FormatItalic, contentDescription = null)
        }
    }
}

@Preview
@Composable
private fun TextStyleControlRowPreview() = PreviewContainer {
    TextStyleControlRow(
        config = TextStyleConfig(),
        onBoldChange = {},
        onBaseTextFormatClick = {},
        onItalicChange = {},
    )
}