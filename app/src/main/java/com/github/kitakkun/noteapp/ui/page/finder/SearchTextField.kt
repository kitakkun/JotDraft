package com.github.kitakkun.noteapp.ui.page.finder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer

@Composable
fun SearchField(
    searchWord: String,
    onChangeSearchWord: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = searchWord,
        singleLine = true,
        onValueChange = onChangeSearchWord,
        decorationBox = { textField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp,
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
                textField()
            }
        },
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier,
    )
}


@Preview
@Composable
private fun SearchFieldPreview() = PreviewContainer {
    SearchField(
        searchWord = "",
        onChangeSearchWord = {},
    )
}
