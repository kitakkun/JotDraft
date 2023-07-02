package com.github.kitakkun.noteapp.editor.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun EditorPage(
    viewModel: EditorViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDocumentData()
    }

    EditorView(
        uiState = uiState,
        onContentChange = viewModel::updateContent,
        onBoldChange = { viewModel.toggleBold() },
        onItalicChange = { viewModel.toggleItalic() },
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
        onTitleChange = viewModel::updateDocumentTitle,
        onClickRedo = viewModel::redo,
        onClickUndo = viewModel::undo,
    )
}
