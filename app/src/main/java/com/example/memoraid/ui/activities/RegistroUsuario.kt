package com.example.memoraid.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.memoraid.R
import com.example.memoraid.data.connections.UserConnectionDB
import com.example.memoraid.data.dao.UsuariosDAO
import com.example.memoraid.data.entities.database.UsuariosDB
import com.example.memoraid.databinding.ActivityRegistroUsuarioBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistroUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroUsuarioBinding
    private lateinit var usuarioDao: UsuariosDAO
    private var fireBase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioDao = UserConnectionDB.getDatabase(this).usuarioDao()
        binding.editTextFechaNacimiento.setOnClickListener {
            showDatePicker(binding.editTextFechaNacimiento)
        }
        binding.buttonRegistrarse.setOnClickListener {
            // Obtener los datos del usuario para el registro
            val nombre = binding.editTextNombre.text.toString()
            val apellido = binding.editTextApellido.text.toString()
            val fechaNacimiento = binding.editTextFechaNacimiento.text.toString()
            val usuario = binding.editTextUsuario.text.toString()
            val email = binding.editTextEmail.text.toString()
            val contrasena = binding.editTextContrasena.text.toString()
            val numeroTelefono = binding.editTextNumeroTelefono.text.toString()

            // Crear una instancia de UsuariosDB con los datos recopilados
            val usuarioObj = UsuariosDB(
                id = 0, // El id se generará automáticamente en la base de datos
                nombre = nombre,
                apellido = apellido,
                fechaNacimiento = fechaNacimiento,
                usuario = usuario,
                email = email,
                contrasena = contrasena,
                numeroTelefono = numeroTelefono
            )

            lifecycleScope.launch(Dispatchers.IO) {
                insertarUsuarios(usuarioObj)
                //usuarioDao.insertUser(usuarioObj)
                showSnackbar("Registro exitoso")
                clearFields()
                val ingresar = Intent(this@RegistroUsuario, IniciarSesion::class.java)
                startActivity(ingresar)
            }
        }
    }

    private fun insertarUsuarios(user: UsuariosDB) {
        // Create a new user with a first, middle, and last name
        val TAG = "Memoraid"
        // Add a new document with a generated ID
        fireBase.collection("usuarios")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        binding.editTextNombre.text.clear()
        binding.editTextApellido.text.clear()
        binding.editTextFechaNacimiento.text.clear()
        binding.editTextUsuario.text.clear()
        binding.editTextEmail.text.clear()
        binding.editTextContrasena.text.clear()
        binding.editTextNumeroTelefono.text.clear()
    }

    private fun showDatePicker(editText: EditText) {
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                editText.setText(dateFormatter.format(selectedDate.time))
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}