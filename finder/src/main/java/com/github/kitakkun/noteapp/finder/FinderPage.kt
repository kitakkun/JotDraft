package com.github.kitakkun.noteapp.finder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.navigation.MainPage

@Composable
fun FinderPage(
    viewModel: FinderViewModel,
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDocuments()
    }

    FinderView(
        uiState = uiState,
        onClickAddDocument = {
            navController.navigate(MainPage.Editor.route)
        },
        onClickDocumentItem = { documentId ->
            navController.navigate(MainPage.Editor.routeWithArgs(documentId))
        },
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
