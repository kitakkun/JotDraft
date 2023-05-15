package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.data.DocumentRepository
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
    private val mutableUiState = MutableStateFlow(NoteEditorUiState())
    val uiState = mutableUiState.asStateFlow()

    fun updateContent(content: TextFieldValue) {
        mutableUiState.value = uiState.value.copy(
            content = content
        )
    }

    fun saveDocument() = viewModelScope.launch {
        documentRepository.saveDocument(
            id = UUID.randomUUID().toString(),
            title = uiState.value.documentTitle,
            rawContent = uiState.value.content.text,
        )
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun setBaseTextFormatMode(baseTextFormat: BaseDocumentTextStyle) {
        val styleAnchor = StyleAnchor(
            start = 0,
            style = baseTextFormat,
        )
        mutableUiState.update {
            it.copy(baseStyleAnchors = it.baseStyleAnchors + styleAnchor)
        }
    }

    fun setOverrideItalicMode(isItalic: Boolean, cursorIndex: Int = 0) {
        val styleAnchor = StyleAnchor(
            start = cursorIndex,
            style = OverrideDocumentTextStyle.Italic(isItalic),
        )
        mutableUiState.update {
            it.copy(overrideStyles = it.overrideStyles + styleAnchor)
        }
    }

    fun setOverrideBoldMode(isBold: Boolean) {
        val styleAnchor = StyleAnchor(
            start = uiState.value.content.selection.start,
            style = OverrideDocumentTextStyle.Bold(isBold),
        )
        mutableUiState.update {
            it.copy(overrideStyles = it.overrideStyles + styleAnchor)
        }
    }

    fun clearPendingStyles() {
        mutableUiState.update {
            it.copy(overrideStyles = it.overrideStyles.filter { anchor ->
                anchor.end != null && anchor.start != it.content.text.length
            })
        }
    }

    fun dismissSelectBaseDocumentTextStyleDialog() {
        mutableUiState.update {
            it.copy(
                showSelectBaseDocumentTextStyleDialog = false
            )
        }
    }

    fun updateBaseStyle(baseDocumentTextStyle: BaseDocumentTextStyle) {
        // do something later
    }

    fun showSelectBaseDocumentTextStyleDialog() {
        mutableUiState.update {
            it.copy(
                showSelectBaseDocumentTextStyleDialog = true
            )
        }
    }

    fun dismissSelectColorDialog() {
        mutableUiState.update {
            it.copy(
                showSelectColorDialog = false
            )
        }
    }

    fun dismissColorPickerDialog() {
        mutableUiState.update {
            it.copy(
                showColorPickerDialog = false
            )
        }
    }

    fun showColorPickerDialog() {
        mutableUiState.update {
            it.copy(
                showColorPickerDialog = true
            )
        }
    }

    fun addColor(color: Color) {
        mutableUiState.update {
            it.copy(
                availableColors = it.availableColors + color,
            )
        }
    }

    fun updateCurrentColor(color: Color) {
        mutableUiState.update {
            it.copy(
                currentColor = color,
            )
        }
    }

    fun showSelectColorDialog() {
        mutableUiState.update {
            it.copy(
                showSelectColorDialog = true
            )
        }
    }
}
