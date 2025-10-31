package com.dev.timeflow.Viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.timeflow.Data.Model.Events
import com.dev.timeflow.Data.Model.NotificationAlarmManagerModel
import com.dev.timeflow.Data.Model.Tasks
import com.dev.timeflow.Data.Repo.EventRepo
import com.dev.timeflow.Data.Repo.TaskRepo
import com.dev.timeflow.Managers.notification.TimeFlowAlarmManagerService
import com.dev.timeflow.Managers.notification.TimeFlowNotificationManager
import com.dev.timeflow.Managers.utils.toHour
import com.dev.timeflow.Managers.utils.toLocalDate
import com.dev.timeflow.Managers.utils.toMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

@HiltViewModel
class TaskAndEventViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eventRepo: EventRepo,
    private val taskRepo: TaskRepo
) : ViewModel(){

    // job to track and cancel the task fetch operation
    private var taskJob: Job? = null

    // job to track and cancel the event fetch operation
    private var eventJob: Job? = null


    // variable to hold all the events in the database
    private val _allEvents = MutableStateFlow<List<Events>>(emptyList())
    var allEvents : StateFlow<List<Events>> = _allEvents


    var scrollStateValue = MutableStateFlow<Int>(0)

    fun manageScrollState(scrollValue: Int){
        scrollStateValue.value = scrollValue
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


    private val _eventsForToday = MutableStateFlow<List<Events>>(emptyList())
    var eventForToday : StateFlow<List<Events>> = _eventsForToday

    // function to get event for a date
    fun getEventForToday(date: Long){
        viewModelScope.launch {
            eventRepo.getEventsForADate(
                date = date
            ).collect {
                _eventsForToday.value = it
                Log.d("Events","${it}")
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


    private val _eventForDate = MutableStateFlow<List<Events>>(emptyList())
    var eventForDate: StateFlow<List<Events>> = _eventForDate

    fun getAllEventsForADate(date : Long) {

        // cancel the previous job
        eventJob?.cancel()

        _eventForDate.value = emptyList()
       eventJob =  viewModelScope.launch {
            eventRepo.getEventsForADate(
                date = date
            ).collect {
                _eventForDate.value = it
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
    // function to add a task to the database
    fun insertTask(tasks: Tasks){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.insertTask(
                tasks = tasks
            )
            if (tasks.notification){
                TimeFlowAlarmManagerService(context = context).scheduleNotification(
                    context = context,
                    notificationAlarmManagerModel = NotificationAlarmManagerModel(
                        title = tasks.name,
                        id = tasks.id,
                        hour = tasks.taskTime.toHour(),
                        minute = tasks.taskTime.toMinute()
                    )
                )
            } else {
                println("notification has been turned off")
            }

            Log.d("TESTING NOTIFICATION","${tasks.taskTime.toHour()} ${tasks.taskTime.toMinute()}")
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


    // variable to hold the task for tasks
    private var  _taskForDate = MutableStateFlow<List<Tasks>>(emptyList())
    var taskForDate : StateFlow<List<Tasks>> = _taskForDate

    // function to get tasks for a date
    fun getTasksForADate(date : Long) {

        //cancelling existing task fetch
        taskJob?.cancel()

        _taskForDate.value = emptyList()


        taskJob =  viewModelScope.launch {
            taskRepo.getTasksForADate(
                date = date
            ).collect {
                _taskForDate.value = it
            }
        }
    }

    private var _taskForToday = MutableStateFlow<List<Tasks>>(emptyList())
    var taskForToday : StateFlow<List<Tasks>> = _taskForToday

    fun getTasksForToday (date: Long){
        viewModelScope.launch {
            taskRepo.getTasksForADate(
                date = date
            ).collect {
                _taskForToday.value = it
            }
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


    fun scheduleNotificationForTaskAndEvents() {

    }

}