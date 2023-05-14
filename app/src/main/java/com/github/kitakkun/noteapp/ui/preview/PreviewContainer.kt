package com.github.kitakkun.noteapp.ui.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.kitakkun.noteapp.ui.theme.NoteAppTheme

@Composable
fun PreviewContainer(content: @Composable () -> Unit) {
    NoteAppTheme {
        Surface {
            content()
        }
    }
}
