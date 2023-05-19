package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.data.DocumentRepository
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.TextFieldEvent
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.StyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideDocumentTextStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class NoteEditorViewModel(
    private val documentId: String?,
    private val documentRepository: DocumentRepository,
    private val navController: NavController,
) : ViewModel() {
    companion object {
        private const val TAG = "NoteEditorViewModel"
    }

    private val mutableUiState = MutableStateFlow(NoteEditorUiState())
    val uiState = mutableUiState.asStateFlow()

    fun fetchDocumentData() = viewModelScope.launch {
        if (documentId == null) return@launch
        val document = documentRepository.getDocumentById(documentId) ?: return@launch
        mutableUiState.value = uiState.value.copy(
            documentTitle = document.title,
            content = TextFieldValue(document.content),
        )
    }

    fun updateDocumentTitle(title: String) {
        mutableUiState.update { it.copy(documentTitle = title) }
    }

    fun updateContent(textFieldValue: TextFieldValue) {
        val oldTextFieldValue = uiState.value.content
        val event = TextFieldEvent.fromTextFieldValueChange(
            old = oldTextFieldValue,
            new = textFieldValue,
        )

        if (event is TextFieldEvent.TextDeleted) {
            mutableUiState.update {
                it.copy(styleAnchors = it.styleAnchors
                    .map { anchor ->
                        shiftAnchorLeft(
                            anchor = anchor,
                            insertPos = oldTextFieldValue.selection.start,
                            shiftOffset = event.deletedLength,
                        )
                    }
                    .filter { anchor -> anchor.isValid(textFieldValue.text.length) }
                )
            }
        } else if (event is TextFieldEvent.TextInserted) {
            val editorConfig = uiState.value.editorConfig
            val styleAnchorsToInsert = generateAnchorsToInsert(
                editorConfig = editorConfig,
                insertPos = oldTextFieldValue.selection.start,
            )
            mutableUiState.update {
                it.copy(styleAnchors = (it.styleAnchors + styleAnchorsToInsert)
                    .map { anchor ->
                        shiftAnchorRight(
                            anchor = anchor,
                            insertPos = oldTextFieldValue.selection.start,
                            shiftOffset = event.insertedLength,
                            editorConfig = editorConfig,
                        )
                    }
                    .filter { anchor -> anchor.isValid(textFieldValue.text.length) }
                )
            }
        }
        // update textFieldValue
        mutableUiState.update { it.copy(content = textFieldValue) }
        recalculateStyleAtCursor()
    }

    private fun generateAnchorsToInsert(
        editorConfig: EditorConfig,
        insertPos: Int,
    ): List<StyleAnchor> {
        val anchorsToInsert = mutableListOf<StyleAnchor>()
        if (editorConfig.isBold) {
            anchorsToInsert.add(
                StyleAnchor(
                    start = insertPos,
                    end = insertPos,
                    style = OverrideDocumentTextStyle.Bold(true),
                )
            )
        }
        if (editorConfig.isItalic) {
            anchorsToInsert.add(
                StyleAnchor(
                    start = insertPos,
                    end = insertPos,
                    style = OverrideDocumentTextStyle.Italic(true),
                )
            )
        }
        return anchorsToInsert
    }

    private fun shiftAnchorLeft(
        anchor: StyleAnchor,
        insertPos: Int,
        shiftOffset: Int,
    ): StyleAnchor {
        val shouldShiftStart = anchor.start >= insertPos
        val shouldShiftEnd = anchor.end >= insertPos
        val newStart = when (shouldShiftStart) {
            true -> anchor.start - shiftOffset
            false -> anchor.start
        }
        val newEnd = when (shouldShiftEnd) {
            true -> anchor.end - shiftOffset
            false -> anchor.end
        }
        return anchor.copy(start = newStart, end = newEnd)
    }

    private fun shiftAnchorRight(
        anchor: StyleAnchor,
        insertPos: Int,
        shiftOffset: Int,
        editorConfig: EditorConfig,
    ): StyleAnchor {
        val shouldShiftStart = anchor.start > insertPos
        val shouldShiftEnd = anchor.end >= insertPos &&
                when (anchor.style) {
                    is OverrideDocumentTextStyle.Bold -> editorConfig.isBold
                    is OverrideDocumentTextStyle.Italic -> editorConfig.isItalic
                    else -> true
                }
        val newStart = when (shouldShiftStart) {
            true -> anchor.start + shiftOffset
            false -> anchor.start
        }
        val newEnd = when (shouldShiftEnd) {
            true -> anchor.end + shiftOffset
            false -> anchor.end
        }
        return anchor.copy(
            start = newStart,
            end = newEnd
        )
    }

    private fun recalculateStyleAtCursor() {
        val styleAnchors = uiState.value.styleAnchors
        val cursorPos = uiState.value.content.selection.start
        val appliedAnchors = styleAnchors.filter { it.start <= cursorPos && cursorPos <= it.end }
        val baseStyle =
            appliedAnchors.find { it.style is BaseDocumentTextStyle }?.style as? BaseDocumentTextStyle
        mutableUiState.update {
            it.copy(
                editorConfig = it.editorConfig.copy(
                    baseStyle = baseStyle ?: it.editorConfig.baseStyle,
                    isBold = appliedAnchors.any { it.style is OverrideDocumentTextStyle.Bold },
                    isItalic = appliedAnchors.any { it.style is OverrideDocumentTextStyle.Italic },
                )
            )
        }
    }

    fun toggleBold() {
        mutableUiState.update {
            it.copy(editorConfig = it.editorConfig.copy(isBold = !it.editorConfig.isBold))
        }
    }

    fun toggleItalic() {
        mutableUiState.update {
            it.copy(editorConfig = it.editorConfig.copy(isItalic = !it.editorConfig.isItalic))
        }
    }

    fun saveDocument() = viewModelScope.launch {
        if (documentId == null) {
            documentRepository.saveDocument(
                id = UUID.randomUUID().toString(),
                title = uiState.value.documentTitle,
                rawContent = uiState.value.content.text,
            )
        } else {
            documentRepository.updateDocument(
                id = documentId,
                title = uiState.value.documentTitle,
                rawContent = uiState.value.content.text,
            )
        }
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun dismissSelectBaseDocumentTextStyleDialog() {
        mutableUiState.update { it.copy(showSelectBaseDocumentTextStyleDialog = false) }
    }

    fun updateBaseStyle(baseDocumentTextStyle: BaseDocumentTextStyle) {
        // do something later
    }

    fun showSelectBaseDocumentTextStyleDialog() {
        mutableUiState.update { it.copy(showSelectBaseDocumentTextStyleDialog = true) }
    }

    fun dismissSelectColorDialog() {
        mutableUiState.update { it.copy(showSelectColorDialog = false) }
    }

    fun dismissColorPickerDialog() {
        mutableUiState.update { it.copy(showColorPickerDialog = false) }
    }

    fun showColorPickerDialog() {
        mutableUiState.update { it.copy(showColorPickerDialog = true) }
    }

    fun addColor(color: Color) {
        mutableUiState.update { it.copy(availableColors = it.availableColors + color) }
    }

    fun updateCurrentColor(color: Color) {
        mutableUiState.update { it.copy(currentColor = color) }
    }

    fun showSelectColorDialog() {
        mutableUiState.update { it.copy(showSelectColorDialog = true) }
    }
}
