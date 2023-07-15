package com.github.kitakkun.noteapp.data.model

/**
 * Represents a anchor for BaseStyle.
 * @param lineNumber 0-indexed line number
 * @style style to apply to the line
 */
data class BaseStyleAnchor(
    val lineNumber: Int,
    val style: BaseStyle,
) {
    fun isValid(lineCount: Int) = lineNumber in 0 until lineCount
}
