package com.example.todoappcompose.util

object Constants {
    const val DATABASE_TABLE = "todos_table"
    const val DATABASE_NAME = "todos_database"

//    Routes -
    const val LIST_SCREEN_ROUTE = "list/{action}"
    const val TASK_SCREEN_ROUTE = "task/{taskId}"
    const val SPLASH_SCREEN_ROUTE = "splash"

//    Arguments -
    const val LIST_ARGUMENTS_KEY = "action"
    const val TASK_ARGUMENTS_KEY = "taskId"

    const val MAX_TITLE_LENGTH = 30

//    DataStore -
    const val PREFERENCE_NAME = "todos_preference"
    const val PREFERENCE_KEY = "sort_state"

    const val SPLASH_SCREEN_DELAY = 2500L
}