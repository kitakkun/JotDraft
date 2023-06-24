package com.github.kitakkun.noteapp.ui.page.editor

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.data.DocumentRepository
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.TextFieldChangeEvent
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.StyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.AbstractDocumentTextStyle
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideDocumentTextStyle
import com.github.kitakkun.noteapp.ui.page.editor.ext.applyStyles
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

    private val mutableUiState = MutableStateFlow(EditorUiState())
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
        val event = TextFieldChangeEvent.fromTextFieldValueChange(
            old = oldTextFieldValue,
            new = newTextFieldValue,
        )

        val insertionStyles = listOf(
            OverrideDocumentTextStyle.Bold(uiState.value.editorConfig.isBold),
            OverrideDocumentTextStyle.Italic(uiState.value.editorConfig.isItalic),
        )

        val newAnchors = when (event) {
            is TextFieldChangeEvent.Insert -> {
                // insert new anchors which should be inserted
                val insertedAnchors = generateAnchorsToInsert(
                    editorConfig = uiState.value.editorConfig,
                    insertPos = event.position,
                    length = event.length,
                )
                val shiftedAnchors = uiState.value.styleAnchors
                    .flatMap {
                        // 違うスタイルのものを現在のカーソル位置に挿入する場合，
                        // 挿入点を堺に適切にスタイルアンカーを分割する必要がある
                        val splitted = splitAnchorsIfNeeded(
                            anchor = it,
                            baseline = event.position,
                            currentInsertionStyles = insertionStyles,
                        )
                        if (splitted.size == 2) {
                            Log.d(TAG, "split anchor: $it -> $splitted")
                        }
                        splitted
                    }
                    .map {
                        shiftAnchorRight(
                            anchor = it,
                            baseline = event.position,
                            shiftOffset = event.length,
                            currentInsertionStyles = insertionStyles,
                        )
                    }
                    .filter { it.isValid(newTextFieldValue.text.length) }
                shiftedAnchors + insertedAnchors
            }

            is TextFieldChangeEvent.Delete -> {
                uiState.value.styleAnchors
                    .map {
                        shiftAnchorLeft(
                            anchor = it,
                            baseline = event.position,
                            shiftOffset = event.length,
                        )
                    }
                    .filter { it.isValid(newTextFieldValue.text.length) }
            }

            is TextFieldChangeEvent.Replace -> {
                uiState.value.styleAnchors
                    .map {
                        // リプレイス前のテキスト削除によるアンカーの左シフト
                        shiftAnchorLeft(
                            anchor = it,
                            baseline = event.position,
                            shiftOffset = event.deletedText.length,
                        )
                    }
                    .filter { it.isValid(newTextFieldValue.text.length) }
                    .map {
                        shiftAnchorRight(
                            anchor = it,
                            baseline = event.position,
                            shiftOffset = event.insertedText.length,
                            currentInsertionStyles = insertionStyles,
                        )
                    }
                    .filter { it.isValid(newTextFieldValue.text.length) }
            }

            is TextFieldChangeEvent.NoChange -> return

            else -> uiState.value.styleAnchors
        }

        // カーソル位置での適用スタイルの計算
        val cursorPos = newTextFieldValue.selection.start
        val appliedAnchors = newAnchors.filter { it.start < cursorPos && cursorPos <= it.end }
        val baseStyle =
            appliedAnchors.find { it.style is BaseDocumentTextStyle }?.style as? BaseDocumentTextStyle
        val newEditorConfig = uiState.value.editorConfig.copy(
            baseStyle = baseStyle ?: uiState.value.editorConfig.baseStyle,
            isBold = appliedAnchors.any { it.style is OverrideDocumentTextStyle.Bold },
            isItalic = appliedAnchors.any { it.style is OverrideDocumentTextStyle.Italic },
        )

        mutableUiState.update {
            it.copy(
                content = newTextFieldValue.copy(
                    annotatedString = newTextFieldValue.annotatedString.applyStyles(newAnchors)
                ),
                styleAnchors = newAnchors,
                editorConfig = newEditorConfig,
            )
        }
    }

    private fun generateAnchorsToInsert(
        editorConfig: EditorConfig,
        insertPos: Int,
        length: Int,
    ): List<StyleAnchor> {
        val anchorsToInsert = mutableListOf<StyleAnchor>()
        if (editorConfig.isBold) {
            anchorsToInsert.add(
                StyleAnchor(
                    start = insertPos,
                    end = insertPos + length,
                    style = OverrideDocumentTextStyle.Bold(true),
                )
            )
        }
        if (editorConfig.isItalic) {
            anchorsToInsert.add(
                StyleAnchor(
                    start = insertPos,
                    end = insertPos + length,
                    style = OverrideDocumentTextStyle.Italic(true),
                )
            )
        }
        return anchorsToInsert
    }

    private fun splitAnchorsIfNeeded(
        anchor: StyleAnchor,
        baseline: Int,
        currentInsertionStyles: List<AbstractDocumentTextStyle>,
    ): List<StyleAnchor> {
        val shouldSplit = anchor.style !in currentInsertionStyles &&
                baseline > anchor.start && baseline < anchor.end
        if (!shouldSplit) return listOf(anchor)

        return listOf(
            anchor.copy(start = anchor.start, end = baseline),
            anchor.copy(start = baseline, end = anchor.end),
        )
    }

    private fun shiftAnchorLeft(
        anchor: StyleAnchor,
        baseline: Int,
        shiftOffset: Int,
    ): StyleAnchor {
        val shouldShiftStart = anchor.start >= baseline
        val shouldShiftEnd = anchor.end >= baseline
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

    // 既存のアンカーを右へシフトする
    private fun shiftAnchorRight(
        anchor: StyleAnchor,
        baseline: Int,
        shiftOffset: Int,
        currentInsertionStyles: List<AbstractDocumentTextStyle>,
    ): StyleAnchor {
        // カーソル位置より左側のstartはシフトしない
        val shouldShiftStart = anchor.start >= baseline
        // カーソル位置より左側のendはシフトしない
        val shouldShiftEnd =
            anchor.end > baseline || (anchor.end == baseline && anchor.style in currentInsertionStyles)

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
