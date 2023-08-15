package com.example.todoappcompose.ui.screens.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.todoappcompose.R
import com.example.todoappcompose.components.PriorityDropdown
import com.example.todoappcompose.data.models.Priority
import com.example.todoappcompose.ui.theme.MEDIUM_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskContent(
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    paddingValues: PaddingValues
) {
    Column (
        modifier = Modifier
            .padding(paddingValues)
            .padding(MEDIUM_PADDING)
    ){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {
                onTitleChanged(it)
            },
            label = {
                Text(text = stringResource(id = R.string.title_label))
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

        PriorityDropdown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = {
                onDescriptionChanged(it)
            },
            label = {
                Text(text = stringResource(id = R.string.description_label))
            }
        )
    }
}