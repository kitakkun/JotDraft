package com.github.kitakkun.noteapp.ui.page.editor.editmodel

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TextFieldChangeEvent {
    data class Insert(
        val position: Int,
        val length: Int,
        val insertedText: String,
    ) : TextFieldChangeEvent

    /**
     * @param position The position of the first character to delete.
     * @param length The number of characters to delete.
     */
    data class Delete(
        val position: Int,
        val length: Int,
    ) : TextFieldChangeEvent

    data class Replace(
        val position: Int,
        val deletedText: String,
        val insertedText: String,
    ) : TextFieldChangeEvent

    object CursorMove : TextFieldChangeEvent
    object NoChange : TextFieldChangeEvent

    companion object {
        fun fromTextFieldValueChange(
            old: TextFieldValue,
            new: TextFieldValue,
        ) = when {
            old.text != new.text -> classifyTextChange(old, new)
            old.selection != new.selection -> CursorMove
            else -> NoChange
        }

        private fun classifyTextChange(
            old: TextFieldValue,
            new: TextFieldValue,
        ) = when {
            !old.selection.collapsed -> Replace(
                position = old.selection.start,
                deletedText = old.text.substring(
                    startIndex = old.selection.start,
                    endIndex = old.selection.end,
                ),
                insertedText = new.text.substring(
                    startIndex = new.selection.start,
                    endIndex = new.selection.end,
                ),
            )

            old.text.length < new.text.length -> Insert(
                position = old.selection.start,
                length = new.text.length - old.text.length,
                insertedText = new.text.substring(
                    startIndex = old.selection.start,
                    endIndex = new.selection.start,
                ),
            )

            old.text.length > new.text.length -> Delete(
                position = old.selection.start,
                length = old.text.length - new.text.length,
            )

            else -> NoChange
        }
    }
}
