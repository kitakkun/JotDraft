package com.github.kitakkun.noteapp.ui.page.finder

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FinderViewModel : ViewModel(), KoinComponent {
    private val mutableUiState = MutableStateFlow(FinderUiState())
    val uiState = mutableUiState.asStateFlow()

    private val navController: NavController by inject()

    // 言ってることとやってることが噛み合ってないのであとで直す
    // 新規ドキュメントを作成して，編集画面に遷移したい
    fun createNewDocument() {
        navController.navigate("noteEdit")
    }
}
