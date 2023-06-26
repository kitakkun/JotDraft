package com.github.kitakkun.noteapp.ui.page.editor.editmodel

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TextFieldChangeEvent {
    val oldValue: TextFieldValue
    val newValue: TextFieldValue

    data class Insert(
        val position: Int,
        val length: Int,
        val insertedText: String,
        override val oldValue: TextFieldValue,
        override val newValue: TextFieldValue,
    ) : TextFieldChangeEvent

    /**
     * @param position The position of the first character to delete.
     * @param length The number of characters to delete.
     */
    data class Delete(
        val position: Int,
        val length: Int,
        val deletedText: String,
        override val oldValue: TextFieldValue,
        override val newValue: TextFieldValue,
    ) : TextFieldChangeEvent

    data class Replace(
        val position: Int,
        val deletedText: String,
        val insertedText: String,
        override val oldValue: TextFieldValue,
        override val newValue: TextFieldValue,
    ) : TextFieldChangeEvent

    data class CursorMove(
        override val oldValue: TextFieldValue,
        override val newValue: TextFieldValue,
    ) : TextFieldChangeEvent

    data class NoChange(
        override val oldValue: TextFieldValue,
        override val newValue: TextFieldValue,
    ) : TextFieldChangeEvent

    companion object {
        fun fromTextFieldValueChange(
            old: TextFieldValue,
            new: TextFieldValue,
        ) = when {
            old.text != new.text -> classifyTextChange(old, new)
            old.selection != new.selection -> CursorMove(old, new)
            else -> NoChange(old, new)
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
                oldValue = old,
                newValue = new,
            )

            old.text.length < new.text.length -> Insert(
                position = old.selection.start,
                length = new.text.length - old.text.length,
                insertedText = new.text.substring(
                    startIndex = old.selection.start,
                    endIndex = new.selection.start,
                ),
                oldValue = old,
                newValue = new,
            )

            old.text.length > new.text.length -> Delete(
                position = old.selection.start,
                length = old.text.length - new.text.length,
                deletedText = old.text.substring(
                    startIndex = new.selection.start,
                    endIndex = old.selection.start,
                ),
                oldValue = old,
                newValue = new,
            )

            else -> NoChange(
                oldValue = old,
                newValue = new,
            )
        }
    }
}
