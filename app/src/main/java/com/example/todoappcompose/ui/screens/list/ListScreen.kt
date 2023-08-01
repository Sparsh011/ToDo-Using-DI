package com.example.todoappcompose.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.todoappcompose.R
import com.example.todoappcompose.ui.viewmodels.SharedViewModel
import com.example.todoappcompose.util.SearchAppBarState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    LaunchedEffect(key1 = true) {
        Log.d("ListScreenTag", "ListScreen: Launched Effect Triggered")
        sharedViewModel.getAllTasks()
    }
    val allTasks by sharedViewModel.allTasks.collectAsState() // updates allTasks whenever there is a change in DB

    val searchAppBarState : SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextBarState : String by sharedViewModel.searchTextState

    Scaffold(
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextBarState
            )
        },
        content = { padding ->
            TasksListContent(
                tasks = allTasks,
                navigateToTaskScreen = navigateToTaskScreen,
                padding = padding // passed because content was hidden behind top app bar
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