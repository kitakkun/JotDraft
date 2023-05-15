package com.github.kitakkun.noteapp.ui.page.finder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun FinderPage(
    viewModel: FinderViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    FinderView(
        uiState = uiState,
        onAddDocumentClick = viewModel::createNewDocument,
        onDocumentItemClick = { /*TODO*/ },
    )
}
