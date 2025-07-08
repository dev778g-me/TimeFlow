package com.dev.timeflow.Presentation.Viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.Data.Repo.EventRepo
import com.dev.timeflow.Data.Repo.TaskRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepo: EventRepo,
    private val taskRepo: TaskRepo
) : ViewModel(){

    // variable to hold all the events in the database
    private val _allEvents = MutableStateFlow<List<Events>>(emptyList())
    var allEvents : StateFlow<List<Events>> = _allEvents

    init {
        getAllTasks()
    }
    // function to get all the events from the database
    fun getAllEvents() {
        viewModelScope.launch {
            val events = eventRepo.getAllEvents()
            events.collect {
                _allEvents.value = it
            }
        }
    }


    // variable to hold the all the tasks in the database
    private val _allTasks = MutableStateFlow<List<Tasks>>(emptyList())
    var allTasks : StateFlow<List<Tasks>> = _allTasks

    fun getAllTasks(){
        viewModelScope.launch {
            val tasks =taskRepo.getAllTasks()
            tasks.collect {
                _allTasks.value = it
            }
        }
    }

    // function to insert an event into the database
    fun insertEvent(events: Events){
        viewModelScope.launch(Dispatchers.IO) {
            eventRepo.insertEvent(
                events = events
            )
        }
    }


    // function to delete an event from the database
    fun deleteEvent(events: Events){
        viewModelScope.launch(Dispatchers.IO) {
            eventRepo.deleteEvent(
                events = events
            )
        }
    }


    // function to add a task to the database
    fun insertTask(tasks: Tasks){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.insertTask(
                tasks = tasks
            )
        }
    }

    // function to update a task in the database
    fun updateTask(tasks: Tasks){
        viewModelScope.launch {
            taskRepo.updateTask(
                tasks = tasks
            )
        }
    }

    // function to delete a task from the database
    fun deleteTask(tasks: Tasks){
       viewModelScope.launch(Dispatchers.IO) {
           taskRepo.deleteTask(
               tasks = tasks
           )
       }
    }

}