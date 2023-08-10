package com.example.todoappcompose.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappcompose.data.models.Priority
import com.example.todoappcompose.data.models.ToDoTask
import com.example.todoappcompose.data.repository.DataStoreRepository
import com.example.todoappcompose.data.repository.ToDoRepository
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.Constants.MAX_TITLE_LENGTH
import com.example.todoappcompose.util.RequestState
import com.example.todoappcompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    var action by mutableStateOf(Action.NO_ACTION)
        private set // means action's value cannot be changed outside of viewmodel

    var id by mutableIntStateOf(0)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var priority by mutableStateOf(Priority.LOW)
        private set

    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set

    var searchTextState by mutableStateOf("")
        private set

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _allSearchTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allSearchTasks: StateFlow<RequestState<List<ToDoTask>>> = _allSearchTasks

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    val lowPriorityTasks : StateFlow<List<ToDoTask>> = toDoRepository.sortByLowPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val highPriorityTasks : StateFlow<List<ToDoTask>> = toDoRepository.sortByHighPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun searchDatabase(searchQuery: String) {
        _allSearchTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.searchDatabase(searchQuery = "%$searchQuery%").collect { searchedTasks ->
                    _allSearchTasks.value = RequestState.Success(searchedTasks)
                }
            }
        } catch (e: Exception) {
            _allSearchTasks.value = RequestState.Error(e)
        }

        searchAppBarState = SearchAppBarState.TRIGGERED
    }


    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTasks(taskId: Int) {
        viewModelScope.launch {
            toDoRepository.getSelectedTasks(taskId = taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id = selectedTask.id
            title = selectedTask.title
            description = selectedTask.description
            priority = selectedTask.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length <= MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }

    private fun addTask() {
        viewModelScope.launch {
            val task = ToDoTask(
                title = title,
                description = description,
                priority = priority
            )

            toDoRepository.addTask(toDoTask = task)
        }
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch {
            val task = ToDoTask(
                id = id, // must update the same task that is why passing id
                title = title,
                description = description,
                priority = priority
            )

            toDoRepository.updateTask(toDoTask = task)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            val task = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )

            toDoRepository.deleteTask(toDoTask = task)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }

            Action.UPDATE -> {
                updateTask()
            }

            Action.DELETE -> {
                deleteTask()
            }

            Action.DELETE_ALL -> {
                deleteAllTasks()
            }

            Action.UNDO -> {
                addTask()
            }

            else -> {

            }
        }
    }

    private fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map {
                        Priority.valueOf(it)
                    }
                    .collect {
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun persistSortingTasksByPriorityState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    fun updateAction(newAction: Action) {
        action = newAction
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updatePriority(newPriority: Priority) {
        priority = newPriority
    }

    fun updateSearchAppBarState(newState: SearchAppBarState) {
        searchAppBarState = newState
    }

    fun updateSearchText(newSearchQuery: String) {
        searchTextState = newSearchQuery
    }

    init {
        getAllTasks()
        readSortState()
    }
}