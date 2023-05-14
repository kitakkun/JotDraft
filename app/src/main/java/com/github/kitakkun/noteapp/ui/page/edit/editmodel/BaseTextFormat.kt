package com.github.kitakkun.noteapp.ui.page.edit.editmodel

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class BaseTextFormat(
    private val fontSize: TextUnit,
    private val letterSpacing: TextUnit,
) {
    Title(
        fontSize = 25.sp,
        letterSpacing = 0.sp,
    ),
    Heading(
        fontSize = 22.sp,
        letterSpacing = 0.sp,
    ),
    Body(
        fontSize = 20.sp,
        letterSpacing = 0.5.sp,
    );

    val spanStyle = SpanStyle(
        fontSize = fontSize,
        letterSpacing = letterSpacing
    )
}