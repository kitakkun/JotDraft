package com.github.kitakkun.noteapp.data.model

import androidx.compose.ui.graphics.Color

sealed interface StyleColor {
    data class Dynamic(
        val lightValue: Color,
        val darkValue: Color,
    ) : StyleColor {
        companion object {
            val Default = Dynamic(
                lightValue = Color.Black,
                darkValue = Color.White
            )
        }
    }

    data class Static(
        val value: Color,
    ) : StyleColor
}

val Color.static get() = StyleColor.Static(this)
