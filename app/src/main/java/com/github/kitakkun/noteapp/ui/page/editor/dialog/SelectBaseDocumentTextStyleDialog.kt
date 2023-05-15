package com.github.kitakkun.noteapp.ui.page.editor.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@Composable
fun SelectBaseDocumentTextStyleDialog(
    selectedStyle: BaseDocumentTextStyle,
    onSelectStyle: (BaseDocumentTextStyle) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        LazyColumn(
            modifier = Modifier.background(
                shape = RoundedCornerShape(size = 8.dp),
                color = MaterialTheme.colorScheme.surface,
            )
        ) {
            items(BaseDocumentTextStyle.values()) { style ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedStyle == style,
                        onClick = { onSelectStyle(style) },
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)
                    )
                    Text(text = style.toString(), modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun SelectBaseDocumentTextStyleDialogPreview() = PreviewContainer {
    SelectBaseDocumentTextStyleDialog(
        selectedStyle = BaseDocumentTextStyle.Body,
        onSelectStyle = {},
        onDismissRequest = {},
    )
}
