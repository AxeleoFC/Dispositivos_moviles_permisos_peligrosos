package com.example.memoraid.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.memoraid.R
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.data.entities.Usuario
import com.example.memoraid.data.entities.database.UsuariosDB
import com.example.memoraid.databinding.ActivityDatosUsuarioBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatosUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatosUsuarioBinding
    private val fireBase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDatosUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val user=Usuario(1
            ,binding.usuarioNombre.toString()
            ,binding.usuarioApellido.toString()
            ,binding.usuarFechaN.toString()
            ,binding.usuarioNick.toString()
            ,binding.etiquetaEmail.toString()
            ,"**"
            ,binding.usuarioTelefono.toString())
        binding.buttonCambiar.setOnClickListener {
            sendDatoUsuario(user)
        }
    }

    private fun getUsuario(callback: (UsuariosDB) -> Unit) {
        val TAG = "Memoraid"
        val eventosList = ArrayList<Evento>()
        fireBase.collection("usuario")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data
                    val user=UsuariosDB(
                        1,
                        data["nombre"] as String,
                        data["apellido"] as String,
                        data["fechaNacimiento"]as String,
                        data["usuario"] as String,
                        data["email"] as String,
                        data["contrasena"] as String,
                        data["numeroTelefono"]as String
                    )
                    Log.d(TAG, "${document.id} => ${document.data}")
                    callback(user)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
    fun sendDatoUsuario(item: Usuario):Unit {
        val i = Intent(this, ModificarDatosUsuarioActivity::class.java)
        i.putExtra("usuario",item)
        startActivity(i)

    }
}