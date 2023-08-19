package com.example.memoraid.ui.utilities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.memoraid.R
import com.example.memoraid.data.entities.database.EventoDB
import com.example.memoraid.ui.activities.DatoEvento

class NotificacionesBroadcast : BroadcastReceiver() {

    override fun onReceive(contexto: Context, intent: Intent) {
        val eventoDB = intent.getParcelableExtra<EventoDB>("evento")
        if (eventoDB != null) {
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                contexto,
                0,
                Intent(contexto, DatoEvento::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )

            val noti = NotificationCompat.Builder(contexto, "Eventos")
                .setContentTitle(eventoDB.tipo)
                .setContentText(eventoDB.fechaRecordar.toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.logo)
                .setStyle(NotificationCompat.BigTextStyle().bigText(eventoDB.tipo))
                .setContentIntent(pendingIntent)

            val notificationManager =
                contexto.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(
                System.currentTimeMillis().toInt(),
                noti.build()
            )
        }
    }
}