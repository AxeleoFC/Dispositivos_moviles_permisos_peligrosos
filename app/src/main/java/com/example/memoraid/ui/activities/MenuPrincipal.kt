package com.example.memoraid.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.data.entities.database.EventoDB
import com.example.memoraid.databinding.ActivityMenuPrincipalBinding
import com.example.memoraid.ui.adapter.EventosAdapter
import com.example.memoraid.ui.fragments.DatePikerFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Locale

class MenuPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private var fireBase = Firebase.firestore
    private val PERMISSION_RECORD_AUDIO = 1
    private val REQUEST_CODE_SPEECH_INPUT = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val rvDatos = binding.rvEventos

        binding.buttonEventos.setOnClickListener {
            startActivity(Intent(this, RLocalActivity::class.java))
        }

        binding.btnNuevoEvento.setOnClickListener {
            startActivity(Intent(this, EventoNew::class.java))
        }

        binding.datePicker2.setOnClickListener {
            showDatePickerDialog()
        }

        // Llamar a getData y actualizar el RecyclerView con los datos
        getData()
        binding.voiceCommandButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecognition()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                    // El usuario ya ha denegado el permiso en el pasado pero no ha marcado "No preguntar de nuevo"
                    showPermissionDeniedMessage()
                } else {
                    // Se solicita el permiso por primera vez o se ha marcado "No preguntar de nuevo"
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_RECORD_AUDIO)
                }
            }}

    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Di un comando...")

        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result != null && result.isNotEmpty()) {
                val spokenText = result[0]
                processVoiceCommand(spokenText)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, iniciar reconocimiento de voz
                startVoiceRecognition()
            } else {
                // Permiso denegado, mostrar mensaje
                showPermissionDeniedMessage()
            }
        }
    }

    private fun processVoiceCommand(command: String) {
        when {
            command.contains("abrir evento nuevo") -> {
                val intent = Intent(this, EventoNew::class.java)
                startActivity(intent)
            }
            command.contains("Quiero un evento") -> {
                val intent = Intent(this, EventoNew::class.java)
                startActivity(intent)
            }

            else -> {

                showToast("Comando de voz no reconocido")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun showPermissionDeniedMessage() {
        showToast("Permiso de grabación de audio denegado. No se puede usar el reconocimiento de voz.")
    }
    private fun getData() {
        val adapter = EventosAdapter(
            ArrayList()
        ) { sendDatoItemEven(it) }

        val rvDatos = binding.rvEventos
        rvDatos.adapter = adapter
        rvDatos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Llamar a getEventos y actualizar el adaptador con los datos obtenidos
        getEventos { eventos ->
            adapter.updateData(eventos)
        }
    }

    private fun getEventos(callback: (ArrayList<Evento>) -> Unit) {
        val TAG = "Memoraid"
        val eventosList = ArrayList<Evento>()
        fireBase.collection("eventos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data
                    eventosList.add(
                        Evento(
                            1,
                            data["tipo"] as String,
                            data["formattedFecha"] as String,
                            data["formattedFechaRecordar"]as String,
                            data["presupuesto"] as Double
                        )
                    )
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                callback(eventosList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun showDatePickerDialog() {
        val datePicker= DatePikerFragment { day, month, year -> onDateSelected(day, month, year) }

        datePicker.show(supportFragmentManager,"datePicker")
    }

    fun onDateSelected(day:Int,month:Int,year:Int){
        val c: Calendar = Calendar.getInstance()
        val day=c.get(Calendar.DAY_OF_MONTH)
        val month=c.get(Calendar.MONTH)
        val year=c.get(Calendar.YEAR)
    }

    fun sendDatoItemEven(item: Evento):Unit {
        val i = Intent(this, DatoEvento::class.java)
        i.putExtra("evento",item)
        startActivity(i)

    }
}



/*
*
*
* private fun getMarkedDaysFromFirestore(): Array<Calendar> {
        val markedDaysList = ArrayList<Calendar>()

        getEventos { eventos ->
            for (evento in eventos) {
                val formattedFecha = evento.fecha
                val calendar = convertStringToCalendar(formattedFecha)
                calendar?.let { markedDaysList.add(it) }
            }
        }

        return markedDaysList.toTypedArray()
    }
    * */