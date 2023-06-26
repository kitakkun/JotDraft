package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.data.DocumentRepository
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.TextFieldChangeEvent
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.BaseStyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.OverrideStyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseStyle
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideStyle
import com.github.kitakkun.noteapp.ui.page.editor.ext.deleteLinesAndShiftUp
import com.github.kitakkun.noteapp.ui.page.editor.ext.insertNewAnchorsAndShiftDown
import com.github.kitakkun.noteapp.ui.page.editor.ext.mapWithLeftShift
import com.github.kitakkun.noteapp.ui.page.editor.ext.mapWithRightShift
import com.github.kitakkun.noteapp.ui.page.editor.ext.splitAt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class EditorViewModel(
    private val documentId: String?,
    private val documentRepository: DocumentRepository,
    private val navController: NavController,
) : ViewModel() {
    companion object {
        private const val TAG = "EditorViewModel"
    }

    private val mutableUiState = MutableStateFlow(
        EditorUiState(
            baseStyleAnchors = listOf(
                BaseStyleAnchor(0, BaseStyle.Body)
            )
        )
    )
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

    fun updateContent(newTextFieldValue: TextFieldValue) {
        // テキストの削除や追加に応じて，適切にスタイルアンカーを更新し，最後にスタイルを適用したTextFieldValueをUIStateに反映する
        val oldTextFieldValue = uiState.value.content
        val oldEditorConfig = uiState.value.editorConfig

        val event = TextFieldChangeEvent.fromTextFieldValueChange(
            old = oldTextFieldValue,
            new = newTextFieldValue,
        )

        val newOverrideAnchors = updateOverrideAnchors(
            event = event,
            anchors = uiState.value.overrideStyleAnchors,
            editorConfig = oldEditorConfig,
        )

        val newBaseStyleAnchors = updateBaseStyleAnchors(
            oldAnchors = uiState.value.baseStyleAnchors,
            insertCursorLine = oldTextFieldValue.getLineAtStartCursor(),
            lineCount = newTextFieldValue.text.lines().size,
            event = event,
            insertionBaseStyle = oldEditorConfig.baseStyle,
        )

        val cursorPos = newTextFieldValue.selection.start
        val linePos = newTextFieldValue.getLineAtStartCursor()
        val newEditorConfig = recalculateEditorConfig(
            editorConfig = oldEditorConfig,
            cursorPos = cursorPos,
            linePos = linePos,
            overrideAnchors = newOverrideAnchors,
            baseAnchors = newBaseStyleAnchors,
        )

        mutableUiState.update {
            it.copy(
                content = newTextFieldValue,
                baseStyleAnchors = newBaseStyleAnchors,
                overrideStyleAnchors = newOverrideAnchors,
                editorConfig = newEditorConfig,
            )
        }
    }

    /**
     * @param event [TextFieldValue]の変更を区別するイベント
     * @param editorConfig 現在のエディタの設定
     */
    private fun updateOverrideAnchors(
        event: TextFieldChangeEvent,
        anchors: List<OverrideStyleAnchor>,
        editorConfig: EditorConfig,
    ) = when (event) {
        is TextFieldChangeEvent.Insert -> {
            anchors
                .splitAt(event.position)
                .mapWithRightShift(
                    baseOffset = event.position,
                    shiftOffset = event.length,
                ) + generateAnchorsToInsert(
                editorConfig = editorConfig,
                insertPos = event.position,
                length = event.length,
            )
        }

        is TextFieldChangeEvent.Delete -> {
            anchors
                .mapWithLeftShift(
                    cursorPos = event.position,
                    shiftOffset = event.length,
                )
        }

        is TextFieldChangeEvent.Replace -> {
            anchors
                // 選択部分の削除
                .splitAt(event.oldValue.selection.start)
                .splitAt(event.oldValue.selection.end)
                .filterNot { it.start in event.oldValue.selection && it.end in event.oldValue.selection }
                .mapWithLeftShift(
                    cursorPos = event.position,
                    shiftOffset = event.deletedText.length,
                )
                .mapWithRightShift(
                    baseOffset = event.position,
                    shiftOffset = event.insertedText.length,
                )
        }

        else -> anchors
    }.filter { it.isValid(event.newValue.text.length) }

    private fun updateBaseStyleAnchors(
        oldAnchors: List<BaseStyleAnchor>,
        insertCursorLine: Int,
        lineCount: Int,
        event: TextFieldChangeEvent,
        insertionBaseStyle: BaseStyle,
    ) = when (event) {
        is TextFieldChangeEvent.Insert -> {
            oldAnchors.insertNewAnchorsAndShiftDown(
                baseStyle = insertionBaseStyle,
                insertCursorLine = insertCursorLine,
                insertLines = event.insertedText.count { it == '\n' }
            )
        }

        is TextFieldChangeEvent.Delete -> {
            oldAnchors.deleteLinesAndShiftUp(
                deleteCursorLine = insertCursorLine,
                deleteLines = event.deletedText.count { it == '\n' }
            )
        }

        is TextFieldChangeEvent.Replace -> {
            oldAnchors
                .deleteLinesAndShiftUp(
                    deleteCursorLine = insertCursorLine,
                    deleteLines = event.deletedText.count { it == '\n' }
                )
                .insertNewAnchorsAndShiftDown(
                    baseStyle = insertionBaseStyle,
                    insertCursorLine = insertCursorLine,
                    insertLines = event.insertedText.count { it == '\n' }
                )
        }

        else -> oldAnchors
    }.filter { it.isValid(lineCount) }.distinctBy { it.line }

    private fun generateAnchorsToInsert(
        editorConfig: EditorConfig,
        insertPos: Int,
        length: Int,
    ): List<OverrideStyleAnchor> {
        val anchorsToInsert = mutableListOf<OverrideStyleAnchor>()
        if (editorConfig.isBold) {
            anchorsToInsert.add(
                OverrideStyleAnchor(
                    start = insertPos,
                    end = insertPos + length,
                    style = OverrideStyle.Bold(true),
                )
            )
        }
        if (editorConfig.isItalic) {
            anchorsToInsert.add(
                OverrideStyleAnchor(
                    start = insertPos,
                    end = insertPos + length,
                    style = OverrideStyle.Italic(true),
                )
            )
        }
        if (editorConfig.color != Color.Unspecified) {
            anchorsToInsert.add(
                OverrideStyleAnchor(
                    start = insertPos,
                    end = insertPos + length,
                    style = OverrideStyle.Color(editorConfig.color),
                )
            )
        }
        return anchorsToInsert
    }

    private fun recalculateEditorConfig(
        editorConfig: EditorConfig,
        cursorPos: Int,
        linePos: Int,
        overrideAnchors: List<OverrideStyleAnchor>,
        baseAnchors: List<BaseStyleAnchor>,
    ): EditorConfig {
        val baseStyle = baseAnchors.find { it.line == linePos }?.style
        val activeOverrideAnchors =
            overrideAnchors.filter { it.start < cursorPos && cursorPos <= it.end }
        return editorConfig.copy(
            baseStyle = baseStyle ?: editorConfig.baseStyle,
            isBold = activeOverrideAnchors.any { it.style is OverrideStyle.Bold },
            isItalic = activeOverrideAnchors.any { it.style is OverrideStyle.Italic },
            color = activeOverrideAnchors.find { it.style is OverrideStyle.Color }
                ?.style?.spanStyle?.color
                ?: editorConfig.color
        )
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
        mutableUiState.update { it.copy(showSelectBaseStyleDialog = false) }
    }

    fun updateBaseStyle(baseStyle: BaseStyle) {
        val startCursorLine = uiState.value.content.getLineAtStartCursor()
        val endCursorLine = uiState.value.content.getLineAtEndCursor()
        val insertAnchors = (startCursorLine..endCursorLine).map {
            BaseStyleAnchor(
                line = it,
                style = baseStyle,
            )
        }
        mutableUiState.update {
            it.copy(
                editorConfig = it.editorConfig.copy(baseStyle = baseStyle),
                baseStyleAnchors = (insertAnchors + it.baseStyleAnchors).distinctBy { it.line },
            )
        }
    }

    fun showSelectBaseDocumentTextStyleDialog() {
        mutableUiState.update { it.copy(showSelectBaseStyleDialog = true) }
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
        mutableUiState.update { it.copy(editorConfig = it.editorConfig.copy(color = color)) }
    }

    fun showSelectColorDialog() {
        mutableUiState.update { it.copy(showSelectColorDialog = true) }
    }

    private fun TextFieldValue.getLineAtStartCursor(): Int {
        val cursorPosition = this.selection.start
        if (cursorPosition == 0) return 0
        val textBeforeCursor = this.text.substring(0, cursorPosition)
        return textBeforeCursor.count { it == '\n' }
    }

    private fun TextFieldValue.getLineAtEndCursor(): Int {
        val cursorPosition = this.selection.end
        if (cursorPosition == 0) return 0
        val textBeforeCursor = this.text.substring(0, cursorPosition)
        return textBeforeCursor.count { it == '\n' }
    }
}
