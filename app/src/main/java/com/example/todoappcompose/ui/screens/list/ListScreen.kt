package com.example.todoappcompose.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.todoappcompose.R
import com.example.todoappcompose.ui.viewmodels.SharedViewModel
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    }
    val allTasks by sharedViewModel.allTasks.collectAsState() // updates allTasks var whenever there is a change in DB
    val searchedTasks by sharedViewModel.allSearchTasks.collectAsState()

    val searchAppBarState : SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextBarState : String by sharedViewModel.searchTextState

    val action by sharedViewModel.action

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    DisplaySnackBar(
        snackBarHostState = snackBarHostState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action = action) },
        onUndoClicked = {
            sharedViewModel.action.value = it
        },
        taskTitle = sharedViewModel.title.value,
        action = action
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextBarState
            )
        },
        content = { padding ->
            TasksListContent(
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                navigateToTaskScreen = navigateToTaskScreen,
                padding = padding, // passed because content was hidden behind top app bar
                searchAppBarState = searchAppBarState,
                highPriorityTasks = highPriorityTasks,
                lowPriorityTasks = lowPriorityTasks,
                sortState = sortState
            )
        },
        floatingActionButton = {
            ListFab(
                onFabClicked = navigateToTaskScreen
            )
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit,
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1) // passing -1 to let know that a new task is to be created
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    snackBarHostState: SnackbarHostState,
    handleDatabaseActions : () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle : String,
    action : Action
) {
    handleDatabaseActions()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = setSnackBarMessage(action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(action = action)
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }
}

private fun setSnackBarMessage(
    action: Action,
    taskTitle: String
) : String {
    return when (action) {
        Action.DELETE_ALL -> {
            "All Tasks Removed!"
        }
        else -> {
            "$taskTitle Removed!"
        }
    }
}

private fun setActionLabel(action: Action) : String {
    return if (action.name == "DELETE") {
        "UNDO"
    }
    else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked : (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}
