package com.example.todoappcompose.ui.screens.task

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.todoappcompose.data.models.Priority
import com.example.todoappcompose.data.models.ToDoTask
import com.example.todoappcompose.ui.viewmodels.SharedViewModel
import com.example.todoappcompose.util.Action

private val TAG = "TaskScreenTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit,
) {

    val title : String by sharedViewModel.title
    val description : String by sharedViewModel.description
    val priority : Priority by sharedViewModel.priority

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToListScreen(action)
                    }
                    else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(action)
                        }
                        else {
                            displayToast(context = context)
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            TaskContent(
                title = title,
                onTitleChanged = {
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescriptionChanged = {
                    sharedViewModel.description.value = it
                },
                priority = priority,
                onPrioritySelected = {
                    sharedViewModel.priority.value = it
                },
                paddingValues
            )
        }
    )
}

fun displayToast(context: Context) {
    Toast.makeText(context, "Title And Description Cannot Be Empty!", Toast.LENGTH_SHORT).show()
}
