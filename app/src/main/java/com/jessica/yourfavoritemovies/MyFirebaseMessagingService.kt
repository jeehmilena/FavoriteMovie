package com.jessica.yourfavoritemovies

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jessica.yourfavoritemovies.authentication.view.LoginActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TOPIC = "favoritemovie"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("TOKEN", "----> $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null && remoteMessage.notification!!.body != null) {

            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            createNotificationChannel()
            subscribeTopic()
            val name: CharSequence = getString(R.string.default_notification_channel_id)
            val title = remoteMessage.notification!!.title
            val message = remoteMessage.notification!!.body
            val som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val builder = NotificationCompat.Builder(this, name.toString())
                .setSmallIcon(R.drawable.movie)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(som)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.default_notification_channel_id)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(name.toString(), name, importance)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC).isComplete
    }
}