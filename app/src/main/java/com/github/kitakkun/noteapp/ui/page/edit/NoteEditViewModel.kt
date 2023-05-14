package com.github.kitakkun.noteapp.ui.page.edit

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.data.DocumentRepository
import com.github.kitakkun.noteapp.ui.page.edit.editmodel.BaseTextFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteEditViewModel(
    private val documentRepository: DocumentRepository,
    private val navController: NavController,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(NoteEditUIState())
    val uiState = mutableUiState.asStateFlow()

    fun updateBaseTextFormat(baseTextFormat: BaseTextFormat) {
        mutableUiState.value = uiState.value.copy(
            baseFormatSpans = listOf(
                AnnotatedString.Range(
                    item = baseTextFormat,
                    start = 0,
                    end = uiState.value.rawContent.length,
                )
            )
        )
    }

    fun insertBoldSpanStart(index: Int) {
        mutableUiState.value = uiState.value.copy(
            boldSpans = uiState.value.boldSpans + listOf(index..index)
        )
    }

    fun updateRawContent(content: String) {
        mutableUiState.value = uiState.value.copy(
            rawContent = content
        )
    }

    fun saveDocument() = viewModelScope.launch {
        documentRepository.saveDocument(
            title = uiState.value.documentTitle,
            rawContent = uiState.value.rawContent,
        )
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}