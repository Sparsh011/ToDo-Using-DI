package com.example.todoappcompose.navigation

import androidx.navigation.NavHostController
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.Constants.LIST_SCREEN_ROUTE

class Screens (navController: NavHostController) {
    val list: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN_ROUTE) {
                inclusive = true // when we navigate back to AllTasksScreen, we want to pop TaskDetailsScreen from backstack.
            }
        }
    }

    val task : (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}









