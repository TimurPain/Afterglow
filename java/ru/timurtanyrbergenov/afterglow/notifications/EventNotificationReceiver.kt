package ru.timurtanyrbergenov.afterglow.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.timurtanyrbergenov.afterglow.R

class EventNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: return
        val type = intent.getStringExtra("type") ?: ""

        val notification = NotificationCompat.Builder(
            context,
            NotificationHelper.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Сегодня важная дата")
            .setContentText("$title • $type")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // ❗ Notification permission check (Android 13+)
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            NotificationManagerCompat.from(context)
                .notify(title.hashCode(), notification)
        }
    }
}