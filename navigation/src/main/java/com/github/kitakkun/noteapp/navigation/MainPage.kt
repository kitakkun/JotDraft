package com.github.kitakkun.noteapp.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object MainPage : Page(route = "main") {
    object Finder : Page(route = "main/finder")
    object Editor : Page(
        route = "main/editor",
        arguments = listOf(
            navArgument(name = "documentId") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    )
}
