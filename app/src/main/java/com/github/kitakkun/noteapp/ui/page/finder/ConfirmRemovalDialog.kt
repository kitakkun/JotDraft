package com.github.kitakkun.noteapp.ui.page.finder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@Composable
fun ConfirmRemovalDialog(
    documentName: String,
    onDismissRequest: () -> Unit = {},
    onClickYes: () -> Unit,
    onClickCancel: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(16.dp)
        ) {
            Text("remove this document?")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(onClick = onClickCancel) {
                    Text(text = "Cancel")
                }
                FilledTonalButton(onClick = onClickYes) {
                    Text(text = "Yes")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmRemovalDialogPreview() = PreviewContainer {
    ConfirmRemovalDialog(
        documentName = "documentId",
        onDismissRequest = {},
        onClickCancel = {},
        onClickYes = {},
    )
}
