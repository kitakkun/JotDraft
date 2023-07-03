package com.github.kitakkun.noteapp.editor.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.data.model.BaseStyle
import com.github.kitakkun.noteapp.data.model.BaseStyleAnchor
import com.github.kitakkun.noteapp.data.model.OverrideStyle
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import com.github.kitakkun.noteapp.data.repository.DocumentRepository
import com.github.kitakkun.noteapp.data.room.DocumentEntity
import com.github.kitakkun.noteapp.data.store.SettingDataStore
import com.github.kitakkun.noteapp.editor.editor.editmodel.EditHistory
import com.github.kitakkun.noteapp.editor.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.editor.editor.editmodel.TextFieldChangeEvent
import com.github.kitakkun.noteapp.editor.editor.ext.deleteLinesAndShiftUp
import com.github.kitakkun.noteapp.editor.editor.ext.insertNewAnchorsAndShiftDown
import com.github.kitakkun.noteapp.editor.editor.ext.optimize
import com.github.kitakkun.noteapp.editor.editor.ext.shiftToLeft
import com.github.kitakkun.noteapp.editor.editor.ext.shiftToRight
import com.github.kitakkun.noteapp.editor.editor.ext.splitAt
import com.github.kitakkun.noteapp.editor.editor.ext.toValidOrder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class EditorViewModel(
    documentId: String?,
    private val documentRepository: DocumentRepository,
    private val settingDataStore: SettingDataStore,
    private val navController: NavController,
    private val historyManager: EditHistoryManager,
) : ViewModel() {
    companion object {
        private const val TAG = "EditorViewModel"
        private const val IDLE_TIME_TO_SAVE_HISTORY = 2000L
    }

    private val mutableUiState = MutableStateFlow(
        EditorUiState(
            baseStyleAnchors = listOf(
                BaseStyleAnchor(0, BaseStyle.Body)
            ),
            documentId = documentId ?: UUID.randomUUID().toString(),
            documentExists = documentId != null,
        )
    )
    val uiState = mutableUiState.asStateFlow()

    private val lastEditTimeFlow: MutableSharedFlow<Long> = MutableSharedFlow()
    private val saveHistoryFlow: MutableSharedFlow<EditHistory> = MutableSharedFlow()

    init {
        viewModelScope.launch {
            settingDataStore.showLineNumberFlow.collect { enabled ->
                mutableUiState.update { it.copy(showLineNumber = enabled) }
            }
        }
        viewModelScope.launch {
            lastEditTimeFlow
                .distinctUntilChanged { old, new -> new - old < IDLE_TIME_TO_SAVE_HISTORY }
                .map {
                    EditHistory(
                        content = uiState.value.content,
                        baseStyleAnchors = uiState.value.baseStyleAnchors,
                        overrideStyleAnchors = uiState.value.overrideStyleAnchors,
                    )
                }
                .distinctUntilChanged { old, new -> old.content.text == new.content.text }
                .collect { history ->
                    saveHistoryFlow.emit(history)
                }
        }

        viewModelScope.launch {
            saveHistoryFlow.collect { history ->
                historyManager.pushUndo(history)
                historyManager.clearRedo()
                mutableUiState.update {
                    it.copy(canRedo = historyManager.canRedo, canUndo = historyManager.canUndo)
                }
            }

        }
    }

    fun fetchDocumentData() = viewModelScope.launch {
        val document = documentRepository.getDocumentById(uiState.value.documentId) ?: return@launch
        val recovered = recoverDocumentDataIfNeeded(document)
        mutableUiState.value = uiState.value.copy(
            documentTitle = recovered.title,
            content = TextFieldValue(recovered.content),
            baseStyleAnchors = recovered.baseStyleAnchors,
            overrideStyleAnchors = recovered.overrideStyleAnchors,
        )
    }

    /**
     * FIXME: This is a workaround for the bug that the document data is not saved correctly.
     */
    private fun recoverDocumentDataIfNeeded(document: DocumentEntity): DocumentEntity {
        val lineCount = document.content.count { it == '\n' } + 1
        val textLength = document.content.length
        return document.copy(
            baseStyleAnchors = document.baseStyleAnchors.filter { it.isValid(lineCount) },
            overrideStyleAnchors = document.overrideStyleAnchors.filter { it.isValid() }.filter { it.end <= textLength },
        )
    }

    fun saveDocument() = viewModelScope.launch {
        mutableUiState.update { it.copy(isSavingDocument = true) }
        if (uiState.value.documentExists) {
            documentRepository.updateDocument(
                id = uiState.value.documentId,
                title = uiState.value.documentTitle,
                rawContent = uiState.value.content.text,
                baseStyleAnchors = uiState.value.baseStyleAnchors,
                overrideStyleAnchors = uiState.value.overrideStyleAnchors,
            )
        } else {
            documentRepository.saveDocument(
                id = uiState.value.documentId,
                title = uiState.value.documentTitle,
                rawContent = uiState.value.content.text,
                baseStyleAnchors = uiState.value.baseStyleAnchors,
                overrideStyleAnchors = uiState.value.overrideStyleAnchors,
            )
            mutableUiState.update { it.copy(documentExists = true) }
        }
        mutableUiState.update { it.copy(isSavingDocument = false) }
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

        viewModelScope.launch {
            lastEditTimeFlow.emit(System.currentTimeMillis())
        }
    }

    fun toggleBold() {
        val toggledIsBold = !uiState.value.editorConfig.isBold
        if (!uiState.value.content.selection.collapsed) {
            viewModelScope.launch {
                saveHistoryFlow.emit(
                    EditHistory(
                        content = uiState.value.content,
                        overrideStyleAnchors = uiState.value.overrideStyleAnchors,
                        baseStyleAnchors = uiState.value.baseStyleAnchors,
                    )
                )
            }
        }
        mutableUiState.update {
            it.copy(
                editorConfig = it.editorConfig.copy(isBold = toggledIsBold),
                overrideStyleAnchors = toggleOverrideStyleOfSelection(
                    selection = it.content.selection,
                    overrideStyle = OverrideStyle.Bold(toggledIsBold),
                    overrideAnchors = it.overrideStyleAnchors,
                ),
            )
        }
    }

    fun toggleItalic() {
        val toggledIsItalic = !uiState.value.editorConfig.isItalic
        if (!uiState.value.content.selection.collapsed) {
            viewModelScope.launch {
                saveHistoryFlow.emit(
                    EditHistory(
                        content = uiState.value.content,
                        overrideStyleAnchors = uiState.value.overrideStyleAnchors,
                        baseStyleAnchors = uiState.value.baseStyleAnchors,
                    )
                )
            }
        }
        mutableUiState.update {
            it.copy(
                editorConfig = it.editorConfig.copy(isItalic = toggledIsItalic),
                overrideStyleAnchors = toggleOverrideStyleOfSelection(
                    selection = it.content.selection,
                    overrideStyle = OverrideStyle.Italic(toggledIsItalic),
                    overrideAnchors = it.overrideStyleAnchors,
                ),
            )
        }
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

    fun addColor(color: Color) {
        mutableUiState.update { it.copy(availableColors = it.availableColors + color) }
    }

    fun updateCurrentColor(color: Color) {
        if (!uiState.value.content.selection.collapsed) {
            viewModelScope.launch {
                saveHistoryFlow.emit(
                    EditHistory(
                        content = uiState.value.content,
                        overrideStyleAnchors = uiState.value.overrideStyleAnchors,
                        baseStyleAnchors = uiState.value.baseStyleAnchors,
                    )
                )
            }
        }
        mutableUiState.update { uiState ->
            uiState.copy(
                editorConfig = uiState.editorConfig.copy(color = color),
                overrideStyleAnchors = toggleOverrideStyleOfSelection(
                    selection = uiState.content.selection,
                    overrideStyle = OverrideStyle.Color(color),
                    overrideAnchors = uiState.overrideStyleAnchors,
                ),
            )
        }
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun showSelectColorDialog() {
        mutableUiState.update { it.copy(showSelectColorDialog = true) }
    }

    fun dismissSelectColorDialog() {
        mutableUiState.update { it.copy(showSelectColorDialog = false) }
    }

    fun showColorPickerDialog() {
        mutableUiState.update { it.copy(showColorPickerDialog = true) }
    }

    fun dismissColorPickerDialog() {
        mutableUiState.update { it.copy(showColorPickerDialog = false) }
    }

    fun showSelectBaseDocumentTextStyleDialog() {
        mutableUiState.update { it.copy(showSelectBaseStyleDialog = true) }
    }

    fun dismissSelectBaseDocumentTextStyleDialog() {
        mutableUiState.update { it.copy(showSelectBaseStyleDialog = false) }
    }

    /**
     * update override style anchors
     * @param event [TextFieldValue] change event
     * @param anchors current override style anchors
     * @param editorConfig active editor configuration
     */
    private fun updateOverrideAnchors(
        event: TextFieldChangeEvent,
        anchors: List<OverrideStyleAnchor>,
        editorConfig: EditorConfig,
    ) = when (event) {
        is TextFieldChangeEvent.Insert -> {
            anchors
                .splitAt(event.position)
                .shiftToRight(
                    baseOffset = event.position,
                    shiftOffset = event.length,
                ) + generateAnchorsToInsert(
                editorConfig = editorConfig,
                insertPos = event.position,
                length = event.length,
            )
        }

        is TextFieldChangeEvent.Delete -> {
            anchors.shiftToLeft(
                baseOffset = event.position,
                shiftOffset = event.length,
            )
        }

        is TextFieldChangeEvent.Replace -> {
            anchors
                // 選択部分の削除
                .splitAt(event.oldValue.selection.start)
                .splitAt(event.oldValue.selection.end)
                .filterNot { it.start in event.oldValue.selection && it.end in event.oldValue.selection }
                .shiftToLeft(
                    baseOffset = event.position,
                    shiftOffset = event.deletedText.length,
                )
                .shiftToRight(
                    baseOffset = event.position,
                    shiftOffset = event.insertedText.length,
                )
        }

        else -> anchors
    }.filter { it.isValid() }.optimize()

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

    private fun toggleOverrideStyleOfSelection(
        selection: TextRange,
        overrideStyle: OverrideStyle,
        overrideAnchors: List<OverrideStyleAnchor>,
    ): List<OverrideStyleAnchor> {
        if (selection.collapsed) return overrideAnchors
        val orderedSelection = selection.toValidOrder()
        val newAnchors = uiState.value.overrideStyleAnchors
            .splitAt(orderedSelection.start)
            .splitAt(orderedSelection.end)
            .filterNot {
                (it.start >= orderedSelection.start)
                    && (it.end <= orderedSelection.end)
                    && (it.style::class == overrideStyle::class)
            } + listOf(
            OverrideStyleAnchor(
                start = orderedSelection.start,
                end = orderedSelection.end,
                style = overrideStyle,
            )
        )
        return newAnchors
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

    fun redo() {
        val history = historyManager.popRedo() ?: return
        historyManager.pushUndo(
            EditHistory(
                content = uiState.value.content,
                overrideStyleAnchors = uiState.value.overrideStyleAnchors,
                baseStyleAnchors = uiState.value.baseStyleAnchors,
            )
        )
        mutableUiState.update {
            it.copy(
                content = history.content,
                overrideStyleAnchors = history.overrideStyleAnchors,
                baseStyleAnchors = history.baseStyleAnchors,
                canRedo = historyManager.canRedo,
                canUndo = historyManager.canUndo,
            )
        }
    }

    fun undo() {
        val history = historyManager.popUndo() ?: return
        historyManager.pushRedo(
            EditHistory(
                content = uiState.value.content,
                overrideStyleAnchors = uiState.value.overrideStyleAnchors,
                baseStyleAnchors = uiState.value.baseStyleAnchors,
            )
        )
        mutableUiState.update {
            it.copy(
                content = history.content,
                overrideStyleAnchors = history.overrideStyleAnchors,
                baseStyleAnchors = history.baseStyleAnchors,
                canRedo = historyManager.canRedo,
                canUndo = historyManager.canUndo,
            )
        }
    }
}
