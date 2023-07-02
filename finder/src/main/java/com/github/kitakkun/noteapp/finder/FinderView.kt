package com.github.kitakkun.noteapp.finder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer
import com.github.kitakkun.noteapp.finder.composable.DocumentItem
import com.github.kitakkun.noteapp.finder.composable.DocumentItemUiState
import com.github.kitakkun.noteapp.finder.composable.SearchField
import com.github.kitakkun.noteapp.finder.dialog.ConfirmRemovalDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinderView(
    uiState: FinderUiState,
    onClickDocumentItem: (String) -> Unit,
    onClickAddDocument: () -> Unit,
    onChangeSearchWord: (String) -> Unit,
    onClickEdit: () -> Unit,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                    )
                },
                actions = {
                    TextButton(onClick = onClickEdit) {
                        Text(text = if (!uiState.isEditMode) stringResource(R.string.edit) else stringResource(R.string.done))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickAddDocument) {
                Icon(imageVector = Icons.Default.NoteAdd, contentDescription = null)
            }
        },
    ) { innerPadding ->
        if (uiState.documents.filter { !it.isDeleted }.isEmpty()) {
            EmptyDocumentList(modifier = Modifier.fillMaxSize())
        } else {
            DocumentList(
                documents = uiState.documents,
                searchWord = uiState.searchWord,
                isEditMode = uiState.isEditMode,
                onClickRemoveDocument = onClickRemove,
                onClickDocument = onClickDocumentItem,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun EmptyDocumentList(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.no_documents))
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.LibraryBooks,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentList(
    documents: List<DocumentItemUiState>,
    searchWord: String,
    isEditMode: Boolean,
    onClickRemoveDocument: (documentId: String) -> Unit,
    onClickDocument: (documentId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(documents.filter { it.title.contains(searchWord) }) {
            if (it.isDeleted) return@items
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.animateItemPlacement(),
            ) {
                AnimatedVisibility(visible = isEditMode) {
                    IconButton(onClick = { onClickRemoveDocument(it.id) }) {
                        Icon(
                            imageVector = Icons.Default.RemoveCircleOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
                DocumentItem(
                    uiState = it,
                    onClick = { onClickDocument(it.id) },
                    onClickEnabled = !isEditMode,
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
        onClickAddDocument = {},
        onClickDocumentItem = {},
        onChangeSearchWord = {},
        onClickEdit = {},
        onClickRemove = {},
        onClickCancelConfirmRemovalDialog = {},
        onClickYesConfirmRemovalDialog = {},
    )
}

@Preview
@Composable
private fun FinderViewEmptyPreview() = PreviewContainer {
    FinderView(
        uiState = FinderUiState.buildPreviewData().copy(documents = emptyList()),
        onClickAddDocument = {},
        onClickDocumentItem = {},
        onChangeSearchWord = {},
        onClickEdit = {},
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
        onClickAddDocument = {},
        onClickDocumentItem = {},
        onChangeSearchWord = {},
        onClickEdit = {},
        onClickRemove = {},
        onClickCancelConfirmRemovalDialog = {},
        onClickYesConfirmRemovalDialog = {},
    )
}
