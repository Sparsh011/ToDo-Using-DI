package com.example.todoappcompose.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappcompose.R
import com.example.todoappcompose.ui.theme.fabBackgroundColor
import com.example.todoappcompose.ui.viewmodels.SharedViewModel
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action)
    }
    
    val allTasks by sharedViewModel.allTasks.collectAsState() // updates allTasks var whenever there is a change in DB
    val searchedTasks by sharedViewModel.allSearchTasks.collectAsState()

    val searchAppBarState : SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextBarState : String = sharedViewModel.searchTextState

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    DisplaySnackBar(
        snackBarHostState = snackBarHostState,
        onComplete = { sharedViewModel.updateAction(newAction = it)},
        onUndoClicked = {
            sharedViewModel.updateAction(newAction = it)
        },
        taskTitle = sharedViewModel.title,
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
                sortState = sortState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.updateAction(newAction = action)
                    sharedViewModel.updateTaskFields(selectedTask = task)
                    snackBarHostState.currentSnackbarData?.dismiss()
                }
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
        },
        containerColor = MaterialTheme.colorScheme.fabBackgroundColor,
        shape = FloatingActionButtonDefaults.largeShape
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint =  Color.Black
        )
    }
}

@Composable
fun DisplaySnackBar(
    snackBarHostState: SnackbarHostState,
    onComplete : (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle : String,
    action : Action
) {
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

            onComplete(Action.NO_ACTION)
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
        Action.DELETE -> {
            "$taskTitle Removed!"
        }
        Action.UPDATE -> {
            "$taskTitle Updated!"
        }
        else -> {
            "$taskTitle Added!"
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

@Preview
@Composable
fun ListFabButtonPreview() {
    ListFab(onFabClicked = {})
}