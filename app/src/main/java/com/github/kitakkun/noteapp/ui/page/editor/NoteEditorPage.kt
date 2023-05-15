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
        onContentChange = viewModel::updateContent,
        onBoldChange = viewModel::setOverrideBoldMode,
        onItalicChange = { /*TODO*/ },
        onBaseTextFormatClick = {/*TODO*/ },
        onSaveClick = viewModel::saveDocument,
        onNavigateUpClick = viewModel::navigateUp,
    )
}
