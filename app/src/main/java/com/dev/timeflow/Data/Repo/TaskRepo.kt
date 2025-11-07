package com.dev.timeflow.Data.Repo

import com.dev.timeflow.Data.Dao.TaskDao
import com.dev.timeflow.Data.Model.Tasks
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepo @Inject constructor(
    private val taskDao: TaskDao
){
    //function to insert or add a task
   suspend fun insertTask(tasks: Tasks){
        taskDao.insertTask(
            tasks = tasks
        )
    }

    // fun to update a task
    suspend fun updateTask(tasks: Tasks){
        taskDao.updateTask(
            tasks = tasks
        )
    }

    // function to delete a task
    suspend fun deleteTask(tasks: Tasks){
        taskDao.deleteTask(
            tasks = tasks
        )
    }

    suspend fun getTasksForADate (start: Long, end : Long) : Flow<List<Tasks>>{
        return taskDao.getTaskForDate(
            start = start,
            end = end
        )
    }

   suspend fun getTaskForScheduling() : Flow<List<Tasks>>{
        return taskDao.getTaskForAlarm()
    }

    suspend fun getAllTasks() : Flow<List<Tasks>>{
       return taskDao.getAllTasks()
    }


}