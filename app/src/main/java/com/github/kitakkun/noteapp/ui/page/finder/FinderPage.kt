package com.github.kitakkun.noteapp.ui.page.finder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun FinderPage(
    viewModel: FinderViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDocuments()
    }

    FinderView(
        uiState = uiState,
        onAddDocumentClick = viewModel::createNewDocument,
        onDocumentItemClick = viewModel::openDocument,
        onChangeSearchWord = viewModel::updateSearchWord,
        onClickEdit = viewModel::toggleEditMode,
        onClickNewFolder = {},
        onClickRemove = viewModel::openConfirmRemovalDialog,
        onClickYesConfirmRemovalDialog = {
            viewModel.removeDocument()
            viewModel.closeConfirmRemovalDialog()
        },
        onClickCancelConfirmRemovalDialog = viewModel::closeConfirmRemovalDialog,
    )
}
