package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun NoteEditorPage(
    viewModel: NoteEditorViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    NoteEditorView(
        uiState = uiState,
        onContentChange = viewModel::updateRawContent,
        onBoldChange = {},
        onItalicChange = {},
        onBaseTextFormatClick = {},
        onSaveClick = viewModel::saveDocument,
        onNavigateUpClick = viewModel::navigateUp,
    )
}