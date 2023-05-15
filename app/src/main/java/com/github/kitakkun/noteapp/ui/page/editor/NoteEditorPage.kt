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
        onBaseTextFormatClick = viewModel::showSelectBaseDocumentTextStyleDialog,
        onSaveClick = viewModel::saveDocument,
        onNavigateUpClick = viewModel::navigateUp,
        onBaseStyleChange = {
            viewModel.updateBaseStyle(it)
            viewModel.dismissSelectBaseDocumentTextStyleDialog()
        },
        onDismissSelectBaseDocumentTextStyleDialog = viewModel::dismissSelectBaseDocumentTextStyleDialog,
        onColorSelected = {
            viewModel.updateCurrentColor(it)
            viewModel.dismissSelectColorDialog()
        },
        onAddColorFinished = {
            viewModel.addColor(it)
            viewModel.dismissColorPickerDialog()
        },
        onColorPickerOpenRequest = viewModel::showColorPickerDialog,
        onDismissColorPickerDialog = viewModel::dismissColorPickerDialog,
        onDismissSelectColorDialog = viewModel::dismissSelectColorDialog,
        onTextColorIconClick = viewModel::showSelectColorDialog,
    )
}
