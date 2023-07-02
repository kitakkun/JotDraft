package com.github.kitakkun.noteapp.data.model

data class BaseStyleAnchor(
    val line: Int,
    val style: BaseStyle,
) {
    fun isValid(lineCount: Int) = line in 0 until lineCount
}
