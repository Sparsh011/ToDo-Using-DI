package com.example.todoappcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.todoappcompose.navigation.destinations.listComposable
import com.example.todoappcompose.navigation.destinations.splashComposable
import com.example.todoappcompose.navigation.destinations.taskComposable
import com.example.todoappcompose.ui.viewmodels.SharedViewModel
import com.example.todoappcompose.util.Constants.LIST_SCREEN_ROUTE
import com.example.todoappcompose.util.Constants.SPLASH_SCREEN_ROUTE

@Composable
fun SetupNavigation(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember (navHostController) {
        Screens(navController = navHostController)
    }

    NavHost(
        navController = navHostController,
        startDestination = SPLASH_SCREEN_ROUTE
    ) {
        splashComposable(
            navigateToListScreen = screen.splash
        )

        listComposable(
            navigateToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        taskComposable (
            navigateToListScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
    }
}