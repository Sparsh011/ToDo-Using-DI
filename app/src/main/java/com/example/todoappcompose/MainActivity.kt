package com.example.todoappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoappcompose.navigation.SetupNavigation
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppComposeTheme {
                navHostController = rememberNavController()
                SetupNavigation(navHostController = navHostController)
            }
        }
    }
}