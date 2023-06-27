package com.github.kitakkun.noteapp.ui.page.editor

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.EditHistory
import java.util.Stack

class EditHistoryManager {
    private val undoStack = Stack<EditHistory>()
    private val redoStack = Stack<EditHistory>()

    val canUndo: Boolean get() = undoStack.isNotEmpty()
    val canRedo: Boolean get() = redoStack.isNotEmpty()

    fun pushUndo(editHistory: EditHistory) {
        undoStack.push(editHistory)
    }

    fun pushRedo(editHistory: EditHistory) {
        redoStack.push(editHistory)
    }

    fun popUndo(): EditHistory? {
        if (undoStack.isEmpty()) return null
        return undoStack.pop()
    }

    fun popRedo(): EditHistory? {
        if (redoStack.isEmpty()) return null
        return redoStack.pop()
    }

    fun clearRedo() {
        redoStack.clear()
    }
}
