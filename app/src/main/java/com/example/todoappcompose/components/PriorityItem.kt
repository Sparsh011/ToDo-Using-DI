package com.example.todoappcompose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappcompose.data.models.Priority
import com.example.todoappcompose.ui.theme.LARGE_PADDING
import com.example.todoappcompose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.todoappcompose.ui.theme.Typography

@Composable
fun PriorityItem(priority: Priority) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Canvas(
            modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)
        ) {
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier
                .padding(start = LARGE_PADDING),
            text = priority.name,
            style = Typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
fun PriorityItemPreview() {
    PriorityItem(priority = Priority.HIGH)
}










