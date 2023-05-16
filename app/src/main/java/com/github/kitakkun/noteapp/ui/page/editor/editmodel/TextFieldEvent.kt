package com.github.kitakkun.noteapp.ui.page.editor.editmodel

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TextFieldEvent {
    object CursorMoved : TextFieldEvent
    object TextSelected : TextFieldEvent
    object TextUnselected : TextFieldEvent
    data class TextInserted(
        val insertedLength: Int,
    ) : TextFieldEvent

    data class TextDeleted(
        val deletedLength: Int,
    ) : TextFieldEvent

    companion object {
        fun fromTextFieldValueChange(
            old: TextFieldValue,
            new: TextFieldValue,
        ): TextFieldEvent {
            return when {
                old.text.length < new.text.length -> TextInserted(insertedLength = new.text.length - old.text.length)
                old.text.length > new.text.length -> TextDeleted(deletedLength = old.text.length - new.text.length)
                !new.selection.collapsed -> TextSelected
                new.selection.collapsed && new.selection != old.selection -> CursorMoved
                else -> TextUnselected
            }
        }
    }
}
