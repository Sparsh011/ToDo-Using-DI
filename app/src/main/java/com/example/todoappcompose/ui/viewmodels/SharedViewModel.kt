package com.example.todoappcompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappcompose.data.models.ToDoTask
import com.example.todoappcompose.data.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<ToDoTask>>(emptyList())
    val allTasks : StateFlow<List<ToDoTask>> = _allTasks

    fun getAllTasks() {
        viewModelScope.launch {
            toDoRepository.getAllTasks.collect {
                _allTasks.value = it
            }
        }
    }
}