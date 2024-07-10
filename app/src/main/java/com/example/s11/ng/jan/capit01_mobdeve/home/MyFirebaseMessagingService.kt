package com.example.s11.ng.jan.capit01_mobdeve.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if the message is a data message
        if (remoteMessage.data.isNotEmpty()) {
            // Handle data message
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]

            // Create a notification
            val notification = NotificationCompat.Builder(this, "announcements")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .build()

            // Show the notification
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("announcements", "Announcements", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(12345, notification)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFirebaseMessaging", "Refreshed token: $token")
    }
}