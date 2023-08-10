package com.example.todoappcompose.navigation

import androidx.navigation.NavHostController
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.Constants.LIST_SCREEN_ROUTE
import com.example.todoappcompose.util.Constants.SPLASH_SCREEN_ROUTE

class Screens (navController: NavHostController) {
    val splash: () -> Unit = {
        navController.navigate(route = "list/${Action.NO_ACTION}") {
            popUpTo(SPLASH_SCREEN_ROUTE) { inclusive = true }
        }
    }
    val list : (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }

    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN_ROUTE) {
                inclusive = true // when we navigate back to AllTasksScreen, we want to pop TaskDetailsScreen from backstack.
            }
        }
    }
}









