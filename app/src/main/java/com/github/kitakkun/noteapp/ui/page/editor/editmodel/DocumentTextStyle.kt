package com.github.kitakkun.noteapp.ui.page.editor.editmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

sealed class DocumentTextStyle(
    open val color: Color = Color.Black,
    open val fontSize: TextUnit = 20.sp,
    open val letterSpacing: TextUnit = 0.sp,
) {
    object Title : DocumentTextStyle(fontSize = 25.sp, letterSpacing = 0.sp)

    object Body : DocumentTextStyle(fontSize = 20.sp, letterSpacing = 0.5.sp)

    object Heading : DocumentTextStyle(fontSize = 22.sp, letterSpacing = 0.sp)
}