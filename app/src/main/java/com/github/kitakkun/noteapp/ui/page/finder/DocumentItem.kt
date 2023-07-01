package com.github.kitakkun.noteapp.ui.page.finder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@Composable
fun DocumentItem(
    uiState: DocumentItemUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClickEnabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .clickable(enabled = onClickEnabled) {
                onClick()
            }
            .fillMaxWidth()
            .height(64.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = uiState.title, style = MaterialTheme.typography.titleMedium)
        Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
    }
}

@Preview
@Composable
private fun DocumentItemPreview() = PreviewContainer {
    DocumentItem(
        uiState = DocumentItemUiState(
            title = "Title",
        ),
        onClick = {},
    )
}
