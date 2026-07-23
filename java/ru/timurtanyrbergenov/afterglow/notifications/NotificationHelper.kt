package ru.timurtanyrbergenov.afterglow.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import ru.timurtanyrbergenov.afterglow.R
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity

object NotificationHelper {

    const val CHANNEL_ID = "important_dates_channel"

    // Универсальный метод для уведомления о важной дате
    fun showTomorrowEvent(context: Context, event: EventEntity?) {
        if (event == null) return

        // Проверка разрешений Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!permissionGranted) return
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Завтра важная дата")
            .setContentText("${event.title} • ${event.type.title}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(event.id.toInt(), notification)
    }
}
