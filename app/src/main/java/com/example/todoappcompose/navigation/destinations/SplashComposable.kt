package com.example.todoappcompose.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoappcompose.ui.screens.splash.SplashScreen
import com.example.todoappcompose.util.Constants.SPLASH_SCREEN_ROUTE

fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit,
) {
    composable(
        route = SPLASH_SCREEN_ROUTE,
    ) {
        SplashScreen(navigateToListScreen = navigateToListScreen)
    }
}