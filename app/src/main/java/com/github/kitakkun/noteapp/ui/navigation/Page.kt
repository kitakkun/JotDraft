package com.github.kitakkun.noteapp.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType

abstract class Page(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) {
    // resolve arguments from NavBackStackEntry.arguments
    fun resolveArguments(entry: NavBackStackEntry): List<Any?> {
        return arguments.map {
            when (it.argument.type) {
                NavType.StringType -> entry.arguments?.getString(it.name)
                NavType.IntType -> entry.arguments?.getInt(it.name)
                NavType.LongType -> entry.arguments?.getLong(it.name)
                NavType.FloatType -> entry.arguments?.getFloat(it.name)
                NavType.BoolType -> entry.arguments?.getBoolean(it.name)
                NavType.FloatArrayType -> entry.arguments?.getFloatArray(it.name)
                NavType.IntArrayType -> entry.arguments?.getIntArray(it.name)
                NavType.LongArrayType -> entry.arguments?.getLongArray(it.name)
                NavType.StringArrayType -> entry.arguments?.getStringArray(it.name)
                NavType.BoolArrayType -> entry.arguments?.getBooleanArray(it.name)
                else -> null
            }
        }
    }

    // route with argument placeholders
    val routeWithArgs: String
        get() = when (arguments.isEmpty()) {
            true -> route
            false -> route + arguments.joinToString(
                separator = "/",
                prefix = "/",
                transform = { "{${it.name}}" }
            )
        }

    // generate route with arguments for navigation
    fun routeWithArgs(vararg params: String?): String {
        if (params.size != arguments.size) {
            throw Exception("Illegal arguments length for navigation params.")
        }
        return route + params.joinToString(separator = "/", prefix = "/")
    }
}

