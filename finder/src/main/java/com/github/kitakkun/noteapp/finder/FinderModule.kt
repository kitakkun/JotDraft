package com.github.kitakkun.noteapp.finder

import com.github.kitakkun.noteapp.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val finderModule = module {
    viewModelOf(::FinderViewModel)
    viewModelOf(::SettingViewModel)
}
