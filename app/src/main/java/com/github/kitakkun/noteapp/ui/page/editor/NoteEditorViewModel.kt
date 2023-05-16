package com.github.kitakkun.noteapp.ui.page.editor

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.data.DocumentRepository
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.TextFieldEvent
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.StyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.AbstractDocumentTextStyle
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
        val document = documentRepository.getDocumentById(documentId)
        mutableUiState.value = uiState.value.copy(
            documentTitle = document.title,
            content = TextFieldValue(document.content),
        )
    }

    fun updateDocumentTitle(title: String) {
        mutableUiState.update { it.copy(documentTitle = title) }
    }

    fun updateContent(textFieldValue: TextFieldValue) {
        Log.d(TAG, "${textFieldValue.selection.start}, ${textFieldValue.selection.end}")
        val event = TextFieldEvent.fromTextFieldValueChange(
            old = uiState.value.content,
            new = textFieldValue,
        )
        Log.d(TAG, event.toString())

        // update textFieldValue
        mutableUiState.update { it.copy(content = textFieldValue) }

        // update styleAnchors
        when (event) {
            is TextFieldEvent.CursorMoved -> {
                // recalculate insertion style
                recalculateStyleAtCursor()
            }

            is TextFieldEvent.TextDeleted -> {
                mutableUiState.update {
                    it.copy(styleAnchors = it.styleAnchors
                        .map { anchor -> anchor.shiftEnd(-event.deletedLength) }
                        .filter { anchor -> anchor.isValid }
                    )
                }
            }

            is TextFieldEvent.TextInserted -> {
                mutableUiState.update {
                    it.copy(styleAnchors = it.styleAnchors
                        .map { anchor -> anchor.shiftEnd(event.insertedLength) }
                        .filter { anchor -> anchor.isValid }
                    )
                }
            }

            is TextFieldEvent.TextUnselected -> {
                // recalculate insertion style
            }

            else -> {
                // do nothing
            }
        }
//        val isStringDeleted = textFieldValue.text.length < prevTextFieldValue.text.length
//        val cursorPos = textFieldValue.selection.start
//        val shiftedAnchors = if (isStringDeleted) {
//            uiState.value.styleAnchors.map {
//                if (it.end != null && cursorPos in it.start..it.end) {
//                    it.copy(start = it.start, end = it.end - 1)
//                } else {
//                    it
//                }
//            }
//        } else {
//            uiState.value.styleAnchors.map {
//                if (it.end != null && cursorPos in it.start..it.end) {
//                    it.copy(start = it.start, end = it.end + 1)
//                } else {
//                    it
//                }
//            }
//        }
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
        insertAnchor(OverrideDocumentTextStyle.Bold(enabled = uiState.value.editorConfig.isBold))
    }

    private fun insertAnchor(style: AbstractDocumentTextStyle) {
        val selection = uiState.value.content.selection
        val styleAnchor = StyleAnchor(
            start = selection.start,
            end = selection.end,
            style = style,
        )
        mutableUiState.update {
            it.copy(styleAnchors = it.styleAnchors + styleAnchor)
        }
    }

    private fun removeAnchor(style: AbstractDocumentTextStyle) {
        val styleAnchor = StyleAnchor(
            start = uiState.value.content.selection.start,
            end = uiState.value.content.selection.end,
            style = style,
        )
        mutableUiState.update {
            it.copy(styleAnchors = it.styleAnchors - styleAnchor)
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
