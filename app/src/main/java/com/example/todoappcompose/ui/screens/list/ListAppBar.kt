package com.example.todoappcompose.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappcompose.R
import com.example.todoappcompose.components.PriorityItem
import com.example.todoappcompose.data.models.Priority
import com.example.todoappcompose.ui.theme.LARGE_PADDING
import com.example.todoappcompose.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.todoappcompose.ui.viewmodels.SharedViewModel
import com.example.todoappcompose.util.SearchAppBarState
import com.example.todoappcompose.util.TrailingSearchIconState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = {},
                onDeleteClicked = {}
            )
        }
        else -> {
            SearchAppBar(
                searchQuery = searchTextState,
                onTextChange = { newText ->
                    sharedViewModel.searchTextState.value = newText
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value =
                        SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchClicked = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "Tasks"
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White),
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteClicked = onDeleteClicked
            )
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit,
) {
    SearchAction(
        onSearchClicked
    )
    SortAction(
        onSortClicked = onSortClicked
    )
    DeleteAllAction(
        onDeleteClicked = onDeleteClicked
    )
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(
        onClick = {
            expanded = true
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_filter_list_24),
            contentDescription = stringResource(id = R.string.sort_action),
            tint = Color.Black
        )

        DropdownMenu(
            expanded = expanded, // by default not expanded
            onDismissRequest = {
                expanded = false
            })
        {
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.LOW)
                },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.LOW)
                }
            )
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.HIGH)
                },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.HIGH)
                }
            )
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.NONE)
                },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.NONE)
                }
            )
        }
    }
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit,
) {
    IconButton(
        onClick = { onSearchClicked() }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_tasks),
            tint = Color.Black
        )
    }
}

@Composable
fun DeleteAllAction(
    onDeleteClicked: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(
        onClick = {
            expanded = true
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_more_vert_24),
            contentDescription = stringResource(id = R.string.delete_all_tasks),
            tint = Color.Black
        )

        DropdownMenu(
            expanded = expanded, // by default not expanded
            onDismissRequest = {
                expanded = false
            }
        )
        {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.padding(start = LARGE_PADDING),
                        text = stringResource(id = R.string.delete_all_tasks)
                    )
                },
                onClick = {
                    onDeleteClicked()
                    expanded = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    searchQuery: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    var trailingSearchIconState by remember {
        mutableStateOf(TrailingSearchIconState.READY_TO_DELETE_SEARCH_QUERY)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        color = Color.White
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_tasks),
                    color = Color.LightGray
                )
            },
            textStyle = TextStyle(
                color = Color.Black
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_tasks),
                        tint = Color.Gray
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    when(trailingSearchIconState) {
                        TrailingSearchIconState.READY_TO_DELETE_SEARCH_QUERY -> {
                            onTextChange("")
                            trailingSearchIconState = TrailingSearchIconState.READY_TO_CLOSE_SEARCH_BAR
                        }
                        TrailingSearchIconState.READY_TO_CLOSE_SEARCH_BAR -> {
                            if(searchQuery.isNotEmpty()) {
                                onTextChange("")
                            }
                            else {
                                onCloseClicked()
                                trailingSearchIconState = TrailingSearchIconState.READY_TO_DELETE_SEARCH_QUERY
                            }
                        }
                    }
                })
                {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(id = R.string.clear_search),
                        tint = Color.Gray
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search // keyboard's bottom right button will be search button
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(searchQuery)
                }
            )
        )
    }
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SearchAppBar(
        searchQuery = "Search",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}


@Preview
@Composable
fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteClicked = {}
    )
}










