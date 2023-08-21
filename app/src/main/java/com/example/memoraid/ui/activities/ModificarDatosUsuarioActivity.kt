package com.example.memoraid.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.memoraid.R
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.data.entities.Usuario
import com.example.memoraid.databinding.ActivityModificarDatosUsuarioBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModificarDatosUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModificarDatosUsuarioBinding
    private val fireBase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityModificarDatosUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCambiar.setOnClickListener {
            val nuevoNombre = binding.usuarioNombre.text.toString()
            val nuevoApellido = binding.usuarioApellido.text.toString()
            val nuevaFechaNacimiento = binding.usuarFechaN.text.toString()
            val nuevoUsuario = binding.usuarioNick.text.toString()

            val item = intent.getParcelableExtra<Usuario>("usuario")
            if (item != null) {
                updateUsuario(item.usuario, nuevoNombre, nuevoApellido, nuevaFechaNacimiento, nuevoUsuario)
            }
        }
    }

    private fun updateUsuario(usuario: String, nuevoNombre: String, nuevoApellido: String, nuevaFechaNacimiento: String, nuevoUsuario: String) {
        val usersCollection = fireBase.collection("usuarios")
        usersCollection
            .whereEqualTo("usuario", usuario)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val document = result.documents[0]
                    val data = document.data
                    data?.let {
                        it["nombre"] = nuevoNombre
                        it["apellido"] = nuevoApellido
                        it["fechaNacimiento"] = nuevaFechaNacimiento
                        it["usuario"] = nuevoUsuario
                        document.reference.update(it)
                            .addOnSuccessListener {
                                // Actualización exitosa
                                // Puedes realizar alguna acción aquí si lo deseas
                            }
                            .addOnFailureListener { exception ->
                                // Manejo de error en caso de fallo en la actualización
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Manejo de error en caso de fallo en la obtención de datos
            }
    }
}