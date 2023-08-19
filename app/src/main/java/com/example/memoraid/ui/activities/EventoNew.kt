package com.example.memoraid.ui.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.example.memoraid.R
import com.example.memoraid.data.connections.UserConnectionDB
import com.example.memoraid.data.dao.EventosDAO
import com.example.memoraid.data.entities.database.EventoDB
import com.example.memoraid.data.entities.database.UsuariosDB
import com.example.memoraid.databinding.ActivityEventoNewBinding
import com.example.memoraid.ui.adapter.ColoredSpinnerAdapter
import com.example.memoraid.ui.utilities.NotificacionesBroadcast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventoNew : AppCompatActivity() {

    private lateinit var binding: ActivityEventoNewBinding
    private lateinit var eventosDAO: EventosDAO
    private var fireBase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventoNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventosDAO = UserConnectionDB.getDatabase(this).eventoDao()

        binding.editTextFechaEvento.setOnClickListener {
            showDatePicker(binding.editTextFechaEvento)
        }

        binding.editTextFechaRecordar.setOnClickListener {
            showDatePicker(binding.editTextFechaRecordar)
        }
        val opcionesEvento = arrayOf(""
            ,"Fiesta"
            , "Aniversario"
            , "Cumpleaños"
            , "BabyShawer"
            , "Graduación"
            ,"Cita"
            , "Otro")
        val textColors = listOf(
            Color.parseColor("#000000"), // Sin evento
            Color.parseColor("#FF5733"), // Fiesta (Naranja)
            Color.parseColor("#3366FF"), // Aniversario (Azul)
            Color.parseColor("#000000"), // Cumpleaños (Verde)
            Color.parseColor("#FF3399"), // BabyShawer (Rosa)
            Color.parseColor("#FF9900"), // Graduación (Amarillo anaranjado)
            Color.parseColor("#9933FF"), // Cita (Morado)
            Color.parseColor("#FF33CC")  // Otro (Rosa oscuro)
        )


        val adapter = ColoredSpinnerAdapter(this, opcionesEvento.toList(), textColors)
        binding.spinnerTipoEvento.adapter = adapter

        binding.spinnerTipoEvento.adapter = adapter
        binding.btnNuevoEvento.setOnClickListener {
            val eventoObj = EventoDB(
                0,
                binding.spinnerTipoEvento.selectedItem.toString(),
                parseStringToDate(binding.editTextFechaEvento.text.toString()).time,
                parseStringToDate(binding.editTextFechaRecordar.text.toString()).time,
                binding.editTextPresupuesto.text.toString().toDouble()
            )
            lifecycleScope.launch(Dispatchers.IO) {
                //eventosDAO.insertEvento(eventoObj)
                insertarEvento(eventoObj)
                showSnackbar("Evento agregado")
                createNotificationChannel()
                sendNotification(eventoObj)
                clearFields()
                val ingresar = Intent(this@EventoNew, MenuPrincipal::class.java)
                startActivity(ingresar)
            }
        }
    }

    private fun insertarEvento(evento: EventoDB) {
        // Create a new user with a first, middle, and last name
        val TAG = "Memoraid"
        // Add a new document with a generated ID
        fireBase.collection("eventos")
            .add(evento)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    private fun parseStringToDate(dateString: String): Date {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.parse(dateString) ?: Date()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        binding.spinnerTipoEvento.setSelection(0)
        binding.editTextFechaEvento.text.clear()
        binding.editTextFechaRecordar.text.clear()
        binding.editTextPresupuesto.text.clear()
    }

    private fun showDatePicker(editText: EditText) {
        val newCalendar = Calendar.getInstance()

        // Mostrar DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Una vez que se selecciona la fecha, mostrar TimePickerDialog
                showTimePicker(year, monthOfYear, dayOfMonth, editText)
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun showTimePicker(year: Int, monthOfYear: Int, dayOfMonth: Int, editText: EditText) {
        val newCalendar = Calendar.getInstance()

        // Mostrar TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                val dateTimeFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                editText.setText(dateTimeFormatter.format(selectedDate.time))
            },
            newCalendar.get(Calendar.HOUR_OF_DAY),
            newCalendar.get(Calendar.MINUTE),
            true // Utiliza true si deseas mostrar el formato de 24 horas, o false para 12 horas
        )

        timePickerDialog.show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = "Notificacion evento"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Eventos", "Eventos", importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(eventoDB: EventoDB) {
        val miIntent = Intent(applicationContext, NotificacionesBroadcast::class.java)
        miIntent.putExtra("evento", eventoDB)

        val miPending = PendingIntent.getBroadcast(
            applicationContext,
            0,
            miIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificacion = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificacion.setExact(AlarmManager.RTC_WAKEUP, eventoDB.fechaRecordar, miPending)
    }

}

