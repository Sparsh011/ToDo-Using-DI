package com.example.todoappcompose.ui.screens.list

import android.graphics.drawable.VectorDrawable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoappcompose.R
import java.util.Vector

@Composable
fun EmptyListContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = getEmptyListLogo()), contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.no_tasks)
        )
    }
}

@Composable
fun getEmptyListLogo(): Int {
     return if (isSystemInDarkTheme()) R.drawable.checklist_light else R.drawable.checklist_dark
}

@Preview
@Composable
fun EmptyListContentPreview() {
    EmptyListContent()
}










