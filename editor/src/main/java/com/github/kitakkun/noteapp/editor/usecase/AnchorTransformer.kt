package com.github.kitakkun.noteapp.editor.usecase

import androidx.annotation.VisibleForTesting
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.github.kitakkun.noteapp.data.model.BaseStyle
import com.github.kitakkun.noteapp.data.model.BaseStyleAnchor
import com.github.kitakkun.noteapp.data.model.OverrideStyle
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import com.github.kitakkun.noteapp.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.editor.editmodel.TextFieldChangeEvent
import com.github.kitakkun.noteapp.editor.ext.deleteLinesAndShiftUp
import com.github.kitakkun.noteapp.editor.ext.insertNewAnchorsAndShiftDown
import com.github.kitakkun.noteapp.editor.ext.optimizeRecursively
import com.github.kitakkun.noteapp.editor.ext.shiftToLeft
import com.github.kitakkun.noteapp.editor.ext.shiftToRight
import com.github.kitakkun.noteapp.editor.ext.splitAt
import com.github.kitakkun.noteapp.editor.ext.toValidOrder

class AnchorTransformer {
    /**
     * update override style anchors
     * @param event [TextFieldValue] change event
     * @param anchors current override style anchors
     * @param editorConfig active editor configuration
     */
    fun updateOverrideAnchors(
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
            ).optimizeRecursively(range = IntRange(event.position, event.position + event.length))
        }

        is TextFieldChangeEvent.Delete -> {
            anchors.shiftToLeft(
                baseOffset = event.position,
                shiftOffset = event.length,
            ).optimizeRecursively(range = IntRange(event.position - event.length, event.position))
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
                // TODO: need to be check if this logic is correct
                .optimizeRecursively(range = IntRange(event.position, event.position + event.insertedText.length))
        }

        else -> anchors
    }.filter { it.isValid() }

    @VisibleForTesting
    fun generateAnchorsToInsert(
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
        anchorsToInsert.add(
            OverrideStyleAnchor(
                start = insertPos,
                end = insertPos + length,
                style = OverrideStyle.Color(editorConfig.color),
            )
        )
        return anchorsToInsert
    }


    fun updateBaseStyleAnchors(
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

    fun toggleOverrideStyleOfSelection(
        currentAnchors: List<OverrideStyleAnchor>,
        selection: TextRange,
        overrideStyle: OverrideStyle,
        overrideAnchors: List<OverrideStyleAnchor>,
    ): List<OverrideStyleAnchor> {
        if (selection.collapsed) return overrideAnchors
        val orderedSelection = selection.toValidOrder()
        val newAnchors = currentAnchors
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
}
