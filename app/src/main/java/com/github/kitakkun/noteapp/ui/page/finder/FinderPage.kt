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
        onClickAddDocument = viewModel::createNewDocument,
        onClickDocumentItem = viewModel::openDocument,
        onChangeSearchWord = viewModel::updateSearchWord,
        onClickEdit = viewModel::toggleEditMode,
        onClickRemove = viewModel::openConfirmRemovalDialog,
        onClickYesConfirmRemovalDialog = {
            viewModel.removeDocument()
            viewModel.closeConfirmRemovalDialog()
        },
        onClickCancelConfirmRemovalDialog = viewModel::closeConfirmRemovalDialog,
    )
}
