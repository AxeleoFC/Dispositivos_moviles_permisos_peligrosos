package com.example.memoraid.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.memoraid.data.connections.UserConnectionDB
import com.example.memoraid.data.dao.UsuariosDAO
import com.example.memoraid.data.entities.Usuario
import com.example.memoraid.databinding.ActivityIniciarSesionBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IniciarSesion : AppCompatActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding
    private lateinit var usuarioDao: UsuariosDAO
    private val fireBase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioDao = UserConnectionDB.getDatabase(this).usuarioDao()
    }

    override fun onStart() {
        super.onStart()
        binding.buttonIngresar.setOnClickListener {
            val usuario = binding.usuarioID.text.toString()
            val contrasena = binding.contraseniaID.text.toString()
            getUsuario(usuario, contrasena) { usuarioEncontrado ->
                if (usuarioEncontrado) {
                    sendDatoUsuario(
                        Usuario(0
                        ,""
                        ,""
                        ,""
                        ,usuario
                        ,""
                        ,""
                        ,"")
                    )
                } else {
                    showSnackbar("Usuario o contraseña incorrecta.")
                }
            }
        }
        binding.buttonRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistroUsuario::class.java))
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun getUsuario(usuario: String, contrasena: String, callback: (Boolean) -> Unit) {
        val usersCollection = fireBase.collection("usuarios")
        usersCollection
            .whereEqualTo("usuario", usuario)
            .whereEqualTo("contrasena", contrasena)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val usuarioEncontrado = !querySnapshot.isEmpty
                callback(usuarioEncontrado)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error obteniendo documentos.", exception)
                callback(false)
            }
    }

    fun sendDatoUsuario(item: Usuario): Unit {
        val i = Intent(this, MenuPrincipal::class.java)
        i.putExtra("usuario", item)
        startActivity(i)

    }
}
