package com.github.kitakkun.noteapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a anchor for BaseStyle.
 * @param lineNumber 0-indexed line number
 * @style style to apply to the line
 */
data class BaseStyleAnchor(
    @SerializedName("line", alternate = ["lineNumber"]) val lineNumber: Int,
    val style: BaseStyle,
) {
    fun isValid(lineCount: Int) = lineNumber in 0 until lineCount
}
