package com.github.kitakkun.noteapp.ui.page.finder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FinderView(
    uiState: FinderUiState,
    onDocumentItemClick: (String) -> Unit,
    onAddDocumentClick: () -> Unit,
    onChangeSearchWord: (String) -> Unit,
    onClickEdit: () -> Unit,
    onClickNewFolder: () -> Unit,
    onClickRemove: (documentId: String) -> Unit,
    onClickYesConfirmRemovalDialog: () -> Unit,
    onClickCancelConfirmRemovalDialog: () -> Unit,
) {
    if (uiState.showConfirmRemovalDialog) {
        val documentName = uiState.documents.find { it.id == uiState.selectedDocumentId }?.title
        if (documentName == null) {
            onClickCancelConfirmRemovalDialog()
        } else {
            ConfirmRemovalDialog(
                documentName = documentName,
                onClickYes = onClickYesConfirmRemovalDialog,
                onClickCancel = onClickCancelConfirmRemovalDialog,
                onDismissRequest = onClickCancelConfirmRemovalDialog,
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchField(
                        searchWord = uiState.searchWord,
                        onChangeSearchWord = onChangeSearchWord,
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                    )
                },
                actions = {
                    IconButton(onClick = onClickNewFolder) {
                        Icon(imageVector = Icons.Default.CreateNewFolder, contentDescription = null)
                    }
                    TextButton(onClick = onClickEdit) {
                        Text(text = if (!uiState.isEditMode) "Edit" else "Done")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDocumentClick) {
                Icon(imageVector = Icons.Default.NoteAdd, contentDescription = null)
            }
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            items(uiState.documents.filter { it.title.contains(uiState.searchWord) }) {
                if (it.isDeleted) return@items
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    AnimatedVisibility(visible = uiState.isEditMode) {
                        IconButton(onClick = { onClickRemove(it.id) }) {
                            Icon(
                                imageVector = Icons.Default.RemoveCircleOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                    DocumentItem(
                        uiState = it,
                        onClick = { onDocumentItemClick(it.id) },
                        onClickEnabled = !uiState.isEditMode,
                    )
                }
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
        onChangeSearchWord = {},
        onClickEdit = {},
        onClickNewFolder = {},
        onClickRemove = {},
        onClickCancelConfirmRemovalDialog = {},
        onClickYesConfirmRemovalDialog = {},
    )
}

@Preview
@Composable
private fun FinderViewEditModePreview() = PreviewContainer {
    FinderView(
        uiState = FinderUiState.buildPreviewData().copy(isEditMode = true),
        onAddDocumentClick = {},
        onDocumentItemClick = {},
        onChangeSearchWord = {},
        onClickEdit = {},
        onClickNewFolder = {},
        onClickRemove = {},
        onClickCancelConfirmRemovalDialog = {},
        onClickYesConfirmRemovalDialog = {},
    )
}
