package com.github.kitakkun.noteapp.data.model

import androidx.compose.ui.graphics.Color

sealed interface StyleColor {
    data class Dynamic(
        val lightValue: Color,
        val darkValue: Color,
    ) : StyleColor

    data class Static(
        val value: Color,
    ) : StyleColor
}

val Color.static get() = StyleColor.Static(this)
