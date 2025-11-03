package com.dev.timeflow.Managers.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.dev.timeflow.Di.Application
import com.dev.timeflow.MainActivity
import com.dev.timeflow.R
import dagger.hilt.android.qualifiers.ApplicationContext

class TimeFlowNotificationManager (
    @ApplicationContext context: Context
){
    fun showNotification(context: Context, message: String, notificationId : Int){
        val intent = Intent(context, MainActivity::class.java)
        Log.d("TESTING NOTIFICATION","the notification received for the task_event")
        val notification = NotificationCompat.Builder(context, Application.TIMEFLOW_NOTIFICATION_ID)
            .setSmallIcon(R.drawable.timeflow_mono_logo)
            .setContentTitle("Timeflow")
            .setContentText(message)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(context,POST_NOTIFICATIONS)== PackageManager.PERMISSION_GRANTED){
                NotificationManagerCompat.from(context)
                    .notify(notificationId,notification)
            }else {
                Log.d("TESTING NOTIFICATION","some error occ")
            }
        }
    }

}