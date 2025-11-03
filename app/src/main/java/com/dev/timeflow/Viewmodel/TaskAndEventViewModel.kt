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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    var allEvents : StateFlow<List<Events>> = _allEvents.asStateFlow()

    private val _currentTask = MutableStateFlow<Tasks?>(null)
    var currentTask = _currentTask

    // select task
    fun selectTask (tasks: Tasks){
        _currentTask.value = tasks
    }

    fun clearTask (){
        _currentTask.value = null
    }


    private val _currentEvent = MutableStateFlow<Events?>(null)
    var currentEvent = _currentEvent.asStateFlow()

    fun selectEvent(events: Events){
        _currentEvent.value = events
    }

    fun clearEvent(){
        _currentEvent.value = null
    }

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
    fun getEventForToday(start: Long, end: Long){
        viewModelScope.launch {
            eventRepo.getEventsForADate(
                start = start,
                end = end
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
            if (events.notification && events.eventTime != 0.toLong()){
                TimeFlowAlarmManagerService(context).scheduleNotification(
                    notificationAlarmManagerModel = NotificationAlarmManagerModel(
                        title = events.name,
                        id = events.id,
                        hour = events.eventTime.toHour(),
                        minute = events.eventTime.toMinute(),
                        localDate = events.eventTime.toLocalDate()
                    )
                )
            } else {
                println("notification has been turned off")
            }
        }
    }


    fun updateEvent(events: Events){
        viewModelScope.launch {
            eventRepo.updateEvent(
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

    fun getAllEventsForADate(start: Long, end: Long) {

        // cancel the previous job
        eventJob?.cancel()

        _eventForDate.value = emptyList()
        eventJob = viewModelScope.launch {
            eventRepo.getEventsForADate(
                start = start,
                end = end
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
            if (tasks.notification && tasks.taskTime != 0.toLong()){
                TimeFlowAlarmManagerService(context).scheduleNotification(
                    notificationAlarmManagerModel = NotificationAlarmManagerModel(
                        title = tasks.name,
                        id = tasks.id,
                        hour = tasks.taskTime!!.toHour(),
                        minute = tasks.taskTime.toMinute(),
                        localDate = tasks.taskTime.toLocalDate()
                    )
                )
            } else {
                println("notification has been turned off")
            }

            Log.d("TESTING NOTIFICATION","${tasks.taskTime!!.toHour()} ${tasks.taskTime.toMinute()} task date ${tasks.taskTime.toLocalDate()}")
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
    fun getTasksForADate(start : Long, end : Long) {

        //cancelling existing task fetch
        taskJob?.cancel()

        _taskForDate.value = emptyList()


        taskJob =  viewModelScope.launch {
            taskRepo.getTasksForADate(
              start = start, end = end
            ).collect {
                _taskForDate.value = it
            }
        }
    }

    private var _taskForToday = MutableStateFlow<List<Tasks>>(emptyList())
    var taskForToday : StateFlow<List<Tasks>> = _taskForToday

    fun getTasksForToday (start: Long, end: Long){
        viewModelScope.launch {
            taskRepo.getTasksForADate(
               start = start,
                end = end
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




}


