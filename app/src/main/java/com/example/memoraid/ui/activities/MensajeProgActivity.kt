package com.example.memoraid.ui.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.memoraid.databinding.ActivityMensajeProgBinding
import com.example.memoraid.ui.utilities.MiReceiver
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar
import java.util.Date


class MensajeProgActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMensajeProgBinding

    private val REQUEST_CONTACTS_PERMISSION = 1
    private val REQUEST_SMS_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMensajeProgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @SuppressLint("MissingPermission")
        val mensajeContract = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> {
                enviarMensajeProgramado()
                }
                false -> {
                    Snackbar.make(
                        binding.texto,
                        "Denegado",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

            }
        }

        binding.btn.setOnClickListener {
            mensajeContract.launch(android.Manifest.permission.SEND_SMS)
        }
    }

    @SuppressLint("MissingPermission")
    private fun enviarMensajeProgramado() {
        // Configurar el tiempo de envío programado (por ejemplo, 5 segundos después de la ejecución)
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.SECOND, 5)

        // Obtener el número de teléfono del dispositivo
        val telephonyManager =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val phoneNumber = telephonyManager.line1Number

        if (phoneNumber != null) {
            // Crear un intent para el BroadcastReceiver que enviará el mensaje
            val intent = Intent(this, MiReceiver::class.java)
            intent.putExtra("contactName", "Tú mismo")
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("message", binding.texto.text.toString())
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Obtener el AlarmManager y programar el envío del mensaje
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            Toast.makeText(this, "No se pudo obtener el número de teléfono", Toast.LENGTH_SHORT).show()
        }
    }

}



