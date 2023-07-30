package com.example.todoappcompose.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoappcompose.util.Constants.LIST_ARGUMENTS_KEY
import com.example.todoappcompose.util.Constants.LIST_SCREEN

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENTS_KEY) {
            type = NavType.StringType
        })
    ) {

    }
}