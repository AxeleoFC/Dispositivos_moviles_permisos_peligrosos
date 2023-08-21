package com.example.memoraid.ui.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast

class MiReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val contactName = intent.getStringExtra("contactName")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val message = intent.getStringExtra("message")

        // Enviar el mensaje por SMS
        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)

        // Mostrar un mensaje de éxito
        Toast.makeText(context, "Mensaje enviado a $contactName", Toast.LENGTH_SHORT).show()
    }
}