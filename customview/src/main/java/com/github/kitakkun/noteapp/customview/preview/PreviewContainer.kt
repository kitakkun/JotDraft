package com.github.kitakkun.noteapp.customview.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.kitakkun.noteapp.customview.theme.NoteAppTheme

@Composable
fun PreviewContainer(content: @Composable () -> Unit) {
    NoteAppTheme {
        Surface {
            content()
        }
    }
}
