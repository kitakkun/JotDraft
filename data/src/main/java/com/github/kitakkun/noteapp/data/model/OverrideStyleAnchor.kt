package com.github.kitakkun.noteapp.data.model

data class OverrideStyleAnchor(
    val start: Int,
    val end: Int,
    val style: OverrideStyle,
) {
    fun isValid() = (start < end) && (start >= 0)
}
