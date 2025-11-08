package com.dev.timeflow.Managers

import android.content.Context
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel
import javax.inject.Inject

//class TimeflowWorkManager(context: Context,workerParameters: WorkerParameters) : Worker(context,workerParameters){
////    @Inject
////    lateinit var taskAndEventViewModel: TaskAndEventViewModel
////
////    override fun doWork(): Result {
////
////        taskAndEventViewModel.getTaskFor
////
////        val notificationTask = taskAndEventViewModel.ev
////
////    }
//}