package com.github.kitakkun.noteapp.ui.page.editor.editmodel.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

sealed class BaseDocumentTextStyle(
    val color: Color,
    val fontSize: TextUnit,
    val letterSpacing: TextUnit,
) : AbstractDocumentTextStyle {
    object Title : BaseDocumentTextStyle(
        color = Color.Black,
        fontSize = 25.sp,
        letterSpacing = 0.sp,
    )

    object Heading : BaseDocumentTextStyle(
        color = Color.Black,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
    )

    object Body : BaseDocumentTextStyle(
        color = Color.Black,
        fontSize = 20.sp,
        letterSpacing = 0.5.sp,
    )
}