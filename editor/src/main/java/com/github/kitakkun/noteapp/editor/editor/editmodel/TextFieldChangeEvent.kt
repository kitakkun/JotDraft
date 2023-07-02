package com.github.kitakkun.noteapp.editor.editor.editmodel

import androidx.compose.ui.text.input.TextFieldValue
import com.github.kitakkun.noteapp.editor.editor.ext.toValidOrder

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
        ): TextFieldChangeEvent {
            val oldSelection = old.selection.toValidOrder()
            val newSelection = new.selection.toValidOrder()
            return when {
                !old.selection.collapsed -> {
                    Replace(
                        position = oldSelection.start,
                        deletedText = old.text.substring(
                            startIndex = oldSelection.start,
                            endIndex = oldSelection.end,
                        ),
                        insertedText = new.text.substring(
                            startIndex = newSelection.start,
                            endIndex = newSelection.end,
                        ),
                        oldValue = old,
                        newValue = new,
                    )
                }

                old.text.length < new.text.length -> {
                    Insert(
                        position = oldSelection.start,
                        length = new.text.length - old.text.length,
                        insertedText = new.text.substring(
                            startIndex = oldSelection.start,
                            endIndex = newSelection.start,
                        ),
                        oldValue = old,
                        newValue = new,
                    )
                }

                old.text.length > new.text.length -> {
                    Delete(
                        position = oldSelection.start,
                        length = old.text.length - new.text.length,
                        deletedText = old.text.substring(
                            startIndex = newSelection.start,
                            endIndex = oldSelection.start
                        ),
                        oldValue = old,
                        newValue = new,
                    )
                }

                else -> NoChange(
                    oldValue = old,
                    newValue = new,
                )
            }
        }
    }
}
