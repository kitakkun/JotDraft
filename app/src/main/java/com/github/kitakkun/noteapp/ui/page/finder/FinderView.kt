package com.github.kitakkun.noteapp.ui.page.finder

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinderView(
    uiState: FinderUiState,
    onDocumentItemClick: (String) -> Unit,
    onAddDocumentClick: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDocumentClick) {
                Icon(imageVector = Icons.Default.NoteAdd, contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            items(uiState.documents) {
                DocumentItem(
                    uiState = it,
                    // TODO: 本当はIDにしたい
                    onClick = { onDocumentItemClick(it.title) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun FinderViewPreview() = PreviewContainer {
    FinderView(
        uiState = FinderUiState.buildPreviewData(),
        onAddDocumentClick = {},
        onDocumentItemClick = {},
    )
}
